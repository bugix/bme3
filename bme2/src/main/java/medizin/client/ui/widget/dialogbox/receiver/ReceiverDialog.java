package medizin.client.ui.widget.dialogbox.receiver;

import java.util.ArrayList;

import medizin.client.ui.widget.IconButton;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReceiverDialog extends DialogBox {
	private final BmeConstants constants = GWT.create(BmeConstants.class);
	private final VerticalPanel panel = new VerticalPanel();
	private final HorizontalPanel HPbtn = new HorizontalPanel();
	private final IconButton btnClose = new IconButton();
	private final DialogBox dialogBox;
	private final HTML html = new HTML();
	private Handler handler;
	private static ReceiverDialog receiverDialog = new ReceiverDialog();

	private ReceiverDialog() {
		dialogBox = this;
		super.getCaption().asWidget().addStyleName("confirmbox");
		super.setText(constants.violationMessage());
		Log.info("Call OSCEReceiverPopupViewImpl() Constructor");

		this.add(panel);
		setGlassEnabled(true);
		setAnimationEnabled(true);
		setAutoHideEnabled(true);
		this.hide();
		
		HPbtn.setWidth("100%");
		HPbtn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		HPbtn.getElement().getStyle().setMarginTop(10, Unit.PX);
		
		btnClose.setText(constants.close());
		btnClose.setIcon("close");
		
		HPbtn.add(btnClose);
		
		this.getElement().getStyle().setZIndex(1);
		btnClose.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(handler != null) {
					handler.onClick();
				}
				dialogBox.hide();
			}
		});

		panel.getElement().getStyle().setMargin(5, Unit.PX);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
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
		dialogBox.show();
		dialogBox.center();

	}
	
	public static void showMessageDialog(final String message) {
		receiverDialog.showMessage(message);
	}
	
	public static void showMessageDialog(final String header,final ArrayList<String> messages) {
		if(messages != null && messages.isEmpty() == false) {
			final StringBuilder errorBuffor = new StringBuilder();
			errorBuffor.append("<b>" + header + "</b>" + "<br/><br/>");
			errorBuffor.append("<ul style=\"padding:0px; margin:0px; list-style-type:none;\">");
			
			for (String msg : messages) {
				errorBuffor.append("<li style=\"text-align:left; line-height: 10px; padding:0px 0px 10px 0px;margin-left: 10px;\">")
				.append("<span class=\"ui-icon ui-icon-check\" style=\"float: left; margin-top: 1px; margin-right: 6px;\"></span>")
				.append(msg)
				.append("</li>");
			}
			errorBuffor.append("</ul>");
			showMessageDialog(errorBuffor.toString());
		}
		
	}
	
	public static void showMessageDialog(final String message,Handler handler) {
		receiverDialog.handler = handler;
		receiverDialog.showMessage(message);
	}
	
	public interface Handler {
		void onClick();
	}
}
