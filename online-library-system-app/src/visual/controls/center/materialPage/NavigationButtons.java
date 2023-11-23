package visual.controls.center.materialPage;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class NavigationButtons extends Region {
	private Button back;
	private Button next;
	
	public NavigationButtons(){
		setId("navigationButtons");
		
		setMinSize(60, 35);
		setPrefSize(60, 35);
		setMaxSize(60, 35);
		
		back = new Button();
		back.setMinSize(32, 32);
		back.setPrefSize(32, 32);
		back.setMaxSize(32, 32);
		back.getStyleClass().add("back");
		
		next = new Button();
		back.setMinSize(21, 21);
		back.setPrefSize(21, 21);
		back.setMaxSize(21, 21);
		next.getStyleClass().add("next");
		
		getChildren().addAll(back, next);
	}
	
	@Override
	protected void layoutChildren() {
		back.resizeRelocate(4, 3, 32, 32);
		next.resizeRelocate(34, 10, 21, 21);
    }
}