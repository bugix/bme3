package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class PlaceUserDetails extends AbstractDetailsPlace {

	public static final String PLACE_USER_DETAILS = "PlaceUserDetails";
	
	private PlaceUserDetails(String placeName) {
		super(placeName);
	}

	public PlaceUserDetails(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);
	}

	public PlaceUserDetails(Operation operation) {
		super(operation);
		Log.debug("PlaceUserDetails will be created");
	}

	public PlaceUserDetails(EntityProxyId<?> stableId, Operation operation) {
		super(stableId, operation);
		Log.debug("PlaceUserDetails will be created");
	}

	public static class Tokenizer extends AbstractDetailsPlace.AbstractTokenizer<PlaceUserDetails> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceUserDetails getPlace(String token) {
			Log.debug("Im PlaceInstitutionEvent.getPlace: Token -" + token);

			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new PlaceUserDetails(requestFactory.getProxyId(bits[0]), Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new PlaceUserDetails(requestFactory.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new PlaceUserDetails(/*requestFactory.getProxyId(bits[0]),*/Operation.CREATE);
			}

			return new PlaceUserDetails(token);

		}
	}

	// @Override
	// public boolean equals(Object obj) {
	// if (this == obj) {
	// return true;
	// }
	// if (obj == null) {
	// return false;
	// }
	// if (getClass() != obj.getClass()) {
	// return false;
	// }
	// //wenn ProxyId nicht gesetzt war es eine Löschaktion
	// if (this.proxyId==null){
	// return false;
	// }
	// PlaceUserDetails other = (PlaceUserDetails) obj;
	// //wenn O
	// if (this.getOperation()!=other.getOperation()){
	// return false;
	// }
	// // ProxyListPlace other = (ProxyListPlace) obj;
	// // if (!proxyType.equals(other.proxyType)) {
	// // return false;
	// // }
	// return true;
	// }

}
