package finalProject;
import java.util.ArrayList;

public class GameUI {
	private ArrayList<Emoji> myEmojiList;
	private ArrayList<Emoji> enemyEmojiList;
	private ArrayList<Emoji> emojiList;	// original problem
	private int matchedEmoji;	// store matched result
	private NetModule net;
	
	GameUI() {
		myEmojiList = new ArrayList<Emoji>();
		enemyEmojiList = new ArrayList<Emoji>();
		emojiList = new ArrayList<Emoji>();
	}
	
	public void setResult(int type) {
		matchedEmoji = type;
	}
	
	// generate new emoji and send all existed emojis to client
	public void action() {
		// server side code
		// empty emojiList
		while(emojiList.size() != 0)
			emojiList.remove(0);
		
		// delete matched emoji
		int i = 0;
		while(i < myEmojiList.size()) {
			if(myEmojiList.get(i).getType() == matchedEmoji) {
				myEmojiList.remove(i);
				continue;
			}
			i++;
		}
		
		// random generate emoji to emojiList
		
		
		
		
		// send exist emojiList and myEmojiList
		String str = "new\n";
		for(int j = 0; j < emojiList.size(); ++j)
			str = str + emojiList.get(i).toString();
		net.send(str);
		str = "enemy\n";
		for(int j = 0; j < myEmojiList.size(); ++j)
			str = str + myEmojiList.get(i).toString();
		net.send(str);
	}
	
	public void setEmoji() {
		
	}
	
	public void setImage() {
		
	}
	
}
