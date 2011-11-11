package titli.model.util;

/**
 * 
 */


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
	 * Returns the main table of the specified input table
	 * @param inputTable
	 * @return main table
	 */
	public String returnMainTable(String inputTable)
	{
		Element root = document.getDocumentElement();
		NodeList nodeList =root.getElementsByTagName("table-relation-mapping");
		root.getAttributes();
		int length = nodeList.getLength();
		
		for(int i=0; i<length; i++)
		{
			Element node = (Element)(nodeList.item(i));
			NodeList nodelist = node.getElementsByTagName("main");
			int nodeLength = nodelist.getLength();
			
			for(int j=0;j<nodeLength;j++)
			{
				Element mainNode = (Element)(nodelist.item(j));
				NodeList relatedNodeList = mainNode.getElementsByTagName("related");
				int relatedNodeLength = relatedNodeList.getLength();
				if(relatedNodeLength == 0)
				{
					System.out.println("No related tables for table : "+mainNode.getAttribute("label"));
				}
				for(int relatedCtr=0;relatedCtr<relatedNodeLength;relatedCtr++)
				{
					Element relatedNode = (Element)(relatedNodeList.item(relatedCtr));
					if(relatedNode.getAttribute("table").equals(inputTable))
					{
						return mainNode.getAttribute("table");
					}
				}
			}
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param args args for main
	 * @throws Exception if problems occur
	 */
	public static void main(String[] args) throws Exception
	{	
		TitliTableMapper tableMapper = TitliTableMapper.getInstance();
		
		//mapper.returnTableInheritance("catissue_abs_speci_coll_group");
		System.out.println(mapper.getLabel(new Name("catissue_institution")));
//		System.out.println(mapper.getTable("Specimen Collection Group"));
		//System.out.println(mapper.getToTable(new Name("catissue_abs_speci_coll_group")));
		//System.out.println(mapper.getFromTable(new Name("catissue_specimen")));
		
		/*List<String> tables = new ArrayList<String>();
		tables=mapper.refreshTables("catissue_specimen_coll_group",tables);
		if(tables.size() == 0)
		{
			System.out.println("null");
		}
		else
		{
			for(String table:tables)
			{
				System.out.println(table);
			}
		}
		System.out.println("----------------------------------------");
		tables = new ArrayList<String>();
		tables=mapper.refreshTables("catissue_specimen",tables);
		for(String table:tables)
		{
			System.out.println(table);
		}
		
		System.out.println("----------------------------------------");
		tables = new ArrayList<String>();
		tables=mapper.refreshTables("catissue_tissue_specimen",tables);
		for(String table:tables)
		{
			System.out.println(table);
		}
		
		System.out.println("----------------------------------------");
		tables = new ArrayList<String>();
		tables=mapper.refreshTables("catissue_tisse_specimen",tables);
		for(String table:tables)
		{
			System.out.println(table);
		}*/
		//mapper.returnRelatedTables("catissue_specimen");
		/*List<String> fromTables = mapper.getFromTable(new Name("catissue_specimen"));
		for(String fromTable : fromTables)
		{
			System.out.println(new Name(fromTable));
		}*/
//		List<String> relatedTables = mapper.returnTablesToBeRefreshed("catissue_specimen_coll_group");
//		for(String table:relatedTables)
//		{
//			System.out.println(table);
//		}
		String mainTable = mapper.returnMainTable("catissue_tissue_specimen");
		if(mainTable != null)
			System.out.println(mainTable);
		else
			System.out.println("NULL.........");
		
	}
	

}
