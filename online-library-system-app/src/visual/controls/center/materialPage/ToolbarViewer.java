package visual.controls.center.materialPage;

import utils.OpButton;
import visual.controls.center.CenterViewerMain;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class ToolbarViewer extends ToolBar {
	

	private OpButton search;
	private HBox boxTabRefereces;
	private TabReference tabTematic;
	private TabReference tabSubject;
	private TabReference tabMaterial;
	private Separator sep1;
	private Separator sep2;
	private TabReference tabMoreVisited;
	private TabReference tabLastInserted;
	
	public ToolbarViewer(){
		setId("topBarMaterialViewer");
		
		
				
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		VBox searchBox = new VBox();
		Region topSpacer = new Region();
		VBox.setVgrow(topSpacer, Priority.ALWAYS);
		
	
		
		Region bottomSpacer = new Region();
		VBox.setVgrow(bottomSpacer, Priority.ALWAYS);
		
		search = new OpButton("Búsqueda");
		search.setMinWidth(160);
		search.setPrefWidth(160);
		search.setMaxWidth(160);
		search.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				CenterViewerMain.getInstance().put(SearchPane.getInstance());
				
			}
		});
		
		searchBox.getChildren().addAll(topSpacer,search, bottomSpacer);
		
		boxTabRefereces = new HBox();
		tabTematic = new TabReference("Temáticas", TypeItem.TEMATIC);
		sep1 = new Separator();
		tabSubject = new TabReference("Asignaturas", TypeItem.SUBJECT);
		sep2 = new Separator();
		tabMaterial = new TabReference("Materiales", TypeItem.MATERIAL);
		
		tabMoreVisited = new TabReference("Materiales más visitados", TypeItem.MORE_VISITED);
		tabLastInserted = new TabReference("Últimos materiales añadidos", TypeItem.LAST_INSERTED);
		
		getItems().addAll( boxTabRefereces, spacer, searchBox);
	}
	
	
	public void addTematic(){
		tabTematic.clearName();
		clearTabs();
		boxTabRefereces.getChildren().add(tabTematic);
		ItemsViewer.getInstance().setTypesOfItems(TypeItem.TEMATIC);
	}
	public void addSubject(int tematicId){
		tabSubject.setId(tematicId);
		tabSubject.clearName();
		clearTabs();
		boxTabRefereces.getChildren().addAll(tabTematic, sep1, tabSubject);
		ItemsViewer.getInstance().setTypesOfItems(TypeItem.SUBJECT);
		ItemsViewer.getInstance().setSelectedTematicId(tematicId);
	}
	public void addMaterial(int subjectId){
		tabMaterial.setId(subjectId);
		tabMaterial.clearName();
		clearTabs();
		boxTabRefereces.getChildren().addAll(tabTematic, sep1, tabSubject, sep2, tabMaterial);
		ItemsViewer.getInstance().setTypesOfItems(TypeItem.MATERIAL);
		ItemsViewer.getInstance().setSelectedSubjectId(subjectId);
	}
	public void addMoreVisited(){
		tabMoreVisited.clearName();
		clearTabs();
		boxTabRefereces.getChildren().add(tabMoreVisited);
	}
	public void addLastInserted(){
		tabLastInserted.clearName();
		clearTabs();
		boxTabRefereces.getChildren().add(tabLastInserted);
	}
	
	public void updateNameOfLastRefference(String name){
		TabReference tab = (TabReference) boxTabRefereces.getChildren().get(boxTabRefereces.getChildren().size() - 1);
		tab.setName(name);
	}

	public void clearTabs() {
		boxTabRefereces.getChildren().removeAll(boxTabRefereces.getChildren());
	}
	
	
	class Separator extends Label{
		public Separator(){
			super("|");
			
			setId("separator");
			setAlignment(Pos.CENTER);
			setTextAlignment(TextAlignment.CENTER);
			
		}
	}
}
