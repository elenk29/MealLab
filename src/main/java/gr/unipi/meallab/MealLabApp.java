package gr.unipi.meallab;

import gr.unipi.meallab.model.Recipe;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/* Η κλάση MealLabApp αποτελεί το "View" (Προβολή) της εφαρμογής.
   Υλοποιεί το γραφικό περιβάλλον χρήστη (GUI) με JavaFX.*/

public class MealLabApp extends Application {

	// Σύνδεση με τον Controller που έχει τη λογική και τα δεδομένα
	private final MealLabController controller = new MealLabController();

	// Η λίστα που θα εμφανίζεται αριστερά (ListView)
	// Είναι πεδίο της κλάσης για να έχουμε πρόσβαση σ' αυτήν απο παντου.
	private ListView<Recipe> recipeListView;

	// Αποθηκεύουμε σε ποια προβολή βρίσκεται ο χρήστης. Χρησιμεύει στο κουμπί της
	// διαγραφής.
	private String currentView = "SEARCH";

	// Στοιχεία γραφικού περιβάλλοντος
	private TextArea detailsArea;
	private ImageView mealImageView;
	private Label detailsLabel;
	private Button favBtn, cookedBtn, deleteBtn, openDetailsBtn;

	@Override
	public void start(Stage primaryStage) {

		/*
		 * Φτιάχνω στο πάνω μέρος πεδίο αναζήτησης και κουμπιά αναζήτησης, τυχαίας
		 * συνταγης και save, load
		 */

		TextField searchField = new TextField();
		searchField.setPromptText("Πληκτρολογήστε υλικό (π.χ. Chicken)...");
		searchField.setPrefWidth(220);

		// Κουμπιά για αναζήτηση και τυχαία συνταγή
		Button searchBtn = new Button("🔍 Αναζήτηση");
		Button randomBtn = new Button("Τυχαία Συνταγή");

		// Κουμπιά για μόνιμη Αποθήκευση
		Button saveBtn = new Button("💾 Αποθήκευση");
		Button loadBtn = new Button("📂 Ανάκτηση");
		// Spacer για να τοποθετηθούν τα κουμπιά αποθήκευσης δεξιά.
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		// Τοποθέτηση της πάνω μπάρας σε οριζόντια διάταξη (HBox)
		HBox topBox = new HBox(10, new Label("Αναζήτηση:"), searchField, searchBtn, randomBtn, spacer, loadBtn,
				saveBtn);
		topBox.setPadding(new Insets(10));
		topBox.setAlignment(Pos.CENTER_LEFT);
		topBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #cccccc;");

		// Δημιουργία Αριστερού μέρους με τη λίστα συνταγών, φίλτρα και κουμπιά

		// Κουμπιά για να επιλέγει ο χρήστης λίστα
		Button viewFavBtn = new Button("Αγαπημένα");
		Button viewCookedBtn = new Button("Μαγειρεμένα");

		// Θέλουμε τα κουμπιά να πιάνουν όλο το διαθέσιμο πλάτος και έτσι να έχουν όλα
		// ίσο μέγεθος.
		viewFavBtn.setMaxWidth(Double.MAX_VALUE);
		viewCookedBtn.setMaxWidth(Double.MAX_VALUE);

		// Ομαδοποίηση των κουμπιών κάθετα
		VBox filtersBox = new VBox(5, viewFavBtn, viewCookedBtn);

		// Συνδέουμε τη λίστα της οθόνης με τη λίστα του Controller. Αρχικοποίηση της
		// λίστας.
		recipeListView = new ListView<>(controller.getSearchResults());
		// Ρύθμιση για να φαίνεται σωστά το όνομα στο ListView και όχι κώδικας
		recipeListView.setCellFactory(param -> new ListCell<>() {
			@Override
			protected void updateItem(Recipe item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty || item == null ? null : item.getName());
			}
		});
		recipeListView.setPrefWidth(300);

		// Κουμπί Προβολής Λεπτομερειών
		openDetailsBtn = new Button("Προβολή Λεπτομερειών");
		openDetailsBtn.setStyle("-fx-font-weight: bold; -fx-base: #b6e7c9;");
		openDetailsBtn.setMaxWidth(Double.MAX_VALUE);
		openDetailsBtn.setDisable(true); // Αρχικά είναι απενεργοποιημένο

