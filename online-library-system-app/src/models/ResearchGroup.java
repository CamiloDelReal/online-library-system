package models;

import java.util.Iterator;
import java.util.List;

public class ResearchGroup {
	private int id;
	private String name;
	private boolean canceled;
	
	public ResearchGroup(int id, String name, boolean canceled) {
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

	public void setCanceled(boolean cenceled) {
		this.canceled = cenceled;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public static ResearchGroup find(List<ResearchGroup> rGroups, String name){
		ResearchGroup subject = null;
		Iterator<ResearchGroup> it = rGroups.iterator();
		boolean found = false;
		
		while(it.hasNext() && !found){
			subject = it.next();
			if(subject.getName().equals(name))
				found = true;
		}
		
		return found ? subject : null;
	}
}
