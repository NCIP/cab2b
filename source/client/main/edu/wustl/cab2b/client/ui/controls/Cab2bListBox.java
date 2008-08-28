package edu.wustl.cab2b.client.ui.controls;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

/**
 * A component that allows the user to select one or more objects from a list. 
 * A separate model, ListModel, represents the contents of the list. It's easy to 
 * display an array or vector of objects, using a Cab2bListBox constructor that 
 * builds a ListModel instance for you: 
 * @author Chetan_BH 
 */
public class Cab2bListBox extends JScrollPane {
    /**
     * List reference
     */
    private JList list;

    /**
     * Constructs a Cab2bListBox that displays the elements in the specified, non-null model.
     * @param model
     */
    public Cab2bListBox(ListModel model) {
        super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        list = new JList(model);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setFont(new Font("Arial", Font.PLAIN, 12));
        if (model.getSize() < 5) {
            list.setVisibleRowCount(model.getSize());
        } else {
            list.setVisibleRowCount(5);
        }
        getViewport().setView(list);
        setComponentSize();
    }

    /**
     * Returns selected values from list
     * @return Object[]
     */
    public Object[] getSelectedValues() {
        return list.getSelectedValues();
    }

    /**
     * Sets  components size
     */
    public void setComponentSize() {
        ListModel model = list.getModel();
        FontMetrics fontMetrics = list.getFontMetrics(list.getFont());
        int width = 0;
        int stringWidth;
        for (int i = 0; i < model.getSize(); i++) {
            stringWidth = fontMetrics.stringWidth((String) model.getElementAt(i));
            if (stringWidth > width) {
                width = stringWidth;
            }
        }
        width += 25;
        if (width < 80) {
            width = 100;
        } else if (width > 250) {
            width = 250;
        }

        int height = (fontMetrics.getHeight()) * model.getSize();
        height += fontMetrics.getMaxAscent();
        if (height > 80) {
            height = 80;
        }
        setPreferredSize(new Dimension(width, height));

    }

    /**
     * Set values as selected in list
     * @param values
     */
    public void setSelectedValues(ArrayList<String> values) {
        DefaultListModel model = (DefaultListModel) list.getModel();
        int[] indexes = new int[values.size()];
        for (int j = 0; j < values.size(); j++) {
            String str = values.get(j);
            for (int i = 0; i < model.getSize(); i++) {
                if (0 == model.getElementAt(i).toString().compareToIgnoreCase(str)) {
                    indexes[j] = i;
                    break;
                }
            }
        }
        list.setSelectedIndices(indexes);

    }

    /**
     * to sets datamodel for the ListBox
     * @param model
     */
    public void setModel(ListModel model) {
        list.setModel(model);
    }
}
