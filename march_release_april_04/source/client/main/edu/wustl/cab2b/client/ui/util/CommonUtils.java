package edu.wustl.cab2b.client.ui.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.JXErrorDialog;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineHome;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeHandler;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.locator.Locator;
import edu.wustl.cab2b.common.locator.LocatorException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.common.util.logger.Logger;

public class CommonUtils
{
	public static Frame FrameReference = null;
	
	// Constant storing enumerated values for DAG-Images
	public static enum DagImageConstants {DocumentPaperIcon,
										  PortImageIcon, 
										  ArrowSelectIcon, ArrowSelectMOIcon,
										  SelectIcon, selectMOIcon,
										  ParenthesisIcon, ParenthesisMOIcon};
	
	/**
	 * Method to get BusinessInterface object for given bean name and home class object
	 * @param beanName Name of the bean class
	 * @param homeClassForBean HomeClass object for this bean
	 * @param parentComponent The parent component which will be parent for displaying exception messages
	 * @return the businessInterface object for given bean name
	 */
	public static BusinessInterface getBusinessInterface(String beanName, Class homeClassForBean, Component parentComponent)
	{
		BusinessInterface businessInterface = null;
		
		try
		{
			businessInterface = getBusinessInterface(beanName, homeClassForBean);
		}
		catch (LocatorException e1)
		{
			handleException(e1, parentComponent, true, true, false, false);
		}
		
		return businessInterface;
	}
	
	/**
	 * Method to get BusinessInterface object for given bean name and home class object
	 * @param beanName Name of the bean class
	 * @param homeClassForBean HomeClass object for this bean
	 * @return the businessInterface object for given bean name
	 * @throws LocatorException Throws exception if BusinessInterface is not located for given bean name
	 */
	public static BusinessInterface getBusinessInterface(String beanName, Class homeClassForBean)throws LocatorException
	{
		BusinessInterface businessInterface = null;
		
		try
		{
			businessInterface = Locator.getInstance().locate(beanName, homeClassForBean);
		}
		catch (LocatorException e1)
		{
			throw e1;
		}
		
		return businessInterface;
	}
	
	/** 
	 * A Utility method to handle exception, logging and showing it in a dialog. 
	 **/
	public static void handleException(Exception exception, Component parentComponent, boolean shouldShowErrorDialog, 
											boolean shouldLogException, boolean shouldPrintExceptionInConsole, 
											boolean shouldKillApp)
	{
		String errorMessageForLog = "";
		String errorMessageForDialog = "Error";
		
		/* Cab2b application specific error code, available only with Cab2b's CheckedException and RuntimeException  */
		String errorCode = "";
		/* Cab2b application specific error messages, corresponding to every error code.  */
		String customErrorMessage = "";
		
		if(exception instanceof CheckedException )
		{
			CheckedException checkedException = (CheckedException) exception;
			errorCode = checkedException.getErrorCode();
			customErrorMessage = ErrorCodeHandler.getErrorMessage(errorCode);
			
			errorMessageForDialog = customErrorMessage;  
			errorMessageForLog =  errorCode+":"+customErrorMessage;
		}else if(exception instanceof RuntimeException) 
		{
			RuntimeException runtimeException = (RuntimeException) exception;
			errorCode = runtimeException.getErrorCode();
			customErrorMessage = ErrorCodeHandler.getErrorMessage(errorCode);
			
			errorMessageForDialog = customErrorMessage;  
			errorMessageForLog =  errorCode+":"+customErrorMessage;
		}else
		{
			errorMessageForLog = exception.getMessage();
		}
		if(shouldLogException)
		{
			Logger.out.error(errorMessageForLog, exception);
		}
		if(shouldShowErrorDialog)
		{
			JXErrorDialog.showDialog(parentComponent, "caB2B - Application Error",errorMessageForDialog, exception);
		}
		if(shouldPrintExceptionInConsole)
		{
			exception.printStackTrace();
		}
		if(shouldKillApp)
		{
			System.exit(0);
		}
	}
	
	
	/**
	 * The method executes the encapsulated B2B query. For this it uses the
	 * Locator service to locate an instance of the QueryEngineBusinessInterface
	 * and uses the interace to remotely execute the query.
	 */
    public static IQueryResult executeQuery(ICab2bQuery query, JComponent comp)
    {
    	IQueryResult iQueryResult =  null;
    	QueryEngineBusinessInterface queryEngineBus = (QueryEngineBusinessInterface)getBusinessInterface(
    																		EjbNamesConstants.QUERY_ENGINE_BEAN, 
    																		QueryEngineHome.class, comp);
		try
		{
			iQueryResult = executeQuery(query, queryEngineBus);
		}
		catch(RuntimeException re)
		{
			handleException(re, comp, true, true, false, false);
		}
		catch (RemoteException e1)
		{
			handleException(e1, comp, true, true, false, false);
		}
    	return iQueryResult;
    }
    /**
	 * The method executes the encapsulated B2B query. For this it uses the
	 * Locator service to locate an instance of the QueryEngineBusinessInterface
	 * and uses the interace to remotely execute the query.
	 */
    public static IQueryResult executeQuery(ICab2bQuery query, QueryEngineBusinessInterface queryEngineBus, JComponent comp)
    {
    	IQueryResult iQueryResult =  null;
    	try
		{
			iQueryResult = executeQuery(query, queryEngineBus);
		}
		catch(RuntimeException re)
		{
			handleException(re, comp, true, true, false, false);
		}
		catch (RemoteException e1)
		{
			handleException(e1, comp, true, true, false, false);
		}
    	return iQueryResult;
    }
	/**
	 * The method executes the encapsulated B2B query. For this it uses the
	 * Locator service to locate an instance of the QueryEngineBusinessInterface
	 * and uses the interace to remotely execute the query.
	 */
    public static IQueryResult executeQuery(ICab2bQuery query, QueryEngineBusinessInterface queryEngineBus)
    throws RuntimeException, RemoteException
    {
    	IQueryResult iQueryResult =  null;
    	try
		{
			iQueryResult = queryEngineBus.executeQuery(query);
		}
		catch(RuntimeException re)
		{
			throw re;
		}
		catch (RemoteException e1)
		{
			throw e1;
		}
    	return iQueryResult;
    }
    
