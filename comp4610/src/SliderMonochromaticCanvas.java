import javax.swing.*;
import java.awt.*;


public class SliderMonochromaticCanvas extends JComponent {

	// SliderCanvas - a simple jcomponent which draws a boarder and a line of length value

	static final int gap = 30;

	int value;

	public SliderMonochromaticCanvas(int v) {
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

		float red = 0;
		float green = 0;
		float blue = 0;
		if (value <= 549) {
			green = (value - 380.f) / (549.f - 380);
			blue = 1 - green;
		} else {
			red = (value - 549.f) / (700.f - 549.f);
			green = 1 - red;
		}

		g.setColor(new Color(red, green , blue));
		g.fillRect(0, 0, dim.width, dim.height);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		g.setColor(new Color(1 - red, 1 - green, 1 - blue));
		g.drawString(Integer.toString(value) + "nm", 50, 50);
	}
}
