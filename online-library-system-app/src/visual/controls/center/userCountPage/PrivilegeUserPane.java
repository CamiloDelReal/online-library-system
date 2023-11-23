package visual.controls.center.userCountPage;

import utils.GUI;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import models.Privilege;

public class PrivilegeUserPane extends BorderPane{
	private static PrivilegeUserPane pane = null;
	
	private Label title;
	
	private Label lblHeaderUser;
	
	private Label lblModifyOwnUser;
	private Label lblDeleteOwnUser;
	
	private Label lblCreateOtherUser;
	private Label lblModifyOtherUser;
	private Label lblDeleteOtherUser;
	
	private Label lblPrivilegeEditing;
	
	private Label lblCreatePublicNotes;
	
	private Label lblCreateTeachingGroup;
	private Label lblModifyTeachingGroup;
	private Label lblDeleteTeachingGroup;
	
	private Label lblCreateResearchGroup;
	private Label lblModifyResearchGroup;
	private Label lblDeleteResearchGroup;
	
	private Label lblCreateTeachingCategory;
	private Label lblModifyTeachingCategory;
	private Label lblDeleteTeachingCategory;
	
	private Label lblCreateScientificCategory;
	private Label lblModifyScientificCategory;
	private Label lblDeleteScientificCategory;
	
	private Label lblHeaderMaterial;
	
	private Label lblCreateMaterial;
	private Label lblModifyMaterial;
	private Label lblDeleteMaterial;
	
	private Label lblCreateTematic;
	private Label lblModifyTematic;
	private Label lblDeleteTematic;
	
	private Label lblCreateSubject;
	private Label lblModifySubject;
	private Label lblDeleteSubject;
	
	private Label lblCreateLanguage;
	private Label lblModifyLanguage;
	private Label lblDeleteLanguage;

	public static PrivilegeUserPane getInstance(){
		if(pane == null)
			pane = new PrivilegeUserPane();
		return pane;
	}
	
