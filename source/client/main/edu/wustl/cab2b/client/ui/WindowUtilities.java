package edu.wustl.cab2b.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowEvent;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXStatusBar;

import edu.wustl.cab2b.client.ui.controls.IDialogInterface;
import edu.wustl.common.util.logger.Logger;

public class WindowUtilities {

    /** A phantom Frame. */
    public static final Frame phantomFrame = new Frame();

    //	private static Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    //
    //	private static Cursor lastCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    protected static Point frameLocation = new Point(0, 0);

    protected static Dimension defaultPreferredSizeForDialog = new Dimension(200, 100);

    /** Tell system to use native look and feel.
     * Metal (Java) LAF is the default otherwise.
     */

    public static void setNativeLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            Logger.out.error("Error setting native LAF: " + e);
        }
    }

    public static void setJavaLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            Logger.out.error("Error setting Java LAF: " + e);
        }
    }

    public static void setMotifLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        } catch (Exception e) {
            Logger.out.error("Error setting Motif LAF: " + e);
        }
    }

    /** A simplified way to see a JPanel or other Container.
     *  Pops up a JFrame with specified Container as the content pane.
     */

    public static JFrame openInJFrame(Container content, int width, int height, String title, Color bgColor) {
        JFrame frame = new JFrame(title);
        frame.setBackground(bgColor);
        content.setBackground(bgColor);
        frame.setSize(width, height);
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        return (frame);
    }

    /** Uses Color.white as the background color. */

    public static JFrame openInJFrame(Container content, int width, int height, String title) {
        return (openInJFrame(content, width, height, title, Color.white));
    }

    /** Uses Color.white as the background color, and the
     *  name of the Container's class as the JFrame title.
     */

    public static JFrame openInJFrame(Container content, int width, int height) {
        return (openInJFrame(content, width, height, content.getClass().getName(), Color.white));
    }

    //  ******************* JXFrame *********************
    /**
     * Creates and returns a JXFrame with the specified title, containing
     * the component wrapped into a JScrollPane.
     * 
     * @param component the JComponent to wrap
     * @param title the title to show in the frame
     * @return a configured, packed and located JXFrame.
     */
    public static JXFrame wrapWithScrollingInFrame(JComponent component, String title) {
        JScrollPane scroller = new JScrollPane(component);
        return wrapInFrame(scroller, title);
    }

    /**
     * Creates and returns a JXFrame with the specified title, containing
     * two components individually wrapped into a JScrollPane.
     * 
     * @param leftComp the left JComponent to wrap
     * @param rightComp the right JComponent to wrap
     * @param title the title to show in the frame
     * @return a configured, packed and located JXFrame
     */
    public static JXFrame wrapWithScrollingInFrame(JComponent leftComp, JComponent rightComp, String title) {
        JComponent comp = Box.createHorizontalBox();
        comp.add(new JScrollPane(leftComp));
        comp.add(new JScrollPane(rightComp));
        JXFrame frame = wrapInFrame(comp, title);
        return frame;
    }

    /**
     * Creates and returns a JXFrame with the specified title, containing
     * the component.
     * 
     * @param component the JComponent to wrap
     * @param title the title to show in the frame
     * @return a configured, packed and located JXFrame.
     */
    public static JXFrame wrapInFrame(JComponent component, String title) {
        JXFrame frame = new JXFrame(title, false);
        JToolBar toolbar = new JToolBar();
        frame.getRootPaneExt().setToolBar(toolbar);
        frame.getContentPane().add(BorderLayout.CENTER, component);
        //      frame.getContentPane().add(BorderLayout.NORTH, toolbar);
        frame.pack();
        frame.setLocation(frameLocation);
        if (frameLocation.x == 0) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle(title);
        }
        frameLocation.x += 30;
        frameLocation.y += 30;
        return frame;
    }

    /**
     * Creates, shows and returns a JXFrame with the specified title, containing
     * the component wrapped into a JScrollPane.
     * 
     * @param component the JComponent to wrap
     * @param title the title to show in the frame
     * @return a configured, packed and located JXFrame.
     * @see #wrapWithScrollingInFrame(JComponent, String)
     */
    public static JXFrame showWithScrollingInFrame(JComponent component, String title) {
        JXFrame frame = wrapWithScrollingInFrame(component, title);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Creates and returns a JXFrame with the specified title, containing
     * two components individually wrapped into a JScrollPane.
     * 
     * @param leftComp the left JComponent to wrap
     * @param rightComp the right JComponent to wrap
     * @param title the title to show in the frame
     * @return a configured, packed and located JXFrame
     */
    public static JXFrame showWithScrollingInFrame(JComponent leftComp, JComponent rightComp, String title) {
        JXFrame frame = wrapWithScrollingInFrame(leftComp, rightComp, title);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Creates, shows and returns a JXFrame with the specified title, containing
     * the component.
     * 
     * @param component the JComponent to wrap
     * @param title the title to show in the frame
     * @return a configured, packed and located JXFrame.
     */
    public static JXFrame showInFrame(JComponent component, String title) {
        JXFrame frame = wrapInFrame(component, title);
        frame.setVisible(true);
        return frame;
    }

    public static void addAction(JXFrame frame, Action action) {
        JToolBar toolbar = frame.getRootPaneExt().getToolBar();
        if (toolbar != null) {
            AbstractButton button = toolbar.add(action);
            button.setFocusable(false);
        }
    }

    public static void addMessage(JXFrame frame, String message) {
        JXStatusBar statusBar = getStatusBar(frame);
        statusBar.add(new JLabel(message), JXStatusBar.Constraint.ResizeBehavior.FILL);
    }

    /**
     * Returns the <code>JXFrame</code>'s status bar. Lazily creates and 
     * sets an instance if necessary.
     * @param frame the target frame
     * @return the frame's statusbar
     */
    public static JXStatusBar getStatusBar(JXFrame frame) {
        JXStatusBar statusBar = frame.getRootPaneExt().getStatusBar();
        if (statusBar == null) {
            statusBar = new JXStatusBar();
            frame.getRootPaneExt().setStatusBar(statusBar);
        }
        return statusBar;
    }

    public static void centerFrame(JFrame frame) {
        //	Center frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = frame.getSize();
        screenSize.height = screenSize.height / 2;
        screenSize.width = screenSize.width / 2;
        size.height = size.height / 2;
        size.width = size.width / 2;
        int y = screenSize.height - size.height;
        int x = screenSize.width - size.width;
        frame.setLocation(x, y);
    }

    /**
     * Since we want to control what happens when a user attempts to close
     * out the frame, we need to override the
     * javax.swing.JFrame.processWindowEvent() method.
     * 
     * TODO This is not working properly.
     * 
     * @param e WindowEvent being passed as a result of user actions at the
     *          Window level.
     */
    public static void processWindowEvent(WindowEvent e, JFrame frame) {
        //System.out.println("processWindowEvent " + e.getID() + "  WindowEvent.WINDOW_CLOSING "
        //		+ WindowEvent.WINDOW_CLOSING);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {

            int exit = JOptionPane.showConfirmDialog(frame, "Are you sure?");
            //System.out.println("exit option " + exit + " YES_OPTION " + JOptionPane.YES_OPTION);
            if (exit == JOptionPane.YES_OPTION) {
                //System.out.println("exiting ...");
                System.exit(0);
            } else
                return;
        }
    }

    /* ---------- Some more Utils -------------- */

    /** Center a window on the screen.
     *
     * @param w The window to center.
     */

    public static final void centerWindow(Window w) {
        Dimension s_size, w_size;
        int x, y;

        s_size = w.getToolkit().getScreenSize();
        w_size = w.getSize();

        x = (s_size.width - w_size.width) / 2;
        y = (s_size.height - w_size.height) / 2;

        w.setLocation(x, y);
    }

    /** Center a window within the bounds of another window.
     *
     * @param w The window to center.
     * @param parent The window to center within.
     */

    public static final void centerWindow(Window parent, Window w) {
        Dimension p_size, w_size, s_size;
        int x, y;

        p_size = parent.getSize();
        w_size = w.getSize();
        s_size = w.getToolkit().getScreenSize();
        Point p_loc = parent.getLocationOnScreen();

        x = ((p_size.width - w_size.width) / 2) + p_loc.x;
        y = ((p_size.height - w_size.height) / 2) + p_loc.y;

        // If placing the window at (x,y) would make part if it off-screen,
        // then center it in the middle of the screen instead.

        if (((x + w_size.width) > s_size.width) || (x < 0) || ((y + w_size.height) > s_size.height) || (y < 0))
            centerWindow(w);
        else
            w.setLocation(x, y);
    }

    /** Center a window within the bounds of a component's parent window.
     *
     * @param w The window to center.
     * @param c The component within whose parent window this window should be
     * centered. If a window cannot be found in the component hierarchy above
     * <code>c</code>, the window is centered on the screen.
     */

    public static final void centerWindow(Component c, Window w) {
        Window pw = SwingUtilities.windowForComponent(c);

        if (pw == null)
            centerWindow(w);
        else
            centerWindow(pw, w);
    }

    //	/** Turn on a busy cursor.
    //	 *
    //	 * @param c The component whose cursor will be changed.
    //	 */
    //
    //	public static final void busyOn(Component c)
    //	{
    //		//  synchronized(KiwiUtils.class)
    //		{
    //			lastCursor = c.getCursor();
    //			c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    //		}
    //	}
    //
    //	/** Turn off the busy cursor. The last cursor saved will be restored.
    //	 *
    //	 * @param c The component whose cursor will be changed.
    //	 */
    //
    //	public static final void busyOff(Component c)
    //	{
    //		//  synchronized(KiwiUtils.class)
    //		{
    //			c.setCursor(lastCursor);
    //		}
    //	}

    /** Get the Frame parent of a component. This method searches upward in the
     * component hierarchy, searching for an ancestor that is a Frame.
     */

    public static final Frame getFrameForComponent(Component c) {
        while ((c = c.getParent()) != null)
            if (c instanceof Frame)
                return ((Frame) c);

        return (null);
    }

    /** Print a hardcopy of the contents of a window.
     *
     * @param window The window to print.
     * @param title A title for the print job.
     *
     * @return <b>true</b> if the print job was started, or <b>false</b> if
     * the user cancelled the print dialog.
     */

    public static boolean printWindow(Window window, String title) {
        PrintJob pj = Toolkit.getDefaultToolkit().getPrintJob(phantomFrame, title, null);

        if (pj == null)
            return (false);

        int res = Toolkit.getDefaultToolkit().getScreenResolution();
        // Dimension d = pj.getPageDimension(); // buggy in JDK 1.1.x
        Dimension d = new Dimension((int) (2 * res * 8.5), (int) (res * 11 * 2));

        d.width -= (int) (res * .75 /*in*/);
        d.height -= (int) (res * .75 /*in*/);

        Graphics gc = pj.getGraphics();
        window.paint(gc);

        gc.dispose();
        pj.end();
        return (true);
    }

    //	/** Copy text to the system clipboard.
    //	 *
    //	 * @param text The text to copy to the clipboard.
    //	 */
    //
    //	public static synchronized final void setClipboardText(String text)
    //	{
    //		StringSelection sel = new StringSelection(text);
    //		clipboard.setContents(sel, sel);
    //	}
    //
    //	/** Copy text from the system clipboard.
    //	 *
    //	 * @return The text that is in the clipboard, or <b>null</b> if the
    //	 * clipboard is empty or does not contain plain text.
    //	 */
    //
    //	public static synchronized final String getClipboardText()
    //	{
    //		try
    //		{
    //			return ((String) (clipboard.getContents(Void.class)
    //					.getTransferData(DataFlavor.stringFlavor)));
    //		}
    //		catch (UnsupportedFlavorException ex)
    //		{
    //			return (null);
    //		}catch (IOException ex)
    //		{
    //			return (null);
    //		}
    //	}

    /**
     * Shows the component in the Dialog with specified settings.
     * @param componentToShow
     * @param title
     * @param size
     * @param isReSizable
     * @return
     */
    public static JDialog showInDialog(JComponent componentToShow, String title, Dimension size,
                                       boolean isReSizable) {
        return showInDialog(null, componentToShow, title, size, false, isReSizable);
    }

    /**
     * Shows the component in the Dialog with specified settings.
     * @param parentFrame
     * @param componentToShow
     * @param title
     * @param size
     * @param isModal
     * @param isReSizable
     * @return
     */
    public static JDialog showInDialog(Frame parentFrame, JComponent componentToShow, String title,
                                       Dimension size, boolean isModal, boolean isReSizable) {
        JDialog dialog = new JDialog(parentFrame, title, isModal);
        dialog.getContentPane().add(componentToShow);
        if (componentToShow instanceof IDialogInterface) {
            ((IDialogInterface) componentToShow).setParentWindow(dialog);
        }
        if (size == null)
            dialog.setSize(new Dimension(300, 150));
        else
            dialog.setSize(size);
        if (parentFrame != null && componentToShow != null)
            centerWindow(parentFrame, dialog);
        dialog.setResizable(isReSizable);
        dialog.setVisible(true);
        return dialog;
    }

    /**
     * Sets the component in the Dialog with specified settings.
     * but won't make the dialog visible.
     * @param parentFrame
     * @param componentToShow
     * @param title
     * @param size
     * @param isModal
     * @param isReSizable
     * @return
     */
    public static JDialog setInDialog(Frame parentFrame, JComponent componentToShow, String title, Dimension size,
                                      boolean isModal, boolean isReSizable) {
        JDialog dialog = new JDialog(parentFrame, title, isModal);
        dialog.getContentPane().add(componentToShow);
        if (componentToShow instanceof IDialogInterface) {
            ((IDialogInterface) componentToShow).setParentWindow(dialog);
        }
        if (size == null)
            dialog.setSize(new Dimension(300, 150));
        else
            dialog.setSize(size);
        if (parentFrame != null && componentToShow != null)
            centerWindow(parentFrame, dialog);
        dialog.setResizable(isReSizable);
        //dialog.setVisible(true);
        return dialog;
    }

    /* -------------------------------- */

    public static void main(String[] args) {
        /* Code to test showInDialog, centerWindow utility function */
        JPanel panel = new JPanel();
        panel.add(new JButton("Button"));

        JFrame frame = new JFrame();
        frame.setSize(new Dimension(800, 600));
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        showInDialog(frame, panel, "Dialog", new Dimension(300, 150), true, false);
    }

}