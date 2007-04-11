package edu.wustl.cab2b.client.ui.experiment;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.LinkRenderer;
import org.jdesktop.swingx.action.LinkAction;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.domain.ExperimentGroup;
import edu.wustl.common.tree.ExperimentTreeNode;
import edu.wustl.common.util.logger.Logger;

/**
 * A panel to display details of the selected experiment or experiment 
 * group in a spreadsheet.
 * 
 * @author chetan_bh
 */
public class ExperimentDetailsPanel extends Cab2bPanel
{
	Cab2bTable expTable;
	
	Object[] tableHeader = {"Title","Date Created","Last Updated","Description"};
	Object[][] tableData = {{"SNP Experiment","12-Jul-2003","22-Jan-2004","Scott"},
							{"Microarray Experiment","12-Jul-2004","","Scott"},
							{"Human SNP Experiment","12-Dec-2003","","Rakesh"},
							{"Mouse SNP Experiment","22-Jul-2003","","Scott"},};
	Object[][] emptyTableData = {};
	
	Vector tHeader = new Vector();
	
	JButton deleteButton; 
	
	 ExperimentOpenPanel expPanel = null;
	
	public ExperimentDetailsPanel()
	{
		initGUI();
		tHeader.add("Title");
		tHeader.add("Date Created On");
		tHeader.add("Last Updated");
		tHeader.add("Description");
	}
	
	private void initGUI()
	{
		this.setLayout(new RiverLayout());
		
		expTable = new Cab2bTable(true, emptyTableData, tableHeader);
		
		MyLinkAction myLinkAction  =new MyLinkAction();		
		expTable.getColumn(1).setCellRenderer(new LinkRenderer(myLinkAction));
		expTable.getColumn(1).setCellEditor(new LinkRenderer(myLinkAction));
				
		HighlighterPipeline highlighters = new HighlighterPipeline();
		highlighters.addHighlighter(new AlternateRowHighlighter());
		expTable.setHighlighters(highlighters);
		
		
		this.add("br hfill vfill",new JScrollPane(expTable));
		deleteButton = new Cab2bButton("Delete");
		deleteButton.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent arg0)
						{
							
						}
					});
		this.add("br",deleteButton);
		highlighters.updateUI();
	}
	
	public void refreshDetails(ExperimentTreeNode expTreeNode)
	{
		Vector newTableData = new Vector();
		if(expTreeNode.getChildNodes().size() == 0)
		{
			Vector firstRow = new Vector();
			firstRow.add(expTreeNode.getName());
			firstRow.add(expTreeNode.getCreatedOn());
			firstRow.add(expTreeNode.getLastUpdatedOn());
			firstRow.add(expTreeNode.getDesc());
			newTableData.add(firstRow);
		}else
		{
			updateTableData(expTreeNode, newTableData);
		}
		
		expTable = new Cab2bTable(true, newTableData,tHeader);
		MyLinkAction myLinkAction  =new MyLinkAction();		
		expTable.getColumn(1).setCellRenderer(new LinkRenderer(myLinkAction));
		expTable.getColumn(1).setCellEditor(new LinkRenderer(myLinkAction));

		
		Component comp = this.getComponent(0);
		this.removeAll();
		//Logger.out.info("Component 0 "+comp.getClass());
		this.add("br hfill vfill",new JScrollPane(expTable));
		this.add("br",deleteButton);
		this.updateUI();
	}
	
	
	private void updateTableData(ExperimentTreeNode treeNode, Vector tableData)
	{
		Vector nextRow = new Vector();
		nextRow.add(treeNode.getName());
		nextRow.add(treeNode.getCreatedOn());
		nextRow.add(treeNode.getLastUpdatedOn());
		nextRow.add(treeNode.getDesc());
		
		tableData.add(nextRow);
		
		if(treeNode.getChildNodes().size() == 0)
			return ;
		
		Iterator iter = treeNode.getChildNodes().iterator();
		while(iter.hasNext())
		{
			ExperimentTreeNode child = (ExperimentTreeNode) iter.next();
			updateTableData(child, tableData);
		}
	}
	
	public void refreshDetails(Experiment exp)
	{
		Object[][] newTableData = {{exp.getName(),exp.getCreatedOn(),exp.getLastUpdatedOn(),exp.getDescription()}};
		//expTable.setModel(new Cab2bTable(newTableData, tableHeader));
		expTable = new Cab2bTable(true, newTableData,tableHeader);
		MyLinkAction myLinkAction  =new MyLinkAction();		
		expTable.getColumn(1).setCellRenderer(new LinkRenderer(myLinkAction));
		expTable.getColumn(1).setCellEditor(new LinkRenderer(myLinkAction));
		
		
		Component comp = this.getComponent(0);
		this.removeAll();
		Logger.out.info("Component 0 "+comp.getClass());
		this.add("br hfill vfill",new JScrollPane(expTable));
		this.add("br",deleteButton);
		this.updateUI();
	}
	
	public void refreshDetails(ExperimentGroup expGrp)
	{
		Iterator expIter = expGrp.getExperimentCollection().iterator();
		Object[][] newTableData = new Object[expGrp.getExperimentCollection().size()][];
		
		int counter = 0; 
		
		while(expIter.hasNext())
		{
			Object obj = expIter.next();
			if(obj instanceof Experiment)
			{
				Experiment exp = (Experiment) obj;
				newTableData[counter++] = new Object[] {exp.getName(),exp.getCreatedOn(),exp.getLastUpdatedOn(),exp.getDescription()};
				
			}else if(obj instanceof ExperimentGroup )
			{
				
			}
		}
		
		expTable = new Cab2bTable(true, newTableData,tableHeader);
		MyLinkAction myLinkAction  =new MyLinkAction();		
		expTable.getColumn(1).setCellRenderer(new LinkRenderer(myLinkAction));
		expTable.getColumn(1).setCellEditor(new LinkRenderer(myLinkAction));
		
		this.removeAll();
		this.add("br hfill vfill",new JScrollPane(expTable));
		this.add("br",deleteButton);
		this.updateUI();
	}
	
	public static void main(String[] args)
	{
		ExperimentDetailsPanel expDetPanel = new ExperimentDetailsPanel();
		
		JFrame frame = new JFrame("Experiment");
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(expDetPanel);
		frame.setVisible(true);
	}
	
    class MyLinkAction extends LinkAction
    {
        public MyLinkAction(Object obj) 
        {
            super(obj);
        }
        
        public MyLinkAction() 
        {
            
        }
        
        public void actionPerformed(ActionEvent e) 
        {
            Logger.out.info("link is working");
            setVisited(true);       
            
            Logger.out.info("Panel visible");           
            
            CustomSwingWorker swingWorker = new CustomSwingWorker(MainFrame.openExperimentWelcomePanel)
    		{		
    		@Override
    		protected void doNonUILogic() throws RuntimeException {		
    			ExperimentDetailsPanel.this.expPanel =	new ExperimentOpenPanel();
    		}

    		@Override
    		protected void doUIUpdateLogic() throws RuntimeException {    			
    		    MainFrame.openExperimentWelcomePanel.removeAll();
                MainFrame.openExperimentWelcomePanel.add(ExperimentDetailsPanel.this.expPanel);
                updateUI();
    		}		
    	  };
    		swingWorker.start();    		          
        }
        
    }
	
}
