package medizin.client.activites;


import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceInstitutionEvent;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class ActivityInstitutionMapper implements ActivityMapper {



	private McAppRequestFactory requests;
	private PlaceController placeController;

	@Inject
	public ActivityInstitutionMapper(McAppRequestFactory requests,
			PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im ActivityInstitutionMapper.getActivity");
		 if (place instanceof PlaceInstitutionEvent)
	            return new ActivityInstitutionEvent((PlaceInstitutionEvent) place, requests, placeController);


		return null;
	}

}
