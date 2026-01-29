module gr.unipi.meallab {
	// Απαραίτητα JavaFX για το GUI
	requires javafx.controls;
	requires javafx.fxml;
	requires transitive javafx.graphics;

	// Βιβλιοθήκη Jackson για τη διαχείριση δεδομένων JSON
	requires com.fasterxml.jackson.databind;

	// Επιτρέπουμε στο JavaFX να έχει πρόσβαση στο πακέτο μας για την εκκίνηση της
	// εφαρμογής
	opens gr.unipi.meallab to javafx.graphics, javafx.fxml;

	/*
	 * Επιτρέπουμε στην Jackson να "διαβάζει" τα μοντέλα μας (π.χ. Recipe) για να
	 * μετατρέπει το JSON σε αντικείμενα Java (αποσειριοποίηση)
	 */
	opens gr.unipi.meallab.model to com.fasterxml.jackson.databind;

	// Εξαγωγή του κύριου πακέτου και μοντέλου και service
	exports gr.unipi.meallab;
	exports gr.unipi.meallab.model;

}
