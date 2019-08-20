import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class PolyfillDemo implements Runnable {

	/**
	 * PolyfillDemo - A demo which experiments with filling a polygon 
	 * Eric McCreath 2015, 2019 - GPL
	 */

	JFrame jframe;

	public PolyfillDemo() {
		SwingUtilities.invokeLater(this);
	}

	public static void main(String[] args) {
		new PolyfillDemo();
	}

	public void run() {
		jframe = new JFrame("PolyfillDemo");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tab = new JTabbedPane();

		tab.addTab("Turning Count", new CurveDraw(6, new Spline() {

			// drawCurve - fill the polygon 
			@Override
			void drawCurve(Point2D.Double[] p, Graphics2D g) {
				
				double minx = p[0].x;
				double maxx = p[0].x;
				double miny = p[0].y;
				double maxy = p[0].y;
				for (int i = 1; i < p.length ; i++) {
					minx = Math.min(minx, p[i].x);
					maxx = Math.max(maxx, p[i].x);
					miny = Math.min(miny, p[i].y);
					maxy = Math.max(maxy, p[i].y);
				}
			    Color c = g.getColor();
				g.setColor(new Color(1.0f,0.0f,0.0f,0.3f));
				for (int x = (int) minx; x< maxx; x++) {
					for (int y = (int) miny; y< maxy; y++) {
						if (inPolygonTurningCount(p, (double)x, (double)y)) g.fillRect(x, y, 1, 1);
					}
				}
				
				
				g.setColor(c);
				g.draw(new Rectangle2D.Double(minx,miny,maxx-minx,maxy-miny));	
				// 
				for (int i = 0; i < p.length ; i++) {
					g.draw(new Line2D.Double(p[i].x, p[i].y,p[(i+1)%p.length].x, p[(i+1)%p.length].y));
				}
			}

			private boolean inPolygonTurningCount(Double[] p, double x, double y) {
				double acc = 0;
				double lastAngle = Math.atan2(p[0].x - x, p[0].y - y);

				for (int i = 1; i < p.length; i++) {
					double angle = Math.atan2(p[i].x - x, p[i].y - y);
					double dif = lastAngle - angle;
					if (dif < Math.PI) dif += Math.PI;
					else if (dif > Math.PI) dif -= Math.PI;

					acc += dif;
					lastAngle = angle;
				}

				int winding = (int) Math.round(acc / (Math.PI*2.0));
				while (winding < 0) winding += 2;
				return winding % 2 == 1;
			}
		}));
		
		
		tab.addTab("Crossing Count", new CurveDraw(6, new Spline() {

			// drawCurve - fills the polygon based on crossing count.
			@Override
			void drawCurve(Point2D.Double[] p, Graphics2D g) {
				
				
				// find a bounding rectangle
				double minx = p[0].x;
				double maxx = p[0].x;
				double miny = p[0].y;
				double maxy = p[0].y;
				for (int i = 1; i < p.length ; i++) {
					minx = Math.min(minx, p[i].x);
					maxx = Math.max(maxx, p[i].x);
					miny = Math.min(miny, p[i].y);
					maxy = Math.max(maxy, p[i].y);
				}
			    Color c = g.getColor();
				g.setColor(new Color(1.0f,0.0f,0.0f,0.3f));
				for (int x = (int) minx; x< maxx; x++) {
					for (int y = (int) miny; y< maxy; y++) {
						if (inPolygonLineCount(p, (double)x, (double)y)) g.fillRect(x, y, 1, 1);
					}
				}
			
				// draw this rectangle
				g.setColor(c);
				g.draw(new Rectangle2D.Double(minx,miny,maxx-minx,maxy-miny));	
				// draw the polygon
				for (int i = 0; i < p.length ; i++) {
					g.draw(new Line2D.Double(p[i].x, p[i].y,p[(i+1)%p.length].x, p[(i+1)%p.length].y));
				}
			}

			// inPolygonLineCount - checks if (x,y) is in polygon p. 
			private boolean inPolygonLineCount(Double[] p, double x, double y) {
				int crosscount = 0;
				
				for (int i = 0; i < p.length ; i++) {
					double  pix = p[i].x;
					double  piy = p[i].y;
					double  pi1x = p[(i+1) % p.length].x;
					double  pi1y = p[(i+1) % p.length].y;
					double vx = pi1x - pix;
					double vy = pi1y - piy;
					double xint = pix + ((y-piy) / vy) * vx;
					if (pix > x || pi1x > x) {
						if (piy < y && y <= pi1y && xint > x) {
							crosscount++;
						} else if (pi1y < y && y <= piy && xint > x) {
							crosscount++;
						}
					}
				}
				return crosscount%2 == 1;
			}
		}));

		jframe.getContentPane().add(tab);

		jframe.setVisible(true);
		jframe.pack();

	}
}
