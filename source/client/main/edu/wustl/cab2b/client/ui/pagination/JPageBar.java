package edu.wustl.cab2b.client.ui.pagination;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;

/**
 * A panel to display navigation hyperlinks like next page, prevoius page
 * to move sequentially from current page to next and previous pages
 * respectively, and page indices hyperlinks for random access of pages.
 * 
 * If number of page indices to display is large, there will be hyperlinks to 
 * access next and previous set of page indices.
 * 
 * @author chetan_bh
 */
public class JPageBar extends Cab2bPanel implements ActionListener{
	
	@Deprecated
	JComboBox paginationTypeCombo;
	
	@Deprecated
	JComboBox elementsPerPageCombo;
	
	/**
	 * Panel to hold indices hyperlinks.
	 */
	JXPanel indicesPanel;
	
	/**
	 * A model for page bar. To handle page indices
	 */
	PageBarModel pageBarModel;
	
	/**
	 * TODO Not functional yet.
	 * A model for second level page bar.
	 */
	PageBarModel subPageBaraModel;
	
	/**
	 * A vector of all page indices.
	 */
	Vector<String> indices;
	
	/**
	 * A vector of all sub page indices.
	 */
	Vector subIndices;
	
	/**
	 * A subset of page indices which is currently displayed in the page bar. 
	 */
	Vector<String> currentIndices;
	
	/**
	 * Reference to parent Pagination panel.
	 */
	JPagination pagination;
	
	/**
	 * Reference to the current pagination model.
	 */
	PaginationModel paginationModel;
	
	String nextPageText = PaginationConstants.DEFAULT_PAGE_NEXT_STRING;
	
	String previousPageText = PaginationConstants.DEFAULT_PAGE_PREVIOUS_STRING;
	
	String nextPageIndicesText = PaginationConstants.DEFAULT_PAGE_INDICES_NEXT_STRING;
	
	String previousInidicesText = PaginationConstants.DEFAULT_PAGE_INDICES_PREVIOUS_STRING;
	
	/**
	 * Constructs page bar for a given pagination component.
	 * @param indices Collection of indexes.
	 * @param subIndices Collection of sub indexes.
	 * @param pagination Reference to pagination component.
	 */
	public JPageBar(Vector<String> indices, Vector subIndices, JPagination pagination)
	{
		this(indices, subIndices, pagination, new String[] { PaginationConstants.DEFAULT_PAGE_INDICES_PREVIOUS_STRING, 
															 PaginationConstants.DEFAULT_PAGE_PREVIOUS_STRING,
															 PaginationConstants.DEFAULT_PAGE_NEXT_STRING,
															 PaginationConstants.DEFAULT_PAGE_INDICES_NEXT_STRING
			  });
	}
	
	/**
	 * Constructs page bar for a given pagination component with customizable navigational hyperlinks text.
	 * @param indices Collection of indexes.
	 * @param subIndices Collection of sub indexes.
	 * @param pagination Reference to pagination component.
	 * @param navigationLinksText Array of string text for navigational hyperlinks.
	 */
	public JPageBar(Vector<String> indices, Vector subIndices, JPagination pagination, String[] navigationLinksText)
	{
		if(navigationLinksText != null && navigationLinksText.length == 4)
		{
			if(navigationLinksText[0] != null && !(navigationLinksText[0].trim().equals("")))
				previousInidicesText = navigationLinksText[0];
			if(navigationLinksText[1] != null && !(navigationLinksText[1].trim().equals("")))
				previousPageText = navigationLinksText[1];
			if(navigationLinksText[2] != null && !(navigationLinksText[2].trim().equals("")))
				nextPageText = navigationLinksText[2];
			if(navigationLinksText[3] != null && !(navigationLinksText[3].trim().equals("")))
				nextPageIndicesText = navigationLinksText[3];
		}
		
		this.indices = indices;
		this.subIndices = subIndices;
		
		this.pagination = pagination;
		this.paginationModel = pagination.getPaginationModel();
		
		pageBarModel = new PageBarModel(indices);
		subPageBaraModel = new PageBarModel(subIndices);
		
		//currentIndices = pageBarModel.nextIndices();
		
		//Logger.out.info("indices "+indices);
		// TODO how to dynamically update this list of plugged in pagers.
		paginationTypeCombo = new JComboBox(new Object[] {"Numeric","Alphabetic","Frequency","Keyword"});
		elementsPerPageCombo = new JComboBox(new Object[] {"5","10","15","20"});
		intiGUI();
	}
	
