package medizin.client.ui.view;

import java.util.Arrays;

import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.widget.IconButton;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.SelectionType;
import medizin.shared.i18n.BmeConstants;

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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.ui.client.EntityProxyKeyProvider;

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
	TextBox descriptionTxtbox;	
	
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
	
	@UiField
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
	TextBox imageProportionTxtbox;
	
	@UiField
	Label linearPointLbl;
	 
	@UiField
	CheckBox linearPointChkBox;
		
	@UiField
	Label linearPercentageLbl;
	 
	@UiField
	TextBox linearPercentageTxtbox;
	
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
	
	@UiField
	Label thumbWidthLbl;
	 
	@UiField
	TextBox thumbWidthTxtbox;
	
	@UiField
	Label thumbHeightLbl;
	 
	@UiField
	TextBox thumbHeightTxtbox;
	
	/*@UiField
	Label propostionsLbl;
	 
	@UiField
	TextBox propostionsTxtbox;*/
	
	@UiField
	Label allowZoomOutLbl;
	
	@UiField
    CheckBox allowZoomOutChkBox;
	
	@UiField
	Label allowZoomInLbl;
	
	@UiField
    CheckBox allowZoomInChkBox;
	
	@UiField
	Label maxBytesLbl;
	 
	@UiField
	TextBox maxBytesTxtbox;
    /*@UiHandler ("isWeil")
    public void onWeilSelected(ClickEvent event){
    	if(isWeil.getValue()){
    		Document.get().getElementById("trueAnswers").getStyle().setVisibility(Visibility.HIDDEN);
    		Document.get().getElementById("falseAnswers").getStyle().setVisibility(Visibility.HIDDEN);
    		Document.get().getElementById("sumAnswers").getStyle().setVisibility(Visibility.HIDDEN);
    		Document.get().getElementById("maxLetters").getStyle().setVisibility(Visibility.HIDDEN);
    	} else {
    		Document.get().getElementById("trueAnswers").getStyle().setVisibility(Visibility.VISIBLE);
    		Document.get().getElementById("falseAnswers").getStyle().setVisibility(Visibility.VISIBLE);
    		Document.get().getElementById("sumAnswers").getStyle().setVisibility(Visibility.VISIBLE);
    		Document.get().getElementById("maxLetters").getStyle().setVisibility(Visibility.VISIBLE);
    	}
    	
    }*/
    
//
//    @UiField
//    TextBox displayRenderer;
//	
	

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
        //delegate.saveClicked(questionTypeName.getText(), isWeil.getValue(), trueAnswers.getValue(), falseAnswers.getValue(), sumAnswers.getValue(), maxLetters.getValue());
    
