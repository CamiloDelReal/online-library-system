package services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import models.Subject;
import utils.Connection;

public class SubjectServices {
	
	public static List<Subject> getEnabledSubject() throws SQLException{
		List<Subject> subjects = new LinkedList<Subject>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerAsignaturasHabilitadas\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			subjects.add(new Subject(rs.getInt("idAsignatura"), rs.getString("nombreAsignatura"), false));
		}
		
		call.close();
		rs.close();
		
		return subjects;
	}
	
	public static int getSubjectIdByName(String name) throws SQLException{
		int id = -1;
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerAsignaturaPorNombre\"(?) }";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setString(2, name);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		rs.first();
		
		id = rs.getInt("idAsignatura");
		
		call.close();
		rs.close();
		
		return id;
	}
	
	public static List<Subject> getEnabledSubjectByTematic(int tematic) throws SQLException{
		List<Subject> subjects = new LinkedList<Subject>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerAsignaturasHabilitadasPorTematica\"( ? ) }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setInt(2, tematic);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			subjects.add(new Subject(rs.getInt("idAsignatura"), rs.getString("nombreAsignatura"), false));
		}
		
		call.close();
		rs.close();
		
		return subjects;
	}
	
	public static List<Subject> getSubjectByTematic(int tematic) throws SQLException{
		List<Subject> subjects = new LinkedList<Subject>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerAsignaturasPorTematica\"( ? ) }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setInt(2, tematic);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			subjects.add(new Subject(rs.getInt("idAsignatura"), rs.getString("nombreAsignatura"), rs.getBoolean("cancelado")));
		}
		
		call.close();
		rs.close();
		
		return subjects;
	}
	
	public static int getAmountMaterialsInSubject(int subjectId) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ContarMaterialesEnAsignatura\"( ? ) }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.INTEGER);
		call.setInt(2, subjectId);
		call.execute();
		Integer count = (Integer)call.getObject(1);
		call.close();
		
		return count.intValue();
	}
	
	public static void insertSubject(String name, boolean canceled, int tematicId) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);
		
		String function = "{ call \"InsertarAsignatura\"(?, ?, ?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setString(1, name);
		call.setBoolean(2, canceled);
		call.setInt(3, tematicId);
		call.execute();
		
		call.close();
	}
	public static void modifySubject(int subjectId, String name, boolean canceled, int tematicId) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);
		
		String function = "{ call \"ModificarAsignatura\"(?, ?, ?, ?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, subjectId);
		call.setString(2, name);
		call.setBoolean(3, canceled);
		call.setInt(4, tematicId);
		call.execute();
		
		call.close();
	}
	
	public static void cancelSubject(int idSubject) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);
		
		String function = "{call \"CancelarAsignatura\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, idSubject);
		call.execute();
		
		call.close();
	}
	
	public static void enabledSubject(int idSubject) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);
		
		String function = "{call \"HabilitarAsignatura\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, idSubject);
		call.execute();
		
		call.close();
	}

	public static List<Subject> getSubject() throws SQLException {
		List<Subject> subjects = new LinkedList<Subject>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerAsignaturas\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			subjects.add(new Subject(rs.getInt("idAsignatura"), rs.getString("nombreAsignatura"), rs.getBoolean("cancelado")));
		}
		
		call.close();
		rs.close();
		
		return subjects;
	}
	
}
