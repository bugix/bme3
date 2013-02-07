package medizin.client.ui.view.question;

import java.util.Collection;

import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.AnswerProxy;
import medizin.client.shared.Validity;

import com.google.gwt.place.shared.Place;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ValueListBox;

public interface AnswerDialogbox extends IsWidget {
 


    void display();

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
	

    

}
