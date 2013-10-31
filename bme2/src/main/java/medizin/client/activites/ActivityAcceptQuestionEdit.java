package medizin.client.activites;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.AbstractDetailsPlace.Operation;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceAcceptQuestionDetails;
import medizin.client.place.PlaceQuestion;
import medizin.client.proxy.CommentProxy;
import medizin.client.proxy.McProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.request.CommentRequest;
import medizin.client.request.McRequest;
import medizin.client.request.QuestionEventRequest;
import medizin.client.request.QuestionRequest;
import medizin.client.request.QuestionResourceRequest;
import medizin.client.request.QuestionTypeRequest;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.question.QuestionEditView;
import medizin.client.ui.view.question.QuestionEditViewImpl;
import medizin.client.ui.widget.dialogbox.ConfirmationCheckboxDialog;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.ui.widget.resource.dndview.vo.State;
import medizin.client.util.ClientUtility;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.Status;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class ActivityAcceptQuestionEdit extends AbstractActivityWrapper implements QuestionEditView.Delegate {

	private final PlaceAcceptQuestionDetails questionPlace;
	private final McAppRequestFactory requests;
	private final PlaceController placeController;
	//private Operation operation;
	private QuestionEditView view;
	private QuestionProxy question;

	public ActivityAcceptQuestionEdit(PlaceAcceptQuestionDetails place,McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.questionPlace = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	public ActivityAcceptQuestionEdit(PlaceAcceptQuestionDetails place,McAppRequestFactory requests, PlaceController placeController,Operation edit) {
		super(place, requests, placeController);
		this.questionPlace = place;
		this.requests = requests;
		this.placeController = placeController;
		//this.operation = edit;
	}
	
	@Override
	public void start2(final AcceptsOneWidget widget, EventBus eventBus) {
		final QuestionEditView questionEditView = new QuestionEditViewImpl(reciverMap, eventBus, userLoggedIn,true);
		this.view = questionEditView;
		view.setDelegate(this);
		
		/*view.setRewiewerPickerValues(Collections.<PersonProxy> emptyList(), null);
		PersonRequest personRequestForReviewer = requests.personRequest();
		personRequestForReviewer.findAllPeople().with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).to(new BMEReceiver<List<PersonProxy>>() {

			public void onSuccess(List<PersonProxy> response) {
				List<PersonProxy> values = new ArrayList<PersonProxy>();
				PersonProxy lastSelectedReviewer = ClientUtility.getPersonProxyFromCookie(response);
				values.add(null);
				values.addAll(response);
				view.setRewiewerPickerValues(values, lastSelectedReviewer);
			}
		});
		
		view.setAutorPickerValues(Collections.<PersonProxy> emptyList());
		PersonRequest personRequestForAuthor = personRequestForReviewer.append(requests.personRequest());
		personRequestForAuthor.findAllPeople().with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).to(new BMEReceiver<List<PersonProxy>>() {

			public void onSuccess(List<PersonProxy> response) {
				List<PersonProxy> values = new ArrayList<PersonProxy>();
				values.add(null);
				values.addAll(response);
				view.setAutorPickerValues(values);
			}
		});*/

		view.setQuestEventPickerValues(Collections.<QuestionEventProxy> emptyList());

		QuestionEventRequest questionEventRequest = requests.questionEventRequest();
		questionEventRequest.findQuestionEventByInstitutionAndAccRights(isAdminOrInstitutionalAdmin(), userLoggedIn.getId(), institutionActive.getId()).with(medizin.client.ui.view.roo.QuestionEventProxyRenderer.instance().getPaths()).to(new BMEReceiver<List<QuestionEventProxy>>() {

			public void onSuccess(List<QuestionEventProxy> response) {
				if (response.size() > 0) {
					view.getQuestionEvent().setValue(response.get(0));
				}
				view.setQuestEventPickerValues(response);
			}
		});

		view.setQuestionTypePickerValues(new ArrayList<QuestionTypeProxy>());
		QuestionTypeRequest questionTypeRequest = questionEventRequest.append(requests.questionTypeRequest());
		questionTypeRequest.findAllQuestionTypesForInstituteInSession().to(new BMEReceiver<List<QuestionTypeProxy>>() {

			public void onSuccess(List<QuestionTypeProxy> response) {
				List<QuestionTypeProxy> values = Lists.newArrayList();
				values.addAll(response);
				view.setQuestionTypePickerValues(values);
			}
		});
		view.setMcsPickerValues(new ArrayList<McProxy>());
		
		McRequest mcRequest = questionTypeRequest.append(requests.mcRequest());
		mcRequest.findAllMcs().with(medizin.client.ui.view.roo.McProxyRenderer.instance().getPaths()).to(new BMEReceiver<List<McProxy>>() {

			public void onSuccess(List<McProxy> response) {
				List<McProxy> values = new ArrayList<McProxy>();
				//values.add(null);
				values.addAll(response);
				view.setMcsPickerValues(values);
			}
		});

		QuestionRequest questionRequest = mcRequest.append(requests.questionRequest());
		questionRequest.find(questionPlace.getProxyId()).with("previousVersion", "keywords", "questEvent", "comment", "questionType", "mcs", "rewiewer", "autor", "answers", "answers.autor", "answers.rewiewer", "questionResources").to(new BMEReceiver<Object>() {

			@Override
			public void onSuccess(Object response) {
				if (response instanceof QuestionProxy) {
					Log.info(((QuestionProxy) response).getQuestionText());
					question = (QuestionProxy) response;
					if(isQuestionAuthor(question))
						view.setResendToReviewBtn(false);
					view.setValue(question,false);
					view.setEditTitle(true);
					widget.setWidget(questionEditView.asWidget());
				}
			}
		});
		
		questionRequest.fire();
	}
	
	public void goTo(Place place) {
		placeController.goTo(place);
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
				//goTo(new PlaceAcceptQuestionDetails(stableId,Operation.DETAILS));
				return null;
			}
		};
		
		final Function<EntityProxyId<?>, Void> gotoFunction = new Function<EntityProxyId<?>, Void>() {
			
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

		}else if(isQuestionReviewer(question) == true) {
			if(Status.EDITED_BY_REVIEWER.equals(question.getStatus())) {
				status = Status.EDITED_BY_REVIEWER;
				updateQuestion(status,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor,gotoDetailsFunction);
				Log.info("Reviewer : temparory save (with minor version)");
			} else {
				status = Status.EDITED_BY_REVIEWER;
				createNewQuestion(question, status, isAcceptedByAdmin, isAcceptedByReviewer, isAcceptedByAuthor, gotoFunction);
				Log.info("Reviewer : temparory save (with major version)");
			}
			
		}else if(isQuestionAuthor(question) == true) {
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
		
		
		if(isQuestionReviewer(this.question) == true){
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
	
	/*@Override
	public void disableEnableAuthorReviewerSuggestBox() {
		view.disableEnableAuthorReviewerValue(true);
		
		if (Document.get().getElementById("auther") != null)
			Document.get().getElementById("auther").removeFromParent();	
		//Document.get().getElementById("autherEdit").getStyle().clearDisplay();
		
		if (Document.get().getElementById("reviewer") != null)
			Document.get().getElementById("reviewer").removeFromParent();
		//Document.get().getElementById("reviewerEdit").getStyle().clearDisplay();
	}*/
	
	private final void createNewQuestion(QuestionProxy previousQuestionProxy, final Status status, final boolean isAcceptedByAdmin, final boolean isAcceptedByReviewer, final boolean isAcceptedByAuthor, final Function<EntityProxyId<?>, Void> gotoFunction) {
		
		final CommentRequest commentRequest = requests.commentRequest();
		final QuestionResourceRequest questionResourceRequest = requests.questionResourceRequest();
		final QuestionRequest oldQuestionRequest = requests.questionRequest();
		final QuestionRequest questionRequest = commentRequest.append(questionResourceRequest).append(oldQuestionRequest).append(requests.questionRequest());
		
		QuestionProxy question = questionRequest.create(QuestionProxy.class);
		CommentProxy commentProxy = commentRequest.create(CommentProxy.class);
		Set<QuestionResourceProxy> questionResourceProxies = Sets.newHashSet();
		
		question.setStatus(status);
		view.setValuesForQuestion(question,commentProxy);
		
		question.setIsAcceptedAdmin(isAcceptedByAdmin);
		question.setIsAcceptedAuthor(isAcceptedByAuthor);
		question.setIsAcceptedRewiever(isAcceptedByReviewer);
		question.setIsForcedActive(false);
		question.setQuestionVersion(previousQuestionProxy != null ? previousQuestionProxy.getQuestionVersion()+1 : 0);
		question.setQuestionSubVersion(0);
		question.setDateAdded(new Date());
		question.setQuestionResources(questionResourceProxies);
		
		if(previousQuestionProxy != null) question.setPreviousVersion(previousQuestionProxy);
		
		final QuestionTypes questionType = question.getQuestionType().getQuestionType();
		if(QuestionTypes.Textual.equals(questionType) || QuestionTypes.Sort.equals(questionType) || QuestionTypes.LongText.equals(questionType) || QuestionTypes.Drawing.equals(questionType)) {
			for (QuestionResourceClient questionResource : view.getQuestionResources()) {
				QuestionResourceProxy proxy = questionResourceRequest.create(QuestionResourceProxy.class);
				proxy.setPath(questionResource.getPath());
				proxy.setSequenceNumber(questionResource.getSequenceNumber());
				proxy.setType(questionResource.getType());
				proxy.setQuestion(question);
				questionResourceProxies.add(proxy);
			}
		}
		
		addPicturePathToQuestion(questionResourceRequest, questionResourceProxies, questionType,question);
		
		final QuestionProxy questionProxy2 = question;
		questionRequest.persistQuestion().using(question).fire(new BMEReceiver<Void>(reciverMap) {

			@Override
			public void onSuccess(Void response) {
				gotoFunction.apply(questionProxy2.stableId());	
			}
		});
	}
	
	private final void updateQuestion(final Status status, final boolean isAcceptedByAdmin, final boolean isAcceptedByReviewer, final boolean isAcceptedByAuthor, final Function<EntityProxyId<?>, Void> gotoFunction) {
		
		final CommentRequest commentRequest = requests.commentRequest();
		final QuestionResourceRequest questionResourceRequest = requests.questionResourceRequest();
		final QuestionRequest questionRequest = commentRequest.append(questionResourceRequest).append(requests.questionRequest());
		
		final QuestionProxy questionProxy = questionRequest.edit(this.question);
		final CommentProxy commentProxy = commentRequest.edit(this.question.getComment());
		final Set<QuestionResourceProxy> questionResourceProxies = Sets.newHashSet();
		
		questionProxy.setStatus(status);
		view.setValuesForQuestion(questionProxy,commentProxy);
		
		questionProxy.setIsAcceptedAdmin(isAcceptedByAdmin);
		questionProxy.setIsAcceptedAuthor(isAcceptedByAuthor);
		questionProxy.setIsAcceptedRewiever(isAcceptedByReviewer);
		questionProxy.setIsForcedActive(false);
		questionProxy.setQuestionVersion(question.getQuestionVersion());
		questionProxy.setQuestionSubVersion(question.getQuestionSubVersion() + 1);
		questionProxy.setDateChanged(new Date());
		questionProxy.setQuestionResources(questionResourceProxies);
		
		final QuestionTypes questionType = questionProxy.getQuestionType().getQuestionType();
		
		if(QuestionTypes.Textual.equals(questionType) || QuestionTypes.Sort.equals(questionType) || QuestionTypes.LongText.equals(questionType) || QuestionTypes.Drawing.equals(questionType)) {
			for (QuestionResourceClient questionResource : view.getQuestionResources()) {
				if (questionResource.getState().equals(State.NEW) || questionResource.getState().equals(State.EDITED)) {
					QuestionResourceProxy proxy = questionResourceRequest.create(QuestionResourceProxy.class);
					proxy.setPath(questionResource.getPath());
					proxy.setSequenceNumber(questionResource.getSequenceNumber());
					proxy.setType(questionResource.getType());
					proxy.setQuestion(questionProxy);
					questionResourceProxies.add(proxy);
				}
			}
		}
		
		addPicturePathToQuestion(questionResourceRequest, questionResourceProxies, questionType,questionProxy);
		
		final QuestionProxy questionProxy2 = questionProxy;
		questionRequest.persist().using(questionProxy).fire(new BMEReceiver<Void>(reciverMap) {
			@Override
			public void onSuccess(Void response) {
				gotoFunction.apply(questionProxy2.stableId());
			}
		});
	}

	private void addPicturePathToQuestion(final QuestionResourceRequest questionResourceRequest, final Set<QuestionResourceProxy> questionResourceProxies, final QuestionTypes questionType, final QuestionProxy questionProxy) {
		if(QuestionTypes.Imgkey.equals(questionType) || QuestionTypes.ShowInImage.equals(questionType)) {
		
			final QuestionResourceProxy questionResourceProxyForPicture; 
			if(questionResourceProxies.isEmpty() == true) {
				questionResourceProxyForPicture = questionResourceRequest.create(QuestionResourceProxy.class);
				questionResourceProxies.add(questionResourceProxyForPicture);
			}else {
				questionResourceProxyForPicture = Lists.newArrayList(questionResourceProxies).get(0);
			}
			questionResourceProxyForPicture.setQuestion(questionProxy);
			questionResourceProxyForPicture.setType(MultimediaType.Image);
			questionResourceProxyForPicture.setSequenceNumber(0);
			view.addPictureToQuestionResources(questionResourceProxyForPicture);
		}
	}

	@Override
	public void cancelClicked() {
		if (question != null && question.getId() != null) {
			goTo(new PlaceAcceptQuestionDetails(question.stableId(),Operation.DETAILS));
		} else {
			goTo(new PlaceQuestion(PlaceQuestion.PLACE_QUESTION));
		}
		
	}

	@Override
	public void deleteSelectedQuestionResource(Long qestionResourceId) {
		requests.questionResourceRequest().removeQuestionResource(qestionResourceId).fire(new BMEReceiver<Void>(reciverMap) {

			@Override
			public void onSuccess(Void response) {
				Log.info("selected question resource deleted successfully");
			}
		});
	}

	@Override
	public void placeChanged(Place place) {
		// TODO Auto-generated method stub
		
	}
}
