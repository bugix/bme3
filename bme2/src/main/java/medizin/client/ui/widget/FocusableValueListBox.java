package medizin.client.ui.widget;

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.view.client.ProvidesKey;

/**
 * From http://code.google.com/p/google-web-toolkit/issues/detail?id=6483
 * 
 * @author michael
 * 
 * @param <T>
 */
public class FocusableValueListBox<T> extends ValueListBox<T> implements
		HasAllKeyHandlers,
		HasAllFocusHandlers, 
		Focusable {
	public FocusableValueListBox(Renderer<T> renderer) {
		super(renderer);
	}

	public FocusableValueListBox(Renderer<T> renderer,
			ProvidesKey<T> keyProvider) {
		super(renderer, keyProvider);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return getListBox().addFocusHandler(handler);
	}

	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return getListBox().addBlurHandler(handler);
	}

	@Override
	public int getTabIndex() {
		return getListBox().getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		getListBox().setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused) {
		getListBox().setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
		getListBox().setTabIndex(index);
	}

	public ListBox getListBox() {
		return (ListBox) getWidget();
	}

	@Override
	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return getListBox().addKeyUpHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return getListBox().addKeyDownHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return getListBox().addKeyPressHandler(handler);
	}
}
