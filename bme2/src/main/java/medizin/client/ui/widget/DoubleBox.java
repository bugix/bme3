package medizin.client.ui.widget;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

/**
 * A standard single-line {@link CheckedTextBox}, that only allows integers to be entered.
 * It is possible to set a maximum and minimum allowed value. When ever anything is
 * entered, pasted or cut from the 
 * @author mwagner
 *
 */

public class DoubleBox extends CheckedTextBox {
	private String lastGoodValue = "";
	private double min = -Double.MAX_VALUE;
	private double max = Double.MAX_VALUE;
	
	private class DoubleCheckHandler implements ValueChangeHandler<String> {

		/**
		 * this method is used to check wether the new value in 
		 * the TextBox is a valid integer and is within the 
		 * predefined size boundaries (min, max, unsigned).
		 */
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			String str = event.getValue();
			if (str.length() == 0 
					|| (str.equals("-") && min < 0)
					|| (str.charAt(str.length()-1) == 'e') 
					|| (str.charAt(str.length()-1) == 'E')) {
				lastGoodValue = event.getValue();
			} else {
				try {
					double value = Double.parseDouble(event.getValue());
					if ((value > max && max > 0) || (value < min && min <= 0)) {
						DoubleBox.this.setValue(lastGoodValue);
					} else {
						lastGoodValue = event.getValue();
					}
				} catch (NumberFormatException e) {
					DoubleBox.this.setValue(lastGoodValue);
				}
			}
		}
	}
	
	public DoubleBox() {
		super();
		this.addValueChangeHandler(new DoubleCheckHandler());
	}
	
	protected DoubleBox(Element element) {
		super(element);
		this.addValueChangeHandler(new DoubleCheckHandler());
	}
		
	/**
	 * Set the highest number allowed
	 * @param max
	 */
	public void setMax(double max) {
		this.max = max;
	}
	
	/**
	 * Set the lowest number allowed
	 * @param min
	 */
	public void setMin(double min) {
		this.min = min;
	}
}
