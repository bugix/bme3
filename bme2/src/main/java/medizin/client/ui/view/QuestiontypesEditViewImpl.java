package medizin.client.ui.view;

import static medizin.client.util.ClientUtility.toStringUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.dialogbox.receiver.ReceiverDialog;
import medizin.client.util.ClientUtility;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.SelectionType;
import medizin.shared.i18n.BmeConstants;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class QuestiontypesEditViewImpl extends Composite implements QuestiontypesEditView/*, Editor<QuestionTypeProxy>*/  {

	private static UserEditViewImplUiBinder uiBinder = GWT
			.create(UserEditViewImplUiBinder.class);

	interface UserEditViewImplUiBinder extends
			UiBinder<Widget, QuestiontypesEditViewImpl> {
	}
	
	/*interface EditorDriver extends RequestFactoryEditorDriver<QuestionTypeProxy, QuestiontypesEditViewImpl> {}
	// private final EditorDriver editorDriver = GWT.create(EditorDriver.class);
	
    @Override
    public RequestFactoryEditorDriver<QuestionTypeProxy, QuestiontypesEditViewImpl> createEditorDriver() {
        RequestFactoryEditorDriver<QuestionTypeProxy, QuestiontypesEditViewImpl> driver = GWT.create(EditorDriver.class);
        driver.initialize(this);
        return driver;
    }
*/
    public BmeConstants constants = GWT.create(BmeConstants.class);

	private Presenter presenter;


   /* @UiField
    TextBox questionTypeName;

    @UiField
    CheckBox isWeil;

    @UiField
    IntegerBox trueAnswers;

    @UiField
    IntegerBox falseAnswers;

    @UiField
    IntegerBox sumAnswers;

    @UiField
    IntegerBox maxLetters;*/
	
	@UiField
    Label shortNameLbl;
	
	@UiField
	TextBox shortNameTxtbox;
	
	@UiField
    Label longNameLbl;
	
	@UiField
	TextBox longNameTxtbox;
	
	@UiField
    Label descriptionLbl;
	
	@UiField
	TextArea descriptionTxtbox;	
	
	@UiField
    Label instituteLbl;
	
    @UiField (provided = true)
	ValueListBox<InstitutionProxy> instituteListBox = new ValueListBox<InstitutionProxy>(new AbstractRenderer<InstitutionProxy>() {

		@Override
		public String render(InstitutionProxy object) {
			if (object != null)
				return object.getInstitutionName();
			else
				return "";
		}
	});
    
	@UiField
    Label questionTypeLbl;
	
    @UiField (provided = true)
	ValueListBox<QuestionTypes> questionTypeListBox = new ValueListBox<QuestionTypes>(new AbstractRenderer<QuestionTypes>() {
		// Note: this is not an EnumRenderer bc. translations of language names would be futile.
		@Override
		public String render(QuestionTypes questionType) {
			if (questionType != null)
				return questionType.toString();
			
			return "";
		}
	});    
    
    @UiField
    Label sumAnswerLbl;
	
	@UiField
	TextBox sumAnswerTxtbox;	

	@UiField
	Label sumTrueAnswerLbl;
	 
	@UiField
	TextBox sumTrueAnswerTxtbox;
	
	@UiField
	Label sumFalseAnswerLbl;
	 
	@UiField
	TextBox sumFalseAnswerTxtbox;
	
	@UiField
	Label questionLengthLbl;
	 
	@UiField
	TextBox questionLengthTxtbox;
	
	@UiField
	Label answerLengthLbl;
	 
	@UiField
	TextBox answerLengthTxtbox;
	
	@UiField
	Label answerDiffLbl;
	 
	@UiField
	TextBox answerDiffTxtbox;
	
	@UiField
	Label queHaveImgLbl;
	
	@UiField
    CheckBox queHaveImgChkBox;
	
	@UiField
	Label queHaveVideoLbl;
	
	@UiField
    CheckBox queHaveVideoChkBox;
	
	@UiField
	Label queHaveSoundLbl;
	
	@UiField
    CheckBox queHaveSoundChkBox;
	
	@UiField
	Label keywordCountLbl;
	 
	@UiField
	TextBox keywordCountTxtbox;
	
	@UiField
	Label showAutoCompleteLbl;
	
	@UiField
    CheckBox showAutoCompleteChkBox;
	
	@UiField
	Label isDictionaryKeywordLbl;
	
	@UiField
    CheckBox isDictionaryKeywordChkBox;
	
	@UiField
	Label allowTypingLbl;
	
	@UiField
    CheckBox allowTypingChkBox;
	
	@UiField
	Label minLetterForAutoCompLbl;
	 
	@UiField
	TextBox minLetterForAutoCompTxtbox;
	
	@UiField
	Label acceptNonKeywordLbl;
	
	@UiField
    CheckBox acceptNonKeywordChkBox;
	
	@UiField
	Label shortAnswerLengthLbl;
	 
	@UiField
	TextBox shortAnswerLengthTxtbox;
	
	/*@UiField
	Label imageWidthLbl;
	 
	@UiField
	TextBox imageWidthTxtbox;
	
	@UiField
	Label imageLengthLbl;
	 
	@UiField
	TextBox imageLengthTxtbox;
	
	@UiField
	Label imageProportionLbl;
	 
	@UiField
	TextBox imageProportionTxtbox;*/
	
	/*@UiField
	Label linearPointLbl;
	 
	@UiField
	CheckBox linearPointChkBox;
		
	@UiField
	Label linearPercentageLbl;
	 
	@UiField
	TextBox linearPercentageTxtbox;*/
	
	@UiField
	Label keywordHighlightLbl;
	
	@UiField
    CheckBox keywordHighlightChkBox;
	
	@UiField
	Label richTextLbl;
	
	@UiField
    CheckBox richTextChkBox;
	
	@UiField
	Label minLengthLbl;
	 
	@UiField
	TextBox minLengthTxtbox;
	
	@UiField
	Label maxLengthLbl;
	 
	@UiField
	TextBox maxLengthTxtbox;
	
	@UiField
	Label minWordCountLbl;
	 
	@UiField
	TextBox minWordCountTxtbox;
	
	@UiField
	Label maxWordCountLbl;
	 
	@UiField
	TextBox maxWordCountTxtbox;
	
	@UiField
	Label oneToOneAssLbl;
	
	@UiField
    CheckBox oneToOneAssChkBox;
	
	@UiField
    Label multimediaTypeLbl;
	
    @UiField (provided = true)
	ValueListBox<MultimediaType> multimediaTypeListBox = new ValueListBox<MultimediaType>(new AbstractRenderer<MultimediaType>() {
		// Note: this is not an EnumRenderer bc. translations of language names would be futile.
		@Override
		public String render(MultimediaType multimediaType) {
			if (multimediaType != null)
				return multimediaType.toString();
			
			return "";
		}
	});
    
    @UiField
    Label selectionTypeLbl;
	
    @UiField (provided = true)
	ValueListBox<SelectionType> selectionTypeListBox = new ValueListBox<SelectionType>(new AbstractRenderer<SelectionType>() {
		// Note: this is not an EnumRenderer bc. translations of language names would be futile.
		@Override
		public String render(SelectionType selectionType) {
			if (selectionType != null)
				return selectionType.toString();
			
			return "";
		}
	});
    
    @UiField
	Label columnLbl;
	 
	@UiField
	TextBox columnTxtbox;
	
	/*@UiField
	Label widthLbl;
	 
	@UiField
	TextBox widthTxtbox;
	
	@UiField
	Label heightLbl;
	 
	@UiField
	TextBox heightTxtbox;*/
	
	/*@UiField
	Label thumbWidthLbl;
	 
	@UiField
	TextBox thumbWidthTxtbox;
	
	@UiField
	Label thumbHeightLbl;
	 
	@UiField
	TextBox thumbHeightTxtbox;*/
	
	/*@UiField
	Label propostionsLbl;
	 
	@UiField
	TextBox propostionsTxtbox;*/
	
	/*@UiField
	Label allowZoomOutLbl;
	
	@UiField
    CheckBox allowZoomOutChkBox;
	
	@UiField
	Label allowZoomInLbl;
	
	@UiField
    CheckBox allowZoomInChkBox;*/
	
	@UiField
	Label maxBytesLbl;
	 
	@UiField
	TextBox maxBytesTxtbox;

    @UiField
    IconButton cancel;

    @UiField
    IconButton save;

    private Delegate delegate;
	

	private QuestionTypeProxy proxy;
	
    @UiHandler("cancel")
    void onCancel(ClickEvent event) {
        delegate.cancelClicked();
    }

    @UiHandler("save")
    void onSave(ClickEvent event) {
    	if (validationOfFields(questionTypeListBox.getValue()))
    	{
    		delegate.saveClicked(proxy);
    	}
    }

    private static final ArrayList<String> textualSortList = Lists.newArrayList("sumAnswer","sumTrueAnswer","sumFalseAnswer","questionLength","answerLength","answerDiff","queHaveImg","queHaveVideo","queHaveSound");
	
	private static final ArrayList<String> imgKeyList = Lists.newArrayList("questionLength","keywordCount","showAutoComplete","isDictionaryKeyword","allowTyping","minLetterForAutoComp","answerLength","acceptNonKeyword","shortAnswerLength"/*,"imageWidth","imageLength","imageProportion"*/);

	private static final ArrayList<String> showInImgList = Lists.newArrayList("questionLength"/*,"answerLength","imageWidth","imageLength","imageProportion","linearPoint","linearPercentage"*/);
	
	private static final ArrayList<String> longTextList = Lists.newArrayList("questionLength","keywordHighlight","richText","minLength","maxLength","minWordCount","maxWordCount");
	
	private static final ArrayList<String> matrixList = Lists.newArrayList("questionLength","answerLength","oneToOneAss");
	
	//private static final ArrayList<String> mcqList = Lists.newArrayList("questionLength",/*"imageWidth","imageLength","imageProportion",*/"multimediaType","selectionType","column",/*"thumbWidth","thumbHeight",*/"richText",/*"allowZoomOut","allowZoomIn",*/"maxBytes");
	
	private static final ArrayList<String> mcqList = Lists.newArrayList("questionLength","multimediaType","selectionType","column","richText","maxBytes");
	
//    @UiField
//    SpanElement displayRenderer;

    public void setValue(QuestionTypeProxy proxy) {
       this.proxy = proxy;
       disableField(proxy.getQuestionType());
       DOM.setElementPropertyBoolean(questionTypeListBox.getElement(), "disabled", true);
       instituteListBox.setValue(proxy.getInstitution());
       questionTypeListBox.setValue(proxy.getQuestionType());
       shortNameTxtbox.setValue(proxy.getShortName());
       longNameTxtbox.setValue(proxy.getLongName());
       descriptionTxtbox.setValue(proxy.getDescription());
       
       if (proxy.getQuestionType().equals(QuestionTypes.Textual) || proxy.getQuestionType().equals(QuestionTypes.Sort))
       {
    	   sumAnswerTxtbox.setValue(toStringUtility(proxy.getSumAnswer()));
    	   sumTrueAnswerTxtbox.setValue(toStringUtility(proxy.getSumTrueAnswer()));
    	   sumFalseAnswerTxtbox.setValue(toStringUtility(proxy.getSumFalseAnswer()));
    	   questionLengthTxtbox.setValue(toStringUtility(proxy.getQuestionLength()));
    	   answerLengthTxtbox.setValue(toStringUtility(proxy.getAnswerLength()));
    	   answerDiffTxtbox.setValue(toStringUtility(proxy.getDiffBetAnswer()));
    	   queHaveImgChkBox.setValue(proxy.getQueHaveImage());
    	   queHaveVideoChkBox.setValue(proxy.getQueHaveVideo());
    	   queHaveSoundChkBox.setValue(proxy.getQueHaveSound());
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.Imgkey))
       {
    	   questionLengthTxtbox.setValue(toStringUtility(proxy.getQuestionLength()));
    	   keywordCountTxtbox.setValue(toStringUtility(proxy.getKeywordCount()));
    	   showAutoCompleteChkBox.setValue(proxy.getShowAutocomplete());
    	   isDictionaryKeywordChkBox.setValue(proxy.getIsDictionaryKeyword());
    	   allowTypingChkBox.setValue(proxy.getAllowTyping());
    	   minLetterForAutoCompTxtbox.setValue(toStringUtility(proxy.getMinAutoCompleteLetter()));
    	   answerLengthTxtbox.setValue(toStringUtility(proxy.getAnswerLength()));
    	   acceptNonKeywordChkBox.setValue(proxy.getAcceptNonKeyword());
    	   shortAnswerLengthTxtbox.setValue(toStringUtility(proxy.getLengthShortAnswer()));
    	   /*imageWidthTxtbox.setValue(toStringUtility(proxy.getImageWidth()));
    	   imageLengthTxtbox.setValue(toStringUtility(proxy.getImageHeight()));
    	   imageProportionTxtbox.setValue(toStringUtility(proxy.getImageProportion()));*/
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.ShowInImage))
       {
    	   questionLengthTxtbox.setValue(toStringUtility(proxy.getQuestionLength()));
    	   /*answerLengthTxtbox.setValue(toStringUtility(proxy.getAnswerLength()));    	  
    	   imageWidthTxtbox.setValue(toStringUtility(proxy.getImageWidth()));
    	   imageLengthTxtbox.setValue(toStringUtility(proxy.getImageHeight()));
    	   imageProportionTxtbox.setValue(toStringUtility(proxy.getImageProportion()));
    	   linearPointChkBox.setValue(proxy.getLinearPoint());
    	   linearPercentageTxtbox.setValue(toStringUtility(proxy.getLinearPercentage()));*/	    	   
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.LongText))
       {
    	   questionLengthTxtbox.setValue(toStringUtility(proxy.getQuestionLength()));
    	   keywordHighlightChkBox.setValue(proxy.getKeywordHighlight());
    	   richTextChkBox.setValue(proxy.getRichText());
    	   minLengthTxtbox.setValue(toStringUtility(proxy.getMinLength()));
    	   maxLengthTxtbox.setValue(toStringUtility(proxy.getMaxLength()));
    	   minWordCountTxtbox.setValue(toStringUtility(proxy.getMinWordCount()));
    	   maxWordCountTxtbox.setValue(toStringUtility(proxy.getMaxWordCount()));
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.Matrix))
       {
    	   questionLengthTxtbox.setValue(toStringUtility(proxy.getQuestionLength()));
    	   answerLengthTxtbox.setValue(toStringUtility(proxy.getAnswerLength()));
    	   //maxLengthTxtbox.setValue(toStringUtility(proxy.getMaxLength()));
    	   oneToOneAssChkBox.setValue(proxy.getAllowOneToOneAss());
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.MCQ))
       {
    	   questionLengthTxtbox.setValue(toStringUtility(proxy.getQuestionLength()));
    	  /* imageWidthTxtbox.setValue(toStringUtility(proxy.getImageWidth()));
    	   imageLengthTxtbox.setValue(toStringUtility(proxy.getImageHeight()));
    	   imageProportionTxtbox.setValue(toStringUtility(proxy.getImageProportion()));*/
    	   multimediaTypeListBox.setValue(proxy.getMultimediaType());
    	   selectionTypeListBox.setValue(proxy.getSelectionType());
    	   columnTxtbox.setValue(toStringUtility(proxy.getColumns()));
    	   richTextChkBox.setValue(proxy.getRichText());
    	   /*thumbWidthTxtbox.setValue(toStringUtility(proxy.getThumbWidth()));
    	   thumbHeightTxtbox.setValue(toStringUtility(proxy.getThumbHeight()));    	   
    	   allowZoomOutChkBox.setValue(proxy.getAllowZoomOut());
    	   allowZoomInChkBox.setValue(proxy.getAllowZoomIn());*/
    	   maxBytesTxtbox.setValue(toStringUtility(proxy.getMaxBytes()));
       }
       
    }

	public QuestiontypesEditViewImpl(Map<String, Widget> reciverMap) {
		
		initWidget(uiBinder.createAndBindUi(this));
		
		reciverMap.put("shortName",shortNameTxtbox );
		reciverMap.put("longName", longNameTxtbox);
		reciverMap.put("description", descriptionTxtbox);
		reciverMap.put("institution", instituteListBox);
		reciverMap.put("questionType", questionTypeListBox);
		reciverMap.put("sumAnswer",sumAnswerTxtbox );
		reciverMap.put("sumTrueAnswer", sumTrueAnswerTxtbox);
		reciverMap.put("sumFalseAnswer", sumFalseAnswerTxtbox);
		reciverMap.put("questionLength", questionLengthTxtbox);
		reciverMap.put("answerLength", answerLengthTxtbox);
		reciverMap.put("diffBetAnswer", answerDiffTxtbox);
		reciverMap.put("queHaveImage", queHaveImgChkBox);
		reciverMap.put("queHaveVideo", queHaveVideoChkBox);
		reciverMap.put("queHaveSound", queHaveSoundChkBox);
		reciverMap.put("keywordCount", keywordCountTxtbox);
		reciverMap.put("minAutoCompleteLetter", minLetterForAutoCompTxtbox);
		reciverMap.put("lengthShortAnswer",shortAnswerLengthTxtbox );
		/*reciverMap.put("imageWidth", imageWidthTxtbox);
		reciverMap.put("imageHeight", imageLengthTxtbox);
		reciverMap.put("imageProportion", imageProportionTxtbox);*/
		//reciverMap.put("linearPercentage", linearPercentageTxtbox);
		reciverMap.put("multimediaType", multimediaTypeListBox);
		reciverMap.put("selectionType", selectionTypeListBox);
		reciverMap.put("columns", columnTxtbox);
		/*reciverMap.put("thumbWidth",thumbWidthTxtbox );
		reciverMap.put("thumbHeight", thumbHeightTxtbox);*/
		reciverMap.put("maxBytes", maxBytesTxtbox);
		reciverMap.put("minLength", minLengthTxtbox);
		reciverMap.put("maxLength", maxLengthTxtbox);
		reciverMap.put("minWordCount", minWordCountTxtbox);
		reciverMap.put("maxWordCount", maxWordCountTxtbox);
				
		questionTypePanel.selectTab(0);
		questionTypePanel.getTabBar().setTabText(0, constants.manageQuestionType());
		
		save.setText(constants.save());
		cancel.setText(constants.cancel());
		
		multimediaTypeListBox.setValue(MultimediaType.Image);
		multimediaTypeListBox.setAcceptableValues(Arrays.asList(MultimediaType.values()));		
		
		selectionTypeListBox.setValue(SelectionType.Choose);
		selectionTypeListBox.setAcceptableValues(Arrays.asList(SelectionType.values()));		
		
		questionTypeListBox.setValue(QuestionTypes.Textual);
		questionTypeListBox.setAcceptableValues(Arrays.asList(QuestionTypes.values()));
		
		questionTypeListBox.addValueChangeHandler(new ValueChangeHandler<QuestionTypes>() {

			@Override
			public void onValueChange(ValueChangeEvent<QuestionTypes> event) {
				removeStyles();
				disableField(questionTypeListBox.getValue());
			}
		});
		
		DOM.setElementPropertyBoolean(instituteListBox.getElement(), "disabled", true);
		
		/*instituteListBox.setWidth("120px");
		questionTypeListBox.setWidth("120px");*/
		
		shortNameLbl.setText(constants.shortName());
		longNameLbl.setText(constants.longName());
		descriptionLbl.setText(constants.description());
		instituteLbl.setText(constants.institutionLbl());
		questionTypeLbl.setText(constants.questionType());
		sumAnswerLbl.setText(constants.sumAnswer());
		sumTrueAnswerLbl.setText(constants.sumTrueAnswer());
		sumFalseAnswerLbl.setText(constants.sumFalseAnswer());
		questionLengthLbl.setText(constants.questionLength());
		answerLengthLbl.setText(constants.answerLength());
		answerDiffLbl.setText(constants.diffAnswer());
		queHaveImgLbl.setText(constants.queHaveImg());
		queHaveVideoLbl.setText(constants.queHaveVideo());
		queHaveSoundLbl.setText(constants.queHaveSound());
		
		keywordCountLbl.setText(constants.countKeyword());
		showAutoCompleteLbl.setText(constants.showAutocomplete());
		isDictionaryKeywordLbl.setText(constants.isDictionaryKeyword());
		allowTypingLbl.setText(constants.allowTyping());
		minLetterForAutoCompLbl.setText(constants.minLetterAutoComplete());
		acceptNonKeywordLbl.setText(constants.acceptNonkeyword());
		shortAnswerLengthLbl.setText(constants.lengthShortAns());
		/*imageWidthLbl.setText(constants.imgWidth());
		imageLengthLbl.setText(constants.imgLength());
		imageProportionLbl.setText(constants.imgProportion());*/
		
		/*linearPointLbl.setText(constants.linearPoint());
		linearPercentageLbl.setText(constants.linearPercentage());*/
		
		keywordHighlightLbl.setText(constants.keywordHighlight());
		richTextLbl.setText(constants.richText());
		minLengthLbl.setText(constants.minLength());
		maxLengthLbl.setText(constants.maxLength());
		minWordCountLbl.setText(constants.minWordCount());
		maxWordCountLbl.setText(constants.maxWordCount());
		
		oneToOneAssLbl.setText(constants.oneToOneAss());
		
		multimediaTypeLbl.setText(constants.multimediaType());
		selectionTypeLbl.setText(constants.selectionType());
		columnLbl.setText(constants.column());
		/*widthLbl.setText(constants.width());
		heightLbl.setText(constants.height());*/
		/*thumbWidthLbl.setText(constants.thumbWidth());
		thumbHeightLbl.setText(constants.thumbHeight());*/
		//propostionsLbl.setText(constants.propostions());
		/*allowZoomOutLbl.setText(constants.allowZoomOut());
		allowZoomInLbl.setText(constants.allowZoomIn());*/
		maxBytesLbl.setText(constants.maxBytes());
		
		showAutoCompleteChkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue() == true)
					Document.get().getElementById("isDictionaryKeyword").getStyle().clearDisplay();
				else
					Document.get().getElementById("isDictionaryKeyword").getStyle().setDisplay(Display.NONE);
			}
		});
		
		/*linearPercentageTxtbox.setEnabled(false);
		
		linearPointChkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				
				linearPercentageTxtbox.setEnabled(event.getValue());			
			}
		});*/
	}
	
	public void showField(ArrayList<String> list)
	{
		for(String str : list)
		{
			Document.get().getElementById(str).getStyle().clearDisplay();
		}
	}
	
	public void disableQuestionField(ArrayList<String> list)
	{
		for(String str : list)
		{
			Document.get().getElementById(str).getStyle().setDisplay(Display.NONE);
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}
	
	@UiField
	TabPanel questionTypePanel;
	
	@UiField
	SpanElement title;

	
	/*@UiField
	Element  editTitle;
	@UiField
	Element  createTitle;
*/
	@Override
	public void setEditTitle(boolean edit) {
	      if (edit) {
	    	  title.setInnerText(constants.editQuestionType()); 
	    	  /*editTitle.getStyle().clearDisplay();
	            createTitle.getStyle().setDisplay(Display.NONE);*/
	        } else {
	        	title.setInnerText(constants.addQuestionType());
	            /*editTitle.getStyle().setDisplay(Display.NONE);
	            createTitle.getStyle().clearDisplay();*/
	        }
		
	}

	public void disableField(QuestionTypes questionTypes)
	{
		if (questionTypes.equals(QuestionTypes.Textual) || questionTypes.equals(QuestionTypes.Sort))
		{
			disableQuestionField(mcqList);
			disableQuestionField(imgKeyList);
			disableQuestionField(matrixList);
			disableQuestionField(longTextList);
			disableQuestionField(showInImgList);
			showField(textualSortList);				
		}
		else if (questionTypes.equals(QuestionTypes.Imgkey))
		{
			disableQuestionField(mcqList);
			disableQuestionField(textualSortList);
			disableQuestionField(matrixList);
			disableQuestionField(longTextList);
			disableQuestionField(showInImgList);
			showField(imgKeyList);
			Document.get().getElementById("isDictionaryKeyword").getStyle().setDisplay(Display.NONE);
		}
		else if (questionTypes.equals(QuestionTypes.ShowInImage))
		{
			disableQuestionField(mcqList);
			disableQuestionField(textualSortList);
			disableQuestionField(imgKeyList);
			disableQuestionField(matrixList);
			disableQuestionField(longTextList);
			showField(showInImgList);
		}
		else if (questionTypes.equals(QuestionTypes.LongText))
		{
			disableQuestionField(mcqList);
			disableQuestionField(textualSortList);
			disableQuestionField(imgKeyList);
			disableQuestionField(matrixList);
			disableQuestionField(showInImgList);
			showField(longTextList);
		}
		else if (questionTypes.equals(QuestionTypes.Matrix))
		{
			disableQuestionField(mcqList);
			disableQuestionField(textualSortList);
			disableQuestionField(imgKeyList);
			disableQuestionField(longTextList);
			disableQuestionField(showInImgList);
			showField(matrixList);
		} 
		else if (questionTypes.equals(QuestionTypes.MCQ))
		{
			disableQuestionField(textualSortList);
			disableQuestionField(imgKeyList);
			disableQuestionField(matrixList);
			disableQuestionField(longTextList);
			disableQuestionField(showInImgList);
			showField(mcqList);
		} 
		else
		{
			disableQuestionField(mcqList);
			disableQuestionField(textualSortList);
			disableQuestionField(imgKeyList);
			disableQuestionField(matrixList);
			disableQuestionField(longTextList);
			disableQuestionField(showInImgList);
		}
	}
	
	public void setNullValue(QuestionTypes questionTypes)
	{
		questionTypeListBox.setValue(QuestionTypes.Textual);
		shortNameTxtbox.setValue("");
		longNameTxtbox.setValue("");
		descriptionTxtbox.setValue("");
		
		if (questionTypes.equals(QuestionTypes.Textual) || questionTypes.equals(QuestionTypes.Sort))
	       {
	    	   sumAnswerTxtbox.setValue("");
	    	   sumTrueAnswerTxtbox.setValue("");
	    	   sumFalseAnswerTxtbox.setValue("");
	    	   questionLengthTxtbox.setValue("");
	    	   answerLengthTxtbox.setValue("");
	    	   answerDiffTxtbox.setValue("");
	    	   queHaveImgChkBox.setValue(false);
	    	   queHaveVideoChkBox.setValue(false);
	    	   queHaveSoundChkBox.setValue(false);
	       }
	       else if (questionTypes.equals(QuestionTypes.Imgkey))
	       {
	    	   keywordCountTxtbox.setValue("");
	    	   showAutoCompleteChkBox.setValue(false);
	    	   isDictionaryKeywordChkBox.setValue(false);
	    	   allowTypingChkBox.setValue(false);
	    	   minLetterForAutoCompTxtbox.setValue("");
	    	   questionLengthTxtbox.setValue("");
	    	   answerLengthTxtbox.setValue("");
	    	   acceptNonKeywordChkBox.setValue(false);
	    	   shortAnswerLengthTxtbox.setValue("");
	    	   /*imageWidthTxtbox.setValue("");
	    	   imageLengthTxtbox.setValue("");
	    	   imageProportionTxtbox.setValue("");*/
	    	   
	       }
	       else if (questionTypes.equals(QuestionTypes.ShowInImage))
	       {
	    	   questionLengthTxtbox.setValue("");
	    	   /*answerLengthTxtbox.setValue("");	    	   
	    	   imageWidthTxtbox.setValue("");
	    	   imageLengthTxtbox.setValue("");
	    	   imageProportionTxtbox.setValue("");
	    	   linearPointChkBox.setValue(false);
	    	   linearPercentageTxtbox.setValue("");*/	    	   
	       }
	       else if (questionTypes.equals(QuestionTypes.LongText))
	       {
	    	   questionLengthTxtbox.setValue("");
	    	   keywordHighlightChkBox.setValue(false);
	    	   richTextChkBox.setValue(false);
	    	   minLengthTxtbox.setValue("");
	    	   maxLengthTxtbox.setValue("");
	    	   minWordCountTxtbox.setValue("0");
	    	   maxWordCountTxtbox.setValue("0");
	       }
	       else if (questionTypes.equals(QuestionTypes.Matrix))
	       {
	    	  //maxLengthTxtbox.setValue("");
	    	   answerLengthTxtbox.setValue("");
	    	   questionLengthTxtbox.setValue("");
	    	   oneToOneAssChkBox.setValue(false);
	       }
	       else if (questionTypes.equals(QuestionTypes.MCQ))
	       {
	    	   questionLengthTxtbox.setValue("");
	    	   /*imageWidthTxtbox.setValue("");
	    	   imageLengthTxtbox.setValue("");
	    	   imageProportionTxtbox.setValue("");*/
	    	   multimediaTypeListBox.setValue(MultimediaType.Image);
	    	   selectionTypeListBox.setValue(SelectionType.Choose);
	    	   columnTxtbox.setValue("");
	    	   richTextChkBox.setValue(false);
	    	   /*thumbWidthTxtbox.setValue("");
	    	   thumbHeightTxtbox.setValue("");
	    	   allowZoomOutChkBox.setValue(false);
	    	   allowZoomInChkBox.setValue(false);*/
	    	   maxBytesTxtbox.setValue("");
	       }
	}

	public void setCreating(boolean creating) {
		if (creating) {
			title.setInnerText(constants.addQuestionType());
			//editTitle.getStyle().setDisplay(Display.NONE);
			// change{	//	createTitle.getStyle().clearDisplay();
			questionTypePanel.getTabBar().setTabText(0, "New Question Type");
		} else {
			title.setInnerText(constants.editQuestionType());
			//editTitle.getStyle().clearDisplay();
			// change{	createTitle.getStyle().setDisplay(Display.NONE);
			questionTypePanel.getTabBar().setTabText(0, "Edit Question Type");
		}
	}
	
	public TextBox getShortNameTxtbox() {
		return shortNameTxtbox;
	}

	public void setShortNameTxtbox(TextBox shortNameTxtbox) {
		this.shortNameTxtbox = shortNameTxtbox;
	}

	public TextBox getLongNameTxtbox() {
		return longNameTxtbox;
	}

	public void setLongNameTxtbox(TextBox longNameTxtbox) {
		this.longNameTxtbox = longNameTxtbox;
	}

	public TextArea getDescriptionTxtbox() {
		return descriptionTxtbox;
	}

	public void setDescriptionTxtbox(TextArea descriptionTxtbox) {
		this.descriptionTxtbox = descriptionTxtbox;
	}

	public ValueListBox<InstitutionProxy> getInstituteListBox() {
		return instituteListBox;
	}

	public void setInstituteListBox(ValueListBox<InstitutionProxy> instituteListBox) {
		this.instituteListBox = instituteListBox;
	}

	public ValueListBox<QuestionTypes> getQuestionTypeListBox() {
		return questionTypeListBox;
	}

	public void setQuestionTypeListBox(
			ValueListBox<QuestionTypes> questionTypeListBox) {
		this.questionTypeListBox = questionTypeListBox;
	}

	public TextBox getSumAnswerTxtbox() {
		return sumAnswerTxtbox;
	}

	public void setSumAnswerTxtbox(TextBox sumAnswerTxtbox) {
		this.sumAnswerTxtbox = sumAnswerTxtbox;
	}

	public TextBox getSumTrueAnswerTxtbox() {
		return sumTrueAnswerTxtbox;
	}

	public void setSumTrueAnswerTxtbox(TextBox sumTrueAnswerTxtbox) {
		this.sumTrueAnswerTxtbox = sumTrueAnswerTxtbox;
	}

	public TextBox getSumFalseAnswerTxtbox() {
		return sumFalseAnswerTxtbox;
	}

	public void setSumFalseAnswerTxtbox(TextBox sumFalseAnswerTxtbox) {
		this.sumFalseAnswerTxtbox = sumFalseAnswerTxtbox;
	}

	public TextBox getQuestionLengthTxtbox() {
		return questionLengthTxtbox;
	}

	public void setQuestionLengthTxtbox(TextBox questionLengthTxtbox) {
		this.questionLengthTxtbox = questionLengthTxtbox;
	}

	public TextBox getAnswerLengthTxtbox() {
		return answerLengthTxtbox;
	}

	public void setAnswerLengthTxtbox(TextBox answerLengthTxtbox) {
		this.answerLengthTxtbox = answerLengthTxtbox;
	}

	public TextBox getAnswerDiffTxtbox() {
		return answerDiffTxtbox;
	}

	public void setAnswerDiffTxtbox(TextBox answerDiffTxtbox) {
		this.answerDiffTxtbox = answerDiffTxtbox;
	}

	public CheckBox getQueHaveImgChkBox() {
		return queHaveImgChkBox;
	}

	public void setQueHaveImgChkBox(CheckBox queHaveImgChkBox) {
		this.queHaveImgChkBox = queHaveImgChkBox;
	}

	public CheckBox getQueHaveVideoChkBox() {
		return queHaveVideoChkBox;
	}

	public void setQueHaveVideoChkBox(CheckBox queHaveVideoChkBox) {
		this.queHaveVideoChkBox = queHaveVideoChkBox;
	}

	public CheckBox getQueHaveSoundChkBox() {
		return queHaveSoundChkBox;
	}

	public void setQueHaveSoundChkBox(CheckBox queHaveSoundChkBox) {
		this.queHaveSoundChkBox = queHaveSoundChkBox;
	}

	public TextBox getKeywordCountTxtbox() {
		return keywordCountTxtbox;
	}

	public void setKeywordCountTxtbox(TextBox keywordCountTxtbox) {
		this.keywordCountTxtbox = keywordCountTxtbox;
	}

	public CheckBox getShowAutoCompleteChkBox() {
		return showAutoCompleteChkBox;
	}

	public void setShowAutoCompleteChkBox(CheckBox showAutoCompleteChkBox) {
		this.showAutoCompleteChkBox = showAutoCompleteChkBox;
	}

	public CheckBox getIsDictionaryKeywordChkBox() {
		return isDictionaryKeywordChkBox;
	}

	public void setIsDictionaryKeywordChkBox(CheckBox isDictionaryKeywordChkBox) {
		this.isDictionaryKeywordChkBox = isDictionaryKeywordChkBox;
	}

	public CheckBox getAllowTypingChkBox() {
		return allowTypingChkBox;
	}

	public void setAllowTypingChkBox(CheckBox allowTypingChkBox) {
		this.allowTypingChkBox = allowTypingChkBox;
	}

	public TextBox getMinLetterForAutoCompTxtbox() {
		return minLetterForAutoCompTxtbox;
	}

	public void setMinLetterForAutoCompTxtbox(TextBox minLetterForAutoCompTxtbox) {
		this.minLetterForAutoCompTxtbox = minLetterForAutoCompTxtbox;
	}

	public CheckBox getAcceptNonKeywordChkBox() {
		return acceptNonKeywordChkBox;
	}

	public void setAcceptNonKeywordChkBox(CheckBox acceptNonKeywordChkBox) {
		this.acceptNonKeywordChkBox = acceptNonKeywordChkBox;
	}

	public TextBox getShortAnswerLengthTxtbox() {
		return shortAnswerLengthTxtbox;
	}

	public void setShortAnswerLengthTxtbox(TextBox shortAnswerLengthTxtbox) {
		this.shortAnswerLengthTxtbox = shortAnswerLengthTxtbox;
	}

	/*public TextBox getImageWidthTxtbox() {
		return imageWidthTxtbox;
	}

	public void setImageWidthTxtbox(TextBox imageWidthTxtbox) {
		this.imageWidthTxtbox = imageWidthTxtbox;
	}

	public TextBox getImageLengthTxtbox() {
		return imageLengthTxtbox;
	}

	public void setImageLengthTxtbox(TextBox imageLengthTxtbox) {
		this.imageLengthTxtbox = imageLengthTxtbox;
	}

	public TextBox getImageProportionTxtbox() {
		return imageProportionTxtbox;
	}

	public void setImageProportionTxtbox(TextBox imageProportionTxtbox) {
		this.imageProportionTxtbox = imageProportionTxtbox;
	}*/

	/*public CheckBox getLinearPointChkBox() {
		return linearPointChkBox;
	}

	public void setLinearPointChkBox(CheckBox linearPointChkBox) {
		this.linearPointChkBox = linearPointChkBox;
	}

	public TextBox getLinearPercentageTxtbox() {
		return linearPercentageTxtbox;
	}

	public void setLinearPercentageTxtbox(TextBox linearPercentageTxtbox) {
		this.linearPercentageTxtbox = linearPercentageTxtbox;
	}*/

	public CheckBox getKeywordHighlightChkBox() {
		return keywordHighlightChkBox;
	}

	public void setKeywordHighlightChkBox(CheckBox keywordHighlightChkBox) {
		this.keywordHighlightChkBox = keywordHighlightChkBox;
	}

	public CheckBox getRichTextChkBox() {
		return richTextChkBox;
	}

	public void setRichTextChkBox(CheckBox richTextChkBox) {
		this.richTextChkBox = richTextChkBox;
	}

	public TextBox getMinLengthTxtbox() {
		return minLengthTxtbox;
	}

	public void setMinLengthTxtbox(TextBox minLengthTxtbox) {
		this.minLengthTxtbox = minLengthTxtbox;
	}

	public TextBox getMaxLengthTxtbox() {
		return maxLengthTxtbox;
	}

	public void setMaxLengthTxtbox(TextBox maxLengthTxtbox) {
		this.maxLengthTxtbox = maxLengthTxtbox;
	}

	public TextBox getMinWordCountTxtbox() {
		return minWordCountTxtbox;
	}

	public void setMinWordCountTxtbox(TextBox minWordCountTxtbox) {
		this.minWordCountTxtbox = minWordCountTxtbox;
	}

	public TextBox getMaxWordCountTxtbox() {
		return maxWordCountTxtbox;
	}

	public void setMaxWordCountTxtbox(TextBox maxWordCountTxtbox) {
		this.maxWordCountTxtbox = maxWordCountTxtbox;
	}

	public CheckBox getOneToOneAssChkBox() {
		return oneToOneAssChkBox;
	}

	public void setOneToOneAssChkBox(CheckBox oneToOneAssChkBox) {
		this.oneToOneAssChkBox = oneToOneAssChkBox;
	}

	public ValueListBox<MultimediaType> getMultimediaTypeListBox() {
		return multimediaTypeListBox;
	}

	public void setMultimediaTypeListBox(
			ValueListBox<MultimediaType> multimediaTypeListBox) {
		this.multimediaTypeListBox = multimediaTypeListBox;
	}

	public ValueListBox<SelectionType> getSelectionTypeListBox() {
		return selectionTypeListBox;
	}

	public void setSelectionTypeListBox(
			ValueListBox<SelectionType> selectionTypeListBox) {
		this.selectionTypeListBox = selectionTypeListBox;
	}

	public TextBox getColumnTxtbox() {
		return columnTxtbox;
	}

	public void setColumnTxtbox(TextBox columnTxtbox) {
		this.columnTxtbox = columnTxtbox;
	}

	/*public TextBox getThumbWidthTxtbox() {
		return thumbWidthTxtbox;
	}

	public void setThumbWidthTxtbox(TextBox thumbWidthTxtbox) {
		this.thumbWidthTxtbox = thumbWidthTxtbox;
	}

	public TextBox getThumbHeightTxtbox() {
		return thumbHeightTxtbox;
	}

	public void setThumbHeightTxtbox(TextBox thumbHeightTxtbox) {
		this.thumbHeightTxtbox = thumbHeightTxtbox;
	}

	public CheckBox getAllowZoomOutChkBox() {
		return allowZoomOutChkBox;
	}

	public void setAllowZoomOutChkBox(CheckBox allowZoomOutChkBox) {
		this.allowZoomOutChkBox = allowZoomOutChkBox;
	}

	public CheckBox getAllowZoomInChkBox() {
		return allowZoomInChkBox;
	}

	public void setAllowZoomInChkBox(CheckBox allowZoomInChkBox) {
		this.allowZoomInChkBox = allowZoomInChkBox;
	}*/

	public TextBox getMaxBytesTxtbox() {
		return maxBytesTxtbox;
	}

	public void setMaxBytesTxtbox(TextBox maxBytesTxtbox) {
		this.maxBytesTxtbox = maxBytesTxtbox;
	}
	
	
	
	private boolean validationOfFields(QuestionTypes questionType)
	{
		removeStyles();
		
		ArrayList<String> errorMessage = Lists.newArrayList();
		
		boolean flag = true;
		//StringBuilder errorString = new StringBuilder();
		
		if (shortNameTxtbox.getText().isEmpty())
    	{	
			flag = false;
			//errorString.append(constants.shortName() + " " + constants.questionTypeErroMsg()).append("<br />");
			errorMessage.add(constants.shortName() + " " + constants.questionTypeErroMsg());
			shortNameTxtbox.addStyleName("higlight_onViolation");
    	}
		
		if (longNameTxtbox.getText().isEmpty())
		{
			flag = false;
			//errorString.append(constants.longName() + " " + constants.questionTypeErroMsg()).append("<br />");
			errorMessage.add(constants.longName() + " " + constants.questionTypeErroMsg());
			longNameTxtbox.addStyleName("higlight_onViolation");
		}
		
		if (descriptionTxtbox.getText().isEmpty())
		{
			flag = false;
			//errorString.append(constants.description() + " " + constants.questionTypeErroMsg()).append("<br />");
			errorMessage.add(constants.description() + " " + constants.questionTypeErroMsg());
			descriptionTxtbox.addStyleName("higlight_onViolation");
		}		
		
		switch (questionType)
		{
			case Sort:
			case Textual:
			{
				String msg = "";				
				if ((msg = checkTextWidgetForNumber(sumAnswerTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.sumAnswer() + " " + msg).append("<br />");
					errorMessage.add(constants.sumAnswer() + " " + msg);
					sumAnswerTxtbox.addStyleName("higlight_onViolation");
				}
				else if (Integer.parseInt(sumAnswerTxtbox.getValue()) < 2)
				{
					flag = false;
					//errorString.append(constants.sumOfAnsMsg()).append("<br />");
					errorMessage.add(constants.sumOfAnsMsg());
					sumAnswerTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(sumTrueAnswerTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.sumTrueAnswer() + " " + msg).append("<br />");
					errorMessage.add(constants.sumTrueAnswer() + " " + msg);
					sumTrueAnswerTxtbox.addStyleName("higlight_onViolation");
				}
				else if (Integer.parseInt(sumTrueAnswerTxtbox.getValue()) < 0)
				{
					flag = false;
					//errorString.append(constants.sumOfTrueAnsMsg()).append("<br />");
					errorMessage.add(constants.sumOfTrueAnsMsg());
					sumTrueAnswerTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(sumFalseAnswerTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.sumFalseAnswer() + " " + msg).append("<br />");
					errorMessage.add(constants.sumFalseAnswer() + " " + msg);
					sumFalseAnswerTxtbox.addStyleName("higlight_onViolation");
				}
				else if (Integer.parseInt(sumFalseAnswerTxtbox.getValue()) < 0)
				{
					flag = false;
					//errorString.append(constants.sumOfFalseAnsMsg()).append("<br />");
					errorMessage.add(constants.sumOfFalseAnsMsg());
					sumFalseAnswerTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(questionLengthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.questionLength() + " " + msg).append("<br />");
					errorMessage.add(constants.questionLength() + " " + msg);
					questionLengthTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(answerLengthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.answerLength() + " " + msg).append("<br />");
					errorMessage.add(constants.answerLength() + " " + msg);
					answerLengthTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForDouble(answerDiffTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.diffAnswer() + " " + msg).append("<br />");
					errorMessage.add(constants.diffAnswer() + " " + msg);
					answerDiffTxtbox.addStyleName("higlight_onViolation");
				}
				
				break;
			}
			
			case Imgkey:
			{
				String msg = "";				
				if ((msg = checkTextWidgetForNumber(keywordCountTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.countKeyword() + " " + msg).append("<br />");
					errorMessage.add(constants.countKeyword() + " " + msg);
					keywordCountTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(minLetterForAutoCompTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.minLetterAutoComplete() + " " + msg).append("<br />");
					errorMessage.add(constants.minLetterAutoComplete() + " " + msg);
					minLetterForAutoCompTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(questionLengthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.questionLength() + " " + msg).append("<br />");
					errorMessage.add(constants.questionLength() + " " + msg);
					questionLengthTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(answerLengthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.answerLength() + " " + msg).append("<br />");
					errorMessage.add(constants.answerLength() + " " + msg);
					answerLengthTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(shortAnswerLengthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.lengthShortAns() + " " + msg).append("<br />");
					errorMessage.add(constants.lengthShortAns() + " " + msg);
					shortAnswerLengthTxtbox.addStyleName("higlight_onViolation");
				}
				
				/*msg = "";
				if ((msg = checkTextWidgetForNumber(imageWidthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.imgWidth() + " " + msg).append("<br />");
					errorMessage.add(constants.imgWidth() + " " + msg);
					imageWidthTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(imageLengthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.imgLength() + " " + msg).append("<br />");
					errorMessage.add(constants.imgLength() + " " + msg);
					imageLengthTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if (imageProportionTxtbox.getText().isEmpty())
				{
					flag = false;
					//errorString.append(constants.imgProportion() + " " + constants.questionTypeErroMsg()).append("<br />");
					errorMessage.add(constants.imgProportion() + " " + constants.questionTypeErroMsg());
					imageProportionTxtbox.addStyleName("higlight_onViolation");
				}*/
				
				break;
			}
			
			case ShowInImage:
			{
				String msg = "";
				/*if ((msg = checkTextWidgetForNumber(imageWidthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.imgWidth() + " " + msg).append("<br />");
					errorMessage.add(constants.imgWidth() + " " + msg);
					imageWidthTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(answerLengthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.answerLength() + " " + msg).append("<br />");
					errorMessage.add(constants.answerLength() + " " + msg);
					answerLengthTxtbox.addStyleName("higlight_onViolation");
				}*/
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(questionLengthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.questionLength() + " " + msg).append("<br />");
					errorMessage.add(constants.questionLength() + " " + msg);
					questionLengthTxtbox.addStyleName("higlight_onViolation");
				}
				
				/*msg = "";
				if ((msg = checkTextWidgetForNumber(imageLengthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.imgLength() + " " + msg).append("<br />");
					errorMessage.add(constants.imgLength() + " " + msg);
					imageLengthTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if (imageProportionTxtbox.getText().isEmpty())
				{
					flag = false;
					//errorString.append(constants.imgProportion() + " " + constants.questionTypeErroMsg()).append("<br />");
					errorMessage.add(constants.imgProportion() + " " + constants.questionTypeErroMsg());
					imageProportionTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if (linearPointChkBox.getValue() == true && (msg = checkTextWidgetForNumber(linearPercentageTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.imgProportion() + " " + msg).append("<br />");
					errorMessage.add(constants.imgProportion() + " " + msg);
					linearPercentageTxtbox.addStyleName("higlight_onViolation");
				}*/
				
				break;
			}
			
			case LongText:
			{
				String msg = "";
				if ((msg = checkTextWidgetForNumber(minLengthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.minLength() + " " + msg).append("<br />");
					errorMessage.add(constants.minLength() + " " + msg);
					minLengthTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(questionLengthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.questionLength() + " " + msg).append("<br />");
					errorMessage.add(constants.questionLength() + " " + msg);
					questionLengthTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(maxLengthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.maxLength() + " " + msg).append("<br />");
					errorMessage.add(constants.maxLength() + " " + msg);
					maxLengthTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(minWordCountTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.minWordCount() + " " + msg).append("<br />");
					errorMessage.add(constants.minWordCount() + " " + msg);
					minWordCountTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(maxWordCountTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.maxWordCount() + " " + msg).append("<br />");
					errorMessage.add(constants.maxWordCount() + " " + msg);
					maxWordCountTxtbox.addStyleName("higlight_onViolation");
				}
				
				break;
			}
			
			case Matrix:
			{			
				/*String msg = "";
				if ((msg = checkTextWidgetForNumber(maxLengthTxtbox)) != "")
				{
					flag = false;
					errorString.append(constants.maxLength() + " " + msg).append("<br />");
					maxLengthTxtbox.addStyleName("higlight_onViolation");
				}*/
				String msg = "";
				if ((msg = checkTextWidgetForNumber(questionLengthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.questionLength() + " " + msg).append("<br />");
					errorMessage.add(constants.questionLength() + " " + msg);
					questionLengthTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(answerLengthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.answerLength() + " " + msg).append("<br />");
					errorMessage.add(constants.answerLength() + " " + msg);
					answerLengthTxtbox.addStyleName("higlight_onViolation");
				}
				break;
			}
			
			case MCQ:
			{
				String msg = "";
				/*if ((msg = checkTextWidgetForNumber(imageWidthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.imgWidth() + " " + msg).append("<br />");
					errorMessage.add(constants.imgWidth() + " " + msg);
					imageWidthTxtbox.addStyleName("higlight_onViolation");
				}*/
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(questionLengthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.questionLength() + " " + msg).append("<br />");
					errorMessage.add(constants.questionLength() + " " + msg);
					questionLengthTxtbox.addStyleName("higlight_onViolation");
				}
				
				/*msg = "";
				if ((msg = checkTextWidgetForNumber(imageLengthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.imgLength() + " " + msg).append("<br />");
					errorMessage.add(constants.imgLength() + " " + msg);
					imageLengthTxtbox.addStyleName("higlight_onViolation");
				}*/
				
				/*msg = "";
				if (imageProportionTxtbox.getText().isEmpty())
				{
					flag = false;
					//errorString.append(constants.imgProportion() + " " + constants.questionTypeErroMsg()).append("<br />");
					errorMessage.add(constants.imgProportion() + " " + constants.questionTypeErroMsg());
					imageProportionTxtbox.addStyleName("higlight_onViolation");
				}*/
				
				if (multimediaTypeListBox.getValue().equals(null))
				{
					flag = false;
					//errorString.append(constants.multimediaType() + " " + constants.questionTypeErroMsg()).append("<br />");
					errorMessage.add(constants.multimediaType() + " " + constants.questionTypeErroMsg());
					multimediaTypeListBox.addStyleName("higlight_onViolation");
				}
				
				if (selectionTypeListBox.getValue().equals(null))
				{
					flag = false;
					//errorString.append(constants.selectionType() + " " + constants.questionTypeErroMsg()).append("<br />");
					errorMessage.add(constants.selectionType() + " " + constants.questionTypeErroMsg());
					selectionTypeListBox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(columnTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.column() + " " + msg).append("<br />");
					errorMessage.add(constants.column() + " " + msg);
					columnTxtbox.addStyleName("higlight_onViolation");
				}
				
				/*msg = "";
				if ((msg = checkTextWidgetForNumber(thumbWidthTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.thumbWidth() + " " + msg).append("<br />");
					errorMessage.add(constants.thumbWidth() + " " + msg);
					thumbWidthTxtbox.addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(thumbHeightTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.thumbHeight() + " " + msg).append("<br />");
					errorMessage.add(constants.thumbHeight() + " " + msg);
					thumbHeightTxtbox.addStyleName("higlight_onViolation");
				}*/

				msg = "";
				if ((msg = checkTextWidgetForNumber(maxBytesTxtbox)) != "")
				{
					flag = false;
					//errorString.append(constants.maxBytes() + " " + msg).append("<br />");
					errorMessage.add(constants.maxBytes() + " " + msg);
					maxBytesTxtbox.addStyleName("higlight_onViolation");
				}
				break;
			}
			
			default:
				break;
		}
		
		if(flag == false) {
			ReceiverDialog.showMessageDialog(constants.pleaseEnterWarning(),errorMessage);
		}
		
		return flag;
	}

	private String checkTextWidgetForNumber(TextBox textBox)
	{
		String message = "";
		if (textBox.getText().isEmpty())
		{
			message = constants.questionTypeErroMsg();
		}
		else if (!ClientUtility.isNumber(textBox.getText()))
		{
			message = constants.questionTypeNumErrorMsg();
		}
		
		return message;
	}
	
	private String checkTextWidgetForDouble(TextBox textBox)
	{
		String message = "";
		if (textBox.getText().isEmpty())
		{
			message = constants.questionTypeErroMsg();
		}
		else if (!ClientUtility.isDouble(textBox.getText()))
		{
			message = constants.questionTypeNumErrorMsg();
		}
		
		return message;
	}
	private void removeStyles()
	{
		shortNameTxtbox.removeStyleName("higlight_onViolation");
		longNameTxtbox.removeStyleName("higlight_onViolation");
		descriptionTxtbox.removeStyleName("higlight_onViolation");
		
		sumAnswerTxtbox.removeStyleName("higlight_onViolation");
		sumTrueAnswerTxtbox.removeStyleName("higlight_onViolation");
		sumFalseAnswerTxtbox.removeStyleName("higlight_onViolation");
		questionLengthTxtbox.removeStyleName("higlight_onViolation");
		answerLengthTxtbox.removeStyleName("higlight_onViolation");
		answerDiffTxtbox.removeStyleName("higlight_onViolation");
		
		keywordCountTxtbox.removeStyleName("higlight_onViolation");
		minLetterForAutoCompTxtbox.removeStyleName("higlight_onViolation");
		answerLengthTxtbox.removeStyleName("higlight_onViolation");
		shortAnswerLengthTxtbox.removeStyleName("higlight_onViolation");
		/*imageWidthTxtbox.removeStyleName("higlight_onViolation");
		imageLengthTxtbox.removeStyleName("higlight_onViolation");
		imageProportionTxtbox.removeStyleName("higlight_onViolation");*/
		
		//linearPercentageTxtbox.removeStyleName("higlight_onViolation");
		
		minLengthTxtbox.removeStyleName("higlight_onViolation");
		maxLengthTxtbox.removeStyleName("higlight_onViolation");
		minWordCountTxtbox.removeStyleName("higlight_onViolation");
		maxWordCountTxtbox.removeStyleName("higlight_onViolation");
		
		columnTxtbox.removeStyleName("higlight_onViolation");
		maxBytesTxtbox.removeStyleName("higlight_onViolation");
		/*thumbWidthTxtbox.removeStyleName("higlight_onViolation");
		thumbHeightTxtbox.removeStyleName("higlight_onViolation");*/
		selectionTypeListBox.removeStyleName("higlight_onViolation");
		multimediaTypeListBox.removeStyleName("higlight_onViolation");
	}
}
