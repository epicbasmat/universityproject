package org.basmat.map.util;

import com.google.common.collect.ObjectArrays;
import org.basmat.map.model.ModelStructure;

import java.awt.*;
import java.util.Collection;

public class PointUtilities {
    /**
     * Returns a random coordinate with a circle's area
     * @param point The centre point of a circle
     * @param radius The radius of the circle
     * @return A random coordinate within a designated point and radius
     */
    public static Point calculateRandomCoordinates(Point point, int radius) {
        //Calculates a random point within a circle, in this case the area of effect of a societycell
        double random = Math.random();
        int r = (int) (radius * Math.sqrt(random));
        int x = (int) (point.x + (r * Math.cos(random * 2 * 3.1415)));
        int y = (int) (point.y + (r * Math.sin(random * 2 * 3.1415)));
        return new Point(x, y);
    }

    /**
     * Returns true if the provided point does not exceed the simulations bounds, else false.
     * @param point The point to check against the bounds
     * @return A boolean describing if the point is out of bounds
     */
    public static boolean validateBounds(Point point) {
        return point.x >= 0 && point.x < 145 && point.y >= 0 && point.y < 145;
    }


    /**
     * Calculates a random coordinates within a given radius and point, and ensures that the returned coordinates are not out of bounds, uninhabitable or null
     * @param point The central point of the circle
     * @param radius The radius of a circle
     * @param modelStructure The ModelStructure for referencing coordinates CellType
     * @param validCells A list of valid cells to generate on
     * @return A random coordinate with the set bounds
     */
    public static Point calculateValidCoordinates(Point point, int radius, ModelStructure modelStructure, Collection<ECellType> validCells) {
        Point destination;
        do {
            destination = PointUtilities.calculateRandomCoordinates(point, radius);
        } while (!(validateBounds(destination)) || !(validCells.contains(modelStructure.getCoordinate(destination).getECellType())));
        return destination;
    }


    /**
     * This method calculates all points in the area of a circle, determined by the radius and initial coordinate, and tints them according to the tint provided
     * @param radius The radius from the centre to tint
     * @param societyCoordinate The centre of the Society
     * @param tint The RGB colour to tint
     * @param modelStructure The ModelStructure to manipulate
     */
    public static void tintArea(int radius, Point societyCoordinate, int tint, ModelStructure modelStructure) {
        for (int c = 0; c <= 90; c += 5) {
            int circumferenceX = (int) (radius * Math.cos(c * 3.141519 / 180));
            int circumferenceY = (int) (radius * Math.sin(c * 3.141519 / 180));
            //Get the coordinate of the circumference of the circle and the diametric coordinate of the circle, and fills in the cells between the two calculated coordinates
            //TODO: Reject any attempts to generate a society cell if it is within an AOE of another society cell
            for (int circleX = -circumferenceX + societyCoordinate.x; circleX < circumferenceX + societyCoordinate.x; circleX++) {
                for (int circleY = -circumferenceY + societyCoordinate.y; circleY < circumferenceY + societyCoordinate.y; circleY++) {
                    Point circlePoint = new Point(circleX, circleY);
                    if (validateBounds(circlePoint)) {
                        //Checks to make sure the x and y are not out of bounds, the currently selected cell is of type worldcell, it has no owner and that it is habitable
                        if ( modelStructure.getBackLayer(circlePoint).getECellType().isHabitable() && modelStructure.getBackLayer(circlePoint).getOwner() == null) {
                            //set the tint associated with the society cell
                            TextureHelper.setTint(modelStructure.getBackLayer(circlePoint).getTexture(), tint);
                            //Set the owner from the back layer
                            modelStructure.getBackLayer(circlePoint).setOwner(modelStructure.getFrontLayer(societyCoordinate));
                        }
                    }
                }
            }
        }
    }

    public static Point[] getImmediateNeighbours(Point point) {
        return new Point[]{new Point(point.x,point.y - 1), new Point(point.x,point.y + 1), new Point(point.x - 1, point.y), new Point(point.x + 1, point.y)};
    }

    public static Point[] getAllNeighbours(Point point) {
        return ObjectArrays.concat(new Point[]{new Point(point.x-1,point.y - 1),
                                                new Point(point.x + 1,point.y - 1),
                                                new Point(point.x -1 ,point.y + 1),
                                                new Point(point.x + 1,point.y + 1)},
                                   getImmediateNeighbours(point),
                                   Point.class);
    }
}
