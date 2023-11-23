package visual.controls.center.userCountPage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;

import models.Privilege;
import models.ResearchGroup;
import models.Rol;
import models.ScientificCategory;
import models.Subject;
import models.TeachingCategory;
import models.TeachingGroup;
import models.users.Student;
import models.users.Teacher;
import models.users.User;
import services.ResearchGroupServices;
import services.RolServices;
import services.ScientificCategoryServices;
import services.SubjectServices;
import services.TeachingCategoryServices;
import services.TeachingGroupServices;
import services.UserServices;
import utils.DialogBox;
import utils.GUI;
import utils.NumberField;
import utils.Utilities;
import utils.ValidationErrorException;
import visual.Main;
import visual.controls.Notification;

public class UserDialogBox extends BorderPane{
	public static final double WIDTH = 560;
	public static final double HEIGHT = 540;
	
	private static boolean editPrivilege;
	private StackPane dialogRoot;
	private User user;
	
	private GeneralDataToInsert generalData;
	private TeacherDataToInsert teacherData;
	private StudentDataToInsert studentData;
	
	private HBox buttons;
	private Button btnSave;
	private Button btnCancel;
	
	private TabPane tabs;
	private Tab tabData;
	private VBox data;
	private Tab tabPrivilege;
	private PrivilegeData privileges;
	
	private ToggleButton toggleTeacher;
	private ToggleButton toggleStudent;
	private ToggleGroup toggleGroup;
	private HBox togglePane;
	
	public static void setEditPrivilege(boolean edit){
		editPrivilege = edit;
	}
	
