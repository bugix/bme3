package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class PlaceInstitutionEvent extends AbstractDetailsPlace {

	// only details operation
	// public enum Operation { DETAILS }

	public static final String PLACE_INSTITUTION_EVENT = "PlaceInstitutionEvent";
	
	public PlaceInstitutionEvent(String placeName) {
		super(placeName);
	}

	public PlaceInstitutionEvent(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);
	}

	public PlaceInstitutionEvent(EntityProxyId<?> stableId, Operation operation) {
		super(stableId, Operation.DETAILS);
		Log.debug("PlaceInstitutionEvent wird erstellt");
	}

	public static class Tokenizer extends AbstractDetailsPlace.AbstractTokenizer<PlaceInstitutionEvent> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceInstitutionEvent getPlace(String token) {
			Log.debug("Im PlaceInstitutionEvent.getPlace: Token -" + token);

			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new PlaceInstitutionEvent(token);
			}

			return new PlaceInstitutionEvent(token);

		}
	}

}
