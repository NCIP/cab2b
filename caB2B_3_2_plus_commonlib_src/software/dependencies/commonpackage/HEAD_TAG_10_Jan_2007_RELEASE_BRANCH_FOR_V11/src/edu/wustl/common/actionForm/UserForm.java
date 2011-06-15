/**
 * <p>Title: UserForm Class>
 * <p>Description:  UserForm Class is used to encapsulate all the request parameters passed 
 * from User Add/Edit webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.common.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.domain.User;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
/**
 * UserForm Class is used to encapsulate all the request parameters passed 
 * from User Add/Edit webpage.
 * @author gautam_shetty
 * */
public class UserForm extends AbstractActionForm
{

    /**
     * Last Name of the user.
     */
    private String lastName;

    /**
     * First Name of the user.
     */
    private String firstName;

    /**
     * Institution name of the user.
     */
    private long institutionId;

    /**
     * EmailAddress Address of the user.
     */
    private String emailAddress;
    
    /**
     * Old Password of the user.
     */
    private String oldPassword;
    
    /**
     * New Password of the user.
     */
    private String newPassword;
    
    /**
     * Confirmed new password of the user.
     */
    private String confirmNewPassword;
    
    /**
     * Department name of the user.
     */
    private long departmentId;

    /**
     * Street Address of the user.
     */
    private String street;

    /**
     * The City where the user stays.
     */
    private String city;
    
    /**
     * The State where the user stays.
     */
    private String state;

    /**
     * The Country where the user stays.
     */
    private String country;

    /**
     * The zip code of city where the user stays.
     * */
    private String zipCode;

    /**
     * Phone number of the user.
     * */
    private String phoneNumber;

    /**
     * Fax number of the user.
     * */
    private String faxNumber;

    /**
     * Role of the user.
     * */
    private String role;

    /**
     * Cancer Research Group of the user.  
     */
    private long cancerResearchGroupId;


    /**
     * Comments given by user.
     */
    private String comments;

    /**
     * Status of user in the system.
     */
    private String status;
    
    private Long csmUserId;

    //Mandar 24-Apr-06 Bug 972 : Confirm email address
    /**
     * COnfirm EmailAddress of the user.
     */
    private String confirmEmailAddress;
    
    

    /**
     * No argument constructor for UserForm class. 
     */
    public UserForm()
    {
        reset();
    }

    
    //Mandar : 24-Apr-06 : bug id : 972
	/**
	 * @return Returns the confirmEmailAddress.
	 */
	public String getConfirmEmailAddress()
	{
		return confirmEmailAddress;
	}
	/**
	 * @param confirmEmailAddress The confirmEmailAddress to set.
	 */
	public void setConfirmEmailAddress(String confirmEmailAddress) 
	{
		this.confirmEmailAddress = confirmEmailAddress;
	}
    
    
    
    /**
     * Returns the last name of the user 
     * @return String representing the last name of the user.
     * @see #setFirstName(String)
     */
    public String getLastName()
    {
        return (this.lastName);
    }

    /**
     * Sets the last name of the user.
     * @param lastName Last Name of the user
     * @see #getFirstName()
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Returns the first name of the user.
     * @return String representing the first name of the user.
     * @see #setFirstName(String)
     */
    public String getFirstName()
    {
        return (this.firstName);
    }

    /**
     * Sets the first name of the user.
     * @param firstName String representing the first name of the user.
     * @see #getFirstName()
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Returns the institutionId name of the user.
     * @return String representing the institutionId of the user. 
     * @see #setinstitution(String)
     */
    public long getInstitutionId()
    {
        return (this.institutionId);
    }

    /**
     * Sets the institutionId name of the user.
     * @param institutionId String representing the institutionId of the user.
     * @see #getinstitution()
     */
    public void setInstitutionId(long institution)
    {
        this.institutionId = institution;
    }

    /**
     * Returns the emailAddress Address of the user.
     * @return String representing the emailAddress address of the user.
     */
    public String getEmailAddress()
    {
        return (this.emailAddress);
    }

    /**
     * Sets the emailAddress address of the user.
     * @param emailAddress String representing emailAddress address of the user
     * @see #getEmailAddress()
     */
    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    /**
     * @return Returns the confirmNewPassword.
     */
    public String getConfirmNewPassword()
    {
        return confirmNewPassword;
    }
    
    /**
     * @return Returns the newPassword.
     */
    public String getNewPassword()
    {
        return newPassword;
    }
    
