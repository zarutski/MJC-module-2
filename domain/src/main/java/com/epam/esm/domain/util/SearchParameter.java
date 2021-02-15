package com.epam.esm.domain.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SearchParameter {

    private String name;
    private String description;
    private List<String> tagNames;
    private String sortBy;
    private String order;
}