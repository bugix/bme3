package medizin.client.ui.view.assignquestion;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author dk
 *
 */
public interface SendMailPopupView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		// TODO define methods to be delegated!
	}

    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
    void setPresenter(Presenter systemStartActivity);

	Button getSendMailButton();

	String getMessageContent();

	void setMessageContent(String html);

	/*Button getSaveTemplateButton();*/

	Button getRestoreTemplateButton();

	/*ListBox getSemesterList();

	Button getLoadTemplateButton();*/
}
