package utils;

import java.io.IOException;
import java.sql.SQLException;

import javafx.animation.AnimationTimer;
import models.Privilege;
import models.users.User;
import services.UserServices;
import visual.controls.Notification;
import visual.controls.center.CenterViewerMain;
import visual.controls.center.materialPage.Item;
import visual.controls.center.notesPane.NotesViewer;
import visual.controls.center.userCountPage.AcademicDataPane;
import visual.controls.center.userCountPage.CurrentUserPane;
import visual.controls.center.userCountPage.OtherUsersPane;
import visual.controls.center.userCountPage.UserDialogBox;
import visual.controls.center.userCountPage.UsersCountViewer;
import visual.controls.right.NotesNewsAndMoreVisited;
import visual.controls.top.MenuToolBarPane;
import visual.controls.top.TitledPane;

public class GUI {
	private static int TIME_TO_UPDATE = 3000; //1 min
	private AnimationTimer update;
	private Integer currentValue = 0;
	
	private static GUI gui = null;
	private static int MAX_POINT = 31;
	
	private User user;
	
	private GUI(){
		user = null;
		
		update = new AnimationTimer() {			
			public void handle(long now) {
				currentValue++;
				if(currentValue == TIME_TO_UPDATE){
					NotesNewsAndMoreVisited.getInstance().update();
					currentValue = 0;
					Notification.getInstance().setText("Se han actualizado los materiales más visitados y los últimos añadidos, así como las notas");
					Notification.getInstance().showNotification();
				}				
			}
		};
		
		update.start();
	}
	
	public static GUI getInstance(){
		if(gui == null)
			gui = new GUI();
		return gui;
	}

	public boolean haveUser(){
		return user != null;
	}
	
	public User getUser(){
		return user;
	}
	
	public void setDefault(){
		TitledPane.getInstance().setDefaultData();
		MenuToolBarPane.getInstance().setDefaultData();
		user = null;
		CenterViewerMain.getInstance().put(null);
		UsersCountViewer.getInstance().setDefault();
		Item.thisUserCanDoCancel(false);
		Item.thisUserCanDoSelect(false);
	}
	
	public void setUser(User user){
		this.user = user;
		
		try {
			TitledPane.getInstance().setData();
		} catch (IOException e) {
			DialogBox diag = new DialogBox("No se pudo incluir su avatar en miniatura en la barra de título");
			diag.show();
		}

		
		MenuToolBarPane menuToolBarPane = MenuToolBarPane.getInstance();
		try {
			menuToolBarPane.setData();
		} catch (IOException e) {
			DialogBox diag = new DialogBox("No se pudo incluir su avatar en la barra de menús");
			diag.show();
		}
		menuToolBarPane.showBtnNotes();
		menuToolBarPane.showBtnUsersCount();
		
		
		UsersCountViewer.getInstance().setData();
		NotesViewer.getInstance().setNotes();
		
		adjustGUIByPrivilege(this.user.getPrivilege());
	}
	
