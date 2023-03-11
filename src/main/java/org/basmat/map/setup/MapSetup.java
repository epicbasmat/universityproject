package org.basmat.map.setup;

import org.basmat.map.cellfactory.cells.LifeCell;
import org.basmat.map.cellfactory.cells.NutrientCell;
import org.basmat.map.cellfactory.cells.SocietyCell;
import org.basmat.map.cellfactory.cells.WorldCell;
import org.basmat.map.controller.MVBinder;
import org.basmat.map.data.CellDataHelper;
import org.basmat.map.util.CircleBounds;
import org.basmat.map.util.CubicInterpolation;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.path.Node;
import org.basmat.map.util.path.Pathfind;
import org.basmat.map.view.CellMatrixPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

/**
 * MapSetup provides methods for setting up the initial map of the simulation.
 */
public class MapSetup {

    private BufferedImage noiseGraph;
    private final HashMap<ECellType, BufferedImage> imageCache;
    private LinkedList<Integer> globalSocietyCellList;
    private LinkedList<Integer> globalNutrientCellList;
    private LinkedList<Integer> globalLifeCellList;
    private CellDataHelper cellDataHelper;
    private HashMap<Integer, MVBinder<?>> mapIdToMvBinder;
    private CellMatrixPanel cellMatrixPanel;


    /**
     *
     * @param imageCache The cache of textures for rendering
     * @param cellDataHelper The helper to assist in generating classes and setting them to the matrix
     * @param mapIdToMvBinder A hashmap that contains a map that relates the ID of the cell to the MVBinder
     * @param cellMatrixPanel The parent frame to hold all generated cells
     * @param globalSocietyCellList The list of which all society cells have a reference within
     * @param globalNutrientCellList The list of which all nutrient cells have a reference within
     * @param globalLifeCellList The list of which all life cells have a reference within
     */
    public MapSetup(HashMap<ECellType, BufferedImage> imageCache, CellDataHelper cellDataHelper, HashMap<Integer, MVBinder<?>> mapIdToMvBinder, CellMatrixPanel cellMatrixPanel, LinkedList<Integer> globalSocietyCellList, LinkedList<Integer> globalNutrientCellList, LinkedList<Integer> globalLifeCellList) {
        this.mapIdToMvBinder = mapIdToMvBinder;
        this.cellMatrixPanel = cellMatrixPanel;
        this.imageCache = imageCache;
        this.globalSocietyCellList = globalSocietyCellList;
        this.globalNutrientCellList = globalNutrientCellList;
        this.globalLifeCellList = globalLifeCellList;
        noiseGraph = new BufferedImage(750, 750, BufferedImage.TYPE_INT_ARGB);
        this.cellDataHelper = cellDataHelper;
    }

