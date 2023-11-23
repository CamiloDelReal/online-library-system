package visual.controls.center.userCountPage;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import services.ResearchGroupServices;
import services.ScientificCategoryServices;
import services.TeachingCategoryServices;
import services.TeachingGroupServices;
import utils.DialogBox;
import visual.Main;
import visual.controls.Notification;


public class DialogYesNoToDeleteAcademicData extends VBox{
	private StackPane dialogRoot;
	private String text;
	private static Image picture = new Image(DialogBox.class.getResource("../visual/icons/alert.png").toExternalForm());
	private TypeOfInsertModifyDelete type;
	private int id;

	public DialogYesNoToDeleteAcademicData(String text, TypeOfInsertModifyDelete type, int id, final String name){
		this.text = text;
		dialogRoot = new StackPane();
		dialogRoot.setId("dialogRoot");

		this.type = type;
		this.id = id;

		setId("dialogo");
		setMinSize(280, 110);
		setPrefSize(280, 110);
		setMaxSize(280, 110);

		init(name);
	}

	private void init(final String name){
		HBox content = new HBox();

		if(picture != null){
			ImageView view = new ImageView(picture);
			content.getChildren().add(view);
		}

		Label tag = new Label(text);
		tag.setWrapText(true);

		VBox textBox = new VBox();
		textBox.setTranslateX(8);
		Region spacer1 = new Region();
		spacer1.setPrefHeight(15);
		Region spacer2 = new Region();
		VBox.setVgrow(spacer2, Priority.ALWAYS);

		textBox.getChildren().addAll(spacer1, tag, spacer2);

		content.getChildren().add(textBox);

		Region separation = new Region();
		VBox.setVgrow(separation, Priority.ALWAYS);

		HBox regButton = new HBox();

		Region spacer4 = new Region();
		HBox.setHgrow(spacer4, Priority.ALWAYS);

		Button accept = new Button("Aceptar");
		accept.setMinSize(80, 20);
		accept.setPrefSize(80, 20);
		accept.setMaxSize(80, 20);
		accept.setOnAction(new EventHandler<ActionEvent>(){
			@SuppressWarnings("incomplete-switch")
			public void handle(ActionEvent e){
				try {
					String text = "Se ha cancelado satisfactoriamente ";
					switch(type){
					case TEACHING_GROUP:
						TeachingGroupServices.deleteTeachingGroup(id);
						text += "el grupo docente " + name;
						break;
					case RESEARCH_GROUP:
						ResearchGroupServices.deleteResearchGroup(id);
						text += "el grupo investigativo " + name;
						break;
					case SCIENTIFIC_CATEGORY:
						ScientificCategoryServices.deleteScientificCategory(id);
						text += "la categoría científica " + name;
						break;
					case TEACHING_CATEGORY:
						TeachingCategoryServices.deleteTeachingCategory(id);
						text += "la categoría docente " + name;
						break;

					}
					Notification.getInstance().setText(text);
					Notification.getInstance().showNotification();
				} catch (SQLException e1) {
					DialogBox diag = new DialogBox(e1.getMessage());
					diag.show();
				}

				Main.getStackRoot().getChildren().remove(dialogRoot);
				AcademicDataPane.getInstance().update(type);
			}
		});

		Region spacer5 = new Region();
		spacer5.setMinWidth(12);
		spacer5.setPrefWidth(12);
		spacer5.setMaxWidth(12);

		Button close = new Button("Cancelar");
		close.setDefaultButton(true);
		close.setMinSize(80, 20);
		close.setPrefSize(80, 20);
		close.setMaxSize(80, 20);
		close.setTextAlignment(TextAlignment.CENTER);
		close.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				dialogRoot.getChildren().remove(DialogYesNoToDeleteAcademicData.this);
				if(dialogRoot.getChildren().isEmpty())
					dialogRoot.setVisible(false);
			}
		}); 

		regButton.getChildren().addAll(spacer4,accept, spacer5, close);

		getChildren().addAll(content, separation, regButton);

		dialogRoot.getChildren().add(DialogYesNoToDeleteAcademicData.this);
	}

	public void show(){
		Main.getStackRoot().getChildren().add(dialogRoot);
	}

	public void setSize(double width, double height){
		setMinSize(width, height);
		setPrefSize(width, height);
		setMaxSize(width, height);
	}

}