package edu.wustl.cab2b.client.ui;

/**
 *	http://www.angelfire.com/hero/jtutorials/GBL_tutorial/GBLHelper.html
 *	http://www.angelfire.com/hero/jtutorials/GBL_tutorial/GBL_tutorial.html 
 */

import javax.swing.*;
import java.awt.*;

/** 
 * The class serves for placing Java awt and swing controls into GridBagLayout. <br>
 * It contains static methods for placing most common controls onto an awt Panel or  
 * swing JPanel objects with GridBagLayout. <br>
 * Method addComponent(...) is overloaded for different components. Swing components can be 
 * placed onto JPanel and awt components may be placed onto Panel. Also a awt Canvas object 
 * can be placed onto a JPanel.<br> <br>
 * Additionally methods are available for getting - settting background and foreground 
 * component colours. <br>
 * All methods are static and basicly have the same structure. 
 * Methods are overloaded for different swing or awt components. <br> <br>
 * List of common parameters: <br>
 * Panel (JPanel), awt (or swing) control, gridbag_constraints, gridbag_layout, 
 * column#, row#, spread_N_col, spread_N_rows, col_width, row_height,<br>
 * + alignment(optional) + filling (optional)  - [ See methods definitions for details ]<br>
 * Last two parameters are optional.<br>
 * Alignment may take next values: LEFT, CENTER, RIGHT <br>
 * Filling may have next values: true or false 
 * <br>
 * <br>
 * Last updated: 11 February 2004
 * 
 * @author Dmitry Gakhovich (D.Gakhovich@witt.ac.nz)
 * @version v.4
 */
 
public class GBLHelper{

/**
 *  Control's background color. Default value is 0x939393 (grey)
 */
    protected static Color controlColorB = new Color(0x939393);
/**
 *  Control's foreground  color. Default value is 0x93003D (red)
 */      
    protected static Color controlColorF = new Color(0x93003D);

 /** 
  * Put the component on the left side of its display area, centered vertically. 
  */
  
    protected static final int LEFT = 0;
    
 /** 
  * Put the component in the center of its display area, centered vertically.  
  */    
    protected static final int CENTER = 1;

 /** 
  * Put the component on the right side of its display area, centered vertically. 
  */ 
    protected static final int RIGHT = 2;

	
	// Static methods to get-set colours

/** Set background colour.
 * @param	newColor Background colour.
 * @return Nothing.
 */	
	public static void setBackColor(Color newColor)
	{
		controlColorB = newColor;
	}
/** Get background colour.
 * @param no params
 * @return	controlColorB Background colour
 */
	public static Color getBackColor()
	{
		return controlColorB;
	}
/** Set foreground colour.
 * @param	newColor Foreground colour.
 * @return Nothing.
 */	
	public static void setForeColor(Color newColor)
	{
		controlColorF = newColor;
	}
/** Get foreground colour.
 * @param no params
 * @return	controlColorF Foreground colour
 */
	public static Color getForeColor()
	{
		return controlColorF;
	}




//SWING components
/** Method to put JButton control <br>
  * Fills cells horizontally
  * @param  p - JPanel object which serves as a container for placing other GUI components onto it
  * @param  but  - JButton Java swing object to be placed 
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction
  * @return Nothing.
  */    
    public static void addComponent(JPanel p, JButton but, GridBagConstraints gbc, GridBagLayout gbl, 
                                    int gx, int gy, int gw, int gh, int wx, int wy){
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.gridheight = gh;
        gbc.weighty = wy;
        //but.setBackground(controlColorB);
        //but.setForeground(controlColorF);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbl.setConstraints(but, gbc);
        p.add(but);
    }
/** Method to put JButton control, overloaded
  * @param  p - JPanel object which serves as a container for placing other GUI components onto it
  * @param  but  - JButton Java swing object to be placed 
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction
  * @param FILL_HORIZ  - Boolean fill horizontally an entire cell or keep it as small as possible  if false
  * @return Nothing.
  */ 
        public static void addComponent(JPanel p, JButton but, GridBagConstraints gbc, GridBagLayout gbl, 
                                    int gx, int gy, int gw, int gh, int wx, int wy, boolean FILL_HORIZ){
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.gridheight = gh;
        gbc.weighty = wy;
        //but.setBackground(controlColorB);
        //but.setForeground(controlColorF);
        if (FILL_HORIZ==false)
            gbc.fill = GridBagConstraints.NONE;
        else
            gbc.fill = GridBagConstraints.HORIZONTAL;           
        gbc.anchor = GridBagConstraints.CENTER;
        gbl.setConstraints(but, gbc);
        p.add(but);
    }
    

/** Method to put Canvas control on swing JPanel <br>
  * Fills cells both horizontally and vertically
  * @param  p - JPanel object which serves as a container for placing other GUI components onto it
  * @param  canv - awt Canvas object to be placed
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction 
  * @return Nothing.
  */ 
    public static void addComponent(JPanel p, Canvas canv, GridBagConstraints gbc, GridBagLayout gbl, 
                                    int gx, int gy, int gw, int gh, int wx, int wy){
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.gridheight = gh;
        gbc.weighty = wy;

        gbc.fill = GridBagConstraints.BOTH;
        gbl.setConstraints(canv, gbc);
        p.add(canv);
    }    
    
/** Method to put JComboBox control<br>
  * Does not fill cells
  * @param  p - JPanel object which serves as a container for placing other GUI components onto it
  * @param  cb   - JComboBox Java swing object to be placed 
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction
  * @return Nothing.
  */ 
        public static void addComponent(JPanel p, JComboBox cb, GridBagConstraints gbc, GridBagLayout gbl, 
                                    int gx, int gy, int gw, int gh, int wx, int wy){
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.gridheight = gh;
        gbc.weighty = wy;
        //cb.setBackground(Color.white);
        //cb.setForeground(controlColorF);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbl.setConstraints(cb, gbc);
        p.add(cb);
    }
    
/** 
  * Method to put JLabel control <br>
  * Does not fill cells
  * @param  p - JPanel object which serves as a container for placing other GUI components onto it
  * @param	lb - JLabel Java swing object to be placed 
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction
  * @return Nothing.
  */ 

