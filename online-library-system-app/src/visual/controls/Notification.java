package visual.controls;

import visual.Main;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Notification{
	private static Notification notif = null;
	
	private Label lblText;
	private Button btnClose;
	private OwnerVBox notification;
	
	private Timeline showNotif;
	private Timeline hideNotif;
	private Timeline rebound;
	private AnimationTimer autoClose;
	private Integer currentValue = 0;
	
	private boolean isShowing = false;
	
	public static Notification getInstance(){
		if(notif == null)
			notif = new Notification();
		return notif;
	}
	
	private Notification(){
		notification = new OwnerVBox();
		notification.setId("notification");
		notification.setSpacing(5);		
		StackPane.setAlignment(notification, Pos.TOP_RIGHT);
		StackPane.setMargin(notification, new Insets(170, 0 , 0, 0));
		
		showNotif = new Timeline();
		showNotif.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(notification.changedWidthProperty(), 0)),
				new KeyFrame(new Duration(300), new KeyValue(notification.changedWidthProperty(), 230))
				);
		hideNotif = new Timeline();
		hideNotif.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(notification.changedWidthProperty(), 230)),
				new KeyFrame(new Duration(300), new KeyValue(notification.changedWidthProperty(), 0))
				);
		
		rebound = new Timeline();
		rebound.setCycleCount(3);
		rebound.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(notification.changedOpacityProperty(), 0.4)),
				new KeyFrame(Duration.millis(100), new KeyValue(notification.changedOpacityProperty(), 1.0))
				);
		
		autoClose = new AnimationTimer() {			
			public void handle(long now) {
				if(currentValue == 500){
					autoClose.stop();
					isShowing = false;
					hideNotif.play();
				}
				currentValue++;
			}
		};
		
		lblText = new Label();
		lblText.setWrapText(true);
		
		btnClose = new Button();
		btnClose.setMinSize(16, 16);
		btnClose.setPrefSize(16, 16);
		btnClose.setMaxSize(16, 16);
		btnClose.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				autoClose.stop();
				isShowing = false;
				hideNotif.play();
			}
		});
	}
	
	public void setText(String text){
		lblText.setText(text);
	}
	
	public void showNotification(){
		currentValue = 0;
		//Acceder al StackPane que sera el administrador de diseño
		//root de la aplicacion
		if(Main.getStackRoot().getChildren().contains(notification)){
			rebound.play();
		}
		else{
			autoClose.start();
			isShowing = true;
			notification.getChildren().removeAll(notification.getChildren());
			Main.getStackRoot().getChildren().add(notification);
			showNotif.play();
		}		
	}
	
	class OwnerVBox extends VBox{
		private DoubleProperty changedWidth;
		private DoubleProperty changedOpacity;
		
		public static final double HEIGHT = 80;
		public static final double WIDTH = 230;
		
		public DoubleProperty changedWidthProperty(){
			return changedWidth;
		}
		public DoubleProperty changedOpacityProperty(){
			return changedOpacity;
		}
		
		public OwnerVBox(){
			super();
			
			setMinSize(0, HEIGHT);
			setPrefSize(0, HEIGHT);
			setMaxSize(0, HEIGHT);
			
			changedWidth = new DoubleProperty() {
				public void set(double value) {
					setMinSize(value, HEIGHT);
					setPrefSize(value, HEIGHT);
					setMaxSize(value, HEIGHT);
					
					if(value == WIDTH)
						getChildren().addAll(btnClose, lblText);
					else if(value == 0 && !isShowing)
						Main.getStackRoot().getChildren().remove(notification);
						
				}
				public double get() { return 0; }
				public void removeListener(InvalidationListener listener) { }
				public void addListener(InvalidationListener listener) { }
				public void removeListener(ChangeListener<? super Number> listener) { }
				public void addListener(ChangeListener<? super Number> listener) { }
				public String getName() { return null; }
				public Object getBean() { return null; }
				public void unbind() { }
				public boolean isBound() { return false; }			
				public void bind(ObservableValue<? extends Number> observable) { }
			};
			
			changedOpacity = new DoubleProperty() {
				public void set(double value) {	
					setOpacity(value);						
				}
				public double get() { return 0; }
				public void removeListener(InvalidationListener listener) { }
				public void addListener(InvalidationListener listener) { }
				public void removeListener(ChangeListener<? super Number> listener) { }			
				public void addListener(ChangeListener<? super Number> listener) { }			
				public String getName() { return null; }			
				public Object getBean() { return null; }			
				public void unbind() { }
				public boolean isBound() { return false; }			
				public void bind(ObservableValue<? extends Number> observable) { }
			};
		}
	}
}