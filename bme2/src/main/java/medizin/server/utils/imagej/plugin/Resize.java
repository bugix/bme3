/** Server Side ImageJ 
  *
  * Sample plugin that scales the image.
  *
  * @version 0.2, Apr. 11, 2001
  * @author Werner Bailer <werner@wbailer.com>
  */
package medizin.server.utils.imagej.plugin;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;

public class Resize implements PlugInFilter {

	private static final Logger log = Logger.getLogger(Resize.class);
	/** scale factor [0;1] */
	
	protected int dstWidth;
	private BufferedImage image;
	
	/** setup method 
	  * @param arg the scale factor represented as string
	  * @param img the image to be scaled
	  * @return plugin capabilities, processes all types of images
	  */
	public int setup(String arg, ImagePlus img) {
		dstWidth=img.getWidth();
		try {
			dstWidth=Integer.parseInt(arg);
		}
		catch (NumberFormatException e) { System.err.println("number format exception"); }
		return DOES_ALL;
	} 
	
	/** runs the plugin
	  * @param the image processor of the image */
	public void run(ImageProcessor ip) {
		
		log.info("current width : " + ip.getWidth());
		log.info("new width : " + dstWidth);
		
		if(ip.getWidth() <= dstWidth) {
			log.info("no change");
			image = ip.getBufferedImage();
		} else {
			log.info("resized");
			ImageProcessor processor = ip.resize(dstWidth);
			image = processor.getBufferedImage();	
		}
	}
	
	public BufferedImage getImage(){
		return image;
	}

}


