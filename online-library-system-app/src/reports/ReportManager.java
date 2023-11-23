package reports;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.view.JasperViewer;
import utils.DialogBox;

public class ReportManager {
	private static ReportManager manager = null;
	public static ReportManager getInstance(){
		if(manager == null)
			manager = new ReportManager();
		return manager;
	}
	
	private static java.sql.Connection myConnection = null;
	private static final String ADDRESS_TO_SERVER = "jdbc:postgresql://localhost:5432/SistemaGLR";
	private static final String USER_NAME = "postgres";
	private static final String USER_PASSWORD = "postgres";

	private ReportManager(){
		try{
			myConnection = DriverManager.getConnection(ADDRESS_TO_SERVER, USER_NAME, USER_PASSWORD);
		} catch(SQLException e){
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
	
	public void materialOrderByYearAndGroupByType(){
		try{
			Class.forName("org.postgresql.Driver");
			@SuppressWarnings("rawtypes")
			HashMap parametros = new HashMap();
			//parametros.put("user", f.getText());
			JasperFillManager.fillReportToFile("src/reports/MaterialesOrdenadosPorAnyoYAgrupadosPorTipo.jasper", parametros, myConnection);
			JasperViewer.viewReport("src/reports/MaterialesOrdenadosPorAnyoYAgrupadosPorTipo.jrprint", false);
		} catch (ClassNotFoundException | JRException e){
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
	
	public void materialOrderByTitleAndGroupByType(){
		try{
			Class.forName("org.postgresql.Driver");
			@SuppressWarnings("rawtypes")
			HashMap parametros = new HashMap();
			//parametros.put("user", f.getText());
			JasperFillManager.fillReportToFile("src/reports/MaterialesOrdenadosPorTituloYAgrupadosPorTipo.jasper", parametros, myConnection);
			JasperViewer.viewReport("src/reports/MaterialesOrdenadosPorTituloYAgrupadosPorTipo.jrprint", false);
		} catch (ClassNotFoundException | JRException e){
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
	
	public void visitForEachTypeOfMaterial(){
		try{
			Class.forName("org.postgresql.Driver");
			@SuppressWarnings("rawtypes")
			HashMap parametros = new HashMap();
			//parametros.put("user", f.getText());
			JasperFillManager.fillReportToFile("src/reports/VisitasACadaTipoDeMaterial.jasper", parametros, myConnection);
			JasperViewer.viewReport("src/reports/VisitasACadaTipoDeMaterial.jrprint", false);
		} catch (ClassNotFoundException | JRException e){
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
	
	public void countMeterialInTematics(){
		try{
			Class.forName("org.postgresql.Driver");
			@SuppressWarnings("rawtypes")
			HashMap parametros = new HashMap();
			//parametros.put("user", f.getText());
			JasperFillManager.fillReportToFile("src/reports/CantidadDeMaterialesPorTematica.jasper", parametros, myConnection);
			JasperViewer.viewReport("src/reports/CantidadDeMaterialesPorTematica.jrprint", false);
		} catch (ClassNotFoundException | JRException e){
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void materialByAuthor(String author){
		try{
			Class.forName("org.postgresql.Driver");			
			HashMap parametros = new HashMap();
			parametros.put("autor", author);
			JasperFillManager.fillReportToFile("src/reports/MaterialesDeUnAutorDado.jasper", parametros, myConnection);
			JasperViewer.viewReport("src/reports/MaterialesDeUnAutorDado.jrprint", false);
		} catch (ClassNotFoundException | JRException e){
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void materialSugestedByTeacher(int idTeacher){
		try{
			Class.forName("org.postgresql.Driver");			
			HashMap parametros = new HashMap();
			parametros.put("profesor", idTeacher);
			JasperFillManager.fillReportToFile("src/reports/MaterialesSugeridosPorUnProfesor.jasper", parametros, myConnection);
			JasperViewer.viewReport("src/reports/MaterialesSugeridosPorUnProfesor.jrprint", false);
		} catch (ClassNotFoundException | JRException e){
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void subjectWithMoreCountMaterial(int countSubjects){
		try{
			Class.forName("org.postgresql.Driver");			
			HashMap parametros = new HashMap();
			parametros.put("cantidad", countSubjects);
			JasperFillManager.fillReportToFile("src/reports/AsignaturasConUnaCantidadDeMaterialesMayorQue.jasper", parametros, myConnection);
			JasperViewer.viewReport("src/reports/AsignaturasConUnaCantidadDeMaterialesMayorQue.jrprint", false);
		} catch (ClassNotFoundException | JRException e){
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void teacherWithTeachingCategoryAndEqualCountOfMaterialAt(int teachingCat, int count){
		try{
			Class.forName("org.postgresql.Driver");			
			HashMap parametros = new HashMap();
			parametros.put("catDoc", teachingCat);
			parametros.put("cantSugerencias", count);
			JasperFillManager.fillReportToFile("src/reports/ProfesoresDeUnaCategoriaDocenteQueHanSugeridoUnaCantidadDadaDeMateriales2.jasper", parametros, myConnection);
			JasperViewer.viewReport("src/reports/ProfesoresDeUnaCategoriaDocenteQueHanSugeridoUnaCantidadDadaDeMateriales2.jrprint", false);
		} catch (ClassNotFoundException | JRException e){
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void teacherHasDidSugestedInOtherSubject(int subjectId){
		try{
			Class.forName("org.postgresql.Driver");			
			HashMap parametros = new HashMap();
			parametros.put("asignatura", subjectId);
			JasperFillManager.fillReportToFile("src/reports/ProfesoresQueHanSugeridoMaterialesEnOtrasAsignaturas.jasper", parametros, myConnection);
			JasperViewer.viewReport("src/reports/ProfesoresQueHanSugeridoMaterialesEnOtrasAsignaturas.jrprint", false);
		} catch (ClassNotFoundException | JRException e){
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
}
