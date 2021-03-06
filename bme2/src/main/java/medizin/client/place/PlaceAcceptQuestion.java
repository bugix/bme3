package medizin.client.place;

import com.google.gwt.place.shared.Prefix;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

import medizin.client.factory.request.McAppRequestFactory;

public class PlaceAcceptQuestion extends AbstractPlace {

	public static final String PLACE_ACCEPT_QUESTION = "PlaceAcceptQuestion";
	
	public PlaceAcceptQuestion(String placeName) {
		super(placeName);
	}

	public PlaceAcceptQuestion(String placeName, boolean reload) {
		super(placeName, reload);
	}

	public PlaceAcceptQuestion(String placeName, EntityProxyId<?> proxyId) {
		super(placeName, proxyId);
	}

	public PlaceAcceptQuestion(String placeName, boolean reload, Integer height) {
		super(placeName,reload,height);
	}

	public PlaceAcceptQuestion(String placeName, Integer height) {
		super(placeName, height);
	}

	public PlaceAcceptQuestion(String placeName, EntityProxyId<?> stableId, Integer height) {
		super(placeName,stableId, height);
	}

	@Prefix(PLACE_ACCEPT_QUESTION)
	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceAcceptQuestion> {
		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceAcceptQuestion getPlace(String token) {
			return new PlaceAcceptQuestion(token);
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
		if (this.getPlaceName() != null && this.getPlaceName().equals("")) {
			return false;

			
			 * if(this.getPlaceName().contains("PlaceQuestion!DELETED")){ return
			 * false; }
			 
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
