package gr.unipi.meallab.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/*Η κλάση RecipeResponse υποδέχεται την απάντηση (Response) από το API του TheMealDB.
  Παίρνει το πακέτο από το internet και το βάζει μέσα στην εφαρμογή μας σε μορφή λίστας.
  Εδώ συγκεντρώνονται οι πληροφορίες των συνταγών κατά την ανάκτησή τους από το API. */
@JsonIgnoreProperties(ignoreUnknown = true) // Αγνοεί πεδία του JSON που δεν έχουμε ορίσει στην κλάση.
public class RecipeResponse {
	/*
	 * Εδώ αποθηκεύεται η λίστα με τα φαγητά. Την ονομάζουμε "meals" όπως και στο
	 * JSON του API https://www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata
	 */
	@JsonProperty("meals")
	private List<Recipe> meals;

	// Ο άδειος κατασκευαστής επιτρέπει στη βιβλιοθήκη Jackson να δημιουργήσει το
	// αντικείμενο.
	public RecipeResponse() {
	}

	// Μέθοδος για να παίρνουμε τη λίστα με τις συνταγές και να τις εμφανίζουμε.
	public List<Recipe> getMeals() {
		return meals;
	}

	// Μέθοδος που χρησιμοποιεί η Jackson για να "γεμίσει" τη λίστα με τα δεδομένα
	// που ήρθαν.
	public void setMeals(List<Recipe> meals) {
		this.meals = meals;
	}
}