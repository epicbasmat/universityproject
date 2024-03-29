package org.basmat.map.controller;


import org.basmat.map.controller.rules.RuleApplier;
import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.factory.CellFactory;
import org.basmat.map.model.cells.factory.IMapCell;
import org.basmat.map.setup.ModelSetup;
import org.basmat.map.setup.ViewSetup;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.SimulationProperties;
import org.basmat.map.view.SimulationInteractionUI;
import org.basmat.map.view.SimulationUI;
import org.basmat.map.view.VariableSelectionUI;
import org.basmat.userui.GUI;

import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * The controller is the main class in this system that acts as a proxy between the model and the view. It handles all user events and responds with the appropriate methods.
 * This class also invokes the rules on a given time step, so also contains the Swing Timer and contains the Pause/Play functionality.
 *
 * @author George Brilus
 */
public class Controller {
    private final HashMap<String, ECellType> mapNameToCell;
    private int seed;
    private final SimulationUI simulationUI;
    private ModelStructure modelStructure;
    private GUI primaryGui;
    private final Timer timer;
    private RuleApplier ruleApplier;
    private final SimulationInteractionUI userInteractionUi;
    private final VariableSelectionUI variableSelectionUi;
    private LinkedList<Point> globalSocietyCellList;
    private ArrayList<Point> globalLifeCellList;
    private SimulationProperties simulationProperties;
    private boolean editing;
    private ECellType selectedCell;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Controller(int cellMatrixWidth, int cellMatrixHeight) {
        //generate a map, mapping the names of the cell to the cell itself. this is used in cell selection for the view.
        mapNameToCell = new HashMap<>();
        Arrays.stream(ECellType.values()).toList().stream().filter(ECellType::isUserEditable).forEach(cell -> mapNameToCell.put(cell.getCellName(), cell));
        selectedCell = ECellType.WATER;
        globalSocietyCellList = new LinkedList<>();
        globalLifeCellList = new ArrayList<>();
        modelStructure = new ModelStructure();
        simulationUI = new SimulationUI(cellMatrixWidth, cellMatrixHeight, this);
        userInteractionUi = new SimulationInteractionUI(this);
        variableSelectionUi = new VariableSelectionUI(this);
        ActionListener updateAction = e -> {
            ruleApplier.invokeRules();
            ViewSetup.setupView(simulationUI, modelStructure, ViewSetup.IS_LAZY);
            userInteractionUi.incrementTimeStep();
        };
        timer = new Timer(1000, updateAction);
        File screenshotDir = new File("./screenshots");
        if (!screenshotDir.exists()){
            screenshotDir.mkdirs();
        }
        File saveDir = new File("./saves");
        if (!saveDir.exists()){
            saveDir.mkdirs();
        }

    }

    public void displayUI() {
        primaryGui = new GUI(simulationUI, userInteractionUi, variableSelectionUi);
    }

    public HashMap<String, ECellType> getMapNameToCell() {
        return mapNameToCell;
    }

    public int getAmountOfSocieties() {
        return this.globalSocietyCellList.size();
    }

    public int getAmountOfLifeCells() {
        return this.globalLifeCellList.size();
    }

    /**
     * Pause the currently instantiated timer
     */
    public void pause() {
        timer.stop();
    }

    /**
     * Play the currently instantiated timer
     */
    public void play() {
        timer.start();
    }

