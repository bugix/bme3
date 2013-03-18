package medizin.client.activites;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceOpenDemand;
import medizin.client.ui.view.OpenDemandView;
import medizin.client.ui.view.OpenDemandViewImpl;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class ActivityOpenDemand extends AbstractActivityWrapper implements OpenDemandView.Presenter {

	private PlaceOpenDemand openDemandPlace;

	private AcceptsOneWidget widget;
	private OpenDemandView view;


	private McAppRequestFactory requests;
	private PlaceController placeController;

	@Inject
	public ActivityOpenDemand(PlaceOpenDemand place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.openDemandPlace = place;
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
		OpenDemandView openDemandView = new OpenDemandViewImpl();
		openDemandView.setName("hallo");
		openDemandView.setPresenter(this);
		this.widget = widget;
		this.view = openDemandView;
        widget.setWidget(openDemandView.asWidget());

	}

	@Override
	public void goTo(Place place) {
		  placeController.goTo(place);
	}

	@Override
	public void placeChanged(Place place) {
		// TODO add place changed code here
		
	}

}
