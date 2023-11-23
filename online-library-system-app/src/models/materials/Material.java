package models.materials;

public class Material {
	protected int code;
	protected String location;
	protected String title;
	protected String author;
	protected int yearOfPublication;
	protected String[] keywords;
	protected String language;
	protected int visitedByStudent;
	protected int visitedByTeachers;
	protected int visitedByUnknown;
	protected String description;
	protected boolean canceled;
	
	public Material(int code, String location, String title, String author,
			int yearOfPublication, String keywords, String language,
			int visitedByStudent, int visitedByTeachers, int visitedByUnknown,
			String description, boolean canceled) {
		super();
		this.code = code;
		this.location = location;
		this.title = title;
		this.author = author;
		this.yearOfPublication = yearOfPublication;
		this.keywords = keywords.split(",");
		this.language = language;
		this.visitedByStudent = visitedByStudent;
		this.visitedByTeachers = visitedByTeachers;
		this.visitedByUnknown = visitedByUnknown;
		this.description = description;
		this.canceled = canceled;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getYearOfPublication() {
		return yearOfPublication;
	}

	public void setYearOfPublication(int yearOfPublication) {
		this.yearOfPublication = yearOfPublication;
	}

	public String[] getKeywords() {
		return keywords;
	}
	
	public String getKeywordsFull(){
		String keys = "";
		int i = 0;
		
		for(; i < keywords.length - 1; i++)
			keys += keywords[i] + ", ";
		keys += keywords[i];
		
		return keys;		
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getVisitedByStudent() {
		return visitedByStudent;
	}

	public void setVisitedByStudent(int visitedByStudent) {
		this.visitedByStudent = visitedByStudent;
	}

	public int getVisitedByTeachers() {
		return visitedByTeachers;
	}

	public void setVisitedByTeachers(int visitedByTeachers) {
		this.visitedByTeachers = visitedByTeachers;
	}

	public int getVisitedByUnknown() {
		return visitedByUnknown;
	}

	public void setVisitedByUnknown(int visitedByUnknown) {
		this.visitedByUnknown = visitedByUnknown;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
}
