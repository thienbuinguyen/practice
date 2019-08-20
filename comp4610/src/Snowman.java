import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Snowman extends JComponent implements Runnable, ChangeListener {

	/**
	 * Slider2GUI - a simple swing GUI with a slider. Eric McCreath 2019
	 */

	static final int gap = 30;

	JFrame jframe;
	JSlider jslider;

	public Snowman() {
		SwingUtilities.invokeLater(this);
	}

	public static void main(String[] args) {
		new Snowman();
	}

	public Dimension getPreferredSize() {
		return new Dimension(200, 200);
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

	private void renderLine(double length, Graphics2D g2) {
		AffineTransform af = g2.getTransform();
		  g2.translate(length / 4, 0.5);
		g2.scale(length / 2, 1);
		g2.draw(new Line2D.Double(0.0,0.0,1.0,0.0));
		g2.setTransform(af);
	}

	private void renderPolygon(int sides, double radius, Graphics2D g2) {
		AffineTransform at = g2.getTransform();

		double lineLength = (2 * Math.PI * radius) / sides;
		g2.scale(1, -1);
		g2.translate(lineLength / 2, -radius);


		double angle = (Math.PI * 2) / sides;
		for (double i = 0; i < sides; i++) {
			g2.rotate(angle);
			renderLine(lineLength, g2);
			g2.translate(lineLength, 0);
		}
		g2.setTransform(at);
	}

	private void renderArm(double length, Graphics2D g2) {
		AffineTransform at = g2.getTransform();

		g2.scale(1, -1);
		renderLine(length, g2);
		g2.translate(length, 0);
		g2.rotate(Math.PI / 4);
		renderLine(length / 2.0, g2);
		g2.rotate(-Math.PI / 2);
		renderLine(length / 2.0, g2);

		g2.setTransform(at);
	}

	@Override
	protected void paintComponent(Graphics graph) {
		super.paintComponent(graph);
		Graphics2D g = (Graphics2D) graph;
		Dimension dim = this.getSize();

		// fill the background
		g.setColor(Color.white);
		g.fillRect(0, 0, dim.width, dim.height);

		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.black);

		AffineTransform af = g2.getTransform();
//		g2.translate(dim.width / 2, dim.height / 2 );
//		g2.scale(10, 10);
//		renderLine(2, g2);
//		g2.translate(2, 0);
//		g2.rotate(Math.PI / 2);
//		renderLine(2, g2);

		// render the body
		double bodyDiameter = dim.height / 2.0;
		g2.translate(dim.width / 2, 3 * dim.height /4);
//		g2.drawOval(-5, -5, 10, 10);
		g2.rotate(Math.PI / 16);
		renderPolygon(16, bodyDiameter / 2.0, g2);

		g2.rotate(-Math.PI / 16);
		AffineTransform bodyAt = g2.getTransform();

		// render the arms
		g2.translate(bodyDiameter / 2.0, 0);
		renderArm(bodyDiameter / 6.0, g2);
		g2.setTransform(bodyAt);
		g2.translate(-bodyDiameter / 2.0, 0);
		g2.rotate(Math.PI);
		renderArm(bodyDiameter / 6.0, g2);

		g2.setTransform(af);

		// render the head
		double headDiameter = bodyDiameter * (2.0/3.0);
		g2.translate(dim.width / 2.0, dim.height - bodyDiameter - (headDiameter / 2));
//		g2.drawOval(-5, -5, 10, 10);
		g2.rotate(Math.PI / 16.0);
		renderPolygon(16, headDiameter / 2.0, g2);

		g2.rotate(-Math.PI / 16.0);

		AffineTransform atHead = g2.getTransform();

		// render the eyes
		double eyeDiameter = headDiameter / 10.0;
		g2.translate(-headDiameter / 4.0, 0);
		renderPolygon(16, eyeDiameter / 2.0, g2);
		g2.setTransform(atHead);

		g2.translate(headDiameter / 4.0, 0);
		renderPolygon(16, eyeDiameter / 2.0, g2);
		g2.setTransform(af);
	}
}
