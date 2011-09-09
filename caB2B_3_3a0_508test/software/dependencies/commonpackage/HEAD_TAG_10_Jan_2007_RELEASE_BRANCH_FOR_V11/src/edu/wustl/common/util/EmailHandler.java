/**
 * <p>Title: EmailHandler Class>
 * <p>Description:	EmailHandler is used to send emails from the application.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.common.util;

import java.util.Iterator;
import java.util.List;

import edu.wustl.common.domain.User;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.common.util.global.SendEmail;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;


/**
 * EmailHandler is used to send emails from the application.
 * @author gautam_shetty
 */
public class EmailHandler
{
    /**
     * Sends the email to the administrator regarding the status of the CDE downloading.
     * i.e. the CDEs successfully downloaded and the error messages in case of errors in downloading the CDEs. 
     */
    public void sendCDEDownloadStatusEmail(List errorList)
    {
        // Send the status of the CDE downloading to the administrator.
        String body = "Dear Administrator," + "\n\n" + ApplicationProperties.getValue("email.cdeDownload.body.start") + "\n\n";
        
        Iterator iterator = errorList.iterator();
        while (iterator.hasNext())
        {
            body = body + iterator.next() + "\n\n";
        }
        
        body = "\n\n" + body + ApplicationProperties.getValue("email.catissuecore.team");
        
        String subject = ApplicationProperties.getValue("email.cdeDownload.subject");
        
        boolean emailStatus = sendEmailToAdministrator(subject, body);
        
        if (emailStatus)
		{
			Logger.out.info(ApplicationProperties
			    .getValue("cdeDownload.email.success"));
		}
		else
		{
			Logger.out.info(ApplicationProperties
			    .getValue("cdeDownload.email.failure"));
		}
    }
    
    
    /**
     * Sends email to the adminstrator.
     * Returns true if the email is successfully sent else returns false.
     * @param subject The subject of the email. 
     * @param body The body of the email.
     * @return true if the email is successfully sent else returns false.
     */
    private boolean sendEmailToAdministrator(String subject, String body)
    {
        String adminEmailAddress = XMLPropertyHandler.getValue("email.administrative.emailAddress");
        String sendFromEmailAddress = XMLPropertyHandler.getValue("email.sendEmailFrom.emailAddress");
        String mailServer = XMLPropertyHandler.getValue("email.mailServer");
        
        body = body + "\n\n" + ApplicationProperties.getValue("loginDetails.catissue.url.message") + Variables.catissueURL;
        
        SendEmail email = new SendEmail();
        boolean emailStatus = email.sendmail(adminEmailAddress, 
                								sendFromEmailAddress, mailServer, subject, body);
        
        return emailStatus;
    }
    

    /**
     * Creates and sends the user registration approval emails to user and the administrator.
     * @param user The user whose registration is approved.
     * @param roleOfUser Role of the user.
     */
    public void sendApprovalEmail(User user) throws DAOException
    {
        String subject = ApplicationProperties
        					.getValue("userRegistration.approve.subject");
        
        String body = "Dear " + user.getLastName() +
			        "," + user.getFirstName() +
			        "\n\n"+ ApplicationProperties.getValue("userRegistration.approved.body.start") +
			        getUserDetailsEmailBody(user); // Get the user details in the body of the email.
			        
        //Send login details email to the user.
        sendLoginDetailsEmail(user, body);
			        
		body = body + "\n\n" + ApplicationProperties.getValue("userRegistration.thank.body.end") +
			        "\n\n" + ApplicationProperties.getValue("email.catissuecore.team");
        
		//Send the user registration details email to the administrator.
        boolean emailStatus = sendEmailToAdministrator(subject, body);
		
		if (emailStatus)
        {
            Logger.out.info(ApplicationProperties
                    .getValue("user.approve.email.success")
                    + user.getLastName() + " " + user.getFirstName());
        }
        else
        {
            Logger.out.info(ApplicationProperties
                    .getValue("user.approve.email.failure")
                    + user.getLastName() + " " + user.getFirstName());
        }
    }
    
