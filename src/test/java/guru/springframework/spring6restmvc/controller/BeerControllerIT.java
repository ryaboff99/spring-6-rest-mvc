package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Beer Controller Integration tests")
class BeerControllerIT {
    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Test
    @DisplayName("List of Beers is returned")
    void listOfBeersIsReturned() {
        List<BeerDTO> testDtoList = beerController.listBeers();

        assertThat(testDtoList.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    @DisplayName("Empty list of Beers is returned")
    void emptyListOfBeersIsReturned() {
        beerRepository.deleteAll();
        List<BeerDTO> testDtoList = beerController.listBeers();

        assertThat(testDtoList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Beer is returned by id")
    void beerIsReturnedById() {
        UUID testId = beerRepository.findAll().get(0).getId();
        BeerDTO testBeer = beerController.getBeerById(testId);

        assertThat(testBeer.getId()).isEqualTo(testId);
    }

    @Test
    @DisplayName("Error 404 is thrown if Beer not found by id for returning")
    void error404IsThrownIfBeerNotFoundByIdForReturning() {
        assertThrows(NotFoundException.class, () -> beerController.getBeerById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    @DisplayName("New Beer is saved")
    void newBeerIsSaved() {
        BeerDTO beerDTO = BeerDTO.builder().beerName("New Beer").build();

        ResponseEntity responseEntity = beerController.handlePost(beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Beer beer = beerRepository.findById(savedUUID).get();
        assertThat(beer).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    @DisplayName("Beer is updated by id")
    void beerIsUpdatedById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        final String beerName = "UPDATED";
        beerDTO.setBeerName(beerName);

        ResponseEntity responseEntity = beerController.updateById(beer.getId(), beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
    }

    @Test
    @DisplayName("Error 404 is thrown if Beer not found by id for updating")
    void error404IsThrownIfBeerNotFoundByIdForUpdating() {
        assertThrows(NotFoundException.class, () -> beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build()));
    }

    @Test
    @DisplayName("Beer is patched by id")
    void beerIsPatchedById() {
        Beer beer = beerRepository.findAll().get(0);
        final String beerName = "UPDATED";
        BeerDTO beerDTO = BeerDTO.builder().beerName(beerName).build();

        ResponseEntity responseEntity = beerController.updateBeerPatchById(beer.getId(), beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
        assertThat(updatedBeer.getBeerStyle()).isEqualTo(beer.getBeerStyle());
    }

    @Test
    @DisplayName("Error 404 is thrown if Beer not found by id for patching")
    void error404IsThrownIfBeerNotFoundByIdForPatching() {
        assertThrows(NotFoundException.class, () -> beerController.updateBeerPatchById(UUID.randomUUID(), BeerDTO.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    @DisplayName("Beer is deleted by id")
    void beerIsDeletedById() {
        UUID testId = beerRepository.findAll().get(0).getId();
        ResponseEntity responseEntity = beerController.deleteById(testId);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(beerRepository.findById(testId)).isEmpty();
        assertThat(beerRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("Error 404 is thrown if Beer not found by id for deletion")
    void error404IsThrownIfBeerNotFoundByIdForDeletion() {
        assertThrows(NotFoundException.class, () -> beerController.deleteById(UUID.randomUUID()));
    }
}