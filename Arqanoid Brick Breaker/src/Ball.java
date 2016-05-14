import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Ball {
	private int x, y;
	private int velX, velY;
	private int height, width;
	private Color colour;
	
	//constructor
	public Ball(int x, int y, int height, int width, Color colour) {
		this.x = x;
		this.y = y;
		this.velX = 0;
		this.velY = 0;
		this.height = height;
		this.width = width;
		this.colour = colour;
	}
	
	public Rectangle2D getBoundaryRectangle() {
		int x = getX() + (int)(getWidth()*0.15);
		int y = getY() + (int)(getHeight()*0.15);
		int w = (int)(getWidth()*0.70);
		int h = (int)(getHeight()*0.70);
		return new Rectangle2D.Double(x, y, w, h);
	}
	
	public void flipVelocityX() {
		int newVelocity = getVelocityX() * -1;
		setVelocityX(newVelocity);
	}
	
	public void flipVelocityY() {
		int newVelocity = getVelocityY() * -1;
		setVelocityY(newVelocity);
	}
	
	//getters and setters
	public int getX() {return this.x;}
	public void setX(int x) {this.x = x;}
	
	public int getY() {return this.y;}
	public void setY(int y) {this.y = y;}
	
	public int getVelocityX() {return this.velX;}
	public void setVelocityX(int velX) {this.velX = velX;}
	
	public int getVelocityY() {return this.velY;}
	public void setVelocityY(int velY) {this.velY = velY;}

	public int getHeight() {return this.height;}
	public void setHeight(int height) {this.height = height;}
	
	public int getWidth() {return this.width;}
	public void setWidth(int width) {this.width = width;}
	
	public Color getColour() {return this.colour;}
	public void setColour(Color c) {this.colour = c;}
}
