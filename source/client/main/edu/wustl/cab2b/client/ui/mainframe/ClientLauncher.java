package edu.wustl.cab2b.client.ui.mainframe;

import static edu.wustl.cab2b.client.ui.util.ClientConstants.CAB2B_LOGO_IMAGE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import edu.wustl.cab2b.client.cache.ClientSideCache;
import edu.wustl.cab2b.client.cache.UserCache;
import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.dag.ClassNodeRenderer;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.locator.LocatorException;
import edu.wustl.cab2b.common.user.UserInterface;

/**
 * @author Chandrakant Talele
 */
public class ClientLauncher {
    private static JProgressBar progressBar;

    private static JLabel progressBarLabel;

    private static ClientLauncher clientLauncher;

    private static final Dimension imageSize = new Dimension(442, 251);

    private static final int progressHeight = 11;

    private static final int labelWidth = 20;
    
    private static UserInterface caB2BUser;

    /**
     * @param user 
     * @return Returns the singleton instance of the ClientLauncher class.
     */
    public static synchronized ClientLauncher getInstance(UserInterface user) {
        if (clientLauncher == null) {
            clientLauncher = new ClientLauncher(user);
        }
        return clientLauncher;
    }
    
    public static synchronized ClientLauncher getInstance() {
      /*  if (clientLauncher == null) {
            clientLauncher = new ClientLauncher(user);
        }*/
        return clientLauncher;
    }

    /**
     * Makes the progress bar associated with this object as determinate.
     */
    public void setDeterminate() {
        progressBar.setIndeterminate(false);
    }

    /**
     * Private constructor
     * @param user 
     */
    private ClientLauncher(UserInterface user) {
        progressBar = getProgressbar(imageSize.width, progressHeight);
        progressBarLabel = getProgressBarLabel(" Launching caB2B client....", imageSize.width, labelWidth);
        caB2BUser=user;
    }

    /**
     * @param text Text to show
     * @param completedvalue Percentage completed
     */
    public void showProgress(String text, int completedvalue) {
        if (progressBar != null && progressBar != null) {
            progressBar.setValue(completedvalue);

            progressBarLabel.setText(text);
            Rectangle labelRect = progressBarLabel.getBounds();
            labelRect.x = 0;
            labelRect.y = 0;
            progressBarLabel.paintImmediately(labelRect);

            Rectangle progressRect = progressBar.getBounds();
            progressRect.x = 0;
            progressRect.y = 0;
            progressBar.paintImmediately(progressRect);
        }
    }

    /**
     * Launches the cab2b Client
     */
    public void launchClient() {
        ClassLoader loader = this.getClass().getClassLoader();
        JFrame launchFrame = new JFrame("caB2B client launcher....");
        
        BackgroundImagePanel imagePanel = new BackgroundImagePanel(new ImageIcon(
                loader.getResource("progress_bar.gif")).getImage());

        imagePanel.setPreferredSize(new Dimension(imageSize.width, imageSize.height));
        imagePanel.add(progressBarLabel, BorderLayout.SOUTH);
        imagePanel.updateUI();

        int height = imageSize.height + progressHeight;
        launchFrame.setSize(imageSize.width + 1, height + 1);
        launchFrame.getContentPane().add(imagePanel, BorderLayout.CENTER);
        launchFrame.getContentPane().add(progressBar, BorderLayout.SOUTH);
        WindowUtilities.centerWindow(launchFrame);

        URL url = MainFrame.class.getClassLoader().getResource(CAB2B_LOGO_IMAGE);
        launchFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        launchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        launchFrame.setAlwaysOnTop(false);
        launchFrame.setUndecorated(true);

        launchFrame.setBackground( Color.CYAN);
        launchFrame.setVisible(true);
        Toolkit.getDefaultToolkit().setDynamicLayout(true);

        loadCache(caB2BUser); /* initializing the cache at startup */

        showProgress(" Initializing graphical user interface....", 94);
        // To speed-up the first add limit.
        launchFrame.getGraphics().getFontMetrics(ClassNodeRenderer.font);
        showProgress(" Launching....", 100);
        launchFrame.setVisible(false);
        launchFrame.removeAll();
        launchFrame = null;
    }

    /**
     * @param width desired width of progressbar
     * @param height desired height of progressbar
     * @return Progress bar of given dimensions
     */
    private static JProgressBar getProgressbar(int width, int height) {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setForeground(new Color(0x034E74));
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(width + 1, height + 1));
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        progressBar.setBorder(BorderFactory.createLineBorder(new Color(0x034E74)));
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
     * Loads the client side cache. If fails, logs error and quits
     * @param caB2BUser2 
     */
    private static void loadCache(UserInterface caB2BUser2) {
    	
    	UserCache.getInstance().init(caB2BUser2);
        try {
            ClientSideCache.getInstance();
        } catch (LocatorException le) {
            CommonUtils.handleException(le, progressBar.getParent(), true, true, true, true);
        }
    }
}