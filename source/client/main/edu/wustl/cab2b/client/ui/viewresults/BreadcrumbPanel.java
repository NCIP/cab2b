package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JLabel;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;

public class BreadcrumbPanel extends Cab2bPanel {

	private static final long serialVersionUID = 1L;

	private ActionListener m_breadCrumbListener = null;

	private Vector<String> m_breadCrumbs = null;

	private Container parentContainer;

	/**
	 * Constructor
	 */
	BreadcrumbPanel(ActionListener breadCrumbListener,
			Vector<String> breadCrumbs, Container parentContainer) {
		this.m_breadCrumbListener = breadCrumbListener;
		this.m_breadCrumbs = breadCrumbs;
		this.parentContainer = parentContainer;
		initGUI();
	}

	/**
	 * Creates the breadcrumb based on the elements in the vector.
	 */
	private void initGUI() {
		RiverLayout breadCrumbLayout = new RiverLayout();
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.setLayout(breadCrumbLayout);

		this.add(new JLabel("Path: ")); // Adding "Paths" JLabel.
		int size = m_breadCrumbs.size();

		for (int i = 0; i < size; i++) {
			// Get the text from the vector
			String strWholeText = m_breadCrumbs.get(i);
			String strJustText = strWholeText.substring(strWholeText
					.indexOf("#") + 1);

			if (i == size - 1) {
				// if last element, add lable instead of hyperlink
				JLabel selectedObject = new JLabel(strJustText);
				selectedObject.setForeground(new Cab2bHyperlink()
						.getUnclickedHyperlinkColor());

				// this.add(selectedObject);
				addComponent(selectedObject);
				break;
			}
			Cab2bHyperlink breadCrumbHyperlink = new Cab2bHyperlink(true);
			// Set the whole string as the user object for this link
			breadCrumbHyperlink.setUserObject(strWholeText);

			// Set the display name appropriatly
			breadCrumbHyperlink.setText(strJustText);
			breadCrumbHyperlink.addActionListener(this.m_breadCrumbListener);

			// this.add(breadCrumbHyperlink);
			addComponent(breadCrumbHyperlink);

			if (i < size - 1) {
				JLabel arrow = new JLabel(">>");
				arrow.setForeground(breadCrumbHyperlink
						.getUnclickedHyperlinkColor());

				// this.add(arrow);
				addComponent(arrow);
			}
		}
	}

	private void addComponent(Component newComponent) {
		int parentWidth = parentContainer.getPreferredSize().width;
		final int hSpace = 5;
		int totalWidth = 0;

		Component[] components = this.getComponents();
		for (Component component : components) {
			Dimension dimension = component.getPreferredSize();
			// Point point = component.getLocation();
			// int crumbWidth = point.x + dimension.width;
			int crumbWidth = hSpace + dimension.width;

			if (totalWidth + crumbWidth >= parentWidth) {
				totalWidth = crumbWidth;
			} else {
				totalWidth = totalWidth + crumbWidth;
			}
		}

		Dimension newComponentDimension = newComponent.getPreferredSize();
		int newComponentWidth = newComponentDimension.width;
		// Add the new component
		int crumbWidth = hSpace + newComponentWidth;
		if (totalWidth + crumbWidth >= parentWidth) {
			this.add("br ", newComponent);
			totalWidth = crumbWidth;
		} else {
			this.add(newComponent);
			totalWidth += crumbWidth;
		}
	}

}