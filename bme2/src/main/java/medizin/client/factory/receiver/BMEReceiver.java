package medizin.client.factory.receiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import medizin.client.ui.widget.dialogbox.receiver.ReceiverDialog;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.i18n.BmeEnumConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.Violation;

@SuppressWarnings("deprecation")
public abstract class BMEReceiver<T> extends Receiver<T> {
	private final BmeConstants bmeConstants = GWT.create(BmeConstants.class);
	private final BmeEnumConstants enumConstants = GWT.create(BmeEnumConstants.class);

	private Map<String, Widget> localViewMap = Maps.newHashMap();
	
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
		if (error.startsWith("Server Error"))
		{
			error = new HTML(error + "<br/>" + bmeConstants.sessionTimeoutError()).getHTML();
			ReceiverDialog.showMessageDialog(error, new ReceiverDialog.Handler() {
				
				@Override
				public void onClick() {
					reloadWindow();
				}
			});
		}
		else
		{
			ReceiverDialog.showMessageDialog(error);
		}
		

		/*
		 * Timer t = new Timer() {
		 * 
		 * @Override public void run() { osceReceiverPopupView.hide();
		 * osceReceiverPopupView=null; }
		 * 
		 * }; t.schedule(10000);
		 */

	}
	
	public void reloadWindow()
	{
		int indexOfHash;
		String url = Location.getHref();
		if ((indexOfHash = url.indexOf("#")) > -1) {
			url = url.substring(0, indexOfHash);
		} 
		Window.open(url, "_self", "");
	}

	public void showMessage(ArrayList<String> messages){
		
		ReceiverDialog.showMessageDialog(bmeConstants.pleaseEnterWarning(), messages);
	}
	
	@Override
	public abstract void onSuccess(T response);	

	@Override
	public final void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
	
		//Log.info("Call onConstraintViolation");
				
		// Constraint Violation
		List<ConstraintViolation<?>> violationsWithOrder  = Lists.newArrayList(violations);
		
		Collections.sort(violationsWithOrder, new Comparator<ConstraintViolation<?>>() {

			@Override
			public int compare(ConstraintViolation<?> o1, ConstraintViolation<?> o2) {
				
				return o1.getPropertyPath().toString().compareToIgnoreCase(o2.getPropertyPath().toString());
			}
		});
		
		ArrayList<String> messages = Lists.newArrayList();
		
		for (ConstraintViolation<?> constraintViolation : violationsWithOrder) {
			String path = constraintViolation.getPropertyPath().toString();
			String message;
			try {
				Log.debug("constraintViolation message: " + constraintViolation.getMessage());
				message = enumConstants.getString(constraintViolation.getMessage());
			}catch (Exception e) {
				message = path + " " + constraintViolation.getMessage();
			}
			
			messages.add(message);
			
			if(localViewMap.containsKey(path)) {
				//Log.info("Violated key: " + path);
				//Log.info("Violated value: " + localViewMap.get(path));
				localViewMap.get(path).addStyleName("higlight_onViolation");
			}
		}
		
		showMessage(messages);
	}
	
	@Override
	public final void onViolation(Set<Violation> errors) {
		Log.info("in violation method");
	}

}
