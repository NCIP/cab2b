package edu.wustl.cab2b.client.ui.controls.slider;

import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import java.awt.BorderLayout;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Hrishikesh Rajpathak
 * @author Deepak Shingan
 * @author Atul Jawale
 *
 */
public class BiSlider extends JPanel {

    public static final String EVENT_RANGE_CHANGED = "EVENT_RANGE_CHANGED";
    
    public Cab2bLabel lowVal;

    public Cab2bLabel hiVal;

    MThumbSlider mSlider;

    public BiSlider(Vector data) {
        super();

        mSlider = new MThumbSlider(data, this);
        initUI(data);

    }

    public BiSlider(Vector data,Object initialMin, Object initialMax) {
        super();
        mSlider = new MThumbSlider(data, this,initialMin,initialMax);
        initUI(data);

    }

    private void initUI() {
        lowVal = new Cab2bLabel();
        hiVal = new Cab2bLabel();
        setLayout(new BorderLayout());
        add(mSlider, BorderLayout.CENTER);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(lowVal, BorderLayout.WEST);
        panel.add(hiVal, BorderLayout.EAST);
        add(panel, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    private void initUI(Vector data) {
        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
        if (data.get(0) instanceof java.util.Date) {
            lowVal = new Cab2bLabel(dateFormatter.format(data.get(0)));
            hiVal = new Cab2bLabel(dateFormatter.format(data.get(data.size() - 1)));
        } else {
            String str = data.get(0).toString();
            if (str.length() > 15) {
                str = str.substring(0, 14) + "...";
            }
            lowVal = new Cab2bLabel(str);
            str = data.get(data.size() - 1).toString();
            if (str.length() > 15) {
                str = str.substring(0, 14) + "...";
            }
            hiVal = new Cab2bLabel(str);

        }

        setLayout(new BorderLayout());
        add(mSlider, BorderLayout.CENTER);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(lowVal, BorderLayout.WEST);
        panel.add(hiVal, BorderLayout.EAST);
        add(panel, BorderLayout.NORTH);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Vector vetor = new Vector();
        vetor.add("a");
        vetor.add("b");
        vetor.add("c");

        JFrame frame = new JFrame();
        frame.setSize(300, 400);
        BiSlider cab2bSliderUI = new BiSlider(vetor,"a","b");
        frame.add(cab2bSliderUI);
        frame.setVisible(true);
    }

    void setFilterValues(Object min, Object max) {
        firePropertyChange( EVENT_RANGE_CHANGED, min, max);
    }

}
