package visual.controls.center.userCountPage;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;

import models.ResearchGroup;
import models.ScientificCategory;
import models.Subject;
import models.TeachingCategory;
import models.TeachingGroup;
import models.users.Student;
import models.users.Teacher;
import models.users.User;
import services.ResearchGroupServices;
import services.ScientificCategoryServices;
import services.SubjectServices;
import services.TeachingCategoryServices;
import services.TeachingGroupServices;
import services.UserServices;
import utils.DialogBox;
import utils.GUI;
import utils.NumberField;
import utils.OpButton;
import utils.Utilities;
import utils.ValidationErrorException;
import visual.Main;
import visual.controls.Notification;
import visual.controls.top.MenuToolBarPane;
import visual.controls.top.TitledPane;

public class CurrentUserPane extends BorderPane{
	private static CurrentUserPane pane = null;
	
	private VBox root;
	
	private GeneralData generalData;
	private TeacherData teacherData;
	private StudentData studentData;
	
	private OpButton btnModify;
	private OpButton btnDelete;
	private HBox buttons;
	
	private boolean showingDelete;
	
	public static CurrentUserPane getInstance(){
		if(pane == null)
			pane = new CurrentUserPane();
		return pane;
	}
	
	private CurrentUserPane(){
		setId("currentUserPane");

		generalData = new GeneralData();
		teacherData = new TeacherData();
		studentData = new StudentData();
		
		root = new VBox();
		root.getChildren().addAll(generalData);
		
		setCenter(root);
		
		btnModify = new OpButton("Modificar");
		btnModify.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				if(btnModify.getText().equalsIgnoreCase("Modificar")){
					setModify();
					btnModify.setText("Guardar");
					btnDelete.setText("Cancelar");
					if(!showingDelete)
						buttons.getChildren().add(btnDelete);
				}
				else{
					boolean success = true;
					try {
						saveData();
						generalData.setNullImagePath();
						Notification.getInstance().setText("Sus nuevos datos se han guardado satisfactoriamente");
						Notification.getInstance().showNotification();
					} catch (ValidationErrorException e1) {
						DialogBox diag = new DialogBox(e1.getMessage());
						diag.show();
						success = false;
					} catch (SQLException e2) {
						DialogBox diag = new DialogBox(e2.getMessage());
						diag.show();
						success = false;
					} catch (FileNotFoundException e3) {
						DialogBox diag = new DialogBox(e3.getMessage());
						diag.show();
						success = false;
					}
					
					if(success){
						GUI.getInstance().update();
						try {
							setData();
							TitledPane.getInstance().setData();
							MenuToolBarPane.getInstance().setData();
						} catch (IOException e1) {
							DialogBox diag = new DialogBox("No se pudo obtener su avatar");
							diag.show();
							setDefaultAvatar();
							TitledPane.getInstance().setDefaultAvatar();
						}
						btnModify.setText("Modificar");
						btnDelete.setText("Eliminar");
						if(!showingDelete)
							buttons.getChildren().remove(btnDelete);
					}
				}
			}
		});
		
		btnDelete = new OpButton("Eliminar");
		btnDelete.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				if(btnDelete.getText().equalsIgnoreCase("Eliminar")){
					DialogYesNoToCancelUser diag = new DialogYesNoToCancelUser("¿Realmente desea cancelar su usuario?", GUI.getInstance().getUser());
					diag.show();
				}
				else{
					try {
						setData();
						generalData.setNullImagePath();
					} catch (IOException e1) {
						DialogBox diag = new DialogBox("No se pudo obtener su avatar");
						diag.show();
						setDefaultAvatar();
					}
					btnModify.setText("Modificar");
					btnDelete.setText("Eliminar");
					
					if(!showingDelete)
						buttons.getChildren().remove(btnDelete);
				}
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
	
	public void hideModify(){
		buttons.getChildren().remove(btnModify);
	}
	public void hideDelete(){
		buttons.getChildren().remove(btnDelete);
		showingDelete = false;
	}
	public void showModify(){
		if(!buttons.getChildren().contains(btnModify))
			buttons.getChildren().add(btnModify);
	}
	public void showDelete(){
		if(!buttons.getChildren().contains(btnDelete))
			buttons.getChildren().add(btnDelete);
		showingDelete = true;
	}
	
	public void setData() throws IOException{
		
		//Esta linea es una asquerosidad, pero es mas sencilla
		root.getChildren().removeAll(teacherData, studentData);
		
		if(GUI.getInstance().getUser() instanceof Teacher){
			teacherData.setData();
			root.getChildren().add(teacherData);
		}
		else{
			studentData.setData();
			root.getChildren().add(studentData);
		}
		
		generalData.setData(); //De ultimo para en caso de que lance una excepcion por la
							   //foto, todos los demas datos ya esten cargados	
	}
	
	public void setDefaultButtons() {
		btnModify.setText("Modificar");
		btnDelete.setText("Eliminar");		
	}
	
	public void setDefault(){
		btnModify.setText("Modificar");
		btnDelete.setText("Eliminar");
		generalData.setDefault();
		teacherData.setDefault();
		studentData.setDefault();
	}
	public void setDefaultAvatar(){
		generalData.setDefaultAvatar();
	}
	
	public void setModify(){
		generalData.setModify();
		
		if(GUI.getInstance().getUser() instanceof Teacher)
			teacherData.setModify();
		else
			studentData.setModify();
	}
	
	public void saveData() throws ValidationErrorException, SQLException, FileNotFoundException{
		Object[] general = generalData.saveData();
		
		int id = GUI.getInstance().getUser().getId();
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
		
		Object[] teacher, student;
		if(GUI.getInstance().getUser() instanceof Teacher){
			teacher = teacherData.saveData();
			
			int subject = ((Integer)teacher[0]).intValue();
			boolean teaches = ((Boolean)teacher[1]).booleanValue();
			int teachingCategory = ((Integer)teacher[2]).intValue();
			int scientificCategory = ((Integer)teacher[3]).intValue();
			
			UserServices.modifyUser(id, ci, name, firstLastName, secondLastName, picture, user, password, true, teaches, false, scientificCategory, teachingCategory, subject, -1, -1, null, validPicture);
		}
		else{
			student = studentData.saveData();
			
			int teachingGroup = ((Integer)student[0]).intValue();
			int researchGroup = ((Integer)student[1]).intValue();
			
			UserServices.modifyUser(id, ci, name, firstLastName, secondLastName, picture, user, password, false, false, false, -1, -1, -1, teachingGroup, researchGroup, null, validPicture);
		}
	}

}

