package visual.controls.top;

import java.awt.image.BufferedImage;
import java.io.IOException;

import models.users.User;
import utils.GUI;
import utils.Utilities;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Tag extends Region {
	//Como campo un usuario
	private ImageView picture;
	private Label lblName;
	private Label lblPrivilege;
	
	private Image unknown;
	
	public Tag(){
		setId("tags");
		setMinSize(195, 67);
		setPrefSize(195, 67);
		setMaxSize(195, 67);
		
		unknown = new Image(getClass().getResource("../../icons/unknow70.png").toExternalForm());
		
		Reflection reflection = new Reflection();
		reflection.setTopOffset(0);
		reflection.setFraction(0.4);
		setEffect(reflection);
		
		picture = new ImageView(unknown);
		lblName = new Label("Usuario desconocido");
		lblPrivilege = new Label("Sin privilegios");
		
		VBox box1 = new VBox();
		box1.getChildren().addAll(lblName, lblPrivilege);
		
		HBox box2 = new HBox();
		box2.getChildren().addAll(picture, box1);
		
		getChildren().addAll(picture, lblName, lblPrivilege);
		
	}
	
	@Override protected void layoutChildren() {
		picture.resizeRelocate(5, 3, 64, 60);
		lblName.resizeRelocate(73, 10, 140, 16);
		lblPrivilege.resizeRelocate(73, 27, 140, 16);
    }
	
	public void setDefaultData(){
		picture.setImage(unknown);
		lblName.setText("Usuario desconocido");
		lblPrivilege.setText("Sin privilegios");
	}
	
	public void setData() throws IOException{
		User user = GUI.getInstance().getUser();
		
		lblName.setText(user.getName());
		lblPrivilege.setText(GUI.calculateOnlyPrivilege(user.getPrivilege()));
		
		BufferedImage image = user.getPicture();
		Image photo = unknown;
		
		if(image != null){
			if(image.getWidth() > 56 || image.getHeight() > 58){
				image = Utilities.createBufferedImage(Utilities.scaleImageIcon(image, 56, 60));
				photo = Utilities.createImage(image);
			}
			else
				photo = Utilities.createImage(image);
		}
				
		picture.setImage(photo);
	}
	
}
