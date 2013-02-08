package medizin.client.util;

import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.collect.Lists;

public final class Point implements Comparable<Point> {

	private final int x;
	private final int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

	@Override
	public int compareTo(Point other) {

		if (x - other.x != 0) {
			return x - other.x;
		}

		if (y - other.y != 0) {
			return y - other.y;
		}

		return 0;
	}

	public boolean withinDeltaRange(Point other) {

		int deltaX = Math.abs(x - other.x);
		int deltaY = Math.abs(y - other.y);

		return deltaX < 5 && deltaY < 5;
	}

	public boolean lessThan(Point point) {

		if ((x - point.x < 0) || (x - point.x == 0 & y - point.y < 0))
			return true;

		return false;
	}

	public static List<Point> getPoints(List<String> points) {
		
		List<Point> list = Lists.newArrayList();
		
		for (String stringPoint : points) {
			if(stringPoint != null ) {
				String[] coordinates = stringPoint.split(",");
				if(coordinates.length == 2) {
					int x = Integer.parseInt(coordinates[0],10);
					int y = Integer.parseInt(coordinates[1],10);
					list.add(new  Point(x,y));
				}else {
					Log.error("error in point : " + stringPoint);
				}
			}
		}
		
		return list;
	}
}
