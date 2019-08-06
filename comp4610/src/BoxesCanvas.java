import javax.swing.*;
import java.awt.*;


public class BoxesCanvas extends JComponent {

	// BoxesCanvas - a simple jcomponent which draws as many boxes as possible with a border of 5 px each

	static final int gap = 30;

	public Dimension getPreferredSize() {
		return new Dimension(600, 600);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension dim = this.getSize();

		// fill the background
		g.setColor(Color.white);
		g.fillRect(0, 0, dim.width, dim.height);

		if (dim.width >= 10 || dim.height >= 10) {
			g.setColor(Color.black);

			int x = dim.width / 2;
			int y = dim.height / 2;
			int size = 5;

			while (x - (size / 2) >= 0 && y - (size / 2) >= 0) {
				g.drawRect(x - (size / 2), y - (size / 2), size, size);

				size += 10; // add 5 px border on all sides
			}
		}
	}
}
