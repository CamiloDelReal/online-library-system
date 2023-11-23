package visual.controls.center.materialPage;

import java.sql.SQLException;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.Language;
import models.Subject;
import models.Tematic;
import models.materials.Book;
import models.materials.InternetArticle;
import models.materials.Lesson;
import models.materials.Magazine;
import models.materials.Material;
import models.materials.Monograph;
import models.materials.Other;
import models.materials.Thesis;
import models.users.User;
import services.LanguageService;
import services.MaterialServices;
import services.SubjectServices;
import services.TematicServices;
import utils.DialogBox;
import utils.GUI;
import utils.NumberField;
import utils.Utilities;
import utils.ValidationErrorException;
import visual.Main;
import visual.controls.Notification;

public class InsertModifyMaterialDialog extends BorderPane{
	public static final double WIDTH = 560;
	public static final double HEIGHT = 550;
	
	private static final String INSERT_MATERIAL = "Inserción de material";
	private static final String MODIFY_MATERIAL = "Modificación de material";
	
	private StackPane dialogRoot;
	private Material material;
	
	private Label title;
	private ImageView picture;
	private HBox header;
	private Label lblTematics;
	private ChoiceBox<Tematic> choiceTematic;
	private Label lblSubjects;
	private ChoiceBox<Subject> choiceSubject;
	
	private HBox buttons;
	private Button btnSave;
	private Button btnCancel;
	
	private ToggleButton toggleBook;
	private ToggleButton toggleInternetArticle;
	private ToggleButton toggleMagazine;
	private ToggleButton toggleThesis;
	private ToggleButton toggleMonography;
	private ToggleButton toggleLesson;
	private ToggleButton toggleOther;
	private ToggleGroup toggleGroup;
	private HBox togglePane;
	private Node currentPane;
	
	private VBox data;
	private HBox boxNotReguirement;
	private GeneralData generalData;
	private BookData bookData;
	private InternetArticleData internetArticleData;
	private MagazineData magazineData;
	private ThesisData thesisData;
	
	public InsertModifyMaterialDialog(final Material mat, final int tematicId, final int subjectId){
		setId("dataDialog");
		this.material = mat;
		setMinSize(WIDTH, HEIGHT);
		setPrefSize(WIDTH, HEIGHT);
		setMaxSize(WIDTH, HEIGHT);		
		
		
		if(mat == null)
			title = new Label(INSERT_MATERIAL);
		else
			title = new Label(MODIFY_MATERIAL);
		title.getStyleClass().add("title");
		
		Region spacer1 = new Region();
		spacer1.setMinHeight(25);
		spacer1.setPrefHeight(25);
		spacer1.setMaxHeight(25);
		
		VBox titleBox = new VBox();
		titleBox.getChildren().addAll(spacer1, title);
		
		picture = new ImageView(new Image(getClass().getResource("../../../icons/academicData57x62.png").toExternalForm()));
		
		header = new HBox();
		header.setSpacing(20);
		header.getChildren().addAll(picture, titleBox);
		
		setTop(header);
		
		
		lblTematics = new Label("Temáticas");
		choiceTematic = new ChoiceBox<Tematic>();
		choiceTematic.setPrefWidth(250);
		fillChoiceTematic(tematicId);
		
		choiceTematic.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tematic>() {
			public void changed(ObservableValue<? extends Tematic> observable,
					Tematic oldValue, Tematic newValue) {
				choiceSubject.getItems().removeAll(choiceSubject.getItems());
				fillChoiceSubject(newValue.getId(), subjectId);
			}
		});
		
		lblSubjects = new Label("Asignaturas");
		choiceSubject = new ChoiceBox<Subject>();
		choiceSubject.setPrefWidth(250);
		fillChoiceSubject(tematicId, subjectId);
		
		GridPane grid = new GridPane();
		grid.setTranslateX(20);
		grid.setTranslateY(5);
		//grid.setGridLinesVisible(true);
		ObservableList<Node> content = grid.getChildren();
		
		GridPane.setConstraints(lblTematics, 0, 0);
		GridPane.setMargin(lblTematics, new Insets(10, 5, 2, 10));
		content.add(lblTematics);
		GridPane.setConstraints(choiceTematic, 1, 0);
		GridPane.setMargin(choiceTematic, new Insets(10, 10, 2, 5));
		content.add(choiceTematic);
		GridPane.setConstraints(lblSubjects, 0, 1);
		GridPane.setMargin(lblSubjects, new Insets(10, 5, 2, 10));
		content.add(lblSubjects);
		GridPane.setConstraints(choiceSubject, 1, 1);
		GridPane.setMargin(choiceSubject, new Insets(10, 10, 2, 5));
		content.add(choiceSubject);
		
