package medizin.client.ui;


import medizin.client.McAppShell;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAcceptAnswer;
import medizin.client.place.PlaceAcceptAssQuestion;
import medizin.client.place.PlaceAcceptPerson;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceAsignAssQuestion;
import medizin.client.place.PlaceAssesment;
import medizin.client.place.PlaceBookAssesment;
import medizin.client.place.PlaceDeactivatedQuestion;
import medizin.client.place.PlaceInstitution;
import medizin.client.place.PlaceNotActivatedAnswer;
import medizin.client.place.PlaceNotActivatedQuestion;
import medizin.client.place.PlaceOpenDemand;
import medizin.client.place.PlaceQuestion;
import medizin.client.place.PlaceQuestionInAssessment;
import medizin.client.place.PlaceQuestiontypes;
import medizin.client.place.PlaceStaticContent;
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
	
	private BmeConstants constant = GWT.create(BmeConstants.class);
	
	/*private static McAppNavAdminUiBinder uiBinderAdmin = GWT
			.create(McAppNavAdminUiBinder.class);

	@UiTemplate("McAppNavAdmin.ui.xml")
	interface McAppNavAdminUiBinder extends UiBinder<Widget, McAppNav> {
	}
	
	private static McAppNavUserUiBinder uiBinderUser = GWT
	.create(McAppNavUserUiBinder.class);
	
	@UiTemplate("McAppNavUser.ui.xml")
	interface McAppNavUserUiBinder extends UiBinder<Widget, McAppNav> {
	}*/
	
	public BmeConstants constants = GWT.create(BmeConstants.class);
	
	@UiField
	DisclosurePanel systemOweviewPanel;
	@UiField
	DisclosurePanel managementPanel;
	@UiField
	DisclosurePanel assementPanel;
	@UiField
	DisclosurePanel questionPanel;
	@UiField
	DisclosurePanel extendedQuestionPanel;	
	
	@UiField
	Anchor systemOverview;
	@UiField
	Anchor acceptPerson;
	@UiField
	Anchor acceptQuestion;
	@UiField
	Anchor acceptAnswer;
	@UiField
	Anchor acceptAssQuestion;
	@UiField
	Anchor openDemand;
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
	Anchor staticContent;
	@UiField
	Anchor deactivatedQuestion;	
	@UiField
	Anchor notActivatedAnswer;
	@UiField
	Anchor questionInAssessment;

	@UiHandler("systemOverview")
		void systemOverviewClicked(ClickEvent event) {
			placeController.goTo(new PlaceSystemOverview(PlaceSystemOverview.PLACE_SYSTEM_OVERVIEW, true,shell.getMasterPanel().getOffsetHeight()));
		}
	@UiHandler("acceptPerson")
	void acceptPersonClicked(ClickEvent event) {
		placeController.goTo(new PlaceAcceptPerson(PlaceAcceptPerson.PLACE_ACCEPT_PERSON, true,shell.getMasterPanel().getOffsetHeight()));
	}
	
	@UiHandler("acceptQuestion")
	void acceptQuestionClicked(ClickEvent event) {
		placeController.goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION, true,shell.getMasterPanel().getOffsetHeight()));
	}
	@UiHandler("acceptAnswer")
	void PlaceAcceptAnswerClicked(ClickEvent event) {
		placeController.goTo(new PlaceAcceptAnswer(PlaceAcceptAnswer.PLACE_ACCEPT_ANSWER, true,shell.getMasterPanel().getOffsetHeight()));
	}
	@UiHandler("acceptAssQuestion")
	void acceptAssQuestionClicked(ClickEvent event) {
		placeController.goTo(new PlaceAcceptAssQuestion(PlaceAcceptAssQuestion.PLACE_ACCEPT_ASS_QUESTION, true,shell.getMasterPanel().getOffsetHeight()));
	}
	@UiHandler("openDemand")
	void openDemandClicked(ClickEvent event) {
		placeController.goTo(new PlaceOpenDemand(PlaceOpenDemand.PLACE_OPEN_DEMAND, true,shell.getMasterPanel().getOffsetHeight()));
	}
	@UiHandler("user")
	void userClicked(ClickEvent event) {
		placeController.goTo(new PlaceUser(PlaceUser.PLACE_USER, true,shell.getMasterPanel().getOffsetHeight()));
	}
	@UiHandler("question")
	void questionClicked(ClickEvent event) {
		placeController.goTo(new PlaceQuestion(PlaceQuestion.PLACE_QUESTION, true,shell.getMasterPanel().getOffsetHeight()));
	}
	@UiHandler("notActivatedQuestion")
	void notActivatedQuestionClicked(ClickEvent event) {
		placeController.goTo(new PlaceNotActivatedQuestion(PlaceNotActivatedQuestion.PLACE_NOT_ACTIVATED_QUESTION, true, shell.getMasterPanel().getOffsetHeight()));
	}
	@UiHandler("questionType")
	void questionTypeClicked(ClickEvent event) {
		placeController.goTo(new PlaceQuestiontypes(PlaceQuestiontypes.PLACE_QUESTIONTYPES, true,shell.getMasterPanel().getOffsetHeight()));
	}
	@UiHandler("institution")
	void institutionClicked(ClickEvent event) {
		placeController.goTo(new PlaceInstitution(PlaceInstitution.PLACE_INSTITUTION, true,shell.getMasterPanel().getOffsetHeight()));
	}
	@UiHandler("assesment")
	void assesmentClicked(ClickEvent event) {
		placeController.goTo(new PlaceAssesment(PlaceAssesment.PLACE_ASSESMENT, true,shell.getMasterPanel().getOffsetHeight()));
	}
	@UiHandler("asignAssQuestion")
	void asignAssQuestionClicked(ClickEvent event) {
		placeController.goTo(new PlaceAsignAssQuestion(PlaceAsignAssQuestion.PLACE_ASIGN_ASS_QUESTION, true,shell.getMasterPanel().getOffsetHeight()));
	}
	@UiHandler("bookAssesment")
	void bookAssesmentClicked(ClickEvent event) {
		placeController.goTo(new PlaceBookAssesment(PlaceBookAssesment.PLACE_BOOK_ASSESMENT, true,shell.getMasterPanel().getOffsetHeight()));
	}
	@UiHandler("staticContent")
	void staticContentClicked(ClickEvent event) {
		placeController.goTo(new PlaceStaticContent(PlaceStaticContent.PLACE_STATIC_CONTENT, true,shell.getMasterPanel().getOffsetHeight()));
	}
	@UiHandler("notActivatedAnswer")
	void notActivatedAnswerClicked(ClickEvent event){
		placeController.goTo(new PlaceNotActivatedAnswer(PlaceNotActivatedAnswer.PLACE_NOT_ACTIVATED_ANSWER, true,shell.getMasterPanel().getOffsetHeight()));
	}
	@UiHandler("deactivatedQuestion")
	void deactivatedQuestionClicked(ClickEvent event){
		placeController.goTo(new PlaceDeactivatedQuestion(PlaceDeactivatedQuestion.PLACE_DEACTIVATED_QUESTION, true,shell.getMasterPanel().getOffsetHeight()));
	}
	
	@UiHandler("questionInAssessment")
	void questionInAssessmentClicked(ClickEvent event){
		placeController.goTo(new PlaceQuestionInAssessment(PlaceQuestionInAssessment.PLACE_QUESTION_IN_ASSESSMENT, true,shell.getMasterPanel().getOffsetHeight()));
	}
