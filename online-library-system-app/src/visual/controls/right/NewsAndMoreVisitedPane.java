package visual.controls.right;

import java.sql.SQLException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import models.materials.Material;
import services.MaterialServices;
import utils.DialogBox;
import visual.controls.center.CenterViewerMain;
import visual.controls.center.materialPage.MaterialViewer;

public class NewsAndMoreVisitedPane extends VBox {
	private Label lblNews;
	private HyperlinkToMaterial[] news;
	private Hyperlink hlkNewsFullList;
	
	private Label lblMoreVisited;
	private HyperlinkToMaterial[] more;
	private Hyperlink hlkMoreVisitedFullList;
	
	public NewsAndMoreVisitedPane(){
		setId("newsAndMoreVisitedPane");
		setMinWidth(166);
		setPrefWidth(166);
		setMaxWidth(166);
		setSpacing(3);
		
		lblNews = new Label("Últimos materiales añadidos");
		lblNews.setId("lblNews");
		lblNews.setMinSize(166, 25);
		lblNews.setPrefSize(166, 25);
		lblNews.setMaxSize(166, 25);
		getChildren().add(lblNews);
		
		news = new HyperlinkToMaterial[]{
				new HyperlinkToMaterial(),
				new HyperlinkToMaterial(),
				new HyperlinkToMaterial()
			};
		for(final HyperlinkToMaterial hp : news){
			hp.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e){
					CenterViewerMain.getInstance().put(MaterialViewer.getInstance());
					MaterialViewer.getInstance().putLastInsertedMaterial(hp.getMaterialId());
				}
			});
		}
		getChildren().addAll(news);
		
		HBox box1 = new HBox();
		Region spacer1 = new Region();
		HBox.setHgrow(spacer1, Priority.ALWAYS);
		
		hlkNewsFullList = new Hyperlink("Listado completo");
		hlkNewsFullList.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				CenterViewerMain.getInstance().put(MaterialViewer.getInstance());
				MaterialViewer.getInstance().putLastInserted();
			}
		});
		
		box1.getChildren().addAll(spacer1, hlkNewsFullList);
		
		getChildren().add(box1);
		
		//--------------------------------------------------------------------------
		
		lblMoreVisited = new Label("Materiales más visitados");
		lblMoreVisited.setId("lblMoreVisited");
		lblMoreVisited.setMinSize(166, 25);
		lblMoreVisited.setPrefSize(166, 25);
		lblMoreVisited.setMaxSize(166, 25);
		getChildren().add(lblMoreVisited);
		
		more = new HyperlinkToMaterial[]{
			new HyperlinkToMaterial(),
			new HyperlinkToMaterial(),
			new HyperlinkToMaterial(),
			new HyperlinkToMaterial()
		};
		for(final HyperlinkToMaterial hp : more){
			hp.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e){
					CenterViewerMain.getInstance().put(MaterialViewer.getInstance());
					MaterialViewer.getInstance().putMoreVisitedMaterial(hp.getMaterialId());
				}
			});
		}
		getChildren().addAll(more);
		
		HBox box2 = new HBox();
		Region spacer2 = new Region();
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		
		hlkMoreVisitedFullList = new Hyperlink("Listado completo");
		hlkMoreVisitedFullList.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				CenterViewerMain.getInstance().put(MaterialViewer.getInstance());
				MaterialViewer.getInstance().putMoreVisited();
			}
		});
		
		box2.getChildren().addAll(spacer2, hlkMoreVisitedFullList);
		
		getChildren().add(box2);
		
		update();
	}
	
	public void update(){
		try {
			List<Material> mats = MaterialServices.getMoreVisited4();
			int index = 0;
			for(Material mat : mats){
				more[index].setText(
						"(" + (mat.getVisitedByStudent() + mat.getVisitedByTeachers() + mat.getVisitedByUnknown()) + ") " +
						mat.getTitle());
				more[index].setMaterialId(mat.getCode());
				
				Tooltip tool = new Tooltip(
						mat.getTitle() + "\n" +
						"Visitado por: " + (mat.getVisitedByStudent() + mat.getVisitedByTeachers() + mat.getVisitedByUnknown()) +
						" usuarios"
						);
				tool.getStyleClass().add("ownerTooltips");
				more[index].setTooltip(tool);
				
				index++;
			}
		} catch (SQLException e) {
			DialogBox diag = new DialogBox("No se pudo obtener un preview de los materiales más visitados");
			diag.show();
		}
		
		try{
			List<Material> mats = MaterialServices.getLastInserted3();
			int index = 0;
			for(Material mat : mats){
				news[index].setText(mat.getTitle());
				news[index].setMaterialId(mat.getCode());
				Tooltip tool = new Tooltip(mat.getTitle());
				tool.getStyleClass().add("ownerTooltips");
				news[index].setTooltip(tool);
				
				index++;
			}
		} catch (SQLException e) {
			DialogBox diag = new DialogBox("No se pudo obtener un preview de los ültimos materiales añadidos");
			diag.show();
		}
	}
}

class HyperlinkToMaterial extends Hyperlink{
	private int materialId;
	
	public HyperlinkToMaterial(){
		super();
	}
	
	public void setMaterialId(int id){
		materialId = id;
	}
	
	public int getMaterialId(){
		return materialId;
	}
}
