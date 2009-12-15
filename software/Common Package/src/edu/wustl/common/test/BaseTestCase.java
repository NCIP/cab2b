/*
 * Created on Jul 4, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.test;

import junit.framework.TestCase;

import org.apache.log4j.PropertyConfigurator;

import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;


/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BaseTestCase extends TestCase
{
	public BaseTestCase()
	{
		super();
	}
	
	public BaseTestCase(String name)
	{
		super(name);
	}
	
	protected void setUp()
	{
		Variables.applicationHome = System.getProperty("user.dir");
		Logger.out = org.apache.log4j.Logger.getLogger("");
		PropertyConfigurator.configure(Variables.applicationHome+"/Logger.properties");
	}
}