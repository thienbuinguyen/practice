import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JComponent;

/*
 * DialR - a red nob for controlling a value that ranges between 0 and 1.
 * The dials value is changed as you drag the mouse horizontally.
 * Eric McCreath
 */

public class DialRGB extends JComponent implements MouseMotionListener,
		MouseListener {

	Color color;
	ArrayList<ChangeObserver> observers;
	
	
	private static final double MOUSEVALUESCALINGFACTOR = 100.0;
	final static Dimension dim = new Dimension(70, 70);
	final static int inset = 8;

	private double value; // range from 0.0 - 1.0

	private Integer dragStart;
	
	public DialRGB( Color color) {
		value = 0.5;
		this.color = color;
		dragStart = null;
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		observers = new ArrayList<ChangeObserver>();
	}

	public void addObserver(ChangeObserver co) {
		observers.add(co);
	}
	
	public double value() {
		return value;
	}

	public Dimension getPreferredSize() {
		return dim;
	}

	@Override
	protected void paintComponent(Graphics gg) {
		Graphics2D g = (Graphics2D) gg;
		g.setColor(Color.white);
		g.fillRect(0, 0, dim.width, dim.height);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(1.0f));
		for (int s = 0; s <= 10; s++)
			drawtick(g, s / 10.0, dim.width / 2.0 - inset / 4.0, 0.0);

		g.setColor(color);
		g.fillArc(inset, inset, dim.width - 2 * inset, dim.height - 2 * inset,
				0, 360);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(4.0f));
		drawtick(g, value, dim.width / 2.0 - inset, dim.width / 4.0);

	}

	// draw a line that radiates from the center of the dial
	// the angle of the line is based on the value for this dial
	private void drawtick(Graphics2D g, double v, double s, double e) {
		double ang = (1.0 - v) * Math.PI * 2.0 * 0.8 + Math.PI * 0.2;
		double x1 = Math.sin(ang) * s + dim.width / 2.0;
		double y1 = Math.cos(ang) * s + dim.height / 2.0;
		double x2 = Math.sin(ang) * e + dim.width / 2.0;
		double y2 = Math.cos(ang) * e + dim.height / 2.0;
		g.draw(new Line2D.Double(x1, y1, x2, y2));
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		if (dragStart != null) {
			value += (me.getX() - dragStart) / MOUSEVALUESCALINGFACTOR;
			if (value < 0.0)
				value = 0.0;
			if (value > 1.0)
				value = 1.0;
			dragStart = me.getX();
		}
		this.repaint();
		update();
	}

	private void update() {
		for (ChangeObserver co : observers) {
			co.update();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent me) {
		dragStart = me.getX();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		dragStart = null;
	}
}
