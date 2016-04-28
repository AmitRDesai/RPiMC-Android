package com.rpimc.hari.rpimc;

import java.io.Serializable;

/**
 * Created by Hari on 10/31/2015.
 */

public class Line implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Point p1;
    Point p2;
    int type;

    public Line(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        float dx = p2.x - p1.x;
        if (dx > 0 && slope() < 0)
            type = 0;
        else if (dx < 0 && slope() > 0)
            type = 1;
        else if (dx < 0 && slope() < 0)
            type = 2;
        else
            type = 3;
    }

    public Line(String string) {
        String[] strings = string.split("@");
        p1 = new Point(strings[0]);
        p2 = new Point(strings[1]);
        type = Integer.parseInt(strings[2]);
    }

    public static double angle(Line line1, Line line2) {
        if (line1.slope() == Float.POSITIVE_INFINITY)
            return Math.abs(Math.toDegrees(Math.atan(Math.abs((-1)
                    / line2.slope()))));
        else if (line2.slope() == Float.POSITIVE_INFINITY)
            return Math
                    .abs(Math.toDegrees(Math.atan(Math.abs(1 / line1.slope()))));
        else
            return Math.abs(Math.toDegrees(Math.atan(Math.abs(line1.slope()
                    - line2.slope())
                    / (1 + line1.slope() * line2.slope()))));
    }

    public static Line combineLines(Line l1, Line l2) {
        return new Line(l1.p1, l2.p2);
    }

    public int getType() {
        return type;
    }

    public boolean has(Point p) {
        float x1 = p2.x - p.x, x2 = p1.x - p.x;
        float y1 = p2.y - p.y, y2 = p1.y - p.y;
        if (x2 == 0 || y2 == 0 || (Math.abs(x1 / x2 - y1 / y2) < .05)) {
            p2 = p;
            return true;
        }
        return false;
    }

    public float slope() {
        float dy = p2.y - p1.y, dx = p2.x - p1.x;
        if (dx != 0)
            return dy / dx;
        return Float.POSITIVE_INFINITY;
    }

    public double lenght() {
        float dy = p2.y - p1.y, dx = p2.x - p1.x;
        return Math.sqrt(dy * dy + dx * dx);
    }

    @Override
    public String toString() {
        return p1.toString() + "@" + p2.toString() + "@" + type;
    }
}