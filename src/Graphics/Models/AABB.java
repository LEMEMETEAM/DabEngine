package Graphics.Models;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class AABB {

    private Vector2f center, half_extent;

    public AABB(){
        this.center = new Vector2f(0);
        this.half_extent = new Vector2f(0);
    }

    public AABB(Vector2f center, Vector2f half_extent){
        this.center = center;
        this.half_extent = half_extent;
    }

    public AABB(Vector2f center){
        this.center = center;
        this.half_extent = new Vector2f(0);
    }

    public boolean intersects(AABB bounds2){
        Vector2f distance = bounds2.center.sub(center, new Vector2f());
        distance.x = (float) Math.abs(distance.x);
        distance.y = (float) Math.abs(distance.y);

        distance.sub(half_extent.add(bounds2.half_extent, new Vector2f()));

        return (distance.x < 0 && distance.y < 0);
    }

    public Vector2f getCenter() {
        return center;
    }

    public Vector2f getHalf_extent() {
        return half_extent;
    }

    public void setCenter(Vector2f center) {
        this.center = center;
    }

    public void setHalf_extent(Vector2f half_extent) {
        this.half_extent = half_extent;
    }

    public void addToCenter(Vector2f center) {
        this.center.add(center);
    }

    public void addToHalf_extent(Vector2f half_extent) {
        this.half_extent.add(half_extent);
    }
}
