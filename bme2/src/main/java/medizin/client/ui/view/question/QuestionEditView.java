package medizin.client.ui.view.question;

import java.util.Collection;

import medizin.client.proxy.McProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.view.roo.McSetEditor;
import medizin.client.ui.widget.image.ImageViewer;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;

public interface QuestionEditView extends IsWidget {
    void setName(String helloName);

    void setPresenter(Presenter activityQuestionEdit);
    void setDelegate(Delegate delegate);


    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void cancelClicked();



		void saveClicked(boolean generateNewQuestion);
		
	}

	//RequestFactoryEditorDriver<QuestionProxy, QuestionEditViewImpl> createEditorDriver();

	void setRewiewerPickerValues(Collection<PersonProxy> values);

	void setQuestEventPickerValues(Collection<QuestionEventProxy> values);

	void setAutorPickerValues(Collection<PersonProxy> values);

	void setQuestionTypePickerValues(Collection<QuestionTypeProxy> values);



	void setMcsPickerValues(Collection<McProxy> values);



	void setRichPanelHTML(String html);

	

	String getRichtTextHTML();

	void setEditTitle(boolean edit);

	ValueListBox<QuestionTypeProxy> getQuestionType();

	RichTextArea getQuestionTextArea();

//	ValueListBox<PersonProxy> getAuther();
//
//	ValueListBox<PersonProxy> getReviewer();

	TextArea getQuestionComment();

	ValueListBox<QuestionEventProxy> getQuestionEvent();

	void setValue(QuestionProxy question);

	McSetEditor getMCS();

	DefaultSuggestBox<PersonProxy,  EventHandlingValueHolderItem<PersonProxy>> getAutherListBox();

	DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> getReviewerListBox();

	TextBox getShortName();

	CheckBox getSubmitToReviewComitee();

	ImageViewer getImageViewer();

	Label getAutherLbl();




}