	public UserDialogBox(User user){
		setId("userDialogBox");
		this.user = user;
		
		setMinSize(WIDTH, HEIGHT);
		setPrefSize(WIDTH, HEIGHT);
		setMaxSize(WIDTH, HEIGHT);
		
		Region spacer1 = new Region();
		HBox.setHgrow(spacer1, Priority.ALWAYS);
		
		btnSave = new Button("Guardar");
		btnSave.setMinSize(80, 20);
		btnSave.setPrefSize(80, 20);
		btnSave.setMaxSize(80, 20);
		btnSave.setDefaultButton(true);
		btnSave.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				boolean success = true;
				String textNotification = "";
				if(UserDialogBox.this.user == null)
					textNotification += "Se ha creado satisfactoriamente el ";
				else
					textNotification += "Se han modificado satisfactoriamente los datos del ";
				try {
					Object[] general = generalData.saveData();
					String name = (String)general[0];
					String firstLastName = (String)general[1];
					String secondLastName = (String)general[2];
					long ci = Long.valueOf((String)general[3]).longValue();
					String user = (String)general[4];
					String password = (String)general[5];
					File picture = (File)general[6];
					boolean validPicture = false;
					if(picture != null)
						validPicture = true;
					boolean canceled = ((Boolean)general[7]).booleanValue();
					
					List<Rol> roles = privileges.saveData();
					
					if(toggleStudent.isSelected()){
						textNotification += "estudiante ";
						
						Object[] student = studentData.saveData();
						int teachingGroup = ((Integer)student[0]).intValue();
						int researchGroup = ((Integer)student[1]).intValue();
						
						if(UserDialogBox.this.user == null){
							UserServices.insertUser(ci, name,
									firstLastName, secondLastName,
									picture, user, password,
									false, false,
									-1, -1, -1,
									teachingGroup, researchGroup,
									roles, canceled);
						}
						else{
							UserServices.modifyUser(UserDialogBox.this.user.getId(), 
											ci, name, firstLastName, secondLastName, picture, 
											user, password, false, false, canceled, -1, -1, -1,
											teachingGroup, researchGroup, roles, validPicture);
						}
					}
					else{
						textNotification += "profesor ";
						
						Object[] teacher = teacherData.saveData();
						int subject = ((Integer)teacher[0]).intValue();
						boolean teaches = ((Boolean)teacher[1]).booleanValue();
						int teachingCategory = ((Integer)teacher[2]).intValue();
						int scientificCategory = ((Integer)teacher[3]).intValue();
						
						if(UserDialogBox.this.user == null){
							UserServices.insertUser(ci, name, firstLastName, secondLastName, picture, user, password, 
								  					true, teaches, scientificCategory, teachingCategory, subject, 
								  					-1, -1, roles, canceled);
						}
						else{
							UserServices.modifyUser(UserDialogBox.this.user.getId(),
													ci, name, firstLastName, secondLastName, 
													picture, user, password, true, 
													teaches, canceled, scientificCategory, teachingCategory, subject,
													-1, -1, roles, validPicture);
						}
					}
					
					textNotification += name + " " + firstLastName + " " + secondLastName + " (" + user + ")";
					
					
				} catch (ValidationErrorException | FileNotFoundException | SQLException e1) {
					DialogBox diag = new DialogBox(e1.getMessage());
					diag.show();
					//e1.printStackTrace();
					success = false;
				}
				
				if(success){
					Notification.getInstance().setText(textNotification);
					Notification.getInstance().showNotification();
					dialogRoot.getChildren().remove(UserDialogBox.this);
					Main.getStackRoot().getChildren().remove(dialogRoot);
					OtherUsersPane.getInstance().setData();
				}
			}
		});
		
		btnCancel = new Button("Cancelar");
		btnCancel.setMinSize(80, 20);
		btnCancel.setPrefSize(80, 20);
		btnCancel.setMaxSize(80, 20);
		btnCancel.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				dialogRoot.getChildren().remove(UserDialogBox.this);
				Main.getStackRoot().getChildren().remove(dialogRoot);
			}
		});
		
		buttons = new HBox();
		buttons.setSpacing(7);
		buttons.getChildren().addAll(spacer1, btnSave, btnCancel);
		
		generalData = new GeneralDataToInsert(this.user);
		if(this.user != null){
			if(this.user instanceof Teacher)
				teacherData = new TeacherDataToInsert((Teacher)this.user);
			else
				studentData = new StudentDataToInsert((Student)this.user);
		}
		else{
			teacherData = new TeacherDataToInsert(null);
			studentData = new StudentDataToInsert(null);
		}
		
		toggleGroup = new ToggleGroup();
		toggleTeacher = new ToggleButton("Profesor");
		toggleTeacher.setToggleGroup(toggleGroup);
		toggleTeacher.setMinSize(80, 25);
		toggleTeacher.setPrefSize(80, 25);
		toggleTeacher.setMaxSize(80, 25);
		toggleTeacher.getStyleClass().add("first");
		toggleTeacher.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				toggleTeacher.setSelected(true);
				if(!data.getChildren().contains(teacherData)){
					data.getChildren().remove(studentData);
					data.getChildren().add(teacherData);
				}					
			}
		});
		
		toggleStudent = new ToggleButton("Estudiante");
		toggleStudent.setToggleGroup(toggleGroup);
		toggleStudent.setMinSize(80, 25);
		toggleStudent.setPrefSize(80, 25);
		toggleStudent.setMaxSize(80, 25);
		toggleStudent.getStyleClass().add("last");
		toggleStudent.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				toggleStudent.setSelected(true);
				if(!data.getChildren().contains(studentData)){
					data.getChildren().remove(teacherData);
					data.getChildren().add(studentData);
				}
			}
		});
		
		
		Region spacer2 = new Region();
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		Region spacer3 = new Region();
		HBox.setHgrow(spacer3, Priority.ALWAYS);
		
		togglePane = new HBox();
		togglePane.getChildren().addAll(spacer2, toggleTeacher, toggleStudent, spacer3);
		
		data = new VBox();
		tabData = new Tab("Nuevo usuario");
		tabData.setContent(data);
		
		privileges = new PrivilegeData();
		tabPrivilege = new Tab("Difinir privilegios");
		tabPrivilege.setContent(privileges);
		
		tabs = new TabPane();
		tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		tabs.getTabs().add(tabData);
		
		if(user != null){
			if(user instanceof Teacher){
				toggleStudent.setDisable(true);
				data.getChildren().addAll(generalData, togglePane, teacherData);
				toggleTeacher.setSelected(true);
			}
			else{
				toggleTeacher.setDisable(true);
				data.getChildren().addAll(generalData, togglePane, studentData);
				toggleStudent.setSelected(true);
			}
			tabData.setText("Modificación de usuario");
			tabPrivilege.setText("Redefinir privilegios");
			
			if(editPrivilege)
				tabs.getTabs().add(tabPrivilege);
		}
		else{
			toggleTeacher.setSelected(true);
			tabs.getTabs().add(tabPrivilege);
			data.getChildren().addAll(generalData, togglePane, teacherData);
		}
		
		
		setCenter(tabs);
		setBottom(buttons);
		
		dialogRoot = new StackPane();
		dialogRoot.setId("dialogRoot");
		dialogRoot.getChildren().add(UserDialogBox.this);
	}

	
	public void show(){
		Main.getStackRoot().getChildren().add(dialogRoot);
		try {
			privileges.adjust(user);
		} catch (SQLException e) {
			DialogBox diag = new DialogBox("No se pudieron establecer todos los parámetros de privilegio");
			diag.show();
		}		
		generalData.requestFocus();
	}

}

