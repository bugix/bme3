package medizin.client.activites;

import java.util.List;

import medizin.client.events.RecordChangeEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceNotActivatedQuestion;
import medizin.client.place.PlaceNotActivatedQuestionDetails;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.view.question.QuestionView;
import medizin.client.ui.view.question.QuestionView.Delegate;
import medizin.client.ui.view.question.QuestionViewImpl;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class ActivityNotActivatedQuestion extends AbstractActivityWrapper implements Delegate {

	//private final PlaceNotActivatedQuestion placeNotActivatedQuestion;
	private final PlaceController placeController;
	private final McAppRequestFactory requests;
	private final ActivityNotActivatedQuestionMapper activityNotActivatedQuestionMapper;
	private final ActivityManager activityManger;
	private AcceptsOneWidget widget;
	private QuestionView view;
	private SingleSelectionModel<QuestionProxy> selectionModel;
	private CellTable<QuestionProxy> table;
	private HandlerRegistration rangeChangeHandler;

	public ActivityNotActivatedQuestion(PlaceNotActivatedQuestion place, McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		//this.placeNotActivatedQuestion = place;
		this.requests = requests;
		this.placeController = placeController;
		this.activityNotActivatedQuestionMapper = new ActivityNotActivatedQuestionMapper(requests, placeController);
		this.activityManger = new ActivityManager(activityNotActivatedQuestionMapper, requests.getEventBus());
	}

	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		Log.debug("start()");
		QuestionView questionView = new QuestionViewImpl(eventBus, false);
		Log.debug("start()");
		//questionView.setPresenter(this);
		questionView.setDelegate(this);
		this.widget = widget;
		Log.debug("start()");
		this.view = questionView;
		Log.debug("start()");
		widget.setWidget(questionView.asWidget());
		questionView.removeAdvancedSearchFromView();

		table = view.getTable();

		Log.debug("start2()");
		activityManger.setDisplay(view.getDetailsPanel());

		RecordChangeEvent.register(requests.getEventBus(), (QuestionViewImpl)view);
		init();

		// Inherit the view's key provider
		ProvidesKey<QuestionProxy> keyProvider = ((AbstractHasData<QuestionProxy>) table).getKeyProvider();
		selectionModel = new SingleSelectionModel<QuestionProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				QuestionProxy selectedObject = selectionModel.getSelectedObject();
				if (selectedObject != null) {
					Log.debug(selectedObject.getQuestionText() + " selected!");
					showDetails(selectedObject);
				}
			}
		});

	}

	private void showDetails(QuestionProxy question) {
		Log.debug("Question Stable id: " + question.stableId() + " " + PlaceQuestionDetails.Operation.DETAILS);
		placeController.goTo(new PlaceNotActivatedQuestionDetails(question.stableId()));
	}

	private void init() {
		if (rangeChangeHandler != null) {
			rangeChangeHandler.removeHandler();
			rangeChangeHandler = null;
		}

		performSearch("");

		rangeChangeHandler = table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				ActivityNotActivatedQuestion.this.onRangeChanged();
			}
		});
		/*requests.institutionRequest().findAllInstitutions().fire(new BMEReceiver<List<InstitutionProxy>>() {

			@Override
			public void onSuccess(List<InstitutionProxy> response) {
				view.setInstitutionFilter(response);
			}
		});*/

		requests.questionEventRequest().findQuestionEventByInstitution(this.institutionActive).fire(new BMEReceiver<List<QuestionEventProxy>>() {

			@Override
			public void onSuccess(List<QuestionEventProxy> response) {
				view.setSpecialisationFilter(response);
			}
		});
	}

	private void onRangeChanged() {
		final Range range = table.getVisibleRange();

		requests.questionRequest().findAllNotActivatedQuestionsByPerson(view.getSerachBox().getValue(), view.getSearchValue(), range.getStart(), range.getLength()).with(view.getPaths()).fire(new BMEReceiver<List<QuestionProxy>>() {
			@Override
			public void onSuccess(List<QuestionProxy> values) {
				if (view == null) {
					// This activity is dead
					Log.debug("view ist null");
					return;
				}

				table.setRowData(range.getStart(), values);

				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}
		});
	}

	@Override
	public String mayStop() {
		return null;
	}

	@Override
	public void onCancel() {
		onStop();
	}

	@Override
	public void onStop() {
		activityManger.setDisplay(null);
	}

	@Override
	public void placeChanged(Place place) {

		if (place instanceof PlaceNotActivatedQuestion) {
			init();
		}
	}

	@Override
	public void performSearch(String searchText) {
		
		requests.questionRequest().countNotActivatedQuestionsByPerson(view.getSerachBox().getValue(), view.getSearchValue()).fire(new BMEReceiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Questions (Pr�fungen) aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);
				onRangeChanged();
			}

		});
	}

	@Override
	public void newClicked() {}
}
