package edu.wustl.cab2b.client.ui.mainframe;

import static edu.wustl.cab2b.client.ui.util.ClientConstants.CAB2B_LOGO_IMAGE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.LOGOUT_ICON;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;

import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.CustomizableBorder;
import edu.wustl.cab2b.client.ui.experiment.ExperimentOpenPanel;
import edu.wustl.cab2b.client.ui.experiment.ExperimentPanel;
import edu.wustl.cab2b.client.ui.mainframe.stackbox.MainFrameStackedBoxPanel;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;

/**
 * Main frame of the caB2B application. It is the home page of application.
 * 
 * @author chetan_bh
 * @author Chandrakant Talele
 * @author Deepak
 * @author Juber
 */
public class MainFrame extends JXFrame {
    private static final long serialVersionUID = 1234567890L;

    private String userName;

    /**
     * Everything related GUI's containers and its components size is relative
     * to this size.
     */
    private static Dimension screenDimesion = Toolkit.getDefaultToolkit().getScreenSize();

    private SearchDataWelcomePanel htmlPanel = null;

    private ExperimentPanel experimentPanel = null;

    private NewWelcomePanel welcomePanel = null;

    /**
     *  Global navigation panel which is at the top of the MainFrame 
     */
    private GlobalNavigationPanel globalNavigationPanel;

    private JXPanel homePanel;

    private MainFrameStackedBoxPanel mainFrameStackBox;

    private JSplitPane splitPane;

    private JDialog searchWizardDialog;

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

    private JXPanel mainPanel;

    /**
     * Constructor
     * @param title
     * @param exitOnClose
     */
    public MainFrame(String title, boolean exitOnClose) {
        super(title, exitOnClose);
        statusBar = WindowUtilities.getStatusBar(this);
        initGUI();
    }

