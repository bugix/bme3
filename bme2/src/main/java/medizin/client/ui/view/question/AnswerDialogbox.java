package medizin.client.ui.view.question;

import java.util.Collection;

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

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

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

	ValueListBox<PersonProxy> getRewiewer();

	void setAutherPickerValues(Collection<PersonProxy> values,PersonProxy logedUser);

	DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> getAutherSuggestBox();

VerticalPanel getViewContainer();

	ImagePolygonViewer getImagePolygonViewer();

	void setImagePolygonViewer(ImagePolygonViewer viewer);

	void setImageRectangleViewer(ImageRectangleViewer viewer);

	ImageRectangleViewer getImageRectangleViewer();

	Label getLblUploadText();

	VerticalPanel getUploaderContainer();

	void setSimpleImageViewer(SimpleImageViewer viewer);

	void setAudioViewer(AudioViewer viewer);

	void setVideoViewer(VideoViewer viewer);

	SimpleImageViewer getSimpleImageViewer();
	
	AudioViewer getAudioViewer();

	VideoViewer getVideoViewer();

	RichTextArea getRichtTextArea();

}
