package visual.controls.center.userCountPage;

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
import models.ResearchGroupWithProperty;
import models.ScientificCategoryWithProperty;
import models.TeachingCategoryWithProperty;
import models.TeachingGroupWithProperty;
import services.ResearchGroupServices;
import services.ScientificCategoryServices;
import services.TeachingCategoryServices;
import services.TeachingGroupServices;
import utils.DialogBox;
import utils.Utilities;
import visual.Main;
import visual.controls.Notification;


@SuppressWarnings("incomplete-switch")
public class InsertModifyAcademicDataDialog extends BorderPane {
	private StackPane dialogRoot;
	
	private static InsertModifyAcademicDataDialog dialog;
	
	private TypeOfInsertModifyDelete type;
	private boolean insert = true;
	private Object entity;
	
	private Label title;
	private Label title2;
	private ImageView picture;
	private HBox header;
	
	private Label lblName;
	private TextField txfName;
	private Label lblCanceled;
	private CheckBox canceled;
	
	private Button btnSave;
	private Button btnCancel;
	private HBox buttons;
	
	private static final String SCIENTIFIC_CATEGORY = "\tCategoría Científica";
	private static final String TEACHING_CATEGORY = "\tCategoría Docente";
	private static final String TEACHING_GROUP = "\tGrupo Docente";
	private static final String RESEARCH_GROUP = "\tGrupo Investigativo";
	private static final String NEW_DATA = "\t\t\t\t\tNuevo dato académico";
	private static final String MODIFY_DATA = "\t\t\t\t\tModificación";
	
	public static InsertModifyAcademicDataDialog getInstance(){
		if(dialog == null)
			dialog = new InsertModifyAcademicDataDialog();
		
		return dialog;
	}
	
