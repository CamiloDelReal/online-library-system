package visual.controls.center.materialPage;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.layout.BorderPane;
import models.Subject;
import models.Tematic;
import models.materials.Material;
import models.users.Student;
import models.users.Teacher;
import models.users.User;
import services.MaterialServices;
import services.SubjectServices;
import services.TematicServices;
import utils.DialogBox;
import utils.GUI;
import utils.Utilities;

public class MaterialViewer extends BorderPane {
	private static MaterialViewer viewer= null;
	
	private ToolbarViewer toolbarViewer;//pensado en quitarlo
	private ItemsViewer itemsViewer;
	private CurrentMaterialPane currentMaterial;
	
	
	public static MaterialViewer getInstance(){
		if(viewer == null)
			viewer = new MaterialViewer();
		
		return viewer;
	}
	
	private MaterialViewer(){
		setId("materialViewer");
		
		toolbarViewer = new ToolbarViewer();
		
		setTop(toolbarViewer);
		
		itemsViewer = ItemsViewer.getInstance();
		currentMaterial = new CurrentMaterialPane();

		setCenter(currentMaterial);
	}
	
	public void setData() throws SQLException{
		itemsViewer.clear();
		List<Tematic> tematics = null;
		List<Item> items = new LinkedList<Item>();
		
		User user = GUI.getInstance().getUser();
		if(user != null && user.getPrivilege() != null && user.getPrivilege().isModifyTematic()){
			tematics = TematicServices.getTematics();
		}
		else{
			tematics = TematicServices.getEnabledTematics();
		}
		
		for(Tematic tem : tematics){ 
			String tooltip = "Temática: " + tem.getName() + "\n";
			int countSubject = TematicServices.getAmountSubjectInTematic(tem.getId());
			tooltip += "Cantidad de asignaturas: " + String.valueOf(countSubject) + "\n";
			int countMaterial = TematicServices.getAmountMaterialInTematic(tem.getId());
			tooltip += "Cantidad de materiales: " + String.valueOf(countMaterial);
			items.add(new Item(tem.getId(), tem.getName(), FileExtension.FOLDER, tem.isCanceled(), TypeItem.TEMATIC, tooltip));
		}
		
		toolbarViewer.addTematic();
		itemsViewer.setTypesOfItems(TypeItem.TEMATIC);
		itemsViewer.setItems(items);
		
		int countBut = 0;
		//User user = GUI.getInstance().getUser();  //ya se declaro arriba
		if(user != null && user.getPrivilege() != null){
			if(user.getPrivilege().isInsertTematic())
				countBut++;
			itemsViewer.setCanInsert(user.getPrivilege().isInsertTematic());
			
			if(user.getPrivilege().isModifyTematic())
				countBut++;
			itemsViewer.setCanModify(user.getPrivilege().isModifyTematic());
			
			if(user.getPrivilege().isDeleteTematic())
				countBut++;
			itemsViewer.setCanDelete(user.getPrivilege().isDeleteTematic());
		}
		if(countBut == 0)
			itemsViewer.setVisibleActionButtons(false);
		else
			itemsViewer.setVisibleActionButtons(true);
		
		setCenter(itemsViewer);
	}
	
	
	public void addTematic(){
		toolbarViewer.addTematic();
	}
	public void addSubject(int tematicId){
		toolbarViewer.addSubject(tematicId);
	}
	public void addMaterial(int subjectId){
		toolbarViewer.addMaterial(subjectId);
	}
	public void addMoreVisited(){
		toolbarViewer.addMoreVisited();
	}
	public void addLastInserted(){
		toolbarViewer.addLastInserted();
	}
	public void updateLastTabReference(String name){
		toolbarViewer.updateNameOfLastRefference(name);
	}
	
	public void putTematics(){
		List<Tematic> tematics = null;
		try{
			if(GUI.getInstance().getUser() != null && GUI.getInstance().getUser().getPrivilege().isModifySubject()){
				tematics = TematicServices.getTematics();
			}
			else{
				tematics = TematicServices.getEnabledTematics();
			}
		} catch (SQLException e1) {
			DialogBox diag = new DialogBox("No se pudieron obtener las temáticas");
			diag.show();
		}
		
		if(tematics != null){
			List<Item> items = new LinkedList<Item>();
			for(Tematic tem : tematics){
				String tooltip = "Temática: " + tem.getName() + "\n";
				try{
					int countSubject = TematicServices.getAmountSubjectInTematic(tem.getId());
					tooltip += "Cantidad de asignaturas: " + String.valueOf(countSubject) + "\n";
					int countMaterial = TematicServices.getAmountMaterialInTematic(tem.getId());
					tooltip += "Cantidad de materiales: " + String.valueOf(countMaterial);
				}
				catch(SQLException e1){
					DialogBox diag = new DialogBox("No se pudieron obtener datos referentas a la temática: " + tem.getName());
					diag.show();
				}
				items.add(new Item(tem.getId(), tem.getName(), FileExtension.FOLDER, tem.isCanceled(), TypeItem.TEMATIC, tooltip));
			}
			itemsViewer.setItems(items);
		}
		
		toolbarViewer.addTematic();		
		
		int countBut = 0;
		User user = GUI.getInstance().getUser();
		if(user != null && user.getPrivilege() != null){
			if(user.getPrivilege().isInsertTematic())
				countBut++;
			itemsViewer.setCanInsert(user.getPrivilege().isInsertTematic());
			
			if(user.getPrivilege().isModifyTematic())
				countBut++;
			itemsViewer.setCanModify(user.getPrivilege().isModifyTematic());
			
			if(user.getPrivilege().isDeleteTematic())
				countBut++;
			itemsViewer.setCanDelete(user.getPrivilege().isDeleteTematic());
		}
		if(countBut == 0)
			itemsViewer.setVisibleActionButtons(false);
		else
			itemsViewer.setVisibleActionButtons(true);

		setCenter(itemsViewer);
	}
	
