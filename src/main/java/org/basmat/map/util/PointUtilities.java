package org.basmat.map.util;

import com.google.common.collect.ObjectArrays;
import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.NutrientCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.model.cells.WorldCell;
import org.basmat.map.model.cells.factory.CellFactory;
import org.basmat.map.model.cells.factory.IOwnedCell;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;

/**
 * PointUtilities contains static methods to help Point calculations, such as getting neighbours in taxicab geometry, or calculating all points within a circle.
 */
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
        return point.x >= 0 && point.x < 150 && point.y >= 0 && point.y < 150;
    }


    /**
     * Calculates a random coordinates within a given radius and point, and ensures that the returned coordinates are not out of bounds, uninhabitable or null
     * @param point The central point of the circle
     * @param radius The radius of a circle
     * @param modelStructure The ModelStructure for referencing coordinates CellType
     * @param validCells A list of valid cells to generate on
     * @return A random coordinate with the set bounds
     */
    public static Point calculateRandomValidCoordinates(Point point, int radius, ModelStructure modelStructure, Collection<ECellType> validCells) {
        Point destination;
        int breakcnd = 0;
        do {
            destination = PointUtilities.calculateRandomCoordinates(point, radius);
            breakcnd++;
        } while ((!(validateBounds(destination)) || !(validCells.contains(modelStructure.getCoordinate(destination).getECellType()))) && breakcnd < 8);
        return destination;
    }


    /**
     * This method calculates all points in the area of a circle, determined by the radius and initial coordinate, and tints them according to the tint provided
     * @param radius The radius from the centre to tint
     * @param centralCoordinate The centre of the circle
     * @param tint The RGB colour to tint
     * @param modelStructure The ModelStructure to manipulate
     */
    public static void tintArea(int radius, Point centralCoordinate, int tint, ModelStructure modelStructure) {
        HashSet<ECellType> illegalCellTypes = new HashSet<>();
        illegalCellTypes.add(ECellType.DEEP_WATER);
        illegalCellTypes.add(ECellType.MOUNTAIN_BODY);
        illegalCellTypes.add(ECellType.MOUNTAIN_PEAK);
        illegalCellTypes.add(ECellType.WATER);
        forPointsInCircle(radius, centralCoordinate, (point) -> {
            WorldCell worldCell = modelStructure.getBackLayer(point);
            //For the points in the circle, make sure they're both not owned and an illegal cell type and apply ownership & tinting
            if (!illegalCellTypes.contains(worldCell.getECellType()) && worldCell.getOwner() == null) {
                BufferedImage texture = modelStructure.getBackLayer(point).getTexture();
                for (int x = 0; x < texture.getWidth(); x++) {
                    for (int y = 0; y < texture.getHeight(); y++) {
                        texture.setRGB(x, y, texture.getRGB(x, y) | tint);
                    }
                }
                worldCell.setOwner(modelStructure.getCoordinate(centralCoordinate));
                if (modelStructure.getCoordinate(point) instanceof NutrientCell nutrientCell) {
                    //Add the nutrient cell to the expanding society & set it as nutrient's owner
                    ((SocietyCell) modelStructure.getCoordinate(centralCoordinate)).addNutrientCells(nutrientCell);
                    nutrientCell.setOwner(modelStructure.getCoordinate(centralCoordinate));
                }
            }
        });
    }

    /**
     * This method resets an area in a circle, removing all tints and any ownership of a SocietyCell.
     * @param radius The radius of the circle
     * @param centralCoordinate The central coordinate of the point, usually a societycell
     * @param modelStructure The current ModelStructure containing all simulation data.
     */
    public static void resetArea(int radius, Point centralCoordinate, ModelStructure modelStructure) {
        CellFactory cellFactory = new CellFactory();
        SocietyCell coordinate = modelStructure.getCoordinate(centralCoordinate);
        forPointsInCircle(radius, centralCoordinate, (point) -> {
            if (modelStructure.getCoordinate(point) instanceof WorldCell) {
                //Reset the world cell by getting the cell type and creating a new object with the same cell type and set it to the same coordinates.
                //As the tint is applied with an OR operator, it cannot be reversed as OR operations are destructive
                //So we need to reset the texture by re-applying it from the cache, which means making a new object.
                ECellType celltype = modelStructure.getBackLayer(point).getECellType();
                modelStructure.deleteBackLayer(point);
                modelStructure.setBackLayer(point, cellFactory.createWorldCell(celltype));
            } else if (modelStructure.getCoordinate(point) instanceof NutrientCell nutrientCell) {
                nutrientCell.setOwner(null);
            }
        });
    }

    /**
     * This calculates the circumference of a circle and all points in the circle,
     * and for each point in the circle, executes an action defined by functional interface <code> Action </code>
     * @see Action
     * @param radius The radius of the circle
     * @param centralCoordinate The central coordinate of the circle
     * @param action An action to execute for each point in the circle
     */
    public static void forPointsInCircle(int radius, Point centralCoordinate, Action<Point> action) {
        for (Map.Entry<Point, Point> kvPair : PointUtilities.midpointAlgorithm(radius, centralCoordinate)) {
            for (int i = kvPair.getKey().x; i < kvPair.getValue().x; i++) {
                Point point = new Point(i, kvPair.getKey().y);
                if (validateBounds(point)) {
                    action.action(point);
                }
            }
        }
    }

    /**
     * An implementation of the midpoint algorithm to calculate the circumference of a circle through mirroring quadrants,
     * and for each quadrant there exists a map mapping one side of the circle to another.
     * @param radius The radius of a circle
     * @param centralCoordinate The central coordinate of a circle
     * @return A comprehensive list containing maps, mapping all left hand side of the circumference to the right hand of the circle.
     */
    public static List<Map.Entry<Point, Point>> midpointAlgorithm(int radius, Point centralCoordinate) {
        int x = radius, y = 0;
        List<Map.Entry<Point,Point>> pairList = new java.util.ArrayList<>();
        int p = 1 - radius;
        while (x > y) {
            y++;
            if (p <= 0) {
                p = p + 2 * y + 1;
            } else {
                x--;
                p = p + 2 * y - 2 * x + 1;
            }
            if (x < y) {
                break;
            }
            /*This maps the left hand side of the circle to the right side of the circle like so by reflecting the calculated quadrant
                (-y,o+x) x---------x (o+y,o+x)
            (o-x,o+y) x-----------------x (o+x,o+y)
                               o
            (o-x,o-y) x-----------------x (o+x,o-y)
                (o-y,o-x) x---------x (o+y,o-x)
             Credit to user Shahbaz on stack overflow for the ascii diagram
             */
            pairList.add(new AbstractMap.SimpleEntry<>(new Point(-y + centralCoordinate.x, x + centralCoordinate.y), new Point(y + centralCoordinate.x, x + centralCoordinate.y)));
            pairList.add(new AbstractMap.SimpleEntry<>(new Point(-x + centralCoordinate.x, y + centralCoordinate.y), new Point(x + centralCoordinate.x, y + centralCoordinate.y)));
            pairList.add(new AbstractMap.SimpleEntry<>(new Point(-x + centralCoordinate.x, -y + centralCoordinate.y), new Point(x + centralCoordinate.x, -y + centralCoordinate.y)));
            pairList.add(new AbstractMap.SimpleEntry<>(new Point(-y + centralCoordinate.x, -x + centralCoordinate.y), new Point(y + centralCoordinate.x, -x + centralCoordinate.y)));
        }
        pairList.add(new AbstractMap.SimpleEntry<>(new Point(centralCoordinate.x - radius - 1, centralCoordinate.y), new Point(centralCoordinate.x + radius + 1, centralCoordinate.y)));

        return pairList;
    }

    private static Point[] getImmediateNeighbours(Point point) {
        return new Point[]{new Point(point.x,point.y - 1), new Point(point.x,point.y + 1), new Point(point.x - 1, point.y), new Point(point.x + 1, point.y)};
    }

    private static Point[] getAllNeighbours(Point point) {
        return ObjectArrays.concat(new Point[]{new Point(point.x-1,point.y - 1),
                                                new Point(point.x + 1,point.y - 1),
                                                new Point(point.x -1 ,point.y + 1),
                                                new Point(point.x + 1,point.y + 1)},
                                   getImmediateNeighbours(point),
                                   Point.class);
    }

    public static List<Point> getAllValidatedNeighbours(Point point) {
        return Arrays.stream(getAllNeighbours(point)).filter(PointUtilities::validateBounds).toList();
    }

    public static List<Point> getImmediateValidatedNeighbours(Point point) {
        return Arrays.stream(getImmediateNeighbours(point)).filter(PointUtilities::validateBounds).toList();
    }
}