    /**
     * Returns the users details to be incorporated in the email.
     * @param user The user object.
     * @return the users details to be incorporated in the email.
     */
    private String getUserDetailsEmailBody(User user)
    {
        String userDetailsBody = "\n\n" + ApplicationProperties.getValue("user.loginName")+ Constants.SEPARATOR + user.getLoginName() + 
						"\n\n" + ApplicationProperties.getValue("user.lastName")+ Constants.SEPARATOR + user.getLastName() +
						"\n\n" + ApplicationProperties.getValue("user.firstName")+ Constants.SEPARATOR + user.getFirstName() +
						"\n\n" + ApplicationProperties.getValue("user.street")+ Constants.SEPARATOR + user.getAddress().getStreet() +
						"\n\n" + ApplicationProperties.getValue("user.city")+ Constants.SEPARATOR + user.getAddress().getCity() +
						"\n\n" + ApplicationProperties.getValue("user.zipCode")+ Constants.SEPARATOR + user.getAddress().getZipCode() +
						"\n\n" + ApplicationProperties.getValue("user.state")+ Constants.SEPARATOR + user.getAddress().getState() +
						"\n\n" + ApplicationProperties.getValue("user.country")+ Constants.SEPARATOR + user.getAddress().getCountry() +
						"\n\n" + ApplicationProperties.getValue("user.phoneNumber")+ Constants.SEPARATOR + user.getAddress().getPhoneNumber() +
						"\n\n" + ApplicationProperties.getValue("user.faxNumber")+ Constants.SEPARATOR + user.getAddress().getFaxNumber() +
						"\n\n" + ApplicationProperties.getValue("user.emailAddress")+ Constants.SEPARATOR + user.getEmailAddress() +
						"\n\n" + ApplicationProperties.getValue("user.institution")+ Constants.SEPARATOR + user.getInstitution().getName() +
						"\n\n" + ApplicationProperties.getValue("user.department")+ Constants.SEPARATOR + user.getDepartment().getName() +
						"\n\n" + ApplicationProperties.getValue("user.cancerResearchGroup")+ Constants.SEPARATOR + user.getCancerResearchGroup().getName();
        
        return userDetailsBody;
    }
    
       
    /**
     * Creates and sends the user signup request received email to the user and the administrator.
     * @param user The user registered for the membership.
     */
    public void sendUserSignUpEmail(User user) 
    {
        String subject = ApplicationProperties.getValue("userRegistration.request.subject");
        
        String body = "Dear "+ user.getLastName()+","+ user.getFirstName() + "\n\n" +
					  ApplicationProperties.getValue("userRegistration.request.body.start") + "\n" +
					  getUserDetailsEmailBody(user) +
					  "\n\n\t" + ApplicationProperties.getValue("userRegistration.request.body.end") +
					  "\n\n" + ApplicationProperties.getValue("email.catissuecore.team");
        
        boolean emailStatus = sendEmailToUserAndAdministrator(user.getEmailAddress(), subject, body);
        
        if (emailStatus)
        {
            Logger.out.info(ApplicationProperties
                    .getValue("userRegistration.email.success")
                    + user.getLastName() + " " + user.getFirstName());
        }
        else
        {
            Logger.out.info(ApplicationProperties
                    .getValue("userRegistration.email.failure")
                    + user.getLastName() + " " + user.getFirstName());
        }
    }
    
    /**
     * Creates and sends the login details email to the user.
     * Returns true if the email is successfully sent else returns false.
     * @param user The user whose login details are to be sent. 
     * @param userDetailsBody User registration details.
     * @return true if the email is successfully sent else returns false.
     * @throws DAOException
     */
    public boolean sendLoginDetailsEmail(User user, String userDetailsBody) throws DAOException
    {
        boolean emailStatus = false;
        
        try
        {
            String subject = ApplicationProperties
								.getValue("loginDetails.email.subject");
            
			String body = "Dear " + user.getFirstName()
			    				  + " " + user.getLastName();
			
			if (userDetailsBody != null)
			{
			    body = userDetailsBody;
			}
			
			String roleOfUser = SecurityManager.getInstance(EmailHandler.class)
							.getUserRole(user.getCsmUserId().longValue()).getName();
			body = body + "\n\n" + ApplicationProperties.getValue("forgotPassword.email.body.start")
				+ "\n\t "+ ApplicationProperties.getValue("user.loginName")+ Constants.SEPARATOR + user.getLoginName()
			    + "\n\t "+ ApplicationProperties.getValue("user.password")+ Constants.SEPARATOR + PasswordManager.decode(user.getPassword())
			    + "\n\t "+ ApplicationProperties.getValue("user.role")+ Constants.SEPARATOR + roleOfUser
			    + "\n\n" + ApplicationProperties.getValue("email.catissuecore.team");
			
			emailStatus = sendEmailToUser(user.getEmailAddress(), subject, body);
			
			if (emailStatus)
			{
				Logger.out.info(ApplicationProperties
				    .getValue("user.loginDetails.email.success")
				    + user.getLastName() + " " + user.getFirstName());
			}
			else
			{
				Logger.out.info(ApplicationProperties
				    .getValue("user.loginDetails.email.failure")
				    + user.getLastName() + " " + user.getFirstName());
			}
        }
        catch(SMException smExp)
        {
            throw new DAOException(smExp.getMessage(), smExp);
        }
        
        return emailStatus;
    }
    
  
    
