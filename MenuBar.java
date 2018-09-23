package Uppgift2;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

class MenuBar extends JMenuBar implements ActionListener {

	private JMenuItem newMapMenuItem;
	private JMenuItem loadPlacesMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem exitMenuItem;

	private MapPanel map;

	public MenuBar(MapPanel map) {
		this.map = map;

		JMenu ArchiveMenu = new JMenu("Archive");
		this.add(ArchiveMenu);

		newMapMenuItem = new JMenuItem("New Map");
		ArchiveMenu.add(newMapMenuItem);
		newMapMenuItem.addActionListener(this);

		loadPlacesMenuItem = new JMenuItem("Load Places");
		ArchiveMenu.add(loadPlacesMenuItem);
		loadPlacesMenuItem.addActionListener(this);

		saveMenuItem = new JMenuItem("Save");
		ArchiveMenu.add(saveMenuItem);
		saveMenuItem.addActionListener(this);

		exitMenuItem = new JMenuItem("Exit");
		ArchiveMenu.add(exitMenuItem);
		exitMenuItem.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == newMapMenuItem) {
			map.changeMapPicture();
		}
		if (event.getSource() == loadPlacesMenuItem) {
			map.importPlaces();
		} else if (event.getSource() == saveMenuItem) {
			if (map.getPlacesSaved() == false) {
				map.exportPlaces();
			}
		}
		if (event.getSource() == exitMenuItem) {
			if (map.dataLossPrompt() == true) {
				System.exit(0);
			}
		}
	}
}
