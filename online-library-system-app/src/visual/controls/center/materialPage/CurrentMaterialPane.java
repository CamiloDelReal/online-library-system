package visual.controls.center.materialPage;

import java.sql.SQLException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import models.Language;
import models.materials.Book;
import models.materials.InternetArticle;
import models.materials.Lesson;
import models.materials.Magazine;
import models.materials.Material;
import models.materials.Monograph;
import models.materials.Thesis;
import models.users.User;
import services.LanguageService;
import services.MaterialServices;
import utils.DialogBox;
import utils.GUI;
import utils.NumberField;
import utils.OpButton;
import utils.Utilities;
import utils.ValidationErrorException;
import visual.controls.Notification;

public class CurrentMaterialPane extends BorderPane {
	private Material material;
	private int subjectID;
	
	private GeneralDataToShow generalPane;
	private BookDataToShow bookPane;
	private InternetArticleDataToShow articlePane;
	private MagazineDataToShow magazinePane;
	private ThesisDataToShow thesisPane;
	@SuppressWarnings("rawtypes")
	private Operations currentPane;
	private VBox data;
	
	private OpButton btnModify;
	private OpButton btnDelete;
	private HBox buttons;
	private boolean showingDelete = true;	
	
	public CurrentMaterialPane(){
		setId("currentMaterialPane");
		
		generalPane = new GeneralDataToShow();
		bookPane = new BookDataToShow();
		articlePane = new InternetArticleDataToShow();
		magazinePane = new MagazineDataToShow();
		thesisPane = new ThesisDataToShow();
		
		data = new VBox();
		data.getChildren().add(generalPane);
		
		setCenter(data);
		
		btnModify = new OpButton("Modificar");
		btnModify.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				if(btnModify.getText().equalsIgnoreCase("Modificar")){
					setToModify();
					btnModify.setText("Guardar");
					btnDelete.setText("Cancelar");
					if(!showingDelete)
						buttons.getChildren().add(btnDelete);
				}
				else{
					boolean success = true;
					try {
						String notificationText = saveData();
						Notification.getInstance().setText(notificationText);
						Notification.getInstance().showNotification();
					} catch (SQLException | ValidationErrorException e1) {
						success = false;
						DialogBox diag = new DialogBox(e1.getMessage());
						diag.show();
					}
					
					if(success){
						GUI.getInstance().update();						
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
					ObservableList<Item> items = FXCollections.observableArrayList( 
							new Item(CurrentMaterialPane.this.material.getCode(), CurrentMaterialPane.this.material.getTitle(), FileExtension.DEFAULT,
									CurrentMaterialPane.this.material.isCanceled(), TypeItem.MATERIAL, "")
							);
					DialogCancelTematicSubjectMaterial diag = new DialogCancelTematicSubjectMaterial(TypeItem.MATERIAL,	items, CurrentMaterialPane.this.subjectID, true);
					diag.show();
					
				}
				else{
					setToShow();
					
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
		buttons.getChildren().addAll(spacer, btnModify, btnDelete);
		
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
	
	public void setMaterial(Material material){
		setDefaultTextButtons();
		
		this.material = material;
		generalPane.setMaterial(material);
		
		if(material instanceof Book){
			if(currentPane != null)
				data.getChildren().remove(currentPane);
			bookPane.setMaterial((Book)material);
			data.getChildren().add(bookPane);
			currentPane = bookPane;
		}
		else if(material instanceof Magazine){
			if(currentPane != null)
				data.getChildren().remove(currentPane);
			magazinePane.setMaterial((Magazine)material);
			data.getChildren().add(magazinePane);
			currentPane = magazinePane;
		}
		else if(material instanceof InternetArticle){
			if(currentPane != null)
				data.getChildren().remove(currentPane);
			articlePane.setMaterial((InternetArticle)material);
			data.getChildren().add(articlePane);
			currentPane = articlePane;
		}
		else if(material instanceof Thesis){
			if(currentPane != null)
				data.getChildren().remove(currentPane);
			thesisPane.setMaterial((Thesis)material);
			data.getChildren().add(thesisPane);
			currentPane = thesisPane;
		}
		else{
			if(currentPane != null)
				data.getChildren().remove(currentPane);
			currentPane = null;
		}
	}
	public void setSubjectId(int subjectId){
		this.subjectID = subjectId;
	}
	
	public void setToModify(){
		generalPane.setToModify();
		if(currentPane != null)
			currentPane.setToModify();
	}
	
	public void setToShow(){
		generalPane.setToShow();
		if(currentPane != null)
			currentPane.setToShow();
	}
	
	private void setDefaultTextButtons(){
		btnModify.setText("Modificar");
		btnDelete.setText("Eliminar");
	}
	
	private String saveData() throws SQLException, ValidationErrorException{
		Object[] general = generalPane.saveData();
		String notificatoinText = "";
		
		if(!material.getTitle().equals((String)general[0]))
			notificatoinText += "El material " + material.getTitle() +" ha sido actualizado a " + ((String)general[0]);
		else
			notificatoinText += "Se han guardado los nuevos detos del material " + material.getTitle();
		
		if(material instanceof Book){
			Object[] book = bookPane.saveData();
			
			MaterialServices.modifyMaterial(material.getCode(), (String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
						(String)general[4], ((Language)general[3]).getId(), MaterialServices.BOOK, (String)general[7],
						   (Integer)book[0], (String)book[1],
						   -1, -1, "",
						   "", "",
						   "", -1, "",
						   ((Boolean)general[6]).booleanValue());
		}
		else if(material instanceof Magazine){
			Object[] mag = magazinePane.saveData();
			
			MaterialServices.modifyMaterial(material.getCode(), (String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
						(String)general[4], ((Language)general[3]).getId(), MaterialServices.MAGAZINE, (String)general[7],
						   -1, "",
						   ((Integer)mag[0]).intValue(), ((Integer)mag[1]).intValue(), (String)mag[2],
						   "", "",
						   "", -1, "",
						   ((Boolean)general[6]).booleanValue());
		}
		else if(material instanceof InternetArticle){
			Object[] article = articlePane.saveData();
			
			MaterialServices.modifyMaterial(material.getCode(), (String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
						(String)general[4], ((Language)general[3]).getId(), MaterialServices.INTERNET_ARTICLE, (String)general[7],
						   -1, "",
						   -1, -1, "",
						   (String)article[1], (String)article[0],
						   "", -1, "",
						   ((Boolean)general[6]).booleanValue());
		}
		else if(material instanceof Thesis){
			Object[] thesis = thesisPane.saveData();
			
			MaterialServices.modifyMaterial(material.getCode(), (String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
						(String)general[4], ((Language)general[3]).getId(), MaterialServices.THESIS, (String)general[7],
						   -1, "",
						   -1, -1, "",
						   "", "",
						   (String)thesis[0], ((Integer)thesis[1]).intValue(), (String)thesis[2],
						   ((Boolean)general[6]).booleanValue());
		}
		else if(material instanceof Monograph){
			MaterialServices.modifyMaterial(material.getCode(), (String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
						(String)general[4], ((Language)general[3]).getId(), MaterialServices.MONOGRAPHY, (String)general[7],
						   -1, "",
						   -1, -1, "",
						   "", "",
						   "",-1, "",
						   ((Boolean)general[6]).booleanValue());
		}
		else if(material instanceof Lesson){
			MaterialServices.modifyMaterial(material.getCode(), (String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
						(String)general[4], ((Language)general[3]).getId(), MaterialServices.LESSON, (String)general[7],
						   -1, "",
						   -1, -1, "",
						   "", "",
						   "",-1, "",
						   ((Boolean)general[6]).booleanValue());
		}
		else{//Otros
			MaterialServices.modifyMaterial(material.getCode(), (String)general[5], (String)general[0], (String)general[1], ((Integer)general[2]).intValue(),
						(String)general[4], ((Language)general[3]).getId(), MaterialServices.OTHER, (String)general[7],
						   -1, "",
						   -1, -1, "",
						   "", "",
						   "",-1, "",
						   ((Boolean)general[6]).booleanValue());
		}
		
		return notificatoinText;
}
}

interface Operations<E>{
	public void setMaterial(E mat);
	public void setToShow();
	public void setToModify();
}

class GeneralDataToShow extends AnchorPane implements Operations<Material>{
	private Material material;
	
	private Label title;
	
	private Label lblTitleMat;
	private Label lblTitleMatShow;
	private TextField txfTitleMat;
	
	private Label lblAuthor;
	private Label lblAuthorShow;
	private TextField txfAuthor;
	
	private Label lblPublication;
	private Label lblPublicationShow;
	private NumberField txfPublication;
	
	private Label lblLanguage;
	private Label lblLanguageShow;
	private ChoiceBox<Language> chbLanguages;
	
	private Label lblKeyWord;
	private Label lblKeyWordShow;
	private TextField txfKeyWord;
	
	private Label lblLocalization;
	private Label lblLocalizationShow;
	private TextField txfLocalization;
	
	private Label lblCanceled;
	private Label lblCanceledShow;
	private CheckBox chkCanceled;
	
	private Label lblDescription;
	private Label lblDescriptionShow;
	private TextArea txtDescription;
	
	private Label lblVisitedHeader;
	private Label lblVisitedByStudents;
	private Label lblVisitedByTeachers;
	private Label lblVisitedByUnkwons;
	
	public GeneralDataToShow(){
		title = new Label("Datos generales");
		title.getStyleClass().add("title");
		AnchorPane.setTopAnchor(title, Double.valueOf(10));
		AnchorPane.setLeftAnchor(title, Double.valueOf(50));
		
		lblTitleMat = new Label("Título:");
		lblTitleMat.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblTitleMat, Double.valueOf(52));
		AnchorPane.setLeftAnchor(lblTitleMat, Double.valueOf(25));
		lblTitleMatShow = new Label();
		lblTitleMatShow.setPrefWidth(320);
		AnchorPane.setTopAnchor(lblTitleMatShow, Double.valueOf(52));
		AnchorPane.setLeftAnchor(lblTitleMatShow, Double.valueOf(70));
		txfTitleMat = new TextField();
		txfTitleMat.setPrefWidth(180);
		AnchorPane.setTopAnchor(txfTitleMat, Double.valueOf(50));
		AnchorPane.setLeftAnchor(txfTitleMat, Double.valueOf(70));
		
		lblAuthor = new Label("Autor:");
		lblAuthor.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblAuthor, Double.valueOf(80));
		AnchorPane.setLeftAnchor(lblAuthor, Double.valueOf(25));
		lblAuthorShow = new Label();
		lblAuthorShow.setPrefWidth(180);
		AnchorPane.setTopAnchor(lblAuthorShow, Double.valueOf(80));
		AnchorPane.setLeftAnchor(lblAuthorShow, Double.valueOf(70));
		txfAuthor = new TextField();
		txfAuthor.setPrefWidth(180);
		AnchorPane.setTopAnchor(txfAuthor, Double.valueOf(78));
		AnchorPane.setLeftAnchor(txfAuthor, Double.valueOf(70));
		
		lblPublication = new Label("Año publicación:");
		lblPublication.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblPublication, Double.valueOf(108));
		AnchorPane.setLeftAnchor(lblPublication, Double.valueOf(25));
		lblPublicationShow = new Label();
		lblPublicationShow.setPrefWidth(180);
		AnchorPane.setTopAnchor(lblPublicationShow, Double.valueOf(108));
		AnchorPane.setLeftAnchor(lblPublicationShow, Double.valueOf(125));
		txfPublication = new NumberField();
		txfPublication.setPrefWidth(124);
		AnchorPane.setTopAnchor(txfPublication, Double.valueOf(106));
		AnchorPane.setLeftAnchor(txfPublication, Double.valueOf(125));
		
		lblLanguage = new Label("Idioma:");
		lblLanguage.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblLanguage, Double.valueOf(136));
		AnchorPane.setLeftAnchor(lblLanguage, Double.valueOf(25));
		lblLanguageShow = new Label();
		lblLanguageShow.setPrefWidth(180);
		AnchorPane.setTopAnchor(lblLanguageShow, Double.valueOf(136));
		AnchorPane.setLeftAnchor(lblLanguageShow, Double.valueOf(70));
		chbLanguages = new ChoiceBox<Language>();
		chbLanguages.setPrefWidth(180);
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
		AnchorPane.setTopAnchor(chbLanguages, Double.valueOf(134));
		AnchorPane.setLeftAnchor(chbLanguages, Double.valueOf(70));
		
		lblKeyWord = new Label("Palabras claves:");
		lblKeyWord.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblKeyWord, Double.valueOf(168));
		AnchorPane.setLeftAnchor(lblKeyWord, Double.valueOf(25));
		lblKeyWordShow = new Label();
		lblKeyWordShow.setPrefWidth(400);
		AnchorPane.setTopAnchor(lblKeyWordShow, Double.valueOf(168));
		AnchorPane.setLeftAnchor(lblKeyWordShow, Double.valueOf(125));
		txfKeyWord = new TextField();
		txfKeyWord.setPrefWidth(400);
		AnchorPane.setTopAnchor(txfKeyWord, Double.valueOf(166));
		AnchorPane.setLeftAnchor(txfKeyWord, Double.valueOf(125));
		
		lblLocalization = new Label("Localización:");
		lblLocalization.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblLocalization, Double.valueOf(196));
		AnchorPane.setLeftAnchor(lblLocalization, Double.valueOf(25));
		lblLocalizationShow = new Label();
		lblLocalizationShow.setPrefWidth(400);
		AnchorPane.setTopAnchor(lblLocalizationShow, Double.valueOf(196));
		AnchorPane.setLeftAnchor(lblLocalizationShow, Double.valueOf(125));
		txfLocalization = new TextField();
		txfLocalization.setPrefWidth(400);
		AnchorPane.setTopAnchor(txfLocalization, Double.valueOf(194));
		AnchorPane.setLeftAnchor(txfLocalization, Double.valueOf(125));
		
		lblCanceled = new Label("Cancelado:");
		lblCanceled.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblCanceled, Double.valueOf(136));
		AnchorPane.setLeftAnchor(lblCanceled, Double.valueOf(275));
		lblCanceledShow = new Label();
		lblCanceledShow.setPrefWidth(180);
		AnchorPane.setTopAnchor(lblCanceledShow, Double.valueOf(136));
		AnchorPane.setLeftAnchor(lblCanceledShow, Double.valueOf(340));
		chkCanceled = new CheckBox();
		AnchorPane.setTopAnchor(chkCanceled, Double.valueOf(136));
		AnchorPane.setLeftAnchor(chkCanceled, Double.valueOf(340));
		
		lblDescription = new Label("Descripción:");
		lblDescription.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblDescription, Double.valueOf(224));
		AnchorPane.setLeftAnchor(lblDescription, Double.valueOf(25));
		lblDescriptionShow = new Label();
		lblDescriptionShow.setPrefWidth(400);
		lblDescriptionShow.setPrefHeight(38);
		lblDescriptionShow.setWrapText(true);
		AnchorPane.setTopAnchor(lblDescriptionShow, Double.valueOf(224));
		AnchorPane.setLeftAnchor(lblDescriptionShow, Double.valueOf(125));
		txtDescription = new TextArea();
		txtDescription.setWrapText(true);
		txtDescription.setPrefWidth(400);
		txtDescription.setPrefHeight(40);
		AnchorPane.setTopAnchor(txtDescription, Double.valueOf(222));
		AnchorPane.setLeftAnchor(txtDescription, Double.valueOf(125));
		
		lblVisitedHeader = new Label("Este material ha sido \nvisitado por " + "##" + " usuarios:");
		lblVisitedHeader.setWrapText(true);
		lblVisitedHeader.setPrefWidth(150);
		AnchorPane.setTopAnchor(lblVisitedHeader, Double.valueOf(60));
		AnchorPane.setLeftAnchor(lblVisitedHeader, Double.valueOf(410));
		
		lblVisitedByStudents = new Label("##" + " estudiantes");
		AnchorPane.setTopAnchor(lblVisitedByStudents, Double.valueOf(92));
		AnchorPane.setLeftAnchor(lblVisitedByStudents, Double.valueOf(417));
		
		lblVisitedByTeachers = new Label("##" + " profesores");
		AnchorPane.setTopAnchor(lblVisitedByTeachers, Double.valueOf(107));
		AnchorPane.setLeftAnchor(lblVisitedByTeachers, Double.valueOf(417));
		
		lblVisitedByUnkwons = new Label("##" + " desconocidos");
		AnchorPane.setTopAnchor(lblVisitedByUnkwons, Double.valueOf(122));
		AnchorPane.setLeftAnchor(lblVisitedByUnkwons, Double.valueOf(417));
	}
	
	public void setToShow(){
		getChildren().removeAll(getChildren());
		
		getChildren().addAll(
				title,
				lblTitleMat, lblTitleMatShow,
				lblAuthor, lblAuthorShow,
				lblPublication, lblPublicationShow,
				lblLanguage, lblLanguageShow,
				lblKeyWord, lblKeyWordShow,
				lblLocalization, lblLocalizationShow,
				lblCanceled, lblCanceledShow,
				lblDescription, lblDescriptionShow,
				lblVisitedHeader, lblVisitedByStudents, lblVisitedByTeachers, lblVisitedByUnkwons
				);
	}
	
	public void setMaterial(Material material){
		this.material = material;
		
		if(material != null){
			lblTitleMatShow.setText(material.getTitle());
			lblAuthorShow.setText(material.getAuthor());
			lblPublicationShow.setText(String.valueOf(material.getYearOfPublication()));
			lblLanguageShow.setText(material.getLanguage());
			lblLocalizationShow.setText(material.getLocation());
			lblKeyWordShow.setText(material.getKeywordsFull());
			lblCanceledShow.setText(material.isCanceled() ? "Si" : "No");
			if(material.getDescription() != null)
				lblDescriptionShow.setText(material.getDescription());
			else
				lblDescriptionShow.setText("< No hay descripción referente al material >");
			lblVisitedHeader.setText("Este material ha sido \nvisitado en " + 
				( material.getVisitedByStudent() + material.getVisitedByTeachers() + material.getVisitedByUnknown() ) 
				+ " ocasiones:");
			lblVisitedByStudents.setText(material.getVisitedByStudent() + " por estudiantes");
			lblVisitedByTeachers.setText(material.getVisitedByTeachers() + " por profesores");
			lblVisitedByUnkwons.setText(material.getVisitedByUnknown() + " por desconocidos");
			
			getChildren().removeAll(getChildren());
			
			getChildren().addAll(
					title,
					lblTitleMat, lblTitleMatShow,
					lblAuthor, lblAuthorShow,
					lblPublication, lblPublicationShow,
					lblLanguage, lblLanguageShow,
					lblKeyWord, lblKeyWordShow,
					lblLocalization, lblLocalizationShow,
					lblCanceled, lblCanceledShow,
					lblDescription, lblDescriptionShow,
					lblVisitedHeader, lblVisitedByStudents, lblVisitedByTeachers, lblVisitedByUnkwons
					);
		}
	}
	
	public void setToModify(){
		if(material != null){
			txfTitleMat.setText(material.getTitle());
			txfAuthor.setText(material.getAuthor());
			txfPublication.setText(String.valueOf(material.getYearOfPublication()));
			Language lang = Language.find(chbLanguages.getItems(), material.getLanguage());
			if(lang != null)
				chbLanguages.getSelectionModel().select(lang);
			txfLocalization.setText(material.getLocation());
			txfKeyWord.setText(material.getKeywordsFull());
			chkCanceled.setSelected(material.isCanceled());
			if(material.getDescription() != null)
				txtDescription.setText(material.getDescription());
			else
				txtDescription.setText("");
			
			getChildren().removeAll(getChildren());
			
			getChildren().addAll(
					title,
					lblTitleMat, txfTitleMat,
					lblAuthor, txfAuthor,
					lblPublication, txfPublication,
					lblLanguage, chbLanguages,
					lblKeyWord, txfKeyWord,
					lblLocalization, txfLocalization,
					lblCanceled, chkCanceled,
					lblDescription, txtDescription,
					lblVisitedHeader, lblVisitedByStudents, lblVisitedByTeachers, lblVisitedByUnkwons
					);
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

class BookDataToShow extends AnchorPane implements Operations<Book>{
	private Book book;
	
	private Label title;
	
	private Label lblEdition;
	private Label lblEditionShow;
	private NumberField txfEdition;
	private Label lblISBN;
	private Label lblISBNShow;
	private TextField txfISBN;
	
	public BookDataToShow(){
		
		title = new Label("Datos del libro");
		title.getStyleClass().add("title");
		AnchorPane.setTopAnchor(title, Double.valueOf(10));
		AnchorPane.setLeftAnchor(title, Double.valueOf(50));
		
		lblEdition = new Label("Edición #:");
		lblEdition.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblEdition, Double.valueOf(45));
		AnchorPane.setLeftAnchor(lblEdition, Double.valueOf(25));
		lblEditionShow = new Label();
		AnchorPane.setTopAnchor(lblEditionShow, Double.valueOf(45));
		AnchorPane.setLeftAnchor(lblEditionShow, Double.valueOf(90));
		txfEdition = new NumberField();
		AnchorPane.setTopAnchor(txfEdition, Double.valueOf(43));
		AnchorPane.setLeftAnchor(txfEdition, Double.valueOf(90));
		
		lblISBN = new Label("ISBN:");
		lblISBN.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblISBN, Double.valueOf(73));
		AnchorPane.setLeftAnchor(lblISBN, Double.valueOf(25));
		lblISBNShow = new Label();
		AnchorPane.setTopAnchor(lblISBNShow, Double.valueOf(73));
		AnchorPane.setLeftAnchor(lblISBNShow, Double.valueOf(90));
		txfISBN = new TextField();
		AnchorPane.setTopAnchor(txfISBN, Double.valueOf(71));
		AnchorPane.setLeftAnchor(txfISBN, Double.valueOf(90));
	}
	
	public void setToShow(){
		getChildren().removeAll(getChildren());
		
		getChildren().addAll(
				title,
				lblEdition, lblEditionShow,
				lblISBN, lblISBNShow
				);
	}
	
	public void setMaterial(Book book){
		this.book = book;
		
		if(book != null){
			lblEditionShow.setText(String.valueOf(book.getEditionNumber()));
			lblISBNShow.setText(book.getISBN());
			
			getChildren().removeAll(getChildren());
			
			getChildren().addAll(
					title,
					lblEdition, lblEditionShow,
					lblISBN, lblISBNShow
					);
		}
	}
	
	public void setToModify(){
		if(book != null){
			txfEdition.setText(String.valueOf(book.getEditionNumber()));
			txfISBN.setText(book.getISBN());
			
			getChildren().removeAll(getChildren());
			
			getChildren().addAll(
					title,
					lblEdition, txfEdition,
					lblISBN, txfISBN
					);
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

class InternetArticleDataToShow extends AnchorPane implements Operations<InternetArticle>{
	private InternetArticle article;
	
	private Label title;
	
	private Label lblDate;
	private Label lblDateShow;
	private TextField txfDate;
	private Label lblWebAddress;
	private Label lblWebAddressShow;
	private TextField txfWebAddress;
	
	public InternetArticleDataToShow(){
		title = new Label("Datos del artículo de internet");
		title.getStyleClass().add("title");
		AnchorPane.setTopAnchor(title, Double.valueOf(10));
		AnchorPane.setLeftAnchor(title, Double.valueOf(50));
		
		lblDate = new Label("Fecha de confección:");
		lblDate.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblDate, Double.valueOf(45));
		AnchorPane.setLeftAnchor(lblDate, Double.valueOf(25));
		lblDateShow = new Label();
		AnchorPane.setTopAnchor(lblDateShow, Double.valueOf(45));
		AnchorPane.setLeftAnchor(lblDateShow, Double.valueOf(160));
		txfDate = new TextField();
		AnchorPane.setTopAnchor(txfDate, Double.valueOf(43));
		AnchorPane.setLeftAnchor(txfDate, Double.valueOf(160));
		
		lblWebAddress = new Label("Dirección web:");
		lblWebAddress.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblWebAddress, Double.valueOf(73));
		AnchorPane.setLeftAnchor(lblWebAddress, Double.valueOf(25));
		lblWebAddressShow = new Label();
		lblWebAddressShow.setPrefWidth(365);
		AnchorPane.setTopAnchor(lblWebAddressShow, Double.valueOf(73));
		AnchorPane.setLeftAnchor(lblWebAddressShow, Double.valueOf(160));
		txfWebAddress = new TextField();
		txfWebAddress.setPrefWidth(365);
		AnchorPane.setTopAnchor(txfWebAddress, Double.valueOf(71));
		AnchorPane.setLeftAnchor(txfWebAddress, Double.valueOf(160));
		
	}

	public void setMaterial(InternetArticle article) {
		this.article = article;
		
		if(article != null){
			lblDateShow.setText(article.getDate());
			lblWebAddressShow.setText(article.getAddress());
		
			getChildren().removeAll(getChildren());
			
			getChildren().addAll(title, lblDate, lblDateShow, lblWebAddress, lblWebAddressShow);
		}
		
	}

	public void setToShow() {
		getChildren().removeAll(getChildren());
		
		getChildren().addAll(title, lblDate, lblDateShow, lblWebAddress, lblWebAddressShow);		
	}

	public void setToModify() {
		if(article != null){
			txfDate.setText(article.getDate());
			txfWebAddress.setText(article.getAddress());
		
			getChildren().removeAll(getChildren());
			
			getChildren().addAll(title, lblDate, txfDate, lblWebAddress, txfWebAddress);
		}
	}
	
	public Object[] saveData() throws ValidationErrorException{
		String date = txfDate.getText();
		if(date == null || date.isEmpty() || Utilities.isEmpty(date))
			throw new ValidationErrorException("El campo \"Fecha de confección\" no debe estar vacío");
		
		String address = txfWebAddress.getText();
		if(address == null || address.isEmpty() || Utilities.isEmpty(address))
			throw new ValidationErrorException("El campo \"Dirección web\" no debe estar vacío");
		
		return new Object[]{
				date, address
		};
	}
}

class MagazineDataToShow extends AnchorPane implements Operations<Magazine>{
	private Magazine magazine;
	
	private Label title;
	
	private Label lblNumber;
	private Label lblNumberShow;
	private NumberField txfNumber;
	private Label lblVolume;
	private Label lblVolumeShow;
	private NumberField txfVolume;
	private Label lblISSN;
	private Label lblISSNShow;
	private TextField txfISSN;
	
	public MagazineDataToShow(){
		title = new Label("Datos de la revista");
		title.getStyleClass().add("title");
		AnchorPane.setTopAnchor(title, Double.valueOf(10));
		AnchorPane.setLeftAnchor(title, Double.valueOf(50));
		
		lblNumber = new Label("Número:");
		lblNumber.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblNumber, Double.valueOf(45));
		AnchorPane.setLeftAnchor(lblNumber, Double.valueOf(25));
		lblNumberShow = new Label();
		AnchorPane.setTopAnchor(lblNumberShow, Double.valueOf(45));
		AnchorPane.setLeftAnchor(lblNumberShow, Double.valueOf(90));
		txfNumber = new NumberField();
		AnchorPane.setTopAnchor(txfNumber, Double.valueOf(43));
		AnchorPane.setLeftAnchor(txfNumber, Double.valueOf(90));
		
		lblVolume = new Label("Volumen:");
		lblVolume.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblVolume, Double.valueOf(73));
		AnchorPane.setLeftAnchor(lblVolume, Double.valueOf(25));
		lblVolumeShow = new Label();
		AnchorPane.setTopAnchor(lblVolumeShow, Double.valueOf(73));
		AnchorPane.setLeftAnchor(lblVolumeShow, Double.valueOf(90));
		txfVolume = new NumberField();
		AnchorPane.setTopAnchor(txfVolume, Double.valueOf(71));
		AnchorPane.setLeftAnchor(txfVolume, Double.valueOf(90));
		
		lblISSN = new Label("ISSN:");
		lblISSN.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblISSN, Double.valueOf(45));
		AnchorPane.setLeftAnchor(lblISSN, Double.valueOf(245));
		lblISSNShow = new Label();
		AnchorPane.setTopAnchor(lblISSNShow, Double.valueOf(45));
		AnchorPane.setLeftAnchor(lblISSNShow, Double.valueOf(288));
		txfISSN = new TextField();
		AnchorPane.setTopAnchor(txfISSN, Double.valueOf(43));
		AnchorPane.setLeftAnchor(txfISSN, Double.valueOf(288));
		
		
	}

