package com.xuno.message.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xuno.message.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    @JsonIgnore
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;
}
