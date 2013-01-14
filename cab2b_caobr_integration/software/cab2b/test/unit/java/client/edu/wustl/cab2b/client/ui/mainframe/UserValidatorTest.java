/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.mainframe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;
import edu.wustl.cab2b.common.authentication.Authenticator;
import edu.wustl.cab2b.common.authentication.exception.AuthenticationException;

public class UserValidatorTest extends TestCase {
//    public void testValidateUserAndDelegate() {
//
//        StringBuffer userHome = new StringBuffer(System.getProperty("user.home"));
//        userHome.append(File.separatorChar).append("user.properties");
//        File file = new File(userHome.toString());
//        Properties p = new Properties();
//        try {
//            p.load(new FileInputStream(file));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Authenticator userValidator = new Authenticator(p.getProperty("username"));
//        userValidator.validateAndDelegate(p.getProperty("password"));
//        assertNotNull("serializedDCR in UserValidator might be null due to unappropriate certificates",
//                      Authenticator.getSerializedDCR());
//
//    }
//    public void testValidateUser() {
//        StringBuffer userHome = new StringBuffer(System.getProperty("user.home"));
//        userHome.append(File.separatorChar).append("user.properties");
//        File file = new File(userHome.toString());
//        Properties p = new Properties();
//        try {
//            p.load(new FileInputStream(file));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Authenticator userValidator = new Authenticator(p.getProperty("username"));
//        try{
//            userValidator.validateUser(p.getProperty("password"));
//        }catch(AuthenticationException ae) {
//            ae.printStackTrace();
//            fail("Unable to autheticate user");
//        }
//
//    }
    public void testValidateUserForFail() {
        StringBuffer userHome = new StringBuffer(System.getProperty("user.home"));
        userHome.append(File.separatorChar).append("user.properties");
        File file = new File(userHome.toString());
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Authenticator userValidator = new Authenticator(p.getProperty("username"));
        try{
            userValidator.validateUser("password");
            fail("User should not be autheticated, as no password was provided");
        } catch(AuthenticationException ae) {
           assertTrue(true);
        }
    }
}
