package models;

import java.util.ArrayList;
import java.util.List;

public class Privilege {
	private int id;
	
	private List<String> names;
	
	private boolean modifyUser;
	private boolean deleteUser;
	
	private boolean insertOtherUser;
	private boolean modifyOthersUsers;	
	private boolean deleteOthersUsers;
	
	private boolean privilegeEditing;
	
	private boolean publicNotes;
	
	private boolean insertTeachingGroup;
	private boolean modifyTeachingGroup;
	private boolean deleteTeachingGroup;
	
	private boolean insertResearchGroup;
	private boolean modifyResearchGroup;
	private boolean deleteResearchGroup;
	
	private boolean insertTeachingCategory;
	private boolean modifyTeachingCategory;
	private boolean deleteTeachingCategory;
	
	private boolean insertScientificCategory;
	private boolean modifyScientificCategory;
	private boolean deleteScientificCategory;
	
	private boolean insertMaterial;
	private boolean modifyMaterial;
	private boolean deleteMaterial;
	
	private boolean insertTematic;
	private boolean modifyTematic;
	private boolean deleteTematic;	
	
	private boolean insertSubject;
	private boolean modifySubject;
	private boolean deleteSubject;
	
	private boolean insertLanguage;
	private boolean modifyLanguage;
	private boolean deleteLanguage;
	
	/*public Privilege(int id, 
			boolean modifyUser, boolean deleteUser,
			boolean insertOtherUser, boolean modifyOthersUsers, boolean deleteOthersUsers, 
			boolean privilegeEditing, boolean publicNotes, 
			boolean insertTeachingGroup, boolean modifyTeachingGroup, boolean deleteTeachingGroup,
			boolean insertResearchGroup, boolean modifyResearchGroup, boolean deleteResearchGroup, 
			boolean insertTeachingCategory,	boolean modifyTeachingCategory, boolean deleteTeachingCategory,
			boolean insertScientificCategory, boolean modifyScientificCategory, boolean deleteScientificCategory,
			boolean insertMaterial,	boolean modifyMaterial, boolean deleteMaterial,
			boolean insertTematic, boolean modifyTematic, boolean deleteTematic,
			boolean insertSubject, boolean modifySubject, boolean deleteSubject) {
		this.id = id;
		this.modifyUser = modifyUser;
		this.deleteUser = deleteUser;
		this.insertOtherUser = insertOtherUser;
		this.modifyOthersUsers = modifyOthersUsers;
		this.deleteOthersUsers = deleteOthersUsers;
		this.privilegeEditing = privilegeEditing;
		this.publicNotes = publicNotes;
		this.insertTeachingGroup = insertTeachingGroup;
		this.modifyTeachingGroup = modifyTeachingGroup;
		this.deleteTeachingGroup = deleteTeachingGroup;
		this.insertResearchGroup = insertResearchGroup;
		this.modifyResearchGroup = modifyResearchGroup;
		this.deleteResearchGroup = deleteResearchGroup;
		this.insertTeachingCategory = insertTeachingCategory;
		this.modifyTeachingCategory = modifyTeachingCategory;
		this.deleteTeachingCategory = deleteTeachingCategory;
		this.insertScientificCategory = insertScientificCategory;
		this.modifyScientificCategory = modifyScientificCategory;
		this.deleteScientificCategory = deleteScientificCategory;
		this.insertMaterial = insertMaterial;
		this.modifyMaterial = modifyMaterial;
		this.deleteMaterial = deleteMaterial;
		this.insertTematic = insertTematic;
		this.modifyTematic = modifyTematic;
		this.deleteTematic = deleteTematic;
		this.insertSubject = insertSubject;
		this.modifySubject = modifySubject;
		this.deleteSubject = deleteSubject;
	}*/
	
	

	public Privilege() {
		this.modifyUser = false;
		this.deleteUser = false;
		this.insertOtherUser = false;
		this.modifyOthersUsers = false;
		this.deleteOthersUsers = false;
		this.privilegeEditing = false;
		this.publicNotes = false;
		this.insertTeachingGroup = false;
		this.modifyTeachingGroup = false;
		this.deleteTeachingGroup = false;
		this.insertResearchGroup = false;
		this.modifyResearchGroup = false;
		this.deleteResearchGroup = false;
		this.insertTeachingCategory = false;
		this.modifyTeachingCategory = false;
		this.deleteTeachingCategory = false;
		this.insertScientificCategory = false;
		this.modifyScientificCategory = false;
		this.deleteScientificCategory = false;
		this.insertMaterial = false;
		this.modifyMaterial = false;
		this.deleteMaterial = false;
		this.insertTematic = false;
		this.modifyTematic = false;
		this.deleteTematic = false;
		this.insertSubject = false;
		this.modifySubject = false;
		this.deleteSubject = false;
		insertLanguage = false;
		modifyLanguage = false;
		deleteLanguage = false;
		
		names = new ArrayList<String>();
	}

