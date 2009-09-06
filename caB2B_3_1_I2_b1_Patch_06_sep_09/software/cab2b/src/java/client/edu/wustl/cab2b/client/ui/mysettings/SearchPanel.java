/**
 * 
 */
package edu.wustl.cab2b.client.ui.mysettings;

import static edu.wustl.cab2b.client.ui.util.ClientConstants.SEARCH_EVENT;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;

import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTextField;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;

/**
 * @author atul_jawale
 * This class is a search Panel used for to search through the results
 * This panel will only fire the events and pass the search string. 
 * The panel using this object should catch the events and write the logic of
 * search.    
 * 
 */
public class SearchPanel extends Cab2bPanel implements ActionListener {

    private Cab2bTextField searchTextFld;

    private final Cab2bHyperlink showAll = new Cab2bHyperlink();

    private Cab2bButton searchBtn;

    /**
     * Default Constructor
     */
    public SearchPanel() {
        super(new RiverLayout(5, 10));
        initGUI();
    }

    /**
     * @param objectList
     */
    public SearchPanel(List<? extends Object> objectList) {
        super(new RiverLayout(5, 10));
        initGUI();
    }

    /**
     * This method will be called when user clicks on search button
     * showAll link will be activated if user enters some search string
     * If user entered the string this method will fire an SEARCH_EVENT
     * & pass the search string as the event's new Value.
     * @param event   
     */
    public void actionPerformed(ActionEvent event) {
        final String searchString = searchTextFld.getText();
        if (null == searchString || searchString.length()==0) {
            JOptionPane.showMessageDialog(this.getParent().getParent(), "Please enter the search String.");
            searchTextFld.requestFocus();
        } else {
            showAll.setEnabled(true);
            firePropertyChange(SEARCH_EVENT, null, searchString);
        }
    }

    /**
     * This method initializes the component 
     * by default showAll link will be disabled
     *  
     */
    private void initGUI() {
        setPreferredSize(new Dimension(400, 50));

        searchTextFld = new Cab2bTextField();
        searchTextFld.requestFocus();
        searchTextFld.setColumns(25);

        searchBtn = new Cab2bButton("Search");
        searchBtn.addActionListener(this);

        showAll.setText("Show All");
        showAll.setEnabled(false);
        showAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                showAll.setEnabled(false);
                searchTextFld.setText("");
                firePropertyChange(SEARCH_EVENT, null, "ShowAll");
            }
        });

        final Cab2bLabel searchLabel = new Cab2bLabel("Search for :");
        add("br", searchLabel);
        add(searchTextFld);
        add(searchBtn);
        add(showAll);
    }

    /**
     * This method disable/Enable search Panel
     * @param isEnable
     */
    public void setEnable(boolean isEnable) {
        searchTextFld.setEnabled(isEnable);
        searchBtn.setEnabled(isEnable);
    }
}
