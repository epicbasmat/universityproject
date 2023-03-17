package org.basmat.map.util.path;

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
    g(n) is the cost of the path from the current node to child node n
    h(n) is the heuristic function from n to goal
    */

    /*public static LinkedList<Node> aStar(int allowedIterations, ViewStructure viewStructure, HashMap<Integer, MVBinder<?>> mapIdToMvBinder, Point origin, Point destination) {
        //Cost of moving through
        int sand = 200;
        int water = 700;
        int grass = 100;
        PriorityQueue<Node> openList = new PriorityQueue<>(new HeuristicComparator());   //Candidates to examine
        LinkedList<Node> closedList = new LinkedList<>(); //Good candidates, passed exam
        openList.add(new Node(origin, h(origin, destination)));
        int currentIterations = 0;
        while (!openList.isEmpty() && allowedIterations > currentIterations) {
            currentIterations++;

            Node current = openList.remove();
            closedList.add(current);

            if (current.getPoint().equals(destination)) {
                return closedList;
            }

            int[][] coordinateRef = {{(int) current.getPoint().getX(), (int) (current.getPoint().getY() - 1)}, {(int) current.getPoint().getX(), (int) (current.getPoint().getY() + 1)}, {(int) (current.getPoint().getX() - 1), (int) current.getPoint().getY()}, {(int) (current.getPoint().getX() + 1), (int) current.getPoint().getY()}};

            List<Node> neighbours = Arrays.stream(coordinateRef).filter(e -> e[0] < 150
                    && e[1] < 150
                    && e[0] > 0
                    && e[1] > 0).map(coordinate -> { Point p = new Point(coordinate[0], coordinate[1]);
                                                      Node n = switch (mapIdToMvBinder.get(viewStructure.getPanel(coordinate[0], coordinate[1]).getId()).model().getECellType()) {
                                                          case LIGHT_WATER -> new Node(p, h(p, destination) + water);
                                                          case SAND -> new Node(p, h(p, destination) + sand);
                                                          case GRASS -> new Node(p, h(p, destination) + grass);
                                                          default -> new Node(p, h(p, destination) + 500);
                                                      };
                                                      if (p.equals(destination)) {return new Node(destination, 0);} else { return n ;}
                                                    }).toList();

            for (Node n : neighbours) {
                if (!closedList.contains(n) && !(openList.contains(n))) {
                    if (n.getF() < current.getF()) {
                        openList.add(n);
                    }
                }
            }
        }
        return null;
    }*/
}
 class HeuristicComparator implements Comparator<Node> {
     @Override
     public int compare(Node toBeCompared, Node alreadyIn) {
         return Integer.compare(toBeCompared.getF(), alreadyIn.getF());
     }
 }