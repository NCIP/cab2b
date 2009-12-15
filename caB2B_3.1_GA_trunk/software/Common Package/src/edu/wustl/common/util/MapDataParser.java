package edu.wustl.common.util;

/**
 * @author Kapil Kaveeshwar
 */
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.hibernate.Hibernate;
import edu.wustl.common.query.Table;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

public class MapDataParser 
{
	private String packageName = "";
	private Map bigMap = new HashMap();
	private Collection dataList = new LinkedHashSet();	
	
	public MapDataParser(String packageName)
	{
		this.packageName = packageName;
	}
	
	private Map createMap()
	{
		Map map = new TreeMap();
		
		map.put("DistributedItem:1_Specimen_id","1");
		map.put("DistributedItem:1_quantity","100");
//		map.put("DistributedItem:1_unitSpan","mg");
//		map.put("DistributedItem:1_unit","mg");
		map.put("DistributedItem:1_Specimen_className","Tissue");
		
		map.put("DistributedItem:2_Specimen_id","2");
		map.put("DistributedItem:2_quantity","200");
//		map.put("DistributedItem:2_unitSpan","ml");
//		map.put("DistributedItem:2_unit","ml");
		map.put("DistributedItem:2_Specimen_className","Molecular");
		
//		map.put("SpecimenRequirement#FluidSpecimenRequirement:1_pathologyStatus","Primary Tumor");
//		map.put("SpecimenRequirement#FluidSpecimenRequirement:1_specimenType","Blood");
//		map.put("SpecimenRequirement#FluidSpecimenRequirement:1_tissueSite","Adrenal-Cortex");
//		map.put("SpecimenRequirement#FluidSpecimenRequirement:1_quantityInMiliLiter","20");
		
//		map.put("CollectionProtocolEvent:1_clinicalStatus", "Pre-Opt");                       
//		map.put("CollectionProtocolEvent:1_studyCalendarEventPoint", "11.0");                 
//		map.put("CollectionProtocolEvent:1_SpecimenRequirement:1_tissueSite", "Lung");        
//		map.put("CollectionProtocolEvent:1_SpecimenRequirement:1_specimenType", "Blood");     
//		map.put("CollectionProtocolEvent:1_SpecimenRequirement:1_specimenClass", "Tissue");
//		map.put("CollectionProtocolEvent:1_SpecimenRequirement:1_quantityIn", "6");
		
//		map.put("CollectionProtocolEvent:2_studyCalendarEventPoint", "10.0");                 
//		map.put("CollectionProtocolEvent:2_clinicalStatus", "Pre-Opt");                       
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:1_specimenType", "Blood");     
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:1_tissueSite", "Kidney");      
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:1_specimenClass", "Fluid");    
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:1_quantityIn", "7");
		
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:2_tissueSite", "Brain");       
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:2_specimenType", "Gel");       
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:2_specimenClass", "Cell");     
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:2_quantityIn", "8");
		
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:3_tissueSite", "Lever");       
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:3_specimenType", "Cell");      
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:3_specimenClass", "Molecular");
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:3_quantityIn", "9");
		
		return map; 
	}
	
	private Object toObject(String str,Class type) throws Exception
	{
	    if(type.equals(String.class))
			return str;
		else
		{
			if(str.trim().length()==0)
				return null;
			
			if(type.equals(Long.class))
				return new Long(str);
			else if(type.equals(Double.class))
				return new Double(str);
			else if(type.equals(Float.class))
				return new Float(str);
			else if(type.equals(Integer.class))
				return new Integer(str);
			else if(type.equals(Byte.class))
				return new Integer(str);
			else if(type.equals(Short.class))
				return new Integer(str);
			else if(type.equals(Table.class))
				return new Table(str,str);
            else if(type.equals(Boolean.class))
                return new Boolean(str);
            else if(type.equals(Date.class))
            /** Added by kiran_pinnamaneni
			 *  code reviewer abhijit_naik 
			 */
				return Utility.parseDate(str,Utility.datePattern(str));
            else if(type.equals(Blob.class))
            {
                File file = new File(str);
                DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
                
                byte[] buff = new byte[(int) file.length()];
                dis.readFully(buff);
                dis.close();
                return Hibernate.createBlob(buff);

            }
		}
		return str;
	}
	
