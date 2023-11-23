package visual.controls.center.notesPane;

import java.sql.SQLException;

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
import javafx.scene.text.TextAlignment;
import services.NotesServices;
import utils.DialogBox;
import visual.Main;
import visual.controls.Notification;

public class DialogDeleteNote extends VBox{
	private StackPane dialogPane;
	private String text;
	private static Image picture = new Image(DialogBox.class.getResource("../visual/icons/alert.png").toExternalForm());
	
	public DialogDeleteNote(String text, Note note){
		setId("dialogDeleteNote");
    	this.text = text;
    	dialogPane = new StackPane();
    	dialogPane.setId("dialogRoot");
    	
        setId("dialogo");
        setMinSize(320, 240);
        setPrefSize(320, 240);
        setMaxSize(320, 240);
        
        init(note);
    }
    
    private void init(final Note note){
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
    	
    	HBox noteBox = new HBox();
    	Region spacer3 = new Region();
    	HBox.setHgrow(spacer3, Priority.ALWAYS);
    	Region spacer6 = new Region();
    	HBox.setHgrow(spacer6, Priority.ALWAYS);
    	noteBox.getChildren().addAll(spacer3, note, spacer6);
    	
    	textBox.getChildren().addAll(spacer1, tag, spacer2, noteBox);
    	
    	content.getChildren().add(textBox);
        
        Region separation = new Region();
        VBox.setVgrow(separation, Priority.ALWAYS);
        
        HBox regButton = new HBox();
        
        Region spacer4 = new Region();
        HBox.setHgrow(spacer4, Priority.ALWAYS);
        
        Button accept = new Button("Aceptar");
        accept.setMinSize(80, 20);
        accept.setPrefSize(80, 20);
        accept.setMaxSize(80, 20);
        accept.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e){
        		try{
        			NotesServices.deleteNote(note.getNoteId());
        			NotesViewer.getInstance().setNotes();
        			Notification.getInstance().setText("Su nota ha sido eliminada");
					Notification.getInstance().showNotification();
        		} catch(SQLException e1){
        			DialogBox diag = new DialogBox("Ha ocurrido un error al eliminar la nota");
        			diag.show();
        		}
        		dialogPane.getChildren().remove(DialogDeleteNote.this);
        		if(dialogPane.getChildren().isEmpty())
        			dialogPane.setVisible(false);
        		
        	}
        });
        
        Region spacer5 = new Region();
        spacer5.setMinWidth(12);
        spacer5.setPrefWidth(12);
        spacer5.setMaxWidth(12);
        
        Button close = new Button("Cancelar");
        close.setDefaultButton(true);
        close.setMinSize(80, 20);
        close.setPrefSize(80, 20);
		close.setMaxSize(80, 20);
		close.setTextAlignment(TextAlignment.CENTER);
        close.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e){
        		dialogPane.getChildren().remove(DialogDeleteNote.this);
        		if(dialogPane.getChildren().isEmpty())
        			dialogPane.setVisible(false);
        	}
        }); 
        
        regButton.getChildren().addAll(spacer4,accept, spacer5, close);
        
        getChildren().addAll(content, separation, regButton);
        
        dialogPane.getChildren().add(DialogDeleteNote.this);
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