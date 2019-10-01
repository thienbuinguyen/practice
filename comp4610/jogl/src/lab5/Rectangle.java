public class Rectangle extends Item {

    P3D position;
    P3D topvector;
    P3D sidevector;
    Texture texture;
    P3D normal;
    public Rectangle(P3D position, P3D topvector, P3D sidevector, P3D normal, Texture texture) {
        super();
        this.position = position;
        this.topvector = topvector;
        this.sidevector = sidevector;
        this.normal = normal;
        this.texture = texture;
    }

    @Override
    public Intersect intersect(Ray ray) {
        // see http://en.wikipedia.org/wiki/Ray_tracing_(graphics)

        double top = ray.position.sub(position).dot(ray.direction);
        double bottom = ray.direction.dot(normal);

        if (bottom == 0.0) return null;

        double u = top / bottom;
        P3D hit = ray.position.add(ray.direction.scale(u));
        double xfinal = this.position.x + this.topvector.x;
        double yfinal = this.position.y + + this.sidevector.y;
        if (hit.x >= this.position.x && hit.x < xfinal &&
            hit.y >= this.position.y && hit.y < yfinal) {

            double texu = (hit.x - this.position.x) / this.topvector.x;
            double texv = (hit.y - this.position.y) / this.sidevector.y;
            return new Intersect(u, hit, normal, this, texture.lookup(texu, texv));
        } else {
            return null;
        }


    }
}