		toggleGroup = new ToggleGroup();
		toggleBook = new ToggleButton("Libro");
		toggleBook.getStyleClass().add("first");
		toggleBook.setMinSize(60, 25);
		toggleBook.setPrefSize(60, 25);
		toggleBook.setMaxSize(60, 25);
		toggleBook.setToggleGroup(toggleGroup);
		toggleBook.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				toggleBook.setSelected(true);
				data.getChildren().remove(currentPane);
				data.getChildren().add(bookData);
				currentPane = bookData;
			}
		});
		
		toggleInternetArticle = new ToggleButton("Art. Internet");
		toggleInternetArticle.setMinSize(80, 25);
		toggleInternetArticle.setPrefSize(80, 25);
		toggleInternetArticle.setMaxSize(80, 25);
		toggleInternetArticle.setToggleGroup(toggleGroup);
		toggleInternetArticle.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				toggleInternetArticle.setSelected(true);
				data.getChildren().remove(currentPane);
				data.getChildren().add(internetArticleData);
				currentPane = internetArticleData;
			}
		});
		
		toggleMagazine = new ToggleButton("Revista");
		toggleMagazine.setMinSize(60, 25);
		toggleMagazine.setPrefSize(60, 25);
		toggleMagazine.setMaxSize(60, 25);
		toggleMagazine.setToggleGroup(toggleGroup);
		toggleMagazine.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				toggleMagazine.setSelected(true);
				data.getChildren().remove(currentPane);
				data.getChildren().add(magazineData);
				currentPane = magazineData;
			}
		});
		
		toggleThesis = new ToggleButton("Tesis");
		toggleThesis.setMinSize(60, 25);
		toggleThesis.setPrefSize(60, 25);
		toggleThesis.setMaxSize(60, 25);
		toggleThesis.setToggleGroup(toggleGroup);
		toggleThesis.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				toggleThesis.setSelected(true);
				data.getChildren().remove(currentPane);
				data.getChildren().add(thesisData);
				currentPane = thesisData;
			}
		});
		
		toggleMonography = new ToggleButton("Monografía");
		toggleMonography.setMinSize(80, 25);
		toggleMonography.setPrefSize(80, 25);
		toggleMonography.setMaxSize(80, 25);
		toggleMonography.setToggleGroup(toggleGroup);
		toggleMonography.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				toggleMonography.setSelected(true);
				data.getChildren().remove(currentPane);
				data.getChildren().add(boxNotReguirement);
				currentPane = boxNotReguirement;
			}
		});
		
		toggleLesson = new ToggleButton("Clase");
		toggleLesson.setMinSize(60, 25);
		toggleLesson.setPrefSize(60, 25);
		toggleLesson.setMaxSize(60, 25);
		toggleLesson.setToggleGroup(toggleGroup);
		toggleLesson.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				toggleLesson.setSelected(true);
				data.getChildren().remove(currentPane);
				data.getChildren().add(boxNotReguirement);
				currentPane = boxNotReguirement;
			}
		});
		
		toggleOther = new ToggleButton("Otros");
		toggleOther.setMinSize(60, 25);
		toggleOther.setPrefSize(60, 25);
		toggleOther.setMaxSize(60, 25);
		toggleOther.getStyleClass().add("last");
		toggleOther.setToggleGroup(toggleGroup);
		toggleOther.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				toggleOther.setSelected(true);
				data.getChildren().remove(currentPane);
				data.getChildren().add(boxNotReguirement);
				currentPane = boxNotReguirement;
			}
		});
		
		Region spacer3 = new Region();
		HBox.setHgrow(spacer3, Priority.ALWAYS);
		Region spacer4 = new Region();
		HBox.setHgrow(spacer4, Priority.ALWAYS);
		
		togglePane = new HBox();
		togglePane.getChildren().addAll(spacer3, toggleBook, toggleInternetArticle,
				toggleMagazine, toggleThesis, toggleLesson, toggleMonography, toggleOther, spacer4);
		
		
		generalData = new GeneralData(material);
		boxNotReguirement = new HBox();
		Label lblNotRequirement = new Label("< No se requieren datos >");
		Region spacer5 = new Region();
		Region spacer6 = new Region();
		HBox.setHgrow(spacer5, Priority.ALWAYS);
		HBox.setHgrow(spacer6, Priority.ALWAYS);
		boxNotReguirement.getChildren().addAll(spacer5, lblNotRequirement, spacer6);
		
		data = new VBox();
		data.setSpacing(18);
		data.getChildren().addAll(grid, generalData, togglePane);
		
		if(material != null){
			toggleBook.setDisable(true);
			toggleInternetArticle.setDisable(true);
			toggleMagazine.setDisable(true);
			toggleThesis.setDisable(true);
			toggleMonography.setDisable(true);
			toggleLesson.setDisable(true);
			toggleOther.setDisable(true);
			
			if(material instanceof Book){
				bookData = new BookData((Book)material);
				toggleBook.setSelected(true);
				toggleBook.setDisable(false);
				data.getChildren().add(bookData);
			}
			else if(material instanceof Magazine){
				magazineData = new MagazineData((Magazine)material);
				toggleMagazine.setSelected(true);
				toggleMagazine.setDisable(false);
				data.getChildren().add(magazineData);
			}
			else if(material instanceof InternetArticle){
				internetArticleData = new InternetArticleData((InternetArticle)material);
				toggleInternetArticle.setSelected(true);
				toggleInternetArticle.setDisable(false);
				data.getChildren().add(internetArticleData);
			}
			else if(material instanceof Thesis){
				thesisData = new ThesisData((Thesis)material);
				toggleThesis.setSelected(true);
				toggleThesis.setDisable(false);
				data.getChildren().add(thesisData);
			}
			else if(material instanceof Monograph){
				toggleMonography.setSelected(true);
				toggleMonography.setDisable(false);
				data.getChildren().add(boxNotReguirement);
			}
			else if(material instanceof Lesson){
				toggleLesson.setSelected(true);
				toggleLesson.setDisable(false);
				data.getChildren().add(boxNotReguirement);
			}
			else if(material instanceof Other){
				toggleOther.setSelected(true);
				toggleOther.setDisable(false);
				data.getChildren().add(boxNotReguirement);
			}
		}
		else{
			bookData = new BookData(null);
			magazineData = new MagazineData(null);
			internetArticleData = new InternetArticleData(null);
			thesisData = new ThesisData(null);
			
			data.getChildren().add(bookData);
			toggleBook.setSelected(true);
			currentPane = bookData;
		}
		
		
		setCenter(data);
		
		Region spacer2 = new Region();
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		
		btnSave = new Button("Guardar");
		btnSave.setMinSize(80, 20);
		btnSave.setPrefSize(80, 20);
		btnSave.setMaxSize(80, 20);
		btnSave.setDefaultButton(true);
		btnSave.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				boolean succes = true;
				try{
					String text = saveData();
					Notification.getInstance().setText(text);
					Notification.getInstance().showNotification();
					MaterialViewer.getInstance().putMaterials(subjectId, "");						
				} catch (ValidationErrorException | SQLException e1) {
					DialogBox diag = new DialogBox(e1.getMessage());
					diag.show();
					succes = false;
				}
				
				if(succes){
					dialogRoot.getChildren().remove(InsertModifyMaterialDialog.this);
					Main.getStackRoot().getChildren().remove(dialogRoot);
				}
			}
		});
		
		btnCancel = new Button("Cancelar");
		btnCancel.setMinSize(80, 20);
		btnCancel.setPrefSize(80, 20);
		btnCancel.setMaxSize(80, 20);
		btnCancel.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				dialogRoot.getChildren().remove(InsertModifyMaterialDialog.this);
				Main.getStackRoot().getChildren().remove(dialogRoot);
			}
		});
		
		buttons = new HBox();
		buttons.setSpacing(7);
		buttons.getChildren().addAll(spacer2, btnSave, btnCancel);
		
		setBottom(buttons);
		
		
		dialogRoot = new StackPane();
		dialogRoot.setId("dialogRoot");
		dialogRoot.getChildren().add(InsertModifyMaterialDialog.this);
	}
	
	public void show(){
		Main.getStackRoot().getChildren().add(dialogRoot);
	}
	
	private void fillChoiceTematic(int currentTematic){
		List<Tematic> tems = null;
		try{
			if(GUI.getInstance().getUser() != null && GUI.getInstance().getUser().getPrivilege().isModifyTematic())
				tems = TematicServices.getTematics();
			else
				tems = TematicServices.getEnabledTematics();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		if(tems != null){
			choiceTematic.getItems().addAll(tems);
			choiceTematic.getSelectionModel().select(Tematic.find(tems, currentTematic));
		}
	}
	
	private void fillChoiceSubject(int tematicId, int currentSubject){
		List<Subject> subs = null;
		try{
			if(GUI.getInstance().getUser() != null && GUI.getInstance().getUser().getPrivilege().isModifyTematic())
				subs = SubjectServices.getSubjectByTematic(tematicId);
			else
				subs = SubjectServices.getEnabledSubjectByTematic(tematicId);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}		
		if(subs != null){
			choiceSubject.getItems().addAll(subs);
			choiceSubject.getSelectionModel().select(Subject.find(subs, currentSubject));
		}
	}
	
	private String saveData() throws SQLException, ValidationErrorException{
			Object[] general = generalData.saveData();
			String notificatoinText = "";
			int selectedSubject = choiceSubject.getSelectionModel().getSelectedItem().getId();
			
			if(material == null)
				notificatoinText += "Se ha creado y guardado satisfactoriamente ";
			else{
				if(!material.getTitle().equals((String)general[0]))
					notificatoinText += "El material " + material.getTitle() +" ha sido actualizado a " + ((String)general[0]);
				else
					notificatoinText += "Se han guardado los nuevos detos del material " + material.getTitle();
			}
			
			if(toggleBook.isSelected()){
				Object[] book = bookData.saveData();
				if(material == null){
					MaterialServices.insertMaterial((String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
						(String)general[4], ((Language)general[3]).getId(), MaterialServices.BOOK, (String)general[7],
						   (Integer)book[0], (String)book[1],
						   -1, -1, "",
						   "", "",
						   "", -1, "",
						   selectedSubject, GUI.getInstance().getUser().getId());
					
					notificatoinText += "el nuevo libro ";
				}
				else
					MaterialServices.modifyMaterial(material.getCode(), (String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
							(String)general[4], ((Language)general[3]).getId(), MaterialServices.BOOK, (String)general[7],
							   (Integer)book[0], (String)book[1],
							   -1, -1, "",
							   "", "",
							   "", -1, "",
							   ((Boolean)general[6]).booleanValue());
			}
			else if(toggleMagazine.isSelected()){
				Object[] mag = magazineData.saveData();
				
				if(material == null){
					MaterialServices.insertMaterial((String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
						(String)general[4], ((Language)general[3]).getId(), MaterialServices.MAGAZINE, (String)general[7],
						   -1, "",
						   ((Integer)mag[0]).intValue(), ((Integer)mag[1]).intValue(), (String)mag[2],
						   "", "",
						   "", -1, "",
						   selectedSubject, GUI.getInstance().getUser().getId());
					
					notificatoinText += "la nueva revista ";
				}
				else
					MaterialServices.modifyMaterial(material.getCode(), (String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
							(String)general[4], ((Language)general[3]).getId(), MaterialServices.MAGAZINE, (String)general[7],
							   -1, "",
							   ((Integer)mag[0]).intValue(), ((Integer)mag[1]).intValue(), (String)mag[2],
							   "", "",
							   "", -1, "",
							   ((Boolean)general[6]).booleanValue());
			}
			else if(toggleInternetArticle.isSelected()){
				Object[] article = internetArticleData.saveData();
				
				if(material == null){
					MaterialServices.insertMaterial((String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
						(String)general[4], ((Language)general[3]).getId(), MaterialServices.INTERNET_ARTICLE, (String)general[7],
						   -1, "",
						   -1, -1, "",
						   (String)article[1], (String)article[0],
						   "", -1, "",
						   selectedSubject, GUI.getInstance().getUser().getId());
					
					notificatoinText += "el nuevo articulo de internet ";
				}
				else
					MaterialServices.modifyMaterial(material.getCode(), (String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
							(String)general[4], ((Language)general[3]).getId(), MaterialServices.INTERNET_ARTICLE, (String)general[7],
							   -1, "",
							   -1, -1, "",
							   (String)article[1], (String)article[0],
							   "", -1, "",
							   ((Boolean)general[6]).booleanValue());
			}
			else if(toggleThesis.isSelected()){
				Object[] thesis = thesisData.saveData();
				
				if(material == null){
					MaterialServices.insertMaterial((String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
						(String)general[4], ((Language)general[3]).getId(), MaterialServices.THESIS, (String)general[7],
						   -1, "",
						   -1, -1, "",
						   "", "",
						   (String)thesis[2], ((Integer)thesis[1]).intValue(), (String)thesis[0],
						   selectedSubject, GUI.getInstance().getUser().getId());
					
					notificatoinText += "la nueva tesis ";
				}
				else
					MaterialServices.modifyMaterial(material.getCode(), (String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
							(String)general[4], ((Language)general[3]).getId(), MaterialServices.THESIS, (String)general[7],
							   -1, "",
							   -1, -1, "",
							   "", "",
							   (String)thesis[0], ((Integer)thesis[1]).intValue(), (String)thesis[2],
							   ((Boolean)general[6]).booleanValue());
			}
			else if(toggleMonography.isSelected()){
				if(material == null){
					MaterialServices.insertMaterial((String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
						(String)general[4], ((Language)general[3]).getId(), MaterialServices.MONOGRAPHY, (String)general[7],
						   -1, "",
						   -1, -1, "",
						   "", "",
						   "",-1, "",
						   selectedSubject, GUI.getInstance().getUser().getId());
					
					notificatoinText += "la nueva monografía ";
				}
				else
					MaterialServices.modifyMaterial(material.getCode(), (String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
							(String)general[4], ((Language)general[3]).getId(), MaterialServices.MONOGRAPHY, (String)general[7],
							   -1, "",
							   -1, -1, "",
							   "", "",
							   "",-1, "",
							   ((Boolean)general[6]).booleanValue());
			}
			else if(toggleLesson.isSelected()){
				if(material == null){
					MaterialServices.insertMaterial((String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
						(String)general[4], ((Language)general[3]).getId(), MaterialServices.LESSON, (String)general[7],
						   -1, "",
						   -1, -1, "",
						   "", "",
						   "",-1, "",
						   selectedSubject, GUI.getInstance().getUser().getId());
					
					notificatoinText += "la nueva clase ";
				}
				else
					MaterialServices.modifyMaterial(material.getCode(), (String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
							(String)general[4], ((Language)general[3]).getId(), MaterialServices.LESSON, (String)general[7],
							   -1, "",
							   -1, -1, "",
							   "", "",
							   "",-1, "",
							   ((Boolean)general[6]).booleanValue());
			}
			else{//Otros
				if(material == null){
					MaterialServices.insertMaterial((String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
						(String)general[4], ((Language)general[3]).getId(), MaterialServices.OTHER, (String)general[7],
						   -1, "",
						   -1, -1, "",
						   "", "",
						   "",-1, "",
						   selectedSubject, GUI.getInstance().getUser().getId());
					
					notificatoinText += "el nuevo material ";
				}
				else
					MaterialServices.modifyMaterial(material.getCode(), (String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
							(String)general[4], ((Language)general[3]).getId(), MaterialServices.OTHER, (String)general[7],
							   -1, "",
							   -1, -1, "",
							   "", "",
							   "",-1, "",
							   ((Boolean)general[6]).booleanValue());
			}
			
			if(material == null)
				notificatoinText += (String)general[0];
			
			return notificatoinText;
	}
}

class GeneralData extends AnchorPane{
	
	private Label title;
	
	private Label lblTitleMat;
	private TextField txfTitleMat;
	private Label lblAuthor;
	private TextField txfAuthor;
	private Label lblPublication;
	private NumberField txfPublication;
	private Label lblLanguage;
	private ChoiceBox<Language> chbLanguages;
	private Label lblKeyWord;
	private TextField txfKeyWord;
	private Label lblLocalization;
	private TextField txfLocalization;
	private Label lblCanceled;
	private CheckBox chkCanceled;
	private Label lblDescription;
	private TextArea txtDescription;
	
	public GeneralData(Material material){
		
		title = new Label("Datos generales");
		title.getStyleClass().add("title");
		AnchorPane.setTopAnchor(title, Double.valueOf(10));
		AnchorPane.setLeftAnchor(title, Double.valueOf(35));
		
		lblTitleMat = new Label("Título");
		AnchorPane.setTopAnchor(lblTitleMat, Double.valueOf(42));
		AnchorPane.setLeftAnchor(lblTitleMat, Double.valueOf(10));
		txfTitleMat = new TextField();
		txfTitleMat.setPrefWidth(150);
		AnchorPane.setTopAnchor(txfTitleMat, Double.valueOf(40));
		AnchorPane.setLeftAnchor(txfTitleMat, Double.valueOf(55));
		
		lblAuthor = new Label("Autor");
		AnchorPane.setTopAnchor(lblAuthor, Double.valueOf(70));
		AnchorPane.setLeftAnchor(lblAuthor, Double.valueOf(10));
		txfAuthor = new TextField();
		txfAuthor.setPrefWidth(150);
		AnchorPane.setTopAnchor(txfAuthor, Double.valueOf(68));
		AnchorPane.setLeftAnchor(txfAuthor, Double.valueOf(55));
		
		lblPublication = new Label("Año publicación");
		AnchorPane.setTopAnchor(lblPublication, Double.valueOf(98));
		AnchorPane.setLeftAnchor(lblPublication, Double.valueOf(10));
		txfPublication = new NumberField();
		txfPublication.setPrefWidth(94);
		AnchorPane.setTopAnchor(txfPublication, Double.valueOf(96));
		AnchorPane.setLeftAnchor(txfPublication, Double.valueOf(110));
		
		lblLanguage = new Label("Idioma");
		AnchorPane.setTopAnchor(lblLanguage, Double.valueOf(126));
		AnchorPane.setLeftAnchor(lblLanguage, Double.valueOf(10));
		chbLanguages = new ChoiceBox<Language>();
		chbLanguages.setPrefWidth(150);
		try{
			User user = GUI.getInstance().getUser();
			List<Language> langs = null;
			if( user != null && user.getPrivilege() != null && user.getPrivilege().isModifyLanguage())
				langs = LanguageService.getLanguages();
			else
				langs = LanguageService.getEnablesLanguages();
			
			chbLanguages.getItems().addAll(langs);
		} catch(SQLException e){
			
		}
		AnchorPane.setTopAnchor(chbLanguages, Double.valueOf(124));
		AnchorPane.setLeftAnchor(chbLanguages, Double.valueOf(55));
		
		lblKeyWord = new Label("Palabras claves");
		AnchorPane.setTopAnchor(lblKeyWord, Double.valueOf(154));
		AnchorPane.setLeftAnchor(lblKeyWord, Double.valueOf(10));
		txfKeyWord = new TextField();
		txfKeyWord.setPrefWidth(400);
		AnchorPane.setTopAnchor(txfKeyWord, Double.valueOf(152));
		AnchorPane.setLeftAnchor(txfKeyWord, Double.valueOf(110));
		
		lblLocalization = new Label("Localización");
		AnchorPane.setTopAnchor(lblLocalization, Double.valueOf(182));
		AnchorPane.setLeftAnchor(lblLocalization, Double.valueOf(10));
		txfLocalization = new TextField();
		txfLocalization.setPrefWidth(400);
		AnchorPane.setTopAnchor(txfLocalization, Double.valueOf(180));
		AnchorPane.setLeftAnchor(txfLocalization, Double.valueOf(110));
		
		lblCanceled = new Label("Cancelar");
		AnchorPane.setTopAnchor(lblCanceled, Double.valueOf(41));
		AnchorPane.setLeftAnchor(lblCanceled, Double.valueOf(230));
		chkCanceled = new CheckBox();
		AnchorPane.setTopAnchor(chkCanceled, Double.valueOf(41));
		AnchorPane.setLeftAnchor(chkCanceled, Double.valueOf(310));
		
		lblDescription = new Label("Descripción");
		AnchorPane.setTopAnchor(lblDescription, Double.valueOf(69));
		AnchorPane.setLeftAnchor(lblDescription, Double.valueOf(230));
		txtDescription = new TextArea();
		txtDescription.setWrapText(true);
		txtDescription.setPrefWidth(200);
		txtDescription.setPrefHeight(80);
		AnchorPane.setTopAnchor(txtDescription, Double.valueOf(67));
		AnchorPane.setLeftAnchor(txtDescription, Double.valueOf(310));
		
		getChildren().addAll(
				title,
				lblTitleMat, txfTitleMat,
				lblAuthor, txfAuthor,
				lblPublication, txfPublication,
				lblLanguage, chbLanguages,
				lblKeyWord, txfKeyWord,
				lblLocalization, txfLocalization,
				lblCanceled, chkCanceled,
				lblDescription, txtDescription
				);
		
		if(material != null){
			txfTitleMat.setText(material.getTitle());
			txfAuthor.setText(material.getAuthor());
			txfPublication.setText(String.valueOf(material.getYearOfPublication()));
			Language lang = Language.find(chbLanguages.getItems(), material.getLanguage());
			if(lang != null)
				chbLanguages.getSelectionModel().select(lang);
			txfKeyWord.setText(material.getKeywordsFull());
			txfLocalization.setText(material.getLocation());
			chkCanceled.setSelected(material.isCanceled());
			if(material.getDescription() != null)
				txtDescription.setText(material.getDescription());
		}
	}
	
	public Object[] saveData() throws ValidationErrorException{
		String title = txfTitleMat.getText();
		if(title == null || title.isEmpty() || Utilities.isEmpty(title))
			throw new ValidationErrorException("El campo \"Título\" no debe estar vacío");
		
		String author = txfAuthor.getText();
		if(author == null || author.isEmpty() || Utilities.isEmpty(author))
			throw new ValidationErrorException("El campo \"Autor\" no debe estar vacío");
		
		String anyoCad = txfPublication.getText();		
		if(anyoCad == null || anyoCad.isEmpty() || Utilities.isEmpty(anyoCad))
			throw new ValidationErrorException("El campo \"Año publicación\" no debe estar vacío");
		Integer anyo = Integer.valueOf(anyoCad);
		if(anyo == 0)
			throw new ValidationErrorException("El campo \"Año publicación\" no debe ser cero");
		
		Language lang = chbLanguages.getSelectionModel().getSelectedItem();
		if(lang == null)
			throw new ValidationErrorException("Debe seleccionar un \"idioma\"");
		
		String keys = txfKeyWord.getText();
		if(keys == null || keys.isEmpty() || Utilities.isEmpty(keys))
			throw new ValidationErrorException("Debe insertar las \"palabras claves\" del material");
		
		String locale = txfLocalization.getText();
		if(locale == null || locale.isEmpty() || Utilities.isEmpty(locale))
			throw new ValidationErrorException("El campo \"Localización\" no debe estar vacío");
				
		return new Object[]{
				title, author, anyo, lang, keys, locale, Boolean.valueOf(chkCanceled.isSelected()), txtDescription.getText()
		};
	}
}

class BookData extends AnchorPane{
	
	private Label lblEdition;
	private NumberField txfEdition;
	private Label lblISBN;
	private TextField txfISBN;
	
	public BookData(final Book book){
		
		lblEdition = new Label("Edición #");
		AnchorPane.setTopAnchor(lblEdition, Double.valueOf(15));
		AnchorPane.setLeftAnchor(lblEdition, Double.valueOf(10));
		txfEdition = new NumberField();
		AnchorPane.setTopAnchor(txfEdition, Double.valueOf(13));
		AnchorPane.setLeftAnchor(txfEdition, Double.valueOf(75));
		
		lblISBN = new Label("ISBN");
		AnchorPane.setTopAnchor(lblISBN, Double.valueOf(43));
		AnchorPane.setLeftAnchor(lblISBN, Double.valueOf(10));
		txfISBN = new TextField();
		AnchorPane.setTopAnchor(txfISBN, Double.valueOf(41));
		AnchorPane.setLeftAnchor(txfISBN, Double.valueOf(75));
		
		getChildren().addAll(
				lblEdition, txfEdition,
				lblISBN, txfISBN
				);
		
		if(book != null){
			txfEdition.setText(String.valueOf(book.getEditionNumber()));
			txfISBN.setText(book.getISBN());
		}
	}
	
	public Object[] saveData() throws ValidationErrorException{		
		String editionCad = txfEdition.getText();		
		if(editionCad == null || editionCad.isEmpty() || Utilities.isEmpty(editionCad))
			throw new ValidationErrorException("El campo \"Número de edición\" no debe estar vacío");
		Integer edition = Integer.valueOf(editionCad);
		if(edition == 0)
			throw new ValidationErrorException("El campo \"Número de edición\" no debe ser cero");
		
		String isbn = txfISBN.getText();
		if(isbn == null || isbn.isEmpty() || Utilities.isEmpty(isbn))
			throw new ValidationErrorException("El campo \"ISBN\" no debe estar vacío");
		
		
		return new Object[]{
				edition, isbn
		};
	}
}

class InternetArticleData extends AnchorPane{
	
	private Label lblDate;
	private TextField txfDate;
	private Label lblWebAddress;
	private TextArea txtWebAddress;
	
	public InternetArticleData(final InternetArticle intArticle){
		
		lblDate = new Label("Fecha de confección");
		AnchorPane.setTopAnchor(lblDate, Double.valueOf(15));
		AnchorPane.setLeftAnchor(lblDate, Double.valueOf(10));
		txfDate = new TextField();
		AnchorPane.setTopAnchor(txfDate, Double.valueOf(13));
		AnchorPane.setLeftAnchor(txfDate, Double.valueOf(145));
		
		lblWebAddress = new Label("Dirección web");
		AnchorPane.setTopAnchor(lblWebAddress, Double.valueOf(43));
		AnchorPane.setLeftAnchor(lblWebAddress, Double.valueOf(10));
		txtWebAddress = new TextArea();
		txtWebAddress.setWrapText(true);
		txtWebAddress.setPrefWidth(365);
		txtWebAddress.setPrefHeight(45);
		AnchorPane.setTopAnchor(txtWebAddress, Double.valueOf(41));
		AnchorPane.setLeftAnchor(txtWebAddress, Double.valueOf(145));
		
		getChildren().addAll(lblDate, txfDate, lblWebAddress, txtWebAddress);
		
		if(intArticle != null){
			txfDate.setText(intArticle.getDate());
			txtWebAddress.setText(intArticle.getAddress());
		}
	}
	
	public Object[] saveData() throws ValidationErrorException{
		String date = txfDate.getText();
		if(date == null || date.isEmpty() || Utilities.isEmpty(date))
			throw new ValidationErrorException("El campo \"Fecha de confección\" no debe estar vacío");
		
		String address = txtWebAddress.getText();
		if(address == null || address.isEmpty() || Utilities.isEmpty(address))
			throw new ValidationErrorException("El campo \"Dirección web\" no debe estar vacío");
		
		return new Object[]{
				date, address
		};
	}
}

class MagazineData extends AnchorPane{
	
	private Label lblNumber;
	private NumberField txfNumber;
	private Label lblVolume;
	private NumberField txfVolume;
	private Label lblISSN;
	private TextField txfISSN;
	
	public MagazineData(Magazine magazine){
		
		lblNumber = new Label("Número");
		AnchorPane.setTopAnchor(lblNumber, Double.valueOf(15));
		AnchorPane.setLeftAnchor(lblNumber, Double.valueOf(10));
		txfNumber = new NumberField();
		AnchorPane.setTopAnchor(txfNumber, Double.valueOf(13));
		AnchorPane.setLeftAnchor(txfNumber, Double.valueOf(75));
		
		lblVolume = new Label("Volumen");
		AnchorPane.setTopAnchor(lblVolume, Double.valueOf(43));
		AnchorPane.setLeftAnchor(lblVolume, Double.valueOf(10));
		txfVolume = new NumberField();
		AnchorPane.setTopAnchor(txfVolume, Double.valueOf(41));
		AnchorPane.setLeftAnchor(txfVolume, Double.valueOf(75));
		
		lblISSN = new Label("ISSN");
		AnchorPane.setTopAnchor(lblISSN, Double.valueOf(15));
		AnchorPane.setLeftAnchor(lblISSN, Double.valueOf(230));
		txfISSN = new TextField();
		AnchorPane.setTopAnchor(txfISSN, Double.valueOf(13));
		AnchorPane.setLeftAnchor(txfISSN, Double.valueOf(273));
		
		getChildren().addAll(lblNumber, txfNumber,
				lblVolume, txfVolume,
				lblISSN, txfISSN
				);
		
		if(magazine != null){
			txfNumber.setText(String.valueOf(magazine.getNumber()));
			txfVolume.setText(String.valueOf(magazine.getVolume()));
			txfISSN.setText(magazine.getISSN());
		}
	}
	
	public Object[] saveData() throws ValidationErrorException{
		String numberCad = txfNumber.getText();		
		if(numberCad == null || numberCad.isEmpty() || Utilities.isEmpty(numberCad))
			throw new ValidationErrorException("El campo \"Número\" no debe estar vacío");
		Integer number = Integer.valueOf(numberCad);
		if(number == 0)
			throw new ValidationErrorException("El campo \"Número\" no debe ser cero");
		
		String volumeCad = txfVolume.getText();		
		if(volumeCad == null || volumeCad.isEmpty() || Utilities.isEmpty(volumeCad))
			throw new ValidationErrorException("El campo \"Volumen\" no debe estar vacío");
		Integer volume = Integer.valueOf(numberCad);
		if(volume == 0)
			throw new ValidationErrorException("El campo \"Volumen\" no debe ser cero");
		
		String issn = txfISSN.getText();		
		if(issn == null || issn.isEmpty() || Utilities.isEmpty(issn))
			throw new ValidationErrorException("El campo \"ISSN\" no debe estar vacío");
		
		return new Object[]{
				number, volume, issn
		};
	}
}

class ThesisData extends AnchorPane{
	
	private Label lblDegree;
	private TextField txfDegree;
	private Label lblPages;
	private NumberField txfPages;
	private Label lblUniversity;
	private TextField txfUniversity;
	
	public ThesisData(final Thesis thesis){
		
		lblDegree = new Label("Grado por el que opta");
		AnchorPane.setTopAnchor(lblDegree, Double.valueOf(15));
		AnchorPane.setLeftAnchor(lblDegree, Double.valueOf(10));
		txfDegree = new TextField();
		AnchorPane.setTopAnchor(txfDegree, Double.valueOf(13));
		AnchorPane.setLeftAnchor(txfDegree, Double.valueOf(150));
		
		lblPages = new Label("Cantidad de páginas");
		AnchorPane.setTopAnchor(lblPages, Double.valueOf(43));
		AnchorPane.setLeftAnchor(lblPages, Double.valueOf(10));
		txfPages = new NumberField();
		AnchorPane.setTopAnchor(txfPages, Double.valueOf(41));
		AnchorPane.setLeftAnchor(txfPages, Double.valueOf(150));
		
		lblUniversity = new Label("Universidad");
		AnchorPane.setTopAnchor(lblUniversity, Double.valueOf(71));
		AnchorPane.setLeftAnchor(lblUniversity, Double.valueOf(10));
		txfUniversity = new TextField();
		AnchorPane.setTopAnchor(txfUniversity, Double.valueOf(69));
		AnchorPane.setLeftAnchor(txfUniversity, Double.valueOf(150));
		
		getChildren().addAll(
				lblDegree, txfDegree,
				lblPages, txfPages,
				lblUniversity, txfUniversity
				);
		
		if(thesis != null){
			txfDegree.setText(thesis.getDegree());
			txfPages.setText(String.valueOf(thesis.getAmountPages()));
			txfUniversity.setText(thesis.getUniversity());
		}
	}
	
	public Object[] saveData() throws ValidationErrorException{
		String degree = txfDegree.getText();		
		if(degree == null || degree.isEmpty() || Utilities.isEmpty(degree))
			throw new ValidationErrorException("El campo \"Grado por el que opta\" no debe estar vacío");
		
		String pagesCad = txfPages.getText();		
		if(pagesCad == null || pagesCad.isEmpty() || Utilities.isEmpty(pagesCad))
			throw new ValidationErrorException("El campo \"Cantidad de páginas\" no debe estar vacío");
		Integer pages = Integer.valueOf(pagesCad);
		if(pages == 0)
			throw new ValidationErrorException("El campo \"Cantidad de páginas\" no debe ser cero");
		
		String university = txfUniversity.getText();		
		if(university == null || university.isEmpty() || Utilities.isEmpty(university))
			throw new ValidationErrorException("El campo \"Universidad\" no debe estar vacío");
		
		return new Object[]{
				degree, pages, university
		};
	}
}