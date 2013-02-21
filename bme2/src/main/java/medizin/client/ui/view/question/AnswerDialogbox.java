package medizin.client.ui.view.question;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import medizin.client.proxy.PersonProxy;
import medizin.client.shared.Validity;
import medizin.client.ui.widget.resource.audio.AudioViewer;
import medizin.client.ui.widget.resource.image.polygon.ImagePolygonViewer;
import medizin.client.ui.widget.resource.image.rectangle.ImageRectangleViewer;
import medizin.client.ui.widget.resource.image.simple.SimpleImageViewer;
import medizin.client.ui.widget.resource.video.VideoViewer;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import medizin.shared.QuestionTypes;

import com.google.common.base.Function;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;

public interface AnswerDialogbox extends IsWidget {
 


    void display(QuestionTypes questionTypes);

    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void addAnswerClicked();
		void cancelAnswerClicked();
		void findAllAnswersPoints(Long id,Function<List<String>, Void> function);
		void deleteUploadedFiles(Set<String> paths);
	}

 
    
    void setDelegate(Delegate delegate);

	/*RequestFactoryEditorDriver<AnswerProxy, AnswerDialogboxImpl> createEditorDriver();*/
	void setRewiewerPickerValues(Collection<PersonProxy> values);

	void setValidityPickerValues(Collection<Validity> values);

	void close();

	void setRichPanelHTML(String html);

	String getRichtTextHTML();

	TextArea getComment();

	CheckBox getSubmitToReviewerComitee();

	ValueListBox<Validity> getValidity();

	void setAutherPickerValues(Collection<PersonProxy> values,PersonProxy logedUser);

	DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> getAutherSuggestBox();

	ImagePolygonViewer getImagePolygonViewer();

	ImageRectangleViewer getImageRectangleViewer();

	SimpleImageViewer getSimpleImageViewer();
	
	AudioViewer getAudioViewer();

	VideoViewer getVideoViewer();

	RichTextArea getRichtTextArea();

	DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> getReviewerSuggestBox();

	TextArea getAdditionalKeywords();

	TextBox getSequenceNumber();

}
