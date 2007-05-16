package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JLabel;

import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.common.util.logger.Logger;

/**
 * Panel for Bread crumb on show result screen
 * @author Mahesh Iyer
 * @author Chandrakant Talele
 * @author Rahul Ner
 */
public class BreadcrumbPanel extends Cab2bPanel {

    private static final long serialVersionUID = -684709343105422233L;

    private ActionListener breadCrumbListener;

    private Vector<String> breadCrumbVector;

    /**
     * default constructor
     * @param breadCrumbListener bread Crumb Listener to add
     * @param breadCrumbVector vector of breadCrumbs
     */
    BreadcrumbPanel(ActionListener breadCrumbListener, Vector<String> breadCrumbVector) {
        this.breadCrumbListener = breadCrumbListener;
        this.breadCrumbVector = breadCrumbVector;
        initGUI();
    }

    /**
     * Creates the breadcrumb based on the elements in the vector.
     */
    private void initGUI() {
        this.setLayout(new FlowLayout(5));
        this.add(new JLabel("Path : "));
        int size = this.breadCrumbVector.size();
        Logger.out.debug("SIZE : " + size);
        for (int i = 0; i < size; i++) {
            /* Get the text from the vector.*/
            String strWholeText = this.breadCrumbVector.get(i);
            Cab2bHyperlink breadCrumbHyperlink = new Cab2bHyperlink();
            /*Set the whole string as the user object for this link.*/
            breadCrumbHyperlink.setUserObject(strWholeText);

            String strJustText = strWholeText.substring(strWholeText.indexOf("#") + 1);
            /*Set the display name appropriatly*/
            breadCrumbHyperlink.setText(strJustText);
            breadCrumbHyperlink.addActionListener(this.breadCrumbListener);
            this.add(breadCrumbHyperlink);
            if (i < size - 1) {
                JLabel label = new JLabel(" >> ");
                label.setForeground(Cab2bHyperlink.getUnclickedHyperlinkColor());
                this.add(label);
            }
        }
    }
}