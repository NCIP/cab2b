/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.query;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.treetable.B2BTreeNode;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;

public class TransformCategoryResult {

    /**
     * Method to get root treeNode  
     */
    public B2BTreeNode getB2BRootTreeNode(List<ICategorialClassRecord> iCategorialClassRecordList,
                                          B2BTreeNode rootB2BTreeNode) {

        B2BTreeNode treeNode = new B2BTreeNode();
        if (iCategorialClassRecordList.size() > 0) {
            treeNode = setRecordsToB2BTreeNode(treeNode, iCategorialClassRecordList);
            rootB2BTreeNode.addChild(treeNode);
        }
        return rootB2BTreeNode;
    }

    /**
     * Method to set records to for the given treeNode 
     */

    private B2BTreeNode setRecordsToB2BTreeNode(B2BTreeNode parentTreeNode,
                                                List<ICategorialClassRecord> iCategorialClassRecordList) {

        boolean isOneRecord = false;
        for (ICategorialClassRecord iCategorialClassRecord : iCategorialClassRecordList) {

            B2BTreeNode iCategorialClassRecordNode = null;

            String displayName = edu.wustl.cab2b.common.util.Utility.getOnlyEntityName(iCategorialClassRecord.getCategorialClass().getCategorialClassEntity());

            //String displayName = edu.wustl.cab2b.common.util.Utility.getDisplayName(iCategorialClassRecord.getCategorialClass().getCategorialClassEntity());

            if (iCategorialClassRecordList.size() == 1) {
                iCategorialClassRecordNode = parentTreeNode;
                iCategorialClassRecordNode.setDisplayName(displayName);
                isOneRecord = true;
            } else {
                iCategorialClassRecordNode = new B2BTreeNode();
                displayName = displayName + "_" + iCategorialClassRecord.getRecordId().getId();
                iCategorialClassRecordNode.setDisplayName(displayName);
                isOneRecord = false;
            }

            iCategorialClassRecordNode.setValue(null);

            //geting attribute list for the iCategorialClassRecord
            Set<AttributeInterface> categorialAttributeSet = iCategorialClassRecord.getAttributes();

            for (AttributeInterface attribute : categorialAttributeSet) {
                Object value = iCategorialClassRecord.getValueForAttribute(attribute);
                B2BTreeNode attributeNode = new B2BTreeNode();
                attributeNode.setDisplayName(attribute.getName());
                attributeNode.setValue(value);
                attributeNode.setChildren(null);
                iCategorialClassRecordNode.addChild(attributeNode);
            }

            //getting categorial class node 
            Map<CategorialClass, List<ICategorialClassRecord>> categorialClassRecordMap = iCategorialClassRecord.getChildrenCategorialClassRecords();

            for (List<ICategorialClassRecord> newICategorialClassRecordList : categorialClassRecordMap.values()) {

                //creating subchild
                B2BTreeNode child = new B2BTreeNode();

                //setting child values
                setRecordsToB2BTreeNode(child, newICategorialClassRecordList);

                //adding this child to parent 
                iCategorialClassRecordNode.addChild(child);
            }
            if (isOneRecord == false) {
                parentTreeNode.addChild(iCategorialClassRecordNode);
                //setting display name for group node by removing _ String   
                parentTreeNode.setDisplayName(iCategorialClassRecordNode.toString().substring(
                                                                                              0,
                                                                                              iCategorialClassRecordNode.toString().lastIndexOf(
                                                                                                                                                "_"))
                        + " (" + parentTreeNode.getChildren().size() + ")"); //adding iCategorialClassRecordNode to parent
            } else {
                parentTreeNode = iCategorialClassRecordNode;
            }
        }
        return parentTreeNode;
    }
}