    /**
     * Method to get count of bit 1 set in given BitSet
     * @param bitSet The BitSet object in which to find count
     * @return The count of 1 bit in BitSet
     */
	public static int getCountofOnBits(BitSet bitSet)
	{
		int count = 0;
		for(int i = 0; i < bitSet.size(); i++)
		{
			if(bitSet.get(i))
				count++;
		}
		return count;
	}
    /** 
     * Utility method to find the number of occurrence of a particular character in the String 
     **/
	public int countCharacterIn(String str, char character)
	{
		int count = 0;
		
		char[] chars = str.toCharArray();
		
		for(char characterInStr : chars)
		{
			if(characterInStr == character)
				count++;
		}
		return count;
	}
    
	protected static String capitalizeString(String str)
	{
		String returner = "";
		//System.out.println("STRING to CAPITALIZE "+str);
		char[] chars = str.toCharArray();
		
		char firstChar = chars[0];
		chars[0] = Character.toUpperCase(firstChar);
		
		returner = new String(chars);
		
		return returner;
	}
	
    protected static String[] splitCamelCaseString(String str, int countOfUpperCaseLetter)
    {
    	//System.out.println("str "+str+" count "+countOfUpperCaseLetter);
    	String[] splitStrings = new String[countOfUpperCaseLetter+1];
    	
    	char[] chars = str.toCharArray();
    	int firstIndex = 0;
    	int lastIndex = 0;
    	
    	int splitStrCount = 0;
    	
    	for(int i = 1; i < chars.length; i++) // change indexing from "chars" 1 to length
    	{
    		char character = chars[i];
    		char nextCharacter;
    		char previousCharacter;
    		if(splitStrCount != countOfUpperCaseLetter)
    		{
    			if(Character.isUpperCase(character))
    			{
    				if(i == (chars.length-1))
    				{
    					splitStrings[splitStrCount++] = str.substring(0, i);
    					
    					char[] lasrCharIsUpperCase = new char[1];
    					lasrCharIsUpperCase[0] = character;
    					splitStrings[splitStrCount++] = new String(lasrCharIsUpperCase);
    				}else
    				{
    					lastIndex = i;
    					
    					previousCharacter = chars[i-1];
    					nextCharacter = chars[i+1];
    					if(Character.isUpperCase(previousCharacter) && Character.isLowerCase(nextCharacter) || 
    							Character.isLowerCase(previousCharacter) && Character.isUpperCase(nextCharacter) || 
    							(Character.isLowerCase(previousCharacter) && Character.isLowerCase(nextCharacter)))
    					{
        					String split = str.substring(firstIndex, lastIndex);
        					if(splitStrCount == 0)
        					{
        						split = capitalizeString(split);
        					}
        					splitStrings[splitStrCount] = split;
        					splitStrCount++;
        					firstIndex = lastIndex;
    					}else
    					{
    						continue;
    					}
    				}
    			}
    		}else
    		{
    			firstIndex = lastIndex;
    			lastIndex = str.length();
    			String split = str.substring(firstIndex, lastIndex);
    			splitStrings[splitStrCount] = split;
    			break;
    		}
    		
    	}
    	
    	return splitStrings;
    }
    
