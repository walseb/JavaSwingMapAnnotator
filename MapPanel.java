package Uppgift2;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

class MapPanel extends JPanel implements MouseListener {

	private JFileChooser fileChooser;
	private boolean placesSaved = true;
	private Database database = new Database();

	private TopPanel.PlaceType addPlaceTypeNextClick;
	private ImageIcon mapImage;

	private JTextField namePromptTextField = new JTextField();
	private JTextField descriptionPromptTextField = new JTextField();
	private JComponent[] descriptionPromptFields = new JComponent[] { new JLabel("Name"), namePromptTextField, new JLabel("Description"), descriptionPromptTextField };

	private Place.Category selectedCategory = Place.Category.NONE;

	public MapPanel() {
		setLayout(null);
		fileChooser = new JFileChooser();
		addMouseListener(this);
	}

	public void updateSize() {
		setPreferredSize(new Dimension(mapImage.getIconWidth(), mapImage.getIconHeight()));
	}

	// Returns false if user wants to cancel action
	public boolean dataLossPrompt() {
		if (getPlacesSaved() == false) {
			int result = JOptionPane.showConfirmDialog(this, "Save places?", "Save Places?",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				exportPlaces();
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return false;
			}
		}
		return true;
	}

	public void changeMapPicture() {
		if (dataLossPrompt()) {
			String imageName = promptForFileLocation();
			if (imageName != null && !imageName.isEmpty()) {
				removeAllPlaces();
				mapImage = new ImageIcon(imageName);
				updateSize();
				this.revalidate();
			}
		}
	}

	public void addPlaceNextClick(TopPanel.PlaceType placeType) {
		addPlaceTypeNextClick = placeType;
		this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (mapImage != null) {
			g.drawImage(mapImage.getImage(), 0, 0, mapImage.getIconWidth(), mapImage.getIconHeight(), this);
		}
	}

	public void addPlace(Place place) {
		database.addPlace(place);
		add(place);
		place.addMouseListener(this);
		placesSaved = false;
		this.repaint();
	}

	public void addPlace(Position position) {
		if (addPlaceTypeNextClick == TopPanel.PlaceType.NAMED) {
			String name = JOptionPane.showInputDialog(this, "Enter name", null);
			addPlace(new NamedPlace(selectedCategory, position, name));
		} else {
			setDescriptionPromptTextBoxes("", "", true);
			int result = JOptionPane.showConfirmDialog(null, descriptionPromptFields, "Enter description",
					JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				addPlace(new DescribedPlace(selectedCategory, position, namePromptTextField.getText(),
						descriptionPromptTextField.getText()));
			}
		}
		addPlaceTypeNextClick = null;
	}

	public void setDescriptionPromptTextBoxes(String name, String description, boolean editable) {
		namePromptTextField.setText(name);
		descriptionPromptTextField.setText(description);
		namePromptTextField.setEditable(editable);
		descriptionPromptTextField.setEditable(editable);
	}

	private void unmarkAllMarkedPlaces() {
		Iterator<Place> iterator = database.getMarkedPlaces().iterator();
		while (iterator.hasNext()) {
			Place place = iterator.next();
			place.mark(false);
			iterator.remove();
		}
		repaint();
	}

	public void markPlaceAtPosition(Position position) {
		Place place = database.getPlaceByPosition(position);
		if (place == null) {
			JOptionPane.showMessageDialog(null, "There is no place at these coordinates");
		} else {
			unmarkAllMarkedPlaces();
			toggleMarkPlace(place);
		}
	}

	private void toggleMarkPlace(Place place) {
		if (place.isMarked() == false) {
			database.addToMarkedPlaces(place);
		} else {
			database.removeFromMarkedPlaces(place);
		}
		place.mark(!place.isMarked());
	}

	public void removeMarkedPlaces() {
		for (Place place : database.getMarkedPlaces()) {
			database.removePlaceWithoutUnmarking(place);
			remove(place);
		}
		unmarkAllMarkedPlaces();
	}

	public void hideAll() {
		for (Place place : database.getMarkedPlaces()) {
			place.setHidden(true);
		}
		unmarkAllMarkedPlaces();
	}

	public void search(String searchName) {
		unmarkAllMarkedPlaces();
		ArrayList<NamedPlace> places = database.getPlacesByName(searchName);
		if (places != null) {
			for (NamedPlace namedPlace : places) {
				toggleMarkPlace(namedPlace);
				namedPlace.setHidden(false);
			}
		}
	}

	public void setPlaceVisibilityByCategory(Place.Category category, boolean hidden) {
		for (Place place : database.getPlacesWithCategory(category)) {
			place.setHidden(hidden);
		}
		selectedCategory = category;
	}

	private String promptForFileLocation() {
		int returnVal = fileChooser.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return file.getPath();
		} else {
			return null;
		}
	}

	public void exportPlaces() {
		String file = promptForFileLocation();
		if (file != null) {
			database.exportPlaces(file);
			placesSaved = true;
		}
	}

	private void removeAllPlaces() {
		unmarkAllMarkedPlaces();
		for (Place place : database.getPositionMapValues()) {
			remove(place);
		}
		database.removeAllPlacesWithoutUnmarking();
	}

	public void importPlaces() {
		if (dataLossPrompt() == true) {
			String file = promptForFileLocation();
			if (file != null) {
				removeAllPlaces();
				ArrayList<Place> placesToAdd = database.readPlacesFromFile(file);
				for (Place place : placesToAdd) {
					addPlace(place);
				}
				placesSaved = true;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (addPlaceTypeNextClick != null) {
			Position position = new Position(this.getMousePosition().x, this.getMousePosition().y);
			addPlace(position);
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		// If left click
		if (event.getButton() == MouseEvent.BUTTON1) {
			if (event.getSource() instanceof DescribedPlace) {
				DescribedPlace describedPlace = (DescribedPlace) event.getSource();
				setDescriptionPromptTextBoxes(describedPlace.toString(), describedPlace.getDescription(), false);

				JOptionPane.showConfirmDialog(this, descriptionPromptFields, "Description", JOptionPane.PLAIN_MESSAGE);
			} else if (event.getSource() instanceof NamedPlace) {
				NamedPlace namedPlace = (NamedPlace) event.getSource();
				JOptionPane.showMessageDialog(this, namedPlace.toString());
			}

		}
		// If right click
		else if (event.getButton() == MouseEvent.BUTTON3) {
			if (event.getSource() instanceof Place) {
				Place place = (Place) event.getSource();
				toggleMarkPlace(place);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	public boolean getPlacesSaved() {
		return placesSaved;
	}

}
