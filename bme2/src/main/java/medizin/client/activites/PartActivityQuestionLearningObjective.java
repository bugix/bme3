package medizin.client.activites;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import medizin.client.events.RecordChangeEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.ApplianceProxy;
import medizin.client.proxy.ClassificationTopicProxy;
import medizin.client.proxy.MainClassificationProxy;
import medizin.client.proxy.MainQuestionSkillProxy;
import medizin.client.proxy.MinorQuestionSkillProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.SkillHasApplianceProxy;
import medizin.client.proxy.SkillLevelProxy;
import medizin.client.proxy.SkillProxy;
import medizin.client.proxy.TopicProxy;
import medizin.client.request.MainQuestionSkillRequest;
import medizin.client.request.MinorQuestionSkillRequest;
import medizin.client.request.SkillRequest;
import medizin.client.ui.view.question.learningobjective.LearningObjectiveView;
import medizin.client.ui.view.question.learningobjective.QuestionLearningObjectivePopupView;
import medizin.client.ui.view.question.learningobjective.QuestionLearningObjectiveSubView;
import medizin.client.ui.view.question.learningobjective.QuestionLearningObjectiveSubViewImpl;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import medizin.shared.LearningObjectiveData;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

public class PartActivityQuestionLearningObjective implements LearningObjectiveView.Delegate, QuestionLearningObjectiveSubView.Delegate, QuestionLearningObjectivePopupView.Delegate{
	
	private Long mainClassificationId = null;
	private Long classificationTopicId = null;
	private Long topicId = null;
	private Long skillLevelId = null;
	private Long applianceId = null;
	private LearningObjectiveView learningObjectiveView;
	private LearningObjectiveData learningObjective;
	private String temp;
	private List<LearningObjectiveData> learningObjectiveData = new ArrayList<LearningObjectiveData>();
	
	public BmeConstants constants = GWT.create(BmeConstants.class);
	private final McAppRequestFactory requests;
	private final QuestionLearningObjectiveSubViewImpl view;
	private QuestionProxy question;
	private final boolean readOnly;
	
	public PartActivityQuestionLearningObjective(McAppRequestFactory requests,QuestionLearningObjectiveSubViewImpl view, boolean readOnly){
		this.requests = requests;
		this.view = view;
		this.view.setDelegate(this);
		this.readOnly = readOnly;
		
		if(this.readOnly == true) {
			view.btnAdd.removeFromParent();	
		}
		
	}
	
	public void setQuestionProxy(QuestionProxy questionProxy) {
		this.question = questionProxy;
	}
	
	@Override
	public void mainClassificationSuggestboxChanged(Long value) {
		this.mainClassificationId = value;
		learningObjectiveView.getClassificationTopicSuggestBox().setSelected(null);
		classificationTopicId = null;
		
		fillClassificationTopicSuggestBox(mainClassificationId);
		onLearningObjectiveViewTableRangeChanged();
	}

	@Override
	public void classificationTopicSuggestboxChanged(Long value) {
		this.classificationTopicId = value;
		
		learningObjectiveView.getTopicSuggestBox().setSelected(null);
		topicId = null;
		
		fillTopicSuggestBox(classificationTopicId);
		onLearningObjectiveViewTableRangeChanged();
	}

	@Override
	public void topicSuggestboxChanged(Long value) {
		this.topicId = value;		
		onLearningObjectiveViewTableRangeChanged();
	}

	@Override
	public void skillLevelSuggestboxChanged(Long value) {
		this.skillLevelId = value;
		onLearningObjectiveViewTableRangeChanged();
	}

	@Override
	public void applianceSuggestboxChanged(Long value) {
		this.applianceId = value;
		onLearningObjectiveViewTableRangeChanged();
	}
	
