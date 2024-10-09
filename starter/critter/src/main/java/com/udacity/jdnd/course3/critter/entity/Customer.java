package com.udacity.jdnd.course3.critter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Nationalized
    private String name;
    private String phoneNumber;
    private String notes;
    @OneToMany(targetEntity = Pet.class)
    private List<Pet> pets;


    public Customer(long id, String name, String phoneNumber, String notes) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.notes = notes;
    }

}
