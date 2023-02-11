package org.basmat.map.util.path;

import org.basmat.map.controller.CellMatrixController;
import org.basmat.map.view.CellMatrixPanel;
import org.basmat.map.view.CellPanel;

import java.awt.*;
import java.util.*;

public class CCL {
    private int[][] components;
    private HashSet<CellPanel> processedCoordinates;
    private LinkedList<Point> queuedCoordinates;

    private int height;
    private int length;
    private CellMatrixPanel cellMatrixPanel;
    private CellMatrixController cellMatrixController;

    public CCL(int height, int length, CellMatrixPanel cellMatrixPanel, CellMatrixController cellMatrixController) {
        this.components = new int[height][length];
        this.cellMatrixPanel = cellMatrixPanel;
        this.cellMatrixController = cellMatrixController;
        Point start = new Point(0, 0);
        queuedCoordinates = new LinkedList<>();
        processedCoordinates = new HashSet<>();
        queuedCoordinates.add(start);
    }

    /*public void analyzeMap() {
        HashMap<CellPanel, ? super AbstractCell> cellPanelHashMap = cellMatrixController.getMapViewToModel();
        while (!queuedCoordinates.isEmpty()) {
            Point current = queuedCoordinates.remove();
            ECellType parentCellType = ((WorldCell) cellPanelHashMap.get(cellMatrixPanel.getPanel((int) current.getX(), (int) current.getY()))).getCellType().getParent();
            String name = UUID.randomUUID().toString();

            //Calculating directions of all neighbours as coordinates
            getNeighbours(current);

        }
    }*/

    /*private LinkedList<Point> getNeighbours(Point current) {
        int[][] coordinateRef = {{(int) current.getX(), (int) (current.getY() - 1)}, {(int) current.getX(), (int) (current.getY() + 1)}, {(int) (current.getX() - 1), (int) current.getY()}, {(int) (current.getX() + 1), (int) current.getY()},
                {(int) (current.getX() + 1), (int) (current.getY() + 1)}, {(int) (current.getX() + 1), (int) (current.getY() - 1)}, {(int) (current.getX() - 1), (int) (current.getY() + 1)}, {(int) (current.getX() - 1), (int) (current.getY() - 1)}};

        LinkedList<Point> neighbours = new LinkedList<>();

        //Checking for out of bounds, checking if the neighbour is of type worldcell
        // and that the parent of that worldcell matches that of the parent of the current point to match the same terrain type
        // and does not contain the panel of that point in the processed coordinates to prevent duplication
        Arrays.stream(coordinateRef).filter(e -> e[0] < 150
                        && e[1] < 150
                        && e[0] > 0
                        && e[1] > 0
                        && cellMatrixController.getMapViewToModel().get(cellMatrixPanel.getPanel(e[0], e[1])) instanceof WorldCell worldCell
                        && worldCell.getCellType().getParent() == ((WorldCell) (cellMatrixController.getMapViewToModel().get(cellMatrixPanel.getPanel((int) current.getX(), (int) current.getY())))).getCellType().getParent()
                        && !(processedCoordinates.contains(cellMatrixPanel.getPanel(e[0], e[1]))))
                .forEach(e -> neighbours.add(new Point(e[0], e[1])));
        return neighbours;
    }

    public void recurseNeighbours(Point current) {
        for (Point point : getNeighbours(current)) {
            processedCoordinates.add(cellMatrixPanel.getPanel((int) point.getX(), (int) point.getY()));
            recurseNeighbours(point);
        }
    }*/

}
