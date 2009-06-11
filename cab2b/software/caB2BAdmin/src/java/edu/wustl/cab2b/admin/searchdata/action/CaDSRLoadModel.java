/**
 * 
 */
package edu.wustl.cab2b.admin.searchdata.action;

import static edu.wustl.cab2b.admin.util.AdminConstants.ALL_AVAILABLE_MODELS_TO_LOAD;
import static edu.wustl.cab2b.admin.util.AdminConstants.ALL_SELECTED_MODELS_LIST;
import static edu.wustl.cab2b.admin.util.AdminConstants.FAILURE;
import static edu.wustl.cab2b.admin.util.AdminConstants.FILTERED_AVAILABLE_MODELS_TO_LOAD;
import static edu.wustl.cab2b.admin.util.AdminConstants.OFFSET_PARAMETER;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.beans.CaDSRModelDetailsBean;
import edu.wustl.cab2b.admin.bizlogic.CaDSRModelBizLogic;
import edu.wustl.cab2b.admin.util.AdminConstants;
import edu.wustl.cab2b.common.authentication.util.CagridPropertyLoader;

/**
 * 
 * @author lalit_chand, atul_jawale
 * 
 */
public class CaDSRLoadModel extends BaseAction {
    private static final long serialVersionUID = 1L;

    private List<CaDSRModelDetailsBean> allModelList = new ArrayList<CaDSRModelDetailsBean>();

    private String[] checkboxValues;

    private String includeDescription;

    private final List<CaDSRModelDetailsBean> searchedModelList = new ArrayList<CaDSRModelDetailsBean>();

    private String textbox;

    /**
     * Overrides {@link CaDSRLoadModel#execute()}
     * 
     * @return
     * 
     */
    public String execute() {
        Map<String, List<String>> allSelectedModelList =
                (Map<String, List<String>>) this.session.get(ALL_SELECTED_MODELS_LIST);
        if (allSelectedModelList == null) {
            allSelectedModelList = new HashMap<String, List<String>>();
        }

        final String offset = this.request.getParameter(OFFSET_PARAMETER);
        if (offset != null) {// This block runs when user selected some model
            // and goes to next page
            if (getCheckboxValues() != null) {
                final List<String> selectedModels = new ArrayList<String>(getCheckboxValues().length);
                for (final String chkbox : getCheckboxValues()) {
                    selectedModels.add(chkbox);
                }

                final String pageNO = this.request.getParameter("pageNO");
                allSelectedModelList.put(pageNO, selectedModels);
                this.session.put(ALL_SELECTED_MODELS_LIST, allSelectedModelList);
                this.session.put(pageNO, selectedModels);
            }

            return SUCCESS;
        }

        if (this.textbox != null && !this.textbox.equals("")) { // This code is
            // for search
            // model
            this.allModelList = (List<CaDSRModelDetailsBean>) this.session.get(ALL_AVAILABLE_MODELS_TO_LOAD);
            this.session.put(FILTERED_AVAILABLE_MODELS_TO_LOAD, getSearchedModels(this.allModelList));
        } else {
            if (this.session.get(ALL_AVAILABLE_MODELS_TO_LOAD) != null) {
                this.session
                    .put(FILTERED_AVAILABLE_MODELS_TO_LOAD, this.session.get(ALL_AVAILABLE_MODELS_TO_LOAD));
            } else {
                if (toFetch()) {
                    try {
                        synchronized (this) {
                            boolean isListModelNull =
                                    servletContext.getAttribute(AdminConstants.LIST_OF_MODELS) == null ? true
                                            : false;
                            if (isListModelNull) {
                                this.allModelList = new CaDSRModelBizLogic().getProjectsDisplayDetails();
                                servletContext.setAttribute(AdminConstants.LIST_OF_MODELS, this.allModelList);
                            }
                        }
                    } catch (final RemoteException e) {
                        e.printStackTrace();
                        return FAILURE;
                    }
                } else {
                    this.allModelList =
                            (List<CaDSRModelDetailsBean>) servletContext
                                .getAttribute(AdminConstants.LIST_OF_MODELS);
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

    /**
     * Returns includeDescription
     * 
     * @return
     */
    public String getIncludeDescriptionChecked() {
        return this.includeDescription;
    }

    /**
     * This method returns list of the models which matches with the user search
     * string
     * 
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

    private boolean toFetch() {
        Long currentTime = System.currentTimeMillis();
        Long prevTime = (Long) servletContext.getAttribute(AdminConstants.TIME_OF_MODEL_FETCHED);
        long refreshTime = Long.parseLong(CagridPropertyLoader.getCaDSRRefreshTime());
        boolean isListModelNull =
                servletContext.getAttribute(AdminConstants.LIST_OF_MODELS) == null ? true : false;
        if (prevTime == null || (currentTime - prevTime) >= refreshTime || isListModelNull) {
            servletContext.setAttribute(AdminConstants.TIME_OF_MODEL_FETCHED, new Long(currentTime));
            return true;
        }

        return false;
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
     * 
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
     * @param checkboxValues
     *            the checkboxValues to set
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
     * @param textbox
     */
    public void setTextbox(String textbox) {
        this.textbox = textbox;
    }

}