    /**
     * Sends email to the user with the email address passed.
     * Returns true if the email is successfully sent else returns false.
     * @param userEmailAddress Email address of the user.
     * @param subject The subject of the email. 
     * @param body The body of the email.
     * @return true if the email is successfully sent else returns false.
     */
    private boolean sendEmailToUser(String userEmailAddress, String subject, String body)
    {
        String mailServer = ApplicationProperties
        		.getValue("email.mailServer");
		String sendFromEmailAddress = ApplicationProperties
		        .getValue("email.sendEmailFrom.emailAddress");
		
		body = body + "\n\n" + ApplicationProperties.getValue("loginDetails.catissue.url.message") + Variables.catissueURL;
		
		SendEmail email = new SendEmail();
        boolean emailStatus = email.sendmail(userEmailAddress, sendFromEmailAddress,
				                				mailServer, subject, body);
        return emailStatus;
    }
    
    /**
     * Sends email to the administrator and user with the email address passed.
     * Returns true if the email is successfully sent else returns false.
     * @param userEmailAddress Email address of the user.
     * @param subject The subject of the email. 
     * @param body The body of the email.
     * @return true if the email is successfully sent else returns false.
     */
    private boolean sendEmailToUserAndAdministrator(String userEmailAddress, String subject, String body)
    {
        String adminEmailAddress = ApplicationProperties
        		.getValue("email.administrative.emailAddress");
        String sendFromEmailAddress = ApplicationProperties
        		.getValue("email.sendEmailFrom.emailAddress");
        String mailServer = ApplicationProperties
                .getValue("email.mailServer");
        
        body = body + "\n\n" + ApplicationProperties.getValue("loginDetails.catissue.url.message") + Variables.catissueURL;
         
        SendEmail email = new SendEmail();
        boolean emailStatus = email.sendmail(userEmailAddress, adminEmailAddress, 
                							 null, sendFromEmailAddress, mailServer, subject, body);
        return emailStatus;
    }
    
    /**
     * Creates and sends the user registration rejection emails to user and the administrator.
     * @param user The user whose registration is rejected.
     */
    public void sendRejectionEmail(User user)
    {
        String subject = ApplicationProperties.getValue("userRegistration.reject.subject");
        
        String body = "Dear " + user.getLastName()
        + "," + user.getFirstName()
        + "\n\n"+ ApplicationProperties.getValue("userRegistration.reject.body.start");
        
        //Append the comments given by the administrator, if any.
        if ((user.getComments() != null) 
                && ("".equals(user.getComments()) == false))
        {
            body = body + "\n\n" + ApplicationProperties.getValue("userRegistration.reject.comments")
            					 + user.getComments();
        }
        
        body = body + "\n\n"+ ApplicationProperties.getValue("userRegistration.thank.body.end")
        			+ "\n\n" + ApplicationProperties.getValue("email.catissuecore.team");
        
        boolean emailStatus = sendEmailToUserAndAdministrator(user.getEmailAddress(), subject, body);
        
        if (emailStatus)
        {
            Logger.out.info(ApplicationProperties
                    .getValue("user.reject.email.success")
                    + user.getLastName() + " " + user.getFirstName());
        }
        else
        {
            Logger.out.info(ApplicationProperties
                    .getValue("user.reject.email.success")
                    + user.getLastName() + " " + user.getFirstName());
        }
    }
}
