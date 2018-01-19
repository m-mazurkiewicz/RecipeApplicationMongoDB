package mmazurkiewicz.services;

import lombok.extern.slf4j.Slf4j;
import mmazurkiewicz.commands.RecipeCommand;
import mmazurkiewicz.converters.RecipeCommandToRecipe;
import mmazurkiewicz.converters.RecipeToRecipeCommand;
import mmazurkiewicz.domain.Recipe;
import mmazurkiewicz.repositories.reactive.RecipeReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class RecipesServiceImpl implements RecipesService{

    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeToRecipeCommand recipeToRecipeCommand;
    private final RecipeCommandToRecipe recipeCommandToRecipe;

    public RecipesServiceImpl(RecipeReactiveRepository recipeReactiveRepository,
                              RecipeToRecipeCommand recipeToRecipeCommand, RecipeCommandToRecipe recipeCommandToRecipe) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
    }

    @Override
    public Flux<Recipe> getRecipes() {
        log.debug("In the service");
        return recipeReactiveRepository.findAll();
    }

    @Override
    public Mono<Recipe> findById(String id) {
        return recipeReactiveRepository.findById(id);
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String id) {
        return recipeReactiveRepository.findById(id)
                .map(recipe -> {
                    RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);

                    recipeCommand.getIngredients().forEach(rc -> {
                        rc.setRecipeId(recipeCommand.getId());
                    });
                    return recipeCommand;
                });
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) {

        return recipeReactiveRepository.save(recipeCommandToRecipe.convert(command))
                .map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<Void> deleteById(String idToDelete) {
        recipeReactiveRepository.deleteById(idToDelete).block();

        return Mono.empty();
    }
}
