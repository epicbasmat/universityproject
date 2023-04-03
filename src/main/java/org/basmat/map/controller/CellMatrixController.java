package org.basmat.map.controller;


import org.basmat.map.controller.rules.RuleApplier;
import org.basmat.map.model.ModelStructure;
import org.basmat.map.setup.ModelSetup;
import org.basmat.map.setup.ViewSetup;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.TextureHelper;
import org.basmat.map.view.UI;
import org.basmat.map.view.ViewStructure;
import org.basmat.userui.PanelContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;

public class CellMatrixController {


    private final HashMap<ECellType, BufferedImage> imageCache;
    private final ViewStructure viewStructure;
    private final ModelStructure modelStructure;
    private final RuleApplier ruleApplier;
    private final UI ui;
    private LinkedList<Point> globalSocietyCellList;
    private LinkedList<Point> globalNutrientCellList;
    private LinkedList<Point> globalLifeCellList;


    public CellMatrixController(int cellMatrixWidth, int cellMatrixHeight) throws InterruptedException {
        globalSocietyCellList = new LinkedList<>();
        globalLifeCellList = new LinkedList<>();
        modelStructure = new ModelStructure();
        imageCache = TextureHelper.cacheCellTextures();
        viewStructure = new ViewStructure(cellMatrixWidth, cellMatrixHeight, this);
        ui = new UI(this);
        ruleApplier = new RuleApplier(ui, viewStructure, modelStructure, globalSocietyCellList, globalLifeCellList);
        ModelSetup modelSetup = new ModelSetup(imageCache, modelStructure, globalNutrientCellList,  globalSocietyCellList, globalLifeCellList);
        PanelContainer panelContainer = new PanelContainer(viewStructure, ui);
        modelSetup.setupMap();
        ViewSetup.setupView(viewStructure, modelStructure);
    }

    public void doThing(){
        ruleApplier.invokeGardener();
    }



    public LinkedList<Point> getGlobalSocietyCellList() {
        return globalSocietyCellList;
    }

    public LinkedList<Point> getGlobalNutrientCellList() {
        return globalNutrientCellList;
    }

    public LinkedList<Point> getGlobalLifeCellList() {
        return globalLifeCellList;
    }

    /**
     * Provides a temporary method for viewing data requested by the cell matrix view.
     * @param e the MouseEvent that the cell captures
     */
    public void displayData(Point e) {
        //Weird subtractions are necessary to align click co-ordinate with cell matrix co-ordinate
        ui.appendText(modelStructure.getCoordinate(e).toString() + "\n");
    }
}