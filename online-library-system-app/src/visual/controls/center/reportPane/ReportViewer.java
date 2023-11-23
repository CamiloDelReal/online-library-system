package visual.controls.center.reportPane;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import models.Subject;
import models.TeachingCategory;
import models.Tematic;
import models.users.MiniUser;
import models.users.User;
import reports.ReportManager;
import services.MaterialServices;
import services.SubjectServices;
import services.TeachingCategoryServices;
import services.TematicServices;
import services.UserServices;
import utils.DialogBox;
import utils.GUI;
import utils.NumberField;
import utils.OpButton;
import utils.Utilities;

public class ReportViewer extends BorderPane {
	private static ReportViewer pane = null;
	public static ReportViewer getInstance(){
		if(pane == null)
			pane = new ReportViewer();
		return pane;
	}
	
	private Label title;
	
	private AnchorPane centerRoot;
	
	private Label lblReport1;
	private OpButton btnReport1;
	
	private Label lblReport2;
	private OpButton btnReport2;
	
	private Label lblReport3;
	private OpButton btnReport3;
	
	private Label lblReport4;
	private OpButton btnReport4;
	
	private Label lblReport5;
	private ChoiceBox<String> menuAuthors;
	private OpButton btnReport5;
	
	private Label lblReport6;
	private ChoiceBox<MiniUser> menuTeacher;
	private OpButton btnReport6;
	
	private Label lblReport7;
	private NumberField number1Field;
	private OpButton btnReport7;
	
	private Label lblReport8;
	private ChoiceBox<TeachingCategory> menuTeachingCategory;
	private NumberField number2Field;
	private OpButton btnReport8;
	
	private Label lblReport9;
	private ChoiceBox<Subject> menuSubject;
	private OpButton btnReport9;
	
	private Label lblReport10;
	private ChoiceBox<Tematic> menuTematic;
	private OpButton btnReport10;
	
