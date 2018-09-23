package Uppgift2;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JComponent;

abstract class Place extends JComponent {
	public enum Category {
		BUS, UNDERGROUND, TRAIN, NONE
	}

	private Category category;
	private boolean marked;

	private Position position;
	private final int sizeX = 25;
	private final int sizeY = 25;

	public Place(Category category, Position position) {
		setBounds(position.getX() - (sizeX / 2), position.getY() - sizeY, sizeX, sizeY);
		this.category = category;
		this.position = position;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (marked == true) {
			g.setColor(Color.PINK);
			g.fillRect(0, 0, sizeX, sizeY);
		} else {
			switch (category) {
			case BUS:
				g.setColor(Color.RED);
				break;
			case UNDERGROUND:
				g.setColor(Color.BLUE);
				break;
			case TRAIN:
				g.setColor(Color.GREEN);
				break;
			default:
				g.setColor(Color.BLACK);
				break;
			}
			int x[] = { sizeX / 2, 0, sizeX };
			int y[] = { sizeY, 0, 0 };
			Polygon poly = new Polygon(x, y, 3);
			g.fillPolygon(poly);
		}
	}

	public Position getPosition() {
		return position;
	}

	public void setHidden(boolean hidden) {
		this.setVisible(!hidden);
	}

	public boolean isMarked() {
		return marked;
	}

	public void mark(boolean mark) {
		marked = mark;
		repaint();
	}

	public Category getCategory() {
		return category;
	}
}
