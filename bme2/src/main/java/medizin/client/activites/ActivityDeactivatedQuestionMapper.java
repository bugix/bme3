package medizin.client.activites;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceDeactivatedQuestionDetails;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;

public class ActivityDeactivatedQuestionMapper implements ActivityMapper{

	private McAppRequestFactory requests;
	private PlaceController placeController;

	public ActivityDeactivatedQuestionMapper(McAppRequestFactory requests,PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		if (place instanceof PlaceDeactivatedQuestionDetails){
			PlaceDeactivatedQuestionDetails myPlace = (PlaceDeactivatedQuestionDetails) place;
			 if(myPlace.getOperation() == PlaceDeactivatedQuestionDetails.Operation.DETAILS){
				 return new ActivityDeactivatedQuestionDetails((PlaceDeactivatedQuestionDetails) place, requests, placeController);
			 }
		}
		
		return null;
	}
}
