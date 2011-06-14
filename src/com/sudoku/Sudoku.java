package com.sudoku;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

public class Sudoku extends JFrame implements SudokuQQ {

    private JPanel all;
    //GuiGrid
    private SGraphicGrid guiGrid;
    private GridGame sudGrid;
    
    //private NumDistributuon nD;
    private Generator sudGenerator;
    private boolean hasTheGridBeenChanged;
    
    private URStack uRS;
    
    private int wgame;
    private int lgame;
    
    //Event
    private ButtonHandlerUp button_down;
    
    //JButton
    private JPanel buttondown;
    private JButton Newgame = new JButton("NewGame");
    private JButton Checkgame = new JButton("Check");
    private JButton SaveAs = new JButton("SaveAs");

    private JLabel showgame = new JLabel("win/lose: ");

    public Sudoku() {
        super("Sudoku Game");
        button_down = new ButtonHandlerUp();
        sudGenerator = new Generator();
        sudGrid = sudGenerator.getGrid();
        uRS = new URStack(300);
        
        initGraphic();
        
        Container cp = getContentPane();
        cp.add(all);
        pack();
        
        wgame = 0;
        lgame = 0;
        
        setResizable(false);
        
        sudGrid.addObserver(this);
        try
        {
        	setLookAndFeel();
        	generateNewSud();
        }
        catch (Exception x)
        {
        	x.printStackTrace();        
        }

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        showgame.setText("    Win GAME/Lose GAME: " + wgame + "/" + lgame);
    }

    private void initGraphic() {

        all = new JPanel();
        all.setLayout(new BorderLayout());
        all.setBackground( new Color(184, 207, 229));
        all.setBorder(BorderFactory.createTitledBorder(""));
        
        //Middle
        guiGrid = new SGraphicGrid(sudGrid, uRS);
        guiGrid.setBorder(new LineBorder(new Color(238,238,238), 4, false));
        all.add(guiGrid, BorderLayout.CENTER);
        
        //button
        buttondown = new JPanel();
        BoxLayout butLayout = new BoxLayout(buttondown,BoxLayout.X_AXIS);
        buttondown.setLayout(butLayout);
        buttondown.setBorder(new LineBorder(new Color(184, 207, 229), 2, false));
        buttondown.setBackground(new Color(184, 207, 229));
        
        Newgame.addActionListener(button_down);
        buttondown.add(Newgame);
        Checkgame.addActionListener(button_down);
        buttondown.add(Checkgame);
        SaveAs.addActionListener(button_down);
        buttondown.add(SaveAs);
        buttondown.add(showgame);        
        all.add(buttondown, BorderLayout.SOUTH);
    }
   
    private void generateNewSud() {
        boolean b = messager("generate new Sudoku?");
        if(b) {
            sudGrid.resetGrid();
            sudGenerator.generatePuzzle(30);
            uRS.reset();
            setGridChange(false);
            guiGrid.repaint();
        }
    }
    
    private void doClear() {
        boolean result = messager("clear the grid?");
        if(result) 
        {
            sudGrid.clearNonDefaultCells();
            uRS.reset();
            setGridChange(false);
        }
    }
    
    private void doCheck() {
        if(sudGrid.isPuzzleSolved()) {
            JOptionPane.showMessageDialog(this, "Sudoku solved :)", "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            wgame++;
            setGridToFinished();
            guiGrid.repaint();
        }   
        else 
        {
            JOptionPane.showMessageDialog(this, "Sudoku not corrent, retry again!!", "corrent", 
                    JOptionPane.INFORMATION_MESSAGE);
            lgame++;
        }
        
        showgame.setText("    Win GAME/Lose GAME: " + wgame + "/" + lgame);
    }

    private void doSaveAs() 
    {
		String name = JOptionPane.showInputDialog(null,
				  "Input your name ",
				  "e.g. 小明",
				  JOptionPane.QUESTION_MESSAGE);
		
		if (name.equals(""))
		{
            JOptionPane.showMessageDialog(this, "Name is null, please try again!!", "corrent", 
                    JOptionPane.INFORMATION_MESSAGE);
			return;			
		}
		
		String path = JOptionPane.showInputDialog(null,
				  "Input file path",
				  "e.g. C:\\myprofile.txt",
				  JOptionPane.QUESTION_MESSAGE);
		
		if (path.equals(""))
		{
            JOptionPane.showMessageDialog(this, "Path is null, please try again!!", "corrent", 
                    JOptionPane.INFORMATION_MESSAGE);
			return;			
		}
		
		//Open file
		try
		{
			File sFile = new File(path);
			if (sFile == null) return;
			if(sFile.exists()) 
			{
		        boolean result = messager("file exit, overwrite?");
		        if(!result) 
		        {
		        	return;
		        }
			    sFile.delete();
			}			
			
			//writing file
			Writer output = null;
			output = new BufferedWriter(new FileWriter(sFile));
			output.write( "Player Name: " + name + "\r\n\r\n");
			output.write( "Win/Lose Record: " + "\r\n");
			output.write( "Win: " + wgame + "\r\n");
			output.write( "Lose: " + lgame + "\r\n");
			output.close();			
			
			JOptionPane.showMessageDialog(this, "Save " + sFile.getName() + " OK!", "Save OK", 
                    JOptionPane.INFORMATION_MESSAGE);
		}
		catch (IOException  X)
		{
			JOptionPane.showMessageDialog(this, X, "file error", 
                    JOptionPane.INFORMATION_MESSAGE);
		}		
		catch (Exception X)
		{
			JOptionPane.showMessageDialog(this, X, "file error", 
                    JOptionPane.INFORMATION_MESSAGE);
		}
    }
    
    
    private boolean messager(String sureFor) {
        if(hasTheGridBeenChanged) {
            StringBuffer sb = new StringBuffer();
            sb.append("Do you realy what to ");
            sb.append(sureFor);
            sb.append("\nAll of your changes will be lost!");
            int result = JOptionPane.showConfirmDialog(this,
                    sb.toString(), "" , JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE, null);
            sb = null;
            if(result == JOptionPane.YES_OPTION) {
                setGridChange(false);
                return true;
            } else {
                return false;
            } 
        } else {
            return true;
        }
    }
    
      
    private void setGridToFinished() {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                sudGrid.deleteAllNotes(j,i);
                sudGrid.setEditable(i,j,false);
            }
        }
        uRS.reset();
    }
    
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
            this.pack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setGridChange(boolean isChanged) {
        hasTheGridBeenChanged = isChanged;
    }

    public void updateCellChange(int cell) {
        setGridChange(true);
        repaint();
        //doCheck();
    }
   
    
    private class ButtonHandlerUp implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                JButton tmp = (JButton) e.getSource();
                
                if (tmp == Newgame) {
                	doClear();
                    generateNewSud();
                    guiGrid.hasFocus();
                } else if(tmp == Checkgame) {
                	doCheck();                     
                    guiGrid.hasFocus();
                } else if(tmp == SaveAs) 
                {
                	doSaveAs();
                }
             } catch (ClassCastException ex) {
                 ex.printStackTrace();
             } 
        }
    }

	public static void main(String[] args) 
	{
		Sudoku app = new Sudoku(); // 建立Swing應用程式
		
		// 關閉視窗事件, 結束程式的執行
		app.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				System.exit(0);
			}
		});
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = app.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		app.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		
		//app.setSize(new Dimension(400, 300));
		//app.getContentPane().setLayout(borderLayout);
		
		app.setVisible(true); // 顯示視窗	
	}
}