class GeneralDataToInsert extends AnchorPane{
	private User user;
	
	private Label title;
	
	private Label lblName;
	private Label lblFirstLastName;
	private Label lblSecondLastName;
	private Label lblCI;
	private Label lblCancelado;
	private Label lblUser;
	private Label lblPassword;
	
	private TextField txfName;
	private TextField txfFirstLastName;
	private TextField txfSecondLastName;
	private NumberField txfCI;
	private CheckBox chkCancelado;
	private TextField txfUser;
	private PasswordField psfPassword;
	
	private ImageView avatar;
	private Hyperlink changeAvatar;

	private File imgPath = null;
	private FileChooser chooser;
	
	private Image unknown;
	
	public GeneralDataToInsert(User user){
		setId("generalData");
		this.user = user;
		
		title = new Label("Datos generales");
		title.getStyleClass().add("title");
		
		unknown = new Image(getClass().getResource("../../../icons/unknow70.png").toExternalForm());
		
		lblName = new Label("Nombre:");
		lblName.setPrefWidth(110);
		lblName.getStyleClass().add("bold");
		lblFirstLastName = new Label("Primer apellido:");
		lblFirstLastName.setPrefWidth(110);
		lblFirstLastName.getStyleClass().add("bold");
		lblSecondLastName = new Label("Segundo apellido:");
		lblSecondLastName.setPrefWidth(110);
		lblSecondLastName.getStyleClass().add("bold");
		lblCI = new Label("CI:");
		lblCI.setPrefWidth(110);
		lblCI.getStyleClass().add("bold");
		lblCancelado = new Label("Cancelado:");
		lblCancelado.setPrefWidth(110);
		lblCancelado.getStyleClass().add("bold");
		lblUser = new Label("Usuario:");
		lblUser.setPrefWidth(75);
		lblUser.getStyleClass().add("bold");
		lblPassword = new Label("Contraseña:");
		lblPassword.setPrefWidth(75);
		lblPassword.getStyleClass().add("bold");
		
		
		avatar = new ImageView(unknown);
		avatar.setId("avatar");
		chooser = new FileChooser();
		chooser.setTitle("Seleccionar avatar");
        chooser.getExtensionFilters().addAll(
        		new FileChooser.ExtensionFilter("png", "*.png"),
        		new FileChooser.ExtensionFilter("jpg", "*.jpg"),
        		new FileChooser.ExtensionFilter("jpeg", "*.jpeg")
        		);
        
        changeAvatar = new Hyperlink("Cambiar avatar");
        changeAvatar.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e){
        		imgPath = chooser.showOpenDialog(Main.getScene().getWindow());
        		if(imgPath != null){        		
	        		Image avatarImg = unknown;
	    			try {
	    				BufferedImage picture = ImageIO.read(imgPath);
	    				if(picture != null){
	    					if(picture.getWidth(null) > 64 || picture.getHeight(null) > 60){
	    						picture = Utilities.createBufferedImage(Utilities.scaleImageIcon(picture, 64, 60));
	    						avatarImg = Utilities.createImage(picture);
	    					}
	    					else
	    						avatarImg = Utilities.createImage(picture);
	    				}
	    			} catch (IOException e1) {
	    				DialogBox diag = new DialogBox("No se pudo cargar su avatar");
	    				diag.show();
	    				avatarImg = unknown;
	    			}
	    			avatar.setImage(avatarImg);
        		}
        	}
        });
		
		txfName = new TextField();
		txfName.setPrefWidth(110);
		txfName.setPromptText("Nombre");
		txfFirstLastName = new TextField();
		txfFirstLastName.setPrefWidth(110);
		txfFirstLastName.setPromptText("Primer apellido");
		txfSecondLastName = new TextField();
		txfSecondLastName.setPrefWidth(110);
		txfSecondLastName.setPromptText("Segundo apellido");
		txfCI = new NumberField();
		txfCI.setPrefWidth(110);
		txfCI.setPromptText("###########");
		chkCancelado = new CheckBox();
		txfUser = new TextField();
		txfUser.setPrefWidth(110);
		txfUser.setPromptText("Nombre usuario");
		psfPassword = new PasswordField();
		psfPassword.setPrefWidth(110);
		psfPassword.setPromptText("Contraseña");
		
		if(this.user != null){
			txfName.setText(this.user.getName());
			txfFirstLastName.setText(this.user.getFirstLastName());
			txfSecondLastName.setText(this.user.getSecondLastName());
			txfCI.setText(String.valueOf(this.user.getCi()));
			chkCancelado.setSelected(user.isCanceled());
			txfUser.setText(this.user.getUserName());
			psfPassword.setText(this.user.getPassword());
			
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
				DialogBox diag = new DialogBox("No se pudo cargar su avatar");
				diag.show();
				avatarImg = unknown;
			}
			avatar.setImage(avatarImg);
		}
		
		AnchorPane.setLeftAnchor(title, Double.valueOf(105));
		AnchorPane.setTopAnchor(title, Double.valueOf(20));
		
		AnchorPane.setLeftAnchor(lblName, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblName, Double.valueOf(75));

		AnchorPane.setLeftAnchor(txfName, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfName, Double.valueOf(70));
		
		AnchorPane.setLeftAnchor(lblFirstLastName, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblFirstLastName, Double.valueOf(110));

		AnchorPane.setLeftAnchor(txfFirstLastName, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfFirstLastName, Double.valueOf(105));
		
		AnchorPane.setLeftAnchor(lblSecondLastName, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblSecondLastName, Double.valueOf(145));
		
		AnchorPane.setLeftAnchor(txfSecondLastName, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfSecondLastName, Double.valueOf(140));
		
		AnchorPane.setLeftAnchor(lblCI, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblCI, Double.valueOf(180));
		
		AnchorPane.setLeftAnchor(txfCI, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfCI, Double.valueOf(175));
		
		AnchorPane.setLeftAnchor(lblCancelado, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblCancelado, Double.valueOf(215));
		
		AnchorPane.setLeftAnchor(chkCancelado, Double.valueOf(160));
		AnchorPane.setTopAnchor(chkCancelado, Double.valueOf(215));
		
		AnchorPane.setLeftAnchor(avatar, Double.valueOf(390));
		AnchorPane.setTopAnchor(avatar, Double.valueOf(40));
		
		AnchorPane.setLeftAnchor(changeAvatar, Double.valueOf(385));
		AnchorPane.setTopAnchor(changeAvatar, Double.valueOf(105));
		
		AnchorPane.setLeftAnchor(lblUser, Double.valueOf(300));
		AnchorPane.setTopAnchor(lblUser, Double.valueOf(145));
		
		AnchorPane.setLeftAnchor(txfUser, Double.valueOf(385));
		AnchorPane.setTopAnchor(txfUser, Double.valueOf(140));
		
		AnchorPane.setLeftAnchor(lblPassword, Double.valueOf(300));
		AnchorPane.setTopAnchor(lblPassword, Double.valueOf(180));
		
		AnchorPane.setLeftAnchor(psfPassword, Double.valueOf(385));
		AnchorPane.setTopAnchor(psfPassword, Double.valueOf(175));
		
		getChildren().addAll(title, 
				 lblName, txfName,
				 lblFirstLastName, txfFirstLastName,
				 lblSecondLastName, txfSecondLastName,
				 lblCI, txfCI,
				 lblCancelado, chkCancelado,
				 avatar, changeAvatar,
				 lblUser, txfUser,
				 lblPassword, psfPassword
				);
	}	
	
	public Object[] saveData() throws ValidationErrorException{
		String name = txfName.getText();
		if(name == null || name.isEmpty() || Utilities.isEmpty(name))
			throw new ValidationErrorException("El campo \"Nombre\" no debe estar vacio");
		
		String firstLastName = txfFirstLastName.getText();
		if(firstLastName == null || firstLastName.isEmpty() || Utilities.isEmpty(firstLastName))
			throw new ValidationErrorException("El campo \"Primer apellido\" no debe estar vacio");
		
		String secondLastName = txfSecondLastName.getText();
		if(secondLastName == null || secondLastName.isEmpty() || Utilities.isEmpty(secondLastName))
			throw new ValidationErrorException("El campo \"Segundo apellido\" no debe estar vacio");
		
		String ci = txfCI.getText();
		if(ci == null || ci.isEmpty() || Utilities.isEmpty(ci))
			throw new ValidationErrorException("El campo \"CI\" no debe estar vacio");
		if(ci.length() < 11)
			throw new ValidationErrorException("El campo \"CI\" debe tener 11 digitos");
		
		String user = txfUser.getText();
		if(user == null || user.isEmpty() || Utilities.isEmpty(user))
			throw new ValidationErrorException("El campo \"Usuario\" no debe estar vacio");
		if(user.length() < 4)
			throw new ValidationErrorException("El campo \"Usuario\" debe tener al menos 4 caracteres");
		
		String password = psfPassword.getText();
		if(password == null || password.isEmpty() || Utilities.isEmpty(password))
			throw new ValidationErrorException("El campo \"Contraseña\" no debe estar vacio");
		if(password.length() < 4)
			throw new ValidationErrorException("El campo \"Contraseña\" debe tener al menos 4 caracteres");
		
		//if(imgPath == )
		
		
		return new Object[]{name, firstLastName, secondLastName, ci, user, password, imgPath, Boolean.valueOf(chkCancelado.isSelected())};//imgPath toma la dirrecion de la nueva imagen
	}
	
	public void requestFocus(){
		txfName.requestFocus();
	}
}

