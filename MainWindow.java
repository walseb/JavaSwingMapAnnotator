package Uppgift2;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

class MainWindow extends JFrame implements WindowListener {

	private JPanel mainPanel;
	private MapPanel map;

	public static void main(String[] args) {
		new MainWindow();
	}

	public MainWindow() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(0, 0));
		setContentPane(mainPanel);

		// Add content panel
		JPanel contentPanel = new JPanel();
		mainPanel.add(contentPanel, BorderLayout.SOUTH);
		contentPanel.setLayout(new BorderLayout(0, 0));

		// Add map
		map = new MapPanel();
		JScrollPane scrollPane = new JScrollPane(map);
		scrollPane.setPreferredSize(new Dimension(800, 500));
		contentPanel.add(scrollPane, BorderLayout.WEST);

		// Add side panel
		SidePanel sidePanel = new SidePanel(map);
		contentPanel.add(sidePanel, BorderLayout.EAST);

		// Add top panel
		TopPanel topPanel = new TopPanel(map);
		contentPanel.add(topPanel, BorderLayout.NORTH);

		// Add menu bar
		mainPanel.add(new MenuBar(map), BorderLayout.NORTH);

		this.pack();
		setVisible(true);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (map.dataLossPrompt() == true) {
			System.exit(0);
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}
}
