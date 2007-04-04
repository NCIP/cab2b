
package edu.wustl.cab2b.client.ui.dag.ambiguityresolver;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.controls.CheckBoxTableModel;
import edu.wustl.cab2b.client.ui.controls.IDialogInterface;
import edu.wustl.cab2b.client.ui.controls.TextAreaEditor;
import edu.wustl.cab2b.client.ui.controls.TextAreaRenderer;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;
;/**
 * A Panel which lists all the available paths between the source target entities, 
 * a filter is provided to filter the paths based on some criteria.
 * 
 * @author chetan_bh
 */
public class AvailablePathsPanel extends Cab2bTitledPanel implements IDialogInterface
{
	
	/**
	 * @see FilterPathsPanel
	 */
	private FilterPathsPanel filterPathsPanel;
	
	private Cab2bButton addPathsButton;
	
	private Cab2bLabel ambiguityDescLabel;
	
	private JDialog m_parentWindow = null;
	/**
	 * A table component to display paths, and their popularity, and to
	 * allow users to select the desired paths.
	 */
	private JScrollPane pathsTableSP;
	private JTable pathsTable;
	private CheckBoxTableModel tableModel;
	private Dimension pathsTablePreferredSize = new Dimension(530, 150);
	
	private JXPanel parentPanel;
	
	
	/**
	 * Index of the current ambiguity in the collection of ambiguities.
	 */
	private int currentSrcTar = 0;
	
	private Object[][] tableData;
	
	private String[] tableHeader;
	
	//-- > Variables added by pratibha
	private  List<IPath> m_AllPathlist;
	AmbiguityObject m_ambObj;
	
	/**
	 * Key is a vector of source, target entity interface (which is the input
	 * to ambiguity resolver).
	 * Value is a List of user selected paths, a path is a PathInterfaces.
	 */
	private List<IPath> m_userSelectedPaths;
	private boolean m_isCurated = false;
	
	public AvailablePathsPanel(AmbiguityObject ambObj, List<IPath> list)
	{
		m_AllPathlist = list;
		m_ambObj = ambObj;
		m_userSelectedPaths = new ArrayList<IPath>();
		initGUI();
	}
	
	public AvailablePathsPanel(AmbiguityObject ambObj, List<IPath> list, boolean isCurated)
	{
		m_AllPathlist = list;
		m_ambObj = ambObj;
		m_userSelectedPaths = new ArrayList<IPath>();
		m_isCurated = isCurated;
		initGUI();
	}
	
