package org.basmat.cell;

import org.basmat.mapgen.SemaphoreManagement;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;



public class Cell extends JPanel implements MouseListener{

	private static final long serialVersionUID = -3017287192477717039L;
	private ECellType cellType;
	private Graphics2D g2d;
	private SemaphoreManagement sm;
	private boolean latch;
	private BufferedImage currentTexture;

	public Cell(ECellType cellType, SemaphoreManagement sm) {
		this.cellType = cellType;
		this.sm = sm;
		setSize(5, 5);
        setVisible(true);
        latch = true;
        try {
			currentTexture = ImageIO.read(new File(cellType.getPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Cell(SemaphoreManagement sm) {
		this.cellType = ECellType.UNINHABITABLE;
		this.sm = sm;
		setSize(5, 5);
        setVisible(true);
        latch = true;
        try {
			currentTexture = ImageIO.read(new File(cellType.getPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public BufferedImage getTexture() {
		return this.currentTexture;
	}
	
	public ECellType getCellType() {
		return cellType;
	}
	
	public void setCellType(ECellType cellType) {
		this.cellType = cellType;
		try {
			currentTexture = ImageIO.read(new File(cellType.getPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tints a texture according to the bitwise operator and mask, if needed
	 * @param mask The mask to be applied to the texture
	 * @param operand The operand to be applied to the texture
	 *                1 = 2's complement
	 *                2 = AND
	 *                3 = XOR
	 */
	public void setTint(int mask, int operand) {
		for (int x = 0; x < currentTexture.getWidth(); x++) {
			for (int y = 0; y < currentTexture.getHeight(); y++) {
				switch (operand) {
					case 1:
						currentTexture.setRGB(x, y, ~currentTexture.getRGB(x, y));
						break;
					case 2:
						currentTexture.setRGB(x, y, currentTexture.getRGB(x, y) & mask);
						break;
					case 3:
						currentTexture.setRGB(x, y, currentTexture.getRGB(x, y) ^ mask);


				}
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) { 
		g2d = (Graphics2D) g;
		//TODO: Cache textures
		if (g2d.drawImage(currentTexture, 0, 0, null) == true && latch == true) {
			sm.increment();
			latch = false;
		}
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Point p = e.getPoint();
		if (((p.getX() < 0) &&  (p.getX() < 5)) && ((p.getY() < 0) && (p.getY() < 5))) {
			System.out.println(cellType.getLocalizedName());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
