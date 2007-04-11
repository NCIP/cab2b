package edu.wustl.cab2b.client.ui.mainframe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.CustomizableBorder;
import edu.wustl.cab2b.client.ui.experiment.ExperimentPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeHandler;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * Main frame of the caB2B application.
 * @author chetan_bh
 */
public class MainFrame extends JXFrame {
	
	private static final long serialVersionUID = 1234567890L;

	/** Resource bundle name for getting error codes and its description. */
	public static String errorCodesFileName = "errorcodes";
	
	/** Resource bundle name for getting externalized strings. */
	public static String applicationResourcesFileName = "Cab2bApplicationResources";
	
	/** Resource bundle name for getting jndi properties. */
	public static String jndiResourceFileName = "jndi"; 
	
	/** Everything related GUI's containers and its components size is relative to this size. */
	public static Dimension mainframeScreenDimesion = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static SearchDataWelcomePanel searchDataWelcomePanel = null;
	
	public static ExperimentPanel openExperimentWelcomePanel = null;
	
	public static NewWelcomePanel newWelcomePanel = null; 
	
	/**
	 * Status bar for the application.
	 */
	JXStatusBar statusBar;
	
	/**
	 * Global navigation panel which is at the top of the MainFrame.
	 */
	GlobalNavigationPanel globalNavigationPanel;
	
	JXPanel leftHandSidePanel;
	
	JXPanel homePanel;
	
	JXPanel searchPanel;
	
	JXPanel experimentPanel;
	
	MyStackedBox myStackedBoxPanel;
	
	JSplitPane splitPane;
	
	public static JXPanel splitPanelPanel;
	
	public MainFrame() {
		this("");
	}
	
	
	public MainFrame(String title) {
		this(title,true);
	}
	
	
	public MainFrame(String title, boolean exitOnClose) {
		super(title, exitOnClose);
		statusBar = WindowUtilities.getStatusBar(this);
		initGUI();
	}
	
	/**
	 * Initialize GUI for main frame.
	 */
	private void initGUI()
	{
		setExtendedState(JXFrame.MAXIMIZED_BOTH);

		this.setLayout(new BorderLayout());
		
		globalNavigationPanel = new GlobalNavigationPanel(this);
		this.add(globalNavigationPanel, BorderLayout.NORTH);
		
		//set new welcome panel 
		newWelcomePanel = new NewWelcomePanel(this);	
		homePanel = newWelcomePanel;	
		homePanel.setBorder(new CustomizableBorder(new Insets(1,1,1,1), true, true));
		
		myStackedBoxPanel = new MyStackedBox();
		myStackedBoxPanel.setBorder(new CustomizableBorder(new Insets(1,1,1,1), true, true));
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, myStackedBoxPanel, homePanel);
		splitPane.setOneTouchExpandable(true);
		Dimension dim = CommonUtils.getRelativeDimension(mainframeScreenDimesion, 0.25f, 0.0f);
		splitPane.setDividerLocation(dim.width);
				
		splitPanelPanel = new Cab2bPanel(new RiverLayout());
		splitPanelPanel.add("hfill vfill",splitPane);
		splitPanelPanel.setBorder(new CustomizableBorder(new Insets(10,0,6,10), true, true));
		
		this.add(splitPanelPanel, BorderLayout.CENTER);
		
