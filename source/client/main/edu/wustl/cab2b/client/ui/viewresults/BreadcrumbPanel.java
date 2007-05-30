package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JLabel;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.common.util.logger.Logger;

public class BreadcrumbPanel extends Cab2bPanel {

    private static final long serialVersionUID = 1L;

    private ActionListener m_breadCrumbListener = null;

    private Vector m_breadCrumbs = null;

    /**
     * Constructor
     */
    BreadcrumbPanel(ActionListener breadCrumbListener, Vector breadCrumbs) {
        this.m_breadCrumbListener = breadCrumbListener;
        this.m_breadCrumbs = breadCrumbs;
        initGUI();
    }

    /**
     * Creates the breadcrumb based on the elements in the vector.
     */
    private void initGUI() {
        this.setLayout(new RiverLayout());
        //JLabel label = ;
        //label.setPreferredSize(new Dimension(100, 20));
        this.add(new JLabel("Path: ")); // Adding "Paths" JLabel.
        int size = this.m_breadCrumbs.size();
        Logger.out.info("SIZE : " + size);
        for (int i = 0; i < size; i++) {
            //Get the text from the vector
            String strWholeText = (String) this.m_breadCrumbs.get(i);
            String strJustText = strWholeText.substring(strWholeText.indexOf("#") + 1);
            if(i==size-1) {
                //if last element, add lable instead of hyperlink
                JLabel selectedObject = new JLabel(strJustText);
                selectedObject.setForeground(new Cab2bHyperlink().getUnclickedHyperlinkColor());
                this.add(selectedObject);
                break;
            }
            Cab2bHyperlink breadCrumbHyperlink = new Cab2bHyperlink(true);
            //Set the whole string as the user object for this link
            breadCrumbHyperlink.setUserObject(strWholeText);

            
            //Set the display name appropriatly
            breadCrumbHyperlink.setText(strJustText);
            breadCrumbHyperlink.addActionListener(this.m_breadCrumbListener);
            this.add(breadCrumbHyperlink);
            if (i < size - 1) {
                JLabel arrow = new JLabel(">>");
                //arrow will match with other links due to this
                arrow.setForeground(breadCrumbHyperlink.getUnclickedHyperlinkColor());
                this.add(arrow);
            }
        }

    }

}
