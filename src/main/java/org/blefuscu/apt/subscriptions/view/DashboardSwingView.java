package org.blefuscu.apt.subscriptions.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class DashboardSwingView extends JFrame implements DashboardView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;

	/**
	 * Create the frame.
	 */
	public DashboardSwingView(SearchSwingView searchPanel, ListSwingView listPanel, OrderSwingView orderPanel,
			MessageSwingView messagePanel) {
		setTitle("Dashboard");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		searchPanel.setName("searchPanel");
		GridBagConstraints gbcSearchPanel = new GridBagConstraints();
		gbcSearchPanel.insets = new Insets(0, 0, 5, 5);
		gbcSearchPanel.fill = GridBagConstraints.BOTH;
		gbcSearchPanel.gridx = 0;
		gbcSearchPanel.gridy = 0;
		contentPanel.add(searchPanel, gbcSearchPanel);

		listPanel.setName("listPanel");
		GridBagConstraints gbcListPanel = new GridBagConstraints();
		gbcListPanel.insets = new Insets(0, 0, 5, 5);
		gbcListPanel.fill = GridBagConstraints.BOTH;
		gbcListPanel.gridx = 0;
		gbcListPanel.gridy = 1;
		contentPanel.add(listPanel, gbcListPanel);

		orderPanel.setName("orderPanel");
		GridBagConstraints gbcOrderPanel = new GridBagConstraints();
		gbcOrderPanel.anchor = GridBagConstraints.NORTH;
		gbcOrderPanel.gridheight = 3;
		gbcOrderPanel.insets = new Insets(0, 0, 5, 5);
		gbcOrderPanel.fill = GridBagConstraints.HORIZONTAL;
		gbcOrderPanel.gridx = 1;
		gbcOrderPanel.gridy = 0;
		contentPanel.add(orderPanel, gbcOrderPanel);

		messagePanel.setName("messagePanel");
		GridBagConstraints gbcMessagePanel = new GridBagConstraints();
		gbcMessagePanel.gridwidth = 2;
		gbcMessagePanel.insets = new Insets(0, 0, 0, 5);
		gbcMessagePanel.fill = GridBagConstraints.BOTH;
		gbcMessagePanel.gridx = 0;
		gbcMessagePanel.gridy = 3;
		contentPanel.add(messagePanel, gbcMessagePanel);

	}

}
