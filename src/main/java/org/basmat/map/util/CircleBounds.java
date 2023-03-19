package org.basmat.map.util;

import org.basmat.map.model.cells.SocietyCell;

import java.awt.*;

public class CircleBounds {
    /**
     * Returns a random coordinate with a circle's area
     * @param point The centre point of a circle
     * @param radius The radius of the circle
     * @return
     */
    public static Point calculateAndReturnRandomCoords(Point point, int radius) {
        //Calculates a random point within a circle, in this case the area of effect of a societycell
        double random = Math.random();
        int r = (int) (radius * Math.sqrt(random));
        int x = (int) (point.x + (r * Math.cos(random * 2 * 3.1415)));
        int y = (int) (point.y + (r * Math.sin(random * 2 * 3.1415)));
        return new Point(x, y);
    }
}