	private ReportViewer(){
		setId("reportViewer");
		
		title = new Label("Reportes disponibles");
		title.getStyleClass().add("title");
		setTop(title);
		
		centerRoot = new AnchorPane();
		
		lblReport1 = new Label("Listado con todos los materiales de consulta " +
				"ordenados por su año de publicación y agrupados por su tipo" +
				" de referencia (Libro, Revista, Tesis, etc).");
		lblReport1.setPrefWidth(170);
		lblReport1.setPrefHeight(80);
		lblReport1.setWrapText(true);
		btnReport1 = new OpButton("Generar");
		btnReport1.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				ReportManager.getInstance().materialOrderByYearAndGroupByType();
			}
		});
		AnchorPane.setTopAnchor(lblReport1, Double.valueOf(15));
		AnchorPane.setLeftAnchor(lblReport1, Double.valueOf(15));
		AnchorPane.setTopAnchor(btnReport1, Double.valueOf(64));
		AnchorPane.setLeftAnchor(btnReport1, Double.valueOf(190));
		
		lblReport2 = new Label("Listado con los valores estadisticos " +
				"de las consultas realizadas a cada tipo de material " +
				"por parte de estudiantes, profesores y usuarios desconocidos.");
		lblReport2.setPrefWidth(190);
		lblReport2.setPrefHeight(80);
		lblReport2.setWrapText(true);
		btnReport2 = new OpButton("Generar");
		btnReport2.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				ReportManager.getInstance().visitForEachTypeOfMaterial();
			}
		});
		AnchorPane.setTopAnchor(lblReport2, Double.valueOf(15));
		AnchorPane.setLeftAnchor(lblReport2, Double.valueOf(310));
		AnchorPane.setTopAnchor(btnReport2, Double.valueOf(64));
		AnchorPane.setLeftAnchor(btnReport2, Double.valueOf(505));
		
		lblReport3 = new Label("Listado de las temáticas existentes " +
				"y con el monto total de materiales que a ella pertenecen.");
		lblReport3.setPrefWidth(170);
		lblReport3.setPrefHeight(80);
		lblReport3.setWrapText(true);
		btnReport3 = new OpButton("Generar");
		btnReport3.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				ReportManager.getInstance().countMeterialInTematics();
			}
		});
		AnchorPane.setTopAnchor(lblReport3, Double.valueOf(110));
		AnchorPane.setLeftAnchor(lblReport3, Double.valueOf(15));
		AnchorPane.setTopAnchor(btnReport3, Double.valueOf(169));
		AnchorPane.setLeftAnchor(btnReport3, Double.valueOf(190));
		
		lblReport4 = new Label("Listado de los materiales de consulta " +
				"ordenados por su título ascendentemente y agrupados por su tipo " +
				"(Libro, Revista, Tesis, etc).");
		lblReport4.setPrefWidth(190);
		lblReport4.setPrefHeight(80);
		lblReport4.setWrapText(true);
		btnReport4 = new OpButton("Generar");
		btnReport4.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				ReportManager.getInstance().materialOrderByTitleAndGroupByType();
			}
		});
		AnchorPane.setTopAnchor(lblReport4, Double.valueOf(110));
		AnchorPane.setLeftAnchor(lblReport4, Double.valueOf(310));
		AnchorPane.setTopAnchor(btnReport4, Double.valueOf(169));
		AnchorPane.setLeftAnchor(btnReport4, Double.valueOf(505));
		
		lblReport5 = new Label("Listado de materiales que tienen en común la autoría." +
				" Por favor, seleccione en la lista desplegable el autor del " +
				"que desee ver sus materiales.");
		lblReport5.setPrefWidth(170);
		lblReport5.setPrefHeight(80);
		lblReport5.setWrapText(true);
		menuAuthors = new ChoiceBox<String>();
		menuAuthors.setPrefWidth(170);
		btnReport5 = new OpButton("Generar");
		btnReport5.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				String author = menuAuthors.getSelectionModel().getSelectedItem();
				if(author != null)
					ReportManager.getInstance().materialByAuthor(author);
				else{
					DialogBox diag = new DialogBox("Dabe seleccionar un nombre en la lista");
					diag.show();
				}
			}
		});
		AnchorPane.setTopAnchor(lblReport5, Double.valueOf(205));
		AnchorPane.setLeftAnchor(lblReport5, Double.valueOf(15));
		AnchorPane.setTopAnchor(menuAuthors, Double.valueOf(290));
		AnchorPane.setLeftAnchor(menuAuthors, Double.valueOf(15));
		AnchorPane.setTopAnchor(btnReport5, Double.valueOf(287));
		AnchorPane.setLeftAnchor(btnReport5, Double.valueOf(190));
		
		lblReport6 = new Label("Listado de todas las sugerencias de materiales " +
				"que ha colocado un profesor. Por favor seleccione el nombre del profesor" +
				" en la lista.");
		lblReport6.setPrefWidth(190);
		lblReport6.setPrefHeight(80);
		lblReport6.setWrapText(true);
		menuTeacher = new ChoiceBox<MiniUser>();
		menuTeacher.setPrefWidth(190);
		btnReport6 = new OpButton("Generar");
		btnReport6.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				MiniUser mu = menuTeacher.getSelectionModel().getSelectedItem();
				if(mu != null)
					ReportManager.getInstance().materialSugestedByTeacher(mu.getId());
				else{
					DialogBox diag = new DialogBox("Dabe seleccionar un nombre en la lista");
					diag.show();
				}
			}
		});
		AnchorPane.setTopAnchor(lblReport6, Double.valueOf(205));
		AnchorPane.setLeftAnchor(lblReport6, Double.valueOf(310));
		AnchorPane.setTopAnchor(menuTeacher, Double.valueOf(290));
		AnchorPane.setLeftAnchor(menuTeacher, Double.valueOf(310));
		AnchorPane.setTopAnchor(btnReport6, Double.valueOf(287));
		AnchorPane.setLeftAnchor(btnReport6, Double.valueOf(505));
		
		
		lblReport7 = new Label("Listado de las asignaturas que poseen la mayor " +
				"cantidad de materiales sugeridos. Puede indicar la cantidad de " +
				"resultados que desee obtener.");
		lblReport7.setPrefWidth(170);
		lblReport7.setPrefHeight(80);
		lblReport7.setWrapText(true);
		number1Field = new NumberField();
		number1Field.setPrefWidth(85);
		btnReport7 = new OpButton("Generar");
		btnReport7.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				String valueCad = number1Field.getText();		
				if(valueCad != null){
					int value = 0;
					if(!valueCad.isEmpty() && !Utilities.isEmpty(valueCad))
						value = Integer.valueOf(valueCad);
					if(value == 0)
						value = 100;
					ReportManager.getInstance().subjectWithMoreCountMaterial(value);
				}
				
			}
		});
		AnchorPane.setTopAnchor(lblReport7, Double.valueOf(344));
		AnchorPane.setLeftAnchor(lblReport7, Double.valueOf(15));
		AnchorPane.setTopAnchor(number1Field, Double.valueOf(428));
		AnchorPane.setLeftAnchor(number1Field, Double.valueOf(100));
		AnchorPane.setTopAnchor(btnReport7, Double.valueOf(426));
		AnchorPane.setLeftAnchor(btnReport7, Double.valueOf(190));
		
		
		lblReport8 = new Label("Listado de los profesores de una categoría docente " +
				"específica que tienen una determinada cantidad de materiales sugeridos.");
		lblReport8.setPrefWidth(190);
		lblReport8.setPrefHeight(80);
		lblReport8.setWrapText(true);
		number2Field = new NumberField();
		number2Field.setPrefWidth(85);
		menuTeachingCategory = new ChoiceBox<TeachingCategory>();
		menuTeachingCategory.setPrefWidth(95);
		btnReport8 = new OpButton("Generar");
		btnReport8.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				String valueCad = number1Field.getText();		
				if(valueCad != null){
					int value = 0;
					if(!valueCad.isEmpty() && !Utilities.isEmpty(valueCad))
						value = Integer.valueOf(valueCad);
					if(value == 0)
						value = 100;
					
					TeachingCategory tc = menuTeachingCategory.getSelectionModel().getSelectedItem();
					if(tc != null)
						ReportManager.getInstance().teacherWithTeachingCategoryAndEqualCountOfMaterialAt(tc.getId(), value);
					else{
						DialogBox diag = new DialogBox("Debe seleccionar una Categoría Docente en la lista");
						diag.show();
					}
				}
				
			}
		});
		AnchorPane.setTopAnchor(lblReport8, Double.valueOf(335));
		AnchorPane.setLeftAnchor(lblReport8, Double.valueOf(310));
		AnchorPane.setTopAnchor(menuTeachingCategory, Double.valueOf(428));
		AnchorPane.setLeftAnchor(menuTeachingCategory, Double.valueOf(310));
		AnchorPane.setTopAnchor(number2Field, Double.valueOf(427));
		AnchorPane.setLeftAnchor(number2Field, Double.valueOf(413));
		AnchorPane.setTopAnchor(btnReport8, Double.valueOf(426));
		AnchorPane.setLeftAnchor(btnReport8, Double.valueOf(505));
		
		
		lblReport9 = new Label("Listado de los profesores que han sugerido " +
				"materiales a un asignatura a la que no pertenecen, por favor " +
				"indique esta asignatura.");
		lblReport9.setPrefWidth(170);
		lblReport9.setPrefHeight(80);
		lblReport9.setWrapText(true);
		menuSubject = new ChoiceBox<Subject>();
		menuSubject.setPrefWidth(170);
		btnReport9 = new OpButton("Generar");
		btnReport9.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				Subject sub = menuSubject.getSelectionModel().getSelectedItem();
				if(sub != null)
					ReportManager.getInstance().teacherHasDidSugestedInOtherSubject(sub.getId());
				else{
					DialogBox diag = new DialogBox("Debe seleccionar una Asignatura en la lista");
					diag.show();
				}
			}
		});
		AnchorPane.setTopAnchor(lblReport9, Double.valueOf(485));
		AnchorPane.setLeftAnchor(lblReport9, Double.valueOf(15));
		AnchorPane.setTopAnchor(menuSubject, Double.valueOf(566));
		AnchorPane.setLeftAnchor(menuSubject, Double.valueOf(15));
		AnchorPane.setTopAnchor(btnReport9, Double.valueOf(565));
		AnchorPane.setLeftAnchor(btnReport9, Double.valueOf(190));
		
		lblReport10 = new Label("Guarde en un fichero .txt la información" +
				" de todos los materiales de una temática en particular. Por favor, seleccione " +
				" en la lista la temática deseada.");
		lblReport10.setPrefWidth(190);
		lblReport10.setPrefHeight(80);
		lblReport10.setWrapText(true);
		menuTematic = new ChoiceBox<Tematic>();
		menuTematic.setPrefWidth(190);
		btnReport10 = new OpButton("Guardar");
		btnReport10.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				Tematic tem = menuTematic.getSelectionModel().getSelectedItem();
				if(tem != null)
					try {
						MaterialServices.materialsByTematic(tem.getId(), tem.getName());
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				else{
					DialogBox diag = new DialogBox("Debe seleccionar una Temática en la lista");
					diag.show();
				}
			}
		});
		AnchorPane.setTopAnchor(lblReport10, Double.valueOf(485));
		AnchorPane.setLeftAnchor(lblReport10, Double.valueOf(310));
		AnchorPane.setTopAnchor(menuTematic, Double.valueOf(566));
		AnchorPane.setLeftAnchor(menuTematic, Double.valueOf(310));
		AnchorPane.setTopAnchor(btnReport10, Double.valueOf(565));
		AnchorPane.setLeftAnchor(btnReport10, Double.valueOf(505));
		
		Region spacer = new Region();
		spacer.setPrefHeight(20);
		AnchorPane.setTopAnchor(spacer, Double.valueOf(620));
		AnchorPane.setLeftAnchor(spacer, Double.valueOf(0));
		AnchorPane.setBottomAnchor(spacer, Double.valueOf(0));
		AnchorPane.setRightAnchor(spacer, Double.valueOf(0));
		
		centerRoot.getChildren().addAll(
				lblReport1, btnReport1,
				lblReport2, btnReport2,
				lblReport3, btnReport3,
				lblReport4, btnReport4,
				lblReport5, btnReport5, menuAuthors,
				lblReport6, btnReport6, menuTeacher,
				lblReport7, btnReport7, number1Field,
				lblReport8, btnReport8, menuTeachingCategory, number2Field,
				lblReport9, btnReport9, menuSubject,
				lblReport10, btnReport10, menuTematic,
				spacer
				);
		
		
		ScrollPane scroll = new ScrollPane();
		scroll.setId("scrollPane");
		scroll.setContent(centerRoot);
		setCenter(scroll);
	}
	
	public void setData(){
		try {
			User currentUser = GUI.getInstance().getUser();			
			
			List<String> namesAuthor = null;
			if(currentUser != null && currentUser.getPrivilege().isModifyUser())
				namesAuthor = MaterialServices.getAuthorsOfMaterials();
			else
				namesAuthor = MaterialServices.getAuthorsOfMaterialsEnabled();
			menuAuthors.getItems().removeAll(menuAuthors.getItems());
			menuAuthors.getItems().addAll(namesAuthor);
			
			
			List<MiniUser> namesTeacher = null;
			if(currentUser != null && currentUser.getPrivilege().isModifyUser())
				namesTeacher = UserServices.getTeacherNames();
			else
				namesTeacher = UserServices.getEnabledTeacherNames();
			menuTeacher.getItems().removeAll(menuTeacher.getItems());
			menuTeacher.getItems().addAll(namesTeacher);
			
			
			List<TeachingCategory> namesTeachingCategory = null;
			if(currentUser != null && currentUser.getPrivilege().isModifyTeachingCategory())
				namesTeachingCategory = TeachingCategoryServices.getTeachingCategory();
			else
				namesTeachingCategory = TeachingCategoryServices.getEnabledTeachingCategory();
			menuTeachingCategory.getItems().removeAll(menuTeachingCategory.getItems());
			menuTeachingCategory.getItems().addAll(namesTeachingCategory);
			
			List<Subject> namesSubject = null;
			if(currentUser != null && currentUser.getPrivilege().isModifySubject())
				namesSubject = SubjectServices.getSubject();
			else
				namesSubject = SubjectServices.getEnabledSubject();
			menuSubject.getItems().removeAll(menuSubject.getItems());
			menuSubject.getItems().addAll(namesSubject);
			
			List<Tematic> namesTematic = null;
			if(currentUser != null && currentUser.getPrivilege().isModifyTematic())
				namesTematic = TematicServices.getTematics();				
			else
				namesTematic = TematicServices.getEnabledTematics();
			menuTematic.getItems().removeAll(menuTematic.getItems());
			menuTematic.getItems().addAll(namesTematic);
			
		} catch (SQLException e) {
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
}