    protected static int countUpperCaseLetters(String str)
    {
    	/* This is the count of Capital letters in a string excluding first character, 
    	 * and continuos uppercase letter in the string. */
    	int countOfCapitalLetters = 0;
    	char[] chars = str.toCharArray();
    	
    	for(int i = 1; i < chars.length; i++)
    	{
    		char character = chars[i];
    		char nextCharacter = 'x';
    		char prevCharacter = chars[i-1];
    		
    		if((i+1) < chars.length)
    			nextCharacter = chars[i+1];
    		
    		if((Character.isUpperCase(character) && Character.isUpperCase(prevCharacter) && Character.isLowerCase(nextCharacter) && i != chars.length-1)  || 
    				(Character.isUpperCase(character) && Character.isLowerCase(prevCharacter) && Character.isUpperCase(nextCharacter)) ||
    				(Character.isUpperCase(character) && Character.isLowerCase(prevCharacter) && Character.isLowerCase(nextCharacter)))
    		
    			countOfCapitalLetters++;
    	}
    	return countOfCapitalLetters;
    }
    
    /**
     * Returns a fomatted string.
     * 
     * Example :
     *  -------------------------------------------
     *  -----Input-------|------Output-------------
     	xaQaUtWsdkjsSbAd  > Xa Qa Ut Wsdkjs Sb Ad
		tomDickAndHarry   > Tom Dick And Harry
		id                > Identifier
		pubmedCount	      > Pubmed Count
		organism          > Organism
		chromosomeMap     > Chromosome Map
		pubmed5Count      > Pubmed5 Count
		1234              > 1234
	 *	---------------------------------------------
     * 
     * Note: first character should be in lower case.
     * 
     * @param str String to format.
     * @return formatted string.
     */
    public static String getFormattedString(String str)
    {
    	String returner = "";
    	
    	if(str.equalsIgnoreCase("id"))
    	{
    		return "Identifier";
    	}
    	String[] splitStrings = null;
    	
    	int upperCaseCount = countUpperCaseLetters(str);
    	if(upperCaseCount > 0)
    	{
    		splitStrings = splitCamelCaseString(str, upperCaseCount);
    		returner = getFormattedString(splitStrings);
    	}
    	else
    	{
    		returner = capitalizeString(str);
    	}
    	return returner;
    }
    
    
    protected static String getFormattedString(String[] splitStrings)
    {
    	String returner = "";
    	for(int i = 0; i < splitStrings.length; i++)
    	{
    		String str = splitStrings[i];
    		if(i == splitStrings.length-1)
    		{
    			returner += str;
    		}else
    		{
    			returner += str+" ";
    		}
    	}
    	return returner;
    }
    
    /**
     * Utility method to get a Dimension relative to a reference Dimension.
     * 
     * @param referenceDimnesion
     * @param percentageWidth
     * @param percentageHeight
     * @return a new relative dimension.
     */
    public static Dimension getRelativeDimension(Dimension referenceDimnesion, float percentageWidth, float percentageHeight)
    {
    	if(referenceDimnesion.height <= 0 || referenceDimnesion.width <= 0)
    	{
    		throw new IllegalArgumentException("Reference dimension can't be (0,0) or less");
    	}
    	if((0.0f > percentageHeight || percentageHeight > 1.0f) || 
    			(0.0f > percentageWidth || percentageWidth > 1.0f))
    	{
    		throw new IllegalArgumentException("Percentage width and height should be less than 1.0 and greater than 1.0");
    	}
    	Dimension relativeDimension = new Dimension();
    	
    	relativeDimension.setSize(
    				percentageWidth * referenceDimnesion.getWidth(), 
    				percentageHeight * referenceDimnesion.getHeight() );
    	Logger.out.info("relative dimension " + 
    					relativeDimension + " reference dimension "+
    					referenceDimnesion + " percentage(width, height): ("+
    					percentageWidth+","+percentageHeight+")");
    	return relativeDimension;
    }
    
    public static void main(String[] args)
	{
    	/*Logger.configure("");
		String[] sampleStrs = {"xaQaUtWsdkjsSbAd","tomDickAndHarry","id","pubmedCount","organism","chromosomeMap","pubmed5Count","1234"};
		for(String str: sampleStrs)
			System.out.println("Formatted String ####>>> "+getFormattedString(str));
		
		Dimension srcDimension = Toolkit.getDefaultToolkit().getScreenSize();
		getRelativeDimension(srcDimension,0.75f, 0.5f);*/
    	splitStringWithTextQualifier("\"prat,ibha\", \"fdf\"vishaldhok\"", '"', ',');
	}
    
