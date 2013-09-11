package medizin.client.ui.view.question;

import java.util.Set;

import medizin.client.proxy.KeywordProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;

import com.google.common.base.Function;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface QuestionDetailsView extends IsWidget {
    /*void setName(String helloName);*/

    /*void setPresenter(Presenter activityQuestionDetails);*/
    void setDelegate(Delegate delegate);
    void setValue(QuestionProxy proxy);
   // public EventAccessViewImpl getEventAccessView();


    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void deleteClicked();

		void editClicked();

		void newClicked();

		void deleteSelectedQuestionResource(Long qestionResourceId);

		void addNewQuestionResource(QuestionResourceClient questionResourceClient);

		/*void updatePicturePathInQuestion(String picturePath);

		void deleteUploadedPicture(String picturePath);*/

		void deleteUploadedFiles(Set<String> paths);

		void changedResourceSequence(Set<QuestionResourceClient> questionResourceClients);
		
		void acceptQuestionClicked(QuestionProxy proxy);

		boolean isQuestionDetailsPlace();

		void getQuestionDetails(QuestionProxy previousVersion,Function<QuestionProxy, Void> function);

		void getLatestQuestionDetails(Function<QuestionProxy, Void> function);

		void onResendToReviewClicked(QuestionProxy proxy);

		void checkForResendToReview();

		void forcedActiveClicked();

		void enableBtnOnLatestClicked();

		void keywordAddButtonClicked(String text, QuestionProxy proxy);

		void deleteKeywordClicked(KeywordProxy keyword, QuestionProxy proxy);
	}

	AnswerListViewImpl getAnswerListViewImpl();
	
	public MatrixAnswerListViewImpl getMatrixAnswerListViewImpl();

	public void setVisibleEditAndDeleteBtn(Boolean flag);
	
	public void setVisibleAcceptButton();
	
	public IconButton getEdit();

	public VerticalPanel getAnswerVerticalPanel();
	
	IconButton getResendToReviewBtn();
	
	IconButton getAcceptBtn();
	
	IconButton getForcedActiveBtn();
	
	public CellTable<KeywordProxy> getKeywordTable();
	
	public DefaultSuggestBox<KeywordProxy, EventHandlingValueHolderItem<KeywordProxy>> getKeywordSuggestBox();
	
	public IconButton getKeywordAddButton();
}
