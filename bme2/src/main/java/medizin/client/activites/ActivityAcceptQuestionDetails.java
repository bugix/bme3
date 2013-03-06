package medizin.client.activites;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.QuestionProxy;

import com.google.gwt.place.shared.PlaceController;

public class ActivityAcceptQuestionDetails extends ActivityQuestionDetails {

	public ActivityAcceptQuestionDetails(PlaceQuestionDetails place,McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
	}

	@Override
	protected void startForAccessRights() {
		// do nothing
	}
	
	@Override
	protected void startForAcceptQuestion() {
		view.getAnswerListViewImpl().setVisible(false);
		view.setVisibleAcceptButton();
	}
	
	@Override
	protected void initForActivity(QuestionProxy response) {
		init((QuestionProxy) response);
	}
	
	@Override
	public void editClicked() {
		goTo(new PlaceQuestionDetails(question.stableId(), PlaceQuestionDetails.Operation.EDIT, "ACCEPT_QUESTION"));
	}
	
	@Override
	public boolean isQuestionDetailsPlace() {
		return false;
	}
}