	public void putSubjects(int tematicId, String tematicName){
		List<Subject> subjects = null;
		try{
			if(GUI.getInstance().getUser() != null && GUI.getInstance().getUser().getPrivilege().isModifySubject()){
				subjects = SubjectServices.getSubjectByTematic(tematicId);
			}
			else{
				subjects = SubjectServices.getEnabledSubjectByTematic(tematicId);
			}
		} catch (SQLException e1) {
			DialogBox diag = new DialogBox("No se pudieron obtener las asignaturas de la temática: " + tematicName);
			diag.show();
		}
		
		if(subjects != null){
			List<Item> items = new LinkedList<Item>();
			for(Subject sub : subjects){
				String tooltip = "Asignatura: " + sub.getName() + "\n";
				try{
					int countMaterial = SubjectServices.getAmountMaterialsInSubject(sub.getId());
					tooltip += "Cantidad de materiales: " + String.valueOf(countMaterial);// + "\n";
				}
				catch(SQLException e2){
					DialogBox diag = new DialogBox("No se pudieron obtener datos referentes a la asignatura: " + sub.getName());
					diag.show();
				}
				items.add(new Item(sub.getId(), sub.getName(), FileExtension.FOLDER, sub.isCanceled(), TypeItem.SUBJECT, tooltip));
			}
			itemsViewer.setItems(items);
		}
		
		toolbarViewer.addSubject(tematicId);
		
		int countBut = 0;
		User user = GUI.getInstance().getUser();
		if(user != null && user.getPrivilege() != null){
			if(user.getPrivilege().isInsertSubject())
				countBut++;
			itemsViewer.setCanInsert(user.getPrivilege().isInsertSubject());
			
			if(user.getPrivilege().isModifySubject())
				countBut++;
			itemsViewer.setCanModify(user.getPrivilege().isModifySubject());
			
			if(user.getPrivilege().isDeleteSubject())
				countBut++;
			itemsViewer.setCanDelete(user.getPrivilege().isDeleteSubject());
		}
		if(countBut == 0)
			itemsViewer.setVisibleActionButtons(false);
		else
			itemsViewer.setVisibleActionButtons(true);
		
		setCenter(itemsViewer);
	}
	
	public void putMaterials(int subjectId, String subjectName){
		List<Material> materials = null;
		try{
			if(GUI.getInstance().getUser() != null && GUI.getInstance().getUser().getPrivilege().isModifyMaterial()){
				materials = MaterialServices.getMaterialBySubject(subjectId);
			}
			else{
				materials = MaterialServices.getEnabledMaterialBySubject(subjectId); 
			}
		} catch (SQLException e1) {
			DialogBox diag = new DialogBox("No se pudieron obtener los materiales de la asignatura: " + subjectName);
			diag.show();
		}
		
		if(materials != null){
			List<Item> items = new LinkedList<Item>();
			for(Material mat : materials){
				String tooltip = "Título: " + mat.getTitle() + "\n";
				boolean ref = false;
				try{
					String type = MaterialServices.getTypeOfReference(mat.getCode());
					tooltip += "Tipo de material: " + type;
					if(ref)
						tooltip += "Referenciado\n";
					tooltip += "Visitado por: " + (mat.getVisitedByStudent() + mat.getVisitedByTeachers() + mat.getVisitedByUnknown()) + " usuarios";
				}
				catch(SQLException e2){
					DialogBox diag = new DialogBox("No se pudieron obtener datos referentes al material: " + mat.getTitle());
					diag.show();
					e2.printStackTrace();
				}
				items.add(new Item(mat.getCode(), mat.getTitle(), Utilities.getFileExtension(mat.getLocation()), mat.isCanceled(), TypeItem.MATERIAL, tooltip));
			}
						
			itemsViewer.setItems(items);
		}
		
		toolbarViewer.addMaterial(subjectId);
		
		int countBut = 0;
		User user = GUI.getInstance().getUser();
		if(user != null && user.getPrivilege() != null){
			if(user.getPrivilege().isInsertMaterial())
				countBut++;
			itemsViewer.setCanInsert(user.getPrivilege().isInsertMaterial());
			
			if(user.getPrivilege().isModifyMaterial())
				countBut++;
			itemsViewer.setCanModify(user.getPrivilege().isModifyMaterial());
			
			if(user.getPrivilege().isDeleteMaterial())
				countBut++;
			itemsViewer.setCanDelete(user.getPrivilege().isDeleteMaterial());
		}
		if(countBut == 0)
			itemsViewer.setVisibleActionButtons(false);
		else
			itemsViewer.setVisibleActionButtons(true);
		
		setCenter(itemsViewer);
	}
	
