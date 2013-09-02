package medizin.client.ui.widget.dialogbox;


import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxOkButtonEvent;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxOkButtonEventHandler;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEvent;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEventHandler;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConfirmationDialogBox extends DialogBox {

	private static final BmeConstants constants = GWT.create(BmeConstants.class);

	private Label msgLbl;
	private HorizontalPanel hp;
	private VerticalPanel vp;
	private IconButton yesBtn;
	private IconButton noBtn;
	private IconButton okBtn;

	private Button getNoBtnl() {
		return this.noBtn;
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
		hp.setWidth("100%");
		
		yesBtn = new IconButton();
		noBtn = new IconButton();
		okBtn = new IconButton();
		
		yesBtn.setIcon("check");
		noBtn.setIcon("close");
		okBtn.setIcon("check");		
		
		yesBtn.setText(constants.yes());
		noBtn.setText(constants.no());		
		okBtn.setText(constants.okBtn());

		msgLbl = new Label();
		
		vp.getElement().getStyle().setMarginLeft(5, Unit.PX);
		vp.getElement().getStyle().setMarginTop(5, Unit.PX);
		vp.getElement().getStyle().setMarginRight(5, Unit.PX);
		
		vp.add(msgLbl);
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
		
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		HorizontalPanel btnHP = new HorizontalPanel();
		btnHP.add(yesBtn);
		btnHP.add(noBtn);
		hp.add(btnHP);
		
		yesBtn.getElement().getStyle().setMarginRight(10, Unit.PX);
		
		this.getNoBtnl().setText(constants.no());
		this.getYesBtn().setText(constants.yes());
		this.showBaseDialog(caption);
		
		this.getYesBtn().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) 
			{
				dialogBox.hide();
				handler.onYesButtonClicked(new ConfirmDialogBoxYesNoButtonEvent(true));				
			}
		});
		
		this.getNoBtnl().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				handler.onNoButtonClicked(new ConfirmDialogBoxYesNoButtonEvent(false));				
			}
		});

	}
	
	private void initOkConfirmationDialog(String caption, String msg) {
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		hp.add(okBtn);
		
		msgLbl.setText(msg);

		this.showBaseDialog(caption);
	}

	private void showConfirmationDialog(final ConfirmationDialogBox dialogBox, String caption, String msg, final ConfirmDialogBoxOkButtonEventHandler handler) {
		
		initOkConfirmationDialog(caption, msg);
		
		this.okBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				dialogBox.hide();
				handler.onOkButtonClicked(new ConfirmDialogBoxOkButtonEvent());
			}
		});
	}
	
	private void showConfirmationDialog(final ConfirmationDialogBox dialogBox, String caption, String message) {

		initOkConfirmationDialog(caption, message);
		
		this.okBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				dialogBox.hide();
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

	public static void showOkDialogBox(String caption, String message) {
		ConfirmationDialogBox dialogBox = new ConfirmationDialogBox();
		dialogBox.showConfirmationDialog(dialogBox, caption, message);
	}

}
