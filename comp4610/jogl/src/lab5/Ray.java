
/**
 * Ray - these have a start position and a vector that point in the direction of the ray.  
 *
 * Eric McCreath 2009 - 2019
 * */


public class Ray {
    P3D position;
    P3D direction;  // normalized to length 1
    public Ray(P3D position, P3D direction) {
        super();
        this.position = position;
        this.direction = direction.normalize();
    }

    @Override
    public String toString() {
        return "pos : " + position + " dir : " + direction;
    }

}