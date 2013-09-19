package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.google.gwt.place.shared.Prefix;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class PlaceDeactivatedQuestionDetails extends AbstractDetailsPlace {

	public static final String PLACE_DEACTIVATED_QUESTION_DETAILS = "PlaceDeactivatedQuestionDetails";
	
	public PlaceDeactivatedQuestionDetails(String placeName) {
		super(placeName);
	}

	public PlaceDeactivatedQuestionDetails(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);
	}

	private PlaceDeactivatedQuestionDetails(Operation operation) {
		super(operation);
	}

	private PlaceDeactivatedQuestionDetails(EntityProxyId<?> stableId, Operation operation) {
		super(stableId, operation);
	}

	@Prefix(PLACE_DEACTIVATED_QUESTION_DETAILS)
	public static class Tokenizer extends AbstractDetailsPlace.AbstractTokenizer<PlaceDeactivatedQuestionDetails> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceDeactivatedQuestionDetails getPlace(String token) {
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new PlaceDeactivatedQuestionDetails(requestFactory.getProxyId(bits[0]), Operation.DETAILS);
			}			
			return new PlaceDeactivatedQuestionDetails(token);
		}
	}

}
