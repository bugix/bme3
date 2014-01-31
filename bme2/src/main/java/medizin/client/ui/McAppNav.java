package medizin.client.ui;


import medizin.client.McAppShell;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAcceptAnswer;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceAsignAssQuestion;
import medizin.client.place.PlaceAssesment;
import medizin.client.place.PlaceBookAssesment;
import medizin.client.place.PlaceDeactivatedQuestion;
import medizin.client.place.PlaceInstitution;
import medizin.client.place.PlaceNotActivatedAnswer;
import medizin.client.place.PlaceNotActivatedQuestion;
import medizin.client.place.PlaceQuestion;
import medizin.client.place.PlaceQuestionInAssessment;
import medizin.client.place.PlaceQuestiontypes;
import medizin.client.place.PlaceSystemOverview;
import medizin.client.place.PlaceUser;
import medizin.client.util.ClientUtility;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
/**
 * The applications main navigation element, shown on the left hand side of the user interface.
 * @author masterthesis
 *
 */
public class McAppNav extends Composite {

	private static McAppNavUiBinder uiBinder = GWT.create(McAppNavUiBinder.class);

	interface McAppNavUiBinder extends UiBinder<Widget, McAppNav> {
	}
	
	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	@UiField
	DisclosurePanel overviewPanel;
	@UiField
	DisclosurePanel assessmentQuestionsPanel;
	@UiField
	DisclosurePanel assesmentsPanel;
	@UiField
	DisclosurePanel administrationPanel;
	@UiField
	DisclosurePanel reviewProcessPanel;	
	@UiField
	Anchor systemOverview;
	@UiField
	Anchor acceptQuestion;
	@UiField
	Anchor acceptAnswer;
	@UiField
	Anchor user;
	@UiField
	Anchor question;
	@UiField
	Anchor notActivatedQuestion;
	@UiField
	Anchor questionType;
	@UiField
	Anchor institution;
	@UiField
	Anchor assesment;
	@UiField
	Anchor asignAssQuestion;
	@UiField
	Anchor bookAssesment;
	@UiField
	Anchor deactivatedQuestion;	
	@UiField
	Anchor notActivatedAnswer;
	@UiField
	Anchor questionInAssessment;

	@UiHandler("systemOverview")
	void systemOverviewClicked(ClickEvent event) {
		placeController.goTo(new PlaceSystemOverview(PlaceSystemOverview.PLACE_SYSTEM_OVERVIEW, true));
	}
	
