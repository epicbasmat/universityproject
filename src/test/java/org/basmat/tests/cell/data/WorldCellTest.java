package org.basmat.tests.cell.data;

import org.basmat.cell.util.ECellType;
import org.basmat.cell.data.SocietyCell;
import org.basmat.cell.data.WorldCell;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorldCellTest {

    /**
     * The naming convention will be as follows: MethodDescription_StateUnderTest_ExpectedBehaviour
     */
    WorldCell grass;
    WorldCell water;
    WorldCell stone;

    WorldCellTest() {
        grass = new WorldCell(ECellType.GRASS, null);
        water = new WorldCell(ECellType.WATER, null);
        stone = new WorldCell(ECellType.MOUNTAIN_BODY, null);
    }

    @Test
    void WorldCellSetsAppropriateEnum_CellDataECellType_True () {
        assertEquals(ECellType.GRASS, grass.getCellType());
        assertEquals(ECellType.WATER, water.getCellType());
        assertEquals(ECellType.MOUNTAIN_BODY, stone.getCellType());

    }

    @Test
    void WorldCellReturnsNoOwner_WorldCellOwner_True() {
        assertNull(grass.getOwner());
    }

    @Test
    void TestWorldCellReturnsOwner_WorldCellOwner_True() {
        SocietyCell sc = new SocietyCell("Test", ECellType.SOCIETY_CELL);
        water.setOwner(sc);
        assertEquals(water.getOwner(), sc);
    }
}