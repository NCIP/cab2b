package edu.wustl.cab2b.client.ui.dag.ambiguityresolver;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXPanel;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.controls.IDialogInterface;
import edu.wustl.cab2b.client.ui.controls.RadioButtonRenderer;
import edu.wustl.cab2b.client.ui.controls.RadioButtonEditor;
import edu.wustl.cab2b.client.ui.controls.TextAreaEditor;
import edu.wustl.cab2b.client.ui.controls.TextAreaRenderer;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.util.Utility;

public class AutoConnectAmbiguityResolver extends Cab2bTitledPanel implements IDialogInterface
{
	ICuratedPath[] m_paths;
	
	ButtonGroup radioGroup = new ButtonGroup();

	private FilterPathsPanel filterPathsPanel;
	
	private Cab2bButton addPathsButton;
	
	private JDialog m_parentWindow = null;
	
	/**
	 * A table component to display paths, and their popularity, and to
	 * allow users to select the desired paths.
	 */
	private JScrollPane pathsTableSP;
	private JTable pathsTable;
	private Dimension pathsTablePreferredSize = new Dimension(530, 150);
	
	private JXPanel parentPanel;
	
	private Object[][] tableData;
	
	private String[] tableHeader;
	
	/**
	 * Key is a vector of source, target entity interface (which is the input
	 * to ambiguity resolver).
	 * Value is a List of user selected paths, a path is a PathInterfaces.
	 */
	private ICuratedPath m_userSelectedPath;
	
	public AutoConnectAmbiguityResolver(Set<ICuratedPath> paths)
	{
		Iterator<ICuratedPath> pathListIter = paths.iterator();
		m_paths = new ICuratedPath[paths.size()];
		int i=0;
		while (pathListIter.hasNext())
		{
			m_paths[i] = pathListIter.next();
			i++;
		}
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

		filterPathsPanel = new FilterPathsPanel();
		parentPanel.add("br", filterPathsPanel);
		
		
		tableHeader = new String[3];
		tableHeader[0] = "Select";
		tableHeader[1] ="Paths";
		tableHeader[2] ="Path Popularity";
		tableData = getPathsDetails();
		DefaultTableModel dm = new DefaultTableModel();
		dm.setDataVector(tableData, tableHeader);
		pathsTable = new JTable(dm);
		pathsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		pathsTable.setPreferredScrollableViewportSize(pathsTablePreferredSize);
		pathsTable.getColumnModel().getColumn(0).setPreferredWidth(40);
		pathsTable.getColumnModel().getColumn(1).setPreferredWidth(400);
		pathsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		pathsTable.setFont(new Font("arial", Font.PLAIN, 12));
		RadioButtonRenderer radioRenderer = new RadioButtonRenderer();
		RadioButtonEditor radioEditor = new RadioButtonEditor(new JCheckBox());
		
		pathsTable.getColumnModel().getColumn(0).setCellRenderer(radioRenderer);
		pathsTable.getColumnModel().getColumn(0).setCellEditor(radioEditor);
		
		TextAreaRenderer textAreaRenderer = new TextAreaRenderer();
		TextAreaEditor textEditor = new TextAreaEditor();
		pathsTable.getColumnModel().getColumn(1).setCellRenderer(textAreaRenderer);
		pathsTable.getColumnModel().getColumn(1).setCellEditor(textEditor);
		pathsTableSP = new JScrollPane(pathsTable);
		parentPanel.add("br", pathsTableSP);
		
		addPathsButton = new Cab2bButton("Ok");
		addPathsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			   	
				m_userSelectedPath = null;
				DefaultTableModel dm =	(DefaultTableModel)pathsTable.getModel();
				for(int i=0; i<dm.getRowCount(); i++)
				{
					JRadioButton button = (JRadioButton)dm.getValueAt(i, 0);
					if(button.isSelected() == true)
					{
						// Set the selection properly
						m_userSelectedPath = m_paths[i];
						break;
					}
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
		parentPanel.add("tab", addPathsButton);
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
		int totalResults = m_paths.length;
		Object[][] values = new Object[totalResults][];
		int pathPopularity = (int)(1/(double)totalResults * 100.00);
		for (int i =0; i<totalResults; i++)
		{
			values[rowCnt] = new Object[3];
			values[rowCnt][0]= new JRadioButton();
			radioGroup.add((JRadioButton)values[rowCnt][0]);
			Set<IPath> internalPaths = m_paths[i].getPaths();
			Iterator<IPath> iter = internalPaths.iterator();
			StringBuffer sb = new StringBuffer();
			while (iter.hasNext())
			{
				sb.append(getFullPathNames(iter.next())).append("\n");
			}
			values[rowCnt][1]= sb.toString();
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
				String srcEntityName = edu.wustl.cab2b.common.util.Utility.getDisplayName(srcEntity);
				String tarEntityName = edu.wustl.cab2b.common.util.Utility.getDisplayName(tarEntity);
				firstAssoOver = true;
				returner.append(srcEntityName+" -> ( "+ roleName + " ) -> " +tarEntityName);
			}
			else
			{
				EntityInterface tarEntity = asso.getTargetEntity();
				String tarEntityName = Utility.parseClassName(tarEntity.getName());
				returner.append(" -> ( "+ roleName + " ) -> " + tarEntityName);
			}
		}
		return returner.toString();
	}
	
	
	/**
	 * Returns the users path selection in a map.
	 * @return
	 */
	public ICuratedPath getUserSelectedpaths()
	{
		return m_userSelectedPath;
	}

	public void setParentWindow(JDialog dialog)
	{
		// TODO Auto-generated method stub
		m_parentWindow = dialog;
	}
}
