package com.example.demo.utility;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class PaginationRequest {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final String DEFAULT_SORT_BY = "id";
    private static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

    // số trang hiện tại
    private int page;
    // số lượng phần tử trên mỗi trang
    private int size;
    // sắp xếp theo trường nào
    private String sortBy;
    // hướng sắp xếp
    private Sort.Direction sortDirection;

    public PaginationRequest(Integer page, Integer size, String sortBy, String sortDirection) {
        this.page = (page != null && page >= 0) ? page : DEFAULT_PAGE;
        this.size = (size != null && size > 0) ? size : DEFAULT_SIZE;
        // mặc định sắp xếp theo id
        this.sortBy = (sortBy != null && !sortBy.trim().isEmpty()) ? sortBy : DEFAULT_SORT_BY;
        this.sortDirection = parseDirection(sortDirection);
    }

    // chuyển đổi PaginationRequest thành Pageable
    // Pageable là một interface, được triển khai bởi các class cụ thể như PageRequest.
    // Nó bao gồm các thông tin chính: Page number (số trang), Page size (số lượng phần tử trên mỗi trang), Sort (thứ tự sắp xếp).
    public Pageable toPageable() {
        return PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
    }

    private Sort.Direction parseDirection(String direction) {
        if (direction == null || direction.trim().isEmpty()) {
            return DEFAULT_SORT_DIRECTION;
        }
        return Sort.Direction.fromOptionalString(direction.trim().toUpperCase())
                .orElse(DEFAULT_SORT_DIRECTION);
    }
}
