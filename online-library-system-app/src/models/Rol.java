package models;

public class Rol {
	private int id;
	private String name;
	private boolean canceled;
	
	public Rol(int id, String name, boolean canceled) {
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
	
	public String toString(){
		return name;
	}
}
