package models.users;

import java.awt.image.BufferedImage;

import models.Privilege;

public class Student extends User {
	private String teachingGroup;
	private String researchGroup;
	
	public Student(int id, long ci, String name, String firstLastName,
			String secondLastName, BufferedImage picture, boolean isCanceled,
			String userName, String password, Privilege privilege,
			String teachingGroup, String researchGroup) {
		super(id, ci, name, firstLastName, secondLastName, picture, isCanceled,
				userName, password, privilege);
		this.teachingGroup = teachingGroup;
		this.researchGroup = researchGroup;
	}

	public String getTeachingGroup() {
		return teachingGroup;
	}

	public void setTeachingGroup(String teachingGroup) {
		this.teachingGroup = teachingGroup;
	}

	public String getResearchGroup() {
		return researchGroup;
	}

	public void setResearchGroup(String researchGroup) {
		this.researchGroup = researchGroup;
	}
	
}
