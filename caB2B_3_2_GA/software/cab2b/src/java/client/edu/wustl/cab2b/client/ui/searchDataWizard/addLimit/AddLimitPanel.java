package edu.wustl.cab2b.client.ui.searchDataWizard.addLimit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.main.AbstractTypePanel;
import edu.wustl.cab2b.client.ui.query.ClientPathFinder;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.cab2b.client.ui.searchDataWizard.ContentPanel;
import edu.wustl.cab2b.client.ui.searchDataWizard.SearchNavigationPanel;
import edu.wustl.cab2b.client.ui.searchDataWizard.SearchPanel;
import edu.wustl.cab2b.client.ui.searchDataWizard.SearchResultPanel;
import edu.wustl.cab2b.client.ui.searchDataWizard.dag.DagControlPanel;
import edu.wustl.cab2b.client.ui.searchDataWizard.dag.MainDagPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CommonUtils.DagImages;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

/**
 * This is the searchPanel for the Add limit tab from the main search dialog. The
 * class is also an instance of the {@link ContentPanel},so that child
 * component can cause this searchPanel to refresh in a way required by this searchPanel.
 * 
 * @author mahesh_iyer 
 * @author Deepak_Shingan 
 */
public class AddLimitPanel extends ContentPanel implements IUpdateAddLimitUIInterface {
    private static final long serialVersionUID = 1L;

    /** The titled searchPanel for the top searchPanel. */
    private JXTitledPanel m_topCenterPanel = null;

    /**
     * The dynamically generated searchPanel for the selected class from an
     * advanced/category search.
     */
    private JXPanel m_ContentForTopPanel = null;

    /** The advanced search searchPanel along with the results searchPanel. */
    private SearchPanel searchPanel = null;

    /** The titled searchPanel for the bottom searchPanel. */
    private JXTitledPanel m_bottomCenterPanel = null;

    /** The simple view for the rules added. This is to be replaced by the DAG. */
    private MainDagPanel mainDagPanel = null;

    /** Split pane between the top and center titled panels. */
    public static JSplitPane m_innerPane = null;

    /** Split pane between the LHS and RHS sections of the main searchPanel. */
    private JSplitPane m_outerPane = null;

    private static final String ADD_LIMIT_TITLE = "Define Limit";

    /**
     * Default constructor
     */
    public AddLimitPanel() {
        initGUI();
    }

