/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;

/**
 * Represents a term that can be present in the output of a query. An output
 * term can be given a meaningful name (possibly by the user).
 * 
 * @author srinath_k
 * @see IQuery#getOutputTerms()
 */
public interface IOutputTerm extends IBaseQueryObject, INameable {

    ITerm getTerm();

    void setTerm(ITerm term);

    TimeInterval<?> getTimeInterval();

    void setTimeInterval(TimeInterval<?> timeInterval);
}
