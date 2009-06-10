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

public class Cab2bListBox extends JScrollPane {
    private JList m_list;

    public Cab2bListBox(ListModel model) {
        super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        m_list = new JList(model);
        m_list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        m_list.setFont(new Font("Arial", Font.PLAIN, 12));
        if (model.getSize() < 5) {
            m_list.setVisibleRowCount(model.getSize());
        } else {
            m_list.setVisibleRowCount(5);
        }
        getViewport().setView(m_list);
        setComponentSize();
    }

    public Object[] getSelectedValues() {
        return m_list.getSelectedValues();
    }

    public void setComponentSize() {
        ListModel model = m_list.getModel();
        FontMetrics fontMetrics = m_list.getFontMetrics(m_list.getFont());
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

    public void setSelectedValues(ArrayList<String> values) {
        DefaultListModel model = (DefaultListModel) m_list.getModel();
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
        m_list.setSelectedIndices(indexes);

    }

    /**
     * to sets datamodel for the ListBox
     * @param model
     */
    public void setModel(ListModel model) {
        m_list.setModel(model);
    }
}