class GeneralData extends AnchorPane{
	private Label title;
	
	private Label lblName;
	private Label lblFirstLastName;
	private Label lblSecondLastName;
	private Label lblCI;
	private Label lblUser;
	private Label lblPassword;
	
	private TextField txfName;
	private TextField txfFirstLastName;
	private TextField txfSecondLastName;
	private NumberField txfCI;
	private TextField txfUser;
	private PasswordField psfPassword;
	
	private Label lblNameDatum;
	private Label lblFirstLastNameDatum;
	private Label lblSecondLastNameDatum;
	private Label lblCIDatum;
	private Label lblUserDatum;
	private Label lblPasswordDatum;
	
	private ImageView avatar;
	private Hyperlink changeAvatar;
	private Label lblAvatar;
	private File imgPath = null;
	private FileChooser chooser;
	
	private Image unknown;
	
	public GeneralData(){
		setId("generalData");
		
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
		lblUser = new Label("Usuario:");
		lblUser.setPrefWidth(75);
		lblUser.getStyleClass().add("bold");
		lblPassword = new Label("Contraseña:");
		lblPassword.setPrefWidth(75);
		lblPassword.getStyleClass().add("bold");
		
		lblNameDatum = new Label();
		lblNameDatum.setPrefWidth(110);
		lblFirstLastNameDatum = new Label();
		lblFirstLastNameDatum.setPrefWidth(110);
		lblSecondLastNameDatum = new Label();
		lblSecondLastNameDatum.setPrefWidth(110);
		lblCIDatum = new Label();
		lblCIDatum.setPrefWidth(110);
		lblUserDatum = new Label();
		lblPasswordDatum = new Label();
		
		
		avatar = new ImageView(unknown);
		avatar.setId("avatar");
		lblAvatar = new Label("Avatar");
		lblAvatar.getStyleClass().add("lblAvatar");
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
        			try {
						BufferedImage picture = ImageIO.read(imgPath);
						
						Image avatarImg = null;
						
						if(picture.getWidth() > 64 || picture.getHeight() > 60){
							picture = Utilities.createBufferedImage(Utilities.scaleImageIcon(picture, 64, 60));
							avatarImg = Utilities.createImage(picture);
						}
						else
							avatarImg = Utilities.createImage(picture);
						
	        			avatar.setImage(avatarImg);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
        			
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
		/*txfCI.setOnKeyPressed(new EventHandler<Event>() {
			public void handle(Event e){
				if(txfCI.getText().length() == 11)
					;
			}
		});*/
		txfUser = new TextField();
		txfUser.setPrefWidth(110);
		txfUser.setPromptText("Nombre usuario");
		psfPassword = new PasswordField();
		psfPassword.setPrefWidth(110);
		psfPassword.setPromptText("Contraseña");
		
		AnchorPane.setLeftAnchor(title, Double.valueOf(105));
		AnchorPane.setTopAnchor(title, Double.valueOf(20));
		
		AnchorPane.setLeftAnchor(lblName, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblName, Double.valueOf(75));
		
		AnchorPane.setLeftAnchor(lblNameDatum, Double.valueOf(160));
		AnchorPane.setTopAnchor(lblNameDatum, Double.valueOf(75));
		
		AnchorPane.setLeftAnchor(txfName, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfName, Double.valueOf(70));
		
		AnchorPane.setLeftAnchor(lblFirstLastName, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblFirstLastName, Double.valueOf(110));
		
		AnchorPane.setLeftAnchor(lblFirstLastNameDatum, Double.valueOf(160));
		AnchorPane.setTopAnchor(lblFirstLastNameDatum, Double.valueOf(110));

		AnchorPane.setLeftAnchor(txfFirstLastName, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfFirstLastName, Double.valueOf(105));
		
		AnchorPane.setLeftAnchor(lblSecondLastName, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblSecondLastName, Double.valueOf(145));
		
		AnchorPane.setLeftAnchor(lblSecondLastNameDatum, Double.valueOf(160));
		AnchorPane.setTopAnchor(lblSecondLastNameDatum, Double.valueOf(145));
		
		AnchorPane.setLeftAnchor(txfSecondLastName, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfSecondLastName, Double.valueOf(140));
		
		AnchorPane.setLeftAnchor(lblCI, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblCI, Double.valueOf(180));
		
		AnchorPane.setLeftAnchor(lblCIDatum, Double.valueOf(160));
		AnchorPane.setTopAnchor(lblCIDatum, Double.valueOf(180));
		
		AnchorPane.setLeftAnchor(txfCI, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfCI, Double.valueOf(175));
		
		AnchorPane.setLeftAnchor(avatar, Double.valueOf(390));
		AnchorPane.setTopAnchor(avatar, Double.valueOf(40));
		
		AnchorPane.setLeftAnchor(lblAvatar, Double.valueOf(408));
		AnchorPane.setTopAnchor(lblAvatar, Double.valueOf(105));
		
		AnchorPane.setLeftAnchor(changeAvatar, Double.valueOf(385));
		AnchorPane.setTopAnchor(changeAvatar, Double.valueOf(105));
		
		AnchorPane.setLeftAnchor(lblUser, Double.valueOf(300));
		AnchorPane.setTopAnchor(lblUser, Double.valueOf(145));
		
		AnchorPane.setLeftAnchor(lblUserDatum, Double.valueOf(385));
		AnchorPane.setTopAnchor(lblUserDatum, Double.valueOf(145));
		
		AnchorPane.setLeftAnchor(txfUser, Double.valueOf(385));
		AnchorPane.setTopAnchor(txfUser, Double.valueOf(140));
		
		AnchorPane.setLeftAnchor(lblPassword, Double.valueOf(300));
		AnchorPane.setTopAnchor(lblPassword, Double.valueOf(180));
		
		AnchorPane.setLeftAnchor(lblPasswordDatum, Double.valueOf(385));
		AnchorPane.setTopAnchor(lblPasswordDatum, Double.valueOf(180));
		
		AnchorPane.setLeftAnchor(psfPassword, Double.valueOf(385));
		AnchorPane.setTopAnchor(psfPassword, Double.valueOf(175));
	}
	
	public void setNullImagePath(){
		imgPath = null;
	}
	
	public void setData() throws IOException{
		User user = GUI.getInstance().getUser();
		
		lblNameDatum.setText(user.getName());
		lblFirstLastNameDatum.setText(user.getFirstLastName());
		lblSecondLastNameDatum.setText(user.getSecondLastName());
		lblCIDatum.setText(String.valueOf(user.getCi()));
		lblUserDatum.setText(user.getUserName());
		lblPasswordDatum.setText("*******");
		
		Image avatarImg = unknown;
		BufferedImage picture = user.getPicture();
		if(picture != null){
			if(picture.getWidth(null) > 64 || picture.getHeight(null) > 60){
				picture = Utilities.createBufferedImage(Utilities.scaleImageIcon(picture, 64, 60));
				avatarImg = Utilities.createImage(picture);
			}
			else
				avatarImg = Utilities.createImage(picture);
		}
		avatar.setImage(avatarImg);
		
		getChildren().removeAll(getChildren());
		
		getChildren().addAll(title, 
				 lblName, lblNameDatum,
				 lblFirstLastName, lblFirstLastNameDatum,
				 lblSecondLastName, lblSecondLastNameDatum,
				 lblCI, lblCIDatum,
				 avatar, lblAvatar,
				 lblUser, lblUserDatum,
				 lblPassword, lblPasswordDatum
				);
	}
	
	public void setModify(){
		User user = GUI.getInstance().getUser();
		
		txfName.setText(user.getName());
		txfFirstLastName.setText(user.getFirstLastName());
		txfSecondLastName.setText(user.getSecondLastName());
		txfCI.setText(String.valueOf(user.getCi()));
		txfUser.setText(user.getUserName());
		psfPassword.setText(user.getPassword());
		
		getChildren().removeAll(getChildren());
		
		getChildren().addAll(title, 
				 lblName, txfName,
				 lblFirstLastName, txfFirstLastName,
				 lblSecondLastName, txfSecondLastName,
				 lblCI, txfCI,
				 avatar, changeAvatar,
				 lblUser, txfUser,
				 lblPassword, psfPassword
				);
	}
	
	public void setDefault(){
		lblNameDatum.setText("");
		lblFirstLastNameDatum.setText("");
		lblSecondLastNameDatum.setText("");
		lblCIDatum.setText("");
		lblUserDatum.setText("");
		lblPasswordDatum.setText("");
		
		avatar.setImage(new Image(getClass().getResource("../../../icons/unknow70.png").toExternalForm()));
	}
	
	public void setDefaultAvatar(){
		avatar.setImage(unknown);
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
		
		
		return new Object[]{name, firstLastName, secondLastName, ci, user, password, imgPath};//imgPath toma la dirrecion de la nueva imagen
	}
}

class TeacherData extends AnchorPane{
	private Label title;
	
