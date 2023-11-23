package visual.controls.center.userCountPage;

import java.sql.SQLException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import models.ResearchGroupWithProperty;
import models.ScientificCategoryWithProperty;
import models.TeachingCategoryWithProperty;
import models.TeachingGroupWithProperty;
import services.ResearchGroupServices;
import services.ScientificCategoryServices;
import services.TeachingCategoryServices;
import services.TeachingGroupServices;
import utils.CustomTableColumn;
import utils.CustomTableView;
import utils.DialogBox;
import utils.Utilities;

enum TypeOfInsertModifyDelete {
	SCIENTIFIC_CATEGORY,
	TEACHING_CATEGORY,
	TEACHING_GROUP,
	RESEARCH_GROUP
};

public class AcademicDataPane extends AnchorPane{
	private static AcademicDataPane pane;
	
	private TeachingGroupRegion teachingGroup;
	private ResearchGroupRegion researchGroup;
	private TeachingCategoryRegion teachingCategory;
	private ScientificCategoryRegion scientificCategory;
	private Node current;
	
	private ToggleGroup toggleGroup;
	private ToggleButton toggleTeachingGroup;
	private ToggleButton toggleResearchGroup;
	private ToggleButton toggleTeachingCategory;
	private ToggleButton toggleScientificCategory;
	private HBox buttons;
	
	public static AcademicDataPane getInstance(){
		if(pane == null)
			pane = new AcademicDataPane();
		return pane;
	}
	
