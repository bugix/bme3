package medizin.client.ui.view.question.criteria;

import medizin.client.ui.widget.IconButton;
import medizin.shared.criteria.AdvancedSearchCriteria;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

public interface QuestionAdvancedSearchSubView extends IsWidget {
	
	  public interface Presenter {
	        void goTo(Place place);
	  }
	  
	  interface Delegate {
		void keywordAddClicked(IconButton addKeyword);

		void questionEventAddClicked(IconButton addKeyword);

		void deleteAdvancedSearchCriteriaClicked(AdvancedSearchCriteria object);

		void textSearchAddClicked(IconButton addTextSearch);

		void dateAddClicked(IconButton addDate);

		void mcAddClicked(IconButton addMc);

		void userTypeAddClicked(IconButton addUserType);

		void mediaAvailabilityAddClicked(IconButton addMediaAvailability);

		void questionTypeAddClicked(IconButton addQuestionType);		  
	  }

	  void setDelegate(Delegate delegate);
	  
	  CellTable<AdvancedSearchCriteria> getTable();
}
