package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Prefix;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class PlaceQuestionDetails extends AbstractDetailsPlace {

	public static final String PLACE_QUESTION_DETAILS = "PlaceQuestionDetails";
	
	public PlaceQuestionDetails(String placeName) {
		super(placeName);
	}

	public PlaceQuestionDetails(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);
	}

	public PlaceQuestionDetails(Operation operation) {
		super(operation);
		Log.debug("PlaceQuestionDetails wird erstellt");
	}

	public PlaceQuestionDetails(EntityProxyId<?> stableId, Operation operation) {
		super(stableId, operation);
		Log.debug("PlaceQuestionDetails wird erstellt");
	}
	
	public PlaceQuestionDetails(EntityProxyId<?> stableId, boolean reload) {
		super(stableId);
		//this.reload = reload;
		Log.debug("PlaceQuestionDetails wird erstellt");
	}

	public PlaceQuestionDetails(EntityProxyId<?> stableId, int height) {
		super(stableId,height);
	}

	@Prefix(PLACE_QUESTION_DETAILS)
	public static class Tokenizer extends AbstractDetailsPlace.AbstractTokenizer<PlaceQuestionDetails> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceQuestionDetails getPlace(String token) {
			Log.debug("Im PlaceQuestionDetails.getPlace: Token -" + token);

			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new PlaceQuestionDetails(requestFactory.getProxyId(bits[0]), Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new PlaceQuestionDetails(requestFactory.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new PlaceQuestionDetails(/*requestFactory.getProxyId(bits[0]),*/Operation.CREATE);
			}

			return new PlaceQuestionDetails(token);
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
