package Uppgift2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JOptionPane;

class Database {
	private LinkedList<Place> markedPlacesList = new LinkedList<Place>();

	private ArrayList<Place> busCategoryList = new ArrayList<Place>();
	private ArrayList<Place> undergroundCategoryList = new ArrayList<Place>();
	private ArrayList<Place> trainCategoryList = new ArrayList<Place>();

	private HashMap<String, ArrayList<NamedPlace>> nameMap = new HashMap<String, ArrayList<NamedPlace>>();
	private HashMap<Position, Place> positionMap = new HashMap<Position, Place>();

	public void addPlace(Place place) {
		if (positionMap.get(place.getPosition()) != null) {
			JOptionPane.showMessageDialog(null, "You are only allowed to have one place per position");
		} else {
			positionMap.put(place.getPosition(), place);
			getPlacesWithCategory(place.getCategory()).add(place);

			if (place instanceof NamedPlace) {
				NamedPlace namedPlace = (NamedPlace) place;
				ArrayList<NamedPlace> namedPlaceList = nameMap.get(namedPlace.getName());
				if (namedPlaceList == null) {
					namedPlaceList = new ArrayList<NamedPlace>();
					namedPlaceList.add(namedPlace);
					nameMap.put(namedPlace.getName(), namedPlaceList);
				} else {
					nameMap.get(namedPlace.getName()).add(namedPlace);
				}
			}
		}
	}

	public void removeAllPlacesWithoutUnmarking() {
		busCategoryList = new ArrayList<Place>();
		undergroundCategoryList = new ArrayList<Place>();
		trainCategoryList = new ArrayList<Place>();

		nameMap = new HashMap<String, ArrayList<NamedPlace>>();
		positionMap = new HashMap<Position, Place>();
	}

	public void removePlaceWithoutUnmarking(Place place) {
		if (place instanceof NamedPlace) {
			NamedPlace namedPlace = (NamedPlace) place;
			ArrayList<NamedPlace> namedPlaceList = nameMap.get(namedPlace.getName());
			if (namedPlaceList != null) {
				namedPlaceList.remove(namedPlace);
			}
		}
		positionMap.remove(place.getPosition());
		removePlaceFromCategory(place);
	}


	public ArrayList<Place> getPlacesWithCategory (Place.Category category) {
		switch (category) {
		case BUS:
			return busCategoryList;
		case UNDERGROUND:
			return undergroundCategoryList;
		case TRAIN:
			return trainCategoryList;
		default:
			return new ArrayList<Place>();
		}
	}

	private Place.Category stringToCategory(String category) {
		switch (category) {
		case "Underground":
			return Place.Category.UNDERGROUND;
		case "Train":
			return Place.Category.TRAIN;
		case "Bus":
			return Place.Category.BUS;
		default:
			return Place.Category.NONE;
		}
	}

	public void exportPlaces(String fileName) {
		try {
			FileWriter file = new FileWriter(fileName);
			PrintWriter out = new PrintWriter(file);
			for (Place place : positionMap.values()) {
				String category = place.getCategory().toString().toLowerCase();
				category = category.substring(0, 1).toUpperCase() + category.substring(1);

				if (place instanceof DescribedPlace) {
					DescribedPlace describedPlace = (DescribedPlace) place;
					out.println("Described" + "," + category + "," + place.getPosition().getX() + ","
							+ place.getPosition().getY() + "," + describedPlace.getName() + ","
							+ describedPlace.getDescription());
				} else {
					NamedPlace namedPlace = (NamedPlace) place;
					out.println("Named" + "," + category + "," + place.getPosition().getX() + ","
							+ place.getPosition().getY() + "," + namedPlace.getName());
				}
			}
			out.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Something went wrong when exporting");
		}
	}

	public ArrayList<Place> readPlacesFromFile(String fileName) {
		try {
			FileReader file = new FileReader(fileName);
			BufferedReader br = new BufferedReader(file);
			ArrayList<Place> places = new ArrayList<Place>((int) new File(fileName).length());

			String line;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				String placeType = tokens[0];
				Place.Category category = stringToCategory(tokens[1]);
				Position position = new Position(0, 0);
				try {
					position = new Position(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));

				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "File is corrupted");
				}
				if (placeType.equals("Named")) {
					// If name is blank
					if (tokens.length == 5) {
						places.add(new NamedPlace(category, position, tokens[4]));
					} else {
						JOptionPane.showMessageDialog(null, "File is corrupted");
					}
				} else {
					if (tokens.length == 6) {
						places.add(new DescribedPlace(category, position, tokens[4], tokens[5]));
					} else
						JOptionPane.showMessageDialog(null, "File is corrupted");
				}
			}
			br.close();
			file.close();
			return places;
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not found");
			return new ArrayList<Place>(0);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Something went wrong when loading");
			return new ArrayList<Place>(0);
		}
	}

	public ArrayList<NamedPlace> getPlacesByName(String name) {
		return nameMap.get(name);
	}

	private void removePlaceFromCategory(Place place) {
		getPlacesWithCategory(place.getCategory()).remove(place);
	}

	public Place getPlaceByPosition(Position position) {
		return positionMap.get(position);
	}

	public void addToMarkedPlaces(Place place) {
		markedPlacesList.add(place);
	}

	public LinkedList<Place> getMarkedPlaces() {
		return markedPlacesList;
	}

	public void removeFromMarkedPlaces(Place place) {
		markedPlacesList.remove(place);
	}

	public Collection<Place> getPositionMapValues() {
		return positionMap.values();
	}
}