	public void fillClassificationTopicSuggestBox(Long mainClassificationId)
	{
		requests.classificationTopicRequest().findClassificationTopicByMainClassification(mainClassificationId).fire(new BMEReceiver<List<ClassificationTopicProxy>>() {

			@Override
			public void onSuccess(List<ClassificationTopicProxy> response) {
				DefaultSuggestOracle<ClassificationTopicProxy> suggestOracle = (DefaultSuggestOracle<ClassificationTopicProxy>) learningObjectiveView.getClassificationTopicSuggestBox().getSuggestOracle();
				suggestOracle.setPossiblilities(response);
				learningObjectiveView.getClassificationTopicSuggestBox().setSuggestOracle(suggestOracle);
				
				learningObjectiveView.getClassificationTopicSuggestBox().setRenderer(new AbstractRenderer<ClassificationTopicProxy>() {

					@Override
					public String render(ClassificationTopicProxy object) {
						if (object != null)
							return (object.getDescription() + "[" + object.getShortcut() + "]");
						else
							return "";
					}
				});
			}
		});
	}
	
	public void fillTopicSuggestBox(Long classificationTopicId)
	{
		requests.topicRequest().findTopicByClassificationTopicId(classificationTopicId).fire(new BMEReceiver<List<TopicProxy>>() {

			@Override
			public void onSuccess(List<TopicProxy> response) {
				DefaultSuggestOracle<TopicProxy> suggestOracle = (DefaultSuggestOracle<TopicProxy>) learningObjectiveView.getTopicSuggestBox().getSuggestOracle();
				suggestOracle.setPossiblilities(response);
				learningObjectiveView.getTopicSuggestBox().setSuggestOracle(suggestOracle);
				
				learningObjectiveView.getTopicSuggestBox().setRenderer(new AbstractRenderer<TopicProxy>() {

					@Override
					public String render(TopicProxy object) {
						if (object != null)
							return object.getTopicDesc();
						else
							return "";
					}
				});
			}
		});
	}
	
	public void fillSkillLevelSuggestBox()
	{
		// Added this to show all skill level by its level as ASC - Manish.
		requests.skillLevelRequest().findAllSkillLevelsByLevelASC().fire(new BMEReceiver<List<SkillLevelProxy>>() {
		//requests.skillLevelRequest().findAllSkillLevels().fire(new BMEReceiver<List<SkillLevelProxy>>() {

			@Override
			public void onSuccess(List<SkillLevelProxy> response) {
				DefaultSuggestOracle<SkillLevelProxy> suggestOracle = (DefaultSuggestOracle<SkillLevelProxy>) learningObjectiveView.getSkillLevelSuggestBox().getSuggestOracle();
				suggestOracle.setPossiblilities(response);
				learningObjectiveView.getSkillLevelSuggestBox().setSuggestOracle(suggestOracle);
				
				learningObjectiveView.getSkillLevelSuggestBox().setRenderer(new AbstractRenderer<SkillLevelProxy>() {

					@Override
					public String render(SkillLevelProxy object) {
						if (object != null)
							return String.valueOf(object.getLevelNumber());
						else
							return "";
					}
				});
			}
		});
	}
	
	public void fillApplianceSuggestBox()
	{
		// Added this to show all appliances by it shortcut as ASC - Manish.
		requests.applianceRequest().findAllAppliancesByShortcutASC().fire(new BMEReceiver<List<ApplianceProxy>>() {
		//requests.applianceRequest().findAllAppliances().fire(new BMEReceiver<List<ApplianceProxy>>() {

			@Override
			public void onSuccess(List<ApplianceProxy> response) {
				DefaultSuggestOracle<ApplianceProxy> suggestOracle = (DefaultSuggestOracle<ApplianceProxy>) learningObjectiveView.getApplianceSuggestBox().getSuggestOracle();
				suggestOracle.setPossiblilities(response);
				learningObjectiveView.getApplianceSuggestBox().setSuggestOracle(suggestOracle);
				
				learningObjectiveView.getApplianceSuggestBox().setRenderer(new AbstractRenderer<ApplianceProxy>() {

					@Override
					public String render(ApplianceProxy object) {
						if (object != null)
							return object.getShortcut();
						else
							return "";
					}
				});		
			}
		});
	}
	