	private PrivilegeUserPane() {
		setId("privilegeUserPane");
		
		title = new Label("Privilegio: ?");
		title.getStyleClass().add("title");
		
		lblHeaderUser = new Label("Gestión de usuarios");
		lblHeaderUser.getStyleClass().add("header");
		
		lblModifyOwnUser = new Label("Modificar");
		Label sl1 = new Label("/");
		lblDeleteOwnUser = new Label("Eliminar");
		Label lblEnddingUser = new Label("su usuario");
		lblEnddingUser.getStyleClass().add("endding");
		HBox userGroup = new HBox();
		userGroup.getChildren().addAll(lblModifyOwnUser, sl1, lblDeleteOwnUser, lblEnddingUser);
		
		lblCreateOtherUser = new Label("Crear");
		Label sl2 = new Label("/");
		lblModifyOtherUser = new Label("Modificar");
		Label sl3 = new Label("/");
		lblDeleteOtherUser = new Label("Eliminar");
		Label lblEnddingOtherUser = new Label("otros usuarios");
		lblEnddingOtherUser.getStyleClass().add("endding");
		HBox otherUserGroup = new HBox();
		otherUserGroup.getChildren().addAll(lblCreateOtherUser, sl2, lblModifyOtherUser, sl3, lblDeleteOtherUser, lblEnddingOtherUser);
		
		lblPrivilegeEditing = new Label("Editar");
		Label lblEnddingPrivilege = new Label("privilegios");
		lblEnddingPrivilege.getStyleClass().add("endding");
		HBox privilegeGroup = new HBox();
		privilegeGroup.getChildren().addAll(lblPrivilegeEditing, lblEnddingPrivilege);
		
		lblCreatePublicNotes = new Label("Crear");
		Label lblEnddingNotes = new Label("notas públicas");
		lblEnddingNotes.getStyleClass().add("endding");
		HBox notesGroup = new HBox();
		notesGroup.getChildren().addAll(lblCreatePublicNotes, lblEnddingNotes);
		
		lblCreateTeachingGroup = new Label("Crear");
		Label sl4 = new Label("/");
		lblModifyTeachingGroup = new Label("Modificar");
		Label sl5 = new Label("/");
		lblDeleteTeachingGroup = new Label("Eliminar");
		Label lblEnddingTG = new Label("grupos docentes");
		lblEnddingTG.getStyleClass().add("endding");
		HBox tgGroup = new HBox();
		tgGroup.getChildren().addAll(lblCreateTeachingGroup, sl4, lblModifyTeachingGroup, sl5, lblDeleteTeachingGroup, lblEnddingTG);
		
		lblCreateResearchGroup = new Label("Crear");
		Label sl6 = new Label("/");
		lblModifyResearchGroup = new Label("Modificar");
		Label sl7 = new Label("/");
		lblDeleteResearchGroup = new Label("Eliminar");
		Label lblEnddingRG = new Label("grupos investigativos");
		lblEnddingRG.getStyleClass().add("endding");
		HBox rgGroup = new HBox();
		rgGroup.getChildren().addAll(lblCreateResearchGroup, sl6, lblModifyResearchGroup, sl7, lblDeleteResearchGroup, lblEnddingRG);
		
		lblCreateTeachingCategory = new Label("Crear");
		Label sl8 = new Label("/");
		lblModifyTeachingCategory = new Label("Modificar");
		Label sl9 = new Label("/");
		lblDeleteTeachingCategory = new Label("Eliminar");
		Label lblEnddingTC = new Label("categorías docentes");
		lblEnddingTC.getStyleClass().add("endding");
		HBox tcGroup = new HBox();
		tcGroup.getChildren().addAll(lblCreateTeachingCategory, sl8, lblModifyTeachingCategory, sl9, lblDeleteTeachingCategory, lblEnddingTC);
		
		lblCreateScientificCategory = new Label("Crear");
		Label sl10 = new Label("/");
		lblModifyScientificCategory = new Label("Modificar");
		Label sl11 = new Label("/");
		lblDeleteScientificCategory = new Label("Eliminar");
		Label lblEnddingSC = new Label("categorías científicas");
		lblEnddingSC.getStyleClass().add("endding");
		HBox scGroup = new HBox();
		scGroup.getChildren().addAll(lblCreateScientificCategory, sl10, lblModifyScientificCategory, sl11, lblDeleteScientificCategory, lblEnddingSC);
		
		lblHeaderMaterial = new Label("Gestión de materiales");
		lblHeaderMaterial.getStyleClass().add("header");
		
		lblCreateMaterial = new Label("Añadir");
		Label sl12 = new Label("/");
		lblModifyMaterial = new Label("Modificar");
		Label sl13 = new Label("/");
		lblDeleteMaterial = new Label("Eliminar");
		Label lblEnddingMaterial = new Label("materiales");
		lblEnddingMaterial.getStyleClass().add("endding");
		HBox materialGroup = new HBox();
		materialGroup.getChildren().addAll(lblCreateMaterial, sl12, lblModifyMaterial, sl13, lblDeleteMaterial, lblEnddingMaterial);
		
		lblCreateTematic = new Label("Crear");
		Label sl14 = new Label("/");
		lblModifyTematic = new Label("Modificar");
		Label sl15 = new Label("/");
		lblDeleteTematic = new Label("Eliminar");
		Label lblEnddingTematic = new Label("temáticas");
		lblEnddingTematic.getStyleClass().add("endding");
		HBox tematicGroup = new HBox();
		tematicGroup.getChildren().addAll(lblCreateTematic, sl14, lblModifyTematic, sl15, lblDeleteTematic, lblEnddingTematic);
		
		lblCreateSubject = new Label("Crear");
		Label sl16 = new Label("/");
		lblModifySubject = new Label("Modificar");
		Label sl17 = new Label("/");
		lblDeleteSubject = new Label("Eliminar");
		Label lblEnddingSubject = new Label("asignaturas");
		lblEnddingSubject.getStyleClass().add("endding");
		HBox subjectGroup = new HBox();
		subjectGroup.getChildren().addAll(lblCreateSubject, sl16, lblModifySubject, sl17, lblDeleteSubject, lblEnddingSubject);
		
		lblCreateLanguage = new Label("Crear");
		Label sl18 = new Label("/");
		lblModifyLanguage = new Label("Modificar");
		Label sl19 = new Label("/");
		lblDeleteLanguage = new Label("Eliminar");
		Label lblEnddingLanguage = new Label("idiomas");
		lblEnddingLanguage.getStyleClass().add("endding");
		HBox languageGroup = new HBox();
		languageGroup.getChildren().addAll(lblCreateLanguage, sl18, lblModifyLanguage, sl19, lblDeleteLanguage, lblEnddingLanguage);
		
		GridPane grid = new GridPane();
		//grid.setGridLinesVisible(true);
		ObservableList<Node> content = grid.getChildren();
		
		GridPane.setConstraints(lblHeaderUser, 0, 0);
		GridPane.setMargin(lblHeaderUser, new Insets(10, 5, 2, 10));
		content.add(lblHeaderUser);
		GridPane.setConstraints(lblHeaderMaterial, 1, 0);
		GridPane.setMargin(lblHeaderMaterial, new Insets(10, 10, 2, 5));
		content.add(lblHeaderMaterial);
		
		GridPane.setConstraints(userGroup, 0, 1);
		GridPane.setMargin(userGroup, new Insets(10, 5, 13, 10));
		content.add(userGroup);
		GridPane.setConstraints(materialGroup, 1, 1);
		GridPane.setMargin(materialGroup, new Insets(10, 10, 13, 5));
		content.add(materialGroup);
		
		GridPane.setConstraints(otherUserGroup, 0, 2);
		GridPane.setMargin(otherUserGroup, new Insets(10, 5, 13, 10));
		content.add(otherUserGroup);
		GridPane.setConstraints(tematicGroup, 1, 2);
		GridPane.setMargin(tematicGroup, new Insets(10, 10, 13, 5));
		content.add(tematicGroup);
		
		GridPane.setConstraints(privilegeGroup, 0, 3);
		GridPane.setMargin(privilegeGroup, new Insets(10, 5, 13, 10));
		content.add(privilegeGroup);
		GridPane.setConstraints(subjectGroup, 1, 3);
		GridPane.setMargin(subjectGroup, new Insets(10, 10, 13, 5));
		content.add(subjectGroup);
		
		GridPane.setConstraints(notesGroup, 0, 4);
		GridPane.setMargin(notesGroup, new Insets(10, 5, 13, 10));
		content.add(notesGroup);
		GridPane.setConstraints(languageGroup, 1, 4);
		GridPane.setMargin(languageGroup, new Insets(10, 10, 13, 5));
		content.add(languageGroup);
		
		GridPane.setConstraints(tgGroup, 0, 5);
		GridPane.setMargin(tgGroup, new Insets(10, 5, 13, 10));
		content.add(tgGroup);
		
		GridPane.setConstraints(rgGroup, 0, 6);
		GridPane.setMargin(rgGroup, new Insets(10, 5, 13, 10));
		content.add(rgGroup);
		
		GridPane.setConstraints(tcGroup, 0, 7);
		GridPane.setMargin(tcGroup, new Insets(10, 5, 13, 10));
		content.add(tcGroup);
		
		GridPane.setConstraints(scGroup, 0, 8);
		GridPane.setMargin(scGroup, new Insets(10, 5, 13, 10));
		content.add(scGroup);
		
		setTop(title);
		setCenter(grid);
		
		//adjust();
	}
	
