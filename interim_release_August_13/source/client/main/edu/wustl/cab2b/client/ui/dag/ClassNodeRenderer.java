package edu.wustl.cab2b.client.ui.dag;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

import org.netbeans.graph.api.control.GraphHelper;
import org.netbeans.graph.api.control.IGraphNodeRenderer;
import org.netbeans.graph.api.control.editor.IGraphEditor;
import org.netbeans.graph.api.control.editor.TextFieldEditor;
import org.netbeans.graph.api.model.IGraphNode;
import org.netbeans.graph.api.model.IGraphPort;
import org.netbeans.graph.api.model.ability.IDisplayable;
import org.netbeans.graph.api.model.ability.INameEditable;
import org.openide.util.NbBundle;

import edu.wustl.common.querysuite.queryobject.IExpressionId;

public class ClassNodeRenderer implements IGraphNodeRenderer {
	public static final Font font = Font.decode("Times-bold").deriveFont(10.0f); // NOI18N

	private static Color colorFont = new Color(0, 0, 0); // 174, 100

	protected static int PORT_SPACE = 20; // sanjeev

	protected static int PORT_SPACE_2 = 8;

	private static final int LAYER_BACKGROUND = 100;

	private static final int LAYER_NODE = 400;

	private int[] layers = new int[] { LAYER_BACKGROUND, LAYER_NODE };

	private GraphHelper helper;

	protected Rectangle m_textRect;

	private Rectangle m_option;

	private Rectangle m_maxMinRectangle;

	private Rectangle m_numRectangle;

	private Rectangle[] m_assRectangles;

	private String toolTipName;

	// Set max-min width and height
	private final int m_maxMinLength = 15;

	private boolean m_nodeMinimized = false;

	private Polygon m_optionPolygon;

	// SET index of association clicked
	private int m_associationIndx = -1;

	HashMap m_AssToOperatermap = new HashMap();

	private boolean isForView = false;

	public ClassNodeRenderer(GraphHelper helper, IGraphNode node, boolean isForView) {
		this.helper = helper;
		this.isForView = isForView;
	}

	public IGraphEditor getEditor(IGraphNode node, Point position) {
		if (m_textRect == null) {
			return null;
		}
		final Rectangle rectangle = new Rectangle(m_textRect);
		final Point location = helper.getNodeLocation(node);
		rectangle.translate(location.x, location.y);
		final INameEditable nameEditable = (INameEditable) node.getLookup().lookup(
				INameEditable.class);
		if (rectangle.contains(position) && nameEditable != null && nameEditable.canRename()) {
			return new TextFieldEditor() {
				public String getValue() {
					final String name = nameEditable.getName();
					return name != null ? name : NbBundle.getMessage(ClassNodeRenderer.class,
							"TXT_IconNodeDriver_EnterName"); // NOI18N
				}

				public void setValue(String value) {
					nameEditable.setName(value);
				}

				public Rectangle getActiveArea() {
					return rectangle;
				}

				public void notifyAttached(IGraphEditor.EditorPresenter presenter) {
					super.notifyAttached(presenter);
					final int height = getComponent().getPreferredSize().height;
					getComponent().setMinimumSize(new Dimension(64, height));
					getComponent().setMaximumSize(new Dimension(256, height));
					getComponent().setPreferredSize(new Dimension(128, height));
				}
			};
		}
		return null;
	}

	public int[] getLayers(IGraphNode arg0) {
		return layers;
	}

	public String getToolTipText(IGraphNode node, Point arg1) {
		final IDisplayable displayable = (IDisplayable) node.getLookup().lookup(IDisplayable.class);
		return displayable != null ? displayable.getTooltipText() : null;
	}

