package medizin.client.ui.view;

import medizin.client.ui.view.question.QuestionEditViewImpl;
import medizin.client.ui.widget.IntegerBox;
import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.SelectionType;

import com.google.gwt.place.shared.Place;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;

public interface QuestiontypesEditView extends IsWidget {

    void setPresenter(Presenter activityAcceptAnswer);
    void setDelegate(Delegate delegate);
    void setValue(QuestionTypeProxy proxy);
    
    void setNullValue(QuestionTypes questionTypes);

    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void cancelClicked();

	//	void saveClicked(String questionTypeName, Boolean isWeil, Integer trueAnswers, Integer falseAnswers, Integer sumAnswers, Integer maxLetters);

		void saveClicked(QuestionTypeProxy proxy);


		
	}

	void showFieldsForQuestionType(QuestionTypes questionTypes);
	
	/*RequestFactoryEditorDriver<QuestionTypeProxy, QuestiontypesEditViewImpl> createEditorDriver();*/
	void setEditTitle(boolean edit);
	
	public TextBox getShortNameTxtbox();

	public TextBox getLongNameTxtbox();
	
	public TextArea getDescriptionTxtbox();

	public ValueListBox<InstitutionProxy> getInstituteListBox();

	public ValueListBox<QuestionTypes> getQuestionTypeListBox();

	public IntegerBox getSumAnswerTxtbox();

	public TextBox getSumTrueAnswerTxtbox();

	public TextBox getSumFalseAnswerTxtbox();

	public TextBox getQuestionLengthTxtbox();

	public TextBox getAnswerLengthTxtbox();

	public TextBox getAnswerDiffTxtbox();
	
	public CheckBox getQueHaveImgChkBox();

	public CheckBox getQueHaveVideoChkBox();

	public CheckBox getQueHaveSoundChkBox();
	
	public TextBox getKeywordCountTxtbox();

	public CheckBox getShowAutoCompleteChkBox();
	
	public CheckBox getIsDictionaryKeywordChkBox();

	public CheckBox getAllowTypingChkBox();

	public TextBox getMinLetterForAutoCompTxtbox();

	public CheckBox getAcceptNonKeywordChkBox();

	public TextBox getShortAnswerLengthTxtbox();
	
	/*public TextBox getImageWidthTxtbox();
	
	public TextBox getImageLengthTxtbox();

	public TextBox getImageProportionTxtbox();*/
	
	/*public CheckBox getLinearPointChkBox();
	
	public TextBox getLinearPercentageTxtbox();*/
	
	public CheckBox getKeywordHighlightChkBox();
	
	public CheckBox getRichTextChkBox(); 

	public TextBox getMinLengthTxtbox();

	public TextBox getMaxLengthTxtbox();

	public TextBox getMinWordCountTxtbox();
	
	public TextBox getMaxWordCountTxtbox(); 

	public CheckBox getOneToOneAssChkBox();

	public ValueListBox<MultimediaType> getMultimediaTypeListBox(); 

	public ValueListBox<SelectionType> getSelectionTypeListBox();

	public TextBox getColumnTxtbox();

	/*public TextBox getThumbWidthTxtbox();

	public TextBox getThumbHeightTxtbox();
	
	public CheckBox getAllowZoomOutChkBox(); 
	
	public CheckBox getAllowZoomInChkBox();*/
	
	public TextBox getMaxBytesTxtbox();
}
