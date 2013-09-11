package medizin.client.ui.widget.pager;

import com.google.gwt.user.cellview.client.SimplePager;

public class MySimplePager extends SimplePager {

	public MySimplePager() {
		super();
	}

	public MySimplePager(TextLocation location) {
		super(location);
	}

	public MySimplePager(TextLocation location, boolean showFastForwardButton, boolean showLastPageButton) {
		super(location,showFastForwardButton,showLastPageButton);
	}

	public MySimplePager(TextLocation location, boolean showFastForwardButton, final int fastForwardRows, boolean showLastPageButton) {
		super(location, showFastForwardButton, fastForwardRows, showLastPageButton);
	}

	public MySimplePager(TextLocation location, Resources resources, boolean showFastForwardButton, final int fastForwardRows, boolean showLastPageButton, ImageButtonsConstants imageButtonConstants) {
		super(location, resources, showFastForwardButton, fastForwardRows, showLastPageButton, imageButtonConstants);
	}

	public MySimplePager(TextLocation location, Resources resources, boolean showFastForwardButton, final int fastForwardRows, boolean showLastPageButton) {
		super(location, resources, showFastForwardButton, fastForwardRows, showLastPageButton);
	}
	
	@Override
	protected String createText() {
		if (getDisplay().getRowCount() == 0) {
	           return "0 of 0";
	     }
		return super.createText();
	}

}
