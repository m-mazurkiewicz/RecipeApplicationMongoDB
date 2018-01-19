package mmazurkiewicz.services;

import lombok.extern.slf4j.Slf4j;
import mmazurkiewicz.commands.IngredientCommand;
import mmazurkiewicz.converters.IngredientCommandToIngredient;
import mmazurkiewicz.converters.IngredientToIngredientCommand;
import mmazurkiewicz.domain.Ingredient;
import mmazurkiewicz.domain.Recipe;
import mmazurkiewicz.repositories.RecipeRepository;
import mmazurkiewicz.repositories.UnitOfMeasureRepository;
import mmazurkiewicz.repositories.reactive.RecipeReactiveRepository;
import mmazurkiewicz.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient ingredientCommandToIngredient,
                                 RecipeReactiveRepository recipeReactiveRepository,
                                 RecipeRepository recipeRepository,
                                 UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

        return recipeReactiveRepository.findById(recipeId)
                .map(recipe -> recipe.getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                    .findFirst())
                .filter(Optional::isPresent)
                .map(ingredient -> {
                    IngredientCommand command = ingredientToIngredientCommand.convert(ingredient.get());
                    command.setRecipeId(recipeId);
                    return command;
                });
    }

    @Override
    @Transactional
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeReactiveRepository.findById(command.getRecipeId()).blockOptional();

        if(!recipeOptional.isPresent()){

            log.error("Recipe not found for id: " + command.getRecipeId());
            return Mono.just(new IngredientCommand());
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if(ingredientOptional.isPresent()){
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUnitOfMeasure(unitOfMeasureReactiveRepository
                        .findById(command.getUnitOfMeasure().getId()).block());
                if (ingredientFound.getUnitOfMeasure() == null){
                    new RuntimeException("Unit Of Measure not found");
                }
            } else {
                Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                //ingredient.setRecipe(recipe);
                recipe.addIngredient(ingredient);
            }

            Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                    .findFirst();

            if (!savedIngredientOptional.isPresent()){
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId().equals(command.getUnitOfMeasure().getId()))
                        .findFirst();
            }

            IngredientCommand ingredientCommandSaved = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
            ingredientCommandSaved.setRecipeId(recipe.getId());
            return Mono.just(ingredientCommandSaved);
        }
    }

    @Override
    public Mono<Void> deleteByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

        log.debug("deleting ingredient " + ingredientId + " from recipe " + recipeId);

//        Recipe reactiveRecipe = recipeReactiveRepository.findById(recipeId).block();
        Recipe recipe = recipeRepository.findById(recipeId).get();


        if (recipe != null){

            log.debug("found recipe");

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();

            if (ingredientOptional.isPresent()){
                log.debug("ingredient found");

                recipe.getIngredients().remove(ingredientOptional.get());
                recipeRepository.save(recipe);
            }
        }
        else {
            log.debug("recipe not found id: " + recipeId);
        }
        return Mono.empty();
    }
}