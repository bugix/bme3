package medizin.client.activites;

/*import medizin.client.place.PlaceAcceptAnswer;
import medizin.client.place.PlaceAcceptAssQuestion;
import medizin.client.place.PlaceAcceptPerson;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceAnswerImageEditor;
import medizin.client.place.PlaceAsignAssQuestion;
import medizin.client.place.PlaceAssesment;
import medizin.client.place.PlaceAssesmentDetails;
import medizin.client.place.PlaceBookAssesmentDetails;
import medizin.client.place.PlaceBookAssesment;
import medizin.client.place.PlaceImageEditor;
import medizin.client.place.PlaceInstitution;
import medizin.client.place.PlaceInstitutionEvent;
import medizin.client.place.PlaceMathJax;
import medizin.client.place.PlaceOpenDemand;
import medizin.client.place.PlaceQuestion;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.place.PlaceQuestiontypes;
import medizin.client.place.PlaceStaticContent;
import medizin.client.place.PlaceSystemOverview;
import medizin.client.place.PlaceUser;
import medizin.client.place.PlaceUserDetails;
import medizin.client.place.PlaceQuestiontypesDetails;
*/
import medizin.client.place.PlaceAcceptAnswer;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceAcceptQuestionDetails;
import medizin.client.place.PlaceAsignAssQuestion;
import medizin.client.place.PlaceAssesment;
import medizin.client.place.PlaceAssesmentDetails;
import medizin.client.place.PlaceBookAssesment;
import medizin.client.place.PlaceBookAssesmentDetails;
import medizin.client.place.PlaceDeactivatedQuestion;
import medizin.client.place.PlaceDeactivatedQuestionDetails;
import medizin.client.place.PlaceInstitution;
import medizin.client.place.PlaceInstitutionEvent;
import medizin.client.place.PlaceNotActivatedAnswer;
import medizin.client.place.PlaceNotActivatedQuestion;
import medizin.client.place.PlaceNotActivatedQuestionDetails;
import medizin.client.place.PlaceQuestion;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.place.PlaceQuestionInAssessment;
import medizin.client.place.PlaceQuestionInAssessmentDetails;
import medizin.client.place.PlaceQuestiontypes;
import medizin.client.place.PlaceQuestiontypesDetails;
import medizin.client.place.PlaceSystemOverview;
import medizin.client.place.PlaceUser;
import medizin.client.place.PlaceUserDetails;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.FilteredActivityMapper;
import com.google.gwt.place.shared.Place;

/**
 * Converts a {@link #ProxyPlace} to a {@link ProxyListPlace}.
 */
public class FilterForMainPlaces implements AsyncFilteredActivityMapper.Filter {

