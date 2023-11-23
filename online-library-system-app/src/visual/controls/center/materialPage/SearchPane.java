package visual.controls.center.materialPage;

import java.sql.SQLException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import models.materials.MaterialWithProperty;
import models.users.User;
import services.MaterialServices;
import utils.CustomTableColumn;
import utils.CustomTableView;
import utils.DialogBox;
import utils.GUI;
import utils.OpButton;
import utils.Utilities;
import visual.controls.center.CenterViewerMain;

public class SearchPane extends VBox {
	private static SearchPane searchPane = null;
	private TextField search;
	private OpButton searchButton;
	private CustomTableView<MaterialWithProperty> table;
	private CustomTableColumn<MaterialWithProperty, String> titulo;
	private CustomTableColumn<MaterialWithProperty, String> autor;
	private CustomTableColumn<MaterialWithProperty, String> nombreTipoMaterial;
	private RadioButton rbtnTitulo;
	private RadioButton rbtnAutor;
	private RadioButton rbtnPalabrasClaves;

	@SuppressWarnings("unchecked")
	private SearchPane(){
		setId("SearchPane");
		setMinSize(ItemsViewer.WIDTH -8, ItemsViewer.HEIGHT +20);
		setPrefSize(ItemsViewer.WIDTH -8, ItemsViewer.HEIGHT +20);
		setMaxSize(ItemsViewer.WIDTH -8, ItemsViewer.HEIGHT +20);

		table = new CustomTableView <MaterialWithProperty> ();
		table.getTableView().setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				MaterialWithProperty mat = table.getTableView().getSelectionModel().getSelectedItem();
				if(mat != null && e.getClickCount() == 2){
					CenterViewerMain.getInstance().put(MaterialViewer.getInstance());
					MaterialViewer.getInstance().putMaterial(mat.codigoProperty().get(), mat.asignaturaProperty().get());
					MaterialViewer.getInstance().addMaterial(mat.asignaturaProperty().get());
				}
			}
		});

		titulo = new CustomTableColumn<MaterialWithProperty, String>("Título");
		titulo.setPercentWidth(60);
		titulo.setCellValueFactory(new PropertyValueFactory<MaterialWithProperty, String>("titulo"));

		autor = new CustomTableColumn<MaterialWithProperty, String>("Autor");
		autor.setPercentWidth(30);
		autor.setCellValueFactory(new PropertyValueFactory<MaterialWithProperty, String>("autor"));

		nombreTipoMaterial = new CustomTableColumn<MaterialWithProperty, String>("Tipo");
		nombreTipoMaterial.setPercentWidth(10);
		nombreTipoMaterial.setCellValueFactory(new PropertyValueFactory<MaterialWithProperty, String>("nombreTipoMaterial"));

		table.getTableView().getColumns().addAll(titulo, autor, nombreTipoMaterial);

		search = new TextField();
		search.setId("Buscador");

		searchButton = new OpButton("Buscar");
		searchButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				User user = GUI.getInstance().getUser();
				String cad = search.getText();
				if(cad == null || cad.isEmpty() || Utilities.isEmpty(cad)){
					DialogBox diag = new DialogBox("Debe entrar una cadena de texto para realizar la búsqueda");
					diag.show();
				}
				else{
					try{
						if(rbtnTitulo.isSelected()){
							if(user != null && user.getPrivilege().isModifyMaterial())
								fillTable(MaterialServices.getMaterialByTitle(cad));
							else
								fillTable(MaterialServices.getMaterialEnabledByTitle(cad));
						}
						else if(rbtnAutor.isSelected()){
							if(user != null && user.getPrivilege().isModifyMaterial())
								fillTable(MaterialServices.getMaterialByAuthor(cad));
							else 
								fillTable(MaterialServices.getMaterialEnabledlByAuthor(cad));
						}
						else {
							if(user != null && user.getPrivilege().isModifyMaterial())
								fillTable(Utilities.findCoincidenceAndSort(MaterialServices.getMaterialWithKeyWord(cad), cad.split(",")));
							else 
								fillTable(Utilities.findCoincidenceAndSort(MaterialServices.getMaterialEnabledWithKeyWord(cad), cad.split(",")));
						}
					} catch(SQLException e){
						DialogBox diag = new DialogBox(e.getMessage());
						diag.show();
					}
				}
			}
		});

		HBox top = new HBox();
		HBox.setHgrow(search, Priority.ALWAYS);
		top.getChildren().addAll(search, searchButton);

		ToggleGroup group = new ToggleGroup();
		rbtnTitulo = new RadioButton("Por Título");
		rbtnTitulo.setSelected(true);
		rbtnTitulo.setToggleGroup(group);
		rbtnAutor = new RadioButton("Por Autor");
		rbtnAutor.setToggleGroup(group);
		rbtnPalabrasClaves = new RadioButton("Por Palabras Claves");
		rbtnPalabrasClaves.setToggleGroup(group);

		Region spacer4 = new Region ();
		HBox.setHgrow(spacer4, Priority.ALWAYS);

		Region spacer5 = new Region ();
		HBox.setHgrow(spacer5, Priority.ALWAYS);

		HBox radio = new HBox();
		radio.getChildren().addAll(spacer4,rbtnTitulo, rbtnAutor, rbtnPalabrasClaves, spacer5);

		setSpacing(5);	
		getChildren().addAll(top,radio, table);


	}

	public static SearchPane getInstance(){
		if (searchPane == null)
			searchPane = new SearchPane();
		return searchPane;
	}

	private void fillTable(List<MaterialWithProperty> list){
		table.getTableView().getItems().removeAll(table.getTableView().getItems());
		table.getTableView().getItems().addAll(list);
	}

}
