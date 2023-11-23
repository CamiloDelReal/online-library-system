package models.materials;

public class Magazine extends Material {
	private int number;
	private int volume;
	private String ISSN;

	public Magazine(int code, String location, String title, String author,
			int yearOfPublication, String keywords, String language,
			int visitedByStudent, int visitedByTeachers, int visitedByUnknown,
			String description, boolean canceled, int number, int volume,
			String iSSN) {
		super(code, location, title, author, yearOfPublication, keywords,
				language, visitedByStudent, visitedByTeachers,
				visitedByUnknown, description, canceled);
		this.number = number;
		this.volume = volume;
		ISSN = iSSN;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public String getISSN() {
		return ISSN;
	}

	public void setISSN(String iSSN) {
		ISSN = iSSN;
	}
	
	
}
