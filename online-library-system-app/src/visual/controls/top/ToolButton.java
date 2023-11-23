package visual.controls.top;

import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.VBox;

public class ToolButton extends Button {
	public static final double PREF_WIDTH = 70;
	public static final double PREF_HEIGHT = 70;
	
	private DoubleProperty opacityProperty;
	
	public ToolButton(){
		super();
		
		setMinSize(70, 70);
		setPrefSize(70, 70);
		setMaxSize(70, 70);
		
		setOpacity(0.7);
		
		Reflection reflection = new Reflection();
		reflection.setTopOffset(0);
		reflection.setFraction(0.4);
		setEffect(reflection);
		
		opacityProperty = new DoubleProperty() {
			
			public void set(double value) {
				setOpacity(value);
			}
			
			public double get() {return 0;}
			public void removeListener(InvalidationListener listener) {;}
			public void addListener(InvalidationListener listener) {;}
			public void removeListener(ChangeListener<? super Number> listener) {;}
			public void addListener(ChangeListener<? super Number> listener) {;}
			public String getName() {return null;}
			public Object getBean() {return null;}
			public void unbind() {}
			public boolean isBound() {return false;}
			public void bind(ObservableValue<? extends Number> observable) {}
		};
		
	}
	
	public DoubleProperty setOpacityProperty(){
		return opacityProperty;
	}
}

class Tip extends VBox{
	private Label lblTitle;
	private Label lblText;
	
	public Tip(String title, String text){
		setId("tip");
		
		
		
		lblTitle = new Label(title);
		lblTitle.getStyleClass().add("lblTitle");
		
		lblText = new Label(text);
		lblText.getStyleClass().add("lblText");
		
		
		
	}
	
	
}