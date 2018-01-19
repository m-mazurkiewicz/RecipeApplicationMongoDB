package mmazurkiewicz.services;

import mmazurkiewicz.commands.RecipeCommand;
import mmazurkiewicz.converters.RecipeCommandToRecipe;
import mmazurkiewicz.converters.RecipeToRecipeCommand;
import mmazurkiewicz.domain.Recipe;
import mmazurkiewicz.exceptions.NotFoundException;
import mmazurkiewicz.repositories.RecipeRepository;
import mmazurkiewicz.repositories.reactive.RecipeReactiveRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

//@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecipesServiceImplTest {

    RecipesServiceImpl recipesService;

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        recipesService = new RecipesServiceImpl(recipeReactiveRepository, recipeToRecipeCommand, recipeCommandToRecipe);
    }

    @Test
    public void getRecipeById() throws Exception{
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        Recipe recipeReturned = recipesService.findById("1").block();

        assertNotNull("Null recipe returned", recipeReturned);
        verify(recipeReactiveRepository, times(1)).findById(anyString());
        verify(recipeReactiveRepository, never()).findAll();
    }

    @Test
    public void getRecipesTest() throws Exception {
        Recipe recipe = new Recipe();
        HashSet recipesData = new HashSet();
        recipesData.add(recipe);

        when(recipesService.getRecipes()).thenReturn(Flux.just(recipe));

        List<Recipe> recipes = recipesService.getRecipes().collectList().block();

        assertEquals(recipes.size(),1);
        verify(recipeReactiveRepository, times(1)).findAll();
        verify(recipeReactiveRepository, never()).findById(anyString());
    }

    @Test
    public void getRecipeCommandByIdTest() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1");

        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand commandById = recipesService.findCommandById("1").block();

        assertNotNull("Null recipe returned", commandById);
        verify(recipeReactiveRepository, times(1)).findById(anyString());
        verify(recipeReactiveRepository, never()).findAll();
    }

    @Test
    public void testDeleteById() throws Exception{

        //given
        String idToDelete = "2";

        when(recipeReactiveRepository.deleteById(anyString())).thenReturn(Mono.empty());

        //when
        recipesService.deleteById(idToDelete);

        //then
        verify(recipeReactiveRepository, times(1)).deleteById(anyString());
    }
}