package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class PlaceUser extends AbstractPlace {

	private EntityProxyId<?> proxyId;
	public static final String PLACE_USER = "PlaceUser";

	public PlaceUser(String placeName) {
		super(placeName);
	}

	public PlaceUser(EntityProxyId<?> proxyId) {
		this(PLACE_USER);
		this.proxyId = proxyId;
	}

	public EntityProxyId<?> getProxyId() {
		return proxyId;
	}

	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceUser> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceUser getPlace(String token) {
			return new PlaceUser(token);
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
			if (this.getPlaceName().contains("PlaceUser!DELETED") || this.getPlaceName().contains("PlaceUser!CANCEL")) {
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
