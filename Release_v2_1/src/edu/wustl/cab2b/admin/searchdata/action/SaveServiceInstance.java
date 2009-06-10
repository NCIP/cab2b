package edu.wustl.cab2b.admin.searchdata.action;

import static edu.wustl.cab2b.admin.util.AdminConstants.ALL_SERVICE_INSTANCES;
import static edu.wustl.cab2b.admin.util.AdminConstants.FAILURE;
import static edu.wustl.cab2b.admin.util.AdminConstants.FILTERED_SERVICE_INSTANCES;
import static edu.wustl.cab2b.admin.util.AdminConstants.SELECTED_SERVICE_NAME;
import static edu.wustl.cab2b.admin.util.AdminConstants.USER_OBJECT;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.bizlogic.ServiceInstanceBizLogic;
import edu.wustl.cab2b.common.user.AdminServiceMetadata;
import edu.wustl.cab2b.common.user.User;

/**
 * 
 * @author lalit_chand, chetan_patil, atul_jawale
 * 
 */
public class SaveServiceInstance extends BaseAction {
    private static final long serialVersionUID = -4918500020071716455L;

    private String[] checkedServiceInstances = null;

    private String serviceName = null;

    /**
     * Returns service name
     * 
     * @return
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    @Override
    /**
     * Main method gets called from Ajax call
     * @return
     * @throws IOException
     */
    public String execute() {
        User user = (User) getSession().get(USER_OBJECT);

        Collection<AdminServiceMetadata> selectedServices = getSelectedServiceInstances();
        
        try {
            user = new ServiceInstanceBizLogic().saveServiceInstances(serviceName, selectedServices, user);
            getSession().put(USER_OBJECT, user);
            session.remove(ALL_SERVICE_INSTANCES);
            session.remove(FILTERED_SERVICE_INSTANCES);
        } catch (RemoteException e) {
            e.printStackTrace();
            return FAILURE;
        }

        return SUCCESS;
    }

    /**
     * returnsd selectedServices instances
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public Collection<AdminServiceMetadata> getSelectedServiceInstances() {

        List<AdminServiceMetadata> allServiceRecords = null;
        allServiceRecords = (List<AdminServiceMetadata>) getSession().get(ALL_SERVICE_INSTANCES);
        serviceName =  allServiceRecords.iterator().next().getServiceURLObject().getEntityGroupName();
        String pageNo = request.getParameter("pageNO");
        Map<String, List<String>> pageVsURLs = null;
        if (session.get("SelectedInstanceMap") != null) {
            pageVsURLs = (Map<String, List<String>>) session.get("SelectedInstanceMap");
        } else {
            pageVsURLs = new HashMap<String, List<String>>();
        }
        pageVsURLs.put(pageNo, Arrays.asList(checkedServiceInstances));
        Collection<AdminServiceMetadata> selectedServiceMetaData = new HashSet<AdminServiceMetadata>();
        for (AdminServiceMetadata adminServiceMetaData : allServiceRecords) {
            String serviceURL = adminServiceMetaData.getServiceURL();
            for (String pages : pageVsURLs.keySet()) {
                Collection<String> list = pageVsURLs.get(pages);
                for (String url : list) {
                    if (serviceURL.compareTo(url) == 0) {
                        selectedServiceMetaData.add(adminServiceMetaData);
                    }
                }
            }
        }
        return selectedServiceMetaData;
    }

}