	public void onLearningObjectiveViewTableRangeChanged()
	{
		learningObjectiveData.clear();
		final Range range = learningObjectiveView.getTable().getVisibleRange();
		
		SkillRequest skillRequest = requests.skillRequest();
		skillRequest.countSkillBySearchCriteria(mainClassificationId, classificationTopicId, topicId, skillLevelId, applianceId).to(new BMEReceiver<Integer>() {

			@Override
			public void onSuccess(Integer response) {
				learningObjectiveView.getTable().setRowCount(response);
			}
		});
		
		SkillRequest skillRequest2 = skillRequest.append(requests.skillRequest());
		skillRequest2.findSkillBySearchCriteria(range.getStart(), range.getLength(), mainClassificationId, classificationTopicId, topicId, skillLevelId, applianceId).with("topic", "skillLevel", "skillHasAppliances", "skillHasAppliances.appliance", "topic.classificationTopic", "topic.classificationTopic.mainClassification").to(new BMEReceiver<List<SkillProxy>>() {

			@Override
			public void onSuccess(List<SkillProxy> response) {
				for (int i=0; i<response.size(); i++)
				{
					learningObjective = new LearningObjectiveData();
					SkillProxy skill = response.get(i);
					
					temp = skill.getTopic().getClassificationTopic().getMainClassification().getShortcut() + " " + skill.getTopic().getClassificationTopic().getShortcut() + " " + skill.getShortcut();
					learningObjective.setCode(temp);
					learningObjective.setSkill(skill);
					learningObjective.setText(skill.getDescription());
					learningObjective.setTopic(skill.getTopic().getTopicDesc());	
					
					if (skill.getSkillLevel() != null)
						learningObjective.setSkillLevel(String.valueOf(skill.getSkillLevel().getLevelNumber()));
					else
						learningObjective.setSkillLevel("");
					
					Iterator<SkillHasApplianceProxy> iter = skill.getSkillHasAppliances().iterator();
					
					while (iter.hasNext())
					{
						SkillHasApplianceProxy skillHasApplianceProxy = iter.next();
						
						if (skillHasApplianceProxy.getAppliance().getShortcut().equals("D"))
							learningObjective.setD("D");
						else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("T"))
							learningObjective.setT("T");
						else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("E"))
							learningObjective.setE("E");
						else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("P"))
							learningObjective.setP("P");
						else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("G"))
							learningObjective.setG("G");
					}
					
					learningObjectiveData.add(learningObjective);
					learningObjective = null;
				}
				
