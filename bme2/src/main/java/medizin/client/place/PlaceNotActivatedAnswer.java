package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.google.gwt.place.shared.Prefix;

public class PlaceNotActivatedAnswer extends AbstractPlace {

	public static final String PLACE_NOT_ACTIVATED_ANSWER = "PlaceNotActivatedAnswer";
	
	public PlaceNotActivatedAnswer(String placeName) {
		super(placeName);
	}

	@Prefix(PLACE_NOT_ACTIVATED_ANSWER)
	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceNotActivatedAnswer> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceNotActivatedAnswer getPlace(String token) {
			return new PlaceNotActivatedAnswer(token);
		}
	}
}