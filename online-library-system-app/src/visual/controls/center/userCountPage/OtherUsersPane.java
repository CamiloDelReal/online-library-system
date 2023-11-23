package visual.controls.center.userCountPage;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import models.Privilege;
import models.users.SmallUser;
import models.users.Student;
import models.users.Teacher;
import models.users.User;
import services.PrivilegeServices;
import services.UserServices;
import utils.DialogBox;
import utils.GUI;
import utils.OpButton;
import utils.Utilities;

public class OtherUsersPane extends BorderPane{	
	private static OtherUsersPane pane = null;
	
	private AnchorPane root;
	
	private SearchPeople search;
	private List<UserItem> currentUsers;
	private ListView<UserItem> viewUser;
	
	private OpButton btnInsert;
	private OpButton btnModify;
	private OpButton btnDelete;
	private HBox buttons;
	
	private GeneralDataToShow generalData;
	
	public static OtherUsersPane getInstance(){
		if(pane == null)
			pane = new OtherUsersPane();
		return pane;
	}
	
	private OtherUsersPane() {
		setId("otherUsersPane");
		
		currentUsers = new LinkedList<UserItem>();
		
		search = new SearchPeople();
		
		viewUser = new ListView<UserItem>();
		viewUser.setId("listViewUser");
		viewUser.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserItem>() {
			public void changed(ObservableValue<? extends UserItem> observable,
					UserItem oldValue, UserItem newValue) {
				if(newValue != null){
					SmallUser smallUser = newValue.getUser();
					try{
						User user = UserServices.getUser(smallUser.getId());
						generalData.setData(user);
					}catch(SQLException | IOException e1){
						DialogBox diag = new DialogBox("No se pudieron obtener los datos del usuario " + smallUser.getFullName());
						diag.show();				
					}
				}
			}
		});
		
		root = new AnchorPane();
		root.getChildren().addAll(search, viewUser);
		//setTop(new TextField());
		
		
		AnchorPane.setTopAnchor(search, Double.valueOf(49));
		AnchorPane.setLeftAnchor(search, Double.valueOf(27));
		
		AnchorPane.setTopAnchor(viewUser, Double.valueOf(75));
		AnchorPane.setLeftAnchor(viewUser, Double.valueOf(25));
		AnchorPane.setBottomAnchor(viewUser, Double.valueOf(20));
		AnchorPane.setRightAnchor(viewUser, Double.valueOf(260));
				
		generalData = new GeneralDataToShow();
		
		root.getChildren().add(generalData);
		
		AnchorPane.setTopAnchor(generalData, Double.valueOf(20));
		AnchorPane.setLeftAnchor(generalData, Double.valueOf(275));
		//AnchorPane.setBottomAnchor(dataBox, Double.valueOf(20));
		//AnchorPane.setRightAnchor(dataBox, Double.valueOf(5));
		
		setCenter(root);
		
