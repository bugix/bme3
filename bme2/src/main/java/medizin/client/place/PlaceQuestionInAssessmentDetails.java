package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Prefix;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class PlaceQuestionInAssessmentDetails extends AbstractDetailsPlace {

	public static final String PLACE_QUESTION_IN_ASSESSMENT_DETAILS = "PlaceQuestionInAssessmentDetails";
	
	public PlaceQuestionInAssessmentDetails(String placeName) {
		super(placeName);
	}

	public PlaceQuestionInAssessmentDetails(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);
	}

	public PlaceQuestionInAssessmentDetails(Operation operation) {
		super(operation);
		Log.debug("PlaceAcceptQuestionDetails wird erstellt");
	}

	public PlaceQuestionInAssessmentDetails(EntityProxyId<?> stableId, Operation operation) {
		super(stableId, operation);
		Log.debug("PlaceAcceptQuestionDetails wird erstellt");
	}

	public PlaceQuestionInAssessmentDetails(EntityProxyId<?> stableId,
			Integer height) {
		super(stableId, height);
	}

	@Prefix(PLACE_QUESTION_IN_ASSESSMENT_DETAILS)
	public static class Tokenizer extends AbstractDetailsPlace.AbstractTokenizer<PlaceQuestionInAssessmentDetails> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceQuestionInAssessmentDetails getPlace(String token) {
			Log.debug("Im PlaceAcceptQuestionDetails.getPlace: Token -" + token);

			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new PlaceQuestionInAssessmentDetails(requestFactory.getProxyId(bits[0]), Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new PlaceQuestionInAssessmentDetails(requestFactory.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new PlaceQuestionInAssessmentDetails(/*requestFactory.getProxyId(bits[0]),*/Operation.CREATE);
			}
			
			return new PlaceQuestionInAssessmentDetails(token);
		}
	}
}
