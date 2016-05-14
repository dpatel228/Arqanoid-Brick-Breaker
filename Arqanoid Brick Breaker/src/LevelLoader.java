import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

//file must only be 16 characters in width for each line (maximum of 8 lines)
public class LevelLoader {
	private Scanner fileInput;
	private Vector<String> lines = new Vector<String>();
	private Brick[][] brickGrid;
	
	public void openLevelFile(String fileName) {
		fileInput = null;
    	try {
    		fileInput = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			System.out.println("No such file exists! Add the file to the project OR check to see that the file name is correct.");
		}
	}
	
	public Brick[][] getBrickGrid() {
		return brickGrid;
	}
	
	public void createLevelGrid() {
		while(fileInput.hasNext())
			lines.add(fileInput.nextLine());
		
		int y = lines.size();
		int x = 16;
		brickGrid = new Brick[x][y];
		
		for(int lineNumber=0; lineNumber < lines.size(); lineNumber++) {
			createRowBricks(lines.elementAt(lineNumber), lineNumber);
		}
	}
	
	private void createRowBricks(String line, int y) {
		for(int x=0; x < line.length(); x++) {
			Color brickColour = chooseBrickColour(line.charAt(x));
			createBrick(x, y, brickColour);
		}
	}
	
	private Color chooseBrickColour(char brickColour) {
		if(brickColour == 'R')
			return Color.red;
		else if(brickColour == 'G')
			return Color.green;
		else if(brickColour == 'B')
			return Color.blue;
		else
			return null;
	}
	
	private void createBrick(int x, int y, Color brickColour) {
		if(brickColour == null)
			brickGrid[x][y] = null;
		else
			brickGrid[x][y] = new Brick(x*64, y*30 + 100, brickColour);
	}
		
	public void closeFile() {
		fileInput.close();
	}
}