package models.users;

import java.awt.image.BufferedImage;

import models.Privilege;

public class Teacher extends User {
	private boolean teaches;
	private String scientificCategory;
	private String teachingCategory;
	private String subject;
	
	public Teacher(int id, long ci, String name, String firstLastName,
			String secondLastName, BufferedImage picture, boolean isCanceled,
			String userName, String password, Privilege privilege,
			boolean teaches, String scientificCategory,
			String teachingCategory, String subject) {
		super(id, ci, name, firstLastName, secondLastName, picture, isCanceled,
				userName, password, privilege);
		this.teaches = teaches;
		this.scientificCategory = scientificCategory;
		this.teachingCategory = teachingCategory;
		this.subject = subject;
	}

	public boolean isTeaches() {
		return teaches;
	}

	public void setTeaches(boolean teaches) {
		this.teaches = teaches;
	}

	public String getScientificCategory() {
		return scientificCategory;
	}

	public void setScientificCategory(String scientificCategory) {
		this.scientificCategory = scientificCategory;
	}

	public String getTeachingCategory() {
		return teachingCategory;
	}

	public void setTeachingCategory(String teachingCategory) {
		this.teachingCategory = teachingCategory;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
}
