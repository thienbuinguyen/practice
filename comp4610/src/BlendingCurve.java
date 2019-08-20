import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class BlendingCurve implements Runnable {

	/**
	 * DotToDotDemo - Draws a curve using blending approach. Question A.4 for Lab 2.
	 * Thien Bui-Nguyen 2019
	 */

	JFrame jframe;

	public BlendingCurve() {
		SwingUtilities.invokeLater(this);
	}

	public static void main(String[] args) {
		new BlendingCurve();
	}

	public void run() {
		jframe = new JFrame("Spline");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tab = new JTabbedPane();

		tab.addTab("Blending Curve", new CurveDraw(3, new Spline() {

			// drawCurve - draw the "curve" bye connecting the control points
			@Override
			void drawCurve(Point2D.Double[] p, Graphics2D g) {
				for (int i = 0; i < p.length - 1; i++) {
					for (double u = 0.0; u < 1.0; u += 0.001) {
						double d = 1 + u - u*u;

						double c0 = (1-u)/d;
						double c1 = (u* (1-u))/d;
						double c2 = u/d;

						double x = c0*p[0].x+c1*p[1].x+c2*p[2].x;
						double y = c0*p[0].y+c1*p[1].y+c2*p[2].y;
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
