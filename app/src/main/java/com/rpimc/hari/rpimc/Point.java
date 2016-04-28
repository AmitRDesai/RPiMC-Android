package com.rpimc.hari.rpimc;

import java.io.Serializable;

/**
 * Created by Hari on 10/31/2015.
 */
class Point implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public float x;
    public float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point(String string) {
        String[] strings = string.split(",");
        this.x = Float.parseFloat(strings[0]);
        this.y = Float.parseFloat(strings[1]);
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}
