package visual.controls.right;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import services.NotesServices;
import utils.DialogBox;
import visual.controls.center.notesPane.Note;

public class NotesPane extends VBox {
	private List<Note> notes;
	private Label lblHeader;
	private Note currentPublicNote;
	private int currentIndex;
	private static final Note DEFAULT_NOTE = new Note(-1, "Información", "No se encuentran notas disponibles", "Root", true);
	
	private Button btnNext;
	private Button btnPrev;
	private HBox buttons;
	
	public NotesPane(){
		setId("notesPane");
		setMinWidth(166);
		setPrefWidth(166);
		setMaxWidth(166);
		//setTranslateY(15);
		setSpacing(10);
		
		notes = new LinkedList<Note>();
		
		currentPublicNote = DEFAULT_NOTE;
		currentPublicNote.setTranslateX(15);
		currentPublicNote.setTranslateY(5);
		
		notes.add(currentPublicNote);
		
		lblHeader = new Label("Notas públicas");
		lblHeader.setId("lblNotesHeader");
		lblHeader.getStyleClass().add("lblNotesHeader");
		lblHeader.setMinSize(166, 25);
		lblHeader.setPrefSize(166, 25);
		lblHeader.setMaxSize(166, 25);
		
		buttons = new HBox();
		buttons.setId("buttonsNavi");
		//VBox.setMargin(buttons, new Insets(15, 0, 0, 0));
		
		btnPrev = new Button();
		btnPrev.getStyleClass().add("btnPrev");
		btnPrev.setMinSize(20, 18);
		btnPrev.setPrefSize(20, 18);
		btnPrev.setMaxSize(20, 18);
		btnPrev.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				currentIndex--;
				Note n = getNote(currentIndex);
				currentPublicNote.setNoteId(n.getNoteId());
				currentPublicNote.setHeader(n.getHeader());
				currentPublicNote.setBody(n.getBody());
				currentPublicNote.setFooter(n.getFooter());
			}
		});
		
		btnNext = new Button();
		btnNext.getStyleClass().add("btnNext");
		btnNext.setMinSize(20, 18);
		btnNext.setPrefSize(20, 18);
		btnNext.setMaxSize(20, 18);
		btnNext.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				currentIndex++;
				Note n = getNote(currentIndex);
				currentPublicNote.setNoteId(n.getNoteId());
				currentPublicNote.setHeader(n.getHeader());
				currentPublicNote.setBody(n.getBody());
				currentPublicNote.setFooter(n.getFooter());
			}
		});
		
		Region spacer3 = new Region();
		HBox.setHgrow(spacer3, Priority.ALWAYS);
		
		buttons.getChildren().addAll(btnPrev, spacer3, btnNext);
		
		
		getChildren().addAll(lblHeader, currentPublicNote, buttons);
		
		update();
	}
	
	public void update(){
		List<Note> notes2 = null;
		try{
			notes2 = NotesServices.getPublicNotes();
		} catch (SQLException e) {
			DialogBox diag = new DialogBox("No se pudo obtener una actualización de las notas públicas");
			diag.show();
			e.printStackTrace();
		}
		
		if(notes2 != null){
			notes = notes2;
			Iterator<Note> iterator = notes.iterator();
			
			if(currentPublicNote != null){
				boolean found = false;
				
				currentIndex = 0;
				while(iterator.hasNext() && !found){
					Note n = iterator.next();
					if(n.getNoteId() == currentPublicNote.getNoteId()){
						currentPublicNote.setNoteId(n.getNoteId());
						currentPublicNote.setHeader(n.getHeader());
						currentPublicNote.setBody(n.getBody());
						currentPublicNote.setFooter(n.getFooter());
						found = true;
					}
					else
						currentIndex++;
				}
				
				if(!found){
					iterator = notes.iterator();
					if(iterator.hasNext()){
						Note n = iterator.next();
						currentPublicNote.setNoteId(n.getNoteId());
						currentPublicNote.setHeader(n.getHeader());
						currentPublicNote.setBody(n.getBody());
						currentPublicNote.setFooter(n.getFooter());
						currentIndex = 0;
					}
				}
				
			}
		}
	}
	
	private Note getNote(int index){
		index = index % notes.size();
		if(index < 0)
			index *= -1;
		currentIndex = index;
		return index >= 0 && index < notes.size() ? notes.get(index) : DEFAULT_NOTE;
	}
}