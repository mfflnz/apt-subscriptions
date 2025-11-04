package org.blefuscu.apt.subscriptions.view;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

public class MessageSwingView extends JPanel implements MessageView {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public MessageSwingView() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel label = new JLabel(" ");
		label.setName("messageLabel");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		add(label, gbc_label);

	}

	@Override
	public void showInfoMessage(String string) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showErrorMessage(String string) {
		// TODO Auto-generated method stub

	}

}
