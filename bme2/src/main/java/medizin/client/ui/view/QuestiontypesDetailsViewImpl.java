package medizin.client.ui.view;

import java.util.ArrayList;

import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.view.renderer.EnumRenderer;
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
import static medizin.client.util.ClientUtility.defaultString;

public class QuestiontypesDetailsViewImpl extends Composite implements QuestiontypesDetailsView  {

		private static UserDetailsViewImplUiBinder uiBinder = GWT
				.create(UserDetailsViewImplUiBinder.class);
	
		interface UserDetailsViewImplUiBinder extends
				UiBinder<Widget, QuestiontypesDetailsViewImpl> {
		}
		
		private EnumRenderer<Enum<?>> enumRenderer = new EnumRenderer<Enum<?>>();
	
		public BmeConstants constants = GWT.create(BmeConstants.class);
		 
		private Delegate delegate;
		
	    @UiField
		IconButton edit;
	    
	    @UiField
		IconButton delete;
	    
	    @UiField
	    SpanElement displayRenderer;
		

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
		
		@UiField
		Label maxBytesLbl;
		 
		@UiField
		Label maxBytesValLbl;
	    
		private static final ArrayList<String> textualSortList = Lists.newArrayList("sumAnswer","sumTrueAnswer","sumFalseAnswer","questionLength","answerLength","answerDiff","queHaveImg","queHaveVideo","queHaveSound");
		
		private static final ArrayList<String> imgKeyList = Lists.newArrayList("questionLength","keywordCount","showAutoComplete","isDictionaryKeyword","allowTyping","minLetterForAutoComp","answerLength","acceptNonKeyword","shortAnswerLength"/*,"imageWidth","imageLength","imageProportion"*/);

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
	    	questionTypeValLbl.setText(enumRenderer.render(proxy.getQuestionType()));
	    	instituteValLbl.setText(proxy.getInstitution().getInstitutionName());
	    	
	       if (proxy.getQuestionType().equals(QuestionTypes.Textual) || proxy.getQuestionType().equals(QuestionTypes.Sort))
	       {
	    	   sumAnswerValLbl.setText(defaultString(proxy.getSumAnswer()));
	    	   sumTrueAnswerValLbl.setText(defaultString(proxy.getSumTrueAnswer()));
	    	   sumFalseAnswerValLbl.setText(defaultString(proxy.getSumFalseAnswer()));
	    	   questionLengthValLbl.setText(defaultString(proxy.getQuestionLength()));
	    	   answerLengthValLbl.setText(defaultString(proxy.getAnswerLength()));
	    	   answerDiffValLbl.setText(defaultString(proxy.getDiffBetAnswer()));
	    	   queHaveImgValLbl.setText(defaultString(proxy.getQueHaveImage()));
	    	   queHaveVideoValLbl.setText(defaultString(proxy.getQueHaveVideo()));
	    	   queHaveSoundValLbl.setText(defaultString(proxy.getQueHaveSound()));
	       }
	       else if (proxy.getQuestionType().equals(QuestionTypes.Imgkey))
	       {
	    	   questionLengthValLbl.setText(defaultString(proxy.getQuestionLength()));
	    	   keywordCountValLbl.setText(defaultString(proxy.getKeywordCount()));
	    	   showAutoCompleteValLbl.setText(defaultString(proxy.getShowAutocomplete()));
	    	   isDictionaryKeywordValLbl.setText(defaultString(proxy.getIsDictionaryKeyword()));
	    	   allowTypingValLbl.setText(defaultString(proxy.getAllowTyping()));
	    	   minLetterForAutoCompValLbl.setText(defaultString(proxy.getMinAutoCompleteLetter()));
	    	   answerLengthValLbl.setText(defaultString(proxy.getAnswerLength()));
	    	   acceptNonKeywordValLbl.setText(defaultString(proxy.getAcceptNonKeyword()));
	    	   shortAnswerLengthValLbl.setText(defaultString(proxy.getLengthShortAnswer()));
	    	   /*imageWidthValLbl.setText(defaultString(proxy.getImageWidth()));
	    	   imageLengthValLbl.setText(defaultString(proxy.getImageHeight()));
	    	   imageProportionValLbl.setText(defaultString(proxy.getImageProportion()));*/
	       }
	       else if (proxy.getQuestionType().equals(QuestionTypes.ShowInImage))
	       {
	    	   questionLengthValLbl.setText(defaultString(proxy.getQuestionLength()));
	    	   answerLengthValLbl.setText(defaultString(proxy.getAnswerLength()));
	    	  /* imageWidthValLbl.setText(defaultString(proxy.getImageWidth()));
	    	   imageLengthValLbl.setText(defaultString(proxy.getImageHeight()));
	    	   imageProportionValLbl.setText(defaultString(proxy.getImageProportion()));*/
	    	   /*linearPointValLbl.setText(defaultString(proxy.getLinearPoint()));
	    	   linearPercentageValLbl.setText(defaultString(proxy.getLinearPercentage()));*/	    	   
	       }
	       else if (proxy.getQuestionType().equals(QuestionTypes.LongText))
	       {
	    	   questionLengthValLbl.setText(defaultString(proxy.getQuestionLength()));
	    	   keywordHighlightValLbl.setText(defaultString(proxy.getKeywordHighlight()));
	    	   richTextValLbl.setText(defaultString(proxy.getRichText()));
	    	   minLengthValLbl.setText(defaultString(proxy.getMinLength()));
	    	   maxLengthValLbl.setText(defaultString(proxy.getMaxLength()));
	    	   minWordCountValLbl.setText(defaultString(proxy.getMinWordCount()));
	    	   maxWordCountValLbl.setText(defaultString(proxy.getMaxWordCount()));
	       }
	       else if (proxy.getQuestionType().equals(QuestionTypes.Matrix))
	       {
	    	   //maxLengthValLbl.setText(proxy.getMaxLength()));
	    	   questionLengthValLbl.setText(defaultString(proxy.getQuestionLength()));
	    	   answerLengthValLbl.setText(defaultString(proxy.getAnswerLength()));
	    	   oneToOneAssValLbl.setText(defaultString(proxy.getAllowOneToOneAss()));
	       }
	       else if (proxy.getQuestionType().equals(QuestionTypes.MCQ))
	       {
	    	   questionLengthValLbl.setText(defaultString(proxy.getQuestionLength()));
	    	   /*imageWidthValLbl.setText(defaultString(proxy.getImageWidth()));
	    	   imageLengthValLbl.setText(defaultString(proxy.getImageHeight()));
	    	   imageProportionValLbl.setText(defaultString(proxy.getImageProportion()));*/
	    	   multimediaTypeValLbl.setText(enumRenderer.render(proxy.getMultimediaType()));
	    	   selectionTypeValLbl.setText(enumRenderer.render(proxy.getSelectionType()));
	    	   columnValLbl.setText(defaultString(proxy.getColumns()));
	    	   richTextValLbl.setText(defaultString(proxy.getRichText()));
	    	   /*thumbWidthValLbl.setText(defaultString(proxy.getThumbWidth()));
	    	   thumbHeightValLbl.setText(defaultString(proxy.getThumbHeight()));	    	   
	    	   allowZoomOutValLbl.setText(defaultString(proxy.getAllowZoomOut()));
	    	   allowZoomInValLbl.setText(defaultString(proxy.getAllowZoomIn()));*/
	    	   maxBytesValLbl.setText(defaultString(proxy.getMaxBytes()));
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
			maxBytesLbl.setText(constants.maxBytes());
		}
	
	
		@Override
		public void setName(String helloName) {
			// TODO Auto-generated method stub
			
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
}
