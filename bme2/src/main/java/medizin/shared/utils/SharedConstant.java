package medizin.shared.utils;

import com.google.gwt.core.shared.GWT;

public final class SharedConstant {

	public static String getUploadBaseDIRPath() {
		
		String UPLOAD_BASE_DIR_PATH = "d:/";
		//String UPLOAD_BASE_DIR_PATH =  "/usr/local/bme/";
		
		if(GWT.isClient()) {
			UPLOAD_BASE_DIR_PATH = "";
		}
		
		return UPLOAD_BASE_DIR_PATH;
	}
	 
	public static final String DOWNLOAD_DIR = "download/";
	
	public static final String UPLOAD_QUESTION_PATH =  getUploadBaseDIRPath() + DOWNLOAD_DIR +  "question/";
	
	public static final String UPLOAD_ANSWER_PATH  =  getUploadBaseDIRPath() + DOWNLOAD_DIR + "answer/";
	
	public static final String UPLOAD_QUESTION_IMAGES_PATH = UPLOAD_QUESTION_PATH + "images/";

	public static final String UPLOAD_QUESTION_SOUND_PATH = UPLOAD_QUESTION_PATH + "sound/";

	public static final String UPLOAD_QUESTION_VIDEO_PATH = UPLOAD_QUESTION_PATH + "video/";
	
	public static String[] IMAGE_EXTENSIONS = {"png","jpg","jpge","jpe","jfif","gif","tif","tiff","img","bmp"};
	
	public static String[] SOUND_EXTENSIONS = {"mp3"};
	
	public static String[] VIDEO_EXTENSIONS= {"WebM","ogv","mp4"};

}
