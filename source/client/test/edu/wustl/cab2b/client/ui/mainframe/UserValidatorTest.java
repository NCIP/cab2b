package edu.wustl.cab2b.client.ui.mainframe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;

public class UserValidatorTest extends TestCase {
	public void testValidateUser() {

		StringBuffer userHome = new StringBuffer(System
				.getProperty("user.home"));
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
		String idP = "Training";
		UserValidator userValiodator = new UserValidator(p
				.getProperty("username"), idP);
		userValiodator.validateUser(p.getProperty("password"));
		assertNotNull("serializedDCR in UserValidator might be null due to unappropriate certificates",userValiodator.serializedDCR);

	}
}
