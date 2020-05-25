package finalProject;

public class Emoji {
	
	private int x, y;
	private Face type;
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Face getType() {
		return type;
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setType(Face type) {
		this.type = type;
	}
	
}
