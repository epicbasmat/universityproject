package org.basmat.cell.view;

import org.basmat.cell.controller.CellMatrixController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CellMatrixPanel extends JPanel implements MouseListener{
    private GridBagConstraints c;
    private CellMatrixController cellMatrixController;
    public CellMatrixPanel(int sizeX, int sizeY, CellMatrixController cellMatrixController) {
        setSize(sizeX * 5, sizeY * 5);
        this.cellMatrixController = cellMatrixController;
        setVisible(true);
        c = new GridBagConstraints();
        setLayout(new GridBagLayout());
        //c.fill = GridBagConstraints.BOTH;
    }

    public void removeCell(CellPanel cellPanel) {
        remove(cellPanel);
    }
    public void addCellPanel(CellPanel cellPanel, int x, int y) {
        c.gridx = x;
        c.gridy = y;
        c.ipadx = -5;
        c.ipady = -5;
        this.add(cellPanel, c);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        cellMatrixController.displayData(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
