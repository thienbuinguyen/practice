/**
 * P3D - a simple 3D point class.  Noting it uses an imutable approach in which the idea is that the value of a point never changes. 
 *
 * Eric McCreath 2009, 2019
 */

public class P3D {
    final double x, y, z;

    public P3D(double x, double y, double z) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // sub - subtract p from "this" point producing a new point
    public P3D sub(P3D p) {
        return new P3D(x - p.x, y - p.y, z - p.z);
    }

    // add - add p to "this" point producing a new point
    public P3D add(P3D p) {
        return new P3D(x + p.x, y + p.y, z + p.z);
    }

    // dot - calculate the dot product between p and "this" point.
    public double dot(P3D p) {
        return x * p.x + y * p.y + z * p.z;
    }

    // scale - scale "this" point by s and produce a new point
    public P3D scale(double s) {
        return new P3D(x * s, y * s, z * s);
    }

    // normalize - return the unit vector of "this" point
    public P3D normalize() {
        return scale(1.0 / length());
    }

    // length - return the length of "this" point.
    public double length() {
        return Math.sqrt(dot(this));
    }

    // reflect via the normal (which is "this" object) with the light
    public P3D reflect(P3D light) {
        double dot = this.dot(light);
        P3D proj = this.scale(dot);
        return light.sub(proj).scale(-1.0).add(proj).normalize();
    }

    @Override
    public String toString() {
        return String.format("(%.4f,%.4f,%.4f)", x, y, z);
    }
}