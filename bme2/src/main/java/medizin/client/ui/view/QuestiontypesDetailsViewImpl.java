package medizin.client.ui.view;

import java.util.ArrayList;

import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.TabPanelHelper;
import medizin.shared.QuestionTypes;
import medizin.shared.i18n.BmeConstants;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import static medizin.client.util.ClientUtility.toStringUtility;

public class QuestiontypesDetailsViewImpl extends Composite implements QuestiontypesDetailsView  {

		private static UserDetailsViewImplUiBinder uiBinder = GWT
				.create(UserDetailsViewImplUiBinder.class);
	
		interface UserDetailsViewImplUiBinder extends
				UiBinder<Widget, QuestiontypesDetailsViewImpl> {
		}
	
		public BmeConstants constants = GWT.create(BmeConstants.class);
		 
		private Presenter presenter;
	
		private Delegate delegate;
		
	    @UiField
		IconButton edit;
	    
	    @UiField
		IconButton delete;
	    
	   
		/*@UiField
	    HasClickHandlers edit;	
	    */
	    
	    @UiField
	    SpanElement displayRenderer;	 
	    
		

		/*@UiField
		Image arrow;*/
		

	    QuestionTypeProxy proxy;
	    
	    @UiField
	    Label shortNameLbl;
		
		@UiField
		Label shortNameValLbl;
		
		@UiField
	    Label longNameLbl;
		
		@UiField
		Label longNameValLbl;
		
		@UiField
	    Label descriptionLbl;
		
		@UiField
		Label descriptionValLbl;	
		
		@UiField
	    Label instituteLbl;
		
	    @UiField 
	    Label instituteValLbl;
	    
		@UiField
	    Label questionTypeLbl;
		
	    @UiField 
	    Label questionTypeValLbl;
	    
	    @UiField
	    Label sumAnswerLbl;
		
		@UiField
		Label sumAnswerValLbl;	

		@UiField
		Label sumTrueAnswerLbl;
		 
		@UiField
		Label sumTrueAnswerValLbl;
		
		@UiField
		Label sumFalseAnswerLbl;
		 
		@UiField
		Label sumFalseAnswerValLbl;
		
		@UiField
		Label questionLengthLbl;
		 
		@UiField
		Label questionLengthValLbl;
		
		@UiField
		Label answerLengthLbl;
		 
		@UiField
		Label answerLengthValLbl;
		
		@UiField
		Label answerDiffLbl;
		 
		@UiField
		Label answerDiffValLbl;
		
		@UiField
		Label queHaveImgLbl;
		
		@UiField
		Label queHaveImgValLbl;
		
		@UiField
		Label queHaveVideoLbl;
		
		@UiField
		Label queHaveVideoValLbl;
		
		@UiField
		Label queHaveSoundLbl;
		
		@UiField
		Label queHaveSoundValLbl;
		
		@UiField
		Label keywordCountLbl;
		 
		@UiField
		Label keywordCountValLbl;
			
		@UiField
		TabPanel questionTypeDetailPanel;
		
		@UiField
		Label showAutoCompleteLbl;
		
		@UiField
		Label showAutoCompleteValLbl;
		
		@UiField
		Label isDictionaryKeywordLbl;
		
		@UiField
		Label isDictionaryKeywordValLbl;
		
		@UiField
		Label allowTypingLbl;
		
		@UiField
		Label allowTypingValLbl;
		
		@UiField
		Label minLetterForAutoCompLbl;
		 
		@UiField
		Label minLetterForAutoCompValLbl;
		
		@UiField
		Label acceptNonKeywordLbl;
		
		@UiField
		Label acceptNonKeywordValLbl;
		
		@UiField
		Label shortAnswerLengthLbl;
		 
		@UiField
		Label shortAnswerLengthValLbl;
		
		@UiField
		Label imageWidthLbl;
		 
		@UiField
		Label imageWidthValLbl;
		
		@UiField
		Label imageLengthLbl;
		 