		statusBar = WindowUtilities.getStatusBar(this);
		JXStatusBar.Constraint c1 = new JXStatusBar.Constraint();
		c1.setFixedWidth(100);
		JLabel statusLabel = new Cab2bLabel("Ready");
		statusBar.add(statusLabel, c1);     // Fixed width of 100 with no inserts
		statusBar.add(new JSeparator(JSeparator.VERTICAL));
		statusBar.add(new Cab2bLabel("Status bar message"));
		//WindowUtilities.addMessage(this, "Status bar message");
		this.add(statusBar, BorderLayout.SOUTH);
		
	}
	
	public void setOpenExperimentWelcomePanel()
	{
		CustomSwingWorker swingWorker = new CustomSwingWorker(this.splitPanelPanel)
		{		
		@Override
		protected void doNonUILogic() throws RuntimeException {
			openExperimentWelcomePanel = new ExperimentPanel("My Experiments");
		}

		@Override
		protected void doUIUpdateLogic() throws RuntimeException {
			// TODO Auto-generated method stub
			MainFrame.this.remove(splitPanelPanel);
			MainFrame.this.add(openExperimentWelcomePanel);			
		}		
		};
		swingWorker.start();	
	}
	
	public void setSearchDataWelcomePanel()
	{
		if(searchDataWelcomePanel == null)
		{
			searchDataWelcomePanel = new SearchDataWelcomePanel(this);			
		}
		homePanel = searchDataWelcomePanel; 
		setWelcomePanel();			
	}
	
	public void setHomeWelcomePanel()
	{
		if(newWelcomePanel == null)
		{
			newWelcomePanel = new NewWelcomePanel(this);				
		}
		homePanel = newWelcomePanel;
		setWelcomePanel();
	}
	
	public void setWelcomePanel()
	{
		homePanel.setBorder(new CustomizableBorder(new Insets(1,1,1,1), true, true));	
		splitPane.setRightComponent(homePanel);
		splitPane.setOneTouchExpandable(true);
		Dimension dim = CommonUtils.getRelativeDimension(mainframeScreenDimesion, 0.25f, 0.0f);
		splitPane.setDividerLocation(dim.width);
		if(openExperimentWelcomePanel != null && openExperimentWelcomePanel.isVisible()==true)
		{
			this.remove(openExperimentWelcomePanel);
			this.add(splitPanelPanel);		
		}
	}
	
	public void setDataForMySearchQueriesPanel(Vector data)
	{
		myStackedBoxPanel.setDataForMySearchQueriesPanel(data);
	}
	
	public void setDataForPopularSearchCategoriesPanel(Vector data)
	{
		myStackedBoxPanel.setDataForPopularSearchCategoriesPanel(data);
	}
	
	public void setDataForMyExperimentsPanel(Vector data)
	{
		myStackedBoxPanel.setDataForMyExperimentsPanel(data);
	}
	
	protected static void initializeResources()
	{
		/* Initialze logger. */
		Logger.configure("log4j.properties");
		
		/* Initialize error codes resource bundhe. */
		try
		{
			ErrorCodeHandler.initBundle(errorCodesFileName);
		}
		catch(MissingResourceException mre)
		{
			CheckedException checkedException = new CheckedException(mre.getMessage(), mre, ErrorCodeConstants.IO_0002);
			CommonUtils.handleException(checkedException, null, true, true, false, true);
		}
		/* Initialize application resources bundle. */
		try{
			ApplicationProperties.initBundle(applicationResourcesFileName);
		}catch(MissingResourceException mre)
		{
			CheckedException checkedException = new CheckedException(mre.getMessage(), mre, ErrorCodeConstants.IO_0002);
			CommonUtils.handleException(checkedException, null, true, true, false, true);
		}
		/* Check for jndi properties for initail context. */
		try{
			ResourceBundle bundle = ResourceBundle.getBundle("jndi");
		}catch(MissingResourceException mre)
		{
			CheckedException checkedException = new CheckedException(mre.getMessage(), mre, ErrorCodeConstants.IO_0002);
			CommonUtils.handleException(checkedException, null, true, true, false, true);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		/* Initialize all Resources. */
		initializeResources();
		
		String mainFrameTitle = ApplicationProperties.getValue("cab2b.main.frame.title");
		
		MainFrame mainFrame = new MainFrame(mainFrameTitle); // "ca Bench to Bedside (B2B)"
		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		
		Vector<String> myRecentExperiments = new Vector<String>();
		myRecentExperiments.add("Breast Cancer Microarrays (Hu133 Plus 2.0)");
		myRecentExperiments.add("Breast Cancer Microarrays (MOE430 Plus 2.0)");
		myRecentExperiments.add("Acute Myelogenous Leukemia Microarrays");
		mainFrame.setDataForMyExperimentsPanel(myRecentExperiments);
		
		Vector<String> mySearchQueries = new Vector<String>();
		mySearchQueries.add("Prostate Cancer Microarray Data");
		mySearchQueries.add("Glioblastoma Microarray Data");
		mainFrame.setDataForMySearchQueriesPanel(mySearchQueries);
		
		Vector<String> popularSearchCategories = new Vector<String>();
		popularSearchCategories.add("Gene Annotation");
		popularSearchCategories.add("Microarray Annotation");
		popularSearchCategories.add("Tissue Biospecimens");
		popularSearchCategories.add("Molecular Biospecimens");
		mainFrame.setDataForPopularSearchCategoriesPanel(popularSearchCategories);		
		mainFrame.setVisible(true);		
	}	
}

