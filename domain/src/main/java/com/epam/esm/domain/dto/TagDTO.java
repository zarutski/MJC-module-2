package com.epam.esm.domain.dto;

import com.epam.esm.domain.entity.Certificate;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TagDTO {

    private Long id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Certificate> certificates;

}
