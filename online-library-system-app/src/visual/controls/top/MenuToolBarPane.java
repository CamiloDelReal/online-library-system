package visual.controls.top;

import java.io.IOException;
import java.sql.SQLException;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.util.Duration;
import utils.DialogBox;
import visual.Main;
import visual.controls.center.CenterViewerMain;
import visual.controls.center.materialPage.MaterialViewer;
import visual.controls.center.notesPane.NotesViewer;
import visual.controls.center.reportPane.ReportViewer;
import visual.controls.center.userCountPage.CurrentUserPane;
import visual.controls.center.userCountPage.UsersCountViewer;

public class MenuToolBarPane extends HBox {
	private static MenuToolBarPane menuToolbarPane = null;
	
	private static double DURATION_GLUR_TO_NORMAL = 300;
	private static double DURATION_NORMAL_TO_GLUR = 300;
	
	private Tag tag;
	
	private ToolButton btnMaterials;
	private ToolButton btnReports;
	private ToolButton btnNotes;
	private ToolButton btnUsersCount;
	private ToolButton btnTools;
	private HBox buttons;
	
	private OwnerToolTip tips;
	private boolean showingTooltips = false;
	
	private static double speed = 100;
	
	private double difference = (OwnerToolTip.PREF_WIDTH - ToolButton.PREF_WIDTH) / 2;
	
	double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
	
	public static MenuToolBarPane getInstance(){
		if(menuToolbarPane == null)
			menuToolbarPane = new MenuToolBarPane();
		
		return menuToolbarPane;
	}
	
