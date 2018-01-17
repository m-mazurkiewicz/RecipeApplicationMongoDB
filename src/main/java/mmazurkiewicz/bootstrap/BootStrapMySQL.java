package mmazurkiewicz.bootstrap;

import lombok.extern.slf4j.Slf4j;
import mmazurkiewicz.domain.Category;
import mmazurkiewicz.domain.UnitOfMeasure;
import mmazurkiewicz.repositories.CategoryRepository;
import mmazurkiewicz.repositories.UnitOfMeasureRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"dev","prod"})
public class BootStrapMySQL implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public BootStrapMySQL(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (categoryRepository.count() == 0L){
            log.debug("Loading categories");
            loadCategories();
        }

        if (unitOfMeasureRepository.count() == 0L){
            log.debug("Loading Units of Measure");
            loadUnitsUfMeasure();
        }
    }

    private void loadCategories(){
        Category category1 = new Category();
        category1.setDescription("American");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setDescription("Italian");
        categoryRepository.save(category2);

        Category category3 = new Category();
        category3.setDescription("Mexican");
        categoryRepository.save(category3);

        Category category4 = new Category();
        category4.setDescription("Fast Food");
        categoryRepository.save(category4);
    }

    private void loadUnitsUfMeasure(){
        UnitOfMeasure unitOfMeasure1 = new UnitOfMeasure();
        unitOfMeasure1.setUnitOfMeasure("Teaspoon");
        unitOfMeasureRepository.save(unitOfMeasure1);

        UnitOfMeasure unitOfMeasure2 = new UnitOfMeasure();
        unitOfMeasure2.setUnitOfMeasure("Tablespoon");
        unitOfMeasureRepository.save(unitOfMeasure2);

        UnitOfMeasure unitOfMeasure3 = new UnitOfMeasure();
        unitOfMeasure3.setUnitOfMeasure("Cup");
        unitOfMeasureRepository.save(unitOfMeasure3);

        UnitOfMeasure unitOfMeasure4 = new UnitOfMeasure();
        unitOfMeasure4.setUnitOfMeasure("Pinch");
        unitOfMeasureRepository.save(unitOfMeasure4);

        UnitOfMeasure unitOfMeasure5 = new UnitOfMeasure();
        unitOfMeasure5.setUnitOfMeasure("Ounce");
        unitOfMeasureRepository.save(unitOfMeasure5);

        UnitOfMeasure unitOfMeasure6 = new UnitOfMeasure();
        unitOfMeasure6.setUnitOfMeasure("Dash");
        unitOfMeasureRepository.save(unitOfMeasure6);

        UnitOfMeasure unitOfMeasure7 = new UnitOfMeasure();
        unitOfMeasure7.setUnitOfMeasure("Each");
        unitOfMeasureRepository.save(unitOfMeasure7);

        UnitOfMeasure unitOfMeasure8 = new UnitOfMeasure();
        unitOfMeasure8.setUnitOfMeasure("Clove");
        unitOfMeasureRepository.save(unitOfMeasure8);

        UnitOfMeasure unitOfMeasure9 = new UnitOfMeasure();
        unitOfMeasure9.setUnitOfMeasure("Pint");
        unitOfMeasureRepository.save(unitOfMeasure9);
    }
}
