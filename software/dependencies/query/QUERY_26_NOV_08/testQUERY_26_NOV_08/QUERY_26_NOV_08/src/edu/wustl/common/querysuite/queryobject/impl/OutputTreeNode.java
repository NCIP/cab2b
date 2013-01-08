/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */

package edu.wustl.common.querysuite.queryobject.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.wustl.common.querysuite.exceptions.DuplicateChildException;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.queryobject.IOutputEntity;
import edu.wustl.common.querysuite.queryobject.IOutputTreeNode;

/**
 * @author prafull_kadam Class implementation for IOutputTreeNode.
 */
public class OutputTreeNode extends BaseQueryObject implements IOutputTreeNode {

    private static final long serialVersionUID = -4576110943253229029L;

    /**
     * This constant will be used in autogeneration of the id for this class.
     * Value of this constant indicates the maximum number of children that any
     * parent node can have.
     */
    private static final int CHILD_FACTOR = 10;

    private int childsRemoved = 0;

    private long id = 1;

    private OutputTreeNode parentNode;

    private IAssociation parentAssociation; // association of this obect with
                                            // parent.

    private List<IOutputTreeNode> children = new ArrayList<IOutputTreeNode>();

    // List of associations of children with Parent. childrenAssoiations[i]
    // represents Association of children[i] with parent node.
    private List<IAssociation> childrenAssoiations = new ArrayList<IAssociation>();

    IOutputEntity outputEntity;

    /**
     * The Constructor to instantiate the object of this class.
     * 
     * @param outputEntity The reference to the output Entity.
     */
    public OutputTreeNode(IOutputEntity outputEntity) {
        this.outputEntity = outputEntity;
    }

    /**
     * To get the auto generated id for the class instance. In general the id of
     * the class will be equal to (parent node id) * CHILD_FACTOR + (index of
     * this node in the parent list) Apart from that this value may differ if
     * removeChild() method is called on parent node.
     */
    public Long getId() {
        return id;
    }

    /**
     * To set the id
     * 
     * @param id The id to set.
     */
    private void setId(long id) {
        this.id = id;
    }

    /**
     * To add the child node.
     * 
     * @param association The association between this node & childNode.
     * @param outputEntity The output entity of the child node to be added.
     * @return The reference to the child node.
     * @throws DuplicateChildException if there already exists a child with the
     *             given output Entity and association
     * @see edu.wustl.common.querysuite.queryobject.IOutputTreeNode#addChild(edu.wustl.common.querysuite.queryobject.IAssociation,
     *      edu.wustl.common.querysuite.queryobject.IOutputEntity)
     */
    public IOutputTreeNode addChild(IAssociation association, IOutputEntity outputEntity)
            throws DuplicateChildException {
        for (int index = 0; index < children.size(); index++) {
            if (outputEntity.equals(children.get(index).getOutputEntity())
                    && association.equals(childrenAssoiations.get(index))) {
                throw new DuplicateChildException();
            }
        }

        // child does not exist with same outputEntity & association, so adding
        // new child for the output tree node.
        OutputTreeNode child = new OutputTreeNode(outputEntity);
        long childId = this.getId() * CHILD_FACTOR + children.size() + childsRemoved;
        child.setId(childId);

        child.parentNode = this;
        child.parentAssociation = association;
        children.add(child);
        childrenAssoiations.add(association);

        return child;

    }

    /**
     * @return The reference to the IAssociation representing association of
     *         this node with its parent node.
     * @see edu.wustl.common.querysuite.queryobject.IOutputTreeNode#getAssociationWithParent()
     */
    public IAssociation getAssociationWithParent() {
        return parentAssociation;
    }

    /**
     * @param child the reference to the output entity of the child.
     * @return The list of outputEnintities.
     * @see edu.wustl.common.querysuite.queryobject.IOutputTreeNode#getAssociationsWithChild(edu.wustl.common.querysuite.queryobject.IOutputEntity)
     */
    public List<IAssociation> getAssociationsWithChild(IOutputEntity child) {
        List<IAssociation> associationList = new ArrayList<IAssociation>();

        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getOutputEntity().equals(outputEntity)) {
                associationList.add(childrenAssoiations.get(i));
            }
        }
        return associationList;
    }

    /**
     * @return The list all children of this node
     * @see edu.wustl.common.querysuite.queryobject.IOutputTreeNode#getChildren()
     */
    public List<IOutputTreeNode> getChildren() {
        return children;
    }

    /**
     * @return The reference to the output Entity associated with this node.
     * @see edu.wustl.common.querysuite.queryobject.IOutputTreeNode#getOutputEntity()
     */
    public IOutputEntity getOutputEntity() {
        return outputEntity;
    }

    /**
     * @return Returns the reference to the parent node, null if its root node.
     * @see edu.wustl.common.querysuite.queryobject.IOutputTreeNode#getParent()
     */
    public IOutputTreeNode getParent() {
        return parentNode;
    }

    /**
     * @return true if this node is leaf node. The node is leaf node if, its
     *         children list is empty.
     * @see edu.wustl.common.querysuite.queryobject.IOutputTreeNode#isLeaf()
     */
    public boolean isLeaf() {
        return this.children.isEmpty();
    }

    /**
     * @return true if this node is root node. The node is root node if its
     *         parent node is null.
     * @see edu.wustl.common.querysuite.queryobject.IOutputTreeNode#isRoot()
     */
    public boolean isRoot() {
        return (this.parentNode == null);
    }

    /**
     * @param association The reference to IAssociation representing association
     *            of parent node with child to be removed.
     * @param outputEntity The reference to IOutputEntity representing output
     *            entity of child to be removed.
     * @return true if the child removed succesfully.
     * @see edu.wustl.common.querysuite.queryobject.IOutputTreeNode#removeChild(edu.wustl.common.querysuite.queryobject.IAssociation,
     *      edu.wustl.common.querysuite.queryobject.IOutputEntity)
     */
    public boolean removeChild(IAssociation association, IOutputEntity outputEntity) {
        boolean isRemoved = false;

        for (int index = 0; index < children.size(); index++) {
            if (outputEntity.equals(children.get(index).getOutputEntity())
                    && association.equals(childrenAssoiations.get(index))) {
                // corresponding child found, so removing child &
                // childassociation from children list & childrenAssoiations
                // list.
                children.remove(index);
                childrenAssoiations.remove(index);
                isRemoved = true;
                childsRemoved++;
                break;
            }
        }
        return isRemoved;
    }

    /**
     * @param obj the object to be compared.
     * @return true if following attributes of both object matches: - id
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj != null && this.getClass() == obj.getClass()) {
            OutputTreeNode node = (OutputTreeNode) obj;
            // if (this.outputEntity != null &&
            // this.outputEntity.equals(node.outputEntity)
            // && this.parentAssociation == null
            // ? node.parentAssociation == null
            // : this.parentAssociation.equals(node.parentAssociation)
            // && this.parentNode == null ? node.parentNode == null :
            // this.parentNode
            // .equals(node.parentNode)
            // && this.children.equals(node.children)
            // && this.childrenAssoiations.equals(node.childrenAssoiations))
            if (this.id == node.getId()) {
                return true;
            }

        }
        return false;
    }

    /**
     * To get the HashCode for the object. It will be calculated based on
     * Following attributes: - id
     * 
     * @return The hash code value for the object.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }

    /**
     * @return String representation of object in the form: [outputEntity :
     *         parentNode]
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + id + ":" + outputEntity.toString() + "("
                + (parentNode == null ? "-" : parentNode.getId() + "") + ")" + "]";
    }

}
