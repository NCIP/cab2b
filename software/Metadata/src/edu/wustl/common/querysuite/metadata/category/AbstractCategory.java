/**
 * 
 */

package edu.wustl.common.querysuite.metadata.category;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 
 * 
 * @hibernate.class table="ABSTRACT_CATEGORY"
 * @hibernate.cache usage="read-write"
 */
public abstract class AbstractCategory<C extends AbstractCategorialClass, A extends AbstractCategory>
		implements
			Serializable
{

	protected Long id;

	protected C rootClass;

	protected Set<A> subCategories = new HashSet<A>();

	protected A parentCategory;

	/**
	 * @return the id
	 * 
	 * @hibernate.id name="id" column="ID" type="long" length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="ABSTRACT_CATEGORY_SEQ"
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @return the parentCategory
	 * 
	 * @hibernate.many-to-one column="PARENT_CATEGORY_ID" class="edu.wustl.common.querysuite.metadata.category.AbstractCategory" unique="true" cascade="all" lazy="false"
	 */
	public A getParentCategory()
	{
		return parentCategory;
	}

	/**
	 * @param parentCategory the parentCategory to set
	 */
	public void setParentCategory(A parentCategory)
	{
		this.parentCategory = parentCategory;
	}

	public C getRootClass()
	{
		return rootClass;
	}

	/**
	 * @param rootClass the rootClass to set
	 */
	public void setRootClass(C rootClass)
	{
		this.rootClass = rootClass;
	}

	/**
	 * @return the subCategories
	 * 
	 * @hibernate.set name="subCategories"  table="ABSTRACT_CATEGORY" lazy="false" inverse="false" cascade="none" sort="unsorted"
	 * @hibernate.collection-key column="PARENT_CATEGORY_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.common.querysuite.metadata.category.AbstractCategory"
	 * @hibernate.cache usage="read-write"
	 */
	public Set<A> getSubCategories()
	{
		return subCategories;
	}

	/**
	 * @param subCategories the subCategories to set
	 */
	public void setSubCategories(Set<A> subCategories)
	{
		this.subCategories = subCategories;
	}

}
