/**
 * 
 */
package edu.wustl.cab2b.server.serviceurl;

import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter;
import gov.nih.nci.cagrid.metadata.ServiceMetadataServiceDescription;
import gov.nih.nci.cagrid.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.cagrid.metadata.common.ResearchCenterPointOfContactCollection;
import gov.nih.nci.cagrid.metadata.service.Service;
import gov.nih.nci.cagrid.metadata.service.ServicePointOfContactCollection;

/**
 * @author gaurav_mehta
 *
 */
public class ServiceMetadataProcessor {

    private final static boolean adminDefined = false;

    private final static boolean configured = false;

    private static String hostingCenter;

    private static String serviceDescription;

    private static String contactName;

    private static String contactMailId;

    private static String hostingCenterShortName;

    static ServiceURLInterface serviceURL = new ServiceURL();

    public static ServiceURLInterface getServiceMetadata(ServiceMetadata serviceMetadata, String urlLocation,
                                                         String entityGroupName) {
        boolean servicePOC = false;

        if (serviceMetadata != null) {
            ServiceMetadataServiceDescription serviceDescription = serviceMetadata.getServiceDescription();
            ServiceMetadataHostingResearchCenter hostingCenter = serviceMetadata.getHostingResearchCenter();

            if (serviceDescription != null) {
                Service service = serviceDescription.getService();
                if (service != null) {
                    getServiceDescription(service);
                    ServicePointOfContactCollection servicePointofContact = service.getPointOfContactCollection();
                    if (servicePointofContact != null) {
                        PointOfContact[] pointofContact = servicePointofContact.getPointOfContact();
                        if (pointofContact != null) {
                            servicePOC = getPointofContact(pointofContact[0]);
                        }
                    }
                }
            }
            if (hostingCenter != null) {
                ResearchCenter researchCenter = hostingCenter.getResearchCenter();
                if (researchCenter != null) {
                    getDisplayName(researchCenter);
                    getShortName(researchCenter);
                    if (!servicePOC) {
                        ResearchCenterPointOfContactCollection hostingPointofContact = researchCenter.getPointOfContactCollection();
                        if (hostingPointofContact != null) {
                            PointOfContact[] pointofContact = hostingPointofContact.getPointOfContact();
                            if (pointofContact != null) {
                                getPointofContact(pointofContact[0]);
                            }
                        }
                    }
                }
            }
        }
        serviceURL.setUrlLocation(urlLocation);
        serviceURL.setEntityGroupName(entityGroupName);
        serviceURL.setAdminDefined(adminDefined);
        serviceURL.setConfigured(configured);
        return serviceURL;
    }

    private static void getShortName(ResearchCenter researchCenter) {
        hostingCenterShortName = researchCenter.getShortName();
        if (hostingCenterShortName != null && hostingCenterShortName.length() != 0) {
            serviceURL.setHostingCenterShortName(hostingCenterShortName);
        }
    }

    private static void getDisplayName(ResearchCenter researchCenter) {
        hostingCenter = researchCenter.getDisplayName();
        if (hostingCenter != null && hostingCenter.length() != 0) {
            serviceURL.setHostingCenter(hostingCenter);
        }
    }

    private static boolean getPointofContact(PointOfContact pointofContact) {
        boolean returnValue = true;
        if (pointofContact != null) {
            String firstName = pointofContact.getFirstName();
            String lastName = pointofContact.getLastName();
            String eMail = pointofContact.getEmail();

            if (firstName != null && firstName.length() != 0 && lastName != null && lastName.length() != 0) {
                contactName = firstName + " " + lastName;
                serviceURL.setContactName(contactName);
                returnValue = true;
            }
            if (eMail != null && eMail.length() != 0) {
                contactMailId = eMail;
                serviceURL.setContactMailId(contactMailId);
                returnValue = true;
            }
        } else {
            returnValue = false;
        }
        return returnValue;
    }

    private static void getServiceDescription(Service service) {
        serviceDescription = service.getDescription();
        if (serviceDescription != null || serviceDescription.length() != 0) {
            serviceURL.setDescription(serviceDescription);
        }
    }
}
