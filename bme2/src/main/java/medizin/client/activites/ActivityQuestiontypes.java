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
		
	@Inject	
	public ActivityQuestiontypes(PlaceQuestiontypes place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.questiontypesPlace = place;
        this.requests = requests;
        this.placeController = placeController;
		this.activityQuestiontypesMapper = new ActivityQuestiontypesMapper(requests, placeController);
		this.activityManger = new ActivityManager(activityQuestiontypesMapper,
				requests.getEventBus());
	}

	/**
	 * HashMaps where QuestionType-Objects and QuestionType-Ids are stored by the request
	 */
//	private final Map<EntityProxyId<QuestionTypeProxy>, Integer> idToRow = new HashMap<EntityProxyId<QuestionTypeProxy>, Integer>();
//	
//	private final Map<EntityProxyId<QuestionTypeProxy>, QuestionTypeProxy> idToProxy = new HashMap<EntityProxyId<QuestionTypeProxy>, QuestionTypeProxy>();
//	private Boolean pendingSelection;
	
	
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
	private QuestionTypeProxy questionType;

	private EntityProxyId<?> proxyId = null;
		
//	public void  setTable(CellTable<QuestionTypeProxy> table){
//		this.table = table;
//	}
	
	/*@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);
		Log.info("start");

	}*/
	
	
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		Log.info("start2");
		QuestiontypesView questiontypesView = new QuestiontypesViewImpl();
		questiontypesView.setPresenter(this);
		this.widget = widget;
		this.view = questiontypesView;
        widget.setWidget(questiontypesView.asWidget());
        this.table = view.getTable();
        sortName=view.getPaths();
//        setTable(view.getTable());
        
		/*eventBus.addHandler(PlaceChangeEvent.TYPE,
				new PlaceChangeEvent.Handler() {
					public void onPlaceChange(PlaceChangeEvent event) {

						Place place = event.getNewPlace();
						if(place instanceof PlaceQuestiontypesDetails){
							init();
						}
						

						// updateSelection(event.getNewPlace());
						// TODO implement
					}
				});*/
		
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
        
		/*placeChangeHandlerRegistration = eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			@Override
			public void onPlaceChange(PlaceChangeEvent event) {
				if (event.getNewPlace() instanceof PlaceQuestiontypesDetails) {
					
					
					
					PlaceQuestiontypesDetails place = (PlaceQuestiontypesDetails) event.getNewPlace();
					if (place.getOperation() == Operation.DETAILS) {
						init();
						//requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					}
				}				
			}
		});*/
		
	}//End start()
	
	@Override
	public void goTo(Place place) {
		  placeController.goTo(place);
	}
	
	/**
	 * Range-Requests for fetching QueryType-Objects and Counting all QueryType-Objects in Database
	 */
	
//	 protected Request<java.util.List<medizin.client.managed.request.QuestionTypeProxy>> createRangeRequest(Range range) {
//	        return requests.questionTypeRequest().findQuestionTypeEntries(range.getStart(), range.getLength());
//	    }

