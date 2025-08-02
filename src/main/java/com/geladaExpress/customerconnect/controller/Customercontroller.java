package com.geladaExpress.customerconnect.controller;

import com.geladaExpress.customerconnect.controller.dto.ApiResponse;
import com.geladaExpress.customerconnect.controller.dto.CreateCustomerDto;
import com.geladaExpress.customerconnect.controller.dto.PaginationResponse;
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
}
