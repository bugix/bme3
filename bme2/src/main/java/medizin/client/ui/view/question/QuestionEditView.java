package medizin.client.ui.view.question;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import medizin.client.proxy.CommentProxy;
import medizin.client.proxy.McProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;

import com.google.common.base.Function;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ValueListBox;

public interface QuestionEditView extends IsWidget {

    void setDelegate(Delegate delegate);
    
	interface Delegate {
		void cancelClicked();

		void deleteSelectedQuestionResource(Long id);

		void deleteMediaFileFromDisk(String path);

		boolean isAcceptQuestionView();

		boolean isAdminOrReviewer();

		boolean isAuthor();

		boolean isAdminOrInstitutionalAdmin();

		void saveQuestionWithDetails();

		void resendToReview();

		void disableEnableAuthorReviewerSuggestBox();

	}

	void setRewiewerPickerValues(Collection<PersonProxy> values, PersonProxy lastSelectedReviewer);

	void setQuestEventPickerValues(Collection<QuestionEventProxy> values);

	void setAutorPickerValues(Collection<PersonProxy> values);

	void setQuestionTypePickerValues(List<QuestionTypeProxy> values);

	void setMcsPickerValues(List<McProxy> values);

	void setRichPanelHTML(String html);

	void setEditTitle(boolean edit);

	ValueListBox<QuestionEventProxy> getQuestionEvent();

	void setValue(QuestionProxy question);

	Set<QuestionResourceClient> getQuestionResources();
	
	void setValuesForQuestion(QuestionProxy question, CommentProxy commentProxy);
	
	void comfirmQuestionChanges(Function<Boolean, Void> isMajorOrMinor);
	
	Long getAuthorId();
	
	void addPictureToQuestionResources(QuestionResourceProxy questionResourceProxyForPicture);
	
	void disableEnableAuthorReviewerValue(boolean flag);
}
