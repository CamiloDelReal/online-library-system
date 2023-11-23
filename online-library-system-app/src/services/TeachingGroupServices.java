package services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import models.TeachingGroup;
import utils.Connection;

public class TeachingGroupServices {

	public static List<TeachingGroup> getEnabledTeachingGroups() throws SQLException{
		List<TeachingGroup> teachingGroups= new LinkedList<TeachingGroup>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerGruposDocentesHabilitados\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			teachingGroups.add(new TeachingGroup(rs.getInt("idGrupoDocente"), rs.getString("nombreGrupoDocente"), false));
		}
		
		call.close();
		rs.close();
		
		return teachingGroups;
	}
	
	public static List<TeachingGroup> getTeachingGroups() throws SQLException{
		List<TeachingGroup> teachingGroups= new LinkedList<TeachingGroup>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerGruposDocentes\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			teachingGroups.add(new TeachingGroup(rs.getInt("idGrupoDocente"), rs.getString("nombreGrupoDocente"), rs.getBoolean("cancelado")));
		}
		
		call.close();
		rs.close();
		
		return teachingGroups;
	}
	
	public static int getTeachingGroupIdByName(String name) throws SQLException{
		int id = -1;
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerGrupoDocentePorNombre\"(?) }";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setString(2, name);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		rs.first();
		
		id = rs.getInt("idGrupoDocente");
		
		call.close();
		rs.close();
		
		return id;
	}
	
	public static void insertTeachingGroup(boolean canceled, String name) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"InsertarGrupoDocente\"(?, ?)}";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.setString(1, name);
		call.setBoolean(2, canceled);
		call.execute();
		
		call.close();
	}
	
	public static void modifyTeachingGroup(int id, boolean canceled, String name) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"ModificarGrupoDocente\"(?, ?, ?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, id);
		call.setString(2, name);
		call.setBoolean(3, canceled);
		call.execute();
		
		call.close();
	}
	
	public static void deleteTeachingGroup(int id) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"EliminarGrupoDocente\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, id);
		call.execute();
		
		call.close();
	}
}