	/**
	 * Initializes GUI.
	 * TODO Identify the time consuming task in this initGUI method 
	 * and put that in SwingWorker Thread. 
	 */
	private void initGUI()
	{
		this.setTitle("Available Paths");
		parentPanel = new Cab2bPanel();
		parentPanel.setLayout(new RiverLayout());
		ambiguityDescLabel = new Cab2bLabel("");
		
		parentPanel.add("br", ambiguityDescLabel);

		filterPathsPanel = new FilterPathsPanel();
		parentPanel.add("br", filterPathsPanel);
		
		
		tableHeader = new String[3];
		tableHeader[0] = "Select";
		tableHeader[1] ="Paths";
		tableHeader[2] ="Path Popularity";
		tableData = getPathsDetails();
		tableModel = new CheckBoxTableModel(tableHeader, tableData );
		pathsTable = new JTable(tableModel);
		pathsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		pathsTable.setPreferredScrollableViewportSize(pathsTablePreferredSize);
		pathsTable.getColumnModel().getColumn(0).setPreferredWidth(40);
		pathsTable.getColumnModel().getColumn(1).setPreferredWidth(400);
		pathsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		pathsTable.setFont(new Font("arial", Font.PLAIN, 12));
		TextAreaRenderer textAreaRenderer = new TextAreaRenderer();
		TextAreaEditor textEditor = new TextAreaEditor();
		
		pathsTable.getColumnModel().getColumn(1).setCellRenderer(textAreaRenderer);
		pathsTable.getColumnModel().getColumn(1).setCellEditor(textEditor);
		pathsTableSP = new JScrollPane(pathsTable);
		parentPanel.add("br tab tab", pathsTableSP);
		
		addPathsButton = new Cab2bButton("Ok");
		addPathsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			   /*
				* Here call the getCheckCounts and getCheckedColumns method on 'Cab2bTable' table component;
				*/
				m_userSelectedPaths.clear();
				int[] selectedPathsIndexes = tableModel.getCheckedRowIndexes();
				for(int i =0; i < selectedPathsIndexes.length; i++)
				{
					Logger.out.info("selectedPathsIndexes["+i+"] "+selectedPathsIndexes[i]);
					m_userSelectedPaths.add(m_AllPathlist.get(selectedPathsIndexes[i]));
					
				}
				if(m_parentWindow != null)
				{
					m_parentWindow.setVisible(false);
				}
			}
		});
		
		parentPanel.add("br", new JLabel(""));		
		for(int i=0; i<20; i++)
			parentPanel.add("tab", new JLabel(""));
		
		parentPanel.add(" tab", addPathsButton);	
		
		Cab2bLabel pathDescLabel = new Cab2bLabel();		
		//if pathIdentity is 1 then ambiguty resolver displays GENERAL PATH
		//if pathIdentity is 2 then ambiguty resolver displays CURATED PATH		
		if(m_isCurated == false)
		{
			pathDescLabel.setText("* Displaying GENERAL PATHS");
		}else
		{
			pathDescLabel.setText("* Displaying CURATED PATHS");
		}		
		parentPanel.add("br right",pathDescLabel);		
		setAmbiguityLabel();
		getPathsDetails();
		this.add(parentPanel);
	}
	
	/**
	 * Returns data for the table component.
	 * @param listOfPaths list of paths.
	 * @return 
	 */
	public Object[][] getPathsDetails()
	{
		int rowCnt=0;
		int totalResults = m_AllPathlist.size();
		Object[][] values = new Object[totalResults][];
		int pathPopularity = (int)(1/(double)totalResults * 100.00);
		Iterator<IPath> pathListIter = m_AllPathlist.iterator();
		while (pathListIter.hasNext())
		{
			values[rowCnt] = new Object[3];
			values[rowCnt][0]= new Boolean(false);
			IPath path = pathListIter.next();
			values[rowCnt][1]= getFullPathNames(path);
			values[rowCnt][2] = pathPopularity + " %"; // TODO to remove this hardcoding.
			rowCnt++;
		}
		return values;
	}
	
	private String getFullPathNames(IPath path)
	{
		StringBuffer returner = new StringBuffer(); 
		String roleName;
		List<IAssociation> assoList = path.getIntermediateAssociations();
		Iterator<IAssociation> listIterator = assoList.listIterator();
		
		boolean firstAssoOver = false;
		while(listIterator.hasNext())
		{
			IAssociation asso = listIterator.next();
			roleName = edu.wustl.cab2b.client.ui.query.Utility.getRoleName(asso);
			if(!firstAssoOver)
			{
				EntityInterface srcEntity = asso.getSourceEntity();
				EntityInterface tarEntity = asso.getTargetEntity();
				String srcEntityName = Utility.parseClassName(srcEntity.getName());
				String tarEntityName = Utility.parseClassName(tarEntity.getName());
				firstAssoOver = true;
				returner.append(srcEntityName+" -> ( "+ roleName + " ) -> " +tarEntityName);
			}
			else
			{
				EntityInterface tarEntity = asso.getTargetEntity();
				String tarEntityName = Utility.parseClassName(tarEntity.getName());
				//System.out.println("tar " + tarEntity.getName());
				returner.append(" -> ( "+ roleName + " ) -> " + tarEntityName);
			}
		}
		return returner.toString();
	}
	
	
	/**
	 * Returns the users path selection in a map.
	 * @return
	 */
	public List<IPath>  getUserSelectedpaths()
	{
		return m_userSelectedPaths;
	}
	
	/**
	 * Updates the ambiguity descrption label for the current ambiguity.
	 */
	private void setAmbiguityLabel()
	{
		EntityInterface srcEntInter = m_ambObj.getSourceEntity();
		EntityInterface tarEntInter = m_ambObj.getTargetEntity();
		String srcShortName = Utility.parseClassName(srcEntInter.getName());
		String tarShortName = Utility.parseClassName(tarEntInter.getName());
		
		ambiguityDescLabel.setText("<html><font color=\"RED\"><B>Ambiguity " + (currentSrcTar+1)
				+ "</B></font><html>: " + srcShortName + " -> " + tarShortName);
		parentPanel.updateUI();
	}
	
	public static void main(String[] args)
	{
		Logger.configure("log4j.properties");
		
		String[] searchTerms = {"ProbeSet","MicroArray","OMIM","Gene"};
		Vector<EntityInterface> entIntVector = AmbiguityPathResolverPanel.getEntityInterfaceFor(searchTerms);
		
		AmbiguityObject ambObj = new AmbiguityObject(entIntVector.get(1), entIntVector.get(3));
		
		List<IPath> pathsList = new ArrayList();
		AvailablePathsPanel availablePathsPanel = new AvailablePathsPanel(ambObj, pathsList);
		WindowUtilities.showInFrame(availablePathsPanel, "Available Paths Panel");
	}

	public void setParentWindow(JDialog dialog)
	{
		// TODO Auto-generated method stub
		m_parentWindow = dialog;
	}

}
