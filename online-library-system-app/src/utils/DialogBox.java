package utils;
import visual.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class DialogBox extends VBox{
	private StackPane dialogPane;
	private String text;
	private static Image picture = new Image(DialogBox.class.getResource("../visual/icons/alert.png").toExternalForm());
	
	public DialogBox(String text){
    	this.text = text;
    	dialogPane = new StackPane();
    	dialogPane.setId("dialogRoot");
    	
        setId("dialogo");
        setMinSize(280, 110);
        setPrefSize(280, 110);
        setMaxSize(280, 110);
        
        init();
    }
    
    private void init(){
    	HBox content = new HBox();
    	
    	if(picture != null){
    		ImageView view = new ImageView(picture);
    		content.getChildren().add(view);
    	}
    	
    	Label tag = new Label(text);
    	tag.setWrapText(true);
    	
    	VBox textBox = new VBox();
    	textBox.setTranslateX(8);
    	Region spacer1 = new Region();
    	spacer1.setPrefHeight(15);
    	Region spacer2 = new Region();
    	VBox.setVgrow(spacer2, Priority.ALWAYS);
    	
    	textBox.getChildren().addAll(spacer1, tag, spacer2);
    	
    	content.getChildren().add(textBox);
        
        Region separation = new Region();
        VBox.setVgrow(separation, Priority.ALWAYS);
        
        HBox regButton = new HBox();        
        
        Region spacer4 = new Region();
        HBox.setHgrow(spacer4, Priority.ALWAYS);
        
        Button close = new Button("Cerrar");
        close.setDefaultButton(true);
        close.setMinSize(70, 20);
        close.setPrefSize(70, 20);
		close.setMaxSize(70, 20);
        close.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e){
        		dialogPane.getChildren().remove(DialogBox.this);
        		if(dialogPane.getChildren().isEmpty())
        			dialogPane.setVisible(false);
        	}
        }); 
        
        regButton.getChildren().addAll(spacer4, close);
        
        getChildren().addAll(content, separation, regButton);
        
        dialogPane.getChildren().add(DialogBox.this);
    }
    
    public void show(){
    	Main.getStackRoot().getChildren().add(dialogPane);
    }
    
    public void setSize(double width, double height){
    	setMinSize(width, height);
        setPrefSize(width, height);
        setMaxSize(width, height);
    }
    
}