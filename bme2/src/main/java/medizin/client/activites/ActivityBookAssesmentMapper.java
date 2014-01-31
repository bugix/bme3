package medizin.client.activites;


import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceBookAssesmentDetails;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
/**
 * This class maps the places for a the detail view of a assesment book with its activities view 
 * @author masterthesis
 *
 */
public class ActivityBookAssesmentMapper implements ActivityMapper {



	private McAppRequestFactory requests;
	private PlaceController placeController;


	@Inject
	public ActivityBookAssesmentMapper(McAppRequestFactory requests,
			PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im ActivityBookAssmentMapper.getActivity");
		 if (place instanceof PlaceBookAssesmentDetails){
	            return new ActivityBookAssementDetails((PlaceBookAssesmentDetails) place, requests,  placeController);
		 }

		return null;
	}

}
