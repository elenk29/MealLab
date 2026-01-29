package gr.unipi.meallab.service;

import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
/*Εισάγουμε τη βιβλιοθήκη Jackson (ObjectMapper) που αναλαμβάνει 
   να μετατρέψει το κείμενο JSON σε αντικείμενα Java. */
import com.fasterxml.jackson.databind.ObjectMapper;

/* Εισάγουμε τις κλάσεις Recipe και RecipeResponse από το πακέτο model 
   για να μπορεί η MealService να τις αναγνωρίσει*/
import gr.unipi.meallab.model.Recipe;
import gr.unipi.meallab.model.RecipeResponse;

/*Η κλάση MealService συνδέεται με το REST API, λαμβάνει τα δεδομένα και τα 
  μετατρέπει τα δεδομένα σε αντικείμενα Java. */
public class MealService {

	// Η βασική διεύθυνση (URL) του API από όπου παίρνουμε τα δεδομένα.
	private final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

	// Ο ObjectMapper της Jackson αναλαμβάνει τη μετάφραση από JSON σε Java.
	private final ObjectMapper objectMapper;

	/*Δημιουργώ Constructor που αρχικοποιεί το εργαλείο ObjectMapper. Η κλάση είναι
	  σε θέση να κάνει deserialization αμέσως μετά την κλήση της.*/
	public MealService() {
		this.objectMapper = new ObjectMapper();
	}

	/*Μέθοδος για αναζήτηση συνταγών με βάση το όνομα. name: Το όνομα της συνταγής
	 * που πληκτρολογεί ο χρήστης. return: Μια λίστα με αντικείμενα Recipe ή null σε
	 * περίπτωση σφάλματος.*/

	public List<Recipe> searchByName(String name) {

		/*Κατασκευάζουμε το πλήρες URL δυναμικά προσθέτοντας το όνομα που έδωσε ο
		 * χρήστης. Θέλουμε να μην υπάρχει ευαισθησία στα κενά μεταξύ των λέξεων.*/
		String safeName = name.replace(" ", "%20");
		String urlString = BASE_URL + "search.php?s=" + safeName;

		try {
			/*Δημιουργίa νέου URL αντικειμένου. Χρησιμοποιούμε το URI για έλεγχο
			 * εγκυρότητας και μετά το μετατρέπουμε σε URL. */
			URL url = URI.create(urlString).toURL();

			/* Ο objectMapper διαβάζει τα δεδομένα από αυτό το URL, μετατρέπει το JSON
			   αυτόματα σε αντικείμενο κλάσης RecipeResponse και κάνει αντιστοίχιση
			   Ονομάτων, δηλαδή ψάχνει στο JSON να βρει κλειδιά που έχουν το ίδιο όνομα με
			   τις μεταβλητές*/
			RecipeResponse response = objectMapper.readValue(url, RecipeResponse.class);

			// Αν δεν είναι null το response και η λίστα meals.
			if (response != null && response.getMeals() != null) {
				return response.getMeals();
			}
		} catch (Exception e) {
			/*Exception Handling: Αν προκύψει πρόβλημα (π.χ. διακοπή internet), το catch
			 * "πιάνει" το σφάλμα και το εμφανίζει και έτσι εμποδίζει την κατάρρευση της
			 * εφαρμογής*/
			System.err.println("Παρουσιάστηκε πρόβλημα κατά την επικοινωνία με το API: " + e.getMessage());
		}
		/* Αν έγινε σφάλμα ή δεν βρέθηκε κάτι στην αναζήτηση επιστρέφει μια άδεια λίστα
		   αντί για null*/
		return Collections.emptyList();
	}

