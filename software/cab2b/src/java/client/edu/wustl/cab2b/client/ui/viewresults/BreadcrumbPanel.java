/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JLabel;

import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;

/**
 * BreadcrumbPanel treats its components as crumbs and displays them in a queue or in the next line if the 
 * component is crossing the width of the parent container.  
 * @author chetan_patil
 */
public class BreadcrumbPanel extends Cab2bPanel {
    private static final long serialVersionUID = 1L;

    /** ActionListener for each crumb to be displayed */
    private ActionListener m_breadCrumbListener = null;

    /** Name of the crumbs to be displayed */
    private Vector<String> m_breadCrumbs = null;

    /** The parent container of this Panel */
    private Container parentContainer;
    
    private String currentBreadCrumbName;

    /**
     * Parameterized Constructor
     */
    BreadcrumbPanel(ActionListener breadCrumbListener, Vector<String> breadCrumbs, Container parentContainer) {
        this.m_breadCrumbListener = breadCrumbListener;
        this.m_breadCrumbs = breadCrumbs;
        this.parentContainer = parentContainer;
        initilizeGUI();
    }

    /**
     * This method initialize this panel based on the elements present in the breadcrumb vector.
     */
    private void initilizeGUI() {
        RiverLayout breadCrumbLayout = new RiverLayout(5,5);
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        this.setLayout(breadCrumbLayout);

        this.add(new JLabel("Path: ")); // Adding "Paths" JLabel.
        int size = m_breadCrumbs.size();

        for (int i = 0; i < size; i++) {
            // Get the text from the vector
            String strWholeText = m_breadCrumbs.get(i);
            String strJustText = strWholeText.substring(strWholeText.indexOf('#') + 1);

            if (i == size - 1) {
                // if last element, add lable instead of hyperlink
                JLabel jLabel = new JLabel(strJustText);
                jLabel.setForeground(new Cab2bHyperlink().getUnclickedColor());
                addComponent(jLabel);
                
                currentBreadCrumbName = strJustText;                
                break;
            }
            Cab2bHyperlink<String> breadCrumbHyperlink = new Cab2bHyperlink<String>(true);
            // Set the whole string as the user object for this link
            breadCrumbHyperlink.setUserObject(strWholeText);

            // Set the display name appropriatly
            breadCrumbHyperlink.setText(strJustText);
            breadCrumbHyperlink.addActionListener(this.m_breadCrumbListener);
            addComponent(breadCrumbHyperlink);

            if (i < size - 1) {
                JLabel arrow = new JLabel(">>");
                arrow.setForeground(breadCrumbHyperlink.getUnclickedColor());
                addComponent(arrow);
            }
        }
    }

    /**
     * This method adds a new component to this panel. This method decides whether to display 
     * the new component in the same queue of the crumbs or in the next queue, depending upon the 
     * length of the new component.
     * @param newComponent the Component to be added
     */
    private void addComponent(Component newComponent) {
        int parentWidth = parentContainer.getPreferredSize().width;
        final int hSpace = 5;
        int totalWidth = 0;

        Component[] components = this.getComponents();
        for (Component component : components) {
            Dimension dimension = component.getPreferredSize();
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

    /**
     * @return the currentBreadCrumbName
     */
    public String getCurrentBreadCrumbName() {
        return currentBreadCrumbName;
    }

}