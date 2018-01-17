package mmazurkiewicz.repositories;

import mmazurkiewicz.domain.UnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, String> {

    Optional<UnitOfMeasure> findByUnitOfMeasure(String unitOfMeasure);
}
