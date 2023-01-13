package org.basmat.cell.view;

import org.basmat.cell.controller.CellController;
import org.basmat.cell.data.CellData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class CellPanel extends JPanel implements MouseListener {
    private BufferedImage texture;
    private CellController cellMatrix;
    private Graphics2D g2d;

    public <ChildCell extends CellData> CellPanel(BufferedImage texture, CellController<ChildCell> cellController) {
        setSize(texture.getWidth(), texture.getHeight());
        setVisible(true);
        //To copy a texture to be original for the class
        this.texture = new BufferedImage(texture.getColorModel(),
                texture.copyData(null),
                texture.getColorModel().isAlphaPremultiplied(),
                null);
        this.cellMatrix = cellController;
    }

    public void setTint(int mask, int operand) {
        for (int x = 0; x < texture.getWidth(); x++) {
            for (int y = 0; y < texture.getHeight(); y++) {
                switch (operand) {
                    case 1:
                        texture.setRGB(x, y, ~texture.getRGB(x, y));
                        break;
                    case 2:
                        texture.setRGB(x, y, texture.getRGB(x, y) & mask);
                        break;
                    case 3:
                        texture.setRGB(x, y, texture.getRGB(x, y) ^ mask);
                }
            }
        }
    }

    public void updateTexture(BufferedImage image) {
        this.texture = image;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        g2d = (Graphics2D) g;
        if (g2d.drawImage(texture, 0, 0, null)) {
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

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