	private InsertModifyAcademicDataDialog(){
		setId("dataDialog");
		
		setMinSize(370, 195);
		setPrefSize(370, 195);
		setMaxSize(370, 195);
		
		title = new Label();
		title.getStyleClass().add("title");
		title2 = new Label();
		title2.getStyleClass().add("title2");
		
		Region spacer1 = new Region();
		spacer1.setMinHeight(15);
		spacer1.setPrefHeight(15);
		spacer1.setMaxHeight(15);
		
		VBox titleBox = new VBox();
		titleBox.getChildren().addAll(spacer1, title, title2);
		
		picture = new ImageView(new Image(getClass().getResource("../../../icons/academicData57x62.png").toExternalForm()));
		
		header = new HBox();
		header.setSpacing(5);
		header.getChildren().addAll(picture, titleBox);
		
		setTop(header);
		
		lblName = new Label("Nombre: ");
		lblName.getStyleClass().add("bold");
		txfName = new TextField();
		txfName.setPrefWidth(110);
		txfName.setPromptText("Nombre");
		lblCanceled = new Label("Cancelar:");
		lblCanceled.getStyleClass().add("bold");
		canceled = new CheckBox();
		
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
				String name = txfName.getText();
				if(name == null || name.isEmpty() || Utilities.isEmpty(name)){
					DialogBox diag = new DialogBox("El campo \"Nombre\" no debe estar vacio");
					diag.show();
				}
				else{
					boolean isCanceled = canceled.isSelected();
					
					if(insert){
						try{
							String text = "Se ha insertado correctamente ";
							switch(type){
								case TEACHING_GROUP:
									TeachingGroupServices.insertTeachingGroup(isCanceled, name);
									text += "el grupo docente "+name;
									break;
								case RESEARCH_GROUP:
									ResearchGroupServices.insertResearchGroup(isCanceled, name);
									text += "el grupo investigativo "+name;
									break;
								case SCIENTIFIC_CATEGORY:
									ScientificCategoryServices.insertScientificCategory(isCanceled, name);
									text += "la categoría científica "+name;
									break;
								case TEACHING_CATEGORY:
									TeachingCategoryServices.insertTeachingCategory(isCanceled, name);
									text += "la categoría docente "+name;
									break;
							}
							Notification.getInstance().setText(text);
							Notification.getInstance().showNotification();
						} catch (SQLException e1) {
							DialogBox diag = new DialogBox(e1.getMessage());
							diag.show();
						}
					}
					else{
						try{
							String text = "Se ha modificado correctamente ";
							switch(type){
								
								case TEACHING_GROUP:
									TeachingGroupWithProperty tg = (TeachingGroupWithProperty) entity;
									TeachingGroupServices.modifyTeachingGroup(tg.idProperty().get(), isCanceled, name);
									text += "el grupo docente.";
									break;
								case RESEARCH_GROUP:
									ResearchGroupWithProperty rg = (ResearchGroupWithProperty) entity;
									ResearchGroupServices.modifyResearchGroup(rg.idProperty().get(), isCanceled, name);
									text += "el grupo investigativo.";
									break;
								case SCIENTIFIC_CATEGORY:
									ScientificCategoryWithProperty sc = (ScientificCategoryWithProperty) entity;
									ScientificCategoryServices.modifyScientificCategory(sc.idProperty().get(), isCanceled, name);
									text += "la categoría cientifica.";
									break;
								case TEACHING_CATEGORY:
									TeachingCategoryWithProperty tc = (TeachingCategoryWithProperty) entity;
									TeachingCategoryServices.modifyTeachingCategory(tc.idProperty().get(), isCanceled, name);
									text += "la categoría docente.";
									break;
							}
							Notification.getInstance().setText(text);
							Notification.getInstance().showNotification();
						} catch (SQLException e1) {
							DialogBox diag = new DialogBox(e1.getMessage());
							diag.show();
						}
					}
					
					Main.getStackRoot().getChildren().remove(dialogRoot);
					AcademicDataPane.getInstance().update(type);
				}
			}
		});
		
		btnCancel = new Button("Cancelar");
		btnCancel.setMinSize(80, 20);
		btnCancel.setPrefSize(80, 20);
		btnCancel.setMaxSize(80, 20);
		btnCancel.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				//dialogRoot.getChildren().remove(InsertAcademicDataDialog.this);
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
		dialogRoot.getChildren().add(InsertModifyAcademicDataDialog.this);
	}
	
	public void setType(TypeOfInsertModifyDelete type, boolean insert){
		switch(type){
			case TEACHING_GROUP:
				title.setText(TEACHING_GROUP);
				this.type = type;
				break;
			case RESEARCH_GROUP:
				title.setText(RESEARCH_GROUP);
				this.type = type;
				break;
			case SCIENTIFIC_CATEGORY:
				title.setText(SCIENTIFIC_CATEGORY);
				this.type = type;
				break;
			case TEACHING_CATEGORY:
				title.setText(TEACHING_CATEGORY);
				this.type = type;
				break;
		}
		
		this.insert = insert;
		if(this.insert)
			title2.setText(NEW_DATA);
		else
			title2.setText(MODIFY_DATA);
	}
	
	public void setEntity(Object obj){
		this.entity = obj;
	}
	
	public void show(){
		if(!insert && entity != null){
			switch(type){
				case TEACHING_GROUP:
					TeachingGroupWithProperty tg = (TeachingGroupWithProperty) entity;
					txfName.setText(tg.nameProperty().get());
					canceled.setSelected(tg.canceledProperty().get());
					break;
				case RESEARCH_GROUP:
					ResearchGroupWithProperty rg = (ResearchGroupWithProperty) entity;
					txfName.setText(rg.nameProperty().get());
					canceled.setSelected(rg.canceledProperty().get());
					break;
				case SCIENTIFIC_CATEGORY:
					ScientificCategoryWithProperty sc = (ScientificCategoryWithProperty) entity;
					txfName.setText(sc.nameProperty().get());
					canceled.setSelected(sc.canceledProperty().get());
					break;
				case TEACHING_CATEGORY:
					TeachingCategoryWithProperty tc = (TeachingCategoryWithProperty) entity;
					txfName.setText(tc.nameProperty().get());
					canceled.setSelected(tc.canceledProperty().get());
					break;
			}
		}
		else{
			canceled.setSelected(false);
			txfName.setText("");
		}
		Main.getStackRoot().getChildren().add(dialogRoot);
		txfName.requestFocus();
	}
}
