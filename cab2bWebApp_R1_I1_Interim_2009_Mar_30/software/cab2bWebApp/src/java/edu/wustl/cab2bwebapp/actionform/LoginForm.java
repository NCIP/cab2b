/**
 * 
 */
package edu.wustl.cab2bwebapp.actionform;

import java.util.List;

import org.apache.struts.action.ActionForm;

import edu.wustl.cab2b.common.user.ServiceURL;

/**
 * @author gaurav_mehta
 * This Form is a form bean for Login. 
 * UserName and Password gets populated from this Form and accessed in LoginAction using this Form
 */
public class LoginForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String password;

    private String identityProvider;

    private List<ServiceURL> serviceInstance;

    public List<ServiceURL> getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(List<ServiceURL> serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

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

    public String getIdentityProvider() {
        return identityProvider;
    }

    public void setIdentityProvider(String identityProvider) {
        this.identityProvider = identityProvider;
    }
}
