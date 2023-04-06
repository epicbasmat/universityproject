package org.basmat.map.controller;


import org.basmat.map.controller.rules.RuleApplier;
import org.basmat.map.model.ModelStructure;
import org.basmat.map.setup.ModelSetup;
import org.basmat.map.setup.ViewSetup;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.SimulationProperties;
import org.basmat.map.util.TextureHelper;
import org.basmat.map.view.MenuUI;
import org.basmat.map.view.UserInteractionUI;
import org.basmat.map.view.ViewStructure;
import org.basmat.userui.PanelContainer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;

public class CellMatrixController {


    private final HashMap<ECellType, BufferedImage> imageCache;
    private final ViewStructure viewStructure;
    private final ModelStructure modelStructure;
    private final PanelContainer panelContainer;
    private RuleApplier ruleApplier;
    private final UserInteractionUI userInteractionUi;
    private final MenuUI menuUi;
    private LinkedList<Point> globalSocietyCellList;
    private LinkedList<Point> globalNutrientCellList;
    private LinkedList<Point> globalLifeCellList;


    public CellMatrixController(int cellMatrixWidth, int cellMatrixHeight) throws InterruptedException {
        globalSocietyCellList = new LinkedList<>();
        globalLifeCellList = new LinkedList<>();
        modelStructure = new ModelStructure();
        imageCache = TextureHelper.cacheCellTextures();
        viewStructure = new ViewStructure(cellMatrixWidth, cellMatrixHeight, this);
        userInteractionUi = new UserInteractionUI(this);
        menuUi = new MenuUI(this);
        panelContainer = new PanelContainer(viewStructure, userInteractionUi, menuUi);
    }

    public void doThing(){
        ruleApplier.invokeGardener();
    }

    public void constructSimulation(SimulationProperties simulationProperties) {
        ModelSetup modelSetup = new ModelSetup(simulationProperties, imageCache, modelStructure, globalNutrientCellList,  globalSocietyCellList, globalLifeCellList);
        ruleApplier = new RuleApplier(simulationProperties, userInteractionUi, viewStructure, modelStructure, globalSocietyCellList, globalLifeCellList);
        try {
            modelSetup.setupMap();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ViewSetup.setupView(viewStructure, modelStructure);
        panelContainer.nextCard();
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
        userInteractionUi.appendText(modelStructure.getCoordinate(e).toString() + "\n");
    }
}