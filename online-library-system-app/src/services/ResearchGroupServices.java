package services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import models.ResearchGroup;
import utils.Connection;

public class ResearchGroupServices {

	public static List<ResearchGroup> getEnabledResearchGroups() throws SQLException{
		List<ResearchGroup> researchGroups= new LinkedList<ResearchGroup>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerGruposInvestigativosHabilitados\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			researchGroups.add(new ResearchGroup(rs.getInt("idGrupoInvestigativo"), rs.getString("nombreGrupoInvestigativo"), false));
		}
		
		call.close();
		rs.close();
		
		return researchGroups;
	}
	
	public static List<ResearchGroup> getResearchGroups() throws SQLException{
		List<ResearchGroup> researchGroups= new LinkedList<ResearchGroup>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerGruposInvestigativos\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			researchGroups.add(new ResearchGroup(rs.getInt("idGrupoInvestigativo"), rs.getString("nombreGrupoInvestigativo"), rs.getBoolean("cancelado")));
		}
		
		call.close();
		rs.close();
		
		return researchGroups;
	}
	
	public static int getResearchGroupIdByName(String name) throws SQLException{
		int id = -1;
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerGrupoInvestigativoPorNombre\"(?) }";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setString(2, name);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		rs.first();
		
		id = rs.getInt("idGrupoInvestigativo");
		
		call.close();
		rs.close();
		
		return id;
	}
	
	public static void insertResearchGroup(boolean canceled, String name) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"InsertarGrupoInvestigativo\"(?, ?)}";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.setString(1, name);
		call.setBoolean(2, canceled);
		call.execute();
		
		call.close();
	}
	
	public static void modifyResearchGroup(int id, boolean canceled, String name) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"ModificarGrupoInvestigativo\"(?, ?, ?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, id);
		call.setString(2, name);
		call.setBoolean(3, canceled);
		call.execute();
		
		call.close();
	}
	
	public static void deleteResearchGroup(int id) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"EliminarGrupoInvestigativo\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, id);
		call.execute();
		
		call.close();
	}
}