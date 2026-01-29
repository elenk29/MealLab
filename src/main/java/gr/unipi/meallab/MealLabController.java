package gr.unipi.meallab;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import gr.unipi.meallab.model.Recipe;
import gr.unipi.meallab.service.MealService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*Δημιουργώ την κλάση MealLabController. Συνδέει τη λογική της εφαρμογής (MealService)
  με το γραφικό περιβάλλον (MealLabApp). Επίσης διατηρεί τις λίστες και κάνει αποθήκευση,
  διαγραφή και ελέγχους. */

public class MealLabController {

    // Δημιουργία αντικειμένου της υπηρεσίας για επικοινωνία με το API.
    private final MealService mealService = new MealService();

    /* Χρήση ObservableList αντί για απλή ArrayList για να γίνει το Data Binding. 
       Όταν προσθέτουμε συνταγή η JavaFX θα ενημερώνει αυτόματα την οθόνη.*/
    
    // Λίστα που αποθηκεύει τα τρέχοντα αποτελέσματα της αναζήτησης.
    private final ObservableList<Recipe> searchResults = FXCollections.observableArrayList();
    
    // Λίστα για την αποθήκευση των αγαπημένων συνταγών.
    private final ObservableList<Recipe> favorites = FXCollections.observableArrayList();
    
    // Λίστα για την αποθήκευση των εκτελεσμένων συνταγών.
    private final ObservableList<Recipe> cooked = FXCollections.observableArrayList();

    // Λειτουργίες Αναζήτησης και Ανάκτησης
    
    /*Αναζήτηση συνταγών βάσει ονόματος.
      query: Το κείμενο που πληκτρολoγεί ο χρήστης (π.χ. "pizza").*/
      public boolean search(String query) {
        // Καθαρίζουμε τα παλιά αποτελέσματα πριν ψάξουμε
        searchResults.clear(); 

        if (query != null && !query.trim().isEmpty()) {
            List<Recipe> meals = mealService.searchByName(query);
            
            // Έλεγχος: Αν το API γύρισε null ή κενή λίστα
            if (meals == null || meals.isEmpty()) {
                return false; // Δεν βρέθηκε τίποτα
            }
            
            // Αν βρέθηκαν, τα βάζουμε στη λίστα
            searchResults.setAll(meals);
            return true; // Επιτυχία
        }
        return false; // Ήταν κενό το πεδίο
    }     

    //Ανάκτηση τυχαίας συνταγής από το API.
    
    public void randomRecipe() {
        Recipe r = mealService.getRandomMeal();
        if (r != null) {
            // Ενημερώνουμε τη λίστα αποτελεσμάτων με τη μία τυχαία συνταγή.
            searchResults.setAll(r);
        }
    }

    /*Aνάκτηση των λεπτομερειών μιας συνταγής.
      Θέλουμε όταν ο χρήστης κάνει κλικ σε μια συνταγή από τη λίστα,
      να εμφανιστούν τα Υλικά (με τα bullets ■) και τις Οδηγίες.
      id: Το αναγνωριστικό της συνταγής.
      return: Το αντικείμενο Recipe με συμπληρωμένα όλα τα πεδία.*/
    
    public Recipe getFullRecipeDetails(String id) {
        return mealService.getById(id);
    }

    // Διαχείριση Λιστών Χρήστη

    /*Προσθήκη συνταγής στη λίστα των Αγαπημένων.
      Έλεγχος να μην προστεθεί η ίδια συνταγή δύο φορές.*/
    
    public void addToFavorites(Recipe recipe) {
        if (recipe != null && !favorites.contains(recipe)) {
            favorites.add(recipe);
            System.out.println("Επιτυχής προσθήκη της συνταγής " + recipe.getName() + " στα Αγαπημένα σας!");
        }
    }

    /*Προσθήκη συνταγής στις Μαγειρεμένες.
      Έλεγχος να μην προστεθεί η ίδια συνταγή δύο φορές.*/    
    public void markAsCooked(Recipe recipe) {
        if (recipe != null && !cooked.contains(recipe)) {
            cooked.add(recipe);
            System.out.println("Η συνταγή " + recipe.getName()+ " σημειώθηκε ως μαγειρεμένη.");
        }
    }
    
    // Λειτουργία αποθήκευσης και ανάκτησης.
    
    /* Αποθηκεύονται οι  λίστες "Αγαπημένα" και "Μαγειρεμένα" σε τοπικό αρχείο JSON.
       Χρησιμοποιείται η τεχνική της Σειριοποίησης (Serialization) μέσω της βιβλιοθήκης Jackson.
       Τα αντικείμενα Java μετατρέπονται σε κείμενο μορφής JSON όταν κλείσει η εφαρμογή*/
     
