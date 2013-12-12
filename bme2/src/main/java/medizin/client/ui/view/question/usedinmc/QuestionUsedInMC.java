package medizin.client.ui.view.question.usedinmc;

import java.util.List;

import medizin.client.events.RecordChangeHandler;
import medizin.client.proxy.AssesmentQuestionProxy;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent.Handler;


public interface QuestionUsedInMC extends IsWidget,RecordChangeHandler {
	
	void setDelegate(Delegate delegate);

	public interface Delegate {
		void initUsedInMCView();
	}

	void setTableRowData(int start, List<AssesmentQuestionProxy> response);

	Range getTableVisibleRange();

	void setTableRowCount(Integer response);

	HandlerRegistration addTableRangeChangeHandler(Handler handler);

	AbstractHasData<AssesmentQuestionProxy> getTable();

	String[] getPaths();
	
	void initUsedInMCView();
}
