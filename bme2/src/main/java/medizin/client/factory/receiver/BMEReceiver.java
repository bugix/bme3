package medizin.client.factory.receiver;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import medizin.client.ui.widget.dialogbox.receiver.ReceiverDialog;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.Violation;

public abstract class BMEReceiver<T> extends Receiver<T> {
	private final BmeConstants bmeConstants = GWT.create(BmeConstants.class);

	private Map<String, Widget> localViewMap;
	
	public BMEReceiver() {
		//Log.info("Call BMEReceiver Constructor()");
	}

	public BMEReceiver(Map<String, Widget> viewMap) {
		//Log.info("Call BMEReceiver Constructor(Map)");

		
		localViewMap = viewMap;

		if (localViewMap == null || localViewMap.isEmpty()) {
			Log.info("Map Null from constructor");
		} else {
			//Log.info("Map Not Null  from constructor");
			//Log.info("Size of map is: " + viewMap.size());
			
			for (Widget widget : localViewMap.values()) {
				//Log.info("Remove... Highlight");
				widget.removeStyleName("higlight_onViolation");
			}
		}
	}

	@Override
	public final void onFailure(ServerFailure error) {
		
		if(onReceiverFailure()) {
			Log.error(error.getMessage());
			showMessage(error.getMessage());	
		}

	}
	
	
	public boolean onReceiverFailure() {
		Log.error("call bmeReceiverFailure");
		return true;
	}

	public void showMessage(String error) {

		//final String errorMsg = error;
		//Log.info("Error Message" + errorMsg);

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
	
		//Log.info("Call onConstraintViolation");
		
		StringBuilder errorBuffor = new StringBuilder();
		
		// Constraint Violation
		if (localViewMap != null && violations.isEmpty() == false) {
			

			List<ConstraintViolation<?>> violationsWithOrder  = Lists.newArrayList(violations);
			
			Collections.sort(violationsWithOrder, new Comparator<ConstraintViolation<?>>() {

				@Override
				public int compare(ConstraintViolation<?> o1, ConstraintViolation<?> o2) {
					
					return o1.getPropertyPath().toString().compareToIgnoreCase(o2.getPropertyPath().toString());
				}
			});
						
			errorBuffor.append("<b>" + bmeConstants.pleaseEnterWarning() + "</b>" + "<br/><br/>");
			errorBuffor.append("<ul style=\"padding:0px; margin:0px; list-style-type:none;\">");		
			
			for (ConstraintViolation<?> constraintViolation : violationsWithOrder) {
				String path = constraintViolation.getPropertyPath().toString();
				String message;
				try {
					message = bmeConstants.getString(constraintViolation.getMessage());	
				}catch (Exception e) {
					message = path + " " + constraintViolation.getMessage();
				}
				
				errorBuffor.append("<li style=\"text-align:left; line-height: 10px; padding:0px 0px 10px 0px;margin-left: 10px;\">")
					.append("<span class=\"ui-icon ui-icon-check\" style=\"float: left; margin-top: 1px; margin-right: 6px;\"></span>")
					.append(message)
				.append("</li>");
				
				if(localViewMap.containsKey(path)) {
					//Log.info("Violated key: " + path);
					//Log.info("Violated value: " + localViewMap.get(path));
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
