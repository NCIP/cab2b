/**
 * 
 */
package edu.wustl.cab2b.admin.searchdata.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.util.Cab2bConstants;
import edu.wustl.catissuecore.flex.dag.DAGConstant;

/**
 * @author atul_jawale
 *
 */
public class CreateCategory extends BaseAction implements ServletRequestAware, ServletResponseAware ,SessionAware {

    private HttpServletRequest request;

    private HttpServletResponse response;
    
    private String title;

    private String description;

    private Map session ;
    
    public void setSession(final Map session) {
        this.session = session;
    }
    
    public Map getSession(){
        
      return session;
    }
    
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;

    }

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;

    }

    private static final long serialVersionUID = 7660830850643731270L;

    /**
     * 
     * @return
     */
    @Override
    public String execute() {

        
        if(session.get(DAGConstant.QUERY_OBJECT)!=null){
            
          session.remove(DAGConstant.QUERY_OBJECT);  
            
        }
        
        return Cab2bConstants.SUCCESS;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    
    
}
