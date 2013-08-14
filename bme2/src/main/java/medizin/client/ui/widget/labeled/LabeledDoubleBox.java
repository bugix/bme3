package medizin.client.ui.widget.labeled;

import medizin.client.ui.widget.DoubleBox;

public class LabeledDoubleBox extends LabeledTextBox {

	public LabeledDoubleBox() {
		super(new DoubleBox());
	}
	
	public DoubleBox getDoubleBox() {
		return (DoubleBox)super.valueBox;
	}
	
	public void setMin(double min) {
		((DoubleBox)valueBox).setMin(min);
	}
	
	public void setMax(double max) {
		((DoubleBox)valueBox).setMax(max);
	}
}