		btnInsert = new OpButton("Insertar");
		btnInsert.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				UserDialogBox diag = new UserDialogBox(null);
				diag.show();
				generalData.setDefault();
			}
		});
		
		btnModify = new OpButton("Modificar");
		btnModify.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				UserItem item = viewUser.getSelectionModel().getSelectedItem();
				if(item != null){
					SmallUser user = item.getUser();
					try {
						int id = user.getId();
						User u = UserServices.getUser(id);
						UserDialogBox diag = new UserDialogBox(u);
						diag.show();
					} catch (SQLException | IOException e1) {
						DialogBox diag = new DialogBox(e1.getMessage());
						diag.show();
					}
				}
				else{
					DialogBox diag = new DialogBox("Debe seleccionar un usuario de la lista");
					diag.show();
				}
				generalData.setDefault();
			}
		});
		
		btnDelete = new OpButton("Eliminar");
		btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				UserItem item = viewUser.getSelectionModel().getSelectedItem();
				if(item != null){				
					SmallUser user = item.getUser();
					try{
						User u = UserServices.getUser(user.getId());
						DialogYesNoToCancelUser diag = new DialogYesNoToCancelUser("Realmente desea cancelar el usuario " + user.getFullName(), u);
						diag.show();
					} catch(SQLException | IOException e1){
						DialogBox diag = new DialogBox("Ocurrio un error al obtener los datos del usuario " + user.getFullName());
						diag.show();
					}
				}
				generalData.setDefault();
			}
		});
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		buttons = new HBox();
		buttons.setSpacing(10);
		buttons.setPadding(new Insets(5, 10, 15, 10));
		buttons.getChildren().addAll(spacer);
		
		setBottom(buttons);
	}


	public void setData() {
		viewUser.getItems().removeAll(viewUser.getItems());
		try{
			List<SmallUser> users = UserServices.getSmallUserDistincTo(GUI.getInstance().getUser().getId());
			for(SmallUser user : users){
				UserItem ui = new UserItem(user);
				viewUser.getItems().add(ui);
				currentUsers.add(ui);
			}
		}
		catch(SQLException | IOException e){
			e.printStackTrace();
		}
	}
	
	public void hideInsert(){
		buttons.getChildren().remove(btnInsert);
	}
	public void hideModify(){
		buttons.getChildren().remove(btnModify);
	}
	public void hideDelete(){
		buttons.getChildren().remove(btnDelete);
	}
	
	public void showInsert(){
		if(!buttons.getChildren().contains(btnInsert))
			buttons.getChildren().add(btnInsert);
	}
	public void showModify(){
		if(!buttons.getChildren().contains(btnModify))
			buttons.getChildren().add(btnModify);
	}
	public void showDelete(){
		if(!buttons.getChildren().contains(btnDelete))
			buttons.getChildren().add(btnDelete);
	}
	
	private List<UserItem> findCoincidences(String name){
		List<UserItem> items = new LinkedList<UserItem>();
		
		if(name == null || name.isEmpty() || Utilities.isEmpty(name))
			items = currentUsers;
		else{
			for(UserItem ui : currentUsers){
				if(ui.getUser().getFullName().contains(name))
					items.add(ui);
			}
		}
		
		return items;
	}
	
	
	class SearchPeople extends Region{
		private Button clearButton;
		private TextField searchField;
		
		public SearchPeople(){
			setId("SearchPeople");
			minHeight(24);
			maxHeight(24);
	        setPrefSize(150, 24);
	        
	        
			searchField = new TextField();
			searchField.setPromptText("Filtrar");
			searchField.setMinSize(120, 24);
			searchField.setPrefSize(120, 24);
			searchField.setMaxSize(120, 24);
			
			
			clearButton = new Button();
			clearButton.setVisible(false);
			
			getChildren().addAll(searchField, clearButton);
			
			searchField.textProperty().addListener(new ChangeListener<String>(){
				@Override
				public void changed(ObservableValue<? extends String> arg0,
						String oldCad, String newCad) {
					clearButton.setVisible(searchField.getText().length() != 0);
					List<UserItem> newItems = findCoincidences(newCad);
					viewUser.getItems().removeAll(viewUser.getItems());
					viewUser.getItems().addAll(newItems);
				}
				
			});				
			
			clearButton.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e){
					searchField.setText("");
					searchField.requestFocus();
					clearButton.setVisible(false);
				}
			});
			
			
			
		}
		@Override protected void layoutChildren() {
			searchField.resize(getWidth(),getHeight());
	        clearButton.resizeRelocate(getWidth()-20,6,12,13);
	    }

		private void setPrefSize(int i, int j) {
			// TODO Auto-generated method stub
			
		}
		
		public TextField getTextField(){
			return searchField;
		}
	}
	
}

class UserItem extends HBox {
	private SmallUser user;
	
	public UserItem(SmallUser user){
		setId("userItem");
		this.user = user;
		
		Label lblName = new Label(user.getFullName());
		
		Label lblTypeUser = new Label();
		if(user.isTeacher()){
			if(user.isMale())
				lblTypeUser.setText("Profesor");
			else
				lblTypeUser.setText("Profesora");
		}
		else{
			lblTypeUser.setText("Estudiante");
		}
		
		Label lblCanceled = new Label(user.isCanceled() ? "Deshabilitado" : "Habilitado");
		
		Image picture = new Image(getClass().getResource("../../../icons/unknow70.png").toExternalForm());
		if(user.getPicture() != null)
			try {
				java.awt.Image buffer = Utilities.scaleImageIcon(user.getPicture(), 64, 60);
				picture = Utilities.createImage(Utilities.createBufferedImage(buffer));
			} catch (IOException e) {;}
		ImageView viewPicture = new ImageView(picture);
		
		Region spacer = new Region();
		spacer.setMinHeight(5);
		spacer.setPrefHeight(5);
		spacer.setMaxHeight(5);
		
		VBox textBox = new VBox();
		textBox.getChildren().addAll(spacer, lblName, lblTypeUser, lblCanceled);
		
		setSpacing(7);
		getChildren().addAll(viewPicture, textBox);
	}
	
	public SmallUser getUser(){
		return user;
	}
}

class GeneralDataToShow extends AnchorPane{
	private Label title;
	
	private TextField txfFullName;
	private TextField txfCI;
	private TextField txfUser;	
	private ImageView avatar;
	private TextArea description;
	private Label lblPriv;
	private ListView<String> privileges;
	
	private Image unknown;
	
