package medizin.client.activites;

import java.util.List;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceUser;
import medizin.client.place.PlaceUserDetails;
import medizin.client.proxy.DoctorProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.request.PersonRequest;
import medizin.client.ui.view.user.UserEditView;
import medizin.client.ui.view.user.UserEditViewImpl;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
/**
 * Activity for creating a new User
 * @author masterthesis
 *
 */
public class ActivityUserCreate  extends AbstractActivityWrapper  implements UserEditView.Presenter, UserEditView.Delegate {
	
	
	private PlaceUserDetails userPlace;

	private AcceptsOneWidget widget;
	private UserEditView view;
	private PlaceUserDetails.Operation operation;
	private PersonProxy person;
	
	private McAppRequestFactory requests;
	private PlaceController placeController;

	private boolean save;
	
	public BmeConstants constants = GWT.create(BmeConstants.class);

	@Inject
	public ActivityUserCreate(PlaceUserDetails place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.userPlace = place;
        this.requests = requests;
        this.placeController = placeController;
	}

	@Inject
	public ActivityUserCreate(PlaceUserDetails place,
			McAppRequestFactory requests, PlaceController placeController,
			PlaceUserDetails.Operation operation) {
		super(place, requests, placeController);
		this.userPlace = place;
        this.requests = requests;
        this.placeController = placeController;
        this.operation = operation;
	}

	@Override
	public String mayStop() {
	/*	if(!save)
			return McAppConstant.DO_NOT_SAVE_CHANGES;
		else*/
			return null;
	}

	@Override
	public void onCancel() {
		onStop();

	}

	@Override
	public void onStop() {
		

	}
	
	//private RequestFactoryEditorDriver<PersonProxy,UserEditViewImpl> editorDriver;

	
	/*@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}*/
	
	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		UserEditView userEditView = new UserEditViewImpl(reciverMap);

		this.widget = widget;
		this.view = userEditView;
		//editorDriver = view.createEditorDriver();
		view.setDelegate(this);

//		view.initialiseDriver(requests);
        widget.setWidget(userEditView.asWidget());
		//setTable(view.getTable());
        
		/*eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				
				//updateSelection(event.getNewPlace());
				// TODO implement
			}
		});*/
		//init();
		
		requests.personRequest().myGetLoggedPerson().fire(new BMEReceiver<PersonProxy>() {

			@Override
			public void onSuccess(PersonProxy response) {
				if (response.getIsAdmin())
					view.disableAdminField(true);
				else
					view.disableAdminField(false);
			}
		});
		
		requests.doctorRequest().findAllDoctors().fire(new BMEReceiver<List<DoctorProxy>>() {

			@Override
			public void onSuccess(List<DoctorProxy> response) {
				DefaultSuggestOracle<DoctorProxy> suggestOracle = (DefaultSuggestOracle<DoctorProxy>) view.getDoctorSuggestBox().getSuggestOracle();
				suggestOracle.setPossiblilities(response);
				
				view.getDoctorSuggestBox().setSuggestOracle(suggestOracle);
				
				view.getDoctorSuggestBox().setRenderer(new AbstractRenderer<DoctorProxy>() {

					@Override
					public String render(DoctorProxy object) {
						return object == null ? "" : object.getName();
					}
				});
			}
		});
		
		if(this.operation==PlaceUserDetails.Operation.EDIT){
			Log.info("edit");
		requests.find(userPlace.getProxyId()).fire(new BMEReceiver<Object>() {

			/*public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}*/
			@Override
			public void onSuccess(Object response) {
				if(response instanceof PersonProxy){
					Log.info(((PersonProxy) response).getEmail());
					//init((PersonProxy) response);
					person=(PersonProxy)response;
					
					requests.personRequest().findPerson(person.getId()).with("doctor").fire(new BMEReceiver<PersonProxy>() {

						@Override
						public void onSuccess(PersonProxy response) {
							person = response;
							init();
						}
					});
					
				}

				
			}
		    });
		}
		else{
			
			Log.info("neuePerson");
			//userPlace.setProxyId(person.stableId());
			init();
		}
		
