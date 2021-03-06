/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.mainframe;

import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.SEARCH_FRAME_TITLE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JDialog;

import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.searchDataWizard.MainSearchPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.WindowUtilities;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * This is a Search data welcome panel, which should be visibal when
 * user wants to do Search Data activity.
 * 
 * @author Deepak Shingan
 */

public class SearchDataWelcomePanel extends Cab2bPanel { 

    /**
     * Parent frame reference
     */
    private MainFrame mainFrame;

    /**
     * Constructor 
     * Creates SearchDataWelcomePanel with mainframe as parent frame
     * @param mainFrame
     */
    public SearchDataWelcomePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initGUI();
    }

    /**
     * Method for intilizing UI
     */
    private void initGUI() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);

        Cab2bPanel sidePanel = new Cab2bPanel();

        Cab2bPanel contentPanel = new Cab2bPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        Cab2bLabel searchLabel = new Cab2bLabel("<html><br><br>Search Data<br><br></html>");
        searchLabel.setForeground(new Color(0x006699));
        Font labelFont = searchLabel.getFont();
        Font largeFont = labelFont.deriveFont(Font.BOLD, 20.0f);
        searchLabel.setFont(largeFont);
        contentPanel.add(searchLabel);
        final String title = ApplicationProperties.getValue(SEARCH_FRAME_TITLE);
        Cab2bButton searchDataButton = new Cab2bButton(title);
        searchDataButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GlobalNavigationPanel.setMainSearchPanel(new MainSearchPanel());
                Dimension relDimension = CommonUtils.getRelativeDimension(MainFrame.getScreenDimension(), 0.90f,
                                                                          0.85f);
                MainSearchPanel mainSearchPanel = GlobalNavigationPanel.getMainSearchPanel();
                mainSearchPanel.setPreferredSize(relDimension);
                mainSearchPanel.setSize(relDimension);

                //edu.wustl.cab2b.client.ui.util.CommonUtils.FrameReference = mainFrame;

                // Update the variable for latest screen dimension from the toolkit, this is to handle the situations were
                // Application is started and then screen resolution is changed, but the variable stiil holds old resolution size.
                MainFrame.setScreenDimesion(Toolkit.getDefaultToolkit().getScreenSize());
                Dimension dimension = MainFrame.getScreenDimension();
                JDialog searchWizard = WindowUtilities.setInDialog(mainFrame, mainSearchPanel, title,
                                                                   new Dimension((int) (dimension.width * 0.90),
                                                                           (int) (dimension.height * 0.85)), true,
                                                                   true);
                mainFrame.setSearchWizardDialog(searchWizard);
                searchWizard.setVisible(true);
                mainSearchPanel.getDataList().clear();
                mainSearchPanel = null;
            }
        });
        contentPanel.add(searchDataButton);
        Cab2bLabel searchInfoLabel = new Cab2bLabel("<html>	</head>	<body><br><br>"
                + "<b>What will it Search�</b><br><br>  For conducting an experiment, "
                + "you can search data in diverse biomedical categories using caB2B Search functionality. "
                + "You can also find all the associated <br> information and make your own Data List. "
                + "The Data List can be filtered, analyzed and visualized in the Experiment module.</br></br>"
                + "<br><br><b>Where will it search�</b><br>"
                + "<br>B2B connects you to the databases of hundreds of applications. If you want you"
                + "	can select the  data base instances where you wish to search." + "</body></html>");
        contentPanel.add(searchInfoLabel);
        this.add(sidePanel, BorderLayout.WEST);
        this.add(contentPanel, BorderLayout.CENTER);
    }
}
