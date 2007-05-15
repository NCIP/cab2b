package edu.wustl.cab2b.client.ui.filter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DataFilterUI extends JPanel implements MouseListener, MouseMotionListener {
	private int m_width;

	private int m_xStart = 20;

	// The minimum and maximum value for which filter
	// will be drawn
	private double m_minValue;

	private double m_maxValue;

	public static double m_currentMinValue;

	public static double m_currentMaxValue;

	private double m_scale = 1.0;

	/**
	 * Main UI comonents are declared here
	 */
	JLabel m_leftLabel;

	JButton m_leftButton;

	JLabel m_centerLabel;

	JButton m_rightButton;

	JLabel m_rightLabel;

	FilterComponent m_parentFilter;

	public DataFilterUI(FilterComponent parentFilter, int width, int height, double minValue,
			double maxValue, double prevMinValue, double prevMaxValue) {
		m_width = width;
		m_minValue = minValue;
		m_maxValue = maxValue;

		// Set parent filter object
		m_parentFilter = parentFilter;
		// The scaling factor for showing filter
		m_scale = (double) (m_width - m_xStart) / (double) (m_maxValue - m_minValue);

		m_currentMinValue = prevMinValue;
		m_currentMaxValue = prevMaxValue;
		setLayout(null);
		setPreferredSize(new Dimension(width, height));

		// Method to initialize user interface for filter scroller
		initUI();
		drawFilterUI();
	}

	private void initUI() {
		m_leftLabel = new JLabel();
		m_leftButton = new JButton();
		m_centerLabel = new JLabel();
		m_rightButton = new JButton();
		m_rightLabel = new JLabel();

		m_leftLabel.setText("");
		m_leftLabel.setOpaque(true);
		m_leftLabel.setBackground(new Color(0, 0, 150));
		m_leftLabel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLoweredBevelBorder()));
		this.add(m_leftLabel);
		ForwardArrowIcon lIcon = new ForwardArrowIcon();
		m_leftButton.setIcon(lIcon);
		this.add(m_leftButton);

		m_centerLabel.setText("");
		m_centerLabel.setOpaque(true);
		m_centerLabel.setBackground(new Color(10, 0, 250));
		m_centerLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		this.add(m_centerLabel);

		ReverseArrowIcon rIcon = new ReverseArrowIcon();
		m_rightButton.setIcon(rIcon);
		this.add(m_rightButton);

		m_rightLabel.setText("");
		m_rightLabel.setOpaque(true);
		m_rightLabel.setBackground(new Color(0, 0, 150));
		m_rightLabel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLoweredBevelBorder()));
		this.add(m_rightLabel);

		m_leftLabel.addMouseListener(this);
		m_leftButton.addMouseListener(this);
		m_leftButton.addMouseMotionListener(this);
		m_centerLabel.addMouseListener(this);
		m_rightButton.addMouseListener(this);
		m_rightButton.addMouseMotionListener(this);
		m_rightLabel.addMouseListener(this);

	}

	private void setCurrentMinValue(double currentMinValue) {
		m_currentMinValue = currentMinValue;
	}

	private void setCurrentMaxValue(double currentMaxValue) {
		m_currentMaxValue = currentMaxValue;
	}

	public void setFilterMinValue(double value) {
		if (value < m_minValue) {
			value = m_minValue;
		} else if (m_currentMaxValue < value) {
			value = m_currentMaxValue;
		}
		setCurrentMinValue(value);
		drawFilterUI();
	}

	public void setFilterMaxValue(double value) {
		if (value > m_maxValue) {
			value = m_maxValue;
		} else if (m_currentMinValue > value) {
			value = m_currentMinValue;
		}
		setCurrentMaxValue(value);
		drawFilterUI();
	}

	public void drawFilterUI() {
		// Put left label first
		// m_xStart m_componentHeight
		int leftLabelWidth = (int) ((m_currentMinValue - m_minValue) * m_scale) - 10;
		m_leftLabel.setPreferredSize(new Dimension(leftLabelWidth, 20));
		m_leftLabel.setBounds(m_xStart, 0, leftLabelWidth + 10, 20);

		// Put left button

		m_leftButton.setPreferredSize(new Dimension(20, 20));
		m_leftButton.setBounds(m_xStart + leftLabelWidth + 10, 0, 20, 20);

		// put center label
		int centerLabelWidth = (int) ((m_currentMaxValue - m_currentMinValue) * m_scale) - 20;

		m_centerLabel.setPreferredSize(new Dimension(centerLabelWidth, 20));
		m_centerLabel.setBounds(m_xStart + leftLabelWidth + 20, 0, centerLabelWidth, 20);

		// put right button
		m_rightButton.setPreferredSize(new Dimension(20, 20));
		m_rightButton.setBounds(m_xStart + leftLabelWidth + centerLabelWidth + 20, 0, 20, 20);

		// put right label
		int rightLabelWidth = (int) ((m_maxValue - m_currentMaxValue) * m_scale) - 10;
		m_rightLabel.setPreferredSize(new Dimension(20, 20));
		m_rightLabel.setBounds(m_xStart + leftLabelWidth + centerLabelWidth + 40, 0,
				rightLabelWidth, 20);

		m_parentFilter.setMinValue(m_currentMinValue);
		m_parentFilter.setMaxValue(m_currentMaxValue);
	}

	public void mouseClicked(MouseEvent event) {
		// Check which component is being clicked and take appropriate action
		if (event.getComponent() == m_leftLabel) {
			// This returns position of mouse point with respect to the
			// component of interest
			Point p = event.getPoint();
			double actualValue = (int) (p.x / m_scale) + m_minValue;
			setCurrentMinValue(actualValue);

		} else if (event.getComponent() == m_centerLabel) {
			Point p = event.getPoint();
			Point componentPosition = m_centerLabel.getLocation();
			double actualValue = (int) ((componentPosition.x + p.x - m_xStart) / m_scale)
					+ m_minValue;
			if ((p.x + componentPosition.x) < m_width / 2) {
				setCurrentMinValue(actualValue);
			} else {
				setCurrentMaxValue(actualValue);
			}
		} else if (event.getComponent() == m_rightLabel) {
			Point p = event.getPoint();
			Point componentPosition = m_rightLabel.getLocation();
			double actualValue = (int) ((componentPosition.x + p.x - m_xStart) / m_scale)
					+ m_minValue;
			setCurrentMaxValue(actualValue);
		}
		drawFilterUI();
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		// Method to handle mouse drag event
		if (m_leftButton == arg0.getComponent()) {
			Point p = arg0.getPoint();
			Point componentPosition = m_leftButton.getLocation();
			double actualValue = (float) ((componentPosition.x + p.x - m_xStart) / m_scale)
					+ m_minValue;
			// check if minimum value is within the specified range
			if ((actualValue >= m_minValue) && (actualValue < m_currentMaxValue)) {
				setCurrentMinValue(actualValue);
				drawFilterUI();
			}
		} else if (m_rightButton == arg0.getComponent()) {
			Point p = arg0.getPoint();
			Point componentPosition = m_rightButton.getLocation();
			double actualValue = (float) ((componentPosition.x + p.x - m_xStart) / m_scale)
					+ m_minValue;
			if ((actualValue >= m_currentMinValue) && (actualValue <= this.m_maxValue)) {
				setCurrentMaxValue(actualValue);
				drawFilterUI();
			}
		}

	}

	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
