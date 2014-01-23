package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Prefix;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class PlaceAssesmentDetails extends AbstractDetailsPlace {

	public static final String PLACE_ASSESMENT_DETAILS = "PlaceAssesmentDetails";
	
	public PlaceAssesmentDetails(String placeName) {
		super(placeName);
	}

	public PlaceAssesmentDetails(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);

	}

	public PlaceAssesmentDetails(Operation operation) {
		super(operation);
		Log.debug("PlaceAssesmentDetails wird erstellt");
	}

	public PlaceAssesmentDetails(EntityProxyId<?> stableId, Operation operation) {
		super(stableId, operation);
		Log.debug("PlaceAssesmentDetails wird erstellt");
	}

	public PlaceAssesmentDetails(EntityProxyId<?> stableId, Integer height) {
		super(stableId, height);
	}

	public PlaceAssesmentDetails(Operation operation, Integer height) {
		super(operation, height);
	}

	public PlaceAssesmentDetails(EntityProxyId<?> stableId, Operation operation, Integer height) {
		super(stableId, operation, height);
	}

	@Prefix(PLACE_ASSESMENT_DETAILS)
	public static class Tokenizer extends AbstractDetailsPlace.AbstractTokenizer<PlaceAssesmentDetails>  {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceAssesmentDetails getPlace(String token) {
			Log.debug("Im PlaceAssesmentDetails.getPlace: Token -" + token);

			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new PlaceAssesmentDetails(requestFactory.getProxyId(bits[0]), Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new PlaceAssesmentDetails(requestFactory.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new PlaceAssesmentDetails(/* requestFactory.getProxyId(bits[0]),*/Operation.CREATE);
			}

			return new PlaceAssesmentDetails(token);

		}
	}
}
