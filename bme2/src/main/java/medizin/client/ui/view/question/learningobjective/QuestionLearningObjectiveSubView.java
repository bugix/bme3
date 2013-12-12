package medizin.client.ui.view.question.learningobjective;

import java.util.Map;

import medizin.client.proxy.MainQuestionSkillProxy;
import medizin.client.proxy.MinorQuestionSkillProxy;

public interface QuestionLearningObjectiveSubView {
	interface Delegate {
		public void minorDeleteClicked(MinorQuestionSkillProxy minorSkill);
		public void majorDeleteClicked(MainQuestionSkillProxy mainSkill);
		
		public void addMainClicked();
		public void addMinorClicked();
		
		public void setMainClassiPopUpListBox(QuestionLearningObjectivePopupView popupView);
		public void setSkillLevelPopupListBox(QuestionLearningObjectivePopupView popupView);
		
		public void loadLearningObjectiveData();
		
		public void closeButtonClicked();
		
		public void clearAllButtonClicked();
		
		void initLearningObjectiveView();
	}
	public void setDelegate(Delegate delegate);
	Map getMainSkillMap();
	
	//learning
	public LearningObjectiveViewImpl getLearningObjectiveViewImpl();
	
	void initLearningObjectiveView();
}
