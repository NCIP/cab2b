
package edu.wustl.common.util;

/**
 * This comparator is used where soritng shound be done on relevance counter of NameValueBean
 */
import edu.wustl.common.beans.NameValueBean;

public class NameValueBeanRelevanceComparator implements java.util.Comparator
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
			if (nvb1.getRelevanceCounter() != null && nvb2.getRelevanceCounter() != null)
			{
				int cmp = nvb1.getRelevanceCounter().compareTo(nvb2.getRelevanceCounter());
				//If relevance counter are equal then compare on basis of name.
				if (cmp == 0)
				{
					if (nvb1.getName() != null && nvb2.getName() != null)
					{
						return nvb1.getName().toString().toLowerCase().compareTo(nvb2.getName().toString().toLowerCase());
					}
				}
				else
				{
					return cmp;
				}
			}
		}
		return 0;
	}
}
