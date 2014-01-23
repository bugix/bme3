package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Prefix;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class PlaceAcceptQuestionDetails extends AbstractDetailsPlace {

	public static final String PLACE_ACCEPT_QUESTION_DETAILS = "PlaceAcceptQuestionDetails";
	
	public PlaceAcceptQuestionDetails(String placeName) {
		super(placeName);
	}

	public PlaceAcceptQuestionDetails(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);
	}

	public PlaceAcceptQuestionDetails(Operation operation) {
		super(operation);
		Log.debug("PlaceAcceptQuestionDetails wird erstellt");
	}

	public PlaceAcceptQuestionDetails(EntityProxyId<?> stableId, Operation operation) {
		super(stableId, operation);
		Log.debug("PlaceAcceptQuestionDetails wird erstellt");
	}

	public PlaceAcceptQuestionDetails(EntityProxyId<?> stableId,
			Integer height) {
		super(stableId,height);
	}

	public PlaceAcceptQuestionDetails(EntityProxyId<?> stableId, Operation operation, Integer height) {
		super(stableId, operation, height);
	}

	@Prefix(PLACE_ACCEPT_QUESTION_DETAILS)
	public static class Tokenizer extends AbstractDetailsPlace.AbstractTokenizer<PlaceAcceptQuestionDetails> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceAcceptQuestionDetails getPlace(String token) {
			Log.debug("Im PlaceAcceptQuestionDetails.getPlace: Token -" + token);

			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new PlaceAcceptQuestionDetails(requestFactory.getProxyId(bits[0]), Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new PlaceAcceptQuestionDetails(requestFactory.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new PlaceAcceptQuestionDetails(/*requestFactory.getProxyId(bits[0]),*/Operation.CREATE);
			}
			
			return new PlaceAcceptQuestionDetails(token);
		}
	}
}
