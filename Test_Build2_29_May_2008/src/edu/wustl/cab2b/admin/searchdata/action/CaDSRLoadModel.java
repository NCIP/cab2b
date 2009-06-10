/**
 * 
 */
package edu.wustl.cab2b.admin.searchdata.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.beans.CaDSRModelDetailsBean;
import edu.wustl.cab2b.admin.bizlogic.CaDSRModelBizLogic;
import static edu.wustl.cab2b.admin.util.AdminConstants.*;

public class CaDSRLoadModel extends BaseAction implements ServletRequestAware, SessionAware {

    private static final long serialVersionUID = 1L;

    private List<CaDSRModelDetailsBean> allModelList = new ArrayList<CaDSRModelDetailsBean>();

    private String[] checkboxValues;

    private String includeDescription;

    private final List<CaDSRModelDetailsBean> searchedModelList = new ArrayList<CaDSRModelDetailsBean>();

    private HttpServletRequest servletRequest = null;

    private Map<String, Object> session = null;

    private String textbox;

    @Override
    public String execute() {
        Map<String, List<String>> allSelectedModelList = (Map<String, List<String>>) this.session.get(ALL_SELECTED_MODELS_LIST);
        if (allSelectedModelList == null) {
            allSelectedModelList = new HashMap<String, List<String>>();
        }

        final String offset = this.servletRequest.getParameter(OFFSET_PARAMETER);
        if (offset != null) {//This block runs when user selected some model and goes to next page
            if (getCheckboxValues() != null) {
                final List<String> selectedModels = new ArrayList<String>(getCheckboxValues().length);
                for (final String chkbox : getCheckboxValues()) {
                    selectedModels.add(chkbox);
                }

                final String pageNO = this.servletRequest.getParameter("pageNO");
                allSelectedModelList.put(pageNO, selectedModels);
                this.session.put(ALL_SELECTED_MODELS_LIST, allSelectedModelList);
                this.session.put(pageNO, selectedModels);
            }

            return SUCCESS;
        }
        /**
         * 
         */
        if (this.textbox != null && !this.textbox.equals("")) {//This if code for search model
            this.allModelList = (List<CaDSRModelDetailsBean>) this.session.get(ALL_AVAILABLE_MODELS_TO_LOAD);
            this.session.put(FILTERED_AVAILABLE_MODELS_TO_LOAD, getSearchedModels(this.allModelList));
        } else {
            if (this.session.get(ALL_AVAILABLE_MODELS_TO_LOAD) != null) {
                this.session.put(FILTERED_AVAILABLE_MODELS_TO_LOAD, this.session.get(ALL_AVAILABLE_MODELS_TO_LOAD));
            } else {
                try {
                    this.allModelList = new CaDSRModelBizLogic().getProjectsDisplayDetails();
                } catch (final RemoteException e) {
                    e.printStackTrace();
                    return FAILURE;
                }
                this.session.put(ALL_AVAILABLE_MODELS_TO_LOAD, this.allModelList);
                this.session.put(FILTERED_AVAILABLE_MODELS_TO_LOAD, this.allModelList);
            }
        }

        return SUCCESS;
    }

    /**
     * @return the checkboxValues
     */
    public String[] getCheckboxValues() {
        return this.checkboxValues;
    }

    public String getIncludeDescriptionChecked() {
        return this.includeDescription;
    }

    /**
     * This method returns list of the models which matches with the user search string
     * @param allModels
     * @return
     */
    public List<CaDSRModelDetailsBean> getSearchedModels(List<CaDSRModelDetailsBean> allModels) {
        for (final CaDSRModelDetailsBean caDSR : allModels) {
            if (searchName(caDSR.getLongName(), this.textbox)) {
                this.searchedModelList.add(caDSR);
            }
        }

        if (this.includeDescription != null) {
            for (final CaDSRModelDetailsBean caDSR : allModels) {
                if (searchName(caDSR.getDescription(), this.textbox)) {
                    if (this.searchedModelList.isEmpty()) {
                        this.searchedModelList.add(caDSR);
                    } else if (!this.searchedModelList.contains(caDSR)) {
                        this.searchedModelList.add(caDSR);
                    }
                }
            }
        }

        return this.searchedModelList;
    }

    /**
     * @return the servletRequest
     */
    public HttpServletRequest getServletRequest() {
        return this.servletRequest;
    }

    /**
     * 
     * @return
     */
    public Map getSession() {
        return this.session;
    }

    /**
     * 
     * @return
     */
    public String getTextbox() {
        return this.textbox;
    }

    /**
     * This method search the user search string in model name
     * @param modelName
     * @param searchName
     * @return
     */
    public boolean searchName(String modelName, String searchName) {
        final StringTokenizer tokenizer = new StringTokenizer(modelName);
        int counter = 0;

        while (tokenizer.hasMoreTokens()) {
            if (tokenizer.nextToken().toLowerCase().contains(searchName.toLowerCase())) {
                counter++;
            }
        }

        boolean isPresent = false;
        if (counter > 0) {
            isPresent = true;
        }

        return isPresent;
    }

    /**
     * @param checkboxValues the checkboxValues to set
     */
    public void setCheckboxValues(String[] checkboxValues) {
        this.checkboxValues = checkboxValues;
    }

    /**
     * 
     * @param includeDescription
     */
    public void setIncludeDescription(String includeDescription) {
        this.includeDescription = includeDescription;
    }

    /**
     * 
     */
    public void setServletRequest(HttpServletRequest req) {
        this.servletRequest = req;
    }

    /**
     * 
     */
    public void setSession(final Map session) {
        this.session = session;
    }

    /**
     * 
     * @param textbox
     */
    public void setTextbox(String textbox) {
        this.textbox = textbox;
    }

}