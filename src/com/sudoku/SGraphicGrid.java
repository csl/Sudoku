/* SPanel created on 08.07.2006 */
package com.sudoku;

import javax.swing.BorderFactory;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SGraphicGrid extends JPanel implements SudokuQQ {
    
    private GridGame sgrid;

    private SudokuGuiCell[][] cells;
    
    private SudokuGuiCell lastCell;
    
    private Dimension dimCell;
    
    private URStack uRS;
    
    
    //argc
    public static final Color BORDER_COLOR = new Color(184, 207, 229);
    public static final Color GRID_COLOR_1 = new Color(230,255,255);
    public static final Color GRID_COLOR_2 = BORDER_COLOR;
    public static final Color GRID_COLOR_2_SELC = new Color(160,220,110);
    public static final Color GRID_COLOR_1_SELC = new Color(160,220,110);
    
    public final static int H1 = 12;
    public final static int W1 = 3;
    public final static int H2 = 29;
    public final static int W2 = 22;
    public final static int H3 = 48;
    public final static int W3 = 42;
    public final static int W4 = 35;
    
    private boolean pressIn;
    private int PX;
    private int PY;

    private boolean helpingLines; //JV
    
    private class SudokuGuiCell extends JPanel
        implements MouseListener, FocusListener, KeyListener  {
        
        private Color bg; 
        private Color bgSelected;
        private int sudokuRealGridval;
        private boolean isSelected;
        private boolean isSelectedRightMouseBut;
        private int x;
        private int y;
        
        public SudokuGuiCell(int x, int y, Color bg, Color bgSelected, int realGridVal) 
        {
            this.x = x;
            this.y = y;
            this.bg = bg;
            this.bgSelected = bgSelected;
            
            isSelected = false;
            sudokuRealGridval = realGridVal;
            
            setSize(dimCell);
            setPreferredSize(dimCell);
            setMaximumSize(dimCell);
            setBackground(bg);
            setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            
            addMouseListener(this);
            addKeyListener(this);
            this.setFocusable(true);
            removeFocusListener(this);
        }
        
        public void paint(Graphics g) 
        {
            super.paint(g);

            boolean isDefault = GridGame.isDefault(sudokuRealGridval);
            
                g.setColor(Color.BLUE);
                g.drawLine(2,2,2,49);
                g.drawLine(2,2,49,2);
                g.drawLine(2,49,49,49);
                g.drawLine(49,2,49,49);
                g.setColor(bg);
                g.setFont(new Font("Tahoma", Font.BOLD, 32));
            
            int val = 0;
            if(isDefault) {
                g.setColor(Color.BLACK);
                val = GridGame.getGridVal(sudokuRealGridval);
            } else {
                g.setColor(Color.BLUE);
                val = GridGame.getPuzzleVal(sudokuRealGridval);
            }
            
            g.drawString(val == 0 ? "" : "" + val, 15, 37);

            g.setFont( new Font("Tahoma", Font.BOLD, 11));
            
            g.drawString(noteToString(1, isDefault, sudokuRealGridval), W1, H1);
            g.drawString(noteToString(2, isDefault, sudokuRealGridval), W2, H1);
            g.drawString(noteToString(3, isDefault, sudokuRealGridval), W3, H1);
            g.drawString(noteToString(4, isDefault, sudokuRealGridval), W1, H2);
            g.drawString(noteToString(5, isDefault, sudokuRealGridval), W4, H2);
            g.drawString(noteToString(6, isDefault, sudokuRealGridval), W3, H2);
            g.drawString(noteToString(7, isDefault, sudokuRealGridval), W1, H3);
            g.drawString(noteToString(8, isDefault, sudokuRealGridval), W2, H3);
            g.drawString(noteToString(9, isDefault, sudokuRealGridval), W3, H3);
            
            if(isDefault) {
                setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                isSelected = false;
            }
            //System.out.println("PAINT X: " + gs.getX() +" Y: " + gs.getY());
        }
        
        private String noteToString(int val, boolean isDefault, int realGridVal) {
            if(GridGame.isDefault(realGridVal) || !GridGame.getNote(realGridVal, val)) {
                return "";
            } 
            return  "" + val;
        }
        
        protected void setRealGridVal(int val) {
            sudokuRealGridval = val;
        }
        
        protected boolean isSelected() {
            return isSelected;
        }
        
        protected boolean isSelectedRightMouseButton() {
            return isSelectedRightMouseBut;
        }

        public void mousePressed(MouseEvent arg0) {}

        public void mouseReleased(MouseEvent arg0) {}

        public void mouseEntered(MouseEvent arg0) {}

        public void mouseExited(MouseEvent arg0) {}
        
        public void mouseClicked(MouseEvent me) {
            
            if (me.getButton() == MouseEvent.BUTTON2) { //JV edit
                isSelectedRightMouseBut = false;               
                if (lastCell.equals(this) && isSelected) {
                    lastCell = this;
                    resetBackGroundColor();
                    this.focusLost(null);   
                } else {
                    lastCell.resetBackGroundColor();                   
                    lastCell.focusLost(null);
                    this.focusGained(null);
                    setBackGroundColor();
                    lastCell = this;
                } //END JV edit
            }else{
                lastCell.focusLost(null);
                lastCell.resetBackGroundColor();
                this.focusGained(null);
                this.setBackGroundColor();
                lastCell = this;
                
                if(me.getButton() == MouseEvent.BUTTON1) {
                    isSelectedRightMouseBut = false;
                } else if(me.getButton() == MouseEvent.BUTTON3) {
                    isSelectedRightMouseBut = true;
                }
                /*
                if(SudokuMainFrame.getNumberEntry() == NumberEntry.sntc) {
                    doNumberEntry();
                }
                */
                pressIn = true;
                //doNumberEntry();
            }
            PX=x;
            PY=y;
        }

        public void keyPressed(KeyEvent e) 
        {
            int i = 0;
            if(e.getKeyCode() == KeyEvent.VK_DELETE)
            {
            	i = 0;
            }
            else {
            	try {
                    char c = e.getKeyChar();
                    i = (int) c - '0';  
                    
                    if (i < 0 || i > 10) return;
            	}
            	catch (Exception X)
            	{
            		X.printStackTrace();
            	}
            }
            
           	//System.out.println("keypressed " + PX + "," + PY + ", " + i);
            
            int oldVal = sgrid.getRealGridVal(PX, PY);
            if(isSelectedRightMouseBut) {
                if(i == 0)
                    sgrid.deleteAllNotes(PX, PY);
                else 
                	sgrid.setNote(PX,PY, i);
            } else {
                sgrid.setPuzzleVal(PX,PY,i);
            }
            
            int newVal = sgrid.getRealGridVal(PX, PY);
            uRS.push(oldVal, newVal);

        }        
        

        public void focusGained(FocusEvent arg0) {
            if(GridGame.isEditable(sudokuRealGridval)) {
                isSelected = true;
                setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            }
        }

        public void focusLost(FocusEvent arg0) {
            isSelected = false;
            setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        }
        
        
        private void setBackGroundColor(){ // JV
            if (helpingLines) {
                setBGColor(false);
            }            
        }        

        private void resetBackGroundColor() { // JV
            if (helpingLines) {
                setBGColor(true);
            }
        }
        
        private void setBGColor(boolean reset) {
            for (int i = 0; i < 9; i++) {

                cells[this.x][i].setBackground((reset ? cells[this.x][i].bg : cells[this.x][i].bgSelected));
                cells[i][this.y].setBackground((reset ? cells[i][this.y].bg : cells[i][this.y].bgSelected));
            }

            int n = 3*(x/3)+(y/3);
            
            //SELECT 3X3 SQUARE FOR X, Y
            for (int i = 3*(n % 3); i <= 3*(n % 3)+2; i++) {
                for (int j = 3*(n / 3); j <= 3*(n / 3)+2; j++) {
                    cells[j][i].setBackground((reset ? cells[j][i].bg : cells[j][i].bgSelected));
                }
            }
        }

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
    }

    public SGraphicGrid (GridGame grid, URStack uRS) {
        this.sgrid = grid;
        this.dimCell = new Dimension(52, 52);
        this.uRS = uRS;
        pressIn = false;
        setLayout(new GridLayout(9, 9));
        cells = new SudokuGuiCell[9][9];
        sgrid.addObserver(this);

        boolean b1 = false;
        boolean b2 = false;
        for(int i = 0; i < 9; i++) {
           
            b1 = (i / 3 == 1) ? true : false;
            for(int j = 0; j < 9; j++) {
                b2 = (j / 3 == 1) ? true : false;
                if(b1 && b2) {
                    cells[j][i] = new SudokuGuiCell(j, i, BORDER_COLOR, GRID_COLOR_2_SELC, grid.getRealGridVal(j,i));
                } else if(b1 && !b2) {
                    cells[j][i] = new SudokuGuiCell(j, i, GRID_COLOR_1, GRID_COLOR_1_SELC, grid.getRealGridVal(j,i));
                }else if(!b1 && b2) {
                    cells[j][i] = new SudokuGuiCell(j, i, GRID_COLOR_1, GRID_COLOR_1_SELC, grid.getRealGridVal(j,i));
                } else{
                    cells[j][i] = new SudokuGuiCell(j, i, GRID_COLOR_2, GRID_COLOR_2_SELC, grid.getRealGridVal(j,i));
                }
                add(cells[j][i]);
            }
        }
        lastCell = cells[0][0];
    }
    
    public void reqFocus()
    {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
            	cells[j][i].requestFocus();
            }
        }
    	
    }
    
    
    public void updateCellChange(int cell) {
        int xadr = GridGame.getX(cell);
        int yadr = GridGame.getY(cell);
        cells[xadr][yadr].setRealGridVal(sgrid.getRealGridVal(xadr,yadr));
        cells[xadr][yadr].repaint();
    }

    public void enableHelpingLines() { // JV
        this.helpingLines = true;
    }

    
    public void disableHelpingLines() { // JV
        lastCell.resetBackGroundColor();
        this.helpingLines = false;
    }
    
    public boolean getHelpingLines() { // JV
        return this.helpingLines;
    }
}