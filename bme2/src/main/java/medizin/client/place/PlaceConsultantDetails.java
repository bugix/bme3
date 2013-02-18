package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class PlaceConsultantDetails extends Place {


	public enum Operation {
		DETAILS, CREATE, EDIT
	}
	private static final String SEPARATOR = "!";
	private String placeName;

	private EntityProxyId<?> proxyId;
	private Operation operation = null;
	
	
	public EntityProxyId<?> getProxyId() {
		return proxyId;
	}
	public void setProxyId(EntityProxyId<?> id) {
		this.proxyId = id;
	}
	    public PlaceConsultantDetails(String placeName) {
	        this.placeName = placeName;
	    }
	    
		public PlaceConsultantDetails(EntityProxyId<?> record) {
			this(record, Operation.DETAILS);
			
		}

		public PlaceConsultantDetails(Operation operation) {
	    	Log.debug("PlaceConsultantDetails wird erstellt");
			this.operation = operation;

		}

	    public PlaceConsultantDetails(EntityProxyId<?> stableId, Operation operation) {
	    	Log.debug("PlaceConsultantDetails wird erstellt");
			this.operation = operation;
			proxyId = stableId;

		}

		public String getPlaceName() {
	        return placeName;
	    }
		public Operation getOperation() {
			Log.debug("PlaceConsultantDetails.getOperation: " + operation);
			return operation;
		}


	    public static class Tokenizer implements PlaceTokenizer<PlaceConsultantDetails> {
	    	

	        private McAppRequestFactory requestFactory;

			public Tokenizer(McAppRequestFactory requestFactory) {
	        	this.requestFactory = requestFactory;
			}

			@Override
	        public String getToken(PlaceConsultantDetails place) {
	        	Log.debug("Im PlaceConsultantDetails.getToken: Placename -" + place.getProxyId());
	        	
//	        	if (requests==null)
//	        	Log.warn("requests null");
	        	
				if (Operation.DETAILS == place.getOperation()) {
					return place.getProxyId() + SEPARATOR + PlaceConsultantDetails.Operation.DETAILS;
				}
				else if (Operation.CREATE == place.getOperation()) {
					return /*place.getProxyId() + SEPARATOR + */SEPARATOR + PlaceConsultantDetails.Operation.CREATE;
				}
				else if (Operation.EDIT == place.getOperation()) {
					return place.getProxyId() + SEPARATOR + PlaceConsultantDetails.Operation.EDIT;
				}

	        	
	            return place.getPlaceName();
	        }

	        @Override
	        public PlaceConsultantDetails getPlace(String token) {
	        	Log.debug("Im PlaceConsultantDetails.getPlace: Token -" + token);
	        	
				String bits[] = token.split(SEPARATOR);
				Operation operation = Operation.valueOf(bits[1]);
				if (Operation.DETAILS == operation) {
					return new PlaceConsultantDetails(requestFactory.getProxyId(bits[0]), Operation.DETAILS);
				}
				if (Operation.EDIT == operation) {
					return new PlaceConsultantDetails(requestFactory.getProxyId(bits[0]), Operation.EDIT);
				}
				if (Operation.CREATE == operation) {
					return new PlaceConsultantDetails(/*requestFactory.getProxyId(bits[0]), */Operation.CREATE);
				}

				
	            return new PlaceConsultantDetails(token);
	            
	            
	        }
	    }
	    

}
