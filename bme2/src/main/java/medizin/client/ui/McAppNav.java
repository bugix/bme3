package medizin.client.ui;


import medizin.client.McAppShell;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAcceptAnswer;
import medizin.client.place.PlaceAcceptAssQuestion;
import medizin.client.place.PlaceAcceptPerson;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceAcceptQuestionDetails;
import medizin.client.place.PlaceAsignAssQuestion;
import medizin.client.place.PlaceAssesment;
import medizin.client.place.PlaceAssesmentDetails;
import medizin.client.place.PlaceBookAssesment;
import medizin.client.place.PlaceBookAssesmentDetails;
import medizin.client.place.PlaceInstitution;
import medizin.client.place.PlaceInstitutionEvent;
import medizin.client.place.PlaceNotActivatedQuestion;
import medizin.client.place.PlaceNotActivatedQuestionDetails;
import medizin.client.place.PlaceOpenDemand;
import medizin.client.place.PlaceQuestion;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.place.PlaceQuestiontypes;
import medizin.client.place.PlaceQuestiontypesDetails;
import medizin.client.place.PlaceStaticContent;
import medizin.client.place.PlaceSystemOverview;
import medizin.client.place.PlaceUser;
import medizin.client.place.PlaceUserDetails;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
/**
 * The applications main navigation element, shown on the left hand side of the user interface.
 * @author masterthesis
 *
 */
public class McAppNav extends Composite {

	private static McAppNavUiBinder uiBinder = GWT.create(McAppNavUiBinder.class);

	interface McAppNavUiBinder extends UiBinder<Widget, McAppNav> {
	}
	
	
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

	@UiHandler("systemOverview")
		void systemOverviewClicked(ClickEvent event) {
			placeController.goTo(new PlaceSystemOverview(PlaceSystemOverview.PLACE_SYSTEM_OVERVIEW));
		}
	@UiHandler("acceptPerson")
	void acceptPersonClicked(ClickEvent event) {
		placeController.goTo(new PlaceAcceptPerson(PlaceAcceptPerson.PLACE_ACCEPT_PERSON));
	}
	
