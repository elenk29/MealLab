package gr.unipi.meallab.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true) // Αγνοεί πεδία του JSON που δεν έχουμε ορίσει στην κλάση.

/*Δημιουργία κλάσης Recipe που συνιστά POJO και αναπαριστά μία συνταγή.
Η Recipe αποθηκεύει τα δεδομένα που ανακτώνται από το TheMealDB API. */

public class Recipe {

    // Με το @JsonProperty συνδέουμε το όνομα του πεδίου στο JSON (idMeal) με τη μεταβλητή μας (id).
    @JsonProperty("idMeal")
    private String id;

    @JsonProperty("strMeal") // Το όνομα της συνταγής στο API.
    private String name;

    @JsonProperty("strInstructions") // Οι οδηγίες εκτέλεσης.
    private String instructions;

    @JsonProperty("strMealThumb") // Το link για τη φωτογραφία του φαγητού.
    private String imageUrl;

    /* Κενός κατασκευαστής (Default Constructor) προκειμένου
     * η βιβλιοθήκη Jackson να μπορεί δημιουργήσει το αντικείμενο.*/
    
    public Recipe() {}

    //Getters και Setters για πρόσβαση στα private πεδία (encapsulation).

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

    // Ζητάμε να επιστρέψει το όνομα μιας συνταγής.
    
    @Override
    public String toString() {
        return name;
    }
}