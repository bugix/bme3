package medizin.client.activites;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceQuestiontypes;
import medizin.client.place.PlaceQuestiontypesDetails;
import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.request.InstitutionRequest;
import medizin.client.request.QuestionTypeRequest;
import medizin.client.ui.view.QuestiontypesEditView;
import medizin.client.ui.view.QuestiontypesEditViewImpl;
import medizin.client.ui.widget.process.AppLoader;
import medizin.shared.QuestionTypes;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.collect.Lists;
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
	private PlaceQuestiontypesDetails.Operation operation;
	private HandlerRegistration rangeChangeHandler;
	private QuestionTypeProxy questionType;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	public BmeConstants constants = GWT.create(BmeConstants.class);

	@Inject
	public ActivityQuestiontypesCreate(PlaceQuestiontypesDetails place, McAppRequestFactory requests, PlaceController placeController, PlaceQuestiontypesDetails.Operation create) {
		super(place, requests, placeController);
		this.questiontypePlace = place;
        this.requests = requests;
        this.placeController = placeController;
		this.operation = create;
	}

	@Inject
	public ActivityQuestiontypesCreate(PlaceQuestiontypesDetails place, McAppRequestFactory requests, PlaceController placeController) {
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
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		QuestiontypesEditView questionTypeDetailsView = new QuestiontypesEditViewImpl(reciverMap);

		this.widget = widget;
		this.view = questionTypeDetailsView;
        widget.setWidget(questionTypeDetailsView.asWidget());
        
        /*AppLoader.setNoLoader();
		requests.institutionRequest().findAllInstitutions().fire(new BMEReceiver<List<InstitutionProxy>>() {

			@Override
			public void onSuccess(List<InstitutionProxy> response) {

				view.getInstituteListBox().setAcceptableValues(response);*/
				AppLoader.setNoLoader();
				InstitutionRequest institutionRequest = requests.institutionRequest();
				institutionRequest.myGetInstitutionToWorkWith().to(new BMEReceiver<InstitutionProxy>() {

					@Override
					public void onSuccess(InstitutionProxy response) {
						// only current institution
						view.getInstituteListBox().setAcceptableValues(Lists.newArrayList(response));
						view.getInstituteListBox().setValue(response);
					}
				});
			/*}
		});*/

		view.setDelegate(this);
		view.showFieldsForQuestionType(QuestionTypes.Textual);

		if(this.operation==PlaceQuestiontypesDetails.Operation.EDIT){

			Log.info("edit");			
			AppLoader.setNoLoader();
			QuestionTypeRequest questionTypeRequest = institutionRequest.append(requests.questionTypeRequest());
			questionTypeRequest.find(questiontypePlace.getProxyId()).with("institution").to(new BMEReceiver<Object>() {

				@Override
				public void onSuccess(Object response) {
					if(response instanceof QuestionTypeProxy){
						questionType=(QuestionTypeProxy)response;
						AppLoader.setCurrentLoader(view.getLoadingPopup());
						initEdit(questionType);
					}
				}
			});
			
			questionTypeRequest.fire();
		}
		else{
			institutionRequest.fire();
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

		AppLoader.setNoLoader();
		request.persist().using(questionType);

		Log.info("flush");

	}

	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void cancelClicked() {
		if(questionType.getId()!=null){
			goTo(new PlaceQuestiontypesDetails(questionType.stableId(),PlaceQuestiontypesDetails.Operation.DETAILS,questiontypePlace.getHeight()));
		}
		else {			
			goTo(new PlaceQuestiontypes(PlaceQuestiontypes.PLACE_QUESTIONTYPES, questiontypePlace.getHeight()));
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

				if (selectedQuestionType.equals(QuestionTypes.Textual))
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
					questionTypeProxy.setShowFilterDialog(view.getShowFilterDialogChkBox().getValue());
				}
				else if( selectedQuestionType.equals(QuestionTypes.Sort))
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
					questionTypeProxy.setQuestionLength(Integer.parseInt(view.getQuestionLengthTxtbox().getValue()));
					questionTypeProxy.setAnswerLength(Integer.parseInt(view.getAnswerLengthTxtbox().getValue()));
				}
				else if (selectedQuestionType.equals(QuestionTypes.MCQ))
				{
					questionTypeProxy.setQuestionLength(Integer.parseInt(view.getQuestionLengthTxtbox().getValue()));
					questionTypeProxy.setMultimediaType(view.getMultimediaTypeListBox().getValue());
					questionTypeProxy.setSelectionType(view.getSelectionTypeListBox().getValue());
					questionTypeProxy.setColumns(Integer.parseInt(view.getColumnTxtbox().getValue()));
					questionTypeProxy.setMaxBytes(Integer.parseInt(view.getMaxBytesTxtbox().getValue()));
					questionTypeProxy.setRichText(view.getRichTextChkBox().getValue());
				}
				else if (selectedQuestionType.equals(QuestionTypes.Drawing))
				{
					questionTypeProxy.setQuestionLength(Integer.parseInt(view.getQuestionLengthTxtbox().getValue()));
					questionTypeProxy.setQueHaveImage(view.getQueHaveImgChkBox().getValue());
					questionTypeProxy.setQueHaveSound(view.getQueHaveSoundChkBox().getValue());
					questionTypeProxy.setQueHaveVideo(view.getQueHaveVideoChkBox().getValue());
				}

				final QuestionTypeProxy finalQuestionTypeProxy = questionTypeProxy;
				AppLoader.setNoLoader();
				questionTypeRequest.persist().using(questionTypeProxy).fire(new BMEReceiver<Void>(reciverMap) {

					public void onSuccess(Void response) {
						view.setNullValue(selectedQuestionType);
						placeController.goTo(new PlaceQuestiontypes(PlaceQuestiontypes.PLACE_QUESTIONTYPES,questiontypePlace.getHeight(),finalQuestionTypeProxy.stableId()));
						placeController.goTo(new PlaceQuestiontypesDetails(finalQuestionTypeProxy.stableId(),PlaceQuestiontypesDetails.Operation.DETAILS,questiontypePlace.getHeight()));
					}
				});
			/*}
		});*/


	}

	@Override
	public void placeChanged(Place place) {
	}

}
