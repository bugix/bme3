package medizin.client.ui.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface SystemOverviewSubView extends IsWidget {

	public Label getMcNameLbl();
	
	public Label getClosedDateLbl();
	
	public VerticalPanel getQuestionTypeVP();
}
