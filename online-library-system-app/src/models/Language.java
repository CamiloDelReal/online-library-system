package models;

import java.util.Iterator;
import java.util.List;

public class Language {
	private int id;
	private String name;
	private boolean canceled;
	
	public Language(int id, String name, boolean canceled) {
		super();
		this.id = id;
		this.name = name;
		this.canceled = canceled;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	
	public static Language find(List<Language> lang, String name){
		Language language = null;
		boolean found = false;
		Iterator<Language> it = lang.iterator();
		
		while(it.hasNext() && !found){
			language = it.next();
			if(language.getName().equalsIgnoreCase(name))
				found = true;
		}
		
		return found ? language : null;
	}
	
	public String toString(){
		return name;
	}
}
