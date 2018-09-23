package Uppgift2;
class Position {
	private int x, y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Position) {
			Position position = (Position) obj;
			if (position.x == this.x && position.y == this.y) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return x + y;
	}

	@Override
	public String toString() {
		return "{ " + x + ", " + y + " }";
	}
}
