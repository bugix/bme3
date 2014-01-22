package medizin.client.place;

import com.google.gwt.place.shared.Prefix;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

import medizin.client.factory.request.McAppRequestFactory;

public class PlaceQuestion extends AbstractPlace {

	public static final String PLACE_QUESTION = "PlaceQuestion";
	private PlaceQuestionDetails placeQuestionDetails;
	
	public PlaceQuestion(String placeName) {
		super(placeName);
	}

	public PlaceQuestion(String placeName, boolean reload) {
		super(placeName, reload);
	}

	public PlaceQuestion(String placeName, EntityProxyId<?> stableId) {
		super(placeName,stableId);
	}

	public PlaceQuestion(String placeName, EntityProxyId<?> stableId, PlaceQuestionDetails placeQuestionDetails) {
		super(placeName, stableId);
		this.placeQuestionDetails = placeQuestionDetails;
	}

	public PlaceQuestion(String placeName, boolean reload, int height) {
		super(placeName, reload,height);
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
	
	public PlaceQuestionDetails getPlaceQuestionDetails() {
		return placeQuestionDetails;
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