	public void putMoreVisited(){
		List<Material> materials = null;
		try{
			materials = MaterialServices.getMoreVisited16();
		} catch (SQLException e1) {
			DialogBox diag = new DialogBox("No se pudieron obtener los materiales deseados");
			diag.show();
		}
		
		if(materials != null){
			List<Item> items = new LinkedList<Item>();
			for(Material mat : materials){
				String tooltip = "Título: " + mat.getTitle() + "\n";
				try{
					String type = MaterialServices.getTypeOfReference(mat.getCode());
					tooltip += "Tipo de material: " + type;
					tooltip += "\nVisitado por: " + (mat.getVisitedByStudent() + mat.getVisitedByTeachers() + mat.getVisitedByUnknown()) + " usuarios"; 
				}
				catch(SQLException e2){
					DialogBox diag = new DialogBox("No se pudieron obtener datos referentes al material: " + mat.getTitle());
					diag.show();
					//e2.printStackTrace();
				}
				items.add(new Item(mat.getCode(), mat.getTitle(), Utilities.getFileExtension(mat.getLocation()), false, TypeItem.MORE_VISITED, tooltip));
			}
			itemsViewer.setItems(items);
		}
		
		toolbarViewer.addMoreVisited();
		itemsViewer.setVisibleActionButtons(false);
		
		setCenter(itemsViewer);
	}
	public void putMoreVisitedMaterial(int materialId){
		try{
			addMoreVisited();
			int subjectId = MaterialServices.getSubjectOfMaterial(materialId);
			Material material = putMaterial(materialId, subjectId);
			updateLastTabReference(material.getTitle());
		}catch(SQLException e){
			DialogBox diag = new DialogBox("No se pudieron obtener los datos del material deseado");
			diag.show();
		}
	}
	
	public void putLastInserted(){
		List<Material> materials = null;
		try{
			materials = MaterialServices.getLastInserted16();
		} catch (SQLException e1) {
			DialogBox diag = new DialogBox("No se pudieron obtener los materiales deseados");
			diag.show();
		}
		
		if(materials != null){
			List<Item> items = new LinkedList<Item>();
			for(Material mat : materials){
				String tooltip = "Título: " + mat.getTitle() + "\n";
				try{
					String type = MaterialServices.getTypeOfReference(mat.getCode());
					tooltip += "Tipo de material: " + type;
				}
				catch(SQLException e2){
					DialogBox diag = new DialogBox("No se pudieron obtener datos referentes al material: " + mat.getTitle());
					diag.show();
					//e2.printStackTrace();
				}
				items.add(new Item(mat.getCode(), mat.getTitle(), Utilities.getFileExtension(mat.getLocation()), false, TypeItem.MORE_VISITED, tooltip));
			}
			ItemsViewer.getInstance().setItems(items);
		}
		
		toolbarViewer.addLastInserted();
		itemsViewer.setVisibleActionButtons(false);
		setCenter(itemsViewer);
	}
	
	public void putLastInsertedMaterial(int materialId){
		try{
			addLastInserted();
			int subjectId = MaterialServices.getSubjectOfMaterial(materialId);
			Material material = putMaterial(materialId, subjectId);
			updateLastTabReference(material.getTitle());
		}catch(SQLException e){
			DialogBox diag = new DialogBox("No se pudieron obtener los datos del material deseado");
			diag.show();
		}
	}
	
	public Material putMaterial(int materialId, int subjectId){
		//Añadir visita
		User currentUser = GUI.getInstance().getUser();
		try{
			if(currentUser instanceof Student){
				MaterialServices.increseVisitedByStudent(materialId);
			}
			else if(currentUser instanceof Teacher){
				MaterialServices.increseVisitedByTeacher(materialId);
			}
			else{
				MaterialServices.increseVisitedByUnknow(materialId);
			}
		}
		catch(SQLException e){
			DialogBox diag = new DialogBox("No se pudo incrementar el registro de visitas");
			diag.show();
		}
		
		Material material = null;
		try {
			material = MaterialServices.getMaterialByID(materialId);
		} catch (SQLException e) {
			DialogBox diag = new DialogBox("No se pudieron obtener datos referentes al material deseado");
			diag.show();
			//e.printStackTrace();
		}
		currentMaterial.setMaterial(material);
		currentMaterial.setSubjectId(subjectId);
		setCenter(currentMaterial);		
		
		
		if(currentUser != null && currentUser.getPrivilege().isModifyMaterial())
			currentMaterial.showModify();
		else
			currentMaterial.hideModify();
		if(currentUser != null && currentUser.getPrivilege().isDeleteMaterial())
			currentMaterial.showDelete();
		else
			currentMaterial.hideDelete();
		
		return material;
	}
}