class TeacherDataToInsert extends AnchorPane{
	private Teacher user;
	
	private Label title;
	
	private Label lblSubject;
	private Label lblTeaches;
	private Label lblTeachingCategory;
	private Label lblScientificCategory;
	
	private ChoiceBox<Subject> txfSubject;
	private CheckBox txfTeaches;
	private ChoiceBox<TeachingCategory> txfTeachingCategory;
	private ChoiceBox<ScientificCategory> txfScientificCategory;

	
	public TeacherDataToInsert(Teacher user){
		setId("teacherData");
		this.user = user;
		
		title = new Label("Datos como profesor");
		title.getStyleClass().add("title");
		title.setId("title");
		
		lblSubject = new Label("Asignatura:");
		lblSubject.getStyleClass().add("bold");
		lblTeaches = new Label("Activo:");
		lblTeaches.getStyleClass().add("bold");
		lblTeachingCategory = new Label("Categoría docente:");
		lblTeachingCategory.getStyleClass().add("bold");
		lblScientificCategory = new Label("Categoría científica:");
		lblScientificCategory.getStyleClass().add("bold");
		
		txfSubject = new ChoiceBox<Subject>();
		txfSubject.setPrefWidth(200);
		txfTeaches = new CheckBox();
		txfTeachingCategory = new ChoiceBox<TeachingCategory>();
		txfTeachingCategory.setPrefWidth(200);
		txfScientificCategory = new ChoiceBox<ScientificCategory>();
		txfScientificCategory.setPrefWidth(200);
		
		try {
			User currentUser = GUI.getInstance().getUser();
			
			List<Subject> subjects = null;
			if(currentUser.getPrivilege().isModifySubject())
				subjects = SubjectServices.getSubject();
			else
				subjects = SubjectServices.getEnabledSubject();
			txfSubject.getItems().removeAll(txfSubject.getItems());
			txfSubject.getItems().addAll(subjects);
			
			List<TeachingCategory> teachingCats = null;
			if(currentUser.getPrivilege().isModifyTeachingCategory())
				teachingCats = TeachingCategoryServices.getTeachingCategory();
			else
				teachingCats = TeachingCategoryServices.getEnabledTeachingCategory();
			txfTeachingCategory.getItems().removeAll(txfTeachingCategory.getItems());
			txfTeachingCategory.getItems().addAll(teachingCats);
			
			List<ScientificCategory> scientificCats = null;
			if(currentUser.getPrivilege().isModifyScientificCategory())
				scientificCats = ScientificCategoryServices.getTeachingCategory();
			else
				scientificCats = ScientificCategoryServices.getEnabledTeachingCategory();
			txfScientificCategory.getItems().removeAll(txfScientificCategory.getItems());
			txfScientificCategory.getItems().addAll(scientificCats);
			
			if(this.user != null){
				txfTeaches.setSelected(this.user.isTeaches());
				
				Subject currentSubject = Subject.find(subjects, this.user.getSubject());
				txfSubject.getSelectionModel().select(currentSubject);
				
				TeachingCategory currentTCat = TeachingCategory.find(teachingCats, this.user.getTeachingCategory());
				txfTeachingCategory.getSelectionModel().select(currentTCat);
				
				ScientificCategory currentScat = ScientificCategory.find(scientificCats, this.user.getScientificCategory());
				txfScientificCategory.getSelectionModel().select(currentScat);
			}
			
		} catch (SQLException e) {
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
		
		AnchorPane.setLeftAnchor(title, Double.valueOf(105));
		AnchorPane.setTopAnchor(title, Double.valueOf(15));
		
		AnchorPane.setLeftAnchor(lblSubject, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblSubject, Double.valueOf(65));
		
		AnchorPane.setLeftAnchor(txfSubject, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfSubject, Double.valueOf(55));
		
		AnchorPane.setLeftAnchor(lblTeaches, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblTeaches, Double.valueOf(100));
		
		AnchorPane.setLeftAnchor(txfTeaches, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfTeaches, Double.valueOf(95));
		
		AnchorPane.setLeftAnchor(lblTeachingCategory, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblTeachingCategory, Double.valueOf(135));
		
		AnchorPane.setLeftAnchor(txfTeachingCategory, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfTeachingCategory, Double.valueOf(125));
		
		AnchorPane.setLeftAnchor(lblScientificCategory, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblScientificCategory, Double.valueOf(170));
		
		AnchorPane.setLeftAnchor(txfScientificCategory, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfScientificCategory, Double.valueOf(160));
		
		getChildren().addAll(title,
				 lblSubject, txfSubject,
				 lblTeaches, txfTeaches,
				 lblTeachingCategory, txfTeachingCategory,
				 lblScientificCategory, txfScientificCategory
				);
	}
	
	public Object[] saveData() throws SQLException{
		Integer subject = -1;
		Subject subjectObj = txfSubject.getSelectionModel().getSelectedItem();
		if(subjectObj == null){
			subject = Integer.valueOf(SubjectServices.getSubjectIdByName(((Teacher)GUI.getInstance().getUser()).getSubject()));
		}
		else
			subject = Integer.valueOf(subjectObj.getId());
		
		Boolean teaches = Boolean.valueOf(txfTeaches.isSelected());
		
		Integer teachingCategory = -1;
		TeachingCategory tcat = txfTeachingCategory.getSelectionModel().getSelectedItem();
		if(tcat == null){
			teachingCategory = Integer.valueOf(TeachingCategoryServices.getTeachingCategoryIdByName(((Teacher)GUI.getInstance().getUser()).getTeachingCategory()));
		}
		else
			teachingCategory = Integer.valueOf(tcat.getId());
		
		Integer scientificCategory = -1;
		ScientificCategory scat = txfScientificCategory.getSelectionModel().getSelectedItem();
		if(scat == null){
			scientificCategory = Integer.valueOf(ScientificCategoryServices.getScientificCategoryIdByName(((Teacher)GUI.getInstance().getUser()).getScientificCategory()));
		}
		else
			scientificCategory = Integer.valueOf(scat.getId());
		
		return new Object[]{subject, teaches, teachingCategory, scientificCategory};
	}
}

class StudentDataToInsert extends AnchorPane{
	private Student user;
	
