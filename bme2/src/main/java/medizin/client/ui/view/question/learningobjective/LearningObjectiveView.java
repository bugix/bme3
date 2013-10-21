package medizin.client.ui.view.question.learningobjective;

import medizin.client.proxy.ApplianceProxy;
import medizin.client.proxy.ClassificationTopicProxy;
import medizin.client.proxy.MainClassificationProxy;
import medizin.client.proxy.SkillLevelProxy;
import medizin.client.proxy.TopicProxy;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import medizin.shared.LearningObjectiveData;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.MultiSelectionModel;

public interface LearningObjectiveView extends IsWidget {
	public interface Presenter {
        void goTo(Place place);
    }

	interface Delegate {
		void mainClassificationSuggestboxChanged(Long value);
		
		void classificationTopicSuggestboxChanged(Long value);
		
		void topicSuggestboxChanged(Long value);
		
		void skillLevelSuggestboxChanged(Long value);
		
		void applianceSuggestboxChanged(Long value);
		
	}

	void setDelegate(Delegate delegate);
  
    public CellTable<LearningObjectiveData> getTable();
    
    public MultiSelectionModel<LearningObjectiveData> getMultiselectionModel();
    
    public HorizontalPanel getHpBtnPanel();
    
    public DefaultSuggestBox<MainClassificationProxy, EventHandlingValueHolderItem<MainClassificationProxy>> getMainClassificationSuggestBox();    
    public void setMainClassificationSuggestBox(DefaultSuggestBox<MainClassificationProxy, EventHandlingValueHolderItem<MainClassificationProxy>> mainClassificationSuggestBox);
    
    public DefaultSuggestBox<ClassificationTopicProxy, EventHandlingValueHolderItem<ClassificationTopicProxy>> getClassificationTopicSuggestBox();    
    public void setClassificationTopicSuggestBox(DefaultSuggestBox<ClassificationTopicProxy, EventHandlingValueHolderItem<ClassificationTopicProxy>> classificationTopicSuggestBox);
    
    public DefaultSuggestBox<TopicProxy, EventHandlingValueHolderItem<TopicProxy>> getTopicSuggestBox();    
    public void setTopicSuggestBox(DefaultSuggestBox<TopicProxy, EventHandlingValueHolderItem<TopicProxy>> topicSuggestBox);
    
    public DefaultSuggestBox<SkillLevelProxy, EventHandlingValueHolderItem<SkillLevelProxy>> getSkillLevelSuggestBox();    
    public void setSkillLevelSuggestBox(DefaultSuggestBox<SkillLevelProxy, EventHandlingValueHolderItem<SkillLevelProxy>> skillLevelSuggestBox);
    
    public DefaultSuggestBox<ApplianceProxy, EventHandlingValueHolderItem<ApplianceProxy>> getApplianceSuggestBox();    
    public void setApplianceSuggestBox(DefaultSuggestBox<ApplianceProxy, EventHandlingValueHolderItem<ApplianceProxy>> applianceSuggestBox);
    
    public Label getMainClassiLbl();
    public void setMainClassiLbl(Label mainClassiLbl);
    
    public Label getClassiTopicLbl();
    public void setClassiTopicLbl(Label classiTopicLbl);
    
    public Label getTopicLbl();
	public void setTopicLbl(Label topicLbl);
	
	public Label getSkillLevelLbl();
	public void setSkillLevelLbl(Label skillLevelLbl);
	
	public Label getApplianceLbl();
	public void setApplianceLbl(Label applianceLbl);
	
	public ScrollPanel getLearningScrollPanel();
}
