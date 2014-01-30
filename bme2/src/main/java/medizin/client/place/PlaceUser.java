package medizin.client.place;

import com.google.gwt.place.shared.Prefix;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

import medizin.client.factory.request.McAppRequestFactory;

public class PlaceUser extends AbstractPlace {

	public static final String PLACE_USER = "PlaceUser";

	public PlaceUser(String placeName) {
		super(placeName);
	}

	public PlaceUser(String placeName, boolean reload) {
		super(placeName, reload);
	}

	public PlaceUser(String placeName, EntityProxyId<?> proxyId) {
		super(placeName, proxyId);
	}

	public PlaceUser(String placeName, boolean reload, Integer height) {
		super(placeName,reload,height);
	}

	public PlaceUser(String placeName, Integer height) {		
		super(placeName, height);
	}

	@Prefix(PLACE_USER)
	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceUser> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceUser getPlace(String token) {
			return new PlaceUser(token);
		}
	}

}
