package com.epam.esm.domain.dto;

import com.epam.esm.domain.validation.CreateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Relation(itemRelation = "tag", collectionRelation = "tags")
@EqualsAndHashCode(callSuper = true)
public class TagDTO extends RepresentationModel<TagDTO> {

    @Null(groups = CreateGroup.class)
    private Long id;
    @NotEmpty(groups = CreateGroup.class)
    @Size(min = 3, max = 30, groups = CreateGroup.class)
    private String name;

}