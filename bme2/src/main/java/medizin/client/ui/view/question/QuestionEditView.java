package medizin.client.ui.view.question;

import java.util.Collection;
import java.util.Set;

import medizin.client.proxy.McProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.shared.MultimediaType;
import medizin.shared.Status;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ValueListBox;

public interface QuestionEditView extends IsWidget {
    /*void setName(String helloName);*/

    void setPresenter(Presenter activityQuestionEdit);
    void setDelegate(Delegate delegate);


    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void cancelClicked();

		//void saveClicked(boolean generateNewQuestion);

		QuestionResourceProxy createQuestionResource(String url,int sequenceNumber, MultimediaType type);

		void deleteSelectedQuestionResource(Long id);

		void deleteMediaFileFromDisk(String path);

		void createNewQuestion(QuestionTypeProxy questionType, String questionShortName,String questionText, PersonProxy auther, PersonProxy rewiewer,Boolean submitToReviewComitee, QuestionEventProxy questEvent, Set<McProxy> mcs,String questionComment, int questionVersion, int questionSubVersion, String picturePath, Set<QuestionResourceClient> questionResourceClients, Status status);

		void updateQuestion(QuestionTypeProxy questionType, String questionShortName,String questionText, PersonProxy auther, PersonProxy rewiewer,Boolean submitToReviewComitee, QuestionEventProxy questEvent, Set<McProxy> mcs,String questionComment, int questionVersion,int questionSubVersion, String picturePath,Set<QuestionResourceClient> questionResourceClients, Status status);

		Status getUpdatedStatus(boolean isEdit, boolean withNewMajorVersion);

		boolean isAcceptQuestionView();

		boolean isAdminOrReviewer();

		boolean isAuthor();

		void resendToReview(QuestionTypeProxy value, String text, String html, PersonProxy selected, PersonProxy selected2, Boolean value2, QuestionEventProxy value3, Set<McProxy> value4, String text2, int questionVersion, int questionSubVersion, String picturePath, Set<QuestionResourceClient> questionResourceClients, Status status);

	}

	//RequestFactoryEditorDriver<QuestionProxy, QuestionEditViewImpl> createEditorDriver();

	void setRewiewerPickerValues(Collection<PersonProxy> values);

	void setQuestEventPickerValues(Collection<QuestionEventProxy> values);

	void setAutorPickerValues(Collection<PersonProxy> values);

	void setQuestionTypePickerValues(Collection<QuestionTypeProxy> values);

	void setMcsPickerValues(Collection<McProxy> values);

	void setRichPanelHTML(String html);

	/*String getRichtTextHTML();*/

	void setEditTitle(boolean edit);

	/*ValueListBox<QuestionTypeProxy> getQuestionType();

	RichTextArea getQuestionTextArea();
*/
//	ValueListBox<PersonProxy> getAuther();
//
//	ValueListBox<PersonProxy> getReviewer();

/*	TextArea getQuestionComment(); */

	ValueListBox<QuestionEventProxy> getQuestionEvent();

	void setValue(QuestionProxy question);

/*	McSetEditor getMCS();

	DefaultSuggestBox<PersonProxy,  EventHandlingValueHolderItem<PersonProxy>> getAutherListBox();

	DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> getReviewerListBox();

	TextBox getShortName();

	CheckBox getSubmitToReviewComitee();

	ImageViewer getImageViewer();

	Label getAutherLbl();

	ResourceView getResourceView();

	void setEventBus(EventBus eventBus);*/

	Set<QuestionResourceClient> getQuestionResources();

}
