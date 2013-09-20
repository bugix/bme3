package medizin.client.activites;

import java.util.List;
import java.util.Map;
import java.util.Set;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceSystemOverview;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.AssesmentQuestionProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionSumPerPersonProxy;
import medizin.client.proxy.QuestionTypeCountPerExamProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.request.AnswerRequest;
import medizin.client.request.AssesmentRequest;
import medizin.client.request.PersonRequest;
import medizin.client.request.QuestionRequest;
import medizin.client.ui.view.SystemOverviewExaminerSubView;
import medizin.client.ui.view.SystemOverviewExaminerSubViewImpl;
import medizin.client.ui.view.SystemOverviewView;
import medizin.client.ui.view.SystemOverviewViewImpl;
import medizin.client.util.ClientUtility;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class ActivitySystemOverview extends AbstractActivityWrapper implements SystemOverviewView.Delegate, SystemOverviewExaminerSubView.Delegate {

	private PlaceSystemOverview overviewPlace;
	private AcceptsOneWidget widget;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	private Long acceptQuestionCount = 0l;
	private Long acceptAnswerCount = 0l;
	private SystemOverviewView view;
	private static final Function<QuestionTypeProxy, String> QUESTIONTYPE_TO_SHORTNAME = new Function<QuestionTypeProxy, String>() {

		@Override
		public String apply(QuestionTypeProxy input) {
			return input.getShortName();
		}
	};
	
	@Inject
	public ActivitySystemOverview(PlaceSystemOverview place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.overviewPlace = place;
        this.requests = requests;
        this.placeController = placeController;
	}

	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		SystemOverviewView systemOverview = new SystemOverviewViewImpl();
		systemOverview.setDelegate(this);
		this.widget = widget;
        widget.setWidget(systemOverview.asWidget());
        view = systemOverview;
        init();
	}
	
	public void init()
	{
		if (personRightProxy.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin())
		{
			QuestionRequest questionRequest = requests.questionRequest();
			questionRequest.countQuestionByLoggedUser(userLoggedIn.getId(), true).to(new BMEReceiver<Long>() {

				@Override
				public void onSuccess(Long response) {
					acceptQuestionCount = response;
				}
			});
			
			AnswerRequest answerRequest = questionRequest.append(requests.answerRequest());
			answerRequest.countAnswerByLoggedUser(true, userLoggedIn.getId()).to(new BMEReceiver<Long>() {

				@Override
				public void onSuccess(Long response) {
					acceptAnswerCount = response;
					view.setAcceptQuestionAndAnswer(acceptQuestionCount, acceptAnswerCount);
				}
			});
			
			PersonRequest personRequest = answerRequest.append(requests.personRequest());
			personRequest.findAllPeople().to(new BMEReceiver<List<PersonProxy>>() {

				@Override
				public void onSuccess(List<PersonProxy> response) {
					createExaminerSubView(response);
				}				
			});
			
			personRequest.fire();
		}
		else
		{
			createExaminerView();
		}
	}

	private void createExaminerView() {
		QuestionRequest questionRequest = requests.questionRequest();
		questionRequest.countQuestionByLoggedUser(userLoggedIn.getId(), false).to(new BMEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {
				acceptQuestionCount = response;
			}
		});
		
		AnswerRequest answerRequest = questionRequest.append(requests.answerRequest());
		answerRequest.countAnswerByLoggedUser(false, userLoggedIn.getId()).to(new BMEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {
				acceptAnswerCount = response;
				view.setAcceptQuestionAndAnswer(acceptQuestionCount, acceptAnswerCount);				
			}
		});
		
		AssesmentRequest assesmentRequest = answerRequest.append(requests.assesmentRequest());
		assesmentRequest.findAssessmentByLoggedUser(userLoggedIn.getId(), false).with("mc", "questionSumPerPerson", "questionSumPerPerson.responsiblePerson", "questionSumPerPerson.questionEvent", "questionTypeCountPerExams", "questionTypeCountPerExams.questionTypesAssigned", "assesmentQuestions.question", "assesmentQuestions.question.questEvent", "assesmentQuestions.question.questionType", "assesmentQuestions.autor").to(new BMEReceiver<List<AssesmentProxy>>() {

			@Override
			public void onSuccess(List<AssesmentProxy> response) {
				for (AssesmentProxy assesmentProxy : response)
				{
					Map<String, String> quesitonTypeCountMap = countQuestionTypeCountPerAssesment(assesmentProxy, userLoggedIn);
					
					view.setQuestionTypesCountByAssessment(assesmentProxy.getMc().getMcName(), ClientUtility.SHORT_FORMAT.format(assesmentProxy.getDateClosed()), quesitonTypeCountMap);
				}
			}
		});
		
		assesmentRequest.fire();
	}
	
	private void createExaminerSubView(List<PersonProxy> response) {
		AssesmentRequest finalAssesmentRequest = null;
		for (final PersonProxy personProxy : response)
		{
			if (personProxy.getId().equals(userLoggedIn.getId()) == false)
			{
				final PersonProxy tempPersonProxy = personProxy;
				final SystemOverviewExaminerSubView examinerSubView = new SystemOverviewExaminerSubViewImpl();
				examinerSubView.setDelegate(ActivitySystemOverview.this);
				examinerSubView.setPersonProxy(personProxy);
				
				QuestionRequest questionRequest;
				if(finalAssesmentRequest != null) {
					questionRequest = finalAssesmentRequest.append(requests.questionRequest());
				}else {
					questionRequest = requests.questionRequest();
				}
				
				questionRequest.countQuestionByLoggedUser(personProxy.getId(), false).to(new BMEReceiver<Long>() {

					@Override
					public void onSuccess(Long response) {
						acceptQuestionCount = response;
					}
				});
				
				AnswerRequest answerRequest = questionRequest.append(requests.answerRequest());
				answerRequest.countAnswerByLoggedUser(false, personProxy.getId()).to(new BMEReceiver<Long>() {

					@Override
					public void onSuccess(Long response) {
						acceptAnswerCount = response;		
						examinerSubView.setAcceptAnswerAndQuestion((tempPersonProxy.getPrename() + " " + tempPersonProxy.getName()), acceptQuestionCount, acceptAnswerCount);
					}
				});
				
				finalAssesmentRequest = answerRequest.append(requests.assesmentRequest());
				finalAssesmentRequest.findAssessmentByLoggedUser(personProxy.getId(), false).with("mc", "questionSumPerPerson", "questionSumPerPerson.responsiblePerson", "questionSumPerPerson.questionEvent", "questionTypeCountPerExams", "questionTypeCountPerExams.questionTypesAssigned", "assesmentQuestions.question", "assesmentQuestions.question.questEvent", "assesmentQuestions.question.questionType", "assesmentQuestions.autor").to(new BMEReceiver<List<AssesmentProxy>>() {

					@Override
					public void onSuccess(List<AssesmentProxy> response) {
						examinerSubView.setAssesmentProxy(response);
						for (AssesmentProxy assesmentProxy : response)
						{
							Map<String, String> quesitonTypeCountMap = countQuestionTypeCountPerAssesment(assesmentProxy, personProxy);
							
							view.setQuestionTypesCountByAssessmentExaminer(assesmentProxy.getMc().getMcName(), ClientUtility.SHORT_FORMAT.format(assesmentProxy.getDateClosed()), quesitonTypeCountMap, examinerSubView);
							
							view.getMainVerticalPanel().setSpacing(5);
							
							if (view.getMainVerticalPanel().getWidgetCount() == 1)
								examinerSubView.getExaminerDisclosurePanel().setOpen(true); 
								
							view.getMainVerticalPanel().add(examinerSubView);
						}
					}
				});
				
				//assesmentRequest.fire();
			}
		}
		
		if(finalAssesmentRequest != null)
			finalAssesmentRequest.fire();
	}
	
	private Map<String, String> countQuestionTypeCountPerAssesment(AssesmentProxy assesmentProxy, PersonProxy personProxy)
	{
		Map<String, String> quesitonTypeCountMap = Maps.newHashMap();
		for (QuestionSumPerPersonProxy questionSumPerPersonProxy : assesmentProxy.getQuestionSumPerPerson())
		{
			if (questionSumPerPersonProxy.getResponsiblePerson().getId().equals(personProxy.getId()))
			{
				for (QuestionTypeCountPerExamProxy questionTypeCountPerExamProxy : assesmentProxy.getQuestionTypeCountPerExams())
				{
					int count = 0;
					String questionType = "";
					count = ((questionTypeCountPerExamProxy.getQuestionTypeCount() * questionSumPerPersonProxy.getQuestionSum()) / 100);
					
					if (assesmentProxy.getAssesmentQuestions() != null && questionTypeCountPerExamProxy.getQuestionTypesAssigned() != null && questionSumPerPersonProxy.getQuestionEvent() != null)
					{
						int queTypeCount = countAssessmentQuestionByQuestionType(assesmentProxy.getAssesmentQuestions(), questionTypeCountPerExamProxy.getQuestionTypesAssigned(), questionSumPerPersonProxy.getQuestionEvent(), personProxy);
						count = queTypeCount - count;
						questionType = Joiner.on(", ").join(FluentIterable.from(questionTypeCountPerExamProxy.getQuestionTypesAssigned()).transform(QUESTIONTYPE_TO_SHORTNAME));
						questionType += " " + constants.question() + " (" + questionSumPerPersonProxy.getQuestionEvent().getEventName() + ")";
					}
					quesitonTypeCountMap.put(questionType, String.valueOf(count));
				}
			}
		}
		
		return quesitonTypeCountMap;
	}
	
	private int countAssessmentQuestionByQuestionType(Set<AssesmentQuestionProxy> assQueProxySet, Set<QuestionTypeProxy> queTypeProxySet, QuestionEventProxy questionEventProxy, PersonProxy personProxy)
	{
		int count = 0;
		for (AssesmentQuestionProxy assQuestion : assQueProxySet)
		{
			for (QuestionTypeProxy questionType : queTypeProxySet)
			{
				if (assQuestion.getAutor().getId().equals(personProxy.getId()) && questionType.getId().equals(assQuestion.getQuestion().getQuestionType().getId()) && assQuestion.getQuestion().getQuestEvent().getId().equals(questionEventProxy.getId()))
				{
					count += 1;
				}
			}
		}
		return count;
	}

	@Override
	public void placeChanged(Place place) {}

	@Override
	public void sendMailBtnClicked(PersonProxy personProxy, List<AssesmentProxy> assesmentProxy, String messageContent) {
		requests.assesmentRequest().systemOverviewSendMail(personProxy.getId(), assesmentProxy, messageContent, constants.mailSubject()).fire(new BMEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				if (response)
					Window.alert("Mail Sent Successfully");
				else
					Window.alert("Error in sending mail");
			}
		});
	}
	
	@Override
	public void loadTemplate(final SystemOverviewExaminerSubViewImpl examinerSubViewImpl) {
		requests.assesmentQuestionRequest().loadSystemOverviewTemplate().fire(new BMEReceiver<String>() {

			@Override
			public void onSuccess(String response) {
				examinerSubViewImpl.displayMail(response);
			}
		});
	}
}
