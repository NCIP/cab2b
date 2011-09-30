/*
 * Created on Jun 7, 2006
 */
package edu.wustl.common.util.tag;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.common.beans.NameValueBean;

/**
 * @author chetan_bh
 *
 * A Utility class to generate javascript code for constructing
 * javascript hashtable datastructure from Java Map datastructures.
 * 
 * Assumes that the input is a Map and all keys for outer and inner
 * Maps are strings, and values are either Map or List.
 * Elements of all inner Lists are assumed to be string again. 
 */
public class ScriptGenerator {
	private ScriptGenerator()
	{
		
	}
	
	public static String getJSForOutermostDataTable()
	{
		String returner = "";
		returner += "<script language=\"JavaScript\" type=\"text/javascript\"> \n";
		returner += "var outerMostDataTable = new Hashtable(); \n";
		returner += "</script>\n";
		return returner;
	}

	public static String getJSEquivalentFor(Map dataMap, String rowNum)
	{
		String returner = "<script language=\"JavaScript\" type=\"text/javascript\"> \n";
		if(dataMap == null || dataMap.size() == 0)
		{
			returner += "</script>\n";
			return returner;
		}
		int mapDepth = depthOfMap(dataMap);
		for(int i = 0; i<= mapDepth; i++)
		{
			returner += "var dataTable_"+rowNum+"_"+(i+1)+"; \n"; 
		}
		returner += (String)getVarInStringForm(dataMap,0,"",rowNum);
		returner += "\n";
		returner += "outerMostDataTable.put(\""+rowNum+"\",dataTable_"+rowNum+"_1); \n";
		returner += "</script>\n";
		return returner;
	}
	
	/**
	 * A utility function to find the depth of Map.
	 * Uses iteration to find the depth.
	 * 
	 * depth of the map means the level of recursion used like
	 * Map of Map of Map of Map of Map of List
	 * here the depth of the outer map is 5.
	 * 
	 * @param map Java Map datastructure.
	 * @return depth of the map.
	 */
	public static int depthOfMap(Map map)
	{
		int depth = 0;
		while(map instanceof Map)
		{
			Set kSet = map.keySet();
			Object[] kSetArray = kSet.toArray();
			Object firstVal = map.get(kSetArray[0]);
			if(firstVal instanceof Map)
			{
				map = (Map)firstVal;
			}else
			{
				break;
			}
			depth++;
		}
		depth++;
		return depth;
	}
	
	/**
	 * A recursive function to construct a string equivalent of the Javascript code
	 * for constructing a Javascript hastable.
	 * @param obj an Object, it can be either Map or List, if List the call will terminate.
	 * @param depth the depth of the curent obj in the actual Map. 
	 * @param keyValue key from which we got this obj value, for outer map it is empty.
	 * @return String equivalent of javascript code. 
	 */
	public static String getVarInStringForm(Object obj,int depth,String keyValue,String rowNumber)
	{
    String returner = "";
    String varName = "dataTable_"+rowNumber+"_";
    depth++;
    
		if(obj instanceof Map)
		{
			Map dMap = (Map)obj;
			returner += varName+depth+" = new Hashtable(); \n";  
      
			Set keySet = dMap.keySet();
			Object[] keySetObj = keySet.toArray();
			for(int i = 0; i < keySetObj.length; i++)
			{
				Object valObj = dMap.get(keySetObj[i]);
				//String kValForNext = keySetObj[i].toString();
				NameValueBean nvb=(NameValueBean)keySetObj[i];
				String kValForNext = nvb.getValue();
				returner += getVarInStringForm(valObj,depth,kValForNext,rowNumber);
        
			}
		if(!keyValue.equals(""))
		{
			returner += varName+(depth-1)+".put(\""+keyValue+"\","+varName+depth+"); \n";
		}
			
		}else if(obj instanceof List)
		{
			List dList = (List) obj;
			
			returner += varName+depth+" = new Array(); \n";
			for(int i = 0; i < dList.size(); i++)
			{
				NameValueBean nvb=(NameValueBean)dList.get(i);
				returner += varName+depth+"["+i+"] = "+nvb.getValue()+"; \n";
			}
			returner += varName+(depth-1)+".put(\""+keyValue+"\","+varName+depth+"); \n";
		}
		
		return returner;
	}
	
	public static void main12(String args[])
	{
		Map containerMap = new Hashtable();
		
		Map xMap1 = new Hashtable();
		Map xMap2 = new Hashtable();

		List list1 = new ArrayList();
		list1.add("A");
		List list2 = new ArrayList();
		list2.add("B");
		List list3 = new ArrayList();
		list3.add("C");
		list3.add("D");
		
		xMap1.put("1",list1);
		xMap1.put("2",list2);

		xMap2.put("22",list3);

		containerMap.put("111",xMap1);
		containerMap.put("222",xMap2);
		
		Map fourthMap = new Hashtable();
		fourthMap.put("@",containerMap);
    
		//int maxDepth = depthOfMap(containerMap);
		String result = getJSEquivalentFor(fourthMap,"1");
		System.out.println("\n\nresult \n"+result);
		System.out.println("\n\ncontainerMap "+fourthMap);		
	}
	public static void main(String args[])
	{
		Map containerMap = new Hashtable();
		
		Map xMap1 = new Hashtable();
		Map xMap2 = new Hashtable();

		List list1 = new ArrayList();
		list1.add(new NameValueBean("A","A"));
		List list2 = new ArrayList();
		list2.add(new NameValueBean("B","B"));
		List list3 = new ArrayList();
		list3.add(new NameValueBean("C","C"));
		list3.add(new NameValueBean("D","D"));
		
		xMap1.put(new NameValueBean("1","1"),list1);
		xMap1.put(new NameValueBean("2","2"),list2);

		xMap2.put(new NameValueBean("22","22"),list3);

		containerMap.put(new NameValueBean("111","111"),xMap1);
		containerMap.put(new NameValueBean("222","222"),xMap2);
		
		Map fourthMap = new Hashtable();
		fourthMap.put(new NameValueBean("@","@"),containerMap);
    
		//int maxDepth = depthOfMap(containerMap);
		String result = getJSEquivalentFor(fourthMap,"1");
		System.out.println("\n\nresult \n"+result);
		System.out.println("\n\ncontainerMap "+fourthMap);		
	}
}
