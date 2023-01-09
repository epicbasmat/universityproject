package org.basmat.jlayout;

import org.basmat.cell.Cell;
import org.basmat.cell.ECellType;
import org.basmat.mapgen.CubicInterpolation;
import org.basmat.mapgen.SemaphoreManagement;
import org.basmat.mapgen.TextureFilter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class MainFrame extends JFrame {

	private int frameSizeX;
	private int frameSizeY;
	private int cellSize;
	private int matrixSizeX;
	private int matrixSizeY;
	private Semaphore semaphore;
	
	private static final long serialVersionUID = -472448969254685719L;
	
	public MainFrame() throws InterruptedException {
		semaphore = new Semaphore(1);
		semaphore.acquire();
		System.out.println(semaphore.availablePermits());
		
		frameSizeX = 750;
		frameSizeY = 750;
		cellSize = 5;
		matrixSizeX = frameSizeX/5;
		matrixSizeY = frameSizeY/5;
		
		this.setPreferredSize(new Dimension(frameSizeX, frameSizeY));
		this.setTitle("Ecosystem");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(matrixSizeX, matrixSizeY));
		BufferedImage noiseGraph = new BufferedImage(frameSizeX, frameSizeY, BufferedImage.TYPE_INT_ARGB);
		CubicInterpolation ci = new CubicInterpolation();
		TextureFilter.setupColour(noiseGraph, ci, frameSizeX, frameSizeY);
		//Initialize texture applier
		Cell[][] cellMatrix = new Cell[matrixSizeX][matrixSizeY];
		SemaphoreManagement sm = new SemaphoreManagement(semaphore, matrixSizeX*matrixSizeY);
		 EventQueue.invokeLater(new Runnable() {
	    	@Override
	    	public void run() {
	    		for (int y = 0; y <= 750 - 5; y += 5) { 
	    			for (int z = 0; z <= 750 - 5; z += 5) {
	    				int average = 0;	
	    				for (int i = y; i < y + cellSize; i++) { 
	    					for (int j = z; j < z + cellSize; j++) {
	    						average += noiseGraph.getRGB(i, j);
	    					}
	    				}
	    				Color averagedColour = new Color(average /= cellSize * cellSize);
	    				Cell tempCell = new Cell(TextureFilter.setupCell(averagedColour), sm);
	    				cellMatrix[y/cellSize][z/cellSize] = tempCell;
	    				add(tempCell);
	    			}
	    		}
	    	}
		});
		
	    pack();
	    
	    EventQueue.invokeLater(new Runnable() {
	    	@Override
	    	public void run() {
	    		try {
	    			System.out.println("Semaphore aquisition in progress");
	    			System.out.println(semaphore.availablePermits());
					semaphore.acquire();
					System.out.println("First phase completed");
					for (int i = 0; i < 50; i++) {
						int j = (int)(Math.random()*(150-1 - 1+1)+1);
						int k = (int)(Math.random()*(150-1 - 1+1)+1);
						
						Cell selectedCell = cellMatrix[j][k];
						if (selectedCell.getCellType() == ECellType.GRASS) {
							selectedCell.setCellType(ECellType.SOCIETYBLOCK);
							
							int radius = 12;
							int tempRadius = radius;
							for (int r = 0; r < tempRadius ; r++) {
								int circleX = radius;
								int circleY = 0;
								int p = 1 - radius;
								while (circleX > circleY) {
									circleY++;
									//Mid point inside/on perimeter
									if (p <= 0) {
										p = p + 2 * circleY + 1;
									} 
									//Midpoint outside perimeter
									else {
										circleX--;
										p = p + 2 * circleY - 2 * circleX + 1;
									};
									if (circleX < circleY) {
										break;
									}
									try {
										if (cellMatrix[circleX + j][circleY + k].getCellType().isHabitable()) {cellMatrix[circleX + j][circleY + k].setTint(0x00ff10ff, 1);}
										if (cellMatrix[-circleX + j][circleY + k].getCellType().isHabitable()) {cellMatrix[-circleX + j][circleY + k].setTint(0x00ff10ff, 1);}
										if (cellMatrix[circleX + j][-circleY + k].getCellType().isHabitable()) {cellMatrix[circleX + j][-circleY + k].setTint(0x00ff10ff, 1);}
										if (cellMatrix[-circleX + j][-circleY + k].getCellType().isHabitable()) {cellMatrix[-circleX + j][-circleY + k].setTint(0x00ff10ff, 1);}
										if (circleX != circleY) {
											if(cellMatrix[circleY + j][circleX + k].getCellType().isHabitable()) {cellMatrix[circleY + j][circleX + k].setTint(0x00ff10ff,1 );}
											if(cellMatrix[-circleY + j][circleX + k].getCellType().isHabitable()) {cellMatrix[-circleY + j][circleX + k].setTint(0x00ff10ff,1 );}
											if(cellMatrix[circleY + j][-circleX + k].getCellType().isHabitable()) {cellMatrix[circleY + j][-circleX + k].setTint(0x00ff10ff,1 );}
											if(cellMatrix[-circleY + j][-circleX + k].getCellType().isHabitable()) {cellMatrix[-circleY + j][-circleX + k].setTint(0x00ff10ff,1 );} else {break;};

										} else {
											break;
										}
										
									} catch (Exception e) {
										System.out.println("Error: " + e.getMessage());
									}
									
								}
								radius--;
							} 
						} 
					}
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    	} 
	    });
	    setVisible(true);
	}
}


