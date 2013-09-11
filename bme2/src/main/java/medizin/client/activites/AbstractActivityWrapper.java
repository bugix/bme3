package medizin.client.activites;

import java.util.List;
import java.util.Map;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAssesment;
import medizin.client.place.PlaceAssesmentDetails;
import medizin.client.place.PlaceBookAssesment;
import medizin.client.place.PlaceBookAssesmentDetails;
import medizin.client.place.PlaceInstitution;
import medizin.client.place.PlaceInstitutionEvent;
import medizin.client.place.PlaceNotActivatedAnswer;
import medizin.client.place.PlaceNotActivatedQuestion;
import medizin.client.place.PlaceNotActivatedQuestionDetails;
import medizin.client.place.PlaceQuestiontypes;
import medizin.client.place.PlaceQuestiontypesDetails;
import medizin.client.place.PlaceStaticContent;
import medizin.client.place.PlaceSystemOverview;
import medizin.client.place.PlaceUser;
import medizin.client.place.PlaceUserDetails;
import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.PersonAccessRightProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.UserAccessRightsProxy;
import medizin.client.request.InstitutionRequest;
import medizin.client.request.PersonRequest;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxOkButtonEvent;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxOkButtonEventHandler;
import medizin.client.util.AnswerAccessRightsVO;
import medizin.client.util.QuestionAccessRightsVO;
import medizin.shared.AccessRights;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.i18n.BmeMessages;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.Receiver;
/**
 * This wrapper is used to provide access control in all activities.
 * @author masterthesis
 *
 */

abstract public class AbstractActivityWrapper extends AbstractActivity {

	private McAppRequestFactory requests;
	private final PlaceController placeController;
	private Place place;
	protected Map<String, Widget> reciverMap = Maps.newHashMap();
	
	public BmeConstants constants = GWT.create(BmeConstants.class);
	public BmeMessages bmeMessages = GWT.create(BmeMessages.class);
	
	protected PersonProxy userLoggedIn;
	protected InstitutionProxy institutionActive;
	private int count = 0;
	
	protected PersonAccessRightProxy personRightProxy;
	
	public AbstractActivityWrapper(Place place, McAppRequestFactory requests, PlaceController placeController) {
		this.place = place;
        this.requests = requests;
        this.placeController = placeController;
	}
	
	@Override
	public final void start(final AcceptsOneWidget panel, final EventBus eventBus) {

			Log.info("start method called");
			
			PersonRequest personRequest = requests.personRequest();
			personRequest.myGetLoggedPerson().to(new BMEReceiver<PersonProxy>() {

				@Override
				public void onSuccess(PersonProxy response) {
					userLoggedIn = response;
					newStart(panel, eventBus);
					
				}
			});
			
			InstitutionRequest institutionRequest = personRequest.append(requests.institutionRequest());
			institutionRequest.myGetInstitutionToWorkWith().to(new BMEReceiver<InstitutionProxy>() {

				@Override
				public void onSuccess(InstitutionProxy response) {
					institutionActive = response;
					newStart(panel, eventBus);
				}
			});
			
			PersonRequest personRequest2 = institutionRequest.append(requests.personRequest());
			personRequest2.fetchLoggedPersonAccessRights().with("question", "questionEvent", "institution").to(new Receiver<PersonAccessRightProxy>() {

				@Override
				public void onSuccess(PersonAccessRightProxy response) {
					personRightProxy = response;		
					
					if(checkIfUserHasRightsToMenu() == true) {
						start2(panel, eventBus);	
					}else {
						ConfirmationDialogBox.showOkDialogBox(constants.information(), constants.mayNotHaveRights(),new ConfirmDialogBoxOkButtonEventHandler(){

							@Override
							public void onOkButtonClicked(ConfirmDialogBoxOkButtonEvent event) {
								placeController.goTo(new PlaceSystemOverview(PlaceSystemOverview.PLACE_SYSTEM_OVERVIEW));
							}
						});
					}
				}
			});
			
			eventBus.addHandler(PlaceChangeEvent.TYPE,new PlaceChangeEvent.Handler() {
				public void onPlaceChange(PlaceChangeEvent event) {
					Place place = event.getNewPlace();
					placeChanged(place);
				}
			});
			personRequest2.fire();
			
	}
	
