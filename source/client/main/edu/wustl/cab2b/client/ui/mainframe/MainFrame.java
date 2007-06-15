package edu.wustl.cab2b.client.ui.mainframe;

import static edu.wustl.cab2b.client.ui.util.ClientConstants.ERROR_CODE_FILE_NAME;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.APPLICATION_RESOURCES_FILE_NAME;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.CAB2B_LOGO_IMAGE;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;

import edu.wustl.cab2b.client.cache.ClientSideCache;
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
import edu.wustl.cab2b.common.exception.RuntimeException;
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

    //public static URL progressBarURL;

    /** Everything related GUI's containers and its components size is relative to this size. */
    public static Dimension mainframeScreenDimesion = Toolkit.getDefaultToolkit().getScreenSize();

    private static SearchDataWelcomePanel searchDataWelcomePanel = null;

    public static ExperimentPanel openExperimentWelcomePanel = null;

    public static NewWelcomePanel newWelcomePanel = null;

    private static JProgressBar progressBar;

    private static JLabel progressBarLabel;

    /** Global navigation panel which is at the top of the MainFrame */
    private GlobalNavigationPanel globalNavigationPanel;

    private JXPanel homePanel;

    private MainFrameStackedBoxPanel lefthandStackedBox;

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
        lefthandStackedBox.setDataForMyExperimentsPanel(getExperiments());
        lefthandStackedBox.setDataForMySearchQueriesPanel(getUserSearchQueries());
        lefthandStackedBox.setDataForPopularSearchCategoriesPanel(getPopularSearchCategories());
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
    }

    /** Method to set experiment home panel */
    public void setOpenExperimentWelcomePanel() {
        CustomSwingWorker swingWorker = new CustomSwingWorker(MainFrame.mainPanel) {
            @Override
            protected void doNonUILogic() throws RuntimeException {

                if (openExperimentWelcomePanel != null && openExperimentWelcomePanel.isVisible()) {
                    openExperimentWelcomePanel.removeAll();
                }

                openExperimentWelcomePanel = new ExperimentPanel("My Experiments");
                GradientPaint gp = new GradientPaint(new Point2D.Double(.05d, 0), Color.WHITE, new Point2D.Double(
                        .95d, 0), Color.WHITE);
                openExperimentWelcomePanel.setTitlePainter(new BasicGradientPainter(gp));
                openExperimentWelcomePanel.setTitleForeground(Color.BLUE);
                openExperimentWelcomePanel.validate();
            }

            @Override
            protected void doUIUpdateLogic() throws RuntimeException {
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
     * @return Popular categories at this point
     */
    private Vector getPopularSearchCategories() {
        /* TODO These default Search Categories will be removed after its support*/
        Vector<String> popularSearchCategories = new Vector<String>();
        popularSearchCategories.add("Gene Annotation");
        popularSearchCategories.add("Microarray Annotation");
        popularSearchCategories.add("Tissue Biospecimens");
        popularSearchCategories.add("Molecular Biospecimens");
        return popularSearchCategories;
    }

    /**
     * @return Recent search queries
     */
    private Vector getUserSearchQueries() {
        /* TODO These default UserSearchQueries will be removed later after SAVE QUERY support*/
        Vector<String> mySearchQueries = new Vector<String>();
        mySearchQueries.add("Prostate Cancer Microarray Data");
        mySearchQueries.add("Glioblastoma Microarray Data");
        return mySearchQueries;
    }

    /**
     * @return All the experiments performed by the user.
     */
    private Vector getExperiments() {
        /* TODO These default experiments will be removed later on*/
        Vector<String> myRecentExperiments = new Vector<String>();
        myRecentExperiments.add("Breast Cancer Microarrays (Hu133 Plus 2.0)");
        myRecentExperiments.add("Breast Cancer Microarrays (MOE430 Plus 2.0)");
        myRecentExperiments.add("Acute Myelogenous Leukemia Microarrays");
        return myRecentExperiments;
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
     * Loads the client side cache. If fails, logs error and quits
     */
    private static void loadCache() {
        try {
            ClientSideCache.getInstance();
        } catch (Throwable t) {
            Logger.out.error("Unable to get data from caB2B server. Please verify caB2B server address and port in conf/jndi.properties");
            Logger.out.error(t.getMessage());
            System.exit(1);
        }
    }

    /**
     * @return Returns the progressBarLabel.
     */
    public static JLabel getProgressBarLabel() {
        return progressBarLabel;
    }

    /**
     * @return Returns the progressBar.
     */
    public static JProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * @param width desired width of progressbar
     * @param height desired height of progressbar
     * @return Progress bar of given dimensions
     */
    private static JProgressBar getProgressbar(int width, int height) {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(width, height));
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        progressBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return progressBar;
    }

    /**
     * @param text The text to set as label
     * @param width desired width of label
     * @param height desired height of label
     * @return JLabel with given text and of given dimensions
     */
    private static JLabel getProgressBarLabel(String text, int width, int height) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setPreferredSize(new Dimension(width, height));
        String fontFamily = label.getFont().getFamily();
        label.setFont(new Font(fontFamily, Font.PLAIN, 14));
        return label;
    }

    /**
     * The main method to launch caB2B client application.
     * @param args Command line arguments. They will not be used.
     */
    public static void main(String[] args) {
        Logger.configure("");

        JFrame launchFrame = new JFrame("caB2B client launcher....");
        int imageX = 442;
        int imageY = 251;
        int progressbarY = 14;
        int labelY = 20;
        progressBar = getProgressbar(imageX, progressbarY);
        progressBarLabel = getProgressBarLabel(" Launching caB2B client....", imageX, labelY);

        /* Initialize all Resources. */
        initializeResources();
        MainFrame mainFrame = new MainFrame(ApplicationProperties.getValue("cab2b.main.frame.title"), true);
        ClassLoader loader = mainFrame.getClass().getClassLoader();

        BackgroundImagePanel imagePanel = new BackgroundImagePanel(new ImageIcon(
                loader.getResource("progress_bar.gif")).getImage());
        imagePanel.setPreferredSize(new Dimension(imageX, imageY));
        imagePanel.add(progressBarLabel, BorderLayout.SOUTH);

        int height = imageY + progressbarY;
        launchFrame.setSize(imageX, height);
        launchFrame.getContentPane().add(imagePanel, BorderLayout.CENTER);
        launchFrame.getContentPane().add(progressBar, BorderLayout.SOUTH);
        launchFrame.setLocation((mainframeScreenDimesion.width - imageX) / 2,
                                (mainframeScreenDimesion.height - height) / 2);
        URL url = MainFrame.class.getClassLoader().getResource(CAB2B_LOGO_IMAGE);
        launchFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        launchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        launchFrame.setAlwaysOnTop(true);
        launchFrame.setUndecorated(true);
        launchFrame.setVisible(true);
        Toolkit.getDefaultToolkit().setDynamicLayout(true);

        loadCache(); /*   initializing the cache at startup  */
        progressBar.setValue(100);
        launchFrame.setVisible(false);
        launchFrame.removeAll();
        launchFrame = null;
        mainFrame.setVisible(true);
    }
}