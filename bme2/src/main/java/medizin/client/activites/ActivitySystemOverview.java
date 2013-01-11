package medizin.client.activites;

import java.util.Iterator;
import java.util.List;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceSystemOverview;
import medizin.client.proxy.McProxy;
import medizin.client.ui.view.SystemOverviewView;
import medizin.client.ui.view.SystemOverviewViewImpl;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class ActivitySystemOverview extends AbstractActivityWrapper implements SystemOverviewView.Presenter, SystemOverviewView.Delegate {

	private PlaceSystemOverview overviewPlace;

	private AcceptsOneWidget widget;


	private McAppRequestFactory requests;
	private PlaceController placeController;


	@Inject
	public ActivitySystemOverview(PlaceSystemOverview place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		Log.debug("ActivitySystemOverview.Konstruktor");
		this.overviewPlace = place;
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
	
//		((SimplePanel)widget).clear();

	}
	
	@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}

	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		Log.debug("ActivitySystemOverview.Start");
		SystemOverviewView systemOverview = new SystemOverviewViewImpl();
        systemOverview.setName("hallo");
        systemOverview.setPresenter(this);
        systemOverview.setDelegate(this);
        this.widget = widget;
        widget.setWidget(systemOverview.asWidget());
		Log.debug("ActivitySystemOverview.Started");

	}

	@Override
	public void goTo(Place place) {
		 placeController.goTo(place);

		
	}

	@Override
	public void buttonClicked() {
		requests.mcRequest().findAllMcs().fire(new Receiver<List<McProxy>>(){

			@Override
			public void onSuccess(List<McProxy> response) {
				Iterator<McProxy> iter = response.iterator();
				while(iter.hasNext()){
					Log.info(iter.next().getMcName());
				}
				
			}
			
		});
		
	}

}
