package visual.controls.top;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import models.users.User;
import services.UserServices;
import utils.DialogBox;
import utils.GUI;
import utils.Utilities;
import visual.controls.Notification;

public class TitledPane extends HBox {
	private static TitledPane titledPane = null;
	
	private ImageView applicationName;
	
	//private ImageView logo;
	
	private ImageView userPicture;
	private Label lblUserName;
	private Button btnUserLogout;
	private ImageView imgLogout;
	
	private Tooltip register;
	private Tooltip unregister;
	
	private Image unknown;
	private Image login;
	private Image logout;
	
	public static TitledPane getInstance(){
		if(titledPane == null)
			titledPane = new TitledPane();
		
		return titledPane;
	}
	
	private TitledPane(){
		setId("titledPane");
		setMinHeight(67);
		setPrefHeight(67);
		setMaxHeight(67);
		
		unknown = new Image(getClass().getResource("../../icons/unknow16.png").toExternalForm());
		login = new Image(getClass().getResource("../../icons/login.png").toExternalForm());
		logout = new Image(getClass().getResource("../../icons/logout.png").toExternalForm());
		
		Region inSubtitution_ApplicationName = new Region();
		inSubtitution_ApplicationName.setId("inSubtitution_ApplicationName");
		inSubtitution_ApplicationName.setMinWidth(15);
		inSubtitution_ApplicationName.setPrefWidth(15);
		inSubtitution_ApplicationName.setMaxWidth(15);
		
		Region spacer1 = new Region();
		//HBox.setHgrow(spacer1, Priority.ALWAYS);
		spacer1.setMinWidth(20);
		spacer1.setPrefWidth(20);
		spacer1.setMaxWidth(20);
		
		//logo = new ImageView(new Image(getClass().getResource("../icons/logo.png").toExternalForm()));		
		
		Region spacer2 = new Region();
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		
		Region spacer3 = new Region();
		spacer3.setMinHeight(10);
		spacer3.setPrefHeight(10);
		spacer3.setMaxHeight(10);
		
		register = new Tooltip("Loguearse");
		register.getStyleClass().add("ownerTooltips");
		unregister = new Tooltip("Desloguearse");
		unregister.getStyleClass().add("ownerTooltips");
		
		userPicture = new ImageView(unknown);
		userPicture.getStyleClass().add("userPicture");
		lblUserName = new Label("Desconocido", userPicture);
		lblUserName.getStyleClass().add("userName");
		lblUserName.setMinHeight(24);
		lblUserName.setPrefHeight(24);
		lblUserName.setMaxHeight(24);
		
		imgLogout = new ImageView(login);
		btnUserLogout = new Button();
		btnUserLogout.setTooltip(register);
		btnUserLogout.setId("btnUserLogout");
		btnUserLogout.setGraphic(imgLogout);
		btnUserLogout.setMinSize(28, 24);
		btnUserLogout.setPrefSize(28, 24);
		btnUserLogout.setMaxSize(28, 24);
		btnUserLogout.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				AuthenticationDialog authenDiag = new AuthenticationDialog();
				if(!GUI.getInstance().haveUser())
					authenDiag.show();
				else{
					try {
						UserServices.LogoutUser(GUI.getInstance().getUser().getId());
						Notification.getInstance().setText("Se ha deslogueado satisfactoriamente");
						Notification.getInstance().showNotification();
					} catch (SQLException e1) {
						DialogBox diag = new DialogBox("Ha ocurrido un error al desloguear su usuario de la base de datos");
						diag.show();
					}					
					GUI.getInstance().setDefault();
				}
			}
		});
		
		
		/*Region spacer4 = new Region();
		spacer4.setMinWidth(10);
		spacer4.setPrefWidth(10);
		spacer4.setMaxWidth(10);*/
		
		Region spacer5 = new Region();
		spacer5.setMinWidth(10);
		spacer5.setPrefWidth(10);
		spacer5.setMaxWidth(10);
		
		HBox searchAndExit = new HBox();
		searchAndExit.getChildren().addAll(lblUserName, btnUserLogout, spacer5);
		
		
		Region spacer6 = new Region();
		VBox.setVgrow(spacer6, Priority.ALWAYS);
		
		VBox rightOptions = new VBox();
		rightOptions.getChildren().addAll(spacer3, searchAndExit, spacer6);
		
		applicationName = new ImageView(new Image(getClass().getResource("../../icons/title.png").toExternalForm()));
		applicationName.setTranslateY(3);
		
		getChildren().addAll(inSubtitution_ApplicationName, applicationName, spacer1, spacer2, rightOptions);
	}
	
	public void setDefaultData(){
		userPicture.setImage(unknown);
		lblUserName.setText("Desconocido");
		imgLogout.setImage(login);
		
		imgLogout.setImage(login);
		btnUserLogout.setTooltip(register);
	}
	
	public void setData() throws IOException{
		User user = GUI.getInstance().getUser();
		
		lblUserName.setText(user.getFullName());
		
		imgLogout.setImage(logout);
		btnUserLogout.setTooltip(unregister);
		
		BufferedImage picture = user.getPicture();
		Image photo = unknown;
		
		if(picture != null){
			if(picture.getWidth() > 16 || picture.getHeight() > 16){
				picture = Utilities.createBufferedImage(Utilities.scaleImageIcon(picture, 16, 16));
				photo = Utilities.createImage(picture);
			}
			else
				photo = Utilities.createImage(picture);
		}
		userPicture.setImage(photo);

	}
	
	public void setDefaultAvatar(){
		userPicture.setImage(unknown);
	}
}
