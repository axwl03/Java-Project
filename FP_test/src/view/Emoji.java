package view;

public class Emoji {
	
	private int x, y;	// emoji's coordinate
	private int type;	// emoji's type (ex: Face.LAUGH)
	private String imagePath;
	
	Emoji(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
		if(type == Face.ANGRY)
			imagePath = getClass().getResource("resources/angry.png").toExternalForm();
		else if(type == Face.HAPPY)
			imagePath = getClass().getResource("resources/happy.png").toExternalForm();
		else if(type == Face.SAD)
			imagePath = getClass().getResource("resources/sad.png").toExternalForm();
		else if(type == Face.SURPRISE)
			imagePath = getClass().getResource("resources/surprise.png").toExternalForm();
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
	
	public String getImagePath() {
		return imagePath;
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
