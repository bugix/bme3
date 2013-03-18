package medizin.client.activites;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAcceptPerson;
import medizin.client.ui.view.AcceptPersonView;
import medizin.client.ui.view.AcceptPersonViewImpl;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class ActivityAcceptPerson extends AbstractActivityWrapper implements AcceptPersonView.Presenter {

	private PlaceAcceptPerson personPlace;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private AcceptPersonView view;


	@Inject
	public ActivityAcceptPerson(PlaceAcceptPerson place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.personPlace = place;
        this.requests = requests;
        this.placeController = placeController;
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


	}
	
	/*@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}*/

	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		AcceptPersonView acceptPersonOverview = new AcceptPersonViewImpl();
		acceptPersonOverview.setName("hallo");
		acceptPersonOverview.setPresenter(this);
		this.widget = widget;
		this.view = acceptPersonOverview;
        widget.setWidget(acceptPersonOverview.asWidget());

	}

	@Override
	public void goTo(Place place) {
		  placeController.goTo(place);

		
	}

	@Override
	public void placeChanged(Place place) {
		// TODO Auto-generated method stub
		
	}

}
