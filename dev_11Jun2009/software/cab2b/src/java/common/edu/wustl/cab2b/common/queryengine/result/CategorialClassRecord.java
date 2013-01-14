/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.queryengine.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.QueryExecutorPropertes;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;

/**
 * Record for a CategorialClass
 * @author chandrakant_talele
 */
public class CategorialClassRecord extends Record implements ICategorialClassRecord {
    private static final long serialVersionUID = -7902568245257677861L;

    private static long noOfObjectsPresent = 0;

    private static final long totalObjectsAllowedGlobally = QueryExecutorPropertes.getGlobalAllowedRecords();

    private Map<CategorialClass, List<ICategorialClassRecord>> childrenCategorialClassRecords;

    private CategorialClass categorialClass;

    protected CategorialClassRecord(
            CategorialClass categorialClass,
            Set<AttributeInterface> attributes,
            RecordId id) {
        super(attributes, id);
        childrenCategorialClassRecords = new HashMap<CategorialClass, List<ICategorialClassRecord>>();
        this.categorialClass = categorialClass;
        noOfObjectsPresent++;
        if (noOfObjectsPresent > totalObjectsAllowedGlobally) {
            throw new RuntimeException("No of ICategorialClassRecord present in memory exceeds global limit "
                    + totalObjectsAllowedGlobally);
        }
    }

    public CategorialClass getCategorialClass() {
        return categorialClass;
    }

    public void setCategorialClass(CategorialClass categorialClass) {
        this.categorialClass = categorialClass;
    }

    public Map<CategorialClass, List<ICategorialClassRecord>> getChildrenCategorialClassRecords() {
        return childrenCategorialClassRecords;
    }

    public void setChildrenCategorialClassRecords(
                                                  Map<CategorialClass, List<ICategorialClassRecord>> childrenCategorialClassRecords) {
        this.childrenCategorialClassRecords = childrenCategorialClassRecords;
    }

    public void addCategorialClassRecords(CategorialClass catClass, List<ICategorialClassRecord> catClassRecs) {
        List<ICategorialClassRecord> existingRecs = getChildrenCategorialClassRecords().get(catClass);
        if (existingRecs == null) {
            synchronized (this) {
                if (catClassRecs != null) {
                    getChildrenCategorialClassRecords().put(catClass,
                                                            new ArrayList<ICategorialClassRecord>(catClassRecs));
                }
            }
        } else {
            existingRecs.addAll(catClassRecs);
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        noOfObjectsPresent--;
    }
    public static long getNoOfObjectsPresent() {
        return noOfObjectsPresent;
    }
}
