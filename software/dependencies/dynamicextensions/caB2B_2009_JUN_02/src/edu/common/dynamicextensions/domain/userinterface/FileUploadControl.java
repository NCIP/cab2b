/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/*
 * Created on Nov 3, 2006
 * @author
 *
 */

package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domain.FileAttributeRecordValue;
import edu.common.dynamicextensions.domaininterface.userinterface.FileUploadInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * @author preeti_munot
 * @hibernate.joined-subclass table="DYEXTN_FILE_UPLOAD" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class FileUploadControl extends Control implements FileUploadInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3211268406984504475L;
	Integer columns = null;

	/**
	 * @return 
	 * @throws DynamicExtensionsSystemException
	 */
	protected String generateEditModeHTML() throws DynamicExtensionsSystemException
	{
		FileAttributeRecordValue fileAttributeRecordValue = (FileAttributeRecordValue) this.value;
		String htmlString = "";
		if (fileAttributeRecordValue != null && fileAttributeRecordValue.getFileName() != null)
		{
			htmlString = ApplicationProperties.getValue("eav.file.fileName")
					+ "&nbsp;"
					+ " <A onclick='appendRecordId(this);' href='/dynamicExtensions/DownloadFileAction?attributeIdentifier="
					+ this.abstractAttribute.getId() + "'>"
					+ fileAttributeRecordValue.getFileName() + "</A>";

		}
		htmlString = htmlString + "&nbsp;<input type=\"file\" " + "name=\"value("
				+ getHTMLComponentName() + ")\" " + "id=\"" + getHTMLComponentName() + "\"/>";
		return htmlString;
	}

	/**
	 * @hibernate.property name="columns" type="integer" column="NO_OF_COLUMNS" 
	 * @return Returns the columns.
	 */
	public Integer getColumns()
	{
		return columns;
	}

	/**
	 * 
	 */
	public void setColumns(Integer columns)
	{
		this.columns = columns;
	}

	protected String generateViewModeHTML() throws DynamicExtensionsSystemException
	{
		FileAttributeRecordValue fileAttributeRecordValue = (FileAttributeRecordValue) this.value;

		String htmlString = "&nbsp;";
		if (fileAttributeRecordValue != null)
		{
			String fileName = fileAttributeRecordValue.getFileName();
			htmlString = "<span class = '" + cssClass + "'> " + fileName + "</span>";
		}
		return htmlString;
	}

}