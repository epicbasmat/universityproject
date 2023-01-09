package org.basmat.mapgen;

import java.util.concurrent.Semaphore;

public class SemaphoreManagement {
	
	private int target;
	private int current;
	private Semaphore s;
	
	public SemaphoreManagement(Semaphore s, int target) {
		this.target = target;
		this.s = s;
		current = 0;
		System.out.println("Good morning vietnam");
	}
	
	public void increment() {
		current++;
		if (target >= current) {
			s.release();
		} 
	}
	
}
