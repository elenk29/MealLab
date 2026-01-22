package gr.unipi.meallab.service;
/* Εισάγουμε τις κλάσεις Recipe και RecipeResponse από το πακέτο model 
   για να μπορεί η MealService να τις αναγνωρίσει*/
import gr.unipi.meallab.model.Recipe;
import gr.unipi.meallab.model.RecipeResponse;
/*Εισάγουμε τη βιβλιοθήκη Jackson (ObjectMapper) που αναλαμβάνει 
   να μετατρέψει το κείμενο JSON σε αντικείμενα Java. */
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URL;
import java.util.List;

/*Η κλάση MealService συνδέεται με το REST API και 
  μετατρέπει τα δεδομένα σε αντικείμενα Java. */

public class MealService {

	// Η βασική διεύθυνση (URL) του API από όπου παίρνουμε τα δεδομένα.
    private final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
  
    // Mετάφραση από JSON σε Java.
    private final ObjectMapper objectMapper;

    /*Δημιουργώ Constructor που αρχικοποιεί το εργαλείο ObjectMapper.
      Η κλάση είναι σε θέση να κάνει deserialization αμέσως μετά την κλήση της.*/
    public MealService() {
        this.objectMapper = new ObjectMapper();
    }

    /* Μέθοδος για αναζήτηση συνταγών με βάση το όνομα.
     * @param name: Το όνομα της συνταγής που πληκτρολογεί ο χρήστης.
     * @return: Μια λίστα με αντικείμενα Recipe ή null σε περίπτωση σφάλματος.*/
    public List<Recipe> searchByName(String name) {
        
        // Κατασκευάζουμε το πλήρες URL δυναμικά προσθέτοντας το όνομα που έδωσε ο χρήστης.
        String urlString = BASE_URL + "search.php?s=" + name;
        
        try {
            /*Δημιουργίa νέου URL αντικειμένου.
        	Χρησιμοποιούμε το URI για έλεγχο εγκυρότητας και μετά το μετατρέπουμε σε URL.*/
        	URL url = URI.create(urlString).toURL();
        	
        	/* Ο objectMapper διαβάζει τα δεδομένα από αυτό το URL,       
        	 * μετατρέπει το JSON αυτόματα σε αντικείμενο κλάσης RecipeResponse και
        	 * κάνει αντιστοίχιση Ονομάτων, δηλαδή ψάχνει στο JSON να βρει κλειδιά που έχουν το ίδιο
               όνομα με τις μεταβλητές*/
            RecipeResponse response = objectMapper.readValue(url, RecipeResponse.class);
            
            // Επιστρέφει τη λίστα των γευμάτων που περιέχεται μέσα στην απάντηση.
            return response.getMeals();
            
        } 
        catch (Exception e) {
            /* Exception Handling: Αν προκύψει πρόβλημα (π.χ. διακοπή internet), το catch "πιάνει" 
               το σφάλμα και το εμφανίζει και έτσι εμποδίζει την κατάρρευση της εφαρμογής. */
            System.err.println("Παρουσιάστηκε πρόβλημα κατά την επικοινωνία με το API: " + e.getMessage());
            return null;
        }
    }
}