	private Label lblSubject;
	private Label lblTeaches;
	private Label lblTeachingCategory;
	private Label lblScientificCategory;
	
	private ChoiceBox<Subject> txfSubject;
	private CheckBox txfTeaches;
	private ChoiceBox<TeachingCategory> txfTeachingCategory;
	private ChoiceBox<ScientificCategory> txfScientificCategory;
	
	private Label lblSubjectDatum;
	private Label lblTeachesDatum;
	private Label lblTeachingCategoryDatum;
	private Label lblScientificCategoryDatum;
	
	public TeacherData(){
		setId("teacherData");
		
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
		
		lblSubjectDatum = new Label();
		lblTeachesDatum = new Label();
		lblTeachingCategoryDatum = new Label();
		lblScientificCategoryDatum = new Label();
		
		txfSubject = new ChoiceBox<Subject>();
		txfSubject.setPrefWidth(200);
		txfTeaches = new CheckBox();
		txfTeachingCategory = new ChoiceBox<TeachingCategory>();
		txfTeachingCategory.setPrefWidth(200);
		txfScientificCategory = new ChoiceBox<ScientificCategory>();
		txfScientificCategory.setPrefWidth(200);
		
		AnchorPane.setLeftAnchor(title, Double.valueOf(105));
		AnchorPane.setTopAnchor(title, Double.valueOf(15));
		
		AnchorPane.setLeftAnchor(lblSubject, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblSubject, Double.valueOf(65));
		
		AnchorPane.setLeftAnchor(lblSubjectDatum, Double.valueOf(160));
		AnchorPane.setTopAnchor(lblSubjectDatum, Double.valueOf(65));
		
		AnchorPane.setLeftAnchor(txfSubject, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfSubject, Double.valueOf(55));
		
		AnchorPane.setLeftAnchor(lblTeaches, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblTeaches, Double.valueOf(100));
		
		AnchorPane.setLeftAnchor(lblTeachesDatum, Double.valueOf(160));
		AnchorPane.setTopAnchor(lblTeachesDatum, Double.valueOf(100));
		
		AnchorPane.setLeftAnchor(txfTeaches, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfTeaches, Double.valueOf(95));
		
		AnchorPane.setLeftAnchor(lblTeachingCategory, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblTeachingCategory, Double.valueOf(135));
		
		AnchorPane.setLeftAnchor(lblTeachingCategoryDatum, Double.valueOf(160));
		AnchorPane.setTopAnchor(lblTeachingCategoryDatum, Double.valueOf(135));
		
		AnchorPane.setLeftAnchor(txfTeachingCategory, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfTeachingCategory, Double.valueOf(125));
		
		AnchorPane.setLeftAnchor(lblScientificCategory, Double.valueOf(30));
		AnchorPane.setTopAnchor(lblScientificCategory, Double.valueOf(170));
		
		AnchorPane.setLeftAnchor(lblScientificCategoryDatum, Double.valueOf(160));
		AnchorPane.setTopAnchor(lblScientificCategoryDatum, Double.valueOf(170));
		
		AnchorPane.setLeftAnchor(txfScientificCategory, Double.valueOf(160));
		AnchorPane.setTopAnchor(txfScientificCategory, Double.valueOf(160));
	}
	
