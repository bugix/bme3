package medizin.client.ui.widget.labeled;

import medizin.client.ui.widget.IntegerBox;

public class LabeledIntegerBox extends LabeledTextBox {

	public LabeledIntegerBox() {
		super(new IntegerBox());
	}
	
	public IntegerBox getIntegerBox() {
		return (IntegerBox)super.valueBox;
	}

	public void setMin(int min) {
		((IntegerBox)valueBox).setMin(min);
	}
	
	public void setMax(int max) {
		((IntegerBox)valueBox).setMax(max);
	}
}
