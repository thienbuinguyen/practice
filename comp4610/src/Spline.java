import java.awt.Graphics2D;
import java.awt.geom.Point2D;
/**
 * Spline - an abstract class that will draw a spline based on an array of control points.  Eric McCreath 2015 - GPL
 */


public abstract class Spline {
    abstract void drawCurve(Point2D.Double p[], Graphics2D g);
}