		view.setDelegate(this);
	}
	private void init() {
		if(person==null){
			/*PersonProxy person = request.create(PersonProxy.class);
			this.person=person;
			view.setEditTitle(false);*/
		}
		else
		{
			view.setValue(person);
		}
		
		Log.info("edit");
	      
	    Log.info("persist");
	     //   request.persist().using(person);
		//editorDriver.edit(person, request);

		Log.info("flush");
		//editorDriver.flush();
		this.person = person;
		//Log.debug("Create für: "+person.getEmail());
//		view.setValue(person);
		
	}
	
	
	/*private void init(PersonProxy person) {

		this.person = person;
		PersonRequest request = requests.personRequest();
		request.persist().using(person);
		Log.info("edit");
		editorDriver.edit(person, request);

		Log.info("flush");
		editorDriver.flush();
		Log.debug("Edit für: "+person.getEmail());
//		view.setValue(person);
		
	}*/




	@Override
	public void goTo(Place place) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelClicked() {
		if(this.operation==PlaceUserDetails.Operation.EDIT)
			placeController.goTo(new PlaceUserDetails(person.stableId(), PlaceUserDetails.Operation.DETAILS));
		else
			placeController.goTo(new PlaceUser(PlaceUser.PLACE_USER));
		
	}

	@Override
	public void saveClicked() {
		save=true;
		
		/*if(view.getName().getText().equals("") || view.getPrename().getText().equals("") || view.getEmail().getText().equals("") || view.getAlternativEmail().getText().equals("") || view.getPhoneNumber().getText().equals("")){
			ConfirmationDialogBox.showOkDialogBox(constants.information(),constants.userCreateConstraintsViolationMessage());
			return;
		}*/
		PersonRequest personRequest = requests.personRequest();
		PersonProxy personProxy;
		
		if (view.getProxy() == null)
		{
			personProxy = personRequest.create(PersonProxy.class);
		}
		else
		{
			personProxy = view.getProxy();
			personProxy = personRequest.edit(personProxy);
		}
		
		personProxy.setName(view.getName().getText());
		personProxy.setPrename(view.getPrename().getText());
		personProxy.setEmail(view.getEmail().getText());
		personProxy.setAlternativEmail(view.getAlternativEmail().getText());
		personProxy.setPhoneNumber(view.getPhoneNumber().getText());
		personProxy.setIsAccepted(view.getIsAccepted().getValue());
		personProxy.setIsAdmin(view.getIsAdmin().getValue());
		personProxy.setIsDoctor(view.getIsDoctor().getValue());
		
		if (view.getIsDoctor().getValue())
			personProxy.setDoctor(view.getDoctorSuggestBox().getSelected());
		else
		{
			if (personProxy.getDoctor() != null)
				personProxy.setDoctor(null);
		}
		
		
		final PersonProxy finalPersonProxy = personProxy;
		
		personRequest.persist().using(personProxy).fire(new BMEReceiver<Void>(reciverMap) {

			@Override
			public void onSuccess(Void response) {
				placeController.goTo(new PlaceUser(PlaceUser.PLACE_USER));
				placeController.goTo(new PlaceUserDetails(finalPersonProxy.stableId(), PlaceUserDetails.Operation.DETAILS));
			}
			
			/*@Override
			public void onFailure(ServerFailure error) {
				Log.info(error.getMessage());
			}*/
			
			/*@Override
			public void onViolation(Set<Violation> errors) {
				ConfirmationDialogBox.showOkDialogBox(constants.information(),constants.userCreateConstraintsViolationMessage());
				Log.info("error "+errors.toString());
			}*/
		});
		
		/*editorDriver.flush().fire(new Receiver<Void>() {
			
          @Override
          public void onSuccess(Void response) {
        	  Log.info("PersonSucesfullSaved");
        	  
        		placeController.goTo(new PlaceUserDetails(person.stableId(), PlaceUserDetails.Operation.DETAILS));
          //	goTo(new PlaceUser(person.stableId()));
          }
          
          public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}
      }); */
		
	}

	@Override
	public void placeChanged(Place place) {
		//updateSelection(event.getNewPlace());
		// TODO implement
	}

}
