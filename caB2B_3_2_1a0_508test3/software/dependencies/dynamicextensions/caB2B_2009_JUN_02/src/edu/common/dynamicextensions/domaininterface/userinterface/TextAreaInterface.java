package edu.common.dynamicextensions.domaininterface.userinterface;

/**
 * TextAreaInterface stores necessary information for generating TextArea control on
 * dynamically generated user interface.  
 * @author geetika_bangard
 */
public interface TextAreaInterface extends ControlInterface 
{
    
    /**
	 * @return Returns the columns.
	 */
	Integer getColumns();
	/**
	 * @param columns The columns to set.
	 */
	void setColumns(Integer columns);
	/**
	 * @return Returns the rows.
	 */
	Integer getRows();
	/**
	 * @param rows The rows to set.
	 */
	void setRows(Integer rows);
	/**
	 * @return Returns the isPassword.
	 */
	Boolean getIsPassword();
	/**
	 * @param isPassword The isPassword to set.
	 */
	void setIsPassword(Boolean isPassword);
 

}
