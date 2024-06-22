package com.codewithsiva.fullstackbackend.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Simpson Alfred
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Collection<RegUsers> users = new HashSet<>();

    public Role(String name) {
        this.name = name;
    }

   
    public  String getName(){
        return name != null? name : "";
    }
}
