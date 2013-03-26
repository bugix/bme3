package medizin.client.place;

import com.google.gwt.place.shared.Prefix;

import medizin.client.factory.request.McAppRequestFactory;

public class PlaceQuestion extends AbstractPlace {

	public static final String PLACE_QUESTION = "PlaceQuestion";
	
	public PlaceQuestion(String placeName) {
		super(placeName);
	}

	@Prefix(PLACE_QUESTION)
	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceQuestion> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceQuestion getPlace(String token) {
			return new PlaceQuestion(token);
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
		// bei Löschaktion neu laden
		if (this.getPlaceName() != null) {
			if (this.getPlaceName().contains("PlaceQuestion!DELETED")) {
				return false;
			}
		}

		// //wenn ProxyId nicht gesetzt war es eine Löschaktion
		// if (this.proxyId==null){
		// return false;
		// }

		// ProxyListPlace other = (ProxyListPlace) obj;
		// if (!proxyType.equals(other.proxyType)) {
		// return false;
		// }
		return true;
	}*/
}
