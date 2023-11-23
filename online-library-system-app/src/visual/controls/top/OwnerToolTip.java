package visual.controls.top;

import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class OwnerToolTip extends Popup{
	public static final double PREF_WIDTH = 200;
	public static final double PREF_HEIGHT = 80;
	
	private VBox box;
	private Label lblTitle;
	private Label lblText;
	
	private DoubleProperty positionX;
	
	public OwnerToolTip(String title, String text){
		box = new VBox();
		box.setId("ownerToolTip");
		box.setMinSize(200, 80);
		box.setPrefSize(200, 80);
		box.setMaxSize(200, 80);
		
		
		lblTitle = new Label(title);
		lblTitle.getStyleClass().add("lblTitle");
		
		lblText = new Label(text);
		lblText.setWrapText(true);
		lblText.getStyleClass().add("lblText");
		
		box.getChildren().addAll(lblTitle, lblText);
		
		positionX = new DoubleProperty(){
			public void bind(ObservableValue<? extends Number> observable) {;}
			public boolean isBound() {return false;}
			public void unbind() {;}
			public Object getBean() {return null;}
			public String getName() {return null;}
			public void addListener(ChangeListener<? super Number> listener) {;}
			public void removeListener(ChangeListener<? super Number> listener) {;}
			public void addListener(InvalidationListener listener) {;}
			public void removeListener(InvalidationListener listener) {;}
			public double get() {return 0;}
			public void set(double value) {
				setX(value);				
			}			
		};
		
		getContent().add(box);
		
	}
	
	public void hide(){
		super.hide();
	}

	public DoubleProperty getPositionX(){
		return positionX;
	}
	
	public void setTitle(String title){
		lblTitle.setText(title);
	}
	public void setText(String text){
		lblText.setText(text);
	}
	
}