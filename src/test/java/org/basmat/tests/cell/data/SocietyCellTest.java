package org.basmat.tests.cell.data;

import org.basmat.cell.data.ECellType;
import org.basmat.cell.data.NutrientCell;
import org.basmat.cell.data.SocietyCell;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SocietyCellTest {


    /**
     * The naming convention will be as follows: MethodDescription_StateUnderTest_ExpectedBehaviour
     */

    private SocietyCell sc;

    SocietyCellTest() {
        sc = new SocietyCell("Test");
    }
    @Test
    void SocietyCellAddNutrientCell_NutrientCellHashMap_True() {
        NutrientCell nc = new NutrientCell(sc);
        sc.addNutrientCells(nc);
        assertTrue(sc.getNutrientCell(nc));
    }

    @Test
    void SocietyCellHasName_SocietyCellName_True() {
        assertEquals("Test", sc.getName());
    }

    @Test
    void SocietyCellSetsAppropriateEnum_CellDataECellType_True() {
        assertEquals(ECellType.SOCIETYBLOCK, sc.getCellType());
    }

    @Test
    void SocietyCellReturnsInfo_SocietyCellToString_True() {
        assertEquals("Society Name: "+ sc.getName() + "\n" +
                "Cell Name: Society Block\n" +
                "Cell Description: A town hall", sc.toString());
    }
}