	private Method findMethod(Class objClass, String methodName) throws Exception
	{
		Method method[] = objClass.getMethods();
		for (int i = 0; i < method.length; i++) 
		{
			if(method[i].getName().equals(methodName))
				return method[i]; 
		}
		return null;
	}
	
	//CollectionProtocolEvent:1.studyCalendarEventPoint, new Double(11)
	private void parstData(Object parentObj, String str,String value,String parentKey) throws Exception
	{
		StringTokenizer st = new StringTokenizer(str, "_");
		
		int tokenCount = st.countTokens();
		if(tokenCount>1)
		{
			String className = st.nextToken();
			String mapKey = parentKey+"-"+str.substring(0,str.indexOf("_"));
			Object obj = parseClassAndGetInstance(parentObj,className,mapKey);
			
			if(tokenCount==2)
			{
				String attrName = st.nextToken();
				String methodName =  Utility.createAccessorMethodName(attrName,true);
				
				Class objClass = obj.getClass();
				
				Logger.out.debug("methodName " +methodName);
				Logger.out.debug("objClass " +objClass);
				
				Method method = findMethod(objClass,methodName);
				
				Logger.out.debug("method parameter type " +method.getParameterTypes()[0]);
				Object objArr[] = {toObject(value,method.getParameterTypes()[0])};
				
				method.invoke(obj,objArr);
			}
			else
			{
				int firstIndex = str.indexOf("_");
				className = str.substring(firstIndex+1);
				parstData(obj,className,value,mapKey);
			}
		}
	}
	
	private Object parseClassAndGetInstance(Object parentObj,String str,String mapKey) throws Exception
	{
		//map.put("CollectionProtocolEvent:1.SpecimenRequirement:1.specimenType", "Blood");
		StringTokenizer innerST = new StringTokenizer(str, ":");
		String className = ""; 
		
		int count = innerST.countTokens();
		
		if(count==2) //Case obj is a collection
		{
			className = innerST.nextToken();
			String index = innerST.nextToken();
			
			Collection collection = null;
			
			if(parentObj == null)
			{
				collection = dataList;
				
				StringTokenizer st = new StringTokenizer(className,"#");
				if(st.countTokens()>1)
				{
					st.nextToken();
					className = st.nextToken();
				}
			}
			else//SpecimenRequirement:1.specimenType", "Blood");
			{
				String collectionName = className;
				StringTokenizer st = new StringTokenizer(className,"#");
				if(st.countTokens()>1)
				{
					collectionName = st.nextToken();
					className = st.nextToken();
				}
				collection = getCollectionObj(parentObj,collectionName);
			}
			
			return getObjFromList(collection, index, className, mapKey);
		}
		else //case map.put("CollectionProtocolEvent.studyCalendarEventPoint", new Double(11));
		{
			className = str; 
			
			StringTokenizer st = new StringTokenizer(className,"#");
			if(st.countTokens()>1)
			{
				className = st.nextToken();
			}
			
			Object retObj = Utility.getValueFor(parentObj,className);
			
			//Change for API Search   --- Jitendra 06/10/2006
			if(retObj == null) 
			{
				retObj = Utility.setValueFor(parentObj,className , null);
			}			
			return retObj;
		}
	}
	
	private Object getObjFromList(Collection coll,String index,String className,String mapKey) throws Exception
	{
		Object obj = bigMap.get(mapKey);
		if(obj!=null)
		{
			return obj;
		}
		else
		{
			String fullyQualifiedClassName = packageName + "." +className;
			Class aClass = Class.forName(fullyQualifiedClassName);
			obj = aClass.newInstance();
			coll.add(obj);
			bigMap.put(mapKey,obj);
			return obj;
		}
	}
	
