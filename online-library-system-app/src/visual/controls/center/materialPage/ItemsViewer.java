package visual.controls.center.materialPage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import models.materials.Material;
import services.MaterialServices;
import utils.DialogBox;
import utils.OpButton;
import utils.Utilities;

public class ItemsViewer extends StackPane {
	private static ItemsViewer pane = null;
	
	public static final int WIDTH = 605;
	public static final int HEIGHT = 416;			//Esta es la altura de MaterialViewer, no de este Nodo
	public static final int PADDING_LEFT = 60;
	public static final int PADDING_TOP = 20;
	public static final int PADDING_RIGHT = 30;
	public static final int PADDING_BOTTOM = 15;
	public static final int SPACING = 10;//5
	
	private GridPane grid;
	
	private List<Item> items;
	private List<Item> currentItems;
	private int currentIndex;
	private int nextIndex;
	private int countColumn;
	private int remainingColumn;
	private int countRow;
	
	private ImageViewRunnerNext runnerNext;
	private Timeline timeNext;
	private ImageViewRunnerPrev runnerPrev;
	private Timeline timePrev;
	
	private boolean initDrag = false;
	private double initialX;
	
	private ButtonPrev btnPrev;
	private ButtonNext btnNext;
	private boolean navigationButtonVisible = false;
	
	private ActionButtons actionButtons;
	private Timeline openActionButtons;
	private Timeline closeActionButtons;
	
	public static ItemsViewer getInstance(){
		if(pane == null)
			pane = new ItemsViewer();
		return pane;
	}
	
