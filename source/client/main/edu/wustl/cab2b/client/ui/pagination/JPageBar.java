package edu.wustl.cab2b.client.ui.pagination;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bStandardFonts;

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
public class JPageBar extends Cab2bPanel implements ActionListener {
    private static final long serialVersionUID = 1L;

    /**
     * Panel to hold indices hyperlinks.
     */
    private JXPanel indicesPanel;

    /**
     * A model for page bar. To handle page indices
     */
    private PageBarModel pageBarModel;

    /**
     * TODO Not functional yet.
     * A model for second level page bar.
     */
    private PageBarModel subPageBaraModel;

    /**
     * A vector of all page indices.
     */
    private Vector<String> indices;

    /**
     * A vector of all sub page indices.
     */
    private Vector subIndices;

    /**
     * A subset of page indices which is currently displayed in the page bar. 
     */
    private Vector<String> currentIndices;

    /**
     * Reference to parent Pagination panel.
     */
    private JPagination pagination;

    /**
     * Reference to the current pagination model.
     */
    private PaginationModel paginationModel;

    /**
     * Constructs page bar for a given pagination component.
     * @param indices Collection of indexes.
     * @param subIndices Collection of sub indexes.
     * @param pagination Reference to pagination component.
     */
    public JPageBar(Vector<String> indices, Vector subIndices, JPagination pagination) {
        this(
                indices,
                subIndices,
                pagination,
                new String[] { PaginationConstants.DEFAULT_PAGE_INDICES_PREVIOUS_STRING, PaginationConstants.DEFAULT_PAGE_PREVIOUS_STRING, PaginationConstants.DEFAULT_PAGE_NEXT_STRING, PaginationConstants.DEFAULT_PAGE_INDICES_NEXT_STRING });
    }

    /**
     * Constructs page bar for a given pagination component with customizable navigational hyperlinks text.
     * @param indices Collection of indexes.
     * @param subIndices Collection of sub indexes.
     * @param pagination Reference to pagination component.
     * @param navigationLinksText Array of string text for navigational hyperlinks.
     */
    public JPageBar(Vector<String> indices, Vector subIndices, JPagination pagination, String[] navigationLinksText) {
        if (navigationLinksText != null && navigationLinksText.length == 4) {
            if (navigationLinksText[0] != null && !(navigationLinksText[0].trim().equals(""))) {
                PaginationConstants.DEFAULT_PAGE_INDICES_PREVIOUS_STRING = navigationLinksText[0];
            }
            if (navigationLinksText[1] != null && !(navigationLinksText[1].trim().equals(""))) {
                PaginationConstants.DEFAULT_PAGE_PREVIOUS_STRING = navigationLinksText[1];
            }
            if (navigationLinksText[2] != null && !(navigationLinksText[2].trim().equals(""))) {
                PaginationConstants.DEFAULT_PAGE_NEXT_STRING = navigationLinksText[2];
            }
            if (navigationLinksText[3] != null && !(navigationLinksText[3].trim().equals(""))) {
                PaginationConstants.DEFAULT_PAGE_INDICES_NEXT_STRING = navigationLinksText[3];
            }
        }

        this.indices = indices;
        this.subIndices = subIndices;

        this.pagination = pagination;
        this.paginationModel = pagination.getPaginationModel();

        pageBarModel = new PageBarModel(indices);
        subPageBaraModel = new PageBarModel(subIndices);

        // TODO how to dynamically update this list of plugged in pagers.        
        intiGUI();
    }

    /**
     * Initialize the page bar GUI.
     */
    private void intiGUI() {
        currentIndices = pageBarModel.nextIndices();
        indicesPanel = getIndicesPanel(currentIndices);
        this.add(indicesPanel);

    }

    /**
     * By Default this function adds Next, Forward > >>
     * and previous, Bacward < << links to the given indices.
     * @param indices
     * @return
     */
    private JXPanel getIndicesPanel(Vector<String> indices) {
        indices.add(0, PaginationConstants.DEFAULT_PAGE_INDICES_PREVIOUS_STRING);
        indices.add(1, PaginationConstants.DEFAULT_PAGE_PREVIOUS_STRING);
        indices.add(PaginationConstants.DEFAULT_PAGE_NEXT_STRING);
        indices.add(PaginationConstants.DEFAULT_PAGE_INDICES_NEXT_STRING);

        JXPanel indicesPanel = new Cab2bPanel(new FlowLayout());
        int firstPageIndex = 0;
        for (String index : indices) {
            Cab2bHyperlink hyperLink = new Cab2bHyperlink(true);
            hyperLink.setText(index);
            hyperLink.addActionListener(this);
            indicesPanel.add(hyperLink);

            if (firstPageIndex++ == 2) {
                hyperLink.setFont(Cab2bStandardFonts.ARIAL_BOLD_12);
                hyperLink.setClickedColor(Color.RED);
                hyperLink.setClicked(true);
                hyperLink.updateUI();
                hyperLink.repaint();
            }
        }

        indices.removeElement(PaginationConstants.DEFAULT_PAGE_INDICES_PREVIOUS_STRING);
        indices.removeElement(PaginationConstants.DEFAULT_PAGE_PREVIOUS_STRING);
        indices.removeElement(PaginationConstants.DEFAULT_PAGE_NEXT_STRING);
        indices.removeElement(PaginationConstants.DEFAULT_PAGE_INDICES_NEXT_STRING);
        return indicesPanel;
    }

    /** Replaces the current indices panel with the new panel which has different indices.
     * @param newIndicesPanel 
     */
    protected void changeIndicesPanel(JXPanel newIndicesPanel) {
        this.remove(indicesPanel);
        indicesPanel = newIndicesPanel;
        this.add(indicesPanel);
        this.updateUI();
    }