	/**
	 * Method to layout minimized node
	 * 
	 * @param node
	 *            Node for which to perform layout
	 * @param gr
	 *            Graphics2D object associated with this node renderer
	 */
	private void layoutMinimizedNode(IGraphNode node, Graphics2D gr) {
		FontMetrics fontMetrics = gr.getFontMetrics(font);

		// Node number rectangle setup
		m_numRectangle = fontMetrics.getStringBounds(node.getID(), gr).getBounds();
		m_numRectangle.width += 10;
		m_numRectangle.height += 5;

		// Class name area setup
		String displayName = ((IDisplayable) node.getLookup().lookup(IDisplayable.class))
				.getDisplayName();
		if (displayName.indexOf("(") != -1)
			displayName = displayName.substring(0, displayName.indexOf("("));
		m_textRect = fontMetrics.getStringBounds(displayName, gr).getBounds();
		if (displayName.length() <= 5)
			m_textRect.width += 20;
		else
			m_textRect.width += 10;
		m_textRect.height += 10;
		m_textRect.x = m_numRectangle.x + m_numRectangle.width;
		m_textRect.y = m_numRectangle.y;

		// MaxMin rectangle details
		m_maxMinRectangle = new Rectangle();
		m_maxMinRectangle.width = m_maxMinRectangle.height = m_maxMinLength;
		m_maxMinRectangle.x = m_textRect.x + m_textRect.width;
		m_maxMinRectangle.y = m_textRect.y + 5;

		Rectangle bounds = new Rectangle();
		Rectangle2D.union(m_numRectangle, m_textRect, bounds);
		Rectangle2D.union(m_maxMinRectangle, bounds, bounds);

		ClassNode classNode = (ClassNode) (node.getLookup().lookup(ClassNode.class));
		java.util.List<IGraphPort> associationList = classNode.getSourcePorts();
		for (int i = 0; i < associationList.size(); i++) {
			IGraphPort port = associationList.get(i);
			helper.setPortRelativeLocation(port, new Point(m_maxMinRectangle.x
					+ m_maxMinRectangle.width + 5, m_maxMinRectangle.y + 5));
		}
		java.util.List<IGraphPort> targetPortList = classNode.getTargetPortList();
		for (int i = 0; i < targetPortList.size(); i++) {
			helper.setPortRelativeLocation(targetPortList.get(i), new Point(m_numRectangle.x - 5,
					m_numRectangle.y + 5));
		}
		helper.includeNodePortsToNodeRelativeBounds(node, bounds);
		bounds.grow(4, 4);
		helper.setNodeRelativeActiveAreas(node, new Rectangle[] { bounds, m_textRect,
				m_maxMinRectangle });
		helper.setNodeRelativeBounds(node, bounds);
	}

	/**
	 * Method to layout maximized node
	 * 
	 * @param node
	 *            Node for which to perform layout
	 * @param gr
	 *            Graphics2D object associated with this node renderer
	 */
	private void layoutMaximizedNode(IGraphNode node, Graphics2D gr) {
		final IDisplayable displayable = (IDisplayable) node.getLookup().lookup(IDisplayable.class);
		String displayName = displayable.getDisplayName();
		setNameForToolTip(displayName);
		if (displayName.indexOf("(") != -1)
			displayName = displayName.substring(0, displayName.indexOf("("));

		FontMetrics fontMetrics = gr.getFontMetrics(font);

		// Node number rectangle setup
		m_numRectangle = fontMetrics.getStringBounds(node.getID(), gr).getBounds();
		m_numRectangle.width += 10;
		m_numRectangle.height += 5;

		// set Target ports
		ClassNode classNode = (ClassNode) (node.getLookup().lookup(ClassNode.class));
		java.util.List<IGraphPort> targetPortList = classNode.getTargetPortList();
		for (int i = 0; i < targetPortList.size(); i++) {
			helper.setPortRelativeLocation(targetPortList.get(i), new Point(m_numRectangle.x - 5,
					m_numRectangle.y + 5));
		}

		// Class name area setup
		m_textRect = fontMetrics.getStringBounds(displayName, gr).getBounds();
		if (displayName.length() <= 5)
			m_textRect.width += 20;
		else
			m_textRect.width += 10;
		m_textRect.height += 10;
		m_textRect.x = m_numRectangle.x + m_numRectangle.width;
		m_textRect.y = m_numRectangle.y;

		// MaxMin rectangle details
		m_maxMinRectangle = new Rectangle();
		m_maxMinRectangle.width = m_maxMinRectangle.height = m_maxMinLength;
		m_maxMinRectangle.x = m_textRect.x + m_textRect.width;
		m_maxMinRectangle.y = m_textRect.y + 5;

		// Option area setup
		m_option = fontMetrics.getStringBounds("Options", gr).getBounds();
		m_option.width += 10;
		m_option.height += 10;
		m_option.translate(m_textRect.width / 2, m_textRect.height);

		// If node has associations to display
		// Render association part of the node
		m_assRectangles = null;
		layoutAssociations(node, gr);

		Rectangle bounds = new Rectangle();
		Rectangle2D.union(m_textRect, m_option, bounds);
		Rectangle2D.union(bounds, m_maxMinRectangle, bounds);
		Rectangle2D.union(bounds, m_numRectangle, bounds);
		Rectangle[] allActiveRectangles;
		if (m_assRectangles != null) {
			allActiveRectangles = new Rectangle[m_assRectangles.length + 3];
			for (int i = 0; i < m_assRectangles.length; i++) {
				Rectangle2D.union(bounds, m_assRectangles[i], bounds);
			}
		} else {
			allActiveRectangles = new Rectangle[3];
		}
		// Add common rectangles
		allActiveRectangles[0] = bounds;
		allActiveRectangles[1] = m_option;
		allActiveRectangles[2] = m_maxMinRectangle;

		if (m_assRectangles != null) {
			for (int i = 0; i < m_assRectangles.length; i++) {
				allActiveRectangles[i + 3] = m_assRectangles[i];
			}
		}
		helper.includeNodePortsToNodeRelativeBounds(node, bounds);
		bounds.grow(4, 4);
		helper.setNodeRelativeActiveAreas(node, allActiveRectangles);
		helper.setNodeRelativeBounds(node, bounds);
	}

