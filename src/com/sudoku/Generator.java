package com.sudoku;

import java.util.EmptyStackException;
import java.util.Random;
import java.util.Stack;

public class Generator {

    private GridGame grid;
    private Stack<GeneratorMove> st;
    private Random random;
    
    public Generator() {
        this(new GridGame());
    } 
    
    public Generator(GridGame grid) {
        this.grid = grid;
        st = new Stack<GeneratorMove>();
        random = new Random();
    }

    public GridGame getGrid() {
        return this.grid;
    }

    //RETURN BOOLEAN ??? TODO
    public boolean solveGrid() {
        st.clear();
        GeneratorMove m = grid.getFirstMove();
        if(m != null) {
            grid.setGridVal(m.getX(),m.getY(),m.getVal());
            st.push(m);
            return solveGridIterative(m);
        }
        return true;
    }

    private boolean solveGridIterative(GeneratorMove m) {
        int x = m.getX(); 
        int y = m.getY();
        if(!grid.isGridValid()) {
            return false;
        }

        while(!grid.isGridSolved()) {

            GeneratorMove next = grid.getNextMove(x,y);
            if(next != null) {
                grid.setGridVal(next.getX(),next.getY(),next.getVal());
                st.push(next);
                y = next.getY();
                x = next.getX();
            } else {
                try{
                    next = st.pop();
                    while(next.setNextMove() == false) {
                        grid.setGridVal(next.getX(),next.getY(),0);
                        next = st.pop(); 
                    }
                    grid.setGridVal(next.getX(),next.getY(),next.getVal());
                    st.push(next);
                    y = next.getY();
                    x = next.getX();
                } catch (EmptyStackException e) {
                    return false;
                } 
            }
        } 
        return true;
    }
    
    public boolean solvePuzzle() {
        
        if(!grid.isGridSolved()) {
            if(!solveGrid()) return false;
        }
        showSolution();
        return true;
    }
    
    private void showSolution() {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                grid.setEditable(j,i,true);
                grid.deleteAllNotes(j,i);
                grid.setPuzzleVal(j,i,0);
                grid.setPuzzleVal(j,i,grid.getGridVal(j,i));
                grid.setEditable(j,i,false);
            }
        }
    }
    
    public void generateGrid() {
        generateFirst9Moves();
        solveGrid();
        for(int i = 0; i < 9; i++) {
            grid.setEditable(0,i,true);
            grid.setDefault(0,i,false);
        }
    }
    
    private void generateFirst9Moves() {
        boolean[] b = new boolean[9];
        for(int i = 0; i < 9; i++) {
            int val;
            do {
                val = random.nextInt(9);
            } while(b[val]);
            grid.setGridVal(0,i,val+1);
            grid.setDefault(0,i,true);
            b[val] = true;
        }
        b = null;
    }

    public void generatePuzzle(int openFields) {
        generateGrid();

        int[] count = new int[9];
        int min = openFields / 9;
        int x = 0, y = 0, sqAdr = 0;

        int k = 0;
        for (int j = 0; j < openFields;) {
            k++;
            y = random.nextInt(9);
            x = random.nextInt(9);
            if(grid.isDefault(x,y)) continue;
            
            sqAdr = 3*(x/3)+(y/3);
            
            grid.setDefault(x,y,true);            
            j++;
        }
        grid.clearNonDefaultCells();
        count = null;
    }
    
    private boolean allHaveMinCount(int[] count,int min) {
        for (int i = 0; i < count.length; i++) {
            if(count[i] < min) return false;
        }
        return true;
    }
}
