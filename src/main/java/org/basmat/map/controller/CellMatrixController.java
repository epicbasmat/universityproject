package org.basmat.map.controller;


import org.basmat.map.view.CellPanel;

import java.awt.*;
import java.util.HashMap;

public class CellMatrixController {

    //This HashMap binds the model to the view
    private HashMap<?, Point> mvBinder;

    public CellMatrixController(int cellMatrixWidth, int cellMatrixHeight) {

    }

    /*public HashMap<CellPanel, ? super Cell> getMapViewToModel() {
        return this.mapViewToModel;
    }

    /**
     * Provides a temporary method for viewing data requested by the cell matrix view.
     * @param e the MouseEvent that the cell captures
     */
    /*public void displayData(MouseEvent e) {
        //Weird subtractions are necessary to align click co-ordinate with cell matrix co-ordinate
        int x = (int) e.getPoint().getX() / 5 - 27;
        int y = (int) e.getPoint().getY() / 5 - 29;
        System.out.println("==");
        System.out.println(x + ", " + y);
        System.out.println(mapViewToModel.get(cellMatrixPanel.getPanel(x, y)).toString());
    }*/
}

