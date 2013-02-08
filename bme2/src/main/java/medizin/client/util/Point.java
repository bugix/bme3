package medizin.client.util;


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
}
