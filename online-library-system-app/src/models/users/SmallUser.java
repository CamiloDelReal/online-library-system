package models.users;

import java.awt.image.BufferedImage;

public class SmallUser {
	private int id;
	private String fullName;
	private BufferedImage picture;
	private boolean isCanceled;
	private boolean isTeacher;
	private boolean isMale;
	
	public SmallUser(int id, String fullName,
			BufferedImage picture, boolean isCanceled, boolean isTeacher, boolean isMale) {
		this.id = id;
		this.isMale = isMale;
		this.fullName = fullName;
		this.picture = picture;
		this.isCanceled = isCanceled;
		this.isTeacher = isTeacher;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isMale() {
		return isMale;
	}
	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
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
	public boolean isTeacher() {
		return isTeacher;
	}
	public void setTeacher(boolean isTeacher) {
		this.isTeacher = isTeacher;
	}
	
	public String toString(){
		return fullName;
	}
	
}
