package medizin.client.activites;




import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAcceptAnswer;
import medizin.client.place.PlaceAcceptAssQuestion;
import medizin.client.place.PlaceAcceptPerson;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceAsignAssQuestion;
import medizin.client.place.PlaceAssesment;
import medizin.client.place.PlaceBookAssesment;
import medizin.client.place.PlaceDeactivatedQuestion;
import medizin.client.place.PlaceInstitution;
import medizin.client.place.PlaceNotActivatedAnswer;
import medizin.client.place.PlaceNotActivatedQuestion;
import medizin.client.place.PlaceOpenDemand;
import medizin.client.place.PlaceQuestion;
import medizin.client.place.PlaceQuestionInAssessment;
import medizin.client.place.PlaceQuestiontypes;
import medizin.client.place.PlaceStaticContent;
import medizin.client.place.PlaceSystemOverview;
import medizin.client.place.PlaceUser;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
/**
 * ActivityMapper for all main places. 
 * @author masterthesis
 *
 */
public class McAppActivityMapper implements AsyncActivityMapper {



	private McAppRequestFactory requests;
	private PlaceController placeController;

	
	@Inject
	public McAppActivityMapper(McAppRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

	/**
	 * This method is creating activity instance Asynchronously Manish <spec-india> 
	 */
	@Override
	public void getActivity(final Place place,final ActivityCallbackHandler callbackHandler) {
		Log.debug("im McAppActivityMapper.getActivity");
		 // If place is PlaceSystemOverview initiating ActivitySystemOverview
		if (place instanceof PlaceSystemOverview){
	            //return new ActivitySystemOverview((PlaceSystemOverview) place, requests, placeController);
		 	Log.debug("is PlaceSystemOverview");
			
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new ActivitySystemOverview((PlaceSystemOverview)place,requests, placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide ActivitySystemOverview");
				}
			});
		 }
		// If place is PlaceAcceptPerson initiating ActivityAcceptPerson
		 if (place instanceof PlaceAcceptPerson){
	            //return new ActivityAcceptPerson((PlaceAcceptPerson) place, requests, placeController);
			 Log.debug("is PlaceAcceptPerson");
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						callbackHandler.onRecieveActivity(new ActivityAcceptPerson((PlaceAcceptPerson)place,requests, placeController));	
					}
					
