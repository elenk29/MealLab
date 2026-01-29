package gr.unipi.meallab.service;
//Εισαγωγή των στατικών μεθόδων Assertions της βιβλιοθήκης JUnit.
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

//Εισαγωγή της @Test που δηλώνει ποιες μέθοδοι είναι δοκιμές/
import org.junit.jupiter.api.Test;

import gr.unipi.meallab.model.Recipe;

// Κλάση ελέγχου MealService.

public class MealServiceTest {

    //1. Έλεγχος για την αναζήτηση συνταγών βάσει ονόματος. 
    @Test
    public void testSearchByName() {
        // Δημιουργία αντικειμένου της κλάσης MealService για τον έλεγχο.
        MealService service = new MealService();
        
        // Εκτέλεση αναζήτησης για τη λέξη "Chicken" (γνωστό δεδομένο του API)
        List<Recipe> results = service.searchByName("Chicken");
        
        //1os έλεγχος: Η λίστα δεν πρέπει να είναι null.
        assertNotNull(results, "Η υπηρεσία δεν πρέπει να επιστρέφει null αντικείμενο.");
        
        //2ος έλεγχος: Η λίστα δεν πρέπει να είναι άδεια, αφού έχουμε βάλει λέξη που έχει το API.
        assertFalse(results.isEmpty(), "Πρέπει να βρεθεί τουλάχιστον μία συνταγή για τη λέξη 'Chicken'.");
        
        //3ος έλεγχος: Επαληθεύω ότι η συνταγή έχει το όνομα που ζητήσαμε.
        assertTrue(results.get(0).getName().contains("Chicken"), 
                   "Το όνομα της συνταγής πρέπει να περιέχει τη λέξη 'Chicken'.");
              
        
    }
    
    /* Έλεγχος για ανάκτηση συνταγής με συγκεκριμένο ID.
       Χρησιμοποιούμε το ID 52777 για να επαληθεύσουμε ότι το API 
       επιστρέφει τη συνταγή Mediterranean Pasta Salad*/
    
    @Test
    public void testGetById() {
        // Δημιουργία αντικειμένου της κλάσης MealService για τον έλεγχο.
        MealService service = new MealService();
        
        // Εκτέλεση της μεθόδου getById με το ID 52777.
        Recipe recipe = service.getById("52777");
        
        // 1os έλεγχος: To API δεν επιστρέφει null.
        assertNotNull(recipe, "Σφάλμα: Η συνταγή με ID 52777 θα έπρεπε να βρεθεί.");
        
        // 2os έλεγχος: Tο όνομα της συνταγής είναι "Mediterranean Pasta Salad".
      
        assertEquals("Mediterranean Pasta Salad", recipe.getName(), 
                     "Το όνομα της συνταγής δεν ταιριάζει με το αναμενόμενο για το ID 52777.");
        
        // 3os έλεγχος: Η συνταγή περιέχει οδηγίες (δεν είναι null).
        assertNotNull(recipe.getInstructions(), "Οι οδηγίες της συνταγής δεν θα έπρεπε να είναι null.");
   
        //4ος  Έλεγχος ύπαρξης και μορφοποίησης υλικών.
        assertNotNull(recipe.getIngredients(), "Τα υλικά δεν πρέπει να είναι null.");
        assertTrue(recipe.getIngredients().contains("■"), "Τα υλικά πρέπει να περιέχουν το σύμβολο ■.");
        assertTrue(recipe.getIngredients().contains("\n"), "Τα υλικά πρέπει να είναι χωρισμένα με αλλαγή γραμμής.");
    
    
    }
  
    //3. Έλεγχος για την ανάκτηση τυχαίας συνταγής.
    @Test
    public void testGetRandomMeal() {
    	// Δημιουργία αντικειμένου της κλάσης MealService για τον έλεγχο.
        MealService service = new MealService();
        
        // Κλήση της μεθόδου για τυχαία συνταγή
        Recipe randomRecipe = service.getRandomMeal();
        
        //1os έλεγχος: Το αντικείμενο της συνταγής δεν πρέπει να είναι null.
        assertNotNull(randomRecipe, "Η τυχαία συνταγή δεν ανακτήθηκε σωστά από το API.");
        
        //2os έλεγχος: Η συνταγή πρέπει να έχει οπωσδήποτε όνομα.
        assertNotNull(randomRecipe.getName(), "Το όνομα της τυχαίας συνταγής δεν πρέπει να είναι κενό.");
        
        //3os έλεγχος: Η συνταγή πρέπει να έχει οπωσδήποτε οδηγίες μαγειρέματος.
        assertNotNull(randomRecipe.getInstructions(), "Οι οδηγίες της συνταγής δεν πρέπει να είναι κενές.");
        
        //4ος  Έλεγχος ύπαρξης και μορφοποίησης υλικών.
        assertNotNull(randomRecipe.getIngredients(), "Τα υλικά δεν πρέπει να είναι null.");
        assertTrue(randomRecipe.getIngredients().contains("■"), "Τα υλικά πρέπει να περιέχουν το σύμβολο ■.");
        assertTrue(randomRecipe.getIngredients().contains("\n"), "Τα υλικά πρέπει να είναι χωρισμένα με αλλαγή γραμμής.");
        
    }
}