    /**
     * @return Returns the oldPassword.
     */
    public String getOldPassword()
    {
        return oldPassword;
    }
    
    /**
     * @param confirmNewPassword The confirmNewPassword to set.
     */
    public void setConfirmNewPassword(String confirmNewPassword)
    {
        this.confirmNewPassword = confirmNewPassword;
    }
    
    /**
     * @param newPassword The newPassword to set.
     */
    public void setNewPassword(String newPassword)
    {
        this.newPassword = newPassword;
    }
    
    /**
     * @param oldPassword The oldPassword to set.
     */
    public void setOldPassword(String oldPassword)
    {
        this.oldPassword = oldPassword;
    }
    
    /**
     * Returns the Department Name of the user.
     * @return String representing departmentId of the user.
     * @see #getDepartmentId()
     */
    public long getDepartmentId()
    {
        return (this.departmentId);
    }

    /**
     * Sets the Department Name of the user.
     * @param departmentId String representing departmentId of the user.
     * @see #getDepartmentId()
     */
    public void setDepartmentId(long department)
    {
        this.departmentId = department;
    }

    /**
     * Returns the cancer research group the user belongs.
     * @return Returns the cancerResearchGroupId.
     * @see #setCancerResearchGroupId(String)
     */
    public long getCancerResearchGroupId()
    {
        return cancerResearchGroupId;
    }

    /**
     * Sets the cancer research group the user belongs.
     * @param cancerResearchGroupId The cancerResearchGroupId to set.
     * @see #getCancerResearchGroupId()
     */
    public void setCancerResearchGroupId(long cancerResearchGroup)
    {
        this.cancerResearchGroupId = cancerResearchGroup;
    }

    /**
     * Returns the Street Address of the user.
     * @return String representing mailing address of the user.
     * @see #setStreet(String)
     */
    public String getStreet()
    {
        return (this.street);
    }

    /**
     * Sets the Street Address of the user.
     * @param address String representing mailing address of the user.
     * @see #getStreet()
     */
    public void setStreet(String street)
    {
        this.street = street;
    }

    /**
     * Returns the City where the user stays.
     * @return String representing city of the user.
     * @see #setCity(String)
     */
    public String getCity()
    {
        return (this.city);
    }

    /**
     * Sets the City where the user stays.
     * @param city String representing city of the user.
     * @see #getCity()
     */
    public void setCity(String city)
    {
        this.city = city;
    }

    /**
     * Returns the State where the user stays.
     * @return String representing state of the user.
     * @see #setState(String)
     */
    public String getState()
    {
        return (this.state);
    }

    /**
     * Sets the State where the user stays.
     * @param state String representing state of the user.
     * @see #getState()
     */
    public void setState(String state)
    {
        this.state = state;
    }

    /**
     * Returns the Country where the user stays.
     * @return String representing country of the user.
     * @see #setCountry(String)
     */
    public String getCountry()
    {
        return (this.country);
    }

    /**
     * Sets the Country where the user stays.
     * @param country String representing country of the user.
     * @see #getCountry()
     */
    public void setCountry(String country)
    {
        this.country = country;
    }

    /**
     * Returns the zip code of the user's city. 
     * @return Returns the zip.
     * @see #setZip(String)
     */
    public String getZipCode()
    {
        return zipCode;
    }

    /**
     * Sets the zip code of the user's city.
     * @param zip The zip code to set.
     * @see #getZip()
     */
    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    /**
     * Returns the phone number of the user.
     * @return Returns the phone number.
     * @see #setPhone(String)
     */
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the user. 
     * @param phone The phone number to set.
     * @see #getphoneNumber()
     */
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the fax number of the user.
     * @return Returns the fax.
     * @see #setFax(String)
     */
    public String getFaxNumber()
    {
        return this.faxNumber;
    }

    /**
     * Sets the fax number of the user.
     * @param fax The fax to set.
     * @see #getFax()
     */
    public void setFaxNumber(String faxNumber)
    {
        this.faxNumber = faxNumber;
    }

    /**
     * Returns the role of the user.
     * @return the role of the user.
     * @see #setRoleCollection(String)
     */
    public String getRole()
    {
        return role;
    }

    /**
     * Sets the role of the user.
     * @param role the role of the user.
     * @see #getRole()
     */
    public void setRole(String role)
    {
        this.role = role;
    }


