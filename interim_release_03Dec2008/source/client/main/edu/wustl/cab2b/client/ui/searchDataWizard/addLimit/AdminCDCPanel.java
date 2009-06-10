package edu.wustl.cab2b.client.ui.searchDataWizard.addLimit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JSplitPane;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.mainframe.Cab2bContentPanel;
import edu.wustl.cab2b.client.ui.searchDataWizard.AttributeSelectCDCPanel;
import edu.wustl.cab2b.client.ui.searchDataWizard.SearchPanel;
import edu.wustl.cab2b.client.ui.searchDataWizard.dag.Cab2bDag;
import edu.wustl.cab2b.client.ui.util.WindowUtilities;

/**
 * Panel for creating a custom category.
 * 
 * @author Hrishikesh Rajpathak
 *
 */
public class AdminCDCPanel extends Cab2bPanel {

    public AdminCDCPanel() {
        super();
        initGUI();
    }

    /**
     * Initialize UI
     */
    private void initGUI() {
        Cab2bPanel outerPanel = new Cab2bPanel(new BorderLayout());

        Cab2bPanel innerPanel = new Cab2bPanel(new BorderLayout());
        AttributeSelectCDCPanel cdcPanel = new AttributeSelectCDCPanel();
        SearchPanel searchPanel = new SearchPanel(new Cab2bContentPanel(), cdcPanel);
        MainFrame.setScreenDimesion(Toolkit.getDefaultToolkit().getScreenSize());
        Dimension dimension = MainFrame.getScreenDimesion();
        searchPanel.setMinimumSize(new Dimension(270, dimension.height));
        searchPanel.srhTextField.setPreferredSize(new Dimension(175, 20));

        JSplitPane verticlePane = new JSplitPane();
        JSplitPane horizontalPane = new JSplitPane();

        Cab2bDag testDAG = new Cab2bDag();

        searchPanel.setTestDAG(testDAG);
        cdcPanel.setMinimumSize(new Dimension(300, 300));

        horizontalPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, cdcPanel, testDAG);
        horizontalPane.setOneTouchExpandable(false);
        horizontalPane.setBorder(null);
        horizontalPane.setDividerSize(4);
        horizontalPane.setDividerLocation(275);
        innerPanel.add(horizontalPane);

        verticlePane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, searchPanel, innerPanel);
        verticlePane.setDividerLocation(0.2D);
        verticlePane.setOneTouchExpandable(false);
        verticlePane.setBorder(null);
        verticlePane.setDividerSize(4);
        verticlePane.setDividerLocation(275);

        outerPanel.add(verticlePane, BorderLayout.CENTER);
        WindowUtilities.showInDialog(NewWelcomePanel.getMainFrame(), outerPanel, "hi", new Dimension(
                (int) (dimension.width * 0.90), (int) (dimension.height * 0.85)), true, true);
    }

}