	public void setMaterial(Magazine magazine) {
		this.magazine = magazine;
		
		if(magazine != null){
			lblNumberShow.setText(String.valueOf(magazine.getNumber()));
			lblVolumeShow.setText(String.valueOf(magazine.getVolume()));
			lblISSNShow.setText(magazine.getISSN());
			
			getChildren().removeAll(getChildren());
			
			getChildren().addAll(
					title,
					lblNumber, lblNumberShow,
					lblVolume, lblVolumeShow,
					lblISSN, lblISSNShow
					);
		}
	}

	public void setToShow() {
		getChildren().removeAll(getChildren());
		
		getChildren().addAll(
				title,
				lblNumber, lblNumberShow,
				lblVolume, lblVolumeShow,
				lblISSN, lblISSNShow
				);
	}

	public void setToModify() {
		if(magazine != null){
			txfNumber.setText(String.valueOf(magazine.getNumber()));
			txfVolume.setText(String.valueOf(magazine.getVolume()));
			txfISSN.setText(magazine.getISSN());
			
			getChildren().removeAll(getChildren());
			
			getChildren().addAll(
					title,
					lblNumber, txfNumber,
					lblVolume, txfVolume,
					lblISSN, txfISSN
					);
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

class ThesisDataToShow extends AnchorPane implements Operations<Thesis>{
	private Thesis thesis;
	
	private Label title;
	
	private Label lblDegree;
	private Label lblDegreeShow;
	private TextField txfDegree;
	private Label lblPages;
	private Label lblPagesShow;
	private NumberField txfPages;
	private Label lblUniversity;
	private Label lblUniversityShow;
	private TextField txfUniversity;
	
	public ThesisDataToShow(){
		title = new Label("Datos de la tesis");
		title.getStyleClass().add("title");
		AnchorPane.setTopAnchor(title, Double.valueOf(10));
		AnchorPane.setLeftAnchor(title, Double.valueOf(50));
		
		lblDegree = new Label("Grado por el que opta:");
		lblDegree.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblDegree, Double.valueOf(45));
		AnchorPane.setLeftAnchor(lblDegree, Double.valueOf(25));
		lblDegreeShow = new Label();
		AnchorPane.setTopAnchor(lblDegreeShow, Double.valueOf(45));
		AnchorPane.setLeftAnchor(lblDegreeShow, Double.valueOf(165));
		txfDegree = new TextField();
		AnchorPane.setTopAnchor(txfDegree, Double.valueOf(43));
		AnchorPane.setLeftAnchor(txfDegree, Double.valueOf(165));
		
		lblPages = new Label("Cantidad de páginas:");
		lblPages.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblPages, Double.valueOf(71));
		AnchorPane.setLeftAnchor(lblPages, Double.valueOf(25));
		lblPagesShow = new Label();
		AnchorPane.setTopAnchor(lblPagesShow, Double.valueOf(71));
		AnchorPane.setLeftAnchor(lblPagesShow, Double.valueOf(165));
		txfPages = new NumberField();
		AnchorPane.setTopAnchor(txfPages, Double.valueOf(69));
		AnchorPane.setLeftAnchor(txfPages, Double.valueOf(165));
		
		lblUniversity = new Label("Universidad:");
		lblUniversity.getStyleClass().add("bold");
		AnchorPane.setTopAnchor(lblUniversity, Double.valueOf(45));
		AnchorPane.setLeftAnchor(lblUniversity, Double.valueOf(330));
		lblUniversityShow = new Label();
		AnchorPane.setTopAnchor(lblUniversityShow, Double.valueOf(45));
		AnchorPane.setLeftAnchor(lblUniversityShow, Double.valueOf(415));
		txfUniversity = new TextField();
		AnchorPane.setTopAnchor(txfUniversity, Double.valueOf(43));
		AnchorPane.setLeftAnchor(txfUniversity, Double.valueOf(415));
		
	}

	public void setMaterial(Thesis thesis) {
		this.thesis = thesis;
		
		if(thesis != null){
			lblDegreeShow.setText(thesis.getDegree());
			lblPagesShow.setText(String.valueOf(thesis.getAmountPages()));
			lblUniversityShow.setText(thesis.getUniversity());
			
			getChildren().removeAll(getChildren());
			
			getChildren().addAll(
					title,
					lblDegree, lblDegreeShow,
					lblPages, lblPagesShow,
					lblUniversity, lblUniversityShow
					);
		}		
	}

	public void setToShow() {
		getChildren().removeAll(getChildren());
		
		getChildren().addAll(
				title,
				lblDegree, lblDegreeShow,
				lblPages, lblPagesShow,
				lblUniversity, lblUniversityShow
				);		
	}

	public void setToModify() {
		if(thesis != null){
			txfDegree.setText(thesis.getDegree());
			txfPages.setText(String.valueOf(thesis.getAmountPages()));
			txfUniversity.setText(thesis.getUniversity());
			
			getChildren().removeAll(getChildren());
			
			getChildren().addAll(
					title,
					lblDegree, txfDegree,
					lblPages, txfPages,
					lblUniversity, txfUniversity
					);
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