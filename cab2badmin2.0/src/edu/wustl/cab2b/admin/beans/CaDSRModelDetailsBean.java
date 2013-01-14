/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.admin.beans;

/**
 * @author chetan_patil
 * 
 */
public class CaDSRModelDetailsBean extends BaseBean implements
		Comparable<CaDSRModelDetailsBean> {
	private static final long serialVersionUID = 1L;

	/** Unique identifier of CaDSR Project */
	private String id;

	/** Long name of CaDSR Project */
	private String longName;

	/** Description of CaDSR Project */
	private String description;

	/** The version of model, like 1.2 2.0 etc */
	private String version;

	/**
	 * Default constructor
	 */
	public CaDSRModelDetailsBean() {
		super();
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the longName
	 */
	public String getLongName() {
		return longName;
	}

	/**
	 * @param longName
	 *            the longName to set
	 */
	public void setLongName(String longName) {
		this.longName = longName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Implements {@link Comparable#compareTo(Object)}
	 * 
	 * @param bean
	 * @return
	 * 
	 */
	public int compareTo(CaDSRModelDetailsBean bean) {
		String name = new StringBuffer().append(longName).append(version)
				.toString();
		String nameToCompare = new StringBuffer().append(bean.longName).append(
				bean.version).toString();
		return name.toLowerCase().compareTo(nameToCompare.toLowerCase());
	}

	/**
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return The model version
	 */
	public String getVersion() {
		return version;
	}

}
