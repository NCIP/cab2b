package edu.wustl.cab2b.admin.beans;

public class LoadModelResult {
    private CaDSRModelDetailsBean modelDetails;

    private boolean loaded;

    private String errorMessage;

    private String stackTrace;

    /**
     * @param modelDetails
     * @param loaded
     * @param errorMessage
     * @param exception
     */
    public LoadModelResult(
            CaDSRModelDetailsBean modelDetails,
            boolean loaded,
            String errorMessage,
            String stackTrace) {
        this.modelDetails = modelDetails;
        this.loaded = loaded;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
    }

    /**
     * @return the modelDetails
     */
    public CaDSRModelDetailsBean getModelDetails() {
        return modelDetails;
    }

    /**
     * @param modelDetails the modelDetails to set
     */
    public void setModelDetails(CaDSRModelDetailsBean modelDetails) {
        this.modelDetails = modelDetails;
    }

    /**
     * @return the loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * @param loaded the loaded to set
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the exception
     */
    public String getStackTrace() {
        return stackTrace;
    }

    /**
     * @param exception the exception to set
     */
    public void setStackTrace(String exception) {
        this.stackTrace = exception;
    }

}
