import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Brick {
	private int x, y;
	private final int HEIGHT = 20, WIDTH = 56;
	private boolean isAlive;
	private Color colour;
	
	public Brick(int x, int y, Color colour) {
		this.x = x;
		this.y = y;
		this.colour = colour;
		isAlive = true;
	}

	public Rectangle2D getBoundaryRectangle() {
		return new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
	}
	
	public Rectangle2D getTopBounds() {
		return new Rectangle2D.Double(getX(), getY(), getWidth(), 1);
	}
	
	public Rectangle2D getBottomBounds() {
		return new Rectangle2D.Double(getX(), getY()+getHeight(), getWidth(), 1);
	}
	
	public Rectangle2D getLeftBounds() {
		return new Rectangle2D.Double(getX(), getY(), 1, getHeight());
	}
	
	public Rectangle2D getRightBounds() {
		return new Rectangle2D.Double(getX()+getWidth(), getY(), 1, getHeight());
	}
	
	//getters and setters
	public int getX() {return this.x;}
	public void setX(int x) {this.x = x;}
	
	public int getY() {return this.y;}
	public void setY(int y) {this.y = y;}
	
	public int getHeight() {return this.HEIGHT;}
	
	public int getWidth() {return this.WIDTH;}
	
	public boolean isAlive() {return this.isAlive;}
	public void setAlive(boolean isAlive) {this.isAlive = isAlive;}
	
	public Color getColour() {return this.colour;}
	public void setColour(Color colour) {this.colour = colour;}
}