	/**
	 * Checks if user is logged, if not login dialog is shown.
	 * @param panel
	 * @param eventBus
	 */
	private void newStart(final AcceptsOneWidget panel, final EventBus eventBus){
		count ++;
		
		if(count<2){
			return;
		}

		if (userLoggedIn==null) {
			//Window.alert("Please log in");
			ConfirmationDialogBox.showOkDialogBox(constants.information(),constants.loginInformation());
			return;
		}
		else {
			Document.get().getElementById("userLoggedIn").setInnerHTML("Eingeloggt als: " + userLoggedIn.getName() + " " + userLoggedIn.getPrename());
		}
		if (institutionActive==null) {
			//Window.alert("Please select a institution");
			ConfirmationDialogBox.showOkDialogBox(constants.information(),constants.selectInstitution());
			return;
		}
		else {
			Document.get().getElementById("institutionActive").setInnerHTML("Institution: " + institutionActive.getInstitutionName());
		}
		
		
	}
	
	private final boolean checkIfUserHasRightsToMenu() {
		boolean flag = false;
		
		if(userLoggedIn != null && personRightProxy != null) {
			
			if(isAdminOrInstitutionalAdmin() == true) {
				flag = true;
			}else {
				
				if(place instanceof PlaceNotActivatedQuestion  	|| place instanceof PlaceNotActivatedQuestionDetails
						|| place instanceof PlaceQuestiontypes 	|| place instanceof PlaceQuestiontypesDetails
						|| place instanceof PlaceInstitution 	|| place instanceof PlaceInstitutionEvent
						|| place instanceof PlaceAssesment 		|| place instanceof PlaceAssesmentDetails
						|| place instanceof PlaceBookAssesment	|| place instanceof PlaceBookAssesmentDetails
						|| place instanceof PlaceStaticContent  || place instanceof PlaceNotActivatedAnswer
						|| place instanceof PlaceUser 			|| place instanceof PlaceUserDetails
				) {
					flag = false;
				}else {
					flag = true;
				}
			}
		}
		
		return flag;
	}
	
	public final boolean isAdminOrInstitutionalAdmin() {
		if(userLoggedIn == null || personRightProxy == null) {
			return false;
		}
		
		return  userLoggedIn.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin();
	}
	
	public final QuestionAccessRightsVO hasQuestionRights(QuestionProxy proxy)
	{
		final QuestionAccessRightsVO questionAccessRightsVO = new QuestionAccessRightsVO();
		
		if (userLoggedIn == null || institutionActive == null)
			return questionAccessRightsVO;
		
		if(proxy != null  && proxy.getIsReadOnly() == true) {
			return questionAccessRightsVO;
		}
		
		if (isAdminOrInstitutionalAdmin() == true)
		{
			questionAccessRightsVO.setAddRight(true);
			questionAccessRightsVO.setReadRight(true);
			questionAccessRightsVO.setWriteRight(true);
			
			return questionAccessRightsVO;
		}
		
		if(proxy == null || proxy.getAutor() == null) {
			return questionAccessRightsVO;
		}
						
		if (proxy.getAutor().getId().equals(userLoggedIn.getId()))
		{
			questionAccessRightsVO.setReadRight(true);
			questionAccessRightsVO.setWriteRight(true);
			
			return questionAccessRightsVO;
		}
		
		List<UserAccessRightsProxy> userAccessRightsProxyList = Lists.newArrayList();
		userAccessRightsProxyList.addAll(personRightProxy.getQuestionEventAccList());
		userAccessRightsProxyList.addAll(personRightProxy.getQuestionAccList());
		
		for (UserAccessRightsProxy userRightsProxy : userAccessRightsProxyList)
		{
			AccessRights accRights = userRightsProxy.getAccRights();
			
			if (userRightsProxy.getQuestionEvent() != null && proxy.getQuestEvent() != null && userRightsProxy.getQuestionEvent().getId().equals(proxy.getQuestEvent().getId()))
			{
				if (accRights.equals(AccessRights.AccRead))
					questionAccessRightsVO.setReadRight(true);
				else if (accRights.equals(AccessRights.AccWrite))
					questionAccessRightsVO.setWriteRight(true);
				else if (accRights.equals(AccessRights.AccAddQuestions))
					questionAccessRightsVO.setAddRight(true);
			}
			else if (userRightsProxy.getQuestion() != null && userRightsProxy.getQuestion().getId().equals(proxy.getId()))
			{
				if (accRights.equals(AccessRights.AccRead))
					questionAccessRightsVO.setReadRight(true);
				else if (accRights.equals(AccessRights.AccWrite))
					questionAccessRightsVO.setWriteRight(true);				
			}
		}
		
		return questionAccessRightsVO;
	}
	
