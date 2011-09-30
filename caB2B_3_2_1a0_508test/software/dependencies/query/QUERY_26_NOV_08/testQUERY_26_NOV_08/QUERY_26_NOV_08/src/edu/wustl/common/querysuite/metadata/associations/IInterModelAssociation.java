package edu.wustl.common.querysuite.metadata.associations;

import edu.wustl.common.querysuite.metadata.associations.IMetadataInterModelAssociation;

public interface IInterModelAssociation extends IMetadataInterModelAssociation {

    /**
     * @return the source service url.
     */
    String getSourceServiceUrl();

    /**
     * @param url the source url.
     * 
     */
    void setSourceServiceUrl(String url);

    /**
     * @return the target service url
     */
    String getTargetServiceUrl();

    /**
     * @param url the target url
     * 
     */
    void setTargetServiceUrl(String url);

    /**
     * @return association with swapped source and target attributes, urls.
     */
    public IInterModelAssociation reverse();
}
