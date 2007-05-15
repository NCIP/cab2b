package edu.wustl.cab2b.client.ui.mainframe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.MissingResourceException;
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
import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeHandler;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface;
import edu.wustl.cab2b.common.experiment.ExperimentHome;
import edu.wustl.cab2b.common.locator.Locator;
import edu.wustl.cab2b.common.locator.LocatorException;
import edu.wustl.common.tree.ExperimentTreeNode;
import edu.wustl.common.util.dbManager.DAOException;
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

    MainFrameStackedBoxPanel myStackedBoxPanel;

    JSplitPane splitPane;

    public static JXPanel mainPanel;

    public MainFrame() {
        this("");
    }

    public MainFrame(String title) {
        this(title, true);
    }

    public MainFrame(String title, boolean exitOnClose) {
        super(title, exitOnClose);
        statusBar = WindowUtilities.getStatusBar(this);
        initGUI();
    }

    /**
     * Initialize GUI for main frame.
     */
    private void initGUI() {
        setExtendedState(JXFrame.MAXIMIZED_BOTH);

        this.setLayout(new BorderLayout());

        globalNavigationPanel = new GlobalNavigationPanel(this, this);
        this.add(globalNavigationPanel, BorderLayout.NORTH);

        //set new welcome panel 
        newWelcomePanel = new NewWelcomePanel(this);
        homePanel = newWelcomePanel;
        homePanel.setBorder(new CustomizableBorder(new Insets(1, 1, 1, 1), true, true));

        myStackedBoxPanel = new MainFrameStackedBoxPanel(this);
        myStackedBoxPanel.setBorder(new CustomizableBorder(new Insets(1, 1, 1, 1), true, true));

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, myStackedBoxPanel, homePanel);
        splitPane.setOneTouchExpandable(true);
        Dimension dim = CommonUtils.getRelativeDimension(mainframeScreenDimesion, 0.25f, 0.0f);
        splitPane.setDividerLocation(dim.width);

        mainPanel = new Cab2bPanel(new RiverLayout());
        mainPanel.add("hfill vfill", splitPane);
        mainPanel.setBorder(new CustomizableBorder(new Insets(10, 0, 6, 10), true, true));

        this.add(mainPanel, BorderLayout.CENTER);

        statusBar = WindowUtilities.getStatusBar(this);
        JXStatusBar.Constraint c1 = new JXStatusBar.Constraint();
        c1.setFixedWidth(100);
        JLabel statusLabel = new Cab2bLabel("Ready");
        statusBar.add(statusLabel, c1); // Fixed width of 100 with no inserts
        statusBar.add(new JSeparator(JSeparator.VERTICAL));
        statusBar.add(new Cab2bLabel("Status bar message"));
        //WindowUtilities.addMessage(this, "Status bar message");
        this.add(statusBar, BorderLayout.SOUTH);

    }

    /** Method to set experiment home panel */
    public void setOpenExperimentWelcomePanel() {
        CustomSwingWorker swingWorker = new CustomSwingWorker(MainFrame.mainPanel) {
            @Override
            protected void doNonUILogic() throws RuntimeException {
                openExperimentWelcomePanel = new ExperimentPanel("My Experiments");
            }

            @Override
            protected void doUIUpdateLogic() throws RuntimeException {
                mainPanel.removeAll();
                MainFrame.this.remove(mainPanel);
                MainFrame.this.add(openExperimentWelcomePanel);
            }
        };
        swingWorker.start();
    }

    /** Method to set data search home panel */
    public void setSearchDataWelcomePanel() {

        CustomSwingWorker swingWorker = new CustomSwingWorker(this) {
            @Override
            protected void doNonUILogic() throws RuntimeException {
                if (searchDataWelcomePanel == null) {
                    searchDataWelcomePanel = new SearchDataWelcomePanel(MainFrame.this);
                }
                homePanel = searchDataWelcomePanel;
            }

            @Override
            protected void doUIUpdateLogic() throws RuntimeException {
                setWelcomePanel();
            }
        };
        swingWorker.start();
    }

    /** Method to set home panel */
    public void setHomeWelcomePanel() {

        CustomSwingWorker swingWorker = new CustomSwingWorker(this) {
            @Override
            protected void doNonUILogic() throws RuntimeException {
                if (newWelcomePanel == null) {
                    newWelcomePanel = new NewWelcomePanel(MainFrame.this);
                }
                homePanel = newWelcomePanel;
            }

            @Override
            protected void doUIUpdateLogic() throws RuntimeException {
                setWelcomePanel();
            }
        };
        swingWorker.start();

    }

    /** Method to set Welcome panel */
    public void setWelcomePanel() {
        homePanel.setBorder(new CustomizableBorder(new Insets(1, 1, 1, 1), true, true));
        splitPane.setRightComponent(homePanel);
        splitPane.setOneTouchExpandable(true);
        Dimension dim = CommonUtils.getRelativeDimension(mainframeScreenDimesion, 0.25f, 0.0f);
        splitPane.setDividerLocation(dim.width);
        mainPanel.removeAll();
        mainPanel.add("hfill vfill", splitPane);
        mainPanel.setBorder(new CustomizableBorder(new Insets(10, 0, 6, 10), true, true));
        
        if (openExperimentWelcomePanel != null && openExperimentWelcomePanel.isVisible() == true) {
            Logger.out.info("Removing openExperimentWelcomePanel");
            openExperimentWelcomePanel.removeAll();
            this.remove(openExperimentWelcomePanel);   
            this.validate();
        }
        
        this.add(mainPanel);
    }

    public void setDataForMySearchQueriesPanel(Vector data) {
        myStackedBoxPanel.setDataForMySearchQueriesPanel(data);
    }

    public void setDataForPopularSearchCategoriesPanel(Vector data) {
        myStackedBoxPanel.setDataForPopularSearchCategoriesPanel(data);
    }

    public void setDataForMyExperimentsPanel(Vector data) {
        myStackedBoxPanel.setDataForMyExperimentsPanel(data);
    }

    protected static void initializeResources() {
        /* Initialze logger. */
        Logger.configure("log4j.properties");

        /* Initialize error codes resource bundhe. */
        try {
            ErrorCodeHandler.initBundle(errorCodesFileName);
        } catch (MissingResourceException mre) {
            CheckedException checkedException = new CheckedException(mre.getMessage(), mre,
                    ErrorCodeConstants.IO_0002);
            CommonUtils.handleException(checkedException, null, true, true, false, true);
        }
        /* Initialize application resources bundle. */
        try {
            ApplicationProperties.initBundle(applicationResourcesFileName);
        } catch (MissingResourceException mre) {
            CheckedException checkedException = new CheckedException(mre.getMessage(), mre,
                    ErrorCodeConstants.IO_0002);
            CommonUtils.handleException(checkedException, null, true, true, false, true);
        }
    }

    public Vector getExperiments() {
        Vector dataVector = new Vector();

        // EJB code start
        BusinessInterface bus = null;
        try {
            bus = Locator.getInstance().locate(edu.wustl.cab2b.common.ejb.EjbNamesConstants.EXPERIMENT,
                                               ExperimentHome.class);
        } catch (LocatorException e1) {
            CommonUtils.handleException(e1, this, true, true, false, false);
        }

        ExperimentBusinessInterface expBus = (ExperimentBusinessInterface) bus;
        try {
            dataVector = expBus.getExperimentHierarchy();
        } catch (RemoteException e1) {
            CommonUtils.handleException(e1, this, true, true, false, false);
        } catch (ClassNotFoundException e1) {
            CommonUtils.handleException(e1, this, true, true, false, false);
        } catch (DAOException e1) {
            CommonUtils.handleException(e1, this, true, true, false, false);
        }

        return dataVector;
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

   /*     Vector<String> myRecentExperiments = new Vector<String>();
        Vector exptVector = mainFrame.getExperiments();
        Iterator iter = exptVector.iterator();
        Logger.out.info("Vect Size :" + exptVector.size());
        while (iter.hasNext()) {

            ExperimentTreeNode exp = (ExperimentTreeNode) iter.next();
            myRecentExperiments.add(exp.getName());
        }*/

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
