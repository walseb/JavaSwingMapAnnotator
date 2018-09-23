package Uppgift2;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

class TopPanel extends JPanel implements ActionListener {
	public enum PlaceType {
		NAMED, DESCRIPTION
	}
	private JButton newButton;

	private JRadioButton namedRadioButton;
	private JRadioButton describedRadioButton;

	private JTextField searchTextField;

	private JButton searchButton;

	private JButton hideButton;
	private JButton removeButton;
	private JButton coordinatesButton;

	private MapPanel map;

	private JTextField xCoordinateTextField = new JTextField();
	private JTextField yCoordinateTextField = new JTextField();

	private JComponent[] coordinatesPromptFields = new JComponent[] {
	    new JLabel("x:"), xCoordinateTextField,
	    new JLabel("y:"), yCoordinateTextField };

	public TopPanel(MapPanel map) {
		this.map = map;
		newButton = new JButton("New");
		this.add(newButton);
		newButton.addActionListener(this);

		JPanel RadioButtons = new JPanel();

		this.add(RadioButtons);
		RadioButtons.setLayout(new BorderLayout(0, 0));

		namedRadioButton = new JRadioButton("Named");
		RadioButtons.add(namedRadioButton);
		namedRadioButton.setSelected(true);

		describedRadioButton = new JRadioButton("Described");
		RadioButtons.add(describedRadioButton, BorderLayout.SOUTH);

		ButtonGroup RadioButtonsGroup = new ButtonGroup();
		RadioButtonsGroup.add(describedRadioButton);
		RadioButtonsGroup.add(namedRadioButton);

		searchTextField = new JTextField();
		this.add(searchTextField);
		searchTextField.setColumns(10);

		searchButton = new JButton("Search");
		this.add(searchButton);
		searchButton.addActionListener(this);

		hideButton = new JButton("Hide");
		this.add(hideButton);
		hideButton.addActionListener(this);

		removeButton = new JButton("Remove");
		this.add(removeButton);
		removeButton.addActionListener(this);

		coordinatesButton = new JButton("Coordinates");
		this.add(coordinatesButton);
		coordinatesButton.addActionListener(this);
	}

	private void coordinatesPrompt() {
		xCoordinateTextField.setText("");
		yCoordinateTextField.setText("");

		int result = JOptionPane.showConfirmDialog(null, coordinatesPromptFields, "Coordinates",
				JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {

			int x = 0;
			int y = 0;
			try {
				x = Integer.parseInt(xCoordinateTextField.getText());
				y = Integer.parseInt(yCoordinateTextField.getText());

				Position position = new Position(x, y);
				map.markPlaceAtPosition(position);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Only integers allowed as input");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == newButton) {
			PlaceType placeType;
			if (namedRadioButton.isSelected() == true) {
				placeType = PlaceType.NAMED;
			} else {
				placeType = PlaceType.DESCRIPTION;
			}
			map.addPlaceNextClick(placeType);
		} else if (event.getSource() == searchButton) {
			map.search(searchTextField.getText());
		} else if (event.getSource() == hideButton) {
			map.hideAll();
		} else if (event.getSource() == removeButton) {
			map.removeMarkedPlaces();
		} else if (event.getSource() == coordinatesButton) {
			coordinatesPrompt();
		}
	}
}
