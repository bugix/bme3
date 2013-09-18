package medizin.client.ui.widget.labeled;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;

public class LabeledTextBox extends LabeledValueBox<String> {
	
	public LabeledTextBox() {
		super(new TextBox());
	}
	
	protected LabeledTextBox(ValueBoxBase<String> valueBox) {
		super(valueBox);
	}
	
	public TextBox getTextBox() {
		return (TextBox) super.valueBox;
	}
}