	/**
	 * Method to layout associations of nodes
	 * 
	 * @param associationList
	 *            List of associations to layout
	 * @param gr
	 *            Graphics2D object associated with this node renderer
	 */
	private void layoutAssociations(IGraphNode node, Graphics2D gr) {
		ClassNode classNode = (ClassNode) (node.getLookup().lookup(ClassNode.class));
		java.util.List<IGraphPort> associationList = classNode.getSourcePorts();
		if (associationList.size() == 0) {
			return;
		}
		m_assRectangles = new Rectangle[associationList.size() + 1];
		// Common condtion portion where attribute conditions will be logically
		// ANDED / ORED will association
		// conditions
		m_assRectangles[0] = new Rectangle();
		m_assRectangles[0].width = m_textRect.width;
		m_assRectangles[0].height = m_textRect.height;
		m_assRectangles[0].translate(m_textRect.x, m_option.y + m_option.height);

		for (int i = 1; i <= associationList.size(); i++) {
			m_assRectangles[i] = new Rectangle();
			m_assRectangles[i].width = m_textRect.width;
			m_assRectangles[i].height = m_textRect.height;
			m_assRectangles[i].translate(m_textRect.x, m_assRectangles[i - 1].height
					+ m_assRectangles[i - 1].y);
			IGraphPort port = associationList.get(i - 1);
			helper.setPortRelativeLocation(port, new Point(m_assRectangles[i].x
					+ m_assRectangles[i].width + 5, m_assRectangles[i].y + 5));
		}
	}

	/**
	 * Method to layout maximized node
	 * 
	 * @param node
	 *            Node for which to perform layout
	 * @param gr
	 *            Graphics2D object associated with this node renderer
	 */
	private void layoutDefineViewNode(IGraphNode node, Graphics2D gr) {
		FontMetrics fontMetrics = gr.getFontMetrics(font);

		// Node number rectangle setup
		m_numRectangle = fontMetrics.getStringBounds(node.getID(), gr).getBounds();
		m_numRectangle.width += 10;
		m_numRectangle.height += 5;

		// Class name area setup
		String displayName = ((IDisplayable) node.getLookup().lookup(IDisplayable.class))
				.getDisplayName();
		if (displayName.indexOf("(") != -1)
			displayName = displayName.substring(0, displayName.indexOf("("));
		m_textRect = fontMetrics.getStringBounds(displayName, gr).getBounds();
		m_textRect.width += 10;
		m_textRect.height += 10;
		m_textRect.x = m_numRectangle.x + m_numRectangle.width;
		m_textRect.y = m_numRectangle.y;

		// MaxMin rectangle details
		m_maxMinRectangle = new Rectangle();
		m_maxMinRectangle.width = m_maxMinRectangle.height = m_maxMinLength;
		m_maxMinRectangle.x = m_textRect.x + m_textRect.width;
		m_maxMinRectangle.y = m_textRect.y + 5;

		// Option area setup
		m_option = fontMetrics.getStringBounds("Options", gr).getBounds();
		m_option.width += 10;
		m_option.height += 10;
		m_option.translate(m_textRect.width / 2, m_textRect.height);

		Rectangle bounds = new Rectangle();
		Rectangle2D.union(m_numRectangle, m_textRect, bounds);
		Rectangle2D.union(m_maxMinRectangle, bounds, bounds);
		Rectangle2D.union(m_option, bounds, bounds);

		ClassNode classNode = (ClassNode) (node.getLookup().lookup(ClassNode.class));
		java.util.List<IGraphPort> associationList = classNode.getSourcePorts();
		for (int i = 0; i < associationList.size(); i++) {
			IGraphPort port = associationList.get(i);
			helper.setPortRelativeLocation(port, new Point(m_maxMinRectangle.x
					+ m_maxMinRectangle.width + 5, m_maxMinRectangle.y + 5));
		}

		java.util.List<IGraphPort> targetPortList = classNode.getTargetPortList();
		for (int i = 0; i < targetPortList.size(); i++) {
			helper.setPortRelativeLocation(targetPortList.get(i), new Point(m_numRectangle.x - 5,
					m_numRectangle.y + 5));
		}
		helper.includeNodePortsToNodeRelativeBounds(node, bounds);
		bounds.grow(4, 4);
		helper.setNodeRelativeActiveAreas(node, new Rectangle[] { bounds, m_textRect,
				m_maxMinRectangle });
		helper.setNodeRelativeBounds(node, bounds);
	}

