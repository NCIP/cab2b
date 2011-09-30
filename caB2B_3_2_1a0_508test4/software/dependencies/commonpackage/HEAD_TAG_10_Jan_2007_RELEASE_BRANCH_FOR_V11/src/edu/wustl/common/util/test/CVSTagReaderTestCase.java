/*
 * Created on Jun 29, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.util.test;

import edu.wustl.common.test.BaseTestCase;
import edu.wustl.common.util.CVSTagReader;


/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CVSTagReaderTestCase extends BaseTestCase
{
	public CVSTagReaderTestCase()
	{
		super("CVSTagReaderTestCase");
	}
	
	public void testReadTagPositive()
	{
		CVSTagReader cvsTagReader = new CVSTagReader();
		String tag = cvsTagReader.readTag("Testdata/ApplicationVersionInfo.txt");
		assertEquals("Test_tag",tag);
	}

	public void testReadTagFileNotFound()
	{
		CVSTagReader cvsTagReader = new CVSTagReader();
		String tag = cvsTagReader.readTag("ApplicationVersionInfo1.txt");
		assertEquals(null,tag);
	}

	public void testReadTagWrongPattern()
	{
		CVSTagReader cvsTagReader = new CVSTagReader();
		String tag = cvsTagReader.readTag("Testdata/ApplicationVersionInfo2.txt");
		assertEquals(null,tag);
	}
}