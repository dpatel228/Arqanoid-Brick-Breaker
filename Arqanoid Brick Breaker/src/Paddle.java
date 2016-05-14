import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;

public class Paddle {
	private int x, y;
	private int velX;
	private int height, width;
	private Color c;
	
	public Paddle(int x, int y, int height, int width) {
		this.x = x;
		this.y = y;
		this.velX = 0;
		this.height = height;
		this.width = width;
		this.c = Color.white;
	}
	
	public Rectangle2D getBoundaryRectangle() {
		return new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
	}
	
	//getters and setters
	public int getX() {return this.x;}
	public void setX(int x) {this.x = x;}
	
	public int getY() {return this.y;}
	public void setY(int y) {this.y = y;}
	
	public int getVelocityX() {return this.velX;}
	public void setVelocityX(int velX) {this.velX = velX;}

	public int getHeight() {return this.height;}
	public void setHeight(int height) {this.height = height;}
	
	public int getWidth() {return this.width;}
	public void setWidth(int width) {this.width = width;}
	
	public Color getColour() {return this.c;}
	public void setColour(Color c) {this.c = c;}
}
