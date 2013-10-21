package medizin.client.ui.view.question.learningobjective;

import medizin.client.proxy.ClassificationTopicProxy;
import medizin.client.proxy.MainClassificationProxy;
import medizin.client.proxy.SkillLevelProxy;
import medizin.client.proxy.TopicProxy;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueListBox;

public interface QuestionLearningObjectivePopupView extends IsWidget {
	
	interface Delegate{
		public void mainClassiListBoxClicked(MainClassificationProxy proxy, QuestionLearningObjectivePopupView popupView);
		public void classiTopicListBoxClicked(ClassificationTopicProxy proxy, QuestionLearningObjectivePopupView popupView);
	}
	
	public void setDelegate(Delegate delegate);	
	public Label getMainClssiLbl();
	public Label getClassiTopicLbl();
	public Label getTopicLbl();
	public ValueListBox<MainClassificationProxy> getMainClassiListBox();
	public ValueListBox<ClassificationTopicProxy> getClassiTopicListBox(); 
	public ValueListBox<TopicProxy> getTopicListBox();
	public Label getLevelLbl();
	public ValueListBox<SkillLevelProxy> getLevelListBox();
	public Button getOkBtn(); 
	public Button getCancelBtn(); 
}
