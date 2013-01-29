/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.xmi.importer;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.SemanticAnnotatableInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import gov.nih.nci.cagrid.metadata.common.SemanticMetadata;
import edu.common.dynamicextensions.domaininterface.EntityInterface;


public class XMIImporterUtil
{

	 /**
     * Stores the SemanticMetadata to the owner which can be class or attribute
     * @param owner EntityInterface OR AttributeInterface
     * @param semanticMetadataArr Semantic Metadata array to set.
     */
    public static void setSemanticMetadata(SemanticAnnotatableInterface owner, SemanticMetadata[] semanticMetadataArr) {
        if(semanticMetadataArr==null) {
            return;
        }
        DomainObjectFactory deFactory = DomainObjectFactory.getInstance();
        for (int i = 0; i < semanticMetadataArr.length; i++) {
            SemanticPropertyInterface semanticProp = deFactory.createSemanticProperty();
            semanticProp.setSequenceNumber(i);
            semanticProp.setConceptCode(semanticMetadataArr[i].getConceptCode());
            semanticProp.setTerm(semanticMetadataArr[i].getConceptName());
            owner.addSemanticProperty(semanticProp);
        }
    }
    
    public static List<ArrayList> getProcessedContainerList()
	{
		List<ArrayList> processedContainerListEntityList = new ArrayList<ArrayList>();
		ArrayList<ContainerInterface> processedContainerList = new ArrayList<ContainerInterface>();
		ArrayList<EntityInterface> processedEntityList = new ArrayList<EntityInterface>();
		processedContainerListEntityList.add(0, processedContainerList);
		processedContainerListEntityList.add(1, processedEntityList);
		return processedContainerListEntityList;
	}
}
