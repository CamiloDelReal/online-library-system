package models;

import java.util.Iterator;
import java.util.List;

public class Subject {
	private int id;
	private String name;
	private boolean canceled;

	public Subject(int id, String name, boolean canceled) {
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

	@Override
	public String toString() {
		return name;
	}
	
	public static Subject find(List<Subject> subjects, String name){
		Subject subject = null;
		Iterator<Subject> it = subjects.iterator();
		boolean found = false;
		
		while(it.hasNext() && !found){
			subject = it.next();
			if(subject.getName().equals(name))
				found = true;
		}
		
		return found ? subject : null;
	}
	
	public static Subject find(List<Subject> subjects, int subjectId){
		Subject subject = null;
		Iterator<Subject> it = subjects.iterator();
		boolean found = false;
		
		while(it.hasNext() && !found){
			subject = it.next();
			if(subject.getId() == subjectId)
				found = true;
		}
		
		return found ? subject : null;
	}
}
