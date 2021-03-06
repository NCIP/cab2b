/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2005.08.23 at 10:44:28 GMT+05:30 
//


package edu.wustl.common.cde.xml;


/**
 * Java content class for XMLCDE complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/prg/XML/CDE22AUG05/CDECONFIG.xsd line 11)
 * <p>
 * <pre>
 * &lt;complexType name="XMLCDE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="publicId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="XMLPermissibleValues" type="{}XMLPermissibleValueType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="cache" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="lazyLoading" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface XMLCDE {


    /**
     * Gets the value of the XMLPermissibleValues property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the XMLPermissibleValues property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getXMLPermissibleValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.wustl.common.cde.xml.XMLPermissibleValueType}
     * 
     */
    java.util.List getXMLPermissibleValues();

    /**
     * Gets the value of the cache property.
     * 
     */
    boolean isCache();

    /**
     * Sets the value of the cache property.
     * 
     */
    void setCache(boolean value);

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getName();

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setName(java.lang.String value);

    /**
     * Gets the value of the publicId property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getPublicId();

    /**
     * Sets the value of the publicId property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setPublicId(java.lang.String value);

    /**
     * Gets the value of the lazyLoading property.
     * 
     */
    boolean isLazyLoading();

    /**
     * Sets the value of the lazyLoading property.
     * 
     */
    void setLazyLoading(boolean value);

}
