package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

public class PlaceStaticContent extends AbstractPlace {

	public static final String PLACE_STATIC_CONTENT = "PlaceStaticContent";
	
	public PlaceStaticContent(String placeName) {
		super(placeName);
	}

	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceStaticContent> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceStaticContent getPlace(String token) {
			return new PlaceStaticContent(token);
		}
	}

}
