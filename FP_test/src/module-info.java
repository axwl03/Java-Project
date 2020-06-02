module FP_test {
	requires javafx.controls;
	requires javafx.media;
	requires javafx.graphics;
	requires java.desktop;
	requires javafx.swing;
	
	opens application to javafx.graphics, javafx.fxml;
}
