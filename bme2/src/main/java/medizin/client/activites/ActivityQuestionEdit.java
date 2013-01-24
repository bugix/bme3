package medizin.client.activites;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceQuestion;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.place.PlaceQuestionDetails.Operation;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.CommentProxy;
import medizin.client.proxy.McProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.request.CommentRequest;
import medizin.client.request.QuestionRequest;
import medizin.client.ui.ErrorPanel;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.question.QuestionEditView;
import medizin.client.ui.view.question.QuestionEditViewImpl;
import medizin.shared.Status;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.Violation;


public class ActivityQuestionEdit extends AbstractActivityWrapper implements 
QuestionEditView.Presenter, QuestionEditView.Delegate {

	private PlaceQuestionDetails questionPlace;

	private AcceptsOneWidget widget;
	private QuestionEditView view;
	private McAppRequestFactory requests;
	private PlaceController placeController;

	private Operation operation;

	protected QuestionProxy question;

	//private RequestFactoryEditorDriver<QuestionProxy, QuestionEditViewImpl> editorDriver;

	protected PersonProxy loggedUser;

	@Inject
	public ActivityQuestionEdit(PlaceQuestionDetails place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.questionPlace = place;
        this.requests = requests;
        this.placeController = placeController;
	}

	@Inject
	public ActivityQuestionEdit(PlaceQuestionDetails place,
			McAppRequestFactory requests, PlaceController placeController,
			Operation edit) {
		super(place, requests, placeController);
		this.questionPlace = place;
        this.requests = requests;
        this.placeController = placeController;
        this.operation=edit;
	}

	@Override
	public String mayStop() {
		if(!save)
			return McAppConstant.DO_NOT_SAVE_CHANGES;
		else
			return null;
	}

	@Override
	public void onCancel() {
		onStop();

	}

	@Override
	public void onStop() {
//		((SlidingPanel)widget).remove(view.asWidget());


	}
	
	@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}

	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		QuestionEditView questionEditView = new QuestionEditViewImpl();
		questionEditView.setName("hallo");
		questionEditView.setPresenter(this);
		this.widget = widget;
		this.view = questionEditView;
	//	editorDriver = view.createEditorDriver();
		view.setDelegate(this);
		

//        view.setRepeForPickerValues(Collections.<QuestionProxy>emptyList());
//        requests.questionRequest().findQuestionEntries(0, 50).with(medizin.client.ui.view.roo.QuestionProxyRenderer.instance().getPaths()).fire(new Receiver<List<QuestionProxy>>() {
//
//            public void onSuccess(List<QuestionProxy> response) {
//                List<QuestionProxy> values = new ArrayList<QuestionProxy>();
//                values.add(null);
//                values.addAll(response);
//                view.setRepeForPickerValues(values);
//            }
//        });

		view.setRewiewerPickerValues(Collections.<PersonProxy>emptyList());
        requests.personRequest().findPersonEntries(0, 200).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new Receiver<List<PersonProxy>>() {

            public void onSuccess(List<PersonProxy> response) {
                List<PersonProxy> values = new ArrayList<PersonProxy>();
                values.add(null);
                values.addAll(response);
                view.setRewiewerPickerValues(values);
            }
        });
        view.setAutorPickerValues(Collections.<PersonProxy>emptyList());
        requests.personRequest().findPersonEntries(0, 200).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new Receiver<List<PersonProxy>>() {

            public void onSuccess(List<PersonProxy> response) {
                List<PersonProxy> values = new ArrayList<PersonProxy>();
                values.add(null);
                values.addAll(response);
                view.setAutorPickerValues(values);
            }
        });

        view.setQuestEventPickerValues(Collections.<QuestionEventProxy>emptyList());
        requests.questionEventRequest().findQuestionEventEntries(0, 200).with(medizin.client.ui.view.roo.QuestionEventProxyRenderer.instance().getPaths()).fire(new Receiver<List<QuestionEventProxy>>() {

            public void onSuccess(List<QuestionEventProxy> response) {
                List<QuestionEventProxy> values = new ArrayList<QuestionEventProxy>();
                values.add(null);
                values.addAll(response);
                view.setQuestEventPickerValues(values);
            }
        });

        view.setQuestionTypePickerValues(Collections.<QuestionTypeProxy>emptyList());
        requests.questionTypeRequest().findQuestionTypeEntries(0, 50).with(medizin.client.ui.view.roo.QuestionTypeProxyRenderer.instance().getPaths()).fire(new Receiver<List<QuestionTypeProxy>>() {

            public void onSuccess(List<QuestionTypeProxy> response) {
                List<QuestionTypeProxy> values = new ArrayList<QuestionTypeProxy>();
                values.add(null);
                values.addAll(response);
                view.setQuestionTypePickerValues(values);
            }
        });
        view.setMcsPickerValues(Collections.<McProxy>emptyList());
        requests.mcRequest().findMcEntries(0, 50).with(medizin.client.ui.view.roo.McProxyRenderer.instance().getPaths()).fire(new Receiver<List<McProxy>>() {

            public void onSuccess(List<McProxy> response) {
                List<McProxy> values = new ArrayList<McProxy>();
                values.add(null);
                values.addAll(response);
                view.setMcsPickerValues(values);
            }
        });
       
		
