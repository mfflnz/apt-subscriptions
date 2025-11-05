package org.blefuscu.apt.subscriptions.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class DashboardSwingView extends JFrame implements DashboardView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private SearchSwingView searchPanel;
	private ListSwingView listPanel;
	private MessageSwingView messagePanel;
	private OrderSwingView orderPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DashboardSwingView frame = new DashboardSwingView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DashboardSwingView() {
		setTitle("Dashboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		searchPanel = new SearchSwingView();
		GridBagConstraints gbc_searchPanel = new GridBagConstraints();
		gbc_searchPanel.insets = new Insets(0, 0, 5, 5);
		gbc_searchPanel.fill = GridBagConstraints.BOTH;
		gbc_searchPanel.gridx = 0;
		gbc_searchPanel.gridy = 0;
		contentPanel.add(searchPanel, gbc_searchPanel);
		
		listPanel = new ListSwingView();
		GridBagConstraints gbc_listPanel = new GridBagConstraints();
		gbc_listPanel.insets = new Insets(0, 0, 5, 5);
		gbc_listPanel.fill = GridBagConstraints.BOTH;
		gbc_listPanel.gridx = 0;
		gbc_listPanel.gridy = 1;
		contentPanel.add(listPanel, gbc_listPanel);

		messagePanel = new MessageSwingView();
		GridBagConstraints gbc_messagePanel = new GridBagConstraints();
		gbc_messagePanel.insets = new Insets(0, 0, 5, 5);
		gbc_messagePanel.fill = GridBagConstraints.BOTH;
		gbc_messagePanel.gridx = 0;
		gbc_messagePanel.gridy = 2;
		contentPanel.add(messagePanel, gbc_messagePanel);
		
		orderPanel = new OrderSwingView();
		GridBagConstraints gbc_orderPanel = new GridBagConstraints();
		gbc_orderPanel.anchor = GridBagConstraints.NORTH;
		gbc_orderPanel.gridheight = 3;
		gbc_orderPanel.insets = new Insets(0, 0, 5, 5);
		gbc_orderPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_orderPanel.gridx = 1;
		gbc_orderPanel.gridy = 0;
		contentPanel.add(orderPanel, gbc_orderPanel);

	}



}