    /**
     * @return Returns the comments.
     */
    public String getComments()
    {
        return comments;
    }

    /**
     * @param comments The comments to set.
     */
    public void setComments(String comments)
    {
        this.comments = comments;
    }


    /**
     * Returns the id assigned to form bean.
     */
    public int getFormId()
    {
        int formId = Constants.APPROVE_USER_FORM_ID;
        if ((pageOf != null) && (Constants.PAGEOF_APPROVE_USER.equals(pageOf) == false))
        {
           formId = Constants.USER_FORM_ID;
        }
        Logger.out.debug("................formId...................."+formId);
        
        return formId;
    }

    /**
     * @return Returns the status.
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * @param status The status to set.
     */
    public void setStatus(String status)
    {
        this.status = status;
    }
   
    /**
     * @return Returns the csmUserId.
     */
    public Long getCsmUserId()
    {
        return csmUserId;
    }
    
    /**
     * @param csmUserId The csmUserId to set.
     */
    public void setCsmUserId(Long csmUserId)
    {
        this.csmUserId = csmUserId;
    }
    
    /**
     * Resets the values of all the fields.
     * Is called by the overridden reset method defined in ActionForm.  
     * */
    protected void reset()
    {
        this.lastName = null;
        this.firstName = null;
        this.institutionId = -1;
        this.emailAddress = null;
        this.departmentId = -1;
        this.street = null;
        this.city = null;
        this.state = null;
        this.country = null;
        this.zipCode = null;
        this.phoneNumber = null;
        this.faxNumber = null;
        this.role = null;
        this.cancerResearchGroupId = -1;
        this.status = Constants.ACTIVITY_STATUS_NEW;
        this.activityStatus = Constants.ACTIVITY_STATUS_NEW;
        //Mandar : 24-Apr-06 : bug 972:
        this.confirmEmailAddress = null;
    }
    
    /**
     * Copies the data from an AbstractDomain object to a UserForm object.
     * @param user An AbstractDomain object.  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        if (Constants.PAGEOF_CHANGE_PASSWORD.equals(pageOf) == false)
        {
            User user = (User) abstractDomain;
            
            this.id = user.getId().longValue();
            this.lastName = user.getLastName();
            this.firstName = user.getFirstName();

            // Check for null entries (for admin)
            if(!edu.wustl.common.util.Utility.isNull(user.getInstitution()) )
            	this.institutionId = user.getInstitution().getId().longValue();
            
            this.emailAddress = user.getEmailAddress();
            
            //Mandar : 24-Apr-06 : bug id 972 : confirmEmailAddress
            confirmEmailAddress = this.emailAddress ;
            
            if(!edu.wustl.common.util.Utility.isNull(user.getDepartment()) )
            	this.departmentId = user.getDepartment().getId().longValue();
            
            if(!edu.wustl.common.util.Utility.isNull(user.getCancerResearchGroup()) )
            	this.cancerResearchGroupId = user.getCancerResearchGroup().getId().longValue();

            if(!edu.wustl.common.util.Utility.isNull(user.getAddress()) )
            {
                this.street = user.getAddress().getStreet();
                this.city = user.getAddress().getCity();
                this.state = user.getAddress().getState();
                this.country = user.getAddress().getCountry();
                this.zipCode = user.getAddress().getZipCode();
                this.phoneNumber = user.getAddress().getPhoneNumber();
                this.faxNumber = user.getAddress().getFaxNumber();
            }
            
            //Populate the activity status, comments and role for approve user and user edit.  
            if ((getFormId() == Constants.APPROVE_USER_FORM_ID) || 
                    ((pageOf != null) && (Constants.PAGEOF_USER_ADMIN.equals(pageOf))))
            {
                this.activityStatus = user.getActivityStatus();
                
                if(!edu.wustl.common.util.Utility.isNull(user.getComments()) )
                	this.comments = user.getComments();
                
                this.role = user.getRoleId();
                
                if (getFormId() == Constants.APPROVE_USER_FORM_ID)
                {
                    this.status = user.getActivityStatus();
                    if (activityStatus.equals(Constants.ACTIVITY_STATUS_ACTIVE))
                    {
                        this.status = Constants.APPROVE_USER_APPROVE_STATUS;
                    }
                    else if (activityStatus.equals(Constants.ACTIVITY_STATUS_CLOSED))
                    {
                        this.status = Constants.APPROVE_USER_REJECT_STATUS;
                    }
                    else if (activityStatus.equals(Constants.ACTIVITY_STATUS_NEW))
                    {
                        this.status = Constants.APPROVE_USER_PENDING_STATUS;
                    }
                }
            }
            
            if (Constants.PAGEOF_USER_ADMIN.equals(pageOf))
            {
                this.setCsmUserId(user.getCsmUserId());
            }
        }
        
        Logger.out.debug("this.activityStatus............."+this.activityStatus);
        Logger.out.debug("this.comments"+this.comments);
        Logger.out.debug("this.role"+this.role);
        Logger.out.debug("this.status"+this.status);
        Logger.out.debug("this.csmUserid"+this.csmUserId);
    }
    
    /**
     * Overrides the validate method of ActionForm.
     * */
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();
        try
        {
            if (operation != null)
            {
            	
                if (pageOf.equals(Constants.PAGEOF_CHANGE_PASSWORD))
                {
                    if (validator.isEmpty(oldPassword))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("user.oldPassword")));
                    }
                    
