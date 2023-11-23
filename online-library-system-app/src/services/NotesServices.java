package services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import utils.Connection;
import visual.controls.center.notesPane.Note;

public class NotesServices {
	public static int insertNote(int userId, String header, String body, String footer, boolean isPublic) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);
		
		String function = "{? = call \"InsertarNota\"(?,?,?,?,?)}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.INTEGER);
		call.setInt(2, userId);
		call.setString(3, header);
		call.setString(4, body);
		call.setString(5, footer);
		call.setBoolean(6, isPublic);
		call.execute();
		Integer value = (Integer)call.getObject(1);
		call.close();
		
		return value.intValue();
	}
	
	public static void modifyNote(int noteId, String header, String body, String footer, boolean isPublic) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);
		
		String function = "{ call \"ModificarNota\"(?,?,?,?,?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, noteId);
		call.setString(2, header);
		call.setString(3, body);
		call.setString(4, footer);
		call.setBoolean(5, isPublic);
		call.execute();
		call.close();

	}
	
	public static List<Note> getNotesByUser(int userId) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"ObtenerNotasDelUsuario\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setInt(2, userId);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		List<Note> notes = new LinkedList<Note>();
		
		while(rs.next()){
			notes.add(new Note(
					rs.getInt("idNota"), rs.getString("encabezado"),
					rs.getString("texto"), rs.getString("firma"),
					rs.getBoolean("publica")
					));
		}
		
		rs.close();
		call.close();
		
		return notes;
	}
	
	public static void deleteNote(int noteId) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);
		
		String function = "{ call \"EliminarNota\"(?) }";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, noteId);
		call.execute();
		call.close();
	}
	
	public static List<Note> getPublicNotes() throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"ObtenerNotasPublicas\"()}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		List<Note> notes = new LinkedList<Note>();
		
		while(rs.next()){
			notes.add(new Note(
					rs.getInt("idNota"), rs.getString("encabezado"),
					rs.getString("texto"), rs.getString("firma"),
					true
					));
		}
		
		rs.close();
		call.close();
		
		return notes;
	}
}
