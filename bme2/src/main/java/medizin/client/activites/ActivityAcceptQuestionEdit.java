package medizin.client.activites;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.AbstractDetailsPlace.Operation;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceAcceptQuestionDetails;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.QuestionProxy;
import medizin.shared.Status;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceController;

public class ActivityAcceptQuestionEdit extends ActivityQuestionEdit {

	public ActivityAcceptQuestionEdit(PlaceAcceptQuestionDetails place,McAppRequestFactory requests, PlaceController placeController) {
		super(createNewPlace(place), requests, placeController);
	}

	public ActivityAcceptQuestionEdit(PlaceAcceptQuestionDetails place,McAppRequestFactory requests, PlaceController placeController,Operation edit) {
		super(createNewPlace(place), requests, placeController, edit);
	}

	private static PlaceQuestionDetails createNewPlace(PlaceAcceptQuestionDetails place) {
		PlaceQuestionDetails details;
		
		if(place.getOperation() != PlaceAcceptQuestionDetails.Operation.CREATE) {
			details = new PlaceQuestionDetails(place.getProxyId(),place.getOperation());	
		}else {
			details = new PlaceQuestionDetails(place.getOperation());
		}
		
		return details;
	}
	/*@Override
	protected void gotoDetailsPlace(QuestionProxy questionProxy) {
		goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
	}*/
	
	@Override
	public Status getUpdatedStatus(boolean isEdit, boolean withNewMajorVersion) {
		
		Status status;
		if(isEdit == true ) {
			if(withNewMajorVersion == true) {
				if(userLoggedIn.getIsAdmin() == true || personRightProxy.getIsInstitutionalAdmin() == true) {
					status = Status.CORRECTION_FROM_ADMIN;
				}else if(userLoggedIn.getId().equals(question.getRewiewer().getId())) {
					status = Status.CORRECTION_FROM_REVIEWER;
				}else if(userLoggedIn.getId().equals(question.getAutor().getId())) {
					if(question.getStatus().equals(Status.CORRECTION_FROM_ADMIN)){
						status = Status.ACCEPTED_ADMIN;	
					}else if(question.getStatus().equals(Status.CORRECTION_FROM_REVIEWER)) {
						status = Status.ACCEPTED_REVIEWER;
					}else {
						Log.info("Error this scenario is not considered yet");
						status = question.getStatus();
					}
				}else {
					Log.info("Error this scenario is not considered yet");
					status = question.getStatus();
				}
			}else {
				Log.info("temparory save (with minor version)");
				if(userLoggedIn.getIsAdmin() == true || personRightProxy.getIsInstitutionalAdmin() == true) {
					status = Status.EDITED_BY_ADMIN;
				}else if(userLoggedIn.getId().equals(question.getRewiewer().getId())) {
					status = Status.EDITED_BY_REVIEWER;
				}else if(userLoggedIn.getId().equals(question.getAutor().getId())) {
					status = Status.NEW;
				}else {
					Log.info("Error this scenario is not considered yet");
					status = question.getStatus();
				}
			}
		}else {
			status = Status.NEW;
		}
		return status;
	}
	
	@Override
	public boolean isAcceptQuestionView() {
		return true;
	}
	
	@Override
	protected void showNewDisplay() {
		//do nothing
	}
	
	@Override
	protected void gotoUpdateDetailsPlace() {
		goTo(new PlaceAcceptQuestionDetails(question.stableId(),Operation.DETAILS));
	}
	
	@Override
	protected void cancelClickedGoto(QuestionProxy questionProxy) {
		goTo(new PlaceAcceptQuestionDetails(question.stableId(),Operation.DETAILS));
	}
	
	@Override
	protected void createQuestionGoto(QuestionProxy questionProxy) {
		goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
	}
}
