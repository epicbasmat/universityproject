package org.basmat.map.model.cells.rules;

import org.basmat.map.model.cells.LifeCell;
import org.basmat.map.model.cells.NutrientCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.util.path.Node;
import org.basmat.map.util.path.Pathfind;
import org.basmat.map.view.ViewStructure;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * The gardener rule set provides methods into life. The rules govern how the life cells will recreate, as well as attempts to survive, such as scavenge if food resources are low.
 */
/*
 In the morning, the gardener pushed the seeds down into the wet loam of the garden to see what they would become
 */
public class Gardener {

    private ViewStructure viewStructure;
    private LinkedList<NutrientCell> globalNutrientCellList;
    private LinkedList<SocietyCell> globalSocietyCellList;
    private LinkedList<LifeCell> globalLifeCellList;
    //private HashMap<Integer, MVBinder<?>> mapIdToMvBinder;
    HashMap<SocietyCell, LinkedList<Node>> activeSocietyCells;

    public Gardener(ViewStructure viewStructure, LinkedList<NutrientCell> globalNutrientCellList, LinkedList<SocietyCell> globalSocietyCellList, LinkedList<LifeCell> globalLifeCellList) {
        this.viewStructure = viewStructure;
        this.globalNutrientCellList = globalNutrientCellList;
        this.globalSocietyCellList = globalSocietyCellList;
        this.globalLifeCellList = globalLifeCellList;
        //this.mapIdToMvBinder = mapIdToMvBinder;
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
    public void unison() {
        activeSocietyCells.forEach((k, v) -> v.remove());
    }

    /**
     * Logic provider for reproduction. Determines if a society cell will cause reproduction in it's owned life cells.
     * @param activeSocietyCells A list of society cells with its population currently undergoing reproduction. A society cell can only have 2 reproducing life cells at a time, and gets released when a birth occurs.
     */
    private void reproduceLogic(HashMap<SocietyCell, LinkedList<Node>> activeSocietyCells) {
        for (SocietyCell societyCell : globalSocietyCellList) {
            //Cannot have more than 1 instance of a society cell trying to procreate, though this may change later.
            if (!activeSocietyCells.containsKey(societyCell)) {
                int percentageCapacity = societyCell.getSize() / societyCell.getNutrientCapacity() * 100;
                //The population and it's resources determine the probability of a societycell determining if it needs to reproduce. A high population and not a lot of food left means there is a lower chance of reproduction. Vice versa.
                if (reproduce(percentageCapacity) > Math.random() * 100) {
                    activeSocietyCells.put(societyCell, getPathBetweenCouple(societyCell));
                }
            }
        }
    }

    /**
     * Returns the probability of a couple reproducing, dependent on the overall population / nutrient capacity
     * @param percentageCapacity The percentage capacity of the society cell
     * @return integer between 100 and 0, depending on the percentage
     */
    private int reproduce(double percentageCapacity) {
        if (percentageCapacity < 20) {
           return 100;
        } else if (percentageCapacity < 50) {
            return 50;
        } else if (percentageCapacity < 80) {
            return 20;
        } else {
            return 0;
        }
    }

    private LinkedList<Node> getPathBetweenCouple(SocietyCell societyCell) {
        LifeCell[] couple = selectCoupleLifeCell(societyCell);
        //return Pathfind.aStar(250, viewStructure, mapIdToMvBinder, mapIdToMvBinder.get(couple[0].getId()).point(), mapIdToMvBinder.get(couple[1].getId()).point());
        return null;
    }

    private LifeCell selectRandomLifeCell(SocietyCell societyCell) {
        return societyCell.getLifeCells().get((int) (Math.random() * societyCell.getSize()));
    }

    private LifeCell[] selectCoupleLifeCell(SocietyCell societyCell) {
        LifeCell couple1 = selectRandomLifeCell(societyCell);
        LifeCell couple2 = selectRandomLifeCell(societyCell);
        //Ensure the couple is not the same, asexual reproduction for biological cells only.
        if (couple1.equals(couple2)) {
            selectCoupleLifeCell(societyCell);
        } else {
            return new LifeCell[]{couple1, couple2};
        }
        return new LifeCell[0];
    }
}