		@UiField
		Label imageLengthValLbl;
		
		@UiField
		Label imageProportionLbl;
		 
		@UiField
		Label imageProportionValLbl;
		
		/*@UiField
		Label linearPointLbl;
		 
		@UiField
		Label linearPointValLbl;
			
		@UiField
		Label linearPercentageLbl;
		 
		@UiField
		Label linearPercentageValLbl;*/
		
		@UiField
		Label keywordHighlightLbl;
		
		@UiField
		Label keywordHighlightValLbl;
		
		@UiField
		Label richTextLbl;
		
		@UiField
		Label richTextValLbl;
		
		@UiField
		Label minLengthLbl;
		 
		@UiField
		Label minLengthValLbl;
		
		@UiField
		Label maxLengthLbl;
		 
		@UiField
		Label maxLengthValLbl;
		
		@UiField
		Label minWordCountLbl;
		 
		@UiField
		Label minWordCountValLbl;
		
		@UiField
		Label maxWordCountLbl;
		 
		@UiField
		Label maxWordCountValLbl;
		
		@UiField
		Label oneToOneAssLbl;
		
		@UiField
		Label oneToOneAssValLbl;
		
		@UiField
	    Label multimediaTypeLbl;
		
	    @UiField 
	    Label multimediaTypeValLbl;
	    
	    @UiField
	    Label selectionTypeLbl;
		
	    @UiField 
	    Label selectionTypeValLbl;
	    
	    @UiField
		Label columnLbl;
		 
		@UiField
		Label columnValLbl;
		
		/*@UiField
		Label thumbWidthLbl;
		 
		@UiField
		Label thumbWidthValLbl;
		
		@UiField
		Label thumbHeightLbl;
		 
		@UiField
		Label thumbHeightValLbl;
		
		@UiField
		Label allowZoomOutLbl;
		
		@UiField
		Label allowZoomOutValLbl;
		
		@UiField
		Label allowZoomInLbl;
		
		@UiField
		Label allowZoomInValLbl;*/
		
		@UiField
		Label maxBytesLbl;
		 
		@UiField
		Label maxBytesValLbl;
	    
		private static final ArrayList<String> textualSortList = Lists.newArrayList("sumAnswer","sumTrueAnswer","sumFalseAnswer","questionLength","answerLength","answerDiff","queHaveImg","queHaveVideo","queHaveSound");
		
		private static final ArrayList<String> imgKeyList = Lists.newArrayList("questionLength","keywordCount","showAutoComplete","isDictionaryKeyword","allowTyping","minLetterForAutoComp","answerLength","acceptNonKeyword","shortAnswerLength","imageWidth","imageLength","imageProportion");

		private static final ArrayList<String> showInImgList = Lists.newArrayList("questionLength"/*,"answerLength","imageWidth","imageLength","imageProportion","linearPoint","linearPercentage"*/);
		
		private static final ArrayList<String> longTextList = Lists.newArrayList("questionLength","keywordHighlight","richText","minLength","maxLength","minWordCount","maxWordCount");
		
		private static final ArrayList<String> matrixList = Lists.newArrayList("questionLength","answerLength","oneToOneAss");
		
		private static final ArrayList<String> mcqList = Lists.newArrayList("questionLength",/*"imageWidth","imageLength","imageProportion",*/"multimediaType","selectionType","column",/*"thumbWidth","thumbHeight",*/"richText",/*"allowZoomOut","allowZoomIn",*/"maxBytes");
		
	    @UiHandler("edit")
	    public void onEditClicked(ClickEvent e) {
	        delegate.editClicked();
	      
	    }
	    
	    
	    @UiHandler("delete")
	    public void onDeleteClicked(ClickEvent e) {
	        delegate.deleteClicked();
	    }

