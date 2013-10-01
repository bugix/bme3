package medizin.client.activites;

import java.util.List;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceQuestiontypes;
import medizin.client.place.PlaceQuestiontypesDetails;
import medizin.client.place.PlaceUserDetails;
import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.request.QuestionTypeRequest;
import medizin.client.ui.view.QuestiontypesEditView;
import medizin.client.ui.view.QuestiontypesEditViewImpl;
import medizin.shared.QuestionTypes;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class ActivityQuestiontypesCreate extends AbstractActivityWrapper implements QuestiontypesEditView.Delegate {

	private PlaceQuestiontypesDetails questiontypePlace;

	private AcceptsOneWidget widget;
	private QuestiontypesEditView view;

	private PlaceUserDetails userPlace;

	private PlaceQuestiontypesDetails.Operation operation;

	private HandlerRegistration rangeChangeHandler;

	private QuestionTypeProxy questionType;

	private McAppRequestFactory requests;
	private PlaceController placeController;

	public BmeConstants constants = GWT.create(BmeConstants.class);
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


	/*@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}*/

	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		QuestiontypesEditView questionTypeDetailsView = new QuestiontypesEditViewImpl(reciverMap);

		this.widget = widget;
		this.view = questionTypeDetailsView;
        widget.setWidget(questionTypeDetailsView.asWidget());
        
		/*eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {

			}
		});*/

		requests.institutionRequest().findAllInstitutions().fire(new BMEReceiver<List<InstitutionProxy>>() {

			@Override
			public void onSuccess(List<InstitutionProxy> response) {

				view.getInstituteListBox().setAcceptableValues(response);

				requests.institutionRequest().myGetInstitutionToWorkWith().fire(new BMEReceiver<InstitutionProxy>() {

					@Override
					public void onSuccess(InstitutionProxy response) {

						view.getInstituteListBox().setValue(response);
					}
				});
			}
		});

		view.setDelegate(this);
		view.showFieldsForQuestionType(QuestionTypes.Textual);

		if(this.operation==PlaceQuestiontypesDetails.Operation.EDIT){

			Log.info("edit");			
			requests.find(questiontypePlace.getProxyId()).with("institution").fire(new BMEReceiver<Object>() {
/*
				public void onFailure(ServerFailure error){
					Log.error(error.getMessage());
				}*/
				@Override
				public void onSuccess(Object response) {
					if(response instanceof QuestionTypeProxy){
						questionType=(QuestionTypeProxy)response;
						initEdit(questionType);
						//System.out.println("ID : " + questionType.getId());
					}


				}
			    });
		}
		else{

			Log.info("neues Assement");
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

		Log.info("flush");

	}

	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void cancelClicked() {
		if(questionType.getId()!=null){
			goTo(new PlaceQuestiontypesDetails(questionType.stableId(),PlaceQuestiontypesDetails.Operation.DETAILS));
		}
		else {			
			goTo(new PlaceQuestiontypes(PlaceQuestiontypes.PLACE_QUESTIONTYPES));
		}
	}

	@Override
	public void saveClicked(final QuestionTypeProxy proxy) {
		/*requests.institutionRequest().myGetInstitutionToWorkWith().fire(new Receiver<InstitutionProxy>() {

			@Override
			public void onSuccess(InstitutionProxy response) {
			*/	QuestionTypeRequest questionTypeRequest = requests.questionTypeRequest();
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
				questionTypeProxy.setInstitution(view.getInstituteListBox().getValue());

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
					/*questionTypeProxy.setImageWidth(Integer.parseInt(view.getImageWidthTxtbox().getValue()));
					questionTypeProxy.setImageHeight(Integer.parseInt(view.getImageLengthTxtbox().getValue()));
					questionTypeProxy.setImageProportion(view.getImageProportionTxtbox().getValue());*/
					questionTypeProxy.setQuestionLength(Integer.parseInt(view.getQuestionLengthTxtbox().getValue()));
				}
				else if (selectedQuestionType.equals(QuestionTypes.ShowInImage))
				{
					questionTypeProxy.setQuestionLength(Integer.parseInt(view.getQuestionLengthTxtbox().getValue()));
					/*questionTypeProxy.setAnswerLength(Integer.parseInt(view.getAnswerLengthTxtbox().getValue()));					
					questionTypeProxy.setImageWidth(Integer.parseInt(view.getImageWidthTxtbox().getValue()));
					questionTypeProxy.setImageHeight(Integer.parseInt(view.getImageLengthTxtbox().getValue()));
					questionTypeProxy.setImageProportion(view.getImageProportionTxtbox().getValue());
					questionTypeProxy.setLinearPoint(view.getLinearPointChkBox().getValue());
					questionTypeProxy.setLinearPercentage(Double.parseDouble(view.getLinearPercentageTxtbox().getValue().isEmpty() == true ? "0" : view.getLinearPercentageTxtbox().getValue()));*/
				}
				else if (selectedQuestionType.equals(QuestionTypes.LongText))
				{
					questionTypeProxy.setQuestionLength(Integer.parseInt(view.getQuestionLengthTxtbox().getValue()));
					questionTypeProxy.setKeywordHighlight(view.getKeywordHighlightChkBox().getValue());
					questionTypeProxy.setRichText(view.getRichTextChkBox().getValue());
					questionTypeProxy.setMinLength(Integer.parseInt(view.getMinLengthTxtbox().getValue()));
					questionTypeProxy.setMaxLength(Integer.parseInt(view.getMaxLengthTxtbox().getValue()));
					questionTypeProxy.setMinWordCount(Integer.parseInt(view.getMinWordCountTxtbox().getValue()));
					questionTypeProxy.setMaxWordCount(Integer.parseInt(view.getMaxWordCountTxtbox().getValue()));
					questionTypeProxy.setQueHaveImage(view.getQueHaveImgChkBox().getValue());
					questionTypeProxy.setQueHaveSound(view.getQueHaveSoundChkBox().getValue());
					questionTypeProxy.setQueHaveVideo(view.getQueHaveVideoChkBox().getValue());
				}
				else if (selectedQuestionType.equals(QuestionTypes.Matrix))
				{
					questionTypeProxy.setAllowOneToOneAss(view.getOneToOneAssChkBox().getValue());
					//questionTypeProxy.setMaxLength(Integer.parseInt(view.getMaxLengthTxtbox().getValue()));
					questionTypeProxy.setQuestionLength(Integer.parseInt(view.getQuestionLengthTxtbox().getValue()));
					questionTypeProxy.setAnswerLength(Integer.parseInt(view.getAnswerLengthTxtbox().getValue()));
				}
				else if (selectedQuestionType.equals(QuestionTypes.MCQ))
				{
					questionTypeProxy.setQuestionLength(Integer.parseInt(view.getQuestionLengthTxtbox().getValue()));
					questionTypeProxy.setMultimediaType(view.getMultimediaTypeListBox().getValue());
					questionTypeProxy.setSelectionType(view.getSelectionTypeListBox().getValue());
					questionTypeProxy.setColumns(Integer.parseInt(view.getColumnTxtbox().getValue()));
					/*questionTypeProxy.setImageWidth(Integer.parseInt(view.getImageWidthTxtbox().getValue()));
					questionTypeProxy.setImageHeight(Integer.parseInt(view.getImageLengthTxtbox().getValue()));
					questionTypeProxy.setImageProportion(view.getImageProportionTxtbox().getValue());
					questionTypeProxy.setThumbWidth(Integer.parseInt(view.getThumbWidthTxtbox().getValue()));
					questionTypeProxy.setThumbHeight(Integer.parseInt(view.getThumbHeightTxtbox().getValue()));
					questionTypeProxy.setAllowZoomIn(view.getAllowZoomInChkBox().getValue());
					questionTypeProxy.setAllowZoomOut(view.getAllowZoomOutChkBox().getValue());*/
					questionTypeProxy.setMaxBytes(Integer.parseInt(view.getMaxBytesTxtbox().getValue()));
					questionTypeProxy.setRichText(view.getRichTextChkBox().getValue());
				}

				final QuestionTypeProxy finalQuestionTypeProxy = questionTypeProxy;

				questionTypeRequest.persist().using(questionTypeProxy).fire(new BMEReceiver<Void>(reciverMap) {

					public void onSuccess(Void response) {
						view.setNullValue(selectedQuestionType);
						placeController.goTo(new PlaceQuestiontypes(PlaceQuestiontypes.PLACE_QUESTIONTYPES));
						placeController.goTo(new PlaceQuestiontypesDetails(finalQuestionTypeProxy.stableId(),PlaceQuestiontypesDetails.Operation.DETAILS));
					}
				});
			/*}
		});*/


	}

	@Override
	public void placeChanged(Place place) {
		// TODO add place changed code here
		
	}





}