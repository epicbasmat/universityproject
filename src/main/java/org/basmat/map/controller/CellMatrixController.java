package org.basmat.map.controller;


import org.basmat.map.cellfactory.NutrientCell;
import org.basmat.map.cellfactory.SocietyCell;
import org.basmat.map.cellfactory.WorldCell;
import org.basmat.map.view.CellPanel;

import javax.sound.sampled.Line;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class CellMatrixController {


    //Binds a unique id to an instance of MVBinder
    private HashMap<Integer, MVBinder<?>> bindingAgent;
    private LinkedList<MVBinder<SocietyCell>> globalSocietyCellList;
    private LinkedList<MVBinder<NutrientCell>> globalNutrientCellList;

    public CellMatrixController(int cellMatrixWidth, int cellMatrixHeight) {
        bindingAgent = new HashMap<>();
        globalSocietyCellList = new LinkedList<>();
        globalNutrientCellList = new LinkedList<>();
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

