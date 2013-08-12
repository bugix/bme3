package medizin.client.place;

import com.google.gwt.place.shared.Prefix;

import medizin.client.factory.request.McAppRequestFactory;

public class PlaceAssesment extends AbstractPlace {

	public static final String PLACE_ASSESMENT = "PlaceAssesment";
	
	public PlaceAssesment(String placeName) {
		super(placeName);
	}

	public PlaceAssesment(String placeName, boolean reload) {
		super(placeName, reload);
	}

	@Prefix(PLACE_ASSESMENT)
	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceAssesment> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceAssesment getPlace(String token) {
			return new PlaceAssesment(token);
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
			if (this.getPlaceName().contains("PlaceAssesment!DELETED") || this.getPlaceName().contains("PlaceAssesment!CANCEL")) {
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
