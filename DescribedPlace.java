package Uppgift2;
class DescribedPlace extends NamedPlace {
	String description;

	public DescribedPlace(Category category, Position position, String name, String description) {
		super(category, position, name);
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
