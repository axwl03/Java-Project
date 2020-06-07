package view;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

import java.util.Timer;

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
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class GameViewManager implements Runnable {
	// game parameters
	private static final int GAME_WIDTH = 1024;
	private static final int GAME_HEIGHT = 768;
	public static final int shift = 2;
	public static final int maxX = 500;
	public static final int maxY = 600;
	public static final int myOffsetX = 10;
	public static final int myOffsetY = 10;
	public static final int enemyOffsetX = 700;
	public static final int enemyOffsetY = 50;
	public static final int maxEmojiGen = 1;
	public static final long duration = 60000;	// milliseconds
	public static final int delay = 500;	// milliseconds
	
	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage;
	private Stage menuStage;
	
	private ArrayList<Emoji> myEmojiList;
	private ArrayList<Emoji> emojiList;	// original problem
	private int matchedEmoji;	// store matched result
	private Random rand;
	public NetModule net;
	
	private long startTime;
	private Date date;
	private boolean isServer;
	private String ipAddr;
	public volatile boolean inGame;
	
	public GameViewManager(String character, String server_IP) {
		date = new Date();
		inGame = false;
		myEmojiList = new ArrayList<Emoji>();
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
		gameStage.show();
		Thread actioner = new Thread(this);
		if(isServer) {
			createStartButton();
		}
		actioner.start();

		// display emoji
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(inGame) 
					printEmoji();
			}
		}, 0, 200);
		AnimationTimer animationTimer = new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				for(int i = 0; i < myEmojiList.size(); ++i)
				{
					setEmoji(myEmojiList.get(i));
					if(EmojiIsOutOfBound(myEmojiList.get(i)))
						myEmojiList.remove(i);
				}
			}
		};
		animationTimer.start();	
	}
	
	private void createStartButton() {  
		GameView_FDButton startButton = new GameView_FDButton("START");
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
			System.out.println("not inGame");
			while(!inGame) {}
			System.out.println("inGame");
			startTime = date.getTime();
			net.startGame();
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if(date.getTime() - startTime < duration) {
						action();
					}
				}
			}, 0, delay);
		}
		else {
			net.connect(ipAddr, 8000);
			while(!inGame) {}
			startTime = date.getTime();
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if(inGame) {
						action();
					}
				}
			}, 0, delay);
		}
	}
	
	// generate new emoji and send all existed emojis to client
	public void action() {
		if(net.isServer()) {	// server side code
			// random generate emoji to emojiList and append to myEmojiList
			randomEmojiGen();
			for(int i = 0; i < emojiList.size(); ++i) {
				myEmojiList.add(new Emoji(emojiList.get(i).getX(), emojiList.get(i).getY(), emojiList.get(i).getType()));
				int mySize = myEmojiList.size();
				myEmojiList.get(mySize - 1).setX(myEmojiList.get(mySize - 1).getX() + myOffsetX);
				myEmojiList.get(mySize - 1).setY(myEmojiList.get(mySize - 1).getY() + myOffsetY);
			}
			
			// send exist emojiList and myEmojiList
			String str = "new\n";
			for(int i = 0; i < emojiList.size(); ++i)
				str = str + emojiList.get(i).toString() + "\n";
			net.send(str);
		}
		else {
			// append emojiList to myEmojiList
			for(int i = 0; i < emojiList.size(); ++i) {
				myEmojiList.add(new Emoji(emojiList.get(i).getX(), emojiList.get(i).getY(), emojiList.get(i).getType()));
				int mySize = myEmojiList.size();
				myEmojiList.get(mySize - 1).setX(myEmojiList.get(mySize - 1).getX() + myOffsetX);
				myEmojiList.get(mySize - 1).setY(myEmojiList.get(mySize - 1).getY() + myOffsetY);
			}
		}
	}
	
	private void randomEmojiGen() {
		int num = maxEmojiGen; //rand.nextInt(maxEmojiGen+1)
		if(myEmojiList.size() > 5)
			return;
		ArrayList<Emoji> elist = new ArrayList<Emoji>();
		for(int i = 0; i < num; ++i)
			elist.add(new Emoji(rand.nextInt(maxX+1), 0, rand.nextInt(Face.SURPRISE+1)));
		emojiList = elist;
	}
	
	public void setEmojiList(ArrayList<Emoji> elist) {
		emojiList = elist;
	}
	
	private void setEmoji(Emoji e) { //add emoji to pane and move emoji
		if(e.getIsNew())
		{
			e.getEmojiImage().setLayoutX(e.getX());
			e.getEmojiImage().setLayoutY(e.getY());
			gamePane.getChildren().add(e.getEmojiImage());
			e.setIsNew(false);
		}
		else {
			e.setY(e.getY() + shift);
			e.getEmojiImage().setLayoutY(e.getY());
		}
	}
	
	private boolean EmojiIsOutOfBound(Emoji e)
	{
		if(e.getY() > maxY + myOffsetY)
		{
			gamePane.getChildren().remove(e.getEmojiImage());
			return true;
		}
		else return false;
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
	}
	
	private void createBackground() {
		Image backgroundImage = new Image(getClass().getResource("resources/game_background.jpg").toString(), 1024, 768, false, true);
		BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		gamePane.setBackground(new Background(background));
	}
	
}
