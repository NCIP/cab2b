/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.cagrid.caobr.client.CaObrClient;

import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.SearchResultDVO;
import edu.wustl.cab2bwebapp.util.caObr.PropertyLoader;

public class CheckConceptAction extends Action { 

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(AddLimitAction.class);

    /**
     * The execute method is called by the struts action flow and it calls the code for generating 
     * dynamic HTML for add limit page.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {
    	
        String actionForward = null;
        
        List<List<SearchResultDVO>> searchResultview = null;
        List<SearchResultDVO> searchResultDVOList =  null;        
        String[] arWords = null;
        Object values = null;
        String strValue = null;
        String strWithoutWord =  null;
        String strURL = PropertyLoader.getCaObrServiceURL();
        String tokens[] = null;
        Set<String> setWihoutBlank = null;
        boolean[] flags = null;
        
        logger.info(" CaObr Service URL :: " + strURL);
        
        CaObrClient client = new CaObrClient(strURL);
                             
        try {
            String index = request.getParameter(Constants.INDEX);
            String[] indexs = index.split(Constants.INDEX_SEPEARATOR);
            HashSet<String> conceptSet = null;
            searchResultview = (List<List<SearchResultDVO>>) request.getSession().getAttribute(Constants.SEARCH_RESULTS_VIEW);

            for (String intex : indexs) {
                searchResultDVOList = searchResultview.get(Integer.parseInt(intex));                               
                for (SearchResultDVO dvo : searchResultDVOList) {
                    values = dvo.getValue();                                     
                    if (values instanceof String) {                    	
                    	strValue = (String) values;
                    	conceptSet = new HashSet<String>();                    	                     
                        strWithoutWord =  strValue.replaceAll("\\w+",Constants.WORD_INDICATOR);                        
                        arWords =  strValue.split("\\W+");
                                               
                    	if(arWords != null){
                    		setWihoutBlank = new HashSet<String>(); 
                        	for(String  strWord : arWords){
                        		if(strWord != null && !strWord.equalsIgnoreCase(" ") && !strWord.isEmpty()){
                        			setWihoutBlank.add(strWord.trim());
                        		}        		
                        	}                         	     	        
                        	tokens = setWihoutBlank.toArray(new String[setWihoutBlank.size()]);
                    	}   	 
                                                                    
                        flags = client.isConceptsInAnyOntology(tokens);

                        for(int i = 0;i < tokens.length; i++){
                        	if(flags[i]){
                        		conceptSet.add(tokens[i]);
                        	}                        	
                        }
                        
                        logger.debug(" Set of Concept  :: " + conceptSet);

                        String strConceptURL = "";
                        for(String strWord : arWords){
                        	if(conceptSet.contains(strWord)){
                        		strConceptURL = new StringBuilder("<a class='link' href='ShowLimitedAnnotationResult.do?token=").append(strWord).append(
                                                "' target='_blank' >").append(strWord).append("</a>").toString();
                        		strWithoutWord = strWithoutWord.replaceFirst(Constants.WORD_INDICATOR,strConceptURL);
                        	}else{
                        		strWithoutWord = strWithoutWord.replaceFirst(Constants.WORD_INDICATOR,strWord);
                        	}                        	    		    			    		
                    	}                    
                        dvo.setValue(strWithoutWord);
                    }
                }
                actionForward = Constants.FORWARD_SEARCH_RESULTS_PANEL;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.addlimit.failure");
            errors.add(Constants.FATAL_ADD_LIMIT_FAILURE, error);
            saveErrors(request, errors);
            actionForward = Constants.FORWARD_FAILURE;
        }
        return mapping.findForward(actionForward);
    }
}
