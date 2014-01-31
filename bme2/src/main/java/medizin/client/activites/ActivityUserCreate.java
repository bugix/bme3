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
import medizin.client.ui.widget.process.AppLoader;
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
 *
 */
public class ActivityUserCreate  extends AbstractActivityWrapper  implements UserEditView.Presenter, UserEditView.Delegate {
	
	
	private PlaceUserDetails userPlace;
	private UserEditView view;
	private PlaceUserDetails.Operation operation;
	private PersonProxy person;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	public BmeConstants constants = GWT.create(BmeConstants.class);

	@Inject
	public ActivityUserCreate(PlaceUserDetails place, McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.userPlace = place;
        this.requests = requests;
        this.placeController = placeController;
	}

	@Inject
	public ActivityUserCreate(PlaceUserDetails place,McAppRequestFactory requests, PlaceController placeController,	PlaceUserDetails.Operation operation) {
		super(place, requests, placeController);
		this.userPlace = place;
        this.requests = requests;
        this.placeController = placeController;
        this.operation = operation;
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
	public void onStop() {}
	
	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		UserEditView userEditView = new UserEditViewImpl(reciverMap);
		this.view = userEditView;
		view.setDelegate(this);
        widget.setWidget(userEditView.asWidget());
		requests.personRequest().myGetLoggedPerson().fire(new BMEReceiver<PersonProxy>() {

			@Override
			public void onSuccess(PersonProxy response) {
				if (response.getIsAdmin())
					view.disableAdminField(true);
				else
					view.disableAdminField(false);
			}
		});
		// Added this to get doctor by name ASC.
		requests.doctorRequest().findAllDoctorByNameASC().fire(new BMEReceiver<List<DoctorProxy>>() {

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
			AppLoader.setCurrentLoader(view.getLoadingPopup());			
			requests.find(userPlace.getProxyId()).fire(new BMEReceiver<Object>() {

				@Override
				public void onSuccess(Object response) {
					if(response instanceof PersonProxy){
						Log.info(((PersonProxy) response).getEmail());
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
			init();
		}
		
		view.setDelegate(this);
	}
	private void init() {
		if(person!=null){
			view.setValue(person);
		}		
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void cancelClicked() {
		if(this.operation==PlaceUserDetails.Operation.EDIT)
			placeController.goTo(new PlaceUserDetails(person.stableId(), PlaceUserDetails.Operation.DETAILS,userPlace.getHeight()));
		else
			placeController.goTo(new PlaceUser(PlaceUser.PLACE_USER,userPlace.getHeight()));
	}

	@Override
	public void saveClicked() {
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
		personProxy.setAlternativEmail(view.getAlternativEmail().getText().isEmpty() == true ? "" : view.getAlternativEmail().getText());
		personProxy.setPhoneNumber(view.getPhoneNumber().getText());
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
		AppLoader.setNoLoader();
		personRequest.persist().using(personProxy).fire(new BMEReceiver<Void>(reciverMap) {
			@Override
			public void onSuccess(Void response) {
				placeController.goTo(new PlaceUser(PlaceUser.PLACE_USER,userPlace.getHeight(),finalPersonProxy.stableId()));
				placeController.goTo(new PlaceUserDetails(finalPersonProxy.stableId(), PlaceUserDetails.Operation.DETAILS, userPlace.getHeight()));
			}			
		});	
	}

	@Override
	public void placeChanged(Place place) {
	}

}
