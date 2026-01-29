package gr.unipi.meallab;

/* Εισάγουμε τις κλάσεις που χρειαζόμαστε από τα άλλα πακέτα */
import gr.unipi.meallab.model.Recipe;
import gr.unipi.meallab.service.MealService;
import java.util.List;

/*Δημιουργία κλάσης MainTest για έλεγχο και επαλήθευση λειτουργίας της βιβλιοθήκης.
  Αναλυτικότερα, ελέγχεται η επικοινωνία με το REST API και η αποσειριοποίηση.*/
public class MainTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// 1. Δημιουργία αντικειμένου της υπηρεσίας (Service)
		// Μηχανισμός που κάνει τις κλήσεις στο API.
		MealService service = new MealService();

		System.out.println("Έναρξη Λειτουργικού Ελέγχου MealLabAPI");
		System.out.println("Δοκιμή αναζήτησης της λέξης: 'pasta'");

		try {
			// 2. Εκτέλεση αναζήτησης
			// Καλούμε τη μέθοδο searchByName και αποθηκεύουμε το αποτέλεσμα σε μια λίστα.
			List<Recipe> results = service.searchByName("pasta");

			// 3. Έλεγχος αποτελεσμάτων (Null & Empty Check)
			// Ελέγχουμε αν η λίστα που ήρθε είναι έγκυρη και αν περιέχει συνταγές.
			if (results != null && !results.isEmpty()) {

				System.out.println("Η σύνδεση και η ανάκτηση ολοκληρώθηκαν.");
				System.out.println("Βρέθηκαν συνολικά: " + results.size() + " συνταγές.");

				// 4. Εμφάνιση στοιχείων της πρώτης συνταγής
				// Παίρνουμε το πρώτο αντικείμενο από τη λίστα για να ελέγξουμε το Mapping.
				Recipe firstRecipe = results.get(0);

				System.out.println("\n Στοιχεία Πρώτου Αποτελέσματος");
				System.out.println("ID Συνταγής: " + firstRecipe.getId());
				System.out.println("Όνομα: " + firstRecipe.getName());
				System.out.println("Οδηγίες: " + firstRecipe.getInstructions());
				System.out.println("Φωτογραφία: " + firstRecipe.getImageUrl());

				System.out.println("\n Ο έλεγχος ολοκληρώθηκε επιτυχώς!");

			} else {
				// Σε περίπτωση που δεν βρεθεί κάτι (π.χ. λάθος λέξη pastta)
				System.out.println("Η αναζήτηση ολοκληρώθηκε αλλά δεν βρέθηκαν αποτελέσματα.");
			}

		} catch (Exception e) {
			// Διαχείριση σφαλμάτων (π.χ. αν διακοπεί στο internet)
			System.err.println("Προέκυψε σφάλμα κατά τη δοκιμή: " + e.getMessage());
		}
	}
}