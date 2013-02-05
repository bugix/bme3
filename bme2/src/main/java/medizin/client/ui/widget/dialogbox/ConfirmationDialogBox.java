package medizin.client.ui.widget.dialogbox;


import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxOkButtonEvent;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxOkButtonEventHandler;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEvent;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEventHandler;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;

public class ConfirmationDialogBox extends DialogBox {

	private static final BmeConstants constants = GWT
			.create(BmeConstants.class);

	private final EventBus eventBus = new SimpleEventBus();
	
	private Label msgLbl;
	private HorizontalPanel hp;
	private VerticalPanel vp;
	private Button yesBtn;
	private Button noBtnl;
	private Button okBtnl;

	private Button getNoBtnl() {
		return this.noBtnl;
	}

	private Button getYesBtn() {
		return this.yesBtn;
	}

	private ConfirmationDialogBox() {
		init();
	}
	
	private void init()
	{
		vp = new VerticalPanel();
		hp = new HorizontalPanel();
		
		yesBtn = new Button();
		noBtnl = new Button();
		okBtnl = new Button();
		yesBtn.setText(constants.yes());
		noBtnl.setText(constants.no());
		
		okBtnl.setText(constants.okBtn());

		msgLbl = new Label();
		
		vp.add(msgLbl);
		hp.add(yesBtn);
		hp.add(noBtnl);
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
	
	private void showBaseDialog(String caption) {
		
		super.setText(caption);
		super.center();
		this.getElement().getStyle().setZIndex(3);
		super.show();

	}

	private void showYesNoDialog(final ConfirmationDialogBox dialogBox, String caption, String str, final ConfirmDialogBoxYesNoButtonEventHandler handler) {
		msgLbl.setText(str);
		this.getYesBtn().setVisible(true);
		this.getNoBtnl().setVisible(true);
		this.okBtnl.setVisible(false);
		
		this.getNoBtnl().setText(constants.no());
		this.getYesBtn().setText(constants.yes());
		this.showBaseDialog(caption);
		
		eventBus.addHandler(ConfirmDialogBoxYesNoButtonEvent.TYPE, handler);
		
		this.getYesBtn().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) 
			{
				dialogBox.hide();
				eventBus.fireEvent(new ConfirmDialogBoxYesNoButtonEvent(true));
			}
		});
		
		this.getNoBtnl().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				eventBus.fireEvent(new ConfirmDialogBoxYesNoButtonEvent(false));
			}
		});

	}

	private void showConfirmationDialog(final ConfirmationDialogBox dialogBox, String caption, String msg, final ConfirmDialogBoxOkButtonEventHandler handler) {
		this.getYesBtn().setVisible(false);
		this.getNoBtnl().setVisible(false);
		this.okBtnl.setVisible(true);
		msgLbl.setText(msg);

		this.showBaseDialog(caption);
		
		this.okBtnl.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				
				dialogBox.hide();
				eventBus.addHandler(ConfirmDialogBoxOkButtonEvent.TYPE, handler);
				eventBus.fireEvent(new ConfirmDialogBoxOkButtonEvent());
			}
		});
	}
	
	public static void showYesNoDialogBox(String caption, String message, ConfirmDialogBoxYesNoButtonEventHandler handler)
	{
	    ConfirmationDialogBox dialogBox = new ConfirmationDialogBox();
		dialogBox.showYesNoDialog(dialogBox, caption, message, handler);
	}
	
	public static void showOkDialogBox(String caption, String message, ConfirmDialogBoxOkButtonEventHandler handler)
	{
		ConfirmationDialogBox dialogBox = new ConfirmationDialogBox();
		dialogBox.showConfirmationDialog(dialogBox, caption, message, handler);
	}
}
