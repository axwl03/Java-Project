package view;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.Date;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameViewManager implements Runnable {
	// game parameters
	private static final int GAME_WIDTH = 1024;
	private static final int GAME_HEIGHT = 768;
	public static final int shift = 5;
	public static final int maxX = 100;
	public static final int maxY = 100;
	public static final int myOffsetX = 10;
	public static final int myOffsetY = 10;
	public static final int enemyOffsetX = 50;
	public static final int enemyOffsetY = 50;
	public static final int maxEmojiGen = 5;
	public static final long duration = 60000;	// milliseconds
	public static final int delay = 500;	// milliseconds
	
	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage;
	private Stage menuStage;
	
	private ArrayList<Emoji> myEmojiList;
	private ArrayList<Emoji> enemyEmojiList;
	private ArrayList<Emoji> emojiList;	// original problem
	private int matchedEmoji;	// store matched result
	private Random rand;
	public NetModule net;
	
	private long startTime;
	private Date date;
	private boolean isServer;
	private String ipAddr;
	public boolean inGame;
	
	public GameViewManager(String character, String server_IP) {
		date = new Date();
		inGame = false;
		myEmojiList = new ArrayList<Emoji>();
		enemyEmojiList = new ArrayList<Emoji>();
		emojiList = new ArrayList<Emoji>();
		net = new NetModule(this);
		rand = new Random();
		initializeStage();
		if(character.equals("server"))
			this.isServer = true;
		else if(character.equals("client")) {
			ipAddr = server_IP;
			this.isServer = false;
		}
		//createKeyListeners();
	}
	
	private void initializeStage() {
		gamePane = new AnchorPane();
		gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
		gameStage = new Stage();
		gameStage.setScene(gameScene);
		gameStage.setTitle("FACE DANCE CHALLENGE");
	}
	
	public void createNewGame(Stage menuStage) {
		this.menuStage = menuStage;
		this.menuStage.hide();
		createBackground();
		//createGameElements();
		//createGameLoop();
		gameStage.show();
		Thread actioner = new Thread(this);
		if(isServer) {
			createStartButton();
		}
		actioner.start();
	}
	
	private void createStartButton() {  
		FDButton startButton = new FDButton("START");
		startButton.setPrefWidth(100);
		startButton.setPrefHeight(30);
		startButton.setFont(Font.loadFont(getClass().getResource("resources/kenvector_future.ttf").toExternalForm(), 12));
		startButton.setLayoutX(500);
		startButton.setLayoutY(100);
		gamePane.getChildren().add(startButton);

		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				inGame = true;
			}
		});
	}
	
	public void setResult(int type) {
		matchedEmoji = type;
	}
	
	public void run() {
		if(isServer) {
			net.listen(8000);
			while(!inGame) {
				try {
					Thread.sleep(delay);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			startTime = date.getTime();
			while(date.getTime() - startTime < duration) {
				action();
				try {
					Thread.sleep(delay);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			net.endGame();
		}
		else {
			net.connect(ipAddr, 8000);
			while(!inGame) {
				try {
					Thread.sleep(delay);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			startTime = date.getTime();
			while(inGame) {
				action();
				try {
					Thread.sleep(delay);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
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
				setEmoji(myEmojiList.get(i));
			for(i = 0; i < enemyEmojiList.size(); ++i) {
				enemyEmojiList.get(i).setX(enemyEmojiList.get(i).getX()-myOffsetX+enemyOffsetX);
				enemyEmojiList.get(i).setY(enemyEmojiList.get(i).getY()-myOffsetY+enemyOffsetY);
				setEmoji(enemyEmojiList.get(i));
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
				setEmoji(myEmojiList.get(i));
			for(i = 0; i < enemyEmojiList.size(); ++i) {
				enemyEmojiList.get(i).setX(enemyEmojiList.get(i).getX()-myOffsetX+enemyOffsetX);
				enemyEmojiList.get(i).setY(enemyEmojiList.get(i).getY()-myOffsetY+enemyOffsetY);
				setEmoji(enemyEmojiList.get(i));
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
	
	public void setEmoji(Emoji e) {
		ImageView emojiImage= new ImageView(new Image(e.getImagePath(), 30, 30, false, true));
		emojiImage.setLayoutX(e.getX());
		emojiImage.setLayoutY(e.getY());
		gamePane.getChildren().add(emojiImage);
	}
	
	public void setImage(BufferedImage capture) {
		Image image = SwingFXUtils.toFXImage(capture, null);
		ImageView camera = new ImageView(image);
		camera.setLayoutX(100);
		camera.setLayoutY(150);
		gamePane.getChildren().add(camera);
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
	
	private void createBackground() {
		Image backgroundImage = new Image(getClass().getResource("resources/game_background.jpg").toString(), 1024, 768, false, true);
		BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		gamePane.setBackground(new Background(background));
	}
	
}
