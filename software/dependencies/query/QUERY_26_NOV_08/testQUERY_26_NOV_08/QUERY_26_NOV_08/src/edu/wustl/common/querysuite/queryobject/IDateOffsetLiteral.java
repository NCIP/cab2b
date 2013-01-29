/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;

/**
 * Represents a literal that is to be used as an offset.
 * 
 * @author srinath_k
 * 
 * @see IDateOffset
 * @see ILiteral
 */
public interface IDateOffsetLiteral extends IDateOffset, ILiteral {
    String getOffset();

    void setOffset(String offset);
}
