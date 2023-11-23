package services;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import models.Privilege;
import models.Rol;
import models.users.MiniUser;
import models.users.SmallUser;
import models.users.Student;
import models.users.Teacher;
import models.users.User;
import utils.Connection;
import utils.Utilities;

public class UserServices {
	public static boolean isUserLogin(String user, String pass) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();		
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ElUsuarioEstaLogueado\"(?, ?) }";		
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.VARCHAR);
		call.setString(2, user);
		call.setString(3, Utilities.getMD5(pass));
		call.execute();
		Object type = (Object)call.getObject(1);
		call.close();
		
		boolean is = false;
		if(type != null && type.toString().equals("t"))
			is = true;
		
		return is;
	}
	
	public static void LoginUser(String user, String pass) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();		
		connection.setAutoCommit(true);
		
		String function = "{ call \"LoginUsuario\"(?, ?) }";
		CallableStatement call = connection.prepareCall(function);
		call.setString(1, user);
		call.setString(2, Utilities.getMD5(pass));
		call.execute();
		call.close();
	}
	
	public static void LogoutUser(int idUser) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();		
		connection.setAutoCommit(true);
		
		String function = "{ call \"LogoutUsuario\"(?) }";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, idUser);
		call.execute();
		call.close();
	}
	
	public static User getUserToLogin(String userName, String userPassword) throws SQLException, IOException{
		User user = null;
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerUsuarioParaLogueo\"(?, ?) }";		
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setString(2, userName);
		call.setString(3, Utilities.getMD5(userPassword));
		
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);

		if(rs.first()){
			int id = rs.getInt("idUsuario");
			long ci = rs.getLong("ci");
			String name = rs.getString("nombreUsuario");
			String firstLastName = rs.getString("apellido1");
			String secondLastName = rs.getString("apellido2");
			String nameUser = rs.getString("usuario");
			String password = rs.getString("password");
			boolean isCanceled = rs.getBoolean("cancelado");
			byte[] bytes = rs.getBytes("imagen");
			BufferedImage buffImg = null;
			if(bytes != null){
				Image awtImage = new ImageIcon(bytes).getImage();
				buffImg = Utilities.createBufferedImage(awtImage);
			}
			
			boolean isTeacher = rs.getBoolean("esProfesor");
			
			if(isTeacher){
				boolean teaches = rs.getBoolean("imparteAsignatura");
				String scientificCategory = rs.getString("nombreCategoriaCientifica");
				String teachingCategory = rs.getString("nombreCategoriaDocente");
				String subject = rs.getString("nombreAsignatura");
				
				user = new Teacher(id, ci, name, firstLastName, secondLastName, buffImg, isCanceled, nameUser, password, null, teaches, scientificCategory, teachingCategory, subject);
			}
			else{
				String teachingGroup = rs.getString("nombreGrupoDocente");
				String researchGroup = rs.getString("nombreGrupoInvestigativo");
				
				user = new Student(id, ci, name, firstLastName, secondLastName, buffImg, isCanceled, nameUser, password, null, teachingGroup, researchGroup);
			}
			
			call.close();
			rs.close();
			
			Privilege privilege = PrivilegeServices.getPrivilege(id);
			
			user.setPrivilege(privilege);
		}
		
		call.close();
		rs.close();
		
		return user;
	}
	
	public static User getUser(int userId) throws SQLException, IOException{
		User user = null;
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerUsuario\"( ? ) }";		
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setInt(2, userId);
		
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);

		if(rs.first()){
			//int id = rs.getInt("idUsuario");
			long ci = rs.getLong("ci");
			String name = rs.getString("nombreUsuario");
			String firstLastName = rs.getString("apellido1");
			String secondLastName = rs.getString("apellido2");
			String nameUser = rs.getString("usuario");
			String password = rs.getString("password");
			boolean isCanceled = rs.getBoolean("cancelado");
			byte[] bytes = rs.getBytes("imagen");
			BufferedImage buffImg = null;
			if(bytes != null){
				Image awtImage = new ImageIcon(bytes).getImage();
				buffImg = Utilities.createBufferedImage(awtImage);
			}
			
			boolean isTeacher = rs.getBoolean("esProfesor");
			
			if(isTeacher){
				boolean teaches = rs.getBoolean("imparteAsignatura");
				String scientificCategory = rs.getString("nombreCategoriaCientifica");
				String teachingCategory = rs.getString("nombreCategoriaDocente");
				String subject = rs.getString("nombreAsignatura");
				
				user = new Teacher(userId, ci, name, firstLastName, secondLastName, buffImg, isCanceled, nameUser, password, null, teaches, scientificCategory, teachingCategory, subject);
			}
			else{
				String teachingGroup = rs.getString("nombreGrupoDocente");
				String researchGroup = rs.getString("nombreGrupoInvestigativo");
				
				user = new Student(userId, ci, name, firstLastName, secondLastName, buffImg, isCanceled, nameUser, password, null, teachingGroup, researchGroup);
			}
			
			call.close();
			rs.close();
			
		}		
		
		return user;
	}
	
	public static void modifyUser(int id, long ci, String name,
								  String firstLastName, String secondLastName,
								  File picture, String user, String password,
								  boolean isTeacher, boolean teaches, boolean canceled,
								  int scientificCategory, int teachingCategory, int subject,
								  int teachingGroup, int researchGroup, List<Rol> roles,
								  boolean validPicture) throws SQLException, FileNotFoundException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function = "{call \"ModificarUsuario\"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, id);
		call.setLong(2, ci);
		call.setString(3, name);
		call.setString(4, firstLastName);
		call.setString(5, secondLastName);
		
		if(picture == null)
			call.setBytes(6, null);
		else{
			FileInputStream fis = new FileInputStream(picture);
			call.setBinaryStream(6, fis, (int)picture.length());
		}
		call.setString(7, user);
		call.setString(8, Utilities.getMD5(password));
		call.setBoolean(9, isTeacher);
		call.setBoolean(10, teaches);
		call.setInt(11, scientificCategory);
		call.setInt(12, teachingCategory);
		call.setInt(13, subject);
		call.setInt(14, teachingGroup);
		call.setInt(15, researchGroup);
		call.setBoolean(16, canceled);
		call.setBoolean(17, validPicture);
		call.execute();
		
		if(roles != null){
			RolServices.deleteRolesOfUser(id);
			for(Rol rol : roles)
				RolServices.insertUserRol(id, rol.getId());
		}
		
		call.close();
	}
	
	public static void cancelUser(int id) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function = "{ call \"CancelarUsuario\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, id);
		call.execute();
		
		call.close();
	}
	
	public static void insertUser(long ci, String name,
			  String firstLastName, String secondLastName,
			  File picture, String user, String password,
			  boolean isTeacher, boolean teaches,
			  int scientificCategory, int teachingCategory, int subject,
			  int teachingGroup, int researchGroup,
			  List<Rol> roles, boolean canceled) throws SQLException, FileNotFoundException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function = "{? = call \"InsertarUsuario\"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.INTEGER);
		call.setLong(2, ci);
		call.setString(3, name);
		call.setString(4, firstLastName);
		call.setString(5, secondLastName);
		call.setString(6, user);
		call.setString(7, password);
		if(picture == null)
		call.setBytes(8, null);
		else{
		FileInputStream fis = new FileInputStream(picture);
		call.setBinaryStream(8, fis, (int)picture.length());
		}
		call.setBoolean(9, isTeacher);
		call.setBoolean(10, teaches);
		call.setInt(11, scientificCategory);
		call.setInt(12, teachingCategory);
		call.setInt(13, subject);
		call.setInt(14, teachingGroup);
		call.setInt(15, researchGroup);
		call.setBoolean(16, canceled);
		call.execute();
		Integer id = (Integer)call.getObject(1);
		
		for(Rol rol : roles)
			RolServices.insertUserRol(id, rol.getId());
		
		call.close();
	}

	
	public static List<SmallUser> getSmallUserDistincTo(int userId) throws SQLException, IOException{
		List<SmallUser> users = new LinkedList<SmallUser>();
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerUsuariosDistintosA\"( ? ) }";		
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setInt(2, userId);
		
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);

		while(rs.next()){
			int id = rs.getInt("idUsuario");
			long ci = rs.getLong("ci"); 
			String name = rs.getString("nombreUsuario");
			String firstLastName = rs.getString("apellido1");
			String secondLastName = rs.getString("apellido2");
			boolean isCanceled = rs.getBoolean("cancelado");
			boolean isTeacher = rs.getBoolean("esProfesor");
			byte[] bytes = rs.getBytes("imagen");
			BufferedImage buffImg = null;
			if(bytes != null){
				Image awtImage = new ImageIcon(bytes).getImage();
				buffImg = Utilities.createBufferedImage(awtImage);
			}

			boolean male = false;
			if(Integer.valueOf(String.valueOf(ci).substring(9,10)) % 2 == 0)
				male = true;			
			
			SmallUser u = new SmallUser(id, name + " " + firstLastName + " " + secondLastName,
					buffImg, isCanceled, isTeacher, male);
			
			users.add(u);
		}
		
		call.close();
		rs.close();
		
		return users;
	}
	
	public static List<MiniUser> getTeacherNames() throws SQLException{
		List<MiniUser> users = new LinkedList<MiniUser>();
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerTeacher\"( ) }";		
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);

		while(rs.next()){
			int id = rs.getInt("idUsuario");
			String name = rs.getString("nombreUsuario");
			String firstLastName = rs.getString("apellido1");
			String secondLastName = rs.getString("apellido2");
			MiniUser u = new MiniUser(id, name + " " + firstLastName + " " + secondLastName);
			users.add(u);
		}
		
		call.close();
		rs.close();
		
		return users;
	}
	
	public static List<MiniUser> getEnabledTeacherNames() throws SQLException{
		List<MiniUser> users = new LinkedList<MiniUser>();
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerTeacherHabilitados\"( ) }";		
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);

		while(rs.next()){
			int id = rs.getInt("idUsuario");
			String name = rs.getString("nombreUsuario");
			String firstLastName = rs.getString("apellido1");
			String secondLastName = rs.getString("apellido2");
			MiniUser u = new MiniUser(id, name + " " + firstLastName + " " + secondLastName);
			users.add(u);
		}
		
		call.close();
		rs.close();
		
		return users;
	}
}
