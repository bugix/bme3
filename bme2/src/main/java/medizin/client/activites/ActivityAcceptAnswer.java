package medizin.client.activites;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAcceptAnswer;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.request.AnswerRequest;
import medizin.client.ui.DeclineEmailPopupDelagate;
import medizin.client.ui.ErrorPanel;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.AcceptAnswerSubView;
import medizin.client.ui.view.AcceptAnswerSubViewImpl;
import medizin.client.ui.view.AcceptAnswerView;
import medizin.client.ui.view.AcceptAnswerViewImpl;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.Violation;
/**
 * Activity for accepting answers.
 * @author masterthesis
 *
 */
public class ActivityAcceptAnswer extends AbstractActivityWrapper implements AcceptAnswerView.Delegate, AcceptAnswerSubView.Delegate, DeclineEmailPopupDelagate {

	private PlaceAcceptAnswer answerPlace;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private AcceptAnswerView view;
	private VerticalPanel questionPanel;
	//protected PersonProxy loggedUser;


	@Inject
	public ActivityAcceptAnswer(PlaceAcceptAnswer place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.answerPlace = place;
        this.requests = requests;
        this.placeController = placeController;
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


	}

	/*@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}*/
	
	@Override
	public void start2(AcceptsOneWidget panel, EventBus eventBus) {

		AcceptAnswerView acceptAnswerView = new AcceptAnswerViewImpl();

		acceptAnswerView.setDelegate(this);
		this.widget = panel;
		this.view = acceptAnswerView;
        widget.setWidget(acceptAnswerView.asWidget());
        
        questionPanel=view.getQuestionPanel();
       
        acceptAnswerView.setDelegate(this);
       
        
        init();
//    	requests.personRequest().getLoggedPerson()
//		.fire(new Receiver<PersonProxy>() {
//
//			@Override
//			public void onSuccess(PersonProxy response) {
//				loggedUser = response;
//				init();
//
//			}
//
//			public void onFailure(ServerFailure error) {
//				ErrorPanel erorPanel = new ErrorPanel();
//				erorPanel.setErrorMessage(error.getMessage());
//				Log.error(error.getMessage());
//				onStop();
//			}
//
//			@Override
//			public void onViolation(Set<Violation> errors) {
//				Iterator<Violation> iter = errors.iterator();
//				String message = "";
//				while (iter.hasNext()) {
//					message += iter.next().getMessage() + "<br>";
//				}
//				Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION
//						+ " in Antwort löschen -" + message);
//
//				ErrorPanel erorPanel = new ErrorPanel();
//				erorPanel.setErrorMessage(message);
//				onStop();
//
//			}
//
//		});
	}
	


	
	private void init() {
		

		questionPanel.clear();
		requests.questionRequest().findQuestionsAnswersNonAcceptedAdmin().with("answers").fire(new Receiver<List<QuestionProxy>>() {
			@Override
			public void onSuccess(List<QuestionProxy> response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				
				Iterator<QuestionProxy> iter = response.iterator();
				while (iter.hasNext()) {
					QuestionProxy questionProxy = (QuestionProxy) iter.next();
					AcceptAnswerSubView acceptAnswerSubView = new AcceptAnswerSubViewImpl();				
				    acceptAnswerSubView.getTable().setRowCount(questionProxy.getAnswers().size(), true);
				    acceptAnswerSubView.setDelegate(ActivityAcceptAnswer.this);
				    acceptAnswerSubView.setDelegatePopup(ActivityAcceptAnswer.this);
				    acceptAnswerSubView.setProxy(questionProxy);
				    questionPanel.add(acceptAnswerSubView);
				    
				}
				
			}
			
	          public void onFailure(ServerFailure error){
	        	  ErrorPanel erorPanel = new ErrorPanel();
	        	  erorPanel.setErrorMessage(error.getMessage());
					Log.error(error.getMessage());
				}
	          @Override
				public void onViolation(Set<Violation> errors) {
					Iterator<Violation> iter = errors.iterator();
					String message = "";
					while(iter.hasNext()){
						message += iter.next().getMessage() + "<br>";
					}
					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antwort hinzufügen -" + message);
					
		        	  ErrorPanel erorPanel = new ErrorPanel();
		        	  erorPanel.setErrorMessage(message);
					

					
				}


		});

	}
	@Override
	public void onRangeChanged(final QuestionProxy questionProxy,
			final AbstractHasData<AnswerProxy> table) {
		final Range range = table.getVisibleRange();
		
		requests.answerRequest().countAnswersNonAcceptedAdminByQuestion(questionProxy.getId()).with("question", "autor", "rewiewer").fire(new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				
				findAnswersEntriesNonAcceptedAdminByQuestion(questionProxy.getId(), range.getStart(), range.getLength(), table);
				
			}
			
	          public void onFailure(ServerFailure error){
	        	  ErrorPanel erorPanel = new ErrorPanel();
	        	  erorPanel.setErrorMessage(error.getMessage());
					Log.error(error.getMessage());
				}
	          @Override
				public void onViolation(Set<Violation> errors) {
					Iterator<Violation> iter = errors.iterator();
					String message = "";
					while(iter.hasNext()){
						message += iter.next().getMessage() + "<br>";
					}
					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antwort hinzufügen -" + message);
					
		        	  ErrorPanel erorPanel = new ErrorPanel();
		        	  erorPanel.setErrorMessage(message);
					

					
				}


		});
		
	}
	void findAnswersEntriesNonAcceptedAdminByQuestion(Long questionId, Integer start, Integer length, final AbstractHasData<AnswerProxy> table){
		requests.answerRequest().findAnswersEntriesNonAcceptedAdminByQuestion(questionId, start, length).with("rewiewer","autor").fire(new Receiver<List<AnswerProxy>>() {
			@Override
			public void onSuccess(List<AnswerProxy> response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				table.setRowData(response);
				
				
			}
			
	          public void onFailure(ServerFailure error){
	        	  ErrorPanel erorPanel = new ErrorPanel();
	        	  erorPanel.setErrorMessage(error.getMessage());
					Log.error(error.getMessage());
				}
	          @Override
				public void onViolation(Set<Violation> errors) {
					Iterator<Violation> iter = errors.iterator();
					String message = "";
					while(iter.hasNext()){
						message += iter.next().getMessage() + "<br>";
					}
					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antwort hinzufügen -" + message);
					
		        	  ErrorPanel erorPanel = new ErrorPanel();
		        	  erorPanel.setErrorMessage(message);
				
				}

		});
	}
	@Override
	public void acceptClicked(AnswerProxy answerProxy2) {
		

			AnswerRequest req = requests.answerRequest();
			AnswerProxy answerProxy =  req.edit((AnswerProxy)answerProxy2);
			if(userLoggedIn.getIsAdmin()){
				answerProxy.setIsAnswerAcceptedAdmin(true);
				answerProxy.setIsAnswerActive(true);
			} 
			if(answerProxy2.getRewiewer().getId() == userLoggedIn.getId()) {
				answerProxy.setIsAnswerAcceptedReviewWahrer(true);
			}
			if(answerProxy2.getAutor().getId() == userLoggedIn.getId())
			{
				answerProxy.setIsAnswerAcceptedAutor(true);
			}
			
			
			
			req.persist().using(answerProxy).fire(new Receiver<Void>(){

				@Override
				public void onSuccess(Void arg0) {
					init();
					
				}
		          public void onFailure(ServerFailure error){
		        	  ErrorPanel erorPanel = new ErrorPanel();
		        	  erorPanel.setErrorMessage(error.getMessage());
						Log.error(error.getMessage());
					}
		          @Override
					public void onViolation(Set<Violation> errors) {
						Iterator<Violation> iter = errors.iterator();
						String message = "";
						while(iter.hasNext()){
							message += iter.next().getMessage() + "<br>";
						}
						Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antwort löschen -" + message);
						
			        	  ErrorPanel erorPanel = new ErrorPanel();
			        	  erorPanel.setErrorMessage(message);
						

						
					}
			});
		
	}
	@Override
	public void acceptClicked(EntityProxy entityProxy) {
		if(entityProxy instanceof AnswerProxy){
			Log.debug("is AnswerProxy");
			AnswerRequest req = requests.answerRequest();
			AnswerProxy answerProxy =  req.edit((AnswerProxy)entityProxy);
			if(userLoggedIn.getIsAdmin()){
				answerProxy.setIsAnswerAcceptedAdmin(true);
				answerProxy.setIsAnswerActive(true);
			} 
			if(answerProxy.getRewiewer().getId() == userLoggedIn.getId()) {
				answerProxy.setIsAnswerAcceptedReviewWahrer(true);
			}
			if(answerProxy.getAutor().getId() == userLoggedIn.getId())
			{
				answerProxy.setIsAnswerAcceptedAutor(true);
			}
			
			
			
			req.persist().using(answerProxy).fire(new Receiver<Void>(){

				@Override
				public void onSuccess(Void arg0) {
					init();
					
				}
		          public void onFailure(ServerFailure error){
		        	  ErrorPanel erorPanel = new ErrorPanel();
		        	  erorPanel.setErrorMessage(error.getMessage());
						Log.error(error.getMessage());
					}
		          @Override
					public void onViolation(Set<Violation> errors) {
						Iterator<Violation> iter = errors.iterator();
						String message = "";
						while(iter.hasNext()){
							message += iter.next().getMessage() + "<br>";
						}
						Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antwort löschen -" + message);
						
			        	  ErrorPanel erorPanel = new ErrorPanel();
			        	  erorPanel.setErrorMessage(message);
						

						
					}
			});
		}
		
	}
	@Override
	public void rejectClicked(EntityProxy entityProxy, String message) {
		if(entityProxy instanceof AnswerProxy){
			Log.debug("is Answer");
			AnswerRequest req = requests.answerRequest();
			AnswerProxy answerProxy =  (AnswerProxy)entityProxy;
			
			req.remove().using(answerProxy).fire(new Receiver<Void>(){
	
				@Override
				public void onSuccess(Void arg0) {
					init();
					
				}
		          public void onFailure(ServerFailure error){
		        	  ErrorPanel erorPanel = new ErrorPanel();
		        	  erorPanel.setErrorMessage(error.getMessage());
						Log.error(error.getMessage());
					}
		          @Override
					public void onViolation(Set<Violation> errors) {
						Iterator<Violation> iter = errors.iterator();
						String message = "";
						while(iter.hasNext()){
							message += iter.next().getMessage() + "<br>";
						}
						Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antwort löschen -" + message);
						
			        	  ErrorPanel erorPanel = new ErrorPanel();
			        	  erorPanel.setWarnMessage(message);
						
	
						
					}
			});
		}
	}




}
