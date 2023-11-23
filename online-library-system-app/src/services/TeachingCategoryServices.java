package services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import models.TeachingCategory;
import utils.Connection;

public class TeachingCategoryServices {
	
	public static List<TeachingCategory> getEnabledTeachingCategory() throws SQLException{
		List<TeachingCategory> teachingCats = new LinkedList<TeachingCategory>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerCategoriasDocentesHabilitadas\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			teachingCats.add(new TeachingCategory(rs.getInt("idCategoriaDocente"), rs.getString("nombreCategoriaDocente"), false));
		}
		
		call.close();
		rs.close();
		
		return teachingCats;
	}
	
	public static List<TeachingCategory> getTeachingCategory() throws SQLException{
		List<TeachingCategory> teachingCats = new LinkedList<TeachingCategory>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerCategoriasDocentes\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			teachingCats.add(new TeachingCategory(rs.getInt("idCategoriaDocente"), rs.getString("nombreCategoriaDocente"), rs.getBoolean("cancelado")));
		}
		
		call.close();
		rs.close();
		
		return teachingCats;
	}
	
	public static int getTeachingCategoryIdByName(String name) throws SQLException{
		int id = -1;
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerCategoriaDocentePorNombre\"(?) }";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setString(2, name);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		rs.first();
		
		id = rs.getInt("idCategoriaDocente");
		
		call.close();
		rs.close();
		
		return id;
	}
	
	public static void insertTeachingCategory(boolean canceled, String name) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"InsertarCategoriaDocente\"(?, ?)}";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.setString(1, name);
		call.setBoolean(2, canceled);
		call.execute();
		
		call.close();
	}
	
	public static void modifyTeachingCategory(int id, boolean canceled, String name) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"ModificarCategoriaDocente\"(?, ?, ?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, id);
		call.setString(2, name);
		call.setBoolean(3, canceled);
		call.execute();
		
		call.close();
	}
	
	public static void deleteTeachingCategory(int id) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"EliminarCategoriaDocente\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, id);
		call.execute();
		
		call.close();
	}
}
