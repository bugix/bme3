package medizin.client.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.ui.widget.resource.dndview.vo.State;
import medizin.shared.MultimediaType;
import medizin.shared.UserType;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.utils.SharedConstant;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public final class ClientUtility {

	public static final DateTimeFormat SHORT_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd");
	
	public static final Comparator<QuestionResourceProxy> QUESTION_RESOURCE_SEQUENCE_COMARATOR = new Comparator<QuestionResourceProxy>() {

		@Override
		public int compare(QuestionResourceProxy o1, QuestionResourceProxy o2) {
			return o1.getSequenceNumber().compareTo(o2.getSequenceNumber());
		}
	};
	
	public static void setUserAccess(Widget widget, PersonProxy personProxy, UserType userType, boolean isVisible) {
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

	public static List<QuestionResourceClient> getQuestionResourceClient(List<QuestionResourceProxy> questionResources) {
		List<QuestionResourceClient> clients = Lists.newArrayList();

		for (QuestionResourceProxy proxy : questionResources) {

			if(proxy.getPath() != null) {
				QuestionResourceClient client = new QuestionResourceClient();
				client.setPath(proxy.getPath());
				client.setSequenceNumber(proxy.getSequenceNumber());
				client.setType(proxy.getType());
				client.setState(State.CREATED);
				client.setId(proxy.getId());
				client.setName(proxy.getName());
				clients.add(client);	
			}
		}
		return clients;
	}

	public static List<QuestionResourceClient> getQuestionResourceClient(Set<QuestionResourceProxy> questionResourcesSet) {
		List<QuestionResourceProxy> questionResourcesList = Lists.newArrayList();
		questionResourcesList.addAll(questionResourcesSet);
		Collections.sort(questionResourcesList,QUESTION_RESOURCE_SEQUENCE_COMARATOR);
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
			fileName = path.replace(SharedConstant.UPLOAD_MEDIA_IMAGES_PATH, "");
			break;
		}
		case Sound: {
			fileName = path.replace(SharedConstant.UPLOAD_MEDIA_SOUND_PATH, "");
			break;
		}
		case Video: {
			fileName = path.replace(SharedConstant.UPLOAD_MEDIA_VIDEO_PATH, "");
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
		    Integer.parseInt(value);  
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
	
	public static boolean isDouble(String value) {

		  try  
		  {  
			  Double.parseDouble(value);  
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
	
	public static Boolean defaultBoolean(Boolean object){
		return object == null ? false : object;
	}
	
	public static String defaultString(Object object){
		return object == null ? "" : object.toString();
	}
	
	public static String defaultString(Object object,String defaultValue){
		if(defaultValue == null) defaultValue = "";
		return object == null ? defaultValue : object.toString();
	}
	
	private static BmeConstants constants = GWT.create(BmeConstants.class);

	public static String getUploadLabel(Set<MultimediaType> set) {
		String label=constants.uploadResource();
		if(set.contains(MultimediaType.Image)) label = constants.uploadImages();
		if(set.contains(MultimediaType.Sound)) label = constants.uploadSounds();
		if(set.contains(MultimediaType.Video)) label = constants.uploadVideos();
		if(set.contains(MultimediaType.Image) && set.contains(MultimediaType.Sound)) label = constants.uploadImagesOrSounds();
		if(set.contains(MultimediaType.Image) && set.contains(MultimediaType.Video)) label = constants.uploadImagesOrVideos();
		if(set.contains(MultimediaType.Sound) && set.contains(MultimediaType.Video)) label = constants.uploadSoundsOrVideos();
		if(set.contains(MultimediaType.Image) && set.contains(MultimediaType.Sound) && set.contains(MultimediaType.Video)) label = constants.uploadResource();
		return label;
	}

	public static void getImageWidthHeight(final String url,final ImageWidthHeight imageWidthHeight) {
		Image.prefetch(url);
		final Image image = new Image(new SafeUri() {
			
			@Override
			public String asString() {
				return url;
			}
		});
	
		new Timer() {
			
			@Override
			public void run() {
				imageWidthHeight.apply(image.getWidth(), image.getHeight());
				this.cancel();
			}
		}.schedule(250);
	}

	public static String removeMathJax(String text) {
		return text.replaceAll("\\[", "").replaceAll("\\]", "");		
	}

	@SuppressWarnings("deprecation")
	public static Date getDateFromOneYear() {
		Date date = new Date();
		date.setYear((date.getYear()+1));
		return date;
	}
	
	public static PersonProxy getQuestionReviwerPersonProxyFromCookie(List<PersonProxy> values) {
		
		String cookie = Cookies.getCookie(McAppConstant.LAST_SELECTED_QUESTION_REVIEWER);
		final Long personid;
		if (cookie != null && cookie.isEmpty() == false)
			personid = Long.parseLong(cookie);
		else
			personid = null;
		
		Optional<PersonProxy> firstMatch = FluentIterable.from(values).filter(Predicates.notNull()).firstMatch(personComparator(personid));
		
		return firstMatch.orNull();		
	}
	
	public static PersonProxy getAnswerReviwerPersonProxyFromCookie(List<PersonProxy> values) {
		
		String cookie = Cookies.getCookie(McAppConstant.LAST_SELECTED_ANSWER_REVIEWER);
		final Long personid;
		if (cookie != null && cookie.isEmpty() == false)
			personid = Long.parseLong(cookie);
		else
			personid = null;
		
		Optional<PersonProxy> firstMatch = FluentIterable.from(values).filter(Predicates.notNull()).firstMatch(personComparator(personid));
		
		return firstMatch.orNull();		
	}

	private static Predicate<PersonProxy> personComparator(final Long personid) {
		return new Predicate<PersonProxy>() {

			@Override
			public boolean apply(PersonProxy input) {
				return input.getId().equals(personid);
			}
		};
	}
	
	public static String sumAnswerValue(Integer sumOfAnswer) {
		if (SharedConstant.INFINITE_VALUE.equals(sumOfAnswer))
			return "\u221E";
		
		return sumOfAnswer.toString();
	}
}
