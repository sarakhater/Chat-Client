package signup;

import java.time.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
class DatePanel extends JPanel implements ItemListener {
	
	// Constants
	private static final int MAX_AGE = 100;
	
	// Labels
	private JLabel label_header = new JLabel();
	
	// Combo Boxes
	private JComboBox<Year> comboBox_years;
	private JComboBox<Month> comboBox_months = new JComboBox<Month>(Month.values());
	private JComboBox<Integer> comboBox_days = new JComboBox<Integer>(new DefaultComboBoxModel<Integer>());
	
	// Constructors
	public DatePanel(String header) {
		label_header.setText(header);
		
		int currentYear = LocalDate.now().getYear();
		Year[] years = new Year[MAX_AGE];
		for (int i = 0; i < years.length; i++) {
			years[i] = Year.of(currentYear - i);
		}
		comboBox_years = new JComboBox<Year>(years);
		
		setDays();
		initializeComponents();
	}
	public DatePanel(String header, Font font, Color color) {
		this(header);
		label_header.setFont(font);
		label_header.setForeground(color);
	}
	public DatePanel(String header, LocalDate date) {
		this(header);
		setDate(date);
	}
	public DatePanel(String header, LocalDate date, Font font, Color color) {
		this(header, font, color);
		setDate(date);
	}
	
	// Initialize Components
	private void initializeComponents() {
		comboBox_years.addItemListener(this);
		comboBox_months.addItemListener(this);
		
		this.setLayout(new FlowLayout(FlowLayout.LEADING));
		this.add(label_header);
		this.add(comboBox_days);
		this.add(comboBox_months);
		this.add(comboBox_years);
		
		setDate(null);
	}
	
	// Set Days
	private void setDays() {
		int daysLength = ((Month)comboBox_months.getSelectedItem()).length(((Year)comboBox_years.getSelectedItem()).isLeap());
		DefaultComboBoxModel<Integer> comboBoxModel_days = ((DefaultComboBoxModel<Integer>)comboBox_days.getModel());
		while (comboBoxModel_days.getSize() != daysLength) {
			if (comboBoxModel_days.getSize() > daysLength) {
				comboBoxModel_days.removeElement(comboBoxModel_days.getSize());
			} else {
				comboBoxModel_days.addElement(comboBoxModel_days.getSize() + 1);
			}
		}
	}

	// Item Changed
	public void itemStateChanged(ItemEvent event) {
		Object source = event.getSource();
		if (event.getStateChange() == ItemEvent.SELECTED) {
			if (comboBox_years.getSelectedIndex() == -1 || comboBox_months.getSelectedIndex() == -1 || comboBox_days.getSelectedIndex() == -1) {
				return;
			} else if ((source == comboBox_months) || ( (source == comboBox_years) && (((Month)comboBox_months.getSelectedItem()).equals(Month.FEBRUARY)) )) {
				setDays();
			}
		}
	}
	
	// Set Date
	public void setDate(LocalDate date) {
		if (date == null) {
			comboBox_years.setSelectedIndex(-1);
			comboBox_months.setSelectedIndex(-1);
			comboBox_days.setSelectedIndex(-1);
		} else {
			comboBox_years.setSelectedItem(Year.of(date.getYear()));
			comboBox_months.setSelectedItem(date.getMonth());
			comboBox_days.setSelectedItem(date.getDayOfMonth());
		}
	}
	
	// Get Date
	public LocalDate getDate() {
		Year year = ((Year)comboBox_years.getSelectedItem());
		Month month = ((Month)comboBox_months.getSelectedItem());
		int day = ((Integer)comboBox_days.getSelectedItem());
		return LocalDate.of(year.getValue(), month, day);
	}
	
}