    /**
     * Method initializes the searchPanel by appropriately laying out child
     * components.
     */
    private void initGUI() {
        this.setLayout(new BorderLayout());
        /*
         * Pass the reference , so that the child can cause the parent to
         * refresh for any events triggered in the child.
         */

        /* The top center titled searchPanel */
        m_topCenterPanel = new Cab2bTitledPanel(ADD_LIMIT_TITLE);
        m_topCenterPanel.setTitleForeground(Color.BLACK);
        /* Set a gradient painter for the title searchPanel */
        GradientPaint gp = new GradientPaint(new Point2D.Double(.3d, 0), new Color(185, 211, 238),
                new Point2D.Double(.7, 0), Color.WHITE);
        m_topCenterPanel.setTitlePainter(new BasicGradientPainter(gp));

        /*
         * Set the preferred size for the top searchPanel, as against the preferred
         * size for the contentPanel/child searchPanel itself. Doing the later has the
         * undesired result of content searchPanel getting clipped even if the actual
         * length is more than the set preferred size, and the vertical scroll
         * bars never come into existence. Again, size set based on usability
         * specs.
         */
        m_topCenterPanel.setPreferredSize(new Dimension(546, 341));
        m_topCenterPanel.setBorder(new EmptyBorder(1, 1, 1, 1));

        /*
         * JXTitledPanels work better with only searchPanel as child, and hence the
         * following searchPanel.
         */
        m_ContentForTopPanel = new Cab2bPanel(new BorderLayout());
        m_ContentForTopPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
        m_topCenterPanel.add(this.m_ContentForTopPanel);

        /* The bottom center titled searchPanel.Initialization on the same lines. */
        m_bottomCenterPanel = new Cab2bTitledPanel("Limit Set");
        m_bottomCenterPanel.setTitleForeground(Color.BLACK);
        m_bottomCenterPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
        m_bottomCenterPanel.setTitlePainter(new BasicGradientPainter(gp));

        // Generate ImageMap
        Map<DagImages, Image> imageMap = CommonUtils.getDagImageMap();

        IPathFinder pathFinder = new ClientPathFinder();
        mainDagPanel = new MainDagPanel(this, imageMap, pathFinder, false);
        mainDagPanel.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(DagControlPanel.EVENT_RESET_BUTTON_CLICKED)) {
                    resetPanel();
                    firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
                }
            }
        });
        m_bottomCenterPanel.add(mainDagPanel);

        /* Add components to the conetent pane. */
        m_innerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, m_topCenterPanel, m_bottomCenterPanel);
        m_innerPane.setOneTouchExpandable(false);
        m_innerPane.setBorder(null);
        m_innerPane.setDividerSize(4);

        addSearchPanel(searchPanel);
    }

    /**
     * Method to Add search panel in AddLimitPanel 
     * @param srcPanel
     */
    public void addSearchPanel(SearchPanel srcPanel) {
        //removing the outerpane if available
        if (m_outerPane != null) {
            this.remove(m_outerPane);
        }

        if (srcPanel == null) {
            searchPanel = new SearchPanel(this);
        } else {
            setSearchPanel(srcPanel);
        }

        searchPanel.setMinimumSize(new Dimension(270, 300));
        searchPanel.setUIForAddLimitSearchPanel();
        m_outerPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, searchPanel, m_innerPane);
        m_outerPane.setDividerLocation(0.2D);
        m_outerPane.setOneTouchExpandable(false);
        m_outerPane.setBorder(null);
        m_outerPane.setDividerSize(4);
        m_outerPane.setDividerLocation(275);

        add(BorderLayout.CENTER, this.m_outerPane);
    }

    /**
     * Method to add search result searchPanel
     * @param resultPanel
     */
    public void addResultsPanel(SearchResultPanel resultPanel) {
        searchPanel.addResultsPanel(resultPanel);
    }

    /**
     * This method takes the newly added expression and renders the node accordingly
     * @param expressionId
     */
    public void refreshBottomCenterPanel(int expressionId) {
        // Here code to handle adding new limit will appear
        try {
            mainDagPanel.updateGraph(expressionId);
        } catch (MultipleRootsException e) {
            CommonUtils.handleException(e, this, true, true, false, false);
        }
        this.updateUI();
    }

    /**
     * The method is a custom implementation for the refresh method from the
     * {@link ContentPanel} interface. Custom implementation is to simply set
     * the provided searchPanel as the content searchPanel for the top titled searchPanel.
     * 
     * @param panelToBeRefreshed
     *            The searchPanel to be refreshed.
     * 
     * @param strClassNameAsTitle
     *            The class/category name for which the dynamic UI is generated.
     */
    public void refresh(JXPanel[] arrPanel, String strClassNameAsTitle) {
        /* Set the title for the top titled searchPanel. */
        m_topCenterPanel.setTitle(ADD_LIMIT_TITLE + " on '" + strClassNameAsTitle + "'");
        m_ContentForTopPanel.removeAll();
        m_ContentForTopPanel.add(arrPanel[0], BorderLayout.NORTH);
        m_ContentForTopPanel.add(arrPanel[1], BorderLayout.CENTER);
        validate();
    }

    /**
     * The method returns a reference to the bottom content searchPanel. This is
     * invoked by the main searchPanel in order to form the query.
     * 
     * @return JXPanel The bottom content searchPanel.
     */
    public JXPanel getBottomCenterPanel() {
        return this.mainDagPanel;
    }

    /*
     * Sets queryObject
     * (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.ContentPanel#setQueryObject(edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface)
     */
    public void setQueryObject(IClientQueryBuilderInterface query) {
        mainDagPanel.setQueryObject(query);
    }

    /*
     * Edits/refresh AddLimit User Interface according to node changes 
     * (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.IUpdateAddLimitUIInterface#editAddLimitUI(edu.wustl.common.querysuite.queryobject.IExpression)
     */
    public void editAddLimitUI(IExpression expression) {
        if (expression != null) {
            IQueryEntity entity = expression.getQueryEntity();
            JXPanel[] panels = getSearchResultPanel().createEditLimitPanels(expression);
            Component[] components = ((Cab2bPanel) ((JScrollPane) panels[1].getComponent(0)).getViewport().getComponent(
                                                                                                                        0)).getComponents();
            // passing appropriate class name
            refresh(panels, Utility.getDisplayName(entity.getDynamicExtensionsEntity()));
            IRule rule = (IRule) expression.getOperand(0);
            // Populate panels with corresponding value
            for (int i = 0; i < rule.size(); i++) {
                ICondition condition = rule.getCondition(i);
                setValueForAttribute(components, condition);
            }
            validate();
        }
    }

    /**
     * Sets value for attributes/conditions on Add Limit Top Panel 
     * @param components
     * @param condition
     */
    private void setValueForAttribute(Component[] components, ICondition condition) {
        for (int i = 0; i < components.length; i++) {
            AbstractTypePanel panel = (AbstractTypePanel) components[i];
            AttributeInterface attribute = panel.getAttributeEntity();
            if (attribute == condition.getAttribute()) {
                RelationalOperator operator = condition.getRelationalOperator();
                panel.setCondition(edu.wustl.cab2b.client.ui.query.Utility.displayStringForRelationalOperator(operator));
                List<String> values = condition.getValues();
                panel.setValues(new ArrayList<String>(values));
                break;
            }
        }
    }

    /*
     * Sets search result panel 
     * (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.ContentPanel#setSearchResultPanel(edu.wustl.cab2b.client.ui.SearchResultPanel)
     */
    public void setSearchResultPanel(SearchResultPanel searchResultPanel) {
        searchPanel.setSerachResultPanel(searchResultPanel);
    }

    /**
     * Gets search result panel
     * @return
     */
    public SearchResultPanel getSearchResultPanel() {
        return searchPanel.getSearchResultPanel();
    }

    /*
     * Method to clear (refresh) AddLimitUI when Node is in edit mode
     * (non-Javadoc)
     * 
     * @see edu.wustl.cab2b.client.ui.IUpdateAddLimitUIInterface#clearAddLimitUI(edu.wustl.common.querysuite.queryobject.IExpression)
     */
    public void clearAddLimitUI() {
        if (m_ContentForTopPanel.getComponentCount() > 0) {
            Cab2bPanel panel = (Cab2bPanel) m_ContentForTopPanel.getComponent(1);
            Cab2bPanel cab2bPanel = (Cab2bPanel) ((JViewport) ((JScrollPane) panel.getComponent(0)).getComponent(0)).getComponent(0);

            int i = 0;
            Component[] attributePanels = cab2bPanel.getComponents();
            final JXPanel[] panelArray = new JXPanel[attributePanels.length];
            for (Component component : attributePanels) {
                if (component instanceof AbstractTypePanel) {
                    AbstractTypePanel abstractTypePanel = (AbstractTypePanel) component;
                    abstractTypePanel.resetPanel();
                    panelArray[i++] = abstractTypePanel;
                }
            }

            Cab2bPanel constraintButtonPanel = (Cab2bPanel) ((Cab2bPanel) m_ContentForTopPanel.getComponent(0)).getComponent(0);
            constraintButtonPanel.remove(0);
            SearchResultPanel searchResultPanel = getSearchResultPanel();
            searchResultPanel.initializeAddLimitButton(panelArray, searchResultPanel.getEntityForSelectedLink());
            constraintButtonPanel.add(searchResultPanel.getAddLimitButton(), 0);

            revalidate();
            updateUI();
        }
    }

    /**
     * Refresh/clear Add Limit Panel UI
     */
    public void resetPanel() {
        SearchNavigationPanel.getMessageLabel().setText("");
        clearAddLimitUI();
        mainDagPanel.clearDagPanel();
        revalidate();
        updateUI();
    }

    /*
     * Gets search panel
     *  (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.ContentPanel#getSearchPanel()
     */
    public SearchPanel getSearchPanel() {
        return searchPanel;
    }

    /**
     * Sets search panel
     * @param searchText
     */
    public void setSearchText(String searchText) {
        searchPanel.setSearchtext(searchText);
    }

    /**
     * Set Search text
     * @return
     */
    public String getSearchText() {
        return searchPanel.getSearchtext();
    }

    /* 
     * Set search Panel
     * (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.ContentPanel#setSearchPanel(edu.wustl.cab2b.client.ui.SearchPanel)         
     */
    public void setSearchPanel(SearchPanel panel) {
        searchPanel = panel;
    }

    /**
     * @return the mainDagPanel
     */
    public MainDagPanel getMainDagPanel() {
        return mainDagPanel;
    }

    /**
     * @param mainDagPanel the mainDagPanel to set
     */
    public void setMainDagPanel(MainDagPanel mainDagPanel) {
        this.mainDagPanel = mainDagPanel;
    }
}
