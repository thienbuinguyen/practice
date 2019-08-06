import javax.swing.*;
import java.awt.*;
import java.util.Random;


public class SliderRandomLinesCanvas extends JComponent {

	// SliderCanvas - a simple jcomponent which draws a boarder and a line of length value

	static final int gap = 30;

	int value;

	public SliderRandomLinesCanvas(int v) {
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

		// draw the boarder
		g.setColor(Color.BLACK);

		Random r = new Random();
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(4));
		for (int i = 0; i < 500; i++) {
			g2.setColor(new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), (float) value / 100));
			g2.drawLine(r.nextInt() % dim.width, r.nextInt() % dim.height,
					r.nextInt() % dim.width, r.nextInt() % dim.height);
		}
	}
}
