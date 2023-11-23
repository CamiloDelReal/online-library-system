package visual.controls.center.materialPage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

public class SearchField extends Region{
	private Button clearButton;
	private TextField searchField;
	private ChoiceBox<String> menu;
	
	public SearchField(){
		setId("SearchBox");
		minHeight(24);
		maxHeight(24);
        setPrefSize(150, 24);
        
        
		searchField = new TextField();
		searchField.setPromptText("Filtrar");
		searchField.setMinSize(120, 24);
		searchField.setPrefSize(120, 24);
		searchField.setMaxSize(120, 24);
		
		
		clearButton = new Button();
		clearButton.setVisible(false);
		
		menu = new ChoiceBox<String>();
		menu.getItems().addAll("Por titulo", "Por autor", "Por referencia");
		menu.getSelectionModel().selectFirst();
		
		getChildren().addAll(searchField, clearButton, menu);
		
		searchField.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String oldCad, String newCad) {
				clearButton.setVisible(searchField.getText().length() != 0);				
			}
			
		});				
		
		clearButton.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				searchField.setText("");
				searchField.requestFocus();
				clearButton.setVisible(false);
			}
		});
		
	}
	@Override protected void layoutChildren() {
		searchField.resize(getWidth(),getHeight());
        clearButton.resizeRelocate(getWidth()-38,6,12,13);
        menu.resizeRelocate(getWidth() - 19, 5, 13, 15);
    }

	private void setPrefSize(int i, int j) {
		// TODO Auto-generated method stub
		
	}
}