//		view.initialiseDriver(requests);
        widget.setWidget(questionEditView.asWidget());
	//	setTable(view.getTable());
        
		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				
				//updateSelection(event.getNewPlace());
				// TODO implement
			}
		});
		//init();
		
		view.setDelegate(this);
		
		requests.personRequest().myGetLoggedPerson()
		.fire(new Receiver<PersonProxy>() {

			@Override
			public void onSuccess(PersonProxy response) {
				loggedUser = response;
				start2();

			}

			public void onFailure(ServerFailure error) {
				ErrorPanel erorPanel = new ErrorPanel();
				erorPanel.setErrorMessage(error.getMessage());
				Log.error(error.getMessage());
				onStop();
			}

			@Override
			public void onViolation(Set<Violation> errors) {
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while (iter.hasNext()) {
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION
						+ " in Antwort löschen -" + message);

				ErrorPanel erorPanel = new ErrorPanel();
				erorPanel.setErrorMessage(message);
				onStop();

			}

		});
		
	}
	
	private void start2(){
		if(this.operation==PlaceQuestionDetails.Operation.EDIT){
			Log.info("edit");
		requests.find(questionPlace.getProxyId()).with("previousVersion","keywords","questEvent","comment","questionType","mcs", "rewiewer", "autor", "answers", "answers.autor", "answers.rewiewer").fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}
			@Override
			public void onSuccess(Object response) {
				if(response instanceof QuestionProxy){
					Log.info(((QuestionProxy) response).getQuestionText());
					//init((PersonProxy) response);
					question=(QuestionProxy)response;
					init();
				}

				
			}
		    });
		}
		else{
			
			Log.info("neues Assement");
			//questionPlace.setProxyId(person.stableId());
			init();
		}
	}
	
	QuestionRequest request;
	CommentRequest commentRequest;
	QuestionProxy question2;

	private boolean save;
	private void init() {
		
		request = requests.questionRequest();
		commentRequest=requests.commentRequest();
		
		if(question==null){
			QuestionProxy question = request.create(QuestionProxy.class);
			this.question=question;
			view.setEditTitle(false);
		}
		else{
			Log.info(question.getQuestionText());
			//view.setRichPanelHTML(question.getQuestionText());
			view.setValue(question);
			view.setEditTitle(true);
		}
		
		question2 = request.edit(question);
		
		Log.info("edit");
	      
	       Log.info("persist");
	        request.persist().using(question2);
	//	editorDriver.edit(question2, request);
		


		Log.info("flush");
	//	editorDriver.flush();
//		this.question = question;
		Log.debug("Create für: "+question2.getQuestionText());
//		view.setValue(person);
		
	}
	
	
