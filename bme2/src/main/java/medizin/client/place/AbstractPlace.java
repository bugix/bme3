
package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.google.common.base.Objects;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public abstract class AbstractPlace extends Place {
	private final String placeName;
	private EntityProxyId<?> proxyId;
	private boolean reload;
	protected Integer height;
	public AbstractPlace(String placeName) {
		this.placeName = placeName;
		reload = false;
	}

	public AbstractPlace(String placeName, boolean reload) {
		this.placeName = placeName;
		this.reload = reload;
	}
	
	public AbstractPlace(String placeName, EntityProxyId<?> proxyId) {
		this.placeName = placeName;
		this.proxyId = proxyId;
	}
	
	public AbstractPlace(String placeName, boolean reload, Integer height) {
		this(placeName, reload);
		this.height = height;
	}

	public AbstractPlace(String placeName, Integer height) {
		this(placeName);
		this.height = height;
	}

	public AbstractPlace(String placeName, EntityProxyId<?> stableId,
			Integer height) {
		this(placeName,stableId);
		this.height = height;
	}

	public final String getPlaceName() {
		return placeName;
	}
	
	public final EntityProxyId<?> getProxyId() {
		return proxyId;
	}

	public final void setProxyId(EntityProxyId<?> id) {
		this.proxyId = id;
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
		
		if (reload)
		{
			reload = false;
			return reload;
		}

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

	public Integer getHeight() {
		return height;
	}
}
