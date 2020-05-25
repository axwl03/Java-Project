package finalProject;

public class Emoji {
	
	private int x, y;	// emoji's coordinate
	private int type;	// emoji's type (ex: Face.LAUGH)
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getType() {
		return type;
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
}
