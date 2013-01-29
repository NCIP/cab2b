/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.authentication.Authenticator;
import edu.wustl.cab2b.common.authentication.exception.AuthenticationException;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.modelgroup.ModelGroupOperations;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.cab2bwebapp.bizlogic.ApplicationBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.QueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.TransformedResultObjectWithContactInfo;
import edu.wustl.cab2bwebapp.constants.Constants;

/**
 * JSON REST API for conducting simple keyword searches and returning metadata 
 * in caB2B.
 * 
 * @author <a href="mailto:rokickik@mail.nih.gov">Konrad Rokicki</a>
 */
public class JSONRESTService extends HttpServlet {

    private static Logger log = Logger.getLogger(JSONRESTService.class);

    private static final String GLOBUS_USERNAME = "gssuser";
    private static final String GLOBUS_PASSWORD = "GSS#pswd12";

    /**
     * Handles Get requests.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        PrintWriter pw = new PrintWriter(response.getOutputStream());
        response.setContentType("application/json");
        
        try {
         
            GlobusCredential globusCredential = null;
            try {
                globusCredential = new Authenticator(GLOBUS_USERNAME).validateUser(GLOBUS_PASSWORD);
            } catch (AuthenticationException e) {
                log.error("Could not login to Globus",e);
                // Log error but proceed with null credentials so that users 
                // can access non-secure data at least
            }
            
            // The user must be in the database because QueryStatus will be linked to it later
            UserOperations userOperations = new UserOperations();
            UserInterface globusUser = userOperations.getUserByName(globusCredential.getIdentity());
            if (globusUser == null) {
                log.info("Adding user: "+globusCredential.getIdentity());
                globusUser = userOperations.insertUser(new User(globusCredential.getIdentity(), null, false));
            }
            
            String result = "";
            String path = request.getPathInfo();
            if (path == null) {
                result = getJSONUsage(globusCredential);
            }
            else {
                String[] pathList = path.split("/");
                if (pathList.length < 2) {
                    result = getJSONUsage(globusCredential);
                }
                else {
                    String noun = pathList[1];   
                    if ("search".equals(noun)) {
                        result = handleSearch(request, globusCredential);
                    }
                    else if ("services".equals(noun)) {
                        result = handleService(request, globusCredential);
                    }
                    else {
                        result = getJSONError("UsageError", "Unrecognized noun '"+noun+"'");
                    }
                }
            }
    
            pw.print(result);
        }
        catch (Exception e) {
            log.error("Error",e);
            pw.println(getJSONError(e.getClass().getName(), e.getMessage()));
        }
        finally {
            pw.close();
        }
    }

    /**
     * Handles Post requests by calling doGet.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    /**
     * Unload servlet.
     */
    @Override
    public void destroy() {
        super.destroy();
    }
    
    /**
     * Handles REST requests and returns JSON.
     */
    private String handleService(HttpServletRequest request, 
            GlobusCredential globusCredential) throws Exception {
        
        String[] modelGroupNames = getModelGroups(globusCredential);

        UserInterface user = new UserOperations().getUserByName(Constants.ANONYMOUS);
        
        UserInterface admin = new UserOperations().getAdmin();
        Set<ServiceURLInterface> adminServices = new HashSet(admin.getServiceURLCollection());
        
        JSONObject json = new JSONObject();
        
        for (String modelGroupName : modelGroupNames) {
            
            JSONArray jsonArray = new JSONArray();
            json.put(modelGroupName, jsonArray);
            
            List<ServiceURLInterface> urls = 
                new ApplicationBizLogic().getApplicationInstances(user, modelGroupName);
            
            for(ServiceURLInterface url : urls) {
                JSONObject urlJson = new JSONObject();
                urlJson.put("url",url.getUrlLocation());
                if (adminServices.contains(url)) {
                    urlJson.put("searchDefault","true");
                }
                jsonArray.put(urlJson);
            }
        }
        
        return json.toString();
    }
    