		// Δημιουργώ κάθετο κουτί για την αριστερή πλευρά.
		VBox leftBox = new VBox(10, new Label("1. Επιλέξτε Κατηγορία:"), filtersBox, new Label("2. Επιλέξτε Συνταγή:"),
				recipeListView, openDetailsBtn);
		leftBox.setPadding(new Insets(10));

		// Εικόνα
		mealImageView = new ImageView();
		mealImageView.setFitHeight(200);
		mealImageView.setFitWidth(300);
		mealImageView.setPreserveRatio(true);// Διατήρηση αναλογιών

		// Κείμενο Λεπτομερειών με τα υλικά και τις οδηγίες
		detailsArea = new TextArea();
		detailsArea.setEditable(false); // Ο χρήστης δεν πρέπει να γράφει εδώ
		detailsArea.setWrapText(true);// Αλλαγή γραμμής αυτόματα.
		detailsArea.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 14px;");

		// Κουμπιά ενεργειών
		favBtn = new Button("Προσθήκη στα Αγαπημένα");
		cookedBtn = new Button("Σημείωση ως Μαγειρεμένη");
		deleteBtn = new Button("Διαγραφή");
		// Κουμπί διαγραφής bold με κόκκινα γράμματα
		deleteBtn.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

		// Αρχικά τα κουμπιά απενεργοποιημένα (μέχρι να επιλέξει κάτι ο χρήστης)
		favBtn.setDisable(true);
		cookedBtn.setDisable(true);
		deleteBtn.setDisable(true);

		// Φτιάχνω οριζόντιο κουτί που περιέχει τα κουμπιά add, cooked και delete.
		HBox actionBox = new HBox(10, favBtn, cookedBtn, deleteBtn);
		actionBox.setAlignment(Pos.CENTER);// Κεντρική Στοίχιση
		actionBox.setPadding(new Insets(10));

		// Δημιουργία κεντρικού μέρους με τις λεπτομέρειες της συνταγής
		// Φτιάχνω την ετικέτα και την κρύβω;
		detailsLabel = new Label("Οδηγίες και Υλικά");
		detailsLabel.setVisible(false);// Την κρύβω αρχικά.

		/*
		 * Δημιουργώ κάθετο κουτί συο κέντρο της οθόνης που περιέχει με τη σειρά την
		 * εικόνα, τα κουμπιά του actionBox, τις λεπτομέρειες και το κείμενο με τα
		 * υλικά.
		 */
		VBox centerBox = new VBox(10, mealImageView, actionBox, detailsLabel, detailsArea);
		centerBox.setPadding(new Insets(10));// Περιθώριο
		centerBox.setAlignment(Pos.TOP_CENTER);// Κεντράρισμα από πάνω
		VBox.setVgrow(detailsArea, Priority.ALWAYS); // Γεμίζει το χώρο

		// Event Handling (Διαχείριση Γεγονότων). Ορίζουμε τι γίνεται με το πάτημα
		// κουμπιού.

		// Αλλαγή Λίστας. Θέλουμε να καθαρίζει η οθόνη δεξιά.

		viewFavBtn.setOnAction(e -> {
			currentView = "FAV"; // Σημειώνουμε ότι βλέπουμε Αγαπημένα
			recipeListView.setItems(controller.getFavorites());
			clearDetails();
		});

		viewCookedBtn.setOnAction(e -> {
			currentView = "COOKED"; // Σημειώνουμε ότι βλέπουμε Cooked
			recipeListView.setItems(controller.getCooked());
			clearDetails();
		});