	public void setData(){
		Teacher user = (Teacher)GUI.getInstance().getUser();
		
		lblSubjectDatum.setText(user.getSubject());
		lblTeachesDatum.setText(user.isTeaches() ? "Si" : "No");
		lblTeachingCategoryDatum.setText(user.getTeachingCategory());
		lblScientificCategoryDatum.setText(user.getScientificCategory());
		
		getChildren().removeAll(getChildren());
		
		getChildren().addAll(title,
				 lblSubject, lblSubjectDatum,
				 lblTeaches, lblTeachesDatum,
				 lblTeachingCategory, lblTeachingCategoryDatum,
				 lblScientificCategory, lblScientificCategoryDatum
				);
	}
	
	public void setModify(){
		Teacher user = (Teacher)GUI.getInstance().getUser();
		
		txfTeaches.setSelected(user.isTeaches());
		try {
			List<Subject> subjects = SubjectServices.getEnabledSubject();
			txfSubject.getItems().removeAll(txfSubject.getItems());
			txfSubject.getItems().addAll(subjects);
			Subject currentSubject = Subject.find(subjects, user.getSubject());
			txfSubject.getSelectionModel().select(currentSubject);
		} catch (SQLException e) {
			DialogBox diag = new DialogBox("No se pudieron obtener los datos referentes a las Asignaturas");
			diag.show();
		}
		
		try{
			List<TeachingCategory> teachingCats = TeachingCategoryServices.getEnabledTeachingCategory();
			txfTeachingCategory.getItems().removeAll(txfTeachingCategory.getItems());
			txfTeachingCategory.getItems().addAll(teachingCats);
			TeachingCategory currentTCat = TeachingCategory.find(teachingCats, user.getTeachingCategory());
			txfTeachingCategory.getSelectionModel().select(currentTCat);
		} catch (SQLException e) {
			DialogBox diag = new DialogBox("No se pudieron obtener los datos referentes a las Categorías Docentes");
			diag.show();
		}
		
		try{
			List<ScientificCategory> scientificCats = ScientificCategoryServices.getEnabledTeachingCategory();
			txfScientificCategory.getItems().removeAll(txfScientificCategory.getItems());
			txfScientificCategory.getItems().addAll(scientificCats);
			ScientificCategory currentScat = ScientificCategory.find(scientificCats, user.getScientificCategory());
			txfScientificCategory.getSelectionModel().select(currentScat);
			
		} catch (SQLException e) {
			DialogBox diag = new DialogBox("No se pudieron obtener los datos referentes a las Categorías Científicas");
			diag.show();
		}
		
		getChildren().removeAll(getChildren());
		
		getChildren().addAll(title,
				 lblSubject, txfSubject,
				 lblTeaches, txfTeaches,
				 lblTeachingCategory, txfTeachingCategory,
				 lblScientificCategory, txfScientificCategory
				);
	}
	
