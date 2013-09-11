package medizin.client.activites;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAssesment;
import medizin.client.place.PlaceAssesmentDetails;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.McProxy;
import medizin.client.request.AssesmentRequest;
import medizin.client.ui.TopPanel;
import medizin.client.ui.view.assesment.AssesmentEditView;
import medizin.client.ui.view.assesment.AssesmentEditViewImpl;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class ActivityAssesmentCreate  extends AbstractActivityWrapper  implements AssesmentEditView.Presenter, AssesmentEditView.Delegate {
	
	
	private PlaceAssesmentDetails assesmentPlace;

	private AcceptsOneWidget widget;
	private AssesmentEditView view;
	private PlaceAssesmentDetails.Operation operation;
	private AssesmentProxy assesment;

//	public ActivityAssesmentCreate(PlaceAssesmentDetails place, McAppFactory mcAppFactory) {
//		this.assesmentPlace = place;
//		this.mcAppFactory = mcAppFactory;
//	}
//
//	public ActivityAssesmentCreate(PlaceAssesmentDetails place,
//			McAppFactory mcAppFactory, Operation operation) {
//		this.assesmentPlace = place;
//		this.mcAppFactory = mcAppFactory;
//		this.operation = operation;
//	}
	
	private McAppRequestFactory requests;
	private PlaceController placeController;

	@Inject
	public ActivityAssesmentCreate(PlaceAssesmentDetails place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.assesmentPlace = place;
        this.requests = requests;
        this.placeController = placeController;
	}

	@Inject
	public ActivityAssesmentCreate(PlaceAssesmentDetails place,
			McAppRequestFactory requests, PlaceController placeController,
			PlaceAssesmentDetails.Operation operation) {
		super(place, requests, placeController);
		this.assesmentPlace = place;
        this.requests = requests;
        this.placeController = placeController;
        this.operation = operation;
	}

	@Override
	public String mayStop() {
		//return McAppConstant.ACTIVITY_MAY_STOP;
		return null;
	}

	@Override
	public void onCancel() {
		onStop();

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub

	}
	
	
	private RequestFactoryEditorDriver<AssesmentProxy,AssesmentEditViewImpl> editorDriver;

	/*@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}*/
	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		AssesmentEditView assesmentEditView = new AssesmentEditViewImpl(reciverMap);
		assesmentEditView.setName("hallo");
		assesmentEditView.setPresenter(this);
		this.widget = widget;
		this.view = assesmentEditView;
		editorDriver = view.createEditorDriver();
		view.setDelegate(this);
		
        view.setMcPickerValues(Collections.<McProxy>emptyList());
        requests.mcRequest().findAllMcs().with(medizin.client.ui.view.roo.McProxyRenderer.instance().getPaths()).fire(new Receiver<List<McProxy>>() {

            public void onSuccess(List<McProxy> response) {
                List<McProxy> values = new ArrayList<McProxy>();
                values.add(null);
                values.addAll(response);
                view.setMcPickerValues(values);
            }
        });
        view.setRepeForPickerValues(Collections.<AssesmentProxy>emptyList());
        requests.assesmentRequest().findAssesmentByInsitute(0, 50).with(medizin.client.ui.view.roo.AssesmentProxyRenderer.instance().getPaths()).fire(new Receiver<List<AssesmentProxy>>() {

            public void onSuccess(List<AssesmentProxy> response) {
                List<AssesmentProxy> values = new ArrayList<AssesmentProxy>();
                values.add(null);
                values.addAll(response);
                view.setRepeForPickerValues(values);
                
               
            }
        });
       
        view.setInstitutionPickerValues(TopPanel.getInstitutionalList());
        view.disableInstituteField();
        
        
//		view.initialiseDriver(requests);
        widget.setWidget(assesmentEditView.asWidget());
		//setTable(view.getTable());
        
		/*eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				
				//updateSelection(event.getNewPlace());
				// TODO implement
			}
		});*/
		//init();
		
		view.setDelegate(this);
		if(this.operation==PlaceAssesmentDetails.Operation.EDIT){
			Log.info("edit");
		requests.find(assesmentPlace.getProxyId()).with("mc","repeFor","institution").fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}
			@Override
			public void onSuccess(Object response) {
				if(response instanceof AssesmentProxy){
					Log.info(((AssesmentProxy) response).getName());
					//init((PersonProxy) response);
					assesment=(AssesmentProxy)response;
					init();
				}

				
			}
		    });
		}
		else{
			
			Log.info("neues Assement");
			//assesmentPlace.setProxyId(person.stableId());
			init();
		}
	}
	private void init() {
		
		AssesmentRequest request = requests.assesmentRequest();
		
		if(assesment==null){
			AssesmentProxy assesment = request.create(AssesmentProxy.class);
			this.assesment=assesment;
			view.setEditTitle(false);
			//view.setInstitutionValue(institutionActive);
			assesment.setInstitution(institutionActive);
		}
		else {
			view.setEditTitle(true);
		}
		
		Log.info("edit");
	      
	       Log.info("persist");
	        request.persist().using(assesment);
		editorDriver.edit(assesment, request);

		Log.info("flush");
		editorDriver.flush();
//		this.assesment = assesment;
		Log.debug("Create für: "+assesment.getName());
//		view.setValue(person);
		
	}
	
	
//	private void init(AssesmentProxy assesment) {
//
//		this.assesment = assesment;
//		AssesmentRequest request = requests.assesmentRequest();
//		request.persist().using(assesment);
//		Log.info("edit");
//		editorDriver.edit(assesment, request);
//
//		Log.info("flush");
//		editorDriver.flush();
//		Log.debug("Edit für: "+assesment.getName());
////		view.setValue(person);
//		
//	}




	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
		
	}

	@Override
	public void cancelClicked() {
		if(this.operation==PlaceAssesmentDetails.Operation.EDIT)
			placeController.goTo(new PlaceAssesmentDetails(assesment.stableId()));
		else
			placeController.goTo(new PlaceAssesment("PlaceAssesment!CANCEL"));
		
	}

	@Override
	public void saveClicked() {

		editorDriver.flush().fire(new BMEReceiver<Void>(reciverMap) {
			
          @Override
          public void onSuccess(Void response) {
        	  Log.info("PersonSucesfullSaved");
        	  
        		placeController.goTo(new PlaceAssesmentDetails(assesment.stableId(), PlaceAssesmentDetails.Operation.DETAILS));
          //	goTo(new PlaceAssesment(person.stableId()));
          }
          
      }); 
		
	}

	@Override
	public void placeChanged(Place place) {
		//updateSelection(event.getNewPlace());
		// TODO implement
		
	}

}
