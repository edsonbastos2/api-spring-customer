package com.geladaExpress.customerconnect.controller;

import com.geladaExpress.customerconnect.controller.dto.ApiResponse;
import com.geladaExpress.customerconnect.controller.dto.CreateCustomerDto;
import com.geladaExpress.customerconnect.controller.dto.PaginationResponse;
import com.geladaExpress.customerconnect.controller.dto.UpdateCustomerDto;
import com.geladaExpress.customerconnect.entity.CustomerEntity;
import com.geladaExpress.customerconnect.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = "customers")
public class Customercontroller {

    private final CustomerService service;

    public Customercontroller(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> createCustomer(@RequestBody CreateCustomerDto dto) {

        var costumer = service.createCustomer(dto);

        return ResponseEntity.created(URI.create("/customers/"+costumer.getId())).build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CustomerEntity>> listAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                               @RequestParam(name = "orderBy", defaultValue = "desc") String orderBy,
                                                               @RequestParam(name = "cpf", required = false) String cpf,
                                                               @RequestParam(name = "email", required = false) String email) {
        var pageResp = service.findAll(page, pageSize, orderBy, cpf, email);

        return ResponseEntity.ok(new ApiResponse<>(
                pageResp.getContent(),
                new PaginationResponse(pageResp.getNumber(), pageResp.getSize(), pageResp.getTotalElements(), pageResp.getTotalPages())
        ));
    }

    @GetMapping(path = "/{customerId}")
    public ResponseEntity<CustomerEntity> findById(@PathVariable("customerId") Long customerId) {

        var customer = service.findById(customerId);

        return customer.isPresent() ?
                ResponseEntity.ok(customer.get()) :
                ResponseEntity.notFound().build();

    }

    @PutMapping(path = "/{customerId}")
    public ResponseEntity<CustomerEntity> updateById(@PathVariable("customerId") Long customerId,
                                                     @RequestBody UpdateCustomerDto dto) {

        var customer = service.updateById(customerId, dto);

        return customer.isPresent() ?
                ResponseEntity.noContent().build():
                ResponseEntity.notFound().build();

    }

    @DeleteMapping(path = "/{customerId}")
    public ResponseEntity<CustomerEntity> deleteById(@PathVariable("customerId") Long customerId) {

        var deleted = service.deleteById(customerId);

        return deleted ?
                ResponseEntity.noContent().build():
                ResponseEntity.notFound().build();

    }
}
