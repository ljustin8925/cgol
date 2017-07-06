package com.ljustin.games.cgol;

import java.io.IOException;
import java.util.Random;

public class App {
	static final int GRID_SIZE = 30;
	static Pair[] relNeighbor = { new Pair(-1,-1), new Pair(0,-1), new Pair(1,-1), new Pair(-1,0),
			new Pair(1, 0), new Pair(-1,1), new Pair(0,1), new Pair(1,1)};
	
	public static void main(String[] args) throws InterruptedException {
		Cell grid[][] = new Cell[GRID_SIZE][GRID_SIZE];
		for (int i=0; i<grid.length; i++) {
			for (int j=0; j<grid.length; j++) {
				grid[i][j] = new Cell();
			}
		}
			
		seedGrid(grid);
		display(grid);
		int tick = 100;
		while (true) {
			display(grid);
			Thread.currentThread().sleep(tick);
			applyRule(grid);
		}
	}
	
	public static void seedGrid(Cell[][] grid) {
		for (int i=0; i<grid.length; i++) {
			for (int j=0; j<grid.length; j++) {
				int rand = new Random().nextInt(5000);
				if (rand%2==0) {
					grid[i][j].state = true;
				}
			}
		}
	}
	
	private static boolean validCoord(Cell[][] grid, int x, int y) {
		return !((x < 0 || x >= grid.length) || (y < 0 || y >= grid.length));
	}
	
	private static void applyRule(Cell[][] grid) {
		for (int i=0; i<grid.length; i++) {
			for (int j=0; j<grid.length; j++) {
				int neighbors = neighbors(grid, i, j);
				if (grid[i][j].state) {
					if (neighbors < 2 || neighbors > 3) {
						grid[i][j].nextState = -1;
					} else {
						grid[i][j].nextState = 1;
					}
				} else {
					if (neighbors == 3) {
						grid[i][j].nextState = 1;
					} else {
						grid[i][j].nextState = -1;
					}
				}
			}
		}
		// init grid
		for (int i=0; i<grid.length; i++) {
			for (int j=0; j<grid.length; j++) {
				grid[i][j].state = grid[i][j].nextState==1;
			}
		}
	}
	
	private static int neighbors(Cell[][] grid, int x, int y) {
		if (!validCoord(grid, x,y)) {
			return 0;
		}
		int neighbors = 0;
		for (int i=0; i<relNeighbor.length; i++) {
			int newX = relNeighbor[i].p1 + x;
			int newY = relNeighbor[i].p2 + y;
			if (validCoord(grid, newX, newY)) {
				if (grid[newX][newY].state) {
					neighbors++;
				}
			}
		}
		// (-1, -1), (0, -1), (1, -1)
		// (-1, 0), (1, 0)
		// (-1, 1), (0, 1), (1, 1)
		return neighbors;
	}

	public static void display(Cell[][] grid) {
		cls();
		for (int i=0; i<grid.length; i++) {
			for (int j=0; j<grid.length; j++) {
				if (j != grid.length) {
					System.out.print("|");
				}
				System.out.print(grid[i][j].state ? "x" : " ");
			}
			System.out.print("|");
			System.out.println();
		}
	}
	
	public static void cls() {
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class Pair {
	int p1;
	int p2;
	Pair(int p1, int p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
}

class Cell {
	boolean state;
	// 0 - unknown
	// -1 - die
	// 1 - live
	int nextState;
}