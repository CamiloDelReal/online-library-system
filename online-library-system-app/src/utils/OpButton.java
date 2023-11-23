package utils;

import javafx.scene.control.Button;

public class OpButton extends Button {
	public static final double WIDTH = 80;
	public static final double HEIGHT = 25;
	
	public OpButton(String text){
		super(text);
		setId("opButton");
		setMinSize(WIDTH, HEIGHT);
		setPrefSize(WIDTH, HEIGHT);
		setMaxSize(WIDTH, HEIGHT);
	}
}
