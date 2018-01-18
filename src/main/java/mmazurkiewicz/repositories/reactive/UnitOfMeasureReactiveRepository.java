package mmazurkiewicz.repositories.reactive;

import mmazurkiewicz.domain.UnitOfMeasure;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UnitOfMeasureReactiveRepository extends ReactiveMongoRepository<UnitOfMeasure, String> {

    Mono<UnitOfMeasure> findByUnitOfMeasure(String unitOfMeasure);
}
