package org.basmat.map.util.path;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.util.ECellType;
import org.basmat.map.view.ViewStructure;

import java.awt.*;
import java.util.List;
import java.util.*;


public class Pathfind {

    /**
     * Returns the amount of expected units until destination using taxicab geometry.
     * @return The heuristic of the node
     */
    private static int h(Point point, Point destination) {
        int x = (int) Math.abs(destination.getX() - point.getX());
        int y = (int) Math.abs(destination.getY() - point.getY());
        return (int) (Math.pow(x, 2) + Math.pow(y, 2));
    }

    /*
    Heuristic - f(n) = g(n) + h(n)
    n is the current selected node
    g(n) is the cost of the path from the current node to child node n. Certain cells have higher g(n) requirements than other cells.
    h(n) is the heuristic function from n to goal
    */

    /**
     * A basic implementation of the A Star pathfinding algorithm, which returns a LinkedList from Point Origin to Point Destination if the pathfinding algorithm can find an appropriate path within the maximum allowed iterations.
     * @param allowedIterations The maximum allowed amount of iterations before the pathfinding algorithm is overriden and fails
     * @param modelStructure The ModelStructure that contains all the model data of the system
     * @param origin The origin of the pathfinding algorithm
     * @param destination The destination of the pathfinding algorithm
     * @return A LinkedList containing the path to the destination if there is a valid one, else an empty LinkedList if it failed to get a path within the maximum allowed iterations or the OpenList was exhausted
     */
    public static LinkedList<Node> aStar(int allowedIterations, ModelStructure modelStructure, Point origin, Point destination) {
        //Cost of moving through
        int sand = 200;
        int water = 700;
        int grass = 100;
        PriorityQueue<Node> openList = new PriorityQueue<>(new HeuristicComparator());   //Candidates to examine
        LinkedList<Node> closedList = new LinkedList<>(); //Good candidates, passed exam
        openList.add(new Node(origin, h(origin, destination) + 100000, modelStructure.getCoordinate(origin).getECellType()));
        int currentIterations = 0;
        while (!openList.isEmpty() && allowedIterations > currentIterations) {
            currentIterations++;

            Node current = openList.remove();
            closedList.add(current);

            if (current.point().equals(destination)) {
                return closedList;
            }

            int[][] coordinateRef = {{(int) current.point().getX(), (int) (current.point().getY() - 1)}, {(int) current.point().getX(), (int) (current.point().getY() + 1)}, {(int) (current.point().getX() - 1), (int) current.point().getY()}, {(int) (current.point().getX() + 1), (int) current.point().getY()}};

            //Neighbours need to be filtered if they are to be examined, such as ensuring they are in range of the overall matrix
            //TODO: Release 150 from its hardcoded hell
            List<Node> neighbours = Arrays.stream(coordinateRef).filter(e -> e[0] < 145
                    && e[1] < 145
                    && e[0] > 0
                    && e[1] > 0).map(coordinate -> { Point p = new Point(coordinate[0], coordinate[1]);
                                                      Node n = switch (modelStructure.getCoordinate(p).getECellType()) {
                                                          case LIGHT_WATER -> new Node(p, h(p, destination) + water, ECellType.LIGHT_WATER);
                                                          case SAND -> new Node(p, h(p, destination) + sand, ECellType.SAND);
                                                          case GRASS -> new Node(p, h(p, destination) + grass, ECellType.GRASS);
                                                          //TODO: create case where it cannot go through these cells
                                                          case LIFE_CELL -> new Node(p, h(p, destination) + 150000, ECellType.LIFE_CELL);
                                                          case SOCIETY_CELL -> new Node(p, h(p, destination) + 150000, ECellType.SOCIETY_CELL);
                                                          default -> new Node(p, h(p, destination) + 500, modelStructure.getCoordinate(p).getECellType());
                                                      };
                                                      if (p.equals(destination)) {return new Node(destination, 0, modelStructure.getCoordinate(destination).getECellType());} else { return n ;}
                                                    }).toList();

            for (Node n : neighbours) {
                if (!closedList.contains(n) && !(openList.contains(n))) {
                    if (n.heuristic() < current.heuristic()) {
                        openList.add(n);
                    }
                }
            }
        }
        return new LinkedList<>();
    }
}
 class HeuristicComparator implements Comparator<Node> {
     @Override
     public int compare(Node toBeCompared, Node alreadyIn) {
         return Integer.compare(toBeCompared.heuristic(), alreadyIn.heuristic());
     }
 }