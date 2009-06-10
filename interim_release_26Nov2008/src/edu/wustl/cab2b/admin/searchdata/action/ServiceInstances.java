package edu.wustl.cab2b.admin.searchdata.action;

import static edu.wustl.cab2b.admin.util.AdminConstants.ALL_SERVICE_INSTANCES;
import static edu.wustl.cab2b.admin.util.AdminConstants.FILTERED_SERVICE_INSTANCES;
import static edu.wustl.cab2b.admin.util.AdminConstants.OFFSET_PARAMETER;
import static edu.wustl.cab2b.admin.util.AdminConstants.SELECTED_SERVICE_NAME;
import static edu.wustl.cab2b.admin.util.AdminConstants.SERVICE_INSTANCE_ERROR;
import static edu.wustl.cab2b.admin.util.AdminConstants.USER_OBJECT;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.struts2.interceptor.RequestAware;

import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.bizlogic.ServiceInstanceBizLogic;
import edu.wustl.cab2b.common.user.AdminServiceMetadata;
import edu.wustl.cab2b.common.user.UserInterface;

/**
 * 
 * @author atul_jawale
 * 
 */
public class ServiceInstances extends BaseAction implements RequestAware {
    private static final long serialVersionUID = 3009187890624681650L;

    private String serviceName;

    private String version;

    private String textbox;

    private String includeDescription;

    private Map request = null;

    /**
     * 
     * @param request
     */
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
     * @param includeDescription
     *            the includeDescription to set
     */
    public void setIncludeDescription(String includeDescription) {
        this.includeDescription = includeDescription;
    }

    /**
     * @return the action result type This action gets all the service models
     *         and store them in session.
     */
    @Override
    public String execute() {
        List<AdminServiceMetadata> allServiceInstances = new ArrayList<AdminServiceMetadata>();
        List<AdminServiceMetadata> filteredServiceInstances = new ArrayList<AdminServiceMetadata>();

        String offset = (String) request.get(OFFSET_PARAMETER);
        String callFrom = (String) request.get("callFrom");
        String serviceNameFromSession = (String) session.get(SELECTED_SERVICE_NAME);
        serviceNameFromSession = serviceNameFromSession == null ? "" : serviceNameFromSession;

        if (callFrom != null) {
            session.remove(ALL_SERVICE_INSTANCES);
            session.remove(FILTERED_SERVICE_INSTANCES);
        }

        if (offset != null) {
            return SUCCESS;
        }

        if (textbox != null && !textbox.equals("")) {
            allServiceInstances = (List<AdminServiceMetadata>) session.get(ALL_SERVICE_INSTANCES);
            filteredServiceInstances.clear();
            getSearchedModels(allServiceInstances, filteredServiceInstances);
            session.put(FILTERED_SERVICE_INSTANCES, filteredServiceInstances);
        } else {
            if (session.get(ALL_SERVICE_INSTANCES) != null && serviceName.equalsIgnoreCase(serviceNameFromSession))
                session.put(FILTERED_SERVICE_INSTANCES,
                            (List<AdminServiceMetadata>) session.get(ALL_SERVICE_INSTANCES));
            else {
                try {
                    UserInterface user = (UserInterface) session.get(USER_OBJECT);
                    allServiceInstances.addAll((Collection<AdminServiceMetadata>) new ServiceInstanceBizLogic().getServiceMetadataObjects(
                                                                                                                                          serviceName,
                                                                                                                                          version,
                                                                                                                                          user));
                    if (allServiceInstances.size() > 0) {
                        session.put(ALL_SERVICE_INSTANCES, allServiceInstances);
                        session.put(FILTERED_SERVICE_INSTANCES, allServiceInstances);
                        session.put(SELECTED_SERVICE_NAME, serviceName);

                    } else {
                        session.remove(ALL_SERVICE_INSTANCES);
                        session.remove(FILTERED_SERVICE_INSTANCES);
                        session.remove(SELECTED_SERVICE_NAME);
                    }
                } catch (MalformedURIException e) {
                    e.printStackTrace();
                    request.put(SERVICE_INSTANCE_ERROR, "Index Service not available");
                } catch (RemoteException e) {
                    e.printStackTrace();
                    request.put(SERVICE_INSTANCE_ERROR, "Index Service not available");
                }
            }
        }

        return SUCCESS;
    }

    /**
     * Searches name
     * 
     * @param modelName
     * @param searchName
     * @return
     */
    public boolean searchName(String modelName, String searchName) {
        StringTokenizer tokenizer = new StringTokenizer(modelName);
        int counter = 0;
        while (tokenizer.hasMoreTokens()) {
            if ((tokenizer.nextToken().toLowerCase()).contains(searchName.toLowerCase()))
                counter++;
        }
        if (counter == 0)
            return false;
        else
            return true;
    }

    /**
     * Returns search models
     * 
     * @param allServiceInstances
     * @param filteredServiceInstances
     * 
     */
    public void getSearchedModels(List<AdminServiceMetadata> allServiceInstances,
                                  List<AdminServiceMetadata> filteredServiceInstances) {
        for (AdminServiceMetadata adminServiceMetadata : allServiceInstances) {
            if ((searchName(adminServiceMetadata.getHostingResearchCenter(), textbox))
                    || (searchName(adminServiceMetadata.getServiceURL(), textbox)))
                filteredServiceInstances.add(adminServiceMetadata);
        }
        if (includeDescription != null) {
            for (AdminServiceMetadata serviceMetaData : allServiceInstances) {
                if (searchName(serviceMetaData.getSeviceDescription(), textbox)) {
                    if (filteredServiceInstances.isEmpty()) {
                        filteredServiceInstances.add(serviceMetaData);
                    } else {
                        if (!filteredServiceInstances.contains(serviceMetaData))
                            filteredServiceInstances.add(serviceMetaData);
                    }
                }
            }
        }
    }

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName
     *            the serviceName to set
     */
    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @return the request
     */
    public Map getRequest() {
        return this.request;
    }

    /**
     * @return the textbox
     */
    public String getTextbox() {
        return textbox;
    }

    /**
     * @param textbox
     *            the textbox to set
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

}
