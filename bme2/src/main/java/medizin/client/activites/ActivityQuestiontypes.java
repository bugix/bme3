package medizin.client.activites;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import medizin.client.events.RecordChangeEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceQuestiontypes;
import medizin.client.place.PlaceQuestiontypesDetails;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.style.resources.AdvanceCellTable;
import medizin.client.ui.view.QuestiontypesView;
import medizin.client.ui.view.QuestiontypesViewImpl;
import medizin.client.ui.widget.Sorting;
import medizin.client.ui.widget.process.AppLoader;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class ActivityQuestiontypes extends AbstractActivityWrapper implements QuestiontypesView.Presenter, QuestiontypesView.Delegate {

	private PlaceQuestiontypes questiontypesPlace;
	private AcceptsOneWidget widget;
	private AdvanceCellTable<QuestionTypeProxy> table;
	private ActivityManager activityManger;
	private ActivityQuestiontypesMapper activityQuestiontypesMapper;
	private QuestiontypesView view;
	private SingleSelectionModel<QuestionTypeProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	/*private HandlerRegistration placeChangeHandlerRegistration;*/

	private McAppRequestFactory requests;
	private PlaceController placeController;
	 
	public String sortname = "shortName";
	public Sorting sortorder = Sorting.ASC;
	String sortName[];
	public String columnHeader;
	String searchValue = "";
	private Map<String, String> columnName;
	public List<String> path = new ArrayList<String>();
	public int x;
	public int y;
	private QuestionTypeProxy questionType;
	private EntityProxyId<?> proxyId = null;
	
	@Inject	
	public ActivityQuestiontypes(PlaceQuestiontypes place, McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.questiontypesPlace = place;
        this.requests = requests;
        this.placeController = placeController;
		this.activityQuestiontypesMapper = new ActivityQuestiontypesMapper(requests, placeController);
		this.activityManger = new ActivityManager(activityQuestiontypesMapper, requests.getEventBus());
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
//		((SlidingPanel)widget).remove(view.asWidget());
		activityManger.setDisplay(null);
		if(view != null) {
			view.setDelegate(null);
			view = null;	
		}
		
		
		if (rangeChangeHandler != null)
		rangeChangeHandler.removeHandler();
		
		//rangeChangeHandler = null;
		
		/*if (placeChangeHandlerRegistration != null) {
			placeChangeHandlerRegistration.removeHandler();
		}*/
	}
	
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		Log.info("start2");
		QuestiontypesView questiontypesView = new QuestiontypesViewImpl();
		questiontypesView.setPresenter(this);
		this.widget = widget;
		this.view = questiontypesView;
        widget.setWidget(questiontypesView.asWidget());
        this.table = view.getTable();
        sortName=view.getPaths();
		
		//adding mouse out of table.
		((QuestiontypesViewImpl)view).addColumnOnMouseout();
	
		
		activityManger.setDisplay(view.getDetailsPanel());
		
		/**
		 * Wiring selectionModel with CellTable
		 */
		// Inherit the view's key provider
		ProvidesKey<QuestionTypeProxy> keyProvider = ((AbstractHasData<QuestionTypeProxy>) table).getKeyProvider();
		selectionModel = new SingleSelectionModel<QuestionTypeProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			public void onSelectionChange(SelectionChangeEvent event) {
				QuestionTypeProxy selectedObject = selectionModel.getSelectedObject();
				if (selectedObject != null) {
					//Log.debug(selectedObject.getQuestionTypeName() + " selected!");
					questionType = selectedObject;
					showDetails(selectedObject);
				}
			}
		});
		
		RecordChangeEvent.register(requests.getEventBus(), (QuestiontypesViewImpl)view);
		
		init();
		view.setDelegate(this);
	}//End start()
	
	@Override
	public void goTo(Place place) {
		  placeController.goTo(place);
	}
	
	private void initTable()
	{
		AppLoader.setNoLoader();
		requests.questionTypeRequest().countAllQuestionType(searchValue).fire(new  BMEReceiver<Long>() {

			@Override
			public void onSuccess(final Long count) {
				Log.info("count total--"+count);
				table.setRowCount(count.intValue());
				onRangeChanged();
			}
		});
	}
	
	private void onRangeChanged() {
		final Range range = table.getVisibleRange();
		AppLoader.setNoLoader();
		requests.questionTypeRequest().findAllQuestionType(range.getStart(),range.getLength(),sortname,sortorder,searchValue).with("institution").fire(new BMEReceiver<List<QuestionTypeProxy>>() {

			@Override
			public void onSuccess(List<QuestionTypeProxy> arg0) {
				Log.info("response of all list--"+arg0.size());
				table.setRowData(range.getStart(), arg0);
				selectRow(range);
			}
		});
	}
	
	private void init() {
		initTable();
		
		table.addRangeChangeHandler(new Handler() {
			
			@Override
			public void onRangeChange(RangeChangeEvent arg0) {
				ActivityQuestiontypes.this.onRangeChanged();
			}
		});
		
		 table.addColumnSortHandler(new ColumnSortEvent.Handler() {

				@Override
				public void onColumnSort(ColumnSortEvent event) {
					// By SPEC[Start

					Column<QuestionTypeProxy, String> col = (Column<QuestionTypeProxy, String>) event.getColumn();
					
					
					int index = table.getColumnIndex(col);
					if (index == (table.getColumnCount() - 1)) {
						
						table.getPopup().setPopupPosition(x, y);
						table.getPopup().show();
		
					}else{ 
					
						if(table.getRowCount() > 0 ) {
							sortname = ((QuestiontypesViewImpl)view).getCurrentRows().get(index);;
							if(sortname !="answer"){
								sortorder = (event.isSortAscending()) ? Sorting.ASC: Sorting.DESC;
								// By SPEC]end
								// RoleActivity.this.init2("");
								Log.info("Call Search from addColumnSortHandler sortname "+ sortname +" sortOrder "+sortorder +"index "+index);
								// filter.hide();
								initTable();
							}else{
								sortname="shortName";
							}
						}
					}	
					
				}
			});
	}//End init()
		
	private void selectRow(final Range range) {
		if (proxyId != null)
		{
			requests.find(proxyId).fire(new BMEReceiver<Object>() {

				@Override
				public void onSuccess(final Object response) {
					if (response != null && response instanceof QuestionTypeProxy)
					{
						AppLoader.setNoLoader();
						final QuestionTypeProxy selectedProxy = (QuestionTypeProxy) response;
						requests.questionTypeRequest().findRangeStartForQuestionType(selectedProxy.getId(),range.getLength(), sortname, sortorder, searchValue).fire(new BMEReceiver<Integer>() {

							@Override
							public void onSuccess(Integer start) {
								selectionModel.setSelected(selectedProxy, true);
								table.setPageStart(start < 0 ? 0 : start);
							}
						});							
					}
					proxyId = null;
				}
			});
		}
	}

	private void showDetails(QuestionTypeProxy questiontype) {
		Log.debug("Questiontype Stable id: " + questiontype.stableId() + " " + PlaceQuestiontypesDetails.Operation.DETAILS);
		placeController.goTo(new PlaceQuestiontypesDetails(questiontype.stableId(), view.getScrollDetailPanel().getOffsetHeight()));
	}
		
	@Override
	public void newClicked() {
		Log.debug("New Clicked in Acitivity Qeustiontype");
		placeController.goTo(new PlaceQuestiontypesDetails(PlaceQuestiontypesDetails.Operation.CREATE,view.getScrollDetailPanel().getOffsetHeight()));
	}

	@Override
	public void performSearch(final String searchValue) {
		this.searchValue = searchValue;
		initTable();
	}

	@Override
	public void placeChanged(Place place) {
		if (place instanceof PlaceQuestiontypes)
		{
			proxyId = ((PlaceQuestiontypes)place).getProxyId();
			init();
		}
    }

	@Override
	public void setXandYOfTablePopyp(int x, int y) {
		this.x=x;
		this.y=y;
		
	}

}
