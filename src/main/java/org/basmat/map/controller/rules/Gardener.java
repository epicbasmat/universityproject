package org.basmat.map.controller.rules;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.LifeCell;
import org.basmat.map.model.cells.NutrientCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.model.cells.factory.IMapCell;
import org.basmat.map.util.path.Node;
import org.basmat.map.util.path.Pathfind;
import org.basmat.map.view.CellPanel;
import org.basmat.map.view.ViewStructure;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * The gardener rule set provides methods into life. The rules govern how the life cells will recreate, as well as attempts to survive, such as scavenge if food resources are low.
 */
/*
 In the morning, the gardener pushed the seeds down into the wet loam of the garden to see what they would become
 */
public class Gardener {

    private LinkedList<Point> globalNutrientCellList;
    private LinkedList<Point> globalSocietyCellList;
    private LinkedList<Point> globalLifeCellList;

    //Update
    private HashMap<SocietyCell, LinkedList<Node>> activeSocietyCells;
    private ViewStructure viewStructure;
    private final ModelStructure modelStructure;

    /**
     *
     * @param viewStructure
     * @param modelStructure
     * @param globalNutrientCellList
     * @param globalSocietyCellList
     * @param globalLifeCellList
     */
    public Gardener(ViewStructure viewStructure, ModelStructure modelStructure, LinkedList<Point> globalNutrientCellList, LinkedList<Point> globalSocietyCellList, LinkedList<Point> globalLifeCellList) {
        this.viewStructure = viewStructure;
        this.modelStructure = modelStructure;
        this.globalNutrientCellList = globalNutrientCellList;
        this.globalSocietyCellList = globalSocietyCellList;
        this.globalLifeCellList = globalLifeCellList;
        activeSocietyCells = new HashMap<>();
    }

    /**
     *
     * @param societyCell The society cell to determine if anything needs to happen
     * @param percentageCapacity The population / nutrient capacity * 100
     */
    public void scavenge(SocietyCell societyCell, double percentageCapacity) {
        //If the nutrient capacity of the society exceeds 85%, then tell someone to scavenge
        if (percentageCapacity > 85) {
            //Look for food, or risk dying
        }
    }

    /**
     * This method manages the cells if they are trying to join each other.
     */
    public <T extends IMapCell> void unison() {
        for (SocietyCell societyCell: activeSocietyCells.keySet()) {
            LinkedList<Node> list = activeSocietyCells.get(societyCell);
            if (list.isEmpty()) {
                activeSocietyCells.remove(societyCell);
                continue;
            }
            Node current = list.remove();
            Node toMoveTo = list.peek();
            Point cPoint = current.point();
            T model = modelStructure.getCoordinate(cPoint);
            modelStructure.deleteCoordinate(cPoint);
            modelStructure.setFrontLayer(toMoveTo.point(), model);
            viewStructure.getAndReplace(cPoint, new CellPanel(modelStructure.getBackLayer(cPoint).getTexture()), toMoveTo.point());
        }
    }

    /**
     * Logic provider for reproduction. Determines if a society cell will cause reproduction in it's owned life cells.
     */
    public void reproduce() {
        for (Point point : globalSocietyCellList) {
            //Cannot have more than 1 instance of a society cell trying to procreate, though this may change later.
            //if (!activeSocietyCells.containsKey()) {
                SocietyCell societyCell = modelStructure.getCoordinate(point);
                int percentageCapacity = societyCell.getSize() / societyCell.getNutrientCapacity() * 100;
                //The population and it's resources determine the probability of a societycell determining if it needs to reproduce. A high population and not a lot of food left means there is a lower chance of reproduction. Vice versa.
                int reproduceProbability;
                /*if (percentageCapacity < 20) {
                    reproduceProbability = 100;
                } else if (percentageCapacity < 50) {
                    reproduceProbability =  50;
                } else if (percentageCapacity < 80) {
                    reproduceProbability =  20;
                } else {
                    reproduceProbability =  0;
                }
                 */
                System.out.println(societyCell.getName());
                reproduceProbability = 100;
                if (reproduceProbability > Math.random() * 100) {
                    activeSocietyCells.put(modelStructure.getCoordinate(point), getPathBetweenCouple(societyCell));
                }
            //}
        }
        //For any society cells deciding to populate, we must make the life cells go together. Though there is a chance of reproduction each time tick, we want to guarantee that the life cells try to go together each time tick.
        //unison();
    }

    private LinkedList<Node> getPathBetweenCouple(SocietyCell societyCell) {
        Point[] couple = selectCoupleLifeCell(societyCell);
        LinkedList<Node> nodes = Pathfind.aStar(250, modelStructure, couple[0], couple[1]);
        nodes.addFirst(new Node(couple[0], 0));
        return nodes;
    }

    private Point selectRandomLifeCell(SocietyCell societyCell) {
        return societyCell.getLifeCells().get((int) (Math.random() * societyCell.getSize()));
    }

    private Point[] selectCoupleLifeCell(SocietyCell societyCell) {
        Point[] points = new Point[2];
        points[0] = selectRandomLifeCell(societyCell);
        do {
            points[1] = selectRandomLifeCell(societyCell);
        } while (points[1].equals(points[0]));
        return points;
    }
}
