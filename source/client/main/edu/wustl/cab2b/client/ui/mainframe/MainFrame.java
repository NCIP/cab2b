package edu.wustl.cab2b.client.ui.mainframe;

import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.MAIN_FRAME_TITLE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.APPLICATION_RESOURCES_FILE_NAME;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.CAB2B_LOGO_IMAGE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.ERROR_CODE_FILE_NAME;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.MissingResourceException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXErrorDialog;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;

import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.CustomizableBorder;
import edu.wustl.cab2b.client.ui.experiment.ExperimentPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineHome;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeHandler;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * Main frame of the caB2B application. It is the home page of application.
 * @author chetan_bh
 * @author Chandrakant Talele
 * @author Deepak
 * @author Juber
 */
public class MainFrame extends JXFrame {

    private static final long serialVersionUID = 1234567890L;

    private static String userName;

    /** Everything related GUI's containers and its components size is relative to this size. */
    public static Dimension mainframeScreenDimesion = Toolkit.getDefaultToolkit().getScreenSize();

    private static SearchDataWelcomePanel searchDataWelcomePanel = null;

    public static ExperimentPanel openExperimentWelcomePanel = null;

    public static NewWelcomePanel newWelcomePanel = null;

    /** Global navigation panel which is at the top of the MainFrame */
    private GlobalNavigationPanel globalNavigationPanel;

    private JXPanel homePanel;

    private static MainFrameStackedBoxPanel lefthandStackedBox;

    private JSplitPane splitPane;

    // fields used by the status bar
    public static enum Status {
        READY("Ready"), BUSY("Busy");
        private String textToShow;

        Status(String textToShow) {
            this.textToShow = textToShow;
        }

        public String getTextToShow() {
            return textToShow;
        }
    };

    private static Cab2bLabel status;

    private static Cab2bLabel statusMessage;

    private JXStatusBar statusBar;

    private static JXPanel mainPanel;

    public MainFrame(String title, boolean exitOnClose) {
        super(title, exitOnClose);
        statusBar = WindowUtilities.getStatusBar(this);
        initGUI();
        lefthandStackedBox.setDataForMyExperimentsPanel(CommonUtils.getExperiments());
        lefthandStackedBox.setDataForMySearchQueriesPanel();
        lefthandStackedBox.setDataForPopularSearchCategoriesPanel(CommonUtils.getPopularSearchCategories());
    }