	/**
	 * Called to layout a node. This means to resolve the relative active areas
	 * and the relative bounds of the node. Optionally the relative location of
	 * the node ports also. Use GraphHelper.setNodeRelativeBounds,
	 * GraphHelper.setNodeRelativeActiveAreas, and optionally
	 * GraphHelper.setPortRelativeLocation methods. <p/> Note: The node relative
	 * bounds must include the node ports bounds. This could be done by calling
	 * GraphHelper.includePortsToNodeRelativeBounds method. <p/> Note: The node
	 * do not have resolved location, active areas, and bounds yet.
	 * 
	 * @param node
	 *            the proxy-node
	 * @param gr
	 *            the graphics
	 */
	public void layoutNode(IGraphNode node, Graphics2D gr) {
		if (true == m_nodeMinimized) {
			layoutMinimizedNode(node, gr);
		} else {
			if (isForView) {
				layoutDefineViewNode(node, gr);
			} else {
				this.layoutMaximizedNode(node, gr);
			}
		}
	}

	public Point locationSuggested(IGraphNode arg0, Point arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	public void portLocationSuggested(IGraphPort arg0, Point arg1) {
		// TODO Auto-generated method stub
	}

	/**
	 * Method to render minimized node
	 * 
	 * @param node
	 *            GraphNode to render
	 * @param gr
	 *            Graphics2D object associated with this node renderer
	 * @param layer
	 *            Layer of the node to render
	 */
	private void renderMinimizedNode(IGraphNode node, Graphics2D gr, int layer) {
		Point location = helper.getNodeLocation(node);
		final boolean selected = helper.isComponentSelected(node);

		// Get the node bounds
		Rectangle rect = helper.getBounds(node);

		if (layer == LAYER_BACKGROUND) {
			ClassNode classNode = (ClassNode) node.getLookup().lookup(ClassNode.class);
			// 1. Fill class name rectangle
			if (classNode.getType().equals(ClassNodeType.ConstraintOnlyNode)) {
				gr.setColor(new Color(0xFFFFAA));
			} else if (classNode.getType().equals(ClassNodeType.ViewOnlyNode)) {
				gr.setColor(Color.pink);
			} else {
				gr.setColor(Color.orange);
			}
			gr.fillRect(rect.x, rect.y, rect.width, rect.height);
			Rectangle2D rect2D;
			if (true == selected) {
				gr.setColor(new Color(0x0000FF));
				for (int i = 0; i < 2; i++) {
					rect2D = new Rectangle2D.Float(rect.x + i, rect.y + i, rect.width - i,
							rect.height - i);
					gr.draw(rect2D);
					rect2D = new Rectangle2D.Float(rect.x + 0.5f, rect.y + 0.5f, rect.width - i,
							rect.height - i);
					gr.draw(rect2D);
				}
			} else {
				gr.setColor(new Color(0x000000));
				rect2D = new Rectangle2D.Float(rect.x + 0.5f, rect.y + 0.5f, rect.width - 1,
						rect.height - 1);
				gr.draw(rect2D);
			}
		} else if (layer == LAYER_NODE) {
			String displayName = null;
			final IDisplayable displayable = (IDisplayable) node.getLookup().lookup(
					IDisplayable.class);
			displayName = displayable.getDisplayName();
			if (displayName.indexOf("(") != -1)
				displayName = displayName.substring(0, displayName.indexOf("("));
			gr.setFont(font);

			final FontMetrics fontMetrics = gr.getFontMetrics(font);
			final int ascent = fontMetrics.getAscent();

			// show node index rectangle
			ClassNode classNode = (ClassNode) node.getLookup().lookup(ClassNode.class);
			IGraphPort[] ports = classNode.getPorts();
			if ((ports != null) && (ports.length > 0)) {
				gr.setColor(new Color(0x85E30C));
			} else {
				gr.setColor(new Color(0xFF6666));
			}
			final Rectangle numRect = new Rectangle(this.m_numRectangle);
			numRect.translate(location.x, location.y);
			gr.fillRect(numRect.x, numRect.y, numRect.width, numRect.height - 3);
			gr.setColor(Color.BLACK);
			gr.drawRect(numRect.x, numRect.y, numRect.width, numRect.height - 3);
			gr.setColor(colorFont);
			gr.drawString(node.getID(), numRect.x + 5, numRect.y + 1 + ascent);

			// show class name rectangle
			int currX = numRect.x + numRect.width;
			int currY = numRect.y;
			final Rectangle textRect = new Rectangle(this.m_textRect);
			gr.setColor(colorFont);
			gr.drawString(displayName, currX + 3, currY + 3 + ascent);

			// Draw max-min window rectangle and inner portion of same
			gr.setColor(new Color(0x808080));
			gr.fillRect(currX + textRect.width, currY + 3, m_maxMinLength, m_maxMinLength);

			// draw border for maxMin Window
			gr.setColor(new Color(0x000000)); // border color..
			gr.draw(new Rectangle2D.Float(currX + textRect.width, currY + 3, m_maxMinLength,
					m_maxMinLength));
			gr.setColor(new Color(0xFFFFFF));
			gr.fillRect(currX + textRect.width + m_maxMinLength / 4, currY + 3 + 3 * m_maxMinLength
					/ 4, 3 * m_maxMinLength / 4, m_maxMinLength / 4);

		}
	}

	/**
	 * Method to render miximized node
	 * 
	 * @param node
	 *            GraphNode to render
	 * @param gr
	 *            Graphics2D object associated with this node renderer
	 * @param layer
	 *            Layer of the node to render
	 */
	private void renderMaximizedNode(IGraphNode node, Graphics2D gr, int layer) {
		Point location = helper.getNodeLocation(node);
		final boolean selected = helper.isComponentSelected(node);

		// If node has associations to display
		ClassNode classNode = (ClassNode) node.getLookup().lookup(ClassNode.class);
		java.util.List<IGraphPort> associationList = classNode.getSourcePorts();

		// Get the node bounds
		Rectangle rect = helper.getBounds(node);

		if (layer == LAYER_BACKGROUND) {
			// Fill the complete rectangle
			if (classNode.getType().equals(ClassNodeType.ConstraintOnlyNode)) {
				gr.setColor(new Color(0xFFFFAA));
			} else if (classNode.getType().equals(ClassNodeType.ViewOnlyNode)) {
				gr.setColor(Color.pink);
			} else {
				gr.setColor(Color.orange);
			}

			gr.fillRect(rect.x, rect.y, rect.width, rect.height);

			// 1. Draw border for class name rectangle
			gr.setColor(new Color(0x000000));
			Rectangle2D rect2D = new Rectangle2D.Float(rect.x, rect.y, rect.width - 1,
					m_textRect.height);
			gr.draw(rect2D);

			// 2. Fill Option rectangle and draw border for same
			gr.setColor(new Color(0xDBDBDB));
			gr.fillRect(rect.x, rect.y + m_textRect.height, rect.width, m_option.height);
			gr.setColor(new Color(0x000000));
			rect2D = new Rectangle2D.Float(rect.x, rect.y + m_textRect.height, rect.width - 1,
					m_option.height);
			gr.draw(rect2D);

			// If node has associations show them
			if (associationList.size() > 0) {
				gr.setColor(new Color(0xF5F5F5));
				gr.fillRect(rect.x, rect.y + m_textRect.height + m_option.height, rect.width,
						m_assRectangles[0].height);
				gr.setColor(new Color(0x000000));
				rect2D = new Rectangle2D.Float(rect.x,
						rect.y + m_textRect.height + m_option.height, rect.width - 1,
						m_assRectangles[0].height);
				gr.draw(rect2D);
				rect2D = new Rectangle2D.Float(rect.x + 1, rect.y + m_textRect.height
						+ m_option.height + 1, rect.width - 2, m_assRectangles[0].height - 2);
				gr.draw(rect2D);
				int currY = rect.y + m_textRect.height + m_option.height;
				for (int i = 1; i < m_assRectangles.length; i++) {
					gr.setColor(new Color(0xF5F5F5));
					gr.fillRect(rect.x, currY + (i * m_assRectangles[i - 1].height), rect.width,
							m_assRectangles[i].height);
					gr.setColor(new Color(0x000000));
					rect2D = new Rectangle2D.Float(rect.x, currY
							+ (i * m_assRectangles[i - 1].height), rect.width - 1,
							m_assRectangles[i].height);
					gr.draw(rect2D);
				}
			}

			if (true == selected) {
				gr.setColor(new Color(0x0000FF));
				for (int i = 0; i < 2; i++) {
					rect2D = new Rectangle2D.Float(rect.x + i, rect.y + i, rect.width - i,
							rect.height - i);
					gr.draw(rect2D);
					rect2D = new Rectangle2D.Float(rect.x + 0.5f, rect.y + 0.5f, rect.width - i,
							rect.height - i);
					gr.draw(rect2D);
				}
			} else {
				gr.setColor(new Color(0x000000));
				rect2D = new Rectangle2D.Float(rect.x + 0.5f, rect.y + 0.5f, rect.width - 1,
						rect.height - 1);
				gr.draw(rect2D);
			}

		} else if (layer == LAYER_NODE) {
			String displayName = null;
			final IDisplayable displayable = (IDisplayable) node.getLookup().lookup(
					IDisplayable.class);
			displayName = displayable.getDisplayName();
			if (displayName.indexOf("(") != -1)
				displayName = displayName.substring(0, displayName.indexOf("("));
			gr.setFont(font);

			final FontMetrics fontMetrics = gr.getFontMetrics(font);
			final int ascent = fontMetrics.getAscent();

			// show node index rectangle
			IGraphPort[] ports = classNode.getPorts();
			if ((ports != null) && (ports.length > 0)) {
				gr.setColor(new Color(0x85E30C));
			} else {
				gr.setColor(new Color(0xFF6666));
			}
			final Rectangle numRect = new Rectangle(m_numRectangle);
			numRect.translate(location.x, location.y);
			gr.fillRect(numRect.x, numRect.y, numRect.width, numRect.height - 3);
			gr.setColor(Color.BLACK);
			gr.drawRect(numRect.x, numRect.y, numRect.width, numRect.height - 3);
			gr.setColor(colorFont);
			gr.drawString(node.getID(), numRect.x + 5, numRect.y + 1 + ascent);

			// show class name rectangle
			int currX = numRect.x + numRect.width;
			int currY = numRect.y;
			final Rectangle textRect = new Rectangle(this.m_textRect);
			gr.setColor(colorFont);
			gr.drawString(displayName, currX + 3, currY + 3 + ascent);

			// Draw max-min window rectangle and inner portion of same
			gr.setColor(new Color(0x808080));
			gr.fillRect(currX + textRect.width, currY + 3, m_maxMinLength, m_maxMinLength);

			// draw border for maxMin Window
			gr.setColor(new Color(0x000000)); // border color..
			gr.draw(new Rectangle2D.Float(currX + textRect.width, currY + 3, m_maxMinLength,
					m_maxMinLength));
			gr.setColor(new Color(0xFFFFFF));
			gr.fillRect(currX + textRect.width + m_maxMinLength / 4, currY + 3 + 3 * m_maxMinLength
					/ 4, 3 * m_maxMinLength / 4, m_maxMinLength / 4);

			// show option rectangle
			final Rectangle optionRect = new Rectangle(m_option);
			optionRect.translate(location.x, location.y);
			gr.setColor(colorFont);
			gr.drawString("Options", optionRect.x, optionRect.y + ascent);

			// show tringle in front of option string
			m_optionPolygon = new Polygon();
			m_optionPolygon.addPoint(0, 0);
			m_optionPolygon.addPoint(0, 14);
			m_optionPolygon.addPoint(7, 7);
			gr.setColor(Color.black);
			m_optionPolygon.translate(rect.x + rect.width - 20, optionRect.y + 3);
			gr.fillPolygon(m_optionPolygon);

			// Render association related information
			if (associationList.size() > 0) {

				currY = location.y + m_textRect.height + optionRect.height;
				drawAssociation(classNode, gr, location.x, currY, associationList);
			}
		}
	}

	/**
	 * Method to render miximized node
	 * 
	 * @param node
	 *            GraphNode to render
	 * @param gr
	 *            Graphics2D object associated with this node renderer
	 * @param layer
	 *            Layer of the node to render
	 */
	private void renderDefineViewNode(IGraphNode node, Graphics2D gr, int layer) {
		Point location = helper.getNodeLocation(node);
		final boolean selected = helper.isComponentSelected(node);

		// Get the node bounds
		Rectangle rect = helper.getBounds(node);

		if (layer == LAYER_BACKGROUND) {
			ClassNode classNode = (ClassNode) node.getLookup().lookup(ClassNode.class);
			// 1. Fill class name rectangle
			if (classNode.getType().equals(ClassNodeType.ConstraintOnlyNode)) {
				gr.setColor(new Color(0xFFFFAA));
			} else if (classNode.getType().equals(ClassNodeType.ViewOnlyNode)) {
				gr.setColor(Color.pink);
			} else {
				gr.setColor(Color.orange);
			}
			gr.fillRect(rect.x, rect.y, rect.width, rect.height);
			// 1. Draw border for class name rectangle
			gr.setColor(new Color(0x000000));
			Rectangle2D rect2D = new Rectangle2D.Float(rect.x, rect.y, rect.width - 1,
					m_textRect.height);
			gr.draw(rect2D);

			// 2. Fill Option rectangle and draw border for same
			gr.setColor(new Color(0xDBDBDB));
			gr.fillRect(rect.x, rect.y + m_textRect.height, rect.width, m_option.height);
			gr.setColor(new Color(0x000000));
			rect2D = new Rectangle2D.Float(rect.x, rect.y + m_textRect.height, rect.width - 1,
					m_option.height);
			gr.draw(rect2D);

			if (true == selected) {
				gr.setColor(new Color(0x0000FF));
				for (int i = 0; i < 2; i++) {
					rect2D = new Rectangle2D.Float(rect.x + i, rect.y + i, rect.width - i,
							rect.height - i);
					gr.draw(rect2D);
					rect2D = new Rectangle2D.Float(rect.x + 0.5f, rect.y + 0.5f, rect.width - i,
							rect.height - i);
					gr.draw(rect2D);
				}
			} else {
				gr.setColor(new Color(0x000000));
				rect2D = new Rectangle2D.Float(rect.x + 0.5f, rect.y + 0.5f, rect.width - 1,
						rect.height - 1);
				gr.draw(rect2D);
			}
		} else if (layer == LAYER_NODE) {
			String displayName = null;
			final IDisplayable displayable = (IDisplayable) node.getLookup().lookup(
					IDisplayable.class);
			displayName = displayable.getDisplayName();
			gr.setFont(font);

			final FontMetrics fontMetrics = gr.getFontMetrics(font);
			final int ascent = fontMetrics.getAscent();

			// show node index rectangle
			ClassNode classNode = (ClassNode) node.getLookup().lookup(ClassNode.class);
			IGraphPort[] ports = classNode.getPorts();
			if ((ports != null) && (ports.length > 0)) {
				gr.setColor(new Color(0x85E30C));
			} else {
				gr.setColor(new Color(0xFF6666));
			}
			final Rectangle numRect = new Rectangle(this.m_numRectangle);
			numRect.translate(location.x, location.y);
			gr.fillRect(numRect.x, numRect.y, numRect.width, numRect.height - 3);
			gr.setColor(Color.BLACK);
			gr.drawRect(numRect.x, numRect.y, numRect.width, numRect.height - 3);
			gr.setColor(colorFont);
			gr.drawString(node.getID(), numRect.x + 5, numRect.y + 1 + ascent);

			// show class name rectangle
			int currX = numRect.x + numRect.width;
			int currY = numRect.y;
			final Rectangle textRect = new Rectangle(this.m_textRect);
			gr.setColor(colorFont);
			gr.drawString(displayName, currX + 3, currY + 3 + ascent);

			// Draw max-min window rectangle and inner portion of same
			gr.setColor(new Color(0x808080));
			gr.fillRect(currX + textRect.width, currY + 3, m_maxMinLength, m_maxMinLength);

			// draw border for maxMin Window
			gr.setColor(new Color(0x000000)); // border color..
			gr.draw(new Rectangle2D.Float(currX + textRect.width, currY + 3, m_maxMinLength,
					m_maxMinLength));
			gr.setColor(new Color(0xFFFFFF));
			gr.fillRect(currX + textRect.width + m_maxMinLength / 4, currY + 3 + 3 * m_maxMinLength
					/ 4, 3 * m_maxMinLength / 4, m_maxMinLength / 4);

			// show option rectangle
			final Rectangle optionRect = new Rectangle(m_option);
			optionRect.translate(location.x, location.y);
			gr.setColor(colorFont);
			gr.drawString("Options", optionRect.x, optionRect.y + ascent);

			// show tringle in front of option string
			m_optionPolygon = new Polygon();
			m_optionPolygon.addPoint(0, 0);
			m_optionPolygon.addPoint(0, 14);
			m_optionPolygon.addPoint(7, 7);
			gr.setColor(Color.black);
			m_optionPolygon.translate(rect.x + rect.width - 20, optionRect.y + 3);
			gr.fillPolygon(m_optionPolygon);

		}
	}

	/**
	 * Method to draw association boxes in node
	 * 
	 * @param gr
	 *            Graphics2D object associated with this node renderer
	 * @param currX
	 *            Current X location where association box has to render
	 * @param currY
	 *            Current y location where association box has to render
	 * @param associationList
	 *            List of associations to draw on node
	 */
	private void drawAssociation(ClassNode classNode, Graphics2D gr, int currX, int currY,
			java.util.List<IGraphPort> associationList) {
		final FontMetrics fontMetrics = gr.getFontMetrics(font);
		final int ascent = fontMetrics.getAscent();

		// Show And / OR string
		drawCustomCombo(classNode.getOperatorBetAttrAndAss(), currX, currY, gr);
		for (int i = 1; i < m_assRectangles.length; i++) {
			currY += m_assRectangles[i - 1].height;

			IExpressionId expId = classNode.getLinkForSourcePort(associationList.get(i - 1))
					.getDestinationExpressionId();
			Rectangle assRect = fontMetrics.getStringBounds(String.valueOf(expId.getInt()), gr)
					.getBounds();
			assRect.width += 10;
			assRect.height += 4;
			assRect.translate(currX, currY);
			gr.setColor(Color.WHITE);
			gr.fillRect(assRect.x, assRect.y, assRect.width, assRect.height);
			gr.setColor(Color.BLACK);
			gr.drawRect(assRect.x, assRect.y, assRect.width, assRect.height);
			gr.setColor(colorFont);
			gr.drawString(String.valueOf(expId.getInt()), assRect.x + 5, assRect.y + 3 + ascent);
			if (i < m_assRectangles.length - 1) {
				drawCustomCombo(classNode.getLogicalOperator(associationList.get(i)), currX, currY,
						gr);
			}
		}
	}

	/**
	 * Method to draw custom drop-down boxes for added associations
	 * 
	 * @param str
	 *            String to draw in drop downn
	 * @param x
	 *            X location for drop-down
	 * @param y
	 *            Y location for drop-down
	 * @param gr
	 *            Graphics2D object associated with this node renderer
	 */
	private void drawCustomCombo(String str, int x, int y, Graphics2D gr) {
		int drawWidth;
		final FontMetrics fontMetrics = gr.getFontMetrics(font);
		Rectangle assRect = fontMetrics.getStringBounds(str, gr).getBounds();
		assRect.width += 10;
		assRect.height += 4;

		Rectangle comboRect = new Rectangle();
		comboRect.width = 10;
		comboRect.height = assRect.height;

		Polygon polygon = new Polygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(10, 0);
		polygon.addPoint(5, 10);

		// Calculation for making comboboxes right aligned
		drawWidth = assRect.width + comboRect.width;
		int traslateX = m_textRect.width - drawWidth;
		assRect.translate(x + traslateX, y);
		gr.setColor(Color.WHITE);
		gr.fill(assRect);
		gr.setColor(Color.BLACK);
		gr.draw(assRect);
		gr.drawString(str, assRect.x + 5, y + 3);

		comboRect.translate(assRect.x + assRect.width, assRect.y);
		gr.setColor(Color.LIGHT_GRAY);
		gr.fill(comboRect);
		gr.setColor(Color.BLACK);
		gr.draw(comboRect);

		gr.setColor(Color.WHITE);
		polygon.translate(assRect.x + assRect.width, assRect.y + 5);
		gr.fillPolygon(polygon);
	}

	/**
	 * 
	 * Called to render a layer of a node. This method is always called after
	 * layoutNode method.
	 * 
	 */
	public void renderNode(IGraphNode node, Graphics2D gr, int layer) {
		if (true == m_nodeMinimized) {
			renderMinimizedNode(node, gr, layer);
		} else {
			if (isForView) {
				renderDefineViewNode(node, gr, layer);
			} else {
				renderMaximizedNode(node, gr, layer);
			}
		}
	}

	/**
	 * Method to check if option show button is clicked
	 * 
	 * @param node
	 *            The node for which to check the click
	 * @param point
	 *            the mouse click location
	 * @return true if option button is clicked else return's false
	 */
	public boolean isOptionPopupShow(IGraphNode node, Point point) {
		if (false == m_nodeMinimized) {
			// Check if user has clicked options button
			if (true == m_optionPolygon.contains(point)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to check if maxmin rectangle has been clicked. This checks
	 * wheather to minimize or maximize the node in selection
	 * 
	 * @param node
	 *            Node for which to check max-min rectangle
	 * @param point
	 *            Point location of mouse click
	 * @return
	 */
	public boolean isMaxMinClick(IGraphNode node, Point point) {
		Point nodePosition = helper.getNodeLocation(node);
		// Check if minmax is clicked
		Point tempPoint = new Point();
		tempPoint.x = point.x - nodePosition.x;
		tempPoint.y = point.y - nodePosition.y;
		if (true == m_maxMinRectangle.contains(tempPoint)) {
			m_nodeMinimized = !m_nodeMinimized;
			return true;
		}
		return false;
	}

	/**
	 * Method to check if association dropdown is being checked
	 * 
	 * @param node
	 *            Node for which to check if association dropdown is checked
	 * @param point
	 *            where mouse has been clicked
	 * @return true if association rectangle has been clicked else return false
	 */
	public boolean isAssociationPopupShow(IGraphNode node, Point point) {
		Point nodePosition = helper.getNodeLocation(node);
		Point tempPoint = new Point();
		tempPoint.x = point.x - nodePosition.x;
		tempPoint.y = point.y - nodePosition.y;
		if (false == m_nodeMinimized) {
			// Check if use has clicked options button
			if (m_assRectangles != null) {
				for (int i = 0; i < m_assRectangles.length - 1; i++) {
					if (true == m_assRectangles[i].contains(tempPoint)) {
						m_associationIndx = i;
						return true;
					}
				}
			}
		}
		m_associationIndx = -1;
		return false;
	}

	/**
	 * Get selected association index
	 * 
	 * @return the index of selected association
	 */
	public int getSelectedAssocitationIdx() {
		return m_associationIndx;
	}

	public String getNameForToolTip() {

		return toolTipName;
	}

	public void setNameForToolTip(String name) {
		toolTipName = name;
	}

	/**
	 * Method to get location of Popup to show
	 * 
	 * @param node
	 * @return
	 */
	public Point getLocationOfPopup(IGraphNode node) {
		Point nodePosition = helper.getNodeLocation(node);
		Point p = new Point();
		p.x = nodePosition.x + m_textRect.width;
		p.y = nodePosition.y + m_textRect.height + m_option.height;
		for (int i = 0; i < m_associationIndx; i++) {
			p.y += m_assRectangles[i].height;
		}
		p.y += 7;
		return p;
	}
}
