package org.basmat.tests.cell.data;

import org.basmat.cell.data.ECellType;
import org.basmat.cell.data.NutrientCell;
import org.basmat.cell.data.SocietyCell;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NutrientCellTest {

    /**
     * The naming convention will be as follows: MethodDescription_StateUnderTest_ExpectedBehaviour
     */
    NutrientCell nc;
    NutrientCell ncOwner;
    NutrientCellTest() {
        nc = new NutrientCell();
        ncOwner = new NutrientCell(new SocietyCell("Test"));

    }

    @Test
    void NutrientCellSetsAppropriateEnum_CellDataECellType_True() {
        assertEquals(ECellType.NUTRIENTS, nc.getCellType());
    }

    @Test
    void NutrientCellReturnsNoOwner_NutrientCellOwner_True() {
        assertNull(nc.getOwner());
    }

    @Test
    void NutrientCellReturnsOwner_NutrientCellOwner_True() {
        assertNotNull(ncOwner.getOwner());
    }

    @Test
    void NutrientCellReturnsNoOwnerInfo_NutrientCellToString_True() {
        assertEquals("Owner: This cell has no owner\n" +
                "Cell Name: " + ECellType.NUTRIENTS.getCellName() + "\n" +
                "Cell Description: " + ECellType.NUTRIENTS.getCellDescription(), nc.toString());
    }

    @Test
    void NutrientCellReturnsOwnerInfo_NutrientCellToString_True() {
        assertEquals("Owner: " + ncOwner.getOwner().getName() + "\n" +
                "Cell Name: " + ECellType.NUTRIENTS.getCellName() + "\n" +
                "Cell Description: " + ECellType.NUTRIENTS.getCellDescription(), ncOwner.toString());
    }
}