	public static void addComponent(JPanel p, JLabel lb, GridBagConstraints gbc, GridBagLayout gbl, 
									int gx, int gy, int gw, int gh, int wx, int wy){
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.gridheight = gh;
		gbc.weighty = wy;

		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		//lb.setForeground(controlColorF);

		gbl.setConstraints(lb, gbc);
		p.add(lb);
	}

/** Method to put JLabel control, overloaded <br>
  * Does not fill cells
  * @param  p - JPanel object which serves as a container for placing other GUI components onto it
  * @param  lb - JLabel Java swing object to be placed 
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction
  * @param	alignment  - where swing object to be placed (LEFT, CENTER, RIGHT)
  * @return Nothing.
  */ 
	public static void addComponent(JPanel p, JLabel lb, GridBagConstraints gbc, GridBagLayout gbl, 
									int gx, int gy, int gw, int gh, int wx, int wy, int alignment){
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.gridheight = gh;
		gbc.weighty = wy;

	
		gbc.fill = GridBagConstraints.NONE;
		if (alignment==LEFT)
			gbc.anchor = GridBagConstraints.WEST;
		else if (alignment==CENTER)
			gbc.anchor = GridBagConstraints.CENTER;
		else if (alignment==RIGHT)
			gbc.anchor = GridBagConstraints.EAST;
		else
			gbc.anchor = GridBagConstraints.EAST;

		//lb.setForeground(controlColorF);
		gbl.setConstraints(lb, gbc);
		p.add(lb);

	}

/** Method to put JPanel control <br>
  * Fills cells both horizontally
  * @param  p - JPanel object which serves as a container for placing other GUI components onto it
  * @param  p2  JPanel swing object to be placed
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction 
  * @return Nothing.
  */ 
    public static void addComponent(JPanel p, JPanel p2, GridBagConstraints gbc, GridBagLayout gbl, 
                                    int gx, int gy, int gw, int gh, int wx, int wy){
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.gridheight = gh;
        gbc.weighty = wy;

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbl.setConstraints(p2, gbc);
        p.add(p2);
    }
/** Method to put JPanel control, overloaded
  * @param  p - JPanel object which serves as a container for placing other GUI components onto it
  * @param  p2  JPanel swing object to be placed
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction 
  * @param  FILL_HORIZ - boolean, fills vertical only if false, and in both directions if true
  * @return Nothing.
  */ 
    public static void addComponent(JPanel p, JPanel p2, GridBagConstraints gbc, GridBagLayout gbl, 
                                    int gx, int gy, int gw, int gh, int wx, int wy,boolean FILL_HORIZ ){
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.gridheight = gh;
        gbc.weighty = wy;
        if (FILL_HORIZ==false)
            gbc.fill = GridBagConstraints.VERTICAL;
        else
            gbc.fill = GridBagConstraints.BOTH;

        gbc.anchor = GridBagConstraints.CENTER;
        gbl.setConstraints(p2, gbc);
        p.add(p2);
    }
/** Method to put JRadioButton control
  * @param  p - JPanel object which serves as a container for placing other GUI components onto it
  * @param  but  - JRadioButton Java swing object to be placed 
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction
  * @return Nothing.
  */ 
        public static void addComponent(JPanel p, JRadioButton but, GridBagConstraints gbc, GridBagLayout gbl, 
                                    int gx, int gy, int gw, int gh, int wx, int wy){
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.gridheight = gh;
        gbc.weighty = wy;
        //but.setBackground(controlColorB);
        //but.setForeground(controlColorF);
          
        gbc.anchor = GridBagConstraints.CENTER;
        gbl.setConstraints(but, gbc);
        p.add(but);
    }
    	
/** Method to put JScrollPane control <br>
  * Fills cells both horizontally and vertically
  * @param  p - JPanel object which serves as a container for placing other GUI components onto it
  * @param  sp -    JScrollPane swing object to be placed
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction 
  * @return Nothing.
  */ 
    public static void addComponent(JPanel p, JScrollPane sp, GridBagConstraints gbc, GridBagLayout gbl, 
                                    int gx, int gy, int gw, int gh, int wx, int wy){
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.gridheight = gh;
        gbc.weighty = wy;

        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbl.setConstraints(sp, gbc);
        p.add(sp);
    }
 
/** Method to put JTextArea control <br>
  * Fills cells both horizontally and vertically
  * @param  p - JPanel object which serves as a container for placing other GUI components onto it
  * @param  ta -    JTextArea swing object to be placed
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction 
  * @return Nothing.
  */ 
    public static void addComponent(JPanel p, JTextArea ta, GridBagConstraints gbc, GridBagLayout gbl, 
                                    int gx, int gy, int gw, int gh, int wx, int wy){
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.gridheight = gh;
        gbc.weighty = wy;

        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbl.setConstraints(ta, gbc);
        p.add(ta);
    }


/** Method to put JTextField control<br>
  * Fills cells horizontally
  * @param  p - JPanel object which serves as a container for placing other GUI components onto it
  * @param	tf -	JTextField Java swing object to be placed 
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction
  * @return Nothing.
  */ 
	public static void addComponent(JPanel p, JTextField tf, GridBagConstraints gbc, GridBagLayout gbl, 
									int gx, int gy, int gw, int gh, int wx, int wy){
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.gridheight = gh;
		gbc.weighty = wy;

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(tf, gbc);
		p.add(tf);
	}
/** Method to put JTextField control, overloaded
  * @param  p - JPanel object which serves as a container for placing other GUI components onto it
  * @param  tf  - JTextField Java swing object to be placed 
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction
  * @param FILL_HORIZ  - Boolean fill an entire cell or keep it as small as possible if false
  * @return Nothing.
  */ 
	public static void addComponent(JPanel p, JTextField tf, GridBagConstraints gbc, GridBagLayout gbl, 
									int gx, int gy, int gw, int gh, int wx, int wy, boolean FILL_HORIZ){
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.gridheight = gh;
		gbc.weighty = wy;

		if (FILL_HORIZ==false)
			gbc.fill = GridBagConstraints.NONE;
		else
			gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(tf, gbc);
		p.add(tf);
	}


    
// -----------------------  AWT controls --------------------------------------

/** Method to put Button control, overloaded <br>
  * Fills cells horizontally
  * @param  p - Panel object which serves as a container for placing other GUI components onto it
  * @param  but Button awt object to be placed 
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction 
  * @return Nothing.
  */ 
    public static void addComponent(Panel p, Button but, GridBagConstraints gbc, GridBagLayout gbl, 
                                    int gx, int gy, int gw, int gh, int wx, int wy){
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.gridheight = gh;
        gbc.weighty = wy;
        //but.setBackground(controlColorB);
        //but.setForeground(controlColorF);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbl.setConstraints(but, gbc);
        p.add(but);
    }

/** Method to put Button control, overloaded
  * @param  p - Panel object which serves as a container for placing other GUI components onto it
  * @param  but Button awt object to be placed 
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction  
  * @param fill  - Boolean fill an entire cell or keep it as small as possible
  * @return Nothing.
  */ 
        public static void addComponent(Panel p, Button but, GridBagConstraints gbc, GridBagLayout gbl, 
                                    int gx, int gy, int gw, int gh, int wx, int wy, boolean FILL_HORIZ){
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.gridheight = gh;
        gbc.weighty = wy;
        //but.setBackground(controlColorB);
        //but.setForeground(controlColorF);
        if (FILL_HORIZ==false)
            gbc.fill = GridBagConstraints.NONE;
        else
            gbc.fill = GridBagConstraints.HORIZONTAL;           
        gbc.anchor = GridBagConstraints.CENTER;
        gbl.setConstraints(but, gbc);
        p.add(but);
    }

/** Method to put Canvas control on awt panel <br>
  * Fills cells both horizontally and vertically
  * @param  p - Panel object which serves as a container for placing other GUI components onto it
  * @param  canv  - awt Canvas object to be placed
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction 
  * @return Nothing.
  */ 
    public static void addComponent(Panel p, Canvas canv, GridBagConstraints gbc, GridBagLayout gbl, 
                                    int gx, int gy, int gw, int gh, int wx, int wy){
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.gridheight = gh;
        gbc.weighty = wy;

        gbc.fill = GridBagConstraints.BOTH;
        gbl.setConstraints(canv, gbc);
        p.add(canv);
    }

/** Method to put Checkbox control <br>
  * Fills cells horizontally
  * @param  p - Panel object which serves as a container for placing other GUI components onto it
  * @param  cb -    Checkbox awt object to be placed
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction 
  * @return Nothing.
  */ 
    public static void addComponent(Panel p, Checkbox cb, GridBagConstraints gbc, GridBagLayout gbl, 
                                    int gx, int gy, int gw, int gh, int wx, int wy){
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.gridheight = gh;
        gbc.weighty = wy;
        //cb.setBackground(Color.white);
        //cb.setForeground(controlColorF);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbl.setConstraints(cb, gbc);
        p.add(cb);
    }

/** Method to put Checkbox control, overloaded <br>
  * Fills cells horizontally
  * @param  p - Panel object which serves as a container for placing other GUI components onto it
  * @param  cb -    Checkbox awt object to be placed
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction  
  * @param alignment  - LEFT, CENTER or RIGHT
  * @return Nothing.
  */ 
    public static void addComponent(Panel p, Checkbox cb, GridBagConstraints gbc, GridBagLayout gbl, 
                                    int gx, int gy, int gw, int gh, int wx, int wy, int alignment){
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.gridheight = gh;
        gbc.weighty = wy;
        //cb.setBackground(Color.white);
        //cb.setForeground(controlColorF);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        if (alignment==LEFT)
            gbc.anchor = GridBagConstraints.WEST;
        else if (alignment==CENTER)
            gbc.anchor = GridBagConstraints.CENTER;
        else if (alignment==RIGHT)
            gbc.anchor = GridBagConstraints.EAST;
        else
            gbc.anchor = GridBagConstraints.EAST;

        gbl.setConstraints(cb, gbc);
        p.add(cb);
    }



/** Method to put Choice control <br>
  * Fills cells horizontally
  * @param  p - Panel object which serves as a container for placing other GUI components onto it
  * @param  cb -    Choice awt object to be placed
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction 
  * @return Nothing.
  */ 
        public static void addComponent(Panel p, Choice cb, GridBagConstraints gbc, GridBagLayout gbl, 
                                    int gx, int gy, int gw, int gh, int wx, int wy){
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.gridheight = gh;
        gbc.weighty = wy;
        //cb.setBackground(Color.white);
        //cb.setForeground(controlColorF);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbl.setConstraints(cb, gbc);
        p.add(cb);
    }

/** Method to put Label control <br>
  * Does not fill cells
  * @param  p - Panel object which serves as a container for placing other GUI components onto it
  * @param	lb -	Label awt object to be placed
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction 
  * @return Nothing.
  */ 
	public static void addComponent(Panel p, Label lb, GridBagConstraints gbc, GridBagLayout gbl, 
									int gx, int gy, int gw, int gh, int wx, int wy){
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.gridheight = gh;
		gbc.weighty = wy;

		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		//lb.setForeground(Color.red);
		gbl.setConstraints(lb, gbc);
		p.add(lb);
	}
/** Method to put Label control, overloaded <br>
  * Does not fill cells
  * @param  p - Panel object which serves as a container for placing other GUI components onto it
  * @param  lb -    Label awt object to be placed
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction  
  * @param alignment  - LEFT, CENTER or RIGHT
  * @return Nothing.
  */ 
	public static void addComponent(Panel p, Label lb, GridBagConstraints gbc, GridBagLayout gbl, 
									int gx, int gy, int gw, int gh, int wx, int wy, int alignment){
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.gridheight = gh;
		gbc.weighty = wy;

	
		gbc.fill = GridBagConstraints.NONE;
		if (alignment==LEFT)
			gbc.anchor = GridBagConstraints.WEST;
		else if (alignment==CENTER)
			gbc.anchor = GridBagConstraints.CENTER;
		else if (alignment==RIGHT)
			gbc.anchor = GridBagConstraints.EAST;
		else
			gbc.anchor = GridBagConstraints.EAST;

		//lb.setForeground(Color.red);
		gbl.setConstraints(lb, gbc);
		p.add(lb);


	}
	
/** Method to put Label control, overloaded
  * @param  p - Panel object which serves as a container for placing other GUI components onto it
  * @param  lb -    Label awt object to be placed
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction 
  * @param alignment LEFT, CENTER or RIGHT
  * @param fill  - Boolean fill an entire cell or keep it as small as possible
  * @return Nothing.
  */ 	
	public static void addComponent(Panel p, Label lb, GridBagConstraints gbc, GridBagLayout gbl, 
									int gx, int gy, int gw, int gh, int wx, int wy, int alignment, boolean fill){
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.gridheight = gh;
		gbc.weighty = wy;

		if(fill)	
			gbc.fill = GridBagConstraints.HORIZONTAL;
		else
			gbc.fill = GridBagConstraints.NONE;

		if (alignment==LEFT)
			gbc.anchor = GridBagConstraints.WEST;
		else if (alignment==CENTER)
			gbc.anchor = GridBagConstraints.CENTER;
		else if (alignment==RIGHT)
			gbc.anchor = GridBagConstraints.EAST;
		else
			gbc.anchor = GridBagConstraints.EAST;

		//lb.setForeground(Color.red);
		gbl.setConstraints(lb, gbc);
		p.add(lb);


	}

/** Method to put Panel control, overloaded <br>
  * Fills cells both horizontally and vertically
  * @param  p - Panel object which serves as a container for placing other GUI components onto it
  * @param  slave   Panel awt object to be placed
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction 
  * @return Nothing.
  */ 
    public static void addComponent(Panel p, Panel slave, GridBagConstraints gbc, GridBagLayout gbl, 
                                    int gx, int gy, int gw, int gh, int wx, int wy){
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.gridheight = gh;
        gbc.weighty = wy;

        gbc.fill = GridBagConstraints.BOTH;
        gbl.setConstraints(slave, gbc);
        p.add(slave);
    }
   
    
/** Method to put TextArea control, overloaded <br>
  * Fills cells both horizontally and vertically
  * @param  p - Panel object which serves as a container for placing other GUI components onto it
  * @param  ta  TextArea awt object to be placed
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction 
  * @return Nothing.
  */ 
    public static void addComponent(Panel p, TextArea ta, GridBagConstraints gbc, GridBagLayout gbl, 
                                    int gx, int gy, int gw, int gh, int wx, int wy){
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.gridheight = gh;
        gbc.weighty = wy;

        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbl.setConstraints(ta, gbc);
        p.add(ta);
    }


/** Method to put TextField control, overloaded <br>
  * Fills cells horizontally
  * @param  p - Panel object which serves as a container for placing other GUI components onto it
  * @param	tf	TextField awt object to be placed 
  * @param  gbc - GridBagConstraints object for setting constraints for the given control
  * @param  gbl - GridBagLayout object
  * @param  gx - leftmost cell in a row occupied by the control (cell numeration starts from 0)
  * @param  gy - uppermost cell in a column occupied by the control  (cell numeration starts from 0)
  * @param  gw - how many cells does the control occupy in a row
  * @param  gh - how many cells does the control occupy in a column
  * @param  wx - relative component's size in a horizontal direction
  * @param  wy - relative component's size in a vertical direction 
  * @return Nothing.
  */ 
	public static void addComponent(Panel p, TextField tf, GridBagConstraints gbc, GridBagLayout gbl, 
									int gx, int gy, int gw, int gh, int wx, int wy){
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.gridheight = gh;
		gbc.weighty = wy;

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(tf, gbc);
		p.add(tf);
	}

	public static void updateGridBagConstraints(GridBagConstraints gbc, int gx, int gy, int wx, int wy)
	{
		gbc.gridx = gx;
		gbc.gridy = gy;
		
		gbc.weightx = wx;
		gbc.weighty = wy;
		
	}
}