	private Collection getCollectionObj(Object parentObj, String str) throws Exception
	{
		String attrName = str+"Collection";
		return (Collection) Utility.getValueFor(parentObj, attrName);
	}
	
	public Collection generateData(Map map, boolean isOrdered)  throws Exception
	{
	    if (isOrdered)
	    {
	        dataList = new ArrayList();
	    }
	    return generateData(map);
	}
	
	public Collection generateData(Map map)  throws Exception
	{
		Iterator it = map.keySet().iterator();
		while(it.hasNext())
		{
			String key = (String)it.next();
			String value = (String)map.get(key);
			parstData(null, key, value,"KEY");
		}
		return dataList;
	}
	
	public int parseKeyAndGetRowNo(String key)
	{
		int start = key.indexOf(":");
		int end = key.indexOf("_");
		int rowNo = Integer.parseInt(key.substring(start+1,end));
		return rowNo;
	}
	
	public static void main(String[] args) throws Exception
	{
		MapDataParser aMapDataParser = new MapDataParser("edu.wustl.catissuecore.domain");
		Map map = aMapDataParser.createMap();
		System.out.println(map);
		//map = aMapDataParser.fixMap(map);
		System.out.println(map);
		Collection dataCollection = aMapDataParser.generateData(map);
		System.out.println("Data: "+dataCollection);
	}
	
	public static void deleteRow(List list,Map map, String status)
	{
		deleteRow(list,map,status,null);
	}
	
	/**
	 * Returns boolean used for diabling/enabling checkbox in jsp Page and
	 * rearranging rows
	 */
	public static void deleteRow(List list,Map map, String status, String outer)
	{
		
		//whether delete button is clicked or not
		//String status = request.getParameter("status");
    	if(status == null)
    	{
    		status = Constants.FALSE;
    	}
    	
    	String text;
       	for(int k = 0; k < list.size(); k++){
       		text = (String)list.get(k);
    		String first = text.substring(0,text.indexOf(":"));
    		String second = text.substring(text.indexOf("_"));
    		
    		//condition for creating ids for innerTable
    		boolean condition = false;
    		String third = "",fourth = "";
    		
    		//checking whether key is inneTable'key or not
    		if(second.indexOf(":") != -1){
    			condition = true;
    			third = second.substring(0,second.indexOf(":"));
    			fourth = second.substring(second.lastIndexOf("_"));
    		}
    		
    		if(status.equals(Constants.TRUE)){
    			Map values = map;
    			
    			//for outerTable
    			int outerCount = 1;
    			
    			//for innerTable
    			int innerCount = 1;
    			for(int i = 1; i <= values.size() ; i++){
    				String id = "";
    				String mapId = "";
    				
    				//for innerTable key's rearrangement
    				if(condition){
    					if(outer != null){
    						id = first + ":"+ outer + third + ":"+ i + fourth;
    						mapId = first + ":"+ outer + third + ":"+ innerCount + fourth;
    					}
    					else {
    						//for outer key's rearrangement
    						for(int j = 1; j <= values.size() ; j++){
    							id = first + ":"+ i + third + ":"+ j + fourth;
    							mapId = first + ":"+ outerCount + third + ":"+ j + fourth;
    							
    							//checking whether map from form contains keys or not
    							if(values.containsKey(id)){
    								values.put(mapId,map.get(id));
    		    					outerCount++;
    		    				}
    						}
    					}
    					
    				}
    	    			
    	    		else {
    	    			id = first + ":" + i + second;
    	    			mapId = first + ":" +innerCount + second;
    	    		}
    				
    				//rearranging key's
    				if(values.containsKey(id)){
    					values.put(mapId,map.get(id));
    					innerCount++;
    				}
    			}
    		}
    	}
	}
}