	    public void setValue(QuestionTypeProxy proxy) {
	    	
	    	delegate.getQuestionCount(proxy,new Function<Long, Void>() {

				@Override
				public Void apply(Long input) {
					if(input != null && input > 0) {
						delete.setEnabled(false);	
					}
					return null;
				}
			});
	    	
	    	displayRenderer.setInnerText(proxy.getShortName());
	    	shortNameValLbl.setText(proxy.getShortName());
	    	longNameValLbl.setText(proxy.getLongName());
	    	descriptionValLbl.setText(proxy.getDescription());
	    	questionTypeValLbl.setText(toStringUtility(proxy.getQuestionType()));
	    	instituteValLbl.setText(proxy.getInstitution().getInstitutionName());
	    	
	       if (proxy.getQuestionType().equals(QuestionTypes.Textual) || proxy.getQuestionType().equals(QuestionTypes.Sort))
	       {
	    	   sumAnswerValLbl.setText(toStringUtility(proxy.getSumAnswer()));
	    	   sumTrueAnswerValLbl.setText(toStringUtility(proxy.getSumTrueAnswer()));
	    	   sumFalseAnswerValLbl.setText(toStringUtility(proxy.getSumFalseAnswer()));
	    	   questionLengthValLbl.setText(toStringUtility(proxy.getQuestionLength()));
	    	   answerLengthValLbl.setText(toStringUtility(proxy.getAnswerLength()));
	    	   answerDiffValLbl.setText(toStringUtility(proxy.getDiffBetAnswer()));
	    	   queHaveImgValLbl.setText(toStringUtility(proxy.getQueHaveImage()));
	    	   queHaveVideoValLbl.setText(toStringUtility(proxy.getQueHaveVideo()));
	    	   queHaveSoundValLbl.setText(toStringUtility(proxy.getQueHaveSound()));
	       }
	       else if (proxy.getQuestionType().equals(QuestionTypes.Imgkey))
	       {
	    	   questionLengthValLbl.setText(toStringUtility(proxy.getQuestionLength()));
	    	   keywordCountValLbl.setText(toStringUtility(proxy.getKeywordCount()));
	    	   showAutoCompleteValLbl.setText(toStringUtility(proxy.getShowAutocomplete()));
	    	   isDictionaryKeywordValLbl.setText(toStringUtility(proxy.getIsDictionaryKeyword()));
	    	   allowTypingValLbl.setText(toStringUtility(proxy.getAllowTyping()));
	    	   minLetterForAutoCompValLbl.setText(toStringUtility(proxy.getMinAutoCompleteLetter()));
	    	   answerLengthValLbl.setText(toStringUtility(proxy.getAnswerLength()));
	    	   acceptNonKeywordValLbl.setText(toStringUtility(proxy.getAcceptNonKeyword()));
	    	   shortAnswerLengthValLbl.setText(toStringUtility(proxy.getLengthShortAnswer()));
	    	   imageWidthValLbl.setText(toStringUtility(proxy.getImageWidth()));
	    	   imageLengthValLbl.setText(toStringUtility(proxy.getImageHeight()));
	    	   imageProportionValLbl.setText(toStringUtility(proxy.getImageProportion()));
	       }
	       else if (proxy.getQuestionType().equals(QuestionTypes.ShowInImage))
	       {
	    	   questionLengthValLbl.setText(toStringUtility(proxy.getQuestionLength()));
	    	   answerLengthValLbl.setText(toStringUtility(proxy.getAnswerLength()));
	    	   imageWidthValLbl.setText(toStringUtility(proxy.getImageWidth()));
	    	   imageLengthValLbl.setText(toStringUtility(proxy.getImageHeight()));
	    	   imageProportionValLbl.setText(toStringUtility(proxy.getImageProportion()));
	    	   /*linearPointValLbl.setText(toStringUtility(proxy.getLinearPoint()));
	    	   linearPercentageValLbl.setText(toStringUtility(proxy.getLinearPercentage()));*/	    	   
	       }
	       else if (proxy.getQuestionType().equals(QuestionTypes.LongText))
	       {
	    	   questionLengthValLbl.setText(toStringUtility(proxy.getQuestionLength()));
	    	   keywordHighlightValLbl.setText(toStringUtility(proxy.getKeywordHighlight()));
	    	   richTextValLbl.setText(toStringUtility(proxy.getRichText()));
	    	   minLengthValLbl.setText(toStringUtility(proxy.getMinLength()));
	    	   maxLengthValLbl.setText(toStringUtility(proxy.getMaxLength()));
	    	   minWordCountValLbl.setText(toStringUtility(proxy.getMinWordCount()));
	    	   maxWordCountValLbl.setText(toStringUtility(proxy.getMaxWordCount()));
	       }
	       else if (proxy.getQuestionType().equals(QuestionTypes.Matrix))
	       {
	    	   //maxLengthValLbl.setText(proxy.getMaxLength()));
	    	   questionLengthValLbl.setText(toStringUtility(proxy.getQuestionLength()));
	    	   answerLengthValLbl.setText(toStringUtility(proxy.getAnswerLength()));
	    	   oneToOneAssValLbl.setText(toStringUtility(proxy.getAllowOneToOneAss()));
	       }
	       else if (proxy.getQuestionType().equals(QuestionTypes.MCQ))
	       {
	    	   questionLengthValLbl.setText(toStringUtility(proxy.getQuestionLength()));
	    	   /*imageWidthValLbl.setText(toStringUtility(proxy.getImageWidth()));
	    	   imageLengthValLbl.setText(toStringUtility(proxy.getImageHeight()));
	    	   imageProportionValLbl.setText(toStringUtility(proxy.getImageProportion()));*/
	    	   multimediaTypeValLbl.setText(toStringUtility(proxy.getMultimediaType()));
	    	   selectionTypeValLbl.setText(toStringUtility(proxy.getSelectionType()));
	    	   columnValLbl.setText(toStringUtility(proxy.getColumns()));
	    	   richTextValLbl.setText(toStringUtility(proxy.getRichText()));
	    	   /*thumbWidthValLbl.setText(toStringUtility(proxy.getThumbWidth()));
	    	   thumbHeightValLbl.setText(toStringUtility(proxy.getThumbHeight()));	    	   
	    	   allowZoomOutValLbl.setText(toStringUtility(proxy.getAllowZoomOut()));
	    	   allowZoomInValLbl.setText(toStringUtility(proxy.getAllowZoomIn()));*/
	    	   maxBytesValLbl.setText(toStringUtility(proxy.getMaxBytes()));
	       }
	       
	       disableField(proxy.getQuestionType());
	    }

