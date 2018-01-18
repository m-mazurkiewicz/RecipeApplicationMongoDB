package mmazurkiewicz.repositories.reactive;

import mmazurkiewicz.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UnitOfMeasureReactiveTest {

    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    @Before
    public void setUp() throws Exception{
        unitOfMeasureReactiveRepository.deleteAll().block();
    }

    @Test
    public void testSaveUnitOfMeasure() throws Exception{
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setUnitOfMeasure("pot");

        unitOfMeasureReactiveRepository.save(unitOfMeasure).block();

        Long count = unitOfMeasureReactiveRepository.count().block();

        assertEquals(Long.valueOf(1L), count);
    }

    @Test
    public void findByUnitOfMeasureTest() throws Exception{
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setUnitOfMeasure("pot");

        unitOfMeasureReactiveRepository.save(unitOfMeasure).then().block();

        UnitOfMeasure foundUnitOfMeasure = unitOfMeasureReactiveRepository.findByUnitOfMeasure("pot").block();

        assertEquals("pot", foundUnitOfMeasure.getUnitOfMeasure());
    }
}