	private AcademicDataPane(){
		setId("academicDataPane");
		
		teachingGroup = new TeachingGroupRegion();
		researchGroup = new ResearchGroupRegion();
		teachingCategory = new TeachingCategoryRegion();
		scientificCategory = new ScientificCategoryRegion();
		
		toggleGroup = new ToggleGroup();
		
		toggleTeachingGroup = new ToggleButton("Grupos Docentes");
		toggleTeachingGroup.setToggleGroup(toggleGroup);
		toggleTeachingGroup.getStyleClass().add("menuAcademic");
		toggleTeachingGroup.setSelected(true);
		toggleTeachingGroup.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				toggleTeachingGroup.setSelected(true);
				getChildren().remove(current);
				current = teachingGroup;
				getChildren().add(teachingGroup);
			}
		});
		
		toggleResearchGroup = new ToggleButton("Grupos investigativos");
		toggleResearchGroup.setToggleGroup(toggleGroup);
		toggleResearchGroup.getStyleClass().add("menuAcademic");
		toggleResearchGroup.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				toggleResearchGroup.setSelected(true);
				getChildren().remove(current);
				current = researchGroup;
				getChildren().add(researchGroup);
			}
		});
		
		toggleTeachingCategory = new ToggleButton("Categorías docentes");
		toggleTeachingCategory.setToggleGroup(toggleGroup);
		toggleTeachingCategory.getStyleClass().add("menuAcademic");
		toggleTeachingCategory.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				toggleTeachingCategory.setSelected(true);
				getChildren().remove(current);
				current = teachingCategory;
				getChildren().add(teachingCategory);
			}
		});
		
		toggleScientificCategory = new ToggleButton("Categorías científicas");
		toggleScientificCategory.setToggleGroup(toggleGroup);
		toggleScientificCategory.getStyleClass().add("menuAcademic");
		toggleScientificCategory.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				toggleScientificCategory.setSelected(true);
				getChildren().remove(current);
				current = scientificCategory;
				getChildren().add(scientificCategory);
			}
		});
		
		buttons = new HBox();
		Region spacer1 = new Region();
		spacer1.setId("spacerToggles");
		HBox.setHgrow(spacer1, Priority.ALWAYS);
		buttons.getChildren().addAll(spacer1, toggleTeachingGroup, toggleResearchGroup, toggleTeachingCategory, toggleScientificCategory);
		
		getChildren().addAll(buttons, teachingGroup);
		current = teachingGroup;
		
		AnchorPane.setTopAnchor(buttons, Double.valueOf(0));
		AnchorPane.setLeftAnchor(buttons, Double.valueOf(0));
		AnchorPane.setRightAnchor(buttons, Double.valueOf(0));
		
		AnchorPane.setTopAnchor(teachingGroup, Double.valueOf(50));
		AnchorPane.setLeftAnchor(teachingGroup, Double.valueOf(50));
		
		AnchorPane.setTopAnchor(researchGroup, Double.valueOf(50));
		AnchorPane.setLeftAnchor(researchGroup, Double.valueOf(50));
		
		AnchorPane.setTopAnchor(teachingCategory, Double.valueOf(50));
		AnchorPane.setLeftAnchor(teachingCategory, Double.valueOf(50));
		
		AnchorPane.setTopAnchor(scientificCategory, Double.valueOf(50));
		AnchorPane.setLeftAnchor(scientificCategory, Double.valueOf(50));
	}
	
	public void setData(){
		teachingGroup.setData();
		teachingCategory.setData();
		researchGroup.setData();
		scientificCategory.setData();
	}
	
	@SuppressWarnings("incomplete-switch")
	public void update(TypeOfInsertModifyDelete type){
		switch(type){
			case TEACHING_GROUP:
				teachingGroup.setData();
				break;
			case RESEARCH_GROUP:
				researchGroup.setData();
				break;
			case SCIENTIFIC_CATEGORY:
				scientificCategory.setData();
				break;
			case TEACHING_CATEGORY:
				teachingCategory.setData();
				break;
		}
	}
	
	public void hideInsertTeachingGroup(){ teachingGroup.hideInsert(); }
	public void hideModifyTeachingGroup(){ teachingGroup.hideModify(); }
	public void hideDeleteTeachingGroup(){ teachingGroup.hideDelete(); }
	public void showInsertTeachingGroup(){ teachingGroup.showInsert(); }
	public void showModifyTeachingGroup(){ teachingGroup.showModify(); }
	public void showDeleteTeachingGroup(){ teachingGroup.showDelete(); }
	
	public void hideInsertResearchGroup(){ researchGroup.hideInsert(); }
	public void hideModifyResearchGroup(){ researchGroup.hideModify(); }
	public void hideDeleteResearchGroup(){ researchGroup.hideDelete(); }
	public void showInsertResearchGroup(){ researchGroup.showInsert(); }
	public void showModifyResearchGroup(){ researchGroup.showModify(); }
	public void showDeleteResearchGroup(){ researchGroup.showDelete(); }
	
	public void hideInsertTeachingCategory(){ teachingCategory.hideInsert(); }
	public void hideModifyTeachingCategory(){ teachingCategory.hideModify(); }
	public void hideDeleteTeachingCategory(){ teachingCategory.hideDelete(); }
	public void showInsertTeachingCategory(){ teachingCategory.showInsert(); }
	public void showModifyTeachingCategory(){ teachingCategory.showModify(); }
	public void showDeleteTeachingCategory(){ teachingCategory.showDelete(); }
	
	public void hideInsertScientificCategory(){ scientificCategory.hideInsert(); }
	public void hideModifyScientificCategory(){ scientificCategory.hideModify(); }
	public void hideDeleteScientificCategory(){ scientificCategory.hideDelete(); }
	public void showInsertScientificCategory(){ scientificCategory.showInsert(); }
	public void showModifyScientificCategory(){ scientificCategory.showModify(); }
	public void showDeleteScientificCategory(){ scientificCategory.showDelete(); }
}

class TeachingGroupRegion extends VBox{
	private Label title;
	
	private CustomTableView<TeachingGroupWithProperty> table;
	private CustomTableColumn<TeachingGroupWithProperty, String> tcName;
	private CustomTableColumn<TeachingGroupWithProperty, Boolean> tcCanceled;
	
