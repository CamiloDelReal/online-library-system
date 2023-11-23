package services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Rol;
import utils.Connection;

public class RolServices {	

	public static List<Rol> getRolesByUser(int idUser) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerRolesDeUsuario\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setInt(2, idUser);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		List<Rol> roles = new ArrayList<Rol>();
		while(rs.next())
			roles.add(new Rol(rs.getInt("idRol"), rs.getString("nombreRol"), rs.getBoolean("cancelado")));
		
		call.close();
		rs.close();
		
		return roles;
	}
	
	public static void deleteRolesOfUser(int idUser) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();		
		connection.setAutoCommit(false);
		
		String function = "{call \"EliminarUsuarioRolDeUnUsuario\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, idUser);
		call.execute();
		call.close();
	}
	
	public static void insertUserRol(int userId, int rolId) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();		
		connection.setAutoCommit(true);
		
		String function = "{call \"InsertarUsuarioRol\"(?, ?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, rolId);
		call.setInt(2, userId);
		call.execute();
		call.close();
	}
}
