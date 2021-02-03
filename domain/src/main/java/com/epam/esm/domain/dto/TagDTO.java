package com.epam.esm.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class TagDTO {

    private Long id;
    @NotEmpty
    @Size(min = 3, max = 30)
    private String name;

}