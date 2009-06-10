/**
 * 
 */
package edu.wustl.cab2bwebapp.bean;

/**
 * @author gaurav_mehta
 *
 */
public class ModelBean {
    
    private Long id;
    
    private String name;
    
    public ModelBean(Long id, String name) {
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
    
}
