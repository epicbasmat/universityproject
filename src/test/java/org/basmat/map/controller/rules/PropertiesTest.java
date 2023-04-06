package org.basmat.map.controller.rules;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.factory.CellFactory;
import org.basmat.map.setup.ModelSetup;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.SimulationProperties;
import org.basmat.map.util.TextureHelper;
import org.basmat.map.util.path.Node;
import org.basmat.map.view.UserInteractionUI;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertiesTest {

    private final ModelStructure modelStructure;
    private final CellFactory cellFactory;
    private final HashMap<ECellType, BufferedImage> textureCache;
    private final Winnower winnower;
    private final Gardener gardener;
    private final LinkedList<Point> globalSocietyCellList;
    private final LinkedList<Point> globalLifeCellList;
    private final LinkedList<LinkedList<Node>> listOfPaths;
    private final SimulationProperties simulationProperties;
    private final SimulationProperties simulationProperties1 = new SimulationProperties(7, 100, 1, 20, 5, 75, 0.6);

    PropertiesTest() throws InterruptedException {
        cellFactory = new CellFactory();
        simulationProperties = new SimulationProperties(7, 100, 1, 20, 5, 75, 0.6);
        textureCache = TextureHelper.cacheCellTextures();
        modelStructure = new ModelStructure();
        globalSocietyCellList = new LinkedList<>();
        globalLifeCellList = new LinkedList<>();
        listOfPaths = new LinkedList<>();
        new ModelSetup(simulationProperties1, textureCache, modelStructure, new LinkedList<>(), globalSocietyCellList, globalLifeCellList).setupMap();
        UserInteractionUI userInteractionUi = new UserInteractionUI(null);
        gardener = new Gardener(simulationProperties1, userInteractionUi, modelStructure, globalSocietyCellList, globalLifeCellList, listOfPaths);
        winnower = new Winnower(simulationProperties1, userInteractionUi, modelStructure, globalSocietyCellList, globalLifeCellList, new LinkedList<>());
    }

    @Test
    void simulationSetup_setupCorrectAmountOfSocieties_isEqualToSimProperties() {
        assertEquals(simulationProperties.societyCount(), globalSocietyCellList.size());
    }

    //@Test
    //void simulationSetup_nutrientCount
}