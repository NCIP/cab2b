/**
 * 
 */
package edu.wustl.cab2b.admin.searchdata.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.bizlogic.CaDSRModelBizLogic;
import static edu.wustl.cab2b.admin.util.AdminConstants.*;

/**
 * @author atul_jawale
 *
 */
public class LoadModel extends BaseAction implements ServletRequestAware, ServletResponseAware, SessionAware,
        Runnable {

    private static final long serialVersionUID = -1930745343729436866L;

    private String[] checkboxValues;

    private HttpServletRequest request = null;

    private static Map<String, String> unloadedModel = Collections.synchronizedMap(new HashMap<String, String>());

    private Map<String, Object> session;
    
    private static Map<String,Throwable> modelVsException = Collections.synchronizedMap(new HashMap<String, Throwable>());

    private List<String> loadedModel;

    private String action = null;

    private String modelName = null;

    private HttpServletResponse response;

    private static Map<String, String> modelsVsStatus = new HashMap<String, String>();

    public HttpServletRequest getRequest() {

        return request;
    }

    private boolean flag;

    private CaDSRModelBizLogic caDSR;

    public void setServletRequest(HttpServletRequest req) {
        this.request = req;
    }

    public void setServletResponse(HttpServletResponse res) {
        this.response = res;

    }

    /**
     * @return the flag
     */
    public boolean isFlag() {
        return flag;
    }

    /**
     * @param flag the flag to set
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setSession(Map session) {
        this.session = session;

    }

    /**
     * @return the session
     */
    public Map<String, Object> getSession() {
        return session;
    }

    /**
     * @return the modelName
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * @param modelName the modelName to set
     */
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

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

    public void run() {
        List<String> modelsToLoad = getAllModelNames();
        unloadedModel = caDSR.persistDomainModel(modelsToLoad,modelVsException);
        for (String modelName : modelsToLoad) {
            String onlyName = modelName.substring(modelName.indexOf("|") + 1);
            if (unloadedModel.containsKey(onlyName)) {
                modelsVsStatus.put(modelName, FAILED);
            } else {
                loadedModel.add(modelName);
                modelsVsStatus.put(modelName, PASS);
            }
        }
        getSession().remove(ALL_AVAILABLE_MODELS_TO_LOAD);
        getSession().remove(FILTERED_AVAILABLE_MODELS_TO_LOAD);

    }

    /**
     * @return the checkboxValues
     */
    public String[] getCheckboxValues() {

        return checkboxValues;
    }

    /**
     * This method fetch all the model names from request and session, user selected for loading. 
     * @return the list of all the models user selected for loading
     */
    private List<String> getAllModelNames() {
        List<String> listOfModelsToLoad = new ArrayList<String>();
        try {
            Map<String, List<String>> allSelectedModelList = (Map<String, List<String>>) getSession().get(
                                                                                                          ALL_SELECTED_MODELS_LIST);
            if (allSelectedModelList != null) {
                for (String key : allSelectedModelList.keySet()) {
                    List<String> modelNameList = (List<String>) allSelectedModelList.get(key);
                    for (String modelName : modelNameList)
                        listOfModelsToLoad.add(modelName);
                }
            }
            listOfModelsToLoad.addAll(Arrays.asList(getCheckboxValues()));
        } catch (Exception e) {

        }
        return listOfModelsToLoad;
    }

    /**
     * @param checkboxValues the checkboxValues to set
     */
    public void setCheckboxValues(String[] checkboxValues) {
        this.checkboxValues = checkboxValues;
    }

    /**
     * This method returns the exception thrown while loading the model
     * @param modelName name of the model whose details to view
     * @return exception message for that model
     */
    public String getMoreDetails(String modelName) {
        String error = "";
        if (unloadedModel != null && unloadedModel.containsKey(modelName)) {
            error = unloadedModel.get(modelName);
        }
        return error;
    }

    /**
     * This method returns the status of the model in loading the model 
     * @returns string with modelName+'|'+status separated by ';' 
     */
    public String getModelStatus() {
        String result = "";
        String error = "";
        List<String> listOfModelsTODelete = new ArrayList<String>();
        for (String modelName : modelsVsStatus.keySet()) {

            result = result + ";" + modelName + "|" + modelsVsStatus.get(modelName);
            if (unloadedModel != null && unloadedModel.containsKey(modelName)) {
                result = result + "|" + unloadedModel.get(modelName);
            }
            if (!IN_PROGRESS.equalsIgnoreCase(modelsVsStatus.get(modelName))) {
                listOfModelsTODelete.add(modelName);
            }
        }
        for (String modelName : listOfModelsTODelete) {
            modelsVsStatus.remove(modelName);
        }
        return result;
    }

    /**
     * 
     * @return
     */
    @Override
    public String execute() {

        if (modelsVsStatus == null) {
            modelsVsStatus = new HashMap<String, String>();
        }
        if (getAction() == null) {
            Map<String, List<String>> allSelectedModelList = (Map<String, List<String>>) getSession().get(
                                                                                                          ALL_SELECTED_MODELS_LIST);
            if (allSelectedModelList != null) {
                for (String key : allSelectedModelList.keySet()) {

                    List<String> modelNameList = (List<String>) allSelectedModelList.get(key);
                    for (String modelName : modelNameList)
                        modelsVsStatus.put(modelName, IN_PROGRESS);
                    getSession().remove(key);
                }
            }
            if (checkboxValues != null) {
                for (String modelName : getCheckboxValues())
                    modelsVsStatus.put(modelName, IN_PROGRESS);
            }
            getRequest().setAttribute("modelStatus", modelsVsStatus);
            loadedModel = new ArrayList<String>();
            caDSR = new CaDSRModelBizLogic();
            try {
                Thread t = new Thread(this);

                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (LOAD_MODEL_STATUS_ACTION.equalsIgnoreCase(getAction())) {
            try {
                response.setContentType("text/html");
                response.getWriter().write(getModelStatus());
                modelsVsStatus.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else {
            try {

                String details = getMoreDetails(modelName);
                getRequest().setAttribute(LOAD_MODEL_DETAILS_ACTION, details);
                getRequest().setAttribute(LOAD_MODEL_EXCEPTION, modelVsException.get(modelName));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return LOAD_MODEL_DETAILS_ACTION;
        }
        return SUCCESS;
    }

}
