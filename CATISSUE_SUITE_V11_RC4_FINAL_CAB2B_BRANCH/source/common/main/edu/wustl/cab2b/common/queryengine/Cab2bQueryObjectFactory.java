/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.queryengine;

import edu.wustl.common.querysuite.factory.QueryObjectFactory;

public class Cab2bQueryObjectFactory extends QueryObjectFactory {
    public static ICab2bQuery createCab2bQuery() { 
        return new Cab2bQuery();
    }
}
