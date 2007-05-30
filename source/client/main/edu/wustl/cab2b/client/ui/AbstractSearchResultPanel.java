package edu.wustl.cab2b.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXPanel;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.main.IComponent;
import edu.wustl.cab2b.client.ui.main.ParseXMLFile;
import edu.wustl.cab2b.client.ui.main.SwingUIManager;
import edu.wustl.cab2b.client.ui.pagination.JPagination;
import edu.wustl.cab2b.client.ui.pagination.NumericPager;
import edu.wustl.cab2b.client.ui.pagination.PageElement;
import edu.wustl.cab2b.client.ui.pagination.PageElementImpl;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.queryengine.Cab2bQueryObjectFactory;
import edu.wustl.cab2b.common.util.AttributeInterfaceComparator;
import edu.wustl.cab2b.common.util.EntityInterfaceComparator;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.util.logger.Logger;

/**
 * The abstract class that contains commonalities required for displaying
 * results from the 'AddLimit' and 'choose category' section from the main
 * search dialog. Concrete classes must over ride methods to effect custom
 * layout.
 * 
 * @author mahesh_iyer/chetan_bh/gautam_shetty.
 * 
 */

public abstract class AbstractSearchResultPanel extends Cab2bPanel implements
		ActionListener {

	/** The pagination component to paginate the results of the search */
	private JPagination m_resultsPage = null;

	/**
	 * Saved reference to the content panel that needs to be refreshed for
	 * appropriate events.
	 */
	protected ContentPanel m_addLimitPanel = null;

	/**
	 * Constructor
	 * 
	 * @param addLimitPanel
	 *            Reference to the parent content panel that needs refreshing.
	 * 
	 * @param result
	 *            The collectiond of entities.
	 */

	public AbstractSearchResultPanel(ContentPanel addLimitPanel, Set<EntityInterface> result) {
		initGUI(addLimitPanel, result);
	}

	/**
	 * Method initializes the panel by appropriately laying out child
	 * components.
	 * 
	 * @param addLimitPanel
	 *            Reference to the parent content panel that needs refreshing.
	 * 
	 * @param result
	 *            The collectiond of entities.
	 * 
	 */

	private void initGUI(final ContentPanel addLimitPanel,
			Set<EntityInterface> resultSet) {
		this.setLayout(new RiverLayout());
		this.m_addLimitPanel = addLimitPanel;
		if (m_addLimitPanel instanceof AddLimitPanel) {
			((AddLimitPanel) m_addLimitPanel).setSearchResultPanel(this);
		}
		Vector<PageElement> pageElementCollection = new Vector<PageElement>();
		List<EntityInterface> resultList = new ArrayList<EntityInterface>(
				resultSet);
		Collections.sort(resultList, new EntityInterfaceComparator());
		for (EntityInterface entity : resultList) {
			// set the proper class name
			String className = edu.wustl.cab2b.common.util.Utility
					.getDisplayName(entity);
			Logger.out.info(className);
			String strDescription = entity.getDescription();

			// Create an instance of the PageElement. Initialize with the
			// appropriate data
			PageElement pageElement = new PageElementImpl();
			pageElement.setDisplayName(className);
			pageElement.setDescription(strDescription);
			pageElement.setUserObject(entity);
			pageElementCollection.add(pageElement);
		}

		NumericPager numPager = new NumericPager(pageElementCollection,
				getPageSize());
		/* Initalize the pagination component. */
		this.m_resultsPage = new JPagination(pageElementCollection, numPager,
				this, true);
		this.m_resultsPage.setSelectableEnabled(false);
		this.m_resultsPage.setGroupActionEnabled(false);
		this.m_resultsPage.addPageElementActionListener(this);
		this.add("p vfill", this.m_resultsPage);
	}

	/**
	 * The action listener for the individual page elements.
	 * 
	 * @param actionEvent
	 *            The event that contains details of the click on the individual
	 *            page elements.
	 * 
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		Cab2bHyperlink link = (Cab2bHyperlink) (actionEvent.getSource());
		PageElement element = (PageElement) link.getUserObject();

		/* This is the EntityInterface instance. */
		final EntityInterface entity = (EntityInterface) element
				.getUserObject();
		final JXPanel[] componentPanel = getAddLimitPanels(entity);
		final JXPanel[] panelsToAdd = new Cab2bPanel[componentPanel.length + 1];
		for (int i = 0; i < componentPanel.length; i++) {
			panelsToAdd[i] = componentPanel[i];
		}
		// Add the "Add Limit" button.
		Cab2bButton addLimitButton = new Cab2bButton("Add Limit");
		addLimitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				performAddLimitAction(panelsToAdd, entity);
			}
		});
		panelsToAdd[panelsToAdd.length - 1] = new Cab2bPanel();
		panelsToAdd[panelsToAdd.length - 1].add(addLimitButton);

		if (componentPanel != null) {
			// pass the appropriate class name for display
			performAction(panelsToAdd, edu.wustl.cab2b.common.util.Utility
					.getDisplayName(entity));
		}
	}

	/**
	 * Get panels array to be displayed in add limit panel
	 * 
	 * @param entity
	 * @return
	 */
	public JXPanel[] getAddLimitPanels(final EntityInterface entity) {
		final Collection<AttributeInterface> attributeCollection = entity
				.getAttributeCollection();
		ParseXMLFile parseFile = null;
		try {
			parseFile = ParseXMLFile.getInstance();
		} catch (CheckedException ce) {
			CommonUtils.handleException(ce, this, true, true, false, false);
		}

		if (attributeCollection != null) {
			List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>(
					attributeCollection);
			Collections.sort(attributeList, new AttributeInterfaceComparator());
			final JXPanel[] componentPanels = new Cab2bPanel[attributeList
					.size()];
			try {
				int i = 0;
				for (AttributeInterface attribute : attributeList) {
					componentPanels[i++] = (JXPanel) SwingUIManager
							.generateUIPanel(parseFile, attribute);
				}
			} catch (CheckedException e) {
				CommonUtils.handleException(e, this, true, true, false, false);
				// JXErrorDialog.showDialog(this,
				// ErrorCodeHandler.getErrorMessage(e.getErrorCode()), e);
			}
			return componentPanels;
		}
		return null;
	}

	/**
	 * Get panels array to be displayed in add limit panel
	 * 
	 * @param entity
	 * @return
	 */
	public JXPanel[] getEditLimitPanels(final IExpression expression) {
		/* This is the EntityInterface instance. */
		final EntityInterface entity = expression.getConstraintEntity()
				.getDynamicExtensionsEntity();
		final JXPanel[] componentPanel = getAddLimitPanels(entity);
		final JXPanel[] panelsToAdd = new Cab2bPanel[componentPanel.length + 1];
		for (int i = 0; i < componentPanel.length; i++) {
			panelsToAdd[i] = componentPanel[i];
		}
		// Add the "Add Limit" button.
		Cab2bButton addLimitButton = new Cab2bButton("Edit Limit");
		addLimitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				performEditLimitAction(panelsToAdd, expression);
			}
		});
		panelsToAdd[panelsToAdd.length - 1] = new Cab2bPanel();
		panelsToAdd[panelsToAdd.length - 1].add(addLimitButton);
		return panelsToAdd;
	}

	/**
	 * Method to handle 'Add Limit' button click event
	 */
	public void performAddLimitAction(JXPanel[] componentPanel,
			EntityInterface entity) {
		final Collection collection = entity.getAttributeCollection();
		final int size = collection.size();
		List<AttributeInterface> attributes = new ArrayList<AttributeInterface>(
				size);
		List<String> conditions = new ArrayList<String>(size);
		List<List<String>> values = new ArrayList<List<String>>();

		for (int j = 0; j < componentPanel.length - 1; j++) {
			IComponent panel = (IComponent) componentPanel[j];
			String conditionString = panel.getCondition();

			// Check if condition is set for this panel

			AttributeInterface attribute = getAttribute(collection, panel
					.getAttributeName());
			ArrayList<String> conditionValues = panel.getValues();
			if (0 == conditionString.compareToIgnoreCase("Between")
					&& (conditionValues.size() == 1)) {
				JOptionPane.showInternalMessageDialog((this.m_addLimitPanel)
						.getParent().getParent().getParent(),
						"Please enter both the values for between operator.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if ((conditionString.equals("Is Null"))
					|| conditionString.equals("Is Not Null")
					|| (conditionValues.size() != 0)) {
				attributes.add(attribute);
				conditions.add(conditionString);
				values.add(conditionValues);
			}
		}
		if (attributes.size() == 0) {
			JOptionPane.showInternalMessageDialog((this.m_addLimitPanel)
					.getParent().getParent().getParent(),
					"Please enter atleast one condition.", "Error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			MainSearchPanel mainSearchPanel = (MainSearchPanel) ((JXPanel) m_addLimitPanel)
					.getParent().getParent();
			if (mainSearchPanel.getQueryObject() == null) {
				IClientQueryBuilderInterface query = new ClientQueryBuilder();
				mainSearchPanel.setQueryObject(query);
				m_addLimitPanel.setQueryObject(query);
			}

			IExpressionId expressionId = mainSearchPanel.getQueryObject()
					.addRule(attributes, conditions, values);
			if (expressionId != null) {
				// Pratibha's code will take over from here
				m_addLimitPanel.refreshBottomCenterPanel(expressionId);
			} else {
				JOptionPane
						.showInternalMessageDialog(
								mainSearchPanel.getParent(),
								"This rule cannot be added as it is not associated with the added rules.",
								"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private AttributeInterface getAttribute(Collection collection,
			String attributeName) {
		AttributeInterface attribute = null;
		Iterator iterator = collection.iterator();

		while (iterator.hasNext()) {
			attribute = (AttributeInterface) iterator.next();
			if (attributeName.trim().equals(attribute.getName().trim())) {
				break;
			}
		}

		return attribute;
	}

	/**
	 * Method to perform edit limit action
	 */
	public void performEditLimitAction(JXPanel[] componentPanel,
			IExpression expression) {
		final Collection collection = expression.getConstraintEntity()
				.getDynamicExtensionsEntity().getAttributeCollection();
		List<ICondition> conditionList = new ArrayList<ICondition>();
		for (int j = 0; j < componentPanel.length - 1; j++) {
			IComponent panel = (IComponent) componentPanel[j];
			String conditionString = panel.getCondition();

			AttributeInterface attribute = getAttribute(collection, panel
					.getAttributeName());
			ArrayList<String> values = panel.getValues();
			if (0 == conditionString.compareToIgnoreCase("Between")
					&& (values.size() == 1)) {
				JOptionPane.showInternalMessageDialog((this.m_addLimitPanel)
						.getParent().getParent().getParent(),
						"Please enter both the values for between operator.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if ((conditionString.equals("Is Null"))
					|| conditionString.equals("Is Not Null")
					|| (values.size() != 0)) {

				ICondition condition = Cab2bQueryObjectFactory
						.createCondition();
				condition.setAttribute(attribute);
				condition
						.setRelationalOperator(edu.wustl.cab2b.client.ui.query.Utility
								.getRelationalOperator(conditionString));
				for (int i = 0; i < values.size(); i++) {
					condition.addValue(values.get(i));
				}
				conditionList.add(condition);
			}
		}
		if (conditionList.size() != 0) {
			IRule rule = (IRule) expression.getOperand(0);
			rule.removeAllConditions();
			for (int i = 0; i < conditionList.size(); i++) {
				rule.addCondition(conditionList.get(i));
			}
		} else {
			MainSearchPanel mainSearchPanel = (MainSearchPanel) ((JXPanel) m_addLimitPanel)
					.getParent().getParent();
			JOptionPane
					.showInternalMessageDialog(
							mainSearchPanel.getParent(),
							"This rule cannot be added as it is not associated with the added rules.",
							"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * The abstract method that needs to handle any refresh related activites
	 * 
	 * @param arrComponentPanel
	 *            This is the array of panels that forms the dynamically
	 *            generated criterion pages. Each panel corresponds to one
	 *            attribute from the class/category
	 * 
	 * @param strClassName
	 *            The class/category name.
	 */
	public abstract void performAction(JXPanel[] arrComponentPanel,
			String strClassName);

	/**
	 * The abstract method that is to return the number of elements to be
	 * displayed/page.
	 * 
	 * @return int Value represents the number of elements/page.
	 */
	public abstract int getPageSize();

}
