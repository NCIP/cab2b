package edu.wustl.cab2b.client.ui.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.JXErrorDialog;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.MainSearchPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.datalist.IDataRow;
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
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Chetan B H
 * @author Mahesh Iyer
 */
public class CommonUtils {

    // Constant storing enumerated values for DAG-Images
    public static enum DagImages {
        DocumentPaperIcon, PortImageIcon, ArrowSelectIcon, ArrowSelectMOIcon, SelectIcon, selectMOIcon, ParenthesisIcon, ParenthesisMOIcon
    };

    /**
     * Method to get BusinessInterface object for given bean name and home class
     * object
     * 
     * @param beanName
     *            Name of the bean class
     * @param homeClassForBean
     *            HomeClass object for this bean
     * @param parentComponent
     *            The parent component which will be parent for displaying
     *            exception messages
     * @return the businessInterface object for given bean name
     */
    public static BusinessInterface getBusinessInterface(String beanName, Class homeClassForBean,
                                                         Component parentComponent) {
        BusinessInterface businessInterface = null;

        try {
            businessInterface = Locator.getInstance().locate(beanName, homeClassForBean);
        } catch (LocatorException e1) {
            handleException(e1, parentComponent, true, true, false, false);
        }

        return businessInterface;
    }

    /**
     * Method to disable all components from the specified container
     * @param container
     */
    public static void disableAllComponent(Container container) {
        for (int i = 0; i < container.getComponentCount(); i++) {
            container.getComponent(i).setEnabled(false);
            if (container.getComponent(i) instanceof Container)
                disableAllComponent((Container) container.getComponent(i));
        }
    }

    /**
     * Method to get BusinessInterface object for given bean name and home class
     * object
     * 
     * @param beanName
     *            Name of the bean class
     * @param homeClassForBean
     *            HomeClass object for this bean
     * @return the businessInterface object for given bean name
     * @throws LocatorException
     *             Throws exception if BusinessInterface is not located for
     *             given bean name
     */
    public static BusinessInterface getBusinessInterface(String beanName, Class homeClassForBean)
            throws LocatorException {
        return Locator.getInstance().locate(beanName, homeClassForBean);
    }

    /**
     * A Utility method to handle exception, logging and showing it in a dialog.
     */
    public static void handleException(Exception exception, Component parentComponent,
                                       boolean shouldShowErrorDialog, boolean shouldLogException,
                                       boolean shouldPrintExceptionInConsole, boolean shouldKillApp) {
        String errorMessageForLog = "";
        String errorMessageForDialog = "Error";

        /*
         * Cab2b application specific error code, available only with Cab2b's
         * CheckedException and RuntimeException
         */
        String errorCode = "";
        /*
         * Cab2b application specific error messages, corresponding to every
         * error code.
         */
        String customErrorMessage = "";

        if (exception instanceof CheckedException) {
            CheckedException checkedException = (CheckedException) exception;
            errorCode = checkedException.getErrorCode();
            customErrorMessage = ErrorCodeHandler.getErrorMessage(errorCode);

            errorMessageForDialog = customErrorMessage;
            errorMessageForLog = errorCode + ":" + customErrorMessage;
        } else if (exception instanceof RuntimeException) {
            RuntimeException runtimeException = (RuntimeException) exception;
            errorCode = runtimeException.getErrorCode();
            customErrorMessage = ErrorCodeHandler.getErrorMessage(errorCode);

            errorMessageForDialog = customErrorMessage;
            errorMessageForLog = errorCode + ":" + customErrorMessage;
        } else if (exception instanceof LocatorException) {
            LocatorException locatorException = (LocatorException) exception;
            errorCode = locatorException.getErrorCode();
            customErrorMessage = ErrorCodeHandler.getErrorMessage(errorCode);

            errorMessageForDialog = customErrorMessage;
            errorMessageForLog = errorCode + ":" + customErrorMessage;
        } else {
            errorMessageForLog = exception.getMessage();
        }

        if (shouldLogException) {
            Logger.out.error(errorMessageForLog, exception);
        }

        if (shouldShowErrorDialog) {
            JXErrorDialog.showDialog(parentComponent, "caB2B - Application Error", errorMessageForDialog,
                                     exception);
        }

        if (shouldPrintExceptionInConsole) {
            exception.printStackTrace();
        }

        if (shouldKillApp) {
            System.exit(0);
        }
    }

