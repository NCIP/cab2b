package edu.wustl.cab2b.client.ui.dag;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.IconButton;
import edu.wustl.cab2b.client.ui.util.CommonUtils.DagImageConstants;

public class DagControlPanel extends JPanel implements ActionListener
{
	private JButton m_selectToolB;
	private JButton m_arrowToolB;
	private JButton m_parenthesisToolB;
	
	private JButton m_autoConnectB;
	private Cab2bHyperlink m_clearAllPathsL;
	private static final String AUTOCONNECTLABEL = "Auto Connect";
	private static final String CLEARALLPATHS = "<HTML> <u>Clear All Paths</u>";
	private MainDagPanel m_parentPanel;
	private Map<DagImageConstants, Image> m_dagImageMap;
	
	/**
	 * Constructor for control panel 
	 */
	public DagControlPanel(MainDagPanel parentPanel, Map<DagImageConstants, Image> dagImageMap)
	{
		m_parentPanel = parentPanel;
		m_dagImageMap = dagImageMap;
		initGUI();
	}
	
	private void initGUI()
	{
		JPanel leftPanel = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setHgap(10);
		layout.setAlignment(FlowLayout.LEFT);
		leftPanel.setLayout(layout);
		leftPanel.setOpaque(false);
		m_selectToolB = new IconButton(m_dagImageMap.get(DagImageConstants.SelectIcon), m_dagImageMap.get(DagImageConstants.selectMOIcon));
		m_selectToolB.setToolTipText("Select Tool");
		m_selectToolB.setEnabled(false);
		m_arrowToolB = new IconButton(m_dagImageMap.get(DagImageConstants.ArrowSelectIcon), m_dagImageMap.get(DagImageConstants.ArrowSelectMOIcon));
		m_arrowToolB.setToolTipText("Arrow Tool");
		m_arrowToolB.addActionListener(this);
		m_parenthesisToolB = new IconButton(m_dagImageMap.get(DagImageConstants.ParenthesisIcon), m_dagImageMap.get(DagImageConstants.ParenthesisMOIcon));
		m_parenthesisToolB.setToolTipText("Parenthesis Tool");
		m_parenthesisToolB.setEnabled(false);
		leftPanel.add(m_selectToolB);
		leftPanel.add(m_arrowToolB);
		leftPanel.add(m_parenthesisToolB);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setOpaque(false);
		m_clearAllPathsL = new Cab2bHyperlink(true);
		m_clearAllPathsL.setText(CLEARALLPATHS);
		m_autoConnectB = new JButton(AUTOCONNECTLABEL);
		m_autoConnectB.setEnabled(true);
		JLabel seperator = new JLabel("|");
		layout = new FlowLayout();
		layout.setHgap(10);
		layout.setAlignment(FlowLayout.RIGHT);
		rightPanel.setLayout(layout);
		rightPanel.add(m_autoConnectB);
		m_autoConnectB.addActionListener(this);
		rightPanel.add(seperator);
		rightPanel.add(m_clearAllPathsL);
		m_clearAllPathsL.addActionListener(this);
		setLayout(new BorderLayout());
		add(leftPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.EAST);
	}
	
	public boolean isOpaque()
	{
		return true;
	}
			  
	public void paintComponent(Graphics g) 
	{
	      Graphics2D gd = (Graphics2D)g;
			 
	      Rectangle bounds = getBounds();
			 
	      // Set Paint for filling Shape
	      Paint gradientPaint = new GradientPaint(0, 0, Color.lightGray, bounds.width, bounds.height, Color.WHITE);
	      gd.setPaint(gradientPaint);
	      gd.fillRect(0, 0, bounds.width, bounds.height);
	}

	public void actionPerformed(ActionEvent event) 
	{
		// Handle connect nodes event
		if(event.getSource() == m_arrowToolB)
		{
			// call parents connect node method
			m_parentPanel.linkNodes();
		}
		else if(event.getSource() == m_clearAllPathsL)
		{
			// Clear all the paths
			m_parentPanel.clearAllPaths();
		}
		else if(event.getSource() == m_autoConnectB)
		{
			// Perform auto-connect functionality on selected classes
			m_parentPanel.performAutoConnect();
		}
	}

}
