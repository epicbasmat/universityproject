package org.basmat;

import org.basmat.cell.controller.CellMatrixController;
import org.basmat.cell.view.CellPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        SwingUtilities.invokeLater(() -> {
            try {
                new CellMatrixController(150, 150);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}