/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.mysettings;

import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.SERVICE_URLS_SUCCESS;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.BACK_EVENT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.DIALOG_CLOSE_EVENT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.SERVICE_SELECT_EVENT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.UPDATE_EVENT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.UPDATE_QUERYURLS_EVENT;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * @author atul_jawale
 *
 *  This class is the right panel container which gets updated every time when user selects a menu item
 *  Here we are registering the property change listener so that
 *  
 */
public class RightPanel extends Cab2bPanel {

    private AllServicesPanel allServices;

    private boolean isForQuery;

    private Map<String, List<String>> entityToURLMap = new HashMap<String, List<String>>();

    /**
     * Default Constructor
     */
    public RightPanel() {
        super(new BorderLayout());
        allServices = new AllServicesPanel();
        initGUI();
    }

    /**
     * Default Constructor
     * @param selectedServices
     * @param entityURLMap
     */
    public RightPanel(Collection<EntityGroupInterface> selectedServices, Map<String, List<String>> entityURLMap) {
        super(new BorderLayout());
        allServices = new AllServicesPanel(selectedServices);
        isForQuery = true;
        this.entityToURLMap.putAll(entityURLMap);
        initGUI();
    }

    /**
     * This method registers the service select event so that when user clicks on a service
     * it will fetch all the running instances and display them to user.
     */
    private void initGUI() {

        add(allServices);

        //This is the handler when user clicks on the service name (e.g. CaNanoLab)  
        allServices.addPropertyChangeListener(SERVICE_SELECT_EVENT, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                String serviceName = (String) event.getNewValue();
                loadServiceInstances(serviceName);
            }
        });

        //This is the handler when user clicks on the close button on the
        // all available services page.
        allServices.addPropertyChangeListener(DIALOG_CLOSE_EVENT, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                firePropertyChange(DIALOG_CLOSE_EVENT, true, false);
            }
        });

        addPropertyChangeListener(UPDATE_EVENT, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                String serviceName = (String) event.getNewValue();
                loadServiceInstances(serviceName);
            }
        });
    }

    /**
     * This method is called when user selects the service it will first search all the
     * running instances of the service and shows them to user in ServiceInstancesPanel  
     * @param serviceName
     */
    private void loadServiceInstances(final String serviceName) {
        final CustomSwingWorker swingWorker = new CustomSwingWorker(this) {
            private ServiceInstancesPanel serviceInstancePanel;

            /**
             * This method will fetch the running service instances
             * till this methods execution user will be shown a wait cursor
             */
            protected void doNonUILogic() {
                List<String> URLList = entityToURLMap.get(serviceName);
                serviceInstancePanel = new ServiceInstancesPanel(serviceName, isForQuery, URLList);
                serviceInstancePanel.addPropertyChangeListener(BACK_EVENT, new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent event) {
                        loadAllServices();
                    }
                });

                serviceInstancePanel.addPropertyChangeListener(UPDATE_EVENT, new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent event) {
                        String status = (String) event.getNewValue();
                        allServices.refreshPanel(serviceName, status);
                        loadAllServices();
                    }
                });

                serviceInstancePanel.addPropertyChangeListener(UPDATE_QUERYURLS_EVENT,
                                                               new PropertyChangeListener() {
                                                                   public void propertyChange(
                                                                                              PropertyChangeEvent event) {
                                                                       List<String> newURLList = (List<String>) event.getNewValue();
                                                                       String entityGroupName = serviceName;
                                                                       String version = "";
 
                                                                       if(serviceName.indexOf('~')>-1)
                                                                       {
                                                                           String nameVersion[] = serviceName.split("~");
                                                                           entityGroupName =    nameVersion[0];
                                                                           version = nameVersion[1];
                                                                       }
                                                                       String entityName = Utility.createModelName(entityGroupName, version);
                                                                       entityToURLMap.put(entityName, newURLList);
                                                                       String status = ApplicationProperties.getValue(SERVICE_URLS_SUCCESS);
                                                                       allServices.refreshPanel(serviceName,
                                                                                                status);
                                                                       loadAllServices();
                                                                   }
                                                               });

                serviceInstancePanel.addPropertyChangeListener(DIALOG_CLOSE_EVENT, new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent event) {
                        firePropertyChange(DIALOG_CLOSE_EVENT, true, false);
                    }
                });

            }

            /**
             * Once all the instances are fetched this method will display the ServiceInstancesPanel
             * to the user.
             */
            protected void doUIUpdateLogic() {
                removeAll();
                add(serviceInstancePanel);
                revalidate();
            }
        };
        swingWorker.start();
    }

    /**
     * This method will load Services Page to user when user clicks on back on ServiceInstancesPanel.  
     */
    private void loadAllServices() {
        removeAll();
        repaint();
        add(allServices);
        repaint();
    }

    /**
     * @return the entityToURLMap
     */
    public Map<String, List<String>> getEntityToURLMap() {
        return entityToURLMap;
    }
}
