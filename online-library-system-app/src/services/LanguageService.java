package services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import models.Language;
import utils.Connection;

public class LanguageService {

	public static List<Language> getEnablesLanguages() throws SQLException{
		List<Language> langs = new LinkedList<Language>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerIdiomasHabilitados\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			langs.add(new Language(rs.getInt("idIdioma"), rs.getString("nombreIdioma"), false));
		}
		
		call.close();
		rs.close();
		
		return langs;
	}
	
	public static List<Language> getLanguages() throws SQLException{
		List<Language> langs = new LinkedList<Language>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerIdiomas\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			langs.add(new Language(rs.getInt("idIdioma"), rs.getString("nombreIdioma"), rs.getBoolean("cancelado")));
		}
		
		call.close();
		rs.close();
		
		return langs;
	}
}
