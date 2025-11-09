package org.blefuscu.apt.subscriptions.view;

import javax.swing.JPanel;

import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Dimension;

public class MessageSwingView extends JPanel implements MessageView {

	private static final long serialVersionUID = 1L;
	private JTextField messageTextBox;
	private SubscriptionsController subscriptionsController;

	/**
	 * Create the panel.
	 */
	public MessageSwingView() {
		setMinimumSize(new Dimension(300, 10));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		

		messageTextBox = new JTextField();
		messageTextBox.setName("messageTextBox");
		GridBagConstraints gbcMessageTextBox = new GridBagConstraints();
		gbcMessageTextBox.fill = GridBagConstraints.HORIZONTAL;
		gbcMessageTextBox.gridx = 0;
		gbcMessageTextBox.gridy = 0;
		add(messageTextBox, gbcMessageTextBox);
		messageTextBox.setColumns(10);
		messageTextBox.setMinimumSize(new Dimension(300, 100));

	}

	@Override
	public void showInfoMessage(String string) {
		messageTextBox.setForeground(Color.BLACK);
		messageTextBox.setText(string);
	}

	@Override
	public void showErrorMessage(String string) {
		messageTextBox.setForeground(Color.RED);
		messageTextBox.setText(string);
	}

	public void setSubscriptionsController(SubscriptionsController subscriptionsController) {
		this.subscriptionsController = subscriptionsController;
	}

}
