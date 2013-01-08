/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2bwebapp.actionform;

import org.apache.struts.action.ActionForm;

/**
 * @author gaurav_mehta
 * @author chetan_pundhir
 * This Form is a form bean for login page. 
 * UserName and Password gets populated in this Form and accessed in LoginAction using this Form.
 */
public class LoginForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String password;
    
    private int auth;


    /**
     * This method returns the userName.
     * @return String
     */    
    public String getUserName() {
        return userName;
    }

    /**
     * This method sets the userName.
     * @param userName
     */    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * This method returns the password.
     * @return String
     */     
    public String getPassword() {
        return password;
    }

    /**
     * This method sets the password.
     * @param password
     */    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getAuth(){
    	return auth;
    }
    
    public void setAuth(int auth){
    	this.auth= auth;
    }
}
