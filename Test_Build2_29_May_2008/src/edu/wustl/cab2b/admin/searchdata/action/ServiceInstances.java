package edu.wustl.cab2b.admin.searchdata.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.beans.AdminServiceMetadata;
import edu.wustl.cab2b.admin.bizlogic.ServiceInstanceBizLogic;
import static edu.wustl.cab2b.admin.util.AdminConstants.*;

public class ServiceInstances extends BaseAction implements RequestAware, SessionAware {

    private static final long serialVersionUID = 3009187890624681650L;

    private String serviceName;

    private Map<String, Object> session;

    private String textbox;

    private String includeDescription;

    private Map request = null;

    /**
     * 
     * @param arg0
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
     * @param includeDescription the includeDescription to set
     */
    public void setIncludeDescription(String includeDescription) {
        this.includeDescription = includeDescription;
    }

    /**
     * @return the action result type
     * This action gets all the service models and store them in session.
     */
    @Override
    public String execute() {

        List<AdminServiceMetadata> allServiceInstances = new ArrayList<AdminServiceMetadata>();
        List<AdminServiceMetadata> filteredServiceInstances = allServiceInstances;
        String offset = (String) request.get(OFFSET_PARAMETER);
        String callFrom = (String) request.get("callFrom");
        if(callFrom!=null)
        {
            session.remove(ALL_SERVICE_INSTANCES);
            session.remove(FILTERED_SERVICE_INSTANCES);
        }
        if (offset != null ) {
            return SUCCESS;
        }
        if (textbox != null && !textbox.equals("")) {
            allServiceInstances = (List<AdminServiceMetadata>) session.get(ALL_SERVICE_INSTANCES);
            filteredServiceInstances.clear();
            getSearchedModels(allServiceInstances, filteredServiceInstances);
            session.put(FILTERED_SERVICE_INSTANCES, filteredServiceInstances);
        } else {
            if (session.get(ALL_SERVICE_INSTANCES) != null)
                session.put(FILTERED_SERVICE_INSTANCES,
                            (List<AdminServiceMetadata>) session.get(ALL_SERVICE_INSTANCES));
            else {

                try {
                    allServiceInstances.addAll((Collection<AdminServiceMetadata>) new ServiceInstanceBizLogic().getServiceMetadataObjects(serviceName));
                    session.put(ALL_SERVICE_INSTANCES, allServiceInstances);
                    session.put(FILTERED_SERVICE_INSTANCES, allServiceInstances);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return SUCCESS;
    }

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

    public void getSearchedModels(List<AdminServiceMetadata> allServiceInstances,
                                  List<AdminServiceMetadata> filteredServiceInstances) {
        for (AdminServiceMetadata adminServiceMetadata : allServiceInstances) {
            if (searchName(adminServiceMetadata.getHostingResearchCenter(), textbox))
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
     * @param serviceName the serviceName to set
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
     * @param textbox the textbox to set
     */
    public void setTextbox(String textbox) {
        this.textbox = textbox;
    }

    /**
     * @return Returns the session.
     */
    public Map<String, Object> getSession() {
        return session;
    }

    /**
     * @param session The session to set.
     */
    public void setSession(Map session) {
        this.session = session;
    }

}
