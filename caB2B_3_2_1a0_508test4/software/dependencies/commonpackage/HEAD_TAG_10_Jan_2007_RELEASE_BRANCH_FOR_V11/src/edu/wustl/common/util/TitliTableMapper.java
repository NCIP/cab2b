/**
 * 
 */
package edu.wustl.common.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import titli.controller.Name;

/**
 * This class reads the mapping  xml file and gets the tree into the memory
 * it's a sigleton 
 * 
 * @author Juber Patel
 *
 */
public final class TitliTableMapper
{
	/**
	 * the only instance of this class
	 */
	private static TitliTableMapper mapper;
	
	
	/**
	 * the in-memory document constructed from the xml file
	 */
	private Document document;
	
	
	/**
	 * the private constructor for singleton behaviour
	 * it reads the xml file and creates the Document
	 *
	 */
	private TitliTableMapper()
	{
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("titli-table-mapping.xml");
		
		/*
		InputStream in=null;
		try
		{
			in = new FileInputStream("E:/juber/workspace/catissuecore/WEB-INF/src/titli-table-mapping.xml");
		}
		catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		DocumentBuilder builder = null;
		
		try
		{
			builder =DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		} 
		
		try
		{			
			document = builder.parse(in);
			in.close();
		}
		catch (SAXException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
	/**
	 * get the only instance of this class
	 * @return the only instance of this class
	 */
	public static TitliTableMapper getInstance()
	{
		if (mapper==null)
		{
			mapper= new TitliTableMapper(); 
		}
		
		return mapper;
	}
	
	
	/**
	 * get the the label corresponding to the specified table name
	 * @param tableName the table name
	 * @return the table name
	 * @throws Exception if problems occur
	 */
	public String getLabel(Name tableName) throws Exception
	{
		String label=tableName.toString();
		
		Element root = null;
		root = document.getDocumentElement();
		
		
		if(root==null)
		{
			throw new Exception("Tilti table mapping file can not be read");
		}
		
		NodeList nodeList =root.getElementsByTagName("mapping");
		
		int length = nodeList.getLength();
		
		for(int i=0; i<length; i++)
		{
			Element element = (Element)(nodeList.item(i));
			
			if(new Name(element.getAttribute("table")).equals(tableName))
			{
				label= element.getAttribute("label");
				break;
			}
			
		}
		
		return label;
	}
	
	
	/**
	 * get the pageOf string corresponding to the specified label
	 * @param label the label
	 * @return the pageOf string
	 */
	public String getPageOf(String label)
	{
		String pageOf=null;
		
		Element root = document.getDocumentElement();
		
		NodeList nodeList =root.getElementsByTagName("mapping");
		
		int length = nodeList.getLength();
		
		for(int i=0; i<length; i++)
		{
			Element element = (Element)(nodeList.item(i));
			
			if(element.getAttribute("label").equals(label))
			{
				pageOf= element.getAttribute("pageOf");
				break;
			}
			
		}
		
		return pageOf;
	}

	
	/**
	 * get the table name corrsponding to the specified label
	 * @param label the label
	 * @return the table name
	 */
	public Name getTable(String label)
	{
		Name table =null;
		
		Element root = document.getDocumentElement();
		
		NodeList nodeList =root.getElementsByTagName("mapping");
		
		int length = nodeList.getLength();
		
		for(int i=0; i<length; i++)
		{
			Element element = (Element)(nodeList.item(i));
			
			if(element.getAttribute("label").equals(label))
			{
				table = new Name(element.getAttribute("table"));
				break;
			}
			
		}
		
		return table;
		
	}
	

	
	
	
	/**
	 * 
	 * @param args args for main
	 * @throws Exception if problems occur
	 */
	public static void main(String[] args) throws Exception
	{
		
		TitliTableMapper tableMapper = TitliTableMapper.getInstance();
		
		//System.out.println(mapper.getPageOf("Cancer Research Group"));
		System.out.println(mapper.getLabel(new Name("catissue_institution")));
		System.out.println(mapper.getTable("Specimen Collection Group"));
		
	}
	

}
