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

    private final boolean adminDefined = false;

    private final boolean configured = false;

    private boolean servicePOC = false;

    private ServiceURLInterface serviceURL = new ServiceURL();

    /**
     * This method is called from IndexServiceOperations. This method takes ServiceMetadata object from 
     * IndexService as input and returns the service metadata object in ServiceURLInterface form.
     * @param serviceMetadata
     * @param urlLocation
     * @param entityGroupName
     * @return ServiceURLInterface
     */
    public ServiceURLInterface getServiceMetadata(ServiceMetadata serviceMetadata, String urlLocation,
                                                  String entityGroupName) {

        if (serviceMetadata != null) {
            ServiceMetadataServiceDescription serviceDescription = serviceMetadata.getServiceDescription();
            ServiceMetadataHostingResearchCenter hostingCenter = serviceMetadata.getHostingResearchCenter();

            if (serviceDescription != null) {
                Service service = serviceDescription.getService();
                if (service != null) {
                    getServiceInformation(service);
                }
            }
            if (hostingCenter != null) {
                ResearchCenter researchCenter = hostingCenter.getResearchCenter();
                if (researchCenter != null) {
                    getHostingCenterInformation(researchCenter);
                }
            }
        }
        serviceURL.setUrlLocation(urlLocation);
        serviceURL.setEntityGroupName(entityGroupName);
        serviceURL.setAdminDefined(adminDefined);
        serviceURL.setConfigured(configured);
        return serviceURL;
    }

    private void getHostingCenterInformation(ResearchCenter researchCenter) {
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

    private void getServiceInformation(Service service) {
        getServiceDescription(service);
        ServicePointOfContactCollection servicePointofContact = service.getPointOfContactCollection();
        if (servicePointofContact != null) {
            PointOfContact[] pointofContact = servicePointofContact.getPointOfContact();
            if (pointofContact != null) {
                servicePOC = getPointofContact(pointofContact[0]);
            }
        }
    }

    private void getShortName(ResearchCenter researchCenter) {
        String hostingCenterShortName = researchCenter.getShortName();
        if (hostingCenterShortName != null && hostingCenterShortName.length() != 0) {
            serviceURL.setHostingCenterShortName(hostingCenterShortName);
        }
    }

    private void getDisplayName(ResearchCenter researchCenter) {
        String hostingCenter = researchCenter.getDisplayName();
        if (hostingCenter != null && hostingCenter.length() != 0) {
            serviceURL.setHostingCenter(hostingCenter);
        }
    }

    private boolean getPointofContact(PointOfContact pointofContact) {
        boolean returnValue = false;
        if (pointofContact != null) {
            String firstName = pointofContact.getFirstName();
            String lastName = pointofContact.getLastName();
            String eMail = pointofContact.getEmail();

            if (firstName != null && firstName.length() != 0 && lastName != null && lastName.length() != 0) {
                StringBuffer contactName = new StringBuffer(firstName);
                contactName.append(" ").append(lastName);
                serviceURL.setContactName(contactName.toString());
                returnValue = true;
            }
            if (eMail != null && eMail.length() != 0) {
                serviceURL.setContactMailId(eMail);
                returnValue = true;
            }
        }
        return returnValue;
    }

    private void getServiceDescription(Service service) {
        String serviceDescription = service.getDescription();
        if (serviceDescription != null && serviceDescription.length() != 0) {
            serviceURL.setDescription(serviceDescription);
        }
    }
}