	private Label title;
	
	private Label lblTeachingGroup;
	private Label lblResearchGroup;
	
	private ChoiceBox<TeachingGroup> txfTeachingGroup;
	private ChoiceBox<ResearchGroup> txfResearchGroup;
	
	private Label lblTeachingGroupDatum;
	private Label lblResearchGroupDatum;
	
	public StudentDataToInsert(Student user){
		setId("studentData");
		this.user = user;
		
		title = new Label("Datos como estudiante");
		title.getStyleClass().add("title");
		title.setId("title");
		
		lblTeachingGroup = new Label("Grupo docente:");
		lblTeachingGroup.getStyleClass().add("bold");
		lblResearchGroup = new Label("Grupo investigativo:");
		lblResearchGroup.getStyleClass().add("bold");
		
		lblTeachingGroupDatum = new Label();
		lblResearchGroupDatum = new Label();
		
		txfTeachingGroup = new ChoiceBox<TeachingGroup>();
		txfTeachingGroup.setPrefWidth(200);
		txfResearchGroup = new ChoiceBox<ResearchGroup>();
		txfResearchGroup.setPrefWidth(200);
		
		try {
			User currentUser = GUI.getInstance().getUser();
			
			List<TeachingGroup> teachingGroups = null;
			if(currentUser.getPrivilege().isModifyTeachingGroup())
				teachingGroups = TeachingGroupServices.getTeachingGroups();
			else
				teachingGroups = TeachingGroupServices.getEnabledTeachingGroups();
			txfTeachingGroup.getItems().removeAll(txfTeachingGroup.getItems());
			txfTeachingGroup.getItems().addAll(teachingGroups);
			
			List<ResearchGroup> researchGroups = null;
			if(currentUser.getPrivilege().isModifyResearchGroup())
				researchGroups = ResearchGroupServices.getResearchGroups();
			else
				researchGroups = ResearchGroupServices.getEnabledResearchGroups();
			txfResearchGroup.getItems().removeAll(txfResearchGroup.getItems());
			txfResearchGroup.getItems().addAll(researchGroups);
				
			if(user != null){
				TeachingGroup currentTGroup = TeachingGroup.find(teachingGroups, this.user.getTeachingGroup());
				txfTeachingGroup.getSelectionModel().select(currentTGroup);
				
				ResearchGroup currentRGroup = ResearchGroup.find(researchGroups, this.user.getResearchGroup());
				txfResearchGroup.getSelectionModel().select(currentRGroup);
			}
			
		} catch (SQLException e) {
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
		
		
		AnchorPane.setLeftAnchor(title, Double.valueOf(105));
		AnchorPane.setTopAnchor(title, Double.valueOf(15));
		
		AnchorPane.setLeftAnchor(lblTeachingGroup, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblTeachingGroup, Double.valueOf(65));
		
		AnchorPane.setLeftAnchor(lblTeachingGroupDatum, Double.valueOf(160));
		AnchorPane.setTopAnchor(lblTeachingGroupDatum, Double.valueOf(65));
		
		AnchorPane.setLeftAnchor(txfTeachingGroup, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfTeachingGroup, Double.valueOf(55));
		
		AnchorPane.setLeftAnchor(lblResearchGroup, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblResearchGroup, Double.valueOf(100));
		
		AnchorPane.setLeftAnchor(lblResearchGroupDatum, Double.valueOf(160));
		AnchorPane.setTopAnchor(lblResearchGroupDatum, Double.valueOf(100));
		
		AnchorPane.setLeftAnchor(txfResearchGroup, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfResearchGroup, Double.valueOf(90));
		
		getChildren().addAll(title,
				 lblTeachingGroup, txfTeachingGroup,
				 lblResearchGroup, txfResearchGroup
				);

	}
	
	public Object[] saveData() throws SQLException{
		Integer teachingGroup = -1;
		TeachingGroup tgroup = txfTeachingGroup.getSelectionModel().getSelectedItem();
		if(tgroup == null){
			teachingGroup = Integer.valueOf(TeachingGroupServices.getTeachingGroupIdByName(((Student)GUI.getInstance().getUser()).getTeachingGroup()));
		}
		else
			teachingGroup = Integer.valueOf(tgroup.getId());
		
		Integer researchGroup = -1;
		ResearchGroup sgroup = txfResearchGroup.getSelectionModel().getSelectedItem();
		if(sgroup == null){
			researchGroup = Integer.valueOf(ResearchGroupServices.getResearchGroupIdByName(((Student)GUI.getInstance().getUser()).getResearchGroup()));
		}
		else
			researchGroup = Integer.valueOf(sgroup.getId());
		
		return new Object[]{teachingGroup, researchGroup};
	}
}

class PrivilegeData extends BorderPane{
	private Label title;
	
