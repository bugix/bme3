package medizin.client.activites;

import java.util.Iterator;
import java.util.Set;

import medizin.client.ui.ErrorPanel;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.QuestiontypesEditView;
import medizin.client.ui.view.QuestiontypesEditViewImpl;
import medizin.client.place.PlaceQuestion;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.place.PlaceQuestiontypes;
import medizin.client.place.PlaceQuestiontypesDetails;
import medizin.client.place.PlaceUserDetails;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.request.QuestionTypeRequest;
import medizin.shared.QuestionTypes;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.Violation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class ActivityQuestiontypesCreate extends AbstractActivityWrapper implements QuestiontypesEditView.Presenter, QuestiontypesEditView.Delegate {

	private PlaceQuestiontypesDetails questiontypePlace;

	private AcceptsOneWidget widget;
	private QuestiontypesEditView view;
	
	private PlaceUserDetails userPlace;

	private PlaceQuestiontypesDetails.Operation operation;




	private HandlerRegistration rangeChangeHandler;
	
	private QuestionTypeProxy questionType;
	
	private McAppRequestFactory requests;
	private PlaceController placeController;

	//private RequestFactoryEditorDriver<QuestionTypeProxy, QuestiontypesEditViewImpl> editorDriver;

	@Inject
	public ActivityQuestiontypesCreate(PlaceQuestiontypesDetails place,
			McAppRequestFactory requests, PlaceController placeController, PlaceQuestiontypesDetails.Operation create) {
		super(place, requests, placeController);
		this.questiontypePlace = place;
        this.requests = requests;
        this.placeController = placeController;
		this.operation = create;
	}

	@Inject
	public ActivityQuestiontypesCreate(PlaceQuestiontypesDetails place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.questiontypePlace = place;
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
//		((SlidingPanel)widget).remove(view.asWidget());


	}

	
	@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}
	
	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		QuestiontypesEditView questionTypeDetailsView = new QuestiontypesEditViewImpl();
		
		questionTypeDetailsView.setPresenter(this);
		
		this.widget = widget;
		this.view = questionTypeDetailsView;
        widget.setWidget(questionTypeDetailsView.asWidget());
      //  editorDriver = view.createEditorDriver();
		//setTable(view.getTable());
        
		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				//updateSelection(event.getNewPlace());
				// TODO implement
			}
		});
		//init();
		
		view.setDelegate(this);
		view.disableField(QuestionTypes.Textual);
		
//		requests.find(questiontypePlace.getProxyId()).fire(new Receiver<Object>() {
//
//			public void onFailure(ServerFailure error){
//				Log.error(error.getMessage());
//			}
//			@Override
//			public void onSuccess(Object response) {
//				if(response instanceof QuestionTypeProxy){
//					Log.info(((QuestionTypeProxy) response).getQuestionTypeName());
//					init((QuestionTypeProxy) response);
//				}
//
//				
//			}
//		    });

