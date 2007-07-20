package edu.wustl.cab2b.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXPanel;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bComboBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.pagination.JPagination;
import edu.wustl.cab2b.client.ui.pagination.NumericPager;
import edu.wustl.cab2b.client.ui.pagination.PageElement;
import edu.wustl.cab2b.client.ui.pagination.PageElementImpl;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeHandler;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mahesh_iyer
 * 
 * This is the searchPanel for defining results view searchPanel.
 */

public class AdvancedDefineViewPanel extends Cab2bPanel {
    private static final long serialVersionUID = 1L;

    /** Reference to the parent container. */
    private SearchCenterPanel m_searchCenterPanel = null;

    /** Pagination component */
    private JPagination m_resultsPage = null;

    /** The functional class corresponding to root. */
    private EntityInterface m_rootEntity = null;

    public static boolean isMultipleGraphException = false;

    private Set<EntityInterface> allEntities = new HashSet<EntityInterface>();

    public AdvancedDefineViewPanel(SearchCenterPanel searchCenterPanel) {
        this.m_searchCenterPanel = searchCenterPanel;
        isMultipleGraphException = false;
        initGUI();
    }

    private void initGUI() {
        this.setLayout(new BorderLayout());

        // Add the hyperlink to show all classes.
        Cab2bHyperlink showAllLink = new Cab2bHyperlink();
        showAllLink.setText("Show All Search Results Views");
        showAllLink.setEnabled(false);

        JXPanel topPanel = new Cab2bPanel(new RiverLayout(5,5));
        topPanel.add(showAllLink);
        this.add(BorderLayout.NORTH, topPanel);

        Vector<PageElement> pageElementCollection = new Vector<PageElement>();
        // Get the classes in the where part from the query.
        MainSearchPanel mainSearchPanel = (MainSearchPanel) (this.m_searchCenterPanel.getParent());

        /* Get the wrapper. */
        final IClientQueryBuilderInterface queryObject = mainSearchPanel.getQueryObject();

        /* Get the collection of EntityInterface. */
        Collection<EntityInterface> entityCollection = queryObject.getEntities();
        for (EntityInterface entityInterface : entityCollection) {
            // Form the PageElements out of each Entity and add it to the pageElement Collection
            PageElement pageElement = new PageElementImpl();
            pageElement.setDisplayName(Utility.getDisplayName(entityInterface));
            pageElement.setDescription(entityInterface.getDescription());

            pageElementCollection.add(pageElement);
        }
        NumericPager numericPager = new NumericPager(pageElementCollection);

        // Initalize the pagination component
        m_resultsPage = new JPagination(pageElementCollection, numericPager, this, true, false);
        m_resultsPage.resetAllLabels();
        m_resultsPage.setPageLinksDisabled();

        // Create a Panel for the center searchPanel, and add the pagination component to that.
        JXPanel centerPanel = new Cab2bPanel(new RiverLayout(5,5));
        centerPanel.add(new Cab2bLabel());
       /* centerPanel.add("br", new Cab2bLabel());
        centerPanel.add("br", new Cab2bLabel());
        centerPanel.add("br", new Cab2bLabel());
        centerPanel.add("br", new Cab2bLabel());*/
        centerPanel.add("br hfill vfill", this.m_resultsPage);
        this.add(BorderLayout.CENTER, centerPanel);

        // The bottom searchPanel
        JXPanel bottomPanel = new Cab2bPanel(new RiverLayout(0, 0));
        JLabel asterix = new Cab2bLabel("* ");
        asterix.setFont(new Font("Arial", Font.BOLD, 16));
        asterix.setForeground(Color.RED);
        bottomPanel.add(asterix);
        bottomPanel.add(new Cab2bLabel("Select Default View   "));

        /*
         * Get the class corresponding to the root expression and show that in
         * the drop down. For this the IClass should be enough as we are
         * interested only in the name.
         */
        Logger.out.debug("queryObject.getQuery().getClass() " + queryObject.getQuery().getClass());
        final IQuery b2bquery = queryObject.getQuery();

        /* Get the root expression ID. */
        try {
            b2bquery.getConstraints().getRootExpressionId();
        } catch (MultipleRootsException e) {
            JOptionPane.showMessageDialog(null, ErrorCodeHandler.getErrorMessage(ErrorCodeConstants.QM_0003),
                                          "No result found.", JOptionPane.WARNING_MESSAGE);
            isMultipleGraphException = true;
            return;
        }

        Vector<String> comboModel = getAllEntityNamesInWhereClause(queryObject);
        Cab2bComboBox combo = new Cab2bComboBox(comboModel);
        // Updated since the Cab2bComboBox default preferredSize is to small.
        combo.setPreferredSize(new Dimension(new Dimension(250, 20)));
        combo.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                    String userSelectionOfEntity = itemEvent.getItem().toString();
                    setOutputForQuery(queryObject, userSelectionOfEntity);
                }
            }
        });

        // If user doesn't bother to change the output entity he wants, set the default selected entity in the combox as the output entity.
        String defaultSelectionOfEntity = (String) combo.getSelectedItem();
        setOutputForQuery(queryObject, defaultSelectionOfEntity);

        bottomPanel.add(combo);
        this.add(BorderLayout.SOUTH, bottomPanel);
    }

    private void setOutputForQuery(final IClientQueryBuilderInterface queryObject, String selectionOfEntity) {
        EntityInterface entity = findTargetEntityInterface(selectionOfEntity);
        try {
            queryObject.setOutputForQuery(entity);
        } catch (RemoteException e) {
            CommonUtils.handleException(e, m_searchCenterPanel, true, true, true, false);
        }
    }

    private EntityInterface findTargetEntityInterface(String targetEntityName) {
        EntityInterface targetEntityInterface = null;
        for (EntityInterface entity : allEntities) {
            if (Utility.getDisplayName(entity).equals(targetEntityName)) {
                targetEntityInterface = entity;
                break;
            }
        }

        return targetEntityInterface;
    }

    private Vector<String> getAllEntityNamesInWhereClause(IClientQueryBuilderInterface queryBuilder) {
        Set<IExpressionId> expressionIdEnum = queryBuilder.getVisibleExressionIds();
        IQuery query = queryBuilder.getQuery();

        Set<String> returnerSet = new HashSet<String>();
        for (IExpressionId expressionId : expressionIdEnum) {
            IExpression expression = query.getConstraints().getExpression(expressionId);
            EntityInterface entity = expression.getConstraintEntity().getDynamicExtensionsEntity();
            allEntities.add(entity);

            String entityName = Utility.getDisplayName(entity);
            returnerSet.add(entityName);
        }

        IExpressionId rootExprId = null;
        try {
            rootExprId = query.getConstraints().getRootExpressionId();
        } catch (MultipleRootsException e) {
            // can't occur
        }

        IExpression rootExpr = query.getConstraints().getExpression(rootExprId);
        String rootEntityName = Utility.getDisplayName(rootExpr.getConstraintEntity().getDynamicExtensionsEntity());
        Vector<String> returner = new Vector<String>();
        returner.add(rootEntityName);
        returnerSet.remove(rootEntityName);
        returner.addAll(returnerSet);

        return returner;
    }

    public EntityInterface getRootEntity() {
        return this.m_rootEntity;
    }
}
