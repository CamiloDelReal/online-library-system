package models.materials;

public class Book extends Material {
	private int editionNumber;
	private String ISBN;

	public Book(int code, String location, String title, String author,
			int yearOfPublication, String keywords, String language,
			int visitedByStudent, int visitedByTeachers, int visitedByUnknown,
			String description, boolean canceled, int editionNumber, String iSBN) {
		super(code, location, title, author, yearOfPublication, keywords,
				language, visitedByStudent, visitedByTeachers,
				visitedByUnknown, description, canceled);
		this.editionNumber = editionNumber;
		ISBN = iSBN;
	}

	public int getEditionNumber() {
		return editionNumber;
	}

	public void setEditionNumber(int editionNumber) {
		this.editionNumber = editionNumber;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}
	
	
}