    /**
     * Action listener for page bar hyperlinks.
     * @param e actionEvent
     */
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj instanceof Cab2bHyperlink) {
            Cab2bHyperlink hyperlink = (Cab2bHyperlink) obj;
            String index = hyperlink.getText();
            String currPageIndex = "";
            if (index.equals(PaginationConstants.DEFAULT_PAGE_INDICES_NEXT_STRING)) {
                if (pageBarModel.hasNextIndices()) {
                    currentIndices = pageBarModel.nextIndices();
                    JXPanel newIndicesPanel = getIndicesPanel(currentIndices);
                    changeIndicesPanel(newIndicesPanel);

                    String firstPageIndex = currentIndices.get(0);
                    Vector<PageElement> firstPage = paginationModel.getPage(firstPageIndex);
                    JXPanel newPagePanel = pagination.getPagePanel(firstPage);
                    pagination.changePagePanel(newPagePanel);
                    currPageIndex = firstPageIndex;
                }
            } else if (index.equals(PaginationConstants.DEFAULT_PAGE_INDICES_PREVIOUS_STRING)) {
                if (pageBarModel.hasPreviousIndices()) {
                    currentIndices = pageBarModel.previousIndices();
                    JXPanel newIndicesPanel = getIndicesPanel(currentIndices);
                    changeIndicesPanel(newIndicesPanel);

                    String firstPageIndex = currentIndices.get(0);
                    Vector<PageElement> firstPage = paginationModel.getPage(firstPageIndex);
                    JXPanel newPagePanel = pagination.getPagePanel(firstPage);
                    pagination.changePagePanel(newPagePanel);
                    currPageIndex = firstPageIndex;
                }
            } else {
                currPageIndex = sequentialNavigation(index);
            }

            displayLinkColor(currPageIndex);
        }
    }

    /**
     * @param index
     * @return next or previous page index
     */
    protected String sequentialNavigation(String index) {
        String currPageIndex = "";

        if (index.equals(PaginationConstants.DEFAULT_PAGE_NEXT_STRING)) {
            if (paginationModel.hasNextPage()) {
                Vector<PageElement> nextPage = paginationModel.nextPage();
                JXPanel newPagePanel = pagination.getPagePanel(nextPage);
                pagination.changePagePanel(newPagePanel);

                currPageIndex = paginationModel.getCurrentPageIndex();
                if (!currentIndices.contains(currPageIndex)) {
                    currentIndices = pageBarModel.nextIndices();
                    JXPanel newIndicesPanel = getIndicesPanel(currentIndices);
                    changeIndicesPanel(newIndicesPanel);
                }
            }
        } else if (index.equals(PaginationConstants.DEFAULT_PAGE_PREVIOUS_STRING)) {
            if (paginationModel.hasPreviousPage()) {
                Vector<PageElement> previousPage = paginationModel.previousPage();
                JXPanel newPagePanel = pagination.getPagePanel(previousPage);
                pagination.changePagePanel(newPagePanel);
                currPageIndex = paginationModel.getCurrentPageIndex();
                if (!currentIndices.contains(currPageIndex)) {
                    currentIndices = pageBarModel.previousIndices();
                    JXPanel newIndicesPanel = getIndicesPanel(currentIndices);
                    changeIndicesPanel(newIndicesPanel);
                }
            }
        } else {
            currPageIndex = index;
            Vector<PageElement> pageRequested = paginationModel.getPage(index);
            JXPanel newPagePanel = pagination.getPagePanel(pageRequested);
            pagination.changePagePanel(newPagePanel);
        }
        return currPageIndex;
    }

    /**
     * Displays the current page selection color
     * @param currentPageIndex
     */
    protected void displayLinkColor(String currentPageIndex) {
        /* If current page index is null or empty just return.
         * so that last time colored link remain the same. */
        if (currentPageIndex == null || currentPageIndex.equals("")) {
            return;
        }
        for (int i = 0; i < indicesPanel.getComponentCount(); i++) {
            Cab2bHyperlink hyperLink = (Cab2bHyperlink) indicesPanel.getComponent(i);
            if (hyperLink.getText().equalsIgnoreCase(currentPageIndex)) {
                hyperLink.setClicked(true);
                hyperLink.setFont(Cab2bStandardFonts.ARIAL_BOLD_12);
                hyperLink.setClickedColor(Color.RED);
                hyperLink.updateUI();
                hyperLink.repaint();
            } else {
                hyperLink.setFont(Cab2bStandardFonts.ARIAL_PLAIN_12);
                hyperLink.setClicked(false);
            }
        }
        indicesPanel.revalidate();
        indicesPanel.updateUI();
        indicesPanel.repaint();
    }

    /**
     * @param args
     */
    /* public static void main(String[] args) {
     Vector<PageElement> elements = new Vector<PageElement>();
     for (int i = 0; i < 52; i++) {
     PageElement element = new PageElementImpl();
     element.setDisplayName("ABC-" + i);
     elements.add(element);
     }

     Vector subIndices = new Vector();
     NumericPager numPager = new NumericPager(elements);
     Vector<String> allIndices = numPager.getAllPageIndices();
     JPagination pagi = new JPagination(elements, numPager);
     JPageBar pageBar = new JPageBar(allIndices, subIndices, pagi);

     JFrame frame = new JFrame();
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.setSize(new Dimension(400, 70));
     frame.getContentPane().add(pageBar);
     frame.setVisible(true);
     }*/

    /**
     * @return Default's page text
     */
    public String getNextPageText() {
        return PaginationConstants.DEFAULT_PAGE_NEXT_STRING;
    }

    /**
     * @return Default's page text
     */
    public String getPreviousPageText() {
        return PaginationConstants.DEFAULT_PAGE_PREVIOUS_STRING;
    }

}
