package edu.wustl.cab2b.admin.searchdata.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.bizlogic.ServiceInstanceBizLogic;
import static edu.wustl.cab2b.admin.util.AdminConstants.*;

public class LoadServiceModels extends BaseAction implements ServletRequestAware, SessionAware {

    private static final long serialVersionUID = 1854118345769487660L;

    private HttpServletRequest servletRequest = null;

    private String includeDescription;

    private Map<String, Object> session = null;

    private String textbox;

    private Map request;
    
    private String action ;
        

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    public void setIncludeDescription(String includeDescription) {
        this.includeDescription = includeDescription;
    }

    public String getIncludeDescriptionChecked() {

        return this.includeDescription;

    }

    public void setTextbox(String textbox) {

        this.textbox = textbox;

    }

    public String getTextbox() {

        return this.textbox;

    }

    public void setSession(final Map session) {
        this.session = session;
    }

    public Map getSession() {

        return session;
    }

    public void setServletRequest(HttpServletRequest req) {
        this.servletRequest = req;

    }

    /**
     * @return the servletRequest
     */
    public HttpServletRequest getServletRequest() {
        return servletRequest;
    }

    /**
     * @return the request
     */
    public Map getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(Map request) {
        this.request = request;
    }

    /**
     * @return the action result type
     * This action gets all the service models and store them in session.
     */
    @Override
    public String execute() {
        List<EntityGroupInterface> searchedEntitylList = new ArrayList<EntityGroupInterface>();
        List<EntityGroupInterface> allEntityList = new ArrayList<EntityGroupInterface>();

        String offset = (String) servletRequest.getParameter(OFFSET_PARAMETER);
        String callFrom = (String) servletRequest.getParameter("callFrom");
        if (callFrom != null) {
            session.remove(ALL_LOADED_MODELS);
            session.remove(FILTERED_LOADED_MODELS);
        }
        if(action!=null)
        {
            if (session.get(ALL_LOADED_MODELS) != null)
                session.put(FILTERED_LOADED_MODELS,
                            (List<EntityGroupInterface>) session.get(ALL_LOADED_MODELS));
        }
        if (offset != null) {
            return SUCCESS;
        }
        if (textbox != null && !textbox.equals("")) {
            allEntityList = (List<EntityGroupInterface>) session.get(ALL_LOADED_MODELS);
            getSearchedModels(allEntityList, searchedEntitylList);
            session.put(FILTERED_LOADED_MODELS, searchedEntitylList);
        } else {
            if (session.get(ALL_LOADED_MODELS) != null)
                session.put(FILTERED_LOADED_MODELS,
                            (List<EntityGroupInterface>) session.get(ALL_LOADED_MODELS));
            else {
                allEntityList.addAll((Collection<EntityGroupInterface>) new ServiceInstanceBizLogic().getMetadataEntityGroups());
                session.put(ALL_LOADED_MODELS, allEntityList);
                session.put(FILTERED_LOADED_MODELS, allEntityList);
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

    public void getSearchedModels(List<EntityGroupInterface> allEntityGroup,
                                  List<EntityGroupInterface> searchedEntitylList) {
        for (EntityGroupInterface entityGroup : allEntityGroup) {
            if (searchName(entityGroup.getLongName(), textbox))
                searchedEntitylList.add(entityGroup);
        }
        if (includeDescription != null) {
            for (EntityGroupInterface entityGroup : allEntityGroup) {
                if (searchName(entityGroup.getDescription(), textbox)) {
                    if (searchedEntitylList.isEmpty()) {
                        searchedEntitylList.add(entityGroup);
                    } else {
                        if (!searchedEntitylList.contains(entityGroup))
                            searchedEntitylList.add(entityGroup);
                    }
                }
            }
        }

    }

}