    public HashMap<ECellType, BufferedImage> getImageCache() {
        return imageCache;
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
        for (Node node : new Pathfind(cellMatrixPanel, mapIdToMvBinder, globalNutrientCellList, new Point(10, 10), new Point(80, 80)).aStar()) {
            Point point = node.getPoint();
            mapIdToMvBinder.put(0, cellDataHelper.overwriteCellData(cellDataHelper.generateWorldBinder(ECellType.MISSING_TEXTURE, 0, point), mapIdToMvBinder.get(cellMatrixPanel.getPanel( (int)point.getX(), (int) point.getY()).getId())));
        }
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
        for (int x = 0; x <= 150 - 5; x++) {
            for (int y = 0; y <= 150 - 5; y++) {
                int average = 0;
                //Calculate average RGB value of a 5x5 grid
                for (int i = x * cellSize; i < x * cellSize + 5; i++) {
                    for (int j = y * cellSize; j < y * cellSize + 5; j++) {
                        average += noiseGraph.getRGB(i, j);
                    }
                }
                //Sets the texture according to the averaged colour's RGB bands
                Color averagedColour = new Color(average / (cellSize * cellSize));
                int id = (int) (Math.random() * (100000000 - 1) + 1);
                if ((averagedColour.getBlue() > 0 && averagedColour.getBlue() < 160) && averagedColour.getGreen() < 10 && averagedColour.getRed() < 10) { //DEEP_WATER
                    mapIdToMvBinder.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.DEEP_WATER, id, new Point(x, y))));
                } else if ((averagedColour.getBlue() >= 160 && averagedColour.getBlue() < 220)  && averagedColour.getGreen() < 10 && averagedColour.getRed() < 10) { //WATER
                    mapIdToMvBinder.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.WATER, id, new Point(x, y))));
                } else if ((averagedColour.getBlue() >= 220)  && averagedColour.getGreen() < 10 && averagedColour.getRed() < 10) {// LIGHT WATER
                    mapIdToMvBinder.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.LIGHT_WATER, id, new Point(x, y))));
                } else if (averagedColour.getRed() > 0) { //SAND
                    mapIdToMvBinder.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.SAND, id, new Point(x, y))));
                } else if (averagedColour.getGreen() <= 255 && averagedColour.getGreen() > 210) { //GRASS
                    mapIdToMvBinder.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.GRASS, id, new Point(x, y))));
                } else if (averagedColour.getGreen()  <= 210 && averagedColour.getGreen() > 160) { //MOUNTAIN BASE
                    mapIdToMvBinder.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.MOUNTAIN_BASE, id, new Point(x, y))));
                } else if (averagedColour.getGreen()  <= 160 && averagedColour.getGreen() > 120) { //MOUNTAIN BODY
                    mapIdToMvBinder.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.MOUNTAIN_BODY, id, new Point(x, y))));
                } else if (averagedColour.getGreen()  <= 120 && averagedColour.getGreen() > 10) { //MOUNTAIN PEAK
                    mapIdToMvBinder.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.MOUNTAIN_PEAK, id, new Point(x, y))));
                } else {
                    System.out.println("Error: no RGB band could be assigned with value " + averagedColour.getRGB() + "!");
                    System.out.println("RGB Values: " + "[R: " + averagedColour.getRed() + ", B: " + averagedColour.getBlue() + ", G: " + averagedColour.getGreen() + "]"); //Missing texture
                    mapIdToMvBinder.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.MISSING_TEXTURE, id, new Point(x, y))));
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
        int radius = 12;
        while (counter < target) {
            int j = (int) (Math.random() * (145 - 1 - 1 + 1) + 1);
            int k = (int) (Math.random() * (145 - 1 - 1 + 1) + 1);
            //check if the coordinate that was gotten was an instance of worldcell, has no owner and is of type grass
            if (mapIdToMvBinder.get(cellMatrixPanel.getPanel(j, k).getId()).model() instanceof WorldCell newSocietyCell && newSocietyCell.getOwner() == null && newSocietyCell.getECellType() == ECellType.GRASS) {
                int id = (int) (Math.random() * (100000000 - 1 + 1) + 1);
                counter++;
                //Create a new cell that overwrites the cell that currently inhabits the coordinates
                mapIdToMvBinder.put(id, cellDataHelper.overwriteCellData(cellDataHelper.generateSocietyBinder(UUID.randomUUID().toString(), id, 12, new Point(j, k)), mapIdToMvBinder.get(cellMatrixPanel.getPanel(j,k).getId())));
                globalSocietyCellList.add(id);
                //Then get the reference of the society cell just instantiated because i cannot think of a better way to do it right now
                //bindingAgent.get(cellMatrixPanel.getPanel(j, k))
                //cellSubscriber.addToGlobalSocietyCells(societyCell);

                //Sets a tint in the style that is in standard with Java's ColorModel.
                int tint = 0x00009000;
                tint = tint | (int) (Math.random() * 255 - 150) + 150 << 16; //Set red and bit shift 16 places to align it with the red bytes
                tint = tint | (int) (Math.random() * 255 - 150) + 150; //Set blue to align it to blue bytes

                //1 unit of radius is 1 cell
                for (int c = 0; c <= 180; c += 5) {
                    int circumferenceX = (int) (radius * Math.cos(c * 3.141519 / 180));
                    int circumferenceY = (int) (radius * Math.sin(c * 3.141519 / 180));
                    try{
                        //Get the coordinate of the circumference of the circle and the diametric coordinate of the circle, and fills in the cells between the two calculated coordinates
                        //TODO: Reject any attempts to generate a society cell if it is within an AOE of another society cell
                        for (int x = -circumferenceX + j; x < circumferenceX + j; x++) {
                            for (int y = -circumferenceY + k; y < circumferenceY + k; y++) {
                                //Checks to make sure the x and y are not out of bounds, the currently selected cell is of type worldcell, it has no owner and that it is habitable
                                if (x <= 145 && y <= 145 && x >= 0 && y >= 0 && mapIdToMvBinder.get(cellMatrixPanel.getPanel(x, y).getId()).model() instanceof WorldCell worldCell && worldCell.getECellType().isHabitable() && worldCell.getOwner() == null) {
                                    //Set the owner of the cell, provided the cell has no owner and is habitable
                                    cellMatrixPanel.getPanel(x, y).setTint(tint);
                                    //Get the value of the id associated with the society cell from the earlier generation
                                    worldCell.setOwner((SocietyCell) mapIdToMvBinder.get(id).model());
                                }
                            }
                        }
                    } catch (Exception e) { System.out.println(e.getMessage());}
                }
            }
        }
    }

    /**
     * This method sets up nutrient cell generation, using a similar way of society cell generation.
     */
    private void setupNutrientCells() {
        for (int i = 0; i < 100; i++) {
            int j = (int) (Math.random() * (145 - 1 - 1 + 1) + 1);
            int k = (int) (Math.random() * (145 - 1 - 1 + 1) + 1);
            int id = (int) (Math.random() * (100000000 - 1 + 1) + 1);
            if (mapIdToMvBinder.get(cellMatrixPanel.getPanel(j, k).getId()).model() instanceof WorldCell worldCell && worldCell.getECellType() == ECellType.GRASS) {
                //Necessary administration handling, adding to the global pool, removing the current cell and setting the new cell
                mapIdToMvBinder.put(id, cellDataHelper.overwriteCellData(cellDataHelper.generateNutrientBinder(worldCell.getOwner(), id, new Point(j, k)), mapIdToMvBinder.get(cellMatrixPanel.getPanel(j, k).getId())));
                globalNutrientCellList.add(id);
                if (worldCell.getOwner() != null) {
                    worldCell.getOwner().addNutrientCells((NutrientCell) mapIdToMvBinder.get(id).model());
                }
            }
        }
    }

    private void setupLifeCells() {
        for (int id : globalSocietyCellList) {
            // Generate an amount of life cells between 2 and 50% of the capacity of the society cell
            for (int i = 0; i < Math.random() * (((SocietyCell) mapIdToMvBinder.get(id).model()).getCapacity() * 0.50) + 2; i++) {
                MVBinder<?> binder = mapIdToMvBinder.get(id);
                //Make sure the coordinates generated are within the aoe bounds
                int[] coords = CircleBounds.calculateAndReturnRandomCoords(mapIdToMvBinder, id);
                int lifeid = (int) (Math.random() * (100000000 - 1 + 1) + 1);
                if (coords[0] <= 145 && coords[1] <= 145 && coords[0] >= 0 && coords[1] >= 0) {
                    MVBinder<?> remove = mapIdToMvBinder.get(cellMatrixPanel.getPanel(coords[0],coords[1]).getId());
                    //Check to make sure that the cell we're replacing is the one that can sustain habitation for life cells.
                    if (remove.model().getECellType().isHabitable()) {
                        mapIdToMvBinder.put(lifeid, cellDataHelper.overwriteCellData(cellDataHelper.generateLifeBinder((SocietyCell) binder.model(), lifeid, new Point(coords[0], coords[1])), remove));
                        ((SocietyCell) binder.model()).addLifeCells((LifeCell) mapIdToMvBinder.get(lifeid).model());
                    }
                }
            }
        }
    }
}