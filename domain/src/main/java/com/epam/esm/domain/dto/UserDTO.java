package com.epam.esm.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Data
@NoArgsConstructor
@Relation(itemRelation = "user", collectionRelation = "users")
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends RepresentationModel<UserDTO> {

    private Long id;
    private String name;
    private String surname;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String login;
    @JsonIgnore
    private String password;
    @JsonIgnoreProperties(value = {"giftCertificateList"})
    private List<OrderDTO> orderList;
}