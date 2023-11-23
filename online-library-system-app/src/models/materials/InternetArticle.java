package models.materials;


public class InternetArticle extends Material {
	private String address;
	private String date;

	public InternetArticle(int code, String location, String title,
			String author, int yearOfPublication, String keywords,
			String language, int visitedByStudent, int visitedByTeachers,
			int visitedByUnknown, String description, boolean canceled,
			String address, String date) {
		super(code, location, title, author, yearOfPublication, keywords,
				language, visitedByStudent, visitedByTeachers,
				visitedByUnknown, description, canceled);
		this.address = address;
		this.date = date;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
}
