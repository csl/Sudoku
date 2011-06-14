package com.sudoku;

import javax.swing.JButton;

public class URStack {

    private int[] newStack;
    private int[] oldStack;
    private int start;
    private int end;
    private int popBack;
    private int size;
    
    public URStack(int size) {
        if(size < 3) {
            throw new IllegalArgumentException("Stack size must be atleast 3.");
        }
        newStack = new int[size];
        oldStack = new int[size];
        start = popBack =  0;
        end = size - 1;
        this.size = size;
    }
    
    private void updateButttons() {
        int nextStart = start -1;
        
        if(nextStart < 0) {
            nextStart = size - 1;
        }
    }
     
    public void push(int oldVal, int newVal) {
        start = (start + 1) % size;
        popBack = start;
        if(start == end) {
            end = (end + 1) % size;
        }
        newStack[start] = newVal;
        oldStack[start] = oldVal;
        updateButttons();
    }
    
    public int undo() {
        int r = oldStack[start];
        start--;
        if(start < 0) {
            start = size-1;
        }
        
        if(start == end) {
            start = (start + 1) % size;
        }
        updateButttons();
        return r;
    }
    
    public int redo() {
        if(popBack == start) {
            updateButttons();
            return -1;
        } else {
            start = (start + 1) % size;
            updateButttons();
            return newStack[start];
        }
    }
    
    public void reset() {
        start = popBack = 0;
        end = size - 1;
    }
}
