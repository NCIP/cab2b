/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domaininterface;


/**
 * This is a primitive attribute of type byte array.This type of primitive attribute  can be used for
 * storing large amount of data such as image file or word file. 
 * @author geetika_bangard
 */
public interface ByteArrayTypeInformationInterface extends AttributeTypeInformationInterface 
{
    /**
	 * This method returns the Content type of the binary data (or file), e.g. JPG, DOC etc..
	 * @return the Content type of the binary data (or file).
	 */
     String getContentType();
   /**
	 * This method sets the Content type of the binary data (or file) to be stored.
	 * @param contentType The contentType to be set.
	 */
    void setContentType(String contentType);


}