    /**
     * Splits the string with qualifier
     * @param string
     * @param textQualifier
     * @param seperator
     */
    public static ArrayList<String> splitStringWithTextQualifier(String string, char textQualifier, char seperator)
    {
    	
    	ArrayList<String> list = new ArrayList<String>();
    	StringBuffer sb = new StringBuffer();
    	boolean isTextQualifierStart = false;
    	for(int i=0; i<string.length(); i++)
    	{
    		if(string.charAt(i) == textQualifier)
    		{
    			if(isTextQualifierStart == true)
    			{
    				if(sb.length() != 0)
    				{
    					list.add(sb.toString());
    				}
    				isTextQualifierStart = false;
        			sb.setLength(0);
    			}
    			else
    			{
    				isTextQualifierStart = true;
    			}
    		}
    		else if(string.charAt(i) != seperator)
    		{
    			sb.append(string.charAt(i));
    		}
    		else
    		{
    			if(isTextQualifierStart == true)
    			{
    				sb.append(string.charAt(i));
    			}
    			else
    			{
    				if(sb.length() != 0)
    				{
    					list.add(sb.toString());
    				}
    				sb.setLength(0);
    			}
    			
    		}
    	}
    	// append last string
    	if(isTextQualifierStart == true)
    	{
    		if(sb.length() != 0)
			{
    			list.add("\"" + sb.toString());	
			}
    	}
    	else
    	{
    		if(sb.length() != 0)
			{
    			list.add(sb.toString());
			}
    	}
    	return list;
    }
    
    /**
     * Removes any continuos space chatacters(2 or more) that may appear at the begining, 
     * end or in between to words.
     * @param str string with continuos space characters.
     * @return procssed string with no continuos space characters.
     */
    public static String removeContinuousSpaceCharsAndTrim(String str)
    {
    	String result = "";
    	
    	char[] chars = str.toCharArray();
    	
    	char[] charsWithoutContinousSpaceChars = new char[chars.length]; 
    	
    	char character;
    	char prevCharacter = 'a';
    	int index = 0;
    	
    	for(int i = 0; i < chars.length; i++)
    	{
    		character = chars[i];
    		if(!Character.isSpaceChar(character) && !Character.isSpaceChar(prevCharacter) || 
    				(Character.isSpaceChar(character) && !Character.isSpaceChar(prevCharacter)) ||
    				(!Character.isSpaceChar(character)) && Character.isSpaceChar(prevCharacter))
    		{
    			charsWithoutContinousSpaceChars[index++] = character;
    		}
    		prevCharacter = character;
    	}
    	
    	result = new String(charsWithoutContinousSpaceChars);
    	result = result.trim();
    	return result;
    }
    
    
    public static int getQueryRecordCount(IQueryResult queryResults)
    {
    	return 0;
    }
    
    /** 
     * This method takes the node string and 
     * traverses the tree till it finds the node 
     * matching the string. If the match is found  
     * the node is returned else null is returned 
     *  
     * @param nodeStr node string to search for 
     * @return tree node  
     */ 
    public static DefaultMutableTreeNode searchNode(DefaultMutableTreeNode rootNode, Object userObject) 
    { 
        DefaultMutableTreeNode node = null; 
         
        //Get the enumeration 
        Enumeration benum = rootNode.breadthFirstEnumeration(); 
         
        //iterate through the enumeration 
        while(benum.hasMoreElements()) 
        { 
            //get the node 
            node = (DefaultMutableTreeNode)benum.nextElement(); 
             
            //match the string with the user-object of the node 
            if(userObject.equals(node.getUserObject())) 
            { 
                //tree node with string found 
                return node;                          
            } 
        } 
        //tree node with string node found return null 
        return null; 
    } 
    
    /**
	 * Method to get identifier attribute index for given list of attibutes
	 * @param attributes Ordered list of attributes
	 * @return index of Id attribute
	 */
	public static int getIdAttributeIndexFromAttributes(List<AttributeInterface> attributes)
	{
		int identifierIndex = -1;
		for (int i = 0; i < attributes.size(); i++)
		{
			AttributeInterface attribute = attributes.get(i);
			String attribName = attribute.getName();
			if (attribName.equalsIgnoreCase("id") || attribName.equalsIgnoreCase("identifier"))
			{
				identifierIndex = i;
				break;
			}
		}
		return identifierIndex;
	}
}