package org.basmat.tests.cell.controller;

import org.basmat.map.controller.CellMatrixController;
import org.basmat.map.view.CellMatrixPanel;
import org.junit.jupiter.api.Test;

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