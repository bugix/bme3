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

public class Scale implements PlugInFilter {

	/** scale factor [0;1] */
	protected double factor;
	
	/** setup method 
	  * @param arg the scale factor represented as string
	  * @param img the image to be scaled
	  * @return plugin capabilities, processes all types of images
	  */
	public int setup(String arg, ImagePlus img) {
		factor=1.0;
		try {
			factor=Double.parseDouble(arg);
		}
		catch (NumberFormatException e) { System.err.println("number format exception"); }
		return DOES_ALL;
	} 
	
	/** runs the plugin
	  * @param the image processor of the image */
	public void run(ImageProcessor ip) {
		ip.scale(factor,factor);
	}

}


