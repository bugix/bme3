package medizin.client.ui.widget.dialogbox.receiver;

import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReceiverDialog extends DialogBox {
	private final BmeConstants constants = GWT.create(BmeConstants.class);
	private final VerticalPanel panel = new VerticalPanel();
	private final HorizontalPanel HPbtn = new HorizontalPanel();
	private final Button btnOk = new Button();
	private final DialogBox dialogBox;
	private final HTML html = new HTML();
	private static ReceiverDialog receiverDialog = new ReceiverDialog();

	private ReceiverDialog() {
		dialogBox = this;
		super.getCaption().asWidget().addStyleName("confirmbox");
		super.setText(constants.violationMessage());
		Log.info("Call OSCEReceiverPopupViewImpl() Constructor");

		this.add(panel);
		// center();
		setGlassEnabled(true);
		setAnimationEnabled(true);
		setAutoHideEnabled(true);
		// setWidth("500px");
		// setVisible(false);
		this.hide();
		HPbtn.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		btnOk.setText(constants.okBtn());
		btnOk.addStyleName("marginTop15");
		HPbtn.add(btnOk);
		this.getElement().getStyle().setZIndex(1);
		btnOk.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});

		panel.setWidth("350px");
		panel.add(html);
		panel.add(HPbtn);
	}

	private void showMessage(String message) {
		Log.info("Call showMessage" + message);

		html.setHTML(message);
		display();
	}

	private void display() {
		Log.info("display() call....." + panel.getWidgetCount());
		// dialogBox.setVisible(true);
		dialogBox.show();
		dialogBox.center();

	}
	
	public static void showMessageDialog(final String message) {
		receiverDialog.showMessage(message);
	}
	/*
	 * public void setDelegate(Delegate delegate) { this.delegate = delegate; }
	 */

}
