package medizin.client.ui.view;

import java.util.Collection;
import java.util.HashSet;

import static medizin.client.util.ClientUtility.defaultString;
import static medizin.client.util.ClientUtility.defaultBoolean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

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
import medizin.client.ui.widget.labeled.LabeledValueListBox;
import medizin.client.util.ClientUtility;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.SelectionType;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.i18n.BmeContextHelpConstants;
import medizin.shared.i18n.BmeMessages;
import medizin.shared.utils.SharedConstant;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
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
    HTMLPanel panel;
    
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
    LabeledPanel instituteLblPanel;
    
    @UiField
    Label instituteLbl;
    
    @UiField
    LabeledPanel questionTypeLblPanel;
    
    @UiField
    Label questionTypeLbl;
    
    @UiField
    Label multimediaGroupLbl;
    	
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
    IconButton cancel2;

    @UiField
    IconButton save2;
    
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
    
    @UiField
    CheckBox infiniteChkBox;
    
    private Delegate delegate;

	private QuestionTypeProxy proxy;
	
    @UiHandler("cancel")
    void onCancel(ClickEvent event) {
        delegate.cancelClicked();
    }
    
    @UiHandler("cancel2")
    void onCancel2(ClickEvent event) {
    	delegate.cancelClicked();
    }

    @UiHandler("save")
    void onSave(ClickEvent event) {
    	save();
    }
    
    @UiHandler("save2")
    void onSave2(ClickEvent event) {
    	save();
    }

    public void setValue(QuestionTypeProxy proxy) {
       this.proxy = proxy;
       showFieldsForQuestionType(proxy.getQuestionType());
       questionType.setEnabled(false);
       //DOM.setElementPropertyBoolean(questionType.getValueListBox().getElement(), "disabled", true);
       institute.setValue(proxy.getInstitution());
       instituteLbl.setText((proxy.getInstitution() != null) ? defaultString(proxy.getInstitution().getInstitutionName()) : "");
       questionType.setValue(proxy.getQuestionType());
       questionTypeLbl.setText(new EnumRenderer<QuestionTypes>().render(proxy.getQuestionType()));
       
       shortName.setValue(proxy.getShortName());
       longName.setValue(proxy.getLongName());
       description.setValue(proxy.getDescription());
       
       if (proxy.getQuestionType().equals(QuestionTypes.Textual) || proxy.getQuestionType().equals(QuestionTypes.Sort))
       {
    	   if (SharedConstant.INFINITE_VALUE.equals(proxy.getSumAnswer()))
    	   {
    		   sumAnswer.setValue(defaultString(proxy.getSumAnswer()));
    		   sumAnswer.getTextBox().setEnabled(false);
    		   infiniteChkBox.setValue(true);
    	   }
    	   sumTrueAnswer.setValue(defaultString(proxy.getSumTrueAnswer()));
    	   sumFalseAnswer.setValue(defaultString(proxy.getSumFalseAnswer()));
    	   questionLength.setValue(defaultString(proxy.getQuestionLength()));
    	   answerLength.setValue(defaultString(proxy.getAnswerLength()));
    	   answerDiff.setValue(defaultString(proxy.getDiffBetAnswer()));
    	   queHaveImgChkBox.setValue(proxy.getQueHaveImage());
    	   queHaveVideoChkBox.setValue(proxy.getQueHaveVideo());
    	   queHaveSoundChkBox.setValue(proxy.getQueHaveSound());
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.Imgkey))
       {
    	   questionLength.setValue(defaultString(proxy.getQuestionLength()));
    	   keywordCount.setValue(defaultString(proxy.getKeywordCount()));
    	   showAutoCompleteChkBox.setValue(proxy.getShowAutocomplete());
    	   isDictionaryKeywordChkBox.setValue(proxy.getIsDictionaryKeyword());
    	   allowTypingChkBox.setValue(proxy.getAllowTyping());
    	   minLetterForAutoComp.setValue(defaultString(proxy.getMinAutoCompleteLetter()));
    	   answerLength.setValue(defaultString(proxy.getAnswerLength()));
    	   acceptNonKeywordChkBox.setValue(proxy.getAcceptNonKeyword());
    	   shortAnswerLength.setValue(defaultString(proxy.getLengthShortAnswer()));
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.ShowInImage))
       {
    	   questionLength.setValue(defaultString(proxy.getQuestionLength()));  	   
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.LongText))
       {
    	   questionLength.setValue(defaultString(proxy.getQuestionLength()));
    	   keywordHighlightChkBox.setValue(proxy.getKeywordHighlight());
    	   richTextChkBox.setValue(proxy.getRichText());
    	   minLength.setValue(defaultString(proxy.getMinLength()));
    	   maxLength.setValue(defaultString(proxy.getMaxLength()));
    	   minWordCount.setValue(defaultString(proxy.getMinWordCount()));
    	   maxWordCount.setValue(defaultString(proxy.getMaxWordCount()));    	   
    	   queHaveImgChkBox.setValue(defaultBoolean(proxy.getQueHaveImage()));
    	   queHaveVideoChkBox.setValue(defaultBoolean(proxy.getQueHaveVideo()));
    	   queHaveSoundChkBox.setValue(defaultBoolean(proxy.getQueHaveSound()));
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.Matrix))
       {
    	   questionLength.setValue(defaultString(proxy.getQuestionLength()));
    	   answerLength.setValue(defaultString(proxy.getAnswerLength()));
    	   oneToOneAssChkBox.setValue(proxy.getAllowOneToOneAss());
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.MCQ))
       {
    	   questionLength.setValue(defaultString(proxy.getQuestionLength()));
    	   multimediaType.setValue(proxy.getMultimediaType());
    	   selectionType.setValue(proxy.getSelectionType());
    	   column.setValue(defaultString(proxy.getColumns()));
    	   richTextChkBox.setValue(proxy.getRichText());
    	   maxBytes.setValue(defaultString(proxy.getMaxBytes()));
       }
       
       updateImgKeyFields();
    }
    
    private ArrayList<Widget> baseFields;
    private ArrayList<Widget> textualFields;
    private ArrayList<Widget> imgKeyFields;
    private ArrayList<Widget> showInImgFields;
    private ArrayList<Widget> longTextFields;
    private ArrayList<Widget> matrixFields;
    private ArrayList<Widget> mcqFields;
    
    private ArrayList<LabeledTextBox> allTextBoxes;
    private HashSet<Widget> allBoxes;

	public QuestiontypesEditViewImpl(Map<String, Widget> reciverMap) {
		initWidget(uiBinder.createAndBindUi(this));
		
		baseFields = Lists.newArrayList((Widget) shortName, longName, description,
				institute, questionType);
		
		textualFields = Lists.newArrayList((Widget) sumAnswer, sumTrueAnswer,
				sumFalseAnswer, questionLength, answerLength, answerDiff,
				queHasMedia, infiniteChkBox);
		textualFields.addAll(baseFields);
		
		imgKeyFields = Lists.newArrayList((Widget) shortName, longName, description,
				institute, questionType, questionLength, keywordCount, allowTyping, 
				answerLength);
		imgKeyFields.addAll(baseFields);
		
		showInImgFields = Lists.newArrayList((Widget) shortName, longName, description,
				institute, questionType, questionLength);
		showInImgFields.addAll(baseFields);
		
		longTextFields = Lists.newArrayList((Widget) questionLength,
				keywordHighlight, richText, minLength, maxLength, minWordCount,
				maxWordCount, queHasMedia);
		longTextFields.addAll(baseFields);
		
		matrixFields = Lists.newArrayList((Widget) questionLength,
				answerLength, oneToOneAss);
		matrixFields.addAll(baseFields);
		
		mcqFields = Lists.newArrayList((Widget) questionLength, multimediaType,
				selectionType, column, richText, maxBytes);
		mcqFields.addAll(baseFields);

		allTextBoxes = Lists.newArrayList((LabeledTextBox) shortName, longName,
				sumAnswer, sumTrueAnswer, sumFalseAnswer, questionLength,
				answerLength, answerDiff, keywordCount, minLetterForAutoComp,
				shortAnswerLength, minLength, maxLength, minWordCount,
				maxWordCount, maxBytes);

		allBoxes = Sets.newHashSet((Widget) shortName, longName, description,
				institute, questionType, sumAnswer, sumTrueAnswer,
				sumFalseAnswer, questionLength, answerLength, answerDiff,
				queHasMedia, keywordCount, isDictionaryKeyword, allowTyping,
				minLetterForAutoComp, acceptNonKeyword, shortAnswerLength,
				keywordHighlight, richText, minLength, maxLength, minWordCount,
				maxWordCount, oneToOneAss, multimediaType, selectionType,
				column, maxBytes, showAutoComplete, infiniteChkBox);
		allBoxes.addAll(baseFields);
	    		
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
		save2.setText(constants.save());
		cancel.setText(constants.cancel());
		cancel2.setText(constants.cancel());
		
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
		multimediaGroupLbl.setText(constants.multimediaAttributes());
		
		shortName.setLabelText(constants.shortName());
		shortName.setHelpText(contextHelp.qtShortName());
		
		longName.setLabelText(constants.longName());
		longName.setHelpText(contextHelp.qtLongName());
		
		description.setLabelText(constants.description());
		description.setHelpText(contextHelp.qtDescription());
		
		institute.setLabelText(constants.institutionLbl());
		institute.setHelpText(contextHelp.qtInstitution());
		instituteLblPanel.setLabelText(constants.institutionLbl());
		instituteLblPanel.setHelpText(contextHelp.qtInstitution());
		
		questionTypeLblPanel.setLabelText(constants.questionType());
		questionTypeLblPanel.setHelpText(contextHelp.qtQuestionType());
		questionType.setLabelText(constants.questionType());
		questionType.setHelpText(contextHelp.qtQuestionType());
		
		sumAnswer.setLabelText(constants.sumAnswer());
		sumAnswer.setHelpText(contextHelp.qtSumAnswer());
		
		sumTrueAnswer.setLabelText(constants.sumTrueAnswer());
		sumTrueAnswer.setHelpText(contextHelp.qtSumTrueAnswer());

		sumFalseAnswer.setLabelText(constants.sumFalseAnswer());
		sumFalseAnswer.setHelpText(contextHelp.qtSumFalseAnswer());
		
		questionLength.setLabelText(constants.questionLength());
		questionLength.setHelpText(contextHelp.qtQuestionLength());
		
		answerLength.setLabelText(constants.answerLength());
		answerLength.setHelpText(contextHelp.qtAnswerLength());
		
		answerDiff.setLabelText(constants.diffAnswer());
		answerDiff.setHelpText(contextHelp.qtAnswerDiff());
		
		queHasMedia.setLabelText(constants.queHaveMedia());
		queHasMedia.setHelpText(contextHelp.qtQueHasMedia());
		queHaveImgChkBox.setText(constants.queHaveImg());
		queHaveVideoChkBox.setText(constants.queHaveVideo());
		queHaveSoundChkBox.setText(constants.queHaveSound());
		
		keywordCount.setLabelText(constants.countKeyword());
		keywordCount.setHelpText(contextHelp.qtKeywordCount());
		
		showAutoComplete.setLabelText(constants.showAutocomplete());
		showAutoComplete.setHelpText(contextHelp.qtShowAutoComplete());
		showAutoCompleteChkBox.setText(constants.showAutocomplete());
		
		isDictionaryKeyword.setLabelText(constants.isDictionaryKeyword());
		isDictionaryKeyword.setHelpText(contextHelp.qtIsDictionaryKeyword());
		isDictionaryKeywordChkBox.setText(constants.isDictionaryKeyword());
		
		allowTyping.setLabelText(constants.allowTyping());
		allowTyping.setHelpText(contextHelp.qtAllowTyping());
		allowTypingChkBox.setText(constants.allowTyping());
		
		minLetterForAutoComp.setLabelText(constants.minLetterAutoComplete());
		minLetterForAutoComp.setHelpText(contextHelp.qtMinAutoCompleteLetter());
		
		acceptNonKeyword.setLabelText(constants.acceptNonkeyword());
		acceptNonKeyword.setHelpText(contextHelp.qtAcceptNonKeyword());
		acceptNonKeywordChkBox.setText(constants.acceptNonkeyword());
		shortAnswerLength.setLabelText(constants.lengthShortAns());
		shortAnswerLength.setHelpText(contextHelp.qtShortAnswerLength());
		
		keywordHighlight.setLabelText(constants.keywordHighlight());
		keywordHighlight.setHelpText(contextHelp.qtKeywordHighlight());
		keywordHighlightChkBox.setText(constants.keywordHighlight());
		
		minLength.setLabelText(constants.minLength());
		minLength.setHelpText(contextHelp.qtMinLength());
		maxLength.setLabelText(constants.maxLength());
		maxLength.setHelpText(contextHelp.qtMaxLength());
		minWordCount.setLabelText(constants.minWordCount());
		minWordCount.setHelpText(contextHelp.qtMinWordCount());
		maxWordCount.setLabelText(constants.maxWordCount());
		maxWordCount.setHelpText(contextHelp.qtMaxWordCount());
		
		oneToOneAss.setLabelText(constants.oneToOneAss());
		oneToOneAss.setHelpText(contextHelp.qtOneToOneAss());
		oneToOneAssChkBox.setText(constants.oneToOneAss());
		
		multimediaType.setLabelText(constants.multimediaType());
		multimediaType.setHelpText(contextHelp.qtMultimediaType());
		selectionType.setLabelText(constants.selectionType());
		selectionType.setHelpText(contextHelp.qtSelectionType());
		column.setLabelText(constants.column());
		column.setHelpText(contextHelp.qtColumns());
		maxBytes.setLabelText(constants.maxBytes());
		maxBytes.setHelpText(contextHelp.qtMaxBytes());
		
		infiniteChkBox.setText(constants.infinite());
		
		richText.setLabelText(constants.allowRichText());
		richText.setHelpText(contextHelp.qtRichText());
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
			//@Override
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
		
		allowTypingChkBox.addValueChangeHandler(new ImgKeyFieldChangeHandler());
		showAutoCompleteChkBox.addValueChangeHandler(new ImgKeyFieldChangeHandler());
		acceptNonKeywordChkBox.addValueChangeHandler(new ImgKeyFieldChangeHandler());
		
		shortName.setFocus(true);
		
		infiniteChkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue())
				{
					sumAnswer.getTextBox().setValue(SharedConstant.INFINITE_VALUE.toString());
					sumAnswer.getTextBox().setEnabled(false);
				}
				else
				{
					sumAnswer.getTextBox().setValue("");
					sumAnswer.getTextBox().setEnabled(true);
				}
			}
		});
		
		sumAnswer.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (ClientUtility.isNumber(event.getValue()))
				{
					if (SharedConstant.INFINITE_VALUE.equals(Integer.parseInt(event.getValue())))
					{
						sumAnswer.getTextBox().setEnabled(false);
						infiniteChkBox.setValue(true);
					}
				}
			}
		});
		
		updateImgKeyFields();
	}
	
	private class ImgKeyFieldChangeHandler implements ValueChangeHandler<Boolean> {

		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			updateImgKeyFields();
		}
		
	}
		
	private void updateImgKeyFields() {
		if (questionType.getValue() == QuestionTypes.Imgkey) {
			boolean visibility = allowTypingChkBox.getValue();
			showAutoComplete.setVisible(visibility);
			isDictionaryKeyword.setVisible(visibility);
			acceptNonKeyword.setVisible(visibility);
			shortAnswerLength.setVisible(visibility && acceptNonKeywordChkBox.getValue());
			minLetterForAutoComp.setVisible(visibility && showAutoCompleteChkBox.getValue());
		}
	}
	
	private void save() {
		if (validationOfFields(questionType.getValue())) {
    		delegate.saveClicked(proxy);
    	}
	}
	
	private void changeVisibility(Collection<Widget> fields, boolean visibility) {
		for (Widget w : fields) {
			w.setVisible(visibility);
		}
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;	
	}

	@Override
	public void setEditTitle(boolean edit) {
	      if (edit) {
	    	  title.setInnerText(constants.editQuestionType());
	    	  instituteLblPanel.setVisible(true);
	    	  questionTypeLblPanel.setVisible(true);
	    	  institute.setVisible(false);
	    	  questionType.setVisible(false);
	        } else {
		    	  instituteLblPanel.setVisible(false);
		    	  questionTypeLblPanel.setVisible(false);
		    	  institute.setVisible(true);
		    	  questionType.setVisible(true);
	        	title.setInnerText(constants.addQuestionType());
	        }
	}

	public void showFieldsForQuestionType(QuestionTypes questionTypes)
	{
		if (questionTypes.equals(QuestionTypes.Textual) || questionTypes.equals(QuestionTypes.Sort))
		{
			changeVisibility(allBoxes, false);
			changeVisibility(textualFields, true);
			evaluationGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
			examGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
			multimediaGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
		}
		else if (questionTypes.equals(QuestionTypes.Imgkey))
		{
			changeVisibility(allBoxes, false);
			changeVisibility(imgKeyFields, true);
			//Document.get().getElementById("isDictionaryKeyword").getStyle().setDisplay(Display.NONE);
			evaluationGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
			examGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
			multimediaGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
		}
		else if (questionTypes.equals(QuestionTypes.ShowInImage))
		{
			changeVisibility(allBoxes, false);
			changeVisibility(showInImgFields, true);
			evaluationGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
			examGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
			multimediaGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
		}
		else if (questionTypes.equals(QuestionTypes.LongText))
		{
			changeVisibility(allBoxes, false);
			changeVisibility(longTextFields, true);
			evaluationGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
			examGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
			multimediaGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
		}
		else if (questionTypes.equals(QuestionTypes.Matrix))
		{
			changeVisibility(allBoxes, false);
			changeVisibility(matrixFields, true);
			evaluationGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
			examGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
			multimediaGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
		} 
		else if (questionTypes.equals(QuestionTypes.MCQ))
		{
			changeVisibility(allBoxes, false);
			changeVisibility(mcqFields, true);
			evaluationGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
			examGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
			multimediaGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
		} 
		else
		{
			changeVisibility(allBoxes, false);
			// when is "else" displayed??
			evaluationGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
			examGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
			multimediaGroupLbl.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
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
	    	   infiniteChkBox.setValue(false);
	    	   sumAnswer.getTextBox().setEnabled(true);
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
	    	   
	       }
	       else if (questionTypes.equals(QuestionTypes.ShowInImage))
	       {
	    	   questionLength.setValue("");  	   
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
	    	   queHaveImgChkBox.setValue(false);
	    	   queHaveVideoChkBox.setValue(false);
	    	   queHaveSoundChkBox.setValue(false);
	       }
	       else if (questionTypes.equals(QuestionTypes.Matrix))
	       {
	    	   answerLength.setValue("");
	    	   questionLength.setValue("");
	    	   oneToOneAssChkBox.setValue(false);
	       }
	       else if (questionTypes.equals(QuestionTypes.MCQ))
	       {
	    	   questionLength.setValue("");
	    	   multimediaType.setValue(MultimediaType.Image);
	    	   selectionType.setValue(SelectionType.SEL_CHOOSE);
	    	   column.setValue("");
	    	   richTextChkBox.setValue(false);
	    	   maxBytes.setValue("");
	       }
	}

	public void setCreating(boolean creating) {
		if (creating) {
			title.setInnerText(constants.addQuestionType());
		} else {
			title.setInnerText(constants.editQuestionType());
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
		boolean answerFlag = false;
				
		if (shortName.getText().isEmpty())
    	{	
			flag = false;
						errorMessage.add(constants.shortName() + " " + constants.questionTypeErroMsg());
			shortName.getTextBox().addStyleName("higlight_onViolation");
    	}
		
		if (longName.getText().isEmpty())
		{
			flag = false;
			errorMessage.add(constants.longName() + " " + constants.questionTypeErroMsg());
			longName.getTextBox().addStyleName("higlight_onViolation");
		}
		
		if (description.getText().isEmpty())
		{
			flag = false;
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
					answerFlag = true;
					errorMessage.add(constants.sumAnswer() + " " + msg);
					sumAnswer.getTextBox().addStyleName("higlight_onViolation");
				}
				else if (Integer.parseInt(sumAnswer.getValue()) < 2)
				{
					flag = false;
					answerFlag = true;
					errorMessage.add(constants.sumOfAnsMsg());
					sumAnswer.getTextBox().addStyleName("higlight_onViolation");
				}
				else if (infiniteChkBox.getValue() && sumAnswer.getValue().equals(SharedConstant.INFINITE_VALUE.toString()) == false)
				{
					flag = false;
					answerFlag = true;
					errorMessage.add(constants.sumOfAnsInfiniteMsg());		
					sumAnswer.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(sumTrueAnswer.getIntegerBox())) != "")
				{
					flag = false;
					answerFlag = true;
					errorMessage.add(constants.sumTrueAnswer() + " " + msg);
					sumTrueAnswer.getTextBox().addStyleName("higlight_onViolation");
				}
				else if (Integer.parseInt(sumTrueAnswer.getValue()) < 0)
				{
					flag = false;
					answerFlag = true;
					errorMessage.add(constants.sumOfTrueAnsMsg());
					sumTrueAnswer.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(sumFalseAnswer.getIntegerBox())) != "")
				{
					flag = false;
					answerFlag = true;
					errorMessage.add(constants.sumFalseAnswer() + " " + msg);
					sumFalseAnswer.getTextBox().addStyleName("higlight_onViolation");
				}
				else if (Integer.parseInt(sumFalseAnswer.getValue()) < 0)
				{
					flag = false;
					answerFlag = true;
					errorMessage.add(constants.sumOfFalseAnsMsg());
					sumFalseAnswer.getTextBox().addStyleName("higlight_onViolation");
				}
				
				if (answerFlag == false)
				{
					if (checkTextualAnswerCondition(Integer.parseInt(sumAnswer.getValue()), Integer.parseInt(sumTrueAnswer.getValue()), Integer.parseInt(sumFalseAnswer.getValue())) == false)
					{
						flag = false;
						errorMessage.add(constants.sumOfAnsValidateMsg());
						sumTrueAnswer.getTextBox().addStyleName("higlight_onViolation");
						sumFalseAnswer.getTextBox().addStyleName("higlight_onViolation");
					}
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(questionLength.getIntegerBox())) != "")
				{
					flag = false;
					errorMessage.add(constants.questionLength() + " " + msg);
					questionLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(answerLength.getIntegerBox())) != "")
				{
					flag = false;
					errorMessage.add(constants.answerLength() + " " + msg);
					answerLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if((msg = checkTextWidgetForDoubleWithRange(answerDiff.getTextBox(),0,100)) != "") {
					flag = false;
					errorMessage.add(constants.diffAnswer() + " " + msg);
					answerDiff.addStyleName("higlight_onViolation");
				}
				
				break;
			}
			
			case Imgkey:
			{
				String msg = "";				
				if ((msg = checkTextWidgetForNumber(keywordCount.getIntegerBox())) != "")
				{
					flag = false;
					errorMessage.add(constants.countKeyword() + " " + msg);
					keywordCount.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(minLetterForAutoComp.getIntegerBox())) != "")
				{
					flag = false;
					errorMessage.add(constants.minLetterAutoComplete() + " " + msg);
					minLetterForAutoComp.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(questionLength.getIntegerBox())) != "")
				{
					flag = false;
					errorMessage.add(constants.questionLength() + " " + msg);
					questionLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(answerLength.getIntegerBox())) != "")
				{
					flag = false;
					errorMessage.add(constants.answerLength() + " " + msg);
					answerLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(shortAnswerLength.getIntegerBox())) != "")
				{
					flag = false;
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
					errorMessage.add(constants.minLength() + " " + msg);
					minLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(questionLength.getIntegerBox())) != "")
				{
					flag = false;
					errorMessage.add(constants.questionLength() + " " + msg);
					questionLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(maxLength.getIntegerBox())) != "")
				{
					flag = false;
					errorMessage.add(constants.maxLength() + " " + msg);
					maxLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(minWordCount.getIntegerBox())) != "")
				{
					flag = false;
					errorMessage.add(constants.minWordCount() + " " + msg);
					minWordCount.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(maxWordCount.getIntegerBox())) != "")
				{
					flag = false;
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
					errorMessage.add(constants.questionLength() + " " + msg);
					questionLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(answerLength.getIntegerBox())) != "")
				{
					flag = false;
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
					errorMessage.add(constants.questionLength() + " " + msg);
					questionLength.getTextBox().addStyleName("higlight_onViolation");
				}
				
				if (multimediaType.getValue().equals(null))
				{
					flag = false;
					errorMessage.add(constants.multimediaType() + " " + constants.questionTypeErroMsg());
					multimediaType.getValueListBox().addStyleName("higlight_onViolation");
				}
				
				if (selectionType.getValue().equals(null))
				{
					flag = false;
					errorMessage.add(constants.selectionType() + " " + constants.questionTypeErroMsg());
					selectionType.getValueListBox().addStyleName("higlight_onViolation");
				}
				
				msg = "";
				if ((msg = checkTextWidgetForNumber(column.getIntegerBox())) != "")
				{
					flag = false;
					errorMessage.add(constants.column() + " " + msg);
					column.getTextBox().addStyleName("higlight_onViolation");
				}

				msg = "";
				if ((msg = checkTextWidgetForNumber(maxBytes.getIntegerBox())) != "")
				{
					flag = false;
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

	private boolean checkTextualAnswerCondition(int sumOfTotalAns, int sumOfTrueAns, int sumOfFalseAns) {
		
		if ( (sumOfTrueAns + sumOfFalseAns) <=1 )
			return true;
		else if ( (sumOfTrueAns + sumOfFalseAns) <= sumOfTotalAns )
			return true;
		
		return false;
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
		
	private String checkTextWidgetForDoubleWithRange(TextBox textBox, int start, int end) {
		String message = "";
		if (textBox.getText().isEmpty())
		{
			message = constants.questionTypeErroMsg();
		}
		else if (!ClientUtility.isDouble(textBox.getText()))
		{
			message = constants.questionTypeNumErrorMsg();
		}else if (ClientUtility.isDouble(textBox.getText()) == true) {
			double value = Double.parseDouble(textBox.getText());
			if(value < 0 || value > 100) {
				message = constants.questionTypeNumRanageErrorMsg();
			}
		}
		
		return message;
	}
	
	private void removeStyles()
	{
		shortName.getTextBox().removeStyleName("higlight_onViolation");
		longName.getTextBox().removeStyleName("higlight_onViolation");
		description.getTextArea().removeStyleName("higlight_onViolation");
		
		sumAnswer.getTextBox().removeStyleName("higlight_onViolation");
		sumTrueAnswer.getTextBox().removeStyleName("higlight_onViolation");
		sumFalseAnswer.getTextBox().removeStyleName("higlight_onViolation");
		questionLength.getTextBox().removeStyleName("higlight_onViolation");
		answerLength.getTextBox().removeStyleName("higlight_onViolation");
		answerDiff.getTextBox().removeStyleName("higlight_onViolation");
		
		keywordCount.getTextBox().removeStyleName("higlight_onViolation");
		minLetterForAutoComp.getTextBox().removeStyleName("higlight_onViolation");
		answerLength.getTextBox().removeStyleName("higlight_onViolation");
		shortAnswerLength.getTextBox().removeStyleName("higlight_onViolation");
		
		minLength.getTextBox().removeStyleName("higlight_onViolation");
		maxLength.getTextBox().removeStyleName("higlight_onViolation");
		minWordCount.getTextBox().removeStyleName("higlight_onViolation");
		maxWordCount.getTextBox().removeStyleName("higlight_onViolation");
		
		column.getTextBox().removeStyleName("higlight_onViolation");
		maxBytes.getTextBox().removeStyleName("higlight_onViolation");
		selectionType.getValueListBox().removeStyleName("higlight_onViolation");
		multimediaType.getValueListBox().removeStyleName("higlight_onViolation");
	}
}