	public final AnswerAccessRightsVO hasAnswerRights(QuestionProxy proxy)
	{
		final AnswerAccessRightsVO answerAccessRightsVO = new AnswerAccessRightsVO();
		
		if (userLoggedIn == null || institutionActive == null)
			return answerAccessRightsVO;
		
		if (isAdminOrInstitutionalAdmin() == true)
		{
			answerAccessRightsVO.setAddRight(true);
			answerAccessRightsVO.setReadRight(true);
			answerAccessRightsVO.setWriteRight(true);
			
			return answerAccessRightsVO;
		}
			
		if(proxy == null || proxy.getAutor() == null ) {
			return answerAccessRightsVO;
		}
		
		if (proxy.getAutor().getId().equals(userLoggedIn.getId()))
		{
			answerAccessRightsVO.setReadRight(true);
			answerAccessRightsVO.setWriteRight(true);
			answerAccessRightsVO.setAddRight(true);
			
			return answerAccessRightsVO;
		}
		
		List<UserAccessRightsProxy> userAccessRightsProxyList = Lists.newArrayList();
		userAccessRightsProxyList.addAll(personRightProxy.getQuestionEventAccList());
		userAccessRightsProxyList.addAll(personRightProxy.getQuestionAccList());
		
		for (UserAccessRightsProxy userRightsProxy : userAccessRightsProxyList)
		{
			AccessRights accRights = userRightsProxy.getAccRights();
			
			if (userRightsProxy.getQuestionEvent() != null && proxy.getQuestEvent() != null && userRightsProxy.getQuestionEvent().getId().equals(proxy.getQuestEvent().getId()))
			{
				if (accRights.equals(AccessRights.AccRead))
					answerAccessRightsVO.setReadRight(true);
				else if (accRights.equals(AccessRights.AccWrite))
					answerAccessRightsVO.setWriteRight(true);
				else if (accRights.equals(AccessRights.AccAddQuestions))
					answerAccessRightsVO.setAddRight(true);
			}
			else if (userRightsProxy.getQuestion() != null && userRightsProxy.getQuestion().getId().equals(proxy.getId()))
			{
				if (accRights.equals(AccessRights.AccRead))
					answerAccessRightsVO.setReadRight(true);
				else if (accRights.equals(AccessRights.AccWrite))
					answerAccessRightsVO.setWriteRight(true);	
				else if (accRights.equals(AccessRights.AccAddAnswers))
					answerAccessRightsVO.setAddRight(true);
			}			
		}
			
		return answerAccessRightsVO;
	}
	
	public abstract void start2(AcceptsOneWidget panel, EventBus eventBus);
	public abstract void placeChanged(Place place);
	
}