	/**
	 * Initialize the page bar GUI.
	 */
	private void intiGUI()
	{
		setLayout(new RiverLayout());
		//setPreferredSize(new Dimension(200,50));
		//this.add(indicesLabel);
		currentIndices = pageBarModel.nextIndices();
		indicesPanel = getIndicesPanel(currentIndices);
		//this.add(paginationTypeCombo);  // TODO needed later
		//this.add(elementsPerPage);      // TODO needed as and when required.
		this.add(indicesPanel);
		
	}
	
	/**
	 * By Default this function adds Next, Forward > >>
	 * and previous, Bacward < << links to the given indices.
	 * @param indices
	 * @return
	 */
	private JXPanel getIndicesPanel(Vector<String> indices)
	{
		/* Remove below comments to fix the Bug#xxx on pagination. */
		//if(pageBarModel.hasPreviousIndices())
		//{
			indices.add(0, previousInidicesText);
			indices.add(1, previousPageText);
		//}else
		//{
		//	if(paginationModel.hasPreviousPage())
		//		indices.add(0, previousPageText);
		//}
		
		//if(paginationModel.hasNextPage())
			indices.add(nextPageText);
		
		//if(pageBarModel.hasNextIndices())
			indices.add(nextPageIndicesText);
		
		JXPanel indicesPanel = new Cab2bPanel();
		indicesPanel.setLayout(new FlowLayout());
		Iterator iter = indices.iterator();
		
		while(iter.hasNext())
		{
			String index = (String) iter.next();
			//JXHyperlink hyperlink = new JXHyperlink();
			Cab2bHyperlink hyperlink = new Cab2bHyperlink();
			hyperlink.setText(index);
			hyperlink.addActionListener(this);
			indicesPanel.add(hyperlink);
		}
		
		indices.removeElement(previousInidicesText);
		indices.removeElement(previousPageText);
		indices.removeElement(nextPageText);
		indices.removeElement(nextPageIndicesText);
		return indicesPanel;
	}
	
	/** Replaces the current indices panel with the new panel which has different indices. */
	protected void changeIndicesPanel(JXPanel newIndicesPanel)
	{
		this.remove(indicesPanel);
		indicesPanel = newIndicesPanel;
		this.add(indicesPanel);
		this.updateUI();
	}
	
