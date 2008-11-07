/**
 * 
 */
package edu.wustl.cab2b.client.ui.controls;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wustl.cab2b.client.ui.treetable.CategorialTableModel;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;

/**
 * @author atul_jawale
 *
 */
public class CategorialTablePanel extends JPanel {

    /**
     * 
     * @param record
     */
    public CategorialTablePanel(ICategorialClassRecord record) {
        super();
        init(record);

    }

    /**
     * This method initialises table and formats the categorialclasrecord as a table
     * @param record
     */
    public void init(ICategorialClassRecord record) {
        Cab2bTable table = new Cab2bTable(new CategorialTableModel(record));
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(600, 400));
        add(pane);
        this.setVisible(true);
    }

}
