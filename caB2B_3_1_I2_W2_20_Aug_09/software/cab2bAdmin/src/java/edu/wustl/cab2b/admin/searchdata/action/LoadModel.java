/**
 * 
 */
package edu.wustl.cab2b.admin.searchdata.action;

import static edu.wustl.cab2b.admin.util.AdminConstants.ALL_AVAILABLE_MODELS_TO_LOAD;
import static edu.wustl.cab2b.admin.util.AdminConstants.ALL_SELECTED_MODELS_LIST;
import static edu.wustl.cab2b.admin.util.AdminConstants.FILTERED_AVAILABLE_MODELS_TO_LOAD;
import static edu.wustl.cab2b.admin.util.AdminConstants.IN_PROGRESS;
import static edu.wustl.cab2b.admin.util.AdminConstants.LIST_OF_MODELS;
import static edu.wustl.cab2b.admin.util.AdminConstants.LOAD_MODEL_DETAILS_ACTION;
import static edu.wustl.cab2b.admin.util.AdminConstants.LOAD_MODEL_STATUS_ACTION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.beans.CaDSRModelDetailsBean;
import edu.wustl.cab2b.admin.beans.LoadModelResult;
import edu.wustl.cab2b.admin.bizlogic.CaDSRModelBizLogic;
import edu.wustl.cab2b.server.serviceurl.IndexServiceOperations;

/**
 * @author atul_jawale
 * 
 */
public class LoadModel extends BaseAction {

    private final Logger logger = Logger.getLogger(LoadModel.class);

    private static final long serialVersionUID = -1930745343729436866L;

    private String[] checkboxValues;

    private String action = null;

    private CaDSRModelBizLogic caDSR;

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action
     *            the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * This method will load all models one by one
     * 
     * @return map of modelName Vs LoadModelResult
     */
    public Map<String, LoadModelResult> load() {
        Collection<CaDSRModelDetailsBean> modelsToLoad =
                (Collection<CaDSRModelDetailsBean>) session.get("ListOfUserModels");
        Map<String, LoadModelResult> loadModelResultList = caDSR.persistDomainModel(modelsToLoad);

        for (CaDSRModelDetailsBean caDSRModelDetails : modelsToLoad) {
            IndexServiceOperations.refreshServiceURLsMetadata(caDSRModelDetails.getLongName(), caDSRModelDetails
                .getVersion());
        }
        session.remove("ListOfUserModels");
        session.remove(ALL_AVAILABLE_MODELS_TO_LOAD);
        session.remove(FILTERED_AVAILABLE_MODELS_TO_LOAD);
        removeLoadedModelsFromList(loadModelResultList);

        return loadModelResultList;
    }

    /**
     * This method gets List of CaDSRModelDetailsBean from servletcontext and
     * removes the models which are loaded successfully in the system.
     * 
     * @param loadModelResultList
     */
    public void removeLoadedModelsFromList(Map<String, LoadModelResult> loadModelResultList) {
        List<CaDSRModelDetailsBean> allModelList =
                (List<CaDSRModelDetailsBean>) servletContext.getAttribute(LIST_OF_MODELS);
        Collection<LoadModelResult> loadModelResults = loadModelResultList.values();
        for (LoadModelResult loadModelResult : loadModelResults) {
            CaDSRModelDetailsBean cadsrbean = loadModelResult.getModelDetails();
            if (loadModelResult.isLoaded() && allModelList.contains(cadsrbean)) {
                allModelList.remove(cadsrbean);
            }
        }
        servletContext.setAttribute(LIST_OF_MODELS, allModelList);
    }

    /**
     * @return the checkboxValues
     */
    public String[] getCheckboxValues() {
        return checkboxValues;
    }

