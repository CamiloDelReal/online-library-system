package services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import models.materials.Book;
import models.materials.InternetArticle;
import models.materials.Magazine;
import models.materials.Material;
import models.materials.MaterialWithProperty;
import models.materials.Thesis;
import utils.Connection;

public class MaterialServices {
	public static final int BOOK = 1;
	public static final int MAGAZINE = 2;
	public static final int INTERNET_ARTICLE = 3;
	public static final int THESIS = 4;
	public static final int MONOGRAPHY = 5;
	public static final int LESSON = 6;
	public static final int OTHER = 7;
	
	public static int getSubjectOfMaterial(int materialId) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"ObtenerAsignaturaDelMaterial\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.INTEGER);
		call.setInt(2, materialId);
		call.execute();
		Integer value = (Integer)call.getObject(1);
		
		call.close();
		
		return value.intValue();
	}
	
	public static List<Material> getEnabledMaterialBySubject(int idSubject) throws SQLException{
		List<Material> materials = new LinkedList<Material>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerMaterialesHabilitadosPorAsignatura\"( ? ) }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setInt(2, idSubject);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			materials.add(new Material(
					rs.getInt("codMaterial"), rs.getString("ubicacionRed"), rs.getString("titulo"),
					rs.getString("autor"), rs.getInt("anyoPublicacion"), rs.getString("palabrasClaves"),
					rs.getString("nombreIdioma"), rs.getInt("visitadoPorEstudiantes"),
					rs.getInt("visitadoPorProfesores"), rs.getInt("visitadoPorDesconocidos"),
					rs.getString("descripcion"), false
					));
		}
		
		call.close();
		rs.close();
		
		return materials;
	}
	
	public static List<Material> getMaterialBySubject(int idSubject) throws SQLException{
		List<Material> materials = new LinkedList<Material>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerMaterialesPorAsignatura\"( ? ) }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setInt(2, idSubject);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			materials.add(new Material(
					rs.getInt("codMaterial"), rs.getString("ubicacionRed"), rs.getString("titulo"),
					rs.getString("autor"), rs.getInt("anyoPublicacion"), rs.getString("palabrasClaves"),
					rs.getString("nombreIdioma"), rs.getInt("visitadoPorEstudiantes"),
					rs.getInt("visitadoPorProfesores"), rs.getInt("visitadoPorDesconocidos"),
					rs.getString("descripcion"), rs.getBoolean("cancelado")
					));
		}
		
		call.close();
		rs.close();
		
		return materials;
	}
	
	public static String getTypeOfReference(int code) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerTipoDeReferenciaDelMaterial\"( ? ) }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.VARCHAR);
		call.setInt(2, code);
		call.execute();
		String type = (String)call.getObject(1);
		call.close();
		
		return type;
	}
	
		
	public static void cancelMaterial(int idMaterial) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);
		
		String function = "{call \"CancelarMaterial\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, idMaterial);
		call.execute();
		
		call.close();
	}
	
	public static void enabledMaterial(int idMaterial) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);
		
		String function = "{call \"HabilitarMaterial\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, idMaterial);
		call.execute();
		
		call.close();
	}
	
	public static List<Material> getMoreVisited16() throws SQLException{
		List<Material> materials = new LinkedList<Material>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"MaterialesMasVisitados16\"()}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			materials.add(new Material(
					rs.getInt("codMaterial"), rs.getString("ubicacionRed"), rs.getString("titulo"),
					rs.getString("autor"), rs.getInt("anyoPublicacion"), rs.getString("palabrasClaves"),
					rs.getString("nombreIdioma"), rs.getInt("visitadoPorEstudiantes"),
					rs.getInt("visitadoPorProfesores"), rs.getInt("visitadoPorDesconocidos"),
					rs.getString("descripcion"), false
					));
		}
		
		call.close();
		rs.close();
		
		return materials;
	}
	
	public static List<Material> getMoreVisited4() throws SQLException{
		List<Material> materials = new LinkedList<Material>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"MaterialesMasVisitados4\"()}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			materials.add(new Material(
					rs.getInt("codMaterial"), rs.getString("ubicacionRed"), rs.getString("titulo"),
					rs.getString("autor"), rs.getInt("anyoPublicacion"), rs.getString("palabrasClaves"),
					rs.getString("nombreIdioma"), rs.getInt("visitadoPorEstudiantes"),
					rs.getInt("visitadoPorProfesores"), rs.getInt("visitadoPorDesconocidos"),
					rs.getString("descripcion"), false
					));
		}
		
		call.close();
		rs.close();
		
		return materials;
	}

	public static List<Material> getLastInserted16() throws SQLException {
		List<Material> materials = new LinkedList<Material>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"UltimosMaterialesInsertados16\"()}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			materials.add(new Material(
					rs.getInt("codMaterial"), rs.getString("ubicacionRed"), rs.getString("titulo"),
					rs.getString("autor"), rs.getInt("anyoPublicacion"), rs.getString("palabrasClaves"),
					rs.getString("nombreIdioma"), rs.getInt("visitadoPorEstudiantes"),
					rs.getInt("visitadoPorProfesores"), rs.getInt("visitadoPorDesconocidos"),
					rs.getString("descripcion"), false
					));
		}
		
		call.close();
		rs.close();
		
		return materials;
	}
	
	public static List<Material> getLastInserted3() throws SQLException {
		List<Material> materials = new LinkedList<Material>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"UltimosMaterialesInsertados3\"()}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			materials.add(new Material(
					rs.getInt("codMaterial"), rs.getString("ubicacionRed"), rs.getString("titulo"),
					rs.getString("autor"), rs.getInt("anyoPublicacion"), rs.getString("palabrasClaves"),
					rs.getString("nombreIdioma"), rs.getInt("visitadoPorEstudiantes"),
					rs.getInt("visitadoPorProfesores"), rs.getInt("visitadoPorDesconocidos"),
					rs.getString("descripcion"), false
					));
		}
		
		call.close();
		rs.close();
		
		return materials;
	}
	
	public static Material getMaterialByID(int materialID) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"MaterialPorID\"(?)}";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setInt(2, materialID);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		Material material = null;
		if(rs.first()){
			int type = rs.getInt("tipo");
			
			switch(type){
				case BOOK:
					material = new Book(
							rs.getInt("codMaterial"), rs.getString("ubicacionRed"), rs.getString("titulo"),
							rs.getString("autor"), rs.getInt("anyoPublicacion"), rs.getString("palabrasClaves"),
							rs.getString("nombreIdioma"), rs.getInt("visitadoPorEstudiantes"),
							rs.getInt("visitadoPorProfesores"), rs.getInt("visitadoPorDesconocidos"),
							rs.getString("descripcion"), rs.getBoolean("cancelado"),
							rs.getInt("numEdicion"), rs.getString("isbn")
							);
					break;
				case MAGAZINE:
					material = new Magazine(
							rs.getInt("codMaterial"), rs.getString("ubicacionRed"), rs.getString("titulo"),
							rs.getString("autor"), rs.getInt("anyoPublicacion"), rs.getString("palabrasClaves"),
							rs.getString("nombreIdioma"), rs.getInt("visitadoPorEstudiantes"),
							rs.getInt("visitadoPorProfesores"), rs.getInt("visitadoPorDesconocidos"),
							rs.getString("descripcion"), rs.getBoolean("cancelado"),
							rs.getInt("numero"), rs.getInt("volumen"), rs.getString("issn")
							);
					break;
				case INTERNET_ARTICLE:
					material = new InternetArticle(
							rs.getInt("codMaterial"), rs.getString("ubicacionRed"), rs.getString("titulo"),
							rs.getString("autor"), rs.getInt("anyoPublicacion"), rs.getString("palabrasClaves"),
							rs.getString("nombreIdioma"), rs.getInt("visitadoPorEstudiantes"),
							rs.getInt("visitadoPorProfesores"), rs.getInt("visitadoPorDesconocidos"),
							rs.getString("descripcion"), rs.getBoolean("cancelado"),
							rs.getString("direccion"), rs.getString("fecha")
							);
					break;
				case THESIS:
					material = new Thesis(
							rs.getInt("codMaterial"), rs.getString("ubicacionRed"), rs.getString("titulo"),
							rs.getString("autor"), rs.getInt("anyoPublicacion"), rs.getString("palabrasClaves"),
							rs.getString("nombreIdioma"), rs.getInt("visitadoPorEstudiantes"),
							rs.getInt("visitadoPorProfesores"), rs.getInt("visitadoPorDesconocidos"),
							rs.getString("descripcion"), rs.getBoolean("cancelado"),
							rs.getInt("ctdadPaginas"), rs.getString("grado"), rs.getString("universidad")
							);
					break;
				case MONOGRAPHY:
				case LESSON:
				case OTHER:
					material = new Material(
							rs.getInt("codMaterial"), rs.getString("ubicacionRed"), rs.getString("titulo"),
							rs.getString("autor"), rs.getInt("anyoPublicacion"), rs.getString("palabrasClaves"),
							rs.getString("nombreIdioma"), rs.getInt("visitadoPorEstudiantes"),
							rs.getInt("visitadoPorProfesores"), rs.getInt("visitadoPorDesconocidos"),
							rs.getString("descripcion"), rs.getBoolean("cancelado")
							);
					break;
			}			
		}
		
		call.close();
		rs.close();
		
		return material;
	}
	
	public static void insertMaterial(String location, String title, String author, int publication,
							   String keys, int language, int type, String description,
							   int edition, String isbn,
							   int number, int volume, String issn,
							   String webAddress, String date,
							   String university, int pages, String degree,
							   int subject, int teacher) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);
		
		String function = "{call \"InsertarMaterial\"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
		CallableStatement call = connection.prepareCall(function);
		//Material
		call.setString(1, location);
		call.setString(2, title);
		call.setString(3, author);
		call.setInt(4, publication);
		call.setString(5, keys);
		call.setInt(6, language);
		call.setInt(7, type);
		call.setString(8, description);
		//Libro
		call.setShort(9, (short)edition);
		call.setString(10, isbn);
		//Revista
		call.setInt(11, number);
		call.setShort(12, (short)volume);
		call.setString(13, issn);
		//Articulo Internet
		call.setString(14, webAddress);
		call.setString(15, date);
		//Tesis
		call.setString(16, university);
		call.setInt(17, pages);
		call.setString(18, degree);
		
		call.setInt(19, subject);
		call.setInt(20, teacher);
		
		call.execute();
		
		call.close();
	}
	
	public static void modifyMaterial(int code, String location, String title, String author, int publication,
				   String keys, int language, int type, String description,
				   int edition, String isbn,
				   int number, int volume, String issn,
				   String webAddress, String date,
				   String university, int pages, String degree,
				   boolean cancelado) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);
		
		String function = "{call \"ModificarMaterial\"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, code);
		//Material
		call.setString(2, location);
		call.setString(3, title);
		call.setString(4, author);
		call.setInt(5, publication);
		call.setString(6, keys);
		call.setInt(7, language);
		call.setInt(8, type);
		call.setString(9, description);
		//Libro
		call.setShort(10, (short)edition);
		call.setString(11, isbn);
		//Revista
		call.setInt(12, number);
		call.setShort(13, (short)volume);
		call.setString(14, issn);
		//Articulo Internet
		call.setString(15, webAddress);
		call.setString(16, date);
		//Tesis
		call.setString(17, university);
		call.setInt(18, pages);
		call.setString(19, degree);
		call.setBoolean(20, cancelado);
		
		call.execute();
		
		call.close();
	}
	
	public static void increseVisitedByStudent(int codMaterial) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);
		
		String function = "{call \"AumentarVisitadoPorEstudiante\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, codMaterial);
		call.execute();
		call.close();
	}
	
	public static void increseVisitedByTeacher(int codMaterial) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);

		String function = "{call \"AumentarVisitadoPorProfesor\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, codMaterial);
		call.execute();
		call.close();
	}

	public static void increseVisitedByUnknow(int codMaterial) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(true);
		
		String function = "{call \"AumentarVisitadoPorDesconocidos\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, codMaterial);
		call.execute();
		call.close();
	}
	
	public static List<String> getAuthorsOfMaterials() throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"ObtenerAutoresDeMateriales\"()}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		List<String> names = new LinkedList<String>();
		while(rs.next())
			names.add(rs.getString("autor"));
		
		rs.close();
		call.close();
		
		return names;
	}
	
	public static List<String> getAuthorsOfMaterialsEnabled() throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"ObtenerAutoresDeMaterialesHabilitados\"()}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		List<String> names = new LinkedList<String>();
		while(rs.next())
			names.add(rs.getString("autor"));
		
		rs.close();
		call.close();
		
		return names;
	}
	
	public static void materialsByTematic(int tematic, String tematicName) throws SQLException, FileNotFoundException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"MaterialesDeUnaTematica\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setInt(2,tematic);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
				
		File textFile = new File ("src/reports/materialesDeUnaTematicaAgrupadosPorAsignatura.txt");
		PrintWriter pw = new PrintWriter(textFile);
		pw.println("\t\t Temática: \t" + tematicName);
		
		String nameSubject = " ";
		while(rs.next()){
			String subject = rs.getString("nombreAsignatura");
			if(!nameSubject.equals(subject)){
				pw.println();
				pw.println("\t Asignatura: \t" + subject);
				nameSubject = subject;
			}
			pw.println( rs.getString("titulo")+"  "+ rs.getString("autor")+"  " + rs.getInt("anyoPublicacion")+"  " + rs.getString("ubicacionRed") );
			
		}
		pw.close();		
	}
	
	public static List<MaterialWithProperty> getMaterialByTitle(String title) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"MaterialesPorTitulo\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setString(2,title);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		List<MaterialWithProperty> list = new LinkedList<MaterialWithProperty>();
		while (rs.next()){
			list.add(new MaterialWithProperty(rs.getString("titulo"), rs.getString("autor"), rs.getInt("codMaterial"), rs.getString("nombreTipoMaterial"), rs.getInt("idAsignatura"), rs.getInt("tematica"),""));
		}
		rs.close();
		call.close();
		return list;
	}
	
	public static List<MaterialWithProperty> getMaterialByAuthor(String autor) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"MaterialesPorAutor\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setString(2,autor);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		List<MaterialWithProperty> list = new LinkedList<MaterialWithProperty>();
		while (rs.next()){
			list.add(new MaterialWithProperty(rs.getString("titulo"), rs.getString("autor"), rs.getInt("codMaterial"), rs.getString("nombreTipoMaterial"), rs.getInt("idAsignatura"), rs.getInt("tematica"),""));
		}
		rs.close();
		call.close();
		return list;
	}
	
	public static List<MaterialWithProperty> getMaterialEnabledlByAuthor(String autor) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"MaterialesHabilitadosPorAutor\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setString(2,autor);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		List<MaterialWithProperty> list = new LinkedList<MaterialWithProperty>();
		while (rs.next()){
			list.add(new MaterialWithProperty(rs.getString("titulo"), rs.getString("autor"), rs.getInt("codMaterial"), rs.getString("nombreTipoMaterial"), rs.getInt("idAsignatura"), rs.getInt("tematica"),""));
		}
		rs.close();
		call.close();
		return list;
	}
	
	public static List<MaterialWithProperty> getMaterialEnabledByTitle(String title) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"MaterialesHabilitadosPorTitulo\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setString(2,title);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		List<MaterialWithProperty> list = new LinkedList<MaterialWithProperty>();
		while (rs.next()){
			list.add(new MaterialWithProperty(rs.getString("titulo"), rs.getString("autor"), rs.getInt("codMaterial"), rs.getString("nombreTipoMaterial"), rs.getInt("idAsignatura"), rs.getInt("tematica"),""));
		}
		rs.close();
		call.close();
		return list;
	}
	
	public static List<MaterialWithProperty> getMaterialWithKeyWord(String title) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"MaterialesPorPalabrasClaves\"()}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		List<MaterialWithProperty> list = new LinkedList<MaterialWithProperty>();
		while (rs.next()){
			list.add(new MaterialWithProperty(rs.getString("titulo"), rs.getString("autor"), rs.getInt("codMaterial"), rs.getString("nombreTipoMaterial"), rs.getInt("idAsignatura"), rs.getInt("tematica"),rs.getString("palabrasClaves")));
		}
		rs.close();
		call.close();
		return list;
	}
	
	public static List<MaterialWithProperty> getMaterialEnabledWithKeyWord(String title) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"MaterialesHabilitadosPorPalabrasClaves\"()}";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		List<MaterialWithProperty> list = new LinkedList<MaterialWithProperty>();
		while (rs.next()){
			list.add(new MaterialWithProperty(rs.getString("titulo"), rs.getString("autor"), rs.getInt("codMaterial"), rs.getString("nombreTipoMaterial"), rs.getInt("idAsignatura"), rs.getInt("tematica"),rs.getString("palabrasClaves")));
		}
		rs.close();
		call.close();
		return list;
	}
	
}
