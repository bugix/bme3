package medizin.client.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.ui.widget.resource.dndview.vo.State;
import medizin.shared.QuestionTypes;
import medizin.shared.UserType;

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
	
	public static void checkImageSize(final String url,
			final QuestionTypeProxy questionType,final Function<Boolean, Void> function) {
		
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
				if(QuestionTypes.Textual.equals(questionType.getQuestionType())) {
					function.apply(true);
				}
				else if(questionType.getImageWidth() != null && questionType.getImageWidth().equals(image.getWidth()) && questionType.getImageHeight() != null && questionType.getImageHeight().equals(image.getHeight())) {
					function.apply(true);
				}else {
					function.apply(false);	
				}
				
				this.cancel();
			}
		}.schedule(2000);
	
	}
}
