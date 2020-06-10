package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TimerDisplay {
	
	private  ImageView zeroImage;	//0
	private  ImageView oneImage;		//1
	private  ImageView twoImage;		//2
	private  ImageView threeImage;	//3
	private  ImageView fourImage;	//4
	private  ImageView fiveImage;	//5
	private  ImageView sixImage;		//6
	private  ImageView sevenImage;	//7
	private  ImageView eightImage;	//8
	private  ImageView nineImage;	//9
	
	public TimerDisplay() {
		// TODO Auto-generated constructor stub
		zeroImage = new ImageView(new Image(getClass().getResource("resources/0.png").toExternalForm(), 100, 150, false, true));
		oneImage = new ImageView(new Image(getClass().getResource("resources/1.png").toExternalForm(), 100, 150, false, true));
		twoImage = new ImageView(new Image(getClass().getResource("resources/2.png").toExternalForm(), 100, 150, false, true));
		threeImage = new ImageView(new Image(getClass().getResource("resources/3.png").toExternalForm(), 100, 150, false, true));
		fourImage = new ImageView(new Image(getClass().getResource("resources/4.png").toExternalForm(), 100, 150, false, true));
		fiveImage = new ImageView(new Image(getClass().getResource("resources/5.png").toExternalForm(), 100, 150, false, true));
		sixImage = new ImageView(new Image(getClass().getResource("resources/6.png").toExternalForm(), 100, 150, false, true));
		sevenImage = new ImageView(new Image(getClass().getResource("resources/7.png").toExternalForm(), 100, 150, false, true));
		eightImage = new ImageView(new Image(getClass().getResource("resources/8.png").toExternalForm(), 100, 150, false, true));
		nineImage = new ImageView(new Image(getClass().getResource("resources/9.png").toExternalForm(), 100, 150, false, true));
	}
	
	public ImageView getNumberImage(long num) {
		if(num == 0) {
			return zeroImage;
		}
		else if(num == 1) {
			return oneImage;
		}
		else if(num == 2) {
			return twoImage;
		}
		else if(num == 3) {
			return threeImage;
		}
		else if(num == 4) {
			return fourImage;
		}
		else if(num == 5) {
			return fiveImage;
		}
		else if(num == 6) {
			return sixImage;
		}
		else if(num == 7) {
			return sevenImage;
		}
		else if(num == 8) {
			return eightImage;
		}
		else {
			return nineImage;
		}
	}

}
