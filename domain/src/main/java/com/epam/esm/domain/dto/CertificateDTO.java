package com.epam.esm.domain.dto;

import com.epam.esm.domain.validation.CreateGroup;
import com.epam.esm.domain.validation.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Relation(itemRelation = "certificate", collectionRelation = "certificates")
@EqualsAndHashCode(callSuper = true)
public class CertificateDTO extends RepresentationModel<CertificateDTO> {

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private Long id;
    @NotEmpty(groups = CreateGroup.class)
    @Size(min = 3, max = 45, groups = {CreateGroup.class, UpdateGroup.class})
    private String name;
    @NotEmpty(groups = CreateGroup.class)
    @Size(min = 3, max = 200, groups = {CreateGroup.class, UpdateGroup.class})
    private String description;
    @NotNull(groups = CreateGroup.class)
    @Min(value = 0, groups = {CreateGroup.class, UpdateGroup.class})
    @Max(value = 3000, groups = {CreateGroup.class, UpdateGroup.class})
    private BigDecimal price;
    @NotNull(groups = CreateGroup.class)
    @Min(value = 15, groups = {CreateGroup.class, UpdateGroup.class})
    @Max(value = 730, groups = {CreateGroup.class, UpdateGroup.class})
    private Integer duration;
    @Null
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createDate;
    @Null
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime lastUpdateDate;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @NotEmpty(groups = CreateGroup.class)
    @Valid
    private List<TagDTO> tags;

}