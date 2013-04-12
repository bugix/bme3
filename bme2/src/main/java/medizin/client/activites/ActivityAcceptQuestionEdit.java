package medizin.client.activites;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.AbstractDetailsPlace.Operation;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceAcceptQuestionDetails;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.QuestionProxy;
import medizin.shared.Status;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

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
		
	@Override
	public boolean isAcceptQuestionView() {
		return true;
	}
	
	@Override
	protected void showNewDisplay() {
		//do nothing
	}
	
	@Override
	protected void cancelClickedGoto(QuestionProxy questionProxy) {
		goTo(new PlaceAcceptQuestionDetails(question.stableId(),Operation.DETAILS));
	}
	
	public void saveQuestionWithDetails() {
		// save with minor version in edit
		Status status = question.getStatus();
		boolean isAcceptedByAdmin = question.getIsAcceptedAdmin();
		boolean isAcceptedByReviewer = question.getIsAcceptedRewiever();
		boolean isAcceptedByAuthor = question.getIsAcceptedAuthor();
		
		final Function<EntityProxyId<?>, Void> gotoFunction = new Function<EntityProxyId<?>, Void>() {
			
			@Override
			public Void apply(EntityProxyId<?> stableId) {
				goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
				return null;
			}
		};
		
		final Function<EntityProxyId<?>, Void> gotoDetailsFunction = new Function<EntityProxyId<?>, Void>() {
			
			@Override
			public Void apply(EntityProxyId<?> stableId) {
				goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
				goTo(new PlaceAcceptQuestionDetails(stableId,Operation.DETAILS));
				return null;
			}
		};
		
		if(isAdminOrInstitutionalAdmin() == true) {
			status = Status.EDITED_BY_ADMIN;
			updateQuestion(status,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor,gotoDetailsFunction);
			Log.info("temparory save (with minor version)");
		}else if(isReviewer() == true) {
			status = Status.EDITED_BY_REVIEWER;
			updateQuestion(status,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor,gotoDetailsFunction);
			Log.info("temparory save (with minor version)");
		}else if(isAuthor() == true) {
			status = Status.NEW;
			isAcceptedByAdmin = false;
			isAcceptedByReviewer = false;
			isAcceptedByAuthor = true;
			createNewQuestion(question,status,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor,gotoFunction);
			Log.info("save (with major version)");
		}
		
	}
	
	@Override
	public void resendToReview() {
		Function<EntityProxyId<?>, Void> gotoFunction = new Function<EntityProxyId<?>, Void>() {
			
			@Override
			public Void apply(EntityProxyId<?> stableId) {
				goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
				return null;
			}
		};

		Status status = this.question.getStatus();
		boolean isAcceptedByAdmin = false;
		boolean isAcceptedByReviewer = false;
		boolean isAcceptedByAuthor = false;
		
		
		if(isReviewer() == true){
			status = Status.CORRECTION_FROM_REVIEWER;
			isAcceptedByReviewer = true;
		}
		
		if(isAdminOrInstitutionalAdmin() == true) {
			status = Status.CORRECTION_FROM_ADMIN;
			isAcceptedByAdmin = true;
		}
		
		if(Status.CORRECTION_FROM_ADMIN.equals(status) || Status.CORRECTION_FROM_REVIEWER.equals(status)) {
			createNewQuestion(this.question,status,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor, gotoFunction);	
		}
	}
}