	/**
	 * Action listener for page bar hyperlinks.
	 */
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj instanceof Cab2bHyperlink)
		{
			Cab2bHyperlink hyperlink = (Cab2bHyperlink) obj;
			//hyperlink.setClickedColor(Color.RED);
			String index = hyperlink.getText();
			//Logger.out.info("index selected "+index);
			String currPageIndex = "";
			if(index.equals(nextPageIndicesText))
			{
				if(pageBarModel.hasNextIndices())
				{
					currentIndices = pageBarModel.nextIndices();
					//Logger.out.info("indices actionPerformed >>>> "+indices);
					JXPanel newIndicesPanel = getIndicesPanel(currentIndices);
					changeIndicesPanel(newIndicesPanel);

					String firstPageIndex = currentIndices.get(0);
					Vector<PageElement> firstPage = paginationModel.getPage(firstPageIndex);
					JXPanel newPagePanel = pagination.getPagePanel(firstPage);
					pagination.changePagePanel(newPagePanel);
					currPageIndex = firstPageIndex;
				}
			}else if(index.equals(previousInidicesText))
			{
				if(pageBarModel.hasPreviousIndices())
				{
					currentIndices = pageBarModel.previousIndices();
					//Logger.out.info("indices actionPerformed <<<< "+indices);
					JXPanel newIndicesPanel = getIndicesPanel(currentIndices);
					changeIndicesPanel(newIndicesPanel);

					String firstPageIndex = currentIndices.get(0);
					Vector<PageElement> firstPage = paginationModel.getPage(firstPageIndex);
					JXPanel newPagePanel = pagination.getPagePanel(firstPage);
					pagination.changePagePanel(newPagePanel);
					currPageIndex = firstPageIndex;
				}
			}else
			{
				currPageIndex = sequentialNavigation(index);
			}
			
//			if(!pageBarModel.hasNextIndices() && !paginationModel.hasNextPage())
//				changeIndicesPanel(getIndicesPanel(pageBarModel.currentIndices()));
//			
//			if(!pageBarModel.hasPreviousIndices() && !paginationModel.hasPreviousPage())
//				changeIndicesPanel(getIndicesPanel(pageBarModel.currentIndices()));
			
			displayLinkColor(currPageIndex);
		}
	}
	
	protected String sequentialNavigation(String index)
	{
		String currPageIndex = "";
		
		if(index.equals(nextPageText))
		{
			if(paginationModel.hasNextPage())
			{
				Vector<PageElement> nextPage = paginationModel.nextPage();
				JXPanel newPagePanel = pagination.getPagePanel(nextPage);
				pagination.changePagePanel(newPagePanel);
				currPageIndex = paginationModel.getCurrentPageIndex();
				//Logger.out.info("current page Index > "+currPageIndex);
				if(! currentIndices.contains(currPageIndex))
				{
					currentIndices = pageBarModel.nextIndices();
					JXPanel newIndicesPanel = getIndicesPanel(currentIndices);
					changeIndicesPanel(newIndicesPanel);
				}
			}
		}else if(index.equals(previousPageText))
		{
			if(paginationModel.hasPreviousPage())
			{
				Vector<PageElement> previousPage = paginationModel.previousPage();
				JXPanel newPagePanel = pagination.getPagePanel(previousPage);
				pagination.changePagePanel(newPagePanel);
				currPageIndex = paginationModel.getCurrentPageIndex();
				//Logger.out.info("current page index < "+currPageIndex);
				if(! currentIndices.contains(currPageIndex))
				{
					currentIndices = pageBarModel.previousIndices();
					JXPanel newIndicesPanel = getIndicesPanel(currentIndices);
					changeIndicesPanel(newIndicesPanel);
				}
			}
		}else
		{
			currPageIndex = index;
			Vector<PageElement> pageRequested = paginationModel.getPage(index);
			JXPanel newPagePanel = pagination.getPagePanel(pageRequested);
			pagination.changePagePanel(newPagePanel);
		}
		return currPageIndex;
	}
	
	protected void displayLinkColor(String currentPageIndex)
	{
		//Logger.out.info("JPAGEBAR ::: displayLinkColor :: currentPageIndex : "+currentPageIndex);
		/* If current page index is null or empty just return.
		 * so that last time colored link remain the same. */
		if(currentPageIndex == null || currentPageIndex.equals(""))
			return;
		for(int i = 0; i< indicesPanel.getComponentCount(); i++)
		{
			Cab2bHyperlink hyperLink = (Cab2bHyperlink) indicesPanel.getComponent(i);
			//Logger.out.info("Hyperlink["+i+"] -> "+hyperLink.getText());
			if(hyperLink.getText().equalsIgnoreCase(currentPageIndex))
			{
				hyperLink.setClicked(true);
				hyperLink.setClickedColor(Color.RED);
				hyperLink.updateUI();
				hyperLink.repaint();
			}else
				hyperLink.setClicked(false);
		}
		indicesPanel.revalidate();
		indicesPanel.updateUI();
		indicesPanel.repaint();
	}
	
	/**
	 * Model for page bar.
	 * 
	 * @author chetan_bh
	 */
	private class PageBarModel
	{
		/** Indices to display per view. */
		int indicesPerView = PaginationConstants.DEFAULT_INDICES_PER_VIEW;
		
		/** A collection of set of page indices.  */
		Vector<Vector<String>> brokenPageIndices;
		
		/** Current page index. */
		int currentPageIndices = -1;
		
		/** Count of broken page indices. */
		int noOfBrokenPageIndices = 0;
		
		public PageBarModel(Vector fullPageIndices)
		{
			brokenPageIndices = breakFullIndices(fullPageIndices);
		}
		
		/** Returns true if there is next set of indices avalable, false otherwise. */
		public boolean hasNextIndices()
		{
			if(currentPageIndices == brokenPageIndices.size()-1)
				return false;
			return true;
		}
		
		/** Returns true if there is previous set of indices available, false otherwise. */
		public boolean hasPreviousIndices()
		{
			if(currentPageIndices == 0)
				return false;
			return true;
		}
		
		/** Returns next set of page indices. */
		public Vector<String> nextIndices()
		{
			return brokenPageIndices.get(++currentPageIndices);
		}
		
		/** Returns previous set of page indices. */
		public Vector<String> previousIndices()
		{
			return brokenPageIndices.get(--currentPageIndices);
		}
		
		/** Returns current set of page indices. */
		public Vector<String> currentIndices()
		{
			return currentIndices;
		}
		
		public String toString()
		{
			return brokenPageIndices.toString();
		}
		
		/**
		 * Returns a vector of vector of paghe indices.
		 * @param fullIndices full set of indices.
		 * @return 
		 */
		private Vector<Vector<String>> breakFullIndices(Vector fullIndices)
		{
			Vector<Vector<String>> returner = new Vector<Vector<String>>();
			Iterator indexIter = fullIndices.iterator();
			
			int counter = 0;
			Vector<String> subIndices = new Vector<String>();

			while(indexIter.hasNext())
			{
				String index = (String) indexIter.next();

				if(counter == indicesPerView)
				{
					counter = 0;
					noOfBrokenPageIndices++;
					returner.add(subIndices);
					subIndices = new Vector<String>();
				}
				subIndices.add(index);
				counter++;
			}
			returner.add(subIndices);
			return returner;
		}
	}
	