    /**
     * Handles REST requests and returns JSON.
     */
    private String handleSearch(HttpServletRequest request, 
            GlobusCredential globusCredential) throws Exception {

        final String searchString = request.getParameter("searchString");
        final String modelGroup = request.getParameter("modelGroup");
        
        final Set<String> serviceUrlSet = new HashSet<String>();
        for(String url : request.getParameterValues("serviceUrl")) {
            serviceUrlSet.add(url.trim());
        }
        
        SavedQueryBizLogic savedQueryBizLogic = new SavedQueryBizLogic();
        
        if (searchString == null || "".equals(searchString)) {
            return getJSONUsage(globusCredential);
        }
        
        // Which model group are we querying?
        
        String[] modelGroupNames = null;
        if (modelGroup == null || "".equals(modelGroup)) {
            if (serviceUrlSet.isEmpty()) {
                // User must specify either serviceUrl or modelGroup
                return getJSONUsage(globusCredential);
            }
            // Use all model groups for now. We can infer a single one later 
            // based on the serviceUrls.
            modelGroupNames = getModelGroups(globusCredential);
        }
        else {
            if (!verifyModelGroup(modelGroup, globusCredential)) {
                return getJSONError("InvalidDataType",
                    "'"+modelGroup+"' is not a valid model group");
            }
            // User specified a single, valid model group
            modelGroupNames = new String[1];
            modelGroupNames[0] = modelGroup;
        }

        // Infer a single model group, if the user did not specify one
        
        String inferredModelGroup = null;
        
        // Create a temporary user 
        
        UserInterface user = new UserOperations().getUserByName(Constants.ANONYMOUS);
        
        // Configure the service urls that we will query
        
        Collection<ServiceURLInterface> userUrls = user.getServiceURLCollection();
        for (String modelGroupName : modelGroupNames) {
            
            List<ServiceURLInterface> urls = 
                new ApplicationBizLogic().getApplicationInstances(user, modelGroupName);
            
            if (!serviceUrlSet.isEmpty()) {
                // The user specified services to be queried
                for (ServiceURLInterface url : urls) {
                    if (serviceUrlSet.contains(url.getUrlLocation())) {
                        // Found a specified service
                        userUrls.add(url);
                        inferredModelGroup = modelGroupName;
                    }
                }
            }
            else {
                // Just query all the services in the model group
                userUrls.addAll(urls);
            }
        }

        // Did we have to infer a model group?
        
        if (inferredModelGroup != null) {
            modelGroupNames = new String[1];
            modelGroupNames[0] = inferredModelGroup;
        }

        if (userUrls.isEmpty()) {
            return getJSONError("UnknownServiceURLs",
                "No known caGrid service URLs were provided");
        }
        
        Long queryId =
                savedQueryBizLogic.getKeywordQueryId(modelGroupNames[0]);
        ICab2bQuery query = savedQueryBizLogic.getQueryById(queryId);
        
        log.info("REST Search - initializing");
        log.info("REST Search - search string: "+searchString);
        log.info("REST Search - model group: "+modelGroupNames[0]);
        for (ServiceURLInterface url : userUrls) {
            log.info("REST Search - url: "+url.getUrlLocation());
        }
        
        QueryBizLogic queryBizLogic = new QueryBizLogic(query, null, 
            searchString, user, globusCredential, modelGroupNames);

        log.info("REST Search - executing");
        Thread.sleep(200);
        while (!queryBizLogic.isProcessingFinished()) {
            Thread.sleep(100); 
        }
        log.info("REST Search - complete");
        
        // Construct result object
        
        JSONObject json = getSearchResultsJSON(queryBizLogic, userUrls);
        json.put("modelGroupName",modelGroupNames[0]);

        return json.toString();
    }

