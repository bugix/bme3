package medizin.client.activites;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import medizin.client.events.QuestionSaveEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.AbstractDetailsPlace.Operation;
import medizin.client.place.PlaceQuestion;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.CommentProxy;
import medizin.client.proxy.McProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.request.CommentRequest;
import medizin.client.request.McRequest;
import medizin.client.request.PersonRequest;
import medizin.client.request.QuestionEventRequest;
import medizin.client.request.QuestionRequest;
import medizin.client.request.QuestionResourceRequest;
import medizin.client.request.QuestionTypeRequest;
import medizin.client.ui.view.question.ConfirmQuestionChangesPopup.ConfirmQuestionHandler;
import medizin.client.ui.view.question.QuestionEditView;
import medizin.client.ui.view.question.QuestionEditViewImpl;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.ui.widget.resource.dndview.vo.State;
import medizin.client.util.ClientUtility;
import medizin.client.util.MathJaxs;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.Status;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class ActivityQuestionEdit extends AbstractActivityWrapper implements QuestionEditView.Delegate {

	private PlaceQuestionDetails questionPlace;
	private QuestionEditView view;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	private Operation operation;
	private QuestionProxy question;
	private EventBus eventBus;

	@Inject
	public ActivityQuestionEdit(PlaceQuestionDetails place, McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.questionPlace = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	@Inject
	public ActivityQuestionEdit(PlaceQuestionDetails place, McAppRequestFactory requests, PlaceController placeController, Operation edit) {
		super(place, requests, placeController);
		this.questionPlace = place;
		this.requests = requests;
		this.placeController = placeController;
		this.operation = edit;
	}

	@Override
	public String mayStop() {
		return null;
	}

	@Override
	public void onCancel() {
		onStop();
	}

	@Override
	public void onStop() {
		// ((SlidingPanel)widget).remove(view.asWidget());
	}

	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		this.eventBus = eventBus;
		QuestionEditView questionEditView = new QuestionEditViewImpl(reciverMap, eventBus, userLoggedIn,false);
		this.view = questionEditView;
		view.setDelegate(this);
		
		view.setRewiewerPickerValues(Collections.<PersonProxy> emptyList(), null);
		PersonRequest personRequestForReviewer = requests.personRequest();
		personRequestForReviewer.findAllPeople().with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).to(new BMEReceiver<List<PersonProxy>>() {

			public void onSuccess(List<PersonProxy> response) {
				List<PersonProxy> values = new ArrayList<PersonProxy>();
				PersonProxy lastSelectedReviewer = ClientUtility.getQuestionReviwerPersonProxyFromCookie(response);
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
		});

		view.setQuestEventPickerValues(Collections.<QuestionEventProxy> emptyList());

		QuestionEventRequest questionEventRequest = personRequestForAuthor.append(requests.questionEventRequest());
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

		if (this.operation == PlaceQuestionDetails.Operation.EDIT) {
			Log.info("edit");
			QuestionRequest questionRequest = mcRequest.append(requests.questionRequest());
			questionRequest.find(questionPlace.getProxyId()).with("previousVersion", "keywords", "questEvent", "comment", "questionType", "mcs", "rewiewer", "autor", "answers", "answers.autor", "answers.rewiewer", "questionResources").to(new BMEReceiver<Object>() {

				@Override
				public void onSuccess(Object response) {
					if (response instanceof QuestionProxy) {
						Log.info(((QuestionProxy) response).getQuestionText());
						question = (QuestionProxy) response;
						
						view.setValue(question,checkIsAuthorReviewerEditable());
						view.setEditTitle(true);
						if(thirdPersonRightsForQuestionEdit()) {
							view.setQuestionAuthor(question.getAutor());
						}
						MathJaxs.delayRenderLatexResult(RootPanel.getBodyElement());
					}
				}
			});
			questionRequest.fire();
		} else {
			Log.info("neues Assement");
			view.setEditTitle(false);
			mcRequest.fire();
		}
		
		widget.setWidget(questionEditView.asWidget());
		
	}

	private boolean thirdPersonRightsForQuestionEdit() {
		boolean hasQuestionWriteRights = hasQuestionWriteRights(question);
		boolean questionAuthor = isQuestionAuthor(question);
		boolean adminOrInstitutionalAdmin = isAdminOrInstitutionalAdmin();
		return (!questionAuthor && !adminOrInstitutionalAdmin && hasQuestionWriteRights);
	}

	private boolean checkIsAuthorReviewerEditable() {
		if(question == null || question.getStatus() == null) {
			return true;
		}else if(Objects.equal(question.getStatus(), Status.NEW) || Objects.equal(question.getStatus(), Status.ACTIVE)) {
			return true;
		}
		return false;
	}

	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void cancelClicked() {

		if (question != null && question.getId() != null) {
			cancelClickedGoto(question);
		} else {
			goTo(new PlaceQuestion(PlaceQuestion.PLACE_QUESTION));
		}
	}

	@Override
	public void deleteSelectedQuestionResource(Long qestionResourceId) {
		requests.questionResourceRequest().removeQuestionResource(qestionResourceId).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				Log.info("selected question resource deleted successfully");
			}
		});
	}

	private void showNewDisplay() {
		Log.info("Question Save Event Fired");
		this.eventBus.fireEvent(new QuestionSaveEvent());
	}

	// also overriden in subclass 
	private void cancelClickedGoto(QuestionProxy questionProxy) {
		goTo(new PlaceQuestionDetails(questionProxy.stableId(), Operation.DETAILS));
	}

	@Override
	public void placeChanged(Place place) {
		// updateSelection(event.getNewPlace());
		// TODO implement
	}
	
	@Override
	public void saveQuestionWithDetails() {
		
		final Function<EntityProxyId<?>, Void> gotoShowNewFunction = new Function<EntityProxyId<?>, Void>() {
			@Override
			public Void apply(EntityProxyId<?> stableId) {
				showNewDisplay();
				placeController.goTo(new PlaceQuestionDetails(stableId, PlaceQuestionDetails.Operation.DETAILS));
				return null;
			}
		};
		
		if(question == null) {
			// save new question for first time
			boolean isAcceptedByAdmin = isAdminOrInstitutionalAdmin();
			boolean isAcceptedByReviewer = false;
			boolean isAcceptedByAuthor = userLoggedIn.getId().equals(view.getAuthorId());
			
			createNewQuestion(null,Status.NEW,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor,gotoShowNewFunction);
		}else {
			// edit accepted question with major or minor version
			final ConfirmQuestionHandler isMajorOrMinor = new ConfirmQuestionHandler() {

				@Override
				public void confirmQuestionClicked(boolean isWithNewMajorVersion,boolean isForceActive) {
					
					if(isWithNewMajorVersion) {
						boolean isAcceptedByAdmin = isAdminOrInstitutionalAdmin();
						boolean isAcceptedByReviewer = false;
						boolean isAcceptedByAuthor = userLoggedIn.getId().equals(view.getAuthorId());
						
						createNewQuestion(question,Status.NEW,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor,gotoShowNewFunction);
					}else {
						Status status; 
						
												
						/*final Function<EntityProxyId<?>, Void> gotoDetailsFunction = new Function<EntityProxyId<?>, Void>() {
							@Override
							public Void apply(EntityProxyId<?> stableId) {
								placeController.goTo(new PlaceQuestionDetails(stableId, PlaceQuestionDetails.Operation.DETAILS));
								return null;
							}
						};*/
						
						final Function<EntityProxyId<?>, Void> gotoFunction = new Function<EntityProxyId<?>, Void>() {
							@Override
							public Void apply(EntityProxyId<?> stableId) {
								placeController.goTo(new PlaceQuestion(PlaceQuestion.PLACE_QUESTION));
								return null;
							}
						};
						
						if(isForceActive) {
							boolean isAcceptedByAdmin = isAdminOrInstitutionalAdmin();
							boolean isAcceptedByReviewer = false;
							boolean isAcceptedByAuthor = userLoggedIn.getId().equals(view.getAuthorId());
							boolean isForcedActive = true;
							// if current state of the question is new so status will remain as it is.
							status = Status.ACTIVE;
							updateQuestion(status,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor,isForcedActive,gotoShowNewFunction);
						} else if(Status.NEW.equals(question.getStatus())) {
							boolean isAcceptedByAdmin = isAdminOrInstitutionalAdmin();
							boolean isAcceptedByReviewer = false;
							boolean isAcceptedByAuthor = userLoggedIn.getId().equals(view.getAuthorId());
							boolean isForcedActive = false;
							// if current state of the question is new so status will remain as it is.
							status = Status.NEW;
							updateQuestion(status,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor,isForcedActive,gotoShowNewFunction);
						}else if(Status.ACTIVE.equals(question.getStatus())) {
							// the current state of the question is active so new status with minor changes will be accepted reviewer
							boolean isAcceptedByAdmin = isAdminOrInstitutionalAdmin();
							boolean isAcceptedByReviewer = true;
							boolean isAcceptedByAuthor = userLoggedIn.getId().equals(view.getAuthorId());
							boolean isForcedActive = false;
							
							status = Status.ACCEPTED_REVIEWER;
							updateQuestion(status,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor,isForcedActive,gotoFunction);
						}
					}
					return;
				}
			};
			view.comfirmQuestionChanges(isMajorOrMinor,isAdminOrInstitutionalAdmin());
		}
	}
	
	protected final void createNewQuestion(QuestionProxy previousQuestionProxy, final Status status, final boolean isAcceptedByAdmin, final boolean isAcceptedByReviewer, final boolean isAcceptedByAuthor, final Function<EntityProxyId<?>, Void> gotoFunction) {
		
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
		
		if(thirdPersonRightsForQuestionEdit()) {
			if(isQuestionReviewer(question)) {
				ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.authorReviewerMayNotBeSame());
				return;
			}
			question.setAutor(userLoggedIn);
			question.setIsAcceptedAdmin(false);
			question.setIsAcceptedAuthor(true);
			question.setIsAcceptedRewiever(false);
		}
		
		if(previousQuestionProxy != null) question.setPreviousVersion(previousQuestionProxy);
		
		final QuestionTypes questionType = question.getQuestionType().getQuestionType();
		if(QuestionTypes.Textual.equals(questionType) || QuestionTypes.Sort.equals(questionType) || QuestionTypes.LongText.equals(questionType) ) {
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
	
	protected final void updateQuestion(final Status status, final boolean isAcceptedByAdmin, final boolean isAcceptedByReviewer, final boolean isAcceptedByAuthor, final boolean isForcedActive, final Function<EntityProxyId<?>, Void> gotoFunction) {
		
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
		questionProxy.setIsForcedActive(isForcedActive);
		questionProxy.setQuestionVersion(question.getQuestionVersion());
		questionProxy.setQuestionSubVersion(question.getQuestionSubVersion() + 1);
		questionProxy.setDateChanged(new Date());
		questionProxy.setQuestionResources(questionResourceProxies);
		
		if(thirdPersonRightsForQuestionEdit()) {
			questionProxy.setIsAcceptedAdmin(false);
			questionProxy.setIsAcceptedAuthor(true);
			questionProxy.setIsAcceptedRewiever(true);
		}
		
		final QuestionTypes questionType = questionProxy.getQuestionType().getQuestionType();
		
		if(QuestionTypes.Textual.equals(questionType) || QuestionTypes.Sort.equals(questionType) || QuestionTypes.LongText.equals(questionType)) {
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
			if(questionResourceProxies.isEmpty()) {
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

	// updated in ActivityAcceptQuestionEdit 
	@Override
	public void resendToReview() {}

	/*@Override
	public void disableEnableAuthorReviewerSuggestBox() {
		
		if (question != null && Status.NEW.equals(question.getStatus()))
		{
			view.disableEnableAuthorReviewerValue(false);
			Document.get().getElementById("autherEdit").removeFromParent();
			Document.get().getElementById("reviewerEdit").removeFromParent();
		}
		else
		{
			view.disableEnableAuthorReviewerValue(true);
			Document.get().getElementById("auther").removeFromParent();	
			Document.get().getElementById("reviewer").removeFromParent();
			Document.get().getElementById("autherEdit").getStyle().clearDisplay();			
			Document.get().getElementById("reviewerEdit").getStyle().clearDisplay();
		}	
		
	}*/
}
