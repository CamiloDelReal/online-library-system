package models;

import java.util.Iterator;
import java.util.List;

public class Tematic {
	private int id;
	private String name;
	private boolean canceled;

	public Tematic(int id, String name, boolean canceled) {
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
	
	public static Tematic find(List<Tematic> tematics, String name){
		Tematic tematic = null;
		Iterator<Tematic> it = tematics.iterator();
		boolean found = false;
		
		while(it.hasNext() && !found){
			tematic = it.next();
			if(tematic.getName().equals(name))
				found = true;
		}
		
		return found ? tematic : null;
	}
	
	public static Tematic find(List<Tematic> tematics, int id){
		Tematic tematic = null;
		Iterator<Tematic> it = tematics.iterator();
		boolean found = false;
		
		while(it.hasNext() && !found){
			tematic = it.next();
			if(tematic.getId() == id)
				found = true;
		}
		
		return found ? tematic : null;
	}
}
