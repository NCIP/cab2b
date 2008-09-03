package edu.wustl.cab2b.client.ui.main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.log4j.Logger;

import edu.wustl.cab2b.client.ui.controls.Cab2bDatePicker;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
/**
 * 
 * @author chetan_bh
 */
public class DateTypePanel extends AbstractTypePanel {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(DateTypePanel.class);
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

    public DateTypePanel(ArrayList<String> conditionList, Dimension maxLabelDimension) {
        super(conditionList, maxLabelDimension);
    }

    public JComponent getFirstComponent() {
        DatePanel datePanel = new DatePanel();
        return datePanel;
    }

    public JComponent getSecondComponent() {
        DatePanel datePanel = new DatePanel();
        Cab2bDatePicker secondDatePicker = datePanel.getDatePicker();

        JLabel dateFormatLabel = datePanel.getDateFormatLabel();

        secondDatePicker.setVisible(false);
        secondDatePicker.setOpaque(false);

        dateFormatLabel.setVisible(false);
        dateFormatLabel.setOpaque(false);

        return datePanel;
    }

    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<String>();
        Date date = ((DatePanel) firstComponent).getDatePicker().getDate();
        if (date != null) {
            values.add(simpleDateFormat.format(date));
            date = ((DatePanel) secondComponent).getDatePicker().getDate();
            if (date != null) {
                values.add(simpleDateFormat.format(date));
            }
        }
        return values;
    }

    public void setValues(ArrayList<String> values) {
        if (values.size() == 1) {
            Date date = null;
            try {
                date = simpleDateFormat.parse(values.get(0));
            } catch (java.text.ParseException e) {
                logger.warn("Problem while setting date : " + e.getMessage());
            }
            ((DatePanel) firstComponent).getDatePicker().setDate(date);
        } else if (values.size() == 2) {
            Date date = null;

            try {
                date = simpleDateFormat.parse(values.get(0));
            } catch (java.text.ParseException e) {
                logger.warn("Problem while setting date : " + e.getMessage());
            }
            ((DatePanel) firstComponent).getDatePicker().setDate(date);

            try {
                date = simpleDateFormat.parse(values.get(1));
            } catch (java.text.ParseException e) {
                logger.warn("Problem while setting date : " + e.getMessage());
            }
            ((DatePanel) secondComponent).getDatePicker().setDate(date);
        } else {
            // This for In condition
        }
    }

    public void setComponentPreference(String condition) {
    }

    public void resetPanel() {
        ((DatePanel) firstComponent).getDatePicker().setDate(null);
        ((DatePanel) secondComponent).getDatePicker().setDate(null);
    }

}

/**
 * A panel containing date picker component and a label component.
 * 
 * @author chetan_bh
 */
class DatePanel extends Cab2bPanel {
    private static final long serialVersionUID = 1L;

    /**
     * Date picker component.
     */
    private Cab2bDatePicker datePicker;

    /**
     * Date format label component.
     */
    private JLabel dateFormatLabel;

    public DatePanel() {
        initGUI();
    }

    /**
     * Initialize GUI.
     */
    private void initGUI() {
        datePicker = new Cab2bDatePicker();

        dateFormatLabel = new Cab2bLabel(" yyyy/mm/dd");
        dateFormatLabel.setOpaque(false);
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setHgap(0);

        this.setLayout(layout);
        this.add(datePicker);
        this.add(dateFormatLabel);
    }

    /**
     * Returns date format label component.
     * @return label component.
     */
    public JLabel getDateFormatLabel() {
        return dateFormatLabel;
    }

    /**
     * Sets date format label.
     * @param dateFormatLabel
     */
    public void setDateFormatLabel(JLabel dateFormatLabel) {
        this.dateFormatLabel = dateFormatLabel;
    }

    /**
     * GReturns date picker component.
     * @return date picker component.
     */
    public Cab2bDatePicker getDatePicker() {
        return datePicker;
    }

    /**
     * Sets date picker component.
     * @param datePicker
     */
    public void setDatePicker(Cab2bDatePicker datePicker) {
        this.datePicker = datePicker;
    }

    /**
     * Sets visible all the components inside the DatePanel.
     */
    public void setVisible(boolean aFlag) {
        if (datePicker != null) {
            datePicker.setVisible(aFlag);
        }

        if (dateFormatLabel != null) {
            dateFormatLabel.setVisible(aFlag);
        }
    }

    /**
     * Sets opaque all the components inside the DatePanel.
     */
    public void setOpaque(boolean isOpaque) {
        if (datePicker != null) {
            datePicker.setOpaque(isOpaque);
        }

        if (dateFormatLabel != null) {
            dateFormatLabel.setOpaque(isOpaque);
        }
    }

    /**
     * Sets enabled all the component inside the DatePanel.
     */
    public void setEnabled(boolean enabled) {
        if (datePicker != null) {
            datePicker.setEnabled(enabled);
        }

        if (dateFormatLabel != null) {
            dateFormatLabel.setEnabled(enabled);
        }
    }
}
