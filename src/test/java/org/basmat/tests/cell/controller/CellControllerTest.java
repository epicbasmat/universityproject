package org.basmat.tests.cell.controller;

import org.basmat.cell.controller.CellMatrixController;
import org.basmat.cell.util.ECellType;
import org.basmat.cell.data.SocietyCell;
import org.basmat.cell.data.WorldCell;
import org.basmat.cell.view.CellMatrixPanel;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

class CellControllerTest {
    private CellMatrixController cmc;
    private CellMatrixPanel cmp;
    //private CellController<WorldCell> cc;

    CellControllerTest() {
        try {
            cmc = new CellMatrixController(150, 150);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        cmp = new CellMatrixPanel(150, 150, cmc);
    }

    @Test
    void SetCellPanelToCellMatrixPanel_CellMatrixJPanel_True() {
        
    }
}