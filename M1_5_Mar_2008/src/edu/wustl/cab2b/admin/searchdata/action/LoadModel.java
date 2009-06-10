/**
 * 
 */
package edu.wustl.cab2b.admin.searchdata.action;

import java.rmi.RemoteException;
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
import edu.wustl.cab2b.admin.util.Cab2bConstants;
import edu.wustl.cab2b.server.cache.EntityCache;

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

        boolean flg = false;
        List<String> ListOfModelsToLoad = new ArrayList<String>();
        try {

            try {

                ListOfModelsToLoad = getAllModelNames();
                unloadedModel = caDSR.persistDomainModel(ListOfModelsToLoad);

            } catch (RemoteException re) {
                re.printStackTrace();

            } catch (Exception re) {
                re.printStackTrace();

            } finally {
                if (!unloadedModel.isEmpty()) {

                }

            }

            for (String i : ListOfModelsToLoad) {
                String onlyName = i.substring(i.indexOf("|") + 1);
                if (unloadedModel.containsKey(onlyName)) {
                    modelsVsStatus.put(i, Cab2bConstants.FAILED);
                } else {
                    loadedModel.add(i);
                    modelsVsStatus.put(i, Cab2bConstants.PASS);
                    flg = true;
                }
            }
            flag = true;
            if (flg) {
                getSession().remove("load");
                getSession().remove("allModelLsit");
                EntityCache.getInstance().refreshCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        List<String> ListOfModelsToLoad = new ArrayList<String>();
        try {
            Map<String, List<String>> allSelectedModelList = (Map<String, List<String>>) getSession().get(
                                                                                                          Cab2bConstants.ALL_SELECTED_MODELS_LIST);
            if (allSelectedModelList != null) {
                for (String key : allSelectedModelList.keySet()) {
                    List<String> modelNameList = (List<String>) allSelectedModelList.get(key);
                    for (String modelName : modelNameList)
                        ListOfModelsToLoad.add(modelName);
                }
            }
            ListOfModelsToLoad.addAll(Arrays.asList(getCheckboxValues()));
        } catch (Exception e) {

        }
        return ListOfModelsToLoad;
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
        String error = "Details not avaliable.";
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
        for (String modelName : modelsVsStatus.keySet()) {

            result = result + ";" + modelName + "|" + modelsVsStatus.get(modelName);
            /* if (!Cab2bConstants.IN_PROGRESS.equalsIgnoreCase(modelsVsStatus.get(modelName))) {
                 modelsVsStatus.remove(modelName);
             }*/
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
                                                                                                          Cab2bConstants.ALL_SELECTED_MODELS_LIST);
            if (allSelectedModelList != null) {
                for (String key : allSelectedModelList.keySet()) {

                    List<String> modelNameList = (List<String>) allSelectedModelList.get(key);
                    for (String modelName : modelNameList)
                        modelsVsStatus.put(modelName, Cab2bConstants.IN_PROGRESS);
                    getSession().remove(key);
                }
            }
            if (checkboxValues != null) {
                for (String modelName : getCheckboxValues())
                    modelsVsStatus.put(modelName, Cab2bConstants.IN_PROGRESS);
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
        } else if (Cab2bConstants.LOAD_MODEL_STATUS_ACTION.equalsIgnoreCase(getAction())) {
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

                String details = getMoreDetails(getModelName());
                getRequest().setAttribute(Cab2bConstants.LOAD_MODEL_DETAILS_ACTION, details);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Cab2bConstants.LOAD_MODEL_DETAILS_ACTION;
        }
        return Cab2bConstants.SUCCESS;
    }

}
