package medizin.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SystemOverviewSubViewImpl extends Composite implements SystemOverviewSubView {

	private static SystemOverviewSubViewImplUiBinder uiBinder = GWT
			.create(SystemOverviewSubViewImplUiBinder.class);

	interface SystemOverviewSubViewImplUiBinder extends
			UiBinder<Widget, SystemOverviewSubViewImpl> {
	}
	
	@UiField
	Label mcNameLbl;
	
	@UiField
	Label closedDateLbl;
	
	@UiField
	VerticalPanel questionTypeVP;

	public SystemOverviewSubViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public Label getMcNameLbl() {
		return mcNameLbl;
	}

	public void setMcNameLbl(Label mcNameLbl) {
		this.mcNameLbl = mcNameLbl;
	}

	public Label getClosedDateLbl() {
		return closedDateLbl;
	}

	public void setClosedDateLbl(Label closedDateLbl) {
		this.closedDateLbl = closedDateLbl;
	}

	public VerticalPanel getQuestionTypeVP() {
		return questionTypeVP;
	}

	public void setQuestionTypeVP(VerticalPanel questionTypeVP) {
		this.questionTypeVP = questionTypeVP;
	}
}
