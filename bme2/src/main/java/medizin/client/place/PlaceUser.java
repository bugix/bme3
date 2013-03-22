package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

public class PlaceUser extends AbstractPlace {

	public static final String PLACE_USER = "PlaceUser";

	public PlaceUser(String placeName) {
		super(placeName);
	}

	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceUser> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceUser getPlace(String token) {
			return new PlaceUser(token);
		}
	}

}
