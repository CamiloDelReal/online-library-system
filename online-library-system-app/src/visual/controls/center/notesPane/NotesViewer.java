package visual.controls.center.notesPane;

import java.sql.SQLException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import services.NotesServices;
import utils.DialogBox;
import utils.GUI;
import utils.OpButton;
import utils.Utilities;
import utils.ValidationErrorException;
import visual.controls.Notification;

public class NotesViewer extends BorderPane {
	private static NotesViewer pane = null;
	public static NotesViewer getInstance(){
		if(pane == null)
			pane = new NotesViewer();
		return pane;
	}
	
	private Label title;
	
	private boolean hasDoNotesPublic;
	
	public void canPublicNotes(boolean can){
		hasDoNotesPublic = can;
		togglePublic.setSelected(false);
		if(can){
			if(!boxPreview.getChildren().contains(boxButtonPublic))
			boxPreview.getChildren().add(boxButtonPublic);
		}
		else
			boxPreview.getChildren().remove(boxButtonPublic);
	}
	
	private VBox centerRoot;
	private GridPane entryDataRoot;
	private HBox dataAndPreviewRoot;
	private Label lblHeader;
	private TextField txfHeader;
	private Label lblBody;
	private TextArea txtBody;
	private Label lblFooter;
	private TextField txfFooter;
	private Note notePreview;
	private ToggleButton togglePublic;
	private VBox boxPreview;
	private HBox boxButtonPublic;
	
	private OpButton btnInsert;
	private OpButton btnModify;
	private OpButton btnDelete;
	private HBox buttons;
	
	private ListView<Note> listNotes;
	
	private int idSelected;
	
