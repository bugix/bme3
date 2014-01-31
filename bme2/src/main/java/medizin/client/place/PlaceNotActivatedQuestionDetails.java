package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Prefix;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class PlaceNotActivatedQuestionDetails extends AbstractDetailsPlace {

	public static final String PLACE_NOT_ACTIVATED_QUESTION_DETAILS = "PlaceNotActivatedQuestionDetails";
	
	public PlaceNotActivatedQuestionDetails(String placeName) {
		super(placeName);
	}

	public PlaceNotActivatedQuestionDetails(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);
	}

	private PlaceNotActivatedQuestionDetails(Operation operation) {
		super(operation);
		Log.debug("PlaceNotActivatedQuestionDetails wird erstellt");
	}

	private PlaceNotActivatedQuestionDetails(EntityProxyId<?> stableId, Operation operation) {
		super(stableId, operation);
		Log.debug("PlaceNotActivatedQuestionDetails wird erstellt");
	}

	public PlaceNotActivatedQuestionDetails(EntityProxyId<?> stableId,
			Integer height) {
		super(stableId, height);
	}

	@Prefix(PLACE_NOT_ACTIVATED_QUESTION_DETAILS)
	public static class Tokenizer extends AbstractDetailsPlace.AbstractTokenizer<PlaceNotActivatedQuestionDetails> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceNotActivatedQuestionDetails getPlace(String token) {
			Log.debug("Im PlaceNotActivatedQuestionDetails.getPlace: Token -" + token);

			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new PlaceNotActivatedQuestionDetails(requestFactory.getProxyId(bits[0]), Operation.DETAILS);
			}
			/*if (Operation.EDIT == operation) {
				return new PlaceNotActivatedQuestionDetails(requestFactory.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new PlaceNotActivatedQuestionDetails(requestFactory.getProxyId(bits[0]),Operation.CREATE);
			}*/

			return new PlaceNotActivatedQuestionDetails(token);
		}
	}
}
