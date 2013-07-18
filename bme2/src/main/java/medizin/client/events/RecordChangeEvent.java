package medizin.client.events;

import medizin.client.ui.McAppConstant;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Cookies;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class RecordChangeEvent extends GwtEvent<RecordChangeHandler> {

	private static final Type<RecordChangeHandler> TYPE = new Type<RecordChangeHandler>();
	
	private String recordValue;
	
	public RecordChangeEvent(String val)
	{
		this.recordValue = val;
		Cookies.setCookie("user", val);
		
		if (recordValue.matches("\\d+"))
		{
			McAppConstant.TABLE_PAGE_SIZE = Integer.parseInt(this.recordValue);
		}
	}
	
	public String getRecordValue() {
		return recordValue;
	}

	public static Type<RecordChangeHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RecordChangeHandler handler) {
		handler.onRecordChange(this);
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<RecordChangeHandler> getAssociatedType() {
		return TYPE;
	}
	
	public static HandlerRegistration register(EventBus eventBus,
			RecordChangeHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}
}
