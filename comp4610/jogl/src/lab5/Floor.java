import java.awt.*;

public class Floor extends Item {

    double y;
    P3D position;
    Texture texture;
    P3D normal;
    public Floor(double y, P3D normal) {
        super();
        this.y = y;
        this.normal = normal;
        this.position = new P3D(0, y, 0);
    }

    private Color lookup(double u, double v) {
        boolean reverse = false; //Math.floor(v * 100.0) % 2 == 0;

        if (Math.floor(u * 10.0) % 2 == 0) {
            if (reverse) return Color.white;
            else return Color.black;
        } else {
            if (reverse) return Color.black;
            else return Color.white;
        }

    }

    @Override
    public Intersect intersect(Ray ray) {
        // see http://en.wikipedia.org/wiki/Ray_tracing_(graphics)

        double top = ray.position.sub(position).dot(ray.direction);
        double bottom = ray.direction.dot(normal);

        if (bottom == 0.0) return null;

        double u = top / bottom;
        P3D hit = ray.position.add(ray.direction.scale(u));

        if (hit.z < 0) return null;

        return new Intersect(top/bottom, hit, normal, this, lookup(hit.x / 100.0, hit.y / 100.0));
    }
}
