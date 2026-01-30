package gr.unipi.meallab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) // Αγνοεί πεδία του JSON που δεν έχουμε ορίσει στην κλάση.

/*
 * Δημιουργία κλάσης Recipe που συνιστά POJO και αναπαριστά μία συνταγή. Η
 * Recipe αποθηκεύει τα δεδομένα που ανακτώνται από το TheMealDB API.
 */

public class Recipe {

	// Με το @JsonProperty συνδέουμε το όνομα του πεδίου στο JSON (idMeal) με τη
	// μεταβλητή μας (id).
	@JsonProperty("idMeal")
	private String id;

	@JsonProperty("strMeal") // Αντιστοίχιση του πεδίου "strMeal" στη μεταβλητή μας name.
	private String name;

	@JsonProperty("strInstructions") // Αντιστοίχιση του πεδίου "strInstructions" στη μεταβλητή μας instructions.
	private String instructions;

	@JsonProperty("strMealThumb") // Αντιστοίχιση του URL για τη φωτογραφία του φαγητού.
	private String imageUrl;

	@JsonProperty("strCategory") // Αντιστοίχιση της κατηγορίας φαγητού.
	private String category;

	@JsonProperty("strArea") // Αντιστοίχιση της περιοχή/εθνικότητας
	private String area;

	/*
	 * Αυτό δεν έχει @JsonProperty γιατί το API στέλνει τα υλικά σε πολλά
	 * πεδία. Θα ενώσουμε χειροκίνητα τα υλικά με τις ποσότητές τους σε ενιαίο
	 * κείμενο.
	 */
	private String ingredients;

	/*
	 * Κενός κατασκευαστής (Default Constructor) προκειμένου η βιβλιοθήκη Jackson να
	 * μπορεί δημιουργήσει το αντικείμενο.
	 */

	public Recipe() {
	}

	/*
	 * Getters και Setters για πρόσβαση στα private πεδία (encapsulation). θα
	 * επιτρέπουν την πρόσβαση και τροποποίηση των δεδομένων από το GUI.
	 */

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	// Ζητάμε να επιστρέψει το όνομα μιας συνταγής κατά τον έλεγχο αποτελεσμάτων.

	@Override
	public String toString() {
		// Επιστρέφει το όνομα, την κατηγορία και την περιοχή με απλό κείμενο
		return "Συνταγή: " + name + " | Κατηγορία: " + category + " | Περιοχή: " + area;

	}

}
