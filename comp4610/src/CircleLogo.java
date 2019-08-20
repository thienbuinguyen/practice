import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class CircleLogo extends JComponent implements Runnable, ChangeListener {

	/**
	 * Slider2GUI - a simple swing GUI with a slider. Eric McCreath 2019
	 */

	static final int gap = 30;

	JFrame jframe;
	JSlider jslider;
	BufferedImage img;

	public CircleLogo() {
		SwingUtilities.invokeLater(this);
	}

	public static void main(String[] args) {
		new CircleLogo();
	}

	public Dimension getPreferredSize() {
		return new Dimension(200, 200);
	}

	public void run() {
		jframe = new JFrame("Slider2GUI");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set up the slider
		jslider = new JSlider();
		jslider.setMaximum(360);
		jslider.setMinimum(0);
		jslider.addChangeListener(this);

		// make the main panel
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BorderLayout());
		mainpanel.add(this, BorderLayout.CENTER);
		mainpanel.add(jslider, BorderLayout.PAGE_END);

		// add panel to jframe and make viewable
		jframe.getContentPane().add(mainpanel);
		jframe.setVisible(true);
		jframe.pack();

		// render logo
		try {
			img = ImageIO.read(new File(("./compgraphicslogo.png")));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		repaint();
	}

	@Override
	protected void paintComponent(Graphics graph) {
		super.paintComponent(graph);
		Graphics2D g = (Graphics2D) graph;
		Dimension dim = this.getSize();
		// fill the background
		g.setColor(Color.white);
		g.fillRect(0, 0, dim.width, dim.height);

		// render circle
		g.setColor(Color.black);
		int strokeSize = 4;
		int size = dim.width;
		if (dim.width > dim.height) size = dim.height;

		size -= strokeSize;
		g.setStroke(new BasicStroke(strokeSize));
		g.drawOval((dim.width - size) / 2, (dim.height - size) / 2, size, size);

		// render logo
		int gap = 8;
		g.translate(dim.width / 2, dim.height / 2);
		g.scale((double) size / (img.getWidth() + gap), (double) size / (img.getWidth() + gap));
		g.rotate(jslider.getValue() * Math.PI / 180.0);
		g.drawImage(img, -img.getWidth() / 2,
				-img.getHeight() / 2, null);

	}
}
