package gr.unipi.meallab;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import gr.unipi.meallab.service.MealService;
import gr.unipi.meallab.model.Recipe;

public class MealLabApp extends Application {
    // Σύνδεση με την κλάση που φέρνει τα δεδομένα από το Internet
    private MealService mealService = new MealService();

    @Override
    public void start(Stage primaryStage) {
        // Τίτλος παραθύρου
        primaryStage.setTitle("Meal Lab App");

        // Δημιουργία των στοιχείων της οθόνης
        TextField searchField = new TextField(); // Πεδίο κειμένου
        searchField.setPromptText("Πληκτρολογήστε υλικό...");
        
        Button searchBtn = new Button("Αναζήτηση"); // Κουμπί Αναζήτησης
        Button randomBtn = new Button("Τυχαία Συνταγή"); // Κουμπί για τυχαία συνταγή
        
        ListView<String> listView = new ListView<>(); // Λίστα που θα δείχνει τα αποτελέσματα

        // Οργάνωση των κουμπιών σε μια οριζόντια γραμμή (HBox)
        HBox topBar = new HBox(10, searchField, searchBtn, randomBtn);
        topBar.setPadding(new Insets(10)); // Κενό γύρω από τα κουμπιά
        
        // Κεντρικό layout που βάζει τη μπάρα πάνω και τη λίστα στο κέντρο
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(listView);

        //Τι βγάζει η αναζήτηση
        searchBtn.setOnAction(e -> {
            // Παίρνουμε το κείμενο από το searchField και ψάχνουμε
            var results = mealService.searchByName(searchField.getText());
            listView.getItems().clear(); // Καθαρίζουμε τη λίστα από πριν
            
            if (results.isEmpty()) {
                // Αν δεν βρει τίποτα, βγάζει μήνυμα (Alert)
                showAlert("Ενημέρωση", "Δεν βρέθηκαν συνταγές για αυτό το υλικό.");
            } else {
                // Αν βρει, τα προσθέτει στη λίστα.
                results.forEach(r -> listView.getItems().add(r.getName()));
            }
        });

        //Τι γίνεται όταν πατάμε τυχαία αναζήτηση
        randomBtn.setOnAction(e -> {
            Recipe r = mealService.getRandomMeal(); // Ζητάμε από το API μια τυχαία συνταγή
            if (r != null) {
                listView.getItems().clear();
                listView.getItems().add("ΠΡΟΤΑΣΗ: " + r.getName());
                // Βγάζουμε αναδυόμενο παράθυρο με την πρόταση
                showAlert("Τυχαία Επιλογή", "Σας προτείνουμε να δοκιμάσετε: " + r.getName());
            }
        });

        // Ρύθμιση μεγέθους παραθύρου και εμφάνιση
        primaryStage.setScene(new Scene(root, 700, 450));
        primaryStage.show();
    }

    // Μέθοδος για τη δημιουργία αναδυόμενων παραθύρων (Alerts)
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) { launch(args); }
}