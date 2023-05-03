package org.basmat.map.setup;

import org.basmat.map.controller.Controller;
import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.NutrientCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.model.cells.factory.CellFactory;
import org.basmat.map.util.CubicInterpolation;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.PointUtilities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * MapSetup generates the initial state of the map, and stores it in a ModelStructure.
 * The initial terrain is stored in the WorldCell matrix and should be immutable after being set, where the IOwnedCell matrix contains mutable data which is allowed to be changed.
 */

/*
 If you followed the umbilical of history in search of some ultimate atavistic embryo that became them, then your journey would end here, in this garden.
 */
public class ModelSetup {
    private final Controller controller;
    private BufferedImage noiseGraph;
    private final LinkedList<Point> globalSocietyCellList;
    private final ArrayList<Point> globalLifeCellList;
    private final ModelStructure modelStructure;
    private final CellFactory cellFactory;


    /**
     * @param modelStructure The ModelStructure containing all the model data within 2 matrices.
     * @param globalSocietyCellList The list of which all society cells have a reference within
     * @param globalLifeCellList The list of which all life cells have a reference within
     */
    public ModelSetup(Controller controller, ModelStructure modelStructure, LinkedList<Point> globalSocietyCellList, ArrayList<Point> globalLifeCellList) {
        this.controller = controller;
        this.cellFactory = new CellFactory();
        this.modelStructure = modelStructure;
        this.globalSocietyCellList = globalSocietyCellList;
        this.globalLifeCellList = globalLifeCellList;
        noiseGraph = new BufferedImage(750, 750, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Calls methods appropriate for rendering and setup for the matrix
     * @throws InterruptedException
     */
    public void setupMap() throws InterruptedException {
        BufferedImage graph = new BufferedImage(750, 750, BufferedImage.TYPE_INT_ARGB);
        controller.pushText("Generating gradient graph");
        setupGradientGraph(-1);
        controller.pushText("Applying texture filter");
        setupWorldCells(5);
        controller.pushText("Applying society cells and rendering area of effect");
        setupSocietyCells();
        controller.pushText("Applying nutrient cells");
        setupNutrientCells();
        controller.pushText("Appyling life cells to society cells");
        setupLifeCells();
        controller.pushText("Complete");
    }

    /**
     * This method provides the setup for the noise graph by applying an arbitrary algorithm to a supplied buffered image.
     * @param seed the seed to provide the noise generator
     */
    private void setupGradientGraph(int seed) {
        CubicInterpolation ci = new CubicInterpolation((int) (Math.random() * (10000000 - 1 + 1) + 1));
        controller.setSeed(ci.getSeed());
        //For X and Y coordinate in the bufferedimage, apply a noise gradient filter to the x,y coords and set an RGB value applied to the coordinates
        for (int i = 0; i < noiseGraph.getWidth(); i++) {
            for (int j = 0; j < noiseGraph.getHeight(); j++) {
                double noise = ci.noiseGenerator(i, j, 110);
                // times by 200 to easily separate values when determining RGB values
                int alphaValue = (int) Math.floor(noise * 200);
                if (alphaValue <= -220) { //Deep water
                    noiseGraph.setRGB(i, j, new Color(0, 0, 100).getRGB());
                }
                if (alphaValue > -220 && alphaValue < -130) { //Water
                    noiseGraph.setRGB(i, j, new Color(0, 0, 190).getRGB());
                }
                if (alphaValue >= -130 && alphaValue < -60) {//Light water
                    noiseGraph.setRGB(i, j, new Color(0, 0, 255).getRGB());
                }
                if (alphaValue >= -60 && alphaValue < -35) { //Beach
                    noiseGraph.setRGB(i, j, new Color(255, 0, 0).getRGB());
                }
                if (alphaValue >= -35 & alphaValue < 130) { //Land
                    noiseGraph.setRGB(i, j, new Color(0, 250, 0).getRGB());
                }
                if (alphaValue >= 130 && alphaValue < 160) { //Base mountain
                    noiseGraph.setRGB(i, j, new Color(0, 200, 0).getRGB());
                }
                if (alphaValue >= 160 && alphaValue < 220) { //Mountain body
                    noiseGraph.setRGB(i, j, new Color(0, 150, 0).getRGB());
                }
                if (alphaValue >= 220) { //Mountain peak
                    noiseGraph.setRGB(i, j, new Color(0, 100, 0).getRGB());
                }
            }
        }
    }

    /**
     * Sets up the world cell generation and maps an RGB range to a cell type and texture.
     * @param cellSize The cell size to provide
     */
    private void setupWorldCells(int cellSize) {
        for (int x = 0; x <= 150 - 1; x++) {
            for (int y = 0; y <= 150 - 1; y++) {
                int average = 0;
                //Calculate average RGB value of a 5x5 grid
                for (int i = x * cellSize; i < x * cellSize + 5; i++) {
                    for (int j = y * cellSize; j < y * cellSize + 5; j++) {
                        average += noiseGraph.getRGB(i, j);
                    }
                }
                Point point = new Point(x, y);
                //Sets the texture according to the averaged colour's RGB bands
                Color averagedColour = new Color(average / (cellSize * cellSize));
                if ((averagedColour.getBlue() > 0 && averagedColour.getBlue() < 160) && averagedColour.getGreen() < 10 && averagedColour.getRed() < 10) { //DEEP_WATER
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.DEEP_WATER));
                } else if ((averagedColour.getBlue() >= 160 && averagedColour.getBlue() < 220)  && averagedColour.getGreen() < 10 && averagedColour.getRed() < 10) { //WATER
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.WATER));
                } else if ((averagedColour.getBlue() >= 220)  && averagedColour.getGreen() < 10 && averagedColour.getRed() < 10) {// LIGHT WATER
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.LIGHT_WATER));
                } else if (averagedColour.getRed() > 0) { //SAND
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.SAND));
                } else if (averagedColour.getGreen() <= 255 && averagedColour.getGreen() > 210) { //GRASS
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.GRASS));
                } else if (averagedColour.getGreen()  <= 210 && averagedColour.getGreen() > 160) { //MOUNTAIN BASE
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.MOUNTAIN_BASE));
                } else if (averagedColour.getGreen()  <= 160 && averagedColour.getGreen() > 120) { //MOUNTAIN BODY
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.MOUNTAIN_BODY));
                } else if (averagedColour.getGreen()  <= 120 && averagedColour.getGreen() > 10) { //MOUNTAIN PEAK
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.MOUNTAIN_PEAK));
                } else {
                    controller.pushText("A non fatal error has occurred.");
                    controller.pushText("Error: no RGB band could be assigned with value " + averagedColour.getRGB() + "!");
                    controller.pushText("RGB Values: " + "[R: " + averagedColour.getRed() + ", B: " + averagedColour.getBlue() + ", G: " + averagedColour.getGreen() + "]"); //Missing texture
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.MISSING_TEXTURE));
                }
            }
        }
        //free it for the jvm
        noiseGraph = null;
    }

    /**
     * This method sets up the society cell generation by spawning society cells at random coordinates and generating an area of effect visualized through tinting all affected cells
     */
    private void setupSocietyCells() {
        //The counter provides an incrementing integer for each time a successful society creation occurs
        int counter = 0;
        int radius = 7;
        while (counter < controller.getSimulationProperties().societyCount()) {
            int newSocietyCoordinateX = (int) (Math.random() * (150 - 1 - 1 + 1) + 1);
            int newSocietyCoordinateY = (int) (Math.random() * (150 - 1 - 1 + 1) + 1);
            Point point = new Point(newSocietyCoordinateX, newSocietyCoordinateY);
            //check if the coordinate that was gotten was an instance of worldcell, has no owner and is of type grass
            if (modelStructure.getBackLayer(point).getOwner() == null && modelStructure.getBackLayer(point).getECellType() == ECellType.GRASS) {
                //Sets a tint in the style that is in standard with Java's ColorModel.
                int tint = 0x00009000;
                tint = tint | (int) (Math.random() * 255 - 150) + 150 << 16; //Set red and bit shift 16 places to align it with the red bytes
                tint = tint | (int) (Math.random() * 255 - 150) + 150; //Set blue to align it to blue bytes
                counter++;
                //Create a new cell that is on top of the cell that currently inhabits the coordinates
                modelStructure.setFrontLayer(point, cellFactory.createSocietyCell(UUID.randomUUID().toString(), radius, tint));
                globalSocietyCellList.add(point);

                //1 unit of radius is 1 cell
                PointUtilities.tintArea(radius, point, tint, modelStructure);
            }
        }
    }


    private void setupNutrientCells() {
        //Setup an initial amount of nutrient cells per society. this makes sure they dont start with nothing
        for (Point societyPoint : globalSocietyCellList) {
            SocietyCell societyCell = modelStructure.getCoordinate(societyPoint);
            for (int initialNutrients = 0; initialNutrients < controller.getSimulationProperties().initialNutrientCount(); initialNutrients++) {
                Point nutrientPoint = PointUtilities.calculateRandomValidCoordinates(societyPoint, societyCell.getRadius(), modelStructure, List.of(new ECellType[]{ECellType.GRASS}));
                NutrientCell nutrientCell = cellFactory.createNutrientCell(societyCell);
                modelStructure.setFrontLayer(nutrientPoint, nutrientCell);
                societyCell.addNutrientCells(nutrientCell);
            }
        }

        int i = 0;
        do {
            //Randomly generate point
            int j = (int) (Math.random() * (150 - 1 - 1 + 1) + 1);
            int k = (int) (Math.random() * (150 - 1 - 1 + 1) + 1);
            Point point = new Point(j, k);
            //Make sure that the nutrient cell generates on grass, and set its owner
            if (modelStructure.getCoordinate(point).getECellType() == ECellType.GRASS) {
                i++;
                SocietyCell owner = modelStructure.getBackLayer(point).getOwner();
                modelStructure.setFrontLayer(point, cellFactory.createNutrientCell(owner));
                if (owner != null) {
                    //Add the nutrient cell to the list of owned cells that the society cell can call its own
                    owner.addNutrientCells(modelStructure.getFrontLayer(point));
                }
            }
        } while (i < controller.getSimulationProperties().nutrientCount());
    }

    private void setupLifeCells() {
        for (Point societyCellPoint: globalSocietyCellList) {
            // Generate an amount of life cells between 2 and 50% of the capacity of the society cell
            SocietyCell frontLayer = modelStructure.getFrontLayer(societyCellPoint);
            int i = 0;
            while (i < Math.random() * frontLayer.getNutrientCapacity() * 0.40 + 2) {
                //Make sure the coordinates generated are within the aoe bounds
                Point coords = PointUtilities.calculateRandomValidCoordinates(societyCellPoint, frontLayer.getRadius(), modelStructure, List.of(new ECellType[]{ECellType.GRASS}));
                //Enforce that whatever we're replacing is not being occupied currently
                if (!(Objects.isNull(coords)) && modelStructure.getFrontLayer(coords) == null) {
                    i++;
                    modelStructure.setFrontLayer(coords, cellFactory.createLifeCell(societyCellPoint));
                    ((SocietyCell) modelStructure.getFrontLayer(societyCellPoint)).addLifeCells();
                    globalLifeCellList.add(coords);
                }
            }
        }
    }
}