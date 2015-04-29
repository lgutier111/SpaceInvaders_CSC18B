package SpaceInvaders_V3.Util;

public class CatmulRom {

    private Point p0, p1, p2, p3;

    public CatmulRom(Point p0, Point p1, Point p2, Point p3) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Point eval(float t) {
        float tx, ty;

        tx = (float) (0.5 * ((2 * p1.x()) + (p2.x() - p0.x()) * t + (2 * p0.x() - 5 * p1.x() + 4 * p2.x() - p3.x()) * t * t + (3 * p1.x() + p3.x() - p0.x() - 3 * p2.x()) * t * t * t));
        ty = (float) (0.5 * ((2 * p1.y()) + (p2.y() - p0.y()) * t + (2 * p0.y() - 5 * p1.y() + 4 * p2.y() - p3.y()) * t * t + (3 * p1.y() + p3.y() - p0.y() - 3 * p2.y()) * t * t * t));

        return new Point(tx, ty);
    }
}
