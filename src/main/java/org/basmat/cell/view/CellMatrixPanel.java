package org.basmat.cell.view;

import org.basmat.cell.controller.CellMatrixController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * CellMatrixPanel provides a container to child all individual CellPanels, extending a JFrame.
 * @see CellPanel
 */
public class CellMatrixPanel extends JPanel implements MouseListener{
    private GridBagConstraints c;
    private CellMatrixController cellMatrixController;

    /**
     * Instantiate the object with current parameters
     * @param cellWidth the width of the matrix
     * @param cellHeight the height of the matrix
     * @param cellMatrixController
     */
    public CellMatrixPanel(int cellWidth, int cellHeight, CellMatrixController cellMatrixController) {
        setSize(cellWidth * 5, cellHeight * 5);
        this.cellMatrixController = cellMatrixController;
        setVisible(true);
        c = new GridBagConstraints();
        setLayout(new GridBagLayout());
        //c.fill = GridBagConstraints.BOTH;
    }

    /**
     * Removes the selected CellPanel from the matrix, de-rendering it from this object's JFrame
     * @param cellPanel the CellPanel to remove
     */
    public void removeCell(CellPanel cellPanel) {
        remove(cellPanel);
    }

    /**
     *
     * @param cellPanel the CellPanel to add to the CellMatrix
     * @param x the x co-ordinate to set the CellPanel
     * @param y the y co-ordinate to set the CellPanel
     */
    public void addCellPanel(CellPanel cellPanel, int x, int y) {
        c.gridx = x;
        c.gridy = y;
        //Make the spacing between elements 0 (default is 5px?)
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
