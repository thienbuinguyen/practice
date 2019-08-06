import javax.swing.*;
import java.awt.*;


public class SliderPolygonCanvas extends JComponent {

	// SliderCanvas - a simple jcomponent which draws a boarder and a line of length value

	static final int gap = 30;

	int value;

	public SliderPolygonCanvas(int v) {
		value = v;
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(600, 600);
	}

	void setValue(int v) {
		value = v;
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension dim = this.getSize();

		// fill the background
		g.setColor(Color.white);
		g.fillRect(0, 0, dim.width, dim.height);

		g.setColor(Color.BLACK);
		int radius = 100;
		double inc = 2 * Math.PI / value;
		int x = dim.width / 2 + radius;
		int y = dim.height / 2;
		for (double i = inc; i <= 2 * Math.PI; i += inc) {
			int nx = (int) ((dim.width / 2) + radius * Math.cos((i)));
			int ny = (int) ((dim.height / 2) + radius * Math.sin(i));

			g.drawLine(x, y, nx, ny);
			x = nx;
			y = ny;
		}

		// make sure line is drawn from last point to initial point
		g.drawLine(x, y, dim.width / 2 + radius, dim.height / 2);
//
//		// draw the boarder
//		g.setColor(Color.BLACK);
//		g.drawRect(gap, gap, dim.width - 2*gap, dim.height - 2*gap);
//
//		// draw the line
//		g.drawLine(gap, dim.height/2, gap + value, dim.height/2);

//		// draw box stacks
//		int size = 200;
//		int x = gap;
//		int y = dim.height - size - 1;
//
//		while (size >= 1) {
//			g.drawRect(x, y, size, size);
//			size = size / 2;
//			y -= size - 1;
//			x += size / 2;
//		}
//
//		// draw pyramid
//		int offset = 400;
//		int pyramidWdith = 200;
//		g.drawLine(offset, 0, offset, dim.height);
//		g.drawLine(offset, 0, offset - pyramidWdith / 2, dim.height);
//		g.drawLine(offset, 0, offset + pyramidWdith / 2, dim.height);
	}
}
