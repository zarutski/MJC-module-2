package com.epam.esm.web.util;

import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaginationProvider<T> {

    public PagedModel<T> addPagination(List<T> entities, int page, int size, long entitiesCount) {
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, entitiesCount);
        return PagedModel.of(entities, pageMetadata);
    }
}