    /**
     * Initialize GUI for main frame.
     */
    private void initGUI() {
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setExtendedState(JXFrame.MAXIMIZED_BOTH);
        URL url = this.getClass().getClassLoader().getResource(CAB2B_LOGO_IMAGE);
        Image im = Toolkit.getDefaultToolkit().getImage(url);
        this.setIconImage(im);
        this.setLayout(new BorderLayout());

        globalNavigationPanel = new GlobalNavigationPanel(this, this);
        this.add(globalNavigationPanel, BorderLayout.NORTH);

        // set new welcome panel
        welcomePanel = new NewWelcomePanel(this);
        homePanel = welcomePanel;
        homePanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));

        mainFrameStackBox = new MainFrameStackedBoxPanel(this);
        mainFrameStackBox.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainFrameStackBox, homePanel);
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
        statusBar.add(status, c1);
        statusBar.add(new JSeparator(JSeparator.VERTICAL));
        statusBar.add(statusMessage);
        this.add(statusBar, BorderLayout.SOUTH);
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                logout();
            }
        });
    }

    /**
     * @return GlobalNavigationPanel
     */
    public GlobalNavigationPanel getGlobalNavigationPanel() {
        return this.globalNavigationPanel;
    }

    /**
     * Generates logout dialog pop-up  
     */
    public void logout() {
        final Icon logoutIcon = new ImageIcon(this.getClass().getClassLoader().getResource(LOGOUT_ICON));
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout",
                                                   JOptionPane.YES_NO_OPTION, JOptionPane.YES_NO_OPTION,
                                                   logoutIcon);
        if (option == JOptionPane.YES_OPTION) {
            System.exit(-1);
        }
    }

    /**
     * Method to set experiment home panel 
     */
    public void setExperimentHomePanel() {
        CustomSwingWorker swingWorker = new CustomSwingWorker(MainFrame.this.mainPanel) {
            @Override
            protected void doNonUILogic() throws Exception {
                if (experimentPanel != null && experimentPanel.isVisible()) {
                    experimentPanel.removeAll();
                }
                experimentPanel = new ExperimentPanel();
                GradientPaint gp = new GradientPaint(new Point2D.Double(.05d, 0), Color.WHITE, new Point2D.Double(
                        .95d, 0), Color.WHITE);
                experimentPanel.setTitlePainter(new BasicGradientPainter(gp));
                experimentPanel.setTitleForeground(Color.BLUE);
                experimentPanel.validate();
            }

            @Override
            protected void doUIUpdateLogic() throws Exception {
                mainPanel.removeAll();
                MainFrame.this.remove(mainPanel);
                mainPanel.add(" hfill vfill ", experimentPanel);
                MainFrame.this.add(mainPanel);
            }
        };
        swingWorker.start();
    }

    /**
     *  Method to set data search home panel
     */
    public void setSearchDataWelcomePanel() {

        CustomSwingWorker swingWorker = new CustomSwingWorker(this) {
            @Override
            protected void doNonUILogic() throws Exception {
                if (htmlPanel == null) {
                    htmlPanel = new SearchDataWelcomePanel(MainFrame.this);
                }
                homePanel = htmlPanel;
            }

            @Override
            protected void doUIUpdateLogic() throws Exception {
                setWelcomePanel();
            }
        };
        swingWorker.start();
    }

    /**
     *  Method to set home panel 
     */
    public void setHomeWelcomePanel() {

        CustomSwingWorker swingWorker = new CustomSwingWorker(this) {
            @Override
            protected void doNonUILogic() throws Exception {
                if (welcomePanel == null) {
                    welcomePanel = new NewWelcomePanel(MainFrame.this);
                }
                homePanel = welcomePanel;
            }

            @Override
            protected void doUIUpdateLogic() throws Exception {
                setWelcomePanel();
            }
        };
        swingWorker.start();
    }

    /**
     *  Method to set right hand side panel on home page 
     * @param panel
     */
    public void setRightHandPanel(Cab2bPanel panel) {
        homePanel = panel;
        CustomSwingWorker swingWorker = new CustomSwingWorker(this, panel) {
            @Override
            protected void doNonUILogic() throws Exception {
            }

            @Override
            protected void doUIUpdateLogic() throws Exception {
                setWelcomePanel();
            }
        };
        swingWorker.start();
    }

    /**
     * Method to set ExperimentOpenPanel as home-panel
     * @param panel
     */
    public void openExperiment(ExperimentOpenPanel panel) {
        mainPanel.removeAll();
        this.remove(mainPanel);
        mainPanel.add("hfill vfill", panel);
        this.add(mainPanel);
    }

    /**
     * Method to set Welcome panel on homepage
     */
    public void setWelcomePanel() {

        if (experimentPanel != null && experimentPanel.isVisible() == true) {
            experimentPanel.removeAll();
            this.remove(experimentPanel);
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
     * set the status bar message
     * 
     * @param message
     *            the status message
     */
    public static void setStatusMessage(String message) {
        MainFrame.statusMessage.setText(message);
    }

    /**
     * set the status in the status bar
     * 
     * @param status
     *            the status
     */
    public static void setStatus(Status status) {
        MainFrame.status.setText(status.getTextToShow());
    }

    /**
     * @return Returns the screenDimesion.
     */
    public static Dimension getScreenDimesion() {
        return screenDimesion;
    }

    /**
     * @return Returns the userName.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            The userName to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the searchWizardDialog
     */
    public JDialog getSearchWizardDialog() {
        return searchWizardDialog;
    }

    /**
     * @param searchWizardDialog
     *            the searchWizardDialog to set
     */
    public void setSearchWizardDialog(JDialog searchWizardDialog) {
        this.searchWizardDialog = searchWizardDialog;
    }

    public void closeSearchWizardDialog() {
        searchWizardDialog.dispose();
    }

    /**
     * @return the mainFrameStackBox
     */
    public MainFrameStackedBoxPanel getMainFrameStackBox() {
        return mainFrameStackBox;
    }

    /**
     * @param mainFrameStackBox the mainFrameStackBox to set
     */
    public void setMainFrameStackBox(MainFrameStackedBoxPanel mainFrameStackBox) {
        this.mainFrameStackBox = mainFrameStackBox;
    }

    /**
     * @return the experimentPanel
     */
    public ExperimentPanel getExperimentPanel() {
        return experimentPanel;
    }

    /**
     * @param experimentPanel the experimentPanel to set
     */
    public void setExperimentPanel(ExperimentPanel experimentPanel) {
        this.experimentPanel = experimentPanel;
    }

    /**
     * @param screenDimesion the screenDimesion to set
     */
    public static void setScreenDimesion(Dimension screenDimesion) {
        MainFrame.screenDimesion = screenDimesion;
    }

}