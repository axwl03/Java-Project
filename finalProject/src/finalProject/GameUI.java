package finalProject;
import java.util.ArrayList;
import java.util.Random;

public class GameUI {
	private ArrayList<Emoji> myEmojiList;
	private ArrayList<Emoji> enemyEmojiList;
	private ArrayList<Emoji> emojiList;	// original problem
	private int matchedEmoji;	// store matched result
	private Random rand;
	public NetModule net;
	
	// game parameters
	public static final int shift = 5;
	public static final int maxX = 100;
	public static final int maxY = 100;
	public static final int myOffsetX = 10;
	public static final int myOffsetY = 10;
	public static final int enemyOffsetX = 50;
	public static final int enemyOffsetY = 50;
	public static final int maxEmojiGen = 5;
	
	GameUI() {
		myEmojiList = new ArrayList<Emoji>();
		enemyEmojiList = new ArrayList<Emoji>();
		emojiList = new ArrayList<Emoji>();
		net = new NetModule(this);
		rand = new Random();
	}
	
	public void setResult(int type) {
		matchedEmoji = type;
	}
	
	// generate new emoji and send all existed emojis to client
	public void action() {
		if(net.isServer()) {	// server side code
			// delete matched emoji
			int i = 0;
			while(i < myEmojiList.size()) {
				if(myEmojiList.get(i).getType() == matchedEmoji) {
					myEmojiList.remove(i);
					continue;
				}
				i++;
			}
			
			// move emoji downward and remove emoji if it exceeds boundary
			i = 0;
			while(i < myEmojiList.size()) {
				myEmojiList.get(i).setY(myEmojiList.get(i).getY() + shift);
				if(myEmojiList.get(i).getY() > maxY + myOffsetY) {
					myEmojiList.remove(i);
					continue;
				}
				i++;
			}
			
			// random generate emoji to emojiList and append to myEmojiList
			randomEmojiGen();
			for(i = 0; i < emojiList.size(); ++i) {
				myEmojiList.add(new Emoji(emojiList.get(i).getX(), emojiList.get(i).getY(), emojiList.get(i).getType()));
				int mySize = myEmojiList.size();
				myEmojiList.get(mySize - 1).setX(myEmojiList.get(mySize - 1).getX() + myOffsetX);
				myEmojiList.get(mySize - 1).setY(myEmojiList.get(mySize - 1).getY() + myOffsetY);
			}
			
			// PROBABLY NEED ANOTHER THREAD
			// display emoji on screen
			for(i = 0; i < myEmojiList.size(); ++i)
				setEmoji();
			for(i = 0; i < enemyEmojiList.size(); ++i) {
				enemyEmojiList.get(i).setX(enemyEmojiList.get(i).getX()-myOffsetX+enemyOffsetX);
				enemyEmojiList.get(i).setY(enemyEmojiList.get(i).getY()-myOffsetY+enemyOffsetY);
				setEmoji();
			}
			
			// send exist emojiList and myEmojiList
			String str = "new\n";
			for(i = 0; i < emojiList.size(); ++i)
				str = str + emojiList.get(i).toString() + "\n";
			net.send(str);
			str = "enemy\n";
			for(i = 0; i < myEmojiList.size(); ++i)
				str = str + myEmojiList.get(i).toString() + "\n";
			net.send(str);
		}
		else {
			// delete matched emoji
			int i = 0;
			while(i < myEmojiList.size()) {
				if(myEmojiList.get(i).getType() == matchedEmoji) {
					myEmojiList.remove(i);
					continue;
				}
				i++;
			}

			// move emoji downward and remove emoji if it exceeds boundary
			i = 0;
			while(i < myEmojiList.size()) {
				myEmojiList.get(i).setY(myEmojiList.get(i).getY() + shift);
				if(myEmojiList.get(i).getY() > maxY + myOffsetY) {
					myEmojiList.remove(i);
					continue;
				}
				i++;
			}
			
			// append emojiList to myEmojiList
			for(i = 0; i < emojiList.size(); ++i) {
				myEmojiList.add(new Emoji(emojiList.get(i).getX(), emojiList.get(i).getY(), emojiList.get(i).getType()));
				int mySize = myEmojiList.size();
				myEmojiList.get(mySize - 1).setX(myEmojiList.get(mySize - 1).getX() + myOffsetX);
				myEmojiList.get(mySize - 1).setY(myEmojiList.get(mySize - 1).getY() + myOffsetY);
			}
			
			// display emoji on screen
			for(i = 0; i < myEmojiList.size(); ++i)
				setEmoji();
			for(i = 0; i < enemyEmojiList.size(); ++i) {
				enemyEmojiList.get(i).setX(enemyEmojiList.get(i).getX()-myOffsetX+enemyOffsetX);
				enemyEmojiList.get(i).setY(enemyEmojiList.get(i).getY()-myOffsetY+enemyOffsetY);
				setEmoji();
			}
			
			// send myEmojiList to server
			String str = "enemy\n";
			for(int j = 0; j < myEmojiList.size(); ++j)
				str = str + myEmojiList.get(j).toString() + "\n";
			net.send(str);
		}
	}
	
	private void randomEmojiGen() {
		int num = maxEmojiGen; //rand.nextInt(maxEmojiGen+1)
		ArrayList<Emoji> elist = new ArrayList<Emoji>();
		for(int i = 0; i < num; ++i)
			elist.add(new Emoji(rand.nextInt(maxX+1), 0, rand.nextInt(Face.SURPRISE+1)));
		emojiList = elist;
	}
	
	public void setEnemyEmojiList(ArrayList<Emoji> elist) {
		enemyEmojiList = elist;
	}
	
	public void setEmojiList(ArrayList<Emoji> elist) {
		emojiList = elist;
	}
	
	public void setEmoji() {
		
	}
	
	public void setImage() {
		
	}
	
	public void printEmoji() {
		System.out.println("myEmojiList:");
		for(int i = 0; i < myEmojiList.size(); ++i) {
			System.out.println(myEmojiList.get(i).toString());
		}
		System.out.println("emojiList:");
		for(int i = 0; i < emojiList.size(); ++i) {
			System.out.println(emojiList.get(i).toString());
		}
		System.out.println("enemyEmojiList:");
		for(int i = 0; i < enemyEmojiList.size(); ++i) {
			System.out.println(enemyEmojiList.get(i).toString());
		}
	}
	
}