	public void setDefault(){
		lblSubjectDatum.setText("");
		lblTeachesDatum.setText("");
		lblTeachingCategoryDatum.setText("");
		lblScientificCategoryDatum.setText("");
	}
	
	public Object[] saveData() throws SQLException, ValidationErrorException{
		Integer subject = -1;
		Subject subjectObj = txfSubject.getSelectionModel().getSelectedItem();
		if(subjectObj == null)
			subject = Integer.valueOf(SubjectServices.getSubjectIdByName(((Teacher)GUI.getInstance().getUser()).getSubject()));
		else
			subject = Integer.valueOf(subjectObj.getId());
		
		Boolean teaches = Boolean.valueOf(txfTeaches.isSelected());
		
		Integer teachingCategory = -1;
		TeachingCategory tcat = txfTeachingCategory.getSelectionModel().getSelectedItem();
		if(tcat == null)
			teachingCategory = Integer.valueOf(TeachingCategoryServices.getTeachingCategoryIdByName(((Teacher)GUI.getInstance().getUser()).getTeachingCategory()));
		else
			teachingCategory = Integer.valueOf(tcat.getId());
		
		Integer scientificCategory = -1;
		ScientificCategory scat = txfScientificCategory.getSelectionModel().getSelectedItem();
		if(scat == null)
			scientificCategory = Integer.valueOf(ScientificCategoryServices.getScientificCategoryIdByName(((Teacher)GUI.getInstance().getUser()).getScientificCategory()));
		else
			scientificCategory = Integer.valueOf(scat.getId());
		
		return new Object[]{subject, teaches, teachingCategory, scientificCategory};
	}
}

class StudentData extends AnchorPane{
	private Label title;
	