    /**
     * This method fetch all the model names from request and session, selected
     * by user for loading.
     * 
     * @return the list of all the models user selected for loading
     */
    @SuppressWarnings("unchecked")
    private Collection<CaDSRModelDetailsBean> getAllModelNames() {
        Collection<CaDSRModelDetailsBean> listOfModelsToLoad = new HashSet<CaDSRModelDetailsBean>();
        List<CaDSRModelDetailsBean> allAvaliableModelsList =
                (List<CaDSRModelDetailsBean>) session.get(ALL_AVAILABLE_MODELS_TO_LOAD);
        Map<String, List<String>> allSelectedModelList =
                (Map<String, List<String>>) session.get(ALL_SELECTED_MODELS_LIST);
        if (allSelectedModelList != null) {
            for (String key : allSelectedModelList.keySet()) {
                List<String> selectedModelNameList = (List<String>) allSelectedModelList.get(key);
                listOfModelsToLoad.addAll(getCaDSRModelDetailsBean(selectedModelNameList, allAvaliableModelsList));
            }
        }

        // Fetch all selected checkbox from requests
        List<String> selectedModels = Arrays.asList(getCheckboxValues());
        listOfModelsToLoad.addAll(getCaDSRModelDetailsBean(selectedModels, allAvaliableModelsList));

        return listOfModelsToLoad;
    }

    /**
     * 
     */
    private Collection<CaDSRModelDetailsBean> getCaDSRModelDetailsBean(
                                                                       List<String> selectedModelNames,
                                                                       List<CaDSRModelDetailsBean> allAvaliableModels) {
        Collection<CaDSRModelDetailsBean> listOfModelsToLoad = new ArrayList<CaDSRModelDetailsBean>();
        for (String modelName : selectedModelNames) {
            for (CaDSRModelDetailsBean bean : allAvaliableModels) {
                if (modelName.equals(bean.getLongName() + " v" + bean.getVersion())) {
                    listOfModelsToLoad.add(bean);
                }
            }
        }

        return listOfModelsToLoad;
    }

    /**
     * @param checkboxValues
     *            the checkboxValues to set
     */
    public void setCheckboxValues(String[] checkboxValues) {
        this.checkboxValues = checkboxValues;
    }

    /**
     * This method returns the status of the model in loading the model
     * 
     * @param loadModelResultMap
     * @return string with modelName+'|'+status separated by ';'
     */
    public String getModelStatus(Map<String, LoadModelResult> loadModelResultMap) {
        StringBuffer result = new StringBuffer();
        for (String modelName : loadModelResultMap.keySet()) {
            LoadModelResult model = loadModelResultMap.get(modelName);
            result.append(";" + model.getModelDetails().getLongName() + "|" + model.isLoaded() + "|"
                    + model.getErrorMessage());
        }

        return result.toString();
    }

    /**
     * Overiding {@link String#toString()}
     * 
     * @return
     */
    public String execute() {
        Map<String, String> modelsVsStatus = new HashMap<String, String>();
        if (getAction() == null) {
            Map<String, List<String>> allSelectedModelList =
                    (Map<String, List<String>>) getSession().get(ALL_SELECTED_MODELS_LIST);
            if (allSelectedModelList != null) {
                for (String key : allSelectedModelList.keySet()) {
                    List<String> modelNameList = (List<String>) allSelectedModelList.get(key);
                    for (String modelName : modelNameList) {
                        modelsVsStatus.put(modelName, IN_PROGRESS);
                    }
                    session.remove(key);
                }
            }

            if (checkboxValues != null) {
                for (String modelName : getCheckboxValues()) {
                    modelsVsStatus.put(modelName, IN_PROGRESS);
                }
            }

            Collection<CaDSRModelDetailsBean> modelsToLoad = getAllModelNames();
            session.put("ListOfUserModels", modelsToLoad);
        } else if (LOAD_MODEL_STATUS_ACTION.equalsIgnoreCase(action)) {
            try {
                caDSR = new CaDSRModelBizLogic();
                Map<String, LoadModelResult> loadModelResultMap = load();
                session.put("loadModelResultList", loadModelResultMap);
                response.setContentType("text/html");
                response.getWriter().write(getModelStatus(loadModelResultMap));
                modelsVsStatus.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        } else if (LOAD_MODEL_DETAILS_ACTION.equalsIgnoreCase(action)) {
            return LOAD_MODEL_DETAILS_ACTION;
        }

        return SUCCESS;
    }

}
