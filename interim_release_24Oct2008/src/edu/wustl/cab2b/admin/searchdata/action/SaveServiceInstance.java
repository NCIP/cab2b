package edu.wustl.cab2b.admin.searchdata.action;

import static edu.wustl.cab2b.admin.util.AdminConstants.ALL_SERVICE_INSTANCES;
import static edu.wustl.cab2b.admin.util.AdminConstants.FAILURE;
import static edu.wustl.cab2b.admin.util.AdminConstants.FILTERED_SERVICE_INSTANCES;
import static edu.wustl.cab2b.admin.util.AdminConstants.USER_OBJECT;
import static edu.wustl.cab2b.admin.util.AdminConstants.SELECTED_SERVICE_NAME;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.bizlogic.ServiceInstanceBizLogic;
import edu.wustl.cab2b.common.user.AdminServiceMetadata;
import edu.wustl.cab2b.common.user.User;

public class SaveServiceInstance extends BaseAction implements SessionAware, RequestAware {
    private static final long serialVersionUID = -4918500020071716455L;

    private Map session = null;

    private Map request = null;

    private String[] checkedServiceInstances = null;

    private String serviceName = null;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setRequest(final Map request) {
        this.request = request;
    }

    public Map getRequest() {
        return request;
    }

    @Override
    public String execute() {
        User user = (User) getSession().get(USER_OBJECT);

        Collection<AdminServiceMetadata> selectedServices = getSelectedServiceInstances();
        String serviceName =(String) session.get(SELECTED_SERVICE_NAME);
        try {
            user =  new ServiceInstanceBizLogic().saveServiceInstances(serviceName,selectedServices, user);
            getSession().put(USER_OBJECT,user);
            session.remove(ALL_SERVICE_INSTANCES);
            session.remove(FILTERED_SERVICE_INSTANCES);
        } catch (RemoteException e) {
            e.printStackTrace();
            return FAILURE;
        }

        return SUCCESS;
    }

    public Collection<AdminServiceMetadata> getSelectedServiceInstances() {
        List<AdminServiceMetadata> allServiceRecords = (List<AdminServiceMetadata>) getSession().get(
                                                                                                     ALL_SERVICE_INSTANCES);
        Collection<AdminServiceMetadata> selectedServiceMetaData = new HashSet<AdminServiceMetadata>();
        for (AdminServiceMetadata adminServiceMetaData : allServiceRecords) {
            String serviceURL = adminServiceMetaData.getHostingResearchCenter();
            for (String selectedServiceHost : getCheckedServiceInstances()) {
                if (serviceURL.compareTo(selectedServiceHost) == 0) {
                    selectedServiceMetaData.add(adminServiceMetaData);
                }
            }
        }

        return selectedServiceMetaData;
    }

    public void setSession(final Map session) {
        this.session = session;
    }

    public Map getSession() {
        return session;
    }

    /**
     * @return Returns the checkedServiceInstances.
     */
    public String[] getCheckedServiceInstances() {
        return checkedServiceInstances;
    }

    /**
     * @param checkedServiceInstances
     *            The checkedServiceInstances to set.
     */
    public void setCheckedServiceInstances(String[] checkedServiceInstances) {
        this.checkedServiceInstances = checkedServiceInstances;
    }

}
