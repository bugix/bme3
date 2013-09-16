package medizin.client.activites;

import java.util.List;

import medizin.client.events.RecordChangeEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceInstitution;
import medizin.client.place.PlaceInstitutionEvent;
import medizin.client.proxy.InstitutionProxy;
import medizin.client.request.InstitutionRequest;
import medizin.client.ui.view.InstitutionView;
import medizin.client.ui.view.InstitutionViewImpl;
import medizin.client.ui.widget.Sorting;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;

public class ActivityInstitution extends AbstractActivityWrapper implements
		InstitutionView.Presenter, InstitutionView.Delegate {

	private PlaceInstitution institutionPlace;

	private AcceptsOneWidget widget;
	private InstitutionView view;

	private SingleSelectionModel<InstitutionProxy> selectionModel;

	private HandlerRegistration rangeChangeHandler;

	//private final Map<EntityProxyId<InstitutionProxy>, Integer> idToRow = new HashMap<EntityProxyId<InstitutionProxy>, Integer>();
	//private final Map<EntityProxyId<InstitutionProxy>, InstitutionProxy> idToProxy = new HashMap<EntityProxyId<InstitutionProxy>, InstitutionProxy>();
	private Boolean pendingSelection;
	private ActivityManager activityManger;
	private ActivityInstitutionMapper activityInstitutionMapper;


	private McAppRequestFactory requests;
	private PlaceController placeController;
	
	public String sortname = "institutionName";
	 public Sorting sortorder = Sorting.ASC;
	 
	 private InstitutionProxy selectedInstitution = null;
	
	 String searchValue = "";
	 //Long loggedPersonId = 0l;

	@Inject
	public ActivityInstitution(PlaceInstitution place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.institutionPlace = place;
        this.requests = requests;
        this.placeController = placeController;
		this.activityInstitutionMapper = new ActivityInstitutionMapper(
				requests, placeController);
		this.activityManger = new ActivityManager(activityInstitutionMapper,
				requests.getEventBus());
	}

	@Override
	public String mayStop() {

		// activityManger.setDisplay(null);
		return null;
	}

	@Override
	public void onCancel() {
		onStop();

	}

	@Override
	public void onStop() {
		// ((SlidingPanel)widget).remove(view.asWidget());
		Log.info("ActivityInstitution stops...");
		activityManger.setDisplay(null);

		Log.info("ActivityManger auf null gestezt");

	}

	
	/*@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}*/
	
	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		Log.info("Activity Institution start");
		/*if (!userLoggedIn.getIsAdmin())
			loggedPersonId = userLoggedIn.getId();*/
			
		InstitutionView institutionView = new InstitutionViewImpl(reciverMap, userLoggedIn.getIsAdmin());
		institutionView.setName("hallo");
		institutionView.setPresenter(this);
		this.widget = widget;
		this.view = institutionView;
		widget.setWidget(institutionView.asWidget());
		setTable(view.getTable());

	/*	eventBus.addHandler(PlaceChangeEvent.TYPE,
				new PlaceChangeEvent.Handler() {
					public void onPlaceChange(PlaceChangeEvent event) {
						// updateSelection(event.getNewPlace());
						// TODO implement
					}
				});*/
		
		RecordChangeEvent.register(requests.getEventBus(), (InstitutionViewImpl)view);
		

		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		
		

		table.addCellPreviewHandler(new Handler<InstitutionProxy>() {

			@Override
			public void onCellPreview(CellPreviewEvent<InstitutionProxy> event) {
				boolean isClicked="click".equals(event.getNativeEvent().getType());
			    
				if(isClicked){
					if(event.getColumn()==0){
						
						InstitutionProxy selectedObject = selectionModel.getSelectedObject();
						
						if (selectedObject != null) {
							Log.debug(selectedObject.getInstitutionName() + " selected!");
							showDetails(selectedObject);
						}
			    	}
			    }
			}
		});
		
		init();
		
		/*selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						InstitutionProxy selectedObject = selectionModel
								.getSelectedObject();
						if (selectedObject != null) {
							Log.debug(selectedObject.getInstitutionName()
									+ " selected!");
							showDetails(selectedObject);
						}
					}
				});*/

		view.setDelegate(this);
		// updateSelection(mcAppFactory.getPlaceController().getWhere());

	}

	protected void showDetails(InstitutionProxy institution) {
		Log.debug("Institution Stable id: " + institution.stableId() + " "
				+ PlaceInstitutionEvent.Operation.DETAILS);
		placeController.goTo(
				new PlaceInstitutionEvent(institution.stableId(),
						PlaceInstitutionEvent.Operation.DETAILS));
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	protected Request<java.util.List<medizin.client.proxy.InstitutionProxy>> createRangeRequest(Range range) 
	{
		//return requests.institutionRequest().findInstitutionEntries(range.getStart(), range.getLength());		
		//return requests.institutionRequest().findAllInstitutions(range.getStart(),range.getLength(),sortname,sortorder,searchValue);
		return requests.institutionRequest().findAllInstitutionsBySearchValue(searchValue, userLoggedIn.getId(), range.getStart(), range.getLength());				
	}

	protected void fireCountRequest(BMEReceiver<Long> callback) {
		//requests.institutionRequest().countInstitutions().fire(callback);
		
		requests.institutionRequest().countAllInstitutionsBySearchValue(searchValue, userLoggedIn.getId()).fire(callback);
	}

	private void init() {

		
		
		fireCountRequest(new BMEReceiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					return;
				}
				Log.debug("Geholte Intitution aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged();
			}

		});

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						ActivityInstitution.this.onRangeChanged();
					}
				});
	}

	private CellTable<InstitutionProxy> table;

	public CellTable<InstitutionProxy> getTable() {
		return table;
	}

	public void setTable(CellTable<InstitutionProxy> table) {
		this.table = table;
	}

	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();

		final BMEReceiver<List<InstitutionProxy>> callback = new BMEReceiver<List<InstitutionProxy>>() {
			@Override
			public void onSuccess(List<InstitutionProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				/*idToRow.clear();
				idToProxy.clear();
				for (int i = 0, row = range.getStart(); i < values.size(); i++, row++) {
					InstitutionProxy institution = values.get(i);
					@SuppressWarnings("unchecked")
					// Why is this cast needed?
					EntityProxyId<InstitutionProxy> proxyId = (EntityProxyId<InstitutionProxy>) institution
							.stableId();
					idToRow.put(proxyId, row);
					idToProxy.put(proxyId, institution);
				}*/
				
				table.setRowData(range.getStart(), values);
				
				selectRow(range);

				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}
		};

		fireRangeRequest(range, callback);

	}

	private void selectRow(Range range) {
		ProvidesKey<InstitutionProxy> keyProvider = ((AbstractHasData<InstitutionProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<InstitutionProxy>(keyProvider);
		if (selectedInstitution != null)
		{
			selectionModel.setSelected(selectedInstitution, true);
			int start = table.getRowCount() - range.getLength();
			table.setPageStart((start < 0 ? 0 : start));
			selectedInstitution = null;
		}
		table.setSelectionModel(selectionModel);
	}

	private void getLastPage() {
		fireCountRequest(new BMEReceiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				int rows = response.intValue();
				table.setRowCount(rows, true);
				if (rows > 0) {
					int pageSize = table.getVisibleRange().getLength();
					int remnant = rows % pageSize;
					if (remnant == 0) {
						table.setVisibleRange(rows - pageSize, pageSize);
					} else {
						table.setVisibleRange(rows - remnant, pageSize);
					}
				}
				onRangeChanged();
			}
		});
	}

	private void fireRangeRequest(final Range range,
			final BMEReceiver<List<InstitutionProxy>> callback) {
		createRangeRequest(range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}

	/**
	 * Finish selecting a proxy that hadn't yet arrived when
	 * {@link #select(EntityProxyId)} was called.
	 */
	/*private void finishPendingSelection() {
		if (pendingSelection != null) {
			InstitutionProxy selectMe = idToProxy.get(pendingSelection);
			pendingSelection = null;
			if (selectMe != null) {
				// TODO make selection Model
				selectionModel.setSelected(selectMe, true);
			}
		}
	}*/

	@Override
	public void deleteClicked(InstitutionProxy institution) {

		requests.institutionRequest().remove()
				.using(institution).fire(new BMEReceiver<Void>(reciverMap) {

					public void onSuccess(Void ignore) {
						Log.debug("Sucessfull deleted");
						placeController.goTo(new PlaceInstitution("PlaceInstitution"));
						init();
					}
					
					@Override
					public boolean onReceiverFailure() {
						ConfirmationDialogBox.showOkDialogBox(constants.error(), constants.instituteDelError());
						return false;
					}

				});

	}

	@Override
	public void editClicked(InstitutionProxy proxy, String institutionName) {
		
		InstitutionRequest request = requests.institutionRequest();
		proxy = request.edit(proxy);
		proxy.setInstitutionName(institutionName);

		final InstitutionProxy tempProxy = proxy;
		request.persist().using(proxy).fire(new BMEReceiver<Void>(reciverMap) {

			public void onSuccess(Void ignore) {
				selectedInstitution = tempProxy;
				init();
			}
		});
	}

	@Override
	public void newClicked(String institutionName) {
		Log.debug("new Institution Clicked");
		InstitutionRequest request = requests.institutionRequest();
		final InstitutionProxy institution = request.create(InstitutionProxy.class);
		institution.setInstitutionName(institutionName);
//		institution.setVersion(0);

		request.persist().using(institution).fire(new BMEReceiver<Void>(reciverMap) {

			public void onSuccess(Void ignore) {
				Log.debug("Sucessfull created");
				selectedInstitution = institution;
				init();
			}
		});
	}
	
	private void tableRangeChangeCall()
	{
		
		final Range range =table.getVisibleRange();
		
		requests.institutionRequest().countAllInstitutions(searchValue).fire(new  BMEReceiver<Long>() {

			@Override
			public void onSuccess(Long arg0) {
				Log.info("count total--"+arg0);
				table.setRowCount(arg0.intValue());
				requests.institutionRequest().findAllInstitutions(range.getStart(),range.getLength(),sortname,sortorder,searchValue).fire(new BMEReceiver<List<InstitutionProxy>>() {
					@Override
					public void onSuccess(List<InstitutionProxy> arg0) {
						// TODO Auto-generated method stub
						Log.info("response of all list--"+arg0.size());
						table.setRowData(range.getStart(), arg0);
					}
				});
			}
		});
	}

	@Override
	public void performSearch(String searchValue) {
		Log.info("performSearch");
		/*final Range range = table.getVisibleRange();
		this.searchValue = searchValue;
		tableRangeChangeCall();*/
		this.searchValue = searchValue;
		init();
	}

	@Override
	public void placeChanged(Place place) {
		// updateSelection(event.getNewPlace());
		// TODO implement
	}
}
