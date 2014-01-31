package medizin.client.activites;


import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAssesmentDetails;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
/**
 * Maps assesment details places with activities.
 * @author masterthesis
 *
 */
public class ActivityAssesmentMapper implements ActivityMapper {



	private McAppRequestFactory requests;
	private PlaceController placeController;

	@Inject
	public ActivityAssesmentMapper(McAppRequestFactory requests,
			PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im ActivityAssesmentMapper.getActivity");
		 if (place instanceof PlaceAssesmentDetails){
			 PlaceAssesmentDetails myPlace = (PlaceAssesmentDetails) place;
			 if(myPlace.getOperation() == PlaceAssesmentDetails.Operation.DETAILS){
				 return new ActivityAssesmentDetails((PlaceAssesmentDetails) place, requests, placeController);
			 }
				
			 else if (myPlace.getOperation() == PlaceAssesmentDetails.Operation.CREATE){
				 return new ActivityAssesmentCreate((PlaceAssesmentDetails) place,  requests, placeController);
				 }
			 
			 else if (myPlace.getOperation() == PlaceAssesmentDetails.Operation.EDIT){
				 return new ActivityAssesmentCreate((PlaceAssesmentDetails) place,  requests, placeController, PlaceAssesmentDetails.Operation.EDIT);
				 }
		 

		 }
	            

		 Log.debug("im ActivityUserMapper.getActivity, null returned");

		return null;
	}

}
