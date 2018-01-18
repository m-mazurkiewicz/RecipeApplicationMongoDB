package mmazurkiewicz.controllers;

import lombok.extern.slf4j.Slf4j;
import mmazurkiewicz.commands.IngredientCommand;
import mmazurkiewicz.commands.RecipeCommand;
import mmazurkiewicz.commands.UnitOfMeasureCommand;
import mmazurkiewicz.services.IngredientService;
import mmazurkiewicz.services.RecipesService;
import mmazurkiewicz.services.UnitOfMeasureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IngredientController {

    private final IngredientService ingredientService;
    private final RecipesService recipeService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(IngredientService ingredientService, RecipesService recipeService, UnitOfMeasureService unitOfMeasureService) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model){
        log.debug("Getting ingredient list for recipe id: " + recipeId);

        model.addAttribute("recipe", recipeService.findCommandById(recipeId));

        return "recipe/ingredient/list";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String showRecipeIngredient(@PathVariable String recipeId,
                                       @PathVariable String ingredientId, Model model){
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));
        return "recipe/ingredient/show";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId,
                                         @PathVariable String ingredientId, Model model){
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));

        model.addAttribute("unitOfMeasureList", unitOfMeasureService.listAllUnitsOfMeasure().collectList().block());
        return "recipe/ingredient/ingredientForm";
    }

    @GetMapping("recipe/{recipeId}/ingredient/new")
    public String newRecipe(@PathVariable String recipeId, Model model){
        RecipeCommand recipeCommand = recipeService.findCommandById(recipeId);

        IngredientCommand ingredientCommand = new IngredientCommand();
        //ingredientCommand.setRecipeId(recipeId);
        model.addAttribute("ingredient", ingredientCommand);

        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());

        model.addAttribute("unitOfMeasureList", unitOfMeasureService.listAllUnitsOfMeasure().collectList().block());

        return "recipe/ingredient/ingredientForm";
    }

    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command){
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

       // log.debug("saved recipe id:" + savedCommand.getRecipeId());
        log.debug("saved ingredient id:" + savedCommand.getId());

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteByIngredientId(@PathVariable String recipeId, @PathVariable String ingredientId){
        log.debug("deleting ingredient id: " + ingredientId);

        ingredientService.deleteByRecipeIdAndIngredientId(recipeId, ingredientId);

        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}
