/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */ 
package edu.wustl.common.util.global;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.security.exceptions.PasswordEncryptionException;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.util.StringEncrypter;
import gov.nih.nci.security.util.StringEncrypter.EncryptionException;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;



/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

public class PasswordManager
{
	
	public static final int SUCCESS=0;
	public static final int FAIL_LENGTH=1;
	public static final int FAIL_SAME_AS_OLD=2;
	public static final int FAIL_SAME_AS_USERNAME=3;
	public static final int FAIL_IN_PATTERN=4;
	public static final int FAIL_SAME_SESSION=5;
	public static final int FAIL_WRONG_OLD_PASSWORD=6;
	public static final int FAIL_INVALID_SESSION=7;
	
	
	/**
     * Generate random alpha numeric password.
     * @param loginName the loginName of a user.
     * @return Returns the generated password.
     */
	public static String generatePassword()
    {
    	//Define a Constants alpha-numeric String 
    	final String UPPER_CHAR_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	final String LOWER_CHAR_STRING = "abcdefghijklmnopqrstuvwxyz";
    	final String DIGITS_STRING = "0123456789";
    	
    	// Generate password of length 6
    	//final int PASSWORD_LENGTH = 6;
    	
    	Random random = new Random();
    	StringBuffer passwordBuff = new StringBuffer(); 
    	//This password must satisfy the following criteria:New Password must include at least one 
    	//Upper Case, Lower Case letter and a Number. It must not include Space.
    	for (int i = 0; i < 2 ; i++)
		{
    		//Generate a random number from 0(inclusive) to lenght of CHAR_STRING(exclusive).
    		//Get the character corrosponding to random number and append it to password buffer.
    		int randomVal = random.nextInt(UPPER_CHAR_STRING.length());
    		passwordBuff.append(UPPER_CHAR_STRING.charAt(randomVal));
    		randomVal = random.nextInt(LOWER_CHAR_STRING.length());
    		passwordBuff.append(LOWER_CHAR_STRING.charAt(randomVal));
    		randomVal = random.nextInt(DIGITS_STRING.length());
    		passwordBuff.append(DIGITS_STRING.charAt(randomVal));
		}
    	return passwordBuff.toString();
    }

	private static StringEncrypter stringEncrypter = null;
	/**
	 * TO get the instance of StringEncrypter class.
	 * @return The Object reference of StringEncrypter class.
	 * @throws EncryptionException
	 */
	private static StringEncrypter getEncrypter() throws EncryptionException
	{
		if (stringEncrypter==null)
		{
			stringEncrypter = new StringEncrypter();
		}
		return stringEncrypter;
	}
	
	/**
	 * TO get Encrypted password for the given password.
	 * @param password The password to be encrypted.
	 * @return The Encrypted password for the given password.
	 * @throws PasswordEncryptionException 
	 */
	public static String encrypt(String password) throws PasswordEncryptionException
	{
		try
		{
			return getEncrypter().encrypt(password);
		}
		catch (EncryptionException e)
		{
			throw new PasswordEncryptionException(e.getMessage(), e);
		}
	}
	

	/**
	 * TO get Decrypted password for the given password.
	 * @param password The password to be Decrypted.
	 * @return The Decrypted password for the given password.
	 * @throws PasswordEncryptionException 
	 */
	public static String decrypt(String password) throws PasswordEncryptionException
	{
		try
		{
			return getEncrypter().decrypt(password);
		}
		catch (EncryptionException e)
		{
			throw new PasswordEncryptionException(e.getMessage(), e);
		}
	}
	@Deprecated 
    public static String encode(String input)
    {

        char char_O = 'O';
        char char_F = 'f';
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
                'b', 'c', 'd', 'e', 'f'};
        char ch1 = 'A';
        String key = new String("" + ch1);
        key += "WelcomeTocaTISSUECORE" + char_O;
        String in = "";
        key += char_F;
        key += "ThisIsTheFirstReleaseOfcaTISSUECOREDevelopedByWashUAtPersistentSystemsPrivateLimited";
        for (int i = 0; i < input.length(); i++)
        {
            in += input.substring(i, i + 1);
            in += key.substring(i, i + 1);
        }