	private MenuToolBarPane(){
		
		setId("menuToolBar");
		
		setMinHeight(100);
		setPrefHeight(100);
		setMaxHeight(100);
		
		Region spacer1 = new Region();
		spacer1.setMinWidth(10);
		Region spacer2 = new Region();
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		Region spacer3 = new Region();
		HBox.setHgrow(spacer3, Priority.ALWAYS);
		
		tag = new Tag();
		
		btnMaterials = new ToolButton();
		btnMaterials.setId("btnMaterials");
		final Timeline btnMaterialsToNormal = new Timeline();
		btnMaterialsToNormal.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(btnMaterials.setOpacityProperty(), 0.7)),
				new KeyFrame(Duration.millis(DURATION_GLUR_TO_NORMAL), new KeyValue(btnMaterials.setOpacityProperty(), 1.0))
				);
		final Timeline btnMaterialsToGlur = new Timeline();
		btnMaterialsToGlur.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(btnMaterials.setOpacityProperty(), 1.0)),
				new KeyFrame(Duration.millis(DURATION_NORMAL_TO_GLUR), new KeyValue(btnMaterials.setOpacityProperty(), 0.7))
				);
		btnMaterials.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				btnMaterialsToNormal.play();
				
				tips.setTitle("Materiales");
				tips.setText("Hay que redactarlo. Hay que redactarlo. Hay que redactarlo. Hay que redactarlo. Hay que redactarlo");
				
				if(!showingTooltips){
					showToolTips(e);
					showingTooltips = true;
				}
				else{					
					double posInButtonX = e.getX();
					double posInScreenX = e.getScreenX();
					
					Timeline moveTo = new Timeline();
					moveTo.getKeyFrames().addAll(
							new KeyFrame(Duration.ZERO, new KeyValue(tips.getPositionX(), tips.getX())),
							new KeyFrame(Duration.millis(speed), new KeyValue(tips.getPositionX(), posInScreenX - posInButtonX - difference))
							);
					moveTo.play();
				}
			}
		});
		btnMaterials.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				btnMaterialsToGlur.play();
			}			
		});
		btnMaterials.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				showingTooltips = false;
				CenterViewerMain.getInstance().put(MaterialViewer.getInstance());
				try {
					MaterialViewer.getInstance().setData();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//System.out.println(MaterialViewer.getInstance().getWidth() + " - " + MaterialViewer.getInstance().getHeight());
			}
		});
		
		btnReports = new ToolButton();
		btnReports.setId("btnReports");
		final Timeline btnReportsToNormal = new Timeline();
		btnReportsToNormal.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(btnReports.setOpacityProperty(), 0.7)),
				new KeyFrame(Duration.millis(DURATION_GLUR_TO_NORMAL), new KeyValue(btnReports.setOpacityProperty(), 1.0))
				);
		final Timeline btnReportsToGlur = new Timeline();
		btnReportsToGlur.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(btnReports.setOpacityProperty(), 1.0)),
				new KeyFrame(Duration.millis(DURATION_NORMAL_TO_GLUR), new KeyValue(btnReports.setOpacityProperty(), 0.7))
				);
		btnReports.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				btnReportsToNormal.play();
				
				tips.setTitle("Reportes");
				tips.setText("Hay que redactarlo. Hay que redactarlo. Hay que redactarlo. Hay que redactarlo. Hay que redactarlo");
				
				if(!showingTooltips){
					showToolTips(e);
					showingTooltips = true;
				}
				else{					
					double posInButtonX = e.getX();
					double posInScreenX = e.getScreenX();
					
					Timeline moveTo = new Timeline();
					moveTo.getKeyFrames().addAll(
							new KeyFrame(Duration.ZERO, new KeyValue(tips.getPositionX(), tips.getX())),
							new KeyFrame(Duration.millis(speed), new KeyValue(tips.getPositionX(), posInScreenX - posInButtonX - difference))
							);
					moveTo.play();
				}
			}
		});
		btnReports.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				btnReportsToGlur.play();
			}
		});
		btnReports.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				showingTooltips = false;
				CenterViewerMain.getInstance().put(ReportViewer.getInstance());
				ReportViewer.getInstance().setData();
			}
		});
		
		btnNotes = new ToolButton();
		btnNotes.setId("btnNotes");
		final Timeline btnNotesToNormal = new Timeline();
		btnNotesToNormal.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(btnNotes.setOpacityProperty(), 0.7)),
				new KeyFrame(Duration.millis(DURATION_GLUR_TO_NORMAL), new KeyValue(btnNotes.setOpacityProperty(), 1.0))
				);
		final Timeline btnNotesToGlur = new Timeline();
		btnNotesToGlur.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(btnNotes.setOpacityProperty(), 1.0)),
				new KeyFrame(Duration.millis(DURATION_NORMAL_TO_GLUR), new KeyValue(btnNotes.setOpacityProperty(), 0.7))
				);
		btnNotes.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				btnNotesToNormal.play();
				
				tips.setTitle("Notas");
				tips.setText("Hay que redactarlo. Hay que redactarlo. Hay que redactarlo. Hay que redactarlo. Hay que redactarlo");
				
				if(!showingTooltips){
					showToolTips(e);
					showingTooltips = true;
				}
				else{					
					double posInButtonX = e.getX();
					double posInScreenX = e.getScreenX();
					
					Timeline moveTo = new Timeline();
					moveTo.getKeyFrames().addAll(
							new KeyFrame(Duration.ZERO, new KeyValue(tips.getPositionX(), tips.getX())),
							new KeyFrame(Duration.millis(speed), new KeyValue(tips.getPositionX(), posInScreenX - posInButtonX - difference))
							);
					moveTo.play();
				}
			}
		});
		btnNotes.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				btnNotesToGlur.play();
			}
		});
		btnNotes.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				showingTooltips = false;
				CenterViewerMain.getInstance().put(NotesViewer.getInstance());
			}
		});
		
		btnUsersCount = new ToolButton();
		btnUsersCount.setId("btnUsersCount");
		final Timeline btnUsersCountToNormal = new Timeline();
		btnUsersCountToNormal.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(btnUsersCount.setOpacityProperty(), 0.7)),
				new KeyFrame(Duration.millis(DURATION_GLUR_TO_NORMAL), new KeyValue(btnUsersCount.setOpacityProperty(), 1.0))
				);
		final Timeline btnUsersCountToGlur = new Timeline();
		btnUsersCountToGlur.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(btnUsersCount.setOpacityProperty(), 1.0)),
				new KeyFrame(Duration.millis(DURATION_NORMAL_TO_GLUR), new KeyValue(btnUsersCount.setOpacityProperty(), 0.7))
				);
		btnUsersCount.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				btnUsersCountToNormal.play();
				
				tips.setTitle("Cuentas de usuarios");
				tips.setText("Hay que redactarlo. Hay que redactarlo. Hay que redactarlo. Hay que redactarlo. Hay que redactarlo");
				
				if(!showingTooltips){
					showToolTips(e);
					showingTooltips = true;
				}
				else{					
					double posInButtonX = e.getX();
					double posInScreenX = e.getScreenX();
					
					Timeline moveTo = new Timeline();
					moveTo.getKeyFrames().addAll(
							new KeyFrame(Duration.ZERO, new KeyValue(tips.getPositionX(), tips.getX())),
							new KeyFrame(Duration.millis(speed), new KeyValue(tips.getPositionX(), posInScreenX - posInButtonX - difference))
							);
					moveTo.play();
				}
			}
		});
		btnUsersCount.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				btnUsersCountToGlur.play();
			}
		});
		btnUsersCount.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				showingTooltips = false;
				try {
					UsersCountViewer.getInstance().setCurrentUserSelected();
				} catch (IOException e1) {
					DialogBox diag = new DialogBox("No se pudo obtener su avatar");
					diag.show();
					CurrentUserPane.getInstance().setDefaultAvatar();
				}
				CenterViewerMain.getInstance().put(UsersCountViewer.getInstance());				
			}
		});
		
		btnTools = new ToolButton();
		btnTools.setId("btnTools");
		final Timeline btnToolsToNormal = new Timeline();
		btnToolsToNormal.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(btnTools.setOpacityProperty(), 0.7)),
				new KeyFrame(Duration.millis(DURATION_GLUR_TO_NORMAL), new KeyValue(btnTools.setOpacityProperty(), 1.0))
				);
		final Timeline btnToolsToGlur = new Timeline();
		btnToolsToGlur.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(btnTools.setOpacityProperty(), 1.0)),
				new KeyFrame(Duration.millis(DURATION_NORMAL_TO_GLUR), new KeyValue(btnTools.setOpacityProperty(), 0.7))
				);
		btnTools.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				btnToolsToNormal.play();
				tips.setTitle("Configuración");
				tips.setText("Hay que redactarlo. Hay que redactarlo. Hay que redactarlo. Hay que redactarlo. Hay que redactarlo");
				
				if(!showingTooltips){
					showToolTips(e);
					showingTooltips = true;
				}
				else{					
					double posInButtonX = e.getX();
					double posInScreenX = e.getScreenX();
					
					Timeline moveTo = new Timeline();
					moveTo.getKeyFrames().addAll(
							new KeyFrame(Duration.ZERO, new KeyValue(tips.getPositionX(), tips.getX())),
							new KeyFrame(Duration.millis(speed), new KeyValue(tips.getPositionX(), posInScreenX - posInButtonX - difference))
							);
					moveTo.play();
				}
			}
		});
		btnTools.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				btnToolsToGlur.play();
			}
		});
		btnTools.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				showingTooltips = false;
			}
		});
		
		buttons = new HBox();
		buttons.setSpacing(10);
		buttons.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){				
				tips.hide();
				showingTooltips = false;
			}
		});
		buttons.getChildren().addAll(btnMaterials, btnReports);
		
		tips = new OwnerToolTip("Nada", "Nada");
		tips.setAutoHide(true);		
		
		
		getChildren().addAll(spacer1, tag, spacer2, buttons, spacer3);		
	}
	
	private void showToolTips(MouseEvent e){
		double posInButtonX = e.getX();
		double posInButtonY = e.getY();
		
		double posInScreenX = e.getScreenX();
		double posInScreenY = e.getScreenY();
		
		difference = (OwnerToolTip.PREF_WIDTH - ToolButton.PREF_WIDTH) / 2;
		
		posInScreenX -= posInButtonX + difference - 10;
		posInScreenY -= posInButtonY - 85;
		
		tips.show(Main.getScene().getWindow(), posInScreenX, posInScreenY);
	}
	
	public void setDefaultData(){
		hideAll();
		buttons.getChildren().addAll(btnMaterials, btnReports);
		tag.setDefaultData();
	}
	
	public void setData() throws IOException{		
		tag.setData();			
	}
	
	public void showAll(){ buttons.getChildren().addAll(btnMaterials, btnReports, btnNotes, btnUsersCount, btnTools); };
	
	public void showBtnMaterials(){ 
		if(!buttons.getChildren().contains(btnMaterials))
			buttons.getChildren().add(btnMaterials);
	}
	public void showBtnReports(){ 
		if(!buttons.getChildren().contains(btnReports))
			buttons.getChildren().add(btnReports);
	}
	public void showBtnNotes(){ 
		if(!buttons.getChildren().contains(btnNotes))
			buttons.getChildren().add(btnNotes);
	}
	public void showBtnUsersCount(){
		if(!buttons.getChildren().contains(btnUsersCount))
			buttons.getChildren().add(btnUsersCount);
	}
	public void showBtnTools(){
		if(!buttons.getChildren().contains(btnTools))
			buttons.getChildren().add(btnTools);
	}
	
	public void hideAll(){ buttons.getChildren().removeAll(buttons.getChildren()); }
	
	public void hideBtnMaterials() { buttons.getChildren().remove(btnMaterials); }
	public void hideBtnReports() { buttons.getChildren().remove(btnReports); }
	public void hideBtnNotes() { buttons.getChildren().remove(btnNotes); }
	public void hideBtnUsersCount() { buttons.getChildren().remove(btnUsersCount); }
	public void hideBtnTools() { buttons.getChildren().remove(btnTools); }
	
}