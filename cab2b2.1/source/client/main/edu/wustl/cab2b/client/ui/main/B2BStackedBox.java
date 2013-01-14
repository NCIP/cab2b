/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.main;

import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.CATEGORY_BOX_TEXT;
import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.POPULAR_CATEGORY_BOX_TEXT;
import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.QUERY_BOX_TEXT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.MY_CATEGORIES_IMAGE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.MY_SEARCH_QUERIES_IMAGE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.POPULAR_CATEGORIES_IMAGE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.StackedBox;
import edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllSavedQueryPanel;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * This will be shown in the first step of search data for experiment wizard
 * 
 * @author Chandrakant Talele
 */
public class B2BStackedBox extends Cab2bPanel {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor. Initializes the object with its UI parameters
     */
    public B2BStackedBox() {
        initGUI();
    }

    /**
     * GUI initialisation method
     */
    private void initGUI() {

        this.setLayout(new BorderLayout());
        this.setBorder(null);
        StackedBox box = new StackedBox();
        box.setTitleBackgroundColor(new Color(224, 224, 224));
        box.setBorder(null);
        JScrollPane scrollPane = new JScrollPane(box);
        // scrollPane.setBorder(new CustomizableBorder(new Insets(1,1,1,1),
        // true, true));

        this.add(scrollPane, BorderLayout.CENTER);

        Cab2bLabel myCategoryLabel = new Cab2bLabel("This will be available in next release");
        Cab2bPanel myCategories = new Cab2bPanel();
        myCategories.add(myCategoryLabel);
        final String titleMyCategories = ApplicationProperties.getValue(CATEGORY_BOX_TEXT);
        box.addBox(titleMyCategories, myCategories, MY_CATEGORIES_IMAGE, false);
        
//        Decision pending on implementation of these boxes    
        
/*        Cab2bLabel savedQueryLabel = new Cab2bLabel("This will be available in next release");
        Cab2bPanel savedQueries = new Cab2bPanel();
        savedQueries.add(savedQueryLabel);
        final String titleQuery = ApplicationProperties.getValue(QUERY_BOX_TEXT);
        box.addBox(titleQuery, savedQueries, MY_SEARCH_QUERIES_IMAGE, false);

        Cab2bLabel popularCategoryLabel = new Cab2bLabel("This will be available in next release");
        Cab2bPanel popularCategories = new Cab2bPanel();
        popularCategories.add(popularCategoryLabel);
        final String titlePopularcategories = ApplicationProperties.getValue(POPULAR_CATEGORY_BOX_TEXT);
        box.addBox(titlePopularcategories, popularCategories, POPULAR_CATEGORIES_IMAGE, false); */

        this.setBorder(null);

    }

    /**
     * Action class for showing a tempory message on hyperlink click.
     * 
     * @author deepak_shingan
     * 
     */
    class ShowMessageActionClass implements ActionListener {
        String msg = "";

        ShowMessageActionClass(String msg) {
            this.msg = msg;
        }

        public void actionPerformed(ActionEvent e) {
            Container comp = (Container) e.getSource();
            while (comp.getParent() != null) {
                comp = comp.getParent();
            }
            JOptionPane.showMessageDialog(comp, msg, "caB2B Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Common method for setting data in panel
     * 
     * @param panel
     * @param data
     */
    private void setDataForPanel(JPanel panel, Collection<IParameterizedQuery> data) {

        panel.removeAll();
        panel.add(new Cab2bLabel());
        Iterator<IParameterizedQuery> iter = data.iterator();
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Cab2bHyperlink queryLink = (Cab2bHyperlink) ae.getSource();
                Long queryID = (Long) queryLink.getUserObject();
                ShowAllSavedQueryPanel.queryLinkAction(queryID);
            }
        };

        while (iter.hasNext()) {
            IParameterizedQuery query = iter.next();
            String hyperlinkName = query.getName();
            Cab2bHyperlink<Long> hyperlink = new Cab2bHyperlink<Long>(true);
            hyperlink.setUserObject(query.getId());
            hyperlink.setText(hyperlinkName);
            if (query.getDescription() == null || query.getDescription().equals("")) {
                hyperlink.setToolTipText("* Description not available");
            } else {
                hyperlink.setToolTipText(query.getDescription());
            }

            hyperlink.addActionListener(actionListener);
            panel.add("br", hyperlink);
        }
        panel.updateUI();
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
