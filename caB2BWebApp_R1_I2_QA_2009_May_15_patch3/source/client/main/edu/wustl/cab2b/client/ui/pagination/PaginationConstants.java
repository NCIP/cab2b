package edu.wustl.cab2b.client.ui.pagination;

/**
 * Pagination related constants.
 * 
 * This class can be an interface. Refer to SwingConstants.java interface for some idea.
 * 
 * @author chetan_bh
 */
public final class PaginationConstants {	
	
	public static final String NUMERIC_PAGER = "numericPager";
	
	public static final String ALPHABETIC_PAGER = "alphabeticPager";
	
	/**
	 * Default number of elements displayed per page.
	 */
	public static int DEFAULT_ELEMENTS_PER_PAGE = 4;
	
	/**
	 * Default number of indices displayed in the page bar panel.
	 */
	public static int DEFAULT_INDICES_PER_VIEW = 5;
	
	/**
	 * Default next page text, used in page bar panel.
	 */
	public static String DEFAULT_PAGE_NEXT_STRING = ">";
	
	/**
	 * Default previous page text, used in page bar panel.
	 */
	public static String DEFAULT_PAGE_PREVIOUS_STRING = "<";
	
	/**
	 * Default next page indices text, used in page bar panel.
	 */
	public static String DEFAULT_PAGE_INDICES_NEXT_STRING = ">>";
	
	/**
	 * Default previous page indices text, used in page bar panel.
	 */
	public static String DEFAULT_PAGE_INDICES_PREVIOUS_STRING = "<<";
	
	/**
	 * Select all text, used in Group selection panel. 
	 */
	public static String SELECT_ALL_TEXT = "Select All";
	
	/**
	 * Clear all text, used in Group selection panel.
	 */
	public static String CLEAR_ALL_TEXT = "Clear All";
	
	/**
	 * Invert selection text, used in Group selection panel.
	 */
	public static String INVERT_SELECTION_TEXT = "Invert Selection";
	
	/**
	 * Vertical orientation of page elements in the page panel.
	 */
	public static int VETICAL_PAGE_ORIENTATION = 0;
	
	/**
	 * Horizontal orientation of page elements in the page panel.
	 */
	public static int HORIZONTAL_PAGE_ORIENTATION = 1;
	
	/**
	 * Default page element orientation in the page panel.
	 */
	public static int DEFAULT_PAGE_ORIENTATION = HORIZONTAL_PAGE_ORIENTATION;
}
