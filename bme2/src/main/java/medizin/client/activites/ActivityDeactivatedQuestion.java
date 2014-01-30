package medizin.client.activites;

import java.util.List;

import medizin.client.events.RecordChangeEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceDeactivatedQuestion;
import medizin.client.place.PlaceDeactivatedQuestionDetails;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.question.QuestionView;
import medizin.client.ui.view.question.QuestionViewImpl;
import medizin.client.ui.widget.Sorting;
import medizin.client.ui.widget.process.AppLoader;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Cookies;
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
	private Sorting sortorder = Sorting.ASC;
	private String sortname = "id";
	
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
		QuestionView questionView = new QuestionViewImpl(eventBus, false, false);
		questionView.setDelegate(this);
		this.widget = widget;
		this.view = questionView;
		widget.setWidget(questionView.asWidget());
		questionView.removeAdvancedSearchFromView();
		
		questionView.getFilterPanel().getShowNew().removeFromParent();
		questionView.getFilterPanel().getCreativeWork().removeFromParent();

		table = view.getTable();
		activityManger.setDisplay(view.getDetailsPanel());

		//Setting splitlayout panel width from Cookie
		setWidthOfWidget();
		
		RecordChangeEvent.register(requests.getEventBus(), (QuestionViewImpl)view);
		
		//adding column mouse out on table.
		((QuestionViewImpl)view).addColumnOnMouseout();
		
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
	
	private void setWidthOfWidget() {
		String widgetWidthFromCookie = Cookies.getCookie(McAppConstant.DEACTIVATED_QUE_VIEW_WIDTH);
        if(widgetWidthFromCookie !=null){
        	((QuestionViewImpl)view).getSplitLayoutPanel().setWidgetSize(((QuestionViewImpl)view).getScrollpanel(),Double.valueOf(widgetWidthFromCookie));	
        }
		
	}
	private void showDetails(QuestionProxy selectedObject) {
		placeController.goTo(new PlaceDeactivatedQuestionDetails(selectedObject.stableId(),view.getScrollDetailPanel().getOffsetHeight()));
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
		
		/*requests.questionEventRequest().findQuestionEventByInstitution(this.institutionActive).fire(new BMEReceiver<List<QuestionEventProxy>>() {

			@Override
			public void onSuccess(List<QuestionEventProxy> response) {
				view.setSpecialisationFilter(response);
			}
		});*/
	}
	
	private void onRangeChanged() {
		final Range range = table.getVisibleRange();
		AppLoader.setNoLoader();
		requests.questionRequest().findDeactivatedQuestion(sortname,sortorder, view.getSerachBox().getValue(), view.getSearchValue(), range.getStart(), range.getLength()).with(view.getPaths()).with("autor").fire(new BMEReceiver<List<QuestionProxy>>() {
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
		
		AppLoader.setNoLoader();
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

	@Override
	public void splitLayoutPanelResized() {
		Double newWidth =(((QuestionViewImpl)view).getSplitLayoutPanel()).getWidgetSize(((QuestionViewImpl)view).getScrollpanel());
       	Cookies.setCookie(McAppConstant.DEACTIVATED_QUE_VIEW_WIDTH, String.valueOf(newWidth));
		
	}

	@Override
	public void columnClickedForSorting(String sortname, Sorting sortorder) {
		this.sortname=sortname;
		this.sortorder=sortorder;
		performSearch("");
		
	}

	@Override
	public void printPdfClicked(int left, int top) {		
	}


}
