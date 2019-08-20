import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class DotToDotDemo implements Runnable {

	/**
	 * DotToDotDemo - A demo which draws dots between points to form a path of lines. 
	 * Eric McCreath 2015, 2019 - GPL
	 */

	JFrame jframe;

	public DotToDotDemo() {
		SwingUtilities.invokeLater(this);
	}

	public static void main(String[] args) {
		new DotToDotDemo();
	}

	public void run() {
		jframe = new JFrame("Splines");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tab = new JTabbedPane();

		tab.addTab("Dot-To-Dot", new CurveDraw(5, new Spline() {

			// drawCurve - draw the "curve" bye connecting the control points
			@Override
			void drawCurve(Point2D.Double[] p, Graphics2D g) {
				for (int i = 0; i < p.length - 1; i++) {
					for (double u = 0.0; u < 1.0; u += 0.001) {
						double x = p[i].x * u + p[i + 1].x * (1.0 - u);
						double y = p[i].y * u + p[i + 1].y * (1.0 - u);
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
