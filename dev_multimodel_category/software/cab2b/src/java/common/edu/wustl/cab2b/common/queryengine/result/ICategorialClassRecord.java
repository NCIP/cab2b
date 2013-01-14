/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.queryengine.result;

import java.util.List;
import java.util.Map;

import edu.wustl.common.querysuite.metadata.category.CategorialClass;

public interface ICategorialClassRecord extends IRecord {
    Map<CategorialClass, List<ICategorialClassRecord>> getChildrenCategorialClassRecords();

    CategorialClass getCategorialClass();

    void addCategorialClassRecords(CategorialClass catClass,
                                   List<ICategorialClassRecord> catClassRecs);
}