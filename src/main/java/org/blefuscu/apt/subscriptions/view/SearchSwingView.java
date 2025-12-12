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
import java.awt.Dimension;

public class SearchSwingView extends JPanel implements SearchView {

	private static final String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
	protected static final String WHITESPACES_REGEX = "^\\s*$";
	private static final long serialVersionUID = 1L;
	private JTextField fromTextBox;
	private JTextField toTextBox;
	private JButton btnSearch;
	private transient SubscriptionsController subscriptionsController;

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
		GridBagConstraints gbcLblFrom = new GridBagConstraints();
		gbcLblFrom.anchor = GridBagConstraints.WEST;
		gbcLblFrom.insets = new Insets(0, 0, 5, 5);
		gbcLblFrom.gridx = 0;
		gbcLblFrom.gridy = 0;
		add(lblFrom, gbcLblFrom);

		JLabel lblTo = new JLabel("To");
		GridBagConstraints gbcLblTo = new GridBagConstraints();
		gbcLblTo.anchor = GridBagConstraints.WEST;
		gbcLblTo.insets = new Insets(0, 0, 5, 0);
		gbcLblTo.gridx = 2;
		gbcLblTo.gridy = 0;
		add(lblTo, gbcLblTo);

		fromTextBox = new JTextField();
		fromTextBox.setMinimumSize(new Dimension(200, 21));

		fromTextBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				enableSearchButtonIfFromDateIsValid();
			}

			private void enableSearchButtonIfFromDateIsValid() {
				btnSearch.setEnabled(fromDateIsValid());
			}

			private boolean fromDateIsValid() {
				return fromTextBox.getText().matches(DATE_REGEX) || fromTextBox.getText().matches(WHITESPACES_REGEX);
			}
		});
		fromTextBox.setName("fromTextBox");
		GridBagConstraints gbcTextField = new GridBagConstraints();
		gbcTextField.insets = new Insets(0, 0, 5, 5);
		gbcTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcTextField.gridx = 0;
		gbcTextField.gridy = 1;
		add(fromTextBox, gbcTextField);
		fromTextBox.setColumns(10);

		toTextBox = new JTextField();
		toTextBox.setMinimumSize(new Dimension(200, 21));

		toTextBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				enableSearchButtonIfToDateIsValid();
			}

			private void enableSearchButtonIfToDateIsValid() {
				btnSearch.setEnabled(toDateIsValid());
			}

			private boolean toDateIsValid() {
				return toTextBox.getText().matches(DATE_REGEX) || toTextBox.getText().matches(WHITESPACES_REGEX);
			}
		});
		toTextBox.setName("toTextBox");
		GridBagConstraints gbcTextField1 = new GridBagConstraints();
		gbcTextField1.insets = new Insets(0, 0, 5, 0);
		gbcTextField1.fill = GridBagConstraints.HORIZONTAL;
		gbcTextField1.gridx = 2;
		gbcTextField1.gridy = 1;
		add(toTextBox, gbcTextField1);
		toTextBox.setColumns(10);

		btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				LocalDate fromDate;
				LocalDate toDate;
				String fromText = fromTextBox.getText();
				String toText = toTextBox.getText();
				
				fromText = removeWhitespacesOnly(fromText);
				toText = removeWhitespacesOnly(toText);
				
				if (fromText.isEmpty()) {
					if (!toText.isEmpty()) {
						fromText = "1970-01-01";
					} else {
						subscriptionsController.requestOrders();
						return;
					}
				}
				if(toText.isEmpty()) {
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
				
				checkIfDatesAreInTheRightOrderAndPerformSearch(fromDate, toDate);
			}

			private void checkIfDatesAreInTheRightOrderAndPerformSearch(LocalDate fromDate, LocalDate toDate) {
				if (fromDate.isBefore(toDate) || fromDate.isEqual(toDate)) {
					subscriptionsController.requestOrders(fromDate, toDate);
				} else {
					subscriptionsController.sendErrorMessage("Start date should be earlier or equal to end date");
				}
			}

			private String removeWhitespacesOnly(String text) {
				if(text.matches(WHITESPACES_REGEX)) {
					text = "";
				}
				return text;
			}
		});

		GridBagConstraints gbcBtnSearch = new GridBagConstraints();
		gbcBtnSearch.anchor = GridBagConstraints.WEST;
		gbcBtnSearch.insets = new Insets(0, 0, 5, 5);
		gbcBtnSearch.gridx = 0;
		gbcBtnSearch.gridy = 2;
		add(btnSearch, gbcBtnSearch);

	}

	public JButton getBtnSearch() {
		return btnSearch;
	}

}