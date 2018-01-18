package mmazurkiewicz.services;

import mmazurkiewicz.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

import java.util.Set;

public interface UnitOfMeasureService {

    Flux<UnitOfMeasureCommand> listAllUnitsOfMeasure();
}