	private ItemsViewer(){
		setId("itemsViewer");		
		
		runnerNext = new ImageViewRunnerNext(0, 0, WIDTH, HEIGHT);
		timeNext = new Timeline();
		timeNext.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(runnerNext.posXProperty(), 0)),
				new KeyFrame(new Duration(1000), new KeyValue(runnerNext.posXProperty(), WIDTH))
				);
		
		runnerPrev = new ImageViewRunnerPrev(WIDTH, 0, WIDTH, HEIGHT);
		timePrev = new Timeline();
		timePrev.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(runnerPrev.posXProperty(), WIDTH)),
				new KeyFrame(new Duration(1000), new KeyValue(runnerPrev.posXProperty(), 0))
				);
		
		btnPrev = new ButtonPrev();
		btnPrev.getButton().setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				if(ItemsViewer.this.getChildren().contains(grid))
					prevScreen();
			}
		});
		StackPane.setAlignment(btnPrev, Pos.CENTER_LEFT);
		btnNext = new ButtonNext();
		btnNext.getButton().setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				if(ItemsViewer.this.getChildren().contains(grid))
					nextScreen();
			}
		});
		StackPane.setAlignment(btnNext, Pos.CENTER_RIGHT);
		
		actionButtons = new ActionButtons();
		StackPane.setAlignment(actionButtons, Pos.BOTTOM_RIGHT);
		
		getChildren().addAll(btnPrev, btnNext, actionButtons);
		btnPrev.setVisible(false);
		btnNext.setVisible(false);
		
		openActionButtons = new Timeline();
		openActionButtons.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(actionButtons.changedWidthProperty(), 30)),
				new KeyFrame(Duration.millis(150), new KeyValue(actionButtons.changedWidthProperty(), ActionButtons.WIDTH))
				);
		
		closeActionButtons = new Timeline();
		closeActionButtons.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(actionButtons.changedWidthProperty(), ActionButtons.WIDTH)),
				new KeyFrame(Duration.millis(150), new KeyValue(actionButtons.changedWidthProperty(), 30))
				);
		
		grid = new GridPane();
		//grid.setTranslateY(PADDING_TOP);
		//grid.setTranslateX(PADDING_LEFT);
		
		grid.setOnMousePressed(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				initDrag = true;
				initialX = e.getX();
			}
		});
		
		grid.setOnMouseDragged(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				if(initDrag){
					double posX = e.getX();
					double difference = posX - initialX;
					
					if(difference <= -100 && (countColumn * countRow < items.size())){
						nextScreen();
						initDrag = false;
					}
					else if(difference >= 100 && (countColumn * countRow < items.size())){
						prevScreen();
						initDrag = false;
					}
				}
			}
		});
		
		grid.setOnMouseReleased(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				initDrag = false;
			}
		});
		
		grid.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				if(countColumn * countRow < items.size() && !navigationButtonVisible){
					navigationButtonVisible = true;
					btnPrev.setVisible(true);
					btnNext.setVisible(true);
				}
			}
		});
		
		grid.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e){
				Point2D pointInPrev = btnPrev.parentToLocal(e.getX(), e.getY());
				Point2D pointInNext = btnNext.parentToLocal(e.getX(), e.getY());
				if(!btnPrev.contains(pointInPrev.getX(), pointInPrev.getY()) && !btnNext.contains(pointInNext.getX(), pointInNext.getY())){
					btnPrev.setVisible(false);
					btnNext.setVisible(false);
					navigationButtonVisible = false;
				}
			}
		});
		
		btnPrev.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				Point2D point = grid.sceneToLocal(e.getSceneX(), e.getSceneY());
				if(!grid.contains(point)){
					btnPrev.setVisible(false);
					btnNext.setVisible(false);
					navigationButtonVisible = false;
				}
			}
		});
		
		btnNext.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				Point2D point = grid.sceneToLocal(e.getSceneX(), e.getSceneY());
				if(!grid.contains(point)){
					btnPrev.setVisible(false);
					btnNext.setVisible(false);
					navigationButtonVisible = false;
				}
			}
		});
		
		getChildren().add(0, grid);
	}
	
	public void setVisibleActionButtons(boolean visibility){
		actionButtons.setVisible(visibility);
	}
	
	public void setItems(List<Item> items){
		this.items = items;
		
		countColumn = (int) ((WIDTH - PADDING_LEFT - PADDING_RIGHT) / (Item.WIDTH + SPACING));
		remainingColumn = (int) ((WIDTH - PADDING_LEFT - PADDING_RIGHT) % (Item.WIDTH + SPACING));
		
		countRow = (int) ((HEIGHT - PADDING_TOP - PADDING_BOTTOM) / (Item.HEIGHT + SPACING));
		//int remainingRow = (int) ((HEIGHT - PADDING_TOP - PADDING_BOTTOM) % (Item.HEIGHT + SPACING));
		
		currentIndex = 0;
		
		nextIndex = countColumn * countRow;		//Max count possible
		if(nextIndex > this.items.size())
			nextIndex = this.items.size();
		
		currentItems = this.items.subList(currentIndex, nextIndex);
		
		fillViewer();
	}
	
	private void nextPackOfItem(){		
		int size = items.size();
		if(nextIndex < size){
			
			int remaining = size - nextIndex;
			
			int count = countColumn * countRow;
			
			if(count > remaining)
				count = remaining;
			
			currentIndex = nextIndex;
			nextIndex += count;
			
			currentItems = items.subList(currentIndex, nextIndex);
		}
		else{
			currentIndex = 0;
			
			nextIndex = countColumn * countRow;		//Max count possible
			if(nextIndex > this.items.size())
				nextIndex = this.items.size();
			
			currentItems = this.items.subList(currentIndex, nextIndex);
		}
	}
	
	public void prevPackOfItem(){
		if(currentIndex >= countColumn * countRow){
			nextIndex = currentIndex;
			currentIndex -= countColumn * countRow;
			currentItems = items.subList(currentIndex, nextIndex);
		}
		else if(currentIndex == 0){
			int size = items.size();
			currentIndex =  size - (size % (countColumn * countRow));
			if(currentIndex == 0)
				currentIndex = size - (countColumn * countRow);
			nextIndex = size;
			currentItems = items.subList(currentIndex, nextIndex);
		}
	}
	
	public void clear(){
		grid.getChildren().removeAll(grid.getChildren());
	}
	
	private void fillViewer(){
		grid.getChildren().removeAll(grid.getChildren());
		
		Iterator<Item> it = currentItems.iterator();

		int paddingLeft = 0;
		int paddingTop = 0;
		
		for(int i = 1; i <= countRow && it.hasNext(); i++){
			for(int j = 1; j <= countColumn && it.hasNext(); j++){
				Item item = it.next();			
				GridPane.setConstraints(item, j - 1, i - 1);
				if(j == 1)
					paddingLeft = PADDING_LEFT;
				else
					paddingLeft = 0;
				if( i == 1)
					paddingTop = PADDING_TOP;
				else
					paddingTop = 0;
				
				GridPane.setMargin(item, new Insets(paddingTop, remainingColumn / countColumn + SPACING, SPACING, paddingLeft));
				grid.getChildren().add(item);
			}
		}
		
		if(countColumn * countRow < items.size() && !navigationButtonVisible){
			navigationButtonVisible = true;
			btnPrev.setVisible(true);
			btnNext.setVisible(true);
		}
		
	}
	
	private BufferedImage changeScreenNext(){
		BufferedImage buffer = new BufferedImage(WIDTH * 2, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffer.getGraphics();
		g.setColor(new Color(210, 210, 210));
		g.setFont(new Font("Serif", Font.PLAIN, 13));
		
		//Items actuales
		
		int posX = PADDING_LEFT;
		int posY = PADDING_TOP;
		
		Iterator<Item> it = currentItems.iterator();

		for(int i = 1; i <= countRow && it.hasNext(); i++){
			for(int j = 1; j <= countColumn && it.hasNext(); j++){
				Item item = it.next();			
				
				g.drawImage(ImageIconRefference.getInstance().get(item.getType()), 
						    (int)(posX + Item.ICON_POS_X), (int)(posY + Item.ICON_POS_Y), null);
				
				if(item.isCanceled())
					g.drawImage(ImageIconRefference.getInstance().getCanceledImg(),
							(int)(posX + Item.CANCELED_POS_X), (int)(posY + Item.CANCELED_POS_Y), null);
				
				if(item.isSelected())
					g.drawImage(ImageIconRefference.getInstance().getSelectImg(),
							(int)(posX + Item.SELECTED_POS_X), (int)(posY + Item.SELECTED_POS_Y), null);
				
				
				g.drawString(item.getName(), (int)(posX + Item.NAME_POS_X), (int)(posY + Item.NAME_POS_Y + 12));
				
				posX += Item.WIDTH + remainingColumn / countColumn + SPACING;
				
			}
			posX = PADDING_LEFT;
			posY += Item.HEIGHT + SPACING;
		}
		
		//Parte con los nuevos items a mostrar
		
		posX = PADDING_LEFT + WIDTH;
		posY = PADDING_TOP;
		
		nextPackOfItem();
		it = currentItems.iterator();
		
		for(int i = 1; i <= countRow && it.hasNext(); i++){
			for(int j = 1; j <= countColumn && it.hasNext(); j++){
				Item item = it.next();			
				
				g.drawImage(ImageIconRefference.getInstance().get(item.getType()), 
						    (int)(posX + Item.ICON_POS_X), (int)(posY + Item.ICON_POS_Y), null);
				
				if(item.isCanceled())
					g.drawImage(ImageIconRefference.getInstance().getCanceledImg(),
							(int)(posX + Item.CANCELED_POS_X), (int)(posY + Item.CANCELED_POS_Y), null);
				
				if(item.isSelected())
					g.drawImage(ImageIconRefference.getInstance().getSelectImg(),
							(int)(posX + Item.SELECTED_POS_X), (int)(posY + Item.SELECTED_POS_Y), null);
				
				
				g.drawString(item.getName(), (int)(posX + Item.NAME_POS_X), (int)(posY + Item.NAME_POS_Y + 12));
				
				posX += Item.WIDTH + remainingColumn / countColumn + SPACING;
				
			}
			posX = PADDING_LEFT + WIDTH;
			posY += Item.HEIGHT + SPACING;
		}
		
		return buffer;
	}
	
	private BufferedImage changeScreenPrev(){
		BufferedImage buffer = new BufferedImage(WIDTH * 2, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffer.getGraphics();
		g.setColor(new Color(210, 210, 210));
		g.setFont(new Font("Serif", Font.PLAIN, 13));
		
		//Items actuales
		
		int posX = PADDING_LEFT + WIDTH;
		int posY = PADDING_TOP;
		
		Iterator<Item> it = currentItems.iterator();

		for(int i = 1; i <= countRow && it.hasNext(); i++){
			for(int j = 1; j <= countColumn && it.hasNext(); j++){
				Item item = it.next();			
				
				g.drawImage(ImageIconRefference.getInstance().get(item.getType()), 
						    (int)(posX + Item.ICON_POS_X), (int)(posY + Item.ICON_POS_Y), null);
				
				if(item.isCanceled())
					g.drawImage(ImageIconRefference.getInstance().getCanceledImg(),
							(int)(posX + Item.CANCELED_POS_X), (int)(posY + Item.CANCELED_POS_Y), null);
				
				if(item.isSelected())
					g.drawImage(ImageIconRefference.getInstance().getSelectImg(),
							(int)(posX + Item.SELECTED_POS_X), (int)(posY + Item.SELECTED_POS_Y), null);
				
				
				g.drawString(item.getName(), (int)(posX + Item.NAME_POS_X), (int)(posY + Item.NAME_POS_Y + 12));
				
				posX += Item.WIDTH + remainingColumn / countColumn + SPACING;
				
			}
			posX = PADDING_LEFT + WIDTH;
			posY += Item.HEIGHT + SPACING;
		}
		
		//Parte con los nuevos items a mostrar
		
		posX = PADDING_LEFT;
		posY = PADDING_TOP;
		
		prevPackOfItem();
		
		it = currentItems.iterator();
				
		for(int i = 1; i <= countRow && it.hasNext(); i++){
			for(int j = 1; j <= countColumn && it.hasNext(); j++){
				Item item = it.next();			
				
				g.drawImage(ImageIconRefference.getInstance().get(item.getType()), 
						    (int)(posX + Item.ICON_POS_X), (int)(posY + Item.ICON_POS_Y), null);
				
				if(item.isCanceled())
					g.drawImage(ImageIconRefference.getInstance().getCanceledImg(),
							(int)(posX + Item.CANCELED_POS_X), (int)(posY + Item.CANCELED_POS_Y), null);
				
				if(item.isSelected())
					g.drawImage(ImageIconRefference.getInstance().getSelectImg(),
							(int)(posX + Item.SELECTED_POS_X), (int)(posY + Item.SELECTED_POS_Y), null);
				
				
				g.drawString(item.getName(), (int)(posX + Item.NAME_POS_X), (int)(posY + Item.NAME_POS_Y + 12));
				
				posX += Item.WIDTH + remainingColumn / countColumn + SPACING;
				
			}
			posX = PADDING_LEFT;
			posY += Item.HEIGHT + SPACING;
		}		
		
		return buffer;
	}
	
	private void nextScreen(){
		BufferedImage img = changeScreenNext();
		try {
			Image image = Utilities.createImage(img);
			getChildren().remove(grid);
			runnerNext.setImage(image);
			getChildren().add(0, runnerNext);
			timeNext.play();
			fillViewer();
		} catch (IOException e1) {
			DialogBox diag = new DialogBox("Ha ocurrido un error en la obtencion de los nuevos datos");
			diag.show();
		}
	}
	private void prevScreen(){
		BufferedImage img = changeScreenPrev();
		try {
			Image image = Utilities.createImage(img);
			getChildren().remove(grid);
			runnerPrev.setImage(image);
			getChildren().add(0, runnerPrev);
			timePrev.play();
			fillViewer();
		} catch (IOException e1) {
			DialogBox diag = new DialogBox("Ha ocurrido un error en la obtencion de los nuevos datos");
			diag.show();
		}
	}
	
	public void showItemsAfterNext(){
		getChildren().add(0, grid);
		getChildren().remove(runnerNext);
	}
	public void showItemsAfterPrev(){
		getChildren().add(0, grid);
		getChildren().remove(runnerPrev);
	}
	
	class ButtonPrev extends Region{
		private Button button;
		
		public Button getButton(){
			return button;
		}
		
		public ButtonPrev(){
			setId("buttonPrevRegion");
			
			setMinSize(54, 144);
			setPrefSize(54, 144);
			setMaxSize(54, 144);
			
			button = new Button();
			button.setMinSize(35, 109);
			button.setPrefSize(35, 109);
			button.setMaxSize(35, 109);
			
			ButtonPrev.this.getChildren().add(button);
		}
		
		protected void layoutChildren(){
			button.resizeRelocate(10, 17.5, 35, 109);
		}
	}
	
	class ButtonNext extends Region{
		private Button button;
		
		public Button getButton(){
			return button;
		}
		
		public ButtonNext(){
			setId("buttonNextRegion");
			
			setMinSize(54, 144);
			setPrefSize(54, 144);
			setMaxSize(54, 144);
			
			button = new Button();
			button.setMinSize(33, 109);
			button.setPrefSize(33, 109);
			button.setMaxSize(33, 109);
			
			ButtonNext.this.getChildren().add(button);
		}
		
		protected void layoutChildren(){
			button.resizeRelocate(10, 17.5, 35, 109);
		}
	}
	
	
	public void setTypesOfItems(TypeItem type){
		actionButtons.setType(type);
	}
	public void setSelectedTematicId(int id){
		actionButtons.setTematicId(id);
	}
	public void setSelectedSubjectId(int id){
		actionButtons.setSubjectId(id);
	}
	
	public Item getFirstSelected(){
		Iterator<Item> it = items.iterator();
		boolean found = false;
		Item item = null;
		
		while(it.hasNext() && !found)
			found = (item = it.next()).isSelected();
		
		return found ? item : null;
	}
	public List<Item> getSelected(){
		List<Item> selected = new LinkedList<Item>();
		Iterator<Item> it = items.iterator();
		
		while(it.hasNext()){
			Item item = it.next();
			if(item.isSelected())
				selected.add(item);
		}
		
		return selected;
	}
	
	public void setCanInsert(boolean can){
		actionButtons.setCanInsert(can);
	}
	public void setCanModify(boolean can){
		actionButtons.setCanModify(can);
	}
	public void setCanDelete(boolean can){
		actionButtons.setCanDelete(can);
	}
	
	public int getCurrentTematicId(){
		return actionButtons.getTematicId();
	}
	public int getCurrentSubjectId(){
		return actionButtons.getSubjectId();
	}
	
	class ActionButtons extends Region{
		public static final double WIDTH = 298;
		public static final double HEIGHT = 46;
		
		private ToggleButton checkPane;
		
		private OpButton btnInsert;
		private OpButton btnModify;
		private OpButton btnDelete;
		
		private int tematicId;
		private int subjectId;
		private TypeItem lastPane;
		
		private boolean canInsert;
		private boolean canModify;
		private boolean canDelete;
		
		private DoubleProperty changedWidth;
		
		public ActionButtons(){
			setId("actionButtons");
			
			setMinSize(30, HEIGHT);
			setPrefSize(30, HEIGHT);
			setMaxSize(30, HEIGHT);
			
			checkPane = new ToggleButton();
			checkPane.setId("checkPane");
			
			setOnMouseEntered(new EventHandler<MouseEvent>(){
				public void handle(MouseEvent e){
					if(!checkPane.isSelected())
						openActionButtons.play();
				}
			});
			setOnMouseExited(new EventHandler<MouseEvent>(){
				public void handle(MouseEvent e){
					if(!checkPane.isSelected()){
						openActionButtons.stop();
						getChildren().removeAll(btnInsert, btnModify, btnDelete);
						closeActionButtons.play();
					}
				}
			});
			
			btnInsert = new OpButton("Nuevo");
			btnInsert.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e){
					if(lastPane.equals(TypeItem.TEMATIC)){
						DialogInsertModifyTematic diag = new DialogInsertModifyTematic(-1, null, false);
						diag.show();
					}
					else if(lastPane.equals(TypeItem.SUBJECT)){
						DialogInsertModifySubject diag = new DialogInsertModifySubject(tematicId, -1, null, false);
						diag.show();
					}
					else if(lastPane.equals(TypeItem.MATERIAL)){
						InsertModifyMaterialDialog diag = new InsertModifyMaterialDialog(null, tematicId, subjectId);
						diag.show();
					}
				}
			});
			btnModify = new OpButton("Modificar");
			btnModify.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e){
					Item item = getFirstSelected();
					
					if(item != null){
						if(lastPane.equals(TypeItem.TEMATIC)){
							DialogInsertModifyTematic diag = new DialogInsertModifyTematic(item.getItemId(), item.getName(), item.isCanceled());
							diag.show();
						}
						else if(lastPane.equals(TypeItem.SUBJECT)){
							DialogInsertModifySubject diag = new DialogInsertModifySubject(tematicId, item.getItemId(), item.getName(), item.isCanceled());
							diag.show();
						}
						else if(lastPane.equals(TypeItem.MATERIAL)){
							Material material;
							try {
								material = MaterialServices.getMaterialByID(item.getItemId());
								InsertModifyMaterialDialog diag = new InsertModifyMaterialDialog(material, tematicId, subjectId);
								diag.show();
							} catch (SQLException e1) {
								DialogBox diag = new DialogBox("No se pudieron obtener los datos del material");
								diag.show();
							}							
						}
					}
					else{
						DialogBox diag = new DialogBox("Debe seleccionar un item");
						diag.show();
					}
				}
			});
			btnDelete = new OpButton("Eliminar");
			btnDelete.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e){
					List<Item> items = getSelected();
					
					if(!items.isEmpty()){
						int id = -1;
						if(lastPane.equals(TypeItem.SUBJECT))
							id = tematicId;
						else if(lastPane.equals(TypeItem.MATERIAL))
							id = subjectId;
						
						DialogCancelTematicSubjectMaterial diag = new DialogCancelTematicSubjectMaterial(lastPane, items, id, true); //es por seleccion
						diag.show();
					}
					else{
						DialogBox diag = new DialogBox("Debe seleccionar al menos un item");
						diag.show();
					}
				}
			});
			
			changedWidth = new DoubleProperty() {
				
				@Override
				public void set(double value) {
					setMinWidth(value);
					setPrefWidth(value);
					setMaxWidth(value);
					
					if(value == WIDTH){
						if(canInsert)
							getChildren().add(btnInsert);
						if(canModify)
							getChildren().add(btnModify);
						if(canDelete)
							getChildren().add(btnDelete);
					}
					
				}
				public double get() { return 0; }
				public void removeListener(InvalidationListener listener) {}
				public void addListener(InvalidationListener listener) { }
				public void removeListener(ChangeListener<? super Number> listener) { }
				public void addListener(ChangeListener<? super Number> listener) { }
				public String getName() { return null; }
				public Object getBean() { return null; }
				public void unbind() { }
				public boolean isBound() { return false; }
				public void bind(ObservableValue<? extends Number> observable) { }
			};
			
			getChildren().addAll(checkPane);
		}
		
		public DoubleProperty changedWidthProperty(){
			return changedWidth;
		}
		
		public void setCanInsert(boolean can){
			canInsert = can;
		}
		public void setCanModify(boolean can){
			canModify = can;
		}
		public void setCanDelete(boolean can){
			canDelete = can;
		}
		
		protected void layoutChildren(){
			checkPane.resizeRelocate(0,  8, 30, 30);
			btnInsert.resizeRelocate(37, 11, OpButton.WIDTH, OpButton.HEIGHT);
			btnModify.resizeRelocate(124, 11, OpButton.WIDTH, OpButton.HEIGHT);
			btnDelete.resizeRelocate(211, 11, OpButton.WIDTH, OpButton.HEIGHT);
		}
		
		public void setType(TypeItem type){
			lastPane = type;
		}
		public void setTematicId(int id){
			tematicId = id;
		}
		public void setSubjectId(int id){
			subjectId = id;
		}

		public int getTematicId() {
			return tematicId;
		}

		public int getSubjectId() {
			return subjectId;
		}
		
	}
}

