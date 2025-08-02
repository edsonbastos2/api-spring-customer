package com.geladaExpress.customerconnect.controller.dto;

public record PaginationResponse(Integer page, Integer pageSize, Long totalElements, Integer totalPage) {
}