				learningObjectiveView.getTable().setRowData(range.getStart(), learningObjectiveData);
			}
		});
		
		skillRequest2.fire();
	}
	
	@Override
	public void addMainClicked() {
		if(readOnly == true) {
			return;
		}
		
		Iterator<LearningObjectiveData> itr = learningObjectiveView.getMultiselectionModel().getSelectedSet().iterator();
		
		while (itr.hasNext())
		{
			LearningObjectiveData learningObjectiveData = itr.next();
			MainQuestionSkillRequest mainQuestionSkillRequest = requests.mainQuestionSkillRequest();
			MainQuestionSkillProxy mainQuestionSkillProxy = mainQuestionSkillRequest.create(MainQuestionSkillProxy.class);
			mainQuestionSkillProxy.setQuestion(question);
			mainQuestionSkillProxy.setSkill(learningObjectiveData.getSkill());
			mainQuestionSkillRequest.persist().using(mainQuestionSkillProxy).fire(new BMEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
				
				}
			});
		}
	
		learningObjectiveView.getMultiselectionModel().clear();
	}

	@Override
	public void addMinorClicked() {
		if(readOnly == true) {
			return;
		}
		
		Iterator<LearningObjectiveData> itr = learningObjectiveView.getMultiselectionModel().getSelectedSet().iterator();
		
		while (itr.hasNext())
		{
			LearningObjectiveData learningObjectiveData = itr.next();
			MinorQuestionSkillRequest minorQuestionSkillRequest = requests.minorQuestionSkillRequest();
			MinorQuestionSkillProxy minorQuestionSkillProxy = minorQuestionSkillRequest.create(MinorQuestionSkillProxy.class);
			minorQuestionSkillProxy.setQuestion(question);
			minorQuestionSkillProxy.setSkill(learningObjectiveData.getSkill());
			minorQuestionSkillRequest.persist().using(minorQuestionSkillProxy).fire(new BMEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
				}
			});
		}
		
		learningObjectiveView.getMultiselectionModel().clear();
	}

	@Override
	public void setMainClassiPopUpListBox(final QuestionLearningObjectivePopupView popupView) {
		popupView.setDelegate(this);
		requests.mainClassificationRequest().findAllMainClassifications().fire(new BMEReceiver<List<MainClassificationProxy>>() {

			@Override
			public void onSuccess(List<MainClassificationProxy> response) {
				popupView.getMainClassiListBox().setAcceptableValues(response);
			}
		});
		
	}
	@Override
	public void setSkillLevelPopupListBox(final QuestionLearningObjectivePopupView popupView) {
		requests.skillLevelRequest().findAllSkillLevels().fire(new BMEReceiver<List<SkillLevelProxy>>() {

			@Override
			public void onSuccess(List<SkillLevelProxy> response) {
				popupView.getLevelListBox().setAcceptableValues(response);
			}
		});
	}

	@Override
	public void loadLearningObjectiveData() {
		learningObjectiveView = view.getLearningObjectiveViewImpl();
		learningObjectiveView.setDelegate(this);
		
		
		
		requests.skillRequest().countSkillBySearchCriteria(mainClassificationId, classificationTopicId, topicId, skillLevelId, applianceId).fire(new BMEReceiver<Integer>() {

			@Override
			public void onSuccess(Integer response) {
				learningObjectiveView.getTable().setRowCount(response);
				onLearningObjectiveViewTableRangeChanged();
				fillMainClassificationSuggestBox();
				fillClassificationTopicSuggestBox(mainClassificationId);
				fillTopicSuggestBox(classificationTopicId);
				fillSkillLevelSuggestBox();
				fillApplianceSuggestBox();			
			}
		});
		
		learningObjectiveView.getTable().addRangeChangeHandler(new RangeChangeEvent.Handler() {
			
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				onLearningObjectiveViewTableRangeChanged();
			}
		});
	}
	
	public void fillMainClassificationSuggestBox()
	{
		//Added this to show main classification in ASC order - Manish.
		requests.mainClassificationRequest().findAllMainClassificationByDescASC().fire(new BMEReceiver<List<MainClassificationProxy>>() {
		//requests.mainClassificationRequest().findAllMainClassifications().fire(new BMEReceiver<List<MainClassificationProxy>>() {

			@Override
			public void onSuccess(List<MainClassificationProxy> response) {
				DefaultSuggestOracle<MainClassificationProxy> suggestOracle1 = (DefaultSuggestOracle<MainClassificationProxy>) learningObjectiveView.getMainClassificationSuggestBox().getSuggestOracle();
				suggestOracle1.setPossiblilities(response);
				learningObjectiveView.getMainClassificationSuggestBox().setSuggestOracle(suggestOracle1);
				
				learningObjectiveView.getMainClassificationSuggestBox().setRenderer(new AbstractRenderer<MainClassificationProxy>() {

					@Override
					public String render(MainClassificationProxy object) {
						if (object != null)
							return (object.getDescription() +  "[" + object.getShortcut() + "]");
						else
							return "";
					}
				});
			}
		});
	}
	
	

	@Override
	public void closeButtonClicked() {
		mainClassificationId = null;
		classificationTopicId = null;
		topicId = null;
		skillLevelId = null;
		applianceId = null;
		learningObjectiveView = null;
		initLearningObjectiveView();
	}

	@Override
	public void clearAllButtonClicked() {
		loadLearningObjectiveData();
	}

	@Override
	public void mainClassiListBoxClicked(MainClassificationProxy proxy, final QuestionLearningObjectivePopupView popupView) {
		requests.classificationTopicRequest().findClassificationTopicByMainClassification(proxy.getId()).fire(new BMEReceiver<List<ClassificationTopicProxy>>() {

			@Override
			public void onSuccess(List<ClassificationTopicProxy> response) {
				popupView.getClassiTopicListBox().setAcceptableValues(response);
				
				if (response.size() > 0)
					classiTopicListBoxClicked(response.get(0), popupView);
			}
		});
	}

	@Override
	public void classiTopicListBoxClicked(ClassificationTopicProxy proxy, final QuestionLearningObjectivePopupView popupView) {
		requests.topicRequest().findTopicByClassificationTopicId(proxy.getId()).fire(new BMEReceiver<List<TopicProxy>>() {

			@Override
			public void onSuccess(List<TopicProxy> response) {
				popupView.getTopicListBox().setAcceptableValues(response);
			}
		});
	}
	
	public void initLearningObjectiveView() {
		RecordChangeEvent.register(requests.getEventBus(), view);
		if (question != null)
		{
			MainQuestionSkillRequest mainQuestionSkillRequest = requests.mainQuestionSkillRequest();
			mainQuestionSkillRequest.countMainQuestionSkillByQuestionId(question.getId()).to(new BMEReceiver<Integer>() {

				@Override
				public void onSuccess(Integer response) {
					view.majorTable.setRowCount(response);
					onMainQuestionSkillTableRangeChanged();
				}
			});
			
			MinorQuestionSkillRequest minorQuestionSkillRequest = mainQuestionSkillRequest.append(requests.minorQuestionSkillRequest());
			minorQuestionSkillRequest.countMinorQuestionSkillByQuestionId(question.getId()).to(new BMEReceiver<Integer>() {

				@Override
				public void onSuccess(Integer response) {
					view.minorTable.setRowCount(response);
					onMinorQuestionSkillTableRangeChanged();
				}
			});
			
			minorQuestionSkillRequest.fire();
			
			view.majorTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
				
				@Override
				public void onRangeChange(RangeChangeEvent event) {
					onMainQuestionSkillTableRangeChanged();
				}
			});
			
			view.minorTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
				
				@Override
				public void onRangeChange(RangeChangeEvent event) {
					onMinorQuestionSkillTableRangeChanged();
				}
			});
		}
	}
	
	public void onMinorQuestionSkillTableRangeChanged()
	{
		if (question != null)
		{
			final Range range = view.minorTable.getVisibleRange();
			
			requests.minorQuestionSkillRequest().findMinorQuestionSkillByQuestionId(question.getId(), range.getStart(), range.getLength()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new BMEReceiver<List<MinorQuestionSkillProxy>>() {

				@Override
				public void onSuccess(List<MinorQuestionSkillProxy> response) {
					view.minorTable.setRowData(range.getStart(), response);
				}
			});
		}
	}
	
	public void onMainQuestionSkillTableRangeChanged()
	{
		if (question != null)
		{
			final Range range = view.majorTable.getVisibleRange();
			requests.mainQuestionSkillRequest().findMainQuestionSkillByQuestionId(question.getId(), range.getStart(), range.getLength()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new BMEReceiver<List<MainQuestionSkillProxy>>() {

				@Override
				public void onSuccess(List<MainQuestionSkillProxy> response) {
					view.majorTable.setRowData(range.getStart(), response);
				}
			});
		}
	}
	
	@Override
	public void minorDeleteClicked(MinorQuestionSkillProxy minorSkill) {
		
		if(readOnly == true) {
			return;
		}
		
		requests.minorQuestionSkillRequest().remove().using(minorSkill).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				initLearningObjectiveView();
			}
		});
	}

	@Override
	public void majorDeleteClicked(MainQuestionSkillProxy mainSkill) {
		if(readOnly == true) {
			return;
		}
		requests.mainQuestionSkillRequest().remove().using(mainSkill).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				initLearningObjectiveView();
			}
		});
	}

}
