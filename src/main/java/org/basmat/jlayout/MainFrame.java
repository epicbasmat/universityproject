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
					for (int i = 0; i < 20; i++) {
						int j = (int)(Math.random()*(150-1 - 1+1)+1);
						int k = (int)(Math.random()*(150-1 - 1+1)+1);
						
						Cell selectedCell = cellMatrix[j][k];
						if (selectedCell.getCellType() == ECellType.GRASS) {
							selectedCell.setCellType(ECellType.SOCIETYBLOCK);
							int radius = 12;
							int tempRadius = radius;

							for (int y = 0; y <= 180; y = y + 5) {
								int circumferenceX = (int) (radius  * Math.cos(y * 3.141519 / 180));
								int circumferenceY = (int) (radius  * Math.sin(y * 3.141519 / 180));
								try{
									for (int b = -circumferenceY; b <= circumferenceY; b++) {
										for (int a = -circumferenceX; a <= circumferenceX; a++) {
											Cell tempCell = cellMatrix[b + j][a + k];
											if (tempCell.getOwner() == "" && tempCell.getCellType().isHabitable()) {
												tempCell.setOwner("test");
												tempCell.setTint(0, 1);
											}
										}
									}
								} catch (Exception e) {
								}
							}

						}
				}
	    	} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		});



	    setVisible(true);
	}
}


