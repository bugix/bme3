package medizin.client.activites;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.place.PlaceQuestionDetails.Operation;
import medizin.client.proxy.QuestionProxy;
import medizin.shared.Status;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceController;

public class ActivityAcceptQuestionEdit extends ActivityQuestionEdit {

	public ActivityAcceptQuestionEdit(PlaceQuestionDetails place,McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
	}

	public ActivityAcceptQuestionEdit(PlaceQuestionDetails place,McAppRequestFactory requests, PlaceController placeController,Operation edit) {
		super(place, requests, placeController, edit);
	}

	@Override
	protected void gotoDetailsPlace(QuestionProxy questionProxy) {
		goTo(new PlaceQuestionDetails(questionProxy.stableId(),PlaceQuestionDetails.Operation.DETAILS, "ACCEPT_QUESTION"));
	}
	
	@Override
	public Status getUpdatedStatus(boolean isEdit, boolean withNewMajorVersion) {
		
		Status status;
		if(isEdit == true ) {
			if(withNewMajorVersion == true) {
				if(userLoggedIn.getIsAdmin() == true || personRightProxy.getIsInstitutionalAdmin() == true) {
					status = Status.CORRECTION_FROM_ADMIN;
				}else if(userLoggedIn.getId().equals(question.getRewiewer().getId())) {
					status = Status.CORRECTION_FROM_REVIEWER;
				}else {
					Log.info("Error this scenario is not considered yet");
					status = question.getStatus();
				}
			}else {
				Log.info("Error this scenario is not considered yet (with minor version)");
				status = question.getStatus();
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
}