	@UiHandler("acceptQuestion")
	void acceptQuestionClicked(ClickEvent event) {
		placeController.goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
	}
	@UiHandler("acceptAnswer")
	void PlaceAcceptAnswerClicked(ClickEvent event) {
		placeController.goTo(new PlaceAcceptAnswer(PlaceAcceptAnswer.PLACE_ACCEPT_ANSWER));
	}
	@UiHandler("acceptAssQuestion")
	void acceptAssQuestionClicked(ClickEvent event) {
		placeController.goTo(new PlaceAcceptAssQuestion(PlaceAcceptAssQuestion.PLACE_ACCEPT_ASS_QUESTION));
	}
	@UiHandler("openDemand")
	void openDemandClicked(ClickEvent event) {
		placeController.goTo(new PlaceOpenDemand(PlaceOpenDemand.PLACE_OPEN_DEMAND));
	}
	@UiHandler("user")
	void userClicked(ClickEvent event) {
		placeController.goTo(new PlaceUser(PlaceUser.PLACE_USER));
	}
	@UiHandler("question")
	void questionClicked(ClickEvent event) {
		placeController.goTo(new PlaceQuestion(PlaceQuestion.PLACE_QUESTION));
	}
	@UiHandler("notActivatedQuestion")
	void notActivatedQuestionClicked(ClickEvent event) {
		placeController.goTo(new PlaceNotActivatedQuestion(PlaceNotActivatedQuestion.PLACE_NOT_ACTIVATED_QUESTION));
	}
	@UiHandler("questionType")
	void questionTypeClicked(ClickEvent event) {
		placeController.goTo(new PlaceQuestiontypes(PlaceQuestiontypes.PLACE_QUESTIONTYPES));
	}
	@UiHandler("institution")
	void institutionClicked(ClickEvent event) {
		placeController.goTo(new PlaceInstitution(PlaceInstitution.PLACE_INSTITUTION));
	}
	@UiHandler("assesment")
	void assesmentClicked(ClickEvent event) {
		placeController.goTo(new PlaceAssesment(PlaceAssesment.PLACE_ASSESMENT));
	}
	@UiHandler("asignAssQuestion")
	void asignAssQuestionClicked(ClickEvent event) {
		placeController.goTo(new PlaceAsignAssQuestion(PlaceAsignAssQuestion.PLACE_ASIGN_ASS_QUESTION));
	}
	@UiHandler("bookAssesment")
	void bookAssesmentClicked(ClickEvent event) {
		placeController.goTo(new PlaceBookAssesment(PlaceBookAssesment.PLACE_BOOK_ASSESMENT));
	}
	@UiHandler("staticContent")
	void staticContentClicked(ClickEvent event) {
		placeController.goTo(new PlaceStaticContent(PlaceStaticContent.PLACE_STATIC_CONTENT));
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
        systemOweviewPanel.setOpen(false);
        managementPanel.setOpen(false);
        assementPanel.setOpen(false);
        questionPanel.setOpen(false);

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
        
        	 requests.institutionRequest().myGetInstitutionToWorkWith().fire(new Receiver<InstitutionProxy>(){

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

	public static void checkAdminRights(McAppRequestFactory requests,boolean isValiduser)
	{
		if(MC_APP_NAV != null) 
		{
			MC_APP_NAV.hideAllMenu();
			
			if(isValiduser)
			requests.personRequest().checkAdminRightToLoggedPerson().fire(new Receiver<Boolean>() {
		        
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
		

        
        requests.getEventBus().addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {

				Place place = event.getNewPlace();
				changeMenue(place);
			}
		});
        
        Place place = placeController.getWhere();
        if(place == null || place.equals(Place.NOWHERE)) {
        	place =  new PlaceSystemOverview(PlaceSystemOverview.PLACE_SYSTEM_OVERVIEW);
        }
        changeMenue(place);
        placeController.goTo(new PlaceSystemOverview(PlaceSystemOverview.PLACE_SYSTEM_OVERVIEW));
        placeController.goTo(place);
	}
	
	protected void changeMenue(Place place){
		
		setConstantText();
		
		/*systemOverview.removeStyleName("gwt-AnchorSelected");

		
		acceptPerson.removeStyleName("gwt-AnchorSelected");
		
		acceptQuestion.removeStyleName("gwt-AnchorSelected");
		
		acceptAnswer.removeStyleName("gwt-AnchorSelected");
		
		acceptAssQuestion.removeStyleName("gwt-AnchorSelected");
		
		openDemand.removeStyleName("gwt-AnchorSelected");
		
		user.removeStyleName("gwt-AnchorSelected");
		
		question.removeStyleName("gwt-AnchorSelected");
		
		questionType.removeStyleName("gwt-AnchorSelected");
		
		institution.removeStyleName("gwt-AnchorSelected");
		
		assesment.removeStyleName("gwt-AnchorSelected");
		
		asignAssQuestion.removeStyleName("gwt-AnchorSelected");
		
		bookAssesment.removeStyleName("gwt-AnchorSelected");
		
		staticContent.removeStyleName("gwt-AnchorSelected");
		
*/        if (place instanceof PlaceSystemOverview){
            systemOweviewPanel.setOpen(true);
            managementPanel.setOpen(false);
            assementPanel.setOpen(false);
            questionPanel.setOpen(false);
        //    systemOverview.addStyleName("gwt-AnchorSelected");
        }
        if (place instanceof PlaceAcceptPerson){
            systemOweviewPanel.setOpen(true);
            managementPanel.setOpen(false);
            assementPanel.setOpen(false);
            questionPanel.setOpen(false);
		//	acceptPerson.addStyleName("gwt-AnchorSelected");
        }
        if (place instanceof PlaceAcceptQuestion || place instanceof PlaceAcceptQuestionDetails){
            systemOweviewPanel.setOpen(true);
            managementPanel.setOpen(false);
            assementPanel.setOpen(false);
            questionPanel.setOpen(false);			
		//	acceptQuestion.addStyleName("gwt-AnchorSelected");
        }
        if (place instanceof PlaceAcceptAnswer){
            systemOweviewPanel.setOpen(true);
            managementPanel.setOpen(false);
            assementPanel.setOpen(false);
            questionPanel.setOpen(false);		
	//		acceptAnswer.addStyleName("gwt-AnchorSelected");
        }
        if (place instanceof PlaceAcceptAssQuestion){
            systemOweviewPanel.setOpen(true);
            managementPanel.setOpen(false);
            assementPanel.setOpen(false);
            questionPanel.setOpen(false);		
		//	acceptAssQuestion.addStyleName("gwt-AnchorSelected");
        }
        if (place instanceof PlaceOpenDemand){
            systemOweviewPanel.setOpen(true);
            managementPanel.setOpen(false);
            assementPanel.setOpen(false);
            questionPanel.setOpen(false);		
		//	openDemand.addStyleName("gwt-AnchorSelected");
        }
        if (place instanceof PlaceUser || place instanceof PlaceUserDetails){
            systemOweviewPanel.setOpen(false);
            managementPanel.setOpen(true);
            assementPanel.setOpen(false);
            questionPanel.setOpen(false);		
	//		user.addStyleName("gwt-AnchorSelected");
        }
        if (place instanceof PlaceQuestion || place instanceof PlaceQuestionDetails){
            systemOweviewPanel.setOpen(false);
            managementPanel.setOpen(false);
            assementPanel.setOpen(false);
            questionPanel.setOpen(true);		
		//	question.addStyleName("gwt-AnchorSelected");
        }
        if (place instanceof PlaceNotActivatedQuestion || place instanceof PlaceNotActivatedQuestionDetails){
            systemOweviewPanel.setOpen(false);
            managementPanel.setOpen(false);
            assementPanel.setOpen(false);
            questionPanel.setOpen(true);		
        }
        if (place instanceof PlaceQuestiontypes || place instanceof PlaceQuestiontypesDetails){
            systemOweviewPanel.setOpen(false);
            managementPanel.setOpen(false);
            assementPanel.setOpen(false);
            questionPanel.setOpen(true);		
		//	questionType.addStyleName("gwt-AnchorSelected");
        }
        if (place instanceof PlaceInstitution || place instanceof PlaceInstitutionEvent){
            systemOweviewPanel.setOpen(false);
            managementPanel.setOpen(false);
            assementPanel.setOpen(false);
            questionPanel.setOpen(true);		
		//	institution.addStyleName("gwt-AnchorSelected");
        }
        if (place instanceof PlaceAssesment || place instanceof PlaceAssesmentDetails){
            systemOweviewPanel.setOpen(false);
            managementPanel.setOpen(false);
            assementPanel.setOpen(true);
            questionPanel.setOpen(false);		
		//	assesment.addStyleName("gwt-AnchorSelected");
        }
        if (place instanceof PlaceAsignAssQuestion){
            systemOweviewPanel.setOpen(false);
            managementPanel.setOpen(false);
            assementPanel.setOpen(true);
            questionPanel.setOpen(false);		
		//	asignAssQuestion.addStyleName("gwt-AnchorSelected");
        }
        if (place instanceof PlaceBookAssesment || place instanceof PlaceBookAssesmentDetails){
            systemOweviewPanel.setOpen(false);
            managementPanel.setOpen(false);
            assementPanel.setOpen(true);
            questionPanel.setOpen(false);	
		//	bookAssesment.addStyleName("gwt-AnchorSelected");
        }
        if (place instanceof PlaceStaticContent){
            systemOweviewPanel.setOpen(false);
            managementPanel.setOpen(false);
            assementPanel.setOpen(true);
            questionPanel.setOpen(false);	
         //   staticContent.addStyleName("gwt-AnchorSelected");
        }
	}
		
	public void setConstantText()
	{
		systemOweviewPanel.getHeaderTextAccessor().setText(constants.systemOweviewPanel());
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
		notActivatedQuestion.setText(constants.notActivatedQuestion());
		questionType.setText(constants.questionType());
		institution.setText(constants.institution());
		
		assementPanel.getHeaderTextAccessor().setText(constants.assementPanel());
		assesment.setText(constants.assesment());
		asignAssQuestion.setText(constants.asignAssQuestion());
		bookAssesment.setText(constants.bookAssesment());
		staticContent.setText(constants.staticContent());
	}
	
	private void hideAllMenu()
	{
		systemOweviewPanel.setVisible(false);
		managementPanel.setVisible(false);
		questionPanel.setVisible(false);
		assementPanel.setVisible(false);
		
		shell.getMasterPanel().clear();
	}
	
	private void showAdminMenu()
	{
		systemOweviewPanel.setVisible(true);
		managementPanel.setVisible(true);
		questionPanel.setVisible(true);
		assementPanel.setVisible(true);
		
		
		notActivatedQuestion.setVisible(true);
		questionType.setVisible(true);
		institution.setVisible(true);
		assesment.setVisible(true);
		bookAssesment.setVisible(true);
		staticContent.setVisible(true);
	}
	
	private void showUserMenu()
	{
		systemOweviewPanel.setVisible(true);
		managementPanel.setVisible(false);
		questionPanel.setVisible(true);
		assementPanel.setVisible(true);
		
		notActivatedQuestion.setVisible(false);
		questionType.setVisible(false);
		institution.setVisible(false);
		assesment.setVisible(false);
		bookAssesment.setVisible(false);
		staticContent.setVisible(false);
	}

}