    /** 
     * Create and return a JSON object with the search results.
     * @param searchResults
     * @return
     * @throws JSONException
     */
    private JSONObject getSearchResultsJSON(QueryBizLogic queryBizLogic, 
            Collection<ServiceURLInterface> userUrls) throws Exception {

        final Map<ICab2bQuery, TransformedResultObjectWithContactInfo> searchResults = 
            queryBizLogic.getSearchResults();
        
        final Collection<ServiceURLInterface> failedURLS =
            queryBizLogic.getFailedServiceUrls();
        
        // Which URLs failed?
        
        Set<String> failedUrlSet = new HashSet<String>();
        for(ServiceURLInterface failedURL : failedURLS) {
            failedUrlSet.add(failedURL.getUrlLocation());
        }
        
        // Create JSON result objects 
        
        JSONObject json = new JSONObject();
        
        JSONObject resultMapJson = new JSONObject();
        json.put("results",resultMapJson);

        for (ICab2bQuery queryObj : searchResults.keySet()) {
            
            JSONObject queryResultsJson = new JSONObject();
            resultMapJson.put(queryObj.getName(),queryResultsJson);
            
            TransformedResultObjectWithContactInfo res = searchResults.get(queryObj);
        
            for(String url : res.getAllUrls()) {

                JSONArray resultsJson = new JSONArray();

                // Create a list of results for this service
                
                for(Map<AttributeInterface, Object> result : res.getResultForUrl(url)) {

                    JSONObject resultJson = new JSONObject();
                    resultsJson.put(resultJson);
                    
                    for(AttributeInterface a : result.keySet()) {
                        String value = String.valueOf(result.get(a));
                        String title = edu.wustl.cab2b.common.util.Utility.getFormattedString(a.getName());
                        resultJson.put(title, value);
                    }
                }
                
                queryResultsJson.put(url,resultsJson);
            }
        }

        JSONArray failedUrlsJson = new JSONArray();
        json.put("failedUrls",failedUrlsJson);
        
        for(String url : failedUrlSet) {
            failedUrlsJson.put(url);
        }
        
        return json;
    }

    /**
     * Ensure the given model group exists and the user has access to it.
     * @param modelGroup
     * @param globusCredential
     * @return
     */
    private boolean verifyModelGroup(String modelGroup, GlobusCredential globusCredential) {

        String[] allModelGroupNames = getModelGroups(globusCredential);
        boolean found = false;
        for(String modelGroupName : allModelGroupNames) {
            if (modelGroupName.equals(modelGroup)) {
                found = true;
                break;
            }
        }
        return found;
    }


    /**
     * Returns a JSON string with the given error message.
     * @return JSON-formatted String
     */
    private String getJSONError(String exception, String message) {
        return "{\"error\":\""+exception+"\",\"message\":\""+message+"\"}";
    }

    /**
     * Returns a JSON string with usage instructions, or an error if a problem
     * occurs.
     * @return JSON-formatted String
     */
    private String getJSONUsage(GlobusCredential globusCredential) {

        try {
            
            String[] allModelGroupNames = getModelGroups(globusCredential);
            StringBuffer modelGroupUsage = new StringBuffer();
            for(String modelGroupName : allModelGroupNames) {
                if (modelGroupUsage.length()>0) modelGroupUsage.append("|");
                modelGroupUsage.append(modelGroupName);
            }
            
            JSONArray jsonArray = new JSONArray();
            jsonArray.put("/api/services");
            jsonArray.put("/api/search?searchString={1}&modelGroup={2["+modelGroupUsage+"]}&serviceUrl={3}");
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("supported_paths", jsonArray);
            
            return jsonObj.toString();
        }
        catch (JSONException x) {
            return getJSONError("JSONError", "Error printing JSON usage");
        }
    }
    
    /**
     * Get the model groups available with the given credentials.
     * @param globusCredential
     * @return
     */
    public String[] getModelGroups(GlobusCredential globusCredential) {

        ModelGroupOperations modelGroupOperations = new ModelGroupOperations();
        List<ModelGroupInterface> modelGroups = new ArrayList<ModelGroupInterface>();
        if (globusCredential == null) {
            modelGroups.addAll(modelGroupOperations.getAllNonSecuredModelGroups());
        } else {
            modelGroups.addAll(modelGroupOperations.getAllModelGroups());
        }
        String[] modelGroupNames = new String[modelGroups.size()];
        for (int i = 0; i < modelGroups.size(); i++) {
            modelGroupNames[i] = modelGroups.get(i).getModelGroupName();
        }
        return modelGroupNames;
    }

}
