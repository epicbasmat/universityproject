package org.basmat.map.controller;


import org.basmat.map.controller.rules.RuleApplier;
import org.basmat.map.model.ModelStructure;
import org.basmat.map.setup.ModelSetup;
import org.basmat.map.setup.ViewSetup;
import org.basmat.map.util.SimulationProperties;
import org.basmat.map.view.SimulationInteractionUI;
import org.basmat.map.view.SimulationUI;
import org.basmat.map.view.VariableSelectionUI;
import org.basmat.userui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class Controller {

    private final File screenshotDir;
    private int seed;
    private final SimulationUI simulationUI;
    private ModelStructure modelStructure;
    private final GUI primaryGui;
    private final Timer timer;
    private RuleApplier ruleApplier;
    private final SimulationInteractionUI userInteractionUi;
    private final VariableSelectionUI variableSelectionUi;
    private LinkedList<Point> globalSocietyCellList;
    private LinkedList<Point> globalLifeCellList;
    private SimulationProperties simulationProperties;

    public Controller(int cellMatrixWidth, int cellMatrixHeight) throws InterruptedException {
        globalSocietyCellList = new LinkedList<>();
        globalLifeCellList = new LinkedList<>();
        modelStructure = new ModelStructure();
        simulationUI = new SimulationUI(cellMatrixWidth, cellMatrixHeight, this);
        userInteractionUi = new SimulationInteractionUI(this);
        variableSelectionUi = new VariableSelectionUI(this);
        primaryGui = new GUI(simulationUI, userInteractionUi, variableSelectionUi);
        ActionListener updateAction = e -> {
            ruleApplier.invokeRules();
            ViewSetup.setupView(simulationUI, modelStructure, ViewSetup.IS_LAZY);
            userInteractionUi.incrementTimeStep();
        };
        timer = new Timer(1000, updateAction);
        screenshotDir = new File("./screenshots");
        if (!screenshotDir.exists()){
            screenshotDir.mkdirs();
        }
    }

    public void pause() {
        timer.stop();
    }

    public void play() {
        timer.start();
    }

    public int getSeed() {
        return this.seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public void constructSimulation(SimulationProperties simulationProperties) {
        this.simulationProperties = simulationProperties;
        ModelSetup modelSetup = new ModelSetup(this, modelStructure, globalSocietyCellList, globalLifeCellList);
        ruleApplier = new RuleApplier(this, modelStructure, globalSocietyCellList, globalLifeCellList);
        try {
            modelSetup.setupMap();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ViewSetup.setupView(simulationUI, modelStructure);
        primaryGui.nextCard();
    }

    /**
     * Provides a temporary method for viewing data requested by the cell matrix view.
     * @param e the MouseEvent that the cell captures
     */
    public void displayData(Point e) {
        pushText(modelStructure.getCoordinate(e).toString());
    }

    public SimulationProperties getSimulationProperties() {
        return this.simulationProperties;
    }

    public void pushText(String string) {
        userInteractionUi.appendText(string + "\n");
    }

    public void saveAsImage(){
        userInteractionUi.disableUserInput();
        timer.stop();
        pushText("Screenshot is being saved.");
        BufferedImage screenshot = new BufferedImage(750, 750, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = screenshot.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        for (int x = 0; x < 150; x++) {
            for (int y = 0; y < 150; y++) {
                g2d.drawImage(modelStructure.getCoordinate(new Point(x, y)).getTexture(),x * 5, y * 5, null);
            }
        }
        g2d.dispose();
        String s = UUID.randomUUID() + ".png";
        try {
            ImageIO.write(screenshot, "png", new File("./screenshots/" + s));
        } catch (IOException e) {
            primaryGui.throwError("Saving the file as an image has failed. \nError: " + e.getLocalizedMessage());
        }
        pushText("Screenshot saved as: " + s + ".");
        userInteractionUi.enableUserInput();
    }

    public void saveAsData() {
        pushText("Saving data to file. This may take a minute.");
        userInteractionUi.disableUserInput();
        timer.stop();
        String name = "./saves/" + UUID.randomUUID() + ".dat";
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(name)))) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("societycells", globalSocietyCellList);
            data.put("lifecells", globalLifeCellList);
            data.put("properties", simulationProperties);
            data.put("model", modelStructure);
            out.writeObject(data);
            pushText("Save complete! Saved at " + name + ".");
            out.close();
        } catch (IOException e) {
            primaryGui.throwError("Serialization of data has failed. \nError: " + e.getLocalizedMessage());
        }
        userInteractionUi.enableUserInput();
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(File currentDirectory) {
        try {
            primaryGui.nextCard();
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(currentDirectory));
            HashMap<String, Object> data = (HashMap<String, Object>) objectInputStream.readObject();
            this.globalLifeCellList = (LinkedList<Point>) data.get("lifecells");
            this.simulationProperties = (SimulationProperties) data.get("properties");
            this.globalSocietyCellList = (LinkedList<Point>) data.get("societycells");
            this.modelStructure = (ModelStructure) data.get("model");
            ruleApplier = new RuleApplier(this, modelStructure, globalSocietyCellList, globalLifeCellList);
            ViewSetup.setupView(simulationUI, modelStructure);
            primaryGui.validate();
        } catch (IOException e) {
            primaryGui.throwError("A File reading error has occurred. \nError: " + e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            primaryGui.throwError("Class cannot be found, this could be due to an incompatible data file. \nError: " + e.getLocalizedMessage());
        } catch (ClassCastException e) {
            primaryGui.throwError("Class cannot be cast, this could be due to an incompatible data file, or has been corrupted. \nError: " + e.getLocalizedMessage());
        } catch (IllegalStateException e) {
            primaryGui.throwError("State of the file is not absolute. This could be due to an incompatible data file, however it is most likely a corrupt data file. \nError: " + e.getLocalizedMessage());
        }
    }
}