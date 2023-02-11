package org.basmat.test.factory;

import org.basmat.map.cellfactory.CellFactory;
import org.basmat.map.cellfactory.cells.NutrientCell;
import org.basmat.map.cellfactory.cells.SocietyCell;
import org.basmat.map.cellfactory.cells.WorldCell;
import org.basmat.map.controller.MVBinder;
import org.basmat.map.util.ECellType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class FactoryTest {

    private final CellFactory cellFactory;
    private final int id;

    private HashMap<Integer, MVBinder<?>> bindingAgent;

    /*
     * The naming convention will be as follows: MethodDescription_StateUnderTest_ExpectedBehaviour
     */

    FactoryTest() {
        cellFactory = new CellFactory();
        id = 0;
        bindingAgent = new HashMap<>();
    }

    @Test
    public void NutrientCellCreation_ReturnNutrientCell_True() {
        assertInstanceOf(NutrientCell.class, cellFactory.createNutrientCell(null, id));
    }

    @Test
    public void SocietyCellCreation_ReturnSocietyCell_True() {
        assertInstanceOf(SocietyCell.class, cellFactory.createSocietyCell("Name", id + 1));
    }

    @Test
    public void WorldCellCreation_ReturnWorldCell_True() {
        assertInstanceOf(WorldCell.class, cellFactory.createWorldCell(ECellType.GRASS, null, id + 2));

    }

    @Test
    public void WorldCellDoesNotHaveOwner_WorldCellOwner_True() {
        assertNull(cellFactory.createWorldCell(ECellType.GRASS, null, 0).getOwner());
    }

    @Test
    public void SocietyHasName_SocietyName_True() {
        assertEquals("test", cellFactory.createSocietyCell("test", 0).getName());
    }

    @Test
    public void MaximumAmountOfSupportedCellsFromNutrients_NutrientSupportedCount_True() {
        NutrientCell nc = cellFactory.createNutrientCell(null, 0);
        nc.setCapacity(12);
        assertEquals(12, nc.getCapacity());
    }

    @Test
    public void ToStringReturnsExpectedNutrientCellNoOwner_WorldCellToString_True() {
        assertEquals("Owner: This cell has no owner\n" +  "Cell Name: " + ECellType.NUTRIENTS.getCellName() + "\n" +
                "Cell Description: " + ECellType.NUTRIENTS.getCellDescription(), cellFactory.createNutrientCell(null, 0).toString());
    }

    @Test
    public void ToStringReturnsExpectedNutrientCellHasOwner_WorldCellToString_True() {
        SocietyCell owner = cellFactory.createSocietyCell("test2", 0);
        assertEquals("Owner: " + owner.getName() + "\n" + "Cell Name: " + ECellType.NUTRIENTS.getCellName() + "\n" +
                "Cell Description: " + ECellType.NUTRIENTS.getCellDescription(), cellFactory.createNutrientCell(owner, 0).toString());
    }

    @Test
    public void SetWorldCellType_WorldCellECellType_True() {
        assertEquals(ECellType.GRASS, cellFactory.createWorldCell(ECellType.GRASS, null, 0).getECellType());
    }

}
