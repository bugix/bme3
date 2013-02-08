package medizin.client.util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class PolygonPath {

	private final List<Point> points; 
	
	public PolygonPath() {
		points = new ArrayList<Point>();
	}
	
	
	public void addPoint(Point p) {
		points.add(p);
	}
	
	public void addPoint(int x,int y) {
		points.add(new Point(x, y));
	}
	
	public void setPoint(Point p) {
		points.add(p);
	}
	public Point getPoint(int index) {
		return points.get(index);
	}
	
	public List<Point> getPoints() {
		return points;
	}
	
	public boolean checkForPolygon() {
		
		boolean flag = true;
		
		List<Point> points = getPoints();
		
		if (points.size() > 2) {
			Point lastPoint = points.get(points.size() - 1);
			Point secondLastPoint = points.get(points.size() - 2);

			for (int i = 0; i < points.size() - 2; i++) {
				if(doLineSegmentsIntersect(points.get(i), points.get(i+1), lastPoint, secondLastPoint) == true) {
					Log.info("line intersect.");
					flag = false;
					break;
				}
			}
		}

		return flag;
	}

	
	public boolean isPointOnLineSegment(Point start,Point end, Point point) {
		
		boolean flag = (start.getX() < point.getX() || end.getX() < point.getX() ) 
						&& (point.getX() < start.getX() || point.getX() < end.getX()) 
						&& (start.getY() < point.getY() || end.getY() < point.getY()) 
						&& (point.getY() < start.getY() || point.getY() < end.getY()); 
		
		return flag;
	}
	
	public int computeDirection(Point start, Point end, Point point) {
		double a = (point.getX() - start.getX()) * (end.getY() - start.getY());
		double b = (end.getX() - start.getX()) * (point.getY() - start.getY());
		return a < b ? -1 : a > b ? 1 : 0;
	}
	
	/** Do line segments (x1, y1)--(x2, y2) and (x3, y3)--(x4, y4) intersect? */
	public boolean doLineSegmentsIntersect(Point line1Start, Point line1End, Point line2Start, Point line2End) {
		int d1 = computeDirection(line2Start, line2End, line1Start);
		int d2 = computeDirection(line2Start, line2End, line1End);
		int d3 = computeDirection(line1Start, line1End, line2Start);
		int d4 = computeDirection(line1Start, line1End, line2End);
		
		return (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) &&
				((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0)) ||
				( d1 == 0 && isPointOnLineSegment(line2Start, line2End, line1Start)) ||
				( d2 == 0 && isPointOnLineSegment(line2Start, line2End, line1End)) ||
				( d3 == 0 && isPointOnLineSegment(line1Start, line1End, line2Start)) ||
				( d4 == 0 && isPointOnLineSegment(line1Start, line1End, line2End))
				);
	}


	public int[] getAllXPoints() {
	
		int[] xPoints = new int[size()];
		int i = 0;
		for (Point point : points) {
			xPoints[i] = point.getX();
			i++;
		}	
		return xPoints;
	}

	public int[] getAllYPoints() {
		int[] yPoints = new int[size()];
		int i = 0;
		for (Point point : points) {
			yPoints[i] = point.getY();
			i++;
		}	
		return yPoints;
	}

	public int size() {
		return getPoints().size();
	}
	
	@Override
	public String toString() {
		Joiner joiner = Joiner.on(",").skipNulls();
		
		return joiner.join(points);
		
		/*StringBuilder builder = new StringBuilder();
		for(int i=0; i<points.size(); i++) {
			
			builder.append(points.get(i));
			
			
			if(i == points.size() - 1) {
				builder.append(",");	
			}
		}
		return builder.toString().substring(0, builder.length()-1);*/
	}
	
	public static List<PolygonPath> getPolygonPaths(List<String> polygons) {
		
		List<PolygonPath> polygonPaths = Lists.newArrayList(); 
		
		if(polygons != null) {
			
			for (String poly : polygons) {
				if(poly != null && poly.length() > 0) {
					PolygonPath p = getPolygonPath(poly);
					if(p != null) {
						polygonPaths.add(p);
					}	
				}
			}
		}
		return polygonPaths;
	}

	public static PolygonPath getPolygonPath(String poly) {
		
		final Splitter splitter = Splitter.on(',').trimResults().omitEmptyStrings();
		Iterator<String> points = splitter.split(poly).iterator();
		PolygonPath path = new PolygonPath();
		
		while(points.hasNext()) {
			int x = Integer.parseInt(points.next(), 10);
			int y;
			if(points.hasNext()) {
				y = Integer.parseInt(points.next(), 10);
			}else {
				break;
			}
			path.addPoint(x,y);	
		}
		
		/*PolygonPath path = new PolygonPath();
		String[] points = poly.split(",");
		
		for (int i=0; i < points.length; i+=2) {
			path.addPoint(Integer.parseInt(points[i], 10),Integer.parseInt(points[i+1], 10));	
		}
*/		
		return path;
	}
}