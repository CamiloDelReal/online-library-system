package models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TeachingGroupWithProperty {
	private IntegerProperty id;
	private StringProperty name;
	private BooleanProperty canceled;
	
	public TeachingGroupWithProperty(int id, String name, boolean canceled) {
		super();
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);;
		this.canceled = new SimpleBooleanProperty(canceled);
	}
	
	public TeachingGroupWithProperty(TeachingGroup tg) {
		super();
		this.id = new SimpleIntegerProperty(tg.getId());
		this.name = new SimpleStringProperty(tg.getName());
		this.canceled = new SimpleBooleanProperty(tg.isCanceled());
	}

	public IntegerProperty idProperty() { return id; }
	public StringProperty nameProperty() { return name; }
	public BooleanProperty canceledProperty() { return canceled; }
	
}