    public int getSeed() {
        return this.seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    /**
     * Construct a simulation given a SimulationProperties parameter. This will generate the entire map and fill it with cells.
     * @param simulationProperties The SimulationProperties to provide the setup with.
     */
    public void constructSimulation(SimulationProperties simulationProperties) {
        primaryGui.goToCard(primaryGui.LOADING_CARD);
        new Thread(() -> {
            setSimulationProperties(simulationProperties);
            ModelSetup modelSetup = new ModelSetup(this, modelStructure, globalSocietyCellList, globalLifeCellList);
            ruleApplier = new RuleApplier(this, modelStructure, globalSocietyCellList, globalLifeCellList);
            try {
                modelSetup.setupMap();
                userInteractionUi.incrementTimeStep();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ViewSetup.setupView(simulationUI, modelStructure);
            primaryGui.goToCard(primaryGui.SIMULATION_CARD);
        }).start();
    }

    /**
     * Manually set the simulationproperties
     * @param simulationProperties
     */
    public void setSimulationProperties(SimulationProperties simulationProperties) {
        this.simulationProperties = simulationProperties;
    }

    /**
     * Provides a temporary method for viewing data requested by the cell matrix view.
     * @param e the MouseEvent that the cell captures
     */
    public void displayData(Point e) {
        if (!editing) {
            IMapCell coordinate = modelStructure.getCoordinate(e);
            if (!Objects.isNull(coordinate)) {
                pushText(coordinate.toString());
            }
        } else {
            modelStructure.deleteBackLayer(e);
            modelStructure.setBackLayer(e, new CellFactory().createWorldCell(selectedCell));
            ViewSetup.setupView(simulationUI, modelStructure, ViewSetup.IS_LAZY);
        }
    }

    /**
     * Get the simulation properties set by the system.
     * @return the currently invoked SimulationProperties
     */
    public SimulationProperties getSimulationProperties() {
        return this.simulationProperties;
    }

    /**
     * Push the provided text to the view
     * @param string The string to show to the user
     */
    public void pushText(String string) {
        userInteractionUi.appendText(string + "\n");
    }

    public void saveAsImage(){
        userInteractionUi.disableTimeButtons();
        timer.stop();
        pushText("Screenshot is being saved.");
        BufferedImage screenshot = new BufferedImage(750, 750, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = screenshot.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        for (int x = 0; x < 150; x++) {
            for (int y = 0; y < 150; y++) {
                //For each coordinate in modelstructure, grab the texture of the point and apply it to a blank bufferedimage
                g2d.drawImage(modelStructure.getCoordinate(new Point(x, y)).getTexture(),x * 5, y * 5, null);
            }
        }
        g2d.dispose();
        String s = UUID.randomUUID() + ".png";
        try {
            ImageIO.write(screenshot, "png", new File("./screenshots/" + s));
            pushText("Screenshot saved as: " + s + ".");
        } catch (FileNotFoundException e) {
            primaryGui.throwError("The path where saving occurs has been deleted. \nError: "  + e.getLocalizedMessage());
        } catch (IOException e) {
            primaryGui.throwError("Saving the file as an image has failed. \nError: " + e.getLocalizedMessage());
        }
        userInteractionUi.enablePlayButton();
    }

    /**
     * Save a file to the standard directory. This serializes all the relevant objects in the Controller and Model. Any errors will get thrown to the user.
     */
    public void saveAsData() {
        primaryGui.goToCard(primaryGui.LOADING_CARD);
        pushText("Saving data to file. This may take a minute.");
        userInteractionUi.disableTimeButtons();
        timer.stop();
        String name = "./saves/" + UUID.randomUUID() + ".dat";
        new Thread(() -> {
            try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(name)))) {
                //Put all objects into a hash map to enable easier serialization for multiple objects
                HashMap<String, Object> data = new HashMap<>();
                data.put("societycells", globalSocietyCellList);
                data.put("lifecells", globalLifeCellList);
                data.put("properties", simulationProperties);
                data.put("model", modelStructure);
                data.put("seed", seed);
                out.writeObject(data);
                pushText("Save complete! Saved at " + name + ".");
                userInteractionUi.enablePlayButton();
                primaryGui.goToCard(primaryGui.SIMULATION_CARD);
            } catch (IOException e) {
                primaryGui.throwError("Serialization of data has failed. \nError: " + e.getLocalizedMessage());
            }
        }).start();
    }

    /**
     * Load a file from the provided directory. The system will throw any errors to the user if caught.
     * @param currentDirectory The directory to open
     */
    @SuppressWarnings("unchecked")
    public void loadFromFile(File currentDirectory) {
        primaryGui.goToCard(primaryGui.LOADING_CARD);
        new Thread(() -> {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(currentDirectory));
                HashMap<String, Object> data = (HashMap<String, Object>) objectInputStream.readObject();
                this.globalLifeCellList = (ArrayList<Point>) data.get("lifecells");
                setSimulationProperties((SimulationProperties) data.get("properties"));
                this.globalSocietyCellList = (LinkedList<Point>) data.get("societycells");
                this.modelStructure = (ModelStructure) data.get("model");
                this.seed = (int) data.get("seed");
                ruleApplier = new RuleApplier(this, modelStructure, globalSocietyCellList, globalLifeCellList);
                ViewSetup.setupView(simulationUI, modelStructure);
                primaryGui.validate();
                userInteractionUi.incrementTimeStep();
                primaryGui.goToCard(primaryGui.SIMULATION_CARD);
            } catch (IOException e) {
                primaryGui.throwError("A File reading error has occurred. \nError: " + e.getLocalizedMessage());
            } catch (ClassNotFoundException e) {
                primaryGui.throwError("Class cannot be found, this could be due to an incompatible data file. \nError: " + e.getLocalizedMessage());
            } catch (ClassCastException e) {
                primaryGui.throwError("Class cannot be cast, this could be due to an incompatible data file, or has been corrupted. \nError: " + e.getLocalizedMessage());
            } catch (IllegalStateException e) {
                primaryGui.throwError("State of the file is not absolute. This could be due to an incompatible data file, however it is most likely a corrupt data file. \nError: " + e.getLocalizedMessage());
            } catch (NullPointerException e) {
                primaryGui.throwError("The file provided is an older file which does not match the current object structure, thus cannot work with the latest application branch. \nError: " + e.getLocalizedMessage());
            }
        }).start();
    }

    public void setEditing(boolean value) {
        editing = value;
    }

    public void setSelectedCell(ECellType eCellType) {
        this.selectedCell = eCellType;
    }
}