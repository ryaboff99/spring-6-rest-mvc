package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Customer;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@DisplayName("Customer Repository Tests")
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    @DisplayName("Repository save Customer with invalid value throws exception")
    void repositorySaveCustomerWithInvalidValueThrowsException() {
        assertThrows(ConstraintViolationException.class, () -> {
            customerRepository.save(Customer.builder().name("").build());

            customerRepository.flush();
        });
    }

    @Test
    @DisplayName("Repository save Customer")
    void repositorySaveCustomer() {
        Customer customer = customerRepository.save(Customer.builder().name("New Name").build());

        customerRepository.flush();

        assertThat(customer.getId()).isNotNull();
        assertThat(customer.getId()).isNotNull();
    }
}