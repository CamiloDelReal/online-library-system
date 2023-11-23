package models.materials;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class MaterialWithProperty {
	private StringProperty titulo;
	private StringProperty autor;
	private IntegerProperty codigo;
	private StringProperty nombreTipoMaterial;
	private IntegerProperty asignatura;
	private IntegerProperty tematica;
	private StringProperty palabrasClaves;
	
	public MaterialWithProperty(String titulo, String autor,
			int codigo, String nombreTipoMaterial,
			int asignatura, int tematica,
			String palabrasClaves) {
		super();
		this.titulo = new SimpleStringProperty(titulo);
		this.autor = new SimpleStringProperty( autor);
		this.codigo =  new SimpleIntegerProperty(codigo);
		this.nombreTipoMaterial =  new SimpleStringProperty(nombreTipoMaterial);
		this.asignatura =  new SimpleIntegerProperty(asignatura);
		this.tematica = new SimpleIntegerProperty(tematica);
		this.palabrasClaves = new SimpleStringProperty(palabrasClaves);
	}
	
	public StringProperty tituloProperty(){
		return titulo;
	}
	
	public StringProperty autorProperty(){
		return  autor;
	}
	
	public StringProperty nombreTipoMaterialProperty(){
		return  nombreTipoMaterial;
	}
	
	public StringProperty palabrasClavesProperty(){
		return palabrasClaves;
	}
	
	public IntegerProperty codigoProperty(){
		return codigo;
	}
	
	public IntegerProperty asignaturaProperty(){
		return asignatura;
	}
	
	public IntegerProperty tematicaProperty(){
		return tematica;
	}
	

}
