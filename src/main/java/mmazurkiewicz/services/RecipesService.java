package mmazurkiewicz.services;

import mmazurkiewicz.commands.RecipeCommand;
import mmazurkiewicz.domain.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipesService {
    public Flux<Recipe> getRecipes();
    public Mono<Recipe> findById(String l);
    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command);
    Mono<RecipeCommand> findCommandById(String l);
    Mono<Void> deleteById(String idToDelete);
}
