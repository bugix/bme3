package medizin.client.place;

import com.google.gwt.place.shared.Prefix;

import medizin.client.factory.request.McAppRequestFactory;

public class PlaceBookAssesment extends AbstractPlace {

	public static final String PLACE_BOOK_ASSESMENT = "PlaceBookAssesment";
	
	public PlaceBookAssesment(String placeName) {
		super(placeName);
	}

	public PlaceBookAssesment(String placeName, boolean reload) {
		super(placeName, reload);
	}

	@Prefix(PLACE_BOOK_ASSESMENT)
	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceBookAssesment> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceBookAssesment getPlace(String token) {
			return new PlaceBookAssesment(token);
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
