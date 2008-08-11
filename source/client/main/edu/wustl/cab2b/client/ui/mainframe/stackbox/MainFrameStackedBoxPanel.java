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

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.StackedBox;
import edu.wustl.cab2b.client.ui.experiment.ExperimentOpenPanel;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllCategoryPanel;
import edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * @author Chandrakant Talele/Deepak_Shingan
 */
public class MainFrameStackedBoxPanel extends Cab2bPanel {

    private static final long serialVersionUID = 1L;

    private SavedQueryLinkPanel mySearchQueriesPanel;

    private JPanel popularSearchCategoryPanel;

    private MainFrame mainFrame;

    private JPanel myExperimentsPanel;

    public static Color CLICKED_COLOR = new Color(76, 41, 157);

    public static Color UNCLICKED_COLOR = new Color(0x034E74);

    public static final String SHOW_ALL_LINK = "Show All";

    /**
     * Constructor
     * @param frame
     * @param mainFrame
     */
    public MainFrameStackedBoxPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    /**
     * GUI initialising panel
     */
    private void initUI() {
        this.setLayout(new BorderLayout());
        StackedBox stackedBox = new StackedBox();
        stackedBox.setTitleBackgroundColor(new Color(200, 200, 220));
        JScrollPane scrollPane = new JScrollPane(stackedBox);
        scrollPane.setBorder(null);
        this.add(scrollPane, BorderLayout.CENTER);

        mySearchQueriesPanel = SavedQueryLinkPanel.getInstance();

        final String titleQuery = ApplicationProperties.getValue(QUERY_BOX_TEXT);
        stackedBox.addBox(titleQuery, mySearchQueriesPanel, MY_SEARCH_QUERIES_IMAGE, false);

        popularSearchCategoryPanel = getPopularSearchCategoriesPanel(CommonUtils.getPopularSearchCategories(),
                                                                     new CategoryHyperlinkActionListener());
        final String titlePopularcategories = ApplicationProperties.getValue(POPULAR_CATEGORY_BOX_TEXT);
        stackedBox.addBox(titlePopularcategories, popularSearchCategoryPanel, POPULAR_CATEGORIES_IMAGE, false);

        myExperimentsPanel = getLatestExperimentsPanel(CommonUtils.getExperiments(null, ""),
                                                       new ExperimentHyperlinkActionListener());

        final String titleExpr = ApplicationProperties.getValue(EXPERIMENT_BOX_TEXT);
        stackedBox.addBox(titleExpr, myExperimentsPanel, MY_EXPERIMENT_IMAGE, false);

        stackedBox.setPreferredSize(new Dimension(250, 500));
        stackedBox.setMinimumSize(new Dimension(250, 500));
        this.setMinimumSize(new Dimension(242, this.getMinimumSize().height)); // for bug#3745
    }

    /**
     * This method returns panel with five most popular categories from database.
     * TODO: Currently getting all categories from database
     * @param data
     */
    public Cab2bPanel getPopularSearchCategoriesPanel(Vector<Category> data, ActionListener actionClass) {
        Cab2bPanel panel = new Cab2bPanel();
        panel.setLayout(new RiverLayout(10, 5));
        panel.add(new Cab2bLabel());
        for (Category category : data) {
            Cab2bHyperlink hyperlink = new Cab2bHyperlink(true);
            CommonUtils.setHyperlinkProperties(hyperlink, category, category.getCategoryEntity().getName(),
                                               category.getCategoryEntity().getDescription(), actionClass);
            panel.add("br", hyperlink);
        }

        ActionListener showAllExpAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.getGlobalNavigationPanel().getGlobalNavigationGlassPane().setShowAllPanel(
                                                                                                    getAllCategoryPanel());
            }
        };
        addShowAllLink(panel, showAllExpAction);
        return panel;
    }

    /**
     * This method returns panel with all popular categories from database 
     * @return
     */
    private ShowAllPanel getAllCategoryPanel() {
        Vector<Category> allPopularCategories = CommonUtils.getPopularSearchCategories();
        final Object objData[][] = new Object[allPopularCategories.size()][6];
        final String headers[] = { ShowAllCategoryPanel.CATEGORY_NAME_TITLE, ShowAllCategoryPanel.CATEGORY_DESCRIPTION_TITLE, ShowAllCategoryPanel.CATEGORY_POPULARITY_TITLE, ShowAllCategoryPanel.CATEGORY_DATE_TITLE, " Category ID-Hidden" };
        int i = 0;
        for (Category category : allPopularCategories) {
            objData[i][0] = category.getCategoryEntity().getName();
            objData[i][1] = category.getCategoryEntity().getDescription();
            objData[i][2] = 0;
            objData[i][3] = category.getCategoryEntity().getLastUpdated();
            objData[i][4] = category;
            i++;
        }
        return new ShowAllCategoryPanel(headers, objData);
    }

    /**
     * Method to add show all link to panel
     * @param panel
     * @param actionClass
     */
    private void addShowAllLink(Cab2bPanel panel, ActionListener actionClass) {
        Cab2bHyperlink hyperlink = new Cab2bHyperlink(true);
        CommonUtils.setHyperlinkProperties(hyperlink, null, SHOW_ALL_LINK, "", actionClass);
        panel.add("br right", hyperlink);
    }

    /**
     * This method returns panel with recently saved five experiments from database.
     * TODO: Currently getting all experiments from database also have to update code for 
     * userbase fetaching of experiments 
     * @param data
     */
    private Cab2bPanel getLatestExperimentsPanel(Vector<Experiment> data, ActionListener actionListener) {
        Cab2bPanel panel = new Cab2bPanel();
        panel.setLayout(new RiverLayout(10, 5));
        panel.add(new Cab2bLabel());
        Cab2bHyperlink hyperlink = null;
        for (Experiment experiment : data) {
            hyperlink = new Cab2bHyperlink(true);
            CommonUtils.setHyperlinkProperties(hyperlink, experiment, experiment.getName(),
                                               experiment.getDescription(), actionListener);
            panel.add("br", hyperlink);
        }

        ActionListener showAllExpAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.getGlobalNavigationPanel().getGlobalNavigationGlassPane().setExperimentHomePanel();
            }
        };

        addShowAllLink(panel, showAllExpAction);
        return panel;
    }

    /**
     * Homepage Experiment Link action listener class
     * 
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
     * 
     * @author deepak_shingan
     * 
     */
    class CategoryHyperlinkActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ShowAllCategoryPanel.categoryLinkAction((Category) ((Cab2bHyperlink) e.getSource()).getUserObject());
        }
    }
}