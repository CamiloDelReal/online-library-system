package visual.controls.center;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class CenterViewerMain extends StackPane {
	private static CenterViewerMain viewer;
	private Node lastItem;
	
	private CenterViewerMain(){
		setId("centerViewerMain");
		setTranslateY(13);
		setTranslateX(25);
		
		/*Region spacer = new Region();
		spacer.setMinWidth(25);
		spacer.setPrefWidth(25);
		spacer.setMaxWidth(25);
		
		getChildren().add(spacer);*/
	}
	
	public static CenterViewerMain getInstance(){
		if(viewer == null)
			viewer = new CenterViewerMain();
		return viewer;
	}
	
	public void put(Node item){
		getChildren().remove(lastItem);
		if(item != null){
			HBox.setHgrow(item, Priority.ALWAYS);
			getChildren().add(item);
			lastItem = item;
		}
	}
}
