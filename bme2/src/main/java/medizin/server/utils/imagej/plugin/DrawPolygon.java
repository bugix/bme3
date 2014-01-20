/** Server Side ImageJ 
  *
  * Sample plugin that scales the image.
  *
  * @version 0.2, Apr. 11, 2001
  * @author Werner Bailer <werner@wbailer.com>
  */
package medizin.server.utils.imagej.plugin;
import ij.ImagePlus;
import ij.gui.ImageRoi;
import ij.gui.Overlay;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.plugin.filter.PlugInFilter;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.util.List;

public class DrawPolygon implements PlugInFilter {

	/** scale factor [0;1] */
	
	private List<Polygon> polygons;
	private BufferedImage image;
	private ImagePlus img;
	/** setup method 
	  * @param arg the scale factor represented as string
	  * @param img the image to be scaled
	  * @return plugin capabilities, processes all types of images
	  */
	public int setup(String arg, ImagePlus img) {
		this.img = img; 
		return DOES_ALL;
	} 
	
	/** runs the plugin
	  * @param the image processor of the image */
	public void run(ImageProcessor ip) {
		System.out.println("in the run method");

		for (Polygon polygon : polygons) {
			
			PolygonRoi polygonRoi = new PolygonRoi(polygon, Roi.POLYGON);
			ImageProcessor processor = new ColorProcessor(ip.getWidth(), ip.getHeight());
			processor.setLineWidth(1);
			processor.setColor(Color.BLACK);
			processor.draw(polygonRoi);
			processor.setColor(Color.RED);
			processor.fill(polygonRoi);
			processor.setRoi(polygon);
			processor = processor.crop();
			ImageRoi imageRoi = new ImageRoi(polygon.getBounds().x,polygon.getBounds().y, processor);
			imageRoi.setOpacity(0.3);
			imageRoi.setZeroTransparent(true);
			Overlay overlay = new Overlay(imageRoi);
		    ip.drawOverlay(overlay);
		    ip.setRoi(imageRoi);
		}
		
	}

	public List<Polygon> getPolygonPaths() {
		return polygons;
	}

	public void setPolygonPaths(List<Polygon> polygonPaths) {
		this.polygons = polygonPaths;
	}
	
	public BufferedImage getImage(){
		return img.getBufferedImage();
	}

	public static Image makeColorTransparent(BufferedImage im, final Color color) {
    	ImageFilter filter = new RGBImageFilter() {

    		// the color we are looking for... Alpha bits are set to opaque
    		public int markerRGB = color.getRGB() | 0xFF000000;

    		public final int filterRGB(int x, int y, int rgb) {
    			if ((rgb | 0xFF000000) == markerRGB) {
    				// Mark the alpha bits as zero - transparent
    				return 0x00FFFFFF & rgb;
    			} else {
    				// nothing to do
    				return rgb;
    			}
    		}
    	};

    	ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
    	return Toolkit.getDefaultToolkit().createImage(ip);
    }
	
	private static BufferedImage imageToBufferedImage(Image image) {

    	BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g2 = bufferedImage.createGraphics();
    	g2.drawImage(image, 0, 0, null);
    	g2.dispose();

    	return bufferedImage;

    }
}


