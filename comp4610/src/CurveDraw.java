import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.beans.Transient;

import javax.swing.JComponent;

/**
 * CurveDraw - a simple JComponet for drawing splines, it also sets up control points which can be dragged around.  Eric McCreath 2015 - GPL
 */


public class CurveDraw extends JComponent implements MouseMotionListener {
	
	private static final int YDIM = 500;
	private static final int XDIM = 500;
	Point2D.Double p[];
	Spline spline;
	
	public CurveDraw(int pc, Spline spline) {
		p = new Point2D.Double[pc];
		this.spline = spline;
		for (int i = 0;i<pc;i++) {
			p[i] = new Point2D.Double(Math.random() * 400.0 + 50.0,Math.random() * 400.0 + 50.0);
		}
		this.addMouseMotionListener(this);
	}
	
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		// clear the screen
		g.setColor(Color.white);
		g.fillRect(0, 0, XDIM, YDIM);
		
		// draw the control points
		g2.setColor(Color.blue);
		double r = 3.0;
		for(int i = 0;i< p.length;i++) g2.fill(new Ellipse2D.Double(p[i].x-r,p[i].y-r, 2.0*r,2.0*r));
		
		// draw the curve
		spline.drawCurve(p, g2);

	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(XDIM,YDIM);
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		// find the closest control point
		int cpi = 0;
		double cpd = p[0].distance(me.getX(),me.getY());
		for(int i = 1;i< p.length;i++) {
			if (p[i].distance(me.getX(),me.getY()) < cpd) {
				cpi = i;
				cpd = p[i].distance(me.getX(),me.getY());
			}
		}
		// move it to the dragged location
		p[cpi] = new Point2D.Double(me.getX(),me.getY());
		this.repaint();

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		
	}
}
