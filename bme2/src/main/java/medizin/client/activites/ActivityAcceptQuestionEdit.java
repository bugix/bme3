package medizin.client.activites;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.place.PlaceQuestionDetails.Operation;
import medizin.client.proxy.QuestionProxy;

import com.google.gwt.place.shared.PlaceController;

public class ActivityAcceptQuestionEdit extends ActivityQuestionEdit {

	public ActivityAcceptQuestionEdit(PlaceQuestionDetails place,McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
	}

	public ActivityAcceptQuestionEdit(PlaceQuestionDetails place,McAppRequestFactory requests, PlaceController placeController,Operation edit) {
		super(place, requests, placeController, edit);
	}

	@Override
	protected void gotoDetailsPlace(QuestionProxy questionProxy) {
		goTo(new PlaceQuestionDetails(questionProxy.stableId(),PlaceQuestionDetails.Operation.DETAILS, "ACCEPT_QUESTION"));
	}
}
