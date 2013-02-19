package medizin.client.activites;

import java.util.Iterator;

import java.util.Set;

import medizin.client.ui.ErrorPanel;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.question.AnswerListViewImpl;
import medizin.client.ui.view.question.QuestionDetailsView;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.Violation;
/**
 * This wrapper is used to provide access control in all activities.
 * @author masterthesis
 *
 */

abstract public class AbstractActivityWrapper extends AbstractActivity {

	private McAppRequestFactory requests;
	private PlaceController placeController;
	private Place place;
	
	public BmeConstants constants = GWT.create(BmeConstants.class);
	
	public AbstractActivityWrapper(Place place,
			McAppRequestFactory requests, PlaceController placeController) {
		this.place = place;
        this.requests = requests;
        this.placeController = placeController;
	}
	
	protected PersonProxy userLoggedIn;

	protected InstitutionProxy institutionActive;
	
	private int count = 0;
	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {

		

			requests.personRequest().myGetLoggedPerson()
			.fire(new Receiver<PersonProxy>() {

				@Override
				public void onSuccess(PersonProxy response) {
					userLoggedIn = response;
					newStart(panel, eventBus);

				}

				public void onFailure(ServerFailure error) {
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

				}

			});
			requests.institutionRequest().myGetInstitutionToWorkWith()
			.fire(new Receiver<InstitutionProxy>() {

				@Override
				public void onSuccess(InstitutionProxy response) {
					institutionActive = response;
					newStart(panel, eventBus);
				}

				public void onFailure(ServerFailure error) {
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
					

				}

			});


	}
	/**
	 * Checks if user is logged, if not login dialog is shown.
	 * @param panel
	 * @param eventBus
	 */
	public void newStart(AcceptsOneWidget panel, EventBus eventBus){
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
		
		start2(panel, eventBus);
		
		
	}
	
	public abstract void start2(AcceptsOneWidget panel, EventBus eventBus);

}
