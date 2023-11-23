package services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import models.ScientificCategory;
import utils.Connection;

public class ScientificCategoryServices {

	public static List<ScientificCategory> getEnabledTeachingCategory() throws SQLException{
		List<ScientificCategory> scientificCats = new LinkedList<ScientificCategory>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerCategoriasCientificasHabilitadas\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			scientificCats.add(new ScientificCategory(rs.getInt("idCategoriaCientifica"), rs.getString("nombreCategoriaCientifica"), false));
		}
		
		call.close();
		rs.close();
		
		return scientificCats;
	}
	
	public static List<ScientificCategory> getTeachingCategory() throws SQLException{
		List<ScientificCategory> scientificCats = new LinkedList<ScientificCategory>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerCategoriasCientificas\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			scientificCats.add(new ScientificCategory(rs.getInt("idCategoriaCientifica"), rs.getString("nombreCategoriaCientifica"), rs.getBoolean("cancelado")));
		}
		
		call.close();
		rs.close();
		
		return scientificCats;
	}
	
	public static int getScientificCategoryIdByName(String name) throws SQLException{
		int id = -1;
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerCategoriaCientificaPorNombre\"(?) }";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setString(2, name);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		rs.first();
		
		id = rs.getInt("idCategoriaCientifica");
		
		call.close();
		rs.close();
		
		return id;
	}
	
	public static void insertScientificCategory(boolean canceled, String name) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"InsertarCategoriaCientifica\"(?, ?)}";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.setString(1, name);
		call.setBoolean(2, canceled);
		call.execute();
		
		call.close();
	}
	
	public static void modifyScientificCategory(int id, boolean canceled, String name) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"ModificarCategoriaCientifica\"(?, ?, ?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, id);
		call.setString(2, name);
		call.setBoolean(3, canceled);
		call.execute();
		
		call.close();
	}
	
	public static void deleteScientificCategory(int id) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"EliminarCategoriaCientifica\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, id);
		call.execute();
		
		call.close();
	}
}