		public QuestiontypesDetailsViewImpl() {
			initWidget(uiBinder.createAndBindUi(this));
			
			questionTypeDetailPanel.selectTab(0);
			questionTypeDetailPanel.getTabBar().setTabText(0, constants.manageQuestionType());
			TabPanelHelper.moveTabBarToBottom(questionTypeDetailPanel);
			
			
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
			/*thumbWidthLbl.setText(constants.thumbWidth());
			thumbHeightLbl.setText(constants.thumbHeight());
			allowZoomOutLbl.setText(constants.allowZoomOut());
			allowZoomInLbl.setText(constants.allowZoomIn());*/
			maxBytesLbl.setText(constants.maxBytes());
		}
	
	
		@Override
		public void setName(String helloName) {
			// TODO Auto-generated method stub
			
		}
	
	
	
		@Override
		public void setPresenter(Presenter presenter) {
			this.presenter = presenter;
			
		}
	
		@Override
		public void setDelegate(Delegate delegate) {
			this.delegate = delegate;
			
		}

		public void disableField(QuestionTypes questionTypes)
		{
			if (questionTypes.equals(QuestionTypes.Textual) || questionTypes.equals(QuestionTypes.Sort))
			{
				disableField(imgKeyList);
				disableField(showInImgList);
				disableField(matrixList);
				disableField(longTextList);
				disableField(mcqList);
				showField(textualSortList);			
			}
			else if (questionTypes.equals(QuestionTypes.Imgkey))
			{
				disableField(textualSortList);
				disableField(showInImgList);
				disableField(matrixList);
				disableField(longTextList);
				disableField(mcqList);
				showField(imgKeyList);			
			}
			else if (questionTypes.equals(QuestionTypes.ShowInImage))
			{
				disableField(textualSortList);
				disableField(imgKeyList);
				disableField(matrixList);
				disableField(longTextList);
				disableField(mcqList);
				showField(showInImgList);
			}
			else if (questionTypes.equals(QuestionTypes.LongText))
			{
				disableField(textualSortList);
				disableField(imgKeyList);
				disableField(matrixList);
				disableField(showInImgList);
				disableField(mcqList);
				showField(longTextList);
			}
			else if (questionTypes.equals(QuestionTypes.Matrix))
			{
				disableField(textualSortList);
				disableField(imgKeyList);
				disableField(showInImgList);
				disableField(longTextList);
				disableField(mcqList);
				showField(matrixList);
			} 
			else if (questionTypes.equals(QuestionTypes.MCQ))
			{
				disableField(textualSortList);
				disableField(imgKeyList);
				disableField(matrixList);
				disableField(longTextList);
				disableField(showInImgList);
				showField(mcqList);
			} 
			else
			{
				disableField(mcqList);
				disableField(textualSortList);
				disableField(imgKeyList);
				disableField(matrixList);
				disableField(longTextList);
				disableField(showInImgList);
			}
		}
		
