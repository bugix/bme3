package medizin.client.place;

import com.google.gwt.place.shared.Prefix;

import medizin.client.factory.request.McAppRequestFactory;

public class PlaceSystemOverview extends AbstractPlace {

	public static final String PLACE_SYSTEM_OVERVIEW = "PlaceSystemOverview";
	
	public PlaceSystemOverview(String placeName) {
		super(placeName);
	}

	public PlaceSystemOverview(String placeName, boolean reload) {
		super(placeName, reload);
	}

	public PlaceSystemOverview(String placeSystemOverview, boolean relaod,
			Integer height) {
		super(placeSystemOverview,relaod,height);
	}

	@Prefix(PLACE_SYSTEM_OVERVIEW)
	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceSystemOverview> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceSystemOverview getPlace(String token) {
			return new PlaceSystemOverview(token);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		return false;
	}

}
