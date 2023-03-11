package org.basmat.map.util.path;

import org.basmat.map.cellfactory.cells.WorldCell;
import org.basmat.map.controller.CellMatrixController;
import org.basmat.map.controller.MVBinder;
import org.basmat.map.util.ECellType;
import org.basmat.map.view.CellMatrixPanel;

import java.awt.*;
import java.util.*;

public class Pathfind {

    private LinkedList<Point> closedList;
    private PriorityQueue<Node> openList;

    private CellMatrixPanel cellMatrixPanel;
    private HashMap<Integer, MVBinder<?>> mapIdToMvBinder;
    private CellMatrixController cellMatrixController;
    private LinkedList<Integer> globalNutrientCellList;
    private Point origin;
    private Point destination;

    public Pathfind(CellMatrixPanel cellMatrixPanel, HashMap<Integer, MVBinder<?>> mapIdToMvBinder, LinkedList<Integer> globalNutrientCellList, Point origin, Point destination) {
        this.cellMatrixPanel = cellMatrixPanel;
        this.mapIdToMvBinder = mapIdToMvBinder;
        this.globalNutrientCellList = globalNutrientCellList;
        this.origin = mapIdToMvBinder.get(globalNutrientCellList.get((int) (Math.random() * 20))).point();
        this.destination = mapIdToMvBinder.get(globalNutrientCellList.get((int) (Math.random() * 20))).point();
        closedList = new LinkedList<>(); //Nodes that contain places that have been examined
    }

    /**
     * Returns the amount of expected units until destination using taxicab geometry.
     * @return The heuristic of the node
     */
    public int h(Point point) {
        int x = (int) Math.abs(destination.getX() - point.getX());
        int y = (int) Math.abs(destination.getY() - point.getY());
        return x + y;
    }

    /*
    Heuristic - f(n) = g(n) + h(n)
    n is the current selected node
    g(n) is the cost of the path from the start node to n
    h(n) is the heuristic function from n to goal
    */

    public LinkedList<Node> aStar() {
        PriorityQueue<Node> openList = new PriorityQueue<>(new HeuristicComparator());   //Candidates
        LinkedList<Node> closedList = new LinkedList<>(); //Path
        openList.add(new Node(this.origin, h(origin)));
        while (!openList.isEmpty()) {
            Node current = openList.remove();
            closedList.add(current);
            if (current.getPoint().equals(destination)) {
                return closedList;
            }

            int[][] coordinateRef = {{(int) current.getPoint().getX(), (int) (current.getPoint().getY() - 1)}, {(int) current.getPoint().getX(), (int) (current.getPoint().getY() + 1)}, {(int) (current.getPoint().getX() - 1), (int) current.getPoint().getY()}, {(int) (current.getPoint().getX() + 1), (int) current.getPoint().getY()},
                    {(int) (current.getPoint().getX() + 1), (int) (current.getPoint().getY() + 1)}, {(int) (current.getPoint().getX() + 1), (int) (current.getPoint().getY() - 1)}, {(int) (current.getPoint().getX() - 1), (int) (current.getPoint().getY() + 1)}, {(int) (current.getPoint().getX() - 1), (int) (current.getPoint().getY() - 1)}};

            LinkedList<Point> neighbours = new LinkedList<>();

            Arrays.stream(coordinateRef).filter(e -> e[0] < 150
                            && e[1] < 150
                            && e[0] > 0
                            && e[1] > 0
                            && mapIdToMvBinder.get(cellMatrixPanel.getPanel(e[0], e[1]).getId()).model().getECellType().isHabitable() || mapIdToMvBinder.get(cellMatrixPanel.getPanel(e[0], e[1]).getId()).model().getECellType() == ECellType.NUTRIENTS)
                    .forEach(f -> neighbours.add(new Point(f[0], f[1])));

            for (Point point : neighbours) {
                if (h(point) < current.getF()) {
                    Node betterNode = new Node(point, h(point));
                    if (!closedList.contains(betterNode)) {
                        openList.add(betterNode);
                    }
                }
            }
        }
        return null;
    }
}
 class HeuristicComparator implements Comparator<Node> {
     @Override
     public int compare(Node toBeCompared, Node alreadyIn) {
         return Integer.compare(toBeCompared.getF(), alreadyIn.getF());
     }
 }