	private NotesViewer(){
		setId("notesViewer");
		
		title = new Label("Mis notas");
		title.getStyleClass().add("title");
		setTop(title);
		
		
		btnInsert = new OpButton("Insertar");
		btnInsert.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				try {
					saveNote();
					setDefault();
					Notification.getInstance().setText("Nueva nota añadida");
					Notification.getInstance().showNotification();
				} catch (ValidationErrorException | SQLException e1) {
					DialogBox diag = new DialogBox(e1.getMessage());
					diag.show();
				}
			}
		});
		btnModify = new OpButton("Modificar");
		btnModify.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				if(btnModify.getText().equals("Modificar")){
					Note selected = listNotes.getSelectionModel().getSelectedItem();
					if(selected != null){
						idSelected = selected.getNoteId();
						btnInsert.setDisable(true);
						btnModify.setText("Guardar");
						btnDelete.setText("Cancelar");
						txfHeader.setText(selected.getHeader());
						txtBody.setText(selected.getBody());
						txfFooter.setText(selected.getFooter());
						notePreview.setHeaderText(selected.getHeader());
						notePreview.setBodyText(selected.getBody());
						notePreview.setFooterText(selected.getFooter());
						togglePublic.setSelected(selected.isPublic());
					}
					else{
						DialogBox diag = new DialogBox("Debe seleccionar una nota");
						diag.show();
					}
				}
				else{
					boolean success = true;
					try {
						modifyNote(idSelected);
						Notification.getInstance().setText("Su nota ha sido modificada");
						Notification.getInstance().showNotification();
					} catch (ValidationErrorException | SQLException e1) {
						success = false;
						DialogBox diag = new DialogBox("Ha ocurrido un error al modificar su nota");
						diag.show();
					}
					
					if(success){
						setDefault();
						btnInsert.setDisable(false);
						btnModify.setText("Modificar");
						btnDelete.setText("Eliminar");
					}
				}
			}
		});
		btnDelete = new OpButton("Eliminar");
		btnDelete.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				if(btnDelete.getText().equals("Eliminar")){
					Note note = listNotes.getSelectionModel().getSelectedItem();
					if(note != null){
						Note copy = note.clone();
						DialogDeleteNote diag = new DialogDeleteNote("¿Realmente desea eliminar esta nota?", copy);
						diag.show();
					}
					else{
						DialogBox diag = new DialogBox("Debe seleccionar una nota de la lista");
						diag.show();
					}
				}
				else{
					setDefault();
					btnInsert.setDisable(false);
					btnModify.setText("Modificar");
					btnDelete.setText("Eliminar");
				}
			}
		});
		Region spacer1 = new Region();
		HBox.setHgrow(spacer1, Priority.ALWAYS);
		buttons = new HBox();
		buttons.setId("buttonsPane");
		buttons.setSpacing(10);
		Region spacer11 = new Region();
		spacer11.setMinWidth(4);
		buttons.getChildren().addAll(spacer1, btnInsert, btnModify, btnDelete, spacer11);
		
		
		lblHeader = new Label("Encabezado");
		txfHeader = new TextField(){
			public void replaceText(int start, int end, String text) {
		        super.replaceText(start, end, text);
		        notePreview.setHeaderText(txfHeader.getText());
		    }
		 
		    public void replaceSelection(String text) {
		    	super.replaceSelection(text);
		    	notePreview.setHeaderText(txfHeader.getText());
		    }
		};
		txfHeader.setPrefWidth(150);
		
		lblBody = new Label("Cuerpo");
		txtBody = new TextArea(){
			public void replaceText(int start, int end, String text) {
		        super.replaceText(start, end, text);
		        notePreview.setBodyText(txtBody.getText());
		    }
		 
		    public void replaceSelection(String text) {
		    	super.replaceSelection(text);
		    	notePreview.setBodyText(txtBody.getText());
		    }
		};
		txtBody.setPrefWidth(150);
		txtBody.setPrefHeight(80);
		txtBody.setWrapText(true);
		
		lblFooter = new Label("Pie de nota");
		txfFooter = new TextField(){
			public void replaceText(int start, int end, String text) {
		        super.replaceText(start, end, text);
		        notePreview.setFooterText(txfFooter.getText());
		    }
		 
		    public void replaceSelection(String text) {
		    	super.replaceSelection(text);
		    	notePreview.setFooterText(txfFooter.getText());
		    }
		};
		txfFooter.setPrefWidth(150);
		
		entryDataRoot = new GridPane();
		
		GridPane.setConstraints(lblHeader, 0, 0);
		GridPane.setMargin(lblHeader, new Insets(10, 5, 2, 10));
		entryDataRoot.getChildren().add(lblHeader);
		GridPane.setConstraints(txfHeader, 1, 0);
		GridPane.setMargin(txfHeader, new Insets(10, 10, 2, 5));
		entryDataRoot.getChildren().add(txfHeader);
		
		GridPane.setConstraints(lblBody, 0, 1);
		GridPane.setMargin(lblBody, new Insets(10, 5, 2, 10));
		entryDataRoot.getChildren().add(lblBody);
		GridPane.setConstraints(txtBody, 1, 1);
		GridPane.setMargin(txtBody, new Insets(10, 10, 2, 5));
		entryDataRoot.getChildren().add(txtBody);
		
		GridPane.setConstraints(lblFooter, 0, 2);
		GridPane.setMargin(lblFooter, new Insets(10, 5, 2, 10));
		entryDataRoot.getChildren().add(lblFooter);
		GridPane.setConstraints(txfFooter, 1, 2);
		GridPane.setMargin(txfFooter, new Insets(10, 10, 2, 5));
		entryDataRoot.getChildren().add(txfFooter);
		
		Region spacer5 = new Region();
		HBox.setHgrow(spacer5, Priority.ALWAYS);
		Region spacer6 = new Region();
		HBox.setHgrow(spacer6, Priority.ALWAYS);
		Region spacer7 = new Region();
		HBox.setHgrow(spacer7, Priority.ALWAYS);
		
		notePreview = new Note(-1, "", "", "", false);
		
		togglePublic = new ToggleButton("Publicar");
		
		boxPreview = new VBox();
		boxPreview.setSpacing(7);
		
		boxButtonPublic = new HBox();
		Region spacer9 = new Region();
		HBox.setHgrow(spacer9, Priority.ALWAYS);
		Region spacer10 = new Region();
		HBox.setHgrow(spacer10, Priority.ALWAYS);
		boxButtonPublic.getChildren().addAll(spacer9, togglePublic, spacer10);
		
		if(hasDoNotesPublic){
			boxPreview.getChildren().addAll(notePreview, boxButtonPublic);
		}
		else{
			boxPreview.getChildren().add(notePreview);
		}
		
		Region spacer8 = new Region();
		HBox.setHgrow(spacer8, Priority.ALWAYS);
		
		dataAndPreviewRoot = new HBox();
		dataAndPreviewRoot.getChildren().addAll(spacer5, entryDataRoot, spacer6, boxPreview, spacer8);
		
		centerRoot = new VBox();
		
		Region spacer2 = new Region();
		VBox.setVgrow(spacer2, Priority.ALWAYS);
		Region spacer3 = new Region();
		VBox.setVgrow(spacer3, Priority.ALWAYS);
		
		centerRoot.getChildren().addAll(spacer2, dataAndPreviewRoot, spacer3, buttons);
		
		setCenter(centerRoot);
		
		listNotes = new ListView<Note>();
		listNotes.setOrientation(Orientation.HORIZONTAL);
		listNotes.setMinHeight(170);
		listNotes.setPrefHeight(170);
		listNotes.setMaxHeight(170);
		
		setBottom(listNotes);
	}
	
	public void setNotes(){
		setDefault();
		
		List<Note> notes = null;
		try{
			notes = NotesServices.getNotesByUser(GUI.getInstance().getUser().getId());
		} catch(SQLException e){
			DialogBox diag = new DialogBox("Ha ocurrido un error al obtener sus notas");
			diag.show();
			e.printStackTrace();
		}
		if(notes != null){
			listNotes.getItems().removeAll(listNotes.getItems());
			listNotes.getItems().addAll(notes);
		}
	}
	
	private void setDefault(){
		txfHeader.setText("");
		txtBody.setText("");
		txfFooter.setText("");
		notePreview.setHeaderText("");
		notePreview.setBodyText("");
		notePreview.setFooterText("");
		togglePublic.setSelected(false);
	}
	
	private void saveNote() throws ValidationErrorException, SQLException{
		String header = txfHeader.getText();
		
		String body = txtBody.getText();
		if(body == null || body.isEmpty() || Utilities.isEmpty(body))
			throw new ValidationErrorException("Debe escribir el cuerpo de la nota");
		
		String footer = txfFooter.getText();
		if(footer == null || footer.isEmpty() || Utilities.isEmpty(footer))
			throw new ValidationErrorException("Debe excribir un pie de nota,\nbien podrían ser los autores");
		
		
		int noteId = NotesServices.insertNote(GUI.getInstance().getUser().getId(),
				header, body, footer, togglePublic.isSelected());
		
		Note note = new Note(noteId, header, body, footer, togglePublic.isSelected());
		listNotes.getItems().add(note);
	}
	
	private void modifyNote(int noteId) throws ValidationErrorException, SQLException{
		String header = txfHeader.getText();
		
		String body = txtBody.getText();
		if(body == null || body.isEmpty() || Utilities.isEmpty(body))
			throw new ValidationErrorException("Debe escribir el cuerpo de la nota");
		
		String footer = txfFooter.getText();
		if(footer == null || footer.isEmpty() || Utilities.isEmpty(footer))
			throw new ValidationErrorException("Debe excribir un pie de nota,\nbien podrían ser los autores");
		
		NotesServices.modifyNote(noteId, header, body, footer, togglePublic.isSelected());
		
		setNotes();
	}
}
