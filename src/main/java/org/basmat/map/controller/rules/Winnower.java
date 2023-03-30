package org.basmat.map.controller.rules;


import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.LifeCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.PointUtilities;
import org.basmat.map.util.path.Node;
import org.basmat.map.view.ViewStructure;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * The winnower class provides methods for death. Whereas the gardener provides methods for birth, survival, the Winnower provides how the cells will die, such as lack of food or fighting.
 */
/*
 "The winnower reaped the day's crop, and separated what would flourish from what had failed."
 */
public class Winnower {

    private ViewStructure viewStructure;
    private ModelStructure modelStructure;
    private LinkedList<Point> globalNutrientCellList;
    private LinkedList<Point> globalSocietyCellList;
    private LinkedList<Point> globalLifeCellList;
    private LinkedList<LinkedList<Node>> listOfPaths;

    public Winnower(ViewStructure viewStructure, ModelStructure modelStructure, LinkedList<Point> globalNutrientCellList, LinkedList<Point> globalSocietyCellList, LinkedList<Point> globalLifeCellList, LinkedList<LinkedList<Node>> listOfPaths) {
        this.viewStructure = viewStructure;
        this.modelStructure = modelStructure;
        this.globalNutrientCellList = globalNutrientCellList;
        this.globalSocietyCellList = globalSocietyCellList;
        this.globalLifeCellList = globalLifeCellList;
        this.listOfPaths = listOfPaths;
    }

    public void overcrowded() {
        List<Point> copyOfList = new LinkedList<>(globalLifeCellList);
        for (Point lifeCell : copyOfList) {
            int overcrowd = 0;
            for (Point coords : PointUtilities.getAllValidatedNeighbours(lifeCell)) {
                if (modelStructure.getCoordinate(coords).getECellType() == ECellType.LIFE_CELL) {
                    overcrowd++;
                }
            } if (overcrowd > 4) {
                System.out.println("A life cell has been killed from overcrowding!");
                kill(lifeCell);
            }
        }
    }

    public void collapse() {
        for (Point point : globalSocietyCellList) {
            SocietyCell coordinate = modelStructure.getCoordinate(point);
            double circleArea = 3.141529 * Math.pow(coordinate.getRadius(), 2);
            if (circleArea/coordinate.getPopulationCount() < 3) {
                System.out.println("Society collapsed, too much land for people!");
                coordinate.getLifeCells().stream().toList().forEach(this::kill);
            }
        }
    }

    public void kill(Point lifeCell) {
        ((SocietyCell) modelStructure.getCoordinate(((LifeCell) modelStructure.getCoordinate(lifeCell)).getSocietyCell())).killCell(lifeCell);
        modelStructure.deleteCoordinate(lifeCell);
        globalLifeCellList.remove(lifeCell);
        List<LinkedList<Node>> elementsToRemove = listOfPaths.parallelStream().filter(Objects::nonNull).filter(e -> {
            assert e.peek() != null;
            return e.peek().point().equals(lifeCell);
        }).toList();
        listOfPaths.removeAll(elementsToRemove);
    }
}
