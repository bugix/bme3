package medizin.client.ui.widget.sendmail;

import com.google.gwt.user.client.ui.IsWidget;

public interface SendMailPopupView extends IsWidget{
	interface Delegate {
	}
    
    void setDelegate(Delegate delegate);
 
    String getMessageContent();

	void setMessageContent(String html);
}
