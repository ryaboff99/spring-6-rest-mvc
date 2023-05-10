package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
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
@DisplayName("Customer Controller Integration tests")
class CustomerControllerIT {
    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Test
    @DisplayName("List of Customers is returned")
    void listOfCustomersIsReturned() {
        List<CustomerDTO> testDtoList = customerController.listAllCustomers();

        assertThat(testDtoList.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    @DisplayName("Empty list of Customers is returned")
    void emptyListOfCustomersIsReturned() {
        customerRepository.deleteAll();
        List<CustomerDTO> testDtoList = customerController.listAllCustomers();

        assertThat(testDtoList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Customer is returned by id")
    void customerIsReturnedById() {
        UUID testId = customerRepository.findAll().get(0).getId();
        CustomerDTO testCustomer = customerController.getCustomerById(testId);

        assertThat(testCustomer.getId()).isEqualTo(testId);
    }

    @Test
    @DisplayName("Error 404 is thrown if Customer not found by id for returning")
    void error404IsThrownIfCustomerNotFoundByIdForReturning() {
        assertThrows(NotFoundException.class, () -> customerController.getCustomerById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    @DisplayName("New Customer is saved")
    void newCustomerIsSaved() {
        CustomerDTO customerDTO = CustomerDTO.builder().name("New Customer").build();

        ResponseEntity responseEntity = customerController.handlePost(customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Customer customer = customerRepository.findById(savedUUID).get();
        assertThat(customer).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    @DisplayName("Customer is updated by id")
    void customerIsUpdatedById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        final String customerName = "UPDATED";
        customerDTO.setName(customerName);

        ResponseEntity responseEntity = customerController.updateCustomerById(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getName()).isEqualTo(customerName);
    }

    @Test
    @DisplayName("Error 404 is thrown if Customer not found by id for updating")
    void error404IsThrownIfCustomerNotFoundByIdForUpdating() {
        assertThrows(NotFoundException.class,
                () -> customerController.updateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    @DisplayName("Customer is patched by id")
    void customerIsPatchedById() {
        Customer customer = customerRepository.findAll().get(0);
        final String customerName = "UPDATED";
        CustomerDTO customerDTO = CustomerDTO.builder().name(customerName).build();

        ResponseEntity responseEntity = customerController.updateCustomerPatchById(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getName()).isEqualTo(customerName);
    }

    @Test
    @DisplayName("Error 404 is thrown if Customer not found by id for patching")
    void error404IsThrownIfCustomerNotFoundByIdForPatching() {
        assertThrows(NotFoundException.class, () -> customerController.updateCustomerPatchById(UUID.randomUUID(),
                CustomerDTO.builder().build()));
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("Customer is deleted by id")
    void customerIsDeletedById() {
        UUID testId = customerRepository.findAll().get(0).getId();

        ResponseEntity responseEntity = customerController.deleteCustomerById(testId);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(customerRepository.findById(testId)).isEmpty();
        assertThat(customerRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("Error 404 is thrown if Customer not found by id for deletion")
    void error404IsThrownICustomerNotFoundByIdForDeletion() {
        assertThrows(NotFoundException.class,
                () -> customerController.deleteCustomerById(UUID.randomUUID()));
    }
}