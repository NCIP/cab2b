package edu.wustl.cab2b.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.dag.FreeDag;
import edu.wustl.cab2b.client.ui.util.UserObjectWrapper;
import edu.wustl.cab2b.common.util.Constants;

/**
 * Panel to add selective attributes to create custom category.
 * 
 * @author Hrishikesh Rajpathak
 *
 */
public class AttributeSelectCDCPanel extends Cab2bPanel {

	private EntityInterface entityInterface;

	private Cab2bButton addToDAGButton;

	private FreeDag testDAG;

	private Cab2bPanel finalPanel;

	private Cab2bPanel addToDAGPanel;

	public AttributeSelectCDCPanel() {

		addToDAGButton = new Cab2bButton("Add To DAG");
		addToDAGButton.setPreferredSize(new Dimension(100, 22));
		addToDAGButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				testDAG.addNode(entityInterface);
			}
		});

		addToDAGPanel = new Cab2bPanel(new BorderLayout());
		Cab2bPanel panel = new Cab2bPanel();
		panel.add(addToDAGButton);
		addToDAGPanel.add(panel, BorderLayout.WEST);
		addToDAGPanel.setPreferredSize(new Dimension(Constants.WIZARD_SIZE2_DIMENSION.width, 80));

		finalPanel = new Cab2bPanel(new BorderLayout());
	}

	/**
	 * @param entityInterface The entityInterface to set.
	 */
	public void setEntityInterface(EntityInterface entityInterface) {
		this.entityInterface = entityInterface;
	}

	/**
	 * This method generated the panel with all the values in left hand list box
	 */
	public void generatePanel() {
		this.removeAll();
		finalPanel.removeAll();
		Collection<AttributeInterface> attributeCollection = entityInterface.getAllAttributes();

		List<UserObjectWrapper> userObj = new ArrayList();

		for (AttributeInterface attribute : attributeCollection) {
			UserObjectWrapper obj = new UserObjectWrapper<AttributeInterface>(attribute, attribute
					.toString());
			userObj.add(obj);
		}

		ObjectSelectionPanel newPanel = new ObjectSelectionPanel(userObj,300,300, "left", "right",15);
		finalPanel.add(addToDAGPanel, BorderLayout.NORTH);
		finalPanel.add(newPanel, BorderLayout.SOUTH);
		this.add(finalPanel);
		updateUI();
	}

	/**
	 * @return Returns the testDAG.
	 */
	public FreeDag getTestDAG() {
		return testDAG;
	}

	/**
	 * @param testDAG The testDAG to set.
	 */
	public void setTestDAG(FreeDag testDAG) {
		this.testDAG = testDAG;
	}
}