	private ListView<Rol> allRolesList;
	private ListView<Rol> rolesToAsign;
	
	private Button btnPutAll;
	private Button btnPut;
	private Button btnRemove;
	private Button btnRemoveAll;
	
	public PrivilegeData(){
		setId("privilegeData");
		
		title = new Label("Privilegio: ?");
		title.getStyleClass().add("title");		
		setTop(title);
		
		HBox center = new HBox();
		center.setSpacing(10);
		
		allRolesList = new ListView<Rol>();
		allRolesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		HBox.setHgrow(allRolesList, Priority.ALWAYS);
		
		Region spacer2 = new Region();
		VBox.setVgrow(spacer2, Priority.ALWAYS);
		
		btnPutAll = new Button();
		btnPutAll.setMinSize(32, 32);
		btnPutAll.setPrefSize(32, 32);
		btnPutAll.setMaxSize(32, 32);
		ImageView viewPutAll = new ImageView(new Image(getClass().getResource("../../../icons/arrow_switch_right.png").toExternalForm(), 28, 32, true, false));
		btnPutAll.setGraphic(viewPutAll);
		btnPutAll.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				rolesToAsign.getItems().addAll(allRolesList.getItems());
				allRolesList.getItems().removeAll(allRolesList.getItems());
				changeTitle();
			}
		});
		
