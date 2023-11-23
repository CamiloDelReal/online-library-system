package visual;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.DepthTest;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import services.UserServices;
import utils.Connection;
import utils.GUI;
import visual.controls.center.CenterViewerMain;
import visual.controls.center.materialPage.MaterialViewer;
import visual.controls.right.NotesNewsAndMoreVisited;
import visual.controls.top.MenuToolBarPane;
import visual.controls.top.TitledPane;

public class Main extends Application {
	private static StackPane stackRoot;
	private static Scene primaryScene;
	
	@Override
	public void start(Stage primaryStage) {
		//GUI.getInstance(primaryStage);
		
		stackRoot = new StackPane();
		stackRoot.setDepthTest(DepthTest.DISABLE);
		
		BorderPane root = new BorderPane();
		root.setId("root");
		
		
		//Adding the top
		VBox top = new VBox();		
		top.getChildren().addAll(TitledPane.getInstance(), MenuToolBarPane.getInstance());
		
		root.setTop(top);
		//---Ending top
		
		//Adding the right
		HBox right = new HBox();
		
		Region spacer1 = new Region();
		spacer1.setMinWidth(25);
		spacer1.setPrefWidth(25);
		spacer1.setMaxWidth(25);
		
		Region spacer2 = new Region();
		spacer2.setMinWidth(25);
		spacer2.setPrefWidth(25);
		spacer2.setMaxWidth(25);
		
		right.getChildren().addAll(spacer1, NotesNewsAndMoreVisited.getInstance(), spacer2);
		
		root.setRight(right);
		//---Ending the right
		
		//---Adding the center		
		Region spacer = new Region();
		spacer.setMinWidth(25);
		spacer.setPrefWidth(25);
		spacer.setMaxWidth(25);
		
		BorderPane center = new BorderPane();
		center.setRight(spacer);
		center.setCenter(CenterViewerMain.getInstance());
		
		root.setCenter(center);
		
		//---Ending the center
		
		//---Adding the bottom
		Region bottom = new Region();
		bottom.setMinHeight(30);
		bottom.setPrefHeight(30);
		bottom.setMaxHeight(30);
		
		root.setBottom(bottom);
		//---Endding the bottom
		
		stackRoot.getChildren().add(root);
		
		primaryScene = new Scene(stackRoot, 850, 660);
		primaryScene.getStylesheets().addAll(getClass().getResource("css/Main_Style.css").toExternalForm(),
									  getClass().getResource("css/TitledPane_Style.css").toExternalForm(),
									  getClass().getResource("css/AuthenticationDialog_Style.css").toExternalForm(),
									  getClass().getResource("css/MenuToolBarPane_Style.css").toExternalForm(),
									  getClass().getResource("css/MaterialViewer_Style.css").toExternalForm(),
									  getClass().getResource("css/NotesNewsAndMoreVisited_Style.css").toExternalForm(),
									  getClass().getResource("css/UsersCountViewer_Style.css").toExternalForm(),
									  getClass().getResource("css/UserDialogBox_Style.css").toExternalForm(),
									  getClass().getResource("css/DataDialog_Style.css").toExternalForm(),
									  getClass().getResource("css/NotesViewer_Style.css").toExternalForm(),
									  getClass().getResource("css/ReportViewer_Style.css").toExternalForm()
									  );
		
		primaryStage.setScene(primaryScene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Libros en la red v24052012");
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
			public void handle(WindowEvent event) {
				try{
					if(GUI.getInstance().haveUser()){
						UserServices.LogoutUser(GUI.getInstance().getUser().getId());
					}
					Connection.getConnection().close();
				} catch(SQLException e){
					
				}
			}			
		});
		primaryStage.show();
		CenterViewerMain.getInstance().put(MaterialViewer.getInstance());
		try {
			MaterialViewer.getInstance().setData();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static StackPane getStackRoot(){
		return stackRoot;
	}
	
	public static Scene getScene(){
		return primaryScene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