	private Label lblTeachingGroup;
	private Label lblResearchGroup;
	
	private ChoiceBox<TeachingGroup> txfTeachingGroup;
	private ChoiceBox<ResearchGroup> txfResearchGroup;
	
	private Label lblTeachingGroupDatum;
	private Label lblResearchGroupDatum;
	
	public StudentData(){
		setId("studentData");
		
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
	}
	
	public void setData(){
		Student user = (Student)GUI.getInstance().getUser();
		
		lblTeachingGroupDatum.setText(user.getTeachingGroup());
		lblResearchGroupDatum.setText(user.getResearchGroup());
		
		getChildren().removeAll(getChildren());
		
		getChildren().addAll(title,
		 lblTeachingGroup, lblTeachingGroupDatum,
		 lblResearchGroup, lblResearchGroupDatum
		);
	}
	
	public void setModify(){
		Student user = (Student)GUI.getInstance().getUser();
		
		try {			
			List<TeachingGroup> teachingGroups = TeachingGroupServices.getEnabledTeachingGroups();
			txfTeachingGroup.getItems().removeAll(txfTeachingGroup.getItems());
			txfTeachingGroup.getItems().addAll(teachingGroups);
			TeachingGroup currentTGroup = TeachingGroup.find(teachingGroups, user.getTeachingGroup());
			txfTeachingGroup.getSelectionModel().select(currentTGroup);
		} catch (SQLException e) {
			DialogBox diag = new DialogBox("No se pudieron obtener los datos referentes a las Grupos Docentes");
			diag.show();
		}
		
		try{
			List<ResearchGroup> researchGroups = ResearchGroupServices.getEnabledResearchGroups();
			txfResearchGroup.getItems().removeAll(txfResearchGroup.getItems());
			txfResearchGroup.getItems().addAll(researchGroups);
			ResearchGroup currentRGroup = ResearchGroup.find(researchGroups, user.getResearchGroup());
			txfResearchGroup.getSelectionModel().select(currentRGroup);
		} catch (SQLException e) {
			DialogBox diag = new DialogBox("No se pudieron obtener los datos referentes a los Grupos Investigativos");
			diag.show();
		}
		
		getChildren().removeAll(getChildren());
		
		getChildren().addAll(title,
		 lblTeachingGroup, txfTeachingGroup,
		 lblResearchGroup, txfResearchGroup
		);
	}
	
	public void setDefault(){
		lblTeachingGroupDatum.setText("");
		lblResearchGroupDatum.setText("");
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
