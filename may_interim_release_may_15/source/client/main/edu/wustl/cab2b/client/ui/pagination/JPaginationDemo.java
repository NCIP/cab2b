package edu.wustl.cab2b.client.ui.pagination;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTaskPane;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.common.util.logger.Logger;


/**
 * Demo application.
 * 
 * @author chetan_bh
 */
public class JPaginationDemo extends JSplitPane
{
	
	Vector<PageElement> realData = RealDataForPagination.getRealData(RealDataForPagination.personsData);
	
	JXTaskPane taskPane;
	
	public JPagination pagination;
	
	JXPanel leftPanel;
	JXPanel rightPanel;
	
	public JPaginationDemo()
	{
		initGUI();
	}
	
	private void initGUI()
	{
		
		leftPanel = new Cab2bPanel();
		//leftPanel.setLayout(new BorderLayout());
		leftPanel.setLayout(new RiverLayout());
		rightPanel = new Cab2bPanel();
		
		
		taskPane = new JXTaskPane();
		//taskPane.setAnimated(false);
		taskPane.setLayout(new RiverLayout());
		taskPane.setTitle("Dummy task pane");
		taskPane.add("p",new JLabel("Name"));
		taskPane.add("tab",new JTextField(10));
		taskPane.add("p",new JLabel("Age"));
		taskPane.add("tab",new JTextField(3));
		taskPane.add("p",new JButton("Submit"));
		
		JXPanel parentPanel = new Cab2bPanel(new RiverLayout());
		NumericPager numPager = new NumericPager(realData);
		pagination = new JPagination(realData, numPager,leftPanel, true );
		pagination.addPageSelectionListener(new PageSelectionListener()
		{
			public void selectionChanged(PageSelectionEvent e)
			{
				System.out.println("selection listener 1");
			}			
		}
		);
		
		pagination.addPageSelectionListener(new PageSelectionListener()
		{
			public void selectionChanged(PageSelectionEvent e)
			{
				System.out.println("selection listener 2");
			}
		}
		);
		pagination.addPageElementActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("page element action listener - 1");
			}	
		}
		);
		
		pagination.addPageElementActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("page element action listener - 2");
			}	
		}
		);
		
		//pagination.setElementsPerPage(1);
		parentPanel.add("vfill", pagination);
		
		leftPanel.add("p",taskPane);
		leftPanel.add("br vfill",parentPanel);
		leftPanel.add("br",new JButton("Add To Data List"));
//		leftPanel.add(taskPane,BorderLayout.NORTH);
//		leftPanel.add(pagination, BorderLayout.SOUTH);
		
		setLeftComponent(leftPanel);
		setRightComponent(rightPanel);
	}
	
	public static void main(String[] args)
	{
		Logger.configure("");
		
		JPaginationDemo paginationDemo = new JPaginationDemo();
		
		WindowUtilities.showInFrame(paginationDemo, "JPagination Demo");
		
		//try{
		//	Thread.sleep(5000);
		//}catch(Exception e)
		//{
		//	e.printStackTrace();
		//}
		
		//paginationDemo.pagination.setSelectableEnabled(false);
		
		//paginationDemo.pagination.setGroupActionEnabled(false);
		
	}
}