	/**
	 * Required by {@link FilteredActivityMapper.Filter}, calls
	 * {@link #proxyListPlaceFor()}.
	 */
	public Place filter(Place place) {
		
		Log.debug("im FilterForMainPlaces");
		 if (place instanceof PlaceSystemOverview)
	            return (PlaceSystemOverview) place;
		 if (place instanceof PlaceAcceptAnswer)
	            return  (PlaceAcceptAnswer) place;		 
		 
		 if (place instanceof PlaceAcceptQuestion)
	            return  (PlaceAcceptQuestion) place;
		 
		 if (place instanceof PlaceAssesment)
	            return  (PlaceAssesment) place;
		 
		 if (place instanceof PlaceAsignAssQuestion)
	            return (PlaceAsignAssQuestion) place;
		 
		 if (place instanceof PlaceBookAssesment)
	            return  (PlaceBookAssesment) place;
		 
		 if (place instanceof PlaceInstitution)
	            return  (PlaceInstitution) place;
		 
		 if (place instanceof PlaceQuestion)
	            return  (PlaceQuestion) place;
		 
		 
		 if (place instanceof PlaceUser)
	            return  (PlaceUser) place;
		 
		 if (place instanceof PlaceQuestiontypes)
			 	return (PlaceQuestiontypes) place;
		 
		 if (place instanceof PlaceInstitutionEvent){
			 //PlaceInstitutionEvent placeInstitutionEvent= (PlaceInstitutionEvent)place;
			 return new PlaceInstitution(PlaceInstitution.PLACE_INSTITUTION);
			 //return  new PlaceInstitution(placeInstitutionEvent.getProxyId());
			 
		 }
		 
		 if (place instanceof PlaceBookAssesmentDetails){
			 //PlaceBookAssesmentDetails placeBookAssementDetails= (PlaceBookAssesmentDetails)place;
			 return  new PlaceBookAssesment(PlaceBookAssesment.PLACE_BOOK_ASSESMENT);
			 //return  new PlaceBookAssesment(placeBookAssementDetails.getProxyId());
			 
		 }
		 if (place instanceof PlaceUserDetails){
			 PlaceUserDetails placeUserDetails= (PlaceUserDetails)place;
			 /*if(placeUserDetails.getOperation()!=PlaceUserDetails.Operation.CREATE)
				 return  new PlaceUser(placeUserDetails.getProxyId());
			 else
				 return  new PlaceUser(placeUserDetails.getOperation().toString());*/
			 
			 return new PlaceUser(PlaceUser.PLACE_USER, placeUserDetails.getProxyId());
			 
		 }
		 if (place instanceof PlaceQuestiontypesDetails){
			 PlaceQuestiontypesDetails placeQuestiontypesDetails = (PlaceQuestiontypesDetails)place;
			 //return  new PlaceQuestiontypes(PlaceQuestiontypes.PLACE_QUESTIONTYPES);
			 return new PlaceQuestiontypes(PlaceQuestiontypes.PLACE_QUESTIONTYPES, placeQuestiontypesDetails.getProxyId());
			
		 }
		 if (place instanceof PlaceAssesmentDetails){
			 PlaceAssesmentDetails placeAssesmentDetails= (PlaceAssesmentDetails)place;
			 //return new PlaceAssesment(PlaceAssesment.PLACE_ASSESMENT);
			 return new PlaceAssesment(PlaceAssesment.PLACE_ASSESMENT, placeAssesmentDetails.getProxyId());
		 }
		 
		 if (place instanceof PlaceQuestionDetails){
			 PlaceQuestionDetails placeQuestionDetails = (PlaceQuestionDetails)place;
			 //return new PlaceQuestion(PlaceQuestion.PLACE_QUESTION);
			 return new PlaceQuestion(PlaceQuestion.PLACE_QUESTION, placeQuestionDetails.getProxyId());
		 }
		
		 if(place instanceof PlaceAcceptQuestionDetails) {
			 PlaceAcceptQuestionDetails placeAcceptQuestionDetails = (PlaceAcceptQuestionDetails) place;
			 //return new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION);
			 return new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION, placeAcceptQuestionDetails.getProxyId());
		 }
		 
		 if (place instanceof PlaceNotActivatedQuestion)
	            return  (PlaceNotActivatedQuestion) place;
		 
		 if (place instanceof PlaceNotActivatedQuestionDetails){
			 return new PlaceNotActivatedQuestion(PlaceNotActivatedQuestion.PLACE_NOT_ACTIVATED_QUESTION);
		 }
		 
		 if (place instanceof PlaceNotActivatedAnswer)
			 return (PlaceNotActivatedAnswer) place;
		 
		 if (place instanceof PlaceDeactivatedQuestion)
			 return (PlaceDeactivatedQuestion) place;
		 
		 if (place instanceof PlaceDeactivatedQuestionDetails){
			 PlaceDeactivatedQuestionDetails placeDeactivatedQuestionDetails = (PlaceDeactivatedQuestionDetails) place;
			 return new PlaceDeactivatedQuestion(PlaceDeactivatedQuestion.PLACE_DEACTIVATED_QUESTION, placeDeactivatedQuestionDetails.getProxyId());
		 }
		 
		 if (place instanceof PlaceQuestionInAssessment){
			 return (PlaceQuestionInAssessment)place;
		 }
		 if (place instanceof PlaceQuestionInAssessmentDetails)
		 {
			 PlaceQuestionInAssessmentDetails placeQuestionInAssessmentDetails = (PlaceQuestionInAssessmentDetails) place;
			 return new PlaceQuestionInAssessment(PlaceQuestionInAssessment.PLACE_QUESTION_IN_ASSESSMENT, placeQuestionInAssessmentDetails.getProxyId());
		 }
		 
		 if(place instanceof Place && Place.NOWHERE.equals(place)) {
			 return place;
		 }
		 
		 return null;
	}
}
