package models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TeachingCategoryWithProperty {
	private IntegerProperty id;
	private StringProperty name;
	private BooleanProperty canceled;

	public TeachingCategoryWithProperty(int id, String name, boolean canceled) {
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.canceled = new SimpleBooleanProperty(canceled);
	}
	
	public TeachingCategoryWithProperty(TeachingCategory tc) {
		this.id = new SimpleIntegerProperty(tc.getId());
		this.name = new SimpleStringProperty(tc.getName());
		this.canceled = new SimpleBooleanProperty(tc.isCanceled());
	}
	
	public IntegerProperty idProperty() { return id; }
	public StringProperty nameProperty() { return name; }
	public BooleanProperty canceledProperty() { return canceled; }
}
