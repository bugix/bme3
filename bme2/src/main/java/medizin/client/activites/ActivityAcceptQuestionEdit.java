package medizin.client.activites;

import java.util.Date;
import java.util.Set;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.AbstractDetailsPlace.Operation;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceAcceptQuestionDetails;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.CommentProxy;
import medizin.client.proxy.McProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.request.QuestionRequest;
import medizin.client.request.QuestionResourceRequest;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.ui.widget.resource.dndview.vo.State;
import medizin.shared.Status;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.collect.Sets;
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
					status = Status.NEW;
					/*if(question.getStatus().equals(Status.CORRECTION_FROM_ADMIN)){
						status = Status.ACCEPTED_ADMIN;	
					}else if(question.getStatus().equals(Status.CORRECTION_FROM_REVIEWER)) {
						status = Status.ACCEPTED_REVIEWER;
					}else {
						Log.info("Error this scenario is not considered yet");
						status = question.getStatus();
					}*/
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
		goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
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
	
	@Override
	public void resendToReview(QuestionTypeProxy questionType, String questionShortName, String questionText, PersonProxy auther, PersonProxy rewiewer, Boolean submitToReviewComitee, QuestionEventProxy questEvent, Set<McProxy> mcs, String questionComment, int questionVersion, int questionSubVersion, String picturePath, final Set<QuestionResourceClient> questionResourceClients, Status status) {
		QuestionRequest req = requests.questionRequest();
		QuestionProxy questionEdit = req.edit(question);
		questionEdit.setQuestionType(questionType);
		questionEdit.setQuestionText(questionText);
		questionEdit.setAutor(auther);
		questionEdit.setQuestionShortName(questionShortName);
		questionEdit.setRewiewer(rewiewer);
		questionEdit.setSubmitToReviewComitee(submitToReviewComitee);
		questionEdit.setQuestEvent(questEvent);
		questionEdit.setDateChanged(new Date());
		questionEdit.setMcs(mcs);
		questionEdit.setQuestionVersion(questionVersion);
		questionEdit.setQuestionSubVersion(questionSubVersion);
		CommentProxy comment = req.edit(questionEdit.getComment());
		comment.setComment(questionComment);
		questionEdit.setComment(comment);
		questionEdit.setPicturePath(picturePath);

		//final QuestionProxy qpoxy = questionEdit;
		req.questionResendToReviewWithMajorVersion(isAdminOrInstitutionalAdmin()).using(questionEdit).fire(new BMEReceiver<QuestionProxy>(reciverMap) {

			@Override
			public void onSuccess(QuestionProxy response) {
				
				if(response == null) {
					goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
					return;
				}
				
				if (questionResourceClients.isEmpty() == false) {

					QuestionResourceRequest questionResourceRequest = requests.questionResourceRequest();
					final Set<QuestionResourceProxy> proxies = Sets.newHashSet();
					Log.info("proxies.size() " + proxies.size());

					for (QuestionResourceClient questionResource : questionResourceClients) {

						QuestionResourceProxy proxy = questionResourceRequest.create(QuestionResourceProxy.class);
						proxy.setPath(questionResource.getPath());
						proxy.setSequenceNumber(questionResource.getSequenceNumber());
						proxy.setType(questionResource.getType());
						proxy.setQuestion(response);
						proxies.add(proxy);

					}

					questionResourceRequest.persistSet(proxies).fire(new BMEReceiver<Void>(reciverMap) {

						@Override
						public void onSuccess(Void response1) {
							Log.info("Added successfuly");
							goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
						
						}
					});
				} else {
					goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
				}
				
			}
		});
	}
}
