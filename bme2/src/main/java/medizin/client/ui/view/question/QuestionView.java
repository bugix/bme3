package medizin.client.ui.view.question;

import java.util.List;
import java.util.Map;

import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.style.resources.AdvanceCellTable;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchSubViewImpl;
import medizin.client.ui.widget.QuickSearchBox;
import medizin.client.ui.widget.Sorting;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

public interface QuestionView extends IsWidget {
	/*void setName(String helloName);

    void setPresenter(Presenter activityQuestion);

    public interface Presenter {
        void goTo(Place place);
    }*/
    
    /**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void newClicked();
		
		void performSearch(String searchText);

		void splitLayoutPanelResized();

		void columnClickedForSorting(String sortname, Sorting sortorder);

		void printPdfClicked(int left, int top);
	}
    
    //Table for Assesments (Pr�fungshefter)
	AdvanceCellTable<QuestionProxy> getTable();
    
    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
	SimplePanel getDetailsPanel();

	

	//void setInstitutionFilter(List<InstitutionProxy> values);

	//void setSpecialisationFilter(List<QuestionEventProxy> values);

	Map<String, Object> getSearchFiledValue();

	QuickSearchBox getSerachBox();

	List<String> getSearchValue();

	QuestionAdvancedSearchSubViewImpl getQuestionAdvancedSearchSubViewImpl();
	
	void removeAdvancedSearchFromView();
	 
	public QuestionFilterViewImpl getFilterPanel();

}
