package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

public class PlaceAcceptAnswer extends AbstractPlace {

	public static final String PLACE_ACCEPT_ANSWER = "PlaceAcceptAnswer";

	public PlaceAcceptAnswer(String placeName) {
		super(placeName);
	}

	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceAcceptAnswer> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceAcceptAnswer getPlace(String token) {
			return new PlaceAcceptAnswer(token);
		}
	}
}
