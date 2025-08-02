package com.geladaExpress.customerconnect.service;

import ch.qos.logback.core.util.StringUtil;
import com.geladaExpress.customerconnect.controller.dto.CreateCustomerDto;
import com.geladaExpress.customerconnect.entity.CustomerEntity;
import com.geladaExpress.customerconnect.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static org.springframework.util.StringUtils.hasText;

@Service
public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public CustomerEntity createCustomer(CreateCustomerDto dto) {
        var entity = new CustomerEntity();

        entity.setFullName(dto.fullName());
        entity.setEmail(dto.email());
        entity.setCpf(dto.cpf());
        entity.setPhoneNumber(dto.phoneNumber());

        return repository.save(entity);
    }

    public Page<CustomerEntity> findAll(Integer page,
                        Integer pageSize,
                        String orderBy,
                        String cpf,
                        String email) {

        var pageRequest = getPageRequest(page, pageSize, orderBy);

        return findWithFilter(cpf, email, pageRequest);
    }

    private Page<CustomerEntity> findWithFilter(String cpf, String email, PageRequest pageRequest) {
        if(hasText(cpf) && hasText(email)) {
            return repository.findByCpfAndEmail(cpf, email, pageRequest);
        }

        if(hasText(cpf)) {
            return repository.findByCpf(cpf, pageRequest);
        }

        if(hasText(email)) {
            return repository.findByEmail(email, pageRequest);
        }

        return repository.findAll(pageRequest);
    }

    private PageRequest getPageRequest(Integer page, Integer pageSize, String orderBy) {
        var direction = Sort.Direction.DESC;
        if(orderBy.equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        }
        return PageRequest.of(page, pageSize, direction, "createdAt" );
    }
}
