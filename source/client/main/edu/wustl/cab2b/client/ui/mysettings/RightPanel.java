package edu.wustl.cab2b.client.ui.mysettings;

import static edu.wustl.cab2b.client.ui.util.ClientConstants.BACK_EVENT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.SERVICE_SELECT_EVENT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.UPDATE_EVENT;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;

/**
 * @author atul_jawale
 *
 *  This class is the right panel container which gets updated every time when user selects a menu item
 *  Here we are registering the property change listener so that
 *  
 */
public class RightPanel extends Cab2bPanel {

    private AllServicesPanel allServices;

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
     */
    public RightPanel(Collection<EntityGroupInterface> selectedServices) {
        super(new BorderLayout());
        allServices = new AllServicesPanel(selectedServices);
        initGUI();
    }
    
    
    /**
     * This method registers the service select event so that when user clicks on a service
     * it will fetch all the running instances and display them to user.
     */
    private void initGUI() {
       
        add(allServices);
        allServices.addPropertyChangeListener(SERVICE_SELECT_EVENT, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                String serviceName = (String) event.getNewValue();
                loadServiceInstances(serviceName);
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
                serviceInstancePanel = new ServiceInstancesPanel(serviceName);
                serviceInstancePanel.addPropertyChangeListener(BACK_EVENT, new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent event) {
                        loadAllServices();
                    }
                });

                serviceInstancePanel.addPropertyChangeListener(UPDATE_EVENT, new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent event) {
                        String status = (String) event.getNewValue();
                      //  firePropertyChange(UPDATE_EVENT, serviceName, status);
                        allServices.refreshPanel(serviceName, status);
                        loadAllServices();
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
}
