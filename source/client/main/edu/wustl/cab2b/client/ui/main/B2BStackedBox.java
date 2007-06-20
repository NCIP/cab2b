package edu.wustl.cab2b.client.ui.main;

import static edu.wustl.cab2b.client.ui.util.ClientConstants.MY_CATEGORIES_IMAGE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.MY_SEARCH_QUERIES_IMAGE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.POPULAR_CATEGORIES_IMAGE;
import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.CATEGORY_BOX_TEXT;
import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.QUERY_BOX_TEXT;
import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.POPULAR_CATEGORY_BOX_TEXT;
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
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * This will be shown in the first step of search data for experiment wizard
 * @author Chandrakant Talele
 */
public class B2BStackedBox extends Cab2bPanel {
    private static final long serialVersionUID = 1L;

    /**
     * default constructor. Initializes the object with its UI parameters 
     */
    public B2BStackedBox() {
        this.setLayout(new BorderLayout());
        this.setBorder(null);
        StackedBox box = new StackedBox();
        box.setTitleBackgroundColor(new Color(224, 224, 224));
        box.setBorder(null);
        JScrollPane scrollPane = new JScrollPane(box);
        // scrollPane.setBorder(new CustomizableBorder(new Insets(1,1,1,1), true, true));

        this.add(scrollPane, BorderLayout.CENTER);

        JPanel status = getPanel();
        setDataForPanel(status, CommonUtils.getUserSearchCategories());
        final String titleMyCategories = ApplicationProperties.getValue(CATEGORY_BOX_TEXT);
        box.addBox(titleMyCategories, status, MY_CATEGORIES_IMAGE, false);

        JPanel profilingResults = getPanel();
        setDataForPanel(profilingResults, CommonUtils.getUserSearchQueries());
        final String titleQuery = ApplicationProperties.getValue(QUERY_BOX_TEXT);
        box.addBox(titleQuery, profilingResults, MY_SEARCH_QUERIES_IMAGE, false);

        JPanel popularCategories = getPanel();
        setDataForPanel(popularCategories, CommonUtils.getPopularSearchCategories());
        final String titlePopularcategories = ApplicationProperties.getValue(POPULAR_CATEGORY_BOX_TEXT);
        box.addBox(titlePopularcategories, popularCategories, POPULAR_CATEGORIES_IMAGE, false);

        this.setBorder(null);
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
    /**
     * @return The panel with common paramters set
     */
    private JPanel getPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new RiverLayout(10, 5));
        panel.setPreferredSize(new Dimension(265, 123));
        panel.setOpaque(false);
        panel.setBorder(null);
        return panel;
    }
}
