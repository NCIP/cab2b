package edu.wustl.cab2b.client.ui.query;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.treetable.B2BTreeNode;
import edu.wustl.cab2b.common.queryengine.result.CategoryResult;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.util.logger.Logger;

public class TransformCategoryResult {

    CategoryResult m_categoryResult = null;

    /**
     * Constructor  
     */
    public TransformCategoryResult(CategoryResult categoryResult) {
        m_categoryResult = categoryResult;        
    }

    /**
     * Method to get root treeNode  
     */
    public B2BTreeNode getB2BRootTreeNode() {
        //getting stringURL and corresponding categoryResultRecords 
        Map<String, List<ICategorialClassRecord>> mapURLToRoot = m_categoryResult.getRecords();
        
        
        B2BTreeNode rootB2BTreeNode = new B2BTreeNode();
        rootB2BTreeNode.setDisplayName("Root");

        if (!mapURLToRoot.isEmpty()) {
            Set keys = mapURLToRoot.keySet();
            Iterator keyIterator = keys.iterator();
            while (keyIterator.hasNext()) {
                String url = (String) keyIterator.next();
                Logger.out.debug("URL :" + url);

                //getting list of records for url key
                List<ICategorialClassRecord> iCategorialClassRecordList = mapURLToRoot.get(url);
                setRecordsToB2BTreeNode(rootB2BTreeNode, iCategorialClassRecordList);
            }
        }
        return rootB2BTreeNode;
    }

    /**
     * Method to set records to for the given treeNode 
     */   
     

    private void setRecordsToB2BTreeNode(B2BTreeNode parentTreeNode,
                                         List<ICategorialClassRecord> iCategorialClassRecordList) {

        for (ICategorialClassRecord iCategorialClassRecord :iCategorialClassRecordList) {
            //Logger.out.debug("Set DisplayName :" + edu.wustl.cab2b.common.util.Utility.getDisplayName( iCategorialClassRecord.getCategorialClass().getCategorialClassEntity()));           
                      
            String  displayName = edu.wustl.cab2b.common.util.Utility.getDisplayName( iCategorialClassRecord.getCategorialClass().getCategorialClassEntity()) + "_"+iCategorialClassRecord.getRecordId().getId();
            Logger.out.debug("Set DisplayName :"+displayName);
            

            //record node
            B2BTreeNode iCategorialClassRecordNode = new B2BTreeNode();
            iCategorialClassRecordNode.setDisplayName(displayName);
            iCategorialClassRecordNode.setValue(null);
            
        

            //geting attribute list for the iCategorialClassRecord
            Set<AttributeInterface> categorialAttributeSet = iCategorialClassRecord.getAttributes();
            Logger.out.debug("Attribute Size :" + categorialAttributeSet.size());
            
            for(AttributeInterface attribute:categorialAttributeSet) {
                Object value = iCategorialClassRecord.getValueForAttribute(attribute);                
                B2BTreeNode attributeNode = new B2BTreeNode();
                attributeNode.setDisplayName(attribute.getName());
                attributeNode.setValue(value);
                attributeNode.setChildren(null);
                iCategorialClassRecordNode.addChild(attributeNode);
            }

            //getting categorial class node 
            Map<CategorialClass, List<ICategorialClassRecord>> categorialClassRecordMap = iCategorialClassRecord.getChildrenCategorialClassRecords();
          
            for (List<ICategorialClassRecord> newICategorialClassRecordList :categorialClassRecordMap.values()) {
                //getting the ICategoryRecordList

                //creating subchild
                B2BTreeNode child = new B2BTreeNode();

                //setting child values
                setRecordsToB2BTreeNode(child, newICategorialClassRecordList);

                //adding this child to parent 
                iCategorialClassRecordNode.addChild(child);

               Logger.out.debug("PARENT :" + iCategorialClassRecordNode + "   CHILD :" + child);
            }
            
            //setting display name for group node by removing _ String   
            parentTreeNode.setDisplayName(iCategorialClassRecordNode.toString().substring(0,iCategorialClassRecordNode.toString().lastIndexOf("_")));
            
            //adding iCategorialClassRecordNode to parent
            parentTreeNode.addChild(iCategorialClassRecordNode);
        }

        return;
    }
    /**
     * Method to set attributeList for the given treeNode  
     */
   /* private B2BTreeNode setAttributeToB2BTreeNode(B2BTreeNode parentTreeNode,
                                                  Set<AttributeInterface> categorialAttributeSet) {
        if (!categorialAttributeSet.isEmpty()) {
            //getting all key set to iterate map 
            Set categorialAttributeMapKeys = categorialAttributeMap.keySet();
            Iterator categorialAttributeMapKeysIter = categorialAttributeMapKeys.iterator();

            while (categorialAttributeMapKeysIter.hasNext()) {
                CategorialAttribute categorialAttribute = (CategorialAttribute) categorialAttributeMapKeysIter.next();
                B2BTreeNode attributeNode = new B2BTreeNode();
                attributeNode.setDisplayName(categorialAttribute.getCategoryAttribute().getName());
                Logger.out.info("Categorial Attribute Name :"
                        + categorialAttribute.getCategoryAttribute().getName());
                Logger.out.info("Categorial Attribute Value :" + categorialAttributeMap.get(categorialAttribute));

                attributeNode.setChildren(null);
                attributeNode.setValue(categorialAttributeMap.get(categorialAttribute));

                //adding attribute as child to the main node
                parentTreeNode.addChild(attributeNode);
                
                Logger.out.info(" PARENT :" + parentTreeNode + " Attribute  CHILD Node:" + attributeNode);
                
            }
        }
        return parentTreeNode;
    }
*/
    /**
     * @param args
     */
    public static void main(String[] args) {

    }
}
