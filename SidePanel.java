package Uppgift2;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

class SidePanel extends JPanel implements ActionListener, ListSelectionListener {
	private JList<String> categoriesList;
	private JButton hideCategoryButton;

	private MapPanel map;

	public SidePanel(MapPanel map) {
		this.map = map;
		this.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel categoriesLabel = new JLabel("Categories");
		this.add(categoriesLabel);

		String[] categories = { "Bus", "Underground", "Train" };
		categoriesList = new JList<String>(categories);
		this.add(categoriesList);
		categoriesList.addListSelectionListener(this);

		hideCategoryButton = new JButton("Hide category");
		this.add(hideCategoryButton);
		hideCategoryButton.addActionListener(this);
	}

	private NamedPlace.Category getSelectedCategory() {
		String selected = (String) categoriesList.getSelectedValue();
		if (selected == null) {
			return NamedPlace.Category.NONE;
		} else {
			switch (selected) {
			case "Bus":
				return NamedPlace.Category.BUS;
			case "Underground":
				return NamedPlace.Category.UNDERGROUND;
			case "Train":
				return NamedPlace.Category.TRAIN;
			default:
				return NamedPlace.Category.NONE;
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		map.setPlaceVisibilityByCategory(getSelectedCategory(), false);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == hideCategoryButton) {
			map.setPlaceVisibilityByCategory(getSelectedCategory(), true);
		}
	}
}