		btnPut = new Button();
		btnPut.setMinSize(32, 32);
		btnPut.setPrefSize(32, 32);
		btnPut.setMaxSize(32, 32);
		ImageView viewPut = new ImageView(new Image(getClass().getResource("../../../icons/arrow_right.png").toExternalForm(), 28, 32, true, false));
		btnPut.setGraphic(viewPut);
		btnPut.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				List<Rol> list = allRolesList.getSelectionModel().getSelectedItems().subList(0, allRolesList.getSelectionModel().getSelectedItems().size());
				rolesToAsign.getItems().addAll(list);
				allRolesList.getItems().removeAll(list);
				changeTitle();
			}
		});
		
		btnRemove = new Button();
		btnRemove.setMinSize(32, 32);
		btnRemove.setPrefSize(32, 32);
		btnRemove.setMaxSize(32, 32);
		ImageView viewRemove = new ImageView(new Image(getClass().getResource("../../../icons/arrow_left.png").toExternalForm(), 28, 32, true, false));
		btnRemove.setGraphic(viewRemove);
		btnRemove.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				List<Rol> list = rolesToAsign.getSelectionModel().getSelectedItems().subList(0, rolesToAsign.getSelectionModel().getSelectedItems().size());
				allRolesList.getItems().addAll(list);
				rolesToAsign.getItems().removeAll(list);
				changeTitle();
			}
		});
		
		btnRemoveAll = new Button();
		btnRemoveAll.setMinSize(32, 32);
		btnRemoveAll.setPrefSize(32, 32);
		btnRemoveAll.setMaxSize(32, 32);
		ImageView viewRemoveAll = new ImageView(new Image(getClass().getResource("../../../icons/arrow_switch_left.png").toExternalForm(), 28, 32, true, false));
		btnRemoveAll.setGraphic(viewRemoveAll);
		btnRemoveAll.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				allRolesList.getItems().addAll(rolesToAsign.getItems());
				rolesToAsign.getItems().removeAll(rolesToAsign.getItems());
				changeTitle();
			}
		});
		
		Region spacer3 = new Region();
		VBox.setVgrow(spacer3, Priority.ALWAYS);
		
		VBox buttons = new VBox();
		buttons.setSpacing(10);
		buttons.getChildren().addAll(spacer2, btnPutAll, btnPut, btnRemove, btnRemoveAll, spacer3);
		
		rolesToAsign = new ListView<Rol>();
		rolesToAsign.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		HBox.setHgrow(rolesToAsign, Priority.ALWAYS);
		
		center.getChildren().addAll(allRolesList, buttons, rolesToAsign);
		
		VBox boxCenter = new VBox();
		
		Region spacer4 = new Region();
		spacer4.setMinHeight(30);
		spacer4.setPrefHeight(30);
		spacer4.setMaxHeight(30);
		
		boxCenter.getChildren().addAll(center, spacer4);
		
		setCenter(boxCenter);
	}
	
	private void changeTitle(){
		List<Rol> rolesUser = rolesToAsign.getItems();
		Privilege priv = new Privilege(rolesUser);
		title.setText(GUI.calculateOnlyPrivilege(priv));
	}
	
	public void adjust(User user) throws SQLException{
		Privilege priv = null;
		List<Rol> rolesThisUser = RolServices.getRolesByUser(GUI.getInstance().getUser().getId());
		
		if(user == null){
			priv = GUI.getInstance().getUser().getPrivilege();
			allRolesList.getItems().addAll(rolesThisUser);
		}
		else{
			List<Rol> rolesUser = RolServices.getRolesByUser(user.getId());
			priv = new Privilege(rolesUser);
			
			rolesToAsign.getItems().addAll(rolesUser);
			allRolesList.getItems().addAll(extractCoincidenceOfOne(rolesThisUser, rolesUser));
		}

		title.setText(GUI.calculateOnlyPrivilege(priv));
	}
	
	private List<Rol> extractCoincidenceOfOne(List<Rol> list1, List<Rol> list2){
		for(Rol rol : list2){
			Iterator<Rol> it = list1.iterator();
			boolean found = false;
			while(it.hasNext() && !found){
				Rol r = it.next();
				if(rol.getId() == r.getId()){
					list1.remove(r);
					found = true;
				}
			}
		}
		
		return list1;
	}
	
	public List<Rol> saveData(){
		return rolesToAsign.getItems();
	}
}
