package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@DisplayName("Beer Repository Tests")
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    @DisplayName("Repository save Beer with invalid value throws exception")
    void repositorySaveBeerWithInvalidValueThrowsException() {

        assertThrows(ConstraintViolationException.class, () -> {
            beerRepository.save(Beer.builder()
                    .beerName("My Beer 666666666666666666666666666666666666666666666666666666666666666666666666666666")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("123123")
                    .price(new BigDecimal("11.99"))
                    .build());

            beerRepository.flush();
        });
    }

    @Test
    @DisplayName("Repository save Beer")
    void repositorySaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("My Beer")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("123123")
                .price(new BigDecimal("11.99"))
                .build());

        beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }
}