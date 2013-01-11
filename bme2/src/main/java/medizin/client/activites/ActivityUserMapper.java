package medizin.client.activites;


import medizin.client.place.PlaceAcceptAnswer;
import medizin.client.place.PlaceAcceptAssQuestion;
import medizin.client.place.PlaceAcceptPerson;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceAsignAssQuestion;
import medizin.client.place.PlaceAssesment;
import medizin.client.place.PlaceBookAssesment;
import medizin.client.place.PlaceInstitution;
import medizin.client.place.PlaceInstitutionEvent;
import medizin.client.place.PlaceOpenDemand;
import medizin.client.place.PlaceQuestion;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.place.PlaceQuestiontypes;
import medizin.client.place.PlaceStaticContent;
import medizin.client.place.PlaceSystemOverview;
import medizin.client.place.PlaceUser;
import medizin.client.place.PlaceUserDetails;
import medizin.client.factory.request.McAppRequestFactory;

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
