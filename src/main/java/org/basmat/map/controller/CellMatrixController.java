package org.basmat.map.controller;


import org.basmat.map.cellfactory.NutrientCell;
import org.basmat.map.cellfactory.SocietyCell;
import org.basmat.map.data.CellDataHelper;
import org.basmat.map.setup.MapSetup;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.TextureRefGen;
import org.basmat.map.view.CellMatrixPanel;
import org.basmat.userui.PanelContainer;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;

public class CellMatrixController {


    private final HashMap<ECellType, BufferedImage> imageCache;
    //Binds a unique id to an instance of MVBinder
    private HashMap<Integer, MVBinder<?>> bindingAgent;
    private LinkedList<MVBinder<SocietyCell>> globalSocietyCellList;
    private LinkedList<MVBinder<NutrientCell>> globalNutrientCellList;


    public CellMatrixController(int cellMatrixWidth, int cellMatrixHeight) throws InterruptedException {
        bindingAgent = new HashMap<>();
        globalSocietyCellList = new LinkedList<>();
        globalNutrientCellList = new LinkedList<>();
        imageCache = TextureRefGen.cacheCellTextures(new HashMap<>());
        CellMatrixPanel cellMatrixPanel = new CellMatrixPanel(150, 150, this);
        CellDataHelper cellDataHelper = new CellDataHelper(cellMatrixPanel, imageCache);
        bindingAgent = new HashMap<>();
        MapSetup setup = new MapSetup(imageCache, cellDataHelper, bindingAgent, cellMatrixPanel);
        PanelContainer panelContainer = new PanelContainer(cellMatrixPanel);
        setup.setupMap();
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

