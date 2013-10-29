package medizin.client.ui.widget.mcs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import medizin.client.proxy.McProxy;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class McCheckboxEditor extends Composite {

	private static McCheckboxEditorUiBinder uiBinder = GWT.create(McCheckboxEditorUiBinder.class);

	interface McCheckboxEditorUiBinder extends UiBinder<Widget, McCheckboxEditor> {
	}

	@UiField
	FlexTable checkboxFlexTable;
	
	private int colCount = 2;
	
	private List<CheckBoxProxyValue> checkboxList = new ArrayList<CheckBoxProxyValue>();
	
	public McCheckboxEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setValue(Set<McProxy> mcs) {
		for (CheckBoxProxyValue checkBoxProxyValue : checkboxList) 
		{
			if (mcs.contains(checkBoxProxyValue.mcProxy))
			{
				checkBoxProxyValue.checkbox.setValue(true);
			}
			else
			{
				checkBoxProxyValue.checkbox.setValue(false);
			}
		}
	}

	public void setAcceptableValues(List<McProxy> values) {
		if (colCount < 1)
			colCount = 2;
		
		int row = 0, col = 0;
		
		for (McProxy mcProxy : values)
		{
			CheckBox checkbox = new CheckBox();
			checkbox.setText(mcProxy.getMcName());
			checkboxFlexTable.setWidget(row, col, checkbox);
			
			col += 1;
			if (col == colCount)
			{
				row += 1;
				col = 0;
			}
			
			checkboxList.add(new CheckBoxProxyValue(checkbox, mcProxy));
		}
	}

	private static final Function<CheckBoxProxyValue, McProxy> CHECKBOXPROXY_MCPROXY = new Function<CheckBoxProxyValue, McProxy>() {

		@Override
		public McProxy apply(CheckBoxProxyValue input) {
			
			if(input.checkbox.getValue()) {
				return input.mcProxy;	
			}else {
				return null;
			}
		}
	};

	public Set<McProxy> getValue() {
		return FluentIterable.from(checkboxList).transform(CHECKBOXPROXY_MCPROXY).filter(Predicates.notNull()).toImmutableSet();
	}
	
	public void setColCount(int colCount) {
		this.colCount = colCount;
	}
	
	private class CheckBoxProxyValue {
		
		CheckBox checkbox;
		McProxy mcProxy;
		
		public CheckBoxProxyValue(CheckBox checkbox, McProxy mcProxy) {
			this.checkbox = checkbox;
			this.mcProxy = mcProxy;
		}
		
	}
}
