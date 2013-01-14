/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common;

import java.io.Serializable;

public class IdName implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 9046059374207557535L;

    private Long id;

    private String name;

    private Long originalEntityId;

    public IdName(Long id, String name, Long originalEntityId) {
        this.id = id;
        this.name = name;
        this.originalEntityId = originalEntityId;
    }
    
    public IdName(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOriginalEntityId() {
        return originalEntityId;
    }

    public void setOriginalEntityId(Long originalEntityId) {
        this.originalEntityId = originalEntityId;
    }

    @Override
    public String toString() {
        return name;
    }
}
