package org.basmat.cell.util.path;

import org.basmat.cell.controller.CellMatrixController;
import org.basmat.cell.data.WorldCell;
import org.basmat.cell.view.CellMatrixPanel;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Pathfind {

    /*
    Heuristic - f(n) = g(n) + h(n)
    n is the current selected node
    g(n) is the cost of the path from the start node to n
    h(n) is the heuristic function from n to goal
     */

    private LinkedList<Point> closedList;
    private PriorityQueue<Node> openList;

    private CellMatrixPanel cellMatrixPanel;
    private CellMatrixController cellMatrixController;
    private Point origin;
    private Point destination;

    public Pathfind(CellMatrixPanel cellMatrixPanel, CellMatrixController cellMatrixController, Point origin, Point destination) {
        this.cellMatrixPanel = cellMatrixPanel;
        this.cellMatrixController = cellMatrixController;
        this.origin = origin;
        this.destination = destination;
        closedList = new LinkedList<>(); //Nodes that contain places that have been examined
        openList = new PriorityQueue<>(new HeuristicComparator()); //Nodes that are candidates for examining
        openList.add(new Node(origin, 0));
    }

    /**
     * Manhattan distance, aka taxicab geometry
     * @return The heuristic of the node
     */
    public int heuristic(Point point) {
        int dx = (int) (point.getX() - destination.getX());
        int dy = (int) (point.getY() - destination.getY());
        // 1  is the amount of grids to search
        return 1 * (dx + dy);
    }

    public LinkedList<Point> aStarPathFind() {
        int x = 0;
        while (x < 10000) {
            //Get the current object to check
            Point current = openList.remove().getPoint();
            closedList.add(current);

            //Calculate the current cost of the objects xy
            int currentCost = heuristic(current);

            //The coordinates surrounding the current cellPanel
            int[][] coordinateRef = { {(int) current.getX(), (int) (current.getY() - 1)}, {(int) current.getX(), (int) (current.getY() + 1)}, {(int) (current.getX() - 1), (int) current.getY()}, {(int) (current.getX() + 1), (int) current.getY()},
                    {(int) (current.getX() + 1), (int) (current.getY() + 1)}, {(int) (current.getX() + 1), (int) (current.getY() - 1)}, {(int) (current.getX() - 1), (int) (current.getY() + 1)}, {(int) (current.getX() - 1), (int) (current.getY() - 1)} };

            LinkedList<Point> neighbours = new LinkedList<>();

            //Check the neighbours to make sure they do not go outside the matrix and are of type worldcell
            Arrays.stream(coordinateRef).filter(e -> e[0] < 150
                    && e[1] < 150
                    && e[0] > 0
                    && e[1] > 0
                    && (cellMatrixController.getMapViewToModel().get(cellMatrixPanel.getPanel(e[0], e[1])) instanceof WorldCell worldCell
                    && worldCell.getCellType().isHabitable()))
                .forEach(e -> neighbours.add(new Point(e[0], e[1])));

            //Generate a heuristic for each neighbour and see if that's any good
            for(Point neighbour : neighbours) {
                int cost = heuristic(neighbour);
                if (currentCost < cost) {
                    if (openList.stream().noneMatch(e -> e.getPoint().getX() + e.getPoint().getY() == neighbour.getX() + neighbour.getY())) {
                        openList.add(new Node(new Point((int) neighbour.getX(), (int) neighbour.getY()), cost));
                    }
                }
            }x++;
        } return this.closedList;
    }
}
 class HeuristicComparator implements Comparator<Node> {
     @Override
     public int compare(Node o1, Node o2) {
         return o2.getF() - o1.getF();
     }
 }