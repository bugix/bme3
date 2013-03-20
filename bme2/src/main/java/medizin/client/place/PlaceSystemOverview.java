package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

public class PlaceSystemOverview extends AbstractPlace {

	public static final String PLACE_SYSTEM_OVERVIEW = "PlaceSystemOverview";
	
	public PlaceSystemOverview(String placeName) {
		super(placeName);
	}

	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceSystemOverview> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceSystemOverview getPlace(String token) {
			return new PlaceSystemOverview(token);
		}
	}

}