class ImageViewRunnerNext extends ImageView {
	private DoubleProperty posX;
	
	@SuppressWarnings("unused")
	private double x;
	private double y;
	private double width;
	private double height;
	
	public ImageViewRunnerNext(double x, double y, double width, double height){
		this(null, x, y, width, height);
	}
	
	public ImageViewRunnerNext(Image img, double x, double y, double width, double height){
		super(img);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		Rectangle2D rect = new Rectangle2D(x, y, width, height);
		setViewport(rect);
		
		posX = new DoubleProperty() {
			
			@Override
			public void set(double value) {
				Rectangle2D rect = new Rectangle2D(value, ImageViewRunnerNext.this.y, ImageViewRunnerNext.this.width, ImageViewRunnerNext.this.height);
				setViewport(rect);
				if(value == ItemsViewer.WIDTH)
					ItemsViewer.getInstance().showItemsAfterNext();
			}
			
			@Override
			public double get() { return 0; }			
			@Override
			public void removeListener(InvalidationListener listener) {;}			
			@Override
			public void addListener(InvalidationListener listener) {;}			
			@Override
			public void removeListener(ChangeListener<? super Number> listener) {;}			
			@Override
			public void addListener(ChangeListener<? super Number> listener) {;}			
			@Override
			public String getName() { return null; }			
			@Override
			public Object getBean() { return null; }			
			@Override
			public void unbind() {;}			
			@Override
			public boolean isBound() { return false; }			
			@Override
			public void bind(ObservableValue<? extends Number> observable) {;}
		};
	}
	