//	public McAppNav() {
//		initWidget(uiBinderUser.createAndBindUi(this));
//		
//	}
	
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
		
        /*systemOweviewPanel.setOpen(true);
        managementPanel.setOpen(true);
        assementPanel.setOpen(true);
        questionPanel.setOpen(true);*/

    	/*requests.personRequest().myGetLoggedPerson().fire(new Receiver<PersonProxy>(){

			@Override
			public void onSuccess(PersonProxy response) {
				if (response == null){
					Window.alert("the User will be redirected to Shibboleth Loginpage. You should select a user in the User-Box to login and reload the Page. Userinfo is strored in a session.");
					return;
				}
				loggedUser = response;
				 displayMenue();
			}});
        
        	 requests.institutionRequest().myGetInstitutionToWorkWith().fire(new BMEReceiver<InstitutionProxy>(){

     			@Override
     			public void onSuccess(InstitutionProxy response) {
     				if (response == null){
     					Window.alert("a Institution selectbox will be shown. You should select the institution from the Institution-Pulldown and reload the Page. The institution is storen in Session");
     					return;
     				}
     				//loggedUser = response;
     				 displayMenue();
		}});*/
        
        checkAdminRights(requests,true);
        
    }

	private void addOpenCloseHandlerToAllDiscloserPanel() {
		systemOweviewPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				Cookies.setCookie(McAppConstant.SYSTEMOVERVIEWPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
			}
		});
		systemOweviewPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				Cookies.setCookie(McAppConstant.SYSTEMOVERVIEWPANEL,String.valueOf(false),ClientUtility.getDateFromOneYear());
			}
		});
		
		managementPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
			
				Cookies.setCookie(McAppConstant.MANAGMENTPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
			}
		});
		managementPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				Cookies.setCookie(McAppConstant.MANAGMENTPANEL,String.valueOf(false),ClientUtility.getDateFromOneYear());
			}
		});
		assementPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				Cookies.setCookie(McAppConstant.ASSESMENTPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
			}
		});
		assementPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				Cookies.setCookie(McAppConstant.ASSESMENTPANEL,String.valueOf(false),ClientUtility.getDateFromOneYear());
			}
		});
		questionPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				Cookies.setCookie(McAppConstant.QUESTIONPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
			}
		});
		questionPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				Cookies.setCookie(McAppConstant.QUESTIONPANEL,String.valueOf(false),ClientUtility.getDateFromOneYear());
			}
		});
		extendedQuestionPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				Cookies.setCookie(McAppConstant.EXTENDEDQUESTIONPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
			}
		});
		extendedQuestionPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
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
				systemOweviewPanel.setOpen(true);
			}else{
				systemOweviewPanel.setOpen(false);
			}
		}
		
		if(managementPanelCookie ==null){
			Cookies.setCookie(McAppConstant.MANAGMENTPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
		}else{
			
			if(managementPanelCookie.equals(String.valueOf(true))){
				managementPanel.setOpen(true);
			}else{
				managementPanel.setOpen(false);
			}
		}
		
		if(assementPanelCookie ==null){
			Cookies.setCookie(McAppConstant.ASSESMENTPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
		}else{
			
			if(assementPanelCookie.equals(String.valueOf(true))){
				assementPanel.setOpen(true);
			}else{
				assementPanel.setOpen(false);
			}
		}
		
		if(questionPanelCookie ==null){
			Cookies.setCookie(McAppConstant.QUESTIONPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
		}else{
			
			if(questionPanelCookie.equals(String.valueOf(true))){
				questionPanel.setOpen(true);
			}else{
				questionPanel.setOpen(false);
			}
		}
		
		if(extendedQuestionPanelCookie ==null){
			Cookies.setCookie(McAppConstant.EXTENDEDQUESTIONPANEL,String.valueOf(true),ClientUtility.getDateFromOneYear());
		}else{
			
			if(extendedQuestionPanelCookie.equals(String.valueOf(true))){
				extendedQuestionPanel.setOpen(true);
			}else{
				extendedQuestionPanel.setOpen(false);
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
	
	//private PersonProxy loggedUser;
	
	private int both = 0;
	
	/*@UiField
	DivElement deletethis;*/
	/*private boolean isValidUser(){
		if(shell.getTopPanel2().getLoggedUser().getValue()!=null)
			return true;
		else
			return false;
	}*/
	private void displayMenue(Boolean flag){
		if (both < 1){
			both++;
			
			return;
		}
		both = 0;
		
		//if (this.loggedUser.getIsAdmin()){
		//System.out.println("Flag " + flag);
		if (flag)
			showAdminMenu();
		else
			showUserMenu();
		
		/*if (flag){
	        //initWidget(uiBinderAdmin.createAndBindUi(this));
	       // DOM.setElementAttribute(user.getParent().getParent().getElement(), "style", "margin-right: -2px; display: none;");
		}
		else if(flag !=null) {
			//initWidget(uiBinderUser.createAndBindUi(this));
//			deletethis.setInnerHTML("");
			//Log.error(Document.get().getElementById("deletethis").getInnerHTML());
		}*/
		//DOM.setElementAttribute(systemOverview.getParent().getParent().getElement(), "style", "margin-right: -2px; display: none;");
		//DOM.setElementAttribute(asignAssQuestion.getParent().getParent().getElement(), "style", "margin-right: -2px; display: none;");
		//DOM.setElementAttribute(question.getParent().getParent().getElement(), "style", "margin-right: -2px; display: none;");
		

        
        /*requests.getEventBus().addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {

				Place place = event.getNewPlace();
				changeMenue(place);
			}
		});*/
        
        Place place = placeController.getWhere();
        if(place == null || place.equals(Place.NOWHERE)) {
        	place =  new PlaceSystemOverview(PlaceSystemOverview.PLACE_SYSTEM_OVERVIEW);
        }
        //changeMenue(place);
        setConstantText();
        
        placeController.goTo(Place.NOWHERE);
        placeController.goTo(place);
	}
		
	public void setConstantText()
	{
		systemOweviewPanel.getHeaderTextAccessor().setText(constants.systemOverview());
		systemOverview.setText(constants.systemOverview());
		acceptPerson.setText(constants.acceptPerson());
		acceptQuestion.setText(constants.acceptQuestion());
		acceptAnswer.setText(constants.acceptAnswer());
		acceptAssQuestion.setText(constants.acceptAssQuestion());
		openDemand.setText(constants.openDemand());
		
		managementPanel.getHeaderTextAccessor().setText(constants.managementPanel());
		user.setText(constants.user());
		
		//doctor.setText(constants.;)
		questionPanel.getHeaderTextAccessor().setText(constants.questionPanel());
		question.setText(constants.question());
		questionType.setText(constants.questionTypes());
		institution.setText(constants.institution());
		
		extendedQuestionPanel.getHeaderTextAccessor().setText(constants.extendedQueMgt());
		deactivatedQuestion.setText(constants.deactivatedQue());
		notActivatedQuestion.setText(constants.notActivatedQuestion());
		notActivatedAnswer.setText(constants.notActivatedAnswer());		
		
		assementPanel.getHeaderTextAccessor().setText(constants.assementPanel());
		assesment.setText(constants.assesment());
		asignAssQuestion.setText(constants.asignAssQuestion());
		questionInAssessment.setText(constant.questionInAssessment());
		bookAssesment.setText(constants.bookAssesment());
		staticContent.setText(constants.staticContent());
	}
	
	private void hideAllMenu()
	{
		systemOweviewPanel.setVisible(false);
		managementPanel.setVisible(false);
		questionPanel.setVisible(false);
		assementPanel.setVisible(false);
		extendedQuestionPanel.setVisible(false);
		
		shell.getMasterPanel().clear();
	}
	
	private void showAdminMenu()
	{
		systemOweviewPanel.setVisible(true);
		managementPanel.setVisible(true);
		questionPanel.setVisible(true);
		assementPanel.setVisible(true);
		extendedQuestionPanel.setVisible(true);
		
		notActivatedQuestion.setVisible(true);
		notActivatedAnswer.setVisible(true);
		questionType.setVisible(true);
		institution.setVisible(true);
		assesment.setVisible(true);
		questionInAssessment.setVisible(true);
		bookAssesment.setVisible(true);
		staticContent.setVisible(true);
		deactivatedQuestion.setVisible(true);
	}
	
	private void showUserMenu()
	{
		systemOweviewPanel.setVisible(true);
		managementPanel.setVisible(false);
		questionPanel.setVisible(true);
		assementPanel.setVisible(true);
		extendedQuestionPanel.setVisible(false);
		
		notActivatedQuestion.setVisible(false);
		notActivatedAnswer.setVisible(false);
		questionType.setVisible(false);
		institution.setVisible(false);
		assesment.setVisible(false);
		questionInAssessment.setVisible(false);
		bookAssesment.setVisible(false);
		staticContent.setVisible(false);
		deactivatedQuestion.setVisible(false);
	}

}
