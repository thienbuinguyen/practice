
/*
 * Item - an abstract class that is for different items that make up a scene
 * Eric McCreath 2019
 */


public abstract class Item {
    P3D position;
    boolean mirror = false;
    public abstract Intersect intersect(Ray ray);
}