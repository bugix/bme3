package medizin.client.ui.widget.labeled;

import com.google.gwt.user.client.ui.TextArea;

public class LabeledTextArea extends LabeledValueBox<String> {
	
	public LabeledTextArea() {
		super(new TextArea());
	}
	
	public TextArea getTextArea() {
		return (TextArea) super.valueBox;
	}
}
