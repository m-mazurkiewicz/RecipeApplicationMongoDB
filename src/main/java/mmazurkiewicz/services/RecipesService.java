package mmazurkiewicz.services;

import mmazurkiewicz.commands.RecipeCommand;
import mmazurkiewicz.domain.Recipe;

import java.util.Set;

public interface RecipesService {
    public Set<Recipe> getRecipes();
    public Recipe findById(String l);
    RecipeCommand saveRecipeCommand(RecipeCommand command);
    RecipeCommand findCommandById(String l);
    void deleteById(String idToDelete);
}