	public GeneralDataToShow(){
		setId("generalData");
		
		title = new Label("Datos generales");
		title.getStyleClass().add("title");
		
		unknown = new Image(getClass().getResource("../../../icons/unknow70.png").toExternalForm());		
		
		avatar = new ImageView(unknown);
		avatar.setId("avatar");
		
        txfFullName = new TextField();
        txfFullName.setPrefWidth(230);
        txfFullName.setEditable(false);
        txfFullName.setPromptText("Nombre completo");
		txfCI = new TextField(){
			@Override
			public void replaceText(int start, int end, String text) {
				if (!text.matches("[a-z]")) {
					super.replaceText(start, end, text);
				}
			}
			@Override
			public void replaceSelection(String text) {
				if (!text.matches("[a-z]")) {
					super.replaceSelection(text);
				}
			}
		};
		txfCI.setPrefWidth(110);
		txfCI.setPromptText("CI");
		txfCI.setEditable(false);
		txfUser = new TextField();
		txfUser.setPrefWidth(110);
		txfUser.setEditable(false);
		txfUser.setPromptText("Usuario");
		description = new TextArea();
		description.setEditable(false);
		description.setPrefWidth(230);
		description.setPrefHeight(70);
		description.setWrapText(true);
		lblPriv = new Label("Privilegios");
		privileges = new ListView<String>();
		privileges.setPrefWidth(230);
		privileges.setPrefHeight(120);

		AnchorPane.setLeftAnchor(title, Double.valueOf(10));
		AnchorPane.setTopAnchor(title, Double.valueOf(5));
		
		AnchorPane.setLeftAnchor(txfUser, Double.valueOf(0));
		AnchorPane.setTopAnchor(txfUser, Double.valueOf(70));

		AnchorPane.setLeftAnchor(txfFullName, Double.valueOf(0));
		AnchorPane.setTopAnchor(txfFullName, Double.valueOf(100));
		
		AnchorPane.setLeftAnchor(txfCI, Double.valueOf(0));
		AnchorPane.setTopAnchor(txfCI, Double.valueOf(130));
		
		AnchorPane.setLeftAnchor(avatar, Double.valueOf(130));
		AnchorPane.setTopAnchor(avatar, Double.valueOf(30));
		
		AnchorPane.setLeftAnchor(description, Double.valueOf(0));
		AnchorPane.setTopAnchor(description, Double.valueOf(160));
		
		AnchorPane.setLeftAnchor(lblPriv, Double.valueOf(0));
		AnchorPane.setTopAnchor(lblPriv, Double.valueOf(240));
		
		AnchorPane.setLeftAnchor(privileges, Double.valueOf(0));
		AnchorPane.setTopAnchor(privileges, Double.valueOf(255));
		
		getChildren().addAll(
				 title,
				 txfFullName,
				 txfCI,
				 avatar,
				 txfUser,
				 description,
				 lblPriv,
				 privileges
				);
	}
	
	public void setData(User user){
		if(user != null){
			txfFullName.setText(user.getFullName());
			txfCI.setText(String.valueOf(user.getCi()));
			txfUser.setText(user.getUserName());
			
			Image avatarImg = unknown;
			try {
				BufferedImage picture = user.getPicture();
				if(picture != null){
					if(picture.getWidth(null) > 64 || picture.getHeight(null) > 60){
						picture = Utilities.createBufferedImage(Utilities.scaleImageIcon(picture, 64, 60));
						avatarImg = Utilities.createImage(picture);
					}
					else
						avatarImg = Utilities.createImage(picture);
				}
			} catch (IOException e1) {
				DialogBox diag = new DialogBox("No se pudo cargar el avatar del usuario " + user.getFullName());
				diag.show();
				avatarImg = unknown;
			}
			avatar.setImage(avatarImg);
			
			privileges.getItems().removeAll(privileges.getItems());
			
			try{
				List<String> privs = PrivilegeServices.getPrivilegeNames(user.getId());
				Privilege privilege = new Privilege(privs);
				lblPriv.setText(GUI.calculateOnlyPrivilege(privilege));
				privileges.getItems().addAll(privs);
			}catch(SQLException e){
				DialogBox diag = new DialogBox("No se pudieron obtener los privilegios del usuario " + user.getFullName());
				diag.show();
			}
			
			if(user instanceof Teacher){
				Teacher teacher = (Teacher)user;
				String text = "Profesor " + teacher.getTeachingCategory() + ".\n";
				text += "Pertenece a la asignatura " + teacher.getSubject() + "; ";
				if(teacher.isTeaches())
					text += "y está activo.\n";
				else
					text += "y no está activo.\n";
				if(teacher.getScientificCategory().equalsIgnoreCase("Ninguna"))
					text += "No tiene categoría científica.";
				else
					text += "Categoría científica: " + teacher.getScientificCategory() + ".";
				description.setText(text);
			}
			else{
				Student student = (Student)user;
				String text = "Estudiante del grupo " + student.getTeachingGroup() + ".\n";
				if(student.getResearchGroup().equalsIgnoreCase("Ninguno"))
					text += "No está en ningún grupo investigativo";
				else
					text += "Grupo investigativo: " + student.getResearchGroup() + ".";
				description.setText(text);
			}
		}
	}
	
	public void setDefault(){
		txfFullName.setText("");
		txfCI.setText("");
		txfUser.setText("");
		avatar.setImage(unknown);
		description.setText("");
		lblPriv.setText("Privilegios");
		privileges.getItems().removeAll(privileges.getItems());
	}

}