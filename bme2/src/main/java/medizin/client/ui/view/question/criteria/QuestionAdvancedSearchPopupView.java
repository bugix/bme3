package medizin.client.ui.view.question.criteria;

import medizin.shared.criteria.AdvancedSearchCriteria;

import com.google.gwt.user.client.ui.Button;

public interface QuestionAdvancedSearchPopupView {

	interface Delegate{
		void advancedSearchCriteriaClicked(AdvancedSearchCriteria criteria);
	}
	
	void display(Button parentButton);
	
	void setDelegate(Delegate delegate);

}
