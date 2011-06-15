
package edu.wustl.common.querysuite.queryobject;

import java.util.List;

import edu.wustl.common.querysuite.exceptions.DuplicateChildException;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;

/**
 * A tree node to represent an output entity  that is a part of the outputs of the
 * query. It is a tree with <br> vertex : {@link edu.wustl.common.querysuite.
 * queryobject.IOutputEntity} label on edge : {@link edu.wustl.common.querysuite.
 * queryobject.IAssociation}.<br> e.g. if a node with entity  Participant has a
 * child node with entity  Specimen, it means "select participants, and associated
 * specimens"; the association is as given by the label on the edge. Note that the
 * same entity  can be present multiple times in this tree.<br> Let et(X) denote
 * the entity  of the node X; L(X,Y) denote the label (association) on the edge
 * from X to Y. Given a node X, the only restriction is : given any two children
 * nodes of X, say, Y1 and Y2, et(Y1) = et(Y2) if and only if L(X, Y1) != L(X, Y2).
 * <br> Thus, we can say "select specimens, and the available and used quantities
 * of the specimens". Here, Specimen is parent, with two children; both children
 * are the class Quantity, but the associations are different (available and used
 * quantities).<br>
 * @author srinath_k
 * @version 1.0
 * @updated 22-Dec-2006 2:50:17 PM
 */
public interface IOutputTreeNode extends IBaseQueryObject
{
	/**
	 * To add the child node.
	 * @param association The association between this node & childNode.
	 * @param outputEntity The output entity of the child node to be added.
	 * @return The reference to the child node.
	 * @exception DuplicateChildException if there already exists a child with the
	 * given output Entity and association.
	 */
	IOutputTreeNode addChild(IAssociation association, IOutputEntity outputEntity)
			throws DuplicateChildException;

	/**
	 * To get the output entity associated with this node.
	 * @return the reference to the output Entity associated with this node.
	 */
	IOutputEntity getOutputEntity();

	/**
	 * To get the list of all children of this node.
	 * @return The list all children of this node.
	 */
	List<IOutputTreeNode> getChildren();

	/**
	 * 
	 * @param child the reference to the output entity of the child.
	 * @return The list of outputEnintities.
	 */
	List<IAssociation> getAssociationsWithChild(IOutputEntity child);

	/**
	 * To get the association of this node with its parent node.
	 * @return The reference to the IAssociation representing association of this node with its parent node.
	 */
	IAssociation getAssociationWithParent();

	/**
	 * To remove this child node.
	 * @param association The reference to IAssociation representing association of parent node with child to be removed.
	 * @param outputEntity The reference to IOutputEntity representing output entity of child to be removed.
	 * @return true if the child removed succesfully.
	 */
	boolean removeChild(IAssociation association, IOutputEntity outputEntity);

	/**
	 * To get the parent node of this node.
	 * @return Returns the reference to the parent node, null if its root node. 
	 */
	IOutputTreeNode getParent();

	/**
	 * @return true if this node is leaf node.
	 */
	boolean isLeaf();

	/**
	 * @return true if this node is root node.
	 */
	boolean isRoot();

}