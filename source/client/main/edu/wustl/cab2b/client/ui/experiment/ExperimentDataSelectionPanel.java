package edu.wustl.cab2b.client.ui.experiment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.common.util.logger.Logger;

public class ExperimentDataSelectionPanel extends Cab2bPanel {
	
	JXList myDataListList;
	
	JXList expDataList;
	
	Vector myDataListVector;
	
	Vector expDataListVector;
	
	public ExperimentDataSelectionPanel(Vector myDataListVector)
	{
		this.myDataListVector = myDataListVector;
		initGUI();
	}
	
	private void initGUI()
	{
		this.setLayout(new HorizontalLayout(20));
		
		myDataListList = new JXList(getListModel(myDataListVector));
		myDataListList.setBorder( BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		expDataList = new JXList(getListModel(myDataListVector));
		
		
		JXPanel controlPanel = new Cab2bPanel(new VerticalLayout(50));
		JXPanel addGroupPanel = new Cab2bPanel(new VerticalLayout(10)); 
		JXPanel removeGroupPanel = new Cab2bPanel(new VerticalLayout(10));
		
		
		JButton addAllButton = new JButton("Add All >>");
		addAllButton.setActionCommand("addAll");
		addAllButton.addActionListener(new ControlButtonActionListener());
		addGroupPanel.add(addAllButton, BorderLayout.NORTH);
		
		JButton addButton = new JButton("Add >");
		addButton.setActionCommand("add");
		addButton.addActionListener(new ControlButtonActionListener());
		addGroupPanel.add(addButton, BorderLayout.SOUTH);
		
		
		JButton removeButton = new JButton("< Remove");
		removeButton.setActionCommand("remove");
		removeButton.addActionListener(new ControlButtonActionListener());
		removeGroupPanel.add(removeButton, BorderLayout.NORTH);
		
		JButton removeAllButton = new JButton("<< Remove All");
		removeAllButton.setActionCommand("removeAll");
		removeAllButton.addActionListener(new ControlButtonActionListener());
		removeGroupPanel.add(removeAllButton, BorderLayout.SOUTH);
		
		controlPanel.add(new JLabel());
		controlPanel.add(addGroupPanel);
		controlPanel.add(removeGroupPanel);
		
		
		JScrollPane myDataListSP = new JScrollPane(myDataListList);
		myDataListSP.setPreferredSize(new Dimension(300,400));
		
		JScrollPane expDataListSP = new JScrollPane(expDataList);
		expDataListSP.setPreferredSize(new Dimension(300,400));
		
		
		JXPanel myDataPanel = new Cab2bPanel(new VerticalLayout(10));
		myDataPanel.add(new JLabel());
		myDataPanel.add(new JLabel("My Data List :"));
		myDataPanel.add(myDataListSP);
		
		JXPanel expDataPanel = new Cab2bPanel(new VerticalLayout(10));
		expDataPanel.add(new JLabel());
		expDataPanel.add(new JLabel("Experiment Data :"));
		expDataPanel.add(expDataListSP);
		
		this.add(myDataPanel);
		this.add(controlPanel);
		this.add(expDataPanel);
	}
	
	
	private ListModel getListModel(Vector listData)
	{
		DefaultListModel listModel = new DefaultListModel();
		Iterator iter = listData.iterator();
		while(iter.hasNext())
		{
			Object obj = iter.next();
			listModel.addElement(obj);
		}
		
		return ((ListModel) listModel);
	}
	
	private void refreshListModel(ListModel listModel, Vector newData, boolean removeAll)
	{
		if(removeAll)
		{	
			((DefaultListModel)listModel).removeAllElements();
		}
		Iterator iter = newData.iterator();
		while(iter.hasNext())
		{
			Object obj = iter.next();
			((DefaultListModel)listModel).addElement(obj);
		}
	}
	
	
	private class ControlButtonActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int[] myDataListIndices = myDataListList.getSelectedIndices();
			int[] expDataListIndices = expDataList.getSelectedIndices();
			
			JButton sourceButton = (JButton)e.getSource();
			
			String actionCommnad = sourceButton.getActionCommand();
			if(actionCommnad.equals("add"))
			{
				if(myDataListIndices.length > 0)
				{
					ListModel myListModel = myDataListList.getModel();
					ListModel expListModel = expDataList.getModel();
					//((DefaultListModel)listModel).contains(elem)
					Vector elementsToCopy = new Vector();
					for(int i = 0; i < myDataListIndices.length; i++)
					{
						Object selectedElement = myListModel.getElementAt(myDataListIndices[i]);
						if(! ((DefaultListModel)expListModel).contains(selectedElement))
						{
							elementsToCopy.add(selectedElement);
						}
					}
					refreshListModel(expListModel, elementsToCopy, false);
				}
			}else if(actionCommnad.equals("remove"))
			{
				if(expDataListIndices.length > 0)
				{
					ListModel expListModel = expDataList.getModel();
					
					Vector objectsToDelete = new Vector();
					for(int i = 0; i<expDataListIndices.length; i++)
					{
						Object obj =((DefaultListModel)expListModel).get(expDataListIndices[i]);
						objectsToDelete.add(obj);
					}
					// We can't remove multiple elements from a list using index,
					// since indexing changes after each deletion, but the list of indices 
					// you have is a stale. 
					
					// The solution is to delete elements based on objects.
					for(int i = 0; i < objectsToDelete.size() ; i++)
					{
						Object selectedObj = objectsToDelete.get(i);
						((DefaultListModel)expListModel).removeElement(selectedObj);
					}
				}
			}else if(actionCommnad.equals("addAll"))
			{
				ListModel listModel = expDataList.getModel();
				refreshListModel(listModel, myDataListVector, true);
				
			}else // Remove All.
			{
				ListModel listModel = expDataList.getModel();
				((DefaultListModel)listModel).removeAllElements();
			}
			
			expDataList.updateUI();
			myDataListList.updateUI();
		}
	}
	
	public static void print(int[] array)
	{
		for(int i = 0; i < array.length; i++)
		{
			Logger.out.info(array[i]+", ");
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Vector myDataList = new Vector();
		myDataList.add("Tumor Micro Array for Mouse");
		myDataList.add("Microarray Data - HuAv91s");
		myDataList.add("Microarray Data - MuAv01g");
		myDataList.add("Tumor Vs. Diseased Data Set for Human");
		myDataList.add("SNP Data - Chromosome 12 Hu");
		
		for(int i = 0; i< 25; i++)
		{
			myDataList.add("Microaray Exp Data "+i);
		}
		
		ExperimentDataSelectionPanel expDataSelectionPanel = new ExperimentDataSelectionPanel(myDataList);
		expDataSelectionPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));		
		JFrame frame = new JFrame("New Exp Details -- caB2B");
		frame.setSize(new Dimension(800,600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(expDataSelectionPanel);
		frame.setVisible(true);
		
	}

}
