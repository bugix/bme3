package medizin.client.activites;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceNotActivatedQuestionDetails;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;

public class ActivityNotActivatedQuestionMapper implements ActivityMapper {

	private McAppRequestFactory requests;
	private PlaceController placeController;

	public ActivityNotActivatedQuestionMapper(McAppRequestFactory requests,PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im ActivityNotActivatedQuestionMapper.getActivity");
		 if (place instanceof PlaceNotActivatedQuestionDetails){
			 PlaceNotActivatedQuestionDetails myPlace = (PlaceNotActivatedQuestionDetails) place;
			 if(myPlace.getOperation() == PlaceNotActivatedQuestionDetails.Operation.DETAILS){
				 return new ActivityNotActivatedQuestionDetails((PlaceNotActivatedQuestionDetails) place, requests, placeController);
			 }
				
			 /*else if (myPlace.getOperation() == PlaceNotActivatedQuestionDetails.Operation.CREATE){
				 return new ActivityQuestiontypesCreate((PlaceNotActivatedQuestionDetails) place,  requests, placeController);
				}
			 
			 else if (myPlace.getOperation() == PlaceNotActivatedQuestionDetails.Operation.EDIT){
				 return new ActivityQuestiontypesCreate((PlaceNotActivatedQuestionDetails) place,  requests, placeController, PlaceQuestiontypesDetails.Operation.EDIT);
				 }*/		 

		 }
	          
		 Log.debug("im ActivityNotActivatedQuestionMapper.getActivity, null returned");

		return null;
	}
	
	

}
