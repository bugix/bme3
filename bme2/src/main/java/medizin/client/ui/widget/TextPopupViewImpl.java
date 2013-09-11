package medizin.client.ui.widget;

import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TextPopupViewImpl extends PopupPanel {

	private final VerticalPanel mainVp;
	private final HorizontalPanel hp;
	private final TextBox textBox;
	private final IconButton saveBtn;
	private final IconButton cancelBtn;
	private final BmeConstants constants = GWT.create(BmeConstants.class);	
	
	public TextPopupViewImpl()
	{
		super();
		this.setAutoHideEnabled(true);
		
		mainVp = new VerticalPanel();
		hp = new HorizontalPanel();		
		textBox = new TextBox();
		saveBtn = new IconButton();
		cancelBtn = new IconButton();
		
		saveBtn.setText(constants.save());
		saveBtn.setIcon("disk");
		cancelBtn.setText(constants.cancel());
		cancelBtn.setIcon("close");
		hp.setSpacing(5);
		hp.add(textBox);
		hp.add(saveBtn);
		hp.add(cancelBtn);
		
		mainVp.setSpacing(5);
		mainVp.add(hp);	
		this.add(mainVp);
	}

	public void setText(String text)
	{
		textBox.setText(text);
	}
	
	public String getText()
	{
		return textBox.getText();
	}
	
	public HandlerRegistration addSaveClickHandler(ClickHandler handler)
	{
		return saveBtn.addClickHandler(handler);
	}
	
	public HandlerRegistration addCancelClickHandler(ClickHandler handler)
	{
		return cancelBtn.addClickHandler(handler);
	}
}