/*
		//Log.warn(requests.getProxyId(eventPlace.getProxyId().toString()));
		
		// Inherit the view's key provider
		
		//view.setDelegate(this);
		//updateSelection(placeController.getWhere());

	}
	
//	protected void showDetails(QuestionTypesEventProxy questionEvent) {
//	//	Log.debug("QuestionTypesEvent Stable id: " + questionEvent.stableId() + " " +  PlaceInstitutionEvent.Operation.DETAILS); 
//	//	placeController.goTo(new PlaceInstitutionEvent(questionEvent.stableId(), PlaceInstitutionEvent.Operation.DETAILS));
//	}

	@Override
	public void goTo(Place place) {
		  placeController.goTo(place);
	}
	
//    protected Request<java.util.List<medizin.client.managed.request.QuestionTypesEventProxy>> createRangeRequest(Range range) {
//        return requests.questionEventRequest().findQuestionTypesEventsByInstitutionNonRoo(institution.getId(), range.getStart(), range.getLength());
//    }
//
//    protected void fireCountRequest(Receiver<java.lang.Long> callback) {
//    	requests.questionEventRequest().countQuestionTypesEventsNonRoo(institution.getId()).fire(callback);
//    }
    
	private void init(QuestionTypeProxy questionType) {

		this.questionType = questionType;
		Log.debug("Details für: "+questionType.getQuestionTypeName());
		view.setValue(questionType);
		
		
//		fireCountRequest(new Receiver<Long>() {
//			@Override
//			public void onSuccess(Long response) {
//				if (view == null) {
//					// This activity is dead
//					return;
//				}
//				Log.debug("Geholte Personen aus der Datenbank: " + response);
//				//view.getTable().setRowCount(response.intValue(), true);
//
//				setPersonDetails();
//			}
//		});
		
	}
	
//	private CellTable<QuestionTypesEventProxy> table;
//	
//	public CellTable<QuestionTypesEventProxy> getTable(){
//		return table;
//	}
//	public void  setTable(CellTable<QuestionTypesEventProxy> table){
//		this.table = table;
//	}





//	@Override
//	public void deleteClicked() {
//
//		requests.personRequest().remove().using(person).fire(new Receiver<Void>() {
//
//            public void onSuccess(Void ignore) {
//            	Log.debug("Sucessfull deleted");
//            	placeController.goTo(new PlaceUser("PlaceUser"));
//            	
//            }
//            @Override
//			public void onFailure(ServerFailure error) {
//					Log.warn(McAppConstant.ERROR_WHILE_DELETE + " in Institution:Event -" + error.getMessage());
//					if(error.getMessage().contains("ConstraintViolationException")){
//						Log.debug("Fehlen beim erstellen: Doppelter name");
//						mcAppFactory.getErrorPanel().setErrorMessage(McAppConstant.EVENT_IS_REFERENCED);
//					}
//				
//			}
//			@Override
//			public void onViolation(Set<Violation> errors) {
//				Iterator<Violation> iter = errors.iterator();
//				String message = "";
//				while(iter.hasNext()){
//					message += iter.next().getMessage() + "<br>";
//				}
//				Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);
//				
//				mcAppFactory.getErrorPanel().setErrorMessage(message);
//
//				
//			}
//            
//        });
//		
//	}



	@Override
	public void cancelClicked() {
		
		Log.debug("Edit QuestionTypestype cancelled");
		goTo(new PlaceQuestiontypes("PlaceQuestiontypes"));
	
	}

	@Override
	public void saveClicked (String questionTypeName, Boolean isWeil, Integer trueAnswers, Integer falseAnswers, Integer sumAnswers, Integer maxLetters) {
		QuestionTypeRequest request = requests
		.questionTypeRequest();
		
		questionType =	request.edit(questionType);
		
		// 
		questionType.setQuestionTypeName(questionTypeName);
		questionType.setIsWeil(isWeil);
		questionType.setTrueAnswers(trueAnswers);
		questionType.setFalseAnswers(falseAnswers);
		questionType.setSumAnswers(sumAnswers);
		questionType.setMaxLetters(maxLetters);
		//Every Time questionType is edited, Version is altered.
		Integer version = questionType.getVersion();
		version++;
		questionType.setVersion(version);
		
		Log.debug("�nderungen speichern geklickt");
		

		questionType.setFalseAnswers(falseAnswers);
		
		
		
		
		
		
		
		
		
		request.persist().using(questionType).fire(new Receiver<Void>() {

			public void onSuccess(Void ignore) {
				Log.debug("Sucessfull saved");
				goTo(new PlaceQuestiontypes("PlaceQuestiontypes"));
				//getLastPage();

			}

			@Override
			public void onFailure(ServerFailure error) {
				Log.warn(McAppConstant.ERROR_WHILE_CREATE + " in QuestionTypestype -"
						+ error.getMessage());
				if (error.getMessage().contains("ConstraintViolationException")) {
					Log.debug("Fehlen beim erstellen: Doppelter name");
					//TODO mcAppFactory.getErrorPanel().setErrorMessage(McAppConstant.CONTENT_NOT_UNIQUE);
				}

			}

			@Override
			public void onViolation(Set<Violation> errors) {
				Log.debug("Fehlen beim erstellen, volation: "
						+ errors.toString());
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while (iter.hasNext()) {
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(McAppConstant.ERROR_WHILE_CREATE_VIOLATION
						+ " in Institution -" + message);

				//TODO mcAppFactory.getErrorPanel().setErrorMessage(message);

			}

		});
		
		
	}
*/
		
		if(this.operation==PlaceQuestiontypesDetails.Operation.EDIT){
			Log.info("edit");
			
			
		requests.find(questiontypePlace.getProxyId()).fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}
			@Override
			public void onSuccess(Object response) {
				if(response instanceof QuestionTypeProxy){
					//Log.info(((QuestionTypeProxy) response).getQuestionTypeName());
					//init((PersonProxy) response);
					questionType=(QuestionTypeProxy)response;
					initEdit(questionType);
					System.out.println("ID : " + questionType.getId());
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
	
	private void initEdit(QuestionTypeProxy questionTypeProxy)
	{
		view.setValue(questionTypeProxy);
		init();
	}
	
	private void init() {
		
		QuestionTypeRequest request = requests.questionTypeRequest();
		
		if(questionType==null){
			QuestionTypeProxy question = request.create(QuestionTypeProxy.class);
			this.questionType=question;
			view.setEditTitle(false);
		}
		else {
			view.setEditTitle(true);
		}
		
		Log.info("edit");
	      
		
	       Log.info("persist");
	        request.persist().using(questionType);
		//editorDriver.edit(questionType, request);

		
		Log.info("flush");
		//editorDriver.flush();
//		this.question = question;
		//Log.debug("Create für: "+questionType.getQuestionTypeName());
//		view.setValue(person);
		
	}
	
	
//	private void init(QuestionTypeProxy question) {
//
//		this.question = question;
//		QuestionTypesRequest request = requests.questionRequest();
//		request.persist().using(question);
//		Log.info("edit");
//		editorDriver.edit(question, request);
//
//		Log.info("flush");
//		editorDriver.flush();
//		Log.debug("Edit für: "+question.getQuestionTypeName());
////		view.setValue(person);
//		
//	}




	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
		
	}

	@Override
	public void cancelClicked() {
		if(questionType.getId()!=null){
			goTo(new PlaceQuestiontypesDetails(questionType.stableId(),PlaceQuestiontypesDetails.Operation.DETAILS));
		}
		else {			
			goTo(new PlaceQuestiontypes("PlaceQuestiontypes!DELETED"));
		}
	}

	@Override
	public void saveClicked(final QuestionTypeProxy proxy) {
		/*editorDriver.flush().fire(new Receiver<Void>() {
			
          @Override
          public void onSuccess(Void response) {
        	  Log.info("PersonSucesfullSaved");
        	  
        		placeController.goTo(new PlaceQuestiontypesDetails(questionType.stableId(), PlaceQuestiontypesDetails.Operation.DETAILS));
          //	goTo(new PlaceQuestionTypes(person.stableId()));
          }
          
          public void onFailure(ServerFailure error){
        	  ErrorPanel errorPanel = new ErrorPanel();
        	  errorPanel.setErrorMessage(error.getMessage());
				Log.error(error.getMessage());
			}
          @Override
			public void onViolation(Set<Violation> errors) {
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while(iter.hasNext()){
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Fragetyp speichern -" + message);
				
	        	  ErrorPanel errorPanel = new ErrorPanel();
	        	  errorPanel.setWarnMessage(message);
				

				
			}
      }); */
		
		
		
		requests.institutionRequest().myGetInstitutionToWorkWith().fire(new Receiver<InstitutionProxy>() {

			@Override
			public void onSuccess(InstitutionProxy response) {
				QuestionTypeRequest questionTypeRequest = requests.questionTypeRequest();
				QuestionTypeProxy questionTypeProxy;
				
				if (proxy == null)
				{
					questionTypeProxy = questionTypeRequest.create(QuestionTypeProxy.class);					
				}
				else
				{
					questionTypeProxy = proxy;
					questionTypeProxy = questionTypeRequest.edit(questionTypeProxy);
				}
				
				
				
				final QuestionTypes selectedQuestionType = view.getQuestionTypeListBox().getValue();
				questionTypeProxy.setShortName(view.getShortNameTxtbox().getValue());
				questionTypeProxy.setLongName(view.getLongNameTxtbox().getValue());
				questionTypeProxy.setDescription(view.getDescriptionTxtbox().getValue());
				questionTypeProxy.setQuestionType(selectedQuestionType);
				questionTypeProxy.setInstitution(response);
				
				if (selectedQuestionType.equals(QuestionTypes.Textual) || selectedQuestionType.equals(QuestionTypes.Sort))
				{
					questionTypeProxy.setSumAnswer(Integer.parseInt(view.getSumAnswerTxtbox().getValue()));
					questionTypeProxy.setSumTrueAnswer(Integer.parseInt(view.getSumTrueAnswerTxtbox().getValue()));
					questionTypeProxy.setSumFalseAnswer(Integer.parseInt(view.getSumFalseAnswerTxtbox().getValue()));
					questionTypeProxy.setQuestionLength(Integer.parseInt(view.getQuestionLengthTxtbox().getValue()));
					questionTypeProxy.setAnswerLength(Integer.parseInt(view.getAnswerLengthTxtbox().getValue()));
					questionTypeProxy.setDiffBetAnswer(Double.parseDouble(view.getAnswerDiffTxtbox().getValue()));
					questionTypeProxy.setQueHaveImage(view.getQueHaveImgChkBox().getValue());
					questionTypeProxy.setQueHaveSound(view.getQueHaveSoundChkBox().getValue());
					questionTypeProxy.setQueHaveVideo(view.getQueHaveVideoChkBox().getValue());
				}
				else if (selectedQuestionType.equals(QuestionTypes.Imgkey))
				{
					questionTypeProxy.setKeywordCount(Integer.parseInt(view.getKeywordCountTxtbox().getValue()));
					questionTypeProxy.setShowAutocomplete(view.getShowAutoCompleteChkBox().getValue());
					questionTypeProxy.setIsDictionaryKeyword(view.getIsDictionaryKeywordChkBox().getValue());
					questionTypeProxy.setAllowTyping(view.getAllowTypingChkBox().getValue());
					questionTypeProxy.setMinAutoCompleteLetter(Integer.parseInt(view.getMinLetterForAutoCompTxtbox().getValue()));
					questionTypeProxy.setAcceptNonKeyword(view.getAcceptNonKeywordChkBox().getValue());
					questionTypeProxy.setAnswerLength(Integer.parseInt(view.getAnswerLengthTxtbox().getValue()));
					questionTypeProxy.setLengthShortAnswer(Integer.parseInt(view.getShortAnswerLengthTxtbox().getValue()));
					questionTypeProxy.setImageWidth(Integer.parseInt(view.getImageWidthTxtbox().getValue()));
					questionTypeProxy.setImageHeight(Integer.parseInt(view.getImageLengthTxtbox().getValue()));
					questionTypeProxy.setImageProportion(view.getImageProportionTxtbox().getValue());
				}
				else if (selectedQuestionType.equals(QuestionTypes.Area))
				{
					questionTypeProxy.setImageWidth(Integer.parseInt(view.getImageWidthTxtbox().getValue()));
					questionTypeProxy.setImageHeight(Integer.parseInt(view.getImageLengthTxtbox().getValue()));
					questionTypeProxy.setImageProportion(view.getImageProportionTxtbox().getValue());
					questionTypeProxy.setLinearPoint(view.getLinearPointChkBox().getValue());
					questionTypeProxy.setLinearPercentage(Double.parseDouble(view.getLinearPercentageTxtbox().getValue()));
				}
				else if (selectedQuestionType.equals(QuestionTypes.LongText))
				{
					questionTypeProxy.setKeywordHighlight(view.getKeywordHighlightChkBox().getValue());
					questionTypeProxy.setRichText(view.getRichTextChkBox().getValue());
					questionTypeProxy.setMinLength(Integer.parseInt(view.getMinLengthTxtbox().getValue()));
					questionTypeProxy.setMaxLength(Integer.parseInt(view.getMaxLengthTxtbox().getValue()));
					questionTypeProxy.setMinWordCount(Integer.parseInt(view.getMinWordCountTxtbox().getValue()));
					questionTypeProxy.setMaxWordCount(Integer.parseInt(view.getMaxWordCountTxtbox().getValue()));
				}
				else if (selectedQuestionType.equals(QuestionTypes.Matrix))
				{
					questionTypeProxy.setAllowOneToOneAss(view.getOneToOneAssChkBox().getValue());
					questionTypeProxy.setMaxLength(Integer.parseInt(view.getMaxLengthTxtbox().getValue()));
				}
				else if (selectedQuestionType.equals(QuestionTypes.MCQ))
				{
					questionTypeProxy.setMultimediaType(view.getMultimediaTypeListBox().getValue());
					questionTypeProxy.setSelectionType(view.getSelectionTypeListBox().getValue());
					questionTypeProxy.setColumns(Integer.parseInt(view.getColumnTxtbox().getValue()));
					questionTypeProxy.setImageWidth(Integer.parseInt(view.getImageWidthTxtbox().getValue()));
					questionTypeProxy.setImageHeight(Integer.parseInt(view.getImageLengthTxtbox().getValue()));
					questionTypeProxy.setImageProportion(view.getImageProportionTxtbox().getValue());
					questionTypeProxy.setThumbWidth(Integer.parseInt(view.getThumbWidthTxtbox().getValue()));
					questionTypeProxy.setThumbHeight(Integer.parseInt(view.getThumbHeightTxtbox().getValue()));
					questionTypeProxy.setAllowZoomIn(view.getAllowZoomInChkBox().getValue());
					questionTypeProxy.setAllowZoomOut(view.getAllowZoomOutChkBox().getValue());
					questionTypeProxy.setMaxBytes(Integer.parseInt(view.getMaxBytesTxtbox().getValue()));
					questionTypeProxy.setRichText(view.getRichTextChkBox().getValue());
				}
				
				final QuestionTypeProxy finalQuestionTypeProxy = questionTypeProxy;
				
				questionTypeRequest.persist().using(questionTypeProxy).fire(new Receiver<Void>() {

					public void onSuccess(Void response) {
						view.setNullValue(selectedQuestionType);
						//Window.alert("Record Inserted Successfully...");
						placeController.goTo(new PlaceQuestiontypesDetails(finalQuestionTypeProxy.stableId(),PlaceQuestiontypesDetails.Operation.DETAILS));
						
						//placeController.goTo(new PlaceQuestiontypes(finalQuestionTypeProxy.stableId()));
					}
				});
			}
		});
		
		
	}





}
