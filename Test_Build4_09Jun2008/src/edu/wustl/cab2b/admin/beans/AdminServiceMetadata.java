package edu.wustl.cab2b.admin.beans;

import java.io.Serializable;

import org.apache.axis.message.addressing.AttributedURI;

public class AdminServiceMetadata implements Serializable {
    private static final long serialVersionUID = 1L;

    private AttributedURI serviceURL;

    private String serviceName;

    private String serviceDescription;

    private String hostingResearchCenter;

    private boolean isAlive;

    /**
     * @return Returns the isAlive.
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * @param isAlive The isAlive to set.
     */
    public void setAlive(final boolean isAlive) {
        this.isAlive = isAlive;
    }

    /**
     * @return Returns the serviceURL.
     */
    public AttributedURI getServiceURL() {
        return serviceURL;
    }

    /**
     * @param serviceURL The serviceURL to set.
     */
    public void setServiceURL(final AttributedURI serviceURL) {
        this.serviceURL = serviceURL;
    }

    /**
     * @return Returns the seviceDescription.
     */
    public String getSeviceDescription() {
        return serviceDescription;
    }

    /**
     * @param seviceDescription The seviceDescription to set.
     */
    public void setSeviceDescription(final String seviceDescription) {
        this.serviceDescription = seviceDescription;
    }

    /**
     * @return Returns the seviceName.
     */
    public String getSeviceName() {
        return serviceName;
    }

    /**
     * @param seviceName The seviceName to set.
     */
    public void setSeviceName(final String seviceName) {
        this.serviceName = seviceName;
    }

    /**
     * @return the hostingResearchCenter
     */
    public String getHostingResearchCenter() {
        return hostingResearchCenter;
    }

    /**
     * @param hostingResearchCenter the hostingResearchCenter to set
     */
    public void setHostingResearchCenter(String hostingResearchCenter) {
        this.hostingResearchCenter = hostingResearchCenter;
    }

}
