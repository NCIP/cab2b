package edu.wustl.cab2b.client.ui.mainframe;

import static edu.wustl.cab2b.client.ui.util.ClientConstants.MY_EXPERIMENT_IMAGE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.MY_SEARCH_QUERIES_IMAGE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.POPULAR_CATEGORIES_IMAGE;
import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.QUERY_BOX_TEXT;
import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.POPULAR_CATEGORY_BOX_TEXT;
import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.EXPERIMENT_BOX_TEXT;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.StackedBox;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * @author Chandrakant Talele
 */
public class MainFrameStackedBoxPanel extends Cab2bPanel {

    private static final long serialVersionUID = 1L;

    JPanel mySearchQueriesPanel;

    JPanel popularSearchCategoryPanel;

    JPanel myExperimentsPanel;

    /**
     * @param mainFrame
     */
    public MainFrameStackedBoxPanel() {
        this.setLayout(new BorderLayout());
        StackedBox stackedBox = new StackedBox();
        stackedBox.setTitleBackgroundColor(new Color(200, 200, 220));
        JScrollPane scrollPane = new JScrollPane(stackedBox);
        scrollPane.setBorder(null);
        this.add(scrollPane, BorderLayout.CENTER);
        
        mySearchQueriesPanel = getPanel();
        final String titleQuery = ApplicationProperties.getValue(QUERY_BOX_TEXT);
        stackedBox.addBox(titleQuery, mySearchQueriesPanel, MY_SEARCH_QUERIES_IMAGE, false);

        popularSearchCategoryPanel = getPanel();
        final String titlePopularcategories = ApplicationProperties.getValue(POPULAR_CATEGORY_BOX_TEXT);
        stackedBox.addBox(titlePopularcategories, popularSearchCategoryPanel, POPULAR_CATEGORIES_IMAGE, false);

        myExperimentsPanel = getPanel();
        final String titleExpr = ApplicationProperties.getValue(EXPERIMENT_BOX_TEXT);
        stackedBox.addBox(titleExpr, myExperimentsPanel, MY_EXPERIMENT_IMAGE, false);

        stackedBox.setPreferredSize(new Dimension(250, 500));
        stackedBox.setMinimumSize(new Dimension(250, 500));

        this.setMinimumSize(new Dimension(242, this.getMinimumSize().height)); //fix for bug#3745
    }

    /**
     * @return
     */
    private JPanel getPanel() {
        JPanel panel = new Cab2bPanel();
        panel.setLayout(new RiverLayout(10, 5));
        panel.setPreferredSize(new Dimension(250, 150));
        panel.setOpaque(false);
        return panel;
    }

    /**
     * @param data
     */
    public void setDataForMySearchQueriesPanel(Vector data) {
        setDataForPanel(mySearchQueriesPanel, data);
    }

    /**
     * @param data
     */
    public void setDataForPopularSearchCategoriesPanel(Vector data) {
        setDataForPanel(popularSearchCategoryPanel, data);
    }

    /**
     * @param data
     */
    public void setDataForMyExperimentsPanel(Vector data) {
        setDataForPanel(myExperimentsPanel, data);
    }

    /**
     * @param panel
     * @param data
     */
    private void setDataForPanel(JPanel panel, Vector data) {
        panel.removeAll();
        panel.add(new Cab2bLabel());
        Iterator iter = data.iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            String hyperlinkName = obj.toString();
            Cab2bHyperlink hyperlink = new Cab2bHyperlink(true);
            hyperlink.setText(hyperlinkName);
            panel.add("br", hyperlink);
        }
        panel.revalidate();
    }
}