//	    protected void fireCountRequest(Receiver<Long> callback) {
//	    	requests.questionTypeRequest().countQuestionTypes().fire(callback);
//	    }
	
	private void tableRangeChangeCall()
	{
		final Range range =table.getVisibleRange();
		
		requests.questionTypeRequest().countAllQuestionType(searchValue).fire(new  BMEReceiver<Long>() {

			@Override
			public void onSuccess(final Long count) {
				// TODO Auto-generated method stub
				Log.info("count total--"+count);
				table.setRowCount(count.intValue());
				
			//	System.out.println("Start: " + range.getStart() + " Length: " + range.getLength());
				requests.questionTypeRequest().findAllQuestionType(range.getStart(),range.getLength(),sortname,sortorder,searchValue).with("institution").fire(new BMEReceiver<List<QuestionTypeProxy>>() {

					@Override
					public void onSuccess(List<QuestionTypeProxy> arg0) {
						// TODO Auto-generated method stub
						Log.info("response of all list--"+arg0.size());
						table.setRowData(range.getStart(), arg0);
						selectRow(range, count);
					}
				});
			}
		});
	}
	
	private void init() {
			
			
			final Range range =table.getVisibleRange();
			requests.questionTypeRequest().countAllQuestionType(searchValue).fire(new  BMEReceiver<Long>() {

				@Override
				public void onSuccess(final Long response) {
					// TODO Auto-generated method stub
					Log.info("count total--"+response);
					table.setRowCount(response.intValue());
					
				//	System.out.println("Start: " + range.getStart() + " Length: " + range.getLength());
					requests.questionTypeRequest().findAllQuestionType(range.getStart(),range.getLength(),sortname,sortorder,searchValue).with("institution").fire(new BMEReceiver<List<QuestionTypeProxy>>() {

						@Override
						public void onSuccess(List<QuestionTypeProxy> arg0) {
							// TODO Auto-generated method stub
							Log.info("response of all list--"+arg0.size());
							table.setRowData(range.getStart(), arg0);
							selectRow(range, response);
						}
					});
				}
			});
			
			table.addRangeChangeHandler(new Handler() {
				
				@Override
				public void onRangeChange(RangeChangeEvent arg0) {
					// TODO Auto-generated method stub
					tableRangeChangeCall();
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
									tableRangeChangeCall();
								}else{
									sortname="shortName";
								}
							}
						}	
						
					}
				});
			
			
			
			/*requests.questionTypeRequest().findAllQuestionTypes().fire(new Receiver<List<QuestionTypeProxy>>() {

				@Override
				public void onSuccess(List<QuestionTypeProxy> response) {
					table.setRowData(response);
				}
			});*/
			/*requests.questionTypeRequest().countQuestionTypes().fire(new Receiver<Long>() {
				@Override
				public void onSuccess(Long response) {
					if (view == null) {
						// This activity is dead
						return;
					}
					Log.debug("Geholte QuestionTypes (Fragetypen) aus der Datenbank: " + response);
					view.getTable().setRowCount(response.intValue(), true);
					onRangeChanged();
				}
			});
			
			
			if(rangeChangeHandler!=null){
				rangeChangeHandler.removeHandler();
				rangeChangeHandler = null;
			}
			
			rangeChangeHandler = table
			.addRangeChangeHandler(new RangeChangeEvent.Handler() {
				public void onRangeChange(RangeChangeEvent event) {
					ActivityQuestiontypes.this.onRangeChanged();
				}
			});*/
			
			
			
		}//End init()
	
		

		
//		public CellTable<QuestionTypeProxy> getTable(){
//			return table;
//		}

		
		/**
		 * @author adrian.alioski
		 * @param table
		 */
		protected void onRangeChanged() {
			final Range range = table.getVisibleRange();

//			final Receiver<List<QuestionTypeProxy>> callback = new Receiver<List<QuestionTypeProxy>>() {
//				@Override
//				public void onSuccess(List<QuestionTypeProxy> values) {
//					if (view == null) {
//						// This activity is dead
//						Log.debug("view ist null");
//						return;
//					}
//					idToRow.clear();
//					idToProxy.clear();
//					for (int i = 0, row = range.getStart(); i < values.size(); i++, row++) {
//						QuestionTypeProxy questionType = values.get(i);
//						Log.debug("FrageTypen mit ID und Namen: " + questionType.getId() + " " + questionType.getQuestionTypeName());
//						@SuppressWarnings("unchecked")
//						// Why is this cast needed?
//						EntityProxyId<QuestionTypeProxy> proxyId = (EntityProxyId<QuestionTypeProxy>) questionType.stableId();
//						idToRow.put(proxyId, row);
//						idToProxy.put(proxyId, questionType);
//					}
//					table.setRowData(range.getStart(), values);
//				
//					finishPendingSelection();
//				if (widget != null) {
//			          widget.setWidget(view.asWidget());
//					}
//				}
//			};
//
//			fireRangeRequest(range, callback);
			
			requests.questionTypeRequest().findQuestionTypeEntries(range.getStart(), range.getLength()).with(view.getPaths()).fire(new BMEReceiver<List<QuestionTypeProxy>>() {
				@Override
				public void onSuccess(List<QuestionTypeProxy> values) {
					if (view == null) {
						// This activity is dead
						Log.debug("view ist null");
						return;
					}
					
					table.setRowData(range.getStart(), values);
					
					selectRow(range, 0l);
				
				if (widget != null) {
			          widget.setWidget(view.asWidget());
					}
				}
			});
			
		}//End onRangeChanged
				
		private void selectRow(final Range range, final Long count) {
			if (proxyId != null)
			{
				requests.find(proxyId).fire(new BMEReceiver<Object>() {

					@Override
					public void onSuccess(final Object response) {
						if (response != null && response instanceof QuestionTypeProxy)
						{
							requests.questionTypeRequest().findAllQuestionType(range.getStart(), count.intValue(), sortname, sortorder, searchValue).fire(new BMEReceiver<List<QuestionTypeProxy>>() {

								@Override
								public void onSuccess(List<QuestionTypeProxy> questionTypeList) {
									QuestionTypeProxy selectedProxy = (QuestionTypeProxy) response;
									selectionModel.setSelected(selectedProxy, true);							
									int index = questionTypeList.indexOf(selectedProxy);							
									int start = ((index / range.getLength()) * range.getLength());
									table.setPageStart((start < 0 ? 0 : start));
								}
							});							
						}
						proxyId = null;
					}
				});
			}
		}

