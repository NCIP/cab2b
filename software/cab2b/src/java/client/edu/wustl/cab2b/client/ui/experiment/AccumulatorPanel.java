/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.experiment;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;

import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bListBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;
import edu.wustl.cab2b.client.ui.util.UserObjectWrapper;

/**
 * Component to which allows to return selective objects from a list of objects.
 * 
 * @author Hrishikesh Rajpathak
 * 
 */
public class AccumulatorPanel extends Cab2bPanel {

    private Cab2bListBox leftListBox;

    private Cab2bListBox rightListBox;

    private DefaultListModel availableAttributeModel;

    private DefaultListModel selectedAttributeModel;

    private Cab2bButton add;

    private Cab2bButton addAll;

    private Cab2bButton remove;

    private Cab2bButton removeAll;

    private JLabel leftListTitle;

    private JLabel rightListTitle;

    private int listBoxWidth;

    private int listBoxHeight;

    private int heightBetweenButtons;

    /**
     * @param width
     * @param height
     * @param leftTitle
     * @param rightTitle
     * @param heightBetweenButtons
     */
    public AccumulatorPanel(int width, int height, String leftTitle, String rightTitle, int heightBetweenButtons) {

        this.listBoxWidth = width;
        this.listBoxHeight = height;
        this.heightBetweenButtons = heightBetweenButtons;
        availableAttributeModel = new DefaultListModel();
        selectedAttributeModel = new DefaultListModel();
        leftListBox = new Cab2bListBox(availableAttributeModel);
        rightListBox = new Cab2bListBox(selectedAttributeModel);
        add = new Cab2bButton("Add");
        addAll = new Cab2bButton("Add All");
        remove = new Cab2bButton("Remove");
        removeAll = new Cab2bButton("Remove All");

        leftListTitle = new JLabel(leftTitle);
        rightListTitle = new JLabel(rightTitle);
        initGUI();
    }

    /**
     * @param availableAttributes
     * @param selectedAttributes
     */
    public void setModel(Collection<UserObjectWrapper> availableAttributes,
                         Collection<UserObjectWrapper> selectedAttributes) {
        availableAttributeModel.clear();
        selectedAttributeModel.clear();

        for (UserObjectWrapper availableObject : availableAttributes) {
            availableAttributeModel.addElement(availableObject);
        }
        for (UserObjectWrapper selectedObject : selectedAttributes) {
            selectedAttributeModel.addElement(selectedObject);
        }

    }

    /**
     * @param leftTitle
     * @param rightTitle
     */
    public AccumulatorPanel(String leftTitle, String rightTitle) {
        this(300, 350, leftTitle, rightTitle, 10);
    }

    private void initGUI() {
        this.setPreferredSize(new Dimension(130 + listBoxWidth + listBoxWidth, listBoxHeight + 30));
        leftListBox.setPreferredSize(new Dimension(listBoxWidth, listBoxHeight));
        Cab2bPanel leftPanel = new Cab2bPanel(new RiverLayout(5, 5));
        leftPanel.add(leftListTitle);
        leftPanel.add("br", leftListBox);
        this.add(leftPanel);

        rightListBox = new Cab2bListBox(selectedAttributeModel);
        add = new Cab2bButton("Add");
        add.setPreferredSize(new Dimension(100, 22));
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object[] selectedValues = leftListBox.getSelectedValues();
                for (int i = 0; i < selectedValues.length; i++) {
                    selectedAttributeModel.addElement(selectedValues[i]);
                    availableAttributeModel.removeElement(selectedValues[i]);
                }
            }
        });

        addAll = new Cab2bButton("Add All");
        addAll.setPreferredSize(new Dimension(100, 22));
        addAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < availableAttributeModel.getSize(); i++) {
                    selectedAttributeModel.addElement(availableAttributeModel.get(i));
                }
                availableAttributeModel.removeAllElements();
            }
        });
        remove = new Cab2bButton("Remove");
        remove.setPreferredSize(new Dimension(100, 22));
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object[] selectedValues = rightListBox.getSelectedValues();
                for (int i = 0; i < selectedValues.length; i++) {
                    selectedAttributeModel.removeElement(selectedValues[i]);
                    availableAttributeModel.addElement(selectedValues[i]);
                }
            }
        });
        removeAll = new Cab2bButton("Remove All");
        removeAll.setPreferredSize(new Dimension(100, 22));
        removeAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < selectedAttributeModel.getSize(); i++) {
                    availableAttributeModel.addElement(selectedAttributeModel.get(i));
                }
                selectedAttributeModel.removeAllElements();
            }
        });
        Cab2bPanel buttonPanel = new Cab2bPanel(new RiverLayout(0, heightBetweenButtons));
        buttonPanel.setPreferredSize(new Dimension(110, listBoxHeight));
        buttonPanel.add(new JLabel(" "));
        buttonPanel.add("br", new JLabel(" "));
        buttonPanel.add("br", add);
        buttonPanel.add("br", addAll);
        buttonPanel.add("br", remove);
        buttonPanel.add("br", removeAll);

        rightListBox.setPreferredSize(new Dimension(listBoxWidth, listBoxHeight));

        Cab2bPanel rightPanel = new Cab2bPanel(new RiverLayout(5, 5));
        rightPanel.add(rightListTitle);
        rightPanel.add("br", rightListBox);

        this.add(buttonPanel);
        this.add(rightPanel);
    }

    /**
     * @return Collection of selected objects
     */
    public Collection getSelectedObjects() {
        Collection returnSet = new ArrayList();
        for (int i = 0; i < selectedAttributeModel.size(); i++) {
            returnSet.add(selectedAttributeModel.get(i));
        }
        return returnSet;
    }
}
