package models.materials;

public class Thesis extends Material {
	private int amountPages;
	private String degree;
	private String university;
	
	public Thesis(int code, String location, String title, String author,
			int yearOfPublication, String keywords, String language,
			int visitedByStudent, int visitedByTeachers, int visitedByUnknown,
			String description, boolean canceled, int amountPages,
			String degree, String university) {
		super(code, location, title, author, yearOfPublication, keywords,
				language, visitedByStudent, visitedByTeachers,
				visitedByUnknown, description, canceled);
		this.amountPages = amountPages;
		this.degree = degree;
		this.university = university;
	}

	public int getAmountPages() {
		return amountPages;
	}

	public void setAmountPages(int amountPages) {
		this.amountPages = amountPages;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}
	
	
}
