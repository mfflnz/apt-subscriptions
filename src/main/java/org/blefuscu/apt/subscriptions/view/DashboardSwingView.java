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
		setBounds(100, 100, 800, 500);
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
		gbc_searchPanel.anchor = GridBagConstraints.NORTH;
		gbc_searchPanel.insets = new Insets(0, 0, 5, 5);
		gbc_searchPanel.fill = GridBagConstraints.HORIZONTAL;
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

	}

	@Override
	public void showMessage(String message) {
		// TODO Auto-generated method stub
		
	}

}
