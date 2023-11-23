package visual.controls.center.materialPage;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import services.MaterialServices;
import services.SubjectServices;
import services.TematicServices;
import utils.DialogBox;
import visual.Main;
import visual.controls.Notification;

public class DialogCancelTematicSubjectMaterial extends BorderPane {
	private StackPane dialogRoot;
	
	public static final double WIDTH = 350;
	public static final double HEIGHT = 215;
	public static final String CANCEL_TEMATIC = "Cancelar Temática";
	public static final String CANCEL_SUBJECT = "Cancelar Asignatura";
	public static final String CANCEL_MATERIAL = "Cancelar Material";

	private Label title;
	private ImageView picture;
	private HBox header;
	private Label lblText;
	private ListView<String> listView;
	private String names = "";
	private VBox centerBox;
	
	private Button btnSave;
	private Button btnCancel;
	private HBox buttons;
	
	public DialogCancelTematicSubjectMaterial(final TypeItem type, final List<Item> items, final int neccesaryId, final boolean isBySelect){
		setId("dataDialog");
		setMinSize(WIDTH, HEIGHT);
		setPrefSize(WIDTH, HEIGHT);
		setMaxSize(WIDTH, HEIGHT);
		
		title = new Label();
		title.getStyleClass().add("title");
		
		lblText = new Label();
		lblText.setWrapText(true);
		
		if(type.equals(TypeItem.TEMATIC)){
			title.setText(CANCEL_TEMATIC);
			lblText.setText("¿Realmente desea cancelar/habilitar la(s) temática(s)?");
		}
		else if(type.equals(TypeItem.SUBJECT)){
			title.setText(CANCEL_SUBJECT);
			lblText.setText("¿Realmente desea cancelar/habilitar la(s) asignatura(s)?");
		}
		else if(type.equals(TypeItem.MATERIAL)){
			title.setText(CANCEL_MATERIAL);
			lblText.setText("¿Realmente desea cancelar/habilitar el(los) material(es)?");
		}
		
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
		
		centerBox = new VBox();
		centerBox.setSpacing(7);
		
		listView = new ListView<String>();
		Iterator<Item> iterator = items.iterator();
		while(iterator.hasNext()){
			Item it = iterator.next();
			listView.getItems().add(it.getName());
			names += it.getName();
			if(iterator.hasNext())
				names += ", ";
		}
		
		Region spacer = new Region();
		spacer.setMinHeight(15);
		spacer.setPrefHeight(15);
		spacer.setMaxHeight(15);
		
		centerBox.getChildren().addAll(lblText, listView, spacer);
		
		setCenter(centerBox);
		
		btnSave = new Button("Aceptar");
		btnSave.setMinSize(80, 20);
		btnSave.setPrefSize(80, 20);
		btnSave.setMaxSize(80, 20);
		btnSave.setDefaultButton(true);
		btnSave.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){				
				for(Item it : items){
					try{
						if( (!isBySelect && it.isCanceled()) || (isBySelect && !it.isCanceled())){
							if(type.equals(TypeItem.TEMATIC)){
								TematicServices.cancelTematic(it.getItemId());
							}
							else if(type.equals(TypeItem.SUBJECT)){
								SubjectServices.cancelSubject(it.getItemId());
							}
							else  if(type.equals(TypeItem.MATERIAL)){
								MaterialServices.cancelMaterial(it.getItemId());
							}
						}
						else{
							if(type.equals(TypeItem.TEMATIC)){
								TematicServices.enabledTematic(it.getItemId());
							}
							else if(type.equals(TypeItem.SUBJECT)){
								SubjectServices.enabledSubject(it.getItemId());
							}
							else  if(type.equals(TypeItem.MATERIAL)){
								MaterialServices.enabledMaterial(it.getItemId());
							}
						}
					}
					catch(SQLException e1){
						DialogBox diag = new DialogBox("Ha ocurrido un error al modificar los datos");
						diag.show();
					}
				}
				
				int size = items.size();
				if(type.equals(TypeItem.TEMATIC)){
					if(size == 1)
						Notification.getInstance().setText("Se ha cancelado/habilitado la temática " + names);
					else
						Notification.getInstance().setText("Se han cancelado/habilitado las temáticas " + names);
					Notification.getInstance().showNotification();
					MaterialViewer.getInstance().putTematics();
				}
				else if(type.equals(TypeItem.SUBJECT)){
					if(size == 1)
						Notification.getInstance().setText("Se ha cancelado/habilitado la asignatura " + names);
					else
						Notification.getInstance().setText("Se han cancelado/habilitado las asignaturas " + names);
					Notification.getInstance().showNotification();
					MaterialViewer.getInstance().putSubjects(neccesaryId, "");
				}
				else  if(type.equals(TypeItem.MATERIAL)){
					if(size == 1)
						Notification.getInstance().setText("Se ha cancelado/habilitado el material " + names);
					else
						Notification.getInstance().setText("Se han cancelado/habilitado los materiales " + names);
					Notification.getInstance().showNotification();
					MaterialViewer.getInstance().putMaterials(neccesaryId, "");
				}
				
				dialogRoot.getChildren().remove(DialogCancelTematicSubjectMaterial.this);
				Main.getStackRoot().getChildren().remove(dialogRoot);
			}
		});
		
		btnCancel = new Button("Cancelar");
		btnCancel.setMinSize(80, 20);
		btnCancel.setPrefSize(80, 20);
		btnCancel.setMaxSize(80, 20);
		btnCancel.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				dialogRoot.getChildren().remove(DialogCancelTematicSubjectMaterial.this);
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
		dialogRoot.getChildren().add(DialogCancelTematicSubjectMaterial.this);
		
	}
	
	public void show(){
		Main.getStackRoot().getChildren().add(dialogRoot);
	}
}

