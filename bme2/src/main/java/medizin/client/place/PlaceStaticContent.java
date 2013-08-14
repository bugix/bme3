package medizin.client.place;

import com.google.gwt.place.shared.Prefix;

import medizin.client.factory.request.McAppRequestFactory;

public class PlaceStaticContent extends AbstractPlace {

	public static final String PLACE_STATIC_CONTENT = "PlaceStaticContent";
	
	public PlaceStaticContent(String placeName) {
		super(placeName);
	}

	public PlaceStaticContent(String placeName, boolean reload) {
		super(placeName, reload);
	}

	@Prefix(PLACE_STATIC_CONTENT)
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
