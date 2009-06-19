package edu.wustl.cab2bwebapp.util;

import java.util.Comparator;

import edu.wustl.cab2b.common.user.ServiceURLInterface;

/**
 * @author gaurav_mehta
 * This class sorts the Service instances based on whether it is configured or not and if both 
 * are configured then on URL Location
 */
public class ServiceInstanceComparator implements Comparator<ServiceURLInterface> {

    /**
     * @param obj1
     * @param obj2
     * @return value depending on whether the 2 values are smaller or greater
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(ServiceURLInterface obj1, ServiceURLInterface obj2) {

        if (obj1.isConfigured() && obj2.isConfigured()) {
            return obj1.getHostingCenter().compareTo(obj2.getHostingCenter());
        } else if (obj1.isConfigured() && !obj2.isConfigured()) {
            return -1;
        } else if (!obj1.isConfigured() && obj2.isConfigured()) {
            return 1;
        } else {
            obj1.getHostingCenter().compareTo(obj2.getHostingCenter());
        }
        return 0;
    }
}