//	// ----------------------------
//	
//	public void mouseWheelMoved(MouseWheelEvent e)
//	{
//		//Logger.out.info("mouse wheel event Scroll_Type : "+e.getScrollType()+", Scroll_Mount : "+e.getScrollAmount()+", "+e.getSource().getClass());
//		//Logger.out.info("MouseWheelEvent UnitScroll "+MouseWheelEvent.WHEEL_UNIT_SCROLL+", BlockScroll "+MouseWheelEvent.WHEEL_BLOCK_SCROLL);
//		//Logger.out.info("e.getWheelRotation() "+e.getWheelRotation());
//		//Logger.out.info("e.getUnitsToScroll() "+e.getUnitsToScroll());
//		
//		/* Return without doing nothing, if the mouse wheel 
//		 * support is not enabled for JPagination. */
//		if(!pagination.mouseWheelEnabled)
//			return;
//		
//		if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) 
//		{
//			/* Wheel rotation is either 1 or -1. */
//			int wheelRotation = e.getWheelRotation();
//			String currentPageIndex = "";
//			if(wheelRotation == 1)      /* If mouse wheel is rotated down/toward the user.  -> move behind, decrease the page index. */
//			{
//				currentPageIndex = sequentialNavigation(previousPageText);
//			}
//			else if(wheelRotation == -1) /* If mouse wheel is rotated up/away from the user. -> move forward, increase the page index. */
//			{
//				currentPageIndex = sequentialNavigation(nextPageText);
//			}
//			Logger.out.info("JPAGEBAR :: currPageIndex : "+currentPageIndex);
//			displayLinkColor(currentPageIndex);
//			
//		}
//	}
//	
//	// ----------------------------
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Vector subIndices = new Vector();
		
		Vector<PageElement> elements = new Vector<PageElement>();
		for(int i = 0; i < 52; i++)
		{
			PageElement element = new PageElementImpl();
			element.setDisplayName("ABC-"+i);
			elements.add(element);
		}
		NumericPager numPager = new NumericPager( elements );
		//PaginationModel pageModel = new PaginationModel(elements, numPager);
		
		Vector allIndices = numPager.getAllPageIndices();
		JPagination pagi = new JPagination(elements, numPager);
		
		JPageBar pageBar = new JPageBar(allIndices, subIndices, pagi);
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(400,70));
		frame.getContentPane().add(pageBar);
		frame.setVisible(true);
	}

	
	public String getNextPageText()
	{
		return nextPageText;
	}

	
	public String getPreviousPageText()
	{
		return previousPageText;
	}
	
}