                    if (validator.isEmpty(newPassword))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("user.newPassword")));
                    }
                    
                    if (validator.isEmpty(confirmNewPassword))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("user.confirmNewPassword")));
                    }
                    
                    if (!validator.isEmpty(newPassword) && !validator.isEmpty(confirmNewPassword))
                    {
                        if (!newPassword.equals(confirmNewPassword))
                        {
                            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.confirmNewPassword.reType"));
                        }
                    }
                    /*
                     * begin: Added for Password validation
                     */          			
        			Logger.out.debug("before if of Validate password " + newPassword + " " + oldPassword);
        			if (!validator.isEmpty(newPassword) && !validator.isEmpty(oldPassword))
                    {
                    	int result=-1;
            			// Call static method PasswordManager.validate() where params are
            			// new password,old password,user name
            			// returns int value.
            			result=PasswordManager.validate(newPassword,oldPassword,request.getSession());
            			Logger.out.debug("return from Password validate " + result);
            			// if validate method returns value greater than zero then validation fails  
            			if(result!=PasswordManager.SUCCESS)  
            			{
            				// get error message of validation failure where param is result of validate() method
            				String errorMessage=PasswordManager.getErrorMessage(result);
            				Logger.out.debug("error from Password validate " + errorMessage);
            				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item",errorMessage));
            			}
                    }
            		
                    
                    Logger.out.debug("after call to Validate password");
                    /*
                     * end: Password validation 
                     */
                }
                else
                {
                	setRedirectValue(validator);
                	Logger.out.debug("user form " ); 
                    if (operation.equals(Constants.ADD)
                            || operation.equals(Constants.EDIT))
                    {
                    	// Mandar 10-apr-06 : bugid :353 
                    	// Error messages should be in the same sequence as the sequence of fields on the page.
                    	
                        if (validator.isEmpty(emailAddress))
                        {
                            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                    "errors.item.required", ApplicationProperties
                                            .getValue("user.emailAddress")));
                        }
                        else
                        {
                            if (!validator.isValidEmailAddress(emailAddress))
                            {
                                errors.add(ActionErrors.GLOBAL_ERROR,
                                                new ActionError("errors.item.format",
                                                        ApplicationProperties.getValue("user.emailAddress")));
                            }
                        }
                        
                        // Mandar : 24-Apr-06 Bugid:972 confirmEmailAddress start
                        if (!pageOf.equals(Constants.PAGEOF_USER_PROFILE))
                        {
                            if (validator.isEmpty(confirmEmailAddress))
                            {
                                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                        "errors.item.required", ApplicationProperties
                                                .getValue("user.confirmemailAddress")));
                            }
                            else
                            {
                                if (!validator.isValidEmailAddress(confirmEmailAddress))
                                {
                                    errors.add(ActionErrors.GLOBAL_ERROR,
                                                    new ActionError("errors.item.format",
                                                            ApplicationProperties.getValue("user.confirmemailAddress")));
                                }
                            }
                            if(!confirmEmailAddress.equals(emailAddress ) )
                            {
                            	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.email.mismatch"));
                            }
                        	
                        }
                        //Mandar : 24-Apr-06 Bugid:972 confirmEmailAddress end

                        
                        if (validator.isEmpty(lastName))
                        {
                            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                    "errors.item.required", ApplicationProperties
                                            .getValue("user.lastName")));
                        }

                        if (validator.isEmpty(firstName))
                        {
                            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                    "errors.item.required", ApplicationProperties
                                            .getValue("user.firstName")));
                        }

                        if (validator.isEmpty(city))
                        {
                            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                    "errors.item.required", ApplicationProperties
                                            .getValue("user.city")));
                        }

                        if(!validator.isValidOption(state))
                        {
                            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                    "errors.item.required", ApplicationProperties
                                            .getValue("user.state")));
                        }

                        if (validator.isEmpty(zipCode))
                        {
                            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                    "errors.item.required", ApplicationProperties
                                            .getValue("user.zipCode")));
                        }
                        else
                        {
                        	if(!validator.isValidZipCode(zipCode))
                        	{
                        		errors.add(ActionErrors.GLOBAL_ERROR,
                                        new ActionError("errors.zipCode.format",
                                                ApplicationProperties.getValue("user.zipCode")));
                        	}
                        }
                        if(!validator.isValidOption(country))
                        {
                            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                    "errors.item.required", ApplicationProperties
                                            .getValue("user.country")));
                        }
                        
                        if (validator.isEmpty(phoneNumber))
                        {
                            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                    "errors.item.required", ApplicationProperties
                                            .getValue("user.phoneNumber")));
                        }
