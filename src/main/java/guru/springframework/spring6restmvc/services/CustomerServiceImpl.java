package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        CustomerDTO customer1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("John")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDTO customer2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Sam")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDTO customer3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Fiona")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        this.customerMap = new HashMap<>();
        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);
    }

    @Override
    public Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer) {
        CustomerDTO existingCustomer = customerMap.get(customerId);

        if (StringUtils.hasText(customer.getName())) {
            existingCustomer.setName(customer.getName());
        }
        return Optional.of(existingCustomer);
    }

    @Override
    public Boolean deleteById(UUID customerId) {
        customerMap.remove(customerId);
        return true;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID customerId) {
        log.debug("Get Customer by Id - in service. Id: " + customerId.toString());

        return Optional.of(customerMap.get(customerId));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        CustomerDTO savedCustomer = CustomerDTO.builder()
                .name(customer.getName())
                .version(customer.getVersion())
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(savedCustomer.getId(), savedCustomer);

        return savedCustomer;
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer) {
        CustomerDTO existingCustomer = customerMap.get(customerId);
        existingCustomer.setName(customer.getName());
        existingCustomer.setVersion(customer.getVersion());
        return Optional.of(existingCustomer);
    }
}
