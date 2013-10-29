package medizin.client.ui.view.question;

import medizin.client.proxy.KeywordProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.view.question.learningobjective.QuestionLearningObjectiveSubViewImpl;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;

import com.google.common.base.Function;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface QuestionDetailsView extends IsWidget {
    /*void setName(String helloName);*/

    /*void setPresenter(Presenter activityQuestionDetails);*/
    void setDelegate(Delegate delegate);
    void setValue(QuestionProxy proxy);
   // public EventAccessViewImpl getEventAccessView();


	interface Delegate {
		void deleteClicked();

		void editClicked();

		void acceptQuestionClicked(QuestionProxy proxy);

		void getQuestionDetails(QuestionProxy previousVersion,Function<QuestionProxy, Void> function);

		QuestionProxy getLatestQuestionDetails();

		void onResendToReviewClicked(QuestionProxy proxy);

		void checkForResendToReview();

		void forcedActiveClicked();

		void enableBtnOnLatestClicked();

		void keywordAddButtonClicked(String text, QuestionProxy proxy);

		void deleteKeywordClicked(KeywordProxy keyword, QuestionProxy proxy);

		void acceptQueAnswersClicked();
	}

	AnswerListViewImpl getAnswerListViewImpl();
	
	public MatrixAnswerListViewImpl getMatrixAnswerListViewImpl();

	public void setVisibleEditAndDeleteBtn(Boolean flag);
	
	public void removeEditAndDeleteBtn(Boolean flag);
	
	public void setVisibleAcceptButton();

	public VerticalPanel getAnswerVerticalPanel();
	
	public IconButton getEdit();
	
	public IconButton getResendToReviewBtn();
	
	public IconButton getAcceptBtn();
	
	public IconButton getKeywordAddButton();
	
	public CellTable<KeywordProxy> getKeywordTable();
	
	public DefaultSuggestBox<KeywordProxy, EventHandlingValueHolderItem<KeywordProxy>> getKeywordSuggestBox();
	
	public QuestionLearningObjectiveSubViewImpl getQuestionLearningObjectiveSubViewImpl();
	
}
