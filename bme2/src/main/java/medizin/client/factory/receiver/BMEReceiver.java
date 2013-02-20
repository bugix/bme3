package medizin.client.factory.receiver;

import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import medizin.client.ui.widget.dialogbox.receiver.ReceiverDialog;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.Violation;

public abstract class BMEReceiver<T> extends Receiver<T> {
	private final BmeConstants bmeConstants = GWT.create(BmeConstants.class);

	private Map<String, Widget> localViewMap;
	
	public BMEReceiver() {
		Log.info("Call BMEReceiver Constructor()");
	}

	public BMEReceiver(Map<String, Widget> viewMap) {
		Log.info("Call BMEReceiver Constructor(Map)");

		
		localViewMap = viewMap;

		if (localViewMap == null || localViewMap.isEmpty()) {
			Log.info("Map Null from constructor");
		} else {
			Log.info("Map Not Null  from constructor");
			Log.info("Size of map is: " + viewMap.size());
			
			for (Widget widget : localViewMap.values()) {
				Log.info("Remove... Highlight");
				widget.removeStyleName("higlight_onViolation");
			}
		}
	}

	@Override
	public final void onFailure(ServerFailure error) {
		Log.error(error.getMessage());
		showMessage(error.getMessage());
		onReceiverFailure();
	}
	
	
	public void onReceiverFailure() {
		Log.error("call bmeReceiverFailure");
		
	}

	public void showMessage(String error) {

		final String errorMsg = error;
		Log.info("Error Message" + errorMsg);

		ReceiverDialog.showMessageDialog(error);

		/*
		 * Timer t = new Timer() {
		 * 
		 * @Override public void run() { osceReceiverPopupView.hide();
		 * osceReceiverPopupView=null; }
		 * 
		 * }; t.schedule(10000);
		 */

	}

	@Override
	public abstract void onSuccess(T response);

	@Override
	public final void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
	
		Log.info("Call onConstraintViolation");
		
		StringBuilder errorBuffor = new StringBuilder();
		
		// Constraint Violation
		if (localViewMap != null && violations.isEmpty() == false) {
			
			errorBuffor.append("<b>" + bmeConstants.pleaseEnterWarning() + "</b>" + "<br/><br/>");
			errorBuffor.append("<table>");
			for (ConstraintViolation<?> constraintViolation : violations) {
				String path = constraintViolation.getPropertyPath().toString();
				errorBuffor.append("<tr>")
								.append("<td>").append(path).append("</td>")
								.append("<td>").append(" : ").append(constraintViolation.getMessage()).append("</td>")
						   .append("<tr />");
				
				if(localViewMap.containsKey(path)) {
					Log.info("Violated key: " + path);
					Log.info("Violated value: " + localViewMap.get(path));
					localViewMap.get(path).addStyleName("higlight_onViolation");
				}
			}
			errorBuffor.append("<table>");
			showMessage(errorBuffor.toString());

		}
		onReceiverFailure();
	}
	
	@Override
	public final void onViolation(Set<Violation> errors) {
		Log.info("in violation method");
	}

}
