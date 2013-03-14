package medizin.client.activites;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.PersonAccessRightProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.UserAccessRightsProxy;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.shared.i18n.BmeConstants;
import com.allen_sauer.gwt.log.client.Log;
import com.google.common.collect.Maps;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
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
	private PlaceController placeController;
	private Place place;
	protected Map<String, Widget> reciverMap = Maps.newHashMap();
	
	public BmeConstants constants = GWT.create(BmeConstants.class);
	
	protected PersonProxy userLoggedIn;
	protected InstitutionProxy institutionActive;
	private int count = 0;
	
	protected PersonAccessRightProxy personRightProxy;
	
	public AbstractActivityWrapper(Place place,
			McAppRequestFactory requests, PlaceController placeController) {
		this.place = place;
        this.requests = requests;
        this.placeController = placeController;
	}
	
	@Override
	public final void start(final AcceptsOneWidget panel, final EventBus eventBus) {

			Log.info("start method called");
			
			requests.personRequest().myGetLoggedPerson().fire(new BMEReceiver<PersonProxy>() {

				@Override
				public void onSuccess(PersonProxy response) {
					userLoggedIn = response;
					
					if (userLoggedIn.getIsAdmin() == false)
					{
						newStart(panel, eventBus);
					}
					else
					{
						newStart(panel, eventBus);
					}
				}
				
			

				/*public void onFailure(ServerFailure error) {
					ErrorPanel erorPanel = new ErrorPanel();
					erorPanel.setErrorMessage(error.getMessage());
					Log.error(error.getMessage());
					//onStop();
				}

				@Override
				public void onViolation(Set<Violation> errors) {
					Iterator<Violation> iter = errors.iterator();
					String message = "";
					while (iter.hasNext()) {
						message += iter.next().getMessage() + "<br>";
					}
					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION
							+ " in Antwort löschen -" + message);

					ErrorPanel erorPanel = new ErrorPanel();
					erorPanel.setErrorMessage(message);
					//onStop();

				}*/

			});
			
			requests.institutionRequest().myGetInstitutionToWorkWith().fire(new BMEReceiver<InstitutionProxy>() {

				@Override
				public void onSuccess(InstitutionProxy response) {
					institutionActive = response;
					newStart(panel, eventBus);
				}

				/*public void onFailure(ServerFailure error) {
					ErrorPanel erorPanel = new ErrorPanel();
					erorPanel.setErrorMessage(error.getMessage());
					Log.error(error.getMessage());
					//onStop();
				}

				@Override
				public void onViolation(Set<Violation> errors) {
					Iterator<Violation> iter = errors.iterator();
					String message = "";
					while (iter.hasNext()) {
						message += iter.next().getMessage() + "<br>";
					}
					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION
							+ " in Antwort löschen -" + message);

					ErrorPanel erorPanel = new ErrorPanel();
					erorPanel.setErrorMessage(message);
					//onStop();
					

				}*/

			});
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
		
		requests.personRequest().getLoggedPersonAccessRights().with("question", "questionEvent", "institution").fire(new Receiver<PersonAccessRightProxy>() {

			@Override
			public void onSuccess(PersonAccessRightProxy response) {
				personRightProxy = response;		
				
				start2(panel, eventBus);
			}
		});
		
		
		
	}
	
	public abstract void start2(AcceptsOneWidget panel, EventBus eventBus);

}
