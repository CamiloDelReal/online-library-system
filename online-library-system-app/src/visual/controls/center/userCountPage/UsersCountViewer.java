package visual.controls.center.userCountPage;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import javafx.scene.text.TextAlignment;
import utils.DialogBox;

public class UsersCountViewer extends BorderPane {
	private static UsersCountViewer viewer;
	
	private TabButton btnCurrentUser;
	private TabButton btnPrivilege;
	private TabButton btnOthersUsers;
	private TabButton btnAcademicData;
	private TabButton currentSelected;
	
	private AnchorPane toolbar;
	
	private Image unknown;
	private Image multipleUsers;
	private Image privilege;
	private Image task;
	
	private CurrentUserPane currentUserPane;
	
	private PrivilegeUserPane privilegeUserPane;
	
	private OtherUsersPane otherUsersPane;
	
	private AcademicDataPane academicDataPane;
	
	private StackPane center;
	
	private UsersCountViewer(){
		setId("usersCountViewer");
		
		unknown = new Image(getClass().getResource("../../../icons/unknow65x66Colored.png").toExternalForm());
		multipleUsers = new Image(getClass().getResource("../../../icons/people57x62.png").toExternalForm());
		privilege = new Image(getClass().getResource("../../../icons/keys57x57.png").toExternalForm());
		task = new Image(getClass().getResource("../../../icons/academicData57x62.png").toExternalForm());
	
		
		btnCurrentUser = new TabButton(unknown, "Usuario\nactual");
		btnCurrentUser.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				currentSelected.setDeselected();
				currentSelected = btnCurrentUser;
				btnCurrentUser.setSelected();
				center.getChildren().removeAll(center.getChildren());
				//currentUserPane.setData();
				center.getChildren().add(currentUserPane);
				currentUserPane.setDefaultButtons();
			}
		});
		
		btnPrivilege = new TabButton(privilege, "Privilegios");
		btnPrivilege.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				currentSelected.setDeselected();
				currentSelected = btnPrivilege;
				btnPrivilege.setSelected();
				center.getChildren().removeAll(center.getChildren());
				center.getChildren().addAll(privilegeUserPane);
			}
		});
		
		
		btnOthersUsers = new TabButton(multipleUsers, "Otros\nusuarios");
		btnOthersUsers.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
					currentSelected.setDeselected();
					currentSelected = btnOthersUsers;
					btnOthersUsers.setSelected();
					center.getChildren().removeAll(center.getChildren());
					center.getChildren().addAll(otherUsersPane);
			}
		});
		
		btnAcademicData = new TabButton(task, "Datos\nacadémicos");
		btnAcademicData.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
					currentSelected.setDeselected();
					currentSelected = btnAcademicData;
					btnAcademicData.setSelected();
					center.getChildren().removeAll(center.getChildren());
					center.getChildren().addAll(academicDataPane);
					/*StackPane.setAlignment(otherUsersButtons, Pos.BOTTOM_RIGHT);
					center.getChildren().add(otherUsersButtons);*/
			}
		});
		
		ToggleGroup toggleGroup = new ToggleGroup();
		btnCurrentUser.setToggleGroup(toggleGroup);
		btnPrivilege.setToggleGroup(toggleGroup);
		btnOthersUsers.setToggleGroup(toggleGroup);
		btnAcademicData.setToggleGroup(toggleGroup);
		
		Region spacer = new Region();
		spacer.setMinWidth(80);
		spacer.setPrefWidth(80);
		spacer.setMaxWidth(80);
		spacer.setId("spacerInToolbar");
		
		toolbar = new AnchorPane();
		toolbar.setId("toolbar");
		toolbar.getChildren().addAll(btnCurrentUser, btnPrivilege, btnOthersUsers, btnAcademicData, spacer);
		AnchorPane.setTopAnchor(btnCurrentUser, Double.valueOf(0));
		AnchorPane.setTopAnchor(btnPrivilege, Double.valueOf(100));
		AnchorPane.setTopAnchor(btnOthersUsers, Double.valueOf(200));
		AnchorPane.setTopAnchor(btnAcademicData, Double.valueOf(300));
		AnchorPane.setTopAnchor(spacer, Double.valueOf(400));
		AnchorPane.setBottomAnchor(spacer, Double.valueOf(0));
		
		setLeft(toolbar);
		
		currentUserPane = CurrentUserPane.getInstance();
		privilegeUserPane = PrivilegeUserPane.getInstance();
		otherUsersPane = OtherUsersPane.getInstance();
		academicDataPane = AcademicDataPane.getInstance();
		
		center = new StackPane();
		
		setCenter(center);
		
	}
	
	public static UsersCountViewer getInstance(){
		if(viewer == null)
			viewer = new UsersCountViewer();
		return viewer;
	}
	
	public void setCurrentUserSelected() throws IOException{
		btnCurrentUser.setSelected();
		btnPrivilege.setDeselected();
		btnOthersUsers.setDeselected();
		btnAcademicData.setDeselected();
		
		center.getChildren().removeAll(center.getChildren());
		currentUserPane.setData();
		center.getChildren().add(currentUserPane);
		currentUserPane.setDefaultButtons();
		
		currentSelected = btnCurrentUser;
	}
	
	public void setData(){
		try {
			currentUserPane.setData();
		} catch (IOException e) {
			DialogBox diag = new DialogBox("No se pudo obtener su avatar");
			diag.show();
			CurrentUserPane.getInstance().setDefaultAvatar();
		}
		otherUsersPane.setData();
		academicDataPane.setData();
		PrivilegeUserPane.getInstance().adjust();
	}
	
	public void setDefault(){
		currentUserPane.setDefault();
	}
}

class TabButton extends ToggleButton {
	private Path backgroundPath;
	private boolean isSelected = false;
	private ColorAdjust adjustment;
	
	public TabButton(Image picture, String text){
		setId("tabButton");
		
		ImageView pictureView = new ImageView(picture);
		backgroundPath = new Path();
		backgroundPath.getElements().addAll(
				new MoveTo(0, 0),
				new HLineTo(80),
				new VLineTo(42),
				new LineTo(69, 50),
				new LineTo(80, 58),
				new VLineTo(100),
				new HLineTo(0),
				new VLineTo(0),
				new ClosePath()
				);
		backgroundPath.setFill(Color.WHITE);
		
		adjustment = new ColorAdjust();
		pictureView.setEffect(adjustment);
		adjustment.setSaturation(-1);
		
		setGraphic(pictureView);
		setText(text);
		setWrapText(true);
		setTextAlignment(TextAlignment.CENTER);
		setContentDisplay(ContentDisplay.TOP);
		
		
		setMinSize(80, 100);
		setPrefSize(80, 100);
		setMaxSize(80, 100);
		
		setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				if(!isSelected){
					isSelected = true;
					setClip(backgroundPath);
					adjustment.setSaturation(0);
				}
				else
					setSelected(true);
			}
		});
		
	}
	
	public boolean getIsSelected(){
		return isSelected;
	}
	
	public void setSelected(){
		setSelected(true);
		isSelected = true;
		setClip(backgroundPath);
		adjustment.setSaturation(0);
	}
	public void setDeselected(){
		setSelected(false);
		isSelected = false;
		setClip(null);
		adjustment.setSaturation(-1);
	}

}
