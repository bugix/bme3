package medizin.shared.utils;

import medizin.shared.MultimediaType;

public class SharedUtility {

	// Get extension for given file name
		// eg for Chrysanthemum.jpg value is jpg 
		public static String getFileExtension(String fileName) {
			String extension = null;

			if(fileName != null && fileName.length() > 0) {
				extension = fileName.substring(fileName.lastIndexOf('.')+1, fileName.length());
			}
			return extension;
		}

		public static MultimediaType getFileMultimediaType(String fileExtension) {
			MultimediaType type = null;
			
			if(fileExtension != null && fileExtension.length() > 0) {
				
				// for image
				for (String ext : SharedConstant.IMAGE_EXTENSIONS) {
					if(ext.equalsIgnoreCase(fileExtension)) {
						return MultimediaType.Image;
					}
				}
				
				// for sound
				for (String ext : SharedConstant.SOUND_EXTENSIONS) {
					if(ext.equalsIgnoreCase(fileExtension)) {
						return MultimediaType.Sound;
					}
				}
				
				// for video
				for (String ext : SharedConstant.VIDEO_EXTENSIONS) {
					if(ext.equalsIgnoreCase(fileExtension)) {
						return MultimediaType.Video;
					}
				}
				
			}
			
			return type;
		}
}
