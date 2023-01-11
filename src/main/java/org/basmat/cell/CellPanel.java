package org.basmat.cell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class CellPanel extends JPanel implements MouseListener {
    private BufferedImage image;
    private Graphics2D g2d;
    private CellController cellController;
    CellPanel(int sizeX, int sizeY, BufferedImage image) {
        setSize(sizeX, sizeY);
        setVisible(true);
        this.image = image;
    }

    public void updateTexture(BufferedImage image) {
        this.image = image;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        g2d = (Graphics2D) g;
        if (g2d.drawImage(image, 0, 0, null) == true) {

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