		// Ενεργοποιείται το κουμπί Προβολής με κλικ στη λίστα
		recipeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			openDetailsBtn.setDisable(newVal == null);
		});

		// Εμφάνιση στοιχείων με το πάτημα κουμπιού προβολής
		openDetailsBtn.setOnAction(e -> {
			Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
			if (selected != null) {
				showRecipeDetails(selected);
			}
		});

		// Για αναζήτηση
		searchBtn.setOnAction(e -> {
			String inputText = searchField.getText();
			// Καλούμε την αναζήτηση και κρατάμε το αποτέλεσμα (true/false)
			boolean found = controller.search(inputText);

			currentView = "SEARCH";
			recipeListView.setItems(controller.getSearchResults());
			clearDetails();
			// Έλεγχος αν δεν βρέθηκε και ο χρήστης είχε γράψει κάτι.
			if (!found && !inputText.trim().isEmpty()) {
				showAlert("Δεν βρέθηκαν αποτελέσματα.", "Δεν βρέθηκε συνταγή με το όνομα: \"" + inputText + "\".");
			}

		});
		// Enter αντί για πάτημα κουμπιού
		searchField.setOnAction(e -> searchBtn.fire());

		// Για τυχαία συνταγή
		randomBtn.setOnAction(e -> {
			controller.randomRecipe();
			currentView = "SEARCH";
			recipeListView.setItems(controller.getSearchResults());
			clearDetails();
		});

		// Αποθήκευση και Φόρτωση. Καλούν τις μεθόδους του Controller
		saveBtn.setOnAction(e -> {
			controller.saveToDisk();
			showAlert("Αποθήκευση", "Οι λίστες σας αποθηκεύτηκαν επιτυχώς στο δίσκο!");
		});
		loadBtn.setOnAction(e -> {
			controller.loadFromDisk();
			currentView = "FAV"; // Πάμε στα Αγαπημένα για να δει ο χρήστης τι φόρτωσε
			recipeListView.setItems(controller.getFavorites());
			showAlert("Φόρτωση", "Οι λίστες φορτώθηκαν!");
		});

		// Κουμπιά προσθήκης σε λίστες
		favBtn.setOnAction(e -> {
			Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
			controller.addToFavorites(selected);
			showAlert("Επιτυχία", "Η συνταγή προστέθηκε στα Αγαπημένα!");
		});

		cookedBtn.setOnAction(e -> {
			Recipe sel = recipeListView.getSelectionModel().getSelectedItem();
			controller.markAsCooked(sel);
			showAlert("Επιτυχία", "Η συνταγή σημειώθηκε ως Μαγειρεμένη!");
		});
		// Διαγραφή
		deleteBtn.setOnAction(e -> {
			Recipe sel = recipeListView.getSelectionModel().getSelectedItem();
			if (sel != null) {
				if (currentView.equals("FAV"))
					controller.removeFromFavorites(sel);
				else if (currentView.equals("COOKED"))
					controller.removeFromCooked(sel);
				recipeListView.refresh();// Ανανέωση λίστας
				clearDetails(); // Καθαρίζουμε μετά τη διαγραφή
				showAlert("Διαγραφή", "Η συνταγή διαγράφηκε.");
			}
		});
		// Αρχικό μήνυμα δεξιά στην οθόνη
		detailsArea.setText("      Καλώς ήρθατε στο Meal Lab!\n\n" + "Εδω θα βρείτε τις πιο νόστιμες συνταγές!\n"
				+ "          Καλά Μαγειρέματα!");

		// Τελική Διάταξη Σκηνής

		BorderPane root = new BorderPane();
		root.setTop(topBox);
		root.setLeft(leftBox);
		root.setCenter(centerBox);

		Scene scene = new Scene(root, 850, 550);
		primaryStage.setTitle("Meal Lab App");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// Βοηθητική μέθοδος
	private void showRecipeDetails(Recipe recipe) {

		Recipe full = controller.getFullRecipeDetails(recipe.getId());

		String txt = "=== " + full.getName().toUpperCase() + " ===\n" + "Κατηγορία: " + full.getCategory() + "\n\n"
				+ "--- ΥΛΙΚΑ ---\n" + full.getIngredients() + "\n\n" + "--- ΟΔΗΓΙΕΣ ---\n" + full.getInstructions();
		detailsArea.setText(txt);
		detailsLabel.setVisible(true);

		try {
			if (full.getImageUrl() != null)
				mealImageView.setImage(new Image(full.getImageUrl()));
			else
				mealImageView.setImage(null);
		} catch (Exception e) {
			mealImageView.setImage(null);
		}

		favBtn.setDisable(false);
		cookedBtn.setDisable(false);
		deleteBtn.setDisable(currentView.equals("SEARCH"));
	}

	/*
	 * Κατασκευάζουμε τη μέθοδο ClearDetails: που καθαρίζει το μέρος της οθόνης όταν
	 * αλλάζουμε κατηγορία ή κάνουμε νέα αναζήτηση.
	 */

	private void clearDetails() {
		detailsArea.clear();
		mealImageView.setImage(null);

		// κρύβουμε την ετικέτα όταν καθαρίζουμε την οθόνη;
		if (detailsLabel != null) {
			detailsLabel.setVisible(false);
		}

		favBtn.setDisable(true);
		cookedBtn.setDisable(true);
		deleteBtn.setDisable(true);
		openDetailsBtn.setDisable(true); // Απενεργοποιούμε και το κουμπί προβολής
	}

	// Βοηθητική μέθοδος για εμφάνιση μηνυμάτων (Pop-up Alerts)
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