	public Privilege(@SuppressWarnings("rawtypes") List roles){
		names = new ArrayList<String>();
		for(Object rol : roles){
			String nameRol = null;
			
			if(rol instanceof String)
				nameRol = (String)rol;
			else if(rol instanceof Rol)
				nameRol = ((Rol)rol).getName();
			
			names.add(nameRol);
			
			switch(nameRol){
				case "Modificar su usuario":
					modifyUser = true;
					break;
				case "Eliminar su usuario":
					deleteUser = true;
					break;
				case "Insertar otros usuarios":
					insertOtherUser = true;
					break;
				case "Modificar otros usuarios":
					modifyOthersUsers = true;
					break;
				case "Eliminar otros usuarios":
					deleteOthersUsers = true;
					break;
				case "Insertar Grupos Docentes":
					insertTeachingGroup = true;
					break;
				case "Modificar Grupos Docentes":
					modifyTeachingGroup = true;
					break;
				case "Eliminar Grupos Docentes":
					deleteTeachingGroup = true;
					break;
				case "Insertar Grupos Investigativos":
					insertResearchGroup = true;
					break;
				case "Modificar Grupos Investigativos":
					modifyResearchGroup = true;
					break;
				case "Eliminar Grupos Investigativos":
					deleteResearchGroup = true;
					break;
				case "Insertar Categoría Docente":
					insertTeachingCategory = true;
					break;
				case "Modificar Categoría Docente":
					modifyTeachingCategory = true;
					break;
				case "Eliminar Categoría Docente":
					deleteTeachingCategory = true;
					break;
				case "Insertar Categoría Científica":
					insertScientificCategory = true;
					break;
				case "Modificar Categoría Científica":
					modifyScientificCategory = true;
					break;
				case "Eliminar Categoría Científica":
					deleteScientificCategory = true;
					break;
				case "Crear notas públicas":
					publicNotes = true;
					break;
				case "Editar los privilegios de otros usuarios":
					privilegeEditing = true;
					break;
				case "Insertar Temáticas":
					insertTematic = true;
					break;
				case "Modificar Temáticas":
					modifyTematic = true;
					break;
				case "Eliminar Temáticas":
					deleteTematic = true;
					break;
				case "Insertar Asignaturas":
					insertSubject = true;
					break;
				case "Modificar Asignaturas":
					modifySubject = true;
					break;
				case "Eliminar Asignaturas":
					deleteSubject = true;
					break;
				case "Insertar Materiales":
					insertMaterial = true;
					break;
				case "Modificar Materiales":
					modifyMaterial = true;
					break;
				case "Eliminar Materiales":
					deleteMaterial = true;
					break;
				case "Insertar Idioma":
					insertLanguage = true;
					break;
				case "Modificar Idioma":
					modifyLanguage= true;
					break;
				case "Eliminar Idioma":
					deleteLanguage = true;
					break;
			}
		}
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public List<String> getNamesPrivileges(){
		return names;
	}

	public void setModifyUser(boolean modifyUser) {
		this.modifyUser = modifyUser;
	}



	public void setDeleteUser(boolean deleteUser) {
		this.deleteUser = deleteUser;
	}



	public void setInsertOtherUser(boolean insertOtherUser) {
		this.insertOtherUser = insertOtherUser;
	}



	public void setModifyOthersUsers(boolean modifyOthersUsers) {
		this.modifyOthersUsers = modifyOthersUsers;
	}



	public void setDeleteOthersUsers(boolean deleteOthersUsers) {
		this.deleteOthersUsers = deleteOthersUsers;
	}



	public void setPrivilegeEditing(boolean privilegeEditing) {
		this.privilegeEditing = privilegeEditing;
	}



	public void setPublicNotes(boolean publicNotes) {
		this.publicNotes = publicNotes;
	}



	public void setInsertTeachingGroup(boolean insertTeachingGroup) {
		this.insertTeachingGroup = insertTeachingGroup;
	}



	public void setModifyTeachingGroup(boolean modifyTeachingGroup) {
		this.modifyTeachingGroup = modifyTeachingGroup;
	}



	public void setDeleteTeachingGroup(boolean deleteTeachingGroup) {
		this.deleteTeachingGroup = deleteTeachingGroup;
	}



	public void setInsertResearchGroup(boolean insertResearchGroup) {
		this.insertResearchGroup = insertResearchGroup;
	}



	public void setModifyResearchGroup(boolean modifyResearchGroup) {
		this.modifyResearchGroup = modifyResearchGroup;
	}



	public void setDeleteResearchGroup(boolean deleteResearchGroup) {
		this.deleteResearchGroup = deleteResearchGroup;
	}



	public void setInsertTeachingCategory(boolean insertTeachingCategory) {
		this.insertTeachingCategory = insertTeachingCategory;
	}



	public void setModifyTeachingCategory(boolean modifyTeachingCategory) {
		this.modifyTeachingCategory = modifyTeachingCategory;
	}



	public void setDeleteTeachingCategory(boolean deleteTeachingCategory) {
		this.deleteTeachingCategory = deleteTeachingCategory;
	}



	public void setInsertScientificCategory(boolean insertScientificCategory) {
		this.insertScientificCategory = insertScientificCategory;
	}



	public void setModifyScientificCategory(boolean modifyScientificCategory) {
		this.modifyScientificCategory = modifyScientificCategory;
	}



	public void setDeleteScientificCategory(boolean deleteScientificCategory) {
		this.deleteScientificCategory = deleteScientificCategory;
	}



	public void setInsertMaterial(boolean insertMaterial) {
		this.insertMaterial = insertMaterial;
	}



	public void setModifyMaterial(boolean modifyMaterial) {
		this.modifyMaterial = modifyMaterial;
	}



	public void setDeleteMaterial(boolean deleteMaterial) {
		this.deleteMaterial = deleteMaterial;
	}



	public void setInsertTematic(boolean insertTematic) {
		this.insertTematic = insertTematic;
	}



	public void setModifyTematic(boolean modifyTematic) {
		this.modifyTematic = modifyTematic;
	}



	public void setDeleteTematic(boolean deleteTematic) {
		this.deleteTematic = deleteTematic;
	}



	public void setInsertSubject(boolean insertSubject) {
		this.insertSubject = insertSubject;
	}



	public void setModifySubject(boolean modifySubject) {
		this.modifySubject = modifySubject;
	}



	public void setDeleteSubject(boolean deleteSubject) {
		this.deleteSubject = deleteSubject;
	}



	public int getId() {
		return id;
	}

	public boolean isModifyUser() {
		return modifyUser;
	}

	public boolean isDeleteUser() {
		return deleteUser;
	}

	public boolean isInsertOtherUser() {
		return insertOtherUser;
	}

	public boolean isModifyOthersUsers() {
		return modifyOthersUsers;
	}

	public boolean isDeleteOthersUsers() {
		return deleteOthersUsers;
	}

	public boolean isPrivilegeEditing() {
		return privilegeEditing;
	}

	public boolean isPublicNotes() {
		return publicNotes;
	}

	public boolean isInsertTeachingGroup() {
		return insertTeachingGroup;
	}

	public boolean isModifyTeachingGroup() {
		return modifyTeachingGroup;
	}

	public boolean isDeleteTeachingGroup() {
		return deleteTeachingGroup;
	}

	public boolean isInsertResearchGroup() {
		return insertResearchGroup;
	}

	public boolean isModifyResearchGroup() {
		return modifyResearchGroup;
	}

	public boolean isDeleteResearchGroup() {
		return deleteResearchGroup;
	}

	public boolean isInsertTeachingCategory() {
		return insertTeachingCategory;
	}

	public boolean isModifyTeachingCategory() {
		return modifyTeachingCategory;
	}

	public boolean isDeleteTeachingCategory() {
		return deleteTeachingCategory;
	}

	public boolean isInsertScientificCategory() {
		return insertScientificCategory;
	}

	public boolean isModifyScientificCategory() {
		return modifyScientificCategory;
	}

	public boolean isDeleteScientificCategory() {
		return deleteScientificCategory;
	}

	public boolean isInsertMaterial() {
		return insertMaterial;
	}

	public boolean isModifyMaterial() {
		return modifyMaterial;
	}

	public boolean isDeleteMaterial() {
		return deleteMaterial;
	}

	public boolean isInsertTematic() {
		return insertTematic;
	}

	public boolean isModifyTematic() {
		return modifyTematic;
	}

	public boolean isDeleteTematic() {
		return deleteTematic;
	}

	public boolean isInsertSubject() {
		return insertSubject;
	}

	public boolean isModifySubject() {
		return modifySubject;
	}

	public boolean isDeleteSubject() {
		return deleteSubject;
	}

	public boolean isInsertLanguage() {
		return insertLanguage;
	}

	public void setInsertLanguage(boolean insertLanguage) {
		this.insertLanguage = insertLanguage;
	}

	public boolean isModifyLanguage() {
		return modifyLanguage;
	}

	public void setModifyLanguage(boolean modifyLanguage) {
		this.modifyLanguage = modifyLanguage;
	}

	public boolean isDeleteLanguage() {
		return deleteLanguage;
	}

	public void setDeleteLanguage(boolean deleteLanguage) {
		this.deleteLanguage = deleteLanguage;
	}
	
	
}
