import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Bicycle extends JComponent implements Runnable, ChangeListener {

	/**
	 * Slider2GUI - a simple swing GUI for a bicycle. Thien Bui-Nguyen 2019. Question A.7 lab 2.
	 */

	static final int gap = 30;

	JFrame jframe;

	public Bicycle() {
		SwingUtilities.invokeLater(this);
	}

	public static void main(String[] args) {
		new Bicycle();
	}

	public Dimension getPreferredSize() {
		return new Dimension(200, 200);
	}

	private void wheel(Graphics2D g2) {
		int spokes = 10;
		int radius = 29;
		double angle = Math.PI / spokes;

		// draw spokes
		g2.setColor(Color.gray);
		g2.setStroke(new BasicStroke(1.0f));
		Point2D center = new Point2D.Double(0,0);
		for (double i = 0; i <= 2 * Math.PI; i += angle) {
			Point2D p = new Point2D.Double(radius * Math.sin(i), radius * Math.cos(i));
			g2.draw(new Line2D.Double(center, p));
		}

		// draw rim
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(2.0f));
		g2.drawOval(0 - radius, 0 - radius, 2 * radius, 2 * radius);
	}

	// draw the bike - ECM 2019
	private void bike(Graphics2D g2) {
		AffineTransform af = g2.getTransform();

		g2.translate(-45, 30);
		wheel(g2);
		g2.translate(90, 0);
		wheel(g2);

		// draw the frame and forks
		g2.setTransform(af);
		g2.setStroke(new BasicStroke(5.0f));
		// key points in the drawing
		Point2D p0 = new Point2D.Double(-5.0,30.0);
		Point2D p1 = new Point2D.Double(-45.0,30.0);
		Point2D p2 = new Point2D.Double(-20.0,70.0);
		Point2D p3 = new Point2D.Double(25.0,70.0);
		Point2D p4 = new Point2D.Double(-45.0,30.0);
		Point2D p5 = new Point2D.Double(45.0,30.0);
		Point2D p6 = new Point2D.Double(-25.0,80.0);
		Point2D p8 = new Point2D.Double(15.0,85.0);
		Point2D p7 = new Point2D.Double(10.0,85.0);
		Point2D p9 = new Point2D.Double(-20.0,80.0);
		// draw the post and handle bars
		g2.setColor(Color.GRAY);
		g2.draw(new Line2D.Double(p3,p8));
		g2.draw(new Line2D.Double(p2,p9));
		// draw the frame
		g2.setColor(Color.RED);
		g2.draw(new Line2D.Double(p1,p0));
		g2.draw(new Line2D.Double(p1,p2));
		g2.draw(new Line2D.Double(p2,p3));
		g2.draw(new Line2D.Double(p0,p2));
		g2.draw(new Line2D.Double(p0,p3));
		g2.draw(new Line2D.Double(p5,p3));

		// draw the seat and the handles
		g2.setColor(Color.BLACK);
		g2.draw(new Line2D.Double(p7,p8));
		g2.draw(new Line2D.Double(p6,p9));

		g2.setTransform(af);
	}

	public void run() {
		jframe = new JFrame("Slider2GUI");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// make the main panel
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BorderLayout());
		mainpanel.add(this, BorderLayout.CENTER);

		// add panel to jframe and make viewable
		jframe.getContentPane().add(mainpanel);
		jframe.setVisible(true);
		jframe.pack();
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

		g.translate(dim.width / 2, dim.height);
		double size = dim.width / 150.0;
		g.scale(size, -size);
		bike(g);

	}
}