	private HBox buttons;
	private Button btnInsert;
	private Button btnModify;
	private Button btnDelete;
	
	@SuppressWarnings("unchecked")
	public TeachingGroupRegion(){
		setId("teachingGroupRegion");
		
		setSpacing(15);		
		setMinSize(440, 387);
		setPrefSize(440, 387);
		setMaxSize(440, 387);
		
		title = new Label("Grupos docentes");
		title.getStyleClass().add("title");
		
		table = new CustomTableView<TeachingGroupWithProperty>();
		table.setId("table");
		
		tcName = new CustomTableColumn<TeachingGroupWithProperty, String>("Nombre");
		tcName.setPercentWidth(62.8);
		tcName.setCellValueFactory(new PropertyValueFactory<TeachingGroupWithProperty, String>("name"));
		
		tcCanceled = new CustomTableColumn<TeachingGroupWithProperty, Boolean>("Cancelado");
		tcCanceled.setPercentWidth(35);
		tcCanceled.setCellValueFactory(new PropertyValueFactory<TeachingGroupWithProperty, Boolean>("canceled"));
		
		table.getTableView().getColumns().addAll(tcCanceled, tcName);
		
		
		buttons = new HBox();
		buttons.setTranslateX(20);
		buttons.setSpacing(10);
		Region spacer1 = new Region();
		HBox.setHgrow(spacer1, Priority.ALWAYS);
		
		btnInsert = new Button("Insertar");
		btnInsert.setMinSize(80, 26);
		btnInsert.setPrefSize(80, 26);
		btnInsert.setMaxSize(80, 26);
		btnInsert.getStyleClass().addAll("userOpButtons");
		btnInsert.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				InsertModifyAcademicDataDialog dialog = InsertModifyAcademicDataDialog.getInstance();
				dialog.setType(TypeOfInsertModifyDelete.TEACHING_GROUP, true);
				dialog.show();
			}
		});
		
		btnModify = new Button("Modificar");
		btnModify.setMinSize(80, 26);
		btnModify.setPrefSize(80, 26);
		btnModify.setMaxSize(80, 26);
		btnModify.getStyleClass().add("userOpButtons");
		btnModify.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				TeachingGroupWithProperty obj = table.getTableView().getSelectionModel().getSelectedItem();
				if(obj != null){
					InsertModifyAcademicDataDialog dialog = InsertModifyAcademicDataDialog.getInstance();
					dialog.setType(TypeOfInsertModifyDelete.TEACHING_GROUP, false);
					dialog.setEntity(obj);
					dialog.show();
				}
				else{
					DialogBox diag = new DialogBox("Debe seleccionar una entidad en la tabla");
					diag.show();
				}
			}
		});
		
		btnDelete = new Button("Eliminar");
		btnDelete.setMinSize(80, 26);
		btnDelete.setPrefSize(80, 26);
		btnDelete.setMaxSize(80, 26);
		btnDelete.getStyleClass().addAll("userOpButtons");
		btnDelete.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				TeachingGroupWithProperty obj = table.getTableView().getSelectionModel().getSelectedItem();
				if(obj != null){
					String text = "Realmente desea eliminar el grupo docente " + obj.nameProperty().get();
					DialogYesNoToDeleteAcademicData dialog = new DialogYesNoToDeleteAcademicData(text, TypeOfInsertModifyDelete.TEACHING_GROUP, obj.idProperty().get(), obj.nameProperty().get());
					dialog.show();
				}
			}
		});
		
		
		buttons.getChildren().addAll(spacer1);
		
		getChildren().addAll(title, table, buttons);
	}
	
	public void setData(){
		table.getTableView().getItems().removeAll(table.getTableView().getItems());
		try {
			List<TeachingGroupWithProperty> list = Utilities.toPropertiesTG(TeachingGroupServices.getTeachingGroups());
			table.getTableView().getItems().addAll(list);
		} catch (SQLException e) {
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
	
	public void hideInsert(){ buttons.getChildren().remove(btnInsert); }
	public void hideModify(){ buttons.getChildren().remove(btnModify); }
	public void hideDelete(){ buttons.getChildren().remove(btnDelete); }
	
	public void showInsert(){ if(!buttons.getChildren().contains(btnInsert)) buttons.getChildren().add(btnInsert); }
	public void showModify(){ if(!buttons.getChildren().contains(btnModify)) buttons.getChildren().add(btnModify); }
	public void showDelete(){ if(!buttons.getChildren().contains(btnDelete)) buttons.getChildren().add(btnDelete); }
	
}

class ResearchGroupRegion extends VBox{
	private Label title;
	
	private CustomTableView<ResearchGroupWithProperty> table;
	private CustomTableColumn<ResearchGroupWithProperty, String> tcName;
	private CustomTableColumn<ResearchGroupWithProperty, Boolean> tcCanceled;
	
	private HBox buttons;
	private Button btnInsert;
	private Button btnModify;
	private Button btnDelete;
	
	@SuppressWarnings("unchecked")
	public ResearchGroupRegion(){
		setId("researchGroupRegion");
		
		setSpacing(15);		
		setMinSize(440, 387);
		setPrefSize(440, 387);
		setMaxSize(440, 387);
		
		title = new Label("Grupos investigativos");
		title.getStyleClass().add("title");
		
		table = new CustomTableView<ResearchGroupWithProperty>();
		table.setId("table");
		
		tcName = new CustomTableColumn<ResearchGroupWithProperty, String>("Nombre");
		tcName.setPercentWidth(65);
		tcName.setCellValueFactory(new PropertyValueFactory<ResearchGroupWithProperty, String>("name"));
		
		tcCanceled = new CustomTableColumn<ResearchGroupWithProperty, Boolean>("Cancelado");
		tcCanceled.setPercentWidth(35);
		tcCanceled.setCellValueFactory(new PropertyValueFactory<ResearchGroupWithProperty, Boolean>("canceled"));
		
		table.getTableView().getColumns().addAll(tcCanceled, tcName);
		
		
		buttons = new HBox();
		buttons.setTranslateX(20);
		buttons.setSpacing(10);
		Region spacer1 = new Region();
		HBox.setHgrow(spacer1, Priority.ALWAYS);
		
		btnInsert = new Button("Insertar");
		btnInsert.setMinSize(80, 26);
		btnInsert.setPrefSize(80, 26);
		btnInsert.setMaxSize(80, 26);
		btnInsert.getStyleClass().addAll("userOpButtons");
		btnInsert.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				InsertModifyAcademicDataDialog dialog = InsertModifyAcademicDataDialog.getInstance();
				dialog.setType(TypeOfInsertModifyDelete.RESEARCH_GROUP, true);
				dialog.show();
			}
		});
		
		btnModify = new Button("Modificar");
		btnModify.setMinSize(80, 26);
		btnModify.setPrefSize(80, 26);
		btnModify.setMaxSize(80, 26);
		btnModify.getStyleClass().add("userOpButtons");
		btnModify.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				ResearchGroupWithProperty obj = table.getTableView().getSelectionModel().getSelectedItem();
				if(obj != null){
					InsertModifyAcademicDataDialog dialog = InsertModifyAcademicDataDialog.getInstance();
					dialog.setType(TypeOfInsertModifyDelete.RESEARCH_GROUP, false);
					dialog.setEntity(obj);
					dialog.show();
				}
				else{
					DialogBox diag = new DialogBox("Debe seleccionar una entidad en la tabla");
					diag.show();
				}
			}
		});
		
		btnDelete = new Button("Eliminar");
		btnDelete.setMinSize(80, 26);
		btnDelete.setPrefSize(80, 26);
		btnDelete.setMaxSize(80, 26);
		btnDelete.getStyleClass().addAll("userOpButtons");
		btnDelete.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				ResearchGroupWithProperty obj = table.getTableView().getSelectionModel().getSelectedItem();
				if(obj != null){
					String text = "Realmente desea eliminar el grupo investigativo " + obj.nameProperty().get();
					DialogYesNoToDeleteAcademicData dialog = new DialogYesNoToDeleteAcademicData(text, TypeOfInsertModifyDelete.RESEARCH_GROUP, obj.idProperty().get(),obj.nameProperty().get());
					dialog.show();
				}
			}
		});
		
		
		buttons.getChildren().addAll(spacer1, btnInsert, btnModify, btnDelete);
		
		getChildren().addAll(title, table, buttons);
	}
	
	public void setData(){
		table.getTableView().getItems().removeAll(table.getTableView().getItems());
		try {
			List<ResearchGroupWithProperty> list = Utilities.toPropertiesRG(ResearchGroupServices.getResearchGroups());
			table.getTableView().getItems().addAll(list);
		} catch (SQLException e) {
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
	
	public void hideInsert(){ buttons.getChildren().remove(btnInsert); }
	public void hideModify(){ buttons.getChildren().remove(btnModify); }
	public void hideDelete(){ buttons.getChildren().remove(btnDelete); }
	
	public void showInsert(){ if(!buttons.getChildren().contains(btnInsert)) buttons.getChildren().add(btnInsert); }
	public void showModify(){ if(!buttons.getChildren().contains(btnModify)) buttons.getChildren().add(btnModify); }
	public void showDelete(){ if(!buttons.getChildren().contains(btnDelete)) buttons.getChildren().add(btnDelete); }
}

class TeachingCategoryRegion extends VBox{
	private Label title;
	
	private CustomTableView<TeachingCategoryWithProperty> table;
	private CustomTableColumn<TeachingCategoryWithProperty, String> tcName;
	private CustomTableColumn<TeachingCategoryWithProperty, Boolean> tcCanceled;
	
	private HBox buttons;
	private Button btnInsert;
	private Button btnModify;
	private Button btnDelete;
	
	@SuppressWarnings("unchecked")
	public TeachingCategoryRegion(){
		setId("teachingCategoryRegion");
		
		setSpacing(15);		
		setMinSize(440, 387);
		setPrefSize(440, 387);
		setMaxSize(440, 387);
		
		title = new Label("Categorías docentes");
		title.getStyleClass().add("title");
		
		table = new CustomTableView<TeachingCategoryWithProperty>();
		table.setId("table");
		
		tcName = new CustomTableColumn<TeachingCategoryWithProperty, String>("Nombre");
		tcName.setPercentWidth(65);
		tcName.setCellValueFactory(new PropertyValueFactory<TeachingCategoryWithProperty, String>("name"));
		
		tcCanceled = new CustomTableColumn<TeachingCategoryWithProperty, Boolean>("Cancelada");
		tcCanceled.setPercentWidth(35);
		tcCanceled.setCellValueFactory(new PropertyValueFactory<TeachingCategoryWithProperty, Boolean>("canceled"));
		
		table.getTableView().getColumns().addAll(tcCanceled, tcName);
		
		
		buttons = new HBox();
		buttons.setTranslateX(20);
		buttons.setSpacing(10);
		Region spacer1 = new Region();
		HBox.setHgrow(spacer1, Priority.ALWAYS);
		
		btnInsert = new Button("Insertar");
		btnInsert.setMinSize(80, 26);
		btnInsert.setPrefSize(80, 26);
		btnInsert.setMaxSize(80, 26);
		btnInsert.getStyleClass().addAll("userOpButtons");
		btnInsert.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				InsertModifyAcademicDataDialog dialog = InsertModifyAcademicDataDialog.getInstance();
				dialog.setType(TypeOfInsertModifyDelete.TEACHING_CATEGORY, true);
				dialog.show();
			}
		});
		
		btnModify = new Button("Modificar");
		btnModify.setMinSize(80, 26);
		btnModify.setPrefSize(80, 26);
		btnModify.setMaxSize(80, 26);
		btnModify.getStyleClass().add("userOpButtons");
		btnModify.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				TeachingCategoryWithProperty obj = table.getTableView().getSelectionModel().getSelectedItem();
				if(obj != null){
					InsertModifyAcademicDataDialog dialog = InsertModifyAcademicDataDialog.getInstance();
					dialog.setType(TypeOfInsertModifyDelete.TEACHING_CATEGORY, false);
					dialog.setEntity(obj);
					dialog.show();
				}
				else{
					DialogBox diag = new DialogBox("Debe seleccionar una entidad en la tabla");
					diag.show();
				}
			}
		});
		
		btnDelete = new Button("Eliminar");
		btnDelete.setMinSize(80, 26);
		btnDelete.setPrefSize(80, 26);
		btnDelete.setMaxSize(80, 26);
		btnDelete.getStyleClass().addAll("userOpButtons");
		btnDelete.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				TeachingCategoryWithProperty obj = table.getTableView().getSelectionModel().getSelectedItem();
				if(obj != null){
					String text = "Realmente desea eliminar el grupo docente: " + obj.nameProperty().get();
					DialogYesNoToDeleteAcademicData dialog = new DialogYesNoToDeleteAcademicData(text, TypeOfInsertModifyDelete.TEACHING_CATEGORY, obj.idProperty().get(), obj.nameProperty().get());
					dialog.show();
				}
			}
		});
		
		
		buttons.getChildren().addAll(spacer1, btnInsert, btnModify, btnDelete);
		
		getChildren().addAll(title, table, buttons);
	}
	
	public void setData(){
		table.getTableView().getItems().removeAll(table.getTableView().getItems());
		try {
			List<TeachingCategoryWithProperty> list = Utilities.toPropertiesTC(TeachingCategoryServices.getTeachingCategory());
			table.getTableView().getItems().addAll(list);
		} catch (SQLException e) {
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
	
	public void hideInsert(){ buttons.getChildren().remove(btnInsert); }
	public void hideModify(){ buttons.getChildren().remove(btnModify); }
	public void hideDelete(){ buttons.getChildren().remove(btnDelete); }
	
	public void showInsert(){ if(!buttons.getChildren().contains(btnInsert)) buttons.getChildren().add(btnInsert); }
	public void showModify(){ if(!buttons.getChildren().contains(btnModify)) buttons.getChildren().add(btnModify); }
	public void showDelete(){ if(!buttons.getChildren().contains(btnDelete)) buttons.getChildren().add(btnDelete); }
}

class ScientificCategoryRegion extends VBox{
	private Label title;
	
	private CustomTableView<ScientificCategoryWithProperty> table;
	private CustomTableColumn<ScientificCategoryWithProperty, String> tcName;
	private CustomTableColumn<ScientificCategoryWithProperty, Boolean> tcCanceled;
	
	private HBox buttons;
	private Button btnInsert;
	private Button btnModify;
	private Button btnDelete;
	
	@SuppressWarnings("unchecked")
	public ScientificCategoryRegion(){
		setId("scientificCategoryRegion");
		
		setSpacing(15);
		setMinSize(440, 387);
		setPrefSize(440, 387);
		setMaxSize(440, 387);
		
		title = new Label("Categorías científicas");
		title.getStyleClass().add("title");
		
		table = new CustomTableView<ScientificCategoryWithProperty>();
		table.setId("table");
		
		tcName = new CustomTableColumn<ScientificCategoryWithProperty, String>("Nombre");
		tcName.setPercentWidth(65);
		tcName.setCellValueFactory(new PropertyValueFactory<ScientificCategoryWithProperty, String>("name"));
		
		tcCanceled = new CustomTableColumn<ScientificCategoryWithProperty, Boolean>("Cancelada");
		tcCanceled.setPercentWidth(35);
		tcCanceled.setCellValueFactory(new PropertyValueFactory<ScientificCategoryWithProperty, Boolean>("canceled"));
		
		table.getTableView().getColumns().addAll(tcCanceled, tcName);
		
		
		buttons = new HBox();
		buttons.setTranslateX(20);
		buttons.setSpacing(10);
		Region spacer1 = new Region();
		HBox.setHgrow(spacer1, Priority.ALWAYS);
		
		btnInsert = new Button("Insertar");
		btnInsert.setMinSize(80, 26);
		btnInsert.setPrefSize(80, 26);
		btnInsert.setMaxSize(80, 26);
		btnInsert.getStyleClass().addAll("userOpButtons");
		btnInsert.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				InsertModifyAcademicDataDialog dialog = InsertModifyAcademicDataDialog.getInstance();
				dialog.setType(TypeOfInsertModifyDelete.SCIENTIFIC_CATEGORY, true);
				dialog.show();
			}
		});
		
		btnModify = new Button("Modificar");
		btnModify.setMinSize(80, 26);
		btnModify.setPrefSize(80, 26);
		btnModify.setMaxSize(80, 26);
		btnModify.getStyleClass().add("userOpButtons");
		btnModify.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				ScientificCategoryWithProperty obj = table.getTableView().getSelectionModel().getSelectedItem();
				if(obj != null){
					InsertModifyAcademicDataDialog dialog = InsertModifyAcademicDataDialog.getInstance();
					dialog.setType(TypeOfInsertModifyDelete.SCIENTIFIC_CATEGORY, false);
					dialog.setEntity(obj);
					dialog.show();
				}
				else{
					DialogBox diag = new DialogBox("Debe seleccionar una entidad en la tabla");
					diag.show();
				}
			}
		});
		
		btnDelete = new Button("Eliminar");
		btnDelete.setMinSize(80, 26);
		btnDelete.setPrefSize(80, 26);
		btnDelete.setMaxSize(80, 26);
		btnDelete.getStyleClass().addAll("userOpButtons");
		btnDelete.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				ScientificCategoryWithProperty obj = table.getTableView().getSelectionModel().getSelectedItem();
				if(obj != null){
					String text = "Realmente desea eliminar el grupo docente: " + obj.nameProperty().get();
					DialogYesNoToDeleteAcademicData dialog = new DialogYesNoToDeleteAcademicData(text, TypeOfInsertModifyDelete.SCIENTIFIC_CATEGORY, obj.idProperty().get(), obj.nameProperty().get());
					dialog.show();
				}
			}
		});
		
		
		buttons.getChildren().addAll(spacer1, btnInsert, btnModify, btnDelete);
		
		getChildren().addAll(title, table, buttons);
	}
	
	public void setData(){
		table.getTableView().getItems().removeAll(table.getTableView().getItems());
		try {
			List<ScientificCategoryWithProperty> list = Utilities.toPropertiesSC(ScientificCategoryServices.getTeachingCategory());
			table.getTableView().getItems().addAll(list);
		} catch (SQLException e) {
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
	
	public void hideInsert(){ buttons.getChildren().remove(btnInsert); }
	public void hideModify(){ buttons.getChildren().remove(btnModify); }
	public void hideDelete(){ buttons.getChildren().remove(btnDelete); }
	
	public void showInsert(){ if(!buttons.getChildren().contains(btnInsert)) buttons.getChildren().add(btnInsert); }
	public void showModify(){ if(!buttons.getChildren().contains(btnModify)) buttons.getChildren().add(btnModify); }
	public void showDelete(){ if(!buttons.getChildren().contains(btnDelete)) buttons.getChildren().add(btnDelete); }
}