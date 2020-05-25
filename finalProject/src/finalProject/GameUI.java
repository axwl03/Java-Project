package finalProject;
import java.util.ArrayList;

public class GameUI {
	private ArrayList<Emoji> emojiList;
	private int matchedEmoji;	// store matched result
	
	GameUI() {
		emojiList = new ArrayList<Emoji>();
	}
	
	public void setResult(int type) {
		matchedEmoji = type;
	}
	
	// inappropriate function name
	// generate new emoji and send all existed emojis to client
	public void getEmoji() {
		
	}
	
	public void setEmoji() {
		
	}
	
	public void setImage() {
		
	}
	
}
