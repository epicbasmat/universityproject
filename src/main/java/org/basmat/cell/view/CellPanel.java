package org.basmat.cell.view;

import org.basmat.cell.controller.CellController;
import org.basmat.cell.data.CellData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * CellPanel provides the view implementation for M-C-V for a specific cell, rendering an image provided a texture.
 */
public class CellPanel extends JPanel implements MouseListener {
    private BufferedImage texture;
    private CellController cellMatrix;
    private Graphics2D g2d;

    /**
     * Instantiate CellPanel with associated parameters
     * @param texture the texture to render for the cell
     * @param cellController the parent controller that instantiated the class.
     */
    public <ChildCell extends CellData> CellPanel(BufferedImage texture, CellController<ChildCell> cellController ) {
        setSize(texture.getWidth(), texture.getHeight());
        setVisible(true);
        //To copy a texture to be original for the class
        this.texture = new BufferedImage(texture.getColorModel(),
                texture.copyData(null),
                texture.getColorModel().isAlphaPremultiplied(),
                null);
        this.cellMatrix = cellController;
    }

    /**
     * Tints the loaded bufferedimage from the view.
     * @param mask The RGB value to mask the image with using an OR operator. \n 0xAARRGGBB - AA = Alpha, RR = Red, GG = Green, BB = Blue
     */
    public void setTint(int mask) {
        for (int x = 0; x < texture.getWidth(); x++) {
            for (int y = 0; y < texture.getHeight(); y++) {
                texture.setRGB(x, y , texture.getRGB(x, y) | mask);
            }
        }
        repaint();
    }

    public void updateTexture(BufferedImage image) {
        this.texture = image;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        g2d = (Graphics2D) g;
        if (g2d.drawImage(texture, 0, 0, null)) { }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        if (((p.getX() < 0) &&  (p.getX() < 5)) && ((p.getY() < 0) && (p.getY() < 5))) {
            System.out.println(cellMatrix.getStringFromModel());
        }
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
