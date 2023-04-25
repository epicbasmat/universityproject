package org.basmat.map.view;

import org.basmat.map.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * CellMatrixPanel provides a container to child all individual CellPanels, extending a JPanel. This JPanel also deals with mouse events.
 * @see CellPanel
 */
public class SimulationUI extends JPanel implements MouseListener {
    private final Controller controller;
    private GridBagConstraints c;
    private CellPanel[][] cellPanelMatrix;

    /**
     * @param matrixWidth the width of the matrix
     * @param matrixHeight the height of the matrix
     * @param controller the cell controller matrix that instantiated this object
     */
    public SimulationUI(int matrixWidth, int matrixHeight, Controller controller) {
        cellPanelMatrix = new CellPanel[matrixWidth][matrixHeight];
        this.controller = controller;
        setVisible(true);
        c = new GridBagConstraints();
        setLayout(new GridBagLayout());
        this.setMinimumSize(new Dimension(matrixWidth * 5, matrixHeight * 5));
    }

    /**
     * Removes the selected CellPanel from the matrix, de-rendering it from this object's JFrame
     * @param point the point to remove
     */
    public void removeCell(Point point) {
        remove(cellPanelMatrix[point.x][point.y]);
        revalidate();
    }

    /**
     * Overwrites the cellpanel from a given point, and replaces it
     * @param point The point to replace
     * @param replacement The CellPanel to replace it with
     */
    public void overwriteCell(Point point, CellPanel replacement) {
        removeCell(point);
        addCellPanel(replacement, point);
    }

    /**
     * Adds the CellPanel to the point
     * @param cellPanel The CellPanel to set
     * @param point The point to set it at
     */
    public void addCellPanel(CellPanel cellPanel, Point point) {
        c.gridx = point.x;
        c.gridy = point.y;
        this.cellPanelMatrix[point.x][point.y] = cellPanel;
        //Make the spacing between elements 0 (default is 5px?)
        c.ipadx = -5;
        c.ipady = -5;
        this.add(cellPanel, c);
        this.revalidate();
    }

    public void refreshImage() {
        this.revalidate();
    }


    /**
     * Gets the CellPanel at the designated point within the internal array
     * @param point The point to get
     * @return The specified CellPanel
     */
    public CellPanel getPanel(Point point) {
        return cellPanelMatrix[point.x][point.y];
    }

    /**
     *
     * @param toRemove The point that needs to be removed
     * @param toReplaceRemoved The CellPanel that replaces the coordinate at replaceAt
     * @param replaceAt The point to replace with the new CellPanel
     */
    public void getAndReplace(Point toRemove, CellPanel toReplaceRemoved, Point replaceAt){
        CellPanel cellPanel = getPanel(toRemove);
        removeCell(toRemove);
        removeCell(replaceAt);
        addCellPanel(toReplaceRemoved, toRemove);
        addCellPanel(cellPanel, replaceAt);
        revalidate();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //Get the amount of total space occupied by the frame and subtract it from the simulation size (real size), then /2 to create one half of that space
        Dimension difference = new Dimension((int) (this.getSize().getWidth() - 750) / 2, (int) (this.getSize().getHeight() - 750) / 2);
        //deduct the difference from the clicked point to align it to (0,0), using quotients to floor the multiple of 5
        //-33 for some reason? not sure why
        controller.displayData(new Point((e.getPoint().x - difference.width) / 5 - 33, (e.getPoint().y - difference.height) / 5));
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