    /**
     * Initialize GUI for main frame.
     */
    private void initGUI() {
        setExtendedState(JXFrame.MAXIMIZED_BOTH);
        URL url = this.getClass().getClassLoader().getResource(CAB2B_LOGO_IMAGE);
        Image im = Toolkit.getDefaultToolkit().getImage(url);
        this.setIconImage(im);
        this.setLayout(new BorderLayout());

        globalNavigationPanel = new GlobalNavigationPanel(this, this);
        this.add(globalNavigationPanel, BorderLayout.NORTH);

        // set new welcome panel
        newWelcomePanel = new NewWelcomePanel(this);
        homePanel = newWelcomePanel;
        homePanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));

        lefthandStackedBox = new MainFrameStackedBoxPanel(this);
        lefthandStackedBox.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, lefthandStackedBox, homePanel);
        splitPane.setBorder(null);
        splitPane.setDividerSize(4);
        splitPane.setDividerLocation(242);
        splitPane.setOneTouchExpandable(false);

        mainPanel = new Cab2bPanel();
        mainPanel.setBorder(new CustomizableBorder(new Insets(10, 0, 6, 10), true, true));
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
        mainPanel.add("hfill vfill", splitPane);

        this.add(mainPanel, BorderLayout.CENTER);

        statusBar = WindowUtilities.getStatusBar(this);
        JXStatusBar.Constraint c1 = new JXStatusBar.Constraint();
        c1.setFixedWidth(100);
        status = new Cab2bLabel(Status.READY.getTextToShow());
        statusMessage = new Cab2bLabel();
        statusBar.add(status, c1); // Fixed width of 100 with no inserts
        statusBar.add(new JSeparator(JSeparator.VERTICAL));
        statusBar.add(statusMessage);
        this.add(statusBar, BorderLayout.SOUTH);
        JFrame.setDefaultLookAndFeelDecorated(true);
    }

    public GlobalNavigationPanel getGlobalNavigationPanel() {
        return this.globalNavigationPanel;
    }

    /** Method to set experiment home panel */
    public void setOpenExperimentWelcomePanel() {
        CustomSwingWorker swingWorker = new CustomSwingWorker(MainFrame.mainPanel) {
            @Override
            protected void doNonUILogic() throws Exception {
                if (openExperimentWelcomePanel != null && openExperimentWelcomePanel.isVisible()) {
                    openExperimentWelcomePanel.removeAll();
                }
                openExperimentWelcomePanel = new ExperimentPanel();
                GradientPaint gp = new GradientPaint(new Point2D.Double(.05d, 0), Color.WHITE, new Point2D.Double(
                        .95d, 0), Color.WHITE);
                openExperimentWelcomePanel.setTitlePainter(new BasicGradientPainter(gp));
                openExperimentWelcomePanel.setTitleForeground(Color.BLUE);
                openExperimentWelcomePanel.validate();
            }

            @Override
            protected void doUIUpdateLogic() throws Exception {
                mainPanel.removeAll();
                MainFrame.this.remove(mainPanel);
                mainPanel.add(" hfill vfill ", openExperimentWelcomePanel);
                MainFrame.this.add(mainPanel);
            }
        };
        swingWorker.start();
    }

    /** Method to set data search home panel */
    public void setSearchDataWelcomePanel() {

        CustomSwingWorker swingWorker = new CustomSwingWorker(this) {
            @Override
            protected void doNonUILogic() throws Exception {
                if (searchDataWelcomePanel == null) {
                    searchDataWelcomePanel = new SearchDataWelcomePanel(MainFrame.this);
                }
                homePanel = searchDataWelcomePanel;
            }

            @Override
            protected void doUIUpdateLogic() throws Exception {
                setWelcomePanel();
            }
        };
        swingWorker.start();
    }

    /** Method to set home panel */
    public void setHomeWelcomePanel() {

        CustomSwingWorker swingWorker = new CustomSwingWorker(this) {
            @Override
            protected void doNonUILogic() throws Exception {
                if (newWelcomePanel == null) {
                    newWelcomePanel = new NewWelcomePanel(MainFrame.this);
                }
                homePanel = newWelcomePanel;
            }

            @Override
            protected void doUIUpdateLogic() throws Exception {
                setWelcomePanel();
            }
        };
        swingWorker.start();
    }

    /** Method to set Welcome panel */
    public void setWelcomePanel() {

        if (openExperimentWelcomePanel != null && openExperimentWelcomePanel.isVisible() == true) {
            Logger.out.debug("Removing openExperimentWelcomePanel");
            openExperimentWelcomePanel.removeAll();
            this.remove(openExperimentWelcomePanel);
            this.validate();
        }

        mainPanel.removeAll();
        splitPane.setRightComponent(homePanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(242);
        splitPane.setOneTouchExpandable(false);
        mainPanel.add("hfill vfill", splitPane);
        this.add(mainPanel);
    }

    /**
     * initializes resources like errorcode handler , Application Properties etc
     */
    protected static void initializeResources() {
        try {
            ErrorCodeHandler.initBundle(ERROR_CODE_FILE_NAME);
            ApplicationProperties.initBundle(APPLICATION_RESOURCES_FILE_NAME);
        } catch (MissingResourceException mre) {
            CheckedException checkedException = new CheckedException(mre.getMessage(), mre,
                    ErrorCodeConstants.IO_0002);
            CommonUtils.handleException(checkedException, null, true, true, false, true);
        }
    }

    /**
     * set the status bar message
     * @param message the status message
     */
    public static void setStatusMessage(String message) {
        MainFrame.statusMessage.setText(message);
    }

    /**
     * set the status in the status bar
     * @param status the status
     */
    public static void setStatus(Status status) {
        MainFrame.status.setText(status.getTextToShow());
    }

    /**
     * The main method to launch caB2B client application.
     * @param args Command line arguments. They will not be used.
     */
    public static void main(String[] args) {
        try {
            
            setHome();
            Logger.configure(); //pick config from log4j.properties
            initializeResources(); // Initialize all Resources
            ClientLauncher clientLauncher = ClientLauncher.getInstance();
            clientLauncher.launchClient();

            MainFrame mainFrame = new MainFrame(ApplicationProperties.getValue(MAIN_FRAME_TITLE), true);
            mainFrame.pack();
            mainFrame.setVisible(true);
            mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } catch (Throwable t) {
            JXErrorDialog.showDialog(
                                     null,
                                     "caB2B Fatal Error",
                                     "Fatal error orccured while launching caB2B client.\nPlease contact administrator",
                                     t);
            System.exit(1);
        }
    }

    /**
     * @return Returns the mainframeScreenDimesion.
     */
    public static Dimension getScreenDimesion() {
        return mainframeScreenDimesion;
    }

    /**
     * set the caB2B Home used to keep editable configurations,
     * appliaction logs etc.
     *
     */
    public static void setHome() {
        String userHome = System.getProperty("user.home");

        File cab2bHome = new File(userHome, "cab2b");
        System.setProperty("cab2b.home", cab2bHome.getAbsolutePath());
    }

    /**
     * @return Returns the userName.
     */
    public static String getUserName() {
        return userName;
    }

    /**
     * @param userName The userName to set.
     */
    public static void setUserName(String userName) {
        MainFrame.userName = userName;
    }

    public static void updateMySeachQueryBox() {
        QueryEngineBusinessInterface queryEngineBusinessInterface = (QueryEngineBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                                    EjbNamesConstants.QUERY_ENGINE_BEAN,
                                                                                                                                    QueryEngineHome.class,
                                                                                                                                    null);
        List<ICab2bParameterizedQuery> cab2bQueryList = null;
        try {
            cab2bQueryList = queryEngineBusinessInterface.retrieveAllQueries();
        } catch (Exception exception) {
            CommonUtils.handleException(exception, MainFrame.newWelcomePanel, true, true, true, false);
        }

        if (cab2bQueryList != null && !cab2bQueryList.isEmpty()) {
            lefthandStackedBox.updateMySearchQueryPanel(cab2bQueryList);
        }
    }
}