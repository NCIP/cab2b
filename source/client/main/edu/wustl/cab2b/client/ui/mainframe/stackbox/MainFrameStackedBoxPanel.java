package edu.wustl.cab2b.client.ui.mainframe.stackbox;

import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.EXPERIMENT_BOX_TEXT;
import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.POPULAR_CATEGORY_BOX_TEXT;
import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.QUERY_BOX_TEXT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.MY_EXPERIMENT_IMAGE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.MY_SEARCH_QUERIES_IMAGE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.POPULAR_CATEGORIES_IMAGE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wustl.cab2b.client.ui.AddLimitPanel;
import edu.wustl.cab2b.client.ui.MainSearchPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.StackedBox;
import edu.wustl.cab2b.client.ui.experiment.ExperimentOpenPanel;
import edu.wustl.cab2b.client.ui.mainframe.GlobalNavigationPanel;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * @author Chandrakant Talele/Deepak_Shingan
 */
public class MainFrameStackedBoxPanel extends Cab2bPanel {
    private static final long serialVersionUID = 1L;

    private StackBoxMySearchQueriesPanel mySearchQueriesPanel;

    private JPanel popularSearchCategoryPanel;

    private MainFrame mainFrame;

    private JPanel myExperimentsPanel;

    public static Color CLICKED_COLOR = new Color(76, 41, 157);

    public static Color UNCLICKED_COLOR = new Color(0x034E74);

    /**
     * @param frame 
     * @param mainFrame
     */
    public MainFrameStackedBoxPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(new BorderLayout());

        StackedBox stackedBox = new StackedBox();
        stackedBox.setTitleBackgroundColor(new Color(200, 200, 220));
        JScrollPane scrollPane = new JScrollPane(stackedBox);
        scrollPane.setBorder(null);
        this.add(scrollPane, BorderLayout.CENTER);

        mySearchQueriesPanel = StackBoxMySearchQueriesPanel.getInstance();
        JScrollPane jScrollPane = new JScrollPane(mySearchQueriesPanel);
        jScrollPane.setPreferredSize(new Dimension(250, 150));
        jScrollPane.setBorder(null);
        jScrollPane.getViewport().setBackground(Color.WHITE);

        final String titleQuery = ApplicationProperties.getValue(QUERY_BOX_TEXT);
        stackedBox.addBox(titleQuery, jScrollPane, MY_SEARCH_QUERIES_IMAGE, false);

        popularSearchCategoryPanel = CommonUtils.getPopularSearchCategoriesPanel(
                                                                                 CommonUtils.getPopularSearchCategories(),
                                                                                 new CategoryHyperlinkActionListener());
        final String titlePopularcategories = ApplicationProperties.getValue(POPULAR_CATEGORY_BOX_TEXT);
        stackedBox.addBox(titlePopularcategories, popularSearchCategoryPanel, POPULAR_CATEGORIES_IMAGE, false);

        myExperimentsPanel = CommonUtils.getMyLatestExperimentsPanel(CommonUtils.getExperiments(null, ""),
                                                                     new ExperimentHyperlinkActionListener());

        final String titleExpr = ApplicationProperties.getValue(EXPERIMENT_BOX_TEXT);
        stackedBox.addBox(titleExpr, myExperimentsPanel, MY_EXPERIMENT_IMAGE, false);

        stackedBox.setPreferredSize(new Dimension(250, 500));
        stackedBox.setMinimumSize(new Dimension(250, 500));

        this.setMinimumSize(new Dimension(242, this.getMinimumSize().height)); //fix for bug#3745
    }

    /**
     * @param data
     */
    public void setDataForPopularSearchCategoriesPanel(Vector<Category> data) {
        popularSearchCategoryPanel.removeAll();
        popularSearchCategoryPanel.add(new Cab2bLabel());
        for (Category category : data) {
            String hyperlinkName = category.getCategoryEntity().getName();
            Cab2bHyperlink hyperlink = new Cab2bHyperlink(true);
            hyperlink.setClickedColor(CLICKED_COLOR);
            hyperlink.setUnclickedColor(UNCLICKED_COLOR);
            hyperlink.setUserObject(category);
            hyperlink.setText(hyperlinkName);
            hyperlink.setToolTipText(category.getCategoryEntity().getDescription());
            hyperlink.addActionListener(new CategoryHyperlinkActionListener());
            popularSearchCategoryPanel.add("br", hyperlink);
        }
        popularSearchCategoryPanel.revalidate();
    }

    /**
     * @param data
     */
    public void setDataForMyExperimentsPanel(Vector<Experiment> data) {
        myExperimentsPanel.removeAll();
        myExperimentsPanel.add(new Cab2bLabel());
        for (Experiment experiment : data) {
            String hyperlinkName = experiment.getName();
            Cab2bHyperlink hyperlink = new Cab2bHyperlink(true);
            hyperlink.setClickedColor(CLICKED_COLOR);
            hyperlink.setUnclickedColor(UNCLICKED_COLOR);
            hyperlink.setUserObject(experiment);
            hyperlink.setText(hyperlinkName);
            hyperlink.setToolTipText(experiment.getDescription());
            hyperlink.addActionListener(new ExperimentHyperlinkActionListener());
            myExperimentsPanel.add("br", hyperlink);
        }
        myExperimentsPanel.revalidate();
    }

    /**
     * Homepage Experiment Link action listener class
     * @author deepak_shingan
     *
     */
    class ExperimentHyperlinkActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Cab2bHyperlink hyperLink = (Cab2bHyperlink) e.getSource();
            Experiment exp = (Experiment) hyperLink.getUserObject();
            ExperimentOpenPanel expPanel = new ExperimentOpenPanel(exp);
            MainFrameStackedBoxPanel.this.mainFrame.openExperiment(expPanel);
            updateUI();
        }
    }

    /**
     * Homepage Category Link action listener class
     * @author deepak_shingan
     *
     */
    class CategoryHyperlinkActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Cab2bHyperlink hyperLink = (Cab2bHyperlink) e.getSource();
            MainSearchPanel mainSearchPanel = null;
            if (GlobalNavigationPanel.getMainSearchPanel() == null)
                mainSearchPanel = new MainSearchPanel();
            GlobalNavigationPanel.setMainSearchPanel(mainSearchPanel);
            mainSearchPanel.getSearchNavigationPanel().setAddLimitPanelInWizard();
            AddLimitPanel addLimitPanel = mainSearchPanel.getCenterPanel().getAddLimitPanel();
            addLimitPanel.getSearchResultPanel().updateAddLimitPage(
                                                                    addLimitPanel,
                                                                    ((Category) hyperLink.getUserObject()).getCategoryEntity());

            CommonUtils.launchSearchDataWizard();
            updateUI();
        }
    }

}