//		private void fireRangeRequest(final Range range,
//	            final Receiver<List<QuestionTypeProxy>> callback) {
//				createRangeRequest(range).with(view.getPaths()).fire(callback);
//				// TODO what makes getPaths?
//			//	Log.debug(((String[])view.getPaths().toArray()).toString());
//				}

		
		protected void showDetails(QuestionTypeProxy questiontype) {
			Log.debug("Questiontype Stable id: " + questiontype.stableId() + " "
					+ PlaceQuestiontypesDetails.Operation.DETAILS);
			placeController.goTo(
					new PlaceQuestiontypesDetails(questiontype.stableId()));
		}
		
		


		@Override
		public void newClicked() {

					Log.debug("New Clicked in Acitivity Qeustiontype");
					placeController.goTo(new PlaceQuestiontypesDetails(PlaceQuestiontypesDetails.Operation.CREATE));
				
		}

		@Override
		public void performSearch(final String searchValue) {
			final Range range = table.getVisibleRange();
			/*requests.questionTypeRequest().findAllQuestionType(range.getStart(), range.getLength(), sortname, sortorder, searchValue).with(view.getPaths()).fire(new Receiver<List<QuestionTypeProxy>>() {

				@Override
				public void onSuccess(List<QuestionTypeProxy> response) {

					table.setRowData(range.getStart(), response);
				}
			});*/
			this.searchValue = searchValue;
			tableRangeChangeCall();
			/*requests.questionTypeRequest().countAllQuestionType(searchValue).fire(new  Receiver<Long>() {

				@Override
				public void onSuccess(Long response) {
					// TODO Auto-generated method stub
					Log.info("count total--"+response);
					table.setRowCount(response.intValue());
					
				//	System.out.println("Start: " + range.getStart() + " Length: " + range.getLength());
					requests.questionTypeRequest().findAllQuestionType(range.getStart(),range.getLength(),sortname,sortorder,searchValue).fire(new Receiver<List<QuestionTypeProxy>>() {

						@Override
						public void onSuccess(List<QuestionTypeProxy> arg0) {
							// TODO Auto-generated method stub
							Log.info("response of all list--"+arg0.size());
							table.setRowData(range.getStart(), arg0);
						}
					});
				}
			});*/
			
		}

		@Override
		public void placeChanged(Place place) {
			if (place instanceof PlaceQuestiontypesDetails) {
				if (((PlaceQuestiontypesDetails)place).getProxyId() != null)
				{
					proxyId = ((PlaceQuestiontypesDetails)place).getProxyId();
					//init();
				}
			}	
			
			if (place instanceof PlaceQuestiontypes)
			{
				init();
			}
	    }

		@Override
		public void setXandYOfTablePopyp(int x, int y) {
			this.x=x;
			this.y=y;
			
		}
	
		






//		@Override
//		public void deleteClicked() {
//			requests.questionTypeRequest().remove().using(questionType).fire(new Receiver<Void>() {
//
//	            public void onSuccess(Void ignore) {
//	            	Log.debug("Sucessfull deleted");
//	            	init();
//	            	
//	            }
//	            @Override
//				public void onFailure(ServerFailure error) {
//						Log.warn(McAppConstant.ERROR_WHILE_DELETE + " in Institution -" + error.getMessage());
//						if(error.getMessage().contains("ConstraintViolationException")){
//							Log.debug("Fehlen beim erstellen: Doppelter name");
//							mcAppFactory.getErrorPanel().setErrorMessage(McAppConstant.INSTITUTION_IS_REFERENCED);
//						}
//					
//				}
//				@Override
//				public void onViolation(Set<Violation> errors) {
//					Iterator<Violation> iter = errors.iterator();
//					String message = "";
//					while(iter.hasNext()){
//						message += iter.next().getMessage() + "<br>";
//					}
//					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Institution -" + message);
//					
//					mcAppFactory.getErrorPanel().setErrorMessage(message);
//
//					
//				}
//	            
//	        });
//			
//		}


}
