package medizin.client.ui.view.question.criteria;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;

public abstract class QuestionAdvancedSearchAbstractPopupViewImpl extends PopupPanel implements QuestionAdvancedSearchPopupView {
	
	protected Delegate delegate;

	public QuestionAdvancedSearchAbstractPopupViewImpl() {
		this.setAutoHideEnabled(true);
	}
	
	@Override
	public final void display(Button parentButton) {
		this.getElement().getStyle().setZIndex(1);
		this.show();
		int left, top;
		
		if (parentButton.getAbsoluteLeft() + parentButton.getOffsetWidth()/2 + this.getOffsetWidth()/2 > Window.getClientWidth()) {
			left = Window.getClientWidth() - this.getOffsetWidth();
		} else {
			left = parentButton.getAbsoluteLeft() + parentButton.getOffsetWidth()/2 - this.getOffsetWidth()/2;
		}
		
		top = parentButton.getAbsoluteTop() - getOffsetHeight()/2 - 20;
		this.setPopupPosition((left + 35), top);
	}
	
	@Override
	public final void setDelegate(Delegate delegate) {
		this.delegate = delegate;		
	}
	
	public abstract boolean validateField();
}
