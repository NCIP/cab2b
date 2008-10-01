/**
 * 
 */
package edu.wustl.cab2b.client.ui.mysettings;

import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.MYSETTINGS_FRAME_TITLE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.MENU_CLICK_EVENT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.SERVICE_URL;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JPanel;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.util.WindowUtilities;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * @author atul_jawale
 * This class is the mySetting page (main container) opens when user clicks the My Settings link.
 *  used to configure the service instances. 
 */

public class MySettingPanel extends Cab2bPanel {

    private static final long serialVersionUID = 9175665938340239421L;

    /**
     * used to make a reference of the main Cab2bPanel.
     */
    private Frame parent;

    /**
     * This left panel will contain the menus e.g. Service URLS
     */
    private LeftPanel leftMenuPanel;

    /**
     * According to the menu selection right panel will contain the page 
     */
    private Cab2bPanel rightPanel;

    /**
     * 
     * @param mainFrame - reference of the main frame
     */
    public MySettingPanel(final Frame parent) {
        super(new BorderLayout(10, 10));
        this.parent = parent;
        initGUI();
    }

    /**
     * Initialize the component
     * This method will create a structure as main container containing two panels i.e. leftPanel
     * and right panel. left panel contains the menu and right panel contains one more panel in which 
     * we will update the pages according to the menu selected by user.   
     */
    private void initGUI() {
        rightPanel = new Cab2bPanel(new BorderLayout(10, 5));
        final RightPanel rightContainer = new RightPanel();
        setRightPanel(rightContainer);
        rightPanel.addPropertyChangeListener(SERVICE_URL, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent arg0) {
                rightContainer.firePropertyChange(SERVICE_URL, true, false);
            }
        });

        leftMenuPanel = new LeftPanel();
        leftMenuPanel.addPropertyChangeListener(MENU_CLICK_EVENT, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                String propertyName = (String) event.getNewValue();
                if (SERVICE_URL.equalsIgnoreCase(propertyName)) {
                    rightPanel.firePropertyChange(SERVICE_URL, true, false);
                }
            }
        });

        add(leftMenuPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        MainFrame.setScreenDimesion(Toolkit.getDefaultToolkit().getScreenSize());
        Dimension screenDimesion = MainFrame.getScreenDimesion();
        final String title = ApplicationProperties.getValue(MYSETTINGS_FRAME_TITLE);
        Dimension dimension =  new Dimension((int) (screenDimesion.width * 0.90), (int) (screenDimesion.height * 0.85));
        JDialog mySettingDialog = WindowUtilities.setInDialog(parent, this, title, dimension, true, true);
//        mainFrame.setSearchWizardDialog(searchWizard);
        mySettingDialog.setVisible(true);
    }

    /**
     * This method will set the right panel when user clicks the menu
     * @param panel
     */
    public void setRightPanel(JPanel panel) {
        rightPanel.removeAll();
        rightPanel.add(panel, BorderLayout.CENTER);
        rightPanel.repaint();
    }

}
