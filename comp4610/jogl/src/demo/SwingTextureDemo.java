import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SwingTextureDemo extends JComponent implements Runnable {

	/**
	 * SwingTextureDemo - a simple swing GUI that maps a water texture to a triangle.
	 * Thien Bui-Nguyen 14/9/2019
	 * Modified from code by Eric McCreath 2019
	 */

	BufferedImage bufferedImage;

	JFrame jframe;
	public SwingTextureDemo() {
		SwingUtilities.invokeLater(this);
	}

	public static void main(String[] args) {
		new SwingTextureDemo();
	}

	public Dimension getPreferredSize() {
		return new Dimension(800, 600);
	}

	public void run() {
		jframe = new JFrame("SwingTextureDemo");
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// make the main panel
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BorderLayout());
		mainpanel.add(this, BorderLayout.CENTER);

		// add panel to jframe and make viewable
		jframe.getContentPane().add(mainpanel);
		jframe.setVisible(true);
		jframe.pack();

		try {
			bufferedImage = ImageIO.read(new File("water.jpg"));
		} catch (IOException e) {
			System.out.println("Failed to load texture!\nExiting...");
			System.exit(1);
		}
	}

	@Override
	protected void paintComponent(Graphics graph) {
		super.paintComponent(graph);
		Graphics2D g = (Graphics2D) graph;
		Dimension dim = this.getSize();
		// fill the background
		g.setColor(Color.white);
		g.fillRect(0, 0, dim.width, dim.height);

		int[] xPoints = {-1,1,0};
		int[] yPoints = {1, 1, -1};
		g.translate(dim.width / 2, dim.height / 2);
		g.scale(dim.width / 4f, dim.height / 4f);
//		g.setPaint(new TexturePaint(bufferedImage, new Rectangle(-1, -1, 2, 2)));
		g.setColor(Color.red);
		g.fillPolygon(xPoints, yPoints, 3);
	}
}
