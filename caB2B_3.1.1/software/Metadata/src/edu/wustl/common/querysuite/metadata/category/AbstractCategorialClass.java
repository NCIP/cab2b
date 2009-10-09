/**
 * 
 */

package edu.wustl.common.querysuite.metadata.category;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.metadata.path.IPath;

/**
 * @author deepak_shingan
 * @hibernate.class table="ABSTRACT_CATEGORIAL_CLASS"
 */
abstract public class AbstractCategorialClass<A extends AbstractCategorialAttribute, C extends AbstractCategory, CC extends AbstractCategorialClass>
		implements
			Serializable
{

	private Long id;

	protected C category;

	protected Set<A> categorialAttributeCollection = new HashSet<A>();

	protected Set<CC> children = new HashSet<CC>();

	protected CC parent;

	protected Long pathFromParentId;

	protected IPath pathFromParent;

	private Long deEntityId;

	private EntityInterface categorialClassEntity;

	/**
	 * @return the categorialAttributeCollection
	 * 
	 * @hibernate.set name="categorialAttributeCollection" cascade="all-delete-orphan" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ABSTRACT_CATEGORIAL_ATTRIBUTE_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.common.querysuite.metadata.category.AbstractCategorialAttribute"
	 * @hibernate.cache usage="read-write"
	 */
	public Set<A> getCategorialAttributeCollection()
	{
		return categorialAttributeCollection;
	}

	/**
	 * @param categorialAttributeCollection the categorialAttributeCollection to set
	 * 
	 * @hiberbate.one-to-many 
	 */
	public void setCategorialAttributeCollection(Set<A> categorialAttributeCollection)
	{
		this.categorialAttributeCollection = categorialAttributeCollection;
	}

	/**
	 * @return the category
	 * 
	 * @hibernate.many-to-one column="ABSTRACT_CATEGORY_ID"  
	 * class="edu.wustl.common.querysuite.metadata.category.AbstractCategory" 
	 * cascade="all" lazy="false"
	 */
	public C getCategory()
	{
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(C category)
	{
		this.category = category;
	}

	/**
	 * @return the children
	 * 
	 * @hibernate.set name="children" table="ABSTRACT_CATEGORIAL_CLASS" lazy="false" inverse="false"  cascade="save-update"    sort="unsorted"
	 * @hibernate.collection-key column="PARENT_CATEGORIAL_CLASS_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.common.querysuite.metadata.category.AbstractCategorialClass"
	 * @hibernate.cache usage="read-write"
	 */
	public Set<CC> getChildren()
	{
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(Set<CC> children)
	{
		this.children = children;
	}

	/**
	 * Returns the identifier assigned .
	 * @return a unique id assigned to this object.
	 * 
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CONSTRAINT_SEQ"
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
	 * @return the parent
	 * 
	 * @hibernate.many-to-one column="PARENT_CATEGORIAL_CLASS_ID"  
	 * class="edu.wustl.common.querysuite.metadata.category.AbstractCategorialClass" 
	 * cascade="all" lazy="false"
	 */
	public CC getParent()
	{
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(CC parent)
	{
		this.parent = parent;
	}

	public IPath getPathFromParent()
	{
		return pathFromParent;
	}

	/**
	 * @param pathFromParent the pathFromParent to set
	 */
	public void setPathFromParent(IPath pathFromParent)
	{
		this.pathFromParent = pathFromParent;
	}

	/**
	 * 
	 * @return the pathFromParentId
	 * @hibernate.property name="pathFromParentId" type="long" update="true" insert="true"  cascade="all" lazy="false" column="PATH_FROM_PARENT_ID"
	 * 
	 */
	public Long getPathFromParentId()
	{
		return pathFromParentId;
	}

	/**
	 * @param pathFromParentId the pathFromParentId to set
	 */
	public void setPathFromParentId(Long pathFromParentId)
	{
		this.pathFromParentId = pathFromParentId;
	}

	public AttributeInterface findSourceAttribute(AttributeInterface categoryAttribute)
	{
		for (AbstractCategorialAttribute categorialAttribute : getCategorialAttributeCollection())
		{
			if (categoryAttribute.equals(((CategorialAttribute) categorialAttribute)
					.getCategoryAttribute()))
			{
				return categorialAttribute.getSourceClassAttribute();
			}
		}
		return null;
	}

	public void addChildCategorialClass(CC child, IPath pathToChild)
	{
		children.add(child);
		child.setParent(this);
		child.setPathFromParent(pathToChild);
	}

	public void addChildCategorialClass(CC child)
	{
		children.add(child);
		child.setParent(this);
	}

	// rets true if the child was found; false otherwise.
	public boolean removeChildCategorialClass(CC child)
	{
		return children.remove(child);
	}

	public void addCategorialAttribute(A categorialAttribute)
	{
		if (categorialAttributeCollection == null)
		{
			categorialAttributeCollection = new HashSet<A>();
		}
		categorialAttributeCollection.add(categorialAttribute);
		categorialAttribute.setCategorialClass(this);
	}

	/**
	 * @return Returns the categorialClassEntity.
	 */
	public EntityInterface getCategorialClassEntity()
	{
		return categorialClassEntity;
	}

	/**
	 * @param categorialClassEntity The categorialClassEntity to set.
	 */
	public void setCategorialClassEntity(EntityInterface categorialClassEntity)
	{
		this.categorialClassEntity = categorialClassEntity;
	}

	/**
	 * 
	 * @return the deEntityId
	 * @hibernate.property name="deEntityId" type="long" length="30" column="DE_ENTITY_ID"
	 * 
	 */
	public Long getDeEntityId()
	{
		return deEntityId;
	}

	/**
	 * @param deEntityId The deEntityId to set.
	 */
	public void setDeEntityId(Long deEntityId)
	{
		this.deEntityId = deEntityId;
	}

}
