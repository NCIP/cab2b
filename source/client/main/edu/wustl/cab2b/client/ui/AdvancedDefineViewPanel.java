package edu.wustl.cab2b.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXPanel;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
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
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.category.CategoryBusinessInterface;
import edu.wustl.cab2b.common.ejb.category.CategoryHomeInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mahesh_iyer
 * 
 * This is the panel for defining results view panel.
 */

public class AdvancedDefineViewPanel extends Cab2bPanel {

	/** Reference to the parent container. */
	private SearchCenterPanel m_searchCenterPanel = null;

	/** Pagination component */
	private JPagination m_resultsPage = null;

	/** The functional class corresponding to root. */
	private EntityInterface m_rootEntity = null;

	public static boolean isMultipleGraphException = false;

	public AdvancedDefineViewPanel(SearchCenterPanel searchCenterPanel) {
		this.m_searchCenterPanel = searchCenterPanel;
		isMultipleGraphException = false;
		initGUI();
	}

	private void initGUI() {

		this.setLayout(new BorderLayout());

		/**
		 * Add the hyperlink to show all classes.
		 */

		Cab2bHyperlink showAllLink = new Cab2bHyperlink();
		showAllLink.setText("Show All Search Results Views");
		JXPanel topPanel = new Cab2bPanel(new RiverLayout());
		topPanel.add(new Cab2bLabel(""));
		topPanel.add("tab ",showAllLink);
		/* Add the top panel to the North of the dialog. */
		this.add(BorderLayout.NORTH, topPanel);

		Vector<PageElement> pageElementCollection = new Vector<PageElement>();
		/*
		 * Get the classes in the where part from the query.
		 */
		MainSearchPanel mainSearchPanel = (MainSearchPanel) (this.m_searchCenterPanel
				.getParent());

		/* Get the wrapper. */
		final IClientQueryBuilderInterface queryObject = mainSearchPanel
				.getQueryObject();

		/* Get the collection of EntityInterface. */
		Collection entityCollection = queryObject.getEntities();

		Iterator iterator = entityCollection.iterator();
		while (iterator.hasNext()) {

			/*
			 * Iterate over the collection and get the individual entity
			 * instamces.
			 */
			EntityInterface entityInterface = (EntityInterface) iterator.next();
			/*
			 * Get the name and description and create the IPageElements needed
			 * for pagination
			 */
			PageElement pageElement = new PageElementImpl();
			// set the proper display name
			pageElement.setDisplayName(edu.wustl.cab2b.common.util.Utility
					.getDisplayName(entityInterface));
			// pageElement.setDisplayName(entityInterface.getName());

			pageElement.setDescription(entityInterface.getDescription());

			/* Add the instance to the collection of PageElement collection. */
			pageElementCollection.add(pageElement);

		}

		NumericPager numPager = new NumericPager(pageElementCollection);
		/*
		 * Initalize the pagination component.
		 */
		this.m_resultsPage = new JPagination(pageElementCollection, numPager,
				this, true);
		/*
		 * Create a Panel for the center panel, and add the pagination component
		 * to that.
		 */
		JXPanel centerPanel = new Cab2bPanel(new RiverLayout());
		centerPanel.add(new Cab2bLabel());
		centerPanel.add("br", new Cab2bLabel());
		centerPanel.add("br", new Cab2bLabel());
		centerPanel.add("br", new Cab2bLabel());
		// centerPanel.add("br",new Cab2bLabel("Search views available. Please
		// not links are dummies, so please don't try to be dumb ass and try
		// seleting them."));
		centerPanel.add("br", new Cab2bLabel());
		centerPanel.add("br", this.m_resultsPage);
		/* Add the center panel to the main panel */
		this.add(BorderLayout.CENTER, centerPanel);

		/* The bottom panel */
		JXPanel bottomPanel = new Cab2bPanel(new RiverLayout());
		JLabel asterix = new Cab2bLabel("*");
		asterix.setForeground(Color.RED);
		bottomPanel.add(asterix);
		bottomPanel.add(new Cab2bLabel("Select Default View"));

		/*
		 * Get the class corresponding to the root expression and show that in
		 * the drop down. For this the IClass should be enough as we are
		 * interested only in the name.
		 */
		Logger.out.debug("queryObject.getQuery().getClass() "
				+ queryObject.getQuery().getClass());
		// final ICab2bQuery b2bquery = (ICab2bQuery)queryObject.getQuery();

		final IQuery b2bquery = queryObject.getQuery();

		// IQuery query = queryObject.getQuery();

		/* Get the root expression ID. */
		IExpressionId rootExpressionID = null;
		try {
			rootExpressionID = b2bquery.getConstraints().getRootExpressionId();

		} catch (MultipleRootsException e) {
			CheckedException checkedException = new CheckedException(e
					.getMessage(), e, ErrorCodeConstants.QM_0003);
			CommonUtils.handleException(checkedException, this, true, true,
					true, false);
			isMultipleGraphException = true;
		}

		// ------- To be deleted -------
		/* From the root expression get the actual root expression. */
		// IExpression rootExpresssion =
		// b2bquery.getConstraints().getExpression(rootExpressionID);
		/* Get the actual class name and expression. */
		// this.m_rootEntity =
		// rootExpresssion.getConstraintEntity().getDynamicExtensionsEntity();
		/* Get the class name. */
		// String strRootClassName =
		// this.m_rootEntity.getAttribute().get(0).getUMLClass().getFullyQualifiedName();
		// String strRootClassName = m_rootEntity.getName();
		// Vector comboModel = new Vector();
		// comboModel.add(strRootClassName);
		// ------------------------------
		Vector<String> comboModel = getAllEntityNamesInWhereClause(queryObject);
		Cab2bComboBox combo = new Cab2bComboBox(comboModel);
		// Updated since the Cab2bComboBox default preferredSize is to small.
		combo.setPreferredSize(new Dimension(new Dimension(250, 20)));
		combo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getStateChange() == ItemEvent.SELECTED) {
					Logger.out.debug("item changed : selected object :"
							+ ie.getItem());
					/*
					 * Iterate over the where clause entities to pick the
					 * selected entity's entity interface
					 */
					String userSelectionOfEntity = ie.getItem().toString();
					EntityInterface entInt = findTargetEntityInterface(userSelectionOfEntity);
                    try{
                        queryObject.setOutputForQuery(entInt);
                    } catch (RemoteException e) {
                        CommonUtils.handleException(e, m_searchCenterPanel, true, true, true, false);
                    }

				}
			}
		});

		/*
		 * If user doesn't bother to change the output entity he wants, set the
		 * default selected entity in the combox as the output entity.
		 */
		String defaultSelectionOfEntity = (String) combo.getSelectedItem();
		EntityInterface entInt = findTargetEntityInterface(defaultSelectionOfEntity);
        try{
            queryObject.setOutputForQuery(entInt);
        } catch (RemoteException e) {
            CommonUtils.handleException(e, m_searchCenterPanel, true, true, true, false);
        }

		bottomPanel.add(combo);

		this.add(BorderLayout.SOUTH, bottomPanel);

	}

	private EntityInterface findTargetEntityInterface(String targetEntityName) {
		EntityInterface targetEntityInterface = null;
		Logger.out.info("targetEntityName " + targetEntityName);

		Iterator<EntityInterface> entityInter = allEntities.iterator();
		while (entityInter.hasNext()) {
			EntityInterface entity = entityInter.next();
			String entityName = entity.getName();
			if (entityName.equals(targetEntityName)) {
				targetEntityInterface = entity;
				Logger.out
						.info("found target entity interface to be set set as select entity in Query.");
				break;
			}
		}

		return targetEntityInterface;
	}

	/*
	 * This map is maintained, otherwise to get the entities in the category we
	 * have make one more server trip.
	 */
	// Map<EntityInterface, Category> entityToCategoryMap = new
	// HashMap<EntityInterface, Category>();
	// CategoryBusinessInterface catBusInter;
	Set<EntityInterface> allEntities = new HashSet<EntityInterface>();

	private Vector<String> getAllEntityNamesInWhereClause(
			IClientQueryBuilderInterface queryBuilder) {
		Vector<String> returner = new Vector<String>();

		List<IExpressionId> expIdsEnum = queryBuilder.getVisibleExressionIds();
		Iterator<IExpressionId> iter = expIdsEnum.iterator();

		IQuery query = queryBuilder.getQuery();

		while (iter.hasNext()) {
			IExpressionId expId = iter.next();
			IExpression expression = query.getConstraints()
					.getExpression(expId);

			EntityInterface entity = expression.getConstraintEntity()
					.getDynamicExtensionsEntity();

			/*
			 * If Entity is a Category iterate over that category's entities to
			 * get all the entity names
			 * 
			 * 19th Apr : With support for category in output, there is no need
			 * to break down category in constituent classes. Just add the
			 * EntityInterface
			 */
			
			
		/*	if (Utility.isCategory(entity)) {
				CategoryBusinessInterface catBusInter = null;
				if (catBusInter == null) {
					catBusInter = (CategoryBusinessInterface) CommonUtils
							.getBusinessInterface(
									EjbNamesConstants.CATEGORY_BEAN,
									CategoryHomeInterface.class, this);
				}

				try {
					Category category = catBusInter
							.getCategoryByEntityId(entity.getId());

					 Add to this map for later use. 

					Set<EntityInterface> entitiesInCategory = catBusInter
							.getAllSourceClasses(category);

					for (EntityInterface entityInCategory : entitiesInCategory) {
						allEntities.add(entityInCategory);
						String entityName = entityInCategory.getName();
						
						 * Add an entity name if it doesn't exist, so that the
						 * drop down items are unique.
						 
						 Add entity for later use 
						if (!returner.contains(entityName))
							returner.add(entityName);
					}

				} catch (RemoteException re) {
					CommonUtils.handleException(re, this, true, true, true,
							false);
				}
				
				 * If it is an entity, just get its name and add to the
				 * collection
				 
			} else {
*/				/* Add entity for later use */
				allEntities.add(entity);

				String entityName = entity.getName();
				/*
				 * Add an entity name if it doesn't exist, so that the drop down
				 * items are unique.
				 */
				if (!returner.contains(entityName))
					returner.add(entityName);
			//}

		}

		return returner;
	}
	
	public EntityInterface getRootEntity()
	{
		return this.m_rootEntity;
	}
}
