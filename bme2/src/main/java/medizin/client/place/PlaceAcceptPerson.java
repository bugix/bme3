package medizin.client.place;

import com.google.gwt.place.shared.Prefix;

import medizin.client.factory.request.McAppRequestFactory;

public class PlaceAcceptPerson extends AbstractPlace {

	public static final String PLACE_ACCEPT_PERSON = "PlaceAcceptPerson";
	
	public PlaceAcceptPerson(String placeName) {
		super(placeName);
	}

	@Prefix(PLACE_ACCEPT_PERSON)
	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceAcceptPerson> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceAcceptPerson getPlace(String token) {
			return new PlaceAcceptPerson(token);
		}
	}

}
