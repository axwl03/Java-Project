package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Emoji {
	
	private int x, y;	// emoji's coordinate
	private int type;	// emoji's type (ex: Face.LAUGH)
	//private String imagePath;
	private boolean isNew; 
	private ImageView emojiImage;
	
	Emoji(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
		if(type == Face.ANGRY)
			emojiImage = new ImageView(new Image(getClass().getResource("resources/angry.png").toExternalForm() , 30, 30, false, true));
		else if(type == Face.HAPPY)
			emojiImage = new ImageView(new Image(getClass().getResource("resources/happy.png").toExternalForm() , 30, 30, false, true));
		else if(type == Face.SAD)
			emojiImage = new ImageView(new Image(getClass().getResource("resources/sad.png").toExternalForm() , 30, 30, false, true));
		else if(type == Face.SURPRISE)
			emojiImage = new ImageView(new Image(getClass().getResource("resources/surprise.png").toExternalForm() , 30, 30, false, true));
		isNew = true;
	}
	
	public ImageView getEmojiImage() {
		return emojiImage;
	}
	
	public boolean getIsNew() {
		return isNew;
	}
	
	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
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
