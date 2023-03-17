package org.basmat.map.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * CellPanel provides the view implementation for M-C-V for a specific cell, rendering an image provided a texture.
 */
public class CellPanel extends JPanel {
    private BufferedImage texture;
    private Graphics2D g2d;

    /**
     * Instantiate CellPanel with associated parameters
     * @param texture the texture to render for the cell
     */
    public CellPanel(BufferedImage texture) {
        setSize(texture.getWidth(), texture.getHeight());
        setVisible(true);
        this.texture = texture;
    }

    @Override
    public void paintComponent(Graphics g) {
        g2d = (Graphics2D) g;
        if (g2d.drawImage(texture, 0, 0, null)) { }
    }
}
