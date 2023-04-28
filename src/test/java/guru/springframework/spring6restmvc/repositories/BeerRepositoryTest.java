package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Beer Repository Tests")
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    @DisplayName("Repository save Beer")
    void repositorySaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder().beerName("My Beer").build());
                assertThat(savedBeer).isNotNull();
                assertThat(savedBeer.getId()).isNotNull();
    }
}