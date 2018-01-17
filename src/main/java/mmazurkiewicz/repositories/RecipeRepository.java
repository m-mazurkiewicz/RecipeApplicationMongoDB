package mmazurkiewicz.repositories;

import mmazurkiewicz.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, String> {
}
