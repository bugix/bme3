package medizin.client.ui.view.question;

import java.util.Collection;


import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.KeywordProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.McProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionTypeProxy;

import com.google.gwt.place.shared.Place;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;

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

	RequestFactoryEditorDriver<QuestionProxy, QuestionEditViewImpl> createEditorDriver();

	void setRewiewerPickerValues(Collection<PersonProxy> values);

	void setQuestEventPickerValues(Collection<QuestionEventProxy> values);

	void setAutorPickerValues(Collection<PersonProxy> values);

	void setQuestionTypePickerValues(Collection<QuestionTypeProxy> values);



	void setMcsPickerValues(Collection<McProxy> values);



	void setRichPanelHTML(String html);

	

	String getRichtTextHTML();

	void setEditTitle(boolean edit);




}
