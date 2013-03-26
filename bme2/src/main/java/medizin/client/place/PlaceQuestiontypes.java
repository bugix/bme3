package medizin.client.place;

import com.google.gwt.place.shared.Prefix;

import medizin.client.factory.request.McAppRequestFactory;

public class PlaceQuestiontypes extends AbstractPlace {

	public static final String PLACE_QUESTIONTYPES = "PlaceQuestiontypes";
	
	public PlaceQuestiontypes(String placeName) {
		super(placeName);
	}

	@Prefix(PLACE_QUESTIONTYPES)
	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceQuestiontypes> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceQuestiontypes getPlace(String token) {
			return new PlaceQuestiontypes(token);
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
		// //wenn ProxyId nicht gesetzt war es eine Löschaktion
		// if (this.proxyId==null){
		// return false;
		// }
		// bei Löschaktion neu laden
		if (this.getPlaceName() != null) {
			if (this.getPlaceName().contains("PlaceQuestiontypes!DELETED")) {
				return false;
			}
		}

		// ProxyListPlace other = (ProxyListPlace) obj;
		// if (!proxyType.equals(other.proxyType)) {
		// return false;
		// }
		return true;
	}*/

}
