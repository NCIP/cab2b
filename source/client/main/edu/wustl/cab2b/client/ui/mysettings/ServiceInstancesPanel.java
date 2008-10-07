package edu.wustl.cab2b.client.ui.mysettings;

import static edu.wustl.cab2b.client.ui.util.ClientConstants.BACK_EVENT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.SEARCH_EVENT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.UPDATE_EVENT;

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
import edu.wustl.cab2b.client.serviceinstances.ServiceInstanceBizLogic;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.pagination.JPagination;
import edu.wustl.cab2b.client.ui.pagination.NumericPager;
import edu.wustl.cab2b.client.ui.pagination.PageElement;
import edu.wustl.cab2b.client.ui.pagination.PageElementImpl;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.user.AdminServiceMetadata;
import edu.wustl.cab2b.common.user.UserInterface;

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

    private Collection<AdminServiceMetadata> allServiceInstances;

    private final JXTitledPanel titledSearchResultsPanel = new Cab2bTitledPanel();

    private final Collection<AdminServiceMetadata> filteredServiceInstances = new ArrayList<AdminServiceMetadata>();

    private String serviceName;

    /**
     * 
     * @param serviceName
     */
    public ServiceInstancesPanel(String serviceName) {
        super(new BorderLayout());
        this.serviceName = serviceName;
        initGUI();
    }

    /**
     * This method initializes the panel
     * 
     * @param serviceName
     */
    private void initGUI() {
        allServiceInstances = new ArrayList<AdminServiceMetadata>();
        UserInterface user = UserCache.getInstance().getCurrentUser();
        Collection<AdminServiceMetadata> adminServiceMetadata = new ServiceInstanceBizLogic().getServiceMetadataObjects(
                                                                                                                        serviceName,
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

        Cab2bPanel bottomButtonContainer = new Cab2bPanel(new FlowLayout(10, 5, 2));
        bottomButtonContainer.add(backButton);
        bottomButtonContainer.add(saveButton);
        bottomButtonContainer.add(adminSettings);

        add(searchPanel, BorderLayout.NORTH);
        add(titledSearchResultsPanel, BorderLayout.CENTER);
        add(bottomButtonContainer, BorderLayout.SOUTH);
    }

    private Cab2bButton initSaveButton() {
        Cab2bButton saveButton = new Cab2bButton("Save Settings");
        saveButton.setPreferredSize(new Dimension(120, 22));
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                List<AdminServiceMetadata> selectedInstances = resultsPage.getSelectedPageElementsUserObjects();
                saveButtonFunction(selectedInstances);
            }
        });

        return saveButton;
    }

    private Cab2bButton initBackButton() {
        Cab2bButton backButton = new Cab2bButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                firePropertyChange(BACK_EVENT, serviceName, null);
            }
        });

        return backButton;
    }

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

    private void saveButtonFunction(List<AdminServiceMetadata> selectedInstances) {
        if (selectedInstances.size() == 0) {
            JOptionPane.showMessageDialog(this.getParent(), "Please select atleast 1 option");
        } else {
            saveServiceInstances(selectedInstances);
        }
    }

    private void saveServiceInstances(List<AdminServiceMetadata> selectedInstances) {
        ServiceInstanceBizLogic bizLogic = new ServiceInstanceBizLogic();
        try {
            bizLogic.saveServiceInstances(serviceName,selectedInstances, UserCache.getInstance().getCurrentUser());
            firePropertyChange(UPDATE_EVENT, serviceName, "( Configured Successfully )");

        } catch (RemoteException exception) {
            CommonUtils.handleException(exception, titledSearchResultsPanel, true, true, false, false);
            firePropertyChange(UPDATE_EVENT, serviceName, "(Unable to Configure )");
        }
    }

    private void adminDefaultSettings() {
        List<AdminServiceMetadata> emptyList = new ArrayList<AdminServiceMetadata>();
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
    private Cab2bPanel generateContentPanel(final Collection<AdminServiceMetadata> filteredServiceInstances) {
        Cab2bPanel resultPanel = new Cab2bPanel();
        Vector<PageElement> pageElementCollection = new Vector<PageElement>();
        for (AdminServiceMetadata serviceInstance : filteredServiceInstances) {
            // Create an instance of the PageElement. Initialize with the
            // appropriate data
            PageElement pageElement = new PageElementImpl();
            // not able view NCICB completely it was appearing as NCICE so added
            // a space.
            pageElement.setDisplayName(serviceInstance.getHostingResearchCenter() + " ");
            pageElement.setDescription(serviceInstance.getSeviceDescription());
            pageElement.setUserObject(serviceInstance);
            pageElement.setSelected(serviceInstance.isConfigured());
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
     * @return
     */
    private void searchResult(String searchString, final Collection<AdminServiceMetadata> filteredServiceInstances) {
        filteredServiceInstances.clear();
        if (allServiceInstances != null) {
            if ("ShowAll".equals(searchString)) {
                filteredServiceInstances.addAll(allServiceInstances);
            }
            searchString = searchString.toLowerCase();
            for (AdminServiceMetadata metadata : allServiceInstances) {
                String hostingResearchCenter = metadata.getHostingResearchCenter().toLowerCase();
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
