package medizin.client.ui.widget.dialogbox;

import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConfirmationCheckboxDialog extends DialogBox {

	private static final BmeConstants constants = GWT
			.create(BmeConstants.class);
	
	private HTML msgLbl;
	private HTML checkBoxMsg;
	private HorizontalPanel hp;
	private HorizontalPanel checkBoxHp;
	private VerticalPanel vp;
	private Button okBtnl;
	private CheckBox checkBox;

	public ConfirmationCheckboxDialog(String msg, String checkboxmsg) {
		SafeHtml msgHtml = SafeHtmlUtils.fromSafeConstant(msg);
		SafeHtml checkBoxMsgHtml = SafeHtmlUtils.fromSafeConstant(checkboxmsg);
		
		init(msgHtml, checkBoxMsgHtml);
	}
	
	private void init(SafeHtml msgHtml, SafeHtml checkBoxMsgHtml)
	{
		vp = new VerticalPanel();
		hp = new HorizontalPanel();
		checkBoxHp = new HorizontalPanel();
		checkBox = new CheckBox();
		
		checkBoxHp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);	
		
		vp.getElement().getStyle().setMargin(5, Unit.PX);
		
		okBtnl = new Button();
		
		okBtnl.setText(constants.okBtn());

		msgLbl = new HTML();
		msgLbl.setHTML(msgHtml);
		vp.add(msgLbl);
		
		checkBoxMsg = new HTML();
		checkBoxMsg.setHTML(checkBoxMsgHtml);
		checkBoxHp.getElement().getStyle().setMarginTop(10, Unit.PX);
		checkBoxHp.add(checkBox);
		checkBoxHp.add(checkBoxMsg);
		vp.add(checkBoxHp);
		
		okBtnl.getElement().getStyle().setMarginLeft(135, Unit.PX);
		hp.add(okBtnl);
		hp.setSpacing(10);
		
		vp.add(hp);
		
		super.getCaption().asWidget().addStyleName("confirmbox");
		super.setText("caption");
		this.add(vp);
		
		setAnimationEnabled(true);
		setGlassEnabled(true);
		
		this.getElement().getStyle().setZIndex(3);
	}
	
	public void showBaseDialog(String caption) {
		
		super.setText(caption);
		super.center();
		this.getElement().getStyle().setZIndex(3);
		super.show();

	}
	public Boolean getCheckBoxValue() {
		return checkBox.getValue();
	}

	public void addOKClickHandler(ClickHandler clickHandler) {
		okBtnl.addClickHandler(clickHandler);		
	}
}
