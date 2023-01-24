package org.basmat;

import org.basmat.cell.controller.CellMatrixController;
import org.basmat.cell.view.CellPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        CellMatrixController cellMatrixController = new CellMatrixController(150, 150);
        SwingUtilities.invokeLater(() -> {
            try {
                cellMatrixController.setupMap();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}