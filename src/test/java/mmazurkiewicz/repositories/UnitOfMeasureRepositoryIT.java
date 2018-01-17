package mmazurkiewicz.repositories;

import mmazurkiewicz.bootstrap.DevBootstrap;
import mmazurkiewicz.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UnitOfMeasureRepositoryIT {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Before
    public void setUp() throws Exception {
        recipeRepository.deleteAll();
        categoryRepository.deleteAll();
        unitOfMeasureRepository.deleteAll();

        DevBootstrap devBootstrap = new DevBootstrap(categoryRepository, recipeRepository, unitOfMeasureRepository);

        devBootstrap.onApplicationEvent(null);
    }

    @Test
    public void findByUnitOfMeasure() throws Exception {
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByUnitOfMeasure("Teaspoon");

        assertEquals("Teaspoon", unitOfMeasureOptional.get().getUnitOfMeasure());
    }

    @Test
    public void findByUnitOfMeasureCup() throws Exception {
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByUnitOfMeasure("Cup");

        assertEquals("Cup", unitOfMeasureOptional.get().getUnitOfMeasure());
    }

}