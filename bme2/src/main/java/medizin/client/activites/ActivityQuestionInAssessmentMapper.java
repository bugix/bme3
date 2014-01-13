package medizin.client.activites;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceQuestionInAssessment;
import medizin.client.place.PlaceQuestionInAssessmentDetails;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class ActivityQuestionInAssessmentMapper implements ActivityMapper {

	private McAppRequestFactory requests;
	private PlaceController placeController;

	@Inject
	public ActivityQuestionInAssessmentMapper(McAppRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im ActivityQuestionMapper.getActivity");
		 if (place instanceof PlaceQuestionInAssessmentDetails){
			 PlaceQuestionInAssessmentDetails myPlace = (PlaceQuestionInAssessmentDetails) place;
			 if(myPlace.getOperation() == PlaceQuestionInAssessmentDetails.Operation.DETAILS){
				 return new ActivityQuestionInAssessmentDetails((PlaceQuestionInAssessmentDetails) place, requests, placeController);
			 }			
		 }
		 return null;		
	}
}
