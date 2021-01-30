package com.epam.esm.domain.dto;

import com.epam.esm.domain.validation.CreateGroup;
import com.epam.esm.domain.validation.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class CertificateDTO {

    @Null(groups = {CreateGroup.class})
    @NotNull(groups = {UpdateGroup.class})
    private Long id;
    @NotEmpty(groups = {CreateGroup.class})
    @Size(min = 3, max = 45, groups = {CreateGroup.class, UpdateGroup.class})
    private String name;
    @NotEmpty(groups = {CreateGroup.class})
    @Size(min = 3, groups = {CreateGroup.class, UpdateGroup.class})
    private String description;
    @NotNull(groups = {CreateGroup.class})
    @Min(value = 0, groups = {CreateGroup.class, UpdateGroup.class})
    private BigDecimal price;
    @NotNull(groups = {CreateGroup.class})
    @Min(value = 15, groups = {CreateGroup.class, UpdateGroup.class})
    private Integer duration;
    @Null
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createDate;
    @Null
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime lastUpdateDate;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @NotEmpty(groups = {CreateGroup.class})
    private List<TagDTO> tags;

}
