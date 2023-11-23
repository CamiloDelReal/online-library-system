package visual.controls.center.materialPage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

class TabReference extends VBox{
	private Hyperlink lblHeader;
	private Label lblName;
	
	private TypeItem typeItem;
	private int id;
	
	public TabReference(String header, TypeItem typeItem){
		setId("tabReference");
		
		this.typeItem = typeItem;
		
		lblHeader = new Hyperlink(header);
		lblHeader.setMinHeight(22);
		lblHeader.setPrefHeight(22);
		lblHeader.setMaxHeight(22);
		lblHeader.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				if(TabReference.this.typeItem.compareTo(TypeItem.TEMATIC) == 0){
					MaterialViewer.getInstance().putTematics();
				}
				else if(TabReference.this.typeItem.compareTo(TypeItem.SUBJECT) == 0){
					MaterialViewer.getInstance().putSubjects(TabReference.this.id, lblName.getText());
				}
				else if(TabReference.this.typeItem.compareTo(TypeItem.MATERIAL) == 0){
					MaterialViewer.getInstance().putMaterials(TabReference.this.id, lblName.getText());
				}
				else if(TabReference.this.typeItem.compareTo(TypeItem.MORE_VISITED) == 0){
					MaterialViewer.getInstance().putMoreVisited();
				}
				else if(TabReference.this.typeItem.compareTo(TypeItem.LAST_INSERTED) == 0){
					MaterialViewer.getInstance().putLastInserted();
				}
			}
		});
		
		lblName = new Label();
		/*lblName.setTextAlignment(TextAlignment.CENTER);
		lblName.setMinWidth(60);
		lblName.setPrefWidth(90);
		lblName.setMaxWidth(120);*/
		//lblName.setStyle("-fx-background-color:red;");
		
		Region spacer1 = new Region();
		HBox.setHgrow(spacer1, Priority.ALWAYS);
		Region spacer2 = new Region();
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		Region spacer3 = new Region();
		HBox.setHgrow(spacer3, Priority.ALWAYS);
		Region spacer4 = new Region();
		HBox.setHgrow(spacer4, Priority.ALWAYS);
		
		HBox boxHeader = new HBox();
		boxHeader.getChildren().addAll(spacer1, lblHeader, spacer2);
		
		HBox boxName = new HBox();
		boxName.getChildren().addAll(spacer3, lblName, spacer4);
		
		getChildren().addAll(boxHeader, boxName);
	}
	
	public void setName(String name){
		if(name.length() > 20){
			String newName = "";
			for(int i = 0; i < 20; i++)
				newName += String.valueOf(name.charAt(i));
			
			lblName.setText("(" + newName + "...)");
		}
		else
			lblName.setText("(" + name + ")");
	}
	
	public void clearName(){
		lblName.setText("");
	}
	
	public void setId(int id){
		this.id = id;
	}
}