	/* Πρώτη λειτουργική επέκταση: Aναζήτηση συγκεκριμένης συνταγής βάσει του ID της.
	   Μέθοδος που καλεί όλες τις λεπτομέρεις μίας συνταγής. id: Ο μοναδικός
	   κωδικός της συνταγής (π.χ. "52777"). return: Ένα αντικείμενο Recipe, ή null
	   αν δεν βρεθεί η συνταγή.*/
	public Recipe getById(String id) {
		// Σχηματισμός του URL για την εύρεση συνταγής με βάση το id.
		String urlString = BASE_URL + "lookup.php?i=" + id;
		try {
			/* Δημιουργίa νέου URL αντικειμένου. Χρησιμοποιούμε το URI για έλεγχο
			   εγκυρότητας και μετά το μετατρέπουμε σε URL. */
			URL url = URI.create(urlString).toURL();

			/* Χρησιμοποιούμε το JsonNode (Tree Model) για να πλοηγηθούμε στο JSON
			   χειροκίνητα.*/
			JsonNode root = objectMapper.readTree(url);
			JsonNode mealNode = root.get("meals").get(0); // Παίρνουμε το πρώτο (και μοναδικό) γεύμα.

			/*Γεμίζουμε αυτόματα τα πεδία που έχουν @JsonProperty (Name, Category, κλπ).
			 * Εδώ γίνεται το deserialization*/
			Recipe recipe = objectMapper.treeToValue(mealNode, Recipe.class);

			/* Το API στέλνει τα υλικά σε 20 πεδία (strIngredient1 έως 20) και τις ποσότητες
			   σε άλλα 20 (strMeasure1 έως 20).*/
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i <= 20; i++) {
				// Διαβάζουμε δυναμικά το i-οστό υλικό και την i-οστή ποσότητα.
				String ingredient = mealNode.get("strIngredient" + i).asText();
				String measure = mealNode.get("strMeasure" + i).asText();
				// Ελέγχουμε αν το υλικό υπάρχει (δεν είναι κενό, null ή η λέξη "null" ως
				// κείμενο).
				if (ingredient != null && !ingredient.trim().isEmpty() && !ingredient.equals("null")) {
					sb.append("■ ") // Βάζουμε το bullet point στην αρχή.
							.append(ingredient) // Προσθέτουμε το υλικό.
							.append(" (") // Ανοίγουμε παρένθεση για τη δόση.
							.append(measure) // Προσθέτουμε την ποσότητα.
							.append(")\n"); // Κλείνουμε παρένθεση και αλλάζουμε γραμμή (\n).
				}
			}
			// Αποθηκεύουμε όλο το κείμενο των υλικών στο πεδίο ingredients της συνταγής.
			recipe.setIngredients(sb.toString());
			return recipe;
			/* Χειρισμός εξαιρέσεων στην περίπτωση που δημιουργηθεί σφάλμα στην επικοινωνία
			   με το API. Εκτύπωση σφάλματος για να μην προκληθεί crash στην εφαρμογή.*/
		} catch (Exception e) {
			System.err.println("Σφάλμα στο ID " + id + ": " + e.getMessage());
		}
		return null;
	}

	/*Δεύτερη λειτουργική επέκταση:Μέθοδος που κάνει ανάκτηση τυχαίας συνταγής από
	  το API. return: Θα επιστρέφει ένα ανικείμενο Recipe.*/
	public Recipe getRandomMeal() {
		// Δημιουργίa νέου URL αντικειμένου που καλεί μια τυχαία συνταγή.
		String urlString = BASE_URL + "random.php";

		try {
			// Χρησιμοποιούμε το URI για έλεγχο εγκυρότητας και μετά το μετατρέπουμε σε URL.
			URL url = URI.create(urlString).toURL();
			// Μετατροπή του JSON response σε δέντρο.
			JsonNode root = objectMapper.readTree(url);
			JsonNode mealNode = root.get("meals").get(0);// Πάμε στη λίστα meals.
			// Αντιστοίχιση των πεδίων απ' το JSON στην κλάση Recipe.
			Recipe recipe = objectMapper.treeToValue(mealNode, Recipe.class);

			// Μαζεύουμε τα υλικά, το ένα κάτω απ' το άλλο.
			StringBuilder sb = new StringBuilder();

			// Σύμφωνα με το API, τα υλικά και οι ποσότητες είναι το πολύ 20 σε αριθμό.
			for (int i = 1; i <= 20; i++) {
				String ingredient = mealNode.get("strIngredient" + i).asText();
				String measure = mealNode.get("strMeasure" + i).asText();

				// Έλεγχος αν υπάρχει το υλικό, δηλαδή δεν είναι κενό ή null.
				if (ingredient != null && !ingredient.trim().isEmpty() && !ingredient.equals("null")) {

					sb.append("■ ") // Bullet point
							.append(ingredient) // Το όνομα του υλικού.
							.append(" (") // Ανοίγει παρένθεση για την ποσότητα.
							.append(measure).append(")\n"); // Κλείνει παρένθεση και το \n στέλνει το επόμενο υλικό στην
															// από κάτω γραμμή
				}
			}
			// Αποθηκεύουμε όλο το κείμενο των υλικών στο πεδίο ingredients του αντικειμένου
			// recipe.
			recipe.setIngredients(sb.toString());

			return recipe;// Επιστροφή της συνταγής.

		} catch (Exception e) {
			/*Χειρισμός εξαιρέσεων στην περίπτωση που δημιουργηθεί σφάλμα στην επικοινωνία
			  με το API. Εκτύπωση σφάλματος για να μην προκληθεί crash στην εφαρμογή.*/
			System.err.println("Σφάλμα στην τυχαία ανάκτηση: " + e.getMessage());
		}

		return null; // Αν κάτι πήγε στραβά, επιστρέφουμε null ως ασφάλεια.
	}
}