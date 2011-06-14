package com.sudoku;

import java.io.Serializable;

public class SGrid implements Serializable {
    
    public int[][] grid;
    public int difficulty;
    
    public SGrid() {
        grid = new int[9][9];
    }

}
