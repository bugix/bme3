package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

public class PlaceOpenDemand extends AbstractPlace {

	public static final String PLACE_OPEN_DEMAND = "PlaceOpenDemand";
	
	public PlaceOpenDemand(String placeName) {
		super(placeName);
	}

	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceOpenDemand> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}
		
		@Override
		public PlaceOpenDemand getPlace(String token) {
			return new PlaceOpenDemand(token);
		}
	}

}
