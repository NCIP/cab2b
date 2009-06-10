package edu.wustl.cab2b.admin.searchdata.action;

import static edu.wustl.cab2b.admin.util.AdminConstants.ALL_SERVICE_INSTANCES;
import static edu.wustl.cab2b.admin.util.AdminConstants.FILTERED_SERVICE_INSTANCES;
import static edu.wustl.cab2b.admin.util.AdminConstants.OFFSET_PARAMETER;
import static edu.wustl.cab2b.admin.util.AdminConstants.SELECTED_SERVICE_NAME;
import static edu.wustl.cab2b.admin.util.AdminConstants.SERVICE_INSTANCE_ERROR;
import static edu.wustl.cab2b.admin.util.AdminConstants.USER_OBJECT;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;

import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.bizlogic.ServiceInstanceBizLogic;
import edu.wustl.cab2b.common.user.AdminServiceMetadata;
import edu.wustl.cab2b.common.user.UserInterface;

/**
 * 
 * @author atul_jawale
 * This class fetch all the available service instances from the
 * index service as well as from the repository.
 * Also this action is called when user navigates through the different pages 
 */
public class ServiceInstances extends BaseAction {
    private static final long serialVersionUID = 3009187890624681650L;

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ServiceInstances.class);

    private String serviceName;

    private String version;

    private String textbox;

    private String includeDescription;

    //  private Map request = null;


    /**
     * 
     * @param request
     
    public void setRequest(final Map request) {
        this.request = request;
    }

    /**
     * @return the includeDescription
     */
    public String getIncludeDescription() {
        return includeDescription;
    }

    /**
     * @param includeDescription the includeDescription to set
     */
    public void setIncludeDescription(String includeDescription) {
        this.includeDescription = includeDescription;
    }

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName the serviceName to set
     */
    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @return the request
     
    public Map getRequest() {
        return this.request;
    }
    */
    /**
     * @return the textbox
     */
    public String getTextbox() {
        return textbox;
    }

    /**
     * @param textbox the textbox to set
     */
    public void setTextbox(String textbox) {
        this.textbox = textbox;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * This action gets all the service models and store them in session.
     * @return the action result type 
     */
    @SuppressWarnings("unchecked")
    @Override
    public String execute() {

        List<AdminServiceMetadata> allServiceInstances = new ArrayList<AdminServiceMetadata>();
        List<AdminServiceMetadata> filteredServiceInstances = new ArrayList<AdminServiceMetadata>();

        String serviceNameFromSession = (String) session.get(SELECTED_SERVICE_NAME);
        serviceNameFromSession = serviceNameFromSession == null ? "" : serviceNameFromSession;

        /* Check if the page is invoked from the menu link then need to clear all the data from the session if any */

        String callFrom = request.getParameter("callFrom");
        if (callFrom != null) {
            session.remove(ALL_SERVICE_INSTANCES);
            session.remove(FILTERED_SERVICE_INSTANCES);
            session.remove("SelectedInstanceMap");
        }

        /* When offset is not null the call is for the pagination so return */

        String offset = (String) request.getParameter(OFFSET_PARAMETER);
        if (offset != null) {
            
            String pageNO =  request.getParameter("pageNO");
            String[] checkBox = request.getParameterValues("checkedServiceInstances"); 
            if (checkBox==null){
                checkBox  = new String[0];
            }
            Map<String, List<String>> pageVsURLs = null;
            if (session.get("SelectedInstanceMap") != null) {
                pageVsURLs = (Map<String, List<String>>) session.get("SelectedInstanceMap");
            } else {
                pageVsURLs = new HashMap<String, List<String>>();
            }
            pageVsURLs.put(pageNO, Arrays.asList(checkBox));
            session.put("SelectedInstanceMap",pageVsURLs);
            
         
            return SUCCESS;
        }

        /* When call is for the search of the particular instance using the search button  
          then textfield read for the search string and correspondingly call the search method */

        if (textbox != null && !textbox.equals("")) {
            searchInstancesHavingText(filteredServiceInstances);
        } else {
            allServiceInstances = (List<AdminServiceMetadata>) session.get(ALL_SERVICE_INSTANCES);
            if (allServiceInstances != null && serviceName.equalsIgnoreCase(serviceNameFromSession)) {
                session.put(FILTERED_SERVICE_INSTANCES, allServiceInstances);
            } else {
                allServiceInstances = new ArrayList<AdminServiceMetadata>();
                getServiceMetadata(allServiceInstances);
            }
        }
        return SUCCESS;
    }

    /**
     * This method used for the search functionality passed the list where the
     * search results will be stored. 
     * @param filteredServiceInstances
     */
    @SuppressWarnings("unchecked")
    private void searchInstancesHavingText(List<AdminServiceMetadata> filteredServiceInstances) {
        List<AdminServiceMetadata> allServiceInstances;
        allServiceInstances = (List<AdminServiceMetadata>) session.get(ALL_SERVICE_INSTANCES);
        filteredServiceInstances.clear();

        // Now fetch each serviceurl metadata for searchString
        for (AdminServiceMetadata adminServiceMetadata : allServiceInstances) {
            String hostingName = adminServiceMetadata.getHostingResearchCenter().toLowerCase();
            String serviceURL = adminServiceMetadata.getServiceURL().toLowerCase();
            boolean isSearchStringInHosting = hostingName.contains(textbox.toLowerCase());
            boolean isSearchStringInURL = serviceURL.contains(textbox.toLowerCase());
            boolean idAdded = false;

            //Check if search string in the hostingcenter or in the service url. 
            if (isSearchStringInHosting || isSearchStringInURL) {
                filteredServiceInstances.add(adminServiceMetadata);
                idAdded = true;
            }

            //If object not added and user selected include description
            if (!idAdded && includeDescription != null) {
                String desc = adminServiceMetadata.getSeviceDescription().toLowerCase();
                if (desc.contains(textbox.toLowerCase())) {
                    filteredServiceInstances.add(adminServiceMetadata);
                }
            }
        }

        session.put(FILTERED_SERVICE_INSTANCES, filteredServiceInstances);
    }

    /**
     * This method fetch the metadata for the service and put all the
     * service instances metadata in the session for to be used in successive actions by user.  
     * @param allServiceInstances
     */
    @SuppressWarnings("unchecked")
    private void getServiceMetadata(List<AdminServiceMetadata> allServiceInstances) {
        try {

            UserInterface user = (UserInterface) session.get(USER_OBJECT);
            Collection<AdminServiceMetadata> instanceList = null;
            ServiceInstanceBizLogic bizLogic = new ServiceInstanceBizLogic();
            instanceList = bizLogic.getServiceMetadataObjects(serviceName, version, user);
            allServiceInstances.addAll(instanceList);
            //Now add the instance metadata to the session
            if (allServiceInstances.size() > 0) {
                session.put(ALL_SERVICE_INSTANCES, allServiceInstances);
                session.put(FILTERED_SERVICE_INSTANCES, allServiceInstances);
                session.put(SELECTED_SERVICE_NAME, serviceName);
            }
            //else Remove all the existing service metadata from the session
            else {
                session.remove(ALL_SERVICE_INSTANCES);
                session.remove(FILTERED_SERVICE_INSTANCES);
                session.remove(SELECTED_SERVICE_NAME);
            }
        } catch (MalformedURIException e) {
            logger.error(e.getMessage(), e);
            request.setAttribute(SERVICE_INSTANCE_ERROR, "Index Service not available");
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
            request.setAttribute(SERVICE_INSTANCE_ERROR, "Index Service not available");
        }
    }

}