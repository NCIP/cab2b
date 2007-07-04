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
import edu.wustl.cab2b.client.ui.dag.ClassNodeRenderer;
import edu.wustl.cab2b.client.ui.dag.IconNodeRenderer;
import edu.wustl.cab2b.client.ui.dag.SimpleDocumentRenderer;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.locator.LocatorException;

/**
 * @author Chandrakant Talele
 */
public class ClientLauncher {
    private static JProgressBar progressBar;

    private static JLabel progressBarLabel;

    private static ClientLauncher clientLauncher;

    private static Dimension launchImageDimension = new Dimension(442, 251); // dimension

    // of
    // image
    // "progress_bar.gif"

    private static final int progressHeight = 14;

    private static final int labelWidth = 20;

    /**
     * @return Returns the singleton instance of the ClientLauncher class.
     */
    public static synchronized ClientLauncher getInstance() {
        if (clientLauncher == null) {
            clientLauncher = new ClientLauncher();
        }
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
     */
    private ClientLauncher() {
        progressBar = getProgressbar(launchImageDimension.width, progressHeight);
        progressBarLabel = getProgressBarLabel(" Launching caB2B client....", launchImageDimension.width,
                                               labelWidth);
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
        Dimension screenDimension = MainFrame.getScreenDimesion();
        BackgroundImagePanel imagePanel = new BackgroundImagePanel(new ImageIcon(
                loader.getResource("progress_bar.gif")).getImage());

        imagePanel.setPreferredSize(new Dimension(launchImageDimension.width, launchImageDimension.height));
        imagePanel.add(progressBarLabel, BorderLayout.SOUTH);

        int height = launchImageDimension.height + progressHeight;
        launchFrame.setSize(launchImageDimension.width, height);
        launchFrame.getContentPane().add(imagePanel, BorderLayout.CENTER);
        launchFrame.getContentPane().add(progressBar, BorderLayout.SOUTH);
        launchFrame.setLocation((screenDimension.width - launchImageDimension.width) / 2,
                                (screenDimension.height - height) / 2);
        URL url = MainFrame.class.getClassLoader().getResource(CAB2B_LOGO_IMAGE);
        launchFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        launchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        launchFrame.setAlwaysOnTop(true);
        launchFrame.setUndecorated(true);
        launchFrame.setVisible(true);
        Toolkit.getDefaultToolkit().setDynamicLayout(true);

        loadCache(); /* initializing the cache at startup */

        // To speed-up the first add limit.
        launchFrame.getGraphics().getFontMetrics(ClassNodeRenderer.font);
        progressBar.setValue(100);
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
     * Loads the client side cache. If fails, logs error and quits
     */
    private static void loadCache() {
        try {
            ClientSideCache.getInstance();
        } catch (LocatorException le) {
            CommonUtils.handleException(le, progressBar.getParent(), true, true, true, true);
        }
    }
}