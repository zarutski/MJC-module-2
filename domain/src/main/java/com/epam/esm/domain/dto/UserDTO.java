package com.epam.esm.domain.dto;

import com.epam.esm.domain.validation.CreateGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@Relation(itemRelation = "user", collectionRelation = "users")
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends RepresentationModel<UserDTO> {

    @Null(groups = CreateGroup.class)
    private Long id;
    @NotEmpty(groups = CreateGroup.class)
    @Size(min = 2, max = 45, groups = {CreateGroup.class})
    private String firstName;
    @NotEmpty(groups = CreateGroup.class)
    @Size(min = 2, max = 45, groups = {CreateGroup.class})
    private String lastName;
    @Email(groups = CreateGroup.class)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(groups = CreateGroup.class)
    @Size(min = 6, max = 45, groups = {CreateGroup.class})
    private String login;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(groups = CreateGroup.class)
    @Size(min = 8, max = 45, groups = {CreateGroup.class})
    private String password;
    @JsonIgnore
    private List<OrderDTO> orderList;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnoreProperties(value = {"id"})
    private RoleDTO role;
}