		public void showField(ArrayList<String> list)
		{
			for(String str : list)
			{
				Document.get().getElementById(str).getStyle().clearDisplay();
			}
		}
		
		public void disableField(ArrayList<String> list)
		{
			for(String str : list)
			{
				Document.get().getElementById(str).getStyle().setDisplay(Display.NONE);
			}
		}
		
		/*public void showTextualField()
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
			Document.get().getElementById("questionLength").getStyle().clearDisplay();
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
			Document.get().getElementById("questionLength").getStyle().setDisplay(Display.NONE);
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
			Document.get().getElementById("answerLength").getStyle().clearDisplay();
			Document.get().getElementById("imageWidth").getStyle().clearDisplay();
			Document.get().getElementById("imageLength").getStyle().clearDisplay();
			Document.get().getElementById("imageProportion").getStyle().clearDisplay();
			Document.get().getElementById("linearPoint").getStyle().clearDisplay();
			Document.get().getElementById("linearPercentage").getStyle().clearDisplay();
		}
		
		public void disableShowInImageField()
		{
			Document.get().getElementById("questionLength").getStyle().setDisplay(Display.NONE);
			Document.get().getElementById("answerLength").getStyle().setDisplay(Display.NONE);
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
			Document.get().getElementById("questionLength").getStyle().clearDisplay();
			Document.get().getElementById("answerLength").getStyle().clearDisplay();
			Document.get().getElementById("oneToOneAss").getStyle().clearDisplay();
		}
		
		public void disableMatrixFied()
		{
			Document.get().getElementById("questionLength").getStyle().setDisplay(Display.NONE);
			Document.get().getElementById("answerLength").getStyle().setDisplay(Display.NONE);
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
		}*/
		
		/*@UiHandler("arrow")
		void handleClick(ClickEvent e) {
			if (questionTypeDisclosurePanel.isOpen()) {
				questionTypeDisclosurePanel.setOpen(false);
				arrow.setUrl("/ApplicationEntry/gwt/unibas/images/arrowdownselect.png");// set
																					// url
																					// of
																					// up
																					// image

			} else {
				
				questionTypeDisclosurePanel.setOpen(true);
				arrow.setUrl("/ApplicationEntry/gwt/unibas/images/arrowdownselect.png");// set
																					// url
																					// of
																					// down
																					// image
			}

		}*/
		
}