					@Override
					public void onFailure(Throwable reason) {
						
						Window.alert("Not able to provide ActivityAcceptPerson");
					}
				});
		 }
		// If place is PlaceAcceptAnswer initiating ActivityAcceptAnswer
		 if (place instanceof PlaceAcceptAnswer){
	            //return new ActivityAcceptAnswer((PlaceAcceptAnswer) place, requests, placeController);
			 Log.debug("is PlaceAcceptAnswer");
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						callbackHandler.onRecieveActivity(new ActivityAcceptAnswer((PlaceAcceptAnswer)place,requests, placeController));	
					}
					
					@Override
					public void onFailure(Throwable reason) {
						
						Window.alert("Not able to provide ActivityAcceptAnswer");
					}
				});
		 }
		// If place is PlaceAcceptAnswer initiating ActivityAcceptAssQuestion
		 if (place instanceof PlaceAcceptAssQuestion){
	            //return new ActivityAcceptAssQuestion((PlaceAcceptAssQuestion) place, requests, placeController);
			 Log.debug("is PlaceAcceptAssQuestion");
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						callbackHandler.onRecieveActivity(new ActivityAcceptAssQuestion((PlaceAcceptAssQuestion)place,requests, placeController));	
					}
					
					@Override
					public void onFailure(Throwable reason) {
						
						Window.alert("Not able to provide ActivityAcceptAssQuestion");
					}
				});
		 }
		// If place is PlaceAcceptQuestion initiating ActivityAcceptQuestion
		 if (place instanceof PlaceAcceptQuestion){
	            //return new ActivityAcceptQuestion((PlaceAcceptQuestion) place, requests, placeController);
			 Log.debug("is PlaceAcceptQuestion");
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						callbackHandler.onRecieveActivity(new ActivityAcceptQuestion((PlaceAcceptQuestion)place,requests, placeController));	
					}
					
					@Override
					public void onFailure(Throwable reason) {
						
						Window.alert("Not able to provide ActivityAcceptQuestion");
					}
				});
		 }
		// If place is PlaceAssesment initiating ActivityAssesment
		 if (place instanceof PlaceAssesment){
	            //return new ActivityAssesment((PlaceAssesment) place, requests, placeController);
			 Log.debug("is PlaceAssesment");
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						callbackHandler.onRecieveActivity(new ActivityAssesment((PlaceAssesment)place,requests, placeController));	
					}
					
					@Override
					public void onFailure(Throwable reason) {
						
						Window.alert("Not able to provide ActivityAssesment");
					}
				});
		 }
		// If place is PlaceAsignAssQuestion initiating ActivityAsignAssQuestion
		 if (place instanceof PlaceAsignAssQuestion){
	            //return new ActivityAsignAssQuestion((PlaceAsignAssQuestion) place, requests, placeController);
			 Log.debug("is PlaceAsignAssQuestion");
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						callbackHandler.onRecieveActivity(new ActivityAsignAssQuestion((PlaceAsignAssQuestion)place,requests, placeController));	
					}
					
					@Override
					public void onFailure(Throwable reason) {
						
						Window.alert("Not able to provide ActivityAsignAssQuestion");
					}
				});
		 }
		// If place is PlaceBookAssesment initiating ActivityBookAssesment
		 if (place instanceof PlaceBookAssesment){
	            //return new ActivityBookAssesment((PlaceBookAssesment) place, requests, placeController);
			 Log.debug("is PlaceBookAssesment");
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						callbackHandler.onRecieveActivity(new ActivityBookAssesment((PlaceBookAssesment)place,requests, placeController));	
					}
					
					@Override
					public void onFailure(Throwable reason) {
						
						Window.alert("Not able to provide ActivityBookAssesment");
					}
				});
		 }
		 
		// If place is PlaceInstitution initiating ActivityInstitution
		 if (place instanceof PlaceInstitution){
	            //return new ActivityInstitution((PlaceInstitution) place, requests, placeController);
			 Log.debug("is PlaceInstitution");
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						callbackHandler.onRecieveActivity(new ActivityInstitution((PlaceInstitution)place,requests, placeController));	
					}
					
					@Override
					public void onFailure(Throwable reason) {
						
						Window.alert("Not able to provide ActivityInstitution");
					}
				});
		 }
		// If place is PlaceOpenDemand initiating ActivityOpenDemand
		 if (place instanceof PlaceOpenDemand){
	            //return new ActivityOpenDemand((PlaceOpenDemand) place, requests, placeController);
			 Log.debug("is PlaceOpenDemand");
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						callbackHandler.onRecieveActivity(new ActivityOpenDemand((PlaceOpenDemand)place,requests, placeController));	
					}
					
					@Override
					public void onFailure(Throwable reason) {
						
						Window.alert("Not able to provide ActivityOpenDemand");
					}
				});
		 }
		// If place is PlaceQuestion initiating ActivityQuestion
		 if (place instanceof PlaceQuestion){
	            //return new ActivityQuestion((PlaceQuestion) place, requests, placeController);
			 Log.debug("is PlaceQuestion");
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						callbackHandler.onRecieveActivity(new ActivityQuestion((PlaceQuestion)place,requests, placeController));	
					}
					
					@Override
					public void onFailure(Throwable reason) {
						
						Window.alert("Not able to provide ActivityQuestion");
					}
				});
		 }
		// If place is PlaceNotActivatedQuestion initiating ActivityNotActivatedQuestion
		 if (place instanceof PlaceNotActivatedQuestion){
	            //return new ActivityNotActivatedQuestion((PlaceNotActivatedQuestion) place, requests, placeController);
			 Log.debug("is PlaceNotActivatedQuestion");
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						callbackHandler.onRecieveActivity(new ActivityNotActivatedQuestion((PlaceNotActivatedQuestion)place,requests, placeController));	
					}
					
					@Override
					public void onFailure(Throwable reason) {
						
						Window.alert("Not able to provide ActivityNotActivatedQuestion");
					}
				});
		 }
		// If place is PlaceQuestiontypes initiating ActivityQuestiontypes
		 if (place instanceof PlaceQuestiontypes){
	           // return new ActivityQuestiontypes((PlaceQuestiontypes) place, requests, placeController);
			 Log.debug("is PlaceQuestiontypes");
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						callbackHandler.onRecieveActivity(new ActivityQuestiontypes((PlaceQuestiontypes)place,requests, placeController));	
					}
					
					@Override
					public void onFailure(Throwable reason) {
						
						Window.alert("Not able to provide ActivityQuestiontypes");
					}
				});
		 }
		// If place is PlaceStaticContent initiating ActivityStaticContent
		 if (place instanceof PlaceStaticContent){
	           // return new ActivityStaticContent((PlaceStaticContent) place, requests, placeController);
			 Log.debug("is PlaceStaticContent");
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						callbackHandler.onRecieveActivity(new ActivityStaticContent((PlaceStaticContent)place,requests, placeController));	
					}
					
					@Override
					public void onFailure(Throwable reason) {
						
						Window.alert("Not able to provide ActivityStaticContent");
					}
				});
		 }
		// If place is PlaceUser initiating ActivityUser
		 if (place instanceof PlaceUser){
	           // return new ActivityUser((PlaceUser) place, requests, placeController);
			 Log.debug("is PlaceUser");
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						callbackHandler.onRecieveActivity(new ActivityUser((PlaceUser)place,requests, placeController));	
					}
					
					@Override
					public void onFailure(Throwable reason) {
						
						Window.alert("Not able to provide ActivityUser");
					}
				});
		 }
		// If place is PlaceNotActivatedAnswer initiating ActivityNotActivatedAnswer
		 if (place instanceof PlaceNotActivatedAnswer){
			 	//return new ActivityNotActivatedAnswer((PlaceNotActivatedAnswer)place, requests, placeController);
			 Log.debug("is PlaceNotActivatedAnswer");
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						callbackHandler.onRecieveActivity(new ActivityNotActivatedAnswer((PlaceNotActivatedAnswer)place,requests, placeController));	
					}
					
					@Override
					public void onFailure(Throwable reason) {
						
						Window.alert("Not able to provide ActivityNotActivatedAnswer");
					}
				});
		 }
		// If place is PlaceDeactivatedQuestion initiating ActivityDeactivatedQuestion
		 if (place instanceof PlaceDeactivatedQuestion){
			 	//return new ActivityDeactivatedQuestion((PlaceDeactivatedQuestion)place, requests, placeController);
			 Log.debug("is PlaceDeactivatedQuestion");
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						callbackHandler.onRecieveActivity(new ActivityDeactivatedQuestion((PlaceDeactivatedQuestion)place,requests, placeController));	
					}
					
					@Override
					public void onFailure(Throwable reason) {
						
						Window.alert("Not able to provide ActivityDeactivatedQuestion");
					}
				});
		 }
		 
		 if (place instanceof PlaceQuestionInAssessment){
			 	//return new ActivityDeactivatedQuestion((PlaceDeactivatedQuestion)place, requests, placeController);
			 Log.debug("is PlaceDeactivatedQuestion");
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						callbackHandler.onRecieveActivity(new ActivityQuestionInAssessment((PlaceQuestionInAssessment)place,requests, placeController));	
					}
					
					@Override
					public void onFailure(Throwable reason) {
						
						Window.alert("Not able to provide ActivityDeactivatedQuestion");
					}
				});
		 }
		 
		 Log.debug("im McAppActivityMapper.getActivity, return null");
		//return null;
	}



}