        try
        {
            byte[] bytes = in.getBytes();
            StringBuffer s = new StringBuffer(bytes.length * 2);

            for (int i = 0; i < bytes.length; i++)
            {
                byte b = bytes[i];
                s.append(digits[(b & 0xf0) >> 4]);
                s.append(digits[b & 0x0f]);
            }

            return s.toString();
        }
        catch (Exception e)
        {
            Logger.out.warn("Problems in Encryption/Decryption in CommonJdao "
                    );
            Logger.out.warn("Exception= " + e.getMessage());
        }
        return null;
    }
	
	@Deprecated 
    public static String decode(String s)
    {
        try
        {
            int len = s.length();
            byte[] r = new byte[len / 2];
            for (int i = 0; i < r.length; i++)
            {
                int digit1 = s.charAt(i * 2);
                int digit2 = s.charAt(i * 2 + 1);
                if ((digit1 >= '0') && (digit1 <= '9'))
                    digit1 -= '0';
                else if ((digit1 >= 'a') && (digit1 <= 'f'))
                    digit1 -= 'a' - 10;
                if ((digit2 >= '0') && (digit2 <= '9'))
                    digit2 -= '0';
                else if ((digit2 >= 'a') && (digit2 <= 'f'))
                    digit2 -= 'a' - 10;
                r[i] = (byte) ((digit1 << 4) + digit2);
            }
            String sin = new String(r);
            String sout = "";
            for (int i = 0; i < sin.length(); i += 2)
            {
                sout += sin.substring(i, i + 1);
            }
            return sout;
        }
        catch (Exception e)
        {
            Logger.out.warn("Problems in Decription/Encription");
            Logger.out.warn("Exception= " + e.getMessage());
        }
        return null;
    }
    

    /**
     * This method returns the validation results on Form Bean. This method should be called 
     * from validate method of form bean
     * @param newPassword New Password value
     * @param oldPassword Old Password value
     * @param httpSession HttpSession object
     * @param abstractForm UserForm object  
     * @return SUCCESS if all condition passed 
     *   else return respective error code (constant int) value  
     */
    public static int validatePasswordOnFormBean(String newPassword,String oldPassword,HttpSession httpSession) {

       
    	// to check whether password change in same session
       	// get attribute (Boolean) from session object stored when password is changed successfully 
    	Boolean passwordChangedInsameSession = (Boolean)httpSession.getAttribute(Constants.PASSWORD_CHANGE_IN_SESSION);
    	if(passwordChangedInsameSession!=null && passwordChangedInsameSession.booleanValue()==true)
    	{
    		// return error code if attribute (Boolean) is in session   
    		Logger.out.debug("Attempt to change Password in same session Returning FAIL_SAME_SESSION");
    		return FAIL_SAME_SESSION; 
    	}
    	int minimumPasswordLength=Integer.parseInt(XMLPropertyHandler.getValue(Constants.MINIMUM_PASSWORD_LENGTH));
    	// to Check length of password,if not valid return FAIL_LENGTH 
    	if(newPassword.length()<minimumPasswordLength)
		{
    		Logger.out.debug("Password is not valid returning FAIL_LENGHT");
    		return FAIL_LENGTH; 
		}

    	// to Check new password is different as old password ,if bot are same return FAIL_SAME_AS_OLD    	
		if(newPassword.equals(oldPassword))
		{
			Logger.out.debug("Password is not valid returning FAIL_SAME_AS_OLD");
			return FAIL_SAME_AS_OLD; 
		}
				
		/**
		 * following code checks pattern i.e password must include atleast one UCase,LCASE and Number
		 * and must not contain space charecter.
		 */
		char dest[]=new char[newPassword.length()]; 
		// get char array where values get stores in dest[]
		newPassword.getChars(0,newPassword.length(),dest,0);
		boolean foundUCase=false; // boolean to check UCase character found in string
		boolean foundLCase=false; // boolean to check LCase character found in string
		boolean foundNumber=false; // boolean to check Digit/Number character found in string
		boolean foundSpace=false; // boolean to check space in String
		 
		for(int i=0;i<dest.length;i++)
		{
			// to check if character is a Space. if true break from loop
			if(Character.isSpaceChar(dest[i]))
			{
				foundSpace=true;
				break;
			}
			// to check whether char is Upper Case. 
			if(foundUCase==false&&Character.isUpperCase(dest[i])==true)
			{
				foundUCase=true;
			}
			
			// to check whether char is Lower Case 
			if(foundLCase==false&&Character.isLowerCase(dest[i])==true)
			{
				foundLCase=true;
			}
			
			// to check whether char is Number/Digit
			if(foundNumber==false&&Character.isDigit(dest[i])==true)
			{
				foundNumber=true;
			}	
		}
		// condition to check whether all above condotion is satisfied
		if(foundUCase==false||foundLCase==false||foundNumber==false||foundSpace==true)
		{
			Logger.out.debug("Password is not valid returning FAIL_IN_PATTERN");
			return FAIL_IN_PATTERN;
		}
		Logger.out.debug("Password is Valid returning SUCCESS");
		return SUCCESS;
}
    
    /**
     * 
     * @param newPassword New Password value
     * @param oldPassword Old Password value
     * @param httpSession HttpSession object
     * @param abstractForm UserForm object  
     * @return SUCCESS (constant int 0) if all condition passed 
     *   else return respective error code (constant int) value  
     */
    
   /**
    * TODO Remove this method. This method combines UI validation and business rules validation which is incorrect.
    * Call validatePasswordOnFormBean for Form bean validations. Write your own methods for business validations.
    */
    public static int validate(String newPassword,String oldPassword,HttpSession httpSession)
	{
    	// get SessionDataBean objet from session 
    	Object obj = httpSession.getAttribute(Constants.SESSION_DATA);
        SessionDataBean sessionData=null;
        String userName="";
        if(obj!=null)
		{
        	sessionData = (SessionDataBean) obj;
        	// get User Name
        	userName=sessionData.getUserName();
		}
        else
        {
        	return FAIL_INVALID_SESSION;
        }
    	// to check whether user entered correct old password
    	
    	
    	try
    	{
    		// retrieve User DomainObject by user name
    	    IBizLogic bizLogic = new DefaultBizLogic();
    	    String [] selectColumnNames = {"password"};
    	    String [] whereColumnNames = {"loginName"};
    	    String [] whereColumnCondition = {"="};
    	    String [] whereColumnValues = {userName};
    	    
    	    //Gautam_COMMON_TEMP_FIX USER_CLASS_NAME
    	    List userList = bizLogic.retrieve(Constants.USER_CLASS_NAME,selectColumnNames,whereColumnNames,whereColumnCondition,whereColumnValues,null);
    	    String password = null;
    	    if(userList!=null && !userList.isEmpty())
    		{
    	        password = (String) userList.get(0);
    		}
    	    
    	    // compare password stored in database with value of old password currently entered by 
    		// user for Change Password operation
    		if (!oldPassword.equals(PasswordManager.decode(password)))
	        {
	            return FAIL_WRONG_OLD_PASSWORD; //retun value is int 6
	        }
    	}
       	catch(Exception e)
    	{
    		// if error occured during password comparision
       		Logger.out.error(e.getMessage(),e);
    		return FAIL_WRONG_OLD_PASSWORD;
    	}
       	
    	// to check whether password change in same session
       	// get attribute (Boolean) from session object stored when password is changed successfully 
    	Boolean b = null;
    	b = (Boolean)httpSession.getAttribute(Constants.PASSWORD_CHANGE_IN_SESSION);
    	Logger.out.debug("b---" + b);
    	if(b!=null && b.booleanValue()==true)
    	{
    		// return error code if attribute (Boolean) is in session   
    		Logger.out.debug("Attempt to change Password in same session Returning FAIL_SAME_SESSION");
    		return FAIL_SAME_SESSION; // return int value 5
    	}
    	int minimumPasswordLength=Integer.parseInt(XMLPropertyHandler.getValue(Constants.MINIMUM_PASSWORD_LENGTH));
    	// to Check length of password,if not valid return FAIL_LENGTH = 2
    	if(newPassword.length()<minimumPasswordLength)
		{
    		Logger.out.debug("Password is not valid returning FAIL_LENGHT");
    		return FAIL_LENGTH; // return int value 1
		}

    	// to Check new password is different as old password ,if bot are same return FAIL_SAME_AS_OLD = 3    	
		if(newPassword.equals(oldPassword))
		{
			Logger.out.debug("Password is not valid returning FAIL_SAME_AS_OLD");
			return FAIL_SAME_AS_OLD; //return int value 2
		}
		
		// to check password is differnt than user name if same return FAIL_SAME_AS_USERNAME =4
		// eg. username=abc@abc.com newpassword=abc is not valid
		int usernameBeforeMailaddress=userName.indexOf('@');
		// get substring of username before '@' character    
		String name=userName.substring(0,usernameBeforeMailaddress);
		Logger.out.debug("usernameBeforeMailaddress---" + name);
		if(name!=null && newPassword.equals(name))
		{
			Logger.out.debug("Password is not valid returning FAIL_SAME_AS_USERNAME");
			return FAIL_SAME_AS_USERNAME; // return int value 3
		}
		//to check password is differnt than user name if same return FAIL_SAME_AS_USERNAME =4
		// eg. username=abc@abc.com newpassword=abc@abc.com is not valid
		if(newPassword.equals(userName))
		{
			Logger.out.debug("Password is not valid returning FAIL_SAME_AS_USERNAME");
			return FAIL_SAME_AS_USERNAME; // return int value 3
		}
		
		
		// following is to checks pattern i.e password must include atleast one UCase,LCASE and Number
		// and must not contain space charecter.
		// define get char array whose length is equal to length of new password string.
		char dest[]=new char[newPassword.length()]; 
		// get char array where values get stores in dest[]
		newPassword.getChars(0,newPassword.length(),dest,0);
		boolean foundUCase=false; // boolean to check UCase character found in string
		boolean foundLCase=false; // boolean to check LCase character found in string
		boolean foundNumber=false; // boolean to check Digit/Number character found in string
		boolean foundSpace=false;
		 
		for(int i=0;i<dest.length;i++)
		{
			// to check if character is a Space. if true break from loop
			if(Character.isSpaceChar(dest[i]))
			{
				foundSpace=true;
				Logger.out.debug("Found Space in Password");
				break;
			}
			// to check whether char is Upper Case. 
			if(foundUCase==false&&Character.isUpperCase(dest[i])==true)
			{
				//foundUCase=true if char is Upper Case and Upper Case is not found in previous char.
				foundUCase=true;
				Logger.out.debug("Found UCase in Password");
			}
			
			// to check whether char is Lower Case 
			if(foundLCase==false&&Character.isLowerCase(dest[i])==true)
			{
				//foundLCase=true if char is Lower Case and Lower Case is not found in previous char.
				foundLCase=true;
				Logger.out.debug("Found LCase in Password");
			}
			
			// to check whether char is Number/Digit
			if(foundNumber==false&&Character.isDigit(dest[i])==true)
			{
			//	foundNumber=true if char is Digit and Digit is not found in previous char.
				foundNumber=true;
				Logger.out.debug("Found Number in Password");
			}	
		}
		// condition to check whether all above condotion is satisfied
		if(foundUCase==false||foundLCase==false||foundNumber==false||foundSpace==true)
		{
			Logger.out.debug("Password is not valid returning FAIL_IN_PATTERN");
			return FAIL_IN_PATTERN; // return int value 4
		}
		Logger.out.debug("Password is Valid returning SUCCESS");
		return SUCCESS;
	}
    
    /**
     * 
     * @param errorCode int value return by validate() method
     * @return String error message with respect to error code 
     */
    public static String getErrorMessage(int errorCode)
    {
    	String errMsg="";
    	
    	switch(errorCode)
    	{
    		case FAIL_LENGTH:
    			int minimumPasswordLength=Integer.parseInt(XMLPropertyHandler.getValue(Constants.MINIMUM_PASSWORD_LENGTH));
    			List<String> placeHolders = new ArrayList<String>();
    			placeHolders.add(new Integer(minimumPasswordLength).toString());
    			errMsg=ApplicationProperties.getValue("errors.newPassword.length",placeHolders);
    			break;
    		case FAIL_SAME_AS_OLD:
    			errMsg=ApplicationProperties.getValue("errors.newPassword.sameAsOld");
    			break;
    		case FAIL_SAME_AS_USERNAME:
    			errMsg=ApplicationProperties.getValue("errors.newPassword.sameAsUserName");
    			break;
    		case FAIL_IN_PATTERN:
    			errMsg=ApplicationProperties.getValue("errors.newPassword.pattern");
    			break;	
    		case FAIL_SAME_SESSION:
    			errMsg=ApplicationProperties.getValue("errors.newPassword.sameSession");
    			break;
    		case FAIL_WRONG_OLD_PASSWORD:
    			errMsg=ApplicationProperties.getValue("errors.oldPassword.wrong");
    			break;
    		case FAIL_INVALID_SESSION:
    			errMsg=ApplicationProperties.getValue("errors.newPassword.genericmessage");
				break;
    	    default:
    			errMsg=ApplicationProperties.getValue("errors.newPassword.genericmessage");
				break;
    	}	
    	return errMsg;
    	
    }
    public static void main(String[] args) throws PasswordEncryptionException
    {
    	String pwd = "admin";
    	String encodedPWD = encrypt(pwd);
    	System.out.println("encodedPWD:"+encodedPWD+":");
    	
        System.out.println(decrypt("xa2ImfuLjjZavG8j0xzkLA=="));
        System.out.println("old decoding:"+decode("614164576d65696c6e63"));
        //Mandar 08-May-06
        if(args.length > 1 )
    	{
    		String filename = args[0];
    		String password = args[1];
    		encodedPWD = encrypt(password);
    		System.out.println("Filename : "+filename + " : password : "+password+" : encoded"+encodedPWD ); 
    		writeToFile(filename,encodedPWD  );
    	}
    }
    
    /**
     * This method writes the encoded password to the file.
     * @param filename File to be written. 
     * @param encodedPassword Encoded password.
     */
	private static void writeToFile(String filename,String encodedPassword)
    {
		//Mandar: 781 10-May-06: 
    	try
		{
    		File fileObject = new File(filename );
    		FileWriter writeObject = new FileWriter(fileObject );
    		
    		writeObject.write("first.admin.encodedPassword="+encodedPassword+"\n" ) ;
			writeObject.close();

		}
    	catch(Exception ioe)
		{
    		System.out.println("Error : " + ioe);
		}
    } // writeToFile
}