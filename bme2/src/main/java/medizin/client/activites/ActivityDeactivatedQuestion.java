package medizin.client.activites;

import java.util.List;

import medizin.client.events.RecordChangeEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceDeactivatedQuestion;
import medizin.client.place.PlaceDeactivatedQuestionDetails;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.view.question.QuestionView;
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

public class ActivityDeactivatedQuestion extends AbstractActivityWrapper implements QuestionView.Delegate{

	private final McAppRequestFactory requests;
	private final PlaceController placeController;
	private AcceptsOneWidget widget;
	private QuestionView view;
	private SingleSelectionModel<QuestionProxy> selectionModel;
	private CellTable<QuestionProxy> table;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManger;
	private ActivityDeactivatedQuestionMapper activitydeactivatedQuestionMapper;

	public ActivityDeactivatedQuestion(PlaceDeactivatedQuestion place, McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.placeController = placeController;
		this.requests = requests;
		this.activitydeactivatedQuestionMapper = new ActivityDeactivatedQuestionMapper(requests, placeController);
		this.activityManger = new ActivityManager(activitydeactivatedQuestionMapper, requests.getEventBus());		
	}

	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void newClicked() {
	}

	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		QuestionView questionView = new QuestionViewImpl(eventBus, false);
		questionView.setDelegate(this);
		this.widget = widget;
		this.view = questionView;
		widget.setWidget(questionView.asWidget());
		questionView.removeAdvancedSearchFromView();
		
		questionView.getFilterPanel().getShowNew().removeFromParent();

		table = view.getTable();
		activityManger.setDisplay(view.getDetailsPanel());

		RecordChangeEvent.register(requests.getEventBus(), (QuestionViewImpl)view);
		init();

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
	
	private void showDetails(QuestionProxy selectedObject) {
		placeController.goTo(new PlaceDeactivatedQuestionDetails(selectedObject.stableId()));
	}

	private void init() {
		if (rangeChangeHandler != null) {
			rangeChangeHandler.removeHandler();
			rangeChangeHandler = null;
		}

		performSearch("");

		rangeChangeHandler = table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				ActivityDeactivatedQuestion.this.onRangeChanged();
			}
		});
		
		requests.questionEventRequest().findQuestionEventByInstitution(this.institutionActive).fire(new BMEReceiver<List<QuestionEventProxy>>() {

			@Override
			public void onSuccess(List<QuestionEventProxy> response) {
				view.setSpecialisationFilter(response);
			}
		});
	}
	
	private void onRangeChanged() {
		final Range range = table.getVisibleRange();

		requests.questionRequest().findDeactivatedQuestion(view.getSerachBox().getValue(), view.getSearchValue(), range.getStart(), range.getLength()).with(view.getPaths()).fire(new BMEReceiver<List<QuestionProxy>>() {
			@Override
			public void onSuccess(List<QuestionProxy> values) {
				if (view == null) {
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
		
	}

	@Override
	public void performSearch(String searchText) {
		
		requests.questionRequest().countDeactivatedQuestion(view.getSerachBox().getValue(), view.getSearchValue()).fire(new BMEReceiver<Integer>() {
			@Override
			public void onSuccess(Integer response) {
				if (view == null) {
					return;
				}
				view.getTable().setRowCount(response.intValue(), true);
				onRangeChanged();
			}
		});
	}


}