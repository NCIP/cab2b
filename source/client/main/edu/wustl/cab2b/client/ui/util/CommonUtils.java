package edu.wustl.cab2b.client.ui.util;

import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.SEARCH_FRAME_TITLE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.APPLICATION_RESOURCES_FILE_NAME;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.ERROR_CODE_FILE_NAME;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.LIMIT_CONNECT_DESELECTED;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.LIMIT_CONNECT_SELECTED;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.PAPER_GRID_ADD_LIMIT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.PARENTHISIS_ICON_ADD_LIMIT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.PARENTHISIS_ICON_ADD_LIMIT_MOUSEOVER;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.PORT_IMAGE_ADD_LIMIT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.SELECT_ICON_ADD_LIMIT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.SELECT_ICON_ADD_LIMIT_MOUSEOVER;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.openide.util.Utilities;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.JXErrorDialog;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.cache.ClientSideCache;
import edu.wustl.cab2b.client.cache.UserCache;
import edu.wustl.cab2b.client.ui.MainSearchPanel;
import edu.wustl.cab2b.client.ui.SaveDatalistPanel;
import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.mainframe.GlobalNavigationPanel;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.mainframe.UserValidator;
import edu.wustl.cab2b.client.ui.mainframe.stackbox.MainFrameStackedBoxPanel;
import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.category.CategoryBusinessInterface;
import edu.wustl.cab2b.common.ejb.category.CategoryHomeInterface;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineHome;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeHandler;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface;
import edu.wustl.cab2b.common.experiment.ExperimentHome;
import edu.wustl.cab2b.common.locator.Locator;
import edu.wustl.cab2b.common.locator.LocatorException;
import edu.wustl.cab2b.common.queryengine.Cab2bQuery;
import edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IParameterizedCondition;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.impl.Condition;
import edu.wustl.common.querysuite.queryobject.impl.Constraints;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedCondition;
import edu.wustl.common.querysuite.queryobject.impl.QueryEntity;
import edu.wustl.common.querysuite.queryobject.impl.Rule;
import edu.wustl.common.util.global.ApplicationProperties;
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
	public static BusinessInterface getBusinessInterface(String beanName,
			Class homeClassForBean, Component parentComponent) {
		BusinessInterface businessInterface = null;
		try {
			businessInterface = Locator.getInstance().locate(beanName,
					homeClassForBean);
		} catch (LocatorException e1) {
			handleException(e1, parentComponent, true, true, false, false);
		}
		return businessInterface;
	}

	/**
	 * Method to disable all components from the specified container
	 * 
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
	 * @param query
	 * @return
	 */
	public static ICab2bParameterizedQuery copyQueryObject(ICab2bQuery query) {
		// New Query
		Cab2bQuery newCab2bQuery = new Cab2bQuery();
		newCab2bQuery.setOutputEntity(query.getOutputEntity());
		newCab2bQuery
				.setOutputUrls(new ArrayList<String>(query.getOutputUrls()));

		// New Constraint
		IConstraints newConstraints = new Constraints();
		IConstraints constraints = query.getConstraints();

		// Enumeration<IExpressionId> expressionIds =
		// constraints.getExpressionIds();
		Map<IExpression, IExpression> mapOldToNew = new HashMap<IExpression, IExpression>();
		for (IExpression oldExpression : constraints) {
			// IExpression oldExpression = (Expression)
			// constraints.getExpression(expressionIds.nextElement());

			// New Expression
			IQueryEntity newQueryEntity = new QueryEntity(oldExpression
					.getQueryEntity().getDynamicExtensionsEntity());
			IExpression newExpression = newConstraints
					.addExpression(newQueryEntity);
			newExpression.setInView(oldExpression.isInView());
			newExpression.setVisible(oldExpression.isVisible());
			mapOldToNew.put(oldExpression, newExpression);
		}

		for (IExpression oldExpression : constraints) {
			IExpressionOperand newOperand = getNewExpressionOperand(
					oldExpression, 0, mapOldToNew);
			IExpression newExpression = mapOldToNew.get(oldExpression);
			newExpression.addOperand(newOperand);
			for (int index = 1; index < oldExpression.numberOfOperands(); index++) {
				newOperand = getNewExpressionOperand(oldExpression, index,
						mapOldToNew);

				// New LogicalConnector
				IConnector<LogicalOperator> oldLogicalConnector = (IConnector<LogicalOperator>) oldExpression
						.getConnector(index - 1, index);
				IConnector<LogicalOperator> newLogicalConnector = QueryObjectFactory
						.createLogicalConnector(oldLogicalConnector
								.getOperator(), oldLogicalConnector
								.getNestingNumber());
				newExpression.addOperand(newLogicalConnector, newOperand);
			}
		}
		newCab2bQuery.setConstraints(newConstraints);

		return newCab2bQuery;
	}

	/**
	 * @param expression
	 * @param index
	 * @param mapOldToNew
	 * @return
	 */
	private static IExpressionOperand getNewExpressionOperand(
			IExpression expression, int index,
			Map<IExpression, IExpression> mapOldToNew) {
		IExpressionOperand operand = expression.getOperand(index);
		operand.setId(null);

		IExpressionOperand newOperand = null;
		if (operand instanceof IRule) {
			Rule rule = (Rule) operand;

			// New Rule
			Rule newRule = new Rule();
			for (ICondition condition : rule.getConditions()) {
				// New Condition
				ICondition newCondition = null;
				if (condition instanceof IParameterizedCondition) {
					ParameterizedCondition oldParameterizedCondition = (ParameterizedCondition) condition;
					ParameterizedCondition newParameterizedCondition = new ParameterizedCondition(
							condition, oldParameterizedCondition.getIndex(),
							oldParameterizedCondition.getName());
					newCondition = newParameterizedCondition;
				} else {
					newCondition = new Condition();
					newCondition.setAttribute(condition.getAttribute());
					// newCondition.setAttributeId(condition.getAttribute().getId());

					newCondition.setRelationalOperator(condition
							.getRelationalOperator());
					// newCondition.setRelationalOperatorString(condition.getRelationalOperator().getStringRepresentation());

					newCondition.setValues(new ArrayList<String>(condition
							.getValues()));
				}
				newRule.addCondition(newCondition);
			}

			newOperand = newRule;
		} else if (operand instanceof IExpression) {
			newOperand = mapOldToNew.get(operand);
		}

		return newOperand;
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
	public static BusinessInterface getBusinessInterface(String beanName,
			Class homeClassForBean) throws LocatorException {
		return Locator.getInstance().locate(beanName, homeClassForBean);
	}

	/**
	 * A Utility method to handle exception, logging and showing it in a dialog.
	 */
	public static void handleException(Exception exception,
			Component parentComponent, boolean shouldShowErrorDialog,
			boolean shouldLogException, boolean shouldPrintExceptionInConsole,
			boolean shouldKillApp) {
		String errorMessageForLog = "";
		String errorMessageForDialog = "Error";
		Exception e = getOriginalException(exception);
		if (e != null) {
			exception = e;
		}

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
		} else if (exception instanceof LocatorException) {
			LocatorException locatorException = (LocatorException) exception;
			errorCode = locatorException.getErrorCode();
			customErrorMessage = ErrorCodeHandler.getErrorMessage(errorCode);

			errorMessageForDialog = customErrorMessage;
			errorMessageForLog = errorCode + ":" + customErrorMessage;
		} else if (exception instanceof RuntimeException) {
			RuntimeException runtimeException = (RuntimeException) exception;
			errorCode = runtimeException.getErrorCode();
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
			JXErrorDialog.showDialog(parentComponent,
					"caB2B - Application Error", errorMessageForDialog,
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
	 * This method traverses the whole hierarchy of the given exception to check
	 * whether there is any exception which is {@link CheckedException} OR
	 * {@link LocatorException} OR{@link RuntimeException} If yes it returns it
	 * otherwise it returns the original passed exception.
	 * 
	 * @param cause
	 *            exception to verify
	 * @return The root exception
	 */
	private static Exception getOriginalException(Exception cause) {
		if (cause != null && cause instanceof Exception) {
			if (cause instanceof CheckedException
					|| cause instanceof LocatorException
					|| cause instanceof RuntimeException) {
				return cause;
			} else {
				return getOriginalException((Exception) cause.getCause());
			}
		}
		return cause;
	}

	/**
	 * The method executes the encapsulated B2B query. For this it uses the
	 * Locator service to locate an instance of the QueryEngineBusinessInterface
	 * and uses the interace to remotely execute the query.
	 */
	public static IQueryResult executeQuery(ICab2bQuery query, JComponent comp)
			throws RemoteException {
		QueryEngineBusinessInterface queryEngineBus = (QueryEngineBusinessInterface) getBusinessInterface(
				EjbNamesConstants.QUERY_ENGINE_BEAN, QueryEngineHome.class,
				comp);
		return executeQuery(query, queryEngineBus, comp);
	}

	/**
	 * The method executes the encapsulated B2B query. For this it uses the
	 * Locator service to locate an instance of the QueryEngineBusinessInterface
	 * and uses the interace to remotely execute the query.
	 */
	public static IQueryResult executeQuery(ICab2bQuery query,
			QueryEngineBusinessInterface queryEngineBus, JComponent comp)
			throws RemoteException {
		IQueryResult iQueryResult = null;
		try {
			iQueryResult = executeQuery(query, queryEngineBus);
		} catch (RuntimeException re) {
			handleException(re, comp, true, false, false, false);
		} /*
			 * catch (RemoteException e1) { //CheckedException e = new
			 * CheckedException(e1.getMessage(), e1,
			 * ErrorCodeConstants.QM_0004);
			 * handleException(getCab2bException(e1), comp, true, false, false,
			 * false); }
			 */
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
	public static IQueryResult executeQuery(ICab2bQuery query,
			QueryEngineBusinessInterface queryEngineBus)
			throws RuntimeException, RemoteException {
		return queryEngineBus.executeQuery(query, UserValidator.getProxy());
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
		//  
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
						splitStrings[splitStrCount++] = new String(
								lasrCharIsUpperCase);
					} else {
						lastIndex = i;

						previousCharacter = chars[i - 1];
						nextCharacter = chars[i + 1];
						if (Character.isUpperCase(previousCharacter)
								&& Character.isLowerCase(nextCharacter)
								|| Character.isLowerCase(previousCharacter)
								&& Character.isUpperCase(nextCharacter)
								|| (Character.isLowerCase(previousCharacter) && Character
										.isLowerCase(nextCharacter))) {
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

			if ((Character.isUpperCase(character)
					&& Character.isUpperCase(prevCharacter)
					&& Character.isLowerCase(nextCharacter) && i != chars.length - 1)
					|| (Character.isUpperCase(character)
							&& Character.isLowerCase(prevCharacter) && Character
							.isUpperCase(nextCharacter))
					|| (Character.isUpperCase(character)
							&& Character.isLowerCase(prevCharacter) && Character
							.isLowerCase(nextCharacter)))

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
	public static Dimension getRelativeDimension(Dimension referenceDimnesion,
			float percentageWidth, float percentageHeight) {
		if (referenceDimnesion.height <= 0 || referenceDimnesion.width <= 0) {
			throw new IllegalArgumentException(
					"Reference dimension can't be (0,0) or less");
		}
		if ((0.0f > percentageHeight || percentageHeight > 1.0f)
				|| (0.0f > percentageWidth || percentageWidth > 1.0f)) {
			throw new IllegalArgumentException(
					"Percentage width and height should be less than 1.0 and greater than 1.0");
		}
		Dimension relativeDimension = new Dimension();

		relativeDimension.setSize(percentageWidth
				* referenceDimnesion.getWidth(), percentageHeight
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
	public static ArrayList<String> splitStringWithTextQualifier(String string,
			char textQualifier, char delimeter) {
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

		// append last string
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
			if (!(Character.isWhitespace(character) && Character
					.isWhitespace(prevCharacter))) {
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
	public static DefaultMutableTreeNode searchNode(
			DefaultMutableTreeNode rootNode, Object userObject) {
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
	public static int getIdAttributeIndexFromAttributes(
			List<AttributeInterface> attributes) {
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
	public static Component getComponentByName(final Container parentContainer,
			final String panelName) {
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
	public static Dimension getMaximumLabelDimension(
			Collection<AttributeInterface> attributeList) {
		Dimension maxLabelDimension = new Dimension(0, 0);
		for (AttributeInterface attribute : attributeList) {
			String formattedString = attribute.getName();
			if (!Utility.isCategory(attribute.getEntity())) {
				formattedString = CommonUtils
						.getFormattedString(formattedString);
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
	public static Vector<Category> getPopularSearchCategories() {
		Vector<Category> userSearchCategories = new Vector<Category>();

		// TO-Do Change it for Popular categories
		List<Category> categoryList = ClientSideCache.getInstance()
				.getAllCategories();
		for (Category category : categoryList) {
			userSearchCategories.add(category);
		}
		return userSearchCategories;

	}

	

	/**
	 * @return Recent search queries
	 */
	public static Vector getUserSearchQueries() {
		/*
		 * TODO These default UserSearchQueries will be removed later after SAVE
		 * QUERY support
		 */
		Vector<String> userSearchQueries = new Vector<String>();
		userSearchQueries.add("Prostate Cancer Microarray Data");
		userSearchQueries.add("Glioblastoma Microarray Data");
		userSearchQueries.add("High Quality RNA Biospecimens");
		// userSearchQueries.add("Breast Cancer Microarrays (MOE430+2.0)");

		userSearchQueries.add("Adenocarcinoma Specimens");
		userSearchQueries.add("Lung Primary Tumor");
		return userSearchQueries;
	}

	/**
	 * @return All the experiments performed by the user.
	 */
	public static Vector<Experiment> getExperiments(Component comp,
			String userName) {

		ExperimentBusinessInterface expBus = (ExperimentBusinessInterface) CommonUtils
				.getBusinessInterface(EjbNamesConstants.EXPERIMENT,
						ExperimentHome.class);
		List<Experiment> experiments = null;
		try {
			experiments = expBus.getExperimentsForUser("");
		} catch (RemoteException e) {
			handleException(e, comp, true, false, false, false);
		}

		Vector<Experiment> experimentName = new Vector<Experiment>();
		for (Experiment experiment : experiments) {
			experimentName.add(experiment);
		}
		return experimentName;
	}

	

	/**
	 * Method to set hyperlink property values
	 * 
	 * @param hyperlink
	 * @param obj
	 * @param hyperlinkText
	 * @param desc
	 * @param actionClass
	 */
	public static void setHyperlinkProperties(Cab2bHyperlink hyperlink,
			Object obj, String hyperlinkText, String desc,
			ActionListener actionClass) {
		hyperlink.setClickedColor(MainFrameStackedBoxPanel.CLICKED_COLOR);
		hyperlink.setUnclickedColor(MainFrameStackedBoxPanel.UNCLICKED_COLOR);
		hyperlink.setUserObject(obj);
		hyperlink.setText(hyperlinkText);
		hyperlink.setToolTipText(desc);
		hyperlink.addActionListener(actionClass);
	}

	/**
	 * @return Popular categories at this point
	 */
	public static Vector getUserSearchCategories() {
		return getPopularSearchCategories();
	}

	public static String escapeString(String input) {
		if (input.indexOf(",") != -1) {
			input = "\"" + input + "\"";
		}
		return input;
	}

	/**
	 * Method which will return number of elements in current datalist
	 * 
	 * @return
	 */
	public static int getDataListSize() {
		IDataRow rootNode = MainSearchPanel.getDataList().getRootDataRow();
		// This node is hidden node in the tree view
		return rootNode.getChildren().size();

	}

	/**
	 * This method verifies whether service URLs are available for give entity.
	 * 
	 * @param entity
	 * @return String[]
	 */
	private static String[] getServiceURLs(EntityInterface entity) {
		EntityInterface en = entity;
		if (edu.wustl.cab2b.common.util.Utility.isCategory(entity)) {
			CategoryBusinessInterface bus = (CategoryBusinessInterface) CommonUtils
					.getBusinessInterface(EjbNamesConstants.CATEGORY_BEAN,
							CategoryHomeInterface.class, null);
			Category cat = null;
			try {
				cat = bus.getCategoryByEntityId(entity.getId());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			en = cat.getRootClass().getCategorialClassEntity();
		}
		return UserCache.getInstance().getServiceURLs(en);
	}

	/**
	 * This method checks whether service URL is configured for the given query.
	 * If service url is not available it shows an ERROR dialog and returns
	 * false, otherwise returns true.
	 * 
	 * @param query
	 * @param container
	 * @return boolean
	 */
	public static boolean isServiceURLConfigured(ICab2bQuery query,
			Container container) {
		boolean isServiceURLConfigured = true;

		Set<IQueryEntity> entitySet = query.getConstraints().getQueryEntities();
		for (IQueryEntity queryEntity : entitySet) {
			EntityInterface entity = queryEntity.getDynamicExtensionsEntity();
			String[] urls = getServiceURLs(entity);
			if (urls == null || urls.length == 0) {
				isServiceURLConfigured = false;
			} else
				for (String url : urls) {
					if (url.equals("")) {
						isServiceURLConfigured = false;
						break;
					}
				}
		}
		if (!isServiceURLConfigured) {
			JOptionPane.showMessageDialog(container, ErrorCodeHandler
					.getErrorMessage(ErrorCodeConstants.DB_0007), "Query",
					JOptionPane.WARNING_MESSAGE);
		}
		return isServiceURLConfigured;
	}

	/**
	 * initializes resources like errorcode handler , Application Properties etc
	 */
	public static void initializeResources() {
		try {

			ErrorCodeHandler.initBundle(ERROR_CODE_FILE_NAME);
			ApplicationProperties.initBundle(APPLICATION_RESOURCES_FILE_NAME);
		} catch (MissingResourceException mre) {
			CheckedException checkedException = new CheckedException(mre
					.getMessage(), mre, ErrorCodeConstants.IO_0002);
			CommonUtils.handleException(checkedException, null, true, true,
					false, true);
		}
	}

	/**
	 * set the caB2B Home used to keep editable configurations, appliaction logs
	 * etc.
	 * 
	 */
	public static void setHome() {

		String userHome = System.getProperty("user.home");

		File cab2bHome = new File(userHome, "cab2b");
		System.setProperty("cab2b.home", cab2bHome.getAbsolutePath());
	}

	/**
	 * Method to launch caB2B Search data wizard.
	 */
	public static void launchSearchDataWizard() {

		// Update the variable for latest screen dimension from the
		// toolkit, this is to handle the situations where
		// application is started and then screen resolution is
		// changed, but the variable stiil holds old resolution
		// size.

		Dimension dimension = MainFrame.getScreenDimesion();
		final String title = ApplicationProperties.getValue(SEARCH_FRAME_TITLE);

		// Clearing the datalist
		SaveDatalistPanel.setDataListSaved(false);
		MainSearchPanel mainSearchPanel = GlobalNavigationPanel
				.getMainSearchPanel();
		if (mainSearchPanel == null)
			mainSearchPanel = new MainSearchPanel();

		GlobalNavigationPanel.setMainSearchPanel(mainSearchPanel);
		mainSearchPanel.getDataList().clear();
		MainFrame mainFrame = NewWelcomePanel.getMainFrame();
		JDialog searchDialog = WindowUtilities.setInDialog(mainFrame,
				mainSearchPanel, title, new Dimension(
						(int) (dimension.width * 0.90),
						(int) (dimension.height * 0.85)), true, true);
		mainFrame.setSearchWizardDialog(searchDialog);
		searchDialog.setVisible(true);
		GlobalNavigationPanel.setMainSearchPanel(null);
	}
    /**
     * @return The map of DAG image enumerations versus image object
     */
    public static Map<DagImages, Image> getDagImageMap() {
        Map<DagImages, Image> imageMap = new HashMap<DagImages, Image>();
        imageMap.put(DagImages.SelectIcon, Utilities.loadImage(SELECT_ICON_ADD_LIMIT));
        imageMap.put(DagImages.selectMOIcon, Utilities.loadImage(SELECT_ICON_ADD_LIMIT_MOUSEOVER));
        imageMap.put(DagImages.ArrowSelectIcon, Utilities.loadImage(LIMIT_CONNECT_DESELECTED));
        imageMap.put(DagImages.ArrowSelectMOIcon, Utilities.loadImage(LIMIT_CONNECT_SELECTED));
        imageMap.put(DagImages.ParenthesisIcon, Utilities.loadImage(PARENTHISIS_ICON_ADD_LIMIT));
        imageMap.put(DagImages.ParenthesisMOIcon, Utilities.loadImage(PARENTHISIS_ICON_ADD_LIMIT_MOUSEOVER));
        imageMap.put(DagImages.DocumentPaperIcon, Utilities.loadImage(PAPER_GRID_ADD_LIMIT));
        imageMap.put(DagImages.PortImageIcon, Utilities.loadImage(PORT_IMAGE_ADD_LIMIT));
        return imageMap;
    }
}