    /**
     * The method executes the encapsulated B2B query. For this it uses the
     * Locator service to locate an instance of the QueryEngineBusinessInterface
     * and uses the interace to remotely execute the query.
     */
    public static IQueryResult executeQuery(ICab2bQuery query, JComponent comp) throws RemoteException {
        QueryEngineBusinessInterface queryEngineBus = (QueryEngineBusinessInterface) getBusinessInterface(
                                                                                                          EjbNamesConstants.QUERY_ENGINE_BEAN,
                                                                                                          QueryEngineHome.class,
                                                                                                          comp);
        return executeQuery(query, queryEngineBus, comp);
    }

    /**
     * The method executes the encapsulated B2B query. For this it uses the
     * Locator service to locate an instance of the QueryEngineBusinessInterface
     * and uses the interace to remotely execute the query.
     */
    public static IQueryResult executeQuery(ICab2bQuery query, QueryEngineBusinessInterface queryEngineBus,
                                            JComponent comp) throws RemoteException {
        IQueryResult iQueryResult = null;
        try {
            iQueryResult = executeQuery(query, queryEngineBus);
        } catch (RuntimeException re) {
            handleException(re, comp, true, false, false, false);
        } /*catch (RemoteException e1) {
         //CheckedException e = new CheckedException(e1.getMessage(), e1, ErrorCodeConstants.QM_0004);
         handleException(getCab2bException(e1), comp, true, false, false, false);
         }*/
        return iQueryResult;
    }

    /**
     * The method executes the encapsulated B2B query. For this it uses the
     * Locator service to locate an instance of the QueryEngineBusinessInterface
     * and uses the interace to remotely execute the query.
     * 
     * @param query
     * @param queryEngineBus
     * @throws RuntimeException
     * @throws RemoteException
     */
    public static IQueryResult executeQuery(ICab2bQuery query, QueryEngineBusinessInterface queryEngineBus)
            throws RuntimeException, RemoteException {
        return queryEngineBus.executeQuery(query);
    }

    /**
     * Method to get count of bit 1 set in given BitSet
     * 
     * @param bitSet
     *            The BitSet object in which to find count
     * @return The count of 1 bit in BitSet
     */
    public static int getCountofOnBits(BitSet bitSet) {
        int count = 0;
        for (int i = 0; i < bitSet.size(); i++) {
            if (bitSet.get(i))
                count++;
        }
        return count;
    }

    /**
     * Utility method to find the number of occurrence of a particular character
     * in the String
     */
    public static int countCharacterIn(String str, char character) {
        int count = 0;

        char[] chars = str.toCharArray();

        for (char characterInStr : chars) {
            if (characterInStr == character)
                count++;
        }
        return count;
    }

    /**
     * Utility method to capitalise first character in the String
     */

    static String capitalizeFirstCharacter(String str) {
        char[] chars = str.toCharArray();
        char firstChar = chars[0];
        chars[0] = Character.toUpperCase(firstChar);
        return new String(chars);
    }

    /**
     * 
     */

