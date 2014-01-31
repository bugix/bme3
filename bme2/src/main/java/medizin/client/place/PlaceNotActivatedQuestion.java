package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.google.gwt.place.shared.Prefix;

public class PlaceNotActivatedQuestion extends AbstractPlace {

	public static final String PLACE_NOT_ACTIVATED_QUESTION = "PlaceNotActivatedQuestion";
	
	public PlaceNotActivatedQuestion(String placeName) {
		super(placeName);
	}

	public PlaceNotActivatedQuestion(String placeName, boolean reload) {
		super(placeName, reload);
	}

	public PlaceNotActivatedQuestion(String placeName, boolean reload, Integer height) {
		super(placeName, reload, height);
	}

	public PlaceNotActivatedQuestion(String placeName, Integer height) {
		super(placeName, height);
	}

	@Prefix(PLACE_NOT_ACTIVATED_QUESTION)
	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceNotActivatedQuestion> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceNotActivatedQuestion getPlace(String token) {
			return new PlaceNotActivatedQuestion(token);
		}
	}
}
