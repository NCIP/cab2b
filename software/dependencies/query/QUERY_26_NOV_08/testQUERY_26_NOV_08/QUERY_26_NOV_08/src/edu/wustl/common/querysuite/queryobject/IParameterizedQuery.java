/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.common.querysuite.queryobject;

import java.util.List;

/**
 * @author chetan_patil
 * @created Aug 31, 2007, 1:46:07 PM
 */
public interface IParameterizedQuery extends IQuery {
    List<IOutputAttribute> getOutputAttributeList();

    void setOutputAttributeList(List<IOutputAttribute> outputAttributeList);

    List<IParameter<?>> getParameters();
}
