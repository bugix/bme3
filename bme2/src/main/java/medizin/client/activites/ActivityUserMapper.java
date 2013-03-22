package medizin.client.activites;


import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceUserDetails;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
/**
 * Maps user details places with activities.
 * @author masterthesis
 *
 */
public class ActivityUserMapper implements ActivityMapper {



	private McAppRequestFactory requests;
	private PlaceController placeController;

	@Inject
	public ActivityUserMapper(McAppRequestFactory requests,
			PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im ActivityUserMapper.getActivity");
		 if (place instanceof PlaceUserDetails){
			 PlaceUserDetails myPlace = (PlaceUserDetails) place;
			 if(myPlace.getOperation() == PlaceUserDetails.Operation.DETAILS){
				 return new ActivityUserDetails((PlaceUserDetails) place, requests, placeController);
			 }
				
			 else if (myPlace.getOperation() == PlaceUserDetails.Operation.CREATE){
				 return new ActivityUserCreate((PlaceUserDetails) place,  requests, placeController);
				 }
			 
			 else if (myPlace.getOperation() == PlaceUserDetails.Operation.EDIT){
				 return new ActivityUserCreate((PlaceUserDetails) place,  requests, placeController, PlaceUserDetails.Operation.EDIT);
				 }
		 

		 }
	            

		 Log.debug("im ActivityUserMapper.getActivity, null returned");

		return null;
	}

}
