package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class PlaceAcceptQuestion extends Place {

	 private String placeName;
	 private EntityProxyId<?> proxyId;

	    public PlaceAcceptQuestion(String placeName) {
	        this.placeName = placeName;
	    }

	    public PlaceAcceptQuestion(EntityProxyId<?> proxyId) {
			this.proxyId = proxyId;
		}

	    
	    public String getPlaceName() {
	        return placeName;
	    }

	    public static class Tokenizer implements PlaceTokenizer<PlaceAcceptQuestion> {
	    	private final McAppRequestFactory requestFactory;
	        public Tokenizer(McAppRequestFactory requestFactory) {
	        	this.requestFactory = requestFactory;
			}

			@Override
	        public String getToken(PlaceAcceptQuestion place) {
	            return place.getPlaceName();
	        }

	        @Override
	        public PlaceAcceptQuestion getPlace(String token) {
	            return new PlaceAcceptQuestion(token);
	        }
	    }
	    
	    @Override
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
			//bei Löschaktion neu laden
			if(this.getPlaceName()!=null && this.getPlaceName().equals("")){
				return false;
				
				/*if(this.getPlaceName().contains("PlaceQuestion!DELETED")){
					return false;
				}*/
			}
		
//			//wenn ProxyId nicht gesetzt war es eine Löschaktion
//			if (this.proxyId==null){
//				return false;
//			}

//			ProxyListPlace other = (ProxyListPlace) obj;
//			if (!proxyType.equals(other.proxyType)) {
//				return false;
//			}
			return true;
		}

}
