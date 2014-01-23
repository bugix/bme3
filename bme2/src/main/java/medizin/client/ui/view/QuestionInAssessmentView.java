package medizin.client.ui.view;

import java.util.List;

import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.AssesmentQuestionProxy;
import medizin.client.style.resources.AdvanceCellTable;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchSubViewImpl;
import medizin.client.ui.widget.Sorting;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public interface QuestionInAssessmentView extends IsWidget {

		interface Delegate{
			
			void splitLayoutPanelResized();
	
			void columnClickedForSorting(String sortname, Sorting sortorder);
			
			void displayQuestionByAssessment(Long assessmentId);
		}
	
	 	AdvanceCellTable<AssesmentQuestionProxy> getTable();
	
		String[] getPaths();
	    
	    void setDelegate(Delegate delegate);
	    
		SimplePanel getDetailsPanel();
		
		ScrollPanel getScrollDetailPanel();
		
		QuestionAdvancedSearchSubViewImpl getQuestionAdvancedSearchSubViewImpl();
		
		void removeAdvancedSearchFromView();
		
		void setAssessmentSuggestBoxPickerValues(List<AssesmentProxy> values);
		
		DefaultSuggestBox<AssesmentProxy, EventHandlingValueHolderItem<AssesmentProxy>> getAssessmentSuggestBox();
}
