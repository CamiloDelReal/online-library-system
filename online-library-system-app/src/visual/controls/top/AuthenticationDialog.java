package visual.controls.top;

import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.users.User;
import services.UserServices;
import utils.GUI;
import visual.Main;

public class AuthenticationDialog extends HBox {
	
	private Label lblTitle;
	private Label lblTitle2;
	
	private Label lblUser;
	private TextField txfUser;
	
	private Label lblPassword;
	private PasswordField psfPassword;
	
	private Button btnAccept;
	private Button btnCancel;
	
	private ProgressIndicator progress;
	private Label lblValidation;
	
	private VBox controls;
	private HBox validation;
	
	private StackPane dialogRoot;
	
	public AuthenticationDialog(){
		
		setId("authenticationDialog");
		setMinSize(450, 260);
		setPrefSize(450, 260);
		setMaxSize(450, 260);
		
		lblTitle = new Label("Registro");
		lblTitle.getStyleClass().add("title");
		lblTitle2 = new Label("Validación de usuario y contraseña");
		lblTitle2.getStyleClass().add("title2");
		
		lblUser = new Label("Usuario");
		txfUser = new TextField();
		
		lblPassword = new Label("Contraseña");
		psfPassword = new PasswordField();
		
		btnAccept = new Button("Aceptar");
		btnAccept.setDefaultButton(true);
		btnAccept.setMinSize(80, 20);
		btnAccept.setPrefSize(80, 20);
		btnAccept.setMaxSize(80, 20);
		btnAccept.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				//Validation
				String userName = txfUser.getText();
				String userPassword = psfPassword.getText();
				
				if(!validation.getChildren().contains(progress))
					validation.getChildren().add(0, progress);
				
				if(!controls.getChildren().contains(validation))
					controls.getChildren().add(validation);
				
				try {
					if(!UserServices.isUserLogin(userName, userPassword)){
						User user = UserServices.getUserToLogin(userName, userPassword);
						validation.getChildren().remove(progress);
						if(user != null){
							UserServices.LoginUser(userName, userPassword);
							lblValidation.getStyleClass().removeAll("lblValidationIncorrect");
							lblValidation.setText("Todos los datos correctos");
							GUI.getInstance().setUser(user);
							dialogRoot.getChildren().remove(AuthenticationDialog.this);
							Main.getStackRoot().getChildren().remove(dialogRoot);							
						}
						else{
							lblValidation.getStyleClass().add("lblValidationIncorrect");
							lblValidation.setText("Usuario o contraseña incorrectos");
						}
					}
					else{
						validation.getChildren().remove(progress);
						lblValidation.getStyleClass().add("lblValidationIncorrect");
						lblValidation.setText("Cuenta de usuario en uso");
					}
				} catch (SQLException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		btnCancel = new Button("Cancelar");
		btnCancel.setMinSize(80, 20);
		btnCancel.setPrefSize(80, 20);
		btnCancel.setMaxSize(80, 20);
		btnCancel.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				dialogRoot.getChildren().remove(AuthenticationDialog.this);
				Main.getStackRoot().getChildren().remove(dialogRoot);
			}
		});
		
		progress = new ProgressIndicator();
		progress.setMinSize(20, 20);
		progress.setPrefSize(20, 20);
		progress.setMaxSize(20, 20);
		lblValidation = new Label("Validando usuario y contraseña");
		lblValidation.getStyleClass().addAll("lblValidation");
		lblValidation.setMinHeight(20);
		lblValidation.setPrefHeight(20);
		lblValidation.setMaxHeight(20);
		
		validation = new HBox();
		validation.setTranslateY(30);
		validation.setSpacing(7);
		validation.getChildren().addAll(progress, lblValidation);
		
		dialogRoot = new StackPane();
		dialogRoot.setId("dialogRoot");
		
		Region spacer1 = new Region();
		HBox.setHgrow(spacer1, Priority.ALWAYS);
		
		Region spacer2 = new Region();
		spacer2.setMinWidth(40);
		spacer2.setPrefWidth(40);
		spacer2.setMaxWidth(40);
		
		GridPane grid = new GridPane();
		//grid.setGridLinesVisible(true);
		ObservableList<Node> content = grid.getChildren();
		
		GridPane.setConstraints(lblUser, 0, 0);
		GridPane.setMargin(lblUser, new Insets(10, 5, 2, 10));
		content.add(lblUser);
		GridPane.setConstraints(txfUser, 1, 0);
		GridPane.setMargin(txfUser, new Insets(10, 10, 2, 5));
		content.add(txfUser);
		
		GridPane.setConstraints(lblPassword, 0, 1);
		GridPane.setMargin(lblPassword, new Insets(10, 5, 13, 10));
		content.add(lblPassword);
		GridPane.setConstraints(psfPassword, 1, 1);
		GridPane.setMargin(psfPassword, new Insets(10, 10, 13, 5));
		content.add(psfPassword);
		
		HBox buttons = new HBox();
		buttons.setSpacing(7);
		buttons.setTranslateY(10);
		
		Region spacer3 = new Region();
		HBox.setHgrow(spacer3, Priority.ALWAYS);
		Region spacer4 = new Region();
		spacer4.setMinHeight(25);
		spacer4.setPrefHeight(25);
		spacer4.setMaxHeight(25);
		Region spacer5 = new Region();
		HBox.setHgrow(spacer5, Priority.ALWAYS);
		
		buttons.getChildren().addAll(spacer3, btnAccept, btnCancel, spacer5);
		
		Region spacer6 = new Region();
		HBox.setHgrow(spacer6, Priority.ALWAYS);
		HBox toTitle2 = new HBox();
		toTitle2.getChildren().addAll(spacer6, lblTitle2);
		
		VBox titles = new VBox();
		titles.getChildren().addAll(lblTitle, toTitle2);
		
		
		controls = new VBox();
		controls.setTranslateY(50);
		controls.getChildren().addAll(titles, spacer4, grid, buttons);
		
		getChildren().addAll(spacer1, controls, spacer2);
		
		
		dialogRoot.getChildren().add(AuthenticationDialog.this);
				
	}
	
	public void show(){
		Main.getStackRoot().getChildren().add(dialogRoot);
		txfUser.requestFocus();
	}
}
