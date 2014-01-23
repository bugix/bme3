package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Objects;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public abstract class AbstractDetailsPlace extends AbstractPlace {
	
	public enum Operation {
		DETAILS, CREATE, EDIT
	}

	protected static final String SEPARATOR = "!";
	private Operation operation = null;

	
	public AbstractDetailsPlace(String placeName) {
		super(placeName);
	}

	public AbstractDetailsPlace(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);
	}

	public AbstractDetailsPlace(Operation operation) {
		super("");
		Log.debug("AbstractDetailsPlace wird erstellt");
		this.operation = operation;
	}

	public AbstractDetailsPlace(EntityProxyId<?> stableId, Operation operation) {
		super("",stableId);
		Log.debug("AbstractDetailsPlace wird erstellt");
		this.operation = operation;
		assert (operation != Operation.CREATE);
	}

	public AbstractDetailsPlace(EntityProxyId<?> record, Integer height) {
		this(record, Operation.DETAILS);
		this.height = height;
	}

	public AbstractDetailsPlace(Operation operation, Integer height) {
		super("");
		this.operation = operation;
		this.height = height;
	}

	public AbstractDetailsPlace(EntityProxyId<?> stableId, Operation operation, Integer height) {
		super("",stableId);
		this.operation = operation;
		this.height = height;
	}

	public Operation getOperation() {
		Log.debug("AbstractDetailsPlace.getOperation: " + operation);
		return operation;
	}
	
	public static abstract class AbstractTokenizer<P extends AbstractDetailsPlace> implements PlaceTokenizer<P> {

		protected McAppRequestFactory requestFactory;

		public AbstractTokenizer(McAppRequestFactory requestFactory) {
			this.requestFactory = requestFactory;
		}

		@Override
		public final String getToken(P place) {
			Log.debug("Im AbstractDetailsPlace.getToken: Placename -" + place.getProxyId());

			if (Operation.DETAILS == place.getOperation()) {
				return requestFactory.getHistoryToken(place.getProxyId()) + SEPARATOR + AbstractDetailsPlace.Operation.DETAILS;
			} else if (Operation.CREATE == place.getOperation()) {
				return /* place.getProxyId() + SEPARATOR + */SEPARATOR + AbstractDetailsPlace.Operation.CREATE;
			} else if (Operation.EDIT == place.getOperation()) {
				return requestFactory.getHistoryToken(place.getProxyId()) + SEPARATOR + AbstractDetailsPlace.Operation.EDIT;
			}

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
		final AbstractDetailsPlace other = (AbstractDetailsPlace) obj;

		// every create operation is new
		if(Operation.CREATE.equals(other.getOperation())) {
			return false;
		}
		return Objects.equal(getPlaceName(), other.getPlaceName()) 
				&& Objects.equal(getOperation(), other.getOperation()) 
				&& Objects.equal(getProxyId(), other.getProxyId());
	}
	
	@Override
	public int hashCode() {
		 return Objects.hashCode(this.getPlaceName(),this.getOperation(),this.getProxyId()); 
	}
}
