package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Prefix;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class PlaceConsultantDetails extends AbstractDetailsPlace {

	public static final String PLACE_CONSULTANT_DETAILS = "PlaceConsultantDetails";
	
	public PlaceConsultantDetails(String placeName) {
		super(placeName);
	}

	public PlaceConsultantDetails(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);
	}

	public PlaceConsultantDetails(Operation operation) {
		super(operation);
		Log.debug("PlaceConsultantDetails wird erstellt");
	}

	public PlaceConsultantDetails(EntityProxyId<?> stableId, Operation operation) {
		super(stableId, operation);
		Log.debug("PlaceConsultantDetails wird erstellt");
	}

	@Prefix(PLACE_CONSULTANT_DETAILS)
	public static class Tokenizer extends AbstractDetailsPlace.AbstractTokenizer<PlaceConsultantDetails> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceConsultantDetails getPlace(String token) {
			Log.debug("Im PlaceConsultantDetails.getPlace: Token -" + token);

			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new PlaceConsultantDetails(requestFactory.getProxyId(bits[0]), Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new PlaceConsultantDetails(requestFactory.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new PlaceConsultantDetails(/*requestFactory.getProxyId(bits[0]), */Operation.CREATE);
			}

			return new PlaceConsultantDetails(token);
		}
	}
}