	@UiHandler("acceptQuestion")
	void acceptQuestionClicked(ClickEvent event) {
		placeController.goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION, true));
	}
	@UiHandler("acceptAnswer")
	void PlaceAcceptAnswerClicked(ClickEvent event) {
		placeController.goTo(new PlaceAcceptAnswer(PlaceAcceptAnswer.PLACE_ACCEPT_ANSWER, true));
	}
	@UiHandler("user")
	void userClicked(ClickEvent event) {
		placeController.goTo(new PlaceUser(PlaceUser.PLACE_USER, true));
	}
	@UiHandler("question")
	void questionClicked(ClickEvent event) {
		placeController.goTo(new PlaceQuestion(PlaceQuestion.PLACE_QUESTION, true));
	}
	@UiHandler("notActivatedQuestion")
	void notActivatedQuestionClicked(ClickEvent event) {
		placeController.goTo(new PlaceNotActivatedQuestion(PlaceNotActivatedQuestion.PLACE_NOT_ACTIVATED_QUESTION, true));
	}
	@UiHandler("questionType")
	void questionTypeClicked(ClickEvent event) {
		placeController.goTo(new PlaceQuestiontypes(PlaceQuestiontypes.PLACE_QUESTIONTYPES, true));
	}
	@UiHandler("institution")
	void institutionClicked(ClickEvent event) {
		placeController.goTo(new PlaceInstitution(PlaceInstitution.PLACE_INSTITUTION, true));
	}
	@UiHandler("assesment")
	void assesmentClicked(ClickEvent event) {
		placeController.goTo(new PlaceAssesment(PlaceAssesment.PLACE_ASSESMENT, true));
	}
	@UiHandler("asignAssQuestion")
	void asignAssQuestionClicked(ClickEvent event) {
		placeController.goTo(new PlaceAsignAssQuestion(PlaceAsignAssQuestion.PLACE_ASIGN_ASS_QUESTION, true));
	}
	@UiHandler("bookAssesment")
	void bookAssesmentClicked(ClickEvent event) {
		placeController.goTo(new PlaceBookAssesment(PlaceBookAssesment.PLACE_BOOK_ASSESMENT, true));
	}
	@UiHandler("notActivatedAnswer")
	void notActivatedAnswerClicked(ClickEvent event){
		placeController.goTo(new PlaceNotActivatedAnswer(PlaceNotActivatedAnswer.PLACE_NOT_ACTIVATED_ANSWER, true));
	}
	@UiHandler("deactivatedQuestion")
	void deactivatedQuestionClicked(ClickEvent event){
		placeController.goTo(new PlaceDeactivatedQuestion(PlaceDeactivatedQuestion.PLACE_DEACTIVATED_QUESTION, true));
	}
	@UiHandler("questionInAssessment")
	void questionInAssessmentClicked(ClickEvent event){
		placeController.goTo(new PlaceQuestionInAssessment(PlaceQuestionInAssessment.PLACE_QUESTION_IN_ASSESSMENT, true));
	}

	private McAppRequestFactory requests;
	private PlaceController placeController;
	private McAppShell shell;
	public static McAppNav MC_APP_NAV ;
	
	@Inject
	public McAppNav(McAppRequestFactory requests, PlaceController placeController, McAppShell shell) {
        this.requests = requests;
        this.placeController = placeController;
        this.shell = shell;
        MC_APP_NAV = this;
        
        initWidget(uiBinder.createAndBindUi(this));
        
		shell.setNavigation(this);
		//Checking user cookie and based on showing Discloser panel open or closed.
		addOpenCloseHandlerToAllDiscloserPanel();
		checkAndShowDisclosurePanelBasenOnCookie();
        checkAdminRights(requests,true);
    }

	private void addOpenCloseHandlerToAllDiscloserPanel() {
		overviewPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				Cookies.setCookie(McAppConstant.SYSTEMOVERVIEWPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
			}
		});
		overviewPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				Cookies.setCookie(McAppConstant.SYSTEMOVERVIEWPANEL,String.valueOf(false),ClientUtility.getDateFromOneYear());
			}
		});
		
		assessmentQuestionsPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
			
				Cookies.setCookie(McAppConstant.MANAGMENTPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
			}
		});
		assessmentQuestionsPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				Cookies.setCookie(McAppConstant.MANAGMENTPANEL,String.valueOf(false),ClientUtility.getDateFromOneYear());
			}
		});
		assesmentsPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				Cookies.setCookie(McAppConstant.ASSESMENTPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
			}
		});
		assesmentsPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				Cookies.setCookie(McAppConstant.ASSESMENTPANEL,String.valueOf(false),ClientUtility.getDateFromOneYear());
			}
		});
		administrationPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				Cookies.setCookie(McAppConstant.QUESTIONPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
			}
		});
		administrationPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				Cookies.setCookie(McAppConstant.QUESTIONPANEL,String.valueOf(false),ClientUtility.getDateFromOneYear());
			}
		});
		reviewProcessPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				Cookies.setCookie(McAppConstant.EXTENDEDQUESTIONPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
			}
		});
		reviewProcessPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				Cookies.setCookie(McAppConstant.EXTENDEDQUESTIONPANEL,String.valueOf(false),ClientUtility.getDateFromOneYear());
			}
		});
	}

	private void checkAndShowDisclosurePanelBasenOnCookie() {
		
		String systemOverViewPanlCookie = Cookies.getCookie(McAppConstant.SYSTEMOVERVIEWPANEL);
		String managementPanelCookie = Cookies.getCookie(McAppConstant.MANAGMENTPANEL);
		String assementPanelCookie = Cookies.getCookie(McAppConstant.ASSESMENTPANEL);
		String questionPanelCookie = Cookies.getCookie(McAppConstant.QUESTIONPANEL);
		String extendedQuestionPanelCookie = Cookies.getCookie(McAppConstant.EXTENDEDQUESTIONPANEL);
		
		if(systemOverViewPanlCookie ==null){
			Cookies.setCookie(McAppConstant.SYSTEMOVERVIEWPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
		}else{
			
			if(systemOverViewPanlCookie.equals(String.valueOf(true))){
				overviewPanel.setOpen(true);
			}else{
				overviewPanel.setOpen(false);
			}
		}
		
		if(managementPanelCookie ==null){
			Cookies.setCookie(McAppConstant.MANAGMENTPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
		}else{
			
			if(managementPanelCookie.equals(String.valueOf(true))){
				assessmentQuestionsPanel.setOpen(true);
			}else{
				assessmentQuestionsPanel.setOpen(false);
			}
		}
		
		if(assementPanelCookie ==null){
			Cookies.setCookie(McAppConstant.ASSESMENTPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
		}else{
			
			if(assementPanelCookie.equals(String.valueOf(true))){
				assesmentsPanel.setOpen(true);
			}else{
				assesmentsPanel.setOpen(false);
			}
		}
		
		if(questionPanelCookie ==null){
			Cookies.setCookie(McAppConstant.QUESTIONPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
		}else{
			
			if(questionPanelCookie.equals(String.valueOf(true))){
				administrationPanel.setOpen(true);
			}else{
				administrationPanel.setOpen(false);
			}
		}
		
		if(extendedQuestionPanelCookie ==null){
			Cookies.setCookie(McAppConstant.EXTENDEDQUESTIONPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
		}else{
			
			if(extendedQuestionPanelCookie.equals(String.valueOf(true))){
				reviewProcessPanel.setOpen(true);
			}else{
				reviewProcessPanel.setOpen(false);
			}
		}
		
	}
	public static void checkAdminRights(McAppRequestFactory requests,boolean isValiduser)
	{
		if(MC_APP_NAV != null) 
		{
			MC_APP_NAV.hideAllMenu();
			
			if(isValiduser)
			requests.personRequest().checkAdminRightToLoggedPerson().fire(new BMEReceiver<Boolean>() {
		        
				@Override
				public void onSuccess(Boolean response) {
					if (response == null)
						MC_APP_NAV.both = 0;
					else
						MC_APP_NAV.both = 2;
					
					MC_APP_NAV.displayMenue(response);
					
				}
			});
		}
	}
	
	private int both = 0;
	
	private void displayMenue(Boolean flag){
		if (both < 1){
			both++;
			
			return;
		}
		both = 0;
		
		if (flag)
			showAdminMenu();
		else
			showUserMenu();
		
        Place place = placeController.getWhere();
        if(place == null || place.equals(Place.NOWHERE)) {
        	place =  new PlaceSystemOverview(PlaceSystemOverview.PLACE_SYSTEM_OVERVIEW);
        }
        setConstantText();
        
        placeController.goTo(Place.NOWHERE);
        placeController.goTo(place);
	}
		
	public void setConstantText()
	{
		overviewPanel.getHeaderTextAccessor().setText(constants.systemOweviewPanel());
		systemOverview.setText(constants.systemOverview());
		
		acceptQuestion.setText(constants.acceptQuestion());
		acceptAnswer.setText(constants.acceptAnswer());
	
		
		assessmentQuestionsPanel.getHeaderTextAccessor().setText(constants.managementPanel());
		user.setText(constants.user());
		
		administrationPanel.getHeaderTextAccessor().setText(constants.questionPanel());
		question.setText(constants.question());
		questionType.setText(constants.questionTypes());
		institution.setText(constants.institution());
		
		reviewProcessPanel.getHeaderTextAccessor().setText(constants.extendedQueMgt());
		deactivatedQuestion.setText(constants.deactivatedQue());
		notActivatedQuestion.setText(constants.notActivatedQuestion());
		notActivatedAnswer.setText(constants.notActivatedAnswer());		
		
		assesmentsPanel.getHeaderTextAccessor().setText(constants.assementPanel());
		assesment.setText(constants.assesment());
		asignAssQuestion.setText(constants.asignAssQuestion());
		questionInAssessment.setText(constants.questionInAssessment());
		bookAssesment.setText(constants.bookAssesment());
		
	}
	
	private void hideAllMenu()
	{
		overviewPanel.setVisible(false);
		assessmentQuestionsPanel.setVisible(false);
		administrationPanel.setVisible(false);
		assesmentsPanel.setVisible(false);
		reviewProcessPanel.setVisible(false);
		
		shell.getMasterPanel().clear();
	}
	
	private void showAdminMenu()
	{
		overviewPanel.setVisible(true);
		assessmentQuestionsPanel.setVisible(true);
		reviewProcessPanel.setVisible(true);
		assesmentsPanel.setVisible(true);
		administrationPanel.setVisible(true);	
		
		questionInAssessment.setVisible(true);		
		notActivatedQuestion.setVisible(true);
		notActivatedAnswer.setVisible(true);
		deactivatedQuestion.setVisible(true);
		assesment.setVisible(true);
		
		questionType.setVisible(true);
		institution.setVisible(true);
		bookAssesment.setVisible(true);
		
		questionType.setVisible(true);
		institution.setVisible(true);
		user.setVisible(true);
	}
	
	private void showUserMenu()
	{
		overviewPanel.setVisible(true);
		assessmentQuestionsPanel.setVisible(true);
		reviewProcessPanel.setVisible(true);
		assesmentsPanel.setVisible(true);
		administrationPanel.setVisible(false);		
		
		questionInAssessment.setVisible(false);		
		notActivatedQuestion.setVisible(false);
		notActivatedAnswer.setVisible(false);
		deactivatedQuestion.setVisible(false);
		assesment.setVisible(false);
		
		questionType.setVisible(false);
		institution.setVisible(false);
		bookAssesment.setVisible(false);
		
		questionType.setVisible(false);
		institution.setVisible(false);
		user.setVisible(false);
	}

}
