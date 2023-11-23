package services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import models.Tematic;
import utils.Connection;

public class TematicServices {
	
	public static List<Tematic> getEnabledTematics() throws SQLException{
		List<Tematic> tematics = new LinkedList<Tematic>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerTematicasHabilitadas\"()}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next())
			tematics.add(new Tematic(rs.getInt("idTematica"), rs.getString("nombreTematica"), false));
		
		call.close();
		rs.close();
		
		return tematics;
	}
	
	public static List<Tematic> getTematics() throws SQLException{
		List<Tematic> tematics = new LinkedList<Tematic>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerTematicas\"()}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next())
			tematics.add(new Tematic(rs.getInt("idTematica"), rs.getString("nombreTematica"), rs.getBoolean("cancelado")));
		
		call.close();
		rs.close();
		
		return tematics;
	}
	
	public static int getAmountSubjectInTematic(int tematicId) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"ContarAsignaturasEnTematica\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.INTEGER);
		call.setInt(2, tematicId);
		call.execute();
		Integer amount = (Integer)call.getObject(1);
		call.close();
		
		return amount.intValue();
	}
	
	public static int getAmountMaterialInTematic(int tematicId) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"ContarMaterialesEnTematica\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.INTEGER);
		call.setInt(2, tematicId);
		call.execute();
		Integer amount = (Integer)call.getObject(1);
		call.close();
		
		return amount.intValue();
	}
	
	public static void insertTematic(String name, boolean canceled) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);
		
		String function = "{ call \"InsertarTematica\"(?, ?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setString(1, name);
		call.setBoolean(2, canceled);
		call.execute();
		
		call.close();
	}
	
	public static void modifyTematic(int idTematic, String name, boolean canceled) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);
		
		String function = "{ call \"ModificarTematica\"(?, ?, ?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, idTematic);
		call.setString(2, name);
		call.setBoolean(3, canceled);
		call.execute();
		
		call.close();
	}
	
	public static void cancelTematic(int idTematic) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);
		
		String function = "{call \"CancelarTematica\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, idTematic);
		call.execute();
		
		call.close();
	}
	
	public static void enabledTematic(int idTematic) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);
		
		String function = "{call \"HabilitarTematica\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, idTematic);
		call.execute();
		
		call.close();
	}
	
}
