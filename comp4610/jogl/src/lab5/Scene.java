import java.awt.Color;
import java.util.ArrayList;

/**
 * Scene - the list of items that make up a scene.
 *
 * Eric McCreath 2009, 2019
 */

public class Scene extends ArrayList<Item> {

    Color background = Color.black;

    public Color raytrace(Ray r) {

        Double mindis = null;
        Intersect intersect = null;

        // light
        P3D lightpos =  new P3D(25.0,30.0,-40.0);

        for (Item s : this) {
            Intersect i = s.intersect(r);
            if (i != null) {
                if (intersect == null || i.distance < mindis) {
                    mindis = i.distance;
                    intersect = i;
                }
            }
        }
        if (intersect != null) {
            float ambient = 0.2f;

            P3D normal = intersect.hitNormal.normalize();
            P3D lightdir = lightpos.sub(intersect.hitPosition).normalize();
            float diffuse = (float) Math.max(0, normal.dot(lightdir));

            P3D viewdir = r.position.sub(intersect.hitPosition).normalize();
            P3D reflectdir = normal.reflect(lightdir).normalize();
            float spec = (float) Math.pow(Math.max(0, viewdir.dot(reflectdir)), 32) * 0.8f;

            Color c = intersect.color;

            // ambient + diffuse lighting
            float val = Math.min(1, ambient + diffuse);

            // add specular lighting
            c = new Color(clamp((c.getRed() / 255f) * val + spec),
                    clamp((c.getGreen() / 255f) * val + spec),
                    clamp((c.getBlue() / 255f) * val + spec));

            return c;
        } else {
            return background;
        }
    }

    private float clamp(double r) {
        return (float) (r < 0.0 ? 0.0 : (r > 1.0 ? 1.0 : r));
    }



}