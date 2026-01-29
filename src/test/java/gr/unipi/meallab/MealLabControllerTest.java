package gr.unipi.meallab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import gr.unipi.meallab.model.Recipe;

/*Κλάση Ελέγχου (Unit Testing) για τον MealLabController.
  Εδώ ελέγχουμε αν η λογική της εφαρμογής (Αγαπημένα, Αναζήτηση κλπ.) δουλεύει σωστά*/

public class MealLabControllerTest {

    // Δηλώνουμε τον Controller που θα τεστάρουμε
    private MealLabController controller;
    // Δηλώνουμε μια ψεύτικη συνταγή που θα χρησιμοποιούμε στα πειράματα
    private Recipe testRecipe;

    /**
     * Η μέθοδος @BeforeEach τρέχει αυτότατα πριν από ΚΑΘΕ @Test.
     * Καθαρίζει το περιβάλλον ώστε κάθε τεστ να ξεκινάει από το μηδέν.
     */
    @BeforeEach
    public void setUp() {
        // 1. Δημιουργούμε έναν καινούριο, άδειο Controller
        controller = new MealLabController();
        
        // 2. Φτιάχνουμε μια δοκιμαστική συνταγή
        testRecipe = new Recipe();
        testRecipe.setId("7777");          // Τυχαίο ID
        testRecipe.setName("Test Chicken");  // Τυχαίο Όνομα
        testRecipe.setCategory("Poultry");   // Τυχαία Κατηγορία
    }

    /*1o Τεστ: Έλεγχος Λειτουργίας Προσθήκης στα Αγαπημένα.
      Σενάριο: Προσθέτω μια συνταγή και περιμένω να τη βρω στη λίστα.  */
    @Test
    @DisplayName("Έλεγχος: Προσθήκη στα Αγαπημένα")
    public void testAddToFavorites() {
        //Προσθέτουμε τη συνταγή μέσω του Controller
        controller.addToFavorites(testRecipe);
        
        //Παίρνουμε τη λίστα των αγαπημένων
        List<Recipe> favorites = controller.getFavorites();
        
        //Έλεγχος ότι λίστα πρέπει να έχει 1 στοχείο
        assertEquals(1, favorites.size(), "Σφάλμα: Η λίστα αγαπημένων έπρεπε να έχει ακριβώς 1 στοιχείο.");
        
        //Έλεγχος ότι το όνομα της συνταγής είναι 'Test Chicken'.
        assertEquals("Test Chicken", favorites.get(0).getName(), "Σφάλμα: Αποθηκεύτηκε λάθος συνταγή.");
    }

    /*2o Τεστ: Έλεγχος διαγραφής από τα Αγαπημένα.
     * Σενάριο: Προσθέτω μία συνταγή και μετά διαγράφω*/
    
    @Test
    @DisplayName("Έλεγχος: Διαγραφή από τα Αγαπημένα.")
    public void testRemoveFromFavorites() {
        // Προσθέτω τη συνταγή στα αγαπημένα.
        controller.addToFavorites(testRecipe);
        
        //Διαγράφω τη συνταγή από τα αγαπημένα.
        controller.removeFromFavorites(testRecipe);
        
        //Έλεγχος αν η λίστα είναι άδεια.
        assertTrue(controller.getFavorites().isEmpty(), "Σφάλμα: Η λίστα έπρεπε να είναι άδεια μετά τη διαγραφή.");
    }

    /*3ο Τεστ: Έλεγχος Λίστας 'Μαγειρεμένα'
      Σενάριο: Σημειώνω μια συνταγή ως μαγειρεμένη και ελέγχω αν καταγράφηκε.*/
  
    @Test
    @DisplayName("Έλεγχος: Σημείωση ως Μαγειρεμένη")
    public void testMarkAsCooked() {
        // Σημειώνουμε τη συνταγή ως μαγειρεμένη
        controller.markAsCooked(testRecipe);
        
        // Παίρνουμε τη λίστα
        List<Recipe> cooked = controller.getCooked();
        
        // Έλεγχος ότι δεν είναι άδεια
        assertFalse(cooked.isEmpty(), "Σφάλμα: Η λίστα μαγειρεμένων δεν πρέπει να είναι άδεια.");
        
        // Έλεγχος ότι έχει το σωστό όνομα.
        assertEquals("Test Chicken", cooked.get(0).getName());
    }
    /*4ο Τεστ: Έλεγχος τυχαίας συνταγής.
      Δεν ξέρουμε ποια θα έρθει, οπότε ελέγχουμε απλά ότι ήρθε μία*/
    @Test
    @DisplayName("Έλεγχος: Τυχαία Συνταγή")
    public void testRandomRecipe() {
        // Καλούμε τη μέθοδο για τυχαία συνταγή.
        controller.randomRecipe();
        
        // Παίρνουμε τα αποτελέσματα.
        List<Recipe> results = controller.getSearchResults();
        
        // Έλεγχοι:
        assertNotNull(results, "Η λίστα αποτελεσμάτων δεν πρέπει να είναι null.");
        // Πρέπει να έχει επιστρέψει ακριβώς 1 συνταγή
        assertEquals(1, results.size(), "Η λειτουργία Random πρέπει να φέρνει ακριβώς 1 συνταγή.");
        // Η συνταγή αυτή πρέπει να έχει όνομα (να μην είναι κενή).
        assertNotNull(results.get(0).getName(), "Η τυχαία συνταγή πρέπει να έχει όνομα.");
    }
    
    /*5ο Τεστ: Έλεγχος Ασφάλειας Αναζήτησης *
      Σενάριο: Κάνουμε μια αναζήτηση και ελέγχουμε ότι η λίστα αποτελεσμάτων
      αρχικοποιείται σωστά (δεν είναι null), ακόμα κι αν δεν βρει τίποτα.*/
     
    @Test
    @DisplayName("Έλεγχος: Αρχικοποίηση Αναζήτησης")
    public void testSearchSafety() {
        // Κάνουμε αναζήτηση για κάτι άσχετο
        controller.search("αacdgjx");
        
        // Η μέθοδος getSearchResults() δεν πρέπει να επιστρέφει null,
        // αλλά μια άδεια λίστα (empty list). Αν επέστρεφε null θα είχαμε crash.
        assertNotNull(controller.getSearchResults(), "Σφάλμα: Η λίστα αποτελεσμάτων δεν πρέπει ποτέ να είναι null.");
    }
}