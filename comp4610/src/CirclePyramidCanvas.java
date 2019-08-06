import javax.swing.*;
import java.awt.*;


public class CirclePyramidCanvas extends JComponent {

	// SliderCanvas - a simple jcomponent which draws a boarder and a line of length value

	static final int gap = 30;

	public Dimension getPreferredSize() {
		return new Dimension(600, 600);
	}

	private class Point {
		int x;
		int y;

		Point(int x , int y) {
			this.x = x;
			this.y = y;
		}

		double distance(Point o) {
			int dx = x - o.x;
			int dy = y - o.y;
			return Math.sqrt(dx * dx + dy * dy);
		}

		@Override
		public String toString() {
			return "("+x+","+y+")";
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension dim = this.getSize();

		// fill the background
		g.setColor(Color.white);
		g.fillRect(0, 0, dim.width, dim.height);

		// draw the boarder
		g.setColor(Color.BLACK);


		// draw pyramid
		int offset = dim.width / 2;
		int pyramidWidth = 200;

		Point top = new Point(offset, 0);
		Point bottomLeft = new Point(offset - pyramidWidth / 2, dim.height);
		Point bottomRight = new Point(offset + pyramidWidth / 2, dim.height);

		g.drawLine(top.x, top.y, bottomLeft.x, bottomLeft.y);
		g.drawLine(top.x, top.y, top.x, dim.height); // line down center of triangle
		g.drawLine(top.x, top.y, bottomRight.x, bottomRight.y);

		double dy = top.y - bottomLeft.y;
		double dx = top.x - bottomLeft.x;
		double dist = Math.sqrt(dy * dy + dx * dx);
		int base = dim.height;

		// draw circle if point is found such its distance to the left diagonal line is equal to the radius
		for (int i = base - 1; i >= 0; i--) {
			int radius = base - i;
			Point center = new Point(offset, base - radius);
			int distPointToLine = (int)
					(Math.abs(dy * center.x - dx * center.y + top.x * bottomLeft.y - top.y * bottomLeft.x) / dist);

			if (Math.abs(distPointToLine - radius) <= 1) {
				g.drawOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
				base = center.y - radius;
			}
		}

	}
}
