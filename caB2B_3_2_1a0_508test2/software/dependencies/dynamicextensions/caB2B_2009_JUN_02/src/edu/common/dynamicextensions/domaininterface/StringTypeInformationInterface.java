package edu.common.dynamicextensions.domaininterface;


/**
 * This is a primitive attribute of type String.Using this information a column of type string is prepared.
 * @author geetika_bangard
 */
public interface StringTypeInformationInterface extends AttributeTypeInformationInterface
{

    
    /**
     * Size of the string
     * @return Returns the size.
     */
    Integer getSize();
    /**
     * @param size The size to set.
     */
    void setSize(Integer size);

    
}
