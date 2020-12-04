import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;

public class TamagotchiView extends Application implements Observer{

	private static final int WIDTH = 900;
	private static final int HEIGHT = 400;
	private String petImage = "";
	
	
	TamagotchiModel model = new TamagotchiModel();
	TamagotchiController controller = new TamagotchiController(model);
	BorderPane root = new BorderPane();
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		model.addObserver(this);
		primaryStage.setScene(mainMenu(primaryStage));
		primaryStage.setTitle("Tamagotchi");
		primaryStage.show();
	}
	
	private Scene mainMenu(Stage primaryStage) throws FileNotFoundException {
		addBackground(root);
		addMenu(primaryStage);
		return new Scene(root, WIDTH, HEIGHT);
	}
	
	private VBox menuBox = new VBox(-5);
	
	private void addMenu(Stage primaryStage) {
		menuBox.setSpacing(10);
		menuBox.setPadding(new Insets(0,0,0,20));
		menuBox.setAlignment(Pos.CENTER_LEFT);
		Button newGame = new Button("New Game");
		newGame.setPrefWidth(150);
		newGame.setFont(Font.font(15));
		
		newGame.setOnMouseClicked(e -> {
			try {
				Scene newScene = tamagotchiSelection(primaryStage);
				primaryStage.setScene(newScene);
				primaryStage.show();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		Button loadGame = new Button("Load Game");
		loadGame.setPrefWidth(150);
		loadGame.setFont(Font.font(15));
		
		loadGame.setOnMouseClicked(e ->{
			
		});
		
		Button quit = new Button("Quit");
		quit.setPrefWidth(150);
		quit.setFont(Font.font(15));
		
		quit.setOnMouseClicked(e ->{
			primaryStage.close();
		});
		
		
		menuBox.getChildren().addAll(newGame,loadGame,quit);
		root.setLeft(menuBox);
	}
	
	
	
	
	private void addBackground(BorderPane root) throws FileNotFoundException {
		ImageView image = new ImageView(new Image(new FileInputStream("./Pet Images/background.jpg")));
		root.getChildren().add(image);
	}
	
	private Scene runGame(Stage primaryStage) throws Exception {
		BorderPane newRoot = new BorderPane();
		addBackground(newRoot);
		HBox allMechanics = new HBox();
		Button feedSnacks = new Button("Feed Snacks");
		Button feedMeal = new Button("Feed Meal");
		Button feedMedicine = new Button("Give Medicine");
		Button pause = new Button("Pause");
		Image pet;
		pet = null;
		try {
			pet = new Image(new FileInputStream(petImage));
		}
		catch (Exception e){
			Alert a = new Alert(AlertType.ERROR, "Pet Image not Found");
			a.showAndWait();
		}
		ImageView mainView = new ImageView(pet);
		feedSnacks.setPrefWidth(150);
		feedMeal.setPrefWidth(150);
		feedMedicine.setPrefWidth(150);
		
		allMechanics.getChildren().addAll(feedSnacks,feedMeal,feedMedicine);
		allMechanics.setPadding(new Insets(0,5,30,210));
		allMechanics.setSpacing(10);
		
		pause.setOnMouseClicked(e ->{
			//stops the timer from the threading
			//pops up alert box to either save game/resume game/ quit game
			ButtonType saveGame = new ButtonType("Save Game", 
					ButtonBar.ButtonData.OK_DONE);
			ButtonType resumeGame = new ButtonType("Resume Game", 
					ButtonBar.ButtonData.BACK_PREVIOUS);
			ButtonType quitGame = new ButtonType("Quit Game", 
					ButtonBar.ButtonData.CANCEL_CLOSE);
			
			Alert alert = new Alert(AlertType.INFORMATION,"", 
					resumeGame,saveGame,quitGame);
			
			alert.setTitle("Pause Menu");
			alert.setHeaderText("Pause Menu");
			
			Optional<ButtonType> result = alert.showAndWait();
			
			if(result.get() == saveGame) {
				
			} else if(result.get() == resumeGame) {
				
			} else {
				
			}
		});
		
		feedSnacks.setOnMouseClicked(e ->{
			//increase the weight by alot and happiness but hunger alittle
		});
		
		feedMeal.setOnMouseClicked(e ->{
			//only increases weight by alittle and hunger alot
		});

		feedMedicine.setOnMouseClicked(e ->{
			//increase hp
		});
		
		setProgressBars(newRoot);
		
		newRoot.setTop(pause);
		newRoot.setBottom(allMechanics);
		newRoot.setCenter(mainView);
		Scene newScene = new Scene(newRoot, WIDTH,HEIGHT);
		return newScene;
	}

	private Scene tamagotchiSelection(Stage primaryStage) throws FileNotFoundException {
		String[] images = {"./Pet Images/transparent_panda.png", "./Pet Images/transparent_mouse.png", "./Pet Images/transparent_dog.png"};
		String[] buttons = {"Panda", "Mouse", "Dog"};
		BorderPane newRoot = new BorderPane();
		addBackground(newRoot);
		
		HBox backgroundHBox = new HBox();
		VBox confirmVBox = new VBox();
		Button confirm = new Button("Confirm");
		confirm.setOnMouseClicked(e -> {
			Scene newScene;
			try {
				newScene = runGame(primaryStage);
				primaryStage.setScene(newScene);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		for(int i = 0; i < images.length; i++) {
			VBox vbox = new VBox();
			String petStr = images[i];
			Image pet = new Image(new FileInputStream(petStr));
			ImageView petView = new ImageView(pet);
			Button button = new Button(buttons[i]);
			button.setOnMouseClicked(e -> {
				petImage = petStr;
				if(confirm.isDisabled()) {
					confirm.setDisable(false);
				}
			});
			button.setAlignment(Pos.CENTER);
			vbox.getChildren().addAll(petView, button);
			vbox.setAlignment(Pos.CENTER);
			backgroundHBox.getChildren().add(vbox);
			
		}
		if(petImage.equals("")) {
			confirm.setDisable(true);
		}
		confirm.setPrefWidth(200);
		confirm.setPrefHeight(50);
		confirm.setAlignment(Pos.CENTER);
		confirmVBox.getChildren().add(confirm);
		confirmVBox.setAlignment(Pos.BOTTOM_RIGHT);
		
		newRoot.setCenter(backgroundHBox);
		newRoot.setBottom(confirmVBox);
		Scene newScene = new Scene(newRoot, WIDTH, HEIGHT);
		return newScene;
	}

	private void setProgressBars(BorderPane newRoot) {
		HBox progressBars = new HBox();
		StackPane healthPane = new StackPane();
		StackPane weightPane = new StackPane();
		StackPane happinessPane = new StackPane();
		VBox healthVBox = new VBox();
		VBox weightVBox = new VBox();
		VBox happinessVBox = new VBox();
		// Health Bar
		Rectangle healthBackground = new Rectangle(200.0, 50.0, Color.BLACK);
		Rectangle health = new Rectangle(200.0, 50.0, Color.GREEN);
		Text heightText = new Text("100");
		heightText.setFont(Font.font("Impact", 35));
		Text healthLabel = new Text("Health");
		healthPane.getChildren().addAll(healthLabel, healthBackground, health, heightText);
		healthVBox.getChildren().addAll(healthLabel, healthPane);
		healthVBox.setPadding(new Insets(0, 0, 0, 0));
		progressBars.getChildren().add(healthVBox);
		
		// Weight Bar
		Rectangle weightBackground = new Rectangle(200.0, 50.0, Color.BLACK);
		Rectangle weight = new Rectangle(200.0, 50.0, Color.GREEN);
		Text weightText = new Text("100");
		weightText.setFont(Font.font("Impact", 35));
		Text weightLabel = new Text("Weight");
		weightPane.getChildren().addAll(weightLabel, weightBackground, weight, weightText);
		weightVBox.getChildren().addAll(weightLabel, weightPane);
		weightVBox.setPadding(new Insets(0, 100, 100, 100));
		progressBars.getChildren().add(weightVBox);
		
		// Happiness Bar
		Rectangle happinessBackground = new Rectangle(200.0, 50.0, Color.BLACK);
		Rectangle happiness = new Rectangle(200.0, 50.0, Color.GREEN);
		Text happinessText = new Text("100");
		happinessText.setFont(Font.font("Impact", 35));
		Text happinessLabel = new Text("Happiness");
		happinessPane.getChildren().addAll(happinessLabel, happinessBackground, happiness, happinessText);
		happinessVBox.getChildren().addAll(happinessLabel, happinessPane);
		happinessVBox.setPadding(new Insets(0, 100, 100, 0));
		progressBars.getChildren().add(happinessVBox);
				
		progressBars.setPadding(new Insets(10.0, 10.0, 10.0, 50.0));
		newRoot.getChildren().add(progressBars);
	}

}