package edu.wustl.cab2b.client.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import junit.framework.TestCase;
import edu.wustl.cab2b.client.ui.util.WindowUtilities;

/**
 * @author chandrakant_talele
 */
public class WindowUtilitiesTest extends TestCase {
	public void testSetNativeLookAndFeel() {
		WindowUtilities.setNativeLookAndFeel();
		String name = UIManager.getSystemLookAndFeelClassName();
		assertEquals(name, UIManager.getLookAndFeel().getClass().getName());
	}

	public void testSetJavaLookAndFeel() {
		WindowUtilities.setJavaLookAndFeel();
		String name = UIManager.getCrossPlatformLookAndFeelClassName();
		assertEquals(name, UIManager.getLookAndFeel().getClass().getName());
	}

	public void testSetMotifLookAndFeel() {
		WindowUtilities.setMotifLookAndFeel();
		String name = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
		assertEquals(name, UIManager.getLookAndFeel().getClass().getName());
	}
/*
	public void testOpenInJFrame() {
		Container content = new Container();
		JFrame frame = WindowUtilities.openInJFrame(content, 100, 100);
		assertEquals(Color.white, frame.getBackground());
		assertEquals(Color.white, content.getBackground());
	}

	public void testOpenInJFrameWithTitle() {
		String title = "title";
		Container content = new Container();
		int width = 100;
		int height = 100;
		JFrame frame = WindowUtilities.openInJFrame(content, width, height, title);
		assertEquals(Color.white, frame.getBackground());
		assertEquals(Color.white, content.getBackground());
		assertEquals(title, frame.getTitle());
	}

	public void testCenterWindow() {
		JFrame w = new JFrame("Test Frame");
		WindowUtilities.centerWindow(w);
		Dimension size = w.getToolkit().getScreenSize();
		assertEquals(size.width / 2, w.getLocation().x);
		assertEquals(size.height / 2, w.getLocation().y);
		w.dispose();
	}

	public void testCenterWindowPassingParent() {
		JFrame parent = new JFrame("Parent Frame");
		parent.setSize(640, 480);
		parent.setLocation(100, 80);
		parent.setVisible(true);
		JFrame child = new JFrame("Child Frame");
		child.setSize(200, 150);
		WindowUtilities.centerWindow(parent, child);

		assertEquals(320, child.getLocation().x);
		assertEquals(245, child.getLocation().y);
		parent.dispose();
		child.dispose();
	}

	public void testCenterWindowPassingParentChildOffScreen() {

		JFrame parent = new JFrame("Parent Frame");
		Dimension size = parent.getToolkit().getScreenSize();
		parent.setSize(100, 100);
		parent.setLocation(1, 1);
		parent.setVisible(true);

		JFrame child = new JFrame("Child Frame");
		Dimension d = new Dimension(1000, 800);
		child.setSize(d);
		WindowUtilities.centerWindow(parent, child);

		assertEquals((size.width - d.width) / 2, child.getLocation().x);
		assertEquals((size.height - d.height) / 2, child.getLocation().y);
		parent.dispose();
	}

	public void testCenterWindowParentOrphanComponent() {
		Component component = new JFrame("Parent Frame");
		JFrame window = new JFrame("Child Frame");
		WindowUtilities.centerWindow(component, window);
		Dimension size = component.getToolkit().getScreenSize();
		assertEquals(size.width / 2, window.getLocation().x);
		assertEquals(size.height / 2, window.getLocation().y);
		window.dispose();
	}

	public void testCenterWindowParentComponentWithParent() {
		JFrame componentParent = new JFrame("Parent Frame");

		JPanel componentChild = new JPanel();
		componentParent.add(componentChild);
		componentParent.setVisible(true);
		JFrame window = new JFrame("Child Frame");
		WindowUtilities.centerWindow(componentChild, window);

		Dimension size = componentParent.getSize();
		Point location = componentParent.getLocation();

		assertEquals(size.width / 2 + location.x, window.getLocation().x);
		assertEquals(size.height / 2 + location.y, window.getLocation().y);
		componentParent.dispose();
	}*/
}
