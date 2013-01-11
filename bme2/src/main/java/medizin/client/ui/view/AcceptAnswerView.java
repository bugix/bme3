package medizin.client.ui.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface AcceptAnswerView extends IsWidget {


    void setDelegate(Delegate delegate);

    public interface Delegate {
       
    }

	VerticalPanel getQuestionPanel();

}
