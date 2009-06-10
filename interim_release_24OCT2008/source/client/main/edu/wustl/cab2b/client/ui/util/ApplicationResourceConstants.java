package edu.wustl.cab2b.client.ui.util;

/**
 * This holds all the string constants needed to access Cab2bApplicationResources.properties
 * If value of any constant is changed here please update Cab2bApplicationResources.properties and vice versa.
 * @author Chandrakant Talele
 */
public interface ApplicationResourceConstants {
    /**
     * Main frame title
     */
    public static final String MAIN_FRAME_TITLE = "cab2b.main.frame.title";//=caB2B -- cancer Bench to Bedside
    /**
     * search help text for metadata search text box mouse over
     */
    public static final String MAIN_FRAME_SEARCH_HELP_TEXT = "cab2b.main.frame.searchhelptext";//=(E.g. Participant, Gene, Experiment, Protein, Specimen)

    //Experiment Module
    /**
     * Title of experiment frame
     */
    public static final String EXPERIMENT_FRAME_TITLE = "cab2b.experiment.frame.title";//";//=My Experiments
    /**
     * Title of experiment details frame
     */
    public static final String EXPERIMENT_DETAILS_TEXT = "cab2b.experiment.newexp.expdetailstab.text";//=Experiment Details
    /**
     * Title of data-list tab
     */
    public static final String DATALIST_TAB_TEXT = "cab2b.experiment.newexp.datalisttab.text";//=Select Data List
    /**
     * Text for summary tab 
     */
    public static final String SUMMARY_TAB_TEXT = "cab2b.experiment.newexp.summarytab.text";//=Summary

    //Search Module
    /**
     * Title of search frame
     */
    public static final String SEARCH_FRAME_TITLE = "cab2b.search.frame.title";//=Search Data For Experiment
    /**
     * Title of category tab
     */
    public static final String CATEGORY_TAB_TEXT = "cab2b.search.categotytab.text";//=Choose Search Category
    /**
     * Heading in experiment box
     */
    public static final String EXPERIMENT_BOX_TEXT = "cab2b.search.categorytab.myexperiments.text";//=My Experiments
    /**
     * Heading in category box
     */
    public static final String CATEGORY_BOX_TEXT = "cab2b.search.categorytab.mycategories.text";//=My Categories
    /**
     * Heading in query box
     */
    public static final String QUERY_BOX_TEXT = "cab2b.search.categorytab.mysearchqueries.text";//=My Searh Queries
    /**
     * Heading in popular category box
     */
    public static final String POPULAR_CATEGORY_BOX_TEXT = "cab2b.search.categorytab.popularcategories.text";//=Popular Categories
    /**
     * Title of category search
     */
    public static final String CATEGORY_SEARCH_TITLE = "cab2b.search.categorytab.categorysearch.title";//=Category Search
    /**
     * Title of metadata search
     */
    public static final String METADATA_SEARCH_TITLE = "cab2b.search.categorytab.categorysearch.advancedsearch.title";//=Advanced Search
    /**
     * Text to show in metadata search, for class
     */
    public static final String METADATA_SEARCH_CLASS_TEXT = "cab2b.search.categorytab.categorysearch.advancedsearch.classcheckbox.text";//=Class
    /**
     * Text to show in metadata search, for attribute
     */
    public static final String METADATA_SEARCH_ATTRIBUTE_TEXT = "cab2b.search.categorytab.categorysearch.advancedsearch.attributecheckbox.text";//=Attribute
    /**
     * Text to show in metadata search, for permissible values
     */
    public static final String METADATA_SEARCH_PV_TEXT = "cab2b.search.categorytab.categorysearch.advancedsearch.permivalues.text";//=Permissible Values
    /**
     * Add limit text
     */
    public static final String ADD_LIMIT_TEXT = "cab2b.search.limitstab.text";//=Add Limits

    //Common Button text.
    /**
     * Text to be shown on search button
     */
    public static final String BUTTON_SEARCH = "button.search.text";//=Search
    /**
     * Text to be shown on next button
     */
    public static final String BUTTON_NEXT = "button.next.text";//=Next
    /**
     * Text to be shown on previous button
     */
    public static final String BUTTON_PREVIOUS = "button.previous.text";//=Previous
    /**
     * Text to be shown on back button
     */
    public static final String BUTTON_BACK = "button.back.text";//=Back
    /**
     * Text to be shown on reset button
     */
    public static final String BUTTON_RESET = "button.reset.text";//=Reset
    /**
     * Text to be shown on save button
     */
    public static final String BUTTON_SAVE = "button.save.text";//=Save
    /**
     * Text to be shown on delete button
     */
    public static final String BUTTON_DELETE = "button.delete.text";//=Delete
    /**
     * Text to be shown on finish button
     */
    public static final String BUTTON_FINISH = "button.finish.text";//=Finish
    /**
     * Text to be shown on cancel button
     */
    public static final String BUTTON_CANCEL = "button.cancel.text";//=Cancel

    //Common Hyperlink text.
    /**
     * Text to be shown as home link
     */
    public static final String HYPERLINK_HOME = "hyperlink.home.text";//=Home
    /**
     * Text to be shown as login link
     */
    public static final String HYPERLINK_LOGIN = "hyperlink.login.text";//=Login
    /**
     * Text to be shown as logout link
     */
    public static final String HYPERLINK_LOGOUT = "hyperlink.logout.text";//=Logout
    /**
     * Text to be shown as next link
     */
    public static final String HYPERLINK_NEXT = "hyperlink.next.text";//=Next
    /**
     * Text to be shown as previous link
     */
    public static final String HYPERLINK_PREVIOUS = "hyperlink.previous.text";//=Previous
    /**
     * Text to be shown as show all link
     */
    public static final String HYPERLINK_SHOW_ALL = "hyperlink.showall.text";//=Show All
    /**
     *  Text to be shown as MySettings link 
     */
    public static final String MYSETTINGS_FRAME_TITLE = "cab2b.mysettings.frame.title";//=My Settings 
    
    public static final String SERVICE_URLS_SUCCESS = "cab2b.serviceURLs.status.success";//=(Configured Successfully)
    
    public static final String SERVICE_URLS_FAILURE = "cab2b.serviceURLs.status.failure";// = (Unable to Configure)
}
