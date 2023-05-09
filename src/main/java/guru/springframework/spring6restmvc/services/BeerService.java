
package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<BeerDTO> getAllBeers();

    Optional<BeerDTO> getBeerById(UUID beerId);

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer);    // change signatures to return Optional<BeerDTO> for check in the Controller whether come element or Optional.empty() (and throw 404 if empty)

    Boolean deleteById(UUID beerId);    // change signatures to return Boolean for check in the Controller whether element was found in DB and deleted (and throw 404 if was not found & deleted)

    Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer);    // change signatures to return Optional<BeerDTO> for check in the Controller whether come element or Optional.empty() (and throw 404 if empty)
}
