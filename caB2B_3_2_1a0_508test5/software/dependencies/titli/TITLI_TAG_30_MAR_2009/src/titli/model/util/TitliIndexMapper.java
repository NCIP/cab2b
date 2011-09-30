package titli.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class TitliIndexMapper 
{
	/**
	 * the only instance of this class
	 */
	private static TitliIndexMapper mapper;
	
	
	/**
	 * the in-memory document constructed from the xml file
	 */
	private Document document;
	
	private static final String FROM = " FROM ";
	private static final String SELECT = " SELECT ";
	private static final String WHERE = " WHERE ";
	private static final String JOIN = " join ";
	private static final String LEFT_JOIN = " left join ";
	private static final String SELECT_ALL = " SELECT * ";
	private static final String ALIAS = "alias";
	private static final String JOIN_MAPPING = "join";
	private static final String LEFT_JOIN_MAPPING = "leftjoin";
	private static final String NAME = "name";
	private static final String TABLE = "table";
	private static final String KEY_COLUMN = "keyColumn";
	private static final String JOIN_COLUMN = "joinColumn";
	private static final String LEFTJOIN = "leftJoin";
	private static final String SELECT_COLUMN = "selectColumns";
	private static final String TABLE_NAME = "tableName";
	private static final String ACTIVITY_STATUS = "activityStatus";
	private static final String TABLE_JOIN_MAPPING = "tablejoinmapping";
	private static final String DOT = ".";
	private static final String COMMA = ",";
	private static final String COMMA_SPACE = " , ";
	
	/**
	 * the private constructor for singleton behaviour
	 * it reads the xml file and creates the Document
	 *
	 */
	private TitliIndexMapper()
	{
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("titli-index-mapping.xml");
			
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
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	
	}
	
	/**
	 * get the only instance of this class
	 * @return the only instance of this class
	 */
	public static TitliIndexMapper getInstance()
	{
		if (mapper==null)
		{
			mapper= new TitliIndexMapper(); 
		}
		
		return mapper;
	}
	
	/**
	 * Return the query formed
	 * @param tableName Name of the table
	 * @return selectClause
	 * @throws Exception
	 */
	public StringBuilder returnJoinMapping(String tableName) throws Exception
	{
		StringBuilder joinMapping = new StringBuilder(FROM+tableName.toUpperCase()+" ");	
		StringBuilder activityStatusCondition = new StringBuilder(WHERE);
		StringBuilder selectClause = new StringBuilder(SELECT);
		int selectClauseLength = selectClause.length();
		int activityStatusConditionLength = activityStatusCondition.length();
		Element root = null;
		root = document.getDocumentElement();
		
		if(root==null)
		{
			throw new Exception("Tilti table mapping file can not be read");
		}
		
		NodeList nodeList =root.getElementsByTagName(TABLE_JOIN_MAPPING);
		
		int length = nodeList.getLength();
		for(int i=0;i<length;i++)
		{
			Element node = (Element)(nodeList.item(i));
			NodeList nodelist = node.getElementsByTagName(TABLE);
			int nodeLength = nodelist.getLength();
			
			for(int j=0;j<nodeLength;j++)
			{
				Element tableNode = (Element)(nodelist.item(j));
				if(tableNode.getAttribute(NAME).equalsIgnoreCase(tableName))
				{
					String tableAlias = tableNode.getAttribute(ALIAS);
					String tableColumn = tableNode.getAttribute(KEY_COLUMN);
					String activityStatusCond = tableNode.getAttribute(ACTIVITY_STATUS);
					if(activityStatusCond.length()!=0)
					{
						activityStatusCondition.append(tableAlias+DOT+activityStatusCond+" != 'Disabled' AND ");
					}
					
					selectClause.append(tableAlias+DOT+tableColumn+COMMA_SPACE);
					String includeColumns = tableNode.getAttribute(SELECT_COLUMN);
					StringTokenizer st = new StringTokenizer(includeColumns,COMMA);
					while(st.hasMoreTokens())
					{
						String nextToken = st.nextToken();
						selectClause.append(tableAlias+DOT+nextToken+COMMA_SPACE);
					}
					joinMapping.append(tableAlias);
					NodeList joinTableList = tableNode.getElementsByTagName(JOIN_MAPPING);
					int joinTableLength = joinTableList.getLength();
					for(int k=0;k<joinTableLength;k++)
					{
						joinMapping.append(JOIN);
						Element joinTableNode = (Element)(joinTableList.item(k));
						String joinTable = joinTableNode.getAttribute(TABLE_NAME);
						String joinColumn = joinTableNode.getAttribute(JOIN_COLUMN);
						String joinAlias = joinTableNode.getAttribute(ALIAS);
						String activityStatus = joinTableNode.getAttribute(ACTIVITY_STATUS);
						String joinIncludeCols = joinTableNode.getAttribute(SELECT_COLUMN);
						StringTokenizer st1 = new StringTokenizer(joinIncludeCols,COMMA);
						while(st1.hasMoreTokens())
						{
							String nextToken = st1.nextToken();
							selectClause.append(joinAlias+DOT+nextToken+COMMA_SPACE);
						}
						if(activityStatus.length()!=0)
						{
							activityStatusCondition.append(joinAlias+DOT+activityStatus+" != 'Disabled' AND ");
						}
						joinMapping.append(joinTable+" "+joinAlias+" on (");
						joinMapping.append(tableAlias+DOT+tableColumn+" = "+joinAlias+DOT+joinColumn+") ");
						NodeList leftJoinList = joinTableNode.getElementsByTagName(LEFT_JOIN_MAPPING);
						int leftJoinLength = leftJoinList.getLength();
						for(int l=0;l<leftJoinLength;l++)
						{
							joinMapping.append(LEFT_JOIN);
							Element leftJoinNode = (Element)(leftJoinList.item(l));
							String leftJoinTable = leftJoinNode.getAttribute(TABLE_NAME);
							String leftJoinAlias = leftJoinNode.getAttribute(ALIAS);
							String leftJoinColumn = leftJoinNode.getAttribute(KEY_COLUMN);
							String joinCol = leftJoinNode.getAttribute(JOIN_COLUMN);
							String leftJoinIncludeCols = leftJoinNode.getAttribute(SELECT_COLUMN);
							StringTokenizer st2 = new StringTokenizer(leftJoinIncludeCols,COMMA);
							while(st2.hasMoreTokens())
							{
								String nextToken = st2.nextToken();
								selectClause.append(leftJoinAlias+DOT+nextToken+COMMA_SPACE);
							}
							
							joinMapping.append(leftJoinTable+" "+leftJoinAlias+" on ("+leftJoinAlias+DOT+leftJoinColumn+" = "+joinAlias+DOT+joinCol+") ");
						}
					}
										
					NodeList leftJoinTableList = tableNode.getElementsByTagName(LEFTJOIN);
					int leftJoinTableLength = leftJoinTableList.getLength();
					for(int k=0;k<leftJoinTableLength;k++)
					{
						joinMapping.append(LEFT_JOIN);
						Element leftJoinTableNode = (Element)(leftJoinTableList.item(k));
						String leftJoinTable = leftJoinTableNode.getAttribute(TABLE_NAME);
						String leftJoinColumn = leftJoinTableNode.getAttribute(KEY_COLUMN);
						String leftJoinAlias = leftJoinTableNode.getAttribute(ALIAS);
						String joinColumnName = leftJoinTableNode.getAttribute(JOIN_COLUMN);
						String joinIncludeColumn = leftJoinTableNode.getAttribute(SELECT_COLUMN);
						StringTokenizer st3 = new StringTokenizer(joinIncludeColumn,COMMA);
						while(st3.hasMoreTokens())
						{
							String nextToken = st3.nextToken();
							selectClause.append(leftJoinAlias+DOT+nextToken+COMMA_SPACE);
						}
						
						joinMapping.append(leftJoinTable+" "+leftJoinAlias+" on (");
						joinMapping.append(tableAlias+DOT+joinColumnName+" = "+leftJoinAlias+DOT+leftJoinColumn+") ");
					}
				}
			}
		}
		if(activityStatusCondition.length() == activityStatusConditionLength)
			activityStatusCondition = new StringBuilder();
		
		if(selectClause.length() == selectClauseLength)
		{
			selectClause = new StringBuilder(SELECT_ALL);
		}
		else
		{
			selectClause.delete(selectClause.lastIndexOf(COMMA), selectClause.lastIndexOf(COMMA)+1);
		}
		joinMapping.append(" "+activityStatusCondition).toString();
		
		return selectClause.append(" "+joinMapping);	
	}
	
	
	/**
	 * Returns the alias for the corresponding table
	 * @param tableName Name of the table
	 * @return aliasName Alias for tableName
	 * @throws Exception
	 */
	public String getAliasFor(String tableName) throws Exception
	{
		String aliasName = null;
		Element root = null;
		root = document.getDocumentElement();
		
		if(root==null)
		{
			throw new Exception("Tilti table mapping file can not be read");
		}
		
		NodeList nodeList =root.getElementsByTagName(TABLE_JOIN_MAPPING);
		
		int length = nodeList.getLength();
		for(int i=0;i<length;i++)
		{
			Element node = (Element)(nodeList.item(i));
			NodeList nodelist = node.getElementsByTagName(TABLE);
			int nodeLength = nodelist.getLength();
			
			for(int j=0;j<nodeLength;j++)
			{
				Element tableNode = (Element)(nodelist.item(j));
				if(tableNode.getAttribute(NAME).equalsIgnoreCase(tableName))
				{
					return tableNode.getAttribute(ALIAS);
				}
			}
		}
		return aliasName;
	}
	
	/**
	 * Returns the order by clause
	 * @param tableName
	 * @return orderByClause
	 * @throws Exception
	 */
	public String getOrderByClause(String tableName) throws Exception
	{
		StringBuffer orderByClause = new StringBuffer();
		
		Element root = null;
		root = document.getDocumentElement();
		
		if(root==null)
		{
			throw new Exception("Tilti table mapping file can not be read");
		}
		
		NodeList nodeList =root.getElementsByTagName(TABLE_JOIN_MAPPING);
		
		int length = nodeList.getLength();
		for(int i=0;i<length;i++)
		{
			Element node = (Element)(nodeList.item(i));
			NodeList nodelist = node.getElementsByTagName(TABLE);
			int nodeLength = nodelist.getLength();
			
			for(int j=0;j<nodeLength;j++)
			{
				Element tableNode = (Element)(nodelist.item(j));
				if(tableNode.getAttribute(NAME).equalsIgnoreCase(tableName))
				{	
					String alias = tableNode.getAttribute(ALIAS);
					String uniqueColumn = tableNode.getAttribute(KEY_COLUMN);
					orderByClause.append(alias+DOT+uniqueColumn+" ");
					return orderByClause.toString();
				}
			}
		}	
		return orderByClause.toString();
	}
	
	public static void main(String args[]) throws Exception
	{
		TitliIndexMapper indexMapper = TitliIndexMapper.getInstance();
		
		/*String referredTable = new String();
		String table = mapper.getReferredColumnAndTable("IDENTIFIER","catissue_specimen_char");
		if(!table.equals(""))
		{
			System.out.println("table : "+table);
		}
		else
		{
			System.out.println("empty...........");
		}*/
		
		/*List<String> fromTables = new ArrayList<String>();
		fromTables = mapper.getFromTables("catissue_abstract_specimen");
		
		for(String fromTable : fromTables)
		{
			System.out.println(fromTable);
		}*/
		/*boolean flag = mapper.isContainmentRelation("catissue_specimen_char");
		System.out.println("flag : "+flag);*/
		/*String containment = mapper.getContainmentColumn("catissue_specimen_char");
		System.out.println(containment);*/
		
		StringBuilder mappingString = mapper.returnJoinMapping("catissue_specimen");
		System.out.println("mapping : "+mappingString);
	}
}