	public DoubleProperty posXProperty(){
		return posX;
	}
}

class ImageViewRunnerPrev extends ImageView {
	private DoubleProperty posX;
	
	@SuppressWarnings("unused")
	private double x;
	private double y;
	private double width;
	private double height;
	
	public ImageViewRunnerPrev(double x, double y, double width, double height){
		this(null, x, y, width, height);
	}
	
	public ImageViewRunnerPrev(Image img, double x, double y, double width, double height){
		super(img);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		Rectangle2D rect = new Rectangle2D(x, y, width, height);
		setViewport(rect);
		
		posX = new DoubleProperty() {
			
			@Override
			public void set(double value) {
				Rectangle2D rect = new Rectangle2D(value, ImageViewRunnerPrev.this.y, ImageViewRunnerPrev.this.width, ImageViewRunnerPrev.this.height);
				setViewport(rect);
				if(value == 0)
					ItemsViewer.getInstance().showItemsAfterPrev();
			}
			
			@Override
			public double get() { return 0; }			
			@Override
			public void removeListener(InvalidationListener listener) {;}			
			@Override
			public void addListener(InvalidationListener listener) {;}			
			@Override
			public void removeListener(ChangeListener<? super Number> listener) {;}			
			@Override
			public void addListener(ChangeListener<? super Number> listener) {;}			
			@Override
			public String getName() { return null; }			
			@Override
			public Object getBean() { return null; }			
			@Override
			public void unbind() {;}			
			@Override
			public boolean isBound() { return false; }			
			@Override
			public void bind(ObservableValue<? extends Number> observable) {;}
		};
	}
	
	public DoubleProperty posXProperty(){
		return posX;
	}
}