    static String[] splitCamelCaseString(String str, int countOfUpperCaseLetter) {
        // System.out.println("str "+str+" count "+countOfUpperCaseLetter);
        String[] splitStrings = new String[countOfUpperCaseLetter + 1];

        char[] chars = str.toCharArray();
        int firstIndex = 0;
        int lastIndex = 0;

        int splitStrCount = 0;

        for (int i = 1; i < chars.length; i++) // change indexing from "chars"
        // 1 to length
        {
            char character = chars[i];
            char nextCharacter;
            char previousCharacter;
            if (splitStrCount != countOfUpperCaseLetter) {
                if (Character.isUpperCase(character)) {
                    if (i == (chars.length - 1)) {
                        splitStrings[splitStrCount++] = str.substring(0, i);

                        char[] lasrCharIsUpperCase = new char[1];
                        lasrCharIsUpperCase[0] = character;
                        splitStrings[splitStrCount++] = new String(lasrCharIsUpperCase);
                    } else {
                        lastIndex = i;

                        previousCharacter = chars[i - 1];
                        nextCharacter = chars[i + 1];
                        if (Character.isUpperCase(previousCharacter)
                                && Character.isLowerCase(nextCharacter)
                                || Character.isLowerCase(previousCharacter)
                                && Character.isUpperCase(nextCharacter)
                                || (Character.isLowerCase(previousCharacter) && Character.isLowerCase(nextCharacter))) {
                            String split = str.substring(firstIndex, lastIndex);
                            if (splitStrCount == 0) {
                                split = capitalizeFirstCharacter(split);
                            }
                            splitStrings[splitStrCount] = split;
                            splitStrCount++;
                            firstIndex = lastIndex;
                        } else {
                            continue;
                        }
                    }
                }
            } else {
                firstIndex = lastIndex;
                lastIndex = str.length();
                String split = str.substring(firstIndex, lastIndex);
                splitStrings[splitStrCount] = split;
                break;
            }

        }

        return splitStrings;
    }

    /**
     * Utility method to count upper case characters in the String
     */

    static int countUpperCaseLetters(String str) {
        /*
         * This is the count of Capital letters in a string excluding first
         * character, and continuos uppercase letter in the string.
         */
        int countOfCapitalLetters = 0;
        char[] chars = str.toCharArray();

        for (int i = 1; i < chars.length; i++) {
            char character = chars[i];
            char nextCharacter = 'x';
            char prevCharacter = chars[i - 1];

            if ((i + 1) < chars.length)
                nextCharacter = chars[i + 1];

            if ((Character.isUpperCase(character) && Character.isUpperCase(prevCharacter)
                    && Character.isLowerCase(nextCharacter) && i != chars.length - 1)
                    || (Character.isUpperCase(character) && Character.isLowerCase(prevCharacter) && Character.isUpperCase(nextCharacter))
                    || (Character.isUpperCase(character) && Character.isLowerCase(prevCharacter) && Character.isLowerCase(nextCharacter)))

                countOfCapitalLetters++;
        }
        return countOfCapitalLetters;
    }

    /**
     * Returns a fomatted string.
     * 
     * Example : -------------------------------------------
     * -----Input-------|------Output------------- xaQaUtWsdkjsSbAd > Xa Qa Ut
     * Wsdkjs Sb Ad tomDickAndHarry > Tom Dick And Harry id > Identifier
     * pubmedCount > Pubmed Count organism > Organism chromosomeMap > Chromosome
     * Map pubmed5Count > Pubmed5 Count 1234 > 1234
     * ---------------------------------------------
     * 
     * Note: first character should be in lower case.
     * 
     * @param str
     *            String to format.
     * @return formatted string.
     */
    public static String getFormattedString(String str) {
        String returner = "";

        if (str.equalsIgnoreCase("id")) {
            return "Identifier";
        }
        String[] splitStrings = null;

        int upperCaseCount = countUpperCaseLetters(str);
        if (upperCaseCount > 0) {
            splitStrings = splitCamelCaseString(str, upperCaseCount);
            returner = getFormattedString(splitStrings);
        } else {
            returner = capitalizeFirstCharacter(str);
        }
        return returner;
    }

