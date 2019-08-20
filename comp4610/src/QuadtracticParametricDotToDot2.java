import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class QuadtracticParametricDotToDot2 implements Runnable {

	/**
	 * DotToDotDemo - Draws a curve using a quadratic parametric approach. Question A.2 for Lab 2.
	 * Thien Bui-Nguyen 2019
	 */

	JFrame jframe;

	public QuadtracticParametricDotToDot2() {
		SwingUtilities.invokeLater(this);
	}

	public static void main(String[] args) {
		new QuadtracticParametricDotToDot2();
	}

	public void run() {
		jframe = new JFrame("Splines");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tab = new JTabbedPane();

		tab.addTab("Dot-To-Dot", new CurveDraw(3, new Spline() {

			// drawCurve - draw the "curve" bye connecting the control points
			@Override
			void drawCurve(Point2D.Double[] p, Graphics2D g) {
				double xb = p[0].x - p[2].x;
				double xc = p[0].x;
				double xa = p[1].x - xb - xc;

				double yb = p[0].y - p[2].y;
				double yc = p[0].y;
				double ya = p[1].y - yb - yc;

				for (int i = 0; i < p.length - 1; i++) {
					for (double u = 0.0; u < 1.0; u += 0.001) {
						double x = (xa * (u * u)) + xb * u + xc;
						double y = (ya * (u * u)) + yb * u + yc;
						g.fillRect((int) x, (int) y, 1, 1);
					}
				}
			}
		}));

		jframe.getContentPane().add(tab);

		jframe.setVisible(true);
		jframe.pack();

	}
}
