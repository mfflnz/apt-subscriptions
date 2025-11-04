package org.blefuscu.apt.subscriptions.view;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;

import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;

import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SearchSwingView extends JPanel implements SearchView {

	private static final String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
	protected static final String WHITESPACES_REGEX = "^\\s*$";
	private static final long serialVersionUID = 1L;
	private JTextField fromTextBox;
	private JTextField toTextBox;
	private JButton btnSearch;
	private SubscriptionsController subscriptionsController;

	public void setSubscriptionsController(SubscriptionsController subscriptionsController) {
		this.subscriptionsController = subscriptionsController;
	}

	/**
	 * Create the panel.
	 */
	public SearchSwingView() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblFrom = new JLabel("From");
		GridBagConstraints gbc_lblFrom = new GridBagConstraints();
		gbc_lblFrom.anchor = GridBagConstraints.WEST;
		gbc_lblFrom.insets = new Insets(0, 0, 5, 5);
		gbc_lblFrom.gridx = 0;
		gbc_lblFrom.gridy = 0;
		add(lblFrom, gbc_lblFrom);

		JLabel lblTo = new JLabel("To");
		GridBagConstraints gbc_lblTo = new GridBagConstraints();
		gbc_lblTo.anchor = GridBagConstraints.WEST;
		gbc_lblTo.insets = new Insets(0, 0, 5, 0);
		gbc_lblTo.gridx = 2;
		gbc_lblTo.gridy = 0;
		add(lblTo, gbc_lblTo);

		fromTextBox = new JTextField();

		fromTextBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				btnSearch.setEnabled(fromTextBox.getText().matches(DATE_REGEX) || fromTextBox.getText().matches(WHITESPACES_REGEX));
			}
		});
		fromTextBox.setName("fromTextBox");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		add(fromTextBox, gbc_textField);
		fromTextBox.setColumns(10);

		toTextBox = new JTextField();

		toTextBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnSearch.setEnabled(toTextBox.getText().matches(DATE_REGEX) || toTextBox.getText().matches(WHITESPACES_REGEX));

			}
		});
		toTextBox.setName("toTextBox");
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 2;
		gbc_textField_1.gridy = 1;
		add(toTextBox, gbc_textField_1);
		toTextBox.setColumns(10);

		btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				LocalDate fromDate;
				LocalDate toDate;
				String fromText = fromTextBox.getText();
				String toText = toTextBox.getText();
				
				if(fromText.matches(WHITESPACES_REGEX)) {
					fromText = "";
				}
				 
				if(toText.matches(WHITESPACES_REGEX)) {
					toText = "";
				}

				if ((fromText.isEmpty())) {
					if (!toText.isEmpty()) {
						fromText = "1970-01-01";
					} else {
						subscriptionsController.requestOrders();
						return;	
					}
				}

				if((toText.isEmpty())) {
					toText = LocalDate.now().toString();
				}
				
				try {
					fromDate = LocalDate.parse(fromText);
				}
				catch (DateTimeParseException e) {
					subscriptionsController.sendErrorMessage("Please check start date format");
					return;
				}
				
				try {
					toDate = LocalDate.parse(toText);
				} 
				catch (DateTimeParseException e) {
					subscriptionsController.sendErrorMessage("Please check end date format");
					return;
				}
				
				if (fromDate.isBefore(toDate) || fromDate.isEqual(toDate)) {
					subscriptionsController.requestOrders(fromDate, toDate);
				} else {
					subscriptionsController.sendErrorMessage("Start date should be earlier or equal to end date");
				}

			}

		});

		GridBagConstraints gbc_btnSearch = new GridBagConstraints();
		gbc_btnSearch.anchor = GridBagConstraints.WEST;
		gbc_btnSearch.insets = new Insets(0, 0, 5, 5);
		gbc_btnSearch.gridx = 0;
		gbc_btnSearch.gridy = 2;
		add(btnSearch, gbc_btnSearch);

	}

}