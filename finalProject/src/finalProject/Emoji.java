package finalProject;

public class Emoji {
	
	private int x, y;	// emoji's coordinate
	private int type;	// emoji's type (ex: Face.LAUGH)
	
	Emoji(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getType() {
		return type;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public String toString() {
		return x + " " + y + " " + type;
	}
	
	public static Emoji parseString(String str) {
		String[] data = str.split(" ");
		return new Emoji(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]));
	}
}
