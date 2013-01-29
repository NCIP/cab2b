/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.mysettings;

import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.SERVICE_URLS_FAILURE;
import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.SERVICE_URLS_SUCCESS;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.BACK_EVENT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.DIALOG_CLOSE_EVENT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.SEARCH_EVENT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.UPDATE_EVENT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.UPDATE_QUERYURLS_EVENT;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;

import edu.wustl.cab2b.client.cache.UserCache;
import edu.wustl.cab2b.client.serviceinstances.ServiceInstanceConfigurator;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.pagination.JPagination;
import edu.wustl.cab2b.client.ui.pagination.NumericPager;
import edu.wustl.cab2b.client.ui.pagination.PageElement;
import edu.wustl.cab2b.client.ui.pagination.PageElementImpl;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * This class is the service instance panel where user is shown all the running service 
 * instances of the service name passed as the parameter.

 * @author atul_jawale 
 * @author gaurav_mehta 
 */
public class ServiceInstancesPanel extends Cab2bPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -1831708303795078858L;

    private JPagination resultsPage;

    private Collection<ServiceURLInterface> allServiceInstances;

    private final JXTitledPanel titledSearchResultsPanel = new Cab2bTitledPanel();

    private final Collection<ServiceURLInterface> filteredServiceInstances = new ArrayList<ServiceURLInterface>();

    private String serviceName;

    private String version;

    private boolean isForQuery;

    private List<String> URLList;

    /**
     * @param serviceName
     * @param isForQuery
     * @param URLList
     */
    public ServiceInstancesPanel(String serviceName, boolean isForQuery, List<String> URLList) {
        super(new BorderLayout());
        this.serviceName = serviceName;
        this.isForQuery = isForQuery;
        this.URLList = URLList;
        initGUI();
    }

    /**
     * This method initializes the panel
     * 
     * @param serviceName
     */
    private void initGUI() {
        allServiceInstances = new ArrayList<ServiceURLInterface>();
        UserInterface user = UserCache.getInstance().getCurrentUser();
        ServiceInstanceConfigurator configurator = new ServiceInstanceConfigurator();

        if (serviceName.indexOf('~') > -1) {
            String[] serviceVersion = serviceName.split("~");
            serviceName = serviceVersion[0];
            version = serviceVersion[1];
        }

        Collection<ServiceURLInterface> adminServiceMetadata = configurator.getServiceMetadataObjects(serviceName, version,
                                                                                             user);
        allServiceInstances.addAll((adminServiceMetadata));
        filteredServiceInstances.addAll(allServiceInstances);

        String title = serviceName + " Service Instances (" + filteredServiceInstances.size() + ")";
        titledSearchResultsPanel.setTitle(title);
        Cab2bButton saveButton = initSaveButton();
        SearchPanel searchPanel = new SearchPanel();
        Cab2bPanel resultPanel = null;

        if (filteredServiceInstances.size() > 0) {
            resultPanel = generateContentPanel(filteredServiceInstances);
            GradientPaint gp = new GradientPaint(new Point2D.Double(.05d, 0), new Color(185, 211, 238),
                    new Point2D.Double(.95d, 0), Color.WHITE);

            titledSearchResultsPanel.setTitlePainter(new BasicGradientPainter(gp));
            titledSearchResultsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
            titledSearchResultsPanel.setTitleFont(new Font("SansSerif", Font.BOLD, 11));
            titledSearchResultsPanel.setTitleForeground(Color.BLACK);

        } else {
            Cab2bLabel noResultLabel = new Cab2bLabel("No instance found.");
            noResultLabel.setForeground(Color.blue);
            resultPanel = new Cab2bPanel();
            resultPanel.add(noResultLabel);
            saveButton.setEnabled(false);
            searchPanel.setEnable(false);
        }
        titledSearchResultsPanel.setContentContainer(resultPanel);
        searchPanel.addPropertyChangeListener(SEARCH_EVENT, new SearchPanelPropertyListener());

        Cab2bButton backButton = initBackButton();
        Cab2bButton adminSettings = initAdminSettingsButton();

        FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT);
        flowLayout.setHgap(10);
        final Cab2bPanel bottomButtonContainer = new Cab2bPanel();
        bottomButtonContainer.setBackground(null);
        bottomButtonContainer.setLayout(flowLayout);
        bottomButtonContainer.add(backButton);
        bottomButtonContainer.add(saveButton);
        bottomButtonContainer.add(adminSettings);

        Cab2bButton closeButton = new Cab2bButton("Close");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                firePropertyChange(DIALOG_CLOSE_EVENT, true, false);
            }
        });
        bottomButtonContainer.add(closeButton);

        add(searchPanel, BorderLayout.NORTH);
        add(titledSearchResultsPanel, BorderLayout.CENTER);
        add(bottomButtonContainer, BorderLayout.SOUTH);

    }

    /**
     * This method creates the save button add a action listener
     * to it.
     * @return the savebutton object
     */
    private Cab2bButton initSaveButton() {
        Cab2bButton saveButton = new Cab2bButton("Save Settings");
        saveButton.setPreferredSize(new Dimension(120, 22));
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                List<ServiceURLInterface> selectedInstances = resultsPage.getSelectedPageElementsUserObjects();
                saveButtonFunction(selectedInstances);
            }
        });

        return saveButton;
    }

    /**
     * This method initializes the back button 
     * @return
     */
    private Cab2bButton initBackButton() {
        Cab2bButton backButton = new Cab2bButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                firePropertyChange(BACK_EVENT, serviceName, null);
            }
        });

        return backButton;
    }

    /**
     * This method initializes the admin settings button
     * adds the listener
     * @return
     */
    private Cab2bButton initAdminSettingsButton() {
        Cab2bButton adminSettings = new Cab2bButton("Admin Settings");
        adminSettings.setPreferredSize(new Dimension(120, 22));
        adminSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                adminDefaultSettings();
            }
        });

        return adminSettings;
    }

    /**
     * This 
     * @param selectedInstances
     */
    private void saveButtonFunction(List<ServiceURLInterface> selectedInstances) {
        if (selectedInstances.size() == 0) {
            JOptionPane.showMessageDialog(this.getParent(), "Please select atleast 1 option");
        } else {
            saveServiceInstances(selectedInstances);
        }
    }

    /**
     * This method saves the selected service instances.
     * if isForQuery is true that means these selected services will be
     * updated in the present query only so this will pass the new urls
     * as new value parameter to the fireproperty() method.  
     * 
     * Else it will update the database using the ServiceInstanceConfigurator's
     *  saveServiceInstances() method and finally fires the Update event
     * 
     * @param selectedInstances
     */
    private void saveServiceInstances(List<ServiceURLInterface> selectedInstances) {
        String status = ApplicationProperties.getValue(SERVICE_URLS_SUCCESS);
        // if it is for the search data wizard 3rd step 
        // it will update the query object only 
        if (isForQuery) {
            List<String> urlList = new ArrayList<String>();
            for (ServiceURLInterface url : selectedInstances) {
                urlList.add(url.getUrlLocation());
            }
            firePropertyChange(UPDATE_QUERYURLS_EVENT, serviceName, urlList);
            firePropertyChange(UPDATE_EVENT, serviceName, status);
            return;
        }
        try {
            ServiceInstanceConfigurator bizLogic = new ServiceInstanceConfigurator();
            String entityGroupName = Utility.createModelName(serviceName, version);
            UserInterface currentUser = edu.wustl.cab2b.client.ui.query.Utility.getCurrentUser();
            bizLogic.saveServiceInstances(entityGroupName, selectedInstances, currentUser);
            firePropertyChange(UPDATE_EVENT, serviceName, status);
        } catch (RemoteException exception) {
            CommonUtils.handleException(exception, titledSearchResultsPanel, true, true, false, false);
            status = ApplicationProperties.getValue(SERVICE_URLS_FAILURE);
            firePropertyChange(UPDATE_EVENT, serviceName, status);
        }
    }

    /**
     * This method is the admin default settings button functionality
     * it will send a emptylist to the saveServiceInstances() method 
     * so as to remove all the user configurated urls from the serviceURLCollection 
     */
    private void adminDefaultSettings() {
        List<ServiceURLInterface> emptyList = new ArrayList<ServiceURLInterface>();
        saveServiceInstances(emptyList);
        firePropertyChange(UPDATE_EVENT, serviceName, "( Admin Settings restored )");
    }

    /**
     * This method generates the Pagination component using the collection of
     * the service metadata.Mainly used to generate the pagination component at
     * first time and then every time when user search for the service instance.
     * 
     * @param resultPanel
     */
    private Cab2bPanel generateContentPanel(final Collection<ServiceURLInterface> filteredServiceInstances) {
        Cab2bPanel resultPanel = new Cab2bPanel();
        Vector<PageElement> pageElementCollection = new Vector<PageElement>();
        for (ServiceURLInterface serviceInstance : filteredServiceInstances) {
            // Create an instance of the PageElement. Initialize with the
            // appropriate data
            PageElement pageElement = new PageElementImpl();
            // not able view NCICB completely it was appearing as NCICE so added
            // a space.
            pageElement.setDisplayName(serviceInstance.getHostingCenter() + " ");
            pageElement.setDescription(serviceInstance.getDescription());
            pageElement.setUserObject(serviceInstance);
            String serviceURL = serviceInstance.getUrlLocation();
            if (URLList != null && URLList.contains(serviceURL)) {
                pageElement.setSelected(true);
            } else {
                pageElement.setSelected(serviceInstance.isConfigured());
            }
            pageElementCollection.add(pageElement);
        }

        /* Initialize the pagination component. */
        final NumericPager numericPager = new NumericPager(pageElementCollection, 10);
        resultsPage = new JPagination(pageElementCollection, numericPager, this, true);
        resultsPage.setSelectableEnabled(true);
        resultsPage.setGroupActionEnabled(true);
        resultPanel.add("hfill vfill ", resultsPage);

        return resultPanel;
    }

    /**
     * This method is the search method for service instance Panel the user
     * given search string is checked in every instance's hosting
     * researchcenter's name and added to the result.
     * 
     * @param searchString
     * 
     */
    private void searchResult(String searchString, final Collection<ServiceURLInterface> filteredServiceInstances) {
        filteredServiceInstances.clear();
        if (allServiceInstances != null) {
            if ("ShowAll".equals(searchString)) {
                filteredServiceInstances.addAll(allServiceInstances);
            }
            searchString = searchString.toLowerCase();
            for (ServiceURLInterface metadata : allServiceInstances) {
                String hostingResearchCenter = metadata.getHostingCenter().toLowerCase();
                if (hostingResearchCenter.contains(searchString)) {
                    filteredServiceInstances.add(metadata);
                }
            }
        }
    }

    class SearchPanelPropertyListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent event) {
            String searchString = (String) event.getNewValue();
            searchResult(searchString, filteredServiceInstances);
            Cab2bPanel contentPanel = generateContentPanel(filteredServiceInstances);
            titledSearchResultsPanel.setContentContainer(contentPanel);
            String title = serviceName + " Service Instances (" + filteredServiceInstances.size() + ")";
            titledSearchResultsPanel.setTitle(title);
            revalidate();
        }
    }
}
