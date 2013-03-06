package medizin.client.activites;


import medizin.client.place.PlaceQuestionDetails;
import medizin.client.factory.request.McAppRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
/**
 * Maps question detail places with activities (Detailansicht Fragen).
 *
 */
public class ActivityAcceptQuestionMapper implements ActivityMapper {

	private McAppRequestFactory requests;
	private PlaceController placeController;

	@Inject
	public ActivityAcceptQuestionMapper(McAppRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im ActivityQuestionMapper.getActivity");
		 if (place instanceof PlaceQuestionDetails){
			 PlaceQuestionDetails myPlace = (PlaceQuestionDetails) place;
			 if(myPlace.getOperation() == PlaceQuestionDetails.Operation.DETAILS){
				 // for activityAcceptQuestionDetails 
				return new ActivityAcceptQuestionDetails((PlaceQuestionDetails) place, requests, placeController);
			 }
				
			 else if (myPlace.getOperation() == PlaceQuestionDetails.Operation.CREATE){
				 return new ActivityAcceptQuestionEdit((PlaceQuestionDetails) place,  requests, placeController);
			 }
			 
			 else if (myPlace.getOperation() == PlaceQuestionDetails.Operation.EDIT){
				 return new ActivityAcceptQuestionEdit((PlaceQuestionDetails) place,  requests, placeController, PlaceQuestionDetails.Operation.EDIT);
			 }
		 }
		 return null;
	    
	}

}
