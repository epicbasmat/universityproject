package org.basmat.map.controller.rules;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.LifeCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.model.cells.WorldCell;
import org.basmat.map.model.cells.factory.CellFactory;
import org.basmat.map.model.cells.factory.IMapCell;
import org.basmat.map.util.CircleBounds;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.TextureHelper;
import org.basmat.map.util.path.Node;
import org.basmat.map.util.path.Pathfind;
import org.basmat.map.view.ViewStructure;

import java.awt.*;
import java.util.*;
import java.util.List;

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
    private LinkedList<LinkedList<Node>> activeSocietyCells;
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
        activeSocietyCells = new LinkedList<>();
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

    public void expand() {
        for (Point societyCellPoint : globalSocietyCellList) {
            SocietyCell societyCell = modelStructure.getCoordinate(societyCellPoint);
            int additionalArea = societyCell.getSize() / 6;


        }
    }

    public void scatter() {
        for (Point lifeCellPoint : globalLifeCellList) {
            LifeCell lifeCell = modelStructure.getCoordinate(lifeCellPoint);
            SocietyCell societyCell = modelStructure.getCoordinate(lifeCell.getSocietyCell());
            if (lifeCell.getReproductionCooldown() == 9) {
                System.out.println("Abandoning future");
                LinkedList<Node> value = Pathfind.aStar(250, modelStructure, lifeCellPoint, CircleBounds.calculateAndReturnRandomCoords(lifeCell.getSocietyCell(), societyCell.getRadius()));
                activeSocietyCells.add(value);
            }
        }
    }

    public void checkForReproductionRules() {
        System.out.println("Checking reproduction rules");
        //Copy list to prevent concurrency exceptions
        List<Point> copyOfList = new LinkedList<>(globalLifeCellList);
        for (Point lifeCellPoint : copyOfList) {
            LifeCell parent1 = modelStructure.getCoordinate(lifeCellPoint);
            List<int[]> coordinateRef = getLocalPoints(lifeCellPoint);
            List<int[]> ints = coordinateRef.stream().filter(e -> e[0] < 150
                    && e[1] < 150
                    && e[0] > 0
                    && e[1] > 0).toList();

            //Get a 3x3 area from the origin of the cell and see if there are any life cell to reproduce with
            for (int[] point : ints) {
                if (modelStructure.getCoordinate(new Point(point[0], point[1])) instanceof LifeCell lifeCell && lifeCell.getReproductionCooldown() == 0 && parent1.getReproductionCooldown() == 0) {
                    Point newLifeCell;
                    int[] ints1;
                    do {
                        ints1 = coordinateRef.get(((int) (Math.random() * coordinateRef.size())));
                        newLifeCell = new Point(ints1[0], ints1[1]);
                    } while (!(modelStructure.getCoordinate(newLifeCell) instanceof WorldCell worldCell/* && !(worldCell.getECellType().isHabitable())*/));
                    //Create a new life cell and add it to the global array, and add it to the model
                    modelStructure.setFrontLayer(newLifeCell, new CellFactory().createLifeCell(lifeCell.getSocietyCell(), TextureHelper.cacheCellTextures(new HashMap<>()).get(ECellType.LIFE_CELL)));
                    globalLifeCellList.add(newLifeCell);
                    parent1.setReproductionCooldown();
                    ((LifeCell) modelStructure.getCoordinate(new Point(point[0], point[1]))).setReproductionCooldown();
                    ((LifeCell) modelStructure.getCoordinate(newLifeCell)).setReproductionCooldown();
                    System.out.println("A new life cell has been created!");
                    ((SocietyCell) modelStructure.getCoordinate(parent1.getSocietyCell())).addLifeCells(newLifeCell);
                    break;
                }
            }
            parent1.decrementReproductionCooldown();
        }
    }

    public static List<int[]> getLocalPoints(Point point) {
        int[][] ints = {{(int) point.getX(), (int) (point.getY() - 1)}, {(int) point.getX(), (int) (point.getY() + 1)}, {(int) (point.getX() - 1), (int) point.getY()}, {(int) (point.getX() + 1), (int) point.getY()}};
        return Arrays.stream(ints).filter(e -> e[0] < 145
                && e[1] < 145
                && e[0] > 0
                && e[1] > 0).toList();
    }

    /**
     * This method manages the cells if they are trying to join each other.
     */
    public <T extends IMapCell> void unison() {
        //Concurrency exception dodging
        LinkedList<LinkedList<Node>> copyOfList = new LinkedList<>(activeSocietyCells);
        //For each society cell that has an active path
        for (LinkedList<Node> value : copyOfList){
            //Get the LinkedList containing all nodes from point A to point B
            //Stop them before they merge into one cell. Also stop if the value becomes null, but value.size() should prevent that
            //Also, prevent the lifecell from trying to find a mate if the mate or itself has a reproduction cooldown greater than 3
            if (value.peek() == null
                    ||
                            value.size() <= 2
                    ||
                    (
                            modelStructure.getCoordinate(value.peek().point()) instanceof LifeCell lifeCell
                                    && lifeCell.getReproductionCooldown() > 3
                    )
                    ||
                    (
                            modelStructure.getCoordinate(value.getLast().point()) instanceof LifeCell lifeCell2
                                    && lifeCell2.getReproductionCooldown() > 3
                    )
            ) {
                activeSocietyCells.remove(value);
                continue;
            }

            Node current = value.remove();
            Node toMoveTo = value.peek();


            //For each movement, we need to evaluate if there has been a model change since the initial pathfind. If there has been a change, we need to regenerate the path
            if (modelStructure.getCoordinate(toMoveTo.point()).getECellType() != toMoveTo.cellType()) {
                System.out.println("Regenerating path");
                activeSocietyCells.remove(value);
                activeSocietyCells.add(Pathfind.aStar(250, modelStructure, current.point(), value.getLast().point()));
                continue;
            }

            //Get the current node which should be the node the cell is on, and then the cell to move to.
            Point cPoint = current.point();
            modelStructure.replaceFrontLayerAt(cPoint, toMoveTo.point());
            globalLifeCellList.remove(cPoint);
            globalLifeCellList.add(toMoveTo.point());
            ((SocietyCell) modelStructure.getCoordinate(((LifeCell) modelStructure.getCoordinate(toMoveTo.point())).getSocietyCell())).changeLifeCellLoc(current.point(), toMoveTo.point());
        }
        System.out.println("Finished processing");
    }

    /**
     * Logic provider for reproduction. Determines if a society cell will cause reproduction in it's owned life cells.
     */
    public void reproduce() {
        System.out.println("Reproduce time");
        for (Point point : globalSocietyCellList) {
            //Cannot have more than 1 instance of a society cell trying to procreate, though this may change later.
            //if (!activeSocietyCells.containsKey()) {
                SocietyCell societyCell = modelStructure.getCoordinate(point);
                int percentageCapacity = societyCell.getSize() / societyCell.getNutrientCapacity() * 100;
                //The population and it's resources determine the probability of a societycell determining if it needs to reproduce. A high population and not a lot of food left means there is a lower chance of reproduction. Vice versa.
                int reproduceProbability;
                if (percentageCapacity < 20) {
                    reproduceProbability = 100;
                } else if (percentageCapacity < 50) {
                    reproduceProbability =  50;
                } else if (percentageCapacity < 80) {
                    reproduceProbability =  20;
                } else {
                    reproduceProbability =  10;
                }

                if (reproduceProbability > Math.random() * 100) {
                    LinkedList<Node> pathBetweenCouple = getPathBetweenCouple(societyCell);
                    if (activeSocietyCells.stream().map(LinkedList::peek).filter(Objects::nonNull).noneMatch(e -> e.equals(pathBetweenCouple.peek()))){
                        activeSocietyCells.add(pathBetweenCouple);
                    }
                }
            //}
        }
        //For any society cells deciding to populate, we must make the life cells go together. Though there is a chance of reproduction each time tick, we want to guarantee that the life cells try to go together each time tick.
        //unison();
    }

    private LinkedList<Node> getPathBetweenCouple(SocietyCell societyCell) {
        Point[] couple = selectCoupleLifeCell(societyCell);
        return Pathfind.aStar(250, modelStructure, couple[0], couple[1]);
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