    public void saveToDisk() {
        try {
            // Δημιουργία του Mapper που μετατρέπει Objects σε JSON
            ObjectMapper mapper = new ObjectMapper();
            
            /* Δημιουργία Map για ομαδοποίηση των δεδομένων.
               Φτιάχνουμε ένα αντικείμενο που περιέχει δύο λίστες "favorites" και "cooked"*/
            
            Map<String, List<Recipe>> data = new HashMap<>();
            data.put("favorites", new ArrayList<>(favorites)); 
            data.put("cooked", new ArrayList<>(cooked));

            // Εγγραφή των δεδομένων στο αρχείο "meallab_data.json"
            mapper.writeValue(new File("meallab_data.json"), data);
            
            System.out.println("Η αποθήκευση των λιστών ολοκληρώθηκε επιτυχώς.");
            
        } catch (IOException e) {
            // Διαχείριση σφαλμάτων εισόδου/εξόδου (π.χ. αν ο δίσκος είναι γεμάτος)
            System.err.println("Σφάλμα κατά την αποθήκευση: " + e.getMessage());
        }
    }

    /*Ανάκτηση των αποθηκευμένων δεδομένων από το δίσκο κατά την εκκίνηση ή κατόπιν αιτήματος.
      Χρησιμοποιεί την Απο-σειριοποίηση (Deserialization) για να μετατρέψει το JSON
      πίσω σε λίστες αντικειμένων Recipe.*/
    
    public void loadFromDisk() {
        try {
            File file = new File("meallab_data.json");
            
            // Έλεγχος αν υπάρχει το αρχείο (για να αποφύγουμε σφάλματα στην πρώτη εκτέλεση).
            if (file.exists()) {
                ObjectMapper mapper = new ObjectMapper();
                
                /* Χρήση TypeReference για ασφαλή φόρτωση δεδομένων.
                   Επειδή η Java κατά την εκτέλεση δεν θυμάται τι είδους αντικείμενα περιέχουν οι λίστες (Type Erasure),
                   χρησιμοποιούμε το TypeReference προκειμένου η βιβλιοθήκη Jackson να μετατρέψει το JSON σε Συνταγές (Recipe) και όχι
                   σε γενικά αντικείμενα*/
                
                TypeReference<Map<String, List<Recipe>>> typeRef = new TypeReference<>() {};
                
                // Ανάγνωση και μετατροπή
                Map<String, List<Recipe>> data = mapper.readValue(file, typeRef);

                // Ενημέρωση των ObservableLists (UI) με τα δεδομένα που φορτώθηκαν
                if (data.containsKey("favorites")) {
                    favorites.setAll(data.get("favorites"));
                }
                if (data.containsKey("cooked")) {
                    cooked.setAll(data.get("cooked"));
                }
                System.out.println("Οι λίστες φορτώθηκαν επιτυχώς από το αρχείο.");     
            }
            else {
            System.out.println("Δεν βρέθηκε αρχείο αποθήκευσης.");
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την ανάγνωση του αρχείου: " + e.getMessage());
        }
    }
              
    
    //Διαγραφή Συνταγής
    
     /*Διαγραφή συνταγής από τη λίστα των Αγαπημένων με τη μέθοδο removeFromFavorites .
      recipe: Η συνταγή προς διαγραφή. */
    public void removeFromFavorites(Recipe recipe) {
    	/* Έλεγχος εγκυρότητας ότι υπάρχει η συνταγή στα αγαπημένα και διαγραγή.
           Με την επιτυχή διαγραφή του αντικειμένου η μέθοδος επιστρέφει ανάλογο κείμενο*/   	
  
        if (recipe != null && favorites.remove(recipe)) {
            System.out.println("Επιτυχής διαγραφή της συνταγής " + recipe.getName() + " από τα Αγαπημένα.");
        }
    }

    /*Διαγραφή συνταγής από τη λίστα των μαγειρεμένων με τη μέθοδο removeFromCooked.
      recipe: Η συνταγή προς διαγραφή. */
    public void removeFromCooked(Recipe recipe) {
    	/* Έλεγχος εγκυρότητας ότι υπάρχει η συνταγή στα μαγειρεμένα και διαγραγή.
        Με την επιτυχή διαγραφή του αντικειμένου η μέθοδος επιστρέφει ανάλογο κείμενο*/ 	
      	
        if (recipe != null && cooked.remove(recipe)) {
            System.out.println("Επιτυχής διαγραφή της συνταγής " + recipe.getName() + " από τα Μαγειρεμένα.");
        }
    }
    /* Getters για σύνδεση με το γραφικό περιβάλλον. 
    Οι λίστες είναι δηλωμένες ως private, οπότε για να εμφανιστούν στα ListView 
    της εφαρμογής θα περάσουν  οι getters τα δεδομένα απ'τον Controller στην Οθόνη.*/
 
 public ObservableList<Recipe> getSearchResults() { 
     return searchResults; 
 }

 public ObservableList<Recipe> getFavorites() { 
     return favorites; 
 }

 public ObservableList<Recipe> getCooked() { 
     return cooked; 
 }
}