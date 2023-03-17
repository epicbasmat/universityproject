package org.basmat.map.util;

import org.basmat.map.model.cells.SocietyCell;

import java.awt.*;

public class CircleBounds {
    public static Point calculateAndReturnRandomCoords(Point point, SocietyCell societyCell) {
        //Calculates a random point within a circle, in this case the area of effect of a societycell
        double random = Math.random();
        int r = (int) (societyCell.getRadius() * Math.sqrt(random));
        int x = (int) (point.x + (r * Math.cos(random * 2 * 3.1415)));
        int y = (int) (point.y + (r * Math.sin(random * 2 * 3.1415)));
        return new Point(x, y);
    }
}
