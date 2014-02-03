package medizin.client.activites;

import java.util.List;

import medizin.client.events.RecordChangeEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAssesment;
import medizin.client.place.PlaceAssesmentDetails;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.ui.view.assesment.AssesmentView;
import medizin.client.ui.view.assesment.AssesmentViewImpl;
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
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class ActivityAssesment extends AbstractActivityWrapper implements AssesmentView.Delegate {

	private PlaceAssesment assesmentPlace;
	private AcceptsOneWidget widget;
	private AssesmentView view;
	public String sortname = "id";
	public Sorting sortorder = Sorting.ASC;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	private CellTable<AssesmentProxy> table;
	private ActivityManager activityManger;
	private ActivityAssesmentMapper activityAssesmentMapper;
	private SingleSelectionModel<AssesmentProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private EntityProxyId<?> proxyId = null;

	@Inject
	public ActivityAssesment(PlaceAssesment place, McAppRequestFactory requests, PlaceController placeController) {
		super((Place)place, requests, placeController);
		this.assesmentPlace = place;
        this.requests = requests;
        this.placeController = placeController;
		this.activityAssesmentMapper = new ActivityAssesmentMapper(requests, placeController);
		this.activityManger = new ActivityManager(activityAssesmentMapper, requests.getEventBus());
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
	}

	@Override
	public void start2(AcceptsOneWidget panel, EventBus eventBus) {
		AssesmentView assesmentView = new AssesmentViewImpl();
		this.widget = panel;
		this.view = assesmentView;
        widget.setWidget(assesmentView.asWidget());
        table=view.getTable();
		
        RecordChangeEvent.register(requests.getEventBus(), (AssesmentViewImpl)view);
        
      //adding column mouse out on table.
      ((AssesmentViewImpl)view).addColumnOnMouseout();
      		
        init();
		
		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<AssesmentProxy> keyProvider = ((AbstractHasData<AssesmentProxy>) table).getKeyProvider();
		selectionModel = new SingleSelectionModel<AssesmentProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				AssesmentProxy selectedObject = selectionModel.getSelectedObject();
				if (selectedObject != null) {
					Log.debug(selectedObject.getName() + " selected!");
					showDetails(selectedObject);
				}
			}
		});

		view.setDelegate(this);
		
	}

	public void goTo(Place place) {
		  placeController.goTo(place);
	}
	    
	private void init() {
		AppLoader.setNoLoader();
		requests.assesmentRequest().countAssesmentByInsitute().fire(new BMEReceiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Assesments (Pr�fungen) aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);
				onRangeChanged();
			}
		});
		
		rangeChangeHandler = table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				ActivityAssesment.this.onRangeChanged();
			}
		});
	}   
	
	private void onRangeChanged() {
		final Range range = table.getVisibleRange();
		AppLoader.setNoLoader();
		requests.assesmentRequest().findAssesmentByInsitute(sortname,sortorder,range.getStart(), range.getLength()).with(view.getPaths()).fire(new BMEReceiver<List<AssesmentProxy>>() {
			@Override
			public void onSuccess(List<AssesmentProxy> values) {
				if (view == null) {
					// This activity is dead
					Log.debug("view ist null");
					return;
				}
				table.setRowData(range.getStart(), values);
				AppLoader.setNoLoader();
				selectRow(range);
			if (widget != null) {
		          widget.setWidget(view.asWidget());
				}
			}
		});
		
	}
	private void selectRow(final Range range) {
		if (proxyId != null)
		{
			requests.find(proxyId).fire(new BMEReceiver<Object>() {

				@Override
				public void onSuccess(Object response) {
					if (response != null && response instanceof AssesmentProxy)
					{
						final AssesmentProxy selectedProxy = (AssesmentProxy) response;
						requests.assesmentRequest().findRangeStartForInstitution(selectedProxy.getId(),sortname,sortorder,range.getLength()).fire(new BMEReceiver<Integer>() {

							@Override
							public void onSuccess(Integer start) {
								selectionModel.setSelected(selectedProxy, true);
								table.setPageStart((start < 0 ? 0 : start));
							}
						});
					}
				}
			});
			proxyId = null;
		}
	}


	private void showDetails(AssesmentProxy assesement) {
		Log.debug("Assesement Stable id: " + assesement.stableId() + " " + PlaceAssesmentDetails.Operation.DETAILS);
		placeController.goTo(new PlaceAssesmentDetails(assesement.stableId(), view.getScrollDetailPanel().getOffsetHeight()));
	}

	@Override
	public void newClicked() {
		placeController.goTo(new PlaceAssesmentDetails(PlaceAssesmentDetails.Operation.CREATE,view.getScrollDetailPanel().getOffsetHeight()));
	}

	@Override
	public void placeChanged(Place place) {
		if (place instanceof PlaceAssesment) {
			proxyId = ((PlaceAssesment)place).getProxyId();
			init();
		}
	}

	@Override
	public void columnClickedForSorting(String sortname, Sorting sortorder) {
		this.sortname=sortname;
		this.sortorder=sortorder;
		init();
	}
}
