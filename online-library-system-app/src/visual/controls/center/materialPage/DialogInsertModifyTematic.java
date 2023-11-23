package visual.controls.center.materialPage;


import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import services.TematicServices;
import utils.DialogBox;
import utils.Utilities;
import visual.Main;
import visual.controls.Notification;

public class DialogInsertModifyTematic extends BorderPane {
	private StackPane dialogRoot;
	
	public static final double WIDTH = 300;
	public static final double HEIGHT = 195;
	private static final String INSERT_TEMATIC = "Nueva Temática";
	private static final String MODIFY_TEMATIC = "Modificar Temática";
	
	private Label title;
	private ImageView picture;
	private HBox header;
	private Label lblName;
	private TextField txfName;
	private Label lblCanceled;
	private CheckBox canceled;
	
	private Button btnSave;
	private Button btnCancel;
	private HBox buttons;
	
	public DialogInsertModifyTematic(final int tematicId, final String name, boolean isCanceled){
		setId("dataDialog");
		setMinSize(WIDTH, HEIGHT);
		setPrefSize(WIDTH, HEIGHT);
		setMaxSize(WIDTH, HEIGHT);
		
		if(tematicId == -1)
			title = new Label(INSERT_TEMATIC);
		else
			title = new Label(MODIFY_TEMATIC);
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
		if(name != null)
			txfName = new TextField(name);
		else
			txfName = new TextField();
		txfName.setPrefWidth(110);
		txfName.setPromptText("Nombre");
		lblCanceled = new Label("Cancelar:");
		lblCanceled.getStyleClass().add("bold");
		canceled = new CheckBox();
		canceled.setSelected(isCanceled);
		
		GridPane grid = new GridPane();
		grid.setTranslateX(20);
		grid.setTranslateY(20);
		//grid.setGridLinesVisible(true);
		ObservableList<Node> content = grid.getChildren();
		
		GridPane.setConstraints(lblName, 0, 0);
		GridPane.setMargin(lblName, new Insets(10, 5, 2, 10));
		content.add(lblName);
		GridPane.setConstraints(txfName, 1, 0);
		GridPane.setMargin(txfName, new Insets(10, 10, 2, 5));
		content.add(txfName);
		
		GridPane.setConstraints(lblCanceled, 0, 1);
		GridPane.setMargin(lblCanceled, new Insets(10, 5, 13, 10));
		content.add(lblCanceled);
		GridPane.setConstraints(canceled, 1, 1);
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
					
					if(tematicId == -1){
						try {
							TematicServices.insertTematic(newName, DialogInsertModifyTematic.this.canceled.isSelected());
							Notification.getInstance().setText("Se ha guardado satisfactoriamente la temática " + newName);
							Notification.getInstance().showNotification();
						} catch (SQLException e1) {
							success = false;
							DialogBox diag = new DialogBox("Ha ocurrido un error al guardar los datos");
							diag.show();
						}
					}
					else{
						try {
							TematicServices.modifyTematic(tematicId, newName, DialogInsertModifyTematic.this.canceled.isSelected());
							String text = "";							
							if(!name.equals(newName))
								text = "La temática " + name + " se ha actualizado a " + newName;
							else
								text = "Se han actualizado correctamente los datos de la temática " + newName;
							Notification.getInstance().setText(text);
							Notification.getInstance().showNotification();
						} catch (SQLException e1) {
							success = false;
							DialogBox diag = new DialogBox("Ha ocurrido un error al modificar los datos");
							diag.show();
						}
					}
					
					if(success){
						MaterialViewer.getInstance().putTematics();
						dialogRoot.getChildren().remove(DialogInsertModifyTematic.this);
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
				dialogRoot.getChildren().remove(DialogInsertModifyTematic.this);
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
		dialogRoot.getChildren().add(DialogInsertModifyTematic.this);
	}
	
	public void show(){
		Main.getStackRoot().getChildren().add(dialogRoot);
		txfName.requestFocus();
	}
}
