package org.basmat.map.util.path;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.PointUtilities;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;


public class Pathfind {

    /**
     * Returns the heuristic between a point given and a point destination using manhattan distance.
     * @return The heuristic of the node
     */
    private static int h(Point point, Point destination) {
        int x = (int) Math.abs(point.getX() - destination.getX());
        int y = (int) Math.abs(point.getY() - destination.getY());
        return x + y;
    }

    private static int f(int g, int h) {
        return g + h;
    }

    private static int weight(ECellType cellType) {
        return switch (cellType) {
            case GRASS -> 1;
            case LIGHT_WATER, MOUNTAIN_BASE -> 7;
            case SAND -> 2;
            default -> 15;
        };
    }


    /**
     * Returns a boolean
     * @param cellType
     * @return
     */
    public static boolean isInvalid(ECellType cellType) {
        return switch (cellType) {
            case NUTRIENTS, SOCIETY_CELL, LIFE_CELL, MOUNTAIN_PEAK, DEEP_WATER, WATER, MOUNTAIN_BODY -> true;
            default -> false;
        };
    }

    /**
     * A basic implementation of the A Star pathfinding algorithm, which returns a LinkedList from Point Origin to Point Destination if the pathfinding algorithm can find an appropriate path within the maximum allowed iterations.
     * @param allowedIterations The maximum allowed amount of iterations before the pathfinding algorithm is overridden and fails
     * @param modelStructure The ModelStructure that contains all the model data of the system
     * @param origin The origin of the pathfinding algorithm
     * @param destination The destination of the pathfinding algorithm
     * @return A LinkedList containing the path to the destination if there is a valid one, else an empty LinkedList if it failed to get a path within the maximum allowed iterations or the OpenList was exhausted
     */
    public static LinkedList<Node> aStar(int allowedIterations, ModelStructure modelStructure, Point origin, Point destination) {

        PriorityQueue<Node> openList = new PriorityQueue<>();   //Candidates to examine
        LinkedList<Node> closedList = new LinkedList<>(); //Good candidates, passed exam
        openList.add(new Node(origin, h(origin, destination), 0, modelStructure.getCoordinate(origin).getECellType(), null));
        int currentIterations = 0;
        while (!openList.isEmpty() && allowedIterations > currentIterations) {

            //if it ever hits the allowed iterations, which it generally will not, we will just deem the path impossible or not worth the trade-off for compute time.
            currentIterations++;

            Node current = openList.peek();

            if (current.point().equals(destination)) {
                LinkedList<Node> path = new LinkedList<>();
                Node temp = closedList.getLast();
                while (temp.parent() != null) {
                    path.add(temp);
                    temp = temp.parent();
                }
                path.add(temp);
                Collections.reverse(path);
                return path;
            }

            //For each validated neighbours, calculate their h and g score and add them to the open list.
            // If the calculated cost g is less than the current g then the node needs to be re-added to the openlist as there is a better path for that candidate.
            for (Point point : PointUtilities.getImmediateValidatedNeighbours(current.point())) {
                /*
                f(n) = g(n) + h(n)
                n is the current selected node
                g(n) The cost from the start node to n
                h(n) Estimation of the cost from n to goal. this is the heuristic
                */

                if (point.equals(destination)) {
                    openList.add(new Node(destination, 0, 0, modelStructure.getCoordinate(destination).getECellType(), current));
                    break;
                }

                if (!isInvalid(modelStructure.getCoordinate(point).getECellType())) {
                    ECellType candidate  = modelStructure.getCoordinate(point).getECellType();
                    int nextCost = current.g() + weight(candidate);
                    Node neighbour = new Node(point, nextCost + h(point, destination), nextCost, candidate, current);
                    if (!openList.contains(neighbour) && !closedList.contains(neighbour)) {
                        openList.add(neighbour);
                    } else {
                        if (nextCost < current.g()) {
                            if (closedList.contains(neighbour)) {
                                closedList.remove(neighbour);
                                openList.add(neighbour);
                            }
                        }
                    }
                }
            }
            closedList.add(current);
            openList.remove(current);
        }
        return new LinkedList<>();
    }
}
