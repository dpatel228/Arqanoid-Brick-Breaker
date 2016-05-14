import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.geom.*;
import java.text.AttributedString;
import java.util.Random;

import javax.swing.*;

public class Arqanoid extends JPanel implements Runnable, KeyListener{
	private TestFrameExample main;
	public final static int MAXWIDTH = 1024, MAXHEIGHT = 768; //width & height of JPanel
	private boolean running; //keeps track of state of the program
	private Thread thread;
	private Graphics2D graphics;
	private Image image; //used for double buffering

	//game objects
	private LevelLoader levelLoad;
	private Brick[][] brickGrid;
	private Paddle paddle;
	private Ball ball;

	//keyboard variables
	private boolean[] keysPressed = new boolean[4];
	private final int KEY_LEFT = 0, KEY_RIGHT = 1, KEY_SPACE = 2, KEY_ENTER = 3;

	//ball states
	private int ballState;
	private final static int BALL_START = 0;
	private final static int BALL_MOVING = 1;

	//game states
	private int gameState;
	private final static int GAME_START = 0;
	private final static int GAME_PLAY = 1;
	private final static int GAME_LOSE = 2;

	//game components
	private int score;
	private int lives;
	private int level;

	public Arqanoid(TestFrameExample main) {
		this.setDoubleBuffered(false); //we'll use our own double buffering method
		this.setBackground(Color.black);
		this.setPreferredSize(new Dimension(MAXWIDTH, MAXHEIGHT));
		this.setFocusable(true);
		this.requestFocus();
		addKeyListener(this);
		this.main = main;
	}

	public static void main(String[] args) {
		new TestFrameExample();
	}

	public void addNotify() {
		super.addNotify();
		startGame();
	}

	public void stopGame() {
		running = false;
	}

	//Creates a thread and starts it
	public void startGame() {
		if (thread == null || !running) {
			thread = new Thread(this);
		}
		thread.start(); //calls run()
	}

	public void run() {
		running = true;

		//initialize all objects, load level, etc
		init();

		//game loop
		while (running) {
			createImage(); //creates image for double buffering
			updateGame();
			drawImage(); //draws on the JPanel
		}

		levelLoad.closeFile();
		System.exit(0);
	}

	public void init() {
		initializeGame();
		initializeLevelGrid();
		initializePaddle();
		initializeBall();
	}

	public void initializeGame() {
		score = 0;
		lives = 3;
		level = 1;
	}

	public void initializeLevelGrid() {
		levelLoad = new LevelLoader();
		System.out.println(level);
		if(level == 1)
			levelLoad.openLevelFile("level1.txt");
		else if(level == 2)
			levelLoad.openLevelFile("level2.txt");

		levelLoad.createLevelGrid();
		brickGrid = levelLoad.getBrickGrid();
	}

	public void initializePaddle() {
		paddle = new Paddle(MAXWIDTH/2 - 75, 700, 25, 150);
	}

	public void initializeBall() {
		ballState = BALL_START;
		ball = new Ball(MAXWIDTH/2 - 7, 686, 14, 14, Color.white);
	}

	public void updateGame() {
		drawBackground();

		drawLevelGrid(); //do this

		updateGameState();

		updatePaddle();
		drawPaddle();

		updateBall();
		checkForCollisions();
		drawBall();

		drawScore();        
	}

	public void drawBackground() {
		Rectangle2D background = new Rectangle2D.Double(0,0,1024,768);
		graphics.setColor(Color.black);
		graphics.fill(background);
	}

	public void drawLevelGrid() {
		for(int row=0; row < brickGrid.length; row++) {
			for(int col=0; col < brickGrid[0].length; col++) {
				if(brickGrid[row][col] != null)
					drawBrick(row, col);
			}
		}
	}

	public void drawBrick(int row, int col) {
		Brick currentBrick = brickGrid[row][col];
		if(currentBrick.isAlive()) {
			int x = currentBrick.getX();
			int y = currentBrick.getY();
			int w = currentBrick.getWidth();
			int h = currentBrick.getHeight();
			Rectangle2D brickRec = new Rectangle2D.Double(x, y, w, h);
			graphics.setColor(currentBrick.getColour());
			graphics.fill(brickRec);
		}
	}

	public void updateGameState() {
		if(lives <= 0) {
			gameState = GAME_LOSE;
		}

		if(gameState == GAME_START) {
			initializeGame();
			initializeLevelGrid();
			gameState = GAME_PLAY;
		}
		else if(gameState == GAME_LOSE) {
			drawLoseScreen();
			if(keysPressed[KEY_SPACE]) {
				gameState = GAME_START;
				lives = 3;
				//level = 1;
			}
		}

		if(winConditionReached()) {
			gameState = GAME_PLAY;
			level++;
			initializeLevelGrid();
			resetBall();
		}
	}

	public void drawLoseScreen() {
		String loseMessage = "You lose!";
		Font font = new Font("Times New Roman",Font.BOLD,25);
		graphics.setFont(font);
		graphics.setColor(Color.RED);
		graphics.drawString(loseMessage, 450, 300);
	}


	public boolean winConditionReached() {
		for (int s = 0; s < brickGrid.length;s++) {
			for (int j = 0; j < brickGrid[s].length;j++) 
				if (brickGrid[s][j]!=null && brickGrid[s][j].isAlive())
					return false;

		}
		return true;        
	}

	public void updatePaddle() {
		if (keysPressed[KEY_LEFT]) {
			paddle.setVelocityX(-7);
			paddle.setX(paddle.getX()-7);
		}
		else if (keysPressed[KEY_RIGHT]) {
			paddle.setVelocityX(7);
			paddle.setX(paddle.getX()+7);
		}
		else 
			paddle.setVelocityX(0);
		if (paddle.getX()+paddle.getVelocityX()+paddle.getWidth()>1024)
			paddle.setX(1024-paddle.getWidth());
		else if (paddle.getX()+paddle.getVelocityX()<0)
			paddle.setX(0);
	}

