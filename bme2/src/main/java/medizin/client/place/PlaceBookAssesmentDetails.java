package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Prefix;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class PlaceBookAssesmentDetails extends AbstractDetailsPlace {

	// here details operation only.
	// public enum Operation { DETAILS }

	public static final String PLACE_BOOK_ASSESMENT_DETAILS = "PlaceBookAssesmentDetails";
	
	public PlaceBookAssesmentDetails(String placeName) {
		super(placeName);
	}

	public PlaceBookAssesmentDetails(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);

	}

	public PlaceBookAssesmentDetails(EntityProxyId<?> stableId, Operation operation) {
		super(stableId, Operation.DETAILS); // as only details operation is allowed
		Log.debug("PlaceBookAssesmentEvent wird erstellt");
	}

	@Prefix(PLACE_BOOK_ASSESMENT_DETAILS)
	public static class Tokenizer extends AbstractDetailsPlace.AbstractTokenizer<PlaceBookAssesmentDetails> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceBookAssesmentDetails getPlace(String token) {
			Log.debug("Im PlaceBookAssesmentEvent.getPlace: Token -" + token);

			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new PlaceBookAssesmentDetails(token);
			}

			return new PlaceBookAssesmentDetails(token);

		}
	}

}