	public void update(){
		Privilege pri = user.getPrivilege();
		try {
			user = UserServices.getUser(user.getId());
			user.setPrivilege(pri);
		} catch (SQLException | IOException e) {
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
	
	private void adjustGUIByPrivilege(Privilege priv){
		//int counter = 0;
		
		//MenuToolBarPane menuToolBarPane = MenuToolBarPane.getInstance();
		//controlar la visualizacion de botones y pantallas
		//Respecto a la gestion de usuarios
		
		if(priv.isModifyUser()){
			CurrentUserPane.getInstance().showModify();
		}
		else{
			CurrentUserPane.getInstance().hideModify();
		}

		if(priv.isDeleteUser()){
			CurrentUserPane.getInstance().showDelete();
		}
		else{
			CurrentUserPane.getInstance().hideDelete();
		}

		
		if(priv.isInsertOtherUser()){
			OtherUsersPane.getInstance().showInsert();
		}
		else{
			OtherUsersPane.getInstance().hideInsert();
		}

		if(priv.isModifyOthersUsers()){
			OtherUsersPane.getInstance().showModify();
		}
		else{
			OtherUsersPane.getInstance().hideModify();
		}

		if(priv.isDeleteOthersUsers()){
			OtherUsersPane.getInstance().showDelete();
		}
		else{
			OtherUsersPane.getInstance().hideDelete();
		}

		
		//Establecer notas publicas
		NotesViewer.getInstance().canPublicNotes(priv.isPublicNotes());
		
		//Edicion de privilegios de otros usuarios
		UserDialogBox.setEditPrivilege(priv.isPrivilegeEditing());

		
		if(priv.isPublicNotes()){
			//Pendiente
		}
		
		if(priv.isInsertTeachingGroup()){
			AcademicDataPane.getInstance().showInsertTeachingGroup();
		}
		else{
			AcademicDataPane.getInstance().hideInsertTeachingGroup();
		}

		if(priv.isModifyTeachingGroup()){
			AcademicDataPane.getInstance().showModifyTeachingGroup();
		}
		else{
			AcademicDataPane.getInstance().hideModifyTeachingGroup();
		}

		if(priv.isDeleteTeachingGroup()){
			AcademicDataPane.getInstance().showDeleteTeachingGroup();
		}
		else{
			AcademicDataPane.getInstance().hideDeleteTeachingGroup();
		}
			
		
		if(priv.isInsertResearchGroup()){
			AcademicDataPane.getInstance().showInsertResearchGroup();
		}
		else{
			AcademicDataPane.getInstance().hideInsertResearchGroup();
		}			

		if(priv.isModifyResearchGroup()){
			AcademicDataPane.getInstance().showModifyResearchGroup();
		}
		else{
			AcademicDataPane.getInstance().hideModifyResearchGroup();
		}

		if(priv.isDeleteResearchGroup()){
			AcademicDataPane.getInstance().showDeleteResearchGroup();
		}
		else{
			AcademicDataPane.getInstance().hideDeleteResearchGroup();
		}
		
		if(priv.isInsertTeachingCategory()){
			AcademicDataPane.getInstance().showInsertTeachingCategory();
		}
		else{
			AcademicDataPane.getInstance().hideInsertTeachingCategory();
		}

		if(priv.isModifyTeachingCategory()){
			AcademicDataPane.getInstance().showModifyTeachingCategory();
		}
		else{
			AcademicDataPane.getInstance().hideModifyTeachingCategory();
		}

		if(priv.isDeleteTeachingCategory()){
			AcademicDataPane.getInstance().showDeleteTeachingCategory();
		}
		else{
			AcademicDataPane.getInstance().hideDeleteTeachingCategory();
		}

		
		if(priv.isInsertScientificCategory()){
			AcademicDataPane.getInstance().showInsertScientificCategory();
		}
		else{
			AcademicDataPane.getInstance().hideInsertScientificCategory();
		}

		if(priv.isModifyScientificCategory()){
			AcademicDataPane.getInstance().showModifyScientificCategory();
		}
		else{
			AcademicDataPane.getInstance().hideModifyScientificCategory();
		}

		if(priv.isDeleteScientificCategory()){
			AcademicDataPane.getInstance().showDeleteScientificCategory();
		}
		else{
			AcademicDataPane.getInstance().hideDeleteScientificCategory();
		}

		
		Item.thisUserCanDoCancel(priv.isModifyTematic());
		Item.thisUserCanDoSelect(priv.isModifyTematic() || priv.isDeleteTematic());

		/*
		 * El bloqueo de las funcionalidades de insertar, modificar y cancelar/eliminar
		 * tematicas, asignaturas y materiales se gestiona desde la clase MaterialViewer
		 * debido a que como se emplean los mismos botones para todas las acciones,
		 * los cambios eran muy variables para poder controlarlos desde aqui
		 */
		
	}

	public static String calculateOnlyPrivilege(Privilege priv){
		String level = "";
		int counter = 0;
		
		if(priv.isModifyUser()) 				counter++;
		if(priv.isDeleteUser()) 				counter++;		
		if(priv.isInsertOtherUser()) 			counter++;
		if(priv.isModifyOthersUsers()) 			counter++;
		if(priv.isDeleteOthersUsers()) 			counter++;		
		if(priv.isPrivilegeEditing()) 			counter++;		
		if(priv.isPublicNotes()) 				counter++;		
		if(priv.isInsertTeachingGroup()) 		counter++;
		if(priv.isModifyTeachingGroup()) 		counter++;
		if(priv.isDeleteTeachingGroup()) 		counter++;		
		if(priv.isInsertResearchGroup()) 		counter++;
		if(priv.isModifyResearchGroup()) 		counter++;
		if(priv.isDeleteResearchGroup()) 		counter++;		
		if(priv.isInsertTeachingCategory()) 	counter++;
		if(priv.isModifyTeachingCategory()) 	counter++;
		if(priv.isDeleteTeachingCategory()) 	counter++;	
		if(priv.isInsertScientificCategory()) 	counter++;
		if(priv.isModifyScientificCategory()) 	counter++;
		if(priv.isDeleteScientificCategory()) 	counter++;		
		if(priv.isInsertMaterial()) 			counter++;
		if(priv.isModifyMaterial()) 			counter++;
		if(priv.isDeleteMaterial()) 			counter++;		
		if(priv.isInsertTematic()) 				counter++;
		if(priv.isModifyTematic()) 				counter++;
		if(priv.isDeleteTematic()) 				counter++;	
		if(priv.isInsertSubject()) 				counter++;
		if(priv.isModifySubject()) 				counter++;
		if(priv.isDeleteSubject()) 				counter++;
		if(priv.isInsertLanguage())				counter++;
		if(priv.isModifyLanguage())				counter++;
		if(priv.isDeleteLanguage())				counter++;
		
		if(counter == MAX_POINT)
			level = "Privilegio: Total";
		else if(counter < MAX_POINT && counter >= 20)
			level = "Privilegio: Alto";
		else if(counter < 20 && counter >= 10)
			level = "Privilegio: Medio";
		else
			level = "Privilegio: Bajo";
		
		return level;
	}
}