	public void drawPaddle() {
		int x = paddle.getX();
		int y = paddle.getY();
		int width = paddle.getWidth();
		int height = paddle.getHeight();
		RoundRectangle2D paddleR = new RoundRectangle2D.Double(x,y,width,height,10,10);
		graphics.setColor(Color.white);
		graphics.fill(paddleR);
	}

	public void updateBall() {
		System.out.println("State: " + ballState);
		if(ballState == BALL_START) {
			resetBall();
		}
		else if(ballState == BALL_MOVING) {
			if(ball.getX() + ball.getVelocityX() >= MAXWIDTH)
				ball.flipVelocityX();
			else if(ball.getX() + ball.getVelocityX() < 0)
				ball.flipVelocityX();
			ball.setX(ball.getX() + ball.getVelocityX());

			if(ball.getY() + ball.getVelocityY() >= MAXHEIGHT) {
				lives--;

				resetBall();
			}
			else if(ball.getY() + ball.getVelocityY() < 0)
				ball.flipVelocityY();
			ball.setY(ball.getY() + ball.getVelocityY());
		}
	}
	public void resetBall() {
		ball.setX(paddle.getX() + paddle.getWidth()/2);
		ball.setY(686);
		ball.setVelocityX(5);
		ball.setVelocityY(-5);
		ballState = BALL_START;
	}

	public void checkForCollisions() {
		checkForPaddleCollision();
		checkForBrickCollision();
	}

	public void checkForPaddleCollision() {
		int pX = paddle.getX();
		int pY = paddle.getY();
		int pWidth = paddle.getWidth();
		int pHeight = paddle.getHeight();

		RoundRectangle2D paddleRectangle = new RoundRectangle2D.Double(pX,pY,pWidth,pHeight,10,10);
		Rectangle2D ballRectangle = ball.getBoundaryRectangle();
		if(paddleRectangle.intersects(ballRectangle))
			ball.flipVelocityY();
	}

	public void checkForBrickCollision() {
		Rectangle2D ballRectangle = ball.getBoundaryRectangle();
		for (int s = 0; s < brickGrid.length;s++) {
			for (int j = 0; j < brickGrid[s].length;j++) {
				if (brickGrid[s][j]!=null && brickGrid[s][j].isAlive()) {
					Rectangle2D brickRectangle = brickGrid[s][j].getBoundaryRectangle();
					if (ballRectangle.intersects(brickRectangle)) {
						brickLocationWithBall(brickGrid[s][j]);
						updateScore();
					}
				}
			}
		}
	}

	public void brickLocationWithBall(Brick currentBrick) {
		currentBrick.setAlive(false);
		Rectangle2D top = currentBrick.getTopBounds();
		Rectangle2D bottom = currentBrick.getBottomBounds();
		Rectangle2D left = currentBrick.getLeftBounds();
		Rectangle2D right = currentBrick.getRightBounds();

		Rectangle2D ballBounds = currentBrick.getBoundaryRectangle();
		if(ballBounds.intersects(top))
			ball.flipVelocityY();
		else if(ballBounds.intersects(bottom))
			ball.flipVelocityY();
		else if(ballBounds.intersects(left))
			ball.flipVelocityX();
		else if(ballBounds.intersects(right))
			ball.flipVelocityX();
	}

	public void updateScore() {
		score += 100;
	}

	public void drawBall() {
		int x = ball.getX();
		int y = ball.getY();
		int width = ball.getWidth();
		int height = ball.getHeight();
		Ellipse2D ball2 = new Ellipse2D.Double(x,y,width,height);
		graphics.setColor(Color.white);
		graphics.fill(ball2);
	}

	public void drawScore() {
		String scoreString = "Score: ";
		scoreString+=score;
		graphics.setColor(Color.red);
		Font font = new Font("Times New Roman",Font.BOLD,50);
		graphics.setFont(font);
		graphics.drawString(scoreString,20,60);
	}

	//creates an image for double buffering
	public void createImage() {
		if (image == null) {
			image = createImage(MAXWIDTH, MAXHEIGHT);

			if (image == null) {
				System.out.println("Cannot create buffer");
				return;
			}
			else
				graphics = (Graphics2D)image.getGraphics(); //get graphics object from Image
		}
	}

	//outputs everything to the JPanel
	public void drawImage() {
		Graphics g;
		try {
			g = this.getGraphics(); //a new image is created for each frame, this gets the graphics for that image so we can draw on it
			if (g != null && image != null) {
				g.drawImage(image, 0, 0, null);
				g.dispose(); //not associated with swing, so we have to free memory ourselves (not done by the JVM)
			}
			image = null;
		}catch(Exception e) {System.out.println("Graphics objects error");}
	}

	//KEYLISTENER METHODS
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			keysPressed[KEY_LEFT] = true;
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			keysPressed[KEY_RIGHT] = true;
		else if(ballState == BALL_START && e.getKeyCode() == KeyEvent.VK_SPACE) {
			keysPressed[KEY_SPACE] = true;
			ballState = BALL_MOVING;
		}
		else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			keysPressed[KEY_ENTER] = true;
		}

	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			keysPressed[KEY_LEFT] = false;
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			keysPressed[KEY_RIGHT] = false;
		else if(e.getKeyCode() == KeyEvent.VK_SPACE)
			keysPressed[KEY_SPACE] = false;
	}

	public void keyTyped(KeyEvent e) {}
}