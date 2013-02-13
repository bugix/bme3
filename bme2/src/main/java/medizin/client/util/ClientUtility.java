package medizin.client.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.ui.widget.resource.dndview.vo.State;
import medizin.shared.MultimediaType;
import medizin.shared.UserType;
import medizin.shared.utils.SharedConstant;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public final class ClientUtility {
	
	public static void setUserAccess(Widget widget, PersonProxy personProxy,
			UserType userType, boolean isVisible) {
		Log.info("in setUserAccess usertype : " + userType);

		switch (userType) {
		case ADMIN:

			if (personProxy.getIsAdmin()) {
				widget.setVisible(isVisible);
			} else {
				widget.setVisible(!isVisible);
			}
			break;

		case USER:

			if (!personProxy.getIsAdmin()) {
				widget.setVisible(isVisible);
			} else {
				widget.setVisible(!isVisible);
			}

			break;

		default:
			Log.error("In ClientUtility.setUserAccess error");
			break;
		}
	}

	public static List<QuestionResourceClient> getQuestionResourceClient(
			List<QuestionResourceProxy> questionResources) {
		List<QuestionResourceClient> clients = new ArrayList<QuestionResourceClient>();

		for (QuestionResourceProxy proxy : questionResources) {

			QuestionResourceClient client = new QuestionResourceClient();
			client.setPath(proxy.getPath());
			client.setSequenceNumber(proxy.getSequenceNumber());
			client.setType(proxy.getType());
			client.setState(State.CREATED);
			client.setId(proxy.getId());
			clients.add(client);
		}
		return clients;
	}

	public static List<QuestionResourceClient> getQuestionResourceClient(
			Set<QuestionResourceProxy> questionResourcesSet) {
		
		List<QuestionResourceProxy> questionResourcesList = new ArrayList<QuestionResourceProxy>();

		questionResourcesList.addAll(questionResourcesSet);
		Collections.sort(questionResourcesList,
				new Comparator<QuestionResourceProxy>() {

					@Override
					public int compare(QuestionResourceProxy o1,
							QuestionResourceProxy o2) {

						return o1.getSequenceNumber().compareTo(
								o2.getSequenceNumber());
					}
				});
		
		return getQuestionResourceClient(questionResourcesList);

	}
	
	public static void checkImageSize(final String url,final int width,final int height,final Function<Boolean, Void> function) {
		
		final Image image = new Image(new SafeUri() {
			
			@Override
			public String asString() {
				return url;
			}
		});
		
		new Timer() {
			
			@Override
			public void run() {
				
				Log.info("Image width * height : " + image.getWidth() + "*" + image.getHeight());
				if(width == image.getWidth() && height == image.getHeight()) {
					function.apply(true);
				}else {
					function.apply(false);	
				}
				
				this.cancel();
			}
		}.schedule(2000);
	
	}
	
	public static String getFileName(String path, MultimediaType multimediaType) {
		String fileName = "";
		
		if(path.contains("_")) {
			path = path.substring(path.indexOf("_")+1);
		}
			
		switch (multimediaType) {
		case Image: {
			fileName = path.replace(
					SharedConstant.UPLOAD_MEDIA_IMAGES_PATH, "");
			break;
		}
		case Sound: {
			fileName = path.replace(
					SharedConstant.UPLOAD_MEDIA_SOUND_PATH, "");
			break;
		}
		case Video: {
			fileName = path.replace(
					SharedConstant.UPLOAD_MEDIA_VIDEO_PATH, "");
			break;
		}
		default:
			break;
		}

		return fileName;
	}

	public static boolean isNumber(String value) {
		
		  try  
		  {  
		    int d = Integer.parseInt(value);  
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return false;  
		  }  
		  catch (Exception e) {
			  return false;
		  }
		  
		  return true;  
	}
}
