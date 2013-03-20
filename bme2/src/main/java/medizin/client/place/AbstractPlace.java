package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.google.common.base.Objects;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public abstract class AbstractPlace extends Place {
	private final String placeName;

	public AbstractPlace(String placeName) {
		this.placeName = placeName;
	}

	public final String getPlaceName() {
		return placeName;
	}

	public static abstract class AbstractTokenizer<P extends AbstractPlace> implements PlaceTokenizer<P> {
		protected final McAppRequestFactory requestFactory;

		public AbstractTokenizer(McAppRequestFactory requestFactory) {
			this.requestFactory = requestFactory;
		}

		@Override
		public final String getToken(P place) {
			return place.getPlaceName();
		}

		@Override
		public abstract P getPlace(String token);
	}
	
	@Override
	public boolean equals(Object obj) {

		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AbstractPlace other = (AbstractPlace) obj;

		return Objects.equal(getPlaceName(), other.getPlaceName());
	}
	
	@Override
	public int hashCode() {
		 return Objects.hashCode(this.getPlaceName()); 
	}
}
