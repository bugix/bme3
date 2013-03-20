package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class PlaceQuestiontypesDetails extends AbstractDetailsPlace {

	public static final String PLACE_QUESTIONTYPES_DETAILS = "PlaceQuestiontypesDetails";
	
	public PlaceQuestiontypesDetails(String placeName) {
		super(placeName);
	}

	public PlaceQuestiontypesDetails(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);

	}

	public PlaceQuestiontypesDetails(Operation operation) {
		super(operation);

	}

	public PlaceQuestiontypesDetails(EntityProxyId<?> stableId, Operation operation) {
		super(stableId, operation);
		Log.debug("PlaceQuestiontypesDetails wird erstellt");
	}

	public static class Tokenizer extends AbstractDetailsPlace.AbstractTokenizer<PlaceQuestiontypesDetails> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceQuestiontypesDetails getPlace(String token) {
			Log.debug("Im PlaceQuestiontypesDetails.getPlace: Token -" + token);

			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new PlaceQuestiontypesDetails(requestFactory.getProxyId(bits[0]), Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new PlaceQuestiontypesDetails(requestFactory.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new PlaceQuestiontypesDetails(/*requestFactory.getProxyId(bits[0]), */Operation.CREATE);
			}

			return new PlaceQuestiontypesDetails(token);

		}
	}

}
