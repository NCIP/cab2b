package edu.wustl.cab2b.common.user;

import java.io.Serializable;

public class AdminServiceMetadata implements Serializable {
    private static final long serialVersionUID = 1L;

    private String serviceURL;

    private String serviceName;

    private String serviceDescription;

    private String hostingResearchCenter;

    private boolean isConfigured;

    /**
     * @return Returns the isAlive.
     */
    public boolean isConfigured() {
        return this.isConfigured;
    }

    /**
     * @param isAlive The isAlive to set.
     */
    public void setConfigured(final boolean isAlive) {
        this.isConfigured = isAlive;
    }

    /**
     * @return Returns the serviceURL.
     */
    public String getServiceURL() {
        return serviceURL;
    }

    /**
     * @param serviceURL The serviceURL to set.
     */
    public void setServiceURL(final String serviceURL) {
        this.serviceURL = serviceURL;
    }

    /**
     * @return Returns the seviceDescription.
     */
    public String getSeviceDescription() {
        return this.serviceDescription;
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
