package medizin.shared.utils;

import com.google.gwt.core.shared.GWT;

public final class SharedConstant {

	public static String getUploadBaseDIRPath() {
		
		//String UPLOAD_BASE_DIR_PATH = "d:/";
		String UPLOAD_BASE_DIR_PATH =  "/usr/local/bme/";
		
		if(GWT.isClient()) {
			UPLOAD_BASE_DIR_PATH = "";
		}
		
		return UPLOAD_BASE_DIR_PATH;
	}
	 
	public static final String DOWNLOAD_DIR = "download/";
	
	public static final String UPLOAD_QUESTION_PATH =  getUploadBaseDIRPath() + DOWNLOAD_DIR +  "question/";
	
	public static final String UPLOAD_ANSWER_PATH  =  getUploadBaseDIRPath() + DOWNLOAD_DIR + "answer/";
	
	public static final String UPLOAD_MEDIA_PATH =  getUploadBaseDIRPath() + DOWNLOAD_DIR +  "media/";
	
	public static final String UPLOAD_MEDIA_IMAGES_PATH = UPLOAD_MEDIA_PATH + "images/";

	public static final String UPLOAD_MEDIA_SOUND_PATH = UPLOAD_MEDIA_PATH + "sound/";

	public static final String UPLOAD_MEDIA_VIDEO_PATH = UPLOAD_MEDIA_PATH + "video/";

	public static final String FILEPATH = "filepath";

	public static final String WIDTH = "width";

	public static final String HEIGHT = "height";

	public static final String VIDEO_MEDIA_SIZE = "videoMediaSize";

	public static final String SOUND_MEDIA_SIZE = "soundMediaSize";
	
	public static String[] IMAGE_EXTENSIONS = {"png","jpg","jpge","jpe","jfif","gif","tif","tiff","img","bmp"};
	
	public static String[] SOUND_EXTENSIONS = {"mp3"};
	
	public static String[] VIDEO_EXTENSIONS= {"WebM","ogv","mp4"};
	
	public static  String MAIL_TEMPLATE="/application/gwt/unibas/mailTemplate/defaultTemplate.txt";

}
