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
import medizin.client.ui.view.question.QuestionEditView;
import medizin.client.ui.view.question.QuestionEditViewImpl;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.ui.widget.resource.dndview.vo.State;
import medizin.client.util.MathJaxs;
import medizin.shared.Status;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class ActivityQuestionEdit extends AbstractActivityWrapper implements QuestionEditView.Presenter, QuestionEditView.Delegate {

	private PlaceQuestionDetails questionPlace;
	protected QuestionEditView view;
	protected McAppRequestFactory requests;
	private PlaceController placeController;
	private Operation operation;
	protected QuestionProxy question;
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
		QuestionEditView questionEditView = new QuestionEditViewImpl(reciverMap, eventBus, userLoggedIn);
		questionEditView.setPresenter(this);
		this.view = questionEditView;
		view.setDelegate(this);
		
		view.setRewiewerPickerValues(Collections.<PersonProxy> emptyList());
		PersonRequest personRequestForReviewer = requests.personRequest();
		personRequestForReviewer.findPersonEntries(0, 200).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).to(new BMEReceiver<List<PersonProxy>>() {

			public void onSuccess(List<PersonProxy> response) {
				List<PersonProxy> values = new ArrayList<PersonProxy>();
				values.add(null);
				values.addAll(response);
				view.setRewiewerPickerValues(values);
			}
		});
		
		view.setAutorPickerValues(Collections.<PersonProxy> emptyList());
		PersonRequest personRequestForAuthor = personRequestForReviewer.append(requests.personRequest());
		personRequestForAuthor.findPersonEntries(0, 200).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).to(new BMEReceiver<List<PersonProxy>>() {

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

		view.setQuestionTypePickerValues(Collections.<QuestionTypeProxy> emptyList());
		QuestionTypeRequest questionTypeRequest = questionEventRequest.append(requests.questionTypeRequest());
		questionTypeRequest.findAllQuestionTypesForInstituteInSession().to(new BMEReceiver<List<QuestionTypeProxy>>() {

			public void onSuccess(List<QuestionTypeProxy> response) {
				List<QuestionTypeProxy> values = new ArrayList<QuestionTypeProxy>();
				values.add(null);
				values.addAll(response);
				view.setQuestionTypePickerValues(values);
			}
		});
		view.setMcsPickerValues(Collections.<McProxy> emptyList());
		
		McRequest mcRequest = questionTypeRequest.append(requests.mcRequest());
		mcRequest.findMcEntries(0, 50).with(medizin.client.ui.view.roo.McProxyRenderer.instance().getPaths()).to(new BMEReceiver<List<McProxy>>() {

			public void onSuccess(List<McProxy> response) {
				List<McProxy> values = new ArrayList<McProxy>();
				values.add(null);
				values.addAll(response);
				view.setMcsPickerValues(values);
			}
		});

		mcRequest.fire();
		widget.setWidget(questionEditView.asWidget());
		
		start2();
	}

	private void start2() {
		if (this.operation == PlaceQuestionDetails.Operation.EDIT) {
			Log.info("edit");
			requests.find(questionPlace.getProxyId()).with("previousVersion", "keywords", "questEvent", "comment", "questionType", "mcs", "rewiewer", "autor", "answers", "answers.autor", "answers.rewiewer", "questionResources").fire(new BMEReceiver<Object>() {

				@Override
				public void onSuccess(Object response) {
					if (response instanceof QuestionProxy) {
						Log.info(((QuestionProxy) response).getQuestionText());
						question = (QuestionProxy) response;
						init();
						MathJaxs.delayRenderLatexResult(RootPanel.getBodyElement());
					}
				}
			});
		} else {
			Log.info("neues Assement");
			init();
		}
	}

	private void init() {

		if (question == null) {
			view.setEditTitle(false);
		} else {
			Log.info(question.getQuestionText());
			view.setValue(question);
			view.setEditTitle(true);
		}
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void cancelClicked() {

		Set<QuestionResourceClient> clients = view.getQuestionResources();
		Set<String> paths = Sets.newHashSet();

		for (QuestionResourceClient client : clients) {

			Log.info("question resources client : " + Objects.toStringHelper(client).add("path", client.getPath()).add("state", client.getState()).toString());

			if (client.getState() == State.NEW) {
				paths.add(client.getPath());
			}
		}

		deleteUploadedFiles(paths);

		if (question != null && question.getId() != null) {
			cancelClickedGoto(question);
		} else {
			goTo(new PlaceQuestion(PlaceQuestion.PLACE_QUESTION));
		}
	}

	private void deleteUploadedFiles(Set<String> paths) {

		requests.questionResourceRequest().deleteFiles(paths).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				Log.info("Files area deleted");

			}
		});
	}

	/*
	 * @Override public void saveClicked(boolean generateNewQuestion) {
	 * this.save=true;
	 * 
	 * 
	 * //QuestionRequest req = requests.questionRequest();
	 * //editorDriver.flush();
	 * 
	 * if(generateNewQuestion) { // edit question
	 * 
	 * QuestionProxy questionProxy=standardizedRole.getCheckList();
	 * CheckListRequest checklsitRequest=requests.checkListRequest();
	 * checklistProxy=checklsitRequest.edit(checklistProxy);
	 * checklistProxy.setTitle
	 * (((RoleEditCheckListSubViewImpl)checkListView).title.getText
	 * 
	 * QuestionRequest req=requests.questionRequest(); QuestionProxy
	 * questionNew=req.edit(question);
	 * 
	 * //QuestionProxy questionNew = request.create(QuestionProxy.class);
	 * 
	 * questionNew.setQuestionType(view.getQuestionType().getValue());
	 * questionNew.setQuestionText(view.getRichtTextHTML());
	 * 
	 * Log.info("auther "+ view.getAutherListBox().getSelected().getName());
	 * questionNew.setAutor(view.getAutherListBox().getSelected());
	 * 
	 * //questionNew.setRewiewer(view.getReviewer().getValue());
	 * questionNew.setQuestionShortName(view.getShortName().getValue());
	 * if(view.getSubmitToReviewComitee().isChecked()) {
	 * questionNew.setRewiewer(null); } else
	 * if(view.getReviewerListBox().getSelected() != null) {
	 * questionNew.setRewiewer(view.getReviewerListBox().getSelected()); }else {
	 * ConfirmationDialogBox.showOkDialogBox(constants.warning(),
	 * constants.selectReviewerOrComitee()); return; }
	 * 
	 * questionNew.setQuestEvent(view.getQuestionEvent().getValue()); //
	 * questionNew.setDateAdded(new Date()); questionNew.setDateChanged(new
	 * Date()); questionNew.setMcs(view.getMCS().getValue());
	 * questionNew.setSubmitToReviewComitee
	 * (view.getSubmitToReviewComitee().getValue()); //
	 * questionNew.setStatus(Status.NEW);
	 * questionNew.setQuestionVersion(calculateSubversion
	 * (question2.getQuestionVersion())); CommentProxy
	 * comment=req.edit(questionNew.getComment());
	 * //comment.setComment(view.getQuestionComment().getHTML());
	 * comment.setComment(view.getQuestionComment().getValue());
	 * 
	 * questionNew.setComment(comment);
	 * 
	 * if(QuestionTypes.Imgkey.equals(questionNew.getQuestionType().getQuestionType
	 * ()) && view.getImageViewer() != null &&
	 * view.getImageViewer().getImageRelativeUrl() != null) {
	 * questionNew.setPicturePath(view.getImageViewer().getImageRelativeUrl());
	 * }else if(QuestionTypes.ShowInImage.equals(questionNew.getQuestionType().
	 * getQuestionType()) && view.getImageViewer() != null &&
	 * view.getImageViewer().getImageRelativeUrl() != null) {
	 * questionNew.setPicturePath(view.getImageViewer().getImageRelativeUrl());
	 * }else if((QuestionTypes.Textual.equals(questionNew.getQuestionType().
	 * getQuestionType()) ||
	 * QuestionTypes.Sort.equals(questionNew.getQuestionType
	 * ().getQuestionType())) && view.getQuestionResources().size() > 0) {
	 * QuestionResourceRequest questionResourceRequest =
	 * requests.questionResourceRequest(); Set<QuestionResourceProxy> proxies =
	 * Sets.newHashSet(); Log.info("proxies.size() " + proxies.size());
	 * 
	 * for (QuestionResourceClient questionResource :
	 * view.getQuestionResources()) {
	 * 
	 * if(questionResource.getState().equals(State.NEW) ||
	 * questionResource.getState().equals(State.EDITED)) { QuestionResourceProxy
	 * proxy = questionResourceRequest.create(QuestionResourceProxy.class);
	 * proxy.setPath(questionResource.getPath());
	 * proxy.setSequenceNumber(questionResource.getSequenceNumber());
	 * proxy.setType(questionResource.getType());
	 * proxy.setQuestion(questionNew); proxies.add(proxy); } }
	 * 
	 * questionResourceRequest.persistSet(proxies).fire(new
	 * BMEReceiver<Void>(reciverMap) {
	 * 
	 * @Override public void onSuccess(Void response) {
	 * Log.info("Added successfuly"); } }); }
	 * 
	 * CommentProxy comment=commentRequest.create(CommentProxy.class);
	 * comment.setComment(view.getQuestionComment().getHTML());
	 * 
	 * //questionNew.setComment(comment);
	 * 
	 * Iterator<AnswerProxy> iter = question2.getAnswers().iterator();
	 * Set<AnswerProxy> answers = new HashSet<AnswerProxy>(); while
	 * (iter.hasNext()) { AnswerProxy answer = (AnswerProxy) iter.next();
	 * AnswerProxy answerNew = request.create(AnswerProxy.class);
	 * answerNew.setAnswerText(answer.getAnswerText());
	 * answerNew.setAutor(answer.getAutor());
	 * Log.info(answer.getAutor().getName());
	 * answerNew.setRewiewer(answer.getRewiewer());
	 * answerNew.setIsPicture(false); answerNew.setPicturePath("kein Bild");
	 * answerNew.setDateAdded(new Date()); answerNew.setIsAnswerActive(true);
	 * answerNew.setIsAnswerAcceptedAdmin(false);
	 * answerNew.setIsAnswerAcceptedAutor(false);
	 * answerNew.setIsAnswerAcceptedReviewWahrer(false);
	 * answerNew.setRewiewer(question2.getRewiewer());
	 * answerNew.setValidity(answer.getValidity());
	 * answerNew.setQuestion(questionNew); answers.add(answerNew);
	 * 
	 * } Log.debug("answers copied"); questionNew.setAnswers(answers);
	 * questionNew.setAutor(question2.getAutor());
	 * if(question2.getComment()!=null){ CommentProxy commentProxy =
	 * request.create(CommentProxy.class);
	 * commentProxy.setComment(question2.getComment().getComment());
	 * questionNew.setComment(commentProxy); }
	 * 
	 * questionNew.setDateAdded(new Date()); questionNew.setDateChanged(new
	 * Date()); questionNew.setIsAcceptedAdmin(false);
	 * questionNew.setIsAcceptedRewiever(false); questionNew.setIsActive(false);
	 * questionNew.setKeywords(question2.getKeywords());
	 * questionNew.setMcs(question2.getMcs());
	 * questionNew.setPreviousVersion(question2);
	 * questionNew.setQuestionText(question2.getQuestionText());
	 * questionNew.setQuestionType(question2.getQuestionType());
	 * questionNew.setQuestionVersion(question2.getQuestionVersion());
	 * questionNew.setQuestEvent(question2.getQuestEvent());
	 * questionNew.setRewiewer(question2.getRewiewer());
	 * 
	 * 
	 * if(loggedUser.getIsAdmin()){
	 * questionNew.setAutor(question2.getRewiewer()); } else {
	 * questionNew.setAutor(loggedUser); }
	 * 
	 * 
	 * questionNew.setQuestionVersion(question2.getQuestionVersion()+1);
	 * 
	 * final QuestionProxy qpoxy = questionNew;
	 * req.generateNewVersion().using(questionNew).fire(new
	 * BMEReceiver<Void>(reciverMap) {
	 * 
	 * @Override public void onSuccess(Void response) {
	 * Log.info("PersonSucesfullSaved");
	 * 
	 * if (questionPlace.getFromPlace().equals("ACCEPT_QUESTION"))
	 * placeController.goTo(new
	 * PlaceQuestionDetails(question.stableId(),PlaceQuestionDetails
	 * .Operation.DETAILS, "ACCEPT_QUESTION")); else placeController.goTo(new
	 * PlaceQuestionDetails
	 * (question.stableId(),PlaceQuestionDetails.Operation.DETAILS));
	 * 
	 * // goTo(new PlaceQuestion(person.stableId())); }
	 * 
	 * public void onFailure(ServerFailure error){
	 * Log.error(error.getMessage()); }
	 * 
	 * @Override public void onConstraintViolation( Set<ConstraintViolation<?>>
	 * violations) { Iterator<ConstraintViolation<?>> iter =
	 * violations.iterator(); String message = ""; while(iter.hasNext()){
	 * ConstraintViolation<?> v = iter.next(); message += v.getPropertyPath() +
	 * " : " + v.getMessage() + "<br>"; }
	 * Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" +
	 * message);
	 * 
	 * ErrorPanel erorPanel = new ErrorPanel();
	 * erorPanel.setWarnMessage(message);
	 * 
	 * } });
	 * 
	 * } else{
	 * 
	 * // New question //editorDriver.edit(question, req); final QuestionRequest
	 * request=requests.questionRequest(); CommentRequest
	 * commentRequest=requests.commentRequest();
	 * 
	 * QuestionProxy questionNew = request.create(QuestionProxy.class);
	 * 
	 * questionNew.setQuestionType(view.getQuestionType().getValue());
	 * questionNew.setQuestionText(view.getRichtTextHTML()); Log.info("auther "+
	 * view.getAuther().getValue().getName());
	 * questionNew.setQuestionShortName(view.getShortName().getValue()); //
	 * questionNew.setAutor(view.getAuther().getValue());
	 * questionNew.setAutor(view.getAutherListBox().getSelected());
	 * Log.info("reviewer "+ view.getReviewer().getValue().getName());
	 * if(view.getSubmitToReviewComitee().isChecked()) {
	 * questionNew.setRewiewer(null);
	 * 
	 * } else if(view.getReviewerListBox().getSelected() != null) {
	 * questionNew.setRewiewer(view.getReviewerListBox().getSelected()); }else {
	 * ConfirmationDialogBox.showOkDialogBox(constants.warning(),
	 * constants.selectReviewerOrComitee()); return; }
	 * //questionNew.setRewiewer(view.getReviewerListBox().getSelected());
	 * //questionNew.setRewiewer(view.getReviewer().getValue());
	 * questionNew.setQuestEvent(view.getQuestionEvent().getValue());
	 * questionNew.setMcs(view.getMCS().getValue()); //
	 * questionNew.setDateChanged(new Date()); questionNew.setDateAdded(new
	 * Date());
	 * questionNew.setSubmitToReviewComitee(view.getSubmitToReviewComitee
	 * ().getValue()); //questionNew.setQuestionVersion(1.0);
	 * questionNew.setQuestionVersion(incrementVersion(question));
	 * questionNew.setPreviousVersion(question); CommentProxy
	 * comment=commentRequest.create(CommentProxy.class);
	 * //comment.setComment(view.getQuestionComment().getHTML());
	 * comment.setComment(view.getQuestionComment().getValue());
	 * questionNew.setStatus(Status.NEW); questionNew.setComment(comment);
	 * //questionNew.setQuestionResources(view.getQuestionResources());
	 * if(questionNew.getQuestionType() != null) {
	 * if(QuestionTypes.Imgkey.equals
	 * (questionNew.getQuestionType().getQuestionType()) &&
	 * view.getImageViewer() != null &&
	 * view.getImageViewer().getImageRelativeUrl() != null) {
	 * questionNew.setPicturePath(view.getImageViewer().getImageRelativeUrl());
	 * }else if(QuestionTypes.ShowInImage.equals(questionNew.getQuestionType().
	 * getQuestionType()) && view.getImageViewer() != null &&
	 * view.getImageViewer().getImageRelativeUrl() != null) {
	 * questionNew.setPicturePath(view.getImageViewer().getImageRelativeUrl());
	 * } }
	 * 
	 * 
	 * final QuestionProxy newQuestion=questionNew;
	 * 
	 * commentRequest.persist().using(comment).fire(new
	 * BMEReceiver<Void>(reciverMap) {
	 * 
	 * @Override public void onSuccess(Void response) {
	 * Log.info("PersonSucesfullSaved");
	 * 
	 * request.persist().using(newQuestion).fire(new
	 * BMEReceiver<Void>(reciverMap) {
	 * 
	 * @Override public void onSuccess(Void response) {
	 * Log.info("PersonSucesfullSaved"); // persist questionResources if
	 * (QuestionTypes
	 * .Textual.equals(newQuestion.getQuestionType().getQuestionType()) &&
	 * view.getQuestionResources().size() > 0) {
	 * 
	 * requests.find(newQuestion.stableId()).fire(new BMEReceiver<Object>() {
	 * 
	 * @Override public void onSuccess(Object response) { Log.info("Question id"
	 * + ((QuestionProxy)response).getId()); // persist questionResources final
	 * QuestionProxy qproxy = ((QuestionProxy)response); QuestionResourceRequest
	 * questionResourceRequest = requests.questionResourceRequest();
	 * Set<QuestionResourceProxy> proxies = new
	 * HashSet<QuestionResourceProxy>();
	 * 
	 * for (QuestionResourceClient questionResource :
	 * view.getQuestionResources()) { QuestionResourceProxy proxy =
	 * questionResourceRequest.create(QuestionResourceProxy.class);
	 * 
	 * proxy.setPath(questionResource.getPath());
	 * proxy.setSequenceNumber(questionResource.getSequenceNumber());
	 * proxy.setType(questionResource.getType()); proxy.setQuestion(qproxy);
	 * proxies.add(proxy); }
	 * 
	 * questionResourceRequest.persistSet(proxies).fire(new
	 * BMEReceiver<Void>(reciverMap) {
	 * 
	 * @Override public void onSuccess(Void response) { if
	 * (questionPlace.getFromPlace().equals("ACCEPT_QUESTION"))
	 * placeController.goTo(new PlaceQuestionDetails(newQuestion.stableId(),
	 * PlaceQuestionDetails.Operation.DETAILS, "ACCEPT_QUESTION")); else
	 * placeController.goTo(new PlaceQuestionDetails(newQuestion.stableId(),
	 * PlaceQuestionDetails.Operation.DETAILS)); }
	 * 
	 * @Override public void onConstraintViolation( Set<ConstraintViolation<?>>
	 * violations) { Log.error("constraint violation in Question Resourc");
	 * super.onConstraintViolation(violations); }
	 * 
	 * @Override public void onFailure(ServerFailure error) {
	 * Log.error("Failure in Question Resourc " + error);
	 * super.onFailure(error); }
	 * 
	 * 
	 * });
	 * 
	 * 
	 * } });
	 * 
	 * }else { if (questionPlace.getFromPlace().equals("ACCEPT_QUESTION"))
	 * placeController.goTo(new PlaceQuestionDetails(newQuestion.stableId(),
	 * PlaceQuestionDetails.Operation.DETAILS, "ACCEPT_QUESTION")); else
	 * placeController.goTo(new PlaceQuestionDetails(newQuestion.stableId(),
	 * PlaceQuestionDetails.Operation.DETAILS)); }
	 * 
	 * // goTo(new PlaceQuestion(person.stableId())); }
	 * 
	 * public void onFailure(ServerFailure error){
	 * Log.error(error.getMessage()); Log.error(error.getExceptionType());
	 * Log.error(error.getStackTraceString()); }
	 * 
	 * @Override public void onConstraintViolation( Set<ConstraintViolation<?>>
	 * violations) { Iterator<ConstraintViolation<?>> iter =
	 * violations.iterator(); String message = ""; while(iter.hasNext()){
	 * ConstraintViolation<?> v = iter.next(); message += v.getPropertyPath() +
	 * " : " + v.getMessage() + "<br>"; }
	 * Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" +
	 * message);
	 * 
	 * ErrorPanel erorPanel = new ErrorPanel();
	 * erorPanel.setWarnMessage(message);
	 * 
	 * }
	 * 
	 * @Override public void onViolation(Set<Violation> errors) {
	 * Iterator<Violation> iter = errors.iterator(); String message = "";
	 * while(iter.hasNext()){ message += iter.next().getMessage() + "<br>"; }
	 * Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" +
	 * message);
	 * 
	 * ErrorPanel erorPanel = new ErrorPanel();
	 * erorPanel.setWarnMessage(message);
	 * 
	 * 
	 * } });
	 * 
	 * // goTo(new PlaceQuestion(person.stableId())); }
	 * 
	 * public void onFailure(ServerFailure error){
	 * Log.error(error.getMessage()); }
	 * 
	 * @Override public void onViolation(Set<Violation> errors) {
	 * Iterator<Violation> iter = errors.iterator(); String message = "";
	 * while(iter.hasNext()){ message += iter.next().getMessage() + "<br>"; }
	 * Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" +
	 * message);
	 * 
	 * ErrorPanel erorPanel = new ErrorPanel();
	 * erorPanel.setWarnMessage(message);
	 * 
	 * 
	 * } });
	 * 
	 * Log.debug(view.getRichtTextHTML());
	 * question2.setQuestionText(view.getRichtTextHTML());
	 * Log.debug(question2.toString()); if(question2.getId()==null){
	 * question2.setDateAdded(new Date());
	 * question2.setIsAcceptedRewiever(false);
	 * question2.setIsAcceptedAdmin(false); question2.setQuestionVersion(1.0);
	 * if(loggedUser.getIsAdmin()){ question2.setAutor(question2.getRewiewer());
	 * } else { question2.setAutor(loggedUser); }
	 * 
	 * 
	 * } else{ question2.setDateChanged(new Date());
	 * question2.setIsAcceptedAdmin(false);
	 * question2.setQuestionVersion(calculateSubversion
	 * (question2.getQuestionVersion())); } editorDriver.flush()
	 * 
	 * }
	 * 
	 * }
	 * 
	 * private Double incrementVersion(QuestionProxy question) {
	 * 
	 * if(question == null || question.getQuestionVersion() == null) { return
	 * 1.0d; } Double currentVersion = question.getQuestionVersion(); return
	 * currentVersion + 1; }
	 * 
	 * private Double calculateSubversion(Double questionVersion) {
	 * 
	 * Double subversion = questionVersion%1; Double mainVersion =
	 * questionVersion-subversion; Log.info("Subversion basis: "+ subversion +
	 * " " + Math.round(subversion*10000)/10000.0);
	 * subversion=incrementSubversion(Math.round(subversion*10000)/10000.0,
	 * true);
	 * 
	 * return subversion+mainVersion; }
	 * 
	 * private Double incrementSubversion(Double subversion, boolean first) {
	 * Log.info(subversion.toString()); if(subversion*10 == 0.0) {
	 * subversion=1.0; return subversion/10; } else if(subversion*10 == 9.0 &&
	 * first) { subversion=(subversion*10)+1; return subversion/1000; } else
	 * if(subversion*10 == 9.0) { subversion=(subversion*10)+2; return
	 * subversion/10; } else if(subversion*10%1 == 0){
	 * subversion=(subversion*10)+1; return subversion/10; } else{
	 * Log.info("übergabe an Funktion" + (subversion*10-subversion*10%1)/10);
	 * Log.info("Returnwert" +
	 * ((Math.round(subversion*10000)/10000.0)*10)%1/10); return
	 * (subversion*10-subversion*10%1)/10 +
	 * incrementSubversion(Math.round(subversion*10%1*10000)/10000.0, false)/10;
	 * }
	 * 
	 * }
	 */

	@Override
	public void deleteSelectedQuestionResource(Long qestionResourceId) {
		requests.questionResourceRequest().removeSelectedQuestionResource(qestionResourceId).fire(new BMEReceiver<Void>(reciverMap) {

			@Override
			public void onSuccess(Void response) {
				Log.info("selected question resource deleted successfully");
			}
		});
	}

	@Override
	public void deleteMediaFileFromDisk(String path) {

		/*if (question != null) {*/
			final QuestionRequest questionRequest = requests.questionRequest();
			questionRequest.deleteMediaFileFromDisk(path).fire(new BMEReceiver<Boolean>(reciverMap) {

				@Override
				public void onSuccess(Boolean response) {
					Log.info("Media deleted " + response);
				}

			});
		/*} else {
			Log.error("Question is null");
		}*/

	}

	protected void showNewDisplay() {
		Log.info("Question Save Event Fired");
		this.eventBus.fireEvent(new QuestionSaveEvent());
	}

	// also overriden in subclass 
	protected void cancelClickedGoto(QuestionProxy questionProxy) {
		goTo(new PlaceQuestionDetails(questionProxy.stableId(), Operation.DETAILS));
	}
		
	@Override
	public boolean isAcceptQuestionView() {
		return false;
	}

	@Override
	public void placeChanged(Place place) {
		// updateSelection(event.getNewPlace());
		// TODO implement
	}

	@Override
	public final boolean isAdminOrReviewer() {
		
		// for admin 
		if(isAdminOrInstitutionalAdmin()) {
			return true;
		}
		// Reviewer
		if(isReviewer()) {
			return true;
		}
		return  false;
	}

	public final boolean isReviewer() {
		return question != null && question.getRewiewer() != null && question.getRewiewer().getId().equals(userLoggedIn.getId());
	}
	
	@Override
	public final boolean isAuthor() {
		if(question != null && userLoggedIn != null && question.getAutor() != null && question.getAutor().getId().equals(userLoggedIn.getId())) {
			return true;
		}
		return false;
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
			createNewQuestion(null,Status.NEW,false,false,true,gotoShowNewFunction);
		}else {
			// edit accepted question with major or minor version
			final Function<Boolean, Void> isMajorOrMinor = new Function<Boolean, Void>() {

				@Override
				public Void apply(Boolean withNewMajorVersion) {
					
					if(withNewMajorVersion == true) {
						createNewQuestion(question,Status.NEW,false,false,true,gotoShowNewFunction);
					}else {
						Status status; 
						boolean isAcceptedByReviewer = false;
						boolean isAcceptedByAdmin = false;
						boolean isAcceptedByAuthor = true; // as update from author
												
						final Function<EntityProxyId<?>, Void> gotoDetailsFunction = new Function<EntityProxyId<?>, Void>() {
							@Override
							public Void apply(EntityProxyId<?> stableId) {
								placeController.goTo(new PlaceQuestionDetails(stableId, PlaceQuestionDetails.Operation.DETAILS));
								return null;
							}
						};
						
						final Function<EntityProxyId<?>, Void> gotoFunction = new Function<EntityProxyId<?>, Void>() {
							@Override
							public Void apply(EntityProxyId<?> stableId) {
								placeController.goTo(new PlaceQuestion(PlaceQuestion.PLACE_QUESTION));
								return null;
							}
						};
						
						if(Status.NEW.equals(question.getStatus())) {
							// if current state of the question is new so status will remain as it is.
							status = Status.NEW;
							updateQuestion(status,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor,gotoShowNewFunction);
						}else if(Status.ACTIVE.equals(question.getStatus())) {
							// the current state of the question is active so new status with minor changes will be accepted reviewer
							status = Status.ACCEPTED_REVIEWER;
							isAcceptedByReviewer = true; 
							updateQuestion(status,isAcceptedByAdmin,isAcceptedByReviewer,isAcceptedByAuthor,gotoFunction);
						}
					}
					return null;
				}
			};
			view.comfirmQuestionChanges(isMajorOrMinor);
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
		
		view.setValuesForQuestion(question,commentProxy);
		
		question.setStatus(status);
		question.setIsAcceptedAdmin(isAcceptedByAdmin);
		question.setIsAcceptedAuthor(isAcceptedByAuthor);
		question.setIsAcceptedRewiever(isAcceptedByReviewer);
		question.setQuestionVersion(previousQuestionProxy != null ? previousQuestionProxy.getQuestionVersion()+1 : 0);
		question.setQuestionSubVersion(0);
		question.setDateAdded(new Date());
		question.setQuestionResources(questionResourceProxies);
		
		if(previousQuestionProxy != null) question.setPreviousVersion(previousQuestionProxy);
		
		for (QuestionResourceClient questionResource : view.getQuestionResources()) {
			QuestionResourceProxy proxy = questionResourceRequest.create(QuestionResourceProxy.class);
			proxy.setPath(questionResource.getPath());
			proxy.setSequenceNumber(questionResource.getSequenceNumber());
			proxy.setType(questionResource.getType());
			proxy.setQuestion(question);
			questionResourceProxies.add(proxy);
		}
		
		final QuestionProxy questionProxy2 = question;
		questionRequest.persistQuestion().using(question).fire(new BMEReceiver<Void>(reciverMap) {

			@Override
			public void onSuccess(Void response) {
				gotoFunction.apply(questionProxy2.stableId());	
			}
		});
	}
	
	protected final void updateQuestion(final Status status, final boolean isAcceptedByAdmin, final boolean isAcceptedByReviewer, final boolean isAcceptedByAuthor, final Function<EntityProxyId<?>, Void> gotoFunction) {
		
		final CommentRequest commentRequest = requests.commentRequest();
		final QuestionResourceRequest questionResourceRequest = requests.questionResourceRequest();
		final QuestionRequest questionRequest = commentRequest.append(questionResourceRequest).append(requests.questionRequest());
		
		QuestionProxy questionProxy = questionRequest.edit(this.question);
		CommentProxy commentProxy = commentRequest.edit(this.question.getComment());
		Set<QuestionResourceProxy> questionResourceProxies = Sets.newHashSet();
		
		view.setValuesForQuestion(questionProxy,commentProxy);
		
		questionProxy.setStatus(status);
		questionProxy.setIsAcceptedAdmin(isAcceptedByAdmin);
		questionProxy.setIsAcceptedAuthor(isAcceptedByAuthor);
		questionProxy.setIsAcceptedRewiever(isAcceptedByReviewer);
		questionProxy.setQuestionVersion(question.getQuestionVersion());
		questionProxy.setQuestionSubVersion(question.getQuestionSubVersion() + 1);
		questionProxy.setDateChanged(new Date());
		questionProxy.setQuestionResources(questionResourceProxies);
			
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
		
		final QuestionProxy questionProxy2 = questionProxy;
		questionRequest.persist().using(questionProxy).fire(new BMEReceiver<Void>(reciverMap) {
			@Override
			public void onSuccess(Void response) {
				gotoFunction.apply(questionProxy2.stableId());
			}
		});
	}

	// updated in ActivityAcceptQuestionEdit 
	@Override
	public void resendToReview() {}
}
