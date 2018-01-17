package mmazurkiewicz.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mmazurkiewicz.domain.Category;
import mmazurkiewicz.domain.Difficulty;
import mmazurkiewicz.domain.Ingredient;
import mmazurkiewicz.domain.Notes;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand {
    private String id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String description;

    @Min(1)
    @Max(999)
    private Integer prepTime;

    @Min(0)
    @Max(999)
    private Integer cookTime;

    @Min(1)
    @Max(99)
    private Integer servings;
    private String source;

    @URL
    private String url;

    @NotBlank
    private String directions;
    private List<IngredientCommand> ingredients = new ArrayList<>();
    private Byte[] image;
    private Difficulty difficulty;
    private NotesCommand notes;
    private List<CategoryCommand> categories = new ArrayList<>();
}