//    	
//    	String text = questionTypeName.getText();
//    	Boolean value = isWeil.getValue();
//    	Integer value2 = trueAnswers.getValue();
//    	Integer value3 = falseAnswers.getValue();
//    	Integer value4 = sumAnswers.getValue();
//    	Integer value5 = maxLetters.getValue(); 
//    	
    	
    //	delegate.saveClicked();
    	
    	boolean flag = false;
    	
    	if (shortNameTxtbox.getText().equals("") || longNameTxtbox.getText().equals("") || descriptionTxtbox.getText().equals(""))
    	{
    		flag = true;
    		Window.alert("Enter all Value.");
    		return;
    	}
    	
    	if (questionTypeListBox.getValue().equals(QuestionTypes.Textual) || questionTypeListBox.getValue().equals(QuestionTypes.Sort))
        {
     	   if (sumAnswerTxtbox.getText().equals("") || sumTrueAnswerTxtbox.getText().equals("") || sumFalseAnswerTxtbox.getText().equals("") ||
     			   questionLengthTxtbox.getText().equals("") || answerLengthTxtbox.getText().equals("") || answerDiffTxtbox.getText().equals(""))
     	   {
     		  flag = true;
     		  Window.alert("Enter all Value.");
     	   }
        }
        if (questionTypeListBox.getValue().equals(QuestionTypes.Imgkey))
        {
        	if(keywordCountTxtbox.getText().equals("") || minLetterForAutoCompTxtbox.getText().equals("") || answerLengthTxtbox.getText().equals("") ||
        			shortAnswerLengthTxtbox.getText().equals("") || imageWidthTxtbox.getText().equals("") ||
        			imageLengthTxtbox.getText().equals("") || imageProportionTxtbox.getText().equals(""))
        	{
        		flag = true;
        		Window.alert("Enter all Image Key Value.");
        	}
     	   
        }
        if (questionTypeListBox.getValue().equals(QuestionTypes.ShowInImage))
        {
        	if (imageWidthTxtbox.getText().equals("") || imageLengthTxtbox.getText().equals("") || imageProportionTxtbox.getText().equals("") ||
        			linearPercentageTxtbox.getText().equals(""))
        	{
        		flag = true;
        		Window.alert("Enter all Area Value.");
        	}
        }
        if (questionTypeListBox.getValue().equals(QuestionTypes.LongText))
        {
        	if(minLengthTxtbox.getText().equals("") || maxLengthTxtbox.getText().equals("") || minWordCountTxtbox.getText().equals("") || maxWordCountTxtbox.getText().equals(""))
        	{
        		flag = true;
        		Window.alert("Enter all LongText Value.");
        	}
        }
        if (questionTypeListBox.getValue().equals(QuestionTypes.Matrix))
        {
     	   if (maxLengthTxtbox.getText().equals(""))
     	   {
     		  flag = true;
     		  Window.alert("Enter all Matrix Value.");
     	   }
        }
        if (questionTypeListBox.getValue().equals(QuestionTypes.MCQ))
        {
     	   if (imageWidthTxtbox.getText().equals("") || imageLengthTxtbox.getText().equals("") || imageProportionTxtbox.getText().equals("") ||
     			 multimediaTypeListBox.getValue().equals(null) || selectionTypeListBox.getValue().equals(null) || columnTxtbox.getText().equals("") ||
     			 thumbWidthTxtbox.getText().equals("") || thumbHeightTxtbox.getText().equals("") || maxBytesTxtbox.getText().equals(""))
     	   {
     		  flag = true;
     		  Window.alert("Enter all MCQ Value.");
     	   }
        }
        
        if(flag == false)
    	{
    		delegate.saveClicked(proxy);
    	}
    
    }


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
    	   sumAnswerTxtbox.setValue(proxy.getSumAnswer().toString());
    	   sumTrueAnswerTxtbox.setValue(proxy.getSumTrueAnswer().toString());
    	   sumFalseAnswerTxtbox.setValue(proxy.getSumFalseAnswer().toString());
    	   questionLengthTxtbox.setValue(proxy.getQuestionLength().toString());
    	   answerLengthTxtbox.setValue(proxy.getAnswerLength().toString());
    	   answerDiffTxtbox.setValue(proxy.getDiffBetAnswer().toString());
    	   queHaveImgChkBox.setValue(proxy.getQueHaveImage());
    	   queHaveVideoChkBox.setValue(proxy.getQueHaveVideo());
    	   queHaveSoundChkBox.setValue(proxy.getQueHaveSound());
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.Imgkey))
       {
    	   keywordCountTxtbox.setValue(proxy.getKeywordCount().toString());
    	   showAutoCompleteChkBox.setValue(proxy.getShowAutocomplete());
    	   isDictionaryKeywordChkBox.setValue(proxy.getIsDictionaryKeyword());
    	   allowTypingChkBox.setValue(proxy.getAllowTyping());
    	   minLetterForAutoCompTxtbox.setValue(proxy.getMinAutoCompleteLetter().toString());
    	   answerLengthTxtbox.setValue(proxy.getAnswerLength().toString());
    	   acceptNonKeywordChkBox.setValue(proxy.getAcceptNonKeyword());
    	   shortAnswerLengthTxtbox.setValue(proxy.getLengthShortAnswer().toString());
    	   imageWidthTxtbox.setValue(proxy.getImageWidth().toString());
    	   imageLengthTxtbox.setValue(proxy.getImageHeight().toString());
    	   imageProportionTxtbox.setValue(proxy.getImageProportion().toString());
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.ShowInImage))
       {
    	   questionLengthTxtbox.setValue(proxy.getQuestionLength().toString());
    	   imageWidthTxtbox.setValue(proxy.getImageWidth().toString());
    	   imageLengthTxtbox.setValue(proxy.getImageHeight().toString());
    	   imageProportionTxtbox.setValue(proxy.getImageProportion().toString());
    	   linearPointChkBox.setValue(proxy.getLinearPoint());
    	   linearPercentageTxtbox.setValue(proxy.getLinearPercentage().toString());	    	   
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.LongText))
       {
    	   keywordHighlightChkBox.setValue(proxy.getKeywordHighlight());
    	   richTextChkBox.setValue(proxy.getRichText());
    	   minLengthTxtbox.setValue(proxy.getMinLength().toString());
    	   maxLengthTxtbox.setValue(proxy.getMaxLength().toString());
    	   minWordCountTxtbox.setValue(proxy.getMinWordCount().toString());
    	   maxWordCountTxtbox.setValue(proxy.getMaxWordCount().toString());
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.Matrix))
       {
    	   maxLengthTxtbox.setValue(proxy.getMaxLength().toString());
    	   oneToOneAssChkBox.setValue(proxy.getAllowOneToOneAss());
       }
       else if (proxy.getQuestionType().equals(QuestionTypes.MCQ))
       {
    	   imageWidthTxtbox.setValue(proxy.getImageWidth().toString());
    	   imageLengthTxtbox.setValue(proxy.getImageHeight().toString());
    	   imageProportionTxtbox.setValue(proxy.getImageProportion().toString());
    	   multimediaTypeListBox.setValue(proxy.getMultimediaType());
    	   selectionTypeListBox.setValue(proxy.getSelectionType());
    	   columnTxtbox.setValue(proxy.getColumns().toString());
    	   thumbWidthTxtbox.setValue(proxy.getThumbWidth().toString());
    	   thumbHeightTxtbox.setValue(proxy.getThumbHeight().toString());
    	   richTextChkBox.setValue(proxy.getRichText());
    	   allowZoomOutChkBox.setValue(proxy.getAllowZoomOut());
    	   allowZoomInChkBox.setValue(proxy.getAllowZoomIn());
    	   maxBytesTxtbox.setValue(proxy.getMaxBytes().toString());
       }
       
    }

	public QuestiontypesEditViewImpl() {
		
		initWidget(uiBinder.createAndBindUi(this));
		
		questionTypePanel.selectTab(0);
		questionTypePanel.getTabBar().setTabText(0, constants.manageQuestionType());
		
		save.setText(constants.save());
		cancel.setText(constants.cancel());
		multimediaTypeListBox.setAcceptableValues(Arrays.asList(MultimediaType.values()));
		multimediaTypeListBox.setValue(MultimediaType.Image);
		
		selectionTypeListBox.setAcceptableValues(Arrays.asList(SelectionType.values()));
		selectionTypeListBox.setValue(SelectionType.Choose);
		
		questionTypeListBox.setAcceptableValues(Arrays.asList(QuestionTypes.values()));
		questionTypeListBox.setValue(QuestionTypes.Textual);
		questionTypeListBox.addValueChangeHandler(new ValueChangeHandler<QuestionTypes>() {

			@Override
			public void onValueChange(ValueChangeEvent<QuestionTypes> event) {
				disableField(questionTypeListBox.getValue());
			}
		});
		
		DOM.setElementPropertyBoolean(instituteListBox.getElement(), "disabled", true);
		instituteListBox.setWidth("120px");
		questionTypeListBox.setWidth("120px");
		
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
		imageWidthLbl.setText(constants.imgWidth());
		imageLengthLbl.setText(constants.imgLength());
		imageProportionLbl.setText(constants.imgProportion());
		
		linearPointLbl.setText(constants.linearPoint());
		linearPercentageLbl.setText(constants.linearPercentage());
		
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
		thumbWidthLbl.setText(constants.thumbWidth());
		thumbHeightLbl.setText(constants.thumbHeight());
		//propostionsLbl.setText(constants.propostions());
		allowZoomOutLbl.setText(constants.allowZoomOut());
		allowZoomInLbl.setText(constants.allowZoomIn());
		maxBytesLbl.setText(constants.maxBytes());
	}


	public void showTextualField()
	{
		Document.get().getElementById("sumAnswer").getStyle().clearDisplay();
		Document.get().getElementById("sumTrueAnswer").getStyle().clearDisplay();
		Document.get().getElementById("sumFalseAnswer").getStyle().clearDisplay();
		Document.get().getElementById("questionLength").getStyle().clearDisplay();
		Document.get().getElementById("answerLength").getStyle().clearDisplay();
		Document.get().getElementById("answerDiff").getStyle().clearDisplay();
		Document.get().getElementById("queHaveImg").getStyle().clearDisplay();
		Document.get().getElementById("queHaveVideo").getStyle().clearDisplay();
		Document.get().getElementById("queHaveSound").getStyle().clearDisplay();
		
	}
	
	public void disableTextualField()
	{
		Document.get().getElementById("sumAnswer").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("sumTrueAnswer").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("sumFalseAnswer").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("questionLength").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("answerLength").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("answerDiff").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("queHaveImg").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("queHaveVideo").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("queHaveSound").getStyle().setDisplay(Display.NONE);
	}
	
	public void showImgKeyField()
	{
		Document.get().getElementById("keywordCount").getStyle().clearDisplay();
		Document.get().getElementById("showAutoComplete").getStyle().clearDisplay();
		Document.get().getElementById("isDictionaryKeyword").getStyle().clearDisplay();
		Document.get().getElementById("allowTyping").getStyle().clearDisplay();
		Document.get().getElementById("minLetterForAutoComp").getStyle().clearDisplay();
		Document.get().getElementById("answerLength").getStyle().clearDisplay();
		Document.get().getElementById("acceptNonKeyword").getStyle().clearDisplay();
		Document.get().getElementById("shortAnswerLength").getStyle().clearDisplay();
		Document.get().getElementById("imageWidth").getStyle().clearDisplay();
		Document.get().getElementById("imageLength").getStyle().clearDisplay();
		Document.get().getElementById("imageProportion").getStyle().clearDisplay();
	}
	
	public void disableImgKeyField()
	{
		Document.get().getElementById("keywordCount").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("showAutoComplete").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("isDictionaryKeyword").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("allowTyping").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("minLetterForAutoComp").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("answerLength").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("acceptNonKeyword").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("shortAnswerLength").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("imageWidth").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("imageLength").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("imageProportion").getStyle().setDisplay(Display.NONE);
	}
	
	public void showShowInImageField()
	{
		Document.get().getElementById("questionLength").getStyle().clearDisplay();
		Document.get().getElementById("imageWidth").getStyle().clearDisplay();
		Document.get().getElementById("imageLength").getStyle().clearDisplay();
		Document.get().getElementById("imageProportion").getStyle().clearDisplay();
		Document.get().getElementById("linearPoint").getStyle().clearDisplay();
		Document.get().getElementById("linearPercentage").getStyle().clearDisplay();
	}
	
	public void disableShowInImageField()
	{
		Document.get().getElementById("questionLength").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("imageWidth").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("imageLength").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("imageProportion").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("linearPoint").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("linearPercentage").getStyle().setDisplay(Display.NONE);
	}

	public void showLongtextField()
	{
		Document.get().getElementById("keywordHighlight").getStyle().clearDisplay();
		Document.get().getElementById("richText").getStyle().clearDisplay();
		Document.get().getElementById("minLength").getStyle().clearDisplay();
		Document.get().getElementById("maxLength").getStyle().clearDisplay();
		Document.get().getElementById("minWordCount").getStyle().clearDisplay();
		Document.get().getElementById("maxWordCount").getStyle().clearDisplay();
	}
	
	public void disableLongtextField()
	{
		Document.get().getElementById("keywordHighlight").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("richText").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("minLength").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("maxLength").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("minWordCount").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("maxWordCount").getStyle().setDisplay(Display.NONE);
	}
	
	public void showMatrixField()
	{
		Document.get().getElementById("maxLength").getStyle().clearDisplay();
		Document.get().getElementById("oneToOneAss").getStyle().clearDisplay();
	}
	
	public void disableMatrixFied()
	{
		Document.get().getElementById("maxLength").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("oneToOneAss").getStyle().setDisplay(Display.NONE);
	}
	
	public void showMCQField()
	{
		Document.get().getElementById("imageWidth").getStyle().clearDisplay();
		Document.get().getElementById("imageLength").getStyle().clearDisplay();
		Document.get().getElementById("imageProportion").getStyle().clearDisplay();
		Document.get().getElementById("multimediaType").getStyle().clearDisplay();
		Document.get().getElementById("selectionType").getStyle().clearDisplay();
		Document.get().getElementById("column").getStyle().clearDisplay();
		Document.get().getElementById("thumbWidth").getStyle().clearDisplay();
		Document.get().getElementById("thumbHeight").getStyle().clearDisplay();
		Document.get().getElementById("richText").getStyle().clearDisplay();
		Document.get().getElementById("allowZoomOut").getStyle().clearDisplay();
		Document.get().getElementById("allowZoomIn").getStyle().clearDisplay();
		Document.get().getElementById("maxBytes").getStyle().clearDisplay();
	}
	
	public void disableMCQField()
	{
		Document.get().getElementById("imageWidth").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("imageLength").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("imageProportion").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("multimediaType").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("selectionType").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("column").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("thumbWidth").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("thumbHeight").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("richText").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("allowZoomOut").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("allowZoomIn").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("maxBytes").getStyle().setDisplay(Display.NONE);
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
			disableImgKeyField();
			disableShowInImageField();
			disableLongtextField();
			disableMatrixFied();
			disableMCQField();
			showTextualField();					
		}
		else if (questionTypes.equals(QuestionTypes.Imgkey))
		{
			disableTextualField();
			disableShowInImageField();
			disableLongtextField();
			disableMatrixFied();
			disableMCQField();
			showImgKeyField();					
		}
		else if (questionTypes.equals(QuestionTypes.ShowInImage))
		{
			disableTextualField();
			disableImgKeyField();
			disableLongtextField();
			disableMatrixFied();
			disableMCQField();
			showShowInImageField();
		}
		else if (questionTypes.equals(QuestionTypes.LongText))
		{
			disableTextualField();
			disableImgKeyField();
			disableShowInImageField();
			disableMatrixFied();
			disableMCQField();
			showLongtextField();
		}
		else if (questionTypes.equals(QuestionTypes.Matrix))
		{
			disableTextualField();
			disableImgKeyField();
			disableShowInImageField();
			disableLongtextField();
			disableMCQField();
			showMatrixField();
		} 
		else if (questionTypes.equals(QuestionTypes.MCQ))
		{
			disableTextualField();
			disableImgKeyField();
			disableShowInImageField();
			disableLongtextField();
			disableMatrixFied();
			showMCQField();
		} 
		else
		{
			disableTextualField();
			disableImgKeyField();
			disableShowInImageField();
			disableLongtextField();
			disableMatrixFied();
			disableMCQField();
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
	    	   answerLengthTxtbox.setValue("");
	    	   acceptNonKeywordChkBox.setValue(false);
	    	   shortAnswerLengthTxtbox.setValue("");
	    	   imageWidthTxtbox.setValue("");
	    	   imageLengthTxtbox.setValue("");
	    	   imageProportionTxtbox.setValue("");
	       }
	       else if (questionTypes.equals(QuestionTypes.ShowInImage))
	       {
	    	   questionLengthTxtbox.setValue("");
	    	   imageWidthTxtbox.setValue("");
	    	   imageLengthTxtbox.setValue("");
	    	   imageProportionTxtbox.setValue("");
	    	   linearPointChkBox.setValue(false);
	    	   linearPercentageTxtbox.setValue("");	    	   
	       }
	       else if (questionTypes.equals(QuestionTypes.LongText))
	       {
	    	   keywordHighlightChkBox.setValue(false);
	    	   richTextChkBox.setValue(false);
	    	   minLengthTxtbox.setValue("");
	    	   maxLengthTxtbox.setValue("");
	    	   minWordCountTxtbox.setValue("");
	    	   maxWordCountTxtbox.setValue("");
	       }
	       else if (questionTypes.equals(QuestionTypes.Matrix))
	       {
	    	   maxLengthTxtbox.setValue("");
	    	   oneToOneAssChkBox.setValue(false);
	       }
	       else if (questionTypes.equals(QuestionTypes.MCQ))
	       {
	    	   imageWidthTxtbox.setValue("");
	    	   imageLengthTxtbox.setValue("");
	    	   imageProportionTxtbox.setValue("");
	    	   multimediaTypeListBox.setValue(MultimediaType.Image);
	    	   selectionTypeListBox.setValue(SelectionType.Choose);
	    	   columnTxtbox.setValue("");
	    	   thumbWidthTxtbox.setValue("");
	    	   thumbHeightTxtbox.setValue("");
	    	   richTextChkBox.setValue(false);
	    	   allowZoomOutChkBox.setValue(false);
	    	   allowZoomInChkBox.setValue(false);
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

	public TextBox getDescriptionTxtbox() {
		return descriptionTxtbox;
	}

	public void setDescriptionTxtbox(TextBox descriptionTxtbox) {
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

	public TextBox getImageWidthTxtbox() {
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
	}

	public CheckBox getLinearPointChkBox() {
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
	}

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

	public TextBox getThumbWidthTxtbox() {
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
	}

	public TextBox getMaxBytesTxtbox() {
		return maxBytesTxtbox;
	}

	public void setMaxBytesTxtbox(TextBox maxBytesTxtbox) {
		this.maxBytesTxtbox = maxBytesTxtbox;
	}
	
	
}
