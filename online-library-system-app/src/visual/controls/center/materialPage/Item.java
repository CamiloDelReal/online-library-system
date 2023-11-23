package visual.controls.center.materialPage;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import utils.Utilities;

public class Item extends Region{
	public static final double WIDTH = 100;//75
	public static final double HEIGHT = 85;//85
	
	public static final double ICON_POS_X = 4;
	public static final double ICON_POS_Y = 10;
	
	public static final double NAME_POS_X = 0;
	public static final double NAME_POS_Y = 70;
	
	public static final double CANCELED_POS_X = 42;
	public static final double CANCELED_POS_Y = 3;
	
	public static final double SELECTED_POS_X = 59;
	public static final double SELECTED_POS_Y = 3;
	
	public static final double REFERENCE_POS_X = 59;
	public static final double REFERENCE_POS_Y = 20;
	
	private static boolean canDoCancel;
	private static boolean canDoSelect;
	
	private Label lblName;

	private ImageView icon;
	private FileExtension typeExtension;
	private TypeItem typeItem;
	private int id;
	
	private CheckToggleButton select;
	private CheckToggleButton canceled;
	private Label lblReference;
	
	
	public Item(int id, String name, FileExtension typeExtension, boolean isCanceled, TypeItem typeItem, String tooltipText){
		setId("item");
		setMinSize(WIDTH, HEIGHT);
		setPrefSize(WIDTH, HEIGHT);
		setMaxSize(WIDTH, HEIGHT);
		
		Tooltip tooltip = new Tooltip(tooltipText);
		tooltip.getStyleClass().add("ownerTooltips");
		Tooltip.install(Item.this, tooltip);
		
		this.id = id;
		this.typeExtension = typeExtension;
		this.typeItem = typeItem;
		
		lblName = new Label(name);
		
		BufferedImage buffer = ImageIconRefference.getInstance().get(typeExtension);
		Image image = null;
		try {
			image = Utilities.createImage(buffer);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		icon = new ImageView(image);
		
		select = new CheckToggleButton(true);
		select.setVisible(false);
		
		canceled = new CheckToggleButton(false);
		canceled.setSelected(isCanceled);
		canceled.setVisible(isCanceled);
		canceled.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){ 
				DialogCancelTematicSubjectMaterial diag = new DialogCancelTematicSubjectMaterial(Item.this.typeItem, FXCollections.observableArrayList(Item.this), Item.this.id, false); // no es por seleccion
				diag.show();
			}
		});
		
		
		
		lblReference = new Label();
		lblReference.setMinSize(16, 16);
		lblReference.setPrefSize(16, 16);
		lblReference.setMaxSize(16, 16);
		lblReference.getStyleClass().add("reference");
		
		getChildren().addAll(icon, lblName, select, canceled);
		
		
		setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				if(canDoSelect)
					select.setVisible(true);
				if(canDoCancel)
					canceled.setVisible(true);
			}
		});
		
		setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				if(e.getButton().compareTo(MouseButton.SECONDARY) == 0 && canDoSelect)
					select.setSelected(!select.isSelected());
				//est no funcina correctamente
				/*else if(e.getButton().compareTo(MouseButton.MIDDLE) == 0 && canDoCancel){
					canceled.setSelected(!canceled.isSelected());
					DialogCancelTematicSubjectMaterial diag = new DialogCancelTematicSubjectMaterial(Item.this.typeItem, FXCollections.observableArrayList(Item.this), Item.this.id, false); // no es por seleccion
					diag.show();
				}*/
				else if(e.getButton().compareTo(MouseButton.PRIMARY) == 0){
					MaterialViewer.getInstance().updateLastTabReference(lblName.getText());
					if(Item.this.typeItem.compareTo(TypeItem.TEMATIC) == 0){
						MaterialViewer.getInstance().putSubjects(Item.this.id, lblName.getText());						
					}
					else if(Item.this.typeItem.compareTo(TypeItem.SUBJECT) == 0){						
						MaterialViewer.getInstance().putMaterials(Item.this.id, lblName.getText());
					}
					else if(Item.this.typeItem.compareTo(TypeItem.MATERIAL) == 0){
						MaterialViewer.getInstance().putMaterial(Item.this.id, ItemsViewer.getInstance().getCurrentSubjectId());
					}
					else if(Item.this.typeItem.compareTo(TypeItem.MORE_VISITED) == 0){
						MaterialViewer.getInstance().putMoreVisitedMaterial(Item.this.id);
					}
					else if(Item.this.typeItem.compareTo(TypeItem.LAST_INSERTED) == 0){
						MaterialViewer.getInstance().putLastInsertedMaterial(Item.this.id);
					}
				}
			}
		});
		
		setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				if(!select.isSelected())
					select.setVisible(false);
				if(!canceled.isSelected())
					canceled.setVisible(false);
			}
		});
	}
	
	@Override protected void layoutChildren(){
		icon.relocate(4, 10);
		lblName.resizeRelocate(0, 70, WIDTH, 15);
		select.resizeRelocate(59, 3, 16, 16);
		canceled.resizeRelocate(42, 3, 16, 16);
		lblReference.resizeRelocate(59, 20, 16, 16);
	}
	
	public FileExtension getType(){
		return typeExtension;
	}
	
	public boolean isCanceled(){
		return canceled.isSelected();
	}
	public boolean isSelected(){
		return select.isSelected();
	}
	
	public String getName(){
		return lblName.getText();
	}
	public int getItemId(){
		return id;
	}
	
	public static void thisUserCanDoCancel(boolean can){
		canDoCancel = can;
	}
	public static void thisUserCanDoSelect(boolean can){
		canDoSelect = can;
	}
}

enum TypeItem{
	TEMATIC,
	SUBJECT,
	MATERIAL,
	MORE_VISITED,
	LAST_INSERTED
}

class CheckToggleButton extends ToggleButton{
	
	public CheckToggleButton(boolean isSelect){
		setId("checkToggleButton");
		setMinSize(16, 16);
		setPrefSize(16, 16);
		setMaxSize(16, 16);
		setText("fdg");
		
		if(isSelect)
			getStyleClass().add("select");
		else
			getStyleClass().add("canceled");
	}

}