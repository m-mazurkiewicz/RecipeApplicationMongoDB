package mmazurkiewicz.services;

import lombok.extern.slf4j.Slf4j;
import mmazurkiewicz.domain.Recipe;
import mmazurkiewicz.repositories.reactive.RecipeReactiveRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService{

    private final RecipeReactiveRepository recipeReactiveRepository;

    public ImageServiceImpl(RecipeReactiveRepository recipeReactiveRepository) {
        this.recipeReactiveRepository = recipeReactiveRepository;
    }

    @Override
    public Mono<Void> saveImageFile(String recipeId, MultipartFile file) {

        Mono<Recipe> recipeMono = recipeReactiveRepository.findById(recipeId)
                .map(recipe -> {
                    Byte[] byteObjects = new Byte[0];
                    try {
                        byteObjects = new Byte[file.getBytes().length];

                        int iterator = 0;

                        for (byte b : file.getBytes()) {
                            byteObjects[iterator++] = b;
                        }

                        recipe.setImage(byteObjects);

                        return recipe;
                    } catch (IOException exception) {
                        exception.printStackTrace();
                        throw new RuntimeException(exception);
                    }
                });
        recipeReactiveRepository.save(recipeMono.block()).block();
        return Mono.empty();
    }
}