	public void adjust(){
		Privilege priv = GUI.getInstance().getUser().getPrivilege();
		
		title.setText(GUI.calculateOnlyPrivilege(GUI.getInstance().getUser().getPrivilege()));
		
		if(priv.isModifyUser()){
			lblModifyOwnUser.getStyleClass().remove("red");
			lblModifyOwnUser.getStyleClass().add("green");
		}
		else{
			lblModifyOwnUser.getStyleClass().remove("green");
			lblModifyOwnUser.getStyleClass().add("red");
		}
		if(priv.isDeleteUser()){
			lblDeleteOwnUser.getStyleClass().remove("red");
			lblDeleteOwnUser.getStyleClass().add("green");
		}
		else{
			lblDeleteOwnUser.getStyleClass().remove("green");
			lblDeleteOwnUser.getStyleClass().add("red");
		}
		
		if(priv.isInsertOtherUser()){
			lblCreateOtherUser.getStyleClass().remove("red");
			lblCreateOtherUser.getStyleClass().add("green");
		}
		else{
			lblCreateOtherUser.getStyleClass().remove("green");
			lblCreateOtherUser.getStyleClass().add("red");
		}
		if(priv.isModifyOthersUsers()){
			lblModifyOtherUser.getStyleClass().remove("red");
			lblModifyOtherUser.getStyleClass().add("green");
		}
		else{
			lblModifyOtherUser.getStyleClass().remove("green");
			lblModifyOtherUser.getStyleClass().add("red");
		}
		if(priv.isDeleteOthersUsers()){
			lblDeleteOtherUser.getStyleClass().remove("red");
			lblDeleteOtherUser.getStyleClass().add("green");
		}
		else{
			lblDeleteOtherUser.getStyleClass().remove("green");
			lblDeleteOtherUser.getStyleClass().add("red");
		}
		
		if(priv.isPrivilegeEditing()){
			lblPrivilegeEditing.getStyleClass().remove("red");
			lblPrivilegeEditing.getStyleClass().add("green");
		}
		else{
			lblPrivilegeEditing.getStyleClass().remove("green");
			lblPrivilegeEditing.getStyleClass().add("red");
		}
		
		if(priv.isPublicNotes()){
			lblCreatePublicNotes.getStyleClass().remove("red");
			lblCreatePublicNotes.getStyleClass().add("green");
		}
		else{
			lblCreatePublicNotes.getStyleClass().remove("green");
			lblCreatePublicNotes.getStyleClass().add("red");
		}
		
		if(priv.isInsertTeachingGroup()){
			lblCreateTeachingGroup.getStyleClass().remove("red");
			lblCreateTeachingGroup.getStyleClass().add("green");
		}
		else{
			lblCreateTeachingGroup.getStyleClass().remove("green");
			lblCreateTeachingGroup.getStyleClass().add("red");
		}
		if(priv.isModifyTeachingGroup()){
			lblModifyTeachingGroup.getStyleClass().remove("red");
			lblModifyTeachingGroup.getStyleClass().add("green");
		}
		else{
			lblModifyTeachingGroup.getStyleClass().remove("green");
			lblModifyTeachingGroup.getStyleClass().add("red");
		}
		if(priv.isDeleteTeachingGroup()){
			lblDeleteTeachingGroup.getStyleClass().remove("red");
			lblDeleteTeachingGroup.getStyleClass().add("green");
		}
		else{
			lblDeleteTeachingGroup.getStyleClass().remove("green");
			lblDeleteTeachingGroup.getStyleClass().add("red");
		}			
		
		if(priv.isInsertResearchGroup()){
			lblCreateResearchGroup.getStyleClass().remove("red");
			lblCreateResearchGroup.getStyleClass().add("green");
		}
		else{
			lblCreateResearchGroup.getStyleClass().remove("green");
			lblCreateResearchGroup.getStyleClass().add("red");
		}
		if(priv.isModifyResearchGroup()){
			lblModifyResearchGroup.getStyleClass().remove("red");
			lblModifyResearchGroup.getStyleClass().add("green");
		}
		else{
			lblModifyResearchGroup.getStyleClass().remove("green");
			lblModifyResearchGroup.getStyleClass().add("red");
		}
		if(priv.isDeleteResearchGroup()){
			lblDeleteResearchGroup.getStyleClass().remove("red");
			lblDeleteResearchGroup.getStyleClass().add("green");
		}
		else{
			lblDeleteResearchGroup.getStyleClass().remove("green");
			lblDeleteResearchGroup.getStyleClass().add("red");
		}
		
		if(priv.isInsertTeachingCategory()){
			lblCreateTeachingCategory.getStyleClass().remove("red");
			lblCreateTeachingCategory.getStyleClass().add("green");
		}
		else{
			lblCreateTeachingCategory.getStyleClass().remove("green");
			lblCreateTeachingCategory.getStyleClass().add("red");
		}
		if(priv.isModifyTeachingCategory()){
			lblModifyTeachingCategory.getStyleClass().remove("red");
			lblModifyTeachingCategory.getStyleClass().add("green");
		}
		else{
			lblModifyTeachingCategory.getStyleClass().remove("green");
			lblModifyTeachingCategory.getStyleClass().add("red");
		}
		if(priv.isDeleteTeachingCategory()){
			lblDeleteTeachingCategory.getStyleClass().remove("red");
			lblDeleteTeachingCategory.getStyleClass().add("green");
		}
		else{
			lblDeleteTeachingCategory.getStyleClass().remove("green");
			lblDeleteTeachingCategory.getStyleClass().add("red");
		}
		
		if(priv.isInsertScientificCategory()){
			lblCreateScientificCategory.getStyleClass().remove("red");
			lblCreateScientificCategory.getStyleClass().add("green");
		}
		else{
			lblCreateScientificCategory.getStyleClass().remove("green");
			lblCreateScientificCategory.getStyleClass().add("red");
		}
		if(priv.isModifyScientificCategory()){
			lblModifyScientificCategory.getStyleClass().remove("red");
			lblModifyScientificCategory.getStyleClass().add("green");
		}
		else{
			lblModifyScientificCategory.getStyleClass().remove("green");
			lblModifyScientificCategory.getStyleClass().add("red");
		}
		if(priv.isDeleteScientificCategory()){
			lblDeleteScientificCategory.getStyleClass().remove("red");
			lblDeleteScientificCategory.getStyleClass().add("green");
		}
		else{
			lblDeleteScientificCategory.getStyleClass().remove("green");
			lblDeleteScientificCategory.getStyleClass().add("red");
		}
		
		if(priv.isInsertMaterial()){
			lblCreateMaterial.getStyleClass().remove("red");
			lblCreateMaterial.getStyleClass().add("green");
		}
		else{
			lblCreateMaterial.getStyleClass().remove("green");
			lblCreateMaterial.getStyleClass().add("red");
		}
		if(priv.isModifyMaterial()){
			lblModifyMaterial.getStyleClass().remove("red");
			lblModifyMaterial.getStyleClass().add("green");
		}
		else{
			lblModifyMaterial.getStyleClass().remove("green");
			lblModifyMaterial.getStyleClass().add("red");
		}
		if(priv.isDeleteMaterial()){
			lblDeleteMaterial.getStyleClass().remove("red");
			lblDeleteMaterial.getStyleClass().add("green");
		}
		else{
			lblDeleteMaterial.getStyleClass().remove("green");
			lblDeleteMaterial.getStyleClass().add("red");
		}
		
		if(priv.isInsertTematic()){
			lblCreateTematic.getStyleClass().remove("red");
			lblCreateTematic.getStyleClass().add("green");
		}
		else{
			lblCreateTematic.getStyleClass().remove("green");
			lblCreateTematic.getStyleClass().add("red");
		}
		if(priv.isModifyTematic()){
			lblModifyTematic.getStyleClass().remove("red");
			lblModifyTematic.getStyleClass().add("green");
		}
		else{
			lblModifyTematic.getStyleClass().remove("green");
			lblModifyTematic.getStyleClass().add("red");
		}
		if(priv.isDeleteTematic()){
			lblDeleteTematic.getStyleClass().remove("red");
			lblDeleteTematic.getStyleClass().add("green");
		}
		else{
			lblDeleteTematic.getStyleClass().remove("green");
			lblDeleteTematic.getStyleClass().add("red");
		}
		
		if(priv.isInsertSubject()){
			lblCreateSubject.getStyleClass().remove("red");
			lblCreateSubject.getStyleClass().add("green");
		}
		else{
			lblCreateSubject.getStyleClass().remove("green");
			lblCreateSubject.getStyleClass().add("red");
		}
		if(priv.isModifySubject()){
			lblModifySubject.getStyleClass().remove("red");
			lblModifySubject.getStyleClass().add("green");
		}
		else{
			lblModifySubject.getStyleClass().remove("green");
			lblModifySubject.getStyleClass().add("red");
		}
		if(priv.isDeleteSubject()){
			lblDeleteSubject.getStyleClass().remove("red");
			lblDeleteSubject.getStyleClass().add("green");
		}
		else{
			lblDeleteSubject.getStyleClass().remove("green");
			lblDeleteSubject.getStyleClass().add("red");
		}
		
		if(priv.isInsertLanguage()){
			lblCreateLanguage.getStyleClass().remove("red");
			lblCreateLanguage.getStyleClass().add("green");
		}
		else{
			lblCreateLanguage.getStyleClass().remove("green");
			lblCreateLanguage.getStyleClass().add("red");
		}
		if(priv.isModifyLanguage()){
			lblModifyLanguage.getStyleClass().remove("red");
			lblModifyLanguage.getStyleClass().add("green");
		}
		else{
			lblModifyLanguage.getStyleClass().remove("green");
			lblModifyLanguage.getStyleClass().add("red");
		}
		if(priv.isDeleteLanguage()){
			lblDeleteLanguage.getStyleClass().remove("red");
			lblDeleteLanguage.getStyleClass().add("green");
		}
		else{
			lblDeleteLanguage.getStyleClass().remove("green");
			lblDeleteLanguage.getStyleClass().add("red");
		}

	}
	
}