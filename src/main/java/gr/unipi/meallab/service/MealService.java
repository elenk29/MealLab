package gr.unipi.meallab.service;
import java.net.URI;
import java.net.URL;
import java.util.List;

/*Εισάγουμε τη βιβλιοθήκη Jackson (ObjectMapper) που αναλαμβάνει 
   να μετατρέψει το κείμενο JSON σε αντικείμενα Java. */
import com.fasterxml.jackson.databind.ObjectMapper;

/* Εισάγουμε τις κλάσεις Recipe και RecipeResponse από το πακέτο model 
   για να μπορεί η MealService να τις αναγνωρίσει*/
import gr.unipi.meallab.model.Recipe;
import gr.unipi.meallab.model.RecipeResponse;

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
     * name: Το όνομα της συνταγής που πληκτρολογεί ο χρήστης.
     * return: Μια λίστα με αντικείμενα Recipe ή null σε περίπτωση σφάλματος.*/
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
            
            // Αν δεν είναι null το response και η λίστα meals
            if (response != null && response.getMeals() != null) {
                return response.getMeals();     
            }
        }    
        catch (Exception e) {
            /* Exception Handling: Αν προκύψει πρόβλημα (π.χ. διακοπή internet), το catch "πιάνει" 
               το σφάλμα και το εμφανίζει και έτσι εμποδίζει την κατάρρευση της εφαρμογής. */
            System.err.println("Παρουσιάστηκε πρόβλημα κατά την επικοινωνία με το API: " + e.getMessage());
        } 
            /* Αν έγινε σφάλμα ή δεν βρέθηκε κάτι στην αναζήτηση  
               επιστρέφει μια άδεια λίστα αντί για null*/
            return java.util.Collections.emptyList();
        }
        
    /* Πρώτη λειτουργική επέκταση: Aναζήτηση συγκεκριμένης συνταγής βάσει του ID της.
       Μέθοδος που καλεί όλες τις λεπτομέρεις μίας συνταγής.
       id: Ο μοναδικός κωδικός της συνταγής (π.χ. "52777").
       return: Ένα αντικείμενο Recipe, ή null αν δεν βρεθεί η συνταγή.*/
    public Recipe getById(String id) {
        // Σχηματισμός του URL για την εύρεση συνταγής με βάση το id.
        String urlString = BASE_URL + "lookup.php?i=" + id;
        try {
        	/*Δημιουργίa νέου URL αντικειμένου.
        	Χρησιμοποιούμε το URI για έλεγχο εγκυρότητας και μετά το μετατρέπουμε σε URL.*/
        	URL url = URI.create(urlString).toURL();
            
            // Μετατροπή JSON σε αντικείμενο RecipeResponse μέσω Jackson.
            RecipeResponse response = objectMapper.readValue(url, RecipeResponse.class);
            
            // Επιβεβαίωση ότι η απάντηση δεν είναι κενή πριν την επιστροφή της συνταγής.
            if (response != null && response.getMeals() != null && !response.getMeals().isEmpty()) {
                return response.getMeals().get(0);
            }
        } catch (Exception e) {
            System.err.println("Σφάλμα κατά την ανάκτηση του ID " + id + ": " + e.getMessage());
        }
        return null;
    }

    /*Δεύτερη λειτουργική επέκταση: Ανάκτηση μιας τυχαίας συνταγής από το API.
      Μέθοδος που καλεί μια τυχαία συνταγή.
      @return: Ένα τυχαίο αντικείμενο Recipe, ή null σε περίπτωση προβλήματος σύνδεσης.*/
    public Recipe getRandomMeal() {
        // Προετοιμασία του URL για την ανάκτηση τυχαίου γεύματος.
        String urlString = BASE_URL + "random.php";
        try {
            /*Δημιουργίa νέου URL αντικειμένου.
        	Χρησιμοποιούμε το URI για έλεγχο εγκυρότητας και μετά το μετατρέπουμε σε URL.*/
            URL url = URI.create(urlString).toURL();
            
            //Μετατροπή του JSON response σε αντικείμενο RecipeResponse μέσω Jackson.            
            RecipeResponse response = objectMapper.readValue(url, RecipeResponse.class);
            
            // Έλεγχος ότι η απάντηση δεν είναι κενή.
            if (response != null && response.getMeals() != null && !response.getMeals().isEmpty()) {
                return response.getMeals().get(0);
            }
        } catch (Exception e) {
            System.err.println("Αποτυχία επικοινωνίας με το API κατά την τυχαία ανάκτηση:" + e.getMessage());
        }
        return null;
    }       
}