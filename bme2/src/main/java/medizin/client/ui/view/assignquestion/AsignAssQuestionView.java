package medizin.client.ui.view.assignquestion;

import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchSubViewImpl;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface AsignAssQuestionView extends IsWidget {

    void setPresenter(Presenter activityAsignAssQuestion);

    public interface Presenter {
        void goTo(Place place);
    }

	QuestionPanelImpl getQuestionPanel();
	AssesmentTabPanelImpl getAssesmentTabPanel();
	AssesmentQuestionPanelImpl getAssesmentQuestionPanel();
	AddQuestionsTabPanelImpl getAddQuestionsTabPanel();
	QuestionAdvancedSearchSubViewImpl getQuestionAdvancedSearchSubViewImpl();
}
