package services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Privilege;
import utils.Connection;

public class PrivilegeServices {

	public static Privilege getPrivilege(int id) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerUsuarioRolPorUsuario\"(?) }";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setInt(2, id);
		
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		ArrayList<String> roles = new ArrayList<String>();
		while(rs.next()){
			roles.add(rs.getString("nombreRol"));
		}
		
		Privilege privilege = new Privilege(roles);	
		
		call.close();
		rs.close();
		
		return privilege;
	}
	
	public static List<String> getPrivilegeNames(int id) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerUsuarioRolPorUsuario\"(?) }";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setInt(2, id);
		
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		ArrayList<String> roles = new ArrayList<String>();
		while(rs.next()){
			roles.add(rs.getString("nombreRol"));
		}
		
		call.close();
		rs.close();
		
		return roles;
	}
}
