package visual.controls.center.materialPage;

import java.sql.SQLException;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.Tematic;
import services.SubjectServices;
import services.TematicServices;
import utils.DialogBox;
import utils.GUI;
import utils.Utilities;
import visual.Main;
import visual.controls.Notification;

public class DialogInsertModifySubject extends BorderPane {
	private StackPane dialogRoot;
	
	public static final double WIDTH = 300;
	public static final double HEIGHT = 235;
	private static final String INSERT_SUBJECT = "Nueva Asignatura";
	private static final String MODIFY_SUBJECT = "Modificar Asignatura";
	
	private Label title;
	private ImageView picture;
	private HBox header;
	private Label lblName;
	private TextField txfName;
	private Label lblCanceled;
	private CheckBox canceled;
	private Label lblTematics;
	private ChoiceBox<Tematic> choiceTematic;
	
	private Button btnSave;
	private Button btnCancel;
	private HBox buttons;
	

	
	public DialogInsertModifySubject(final int tematicId, final int subjectId, final String name, boolean isCanceled){
		setId("dataDialog");
		setMinSize(WIDTH, HEIGHT);
		setPrefSize(WIDTH, HEIGHT);
		setMaxSize(WIDTH, HEIGHT);
		
		if(subjectId == -1)
			title = new Label(INSERT_SUBJECT);
		else
			title = new Label(MODIFY_SUBJECT);
		title.getStyleClass().add("title");
		
		Region spacer1 = new Region();
		spacer1.setMinHeight(25);
		spacer1.setPrefHeight(25);
		spacer1.setMaxHeight(25);
		
		VBox titleBox = new VBox();
		titleBox.getChildren().addAll(spacer1, title);
		
		picture = new ImageView(new Image(getClass().getResource("../../../icons/academicData57x62.png").toExternalForm()));
		
		header = new HBox();
		header.setSpacing(15);
		header.getChildren().addAll(picture, titleBox);
		
		setTop(header);
		
		
		lblName = new Label("Nombre: ");
		lblName.getStyleClass().add("bold");
		if(name == null)
			txfName = new TextField();
		else
			txfName = new TextField(name);
		txfName.setPrefWidth(110);
		txfName.setPromptText("Nombre");
		lblCanceled = new Label("Cancelar:");
		lblCanceled.getStyleClass().add("bold");
		canceled = new CheckBox();
		canceled.setSelected(isCanceled);
		
		lblTematics = new Label("Temáticas");
		List<Tematic> tems = null;
		try{
			if(GUI.getInstance().getUser() != null && GUI.getInstance().getUser().getPrivilege().isModifyTematic())
				tems = TematicServices.getTematics();
			else
				tems = TematicServices.getEnabledTematics();
		}
		catch (SQLException e) {
			
		}
		choiceTematic = new ChoiceBox<Tematic>();
		if(tems != null){
			choiceTematic.getItems().addAll(tems);
			choiceTematic.getSelectionModel().select(Tematic.find(tems, tematicId));
		}
		
		GridPane grid = new GridPane();
		grid.setTranslateX(20);
		grid.setTranslateY(20);
		//grid.setGridLinesVisible(true);
		ObservableList<Node> content = grid.getChildren();
		
		GridPane.setConstraints(lblTematics, 0, 0);
		GridPane.setMargin(lblTematics, new Insets(10, 5, 2, 10));
		content.add(lblTematics);
		GridPane.setConstraints(choiceTematic, 1, 0);
		GridPane.setMargin(choiceTematic, new Insets(10, 10, 2, 5));
		content.add(choiceTematic);
		
		GridPane.setConstraints(lblName, 0, 1);
		GridPane.setMargin(lblName, new Insets(10, 5, 2, 10));
		content.add(lblName);
		GridPane.setConstraints(txfName, 1, 1);
		GridPane.setMargin(txfName, new Insets(10, 10, 2, 5));
		content.add(txfName);
		
		GridPane.setConstraints(lblCanceled, 0, 2);
		GridPane.setMargin(lblCanceled, new Insets(10, 5, 13, 10));
		content.add(lblCanceled);
		GridPane.setConstraints(canceled, 1, 2);
		GridPane.setMargin(canceled, new Insets(10, 10, 13, 5));
		content.add(canceled);
		
		setCenter(grid);
		
		btnSave = new Button("Guardar");
		btnSave.setMinSize(80, 20);
		btnSave.setPrefSize(80, 20);
		btnSave.setMaxSize(80, 20);
		btnSave.setDefaultButton(true);
		btnSave.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){				
				String newName = txfName.getText();
				if(newName == null || newName.isEmpty() || Utilities.isEmpty(newName)){
					DialogBox diag = new DialogBox("El campo \"nombre\" no debe estar vacío");
					diag.show();
				}
				else{
					boolean success = true;
					
					int newTematicId = choiceTematic.getSelectionModel().getSelectedItem().getId();
					
					if(subjectId == -1){
						try {
							SubjectServices.insertSubject(newName, DialogInsertModifySubject.this.canceled.isSelected(), newTematicId);
							Notification.getInstance().setText("Se ha guardado satisfactoriamente la asignatura " + newName);
							Notification.getInstance().showNotification();
						} catch (SQLException e1) {
							success = false;
							DialogBox diag = new DialogBox("Ha ocurrido un error al guardar los datos");
							diag.show();
						}
					}
					else{
						try {
							SubjectServices.modifySubject(subjectId, newName, DialogInsertModifySubject.this.canceled.isSelected(), newTematicId);
							String text = "";							
							if(!name.equals(newName))
								text = "La asignatura " + name + " se ha actualizado a " + newName;
							else
								text = "Se han actualizado correctamente los datos de la asignatura " + newName;
							Notification.getInstance().setText(text);
							Notification.getInstance().showNotification();
						} catch (SQLException e1) {
							success = false;
							DialogBox diag = new DialogBox("Ha ocurrido un error al modificar los datos");
							diag.show();
						}
					}
					
					if(success){
						MaterialViewer.getInstance().putSubjects(tematicId, "");
						dialogRoot.getChildren().remove(DialogInsertModifySubject.this);
						Main.getStackRoot().getChildren().remove(dialogRoot);
					}
				}
			}
		});
		
		btnCancel = new Button("Cancelar");
		btnCancel.setMinSize(80, 20);
		btnCancel.setPrefSize(80, 20);
		btnCancel.setMaxSize(80, 20);
		btnCancel.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				dialogRoot.getChildren().remove(DialogInsertModifySubject.this);
				Main.getStackRoot().getChildren().remove(dialogRoot);
			}
		});
		
		Region spacer2 = new Region();
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		
		buttons = new HBox();
		buttons.setSpacing(7);
		buttons.getChildren().addAll(spacer2, btnSave, btnCancel);
		
		setBottom(buttons);
		
		dialogRoot = new StackPane();
		dialogRoot.setId("dialogRoot");
		dialogRoot.getChildren().add(DialogInsertModifySubject.this);
	}
	
	public void show(){
		Main.getStackRoot().getChildren().add(dialogRoot);
		txfName.requestFocus();
	}
}
