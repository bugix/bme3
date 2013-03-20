package medizin.client.activites;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceAcceptQuestionDetails;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.QuestionProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceController;

public class ActivityAcceptQuestionDetails extends ActivityQuestionDetails {

	public ActivityAcceptQuestionDetails(PlaceAcceptQuestionDetails place,McAppRequestFactory requests, PlaceController placeController) {
		super(createNewPlace(place), requests, placeController);
	}

	private static PlaceQuestionDetails createNewPlace(PlaceAcceptQuestionDetails place) {
		PlaceQuestionDetails details;
		
		if(place.getOperation() != PlaceAcceptQuestionDetails.Operation.CREATE) {
			details = new PlaceQuestionDetails(place.getProxyId(),place.getOperation());	
		}else {
			details = new PlaceQuestionDetails(place.getOperation());
		}
		
		return details;
	}



	@Override
	protected void startForAccessRights() {
		// do nothing
	}
	
	@Override
	protected void startForAcceptQuestion() {
		view.getAnswerListViewImpl().setVisible(false);
		view.setVisibleAcceptButton();
		view.getMatrixAnswerListViewImpl().setVisible(false);
	}
	
	@Override
	protected void initForActivity(QuestionProxy response) {
		//init((QuestionProxy) response);
		this.question = response;
		Log.debug("Details für: "+question.getQuestionText());
		view.setValue(question);	
	}
	
	@Override
	public void editClicked() {
		goTo(new PlaceAcceptQuestionDetails(question.stableId(), PlaceQuestionDetails.Operation.EDIT));
	}
	
	@Override
	public boolean isQuestionDetailsPlace() {
		return false;
	}
	
	@Override
	public void acceptQuestionClicked(QuestionProxy proxy) {
		/*QuestionRequest questionRequest = requests.questionRequest();
		proxy = questionRequest.edit(proxy);

		if (userLoggedIn.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin()) {
			proxy.setIsAcceptedAdmin(true);

			if (proxy.getIsAcceptedRewiever()) {
				proxy.setStatus(Status.ACTIVE);
				proxy.setIsActive(true);
			} else
				proxy.setStatus(Status.ACCEPTED_ADMIN);
		} else if (proxy.getRewiewer().getId().equals(userLoggedIn.getId())) {
			proxy.setIsAcceptedRewiever(true);

			if (proxy.getIsAcceptedAdmin()) {
				proxy.setStatus(Status.ACTIVE);
				proxy.setIsActive(true);
			} else
				proxy.setStatus(Status.ACCEPTED_REVIEWER);
		}else if (proxy.getAutor().getId().equals(userLoggedIn.getId())) {
			
			if(proxy.getStatus().equals(Status.CORRECTION_FROM_ADMIN)) {
				proxy.setIsAcceptedAdmin(true);
				proxy.setStatus(Status.ACCEPTED_ADMIN);	
			}else if (proxy.getStatus().equals(Status.CORRECTION_FROM_REVIEWER)) {
				proxy.setIsAcceptedRewiever(true);
				proxy.setStatus(Status.ACCEPTED_REVIEWER);	
			}else if (proxy.getStatus().equals(Status.ACCEPTED_ADMIN)) {
				proxy.setStatus(Status.ACTIVE);
				proxy.setIsActive(true);
			}else if (proxy.getStatus().equals(Status.ACCEPTED_REVIEWER)) {
				proxy.setStatus(Status.ACTIVE);
				proxy.setIsActive(true);
			}
		}
		else if (proxy.getAutor().getId().equals(userLoggedIn.getId())
				&& (proxy.getStatus().equals(Status.CORRECTION_FROM_ADMIN) || proxy
						.getStatus().equals(Status.CORRECTION_FROM_REVIEWER))) {
			proxy.setIsAcceptedAdmin(true);
			proxy.setIsAcceptedRewiever(true);
			proxy.setIsActive(true);
			proxy.setStatus(Status.ACTIVE);
		}

		questionRequest.persist().using(proxy).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				goTo(new PlaceAcceptQuestion(""));
			}
		});*/
		
		requests.questionRequest().questionAccepted(question, (userLoggedIn.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin())).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				goTo(new PlaceAcceptQuestion(""));				
			}
		});
	}
	
}
