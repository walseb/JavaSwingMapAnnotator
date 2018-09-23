package Uppgift2;
class NamedPlace extends Place {
	private String name;

	public NamedPlace(Category category, Position position, String name) {
		super(category, position);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name + super.getPosition().toString();
	}
}
