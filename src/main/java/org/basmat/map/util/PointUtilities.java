package org.basmat.map.util;

import org.basmat.map.model.ModelStructure;

import java.awt.*;
import java.util.Objects;

public class PointUtilities {
    /**
     * Returns a random coordinate with a circle's area
     * @param point The centre point of a circle
     * @param radius The radius of the circle
     * @return A random coordinate within a designated point and radius
     */
    public static Point calculateRandomCoords(Point point, int radius) {
        //Calculates a random point within a circle, in this case the area of effect of a societycell
        double random = Math.random();
        int r = (int) (radius * Math.sqrt(random));
        int x = (int) (point.x + (r * Math.cos(random * 2 * 3.1415)));
        int y = (int) (point.y + (r * Math.sin(random * 2 * 3.1415)));
        return new Point(x, y);
    }

    public static boolean noOOBRandomCoords(Point point) {
        return point.x >= 0 && point.x <= 145 && point.y >= 0 && point.y <= 145;
    }


    /**
     * Calculates a random coordinates within a given radius and point, and ensures that the returned coordinates are not out of bounds, uninhabitable or null
     * @param point
     * @param radius
     * @param modelStructure
     * @return
     */
    public static Point calculateValidCoordinates(Point point, int radius, ModelStructure modelStructure) {
        Point destination;
        do {
            destination = PointUtilities.calculateRandomCoords(point, radius);

        } while (Objects.isNull(modelStructure.getCoordinate(destination)) || !(modelStructure.getCoordinate(destination).getECellType().isHabitable()) || !noOOBRandomCoords(destination));
        return destination;
    }
}
