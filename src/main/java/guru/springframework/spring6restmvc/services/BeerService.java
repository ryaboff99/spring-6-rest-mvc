
package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {

    List<Beer> getAllBeers();

    Beer getBeerById(UUID beerId);

    Beer saveNewBeer(Beer beer);
}
