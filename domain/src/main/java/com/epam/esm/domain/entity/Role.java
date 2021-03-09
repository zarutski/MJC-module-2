package com.epam.esm.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_roles")
@Data
@NoArgsConstructor
public class Role {

    @Id
    @Column(name = "id")
    private Long id;
    @Column(nullable = false)
    private String name;

}
