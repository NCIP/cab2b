/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.common.category;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import edu.wustl.common.querysuite.metadata.category.AbstractCategorialClass;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;

/**
 * @author
 * @hibernate.joined-subclass table="DATA_CATEGORIAL_CLASS"
 * @hibernate.joined-subclass-key column="ID" 
 */
public class DataCategorialClass
        extends
        AbstractCategorialClass<DataCategorialAttribute, DataCategory, DataCategorialClass> implements
        Serializable {

    public DataCategorialClass() {

    }

    /**
     * This constructor is mainly used to create a DataCategorialClass Object From 
     * CategorialClass Object.It copies all the values from CategorialClass to
     * new DataCategorialClass Object.
     * @param categorialClass
     */
    public DataCategorialClass(CategorialClass categorialClass) {
        super();
      /*  DataCategorialClass dataCategorialClass = new DataCategorialClass();
        dataCategorialClass.setPathFromParent(categorialClass.getPathFromParent());
        Set<DataCategorialAttribute> dataCategorialAttributeCollection = new HashSet<DataCategorialAttribute>();
        Set<CategorialAttribute> categorialAttributeCollection = categorialClass.getCategorialAttributeCollection();
        for (CategorialAttribute categorialAttribute : categorialAttributeCollection) {
            DataCategorialAttribute newDataCategorialAttribute = new DataCategorialAttribute(categorialAttribute);
            newDataCategorialAttribute.setCategorialClass(dataCategorialClass);

        }
        dataCategorialClass.setCategorialAttributeCollection(dataCategorialAttributeCollection);
        dataCategorialClass.setPathFromParentId(categorialClass.getPathFromParentId());

        Set<DataCategorialClass> dataCategorialChildrenSet = new HashSet<DataCategorialClass>();
        Set<CategorialClass> categorialChildrenSet = categorialClass.getChildren();
        for (CategorialClass categorialClassChild : categorialChildrenSet) {
            DataCategorialClass newDataCategorialClassChild = new DataCategorialClass(categorialClassChild);
            newDataCategorialClassChild.setParent(dataCategorialClass);
            dataCategorialChildrenSet.add(newDataCategorialClassChild);
        }
        dataCategorialClass.setChildren(dataCategorialChildrenSet);*/

        
        this.setPathFromParent(categorialClass.getPathFromParent());
        this.setDeEntityId(categorialClass.getDeEntityId());
        Set<DataCategorialAttribute> dataCategorialAttributeCollection = new HashSet<DataCategorialAttribute>();
        Set<CategorialAttribute> categorialAttributeCollection = categorialClass.getCategorialAttributeCollection();
        for (CategorialAttribute categorialAttribute : categorialAttributeCollection) {
            DataCategorialAttribute newDataCategorialAttribute = new DataCategorialAttribute(categorialAttribute);
            newDataCategorialAttribute.setCategorialClass(this);
            dataCategorialAttributeCollection.add(newDataCategorialAttribute);
        }
        this.setCategorialAttributeCollection(dataCategorialAttributeCollection);
        this.setPathFromParentId(categorialClass.getPathFromParentId());

        Set<DataCategorialClass> dataCategorialChildrenSet = new HashSet<DataCategorialClass>();
        Set<CategorialClass> categorialChildrenSet = categorialClass.getChildren();
        for (CategorialClass categorialClassChild : categorialChildrenSet) {
            DataCategorialClass newDataCategorialClassChild = new DataCategorialClass(categorialClassChild);
            newDataCategorialClassChild.setParent(this);
            dataCategorialChildrenSet.add(newDataCategorialClassChild);
        }
        this.setChildren(dataCategorialChildrenSet);
        
        
        
        /* 
         for(CategorialClass oldChildCategorialClass:this.children){
         CategorialClass  newChildCategorialClass = oldChildCategorialClass.copy();
         newChildCategorialClass.setParent(newCategorialClass);
         }
         newCategorialClass.setChildren(newChildren);
         */

    }

}
