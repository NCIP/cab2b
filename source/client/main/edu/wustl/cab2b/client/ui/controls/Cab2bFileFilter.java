package edu.wustl.cab2b.client.ui.controls;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class Cab2bFileFilter extends FileFilter {

	private String[] m_extensions;
	
	public Cab2bFileFilter(String[] extensions)
	{
		m_extensions = extensions;
	}
	@Override
	public boolean accept(File file) 
	{
		if(file.isDirectory())
		{
			return true;
		}
		int extnIndex =  file.getName().lastIndexOf(".");
		if(extnIndex != -1)
		{
			String actualExt = file.getName().substring(extnIndex+1);
			for(int i=0; i<m_extensions.length; i++)
			{
				if(m_extensions[i].compareToIgnoreCase(actualExt) == 0)
					return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		StringBuffer description= new StringBuffer();
		for(int i=0; i<m_extensions.length; i++)
		{
			if(i>0)
			{
				description.append(" | ");
			}
			description.append("*.").append(m_extensions[i]);
		}
		return description.toString();
	}

}
