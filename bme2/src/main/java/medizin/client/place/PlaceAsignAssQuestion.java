package medizin.client.place;

import com.google.gwt.place.shared.Prefix;

import medizin.client.factory.request.McAppRequestFactory;

public class PlaceAsignAssQuestion extends AbstractPlace {

	public static final String PLACE_ASIGN_ASS_QUESTION = "PlaceAsignAssQuestion";
	
	public PlaceAsignAssQuestion(String placeName) {
		super(placeName);
	}

	public PlaceAsignAssQuestion(String placeName, boolean reload) {
		super(placeName, reload);
	}

	public PlaceAsignAssQuestion(String placeAsignAssQuestion, boolean reload,
			Integer height) {
		super(placeAsignAssQuestion,reload,height);
	}

	@Prefix(PLACE_ASIGN_ASS_QUESTION)
	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceAsignAssQuestion> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceAsignAssQuestion getPlace(String token) {
			return new PlaceAsignAssQuestion(token);
		}
	}

}
