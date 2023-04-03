package org.basmat.map.setup;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.model.cells.factory.CellFactory;
import org.basmat.map.util.PointUtilities;
import org.basmat.map.util.CubicInterpolation;
import org.basmat.map.util.ECellType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * MapSetup generates the initial state of the map, and stores it in a ModelStructure.
 * The initial terrain is stored in the WorldCell matrix and should be immutable after being set, where the IOwnedCell matrix contains mutable data which is allowed to be changed.
 */

/*
 If you followed the umbilical of history in search of some ultimate atavistic embryo that became them, then your journey would end here, in this garden.
 */
public class ModelSetup {
    private BufferedImage noiseGraph;
    private final HashMap<ECellType, BufferedImage> imageCache;
    private final LinkedList<Point> globalSocietyCellList;
    private final LinkedList<Point> globalNutrientCellList;
    private final LinkedList<Point> globalLifeCellList;
    private final ModelStructure modelStructure;
    private final CellFactory cellFactory;


    /**
     * @param imageCache The cache of textures for rendering
     * @param modelStructure The ModelStructure containing all the model data within 2 matrices.
     * @param globalNutrientCellList The list of which all nutrient cells have a reference within
     * @param globalSocietyCellList The list of which all society cells have a reference within
     * @param globalLifeCellList The list of which all life cells have a reference within
     */
    public ModelSetup(HashMap<ECellType, BufferedImage> imageCache, ModelStructure modelStructure, LinkedList<Point> globalNutrientCellList, LinkedList<Point> globalSocietyCellList, LinkedList<Point> globalLifeCellList) {
        this.cellFactory = new CellFactory();
        this.modelStructure = modelStructure;
        this.imageCache = imageCache;
        this.globalSocietyCellList = globalSocietyCellList;
        this.globalNutrientCellList = globalNutrientCellList;
        this.globalLifeCellList = globalLifeCellList;
        noiseGraph = new BufferedImage(750, 750, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Calls methods appropriate for rendering and setup for the matrix
     * @throws InterruptedException
     */
    public void setupMap() throws InterruptedException {
        BufferedImage graph = new BufferedImage(750, 750, BufferedImage.TYPE_INT_ARGB);
        System.out.println("Generating gradient graph");
        setupGradientGraph(-1);
        System.out.println("Applying texture filter");
        setupWorldCells(5);
        System.out.println("Applying society cells and rendering area of effect");
        setupSocietyCells(7);
        System.out.println("Applying nutrient cells");
        setupNutrientCells();
        System.out.println("Appyling life cells to society cells");
        setupLifeCells();
        System.out.println("Complete");
    }

    /**
     * This method provides the setup for the noise graph by applying an arbitrary algorithm to a supplied buffered image.
     * @param seed the seed to provide the noise generator
     */
    private void setupGradientGraph(int seed) {
        CubicInterpolation ci = new CubicInterpolation((int) (Math.random() * (10000 - 1 + 1) + 1));
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
                if (alphaValue > -60 && alphaValue < -35) { //Beach
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
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.DEEP_WATER, imageCache.get(ECellType.DEEP_WATER)));
                } else if ((averagedColour.getBlue() >= 160 && averagedColour.getBlue() < 220)  && averagedColour.getGreen() < 10 && averagedColour.getRed() < 10) { //WATER
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.WATER, imageCache.get(ECellType.WATER)));
                } else if ((averagedColour.getBlue() >= 220)  && averagedColour.getGreen() < 10 && averagedColour.getRed() < 10) {// LIGHT WATER
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.LIGHT_WATER, imageCache.get(ECellType.LIGHT_WATER)));
                } else if (averagedColour.getRed() > 0) { //SAND
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.SAND, imageCache.get(ECellType.SAND)));
                } else if (averagedColour.getGreen() <= 255 && averagedColour.getGreen() > 210) { //GRASS
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.GRASS, imageCache.get(ECellType.GRASS)));
                } else if (averagedColour.getGreen()  <= 210 && averagedColour.getGreen() > 160) { //MOUNTAIN BASE
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.MOUNTAIN_BASE, imageCache.get(ECellType.MOUNTAIN_BASE)));
                } else if (averagedColour.getGreen()  <= 160 && averagedColour.getGreen() > 120) { //MOUNTAIN BODY
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.MOUNTAIN_BODY, imageCache.get(ECellType.MOUNTAIN_BODY)));
                } else if (averagedColour.getGreen()  <= 120 && averagedColour.getGreen() > 10) { //MOUNTAIN PEAK
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.MOUNTAIN_PEAK, imageCache.get(ECellType.MOUNTAIN_PEAK)));
                } else {
                    System.out.println("Error: no RGB band could be assigned with value " + averagedColour.getRGB() + "!");
                    System.out.println("RGB Values: " + "[R: " + averagedColour.getRed() + ", B: " + averagedColour.getBlue() + ", G: " + averagedColour.getGreen() + "]"); //Missing texture
                    modelStructure.setBackLayer(point, cellFactory.createWorldCell(ECellType.MISSING_TEXTURE, imageCache.get(ECellType.MISSING_TEXTURE)));
                }
            }
        }
    }

    /**
     * This method sets up the society cell generation by spawning society cells at random coordinates and generating an area of effect visualized through tinting all affected cells
     * @param target The amount of society cells to generate
     */
    private void setupSocietyCells(int target) {
        //The counter provides an incrementing integer for each time a successful society creation occurs
        int counter = 0;
        int radius = 7;
        while (counter < target) {
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
                modelStructure.setFrontLayer(point, cellFactory.createSocietyCell(UUID.randomUUID().toString(), radius, tint, imageCache.get(ECellType.SOCIETY_CELL)));
                globalSocietyCellList.add(point);

                //Give the society cell a minimum of 1 nutrient cell
                Point randomNutrient = PointUtilities.calculateRandomValidCoordinates(point, radius, modelStructure, List.of(new ECellType[]{ECellType.GRASS, ECellType.SAND}));
                modelStructure.setFrontLayer(randomNutrient, cellFactory.createNutrientCell(modelStructure.getFrontLayer(point), imageCache.get(ECellType.NUTRIENTS)));
                ((SocietyCell) modelStructure.getFrontLayer(point)).addNutrientCells(modelStructure.getFrontLayer(randomNutrient));

                //1 unit of radius is 1 cell
                PointUtilities.tintArea(radius, point, tint, modelStructure);
            }
        }
    }

    /**
     * This method sets up nutrient cell generation, using a similar way of society cell generation.
     */
    private void setupNutrientCells() {
        for (int i = 0; i < 100; i++) {
            //Randomly generate point
            int j = (int) (Math.random() * (150 - 1 - 1 + 1) + 1);
            int k = (int) (Math.random() * (150 - 1 - 1 + 1) + 1);
            Point point = new Point(j, k);
            //Make sure that the nutrient cell generates on grass, and set its owner
            if (modelStructure.getBackLayer(point).getECellType() == ECellType.GRASS) {
                SocietyCell owner = modelStructure.getBackLayer(point).getOwner();
                modelStructure.setFrontLayer(point, cellFactory.createNutrientCell(owner, imageCache.get(ECellType.NUTRIENTS)));
                if (owner != null) {
                    //Add the nutrient cell to the list of owned cells that the society cell can call its own
                    owner.addNutrientCells(modelStructure.getFrontLayer(point));
                }
            }
        }
    }

    private void setupLifeCells() {
        for (Point societyCellPoint: globalSocietyCellList) {
            // Generate an amount of life cells between 2 and 50% of the capacity of the society cell
            SocietyCell frontLayer = modelStructure.getFrontLayer(societyCellPoint);
            //for (int i = 0; i < Math.random() * frontLayer.getNutrientCapacity() * 0.40 + 2; i++) {
            int i = 0;
            while (i < Math.random() * frontLayer.getNutrientCapacity() * 0.40 + 2) {
                //Make sure the coordinates generated are within the aoe bounds
                Point coords = PointUtilities.calculateRandomCoordinates(societyCellPoint, frontLayer.getRadius());
                if (PointUtilities.validateBounds(coords)) {
                    //Enforce that whatever we're replacing is not being occupied currently
                    if (modelStructure.getBackLayer(coords).getECellType().isHabitable() && modelStructure.getFrontLayer(coords) == null) {
                        i++;
                        modelStructure.setFrontLayer(coords, cellFactory.createLifeCell(societyCellPoint, imageCache.get(ECellType.LIFE_CELL)));
                        ((SocietyCell) modelStructure.getFrontLayer(societyCellPoint)).addLifeCells();
                        globalLifeCellList.add(coords);
                    }
                }
            }
        }
    }
}