//	private void init(QuestionProxy question) {
//
//		this.question = question;
//		QuestionRequest request = requests.questionRequest();
//		request.persist().using(question);
//		Log.info("edit");
//		editorDriver.edit(question, request);
//
//		Log.info("flush");
//		editorDriver.flush();
//		Log.debug("Edit für: "+question.getQuestionText());
////		view.setValue(person);
//		
//	}




	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
		
	}

	@Override
	public void cancelClicked() {
		if(question.getId()!=null){
			goTo(new PlaceQuestionDetails(question.stableId()));
		}
		else {
			
			goTo(new PlaceQuestion("PlaceQuestion!DELETED"));
		}
		
	}

	@Override
	public void saveClicked(boolean generateNewQuestion) {
		this.save=true;
	
		
		//QuestionRequest req = requests.questionRequest();
		//editorDriver.flush();
		
		
		
		
		if(generateNewQuestion)
		{
			/*QuestionProxy questionProxy=standardizedRole.getCheckList();
			CheckListRequest checklsitRequest=requests.checkListRequest();
			checklistProxy=checklsitRequest.edit(checklistProxy);
			checklistProxy.setTitle(((RoleEditCheckListSubViewImpl)checkListView).title.getText
			*/		
			QuestionProxy questionNew=request.edit(question);
			
			//QuestionProxy questionNew = request.create(QuestionProxy.class);
			
			questionNew.setQuestionType(view.getQuestionType().getValue());
			questionNew.setQuestionText(view.getRichtTextHTML());
		
			Log.info("auther "+ view.getAutherListBox().getSelected().getName());
			questionNew.setAutor(view.getAutherListBox().getSelected());
			Log.info("reviewer "+ view.getReviewer().getValue().getName());
			//questionNew.setRewiewer(view.getReviewer().getValue());
			questionNew.setRewiewer(view.getReviewerListBox().getSelected());
			questionNew.setDateAdded(new Date());
			questionNew.setMcs(view.getMCS().getValue());
			questionNew.setSubmitToReviewComitee(view.getSubmitToReviewComitee().getValue());
			questionNew.setStatus(Status.NEW);
			
			CommentProxy comment=commentRequest.edit(questionNew.getComment());
			//comment.setComment(view.getQuestionComment().getHTML());
			comment.setComment(view.getQuestionComment().getValue());
			
			
			questionNew.setComment(comment);
			
			/*CommentProxy comment=commentRequest.create(CommentProxy.class);
			comment.setComment(view.getQuestionComment().getHTML());*/
			
			//questionNew.setComment(comment);
			
			
			
			/*Iterator<AnswerProxy> iter = question2.getAnswers().iterator();
			Set<AnswerProxy> answers = new HashSet<AnswerProxy>();
			while (iter.hasNext()) {
				AnswerProxy answer = (AnswerProxy) iter.next();
				AnswerProxy answerNew = request.create(AnswerProxy.class);
				answerNew.setAnswerText(answer.getAnswerText());
				answerNew.setAutor(answer.getAutor());
				Log.info(answer.getAutor().getName());
				answerNew.setRewiewer(answer.getRewiewer());
				answerNew.setIsPicture(false);
				answerNew.setPicturePath("kein Bild");
				answerNew.setDateAdded(new Date());
				answerNew.setIsAnswerActive(true);
				answerNew.setIsAnswerAcceptedAdmin(false);
				answerNew.setIsAnswerAcceptedAutor(false);
				answerNew.setIsAnswerAcceptedReviewWahrer(false);
				answerNew.setRewiewer(question2.getRewiewer());
				answerNew.setValidity(answer.getValidity());
				answerNew.setQuestion(questionNew);
				answers.add(answerNew);
				
			} 
			Log.debug("answers copied");
			questionNew.setAnswers(answers);
			questionNew.setAutor(question2.getAutor());
			if(question2.getComment()!=null){
				CommentProxy commentProxy = request.create(CommentProxy.class);
				commentProxy.setComment(question2.getComment().getComment());
				questionNew.setComment(commentProxy);
			}
			
			questionNew.setDateAdded(new Date());
			questionNew.setDateChanged(new Date());
			questionNew.setIsAcceptedAdmin(false);
			questionNew.setIsAcceptedRewiever(false);
			questionNew.setIsActive(false);
			questionNew.setKeywords(question2.getKeywords());
			questionNew.setMcs(question2.getMcs());
			questionNew.setPreviousVersion(question2);
			questionNew.setQuestionText(question2.getQuestionText());
			questionNew.setQuestionType(question2.getQuestionType());
			questionNew.setQuestionVersion(question2.getQuestionVersion());
			questionNew.setQuestEvent(question2.getQuestEvent());
			questionNew.setRewiewer(question2.getRewiewer());
	
			
			if(loggedUser.getIsAdmin()){
				questionNew.setAutor(question2.getRewiewer());
			} else {
				questionNew.setAutor(loggedUser);
			}
			
			
			questionNew.setQuestionVersion(question2.getQuestionVersion()+1);
	*/		
			request.generateNewVersion().using(questionNew).fire(new Receiver<Void>() {
				
		          @Override
		          public void onSuccess(Void response) {
		        	  Log.info("PersonSucesfullSaved");
		        	  
		        		placeController.goTo(new PlaceQuestionDetails(question.stableId(), PlaceQuestionDetails.Operation.DETAILS));
		          //	goTo(new PlaceQuestion(person.stableId()));
		          }
		          
		          public void onFailure(ServerFailure error){
						Log.error(error.getMessage());
					}
		          @Override
					public void onViolation(Set<Violation> errors) {
						Iterator<Violation> iter = errors.iterator();
						String message = "";
						while(iter.hasNext()){
							
							message += iter.next().getMessage() + "<br>";
						}
						Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);
						
						ErrorPanel erorPanel = new ErrorPanel();
			        	  erorPanel.setWarnMessage(message);
		
						
					}
		      }); 
			
		}
		else{
			//editorDriver.edit(question, req);
			final QuestionRequest request=requests.questionRequest();
			CommentRequest commentRequest=requests.commentRequest();
			
			QuestionProxy questionNew = request.create(QuestionProxy.class);
			
			questionNew.setQuestionType(view.getQuestionType().getValue());
			questionNew.setQuestionText(view.getRichtTextHTML());
/*			Log.info("auther "+ view.getAuther().getValue().getName());*/
			
		//	questionNew.setAutor(view.getAuther().getValue());
			questionNew.setAutor(view.getAutherListBox().getSelected());
			/*Log.info("reviewer "+ view.getReviewer().getValue().getName());*/
			questionNew.setRewiewer(view.getReviewerListBox().getSelected());
			//questionNew.setRewiewer(view.getReviewer().getValue());
			questionNew.setQuestEvent(view.getQuestionEvent().getValue());
			questionNew.setMcs(view.getMCS().getValue());
			questionNew.setDateChanged(new Date());
			questionNew.setSubmitToReviewComitee(view.getSubmitToReviewComitee().getValue());
			CommentProxy comment=commentRequest.create(CommentProxy.class);
			//comment.setComment(view.getQuestionComment().getHTML());
			comment.setComment(view.getQuestionComment().getValue());
			
			questionNew.setComment(comment);
			final QuestionProxy newQuestion=questionNew;
			
			commentRequest.persist().using(comment).fire(new Receiver<Void>() {
				
		          @Override
		          public void onSuccess(Void response) {
		        	  Log.info("PersonSucesfullSaved");
		        	  
		        	  request.persist().using(newQuestion).fire(new Receiver<Void>() {
		  				
		    	          @Override
		    	          public void onSuccess(Void response) {
		    	        	  Log.info("PersonSucesfullSaved");
		    	        	  
		    	        		placeController.goTo(new PlaceQuestionDetails(newQuestion.stableId(), PlaceQuestionDetails.Operation.DETAILS));
		    	          //	goTo(new PlaceQuestion(person.stableId()));
		    	          }
		    	          
		    	          public void onFailure(ServerFailure error){
		    					Log.error(error.getMessage());
		    					Log.error(error.getExceptionType());
		    					Log.error(error.getStackTraceString());
		    				}
		    	          @Override
		    				public void onViolation(Set<Violation> errors) {
		    					Iterator<Violation> iter = errors.iterator();
		    					String message = "";
		    					while(iter.hasNext()){
		    						message += iter.next().getMessage() + "<br>";
		    					}
		    					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);
		    					
		    					ErrorPanel erorPanel = new ErrorPanel();
		    			        	  erorPanel.setWarnMessage(message);
		    	
		    					
		    				}
		    	      }); 
		        		
		          //	goTo(new PlaceQuestion(person.stableId()));
		          }
		          
		          public void onFailure(ServerFailure error){
						Log.error(error.getMessage());
					}
		          @Override
					public void onViolation(Set<Violation> errors) {
						Iterator<Violation> iter = errors.iterator();
						String message = "";
						while(iter.hasNext()){
							message += iter.next().getMessage() + "<br>";
						}
						Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);
						
						ErrorPanel erorPanel = new ErrorPanel();
				        	  erorPanel.setWarnMessage(message);
		
						
					}
		      }); 
			
			/*Log.debug(view.getRichtTextHTML());
			question2.setQuestionText(view.getRichtTextHTML());
			Log.debug(question2.toString());
			if(question2.getId()==null){
				question2.setDateAdded(new Date());
				question2.setIsAcceptedRewiever(false);
				question2.setIsAcceptedAdmin(false);
				question2.setQuestionVersion(1.0);
				if(loggedUser.getIsAdmin()){
					question2.setAutor(question2.getRewiewer());
				} else {
					question2.setAutor(loggedUser);
				}
				

			}
			else{
				question2.setDateChanged(new Date());
				question2.setIsAcceptedAdmin(false);
				question2.setQuestionVersion(calculateSubversion(question2.getQuestionVersion()));
			}*/
			/*editorDriver.flush()*/
			
		}
		
	}

	private Double calculateSubversion(Double questionVersion) {
		
		Double subversion = questionVersion%1;
		Double mainVersion = questionVersion-subversion;
		Log.info("Subversion basis: "+ subversion + " " + Math.round(subversion*10000)/10000.0);
		subversion=incrementSubversion(Math.round(subversion*10000)/10000.0, true);
		
		return subversion+mainVersion;
	}

	private Double incrementSubversion(Double subversion, boolean first) {
		Log.info(subversion.toString());
		if(subversion*10 == 0.0)
		{
			subversion=1.0;
			return subversion/10;
		}
		else if(subversion*10 == 9.0 && first)
		{
			subversion=(subversion*10)+1;
			return subversion/1000;
		}
		else if(subversion*10 == 9.0)
		{
			subversion=(subversion*10)+2;
			return subversion/10;
		}
		else if(subversion*10%1 == 0){
			subversion=(subversion*10)+1;
			return subversion/10;
		}
		else{
			Log.info("übergabe an Funktion" + (subversion*10-subversion*10%1)/10);
			Log.info("Returnwert" + ((Math.round(subversion*10000)/10000.0)*10)%1/10);
			return (subversion*10-subversion*10%1)/10 + incrementSubversion(Math.round(subversion*10%1*10000)/10000.0, false)/10;
		}
		
	}

}
