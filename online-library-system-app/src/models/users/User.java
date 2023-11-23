package models.users;

import java.awt.image.BufferedImage;

import models.Privilege;

public class User {
	protected int id;
	protected long ci;
	protected String name;
	protected String firstLastName;
	protected String secondLastName;
	protected BufferedImage picture;
	protected boolean isCanceled;
	protected String userName;
	protected String password;
	protected Privilege privilege;
	
	public User(int id, long ci, String name, String firstLastName,
			String secondLastName, BufferedImage picture, boolean isCanceled,
			String userName, String password, Privilege privilege) {
		this.id = id;
		this.ci = ci;
		this.name = name;
		this.firstLastName = firstLastName;
		this.secondLastName = secondLastName;
		this.picture = picture;
		this.isCanceled = isCanceled;
		this.userName = userName;
		this.password = password;
		this.privilege = privilege;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getCi() {
		return ci;
	}

	public void setCi(long ci) {
		this.ci = ci;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstLastName() {
		return firstLastName;
	}

	public void setFirstLastName(String firstLastName) {
		this.firstLastName = firstLastName;
	}

	public String getSecondLastName() {
		return secondLastName;
	}

	public void setSecondLastName(String secondLastName) {
		this.secondLastName = secondLastName;
	}

	public BufferedImage getPicture() {
		return picture;
	}

	public void setPicture(BufferedImage picture) {
		this.picture = picture;
	}

	public boolean isCanceled() {
		return isCanceled;
	}

	public void setCanceled(boolean isCanceled) {
		this.isCanceled = isCanceled;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Privilege getPrivilege() {
		return privilege;
	}

	public void setPrivilege(Privilege privilege) {
		this.privilege = privilege;
	}

	public String getFullName() {
		return name + " " + firstLastName + " " + secondLastName;
	}
	
}