    public static String getFormattedString(String[] splitStrings) {
        String returner = "";
        for (int i = 0; i < splitStrings.length; i++) {
            String str = splitStrings[i];
            if (i == splitStrings.length - 1) {
                returner += str;
            } else {
                returner += str + " ";
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
    public static Dimension getRelativeDimension(Dimension referenceDimnesion, float percentageWidth,
                                                 float percentageHeight) {
        if (referenceDimnesion.height <= 0 || referenceDimnesion.width <= 0) {
            throw new IllegalArgumentException("Reference dimension can't be (0,0) or less");
        }
        if ((0.0f > percentageHeight || percentageHeight > 1.0f)
                || (0.0f > percentageWidth || percentageWidth > 1.0f)) {
            throw new IllegalArgumentException(
                    "Percentage width and height should be less than 1.0 and greater than 1.0");
        }
        Dimension relativeDimension = new Dimension();

        relativeDimension.setSize(percentageWidth * referenceDimnesion.getWidth(), percentageHeight
                * referenceDimnesion.getHeight());

        return relativeDimension;
    }

    /**
     * Splits the string with qualifier
     * 
     * @param string
     * @param textQualifier
     * @param seperator
     */
    public static ArrayList<String> splitStringWithTextQualifier(String string, char textQualifier, char delimeter) {
        ArrayList<String> tokenList = new ArrayList<String>();
        StringBuffer token = new StringBuffer();
        boolean isTextQualifierStart = false;

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == textQualifier) {
                if (isTextQualifierStart == true) {
                    if (token.length() != 0) {
                        tokenList.add(token.toString().trim());
                    }
                    isTextQualifierStart = false;
                    token.setLength(0);
                } else {
                    isTextQualifierStart = true;
                }
            } else if (string.charAt(i) != delimeter) {
                token.append(string.charAt(i));
            } else {
                if (isTextQualifierStart == true) {
                    token.append(string.charAt(i));
                } else {
                    if (token.length() != 0) {
                        tokenList.add(token.toString().trim());
                    }
                    token.setLength(0);
                }
            }
        }

        //append last string
        String finalToken = token.toString().trim();
        if (isTextQualifierStart == true) {
            if (!finalToken.equals("")) {
                tokenList.add("\"" + finalToken);
            }
        } else {
            if (!finalToken.equals("")) {
                tokenList.add(finalToken);
            }
        }
        return tokenList;
    }

    /**
     * Removes any continuos space chatacters(2 or more) that may appear at the
     * begining, end or in between to words.
     * 
     * @param str
     *            string with continuos space characters.
     * @return procssed string with no continuos space characters.
     */
    public static String removeContinuousSpaceCharsAndTrim(String str) {
        char[] charsWithoutContinousSpaceChars = new char[str.length()];
        char prevCharacter = 'a';
        int index = 0;

        for (char character : str.toCharArray()) {
            if (!(Character.isWhitespace(character) && Character.isWhitespace(prevCharacter))) {
                charsWithoutContinousSpaceChars[index++] = character;
            }
            prevCharacter = character;
        }

        String result = new String(charsWithoutContinousSpaceChars);
        return result.trim();
    }

    /**
     * This method takes the node string and traverses the tree till it finds
     * the node matching the string. If the match is found the node is returned
     * else null is returned
     * 
     * @param nodeStr
     *            node string to search for
     * @return tree node
     */
    public static DefaultMutableTreeNode searchNode(DefaultMutableTreeNode rootNode, Object userObject) {
        DefaultMutableTreeNode node = null;

        // Get the enumeration
        Enumeration benum = rootNode.breadthFirstEnumeration();

        // iterate through the enumeration
        while (benum.hasMoreElements()) {
            // get the node
            node = (DefaultMutableTreeNode) benum.nextElement();

            // match the string with the user-object of the node
            if (userObject.equals(node.getUserObject())) {
                // tree node with string found
                return node;
            }
        }
        // tree node with string node found return null
        return null;
    }

    /**
     * Method to get identifier attribute index for given list of attibutes
     * 
     * @param attributes
     *            Ordered list of attributes
     * @return index of Id attribute
     */
    public static int getIdAttributeIndexFromAttributes(List<AttributeInterface> attributes) {
        int identifierIndex = -1;
        for (int i = 0; i < attributes.size(); i++) {
            AttributeInterface attribute = attributes.get(i);
            if (Utility.isIdentifierAttribute(attribute)) {
                identifierIndex = i;
                break;
            }
        }
        return identifierIndex;
    }

    /**
     * check the if the specified string is a valid name for an experiment,
     * category etc.
     * 
     * @param name
     *            the name
     * @return true if name is valid otherwise false
     */
    public static boolean isNameValid(String name) {
        if (name == null || name.trim().equals("")) {
            return false;
        }
        return true;
    }

    /**
     * This method returns the Component form the parent Container given the
     * name of the Component
     * 
     * @param parentContainer
     *            the parent Container
     * @param panelName
     *            name of the Component
     * @return the desired Component if present; null otherwise
     */
    public static Component getComponentByName(final Container parentContainer, final String panelName) {
        Component requiredComponent = null;
        Component[] components = parentContainer.getComponents();
        for (Component component : components) {
            String componentName = component.getName();
            if (componentName != null && componentName.equals(panelName)) {
                requiredComponent = component;
                break;
            }
        }
        return requiredComponent;
    }

    /**
     * This Method returns the maximum dimension required to generate the label
     * for the given attributes.
     * 
     * @param attributeList
     * @return
     */
    public static Dimension getMaximumLabelDimension(Collection<AttributeInterface> attributeList) {
        Dimension maxLabelDimension = new Dimension(0, 0);
        for (AttributeInterface attribute : attributeList) {
            String formattedString = attribute.getName();
            if (!Utility.isCategory(attribute.getEntity())) {
                formattedString = CommonUtils.getFormattedString(formattedString);
            }

            Cab2bLabel tempLabel = new Cab2bLabel(formattedString + " : ");
            if (maxLabelDimension.width < tempLabel.getPreferredSize().width) {
                maxLabelDimension = tempLabel.getPreferredSize();
            }
        }
        return maxLabelDimension;
    }

    /**
     * @return Popular categories at this point
     */
    public static Vector getPopularSearchCategories() {
        /* TODO These default Search Categories will be removed after its support*/
        Vector<String> popularSearchCategories = new Vector<String>();
        popularSearchCategories.add("Gene Annotation");
        popularSearchCategories.add("Genomic Identifiers");
        popularSearchCategories.add("Literature-based Gene Association");
        //popularSearchCategories.add("Microarray Annotation");
        popularSearchCategories.add("Orthologus Gene");
        return popularSearchCategories;
    }

    /**
     * @return Recent search queries
     */
    public static Vector getUserSearchQueries() {
        /* TODO These default UserSearchQueries will be removed later after SAVE QUERY support*/
        Vector<String> userSearchQueries = new Vector<String>();
        userSearchQueries.add("Prostate Cancer Microarray Data");
        userSearchQueries.add("Glioblastoma Microarray Data");
        userSearchQueries.add("High Quality RNA Biospecimens");
        //userSearchQueries.add("Breast Cancer Microarrays (MOE430+2.0)");

        userSearchQueries.add("Adenocarcinoma Specimens");
        userSearchQueries.add("Lung Primary Tumor");
        return userSearchQueries;
    }

    /**
     * @return All the experiments performed by the user.
     */
    public static Vector getExperiments() {
        /* TODO These default experiments will be removed later on*/
        Vector<String> experiments = new Vector<String>();
        experiments.add("Breast Cancer Microarrays (Hu133+2.0)");
        experiments.add("Comparative study of specimens between pre and post therapy");
        experiments.add("Acute Myelogenous Leukemia Microarrays");
        experiments.add("Patients with newly diagnose Adenocarcinoma");
        return experiments;
    }

    /**
     * @return Popular categories at this point
     */
    public static Vector getUserSearchCategories() {
        /* TODO These default Search Categories will be removed after its support*/
        Vector<String> userSearchCategories = new Vector<String>();
        //userSearchCategories.add("Gene Annotation");
        userSearchCategories.add("Microarray Annotation");
        userSearchCategories.add("Tissue Biospecimens");
        userSearchCategories.add("Molecular Biospecimens");
        userSearchCategories.add("Participant Details");
        return userSearchCategories;
    }

    public static Exception getCab2bException(Exception e) {
        while (e.getCause() != null) {
            if (e.getCause() instanceof Exception) {
                Exception e1 = (Exception) e.getCause();
                if (e1 instanceof CheckedException || e1 instanceof RuntimeException) {
                    return e1;
                }
                e = e1;
            }
        }
        return e;
    }

    public static String escapeString(String input) {
        if (input.indexOf(",") != -1) {
            input = "\"" + input + "\"";
        }
        return input;
    }  

    /**
     * Method which will return number of elements in current datalist
     * @return
     */
    public static int getDataListSize() {
        IDataRow rootNode = MainSearchPanel.getDataList().getRootDataRow();
        // This node is hidden node in the tree view
        return rootNode.getChildren().size();

    }
}
