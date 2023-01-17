package org.basmat.mapgen;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.util.concurrent.Semaphore;

import org.basmat.cell.data.ECellType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.basmat.cell.*;

class CellTests {
	
	/**
	 * The naming convention will be as follows: MethodDescription_StateUnderTest_ExpectedBehaviour
	 */

	private SemaphoreManagement sm;
	
	@BeforeAll
	static void setup() {
		Semaphore testSemaphore = new Semaphore(1);
		SemaphoreManagement sm = new SemaphoreManagement(testSemaphore, 25);
	}
	
	@Test
	void isEqualNoEnumGiven_ECellTypeUninhabitable_True() {
		Cell cell = new Cell(sm);
		assertEquals(cell.getCellType(), ECellType.UNINHABITABLE);
	}
	
	@Test
	void isEqualParamGiven_ECellTypeUninhabitable_True() {
		Cell cell = new Cell(ECellType.UNINHABITABLE, sm);
		assertEquals(cell.getCellType(), ECellType.UNINHABITABLE);
	}
	
	@Test
	void returnsExpectedPath_ECellTypeUninhabitable_True() {
		assertEquals(ECellType.UNINHABITABLE.getPath(), "./assets/uninhabitable.png");
	}
	
	@Test
	void returnsExpectedLocalizedName_ECellTypeUninhabitable_True() {
		assertEquals(ECellType.UNINHABITABLE.getLocalizedName(), "Untraversable terrain");
	}
	
	@Test
	void returnsUpdatedCellType_ECellTypeUninhabitable_True() {
		Cell cell = new Cell(ECellType.UNINHABITABLE, sm);
		cell.setCellType(ECellType.GRADIENT);
		assertEquals(ECellType.GRADIENT, cell.getCellType());
	}

	@Test 
	void textureUpdates_CellCurrentTexture_True() {
		Cell cell = new Cell(ECellType.UNINHABITABLE, sm);
		BufferedImage oldTexture = cell.getTexture();
		cell.setCellType(ECellType.GRASS);
		BufferedImage newTexture = cell.getTexture();
		for (int x = 0; x > oldTexture.getWidth(); x++) {
			for (int y = 0; y > oldTexture.getHeight(); y++) {
				if (oldTexture.getRGB(x, y) == newTexture.getRGB(x, y)) {
					fail();
				}
			}
		}
		assertTrue(true);
	}

	@Test
	void tintsTexture_CurrentTexture_True() {
		Cell cell = new Cell(ECellType.GRASS, sm);
		BufferedImage oldTexture = cell.getTexture();
		cell.setTint(0x00ff10ff);
		BufferedImage newTexture = cell.getTexture();
		for (int x = 0; x > oldTexture.getWidth(); x++) {
			for (int y = 0; y > oldTexture.getHeight(); y++) {
				if (oldTexture.getRGB(x, y) == newTexture.getRGB(x, y)) {
					fail();
				}
			}
		}
		assertTrue(true);
	}

}
