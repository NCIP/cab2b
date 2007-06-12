package edu.wustl.cab2b.client.ui.filter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.wustl.cab2b.client.ui.experiment.ApplyFilterPanel;

public class FilterComponent extends Cab2bFilterPopup implements MouseListener, ActionListener, FocusListener,
		KeyListener {
	private static final long serialVersionUID = 1L;

	private String m_title;

	private double[] m_values;

	private double m_minValue = Integer.MAX_VALUE;

	private double m_maxValue = Integer.MIN_VALUE;

	private JTextField m_minValTextField;

	private JTextField m_maxValTextField;

	private double prevMinRange;

	private double prevMaxRange;

	private DataFilterUI dataFilter;

	// Constructor method for filter component
	public FilterComponent(
			String title,
			ApplyFilterPanel applyFilterpanel,
			RangeFilter oldFilter,
			double[] columnVal,
			String columnName,
			int columnIndex) {
		super(applyFilterpanel, columnName, columnIndex);
		m_title = title;
		m_values = columnVal;
		if (oldFilter != null) {
			m_minValue = oldFilter.originalMin;
			m_maxValue = oldFilter.originalMax;

			prevMinRange = oldFilter.minRange;
			prevMaxRange = oldFilter.maxRange;
		} else {
			getMinMaxValues();
			prevMinRange = m_minValue;
			prevMaxRange = m_maxValue;
		}
		initUI();
	}

	private void getMinMaxValues() {
		for (int i = 0; i < m_values.length; i++) {
			if (m_values[i] < m_minValue) {
				m_minValue = m_values[i];
			}
			if (m_values[i] > m_maxValue) {
				m_maxValue = m_values[i];
			}
		}
	}

	private void initUI() {
		this.setLayout(null);

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(null);
		titlePanel.setPreferredSize(new Dimension(425, 100));
		titlePanel.setBounds(30, 10, 355, 100);
		titlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.add(titlePanel);

		JLabel titleLabel = new JLabel(m_title);
		titleLabel.setPreferredSize(new Dimension(100, 20));
		titleLabel.setBounds(5, 10, 100, 20);
		titlePanel.add(titleLabel);

		m_minValTextField = new JTextField();
		m_minValTextField.setText(Double.toString(m_minValue));
		m_minValTextField.setBorder(null);
		m_minValTextField.setBackground(titlePanel.getBackground());
		m_minValTextField.setBounds(30, 40, 55, 20);
		m_minValTextField.setEditable(false);
		m_minValTextField.addMouseListener(this);
		m_minValTextField.addFocusListener(this);
		m_minValTextField.addKeyListener(this);
		titlePanel.add(m_minValTextField);

		m_maxValTextField = new JTextField();
		m_maxValTextField.setText(Double.toString(m_maxValue));
		m_maxValTextField.setBorder(null);
		m_maxValTextField.setBackground(titlePanel.getBackground());
		m_maxValTextField.setBounds(295, 40, 55, 20);
		m_maxValTextField.setEditable(false);
		m_maxValTextField.addMouseListener(this);
		m_maxValTextField.addFocusListener(this);
		m_maxValTextField.addKeyListener(this);
		titlePanel.add(m_maxValTextField);

		dataFilter = new DataFilterUI(this, 290, 20, m_minValue, m_maxValue, prevMinRange, prevMaxRange);
		dataFilter.setBounds(20, 60, 300, 30);
		titlePanel.add(dataFilter);

		okButton.setBounds(80, 120, 85, 22);

		this.add(okButton);

		cancelButton.setBounds(245, 120, 85, 22);
		this.add(cancelButton);

	}

	// Fill textfield with appropriate values
	public void setMinValue(double minValue) {
		java.text.DecimalFormat df2 = new java.text.DecimalFormat("###,##0.00");
		m_minValTextField.setText(df2.format(minValue));
	}

	// -------------------------------------------------------------
	// Fill textfield with appropriate values
	// -------------------------------------------------------------
	public void setMaxValue(double maxValue) {
		java.text.DecimalFormat df2 = new java.text.DecimalFormat("###,##0.00");
		m_maxValTextField.setText(df2.format(maxValue));
	}

	public void mouseClicked(MouseEvent arg0) {
		// If mouse double-click event occurs
		if (2 == arg0.getClickCount()) {
			if (arg0.getComponent() == m_minValTextField) {
				m_minValTextField.setBackground(Color.WHITE);
				m_minValTextField.setEditable(true);
				m_minValTextField.setBorder(BorderFactory.createBevelBorder(1));
			} else if (arg0.getComponent() == m_maxValTextField) {
				m_maxValTextField.setBorder(BorderFactory.createBevelBorder(1));
				m_maxValTextField.setBackground(Color.WHITE);
				m_maxValTextField.setEditable(true);
			}
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		// arg0.getComponent().transferFocus();
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void focusLost(FocusEvent arg0) {
		if (arg0.getSource() instanceof JTextField) {
			JTextField field = (JTextField) arg0.getSource();
			field.setBackground(field.getParent().getBackground());
			field.setEditable(false);
			field.setBorder(BorderFactory.createEmptyBorder());
		}
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() instanceof JTextField) {
			((JTextField) arg0.getSource()).transferFocus();
		}
	}

	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			if (arg0.getComponent() == this.m_minValTextField) {
				m_minValTextField.transferFocusBackward();
				m_minValTextField.setBackground(getBackground());
				m_minValTextField.setEditable(false);
				m_minValTextField.setBorder(BorderFactory.createEmptyBorder());
				dataFilter.setFilterMinValue(Float.parseFloat(m_minValTextField.getText()));
			} else if (arg0.getComponent() == this.m_maxValTextField) {
				m_maxValTextField.transferFocusBackward();
				m_maxValTextField.setBackground(getBackground());
				m_maxValTextField.setEditable(false);
				m_maxValTextField.setBorder(BorderFactory.createEmptyBorder());
				dataFilter.setFilterMaxValue(Float.parseFloat(m_maxValTextField.getText()));
			}
		}
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	protected CaB2BFilterInterface okActionPerformed(ActionEvent e) {
		double minimum = DataFilterUI.m_currentMinValue;
		double maximum = DataFilterUI.m_currentMaxValue;
		return new RangeFilter(minimum, maximum, columnIndex, columnName, m_minValue, m_maxValue);
	}

}
