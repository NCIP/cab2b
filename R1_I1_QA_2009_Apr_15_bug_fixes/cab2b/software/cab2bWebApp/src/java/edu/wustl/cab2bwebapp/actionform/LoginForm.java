/**
 * 
 */
package edu.wustl.cab2bwebapp.actionform;

import org.apache.struts.action.ActionForm;

/**
 * @author gaurav_mehta
 * @author chetan_pundhir
 * This Form is a form bean for Login. 
 * UserName and Password gets populated from this Form and accessed in LoginAction using this Form
 */
public class LoginForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
