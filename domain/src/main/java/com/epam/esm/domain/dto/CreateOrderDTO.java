package com.epam.esm.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateOrderDTO {

    @NotNull
    @Min(value = 1)
    private Long userId;
    @NotEmpty
    private List<Long> certificateIds;
}