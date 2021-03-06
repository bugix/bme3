
package medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl;


import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.SuggestWidget;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;


public class DefaultSuggestionPopup<T> extends PopupPanel implements
		SuggestWidget<T> {
	private static final String POPUP_STYLE = "eu-nextstreet-SuggestPopup"; // Border and Transperant

	public DefaultSuggestionPopup() {
		this(true, false);
		super.getElement().getStyle().setZIndex(2);
		
	}

	public DefaultSuggestionPopup(boolean autoHide) {
		this(autoHide, false);
		super.getElement().getStyle().setZIndex(2);
		
		}

	public DefaultSuggestionPopup(boolean autoHide, boolean modal) {
		super(autoHide, modal);
		setStylePrimaryName(POPUP_STYLE);
		super.getElement().getStyle().setZIndex(2);
	}

	@Override
	public void setWidget(ScrollPanel scrollPanel) {
		super.setWidget(scrollPanel);
	}

	@Override
	public void adjustPosition(int absoluteLeft, int absoluteTop) {
		super.setPopupPosition(absoluteLeft, absoluteTop);
	}

	
	
	@Override
	public void hide() {
		
		super.hide();
	}

}
