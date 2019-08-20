import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class CubicBezier implements Runnable {

	/**
	 * DotToDotDemo - Draws a curve using cubic Beizer approach. Question A.3 for Lab 2.
	 * Thien Bui-Nguyen 2019
	 */

	JFrame jframe;

	public CubicBezier() {
		SwingUtilities.invokeLater(this);
	}

	public static void main(String[] args) {
		new CubicBezier();
	}

	public void run() {
		jframe = new JFrame("Spline");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tab = new JTabbedPane();

		tab.addTab("Cubic Bezier", new CurveDraw(4, new Spline() {

			// drawCurve - draw the "curve" bye connecting the control points
			@Override
			void drawCurve(Point2D.Double[] p, Graphics2D g) {
				for (int i = 0; i < p.length - 1; i++) {
					for (double u = 0.0; u < 1.0; u += 0.001) {
						double x = (1-u)*(1-u)*(1-u)*p[0].x + 3*(1-u)*(1-u)*u*p[1].x +
								3*(1-u)*u*u*p[2].x + u*u*u*p[3].x;
						double y = (1-u) * (1-u) * (1-u) * p[0].y + 3*(1-u)*(1-u)*u*p[1].y +
								3*(1-u)*u*u*p[2].y + u*u*u*p[3].y;
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
