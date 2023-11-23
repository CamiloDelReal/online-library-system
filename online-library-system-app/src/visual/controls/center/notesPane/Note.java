package visual.controls.center.notesPane;


import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Note extends VBox implements Cloneable{
	private int id;
	private Label lblTitle;
	private Label lblText;
	private Label lblAuthor;
	private boolean isPublic;
	
	public Note(int id, String title, String text, String author, boolean isPublic){
		setId("note");
		setMinSize(136, 136);
		setPrefSize(136, 136);
		setMaxSize(136, 136);
		this.id = id;
		this.isPublic = isPublic;
		
		lblTitle = new Label(title);
		lblTitle.getStyleClass().add("title");
		lblTitle.setFont(Font.font("Calibri", FontWeight.BOLD, 14));
		
		HBox spacerAndText = new HBox();
		
		Region spacer1 = new Region();
		spacer1.setMinWidth(15);
		spacer1.setPrefWidth(15);
		spacer1.setMaxWidth(15);
		
		lblText = new Label(text);
		lblText.setFont(Font.font("Calibri", FontWeight.NORMAL, 13));
		lblText.setWrapText(true);
		lblText.getStyleClass().add("lblText");
		
		spacerAndText.getChildren().addAll(spacer1, lblText);
		
		HBox spacerAndAuthor = new HBox();
		
		Region spacer2 = new Region();
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		
		lblAuthor = new Label(author);
		lblAuthor.setFont(Font.font("Calibri", FontWeight.NORMAL, 13));
		
		spacerAndAuthor.getChildren().addAll(spacer2, lblAuthor);
		
		Region spacer3 = new Region();
		VBox.setVgrow(spacer3, Priority.ALWAYS);
		Region spacer4 = new Region();
		VBox.setVgrow(spacer4, Priority.ALWAYS);
		
		getChildren().addAll(lblTitle, spacer3, lblText, spacer4, spacerAndAuthor);
	}
	
	public void setHeaderText(String text){
		lblTitle.setText(text);
	}
	public void setBodyText(String text){
		lblText.setText(text);
	}
	public void setFooterText(String text){
		lblAuthor.setText(text);
	}
	
	public int getNoteId(){
		return id;
	}
	public boolean isPublic(){
		return isPublic;
	}
	public String getHeader(){
		return lblTitle.getText();
	}
	public String getBody(){
		return lblText.getText();
	}
	public String getFooter(){
		return lblAuthor.getText();
	}
	
	public void setNoteId(int id){
		this.id = id;
	}
	public void setHeader(String header){
		lblTitle.setText(header);
	}
	public void setBody(String body){
		lblText.setText(body);
	}
	public void setFooter(String footer){
		lblAuthor.setText(footer);
	}
	public void setIsPublic(boolean isPublic){
		this.isPublic = isPublic;
	}
	
	public Note clone(){
		return new Note(id, lblTitle.getText(), lblText.getText(), lblAuthor.getText(), isPublic);		
	}
}
