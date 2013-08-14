package medizin.client.ui.view;

import static medizin.client.util.ClientUtility.toStringUtility;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.style.resources.UiStyles;
import medizin.client.ui.view.renderer.EnumRenderer;
import medizin.client.ui.widget.FocusableValueListBox;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.IntegerBox;
import medizin.client.ui.widget.dialogbox.receiver.ReceiverDialog;
import medizin.client.ui.widget.labeled.LabeledDoubleBox;
import medizin.client.ui.widget.labeled.LabeledIntegerBox;
import medizin.client.ui.widget.labeled.LabeledPanel;
import medizin.client.ui.widget.labeled.LabeledTextArea;
import medizin.client.ui.widget.labeled.LabeledTextBox;
import medizin.client.ui.widget.labeled.LabeledValueBox;
import medizin.client.ui.widget.labeled.LabeledValueListBox;
import medizin.client.util.ClientUtility;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.SelectionType;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.i18n.BmeContextHelpConstants;
import medizin.shared.i18n.BmeMessages;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
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
    public BmeMessages messages = GWT.create(BmeMessages.class);
    public BmeContextHelpConstants contextHelp = GWT.create(BmeContextHelpConstants.class);

	@UiField
	SpanElement title;
	
	@UiField 
	UiStyles uiStyles;
	
	@UiField
	Label baseGroupLbl;
	
	@UiField
	Label examGroupLbl;
	
	@UiField
	Label questionGroupLbl;
	
	@UiField
	Label evaluationGroupLbl;
	
	@UiField
	LabeledTextBox shortName;
	
	@UiField
	LabeledTextBox longName;
	
	@UiField
	LabeledTextArea description;	
	
	@UiField (provided = true)
	LabeledValueListBox<InstitutionProxy> institute = new LabeledValueListBox<InstitutionProxy>(new AbstractRenderer<InstitutionProxy>() {

		@Override
		public String render(InstitutionProxy object) {
			if (object != null)
				return object.getInstitutionName();
			else
				return "";
		}
	});
    	
    @UiField (provided = true)
	LabeledValueListBox<QuestionTypes> questionType = new LabeledValueListBox<QuestionTypes>(new EnumRenderer<QuestionTypes>());    
    	
	@UiField
	LabeledIntegerBox sumAnswer;	

	@UiField
	LabeledIntegerBox sumTrueAnswer;
	
	@UiField
	LabeledIntegerBox sumFalseAnswer;
		 
	@UiField
	LabeledIntegerBox questionLength;
	 
	@UiField
	LabeledIntegerBox answerLength;
	
	@UiField
	LabeledDoubleBox answerDiff;
	
	@UiField
	LabeledPanel queHasMedia;
	
	@UiField
    CheckBox queHaveImgChkBox;
	
	@UiField
    CheckBox queHaveVideoChkBox;
	
	@UiField
    CheckBox queHaveSoundChkBox;
		 
	@UiField
	LabeledIntegerBox keywordCount;
	
	@UiField
    CheckBox showAutoCompleteChkBox;
	
	@UiField
    CheckBox isDictionaryKeywordChkBox;
	
	@UiField
    CheckBox allowTypingChkBox;
	 
	@UiField
	LabeledIntegerBox minLetterForAutoComp;
	
	@UiField
    CheckBox acceptNonKeywordChkBox;
	 
	@UiField
	LabeledIntegerBox shortAnswerLength;
	
	@UiField
    CheckBox keywordHighlightChkBox;
		
	@UiField
    CheckBox richTextChkBox;
	
	@UiField
	LabeledIntegerBox minLength;
		 
	@UiField
	LabeledIntegerBox maxLength;
	
	@UiField
	LabeledIntegerBox minWordCount;
		 
	@UiField
	LabeledIntegerBox maxWordCount;
	
	@UiField
    CheckBox oneToOneAssChkBox;
	
    @UiField (provided = true)
    LabeledValueListBox<MultimediaType> multimediaType = new LabeledValueListBox<MultimediaType>(new EnumRenderer<MultimediaType>());
    	
    @UiField (provided = true)
    LabeledValueListBox<SelectionType> selectionType = new LabeledValueListBox<SelectionType>(new EnumRenderer<SelectionType>());
    	 
	@UiField
	LabeledIntegerBox column;
		 
	@UiField
	LabeledIntegerBox maxBytes;

    @UiField
    IconButton cancel;

    @UiField
    IconButton save;
    
    @UiField
    LabeledPanel richText;
    
    @UiField
    LabeledPanel showAutoComplete;
    
    @UiField
    LabeledPanel isDictionaryKeyword;
    
    @UiField
    LabeledPanel allowTyping;
    
    @UiField
    LabeledPanel acceptNonKeyword;
    
    @UiField
    LabeledPanel oneToOneAss;
    
    @UiField
    LabeledPanel keywordHighlight;
    
    private Delegate delegate;

	private QuestionTypeProxy proxy;
	
    @UiHandler("cancel")
    void onCancel(ClickEvent event) {
        delegate.cancelClicked();
    }

    @UiHandler("save")
    void onSave(ClickEvent event) {
    	save();
    }

    public void setValue(QuestionTypeProxy proxy) {
       this.proxy = proxy;
       showFieldsForQuestionType(proxy.getQuestionType());
       questionType.setEnabled(false);
       //DOM.setElementPropertyBoolean(questionType.getValueListBox().getElement(), "disabled", true);
       institute.setValue(proxy.getInstitution());
       questionType.setValue(proxy.getQuestionType());
       shortName.setValue(proxy.getShortName());
       longName.setValue(proxy.getLongName());
       description.setValue(proxy.getDescription());
       
       if (proxy.getQuestionType().equals(QuestionTypes.Textual) || proxy.getQuestionType().equals(QuestionTypes.Sort))
       {
    	   sumAnswer.setValue(toStringUtility(proxy.getSumAnswer()));
    	   sumTrueAnswer.setValue(toStringUtility(proxy.getSumTrueAnswer()));
    	   sumFalseAnswer.setValue(toStringUtility(proxy.getSumFalseAnswer()));
    	   questionLength.setValue(toStringUtility(proxy.getQuestionLength()));
    	   answerLength.setValue(toStringUtility(proxy.getAnswerLength()));
    	   answerDiff.setValue(toStringUtility(proxy.getDiffBetAnswer()));
    	   queHaveImgChkBox.setValue(proxy.getQueHaveImage());
    	   queHaveVideoChkBox.setValue(proxy.getQueHaveVideo());
    	   queHaveSoundChkBox.setValue(proxy.getQueHaveSound());
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.Imgkey))
       {
    	   questionLength.setValue(toStringUtility(proxy.getQuestionLength()));
    	   keywordCount.setValue(toStringUtility(proxy.getKeywordCount()));
    	   showAutoCompleteChkBox.setValue(proxy.getShowAutocomplete());
    	   isDictionaryKeywordChkBox.setValue(proxy.getIsDictionaryKeyword());
    	   allowTypingChkBox.setValue(proxy.getAllowTyping());
    	   minLetterForAutoComp.setValue(toStringUtility(proxy.getMinAutoCompleteLetter()));
    	   answerLength.setValue(toStringUtility(proxy.getAnswerLength()));
    	   acceptNonKeywordChkBox.setValue(proxy.getAcceptNonKeyword());
    	   shortAnswerLength.setValue(toStringUtility(proxy.getLengthShortAnswer()));
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.ShowInImage))
       {
    	   questionLength.setValue(toStringUtility(proxy.getQuestionLength()));  	   
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.LongText))
       {
    	   questionLength.setValue(toStringUtility(proxy.getQuestionLength()));
    	   keywordHighlightChkBox.setValue(proxy.getKeywordHighlight());
    	   richTextChkBox.setValue(proxy.getRichText());
    	   minLength.setValue(toStringUtility(proxy.getMinLength()));
    	   maxLength.setValue(toStringUtility(proxy.getMaxLength()));
    	   minWordCount.setValue(toStringUtility(proxy.getMinWordCount()));
    	   maxWordCount.setValue(toStringUtility(proxy.getMaxWordCount()));
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.Matrix))
       {
    	   questionLength.setValue(toStringUtility(proxy.getQuestionLength()));
    	   answerLength.setValue(toStringUtility(proxy.getAnswerLength()));
    	   oneToOneAssChkBox.setValue(proxy.getAllowOneToOneAss());
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.MCQ))
       {
    	   questionLength.setValue(toStringUtility(proxy.getQuestionLength()));
    	   multimediaType.setValue(proxy.getMultimediaType());
    	   selectionType.setValue(proxy.getSelectionType());
    	   column.setValue(toStringUtility(proxy.getColumns()));
    	   richTextChkBox.setValue(proxy.getRichText());
    	   maxBytes.setValue(toStringUtility(proxy.getMaxBytes()));
       }
       
    }
    
    private ArrayList<Widget> textualFields;
    private ArrayList<Widget> imgKeyFields;
    private ArrayList<Widget> showInImgFields;
    private ArrayList<Widget> longTextFields;
    private ArrayList<Widget> matrixFields;
    private ArrayList<Widget> mcqFields;
    
    private ArrayList<LabeledTextBox> allTextBoxes;

	public QuestiontypesEditViewImpl(Map<String, Widget> reciverMap) {
		initWidget(uiBinder.createAndBindUi(this));
		
		textualFields = Lists.newArrayList((Widget) sumAnswer, sumTrueAnswer, 
	    		sumFalseAnswer, questionLength, answerLength, answerDiff, queHasMedia);
	    imgKeyFields = Lists.newArrayList((Widget) questionLength, keywordCount, 
	    		showAutoComplete, isDictionaryKeyword, /*allowTyping,*/ /*minLetterForAutoComp,*/ answerLength, 
	    		/*acceptNonKeyword,*/shortAnswerLength);
	    showInImgFields = Lists.newArrayList((Widget) questionLength);
	    longTextFields = Lists.newArrayList((Widget) questionLength,keywordHighlight,richText,minLength,maxLength,minWordCount,maxWordCount);
	    matrixFields = Lists.newArrayList((Widget) questionLength,answerLength,oneToOneAss);
	    mcqFields = Lists.newArrayList((Widget) questionLength,multimediaType,selectionType,column,richText,maxBytes);
	    
	    allTextBoxes = Lists.newArrayList((LabeledTextBox) shortName, longName, sumAnswer, sumTrueAnswer, sumFalseAnswer, 
	    		questionLength, answerLength, answerDiff, keywordCount, minLetterForAutoComp, shortAnswerLength, minLength, 
	    		maxLength, minWordCount, maxWordCount,maxBytes);
	    		
		uiStyles.uiCss().ensureInjected();
		
		reciverMap.put("shortName", shortName);
		reciverMap.put("longName", longName);
		reciverMap.put("description", description);
		reciverMap.put("institution", institute);
		reciverMap.put("questionType", questionType);
		reciverMap.put("sumAnswer",sumAnswer );
		reciverMap.put("sumTrueAnswer", sumTrueAnswer);
		reciverMap.put("sumFalseAnswer", sumFalseAnswer);
		reciverMap.put("questionLength", questionLength);
		reciverMap.put("answerLength", answerLength);
		reciverMap.put("diffBetAnswer", answerDiff);
		reciverMap.put("queHaveImage", queHaveImgChkBox);
		reciverMap.put("queHaveVideo", queHaveVideoChkBox);
		reciverMap.put("queHaveSound", queHaveSoundChkBox);
		reciverMap.put("keywordCount", keywordCount);
		reciverMap.put("minAutoCompleteLetter", minLetterForAutoComp);
		reciverMap.put("lengthShortAnswer",shortAnswerLength );
		reciverMap.put("multimediaType", multimediaType);
		reciverMap.put("selectionType", selectionType);
		reciverMap.put("columns", column);
		reciverMap.put("maxBytes", maxBytes);
		reciverMap.put("minLength", minLength);
		reciverMap.put("maxLength", maxLength);
		reciverMap.put("minWordCount", minWordCount);
		reciverMap.put("maxWordCount", maxWordCount);
		
		save.setText(constants.save());
		cancel.setText(constants.cancel());
		
		multimediaType.setValue(MultimediaType.Image);
		multimediaType.setAcceptableValues(Arrays.asList(MultimediaType.values()));		
		
		selectionType.setValue(SelectionType.SEL_CHOOSE);
		selectionType.setAcceptableValues(Arrays.asList(SelectionType.values()));		
		
		questionType.setValue(QuestionTypes.Textual);
		questionType.setAcceptableValues(Arrays.asList(QuestionTypes.values()));
		
		institute.setEnabled(false);
		
		baseGroupLbl.setText(constants.baseGroupLbl());
		examGroupLbl.setText(constants.examGroupLbl());
		questionGroupLbl.setText(constants.questionGroupLbl());
		evaluationGroupLbl.setText(constants.evaluationGroupLbl());
		
		shortName.setLabelText(constants.shortName());
		longName.setLabelText(constants.longName());
		description.setLabelText(constants.description());
		institute.setLabelText(constants.institutionLbl());
		questionType.setLabelText(constants.questionType());
		sumAnswer.setLabelText(constants.sumAnswer());
		sumTrueAnswer.setLabelText(constants.sumTrueAnswer());
		sumFalseAnswer.setLabelText(constants.sumFalseAnswer());
		questionLength.setLabelText(constants.questionLength());
		answerLength.setLabelText(constants.answerLength());
		answerDiff.setLabelText(constants.diffAnswer());
		
		queHasMedia.setLabelText(constants.queHaveMedia());
		queHaveImgChkBox.setText(constants.queHaveImg());
		queHaveVideoChkBox.setText(constants.queHaveVideo());
		queHaveSoundChkBox.setText(constants.queHaveSound());
		
		keywordCount.setLabelText(constants.countKeyword());
		
		showAutoComplete.setLabelText(constants.showAutocomplete());
		showAutoCompleteChkBox.setText(constants.showAutocomplete());
		
		isDictionaryKeyword.setLabelText(constants.isDictionaryKeyword());
		isDictionaryKeywordChkBox.setText(constants.isDictionaryKeyword());
		
		allowTyping.setVisible(false);
		allowTyping.setLabelText(constants.allowTyping());
		allowTypingChkBox.setText(constants.allowTyping());
		
		minLetterForAutoComp.setLabelText(constants.minLetterAutoComplete());
		
		acceptNonKeyword.setLabelText(constants.acceptNonkeyword());
		acceptNonKeywordChkBox.setText(constants.acceptNonkeyword());
		shortAnswerLength.setLabelText(constants.lengthShortAns());
		
		keywordHighlight.setLabelText(constants.keywordHighlight());
		keywordHighlightChkBox.setText(constants.keywordHighlight());
		
		minLength.setLabelText(constants.minLength());
		maxLength.setLabelText(constants.maxLength());
		minWordCount.setLabelText(constants.minWordCount());
		maxWordCount.setLabelText(constants.maxWordCount());
		
		oneToOneAss.setLabelText(constants.oneToOneAss());
		oneToOneAssChkBox.setText(constants.oneToOneAss());
		
		multimediaType.setLabelText(constants.multimediaType());
		selectionType.setLabelText(constants.selectionType());
		column.setLabelText(constants.column());
		maxBytes.setLabelText(constants.maxBytes());
		
		richText.setLabelText(constants.allowRichText());
		richTextChkBox.setText(constants.allowRichText());
		
		for (final LabeledTextBox box : allTextBoxes) {
			box.addValueChangeHandler(new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					box.getTextBox().removeStyleName("higlight_onViolation");
				}
			});
			
			box.getTextBox().addKeyDownHandler(new KeyDownHandler() {
				
				@Override
				public void onKeyDown(KeyDownEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						save();
					}
				}
			});
		}
		
		description.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				description.getTextArea().removeStyleName("higlight_onViolation");
			}
		});
		
		questionType.addValueChangeHandler(new ValueChangeHandler<QuestionTypes>() {

			@Override
			public void onValueChange(ValueChangeEvent<QuestionTypes> event) {
				removeStyles();
				showFieldsForQuestionType(questionType.getValue());
			}
		});
		
		showAutoCompleteChkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					isDictionaryKeyword.setVisible(true);
					minLetterForAutoComp.setVisible(true);
				} else {
					isDictionaryKeyword.setVisible(false);
					minLetterForAutoComp.setVisible(false);
				}
			}
		});
		
		shortName.setFocus(true);	
	}
	
	private void save() {
		if (validationOfFields(questionType.getValue())) {
    		delegate.saveClicked(proxy);
    	}
	}
	
	private void changeVisibility(ArrayList<Widget> fields, boolean visibility) {
		for (Widget w : fields) {
			w.setVisible(visibility);
		}
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;	
	}

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

	public void showFieldsForQuestionType(QuestionTypes questionTypes)
	{
		if (questionTypes.equals(QuestionTypes.Textual) || questionTypes.equals(QuestionTypes.Sort))
		{
			changeVisibility(mcqFields, false);
			changeVisibility(imgKeyFields, false);
			changeVisibility(matrixFields, false);
			changeVisibility(longTextFields, false);
			changeVisibility(showInImgFields, false);
			changeVisibility(textualFields, true);
			evaluationGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
			examGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
		}
		else if (questionTypes.equals(QuestionTypes.Imgkey))
		{
			changeVisibility(mcqFields, false);
			changeVisibility(textualFields, false);
			changeVisibility(matrixFields, false);
			changeVisibility(longTextFields, false);
			changeVisibility(showInImgFields, false);
			changeVisibility(imgKeyFields, true);
			Document.get().getElementById("isDictionaryKeyword").getStyle().setDisplay(Display.NONE);
			evaluationGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
			examGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
		}
		else if (questionTypes.equals(QuestionTypes.ShowInImage))
		{
			changeVisibility(mcqFields, false);
			changeVisibility(textualFields, false);
			changeVisibility(imgKeyFields, false);
			changeVisibility(matrixFields, false);
			changeVisibility(longTextFields, false);
			changeVisibility(showInImgFields, true);
			evaluationGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
			examGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
		}
		else if (questionTypes.equals(QuestionTypes.LongText))
		{
			changeVisibility(mcqFields, false);
			changeVisibility(textualFields, false);
			changeVisibility(imgKeyFields, false);
			changeVisibility(matrixFields, false);
			changeVisibility(showInImgFields, false);
			changeVisibility(longTextFields, true);
			evaluationGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
			examGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
		}
		else if (questionTypes.equals(QuestionTypes.Matrix))
		{
			changeVisibility(mcqFields, false);
			changeVisibility(textualFields, false);
			changeVisibility(imgKeyFields, false);
			changeVisibility(longTextFields, false);
			changeVisibility(showInImgFields, false);
			changeVisibility(matrixFields, true);
			evaluationGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
			examGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
		} 
		else if (questionTypes.equals(QuestionTypes.MCQ))
		{
			changeVisibility(textualFields, false);
			changeVisibility(imgKeyFields, false);
			changeVisibility(matrixFields, false);
			changeVisibility(longTextFields, false);
			changeVisibility(showInImgFields, false);
			changeVisibility(mcqFields, true);
			evaluationGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
			examGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
		} 
		else
		{
			changeVisibility(mcqFields, false);
			changeVisibility(textualFields, false);
			changeVisibility(imgKeyFields, false);
			changeVisibility(matrixFields, false);
			changeVisibility(longTextFields, false);
			changeVisibility(showInImgFields, false);
			// when is "else" displayed??
			evaluationGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
			examGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
		}
	}
	
	public void setNullValue(QuestionTypes questionTypes)
	{
		questionType.setValue(QuestionTypes.Textual);
		shortName.getTextBox().setValue("");
		longName.setValue("");
		description.setValue("");
		
		if (questionTypes.equals(QuestionTypes.Textual) || questionTypes.equals(QuestionTypes.Sort))
	       {
	    	   sumAnswer.setValue("");
	    	   sumTrueAnswer.setValue("");
	    	   sumFalseAnswer.setValue("");
	    	   questionLength.setValue("");
	    	   answerLength.setValue("");
	    	   answerDiff.setValue("");
	    	   queHaveImgChkBox.setValue(false);
	    	   queHaveVideoChkBox.setValue(false);
	    	   queHaveSoundChkBox.setValue(false);
	       }
	       else if (questionTypes.equals(QuestionTypes.Imgkey))
	       {
	    	   keywordCount.setValue("");
	    	   showAutoCompleteChkBox.setValue(false);
	    	   isDictionaryKeywordChkBox.setValue(false);
	    	   allowTypingChkBox.setValue(false);
	    	   minLetterForAutoComp.setValue("");
	    	   questionLength.setValue("");
	    	   answerLength.setValue("");
	    	   acceptNonKeywordChkBox.setValue(false);
	    	   shortAnswerLength.setValue("");
	    	   /*imageWidth.setValue("");
	    	   imageLength.setValue("");
	    	   imageProportion.setValue("");*/
	    	   
	       }
	       else if (questionTypes.equals(QuestionTypes.ShowInImage))
	       {
	    	   questionLength.setValue("");
	    	   /*answerLength.setValue("");	    	   
	    	   imageWidth.setValue("");
	    	   imageLength.setValue("");
	    	   imageProportion.setValue("");
	    	   linearPointChkBox.setValue(false);
	    	   linearPercentage.setValue("");*/	    	   
	       }
	       else if (questionTypes.equals(QuestionTypes.LongText))
	       {
	    	   questionLength.setValue("");
	    	   keywordHighlightChkBox.setValue(false);
	    	   richTextChkBox.setValue(false);
	    	   minLength.setValue("");
	    	   maxLength.setValue("");
	    	   minWordCount.setValue("0");
	    	   maxWordCount.setValue("0");
	       }
	       else if (questionTypes.equals(QuestionTypes.Matrix))
	       {
	    	  //maxLength.setValue("");
	    	   answerLength.setValue("");
	    	   questionLength.setValue("");
	    	   oneToOneAssChkBox.setValue(false);
	       }
	       else if (questionTypes.equals(QuestionTypes.MCQ))
	       {
	    	   questionLength.setValue("");
	    	   /*imageWidth.setValue("");
	    	   imageLength.setValue("");
	    	   imageProportion.setValue("");*/
	    	   multimediaType.setValue(MultimediaType.Image);
	    	   selectionType.setValue(SelectionType.SEL_CHOOSE);
	    	   column.setValue("");
	    	   richTextChkBox.setValue(false);
	    	   /*thumbWidth.setValue("");
	    	   thumbHeight.setValue("");
	    	   allowZoomOutChkBox.setValue(false);
	    	   allowZoomInChkBox.setValue(false);*/
	    	   maxBytes.setValue("");
	       }
	}

	public void setCreating(boolean creating) {
		if (creating) {
			title.setInnerText(constants.addQuestionType());
			//editTitle.getStyle().setDisplay(Display.NONE);
			// change{	//	createTitle.getStyle().clearDisplay();
//			questionTypePanel.getTabBar().setTabText(0, constants.addQuestionType());
		} else {
			title.setInnerText(constants.editQuestionType());
			//editTitle.getStyle().clearDisplay();
			// change{	createTitle.getStyle().setDisplay(Display.NONE);
//			questionTypePanel.getTabBar().setTabText(0, constants.editQuestionType());
		}
	}
	
	public TextBox getShortNameTxtbox() {
		return shortName.getTextBox();
	}

	public TextBox getLongNameTxtbox() {
		return longName.getTextBox();
	}
	
	public TextArea getDescriptionTxtbox() {
		return description.getTextArea();
	}

	public FocusableValueListBox<InstitutionProxy> getInstituteListBox() {
		return institute.getValueListBox();
	}

	public FocusableValueListBox<QuestionTypes> getQuestionTypeListBox() {
		return questionType.getValueListBox();
	}

	public IntegerBox getSumAnswerTxtbox() {
		return sumAnswer.getIntegerBox();
	}

	public TextBox getSumTrueAnswerTxtbox() {
		return sumTrueAnswer.getIntegerBox();
	}

	public TextBox getSumFalseAnswerTxtbox() {
		return sumFalseAnswer.getIntegerBox();
	}

	public TextBox getQuestionLengthTxtbox() {
		return questionLength.getIntegerBox();
	}

	public TextBox getAnswerLengthTxtbox() {
		return answerLength.getIntegerBox();
	}

	public TextBox getAnswerDiffTxtbox() {
		return answerDiff.getDoubleBox();
	}

	public CheckBox getQueHaveImgChkBox() {
		return queHaveImgChkBox;
	}

	public CheckBox getQueHaveVideoChkBox() {
		return queHaveVideoChkBox;
	}

	public CheckBox getQueHaveSoundChkBox() {
		return queHaveSoundChkBox;
	}

	public TextBox getKeywordCountTxtbox() {
		return keywordCount.getIntegerBox();
	}

	public CheckBox getShowAutoCompleteChkBox() {
		return showAutoCompleteChkBox;
	}

	public CheckBox getIsDictionaryKeywordChkBox() {
		return isDictionaryKeywordChkBox;
	}

	public CheckBox getAllowTypingChkBox() {
		return allowTypingChkBox;
	}

	public TextBox getMinLetterForAutoCompTxtbox() {
		return minLetterForAutoComp.getIntegerBox();
	}

	public CheckBox getAcceptNonKeywordChkBox() {
		return acceptNonKeywordChkBox;
	}

	public TextBox getShortAnswerLengthTxtbox() {
		return shortAnswerLength.getIntegerBox();
	}

	public CheckBox getKeywordHighlightChkBox() {
		return keywordHighlightChkBox;
	}

	public CheckBox getRichTextChkBox() {
		return richTextChkBox;
	}

	public TextBox getMinLengthTxtbox() {
		return minLength.getIntegerBox();
	}

	public TextBox getMaxLengthTxtbox() {
		return maxLength.getIntegerBox();
	}

	public TextBox getMinWordCountTxtbox() {
		return minWordCount.getIntegerBox();
	}

	public TextBox getMaxWordCountTxtbox() {
		return maxWordCount.getIntegerBox();
	}

	public CheckBox getOneToOneAssChkBox() {
		return oneToOneAssChkBox;
	}

	public FocusableValueListBox<MultimediaType> getMultimediaTypeListBox() {
		return multimediaType.getValueListBox();
	}

	public FocusableValueListBox<SelectionType> getSelectionTypeListBox() {
		return selectionType.getValueListBox();
	}

	public TextBox getColumnTxtbox() {
		return column.getIntegerBox();
	}

	public TextBox getMaxBytesTxtbox() {
		return maxBytes.getIntegerBox();
	}
	
	private boolean validationOfFields(QuestionTypes questionType)
	{
		removeStyles();
		ArrayList<String> errorMessage = Lists.newArrayList();
		
		boolean flag = true;
		//StringBuilder errorString = new StringBuilder();
		
		if (shortName.getText().isEmpty())
    	{	
			flag = false;
			//errorString.append(constants.shortName() + " " + constants.questionTypeErroMsg()).append("<br />");
			errorMessage.add(constants.shortName() + " " + constants.questionTypeErroMsg());
			shortName.getTextBox().addStyleName("higlight_onViolation");
    	}
		
		if (longName.getText().isEmpty())
		{
			flag = false;
			//errorString.append(constants.longName() + " " + constants.questionTypeErroMsg()).append("<br />");
			errorMessage.add(constants.longName() + " " + constants.questionTypeErroMsg());
			longName.getTextBox().addStyleName("higlight_onViolation");
		}
		
		if (description.getText().isEmpty())
		{
			flag = false;
			//errorString.append(constants.description() + " " + constants.questionTypeErroMsg()).append("<br />");
			errorMessage.add(constants.description() + " " + constants.questionTypeErroMsg());
			description.getTextArea().addStyleName("higlight_onViolation");
		}		
		
		switch (questionType)
		{
			case Sort:
			case Textual:
			{
				String msg = "";				
				if ((msg = checkTextWidgetForNumber(sumAnswer.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.sumAnswer() + " " + msg).append("<br />");
					errorMessage.add(constants.sumAnswer() + " " + msg);
					sumAnswer.getTextBox().addStyleName("higlight_onViolation");
				}
				else if (Integer.parseInt(sumAnswer.getValue()) < 2)
				{
					flag = false;
					//errorString.append(constants.sumOfAnsMsg()).append("<br />");
					errorMessage.add(constants.sumOfAnsMsg());
					sumAnswer.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(sumTrueAnswer.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.sumTrueAnswer() + " " + msg).append("<br />");
					errorMessage.add(constants.sumTrueAnswer() + " " + msg);
					sumTrueAnswer.getTextBox().addStyleName("higlight_onViolation");
				}
				else if (Integer.parseInt(sumTrueAnswer.getValue()) < 0)
				{
					flag = false;
					//errorString.append(constants.sumOfTrueAnsMsg()).append("<br />");
					errorMessage.add(constants.sumOfTrueAnsMsg());
					sumTrueAnswer.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(sumFalseAnswer.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.sumFalseAnswer() + " " + msg).append("<br />");
					errorMessage.add(constants.sumFalseAnswer() + " " + msg);
					sumFalseAnswer.getTextBox().addStyleName("higlight_onViolation");
				}
				else if (Integer.parseInt(sumFalseAnswer.getValue()) < 0)
				{
					flag = false;
					//errorString.append(constants.sumOfFalseAnsMsg()).append("<br />");
					errorMessage.add(constants.sumOfFalseAnsMsg());
					sumFalseAnswer.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(questionLength.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.questionLength() + " " + msg).append("<br />");
					errorMessage.add(constants.questionLength() + " " + msg);
					questionLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(answerLength.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.answerLength() + " " + msg).append("<br />");
					errorMessage.add(constants.answerLength() + " " + msg);
					answerLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForDouble(answerDiff.getDoubleBox())) != "")
				{
					flag = false;
					//errorString.append(constants.diffAnswer() + " " + msg).append("<br />");
					errorMessage.add(constants.diffAnswer() + " " + msg);
					answerDiff.getTextBox().addStyleName("higlight_onViolation");
				}
				
				break;
			}
			
			case Imgkey:
			{
				String msg = "";				
				if ((msg = checkTextWidgetForNumber(keywordCount.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.countKeyword() + " " + msg).append("<br />");
					errorMessage.add(constants.countKeyword() + " " + msg);
					keywordCount.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(minLetterForAutoComp.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.minLetterAutoComplete() + " " + msg).append("<br />");
					errorMessage.add(constants.minLetterAutoComplete() + " " + msg);
					minLetterForAutoComp.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(questionLength.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.questionLength() + " " + msg).append("<br />");
					errorMessage.add(constants.questionLength() + " " + msg);
					questionLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(answerLength.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.answerLength() + " " + msg).append("<br />");
					errorMessage.add(constants.answerLength() + " " + msg);
					answerLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(shortAnswerLength.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.lengthShortAns() + " " + msg).append("<br />");
					errorMessage.add(constants.lengthShortAns() + " " + msg);
					shortAnswerLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				break;
			}
			
			case ShowInImage:
			{
				String msg = "";
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(questionLength.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.questionLength() + " " + msg).append("<br />");
					errorMessage.add(constants.questionLength() + " " + msg);
					questionLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				break;
			}
			
			case LongText:
			{
				String msg = "";
				if ((msg = checkTextWidgetForNumber(minLength.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.minLength() + " " + msg).append("<br />");
					errorMessage.add(constants.minLength() + " " + msg);
					minLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(questionLength.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.questionLength() + " " + msg).append("<br />");
					errorMessage.add(constants.questionLength() + " " + msg);
					questionLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(maxLength.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.maxLength() + " " + msg).append("<br />");
					errorMessage.add(constants.maxLength() + " " + msg);
					maxLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(minWordCount.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.minWordCount() + " " + msg).append("<br />");
					errorMessage.add(constants.minWordCount() + " " + msg);
					minWordCount.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(maxWordCount.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.maxWordCount() + " " + msg).append("<br />");
					errorMessage.add(constants.maxWordCount() + " " + msg);
					maxWordCount.getTextBox().addStyleName("higlight_onViolation");
				}
				
				break;
			}
			
			case Matrix:
			{
				String msg = "";
				if ((msg = checkTextWidgetForNumber(questionLength.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.questionLength() + " " + msg).append("<br />");
					errorMessage.add(constants.questionLength() + " " + msg);
					questionLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(answerLength.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.answerLength() + " " + msg).append("<br />");
					errorMessage.add(constants.answerLength() + " " + msg);
					answerLength.getTextBox().addStyleName("higlight_onViolation");
				}
				break;
			}
			
			case MCQ:
			{
				String msg = "";
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(questionLength.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.questionLength() + " " + msg).append("<br />");
					errorMessage.add(constants.questionLength() + " " + msg);
					questionLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				if (multimediaType.getValue().equals(null))
				{
					flag = false;
					//errorString.append(constants.multimediaType() + " " + constants.questionTypeErroMsg()).append("<br />");
					errorMessage.add(constants.multimediaType() + " " + constants.questionTypeErroMsg());
					multimediaType.getValueListBox().addStyleName("higlight_onViolation");
				}
				
				if (selectionType.getValue().equals(null))
				{
					flag = false;
					//errorString.append(constants.selectionType() + " " + constants.questionTypeErroMsg()).append("<br />");
					errorMessage.add(constants.selectionType() + " " + constants.questionTypeErroMsg());
					selectionType.getValueListBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(column.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.column() + " " + msg).append("<br />");
					errorMessage.add(constants.column() + " " + msg);
					column.getTextBox().addStyleName("higlight_onViolation");
				}

				msg = "";
				if ((msg = checkTextWidgetForNumber(maxBytes.getIntegerBox())) != "")
				{
					flag = false;
					//errorString.append(constants.maxBytes() + " " + msg).append("<br />");
					errorMessage.add(constants.maxBytes() + " " + msg);
					maxBytes.getTextBox().addStyleName("higlight_onViolation");
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
		shortName.getTextBox().removeStyleName("higlight_onViolation");
		longName.removeStyleName("higlight_onViolation");
		description.removeStyleName("higlight_onViolation");
		
		sumAnswer.removeStyleName("higlight_onViolation");
		sumTrueAnswer.removeStyleName("higlight_onViolation");
		sumFalseAnswer.removeStyleName("higlight_onViolation");
		questionLength.removeStyleName("higlight_onViolation");
		answerLength.removeStyleName("higlight_onViolation");
		answerDiff.removeStyleName("higlight_onViolation");
		
		keywordCount.removeStyleName("higlight_onViolation");
		minLetterForAutoComp.removeStyleName("higlight_onViolation");
		answerLength.removeStyleName("higlight_onViolation");
		shortAnswerLength.removeStyleName("higlight_onViolation");
		/*imageWidthTxtbox.removeStyleName("higlight_onViolation");
		imageLengthTxtbox.removeStyleName("higlight_onViolation");
		imageProportionTxtbox.removeStyleName("higlight_onViolation");*/
		
		//linearPercentageTxtbox.removeStyleName("higlight_onViolation");
		
		minLength.removeStyleName("higlight_onViolation");
		maxLength.removeStyleName("higlight_onViolation");
		minWordCount.removeStyleName("higlight_onViolation");
		maxWordCount.removeStyleName("higlight_onViolation");
		
		column.removeStyleName("higlight_onViolation");
		maxBytes.removeStyleName("higlight_onViolation");
		/*thumbWidthTxtbox.removeStyleName("higlight_onViolation");
		thumbHeightTxtbox.removeStyleName("higlight_onViolation");*/
		selectionType.removeStyleName("higlight_onViolation");
		multimediaType.removeStyleName("higlight_onViolation");
	}
}
