package com.epam.esm.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Relation(itemRelation = "order", collectionRelation = "orders")
@EqualsAndHashCode(callSuper = true)
public class OrderDTO extends RepresentationModel<OrderDTO> {

    private Long id;
    private BigDecimal cost;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime date;
    private Long userId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CertificateDTO> certificateList;
}