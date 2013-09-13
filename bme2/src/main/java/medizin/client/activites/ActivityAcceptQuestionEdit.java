package medizin.client.activites;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.AbstractDetailsPlace.Operation;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceAcceptQuestionDetails;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.dialogbox.ConfirmationCheckboxDialog;
import medizin.client.util.ClientUtility;
import medizin.shared.Status;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Cookies;
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
		
		final Function<EntityProxyId<?>, Void> gotoAuthorFunction = new Function<EntityProxyId<?>, Void>() {
			
			@Override
			public Void apply(EntityProxyId<?> stableId) {
				goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
				return null;
			}
		};
		
		final Function<EntityProxyId<?>, Void> gotoFunction = new Function<EntityProxyId<?>, Void>() {
			
			@Override
			public Void apply(EntityProxyId<?> stableId) {
				String resendToReviewValue = Cookies.getCookie(McAppConstant.RESEND_TO_REVIEW_KEY);
				
				if (resendToReviewValue == null)
				{
					final ConfirmationCheckboxDialog checkBoxDialog = new ConfirmationCheckboxDialog(constants.acceptQueSaveMsg(), constants.neverShowMsg());
					checkBoxDialog.showBaseDialog(constants.warning());
					
					checkBoxDialog.addOKClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							checkBoxDialog.hide();
							
							if (checkBoxDialog.getCheckBoxValue())
								Cookies.setCookie(McAppConstant.RESEND_TO_REVIEW_KEY, String.valueOf(true), ClientUtility.getDateFromOneYear());
							
							goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
						}
					});
				}
				else
				{
					goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
				}
				return null;
			}
		};
		
		final Function<EntityProxyId<?>, Void> gotoDetailsFunction = new Function<EntityProxyId<?>, Void>() {
			
			@Override
			public Void apply(final EntityProxyId<?> stableId) {
				String resendToReviewValue = Cookies.getCookie(McAppConstant.RESEND_TO_REVIEW_KEY);
				
				if (resendToReviewValue == null)
				{
					final ConfirmationCheckboxDialog checkBoxDialog = new ConfirmationCheckboxDialog(constants.acceptQueSaveMsg(), constants.neverShowMsg());
					checkBoxDialog.showBaseDialog(constants.warning());
					
					checkBoxDialog.addOKClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							checkBoxDialog.hide();
							
							if (checkBoxDialog.getCheckBoxValue())
								Cookies.setCookie(McAppConstant.RESEND_TO_REVIEW_KEY, String.valueOf(true), ClientUtility.getDateFromOneYear());
							
							goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
							goTo(new PlaceAcceptQuestionDetails(stableId,Operation.DETAILS));
						}
					});
				}
				else
				{
					goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
					goTo(new PlaceAcceptQuestionDetails(stableId,Operation.DETAILS));
				}
				
				return null;
			}
		};
		
		if(isAdminOrInstitutionalAdmin() == true) {
			if(Status.EDITED_BY_ADMIN.equals(question.getStatus())) {
				status = Status.EDITED_BY_ADMIN;
				updateQuestion(status,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor,gotoDetailsFunction);
				Log.info("Admin : temparory save (with minor version)");
			} else {
				status = Status.EDITED_BY_ADMIN;
				createNewQuestion(question, status, isAcceptedByAdmin, isAcceptedByReviewer, isAcceptedByAuthor, gotoFunction);
				Log.info("Admin : temparory save (with major version)");
			}

		}else if(isReviewer() == true) {
			if(Status.EDITED_BY_REVIEWER.equals(question.getStatus())) {
				status = Status.EDITED_BY_REVIEWER;
				updateQuestion(status,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor,gotoDetailsFunction);
				Log.info("Reviewer : temparory save (with minor version)");
			} else {
				status = Status.EDITED_BY_REVIEWER;
				createNewQuestion(question, status, isAcceptedByAdmin, isAcceptedByReviewer, isAcceptedByAuthor, gotoFunction);
				Log.info("Reviewer : temparory save (with major version)");
			}
			
		}else if(isAuthor() == true) {
			status = Status.NEW;
			isAcceptedByAdmin = false;
			isAcceptedByReviewer = false;
			isAcceptedByAuthor = true;
			createNewQuestion(question,status,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor,gotoAuthorFunction);
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
			isAcceptedByReviewer = true;
			
			Status correctionFromReviewer = Status.CORRECTION_FROM_REVIEWER;
			if(Status.EDITED_BY_REVIEWER.equals(status) == true) {
				// if current state is edited by reviewer then just update the state to correction_from_reviewer
				updateQuestion(correctionFromReviewer, isAcceptedByAdmin, isAcceptedByReviewer, isAcceptedByAuthor, gotoFunction);
			}else {
				createNewQuestion(this.question,correctionFromReviewer,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor, gotoFunction);
			}
			
		}
		
		if(isAdminOrInstitutionalAdmin() == true) {
			isAcceptedByAdmin = true;
			Status correctionFromAdmin = Status.CORRECTION_FROM_ADMIN;
			if(Status.EDITED_BY_ADMIN.equals(status) == true) {
				// if current state is edited by admin then just update the state to correction_from_admin
				updateQuestion(correctionFromAdmin, isAcceptedByAdmin, isAcceptedByReviewer, isAcceptedByAuthor, gotoFunction);
			}else {
				
				createNewQuestion(this.question,correctionFromAdmin,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor, gotoFunction);
			}
		}
		
		/*if(Status.CORRECTION_FROM_ADMIN.equals(status) || Status.CORRECTION_FROM_REVIEWER.equals(status)) {
			createNewQuestion(this.question,status,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor, gotoFunction);	
		}*/
	}
	
	@Override
	public void disableEnableAuthorReviewerSuggestBox() {
		view.disableEnableAuthorReviewerValue(true);
		
		if (Document.get().getElementById("auther") != null)
			Document.get().getElementById("auther").removeFromParent();	
		//Document.get().getElementById("autherEdit").getStyle().clearDisplay();
		
		if (Document.get().getElementById("reviewer") != null)
			Document.get().getElementById("reviewer").removeFromParent();
		//Document.get().getElementById("reviewerEdit").getStyle().clearDisplay();
	}
}
