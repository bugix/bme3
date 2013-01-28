package medizin.client.util;

import medizin.client.proxy.PersonProxy;
import medizin.shared.UserType;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Widget;

public final class ClientUtility 
{
	public static void setUserAccess(Widget widget, PersonProxy personProxy,UserType userType, boolean isVisible) 
	{
		Log.info("in setUserAccess usertype : " + userType);
		
		switch (userType) 
		{
			case ADMIN:
				
				if(personProxy.getIsAdmin()) 
				{
					widget.setVisible(isVisible);
				}
				else 
				{
					widget.setVisible(!isVisible);
				}
				break;
	
			case USER:

				if(!personProxy.getIsAdmin()) 
				{
					widget.setVisible(isVisible);
				}
				else 
				{
					widget.setVisible(!isVisible);
				}

				break;
	
			default:
				Log.error("In ClientUtility.setUserAccess error");
				break;
		}
	}

}
