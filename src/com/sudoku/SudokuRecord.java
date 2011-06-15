package com.sudoku;

import java.util.Random;

public class SudokuRecord {
    
    private int [][] grid;
    private int x;
    private int y;    
    private boolean win;
    
    protected SudokuRecord(GridGame obj, int mx, int my, boolean mwin)
    {
    	x = mx;
    	y = my;
    	win = mwin;
    	
    	grid = new int[x][];
    	
    	for (int i=0; i<x; i++)
    	{
        	grid[i] = new int[y];
        	for (int j=0; j<y; j++)
        	{
        		if (obj.getPuzzleVal(j, i) == 0)
            		grid[i][j] = obj.getGridVal(j, i);
        		else
        			grid[i][j] = obj.getPuzzleVal(j, i);
        	}
    	}

    }
    
    public int[][] getGrid() {
        return grid;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean getWin() {
        return win;
    }
}
