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
 * Register
 */
public class RegisterForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private String userName;
    private String email;
    private String institution;
    private String phone;
    


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

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getInstitution() {
        return institution;
    }
    public void setInstitution(String institution) {
        this.institution = institution;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

}
