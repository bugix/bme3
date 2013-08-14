package medizin.client.place;

import com.google.gwt.place.shared.Prefix;

import medizin.client.factory.request.McAppRequestFactory;

public class PlaceAcceptAssQuestion extends AbstractPlace {

	public static final String PLACE_ACCEPT_ASS_QUESTION = "PlaceAcceptAssQuestion";
	
	public PlaceAcceptAssQuestion(String placeName) {
		super(placeName);
	}

	public PlaceAcceptAssQuestion(String placeName, boolean reload) {
		super(placeName, reload);
	}

	@Prefix(PLACE_ACCEPT_ASS_QUESTION)
	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceAcceptAssQuestion> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceAcceptAssQuestion getPlace(String token) {
			return new PlaceAcceptAssQuestion(token);
		}
	}

}
