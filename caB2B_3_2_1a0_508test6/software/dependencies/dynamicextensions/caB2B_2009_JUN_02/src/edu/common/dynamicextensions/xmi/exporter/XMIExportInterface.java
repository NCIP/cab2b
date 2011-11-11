/*
 * Created on Aug 13, 2007
 * @author
 *
 */
package edu.common.dynamicextensions.xmi.exporter;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;


/**
 * @author preeti_lodha
 *
 * Interface for XMI Export
 */
public interface XMIExportInterface
{
	public void exportXMI(String filename, javax.jmi.reflect.RefPackage extent, String xmiVersion) throws IOException, TransformerException;
	public void exportXMI(String filename, EntityGroupInterface entityGroup, String xmiVersion) throws IOException, Exception;
}
