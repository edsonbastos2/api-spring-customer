package com.geladaExpress.customerconnect.controller.dto;

import java.util.List;

public record ApiResponse<T>(List<T> content, PaginationResponse pagination) {
}
