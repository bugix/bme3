package medizin.client.ui.view.question;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.PersonProxy;
import medizin.shared.QuestionTypes;
import medizin.shared.Validity;

import com.google.common.base.Function;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface AnswerDialogbox extends IsWidget {
 


    void display(QuestionTypes questionTypes);

    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		/*void addAnswerClicked();*/
		void cancelAnswerClicked();
		void findAllAnswersPoints(Long id,Long currentAnswerId, Function<List<String>, Void> function);
		void deleteUploadedFiles(Set<String> paths);
		void saveAnswerProxy(AnswerProxy answerProxy, String answerText, PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment, Validity validity, String points, String mediaPath, String additionalKeywords, Integer sequenceNumber, final Function<AnswerProxy, Void> function);
	}

 
    
    void setDelegate(Delegate delegate);

	/*RequestFactoryEditorDriver<AnswerProxy, AnswerDialogboxImpl> createEditorDriver();*/
	void setRewiewerPickerValues(Collection<PersonProxy> values);

	void setValidityPickerValues(Collection<Validity> values);

	void close();

	void setRichPanelHTML(String html);

	/*String getRichtTextHTML();

	TextArea getComment();

	CheckBox getSubmitToReviewerComitee();

	ValueListBox<Validity> getValidity();*/

	void setAutherPickerValues(Collection<PersonProxy> values,PersonProxy logedUser);

	void setValues(AnswerProxy answer);

	void setMaxDifferenceBetween(long diff1, long diff2);

	/*DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> getAutherSuggestBox();

	ImagePolygonViewer getImagePolygonViewer();

	ImageRectangleViewer getImageRectangleViewer();

	SimpleImageViewer getSimpleImageViewer();
	
	AudioViewer getAudioViewer();

	VideoViewer getVideoViewer();

	RichTextArea getRichtTextArea();

	DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> getReviewerSuggestBox();

	TextArea getAdditionalKeywords();

	TextBox getSequenceNumber();*/

}
