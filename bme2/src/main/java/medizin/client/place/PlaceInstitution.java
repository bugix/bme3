package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Prefix;

public class PlaceInstitution extends AbstractPlace {

	public static final String PLACE_INSTITUTION = "PlaceInstitution";
	
	public PlaceInstitution(String placeName) {
		super(placeName);
	}

	public PlaceInstitution(String placeName, boolean reload) {
		super(placeName, reload);
	}

	public PlaceInstitution(String placeInstitution, boolean reload, Integer height) {
		super(placeInstitution,reload,height);
	}

	@Prefix(PLACE_INSTITUTION)
	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceInstitution> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceInstitution getPlace(String token) {
			Log.debug("Im PlaceInstitution.getPlace: Token -" + token);
			return new PlaceInstitution(token);
		}
	}

	/*@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		// ProxyListPlace other = (ProxyListPlace) obj;
		// if (!proxyType.equals(other.proxyType)) {
		// return false;
		// }
		return true;
	}*/

}
