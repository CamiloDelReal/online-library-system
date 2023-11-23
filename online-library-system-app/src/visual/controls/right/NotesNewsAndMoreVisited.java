package visual.controls.right;

import javafx.scene.layout.VBox;

public class NotesNewsAndMoreVisited extends VBox {
	private static NotesNewsAndMoreVisited notesNewsAndMoreVisited = null;
	
	private NotesPane notesPane;
	private NewsAndMoreVisitedPane newsAndMoreVisitedPane;
	
	public static NotesNewsAndMoreVisited getInstance(){
		if(notesNewsAndMoreVisited == null)
			notesNewsAndMoreVisited = new NotesNewsAndMoreVisited();
		
		return notesNewsAndMoreVisited;
	}
	
	private NotesNewsAndMoreVisited(){
		setId("notesNewsAndMoreVisited");
		setTranslateY(13);
		setSpacing(5);
		
		notesPane = new NotesPane();
		newsAndMoreVisitedPane = new NewsAndMoreVisitedPane();
		
		getChildren().addAll(notesPane, newsAndMoreVisitedPane);
	}
	
	public void update(){
		newsAndMoreVisitedPane.update();
		notesPane.update();
	}
}
