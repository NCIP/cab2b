package edu.wustl.cab2b.common.domain;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * A base class with a set of attributes that defines the metadata for the
 * derived experiments, and categories.
 * 
 * @hibernate.joined-subclass table="CAB2B_ADDITIONAL_META_DATA"
 * @hibernate.joined-subclass-key column="AMD_ID"
 * @author
 */
public class AdditionalMetadata extends AbstractDomainObject implements java.io.Serializable {

	private static final long serialVersionUID = 1234567890L;

	/**
	 * Name of the domain object.
	 */
	protected java.lang.String name;

	/**
	 * Returns the name of the domain object.
	 * 
	 * @hibernate.property name="name" type="string" column="NAME"
	 *                     length="50"
	 * @return name of the domain object.
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name of the domain object.
	 * 
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Description for the domain object.
	 */
	protected java.lang.String description;

	/**
	 * Returns the description for the domain object.
	 * 
	 * @hibernate.property name="description" type="string"
	 *                     column="DESCRIPTION" length="255"
	 * @return description for the domain object.
	 */
	public java.lang.String getDescription() {
		return description;
	}

	/**
	 * Sets the description for the domain object.
	 * 
	 * @param description
	 */
	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	/**
	 * Date of creation of domain object.
	 */
	protected java.util.Date createdOn;

	/**
	 * Returns the date the domain object was created.
	 * 
	 * @hibernate.property name="createdOn" type="date" column="CREATED_ON"
	 * @return date the domain object was created.
	 */
	public java.util.Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * Sets the date of creation.
	 * 
	 * @param createdOn
	 */
	public void setCreatedOn(java.util.Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * Last updated date.
	 */
	protected java.util.Date lastUpdatedOn;

	/**
	 * Returns the last updated date of the domain object.
	 * 
	 * @hibernate.property name="lastModifiedOn" type="date"
	 *                     column="LAST_UPDATED_ON"
	 * @return last modification date of the domain object.
	 */
	public java.util.Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	/**
	 * Sets the last updated date.
	 * 
	 * @param lastUpdatedOn
	 */
	public void setLastUpdatedOn(java.util.Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof AdditionalMetadata) {
			AdditionalMetadata c = (AdditionalMetadata) obj;
			Long thisId = getId();

			if (thisId != null && thisId.equals(c.getId())) {
				eq = true;
			}

		}
		return eq;
	}

	public int hashCode() {
		int h = 0;

		if (getId() != null) {
			h += getId().hashCode();
		}
		return h;
	}
}