//                        else
//                        {
//                        	if(!validator.isValidPhoneNumber(phoneNumber))
//                        	{
//                        		errors.add(ActionErrors.GLOBAL_ERROR,
//                                        new ActionError("errors.phoneNumber.format",
//                                                ApplicationProperties.getValue("user.phoneNumber")));
//                        	}
//                        }
//                        if(!validator.isEmpty(faxNumber)&& !validator.isValidPhoneNumber(faxNumber))
//                        {
//                        	errors.add(ActionErrors.GLOBAL_ERROR,
//                                    new ActionError("errors.phoneNumber.format",
//                                            ApplicationProperties.getValue("user.faxNumber")));
//                        }

                        if (validator.isValidOption(String.valueOf(institutionId)) == false)
                        {
                            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                    "errors.item.required", ApplicationProperties
                                            .getValue("user.institution")));
                        }

                        if (validator.isValidOption(String.valueOf(departmentId)) == false)
                        {
                            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                    "errors.item.required", ApplicationProperties
                                            .getValue("user.department")));
                        }
                        
                        if (validator.isValidOption(String.valueOf(cancerResearchGroupId)) == false)
                        {
                            errors.add(ActionErrors.GLOBAL_ERROR,
                                            new ActionError("errors.item.required",
                                                    ApplicationProperties.getValue("user.cancerResearchGroup")));
                        }

                        if(validator.isValidOption(activityStatus) == false)
                        {
                        	errors.add(ActionErrors.GLOBAL_ERROR,
                                    new ActionError("errors.item.required",
                                            ApplicationProperties.getValue("user.activityStatus")));
                        }
                    }

                    if (pageOf.equals(Constants.PAGEOF_APPROVE_USER))
                    {
                        if (validator.isValidOption(status) == false)
                        {
                            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                    "errors.item.required", ApplicationProperties
                                            .getValue("user.approveOperation")));
                        }
                    }

                    if (pageOf.equals(Constants.PAGEOF_USER_ADMIN) || pageOf.equals(Constants.PAGEOF_APPROVE_USER))
                    {
                        if (role != null)
                        {
                            if (validator.isValidOption(role) == false)
    	                    {
    	                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
    	                                "errors.item.required", ApplicationProperties
    	                                        .getValue("user.role")));
    	                    }
                        }
                    }
                	// Mandar 10-apr-06 : bugid :353 end 
                }
            }
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage(), excp);
        }
        
        return errors;
    }
    
    /**
     * This method sets Identifier of Objects inserted by AddNew activity in Form-Bean which initialized AddNew action
     * @param formBeanId - FormBean ID of the object inserted
     *  @param addObjectIdentifier - Identifier of the Object inserted 
     */
    public void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier)
    {
        if(addNewFor.equals("institution"))
        {
            setInstitutionId(addObjectIdentifier.longValue());
        }
        else if(addNewFor.equals("department"))
        {
            setDepartmentId(addObjectIdentifier.longValue());
        } 
        else if(addNewFor.equals("cancerResearchGroup"))
        {
            setCancerResearchGroupId(addObjectIdentifier.longValue());
        }
    }
    
}