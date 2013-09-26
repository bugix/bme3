package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.google.gwt.place.shared.Prefix;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class PlaceDeactivatedQuestion extends AbstractPlace {

	public static final String PLACE_DEACTIVATED_QUESTION = "PlaceDeactivatedQuestion";
	
	public PlaceDeactivatedQuestion(String placeName) {
		super(placeName);
	}

	public PlaceDeactivatedQuestion(String placeName, boolean reload) {
		super(placeName, reload);
	}

	public PlaceDeactivatedQuestion(String placeName, EntityProxyId<?> proxyId) {
		super(placeName, proxyId);
	}

	@Prefix(PLACE_DEACTIVATED_QUESTION)
	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceDeactivatedQuestion> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceDeactivatedQuestion getPlace(String token) {
			return new PlaceDeactivatedQuestion(token);
		}
	}

}
