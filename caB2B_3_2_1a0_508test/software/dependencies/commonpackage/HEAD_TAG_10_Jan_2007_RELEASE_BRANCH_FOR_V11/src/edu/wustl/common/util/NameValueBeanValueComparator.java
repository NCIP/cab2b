
package edu.wustl.common.util;

/**
 * This comparator is used where soritng shound be done on value of NameValueBean
 */
import edu.wustl.common.beans.NameValueBean;

public class NameValueBeanValueComparator implements java.util.Comparator
{

	/* (non-Javadoc)
	 * @see ava.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1)
	{
		if (arg0 instanceof NameValueBean && arg1 instanceof NameValueBean)
		{
			NameValueBean nvb1 = (NameValueBean) arg0;
			NameValueBean nvb2 = (NameValueBean) arg1;
			//Compare according to relevance counter
			if (nvb1.getValue() != null && nvb2.getValue() != null)
			{
				return (new Long(Long.parseLong(nvb1.getValue().toString()))).compareTo(new Long(Long.parseLong(nvb2.getValue())));
			}
		}
		return 0;
	}
}
